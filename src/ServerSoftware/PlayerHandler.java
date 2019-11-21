package ServerSoftware;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class PlayerHandler extends Thread {
    private Socket clients;
	public static ObjectOutputStream out;//writing to someone
	public static ObjectInputStream in;// take in message
    public static SharedScoreBoard scoreBoard;
    private int id;
	private Object[] selectedScore;

	public PlayerHandler(Socket player, int id, SharedScoreBoard scoreBoard) throws IOException
    	{
		clients = player;
		this.id = id;
		out = new ObjectOutputStream(clients.getOutputStream());
		in = new ObjectInputStream(clients.getInputStream());
		this.scoreBoard = scoreBoard;
    	}
    
    @Override
    public void run() 
    	{
		try
			{
			out.writeObject((String)"Your ID is "+id); //initial message
			out.writeObject(tellPlayersToWait());
			}
		catch (IOException e)
			{
			e.printStackTrace();
			}

		ScoreBoardUploader up = new ScoreBoardUploader();
		up.run();
    	}

	public void sendMessage(String msg)
		{
		try
			{
			out.writeObject(msg);
			waitFunction();
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

	private static void waitFunction()
	{
		try
		{
		Thread.sleep(750);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
class ScoreBoardUploader extends Thread
    {
        @Override
        public void run()
            {
			try
				{
				PlayerHandler.out.writeObject(PlayerHandler.scoreBoard.getCurrentScoreBoard());
				}
			catch (IOException e)
				{
				e.printStackTrace();
				} catch (InterruptedException e)
				{
				e.printStackTrace();
				}
			}
    }

