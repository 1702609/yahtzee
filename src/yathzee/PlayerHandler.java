package yathzee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


public class PlayerHandler extends Thread {
    public static ArrayList<Socket> clients =  new ArrayList<>();;
    public static ArrayList<BufferedReader> in = new ArrayList<>();// take in message
    public static ArrayList<PrintWriter> out = new ArrayList<>();//writing to someone    
    public static int numberOfPlayers = 0;


    public PlayerHandler(Socket player) throws IOException
    	{
		numberOfPlayers++;
        clients.add(player);
        in.add(new BufferedReader(new InputStreamReader(clients.get(numberOfPlayers-1).getInputStream())));
        out.add(new PrintWriter((clients.get(numberOfPlayers-1).getOutputStream()),true));
    	}
    
    @Override
    public void run() 
    	{
		System.out.println("Number of players is "+numberOfPlayers);
		try {
			out.get(numberOfPlayers-1).println("Your ID is "+numberOfPlayers);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean havePlayersBeenToldToWait = false;
		while (clients.size() <2)
			{
			if (havePlayersBeenToldToWait) continue;
			else
				{
				tellPlayersToWait();
				havePlayersBeenToldToWait = true;
				}
			}
		notifyPlayersThatGameWillStart();
    	startGame();
    	}

	private void startGame() 
		{
		try 
			{
			out.get(1).println("Player 1 is playing.");
			out.get(0).println("begin");				
			} 
		catch (Exception e) 
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}	
		}

	private void notifyPlayersThatGameWillStart() 
		{
		for (int i = 0; i < out.size(); i++)
			{
			try 
				{
				out.get(i).println("Get ready!!!");
				} 
			catch (Exception e)
				{
				e.printStackTrace();
				}
			}
		}

	private void tellPlayersToWait() 
		{
		for (int i = 0; i < out.size(); i++)
			{
			try 
				{
				out.get(i).println("Waiting for players to join.");
				out.get(i).println("Currently, "+YahtzeeServer.clients.size()+" have joined!");
				}
			catch (Exception e) 
				{
				e.printStackTrace();
				}
			}
		}
    }

class getPlayerMessage extends Thread 
	{
	
	@Override
    public void run() 
		{
		while (true)
			{
			for (int i = 0; i<PlayerHandler.clients.size(); i++)
				{
				try 
					{
					if(PlayerHandler.in.get(i).ready())
						{
						System.out.println("Message from client");
						}
					} 
				catch (IOException e) 
					{
					e.printStackTrace();
					}
				}
			}
		}
	}


