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
	private SharedScoreBoard scoreBoard;
	private UploadScoreBoard up;

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
			out.writeObject("Your ID is "+id); //initial message
			out.writeObject(tellPlayersToWait());
			}
		catch (IOException e)
			{
			e.printStackTrace();
			}
    	}
	public void notifyGameStartsNow()
		{
			try {
				out.writeObject("Get Ready!!!");
				up = new UploadScoreBoard(out,scoreBoard);
				up.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
class UploadScoreBoard extends Thread
	{
		ObjectOutputStream out;
		SharedScoreBoard scoreBoard;

		UploadScoreBoard(ObjectOutputStream out, SharedScoreBoard scoreBoard)
			{
			this.scoreBoard = scoreBoard;
			this.out = out;
			}
		Object[] temp;
		@Override
		public void run()
			{
			while (true)
				{
				try {
					scoreBoard.acquireLock();
					temp = scoreBoard.getScoreBoard();
					out.writeObject(temp);
					scoreBoard.releaseLock();
				} catch (InterruptedException | IOException e) {
					System.err.println("Failed to get lock when reading:" + e);
				}
				}
			}
	}
