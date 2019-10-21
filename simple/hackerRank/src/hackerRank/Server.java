package hackerRank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
	{
	protected static String[] names = {"Tommy","Bob","Robin","Batman"};
	protected static int PORT = 9090;

	public static ArrayList<ClientHandler> clients = new ArrayList<>();
	private static ExecutorService pool = Executors.newFixedThreadPool(4);

	public static void main(String args[]) throws IOException
		{
		ServerSocket listner = new ServerSocket(PORT);
		while(true) 
			{
			System.out.println("[SERVER] Waiting for client connection...");
			Socket client = listner.accept();
			System.out.println("[SERVER] Connected to client");
			ClientHandler clientThread = new ClientHandler(client);
			clients.add(clientThread);
			pool.execute(clientThread);
			}
		}

	static String getRandomName() 
		{
		String name = names[(int)(Math.random()*names.length)];
		return name;
		}


	}