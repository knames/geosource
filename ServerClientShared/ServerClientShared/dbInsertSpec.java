package ServerClientShared;

/**
 * Created by kts192 on 7/03/15.
 */

import java.sql.*;

// this dbconnection could probably be refactored out and just pass strings to it.
// might allow for some reuse later?

/** inserts a new channel into the channels table */
public class dbInsertSpec {
	   // JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://www.okenso.com:3306/dev";

	   //  Database credentials
	   static final String USER = "hdev";
	   static final String PASS = "devsnake371";
	   
	   /** The spec # for the given username, default -1 if it fails. */
	   private int spec = -1; // the spec we are after to write to file
	   
	   /** Connects to the database, checks if the given channel name and username combo
	    * are already in the database, if not, it inserts the new channel and increases spec.
	    * @param chName the channel name
	    * @param username the username
	    * @param ispublic whether or not the new channel is public or not. */
	   dbInsertSpec(String chName, String username, boolean ispublic){
		   
		   Connection conn = null;
		   Statement stmt = null;
		   try{
			   String SQL = "";
			   ResultSet rs = null;
			  //Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");
		      //Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		      conn.setAutoCommit(false);
		      //Execute a query to create statement with
		      System.out.println("Creating statement...");
		      stmt = conn.createStatement(
		                           ResultSet.TYPE_SCROLL_INSENSITIVE,
		                           ResultSet.CONCUR_UPDATABLE);
		      // This check can probably be removed once the website does the check for us.
		      System.out.println("checking if " + chName + " " + username + " already exists...");
		      SQL = "select count(*) from channels where ch_name = \"" + chName + "\" and ch_owner = \""
		    		  + username + "\";";
		      rs = stmt.executeQuery(SQL);
		      rs.beforeFirst();
		      rs.next();
		      if (rs.getBoolean("count(*)")){
		    	  throw new Exception("That channel name and username already exist!");
		      } else {
		    	  System.out.println("...nope, we're good!");
		      }
		      // end of dupe check
		      
		      
		      SQL = "select ch_spec from channels where ch_owner =\"" + username + "\"order by 1 desc limit 1";
		      rs = stmt.executeQuery(SQL);

		      rs.beforeFirst();
		      rs.next();
		      this.spec = rs.getInt("ch_spec") + 1; //increase because obviously this spec is taken
		      //System.out.println(rs.getString("ch_spec"));
		      //System.out.println(spec);
		      
		      SQL = "insert into channels values (\"" + chName + "\", \"" + username + "\", " + this.spec + ", " + ispublic +");";
		      //System.out.println(SQL);
		      stmt.executeUpdate(SQL);

		      // All good up to here? commit!
		      System.out.println("Commiting data here....");
		      conn.commit();
		      
		      stmt.close();
		      conn.close();
		      
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		      // If there is an error then rollback the changes.
		      System.out.println("Rolling back data here....");
			  try{
				 if(conn!=null)
		            conn.rollback();
		      }catch(SQLException se2){
		         se2.printStackTrace();
		      }
		   }catch(Exception e){
		      e.printStackTrace();
		   }finally{
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
   }
	   /** returns the spec number. Will default return -1 if this class fails */
	   public int getSpecNum(){
		   return this.spec;
	   }
	   
	   
	   /** small class for basic testing. */
		public static void main (String[] args) throws Exception{
			//dbInsertSpec test = new dbInsertSpec("pothole", "cindy", true); // throw error for dupes, works.
			@SuppressWarnings("unused")
			dbInsertSpec test = new dbInsertSpec("pothole", "frank", true);

		}
}
