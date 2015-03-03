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
}
