package yathzee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class PlayerHandler extends Thread {
    public static ArrayList<Socket> clients =  new ArrayList<>();;
    public static ArrayList<BufferedReader> in = new ArrayList<>();// take in message
    public static ArrayList<PrintWriter> out = new ArrayList<>();//writing to someone    
    public static int numberOfPlayers, bottomUp = 0;
	public static boolean acceptingPlayerFlag = true;

    public PlayerHandler(Socket player) throws IOException
    	{
		if (!acceptingPlayerFlag)
			{
			PrintWriter out = new PrintWriter(player.getOutputStream(),true);
			out.println("-1");
			}
		else
			{
			numberOfPlayers++;
			clients.add(player);
			in.add(new BufferedReader(new InputStreamReader(clients.get(numberOfPlayers - 1).getInputStream())));
			out.add(new PrintWriter((clients.get(numberOfPlayers - 1).getOutputStream()), true));
			}
		}
    
    @Override
    public void run() 
    	{
		try {
			out.get(numberOfPlayers-1).println("Your ID is "+numberOfPlayers);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean havePlayersBeenToldToWait = false;
		while (acceptingPlayer())
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

	private static boolean acceptingPlayer()
	{
		if (gameStarted()) return false;
		else return true;
	}

	private static boolean gameStarted()
		{
		if (clients.size() >=2)
			{
			System.out.println("Lets start the game now?");
			Scanner input = new Scanner(System.in);
			String response=input.nextLine();
			if (response.equals("y"))
				{
				acceptingPlayerFlag = false;
				return true;
				}
			else return false;
			}
		else return false;
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


