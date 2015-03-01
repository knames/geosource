package Control;

import DataBase.DBAccess;
import FileSystem.FileAccess;
import ServerClientShared.FieldWithoutContent;
import java.util.ArrayList;

/**
 *
 * @author Connor
 */
public class Controller {
    
    private DBAccess dbAccess;
    private FileAccess fileAccess;
    
    public static void main(String[] args)
    {
        //Main Server runtime code here
    }
    
    public ArrayList<FieldWithoutContent> getForm(String channelName)
    {
        String filePath = dbAccess.getFormSpecLocation(channelName);
        return fileAccess.getFormSpec(filePath);
    }
}
