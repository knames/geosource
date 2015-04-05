package DataBase;

import ServerClientShared.Channel;
import ServerClientShared.ChannelIdentifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Connor
 */
public class DBAccess {
	   // JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://www.okenso.com:3306/dev?allowMultiQueries=true";

	   //  Database credentials
	   static final String USER = "hdev";
	   static final String PASS = "devsnake371";
    
    private final Connection dbconnection;
    
    public DBAccess() throws SQLException
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("fail: " + e.getMessage());
        }
        
        dbconnection = DriverManager.getConnection(DB_URL,USER,PASS);
    }

    /**
     * Get the file name where a serialized dynamic form specification can be
     * found within the file system
     * @param channelName the name of the channel whose form's spec should be retrieved
     * @param ownerName the channel's creator, fort uniqueness
     * @return a string representation of specification's name in the file system
     */
    public String getFormSpecLocation(String channelName, String ownerName)
    {
        Integer fileName = null;
        try (Statement statement = dbconnection.createStatement()) {
            ResultSet results = statement.executeQuery(Queries.getFormSpec(channelName, ownerName));
            results.next();
            fileName = results.getInt(1);
        }
        catch (SQLException SQLe)
        {
            System.out.println("formSpec not found!! Possible broken Incident request" + SQLe.getMessage());
        }
        return ownerName + "." + fileName;
    }
    
    /**
     * saves the filepath to the database for a Field's contents previously saved to the filesystem
     * @param channelName the name of the channel being posted to
     * @param ownerName the creator of the channel, used for uniqueness
     * @param postNum the number of the post we are populating
     * @param fieldName the title of this picture's field
     * @param filePath the filepath to be posted
     */
    public void saveField(String channelName, String ownerName, int postNum, String fieldName, String filePath)
    {
        try (Statement statement = dbconnection.createStatement()) {
            statement.execute(Queries.saveField(channelName, ownerName, postNum, fieldName, filePath));
        }
        catch (SQLException SQLe)
        {
            System.out.println("Saving field to database failed" + SQLe.getMessage());
        }
    }
    
    public boolean channelExists(String title, String owner)
    {
        try (Statement statement = dbconnection.createStatement())
        {
            ResultSet results = statement.executeQuery(Queries.channelExists(title, owner));
            results.next();
            int existing = results.getInt(1);
            return (existing == 1); //if 1 channel was found, channel already exists
        }
        catch (SQLException SQLe)
        {
            System.out.println(SQLe.getMessage());
            throw new RuntimeException("Error finding previous channel");
        }
    }
    
    /**
     * creates a new channel and gives back the number the new spec should have
     * @param title the name of the new channel
     * @param owner the owner of the new channel
     * @param isPublic whether the channel should be publicly visible
     * @param fieldNames a list of the names of any non-standard fields
     * @return the number to be appended to the new spec
     */
    public int createNewChannel(String title, String owner, boolean isPublic, ArrayList<String> fieldNames)
    {
        try (Statement statement = dbconnection.createStatement())
        {
            ResultSet results = statement.executeQuery(Queries.nextSpecNum(owner)); //TODO add query
            results.next();
            int specNum = results.getInt("ch_spec") + 1;
            results.close();
            statement.execute(Queries.makeChannel(title, owner, specNum, isPublic, fieldNames)); //no batching, because the number retrieve does nothing
            return specNum;
        }
        catch (SQLException SQLe)
        {
            System.out.println("Creating new channel failed!" + SQLe.getMessage());
            return -1;
        }
    }

    /**
     * make a new post in the specified channel and return its identifier
     * @param channelName the name of the channel to post to
     * @param ownerName the creator of the channel
     * @param posterName the person posting to this channel
     * @return an integer value indexing the new post within the channel
     */
    public int newPost(String channelName, String ownerName, String posterName) {
        
        try (Statement statement = dbconnection.createStatement()) {
            statement.execute(Queries.newRow(channelName, ownerName, posterName));
            ResultSet results = statement.executeQuery(Queries.getNewPostNum(channelName, ownerName));
            results.next();
            return results.getInt(1);
        }
        catch (SQLException SQLe)
        {
            System.out.println("Creating new Post failed");
            return -1;
        }
    }
    
    public LinkedList<ChannelIdentifier> getChannelList()
    {
        try (Statement statement = dbconnection.createStatement())
        {
            ResultSet results = statement.executeQuery(Queries.getAllChannels());
            LinkedList<ChannelIdentifier> channelList = new LinkedList();
            while (results.next())
            {
                channelList.add(new ChannelIdentifier(results.getString("ch_name"), results.getString("ch_owner")));
            }
            return channelList;
        }
        catch (SQLException DQLe)
        {
            System.out.println("Getting Channel list failed");
            return null;
        }
    }
    
    /**
     * get the unique identifiers for all channels that a user is subscribed to
     * @param userName the user to query against
     * @return a list of the identifiers of subscribed channels
     */
    public LinkedList<ChannelIdentifier> getSubscriptionIDs(String userName)
    {
        try (Statement statement = dbconnection.createStatement())
        {
            ResultSet results = statement.executeQuery(Queries.getSubscriptionIDs(userName));
            LinkedList<ChannelIdentifier> identifiers = new LinkedList();
            while (results.next())
            {
                String chName = results.getString("ch_fav_chname");
                String chOwner = results.getString("ch_fav_chowner");
                identifiers.add(new ChannelIdentifier(chName, chOwner));
            }
            return identifiers;
        }
        catch (SQLException SQLe)
        {
            System.out.println("Error finding user's subscriptions");
            return null;
        }
    }
    
    /**
     * return a list of spec locations based on a list of Identifiers
     * @param IDs the identifiers of all the channels that need to be polled
     * @return a linked list of the filepaths of the forms
     */
    public LinkedList<String> getSpecLocations(LinkedList<ChannelIdentifier> IDs)
    {
        try (Statement statement = dbconnection.createStatement())
        {
            LinkedList<String> formLocations = new LinkedList();
            for (ChannelIdentifier ID : IDs)
            {
                String location = getFormSpecLocation(ID.getChannelName(), ID.getChannelOwner());
                formLocations.add(location);
            }
            return formLocations;
        }
        catch (SQLException SQLe)
        {
            System.out.println("Error getting list of form specifications");
            return null;
        }
    }
    
    /* executes a query  */
    public void easyQuery(String Query){
        try (Statement statement = dbconnection.createStatement()){
            statement.execute(Query);
            
        } catch (SQLException SQLe) {
            String error = "Error: " + SQLe.toString();
            System.out.println(error);
        }
        
    }
}