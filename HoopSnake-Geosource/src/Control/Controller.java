package Control;

import Communication.CommSocket;
import DataBase.DBAccess;
import FileSystem.FileAccess;
import ServerClientShared.FieldWithContent;
import ServerClientShared.FieldWithoutContent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Connor
 */
public class Controller {
    
    private DBAccess dbAccess;
    private FileAccess fileAccess;
    
    private static int numConnections = 5;
    
    public void main(String[] args)
    {
        //Main Server runtime code here
        ExecutorService exec = Executors.newCachedThreadPool();
        LinkedList<Future<FieldWithContent>> list = new LinkedList();
        for (int x = 0; x < numConnections; x ++)
        {
            list.add(exec.submit(new CommSocket(this)));
        }
        
        //TODO loop through taking out done tasks, and making a new connection on the executor for each done one

    }
    
    public ArrayList<FieldWithoutContent> getForm(String channelName)
    {
        String filePath = dbAccess.getFormSpecLocation(channelName);
        return fileAccess.getFormSpec(filePath);
    }
}
