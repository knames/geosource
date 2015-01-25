package socket.spike.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Connor
 */
public class SocketSpikeTest {

    public static void main(String[] args) {
        String ipaddress = "255.255.255.255";
        int portNum = 0;
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out; //wrapped stream to client
        BufferedReader in; //stream from client       
        Socket outSocket;
        
        try //create socket
        {
            outSocket = new Socket(InetAddress.getByName(ipaddress), portNum);
            if (outSocket.isConnected()) System.out.println("Connection Established");
            out = new PrintWriter(outSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(outSocket.getInputStream()));
            System.out.println("Stream Created");
        }
        catch(IOException e)
        {
            System.out.println("Shit got no connection, son!");
            e.printStackTrace();
            return; //end program if connection failed
        }
        
        String streamIn;
        
        try
        {
            System.out.println("Attempting receive");
            while ((streamIn = in.readLine()) != null)
            {
                System.out.println("From Server: " + streamIn);
                out.println(br.readLine());
            }
            System.out.println("Connection Closing");
            in.close();
            out.close();
            outSocket.close();
            System.out.println("Connection Closed");
        }
        catch (IOException e)
        {
            System.out.println("Unknown Error");
            e.printStackTrace();
        }
        
    }
    
}
