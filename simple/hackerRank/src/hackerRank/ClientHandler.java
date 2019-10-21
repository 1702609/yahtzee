package hackerRank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
	private static ArrayList<Socket> client = new ArrayList<>();
	private static ArrayList<BufferedReader> in = new ArrayList<>();
	private static ArrayList<PrintWriter> out = new ArrayList<>();
	private static int numberOfPlayers = 0;
	
	public ClientHandler(Socket clientSocket) throws IOException
		{
		numberOfPlayers++;
		this.client.add(clientSocket);
		in.add(new BufferedReader(new InputStreamReader(client.get(numberOfPlayers-1).getInputStream())));
		out.add(new PrintWriter(client.get(numberOfPlayers-1).getOutputStream(),true));
		}
	
	@Override
	public void run() 
		{
		try
			{
			System.out.println("Number of players is "+numberOfPlayers);
			out.get(numberOfPlayers-1).println("Your ID is "+numberOfPlayers);
			while (Server.clients.size() <2)
				{
				for (int i = 0; i < client.size(); i++)
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
				
				Thread.sleep(2000);
				out.get(0).println("You are player 1 and ur first to join");
				
				out.get(1).println("You are player 2 and ur special");
				
			}
		catch (IOException | InterruptedException e)
			{
			System.err.println("Bye");
			}
		finally
			{
			out.get(0).close();
			try 
				{
				in.get(0).close();
				}
			catch (IOException e)
				{
				e.printStackTrace();
				}
			}
		
		}

}
