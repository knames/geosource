package Control;

import Communication.CommSocket;
import DataBase.DBAccess;
import FileSystem.FileAccess;
import ServerClientShared.FieldType;
import ServerClientShared.FieldWithContent;
import ServerClientShared.FieldWithoutContent;
import ServerClientShared.GeotagFieldWithoutContent;
import ServerClientShared.Incident;
import ServerClientShared.StringFieldWithoutContent;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Connor
 */
public class Controller {
    
    private DBAccess dbAccess; //database abstraction
    private FileAccess fileAccess; //file system abstraction
    
    private static int numConnections = 5; //maximum simultaneous socket inputs
    
    private static ArrayList<CommSocket> socketList;
    
    public Controller()
    {
        boolean successful = false;
        socketList = new ArrayList(numConnections);
        try
        {
            dbAccess = new DBAccess();
            fileAccess = new FileAccess();
            insertTestChannel();
            successful = true;
        }
        catch (SQLException SQLe)
        {
            System.out.println("Database initialization failed");
        }
        catch (URISyntaxException URISe)
        {
            System.out.println("Filesystem not consistent, error initializing path");
        }
        finally
        {
            if (!successful) throw new RuntimeException("Controller Initialization failed");
        }
    }
    
    /**
     * runs the server
     * @param args specifies the port number and concurrent thread count
     */
    public static void main(String[] args)
    {
    	numConnections = Integer.parseInt(args[1]);
        Controller Server = new Controller();
        Server.run(Integer.parseInt(args[0]));
        
    }
    
    public void run(int portNum)
    {
    	CommSocket.portNum = portNum;
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int x = 0; x < numConnections; x ++)
        {
            socketList.add(new CommSocket(this, x)); //store running sockets
        }
    }
    
    /**
     * called by a socket upon complete to signal its own replacement
     * @param SocketNum the sockets stored identification number
     */
    public void socketComplete(int SocketNum)
    {
        socketList.remove(SocketNum);
        socketList.add(SocketNum, new CommSocket(this, SocketNum));
    }
    
    /**
     * inserts the test channel to the system on startup
     */
    private void insertTestChannel()
    {
        String[] types = {"STRING", "IMAGE", "VIDEO", "AUDIO"};
        String[] fieldNames = {"TextField", "PictureField", "VideoField", "AudioField"};
        boolean[] required = {false, false, false, false};
        newChannel("testing", "okenso", true, types, fieldNames, required);
    }
    
    /**
     * make a new channel in the system
     * @param title the name of the new channel
     * @param owner the creator of the new channel
     * @param isPublic whether the channel should be publicly visible
     * @param types the types of the channel's non-default fields, in order
     * @param fieldNames the names of the channel's non-default fields
     * @param required whether or not each non-default field is a required field
     */
    private void newChannel(String title, String owner, boolean isPublic, String[] types, String[] fieldNames, boolean[] required)
    {
        if (!dbAccess.channelExists(title, owner)) //don't re-make on normal server restart
        {
            ArrayList<FieldWithoutContent> newSpec = new ArrayList();
            newSpec.add(new StringFieldWithoutContent("title", true));
            newSpec.add(new GeotagFieldWithoutContent("geotag", true)); //always present
            //make spec array
            for (int i = 0; i < types.length; i ++)
            {
                newSpec.add(FieldType.valueOf(types[i]).getField(fieldNames[i], required[i]));
            }
            int newSpecNum = dbAccess.createNewChannel(title, owner, isPublic, fieldNames);
            if (newSpecNum >=0) //only if new spec should be made
                fileAccess.saveFormSpec(newSpec, owner, newSpecNum);
        }
    }
    
    /**
     * delegates the parsing of an incident to te file and database systems
     * @param incident the incident to be handled
     */
    public void dealWith(Incident incident)
    {
        if (null == incident) return; //wasn't a real request
        int postNum = dbAccess.newPost(incident.getChannelName(), incident.getOwnerName(), incident.getPosterName());
        for (FieldWithContent field : incident.getFieldList())
        {
            String filePath = fileAccess.saveField(field.getContent());
            dbAccess.saveField(incident.getChannelName(), incident.getOwnerName(), postNum, field.getTitle(), filePath);
        }
    }
    
    public ArrayList<FieldWithoutContent> getForm(String channelName, String ownerName)
    {
        String fileName = dbAccess.getFormSpecLocation(channelName, ownerName); //get spec's file name in filesystem
        return fileAccess.getFormSpec(fileName); //retreive form spec
    }
}