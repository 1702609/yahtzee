package yathzee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import hackerRank.Server;

public class PlayerHandler extends Thread {
    protected static ArrayList<Socket> clients =  new ArrayList<>();;
    private static ArrayList<BufferedReader> in = new ArrayList<>();// take in message
	private static ArrayList<PrintWriter> out = new ArrayList<>();//writing to someone    
	private static int numberOfPlayers = 0;


    public PlayerHandler(Socket player) throws IOException
    	{
		numberOfPlayers++;
        clients.add(player);
        in.add(new BufferedReader(new InputStreamReader(clients.get(numberOfPlayers-1).getInputStream())));
		out.add(new PrintWriter(clients.get(numberOfPlayers-1).getOutputStream(),true));
    	}
    
    @Override
    public void run() 
    	{
		System.out.println("Number of players is "+numberOfPlayers);
		out.get(numberOfPlayers-1).println("Your ID is "+numberOfPlayers);
		while (YahtzeeServer.clients.size() <2)
		{
		for (int i = 0; i < clients.size(); i++)
			{
			String request = in.get(i).readLine();
			if (request.contains("name"))
				{
				out.get(i).println(Server.getRandomName());
				}
			else
				{
				out.get(i).println("Type 'name' to get a random name");
				}
			}
		}
    	}
    }

