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
    public static int numberOfPlayers, bottomUp = 0;


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

	public static void startGame() 
		{
		pickNextPlayer();
		for (int i = 0; i < numberOfPlayers; i++)
			{
			if (i == bottomUp-1)
				{		
				out.get(i).println("begin");				
				new getPlayerMessage().start();
				}
			else
				{
				out.get(i).println("Player "+ bottomUp +" is playing.");
				}
			}
		}

	private static void pickNextPlayer() 
		{
		if (bottomUp != numberOfPlayers) bottomUp++;
		else 
			{
			bottomUp = 1;
			++YahtzeeServer.numberOfRound;
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
				out.get(i).println("Currently, "+numberOfPlayers+" have joined!");
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
		String clientMsg;
		outerloop:
		while (true)
			{
			for (int i = 0; i<PlayerHandler.numberOfPlayers; i++)
				{
				try 
					{
					if(isPlayerFinished(i))
						{
						System.out.println("Message from client");
						clientMsg = PlayerHandler.in.get(i).readLine();
						System.out.println(clientMsg);
						int score = Integer.parseInt(clientMsg.replaceAll("\\D+",""));
						break outerloop;
						}
					} 
				catch (IOException e) 
					{
					e.printStackTrace();
					}
				}
			}
		PlayerHandler.startGame();
		}

	private boolean isPlayerFinished(int activePlayer) throws IOException 
		{
		if(PlayerHandler.in.get(activePlayer).ready()) return true;
		return false;
		}
	}


