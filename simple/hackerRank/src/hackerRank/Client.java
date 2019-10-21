package hackerRank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client
	{
	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 9090;

	
	public static void main(String[] args) throws IOException, InterruptedException
		{
		Socket socket = new Socket(SERVER_IP,SERVER_PORT);
		
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(socket.getOutputStream(),true);


		while(true)
			{
			String serverResponse;
			Thread.sleep(1000);
			if (!input.ready())
				{
				continue;
				}
			else
				{
				serverResponse = input.readLine();
				System.out.println("Server says in the Else Statement: " + serverResponse);

				}
			}
		}
	}