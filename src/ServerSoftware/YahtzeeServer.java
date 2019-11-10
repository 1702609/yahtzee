package ServerSoftware;

import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.util.List;

public class YahtzeeServer{

	protected static int playerID = 0;
    protected static List<PlayerHandler> clients = new ArrayList<>();
	protected final static int PORT = 9090;

    public static void main(String[] args) throws IOException
        {
    	ServerSocket listner = new ServerSocket(PORT);
		LimitPlayer lp = new LimitPlayer();
		lp.start();
		while(!lp.isGameReady())
			{
			System.out.println("Waiting for client connection...");
			Socket client = listner.accept();
			System.out.println("Connected to client");
			PlayerHandler playerThread = new PlayerHandler(client,playerID+1);
			clients.add(playerThread);
			clients.get(playerID).start();
			playerID++;
			}
		GameLauncher ge = new GameLauncher();
		ge.start();
		while(lp.isGameReady())
			{
			System.out.println("Rejection mode");
			Socket client = listner.accept();
			PrintWriter out = new PrintWriter(client.getOutputStream(),true);
			out.println("-1");
			}
        }
}

class LimitPlayer extends Thread
	{
		public static boolean acceptingPlayerFlag = true;

		@Override
		public void run()
			{
			didUserAllowServer();
			}

		private static boolean didUserAllowServer() {
			if (YahtzeeServer.clients.size() >= 2 && acceptingPlayerFlag) {
				System.out.println("Lets start the game now?");
				try {
					int x = 6; // wait 6 seconds at most
					BufferedReader serverIn = new BufferedReader(new InputStreamReader(System.in));
					long startTime = System.currentTimeMillis();
					while (true) {
						if (!((System.currentTimeMillis() - startTime) < x * 1000
								&& !serverIn.ready())) break;
					}
					if (serverIn.ready())
						{
						if (serverIn.readLine().equals("y"))
							{
							acceptingPlayerFlag = false;
							return true;
							}
						}
					else {
						System.out.println("You did not enter data");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		return false;
		}

		public boolean isGameReady()
			{
			run();
			 //This had to be done because if more players joined, the server still had to ask if they can start the game
			return !acceptingPlayerFlag;
			}

	}

class GameLauncher extends Thread
	{
	private int numberOfPlayers = YahtzeeServer.clients.size();
	private int currentlyPlaying = 0;
	private Object [] tempScore;

	@Override
	public void run()
		{
		notifyPlayersThatGameWillStart();
		gameSequence();
		}

	private void gameSequence()
		{
		while(true)
			{
			startGame();
			waitUntilPlayerIsFinished();
			retrieveScore();
			displayScoreToOthers();
			pickNextPlayer();
			}
		}

		private void pickNextPlayer()
		{
		if (currentlyPlaying+1 == numberOfPlayers) //when it reaches the end of the array
			{
			currentlyPlaying = 0;
			}
		else currentlyPlaying++;
		}

	private void waitUntilPlayerIsFinished ()
		{
		while(true)
			{
			try
				{
				Thread.sleep(2000);
				if (YahtzeeServer.clients.get(currentlyPlaying).isRoundComplete())
					{
					System.out.println("Hello");
					break;
					}
				}
			catch (Exception e)
				{
				e.printStackTrace();
				}
			}
		}

	private void retrieveScore()
		{
		tempScore =YahtzeeServer.clients.get(currentlyPlaying).getSelectedScore();
		}

	private void displayScoreToOthers()
		{
		for (int i = 0; i < numberOfPlayers; i++)
			{
			if (currentlyPlaying != i)
				{
				int tempId = currentlyPlaying+1;
				String msg = "Player "+tempId+" has choosen "+ tempScore[0]+" with a score of "+tempScore[1];
				YahtzeeServer.clients.get(i).sendMessage(msg);
				}
			}
		}

	private void notifyPlayersThatGameWillStart()
		{
		for (int i = 0; i < numberOfPlayers; i++)
			{
			YahtzeeServer.clients.get(i).sendMessage("Get Ready!!!");
			}
		}

	public void startGame()
		{
		for (int i = 0; i < numberOfPlayers; i++)
			{
			if (currentlyPlaying == i)
				{
				YahtzeeServer.clients.get(i).sendMessage("begin");
				}
			else if (currentlyPlaying != i)
				{
				int tempId = currentlyPlaying+1;
				YahtzeeServer.clients.get(i).sendMessage("Player "+ tempId +" is playing now.");
				}
			}
		}
	}
