package ServerSoftware;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class PlayerHandler extends Thread {
    private Socket clients;
	private ObjectInputStream in;// take in message
	private int id;
	private Object[] selectedScore;
	private SharedScoreBoard scoreBoard;
	private UploadScoreBoard up;
	private SendMessage message;
	
	public PlayerHandler(Socket player, int id, SharedScoreBoard scoreBoard) throws IOException
    	{
		clients = player;
		this.id = id;
		in = new ObjectInputStream(clients.getInputStream());
		this.scoreBoard = scoreBoard;
		message = new SendMessage(clients);
    	}
    
    @Override
    public void run() 
    	{
		message.sendMessage("Your ID is "+id);//initial message
		message.sendMessage(tellPlayersToWait());
    	}
	public void notifyGameStartsNow()
		{
		message.sendMessage("Get Ready!!!");
		up = new UploadScoreBoard(message,scoreBoard);
		up.start();
			
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

	public void sendMessage(Object msg)
	{		
	message.sendMessage(msg);
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
class UploadScoreBoard extends Thread
	{
		SendMessage message;
		SharedScoreBoard scoreBoard;

		UploadScoreBoard(SendMessage message, SharedScoreBoard scoreBoard)
			{
			this.scoreBoard = scoreBoard;
			this.message = message;
			}
		int[] temp;
		@Override
		public void run()
			{
			while (true)
				{
				try {
					scoreBoard.acquireLock();
					message.sendMessage(scoreBoard.getScoreBoard());
					scoreBoard.releaseLock();
					Thread.sleep(800);
				} catch (Exception e) {
					System.err.println("Failed to get lock when reading:" + e);
				}
				}
			}
	}
class SendMessage 
	{
	private ObjectOutputStream out;//writing to someone
	public SendMessage(Socket s)
		{
		try {
			out = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}	
	public synchronized void sendMessage(Object msg)
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
	
	
	}
