package yathzee;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class YahtzeeServer {

	ArrayList<Integer> playerID = new ArrayList<Integer>();
	private static long idCounter = 0;
	
    private static ServerSocket serverSocket;

    private Socket socket = null;
    private ObjectInputStream input;
    private static ObjectOutputStream output;

    public static void main(String[] args) throws IOException {
        InetAddress computerAddr = InetAddress.getLocalHost();;
        serverSocket = new ServerSocket(4321);
        System.out.println("The address of this computer is... " + computerAddr.getHostName());
        boolean listening = true;
        System.out.println("Yahtzee Server up and waiting");
        System.out.println("connected"); //client detected the server

        while(listening)
        {
        	new YahtzeeThread(serverSocket.accept()).start();
        	output.sendObject((String)"You are player "+createID());
            System.out.println("New server thread started");
        }

        serverSocket.close();
    }

    public static synchronized String createID()
    	{
        return String.valueOf(idCounter++);
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
                System.out.println("Thread says " +messageFromClient);
                if (messageFromClient.equals("Player wants to join")) {
                    output.writeObject((String) "You are player number")
                	output.flush();
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