package serverspiketest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSpikeTest {


    public static void main(String[] args) {
        
        int portNum = 0;
        
        PrintWriter out;
        BufferedReader in;
        
        ServerSocket serverSocket;
        Socket clientSocket;
        
        try
        {
            serverSocket = new ServerSocket(portNum);
            System.out.println("Server Bound");
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true); //auto-flushing
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Client Connected");
        }
        catch (IOException e)
        {
                System.out.println("Binding server failed!");
                e.printStackTrace();
                return;
        }
        
        String input;
        
        try
        {
            out.println("Sup Brah?");
            System.out.println("Greeting sent");
            while ((input = in.readLine()) != null)
            {
                System.out.println("From Client: " + input);
                if (input.equals("done")) break;
                else out.println("Cool Story, Bro!");
            }
            System.out.println("Connection Closing");
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
            System.out.println("Connection Closed");
        }
        catch (IOException e)
        {
                System.out.println("Unknown Error");
                e.printStackTrace();
        }
    }
    
}