package yathzee;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class YahtzeeServer {

	protected static ArrayList<Integer> listOfPlayers = new ArrayList<Integer>();
	protected static int idCounter = 0;
	
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        InetAddress computerAddr = InetAddress.getLocalHost();;
        serverSocket = new ServerSocket(4545);
        System.out.println("The address of this computer is... " + computerAddr.getHostName());
        boolean listening = true;
        System.out.println("Yahtzee Server up and waiting");

        while(listening)
        {
        	new YahtzeeThread(serverSocket.accept()).start();
            System.out.println("New server thread started");
        }

        serverSocket.close();
    }

    public static int createID()
    	{
        return idCounter++;
    	}
}

class YahtzeeThread extends Thread {

    private Socket socket = null;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public YahtzeeThread(Socket socket)
    {
        super("YahtzeeThread");
        this.socket = socket;
    }

    public void run() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream()); //stuff written by server
            input = new ObjectInputStream(socket.getInputStream()); // stuff written by client

            while (true) {
                String messageFromClient = (String) input.readObject();
                if (messageFromClient.equals("Player wants to join")) {
                    output.writeObject((String) "You are player number "+ YahtzeeServer.createID());
                    YahtzeeServer.listOfPlayers.add(YahtzeeServer.idCounter);
                    System.out.println("There are "+YahtzeeServer.listOfPlayers.size()+" player connected");
                } else if (messageFromClient.equals("Bye.")) {
                    output.writeObject((String) "Bye.");
                    break;
                }
            }
            socket.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}