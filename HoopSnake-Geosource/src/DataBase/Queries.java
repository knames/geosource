package DataBase;

/**
 *
 * @author Connor
 */
public class Queries {
    
    /**
     * get a query used to get dynamic form specifications from the database
     * @param channelName the string name of the channel whose spec will be retrieved
     * @return a query that will execute to retrieve the file path
     */
    public static String getFormSpec(String channelName)
    {
        String returnString = "SELECT spec FROM channels WHERE name = '"
                + channelName + "'";
        return returnString;
    }
    
    /**
     * get a query to save a string value from an incident post to the database
     * @param channelName the channel being posted to
     * @param ownername the owner of the channel
     * @param postnumber the posts unique number id
     * @param fieldName the name (title) of the string field
     * @param content the string to be posted
     * @return a query that can be executed to save the string correctly
     */
    public static String saveStringField(String channelName, String ownername, int postnumber, String fieldName, String content)
    {
    	//TODO Needs testing.
    	//update posts_okenso_pothole set p_field1 = "yo" where p_number=1; sample of SQL code.
    	String sql = "update posts_" + ownername + "_" + channelName 
    			+ " set p_" + fieldName + " = \"" + content + "\" where p_number = " + postnumber;
        return sql;
    }
    
    /**
     * get a query to save in the database a filepath for a recently saved picture
     * @param channelName the name of the channel to save to
     * @param ownerName the name of the creator of the channel
     * @param postNumber the number of the post to be populated
     * @param fieldName the title of the picture field
     * @param filePath the file path to the picture
     * @return a query that can be executed to save the filepath to the database
     */
    public static String savePictureField(String channelName, String ownerName, int postNumber, String fieldName, String filePath)
    {
    	//TODO Needs testing.
    	//update posts_okenso_pothole set p_field1 = "yo" where p_number=1; sample of SQL code.
    	String sql = "update posts_" + ownerName + "_" + channelName 
    			+ " set p_" + fieldName + " = \"" + filePath + "\" where p_number = " + postNumber;
        return sql;
    }
    
    /** returns a query which will grab the highest post number in a given channel table
     * @param channelName the channel name
     * @param ownername the owner of the channel
     * @return the sql query that will give the highest number in the post */
    public static String getPostNum(String channelName, String ownername){
		String sql = "select p_number from posts_" + ownername + "_" + channelName + " order by 1 asc limit 1;"; 
    	return sql;
    	
    }
}
