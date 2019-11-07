package yathzee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class PlayerHandler extends Thread {
    private Socket clients;
	private BufferedReader in;// take in message
	private PrintWriter out;//writing to someone
	private int id;

    public PlayerHandler(Socket player, int id) throws IOException
    	{
		clients = player;
		this.id = id;
		in = new BufferedReader(new InputStreamReader(clients.getInputStream()));
		out = new PrintWriter(clients.getOutputStream(), true);
		}
    
    @Override
    public void run() 
    	{
		out.println("Your ID is "+id);
		out.println(tellPlayersToWait());
    	}

	public void sendMessage(String msg)
		{
		out.println(msg);
		}

	public boolean isRoundComplete() throws IOException
		{
		if (in.ready())
			{
			if (in.readLine().equals("finished")) return true;
			else return false;
			}
		else return false;
		}

	private String tellPlayersToWait()
		{
		String msg;
		msg = "Waiting for players to join.";
		return msg;
		}
	}