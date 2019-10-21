package yathzee;

import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class YahtzeeServer{

	protected static int currentID = 0;
    protected static List<PlayerHandler> clients = new ArrayList<>();
	protected static int PORT = 9090;

	private static ExecutorService pool = Executors.newFixedThreadPool(4);

	
    public static void main(String[] args) throws IOException
        {
    	ServerSocket listner = new ServerSocket(PORT);
		while(true) 
			{
			System.out.println("[SERVER] Waiting for client connection...");
			Socket client = listner.accept();
			System.out.println("[SERVER] Connected to client");
			PlayerHandler playerThread = new PlayerHandler(client);
			clients.add(playerThread);
			pool.execute(playerThread);
			}
        }

    static synchronized public int createID()
    	{
        return ++currentID;
    	} //if two player join at the same time, they have to take turns


    private void pickNextPlayer() {
    }

    private boolean hasPlayerFinished()
        {
        return false;
        }
}

