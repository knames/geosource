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
     * @param fieldName the name (title) of the string field
     * @param content the string to be posted
     * @return a query that can be executed to save the string correctly
     */
    public static String saveStringField(String channelName, String fieldName, String content)
    {
        return null; //TODO Ken please implement here
    }
    
    /**
     * get a query to save in the database a filepath for a recently saved picture
     * @param channelName the name of the channel to save to
     * @param fieldName the title of the picture field
     * @param filePath the file path to the picture
     * @return a query that can be executed to save the filepath to the database
     */
    public static String savePictureField(String channelName, String fieldName, String filePath)
    {
        return null; //TODO Ken please implement here
    }
}
