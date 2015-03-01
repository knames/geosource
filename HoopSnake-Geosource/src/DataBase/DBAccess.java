package DataBase;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

    public String getFormSpecLocation(String channelName)
    {
        return null;
    }
}