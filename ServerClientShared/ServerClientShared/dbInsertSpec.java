package ServerClientShared;

import java.sql.*;


public class dbInsertSpec {
	   // JDBC driver name and database URL
	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://www.okenso.com:3306/dev";

	   //  Database credentials
	   static final String USER = "hdev";
	   static final String PASS = "devsnake371";
	   
	   private dbInsertSpec(String Path){
		   
		   Connection conn = null;
		   Statement stmt = null;
		   try{
			  //Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");
		      //Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		      conn.setAutoCommit(false);
		      //Execute a query to create statment with
		      System.out.println("Creating statement...");
		      stmt = conn.createStatement(
		                           ResultSet.TYPE_SCROLL_INSENSITIVE,
		                           ResultSet.CONCUR_UPDATABLE);
		      System.out.println("Selecting last ch_spec");
		      String SQL = "select ch_spec from channels order by 1 desc limit 1";
		      ResultSet rs = stmt.executeQuery(SQL);

		      rs.beforeFirst();
		      rs.next();
		      System.out.println(rs.getString("ch_spec"));
		      
		      SQL = "";
		     
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
	   
		public static void main (String[] args) throws Exception{
			dbInsertSpec test = new dbInsertSpec("sample");

		}
}
