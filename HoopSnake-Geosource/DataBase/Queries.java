package DataBase;

/**
 *
 * @author Connor
 */
public class Queries {

    /**
     * get a query used to get the dynamic form specifications number from the
     * database
     *
     * @param channelName the string name of the channel whose spec will be
     * retrieved
     * @param ownerName the channel's creator, for uniqueness
     * @return a query that will execute to retrieve the form spec number. this
     * can be combined with the ownerName to get the name of the spec file ex:
     * okenso.6 if given owner: okenso.
     */
    public static String getFormSpec(String channelName, String ownerName) {
        String sql = "SELECT ch_spec FROM channels WHERE ch_name = \""
                + channelName + "\" and ch_owner = \"" + ownerName + "\";";
        return sql;
    }

    /**
     * get a query to save in the database a filepath for a recently saved
     * picture
     *
     * @param channelName the name of the channel to save to
     * @param ownerName the name of the creator of the channel
     * @param postNumber the number of the post to be populated
     * @param fieldName the title of the picture field
     * @param filePath the file path to the picture
     * @return a query that can be executed to save the filepath to the database
     */
    public static String saveField(String channelName, String ownerName, int postNumber, String fieldName, String filePath) {
    	//TODO Needs testing.
        //update posts_okenso_pothole set p_field1 = "yo" where p_number=1; sample of SQL code.
        String sql = "update posts_" + ownerName + "_" + channelName
                + " set p_" + fieldName + " = \"" + filePath + "\" where p_number = " + postNumber;
        return sql;
    }

    /**
     * returns a query which will insert a new row named after hte poster and
     * then grab the highest post number in a given
     * channel table
     *
     * @param channelName the channel name
     * @param ownername the owner of the channel
     * @param posterName the person posting this new incident
     * @return the sql query that will give the highest number in the post
     */
    public static String newRow(String channelName, String ownername, String posterName) {

        String sql = "Insert into posts_" + ownername + "_" + channelName + " (p_poster) values (\""
                + posterName + "\");";
        return sql;
    }
    
    /**
     * returns the next post number for a channel
     * @param channelName the name of the channel
     * @param ownername the channel's creator
     * @return 
     */
    public static String getNewPostNum(String channelName, String ownername)
    {
        String sql = "select p_number from posts_" + ownername + "_" + channelName + " order by 1 desc limit 1;";
        return sql;
    }
    
    /** returns a query that drops all tables and rebuilds the database
     @return returns the sql to drop and rebuild the database*/
    public static String rebuildDB() {
        String sql = "SOURCE /var/www/okenso.com/cmpt371group2/Database/dbdrop.sql" + 
                " SOURCE /var/www/okenso.com/cmpt371group2/Database/dbinit.sql";
       return sql;
    }
    
    
    
    /** creates a default post table
     @param ownername the name of the owner of the post
     @param tablename the name of the table.
     @param fields an array of column names for the fields. can take null of no fields.
     @return the sql for the table creation*/
    public static String createPosts(String ownername, String tablename, String[] fields){
        String allfields = "";
        if (fields != null){
            for (String i : fields){
                allfields = allfields + i + " varchar(100), ";
            }
        }
        String sql ="CREATE TABLE posts_" + ownername + "_" + tablename + " ( "
                + "p_poster varchar(25) NOT NULL, "
                + "p_number INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                + "p_time DATETIME, "
                + "p_lat DOUBLE, "
                + "p_long DOUBLE, "
                + allfields
                + "FOREIGN KEY (p_poster) REFERENCES users (u_username));";
        return sql;
    }    
    
    /**  a main class to test some functions output
     * @param args.*/
    public static void main(String[] args)
    {
    	String[] test = {"p_pic", "p_video", "p_audio"};
        System.out.println(createPosts("okenso", "toast", test));
        
    }
}

