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
    public static String getNewPostNum(String channelName, String ownername, String posterName) {
        String insertrow = "Insert into posts_" + ownername + "_" + channelName + " (p_poster) values (\""
                + posterName + "\");\n";
        String getNum = "select p_number from posts_" + ownername + "_" + channelName + " order by 1 asc limit 1;";
        
        System.out.println(insertrow + getNum);
        return insertrow + getNum;
    }
    
    /** returns a query that drops all tables and rebuilds the database
     @return returns the sql to drop and rebuild the database*/
    public static String rebuildDB() {
        String sql = "SOURCE /var/www/okenso.com/cmpt371group2/Database/dbdrop.sql" + 
                " SOURCE /var/www/okenso.com/cmpt371group2/Database/dbinit.sql";
       return sql;
    }
}
