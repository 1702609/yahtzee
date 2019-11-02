package yathzee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class PlayerHandler extends Thread {
    public static ArrayList<Socket> clients =  new ArrayList<>();;
    public static ArrayList<BufferedReader> in = new ArrayList<>();// take in message
    public static ArrayList<PrintWriter> out = new ArrayList<>();//writing to someone    
    public static int numberOfPlayers, index = 0;
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
			out.get(numberOfPlayers-1).println(tellPlayersToWait());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tellPlayersToWait();
		while (!acceptingPlayer())
			{
			notifyPlayersThatGameWillStart();
			startGame();
			}
    	}

	public static void startGame() 
		{
		pickNextPlayer();
		for (int i = 0; i < out.size(); i++)
			{
			if (i == index-1)
				{		
				out.get(i).println("begin");				
				new getPlayerMessage().start();
				}
			else
				{
				out.get(i).println("Player "+ index +" is playing.");
				}
			}
		}

	private static void pickNextPlayer() 
		{
		if (index != numberOfPlayers) index++;
		else 
			{
			index = 0;
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

	private String tellPlayersToWait()
		{
		String msg;
		msg = "Waiting for players to join.\nCurrently, "+numberOfPlayers+" have joined!";
		return msg;
		}

	private static boolean acceptingPlayer()
	{
		if (gameStarted()) return false;
		else return true;
	}

	private static boolean gameStarted()
		{
		if (clients.size() >=2 && acceptingPlayerFlag)
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


