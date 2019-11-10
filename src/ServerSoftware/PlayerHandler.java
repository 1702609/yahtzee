package ServerSoftware;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class PlayerHandler extends Thread {
    private Socket clients;
	private ObjectOutputStream out;//writing to someone
	private ObjectInputStream in;// take in message
	private int id;
	private Object[] selectedScore;

	public PlayerHandler(Socket player, int id) throws IOException
    	{
		clients = player;
		this.id = id;
		out = new ObjectOutputStream(clients.getOutputStream());
		in = new ObjectInputStream(clients.getInputStream());
		}
    
    @Override
    public void run() 
    	{
		try
			{
			out.writeObject((String)"Your ID is "+id);
			out.writeObject(tellPlayersToWait());
			}
		catch (IOException e)
			{
			e.printStackTrace();
			}
    	}

	public void sendMessage(String msg)
		{
		try
			{
			out.writeObject(msg);
			}
		catch (IOException e)
			{
			e.printStackTrace();
			}
		}

	public boolean isRoundComplete() throws IOException
		{
		try
			{
			Object[] data = selectedScore = (Object[]) in.readObject();
				if (data != null)
				{
				return true;
				}
			else return false;
			}
		catch (ClassNotFoundException e)
			{
			e.printStackTrace();
			return false;
			}
		}

	public Object[] getSelectedScore()
		{
		return selectedScore;
		}

	private String tellPlayersToWait()
		{
		String msg;
		msg = "Waiting for players to join.";
		return msg;
		}
	}