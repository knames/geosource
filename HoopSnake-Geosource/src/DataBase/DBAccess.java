package DataBase;

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
    
    private Connection dbconnection;
    
    public DBAccess()
    {
        try
        {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("fail");
        }
        
        try
        {
            dbconnection = DriverManager.getConnection
            ("jdbc:postgresql://SERVER_URL:PORTNUM/", "DB_NAME",
            "PASSWORD");
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
        try
        {
            Statement statement = dbconnection.createStatement();
            ResultSet results = statement.executeQuery(Queries.getFormSpec(channelName));
            filePath = results.getNString(1);
            statement.close();
        }
        catch (SQLException SQLe)
        {
            System.out.println("formSpec not found!! Possible broken Incident request");
        }
        return filePath;
    }
}