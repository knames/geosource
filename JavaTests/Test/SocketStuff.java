package Test;

import ServerClientShared.Commands;
import ServerClientShared.FieldWithoutContent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


/**
 * A modified version of sockets used for testing. Lifted partially from the android code.
 * @author Warren
 */
public class SocketStuff 
{
    public static ArrayList<FieldWithoutContent> doStuff(String channelName,String channelOwner)
    {
        ObjectOutputStream outStream; //wrapped stream to client
        ObjectInputStream inStream; //stream from client
        Socket outSocket;




        try //create socket
        {
            SocketWrapper socketWrapper = new SocketWrapper();
            outSocket = socketWrapper.getOutSocket();
            outStream = socketWrapper.getOut();
            inStream = socketWrapper.getIn();
        }
        catch(IOException e)
        {
            throw new RuntimeException("Creating socket error");
        }


        ArrayList<FieldWithoutContent> fieldsToBeFilled;
        try
        {
            //TODO identify with the server whether I am asking for an incident spec or sending an incident.
            System.out.println("Attempting to send incident.");
            outStream.writeObject(Commands.IOCommand.GET_FORM);
            outStream.writeUTF(channelName);
            outStream.flush();
            outStream.writeUTF(channelOwner);
            outStream.flush();


            System.out.println("Retrieving reply...");
            fieldsToBeFilled = (ArrayList<FieldWithoutContent>) inStream.readObject();
            /*if (fieldsToBeFilled == null)
                throw new RuntimeException("Handed null fields.");*/

            inStream.close();
            outStream.close();
            outSocket.close();
            System.out.println("Connection Closed");
        }
        catch (IOException e)
        {

            throw new RuntimeException("Connection failed.");
        }
        catch (ClassNotFoundException e) 
        {
            throw new RuntimeException("Weird stuff happened.");
        }
        
        return fieldsToBeFilled;
       
    }
}
