package DataBase;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Connor
 */
public class DBAccess {
	   // JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://www.okenso.com:3306/dev";

	   //  Database credentials
	   static final String USER = "hdev";
	   static final String PASS = "devsnake371";
    
    private Connection dbconnection;
    
    public DBAccess()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("fail");
        }
        
        try
        {
        	//DriverManager.getConnection(DB_URL,USER,PASS);
            dbconnection = DriverManager.getConnection(DB_URL,USER,PASS);
        }
        catch(SQLException sqle)
        {
            System.out.println("Error connecting to Database...");
            System.out.println(sqle.getMessage());
        }
    }

    /**
     * Get the file path where a serialized dynamic form specification can be
     * found within the file system
     * @param channelName the name of the channel whose form's spec should be retrieved
     * @return a string representation of specification's location in the file system
     */
    public String getFormSpecLocation(String channelName)
    {
        String filePath = null;
        try (Statement statement = dbconnection.createStatement()) {
            ResultSet results = statement.executeQuery(Queries.getFormSpec(channelName));
            filePath = results.getNString(1);
        }
        catch (SQLException SQLe)
        {
            System.out.println("formSpec not found!! Possible broken Incident request");
        }
        return filePath;
    }
    
    /**
     * saves a string to it's field position on a channel's database table
     * @param channelName the channel who's table will be saved to
     * @param fieldName the name of the field we are saving
     * @param content the string to save
     */
    public void saveStringField(String channelName, String fieldName, String content)
    {
        try (Statement statement = dbconnection.createStatement()) {
            statement.execute(Queries.saveStringField(channelName, fieldName, content));
        }
        catch (SQLException SQLe)
        {
            System.out.println("Saving string field failed");
        }
    }
    
    /**
     * saves the filepath to the database for a picture previously saved to the filesystem
     * @param channelName the name of the channel being posted to
     * @param fieldName the title of this picture's field
     * @param filePath the filepath to be posted
     */
    public void savePictureField(String channelName, String fieldName, String filePath)
    {
        try (Statement statement = dbconnection.createStatement()) {
            statement.execute(Queries.savePictureField(channelName, fieldName, filePath));
        }
        catch (SQLException SQLe)
        {
            System.out.println("Saving picture field failed");
        }
    }
}