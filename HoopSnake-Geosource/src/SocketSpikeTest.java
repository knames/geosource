import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.ListIterator;

public class SocketSpikeTest {

    public static void main(String[] args) throws ClassNotFoundException {
        String ipaddress = "localhost";
        int portNum = 25565;
        
        ObjectOutputStream out; //wrapped stream to client
        ObjectInputStream in; //stream from client       
        Socket outSocket;
        
        try //create socket
        {
            outSocket = new Socket(InetAddress.getByName(ipaddress), portNum);
            if (outSocket.isConnected()) System.out.println("Connection Established");
            out = new ObjectOutputStream(outSocket.getOutputStream());
            in = new ObjectInputStream(outSocket.getInputStream());
            System.out.println("Stream Created");
        }
        catch(IOException e)
        {
            System.out.println("Shit got no connection, son!");
            e.printStackTrace();
            return; //end program if connection failed
        }
        
        LinkedList<Integer> testList = new LinkedList();
        testList.add(3);
        testList.add(1);
        testList.add(4);
        testList.add(1);
        testList.add(5);
        testList.add(9);
        testList.add(2);
        testList.add(6);
        
        LinkedList receiveList;
        
        try
        {
            System.out.println("Attempting receive");
            receiveList = (LinkedList)in.readObject();
            ListIterator<Integer> iter = receiveList.listIterator();
            while (iter.hasNext())
            {
                System.out.print(iter.next());
            }
            System.out.print("\n");
            
            System.out.println("Sending Data");
            out.writeObject(testList);
            
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
