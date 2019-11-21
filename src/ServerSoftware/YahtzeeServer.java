package ServerSoftware;

import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.util.List;

public class YahtzeeServer{

	protected static SharedScoreBoard scoreBoard = new SharedScoreBoard();
	protected static int playerID = 0;
    protected static List<PlayerHandler> clients = new ArrayList<>();
	protected final static int PORT = 9090;
	
    public static void main(String[] args) throws IOException
        {
    	ServerSocket listner = new ServerSocket(PORT);
		
		while(!canTheServerStart())
			{
			System.out.println("Waiting for client connection...");
			Socket client = listner.accept();
			System.out.println("Connected to client");
			PlayerHandler playerThread = new PlayerHandler(client,playerID+1,scoreBoard);
			clients.add(playerThread);
			clients.get(playerID).run();
			playerID++;
			}
		scoreBoard.setScoreBoardSize(clients.size());
		GameLauncher ge = new GameLauncher();
		ge.start();
		while(true)
			{
			System.out.println("Rejection mode");
			Socket client = listner.accept();
			ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
			out.writeObject("-1");
			}
        }
    
    private static boolean canTheServerStart() {
		if (YahtzeeServer.clients.size() >= 3) {
			System.out.println("Lets start the game now?");
			try {
				BufferedReader serverIn = new BufferedReader(new InputStreamReader(System.in));
					
					if (serverIn.readLine().equals("y"))
						{
						return true;
						}
					
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	return false;
	}
    
}

class GameLauncher extends Thread
	{
	private int numberOfPlayers = YahtzeeServer.clients.size();
	private int currentlyPlaying = 0;
	private Object [] tempScore;
	private int[] everyoneScore = new int[numberOfPlayers];

	@Override
	public void run()
		{
		notifyPlayersThatGameWillStart();
		gameSequence();
		}

	private void gameSequence()
		{
		for(int i = 0; i < 13*numberOfPlayers; i++)
			{
			startGame();
			waitUntilPlayerIsFinished();
			retrieveScore();
			updateSharedScoreBoard();
			pickNextPlayer();
			}
		announceWinner();
	    System.exit(0);
		}

		private int findHighScore()
			{
			int max = everyoneScore[0];
			int index = 0;

			for (int i = 0; i < everyoneScore.length; i++)
				{
				if (max < everyoneScore[i])
					{
					max = everyoneScore[i];
					index = i;
					}
				}
			return index;
			}

		private void announceWinner()
			{
			int bestPlayer = findHighScore();
			for (int i = 0; i < numberOfPlayers; i++)
				{
                if (i != bestPlayer)
                    {
                    int tempId = bestPlayer + 1;
                    String msg = "The winner is player " + tempId + " scoring with " + everyoneScore[bestPlayer];
                    YahtzeeServer.clients.get(i).sendMessage(msg);
                    }
				else
                    {
                    String msg = "Well done! You are the winner!";
                    YahtzeeServer.clients.get(i).sendMessage(msg);
                    }
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
		everyoneScore[currentlyPlaying] = (int) tempScore[2];
		}

	private void updateSharedScoreBoard()
		{
		for (int i = 0; i < numberOfPlayers; i++)
			{
			if (currentlyPlaying != i)
				{
				int tempId = currentlyPlaying+1;
				String msg = "Player "+tempId+" has choosen "+ tempScore[0]+" with a score of "+tempScore[1];
				YahtzeeServer.clients.get(i).sendMessage(msg);
				YahtzeeServer.scoreBoard.setScoreBoard(tempScore);
				}
			}
		}

	private void notifyPlayersThatGameWillStart()
		{
		for (int i = 0; i < numberOfPlayers; i++)
			{
			YahtzeeServer.clients.get(i).notifyGameStartsNow();
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
