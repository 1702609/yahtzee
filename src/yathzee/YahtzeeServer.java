package yathzee;

import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class YahtzeeServer{

	protected static int currentID = 0;
    protected static List<PlayerHandler> clients = new ArrayList<>();
	protected static int PORT = 9090;

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
			playerThread.start();
			}
        }


}


