package Test;

import ServerClientShared.Channel;
import ServerClientShared.Commands;
import ServerClientShared.FieldWithoutContent;
import ServerClientShared.Incident;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * A modified version of sockets used for testing. Lifted partially from the android code.
 * @author Warren, original by Connor
 */
public class SocketStuff 
{
    
    public static boolean getPing()
    {
        ObjectOutputStream outStream; //wrapped stream to client
        ObjectInputStream inStream; //stream from client
        Socket outSocket;



        Commands.IOCommand ping;
        try //create socket
        {
            SocketWrapper socketWrapper = new SocketWrapper();
            outSocket = socketWrapper.getOutSocket();
            outStream = socketWrapper.getOut();
            inStream = socketWrapper.getIn();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Creating socket error");
        }


        ArrayList<FieldWithoutContent> fieldsToBeFilled;
        try
        {
            System.out.println("Attempting to send a ping.");
            outStream.writeObject(Commands.IOCommand.PING);
            outStream.flush();



            System.out.println("Retrieving reply...");
            ping = (Commands.IOCommand) inStream.readObject();

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
   
        return (ping==Commands.IOCommand.PING);
       
    }
    
    /**
     * Grabs the spec for the given channel.
     * @param channelName the channel we wish to post to grab the spec from.
     * @param channelOwner the owner of the channel "channelname"
     * @return An ArrayList, holding fieldwithoutContents. This represents the specs for
     * our system.
     */
    public static ArrayList<FieldWithoutContent> getSpec(String channelName,String channelOwner)
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
            e.printStackTrace();
            throw new RuntimeException("Creating socket error");
        }


        ArrayList<FieldWithoutContent> fieldsToBeFilled;
        try
        {

            System.out.println("Attempting to send grab spec list.");
            outStream.writeObject(Commands.IOCommand.GET_FORM);
            outStream.writeUTF(channelName);
            outStream.flush();
            outStream.writeUTF(channelOwner);
            outStream.flush();


            System.out.println("Retrieving reply...");
            fieldsToBeFilled = (ArrayList<FieldWithoutContent>) inStream.readObject();


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
    
    /**
     * Sends an incident to be posted to the server.
     * This is a function used for testing!!! Other then checking for socket errors,
     * we need to personally check the incident for correctness!
     * @param incidentToSend A filled incident we want to send.
     */
    public static void makePost(Incident incidentToSend)
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
            throw new RuntimeException("Creating socket error(mP)");
        }


        try
        {
            System.out.println("Attempting to send incident.");
            outStream.writeObject(Commands.IOCommand.SEND_INCIDENT);
            outStream.writeObject(incidentToSend);
            outStream.flush();


            inStream.close();
            outStream.close();
            outSocket.close();
            System.out.println("Connection Closed");
        }
        catch (IOException e)
        {
             throw new RuntimeException("Connection failed.(mP)");
        }
    }
    
    /**
     * Grabs a list of subscribed channels for the user
     * @param user The user we wish to grab the channels for
     * @return the list of subscribed channels.
     */
    public static LinkedList<Channel> grabSubs(String user)
    {
        ObjectOutputStream outStream; //wrapped stream to client
        ObjectInputStream inStream; //stream from client
        Socket outSocket;

        LinkedList<Channel> channels;
        
        try //create socket
        {
            SocketWrapper socketWrapper = new SocketWrapper();
            outSocket = socketWrapper.getOutSocket();
            outStream = socketWrapper.getOut();
            inStream = socketWrapper.getIn();
        }
        catch(IOException e)
        {
            throw new RuntimeException("Creating socket error(gS)");
        }


        try
        {
            System.out.println("Attempting to grab subscriptions of user.");
            outStream.writeObject(Commands.IOCommand.GET_SUBSCRIBED_CHANNELS);
            outStream.writeUTF(user);
            outStream.flush();
            
            channels = (LinkedList<Channel>) inStream.readObject();

            inStream.close();
            outStream.close();
            outSocket.close();
            System.out.println("Connection Closed");
        }
        catch (IOException e)
        {
             throw new RuntimeException("Connection failed.(gS)");
        }
        catch (ClassNotFoundException e) 
        {
            throw new RuntimeException("Weird stuff happened.(gS)");
        }
        return channels;
    }
}
