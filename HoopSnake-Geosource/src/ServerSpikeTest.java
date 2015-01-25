import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.ListIterator;

public class ServerSpikeTest {


    public static void main(String[] args) throws ClassNotFoundException {
        
        int portNum = 25565;
        
        ObjectOutputStream out;
        ObjectInputStream in;
        
        ServerSocket serverSocket;
        Socket clientSocket;
        
        try
        {
            serverSocket = new ServerSocket(portNum);
            System.out.println("Server Bound");
            clientSocket = serverSocket.accept();
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("Client Connected");
        }
        catch (IOException e)
        {
                System.out.println("Binding server failed!");
                e.printStackTrace();
                return;
        }
        
        LinkedList testList = new LinkedList();
        testList.add(1);
        testList.add(2);
        testList.add(3);
        testList.add(4);
        testList.add(5);
        testList.add(6);
        testList.add(7);
        
        LinkedList receiveList;
        
        try
        {
            out.writeObject(testList);
            System.out.println("Greeting sent");
            
            receiveList = (LinkedList)in.readObject();
             ListIterator<Integer> iter = receiveList.listIterator();
            while (iter.hasNext())
            {
                System.out.print(iter.next());
            }
            System.out.print("\n");
            
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