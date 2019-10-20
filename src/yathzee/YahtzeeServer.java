package yathzee;

import java.net.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Collections;
import java.util.List;

public class YahtzeeServer extends Thread {

	protected static int currentID = 0;
    protected List<PlayerHandler> clients = new ArrayList<PlayerHandler>();

    private ServerSocket serverSocket;

    public YahtzeeServer(int port)
        {
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("New server initialized!");
            clients = Collections
                    .synchronizedList(new ArrayList<PlayerHandler>());
            this.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        }

    public static void main(String[] args)
        {
        int portNumber = 4545;
        new YahtzeeServer(portNumber);
        }

    static synchronized public int createID()
    	{
        return ++currentID;
    	} //if two player join at the same time, they have to take turns


    private int order = 0;
    private Socket socket = null;
    private String myActionServerThreadName;
    private BufferedReader stdIn;
    protected String userInput;
    static PrintWriter sendMessage; //writing to someone


    public void run() {
        try {
            while (true) {
                String messageFromClient = userInput;
                if (messageFromClient.equals("Player wants to join")) {
                    msgToClient = Integer.valueOf(myActionServerThreadName.replaceAll("\\D+", ""));
                    PlayerHandler newClient = new PlayerHandler(socket);
                    clients.add(newClient);
                } else if (messageFromClient.equals("Bye.")) {
                    break;
                }
                else if (clients.size() >= 2) {
                    startGame();
                        if (userInput != null & userInput.length() > 0) {
                            for (PlayerHandler client : YahtzeeServer.clients) {
                                client.out.println(userInput);
                                client.out.flush();
                                Thread.currentThread();
                            }
                    }
                    {
                        if (hasPlayerFinished()) {
                            pickNextPlayer();
                        }
                    }
                }
            else continue;
            socket.close();
        }
    }catch (Exception e)
    {
        e.printStackTrace();
    }
}

    private void startGame() throws IOException {
        PlayerHandler chosen = clients.get(order);
        chosen.out.println("player start"+chosen);
        System.out.println("Player "+chosen+" will start now");
    }

    private void pickNextPlayer() {
    }

    private boolean hasPlayerFinished()
        {
        return false;
        }
}

class communicateWithPlayers extends Thread {
    protected List<PlayerHandler> clients;
    protected String userInput;
    protected PrintWriter sendMessage; //writing to someone
    protected BufferedReader getMessage; // take in message

    public communicateWithPlayers(List<PlayerHandler> clients) {
        this.clients = clients;
        this.userInput = null;
        this.start();
    }

    public void run() {
        System.out.println("New Communication Thread Started");

        try {
            getMessage = new BufferedReader(new InputStreamReader(clients.get(0).client.getInputStream()));
            if (getMessage.readLine().equals("Player wants to join")) {
                clients.get(0).out.println(YahtzeeServer.createID());
                
            }
        }catch (Exception e){}
        try {
            if (clients.size() > 0) {
                this.console = new BufferedReader(new InputStreamReader(
                        System.in));
                while ((this.userInput = console.readLine()) != null) {
                    if (userInput != null & userInput.length() > 0) {
                        for (ClientHandler client : clients) {
                            client.out.println(userInput);
                            client.out.flush();
                            Thread.currentThread();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
