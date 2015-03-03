package DataBase;

/**
 *
 * @author Connor
 */
public class Queries {
    public static String getFormSpec(String channelName)
    {
        String returnString = "SELECT spec FROM channels WHERE name = '"
                + channelName + "'";
        return returnString;
    }
}
