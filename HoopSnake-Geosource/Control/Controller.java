package Control;

import Communication.CommSocket;
import DataBase.DBAccess;
import FileSystem.FileAccess;
import ServerClientShared.Channel;
import ServerClientShared.ChannelIdentifier;
import ServerClientShared.FieldType;
import ServerClientShared.FieldWithContent;
import ServerClientShared.FieldWithoutContent;
import ServerClientShared.GeotagFieldWithoutContent;
import ServerClientShared.Incident;
import ServerClientShared.StringFieldWithoutContent;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Connor
 */
public class Controller {
    
    private DBAccess dbAccess; //database abstraction
    private FileAccess fileAccess; //file system abstraction
    
    private static int numConnections = -1; //maximum simultaneous socket inputs
    ExecutorService exec; //thread pool for socket connections
    
    private static ArrayList<CommSocket> socketList; //list of sockets (anti-garbage)
    
    public Controller(int connections)
    {
        numConnections = connections;
        socketList = new ArrayList(numConnections);
        exec = Executors.newFixedThreadPool(numConnections);
        
        boolean successful = false;
        try
        {
            dbAccess = new DBAccess();
            fileAccess = new FileAccess();
            insertTestChannel();
            successful = true;
        }
        catch (IllegalStateException ISe)
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
        Controller Server = new Controller(Integer.parseInt(args[1]));
        Server.run(Integer.parseInt(args[0]));
    }
    
    public void run(int portNum)
    {
    	CommSocket.portNum = portNum;
        for (int x = 0; x < numConnections; x ++)
        {
            newSocket(x);
        }
    }
    
    private void newSocket(int SocketNum)
    {
        CommSocket newSocket = new CommSocket(this, SocketNum);
        socketList.add(SocketNum, newSocket); //store running sockets
        exec.execute(newSocket);
    }
    
    /**
     * called by a socket upon complete to signal its own replacement
     * @param SocketNum the sockets stored identification number
     */
    public void socketComplete(int SocketNum)
    {
        socketList.remove(SocketNum);
        newSocket(SocketNum);
    }
    
    /**
     * inserts the test channel to the system on startup
     */
    private void insertTestChannel()
    {
        String[] types = {"STRING", "IMAGE", "VIDEO", "AUDIO"};
        ArrayList<String> typeList = new ArrayList();
        for (String type : types)
        {
            typeList.add(type);
        }
        String[] fieldNames = {"TextField", "PictureField", "VideoField", "AudioField"};
        ArrayList<String> fieldNameList = new ArrayList();
        for (String fieldName : fieldNames)
        {
            fieldNameList.add(fieldName);
        }
        boolean[] required = {false, false, false, false};
        ArrayList<Boolean> requiredList = new ArrayList();
        for (boolean require : required)
        {
            requiredList.add(require);
        }
        newChannel("testing", "okenso", true, typeList, fieldNameList, requiredList);
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
    public void newChannel(String title, String owner, boolean isPublic, ArrayList<String> types, ArrayList<String> fieldNames, ArrayList<Boolean> required)
    {
        if (!dbAccess.channelExists(title, owner)) //don't re-make on normal server restart
        {
            ArrayList<FieldWithoutContent> newSpec = new ArrayList();
            newSpec.add(new StringFieldWithoutContent("title", true));
            newSpec.add(new GeotagFieldWithoutContent("geotag", true)); //always present
            //newSpec.add(new CheckboxFieldWithoutContent("question", true));
            //make spec array
            if (types.size() != fieldNames.size() || types.size() != required.size() || fieldNames.size() != required.size())
                throw new RuntimeException("non-standard form fields do not match in length!");
            for (int i = 0; i < types.size(); i ++)
            {
                newSpec.add(FieldType.valueOf(types.get(i)).getField(fieldNames.get(i), required.get(i)));
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
            if (field.getContent() != null)
            {
                String filePath = fileAccess.saveField(field);
                dbAccess.saveField(incident.getChannelName(), incident.getOwnerName(), postNum, field.getTitle(), filePath);
            }
        }
    }
    
    public ArrayList<FieldWithoutContent> getForm(String channelName, String ownerName)
    {
        String fileName = dbAccess.getFormSpecLocation(channelName, ownerName); //get spec's file name in filesystem
        return fileAccess.getFormSpec(fileName); //retreive form spec
    }
    
    /**
     * get all the channels in the system
     * @return a list of all channels
     */
    public LinkedList<ChannelIdentifier> getChannelList()
    {
        LinkedList<ChannelIdentifier> channelList = dbAccess.getChannelList();
        if (channelList.isEmpty()) return null;
        else return channelList;
    }
    
    /**
     * retrieve all subscribed channels specs for a given user (expensive operation)
     * @param userName the name of the user
     * @return a list of channel identifiers and form specifications
     */
    public LinkedList<Channel> getSubscriptions(String userName)
    {
        //get list of identifiers and file locations
        LinkedList<ChannelIdentifier> subscriptionIDs = dbAccess.getSubscriptionIDs(userName);
        if (subscriptionIDs == null) return null;
        LinkedList<String> locations = dbAccess.getSpecLocations(subscriptionIDs);
        if (locations == null) return null;
        
        //make list of actual form specs
        LinkedList<ArrayList<FieldWithoutContent>> forms = new LinkedList();
        for (String location : locations)
        {
            forms.add(fileAccess.getFormSpec(location));
        }
        
        //zipper the two informations together
        LinkedList<Channel> returnList = new LinkedList();
        Iterator<ChannelIdentifier> IDiter = subscriptionIDs.iterator();
        Iterator<ArrayList<FieldWithoutContent>> specIter = forms.iterator();
        while (IDiter.hasNext() && specIter.hasNext())
        {
            Channel newChannel = new Channel(IDiter.next(), specIter.next());
            returnList.add(newChannel);
        }
        
        return returnList;
    }
}