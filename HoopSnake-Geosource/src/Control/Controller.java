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
    
    private DBAccess dbAccess; //database abstraction
    private FileAccess fileAccess; //file system abstraction
    
    private static int numConnections = 5; //maximum simultaneous socket inputs
    
    public void main(String[] args)
    {
        
        ExecutorService exec = Executors.newCachedThreadPool();
        LinkedList<Future<FieldWithContent>> list = new LinkedList();
        for (int x = 0; x < numConnections; x ++)
        {
            list.add(exec.submit(new CommSocket(this))); //fill list of future results
        }
        
        //TODO loop through taking out done tasks, and making a new connection
        //on the executor for each one completed

    }
    
    public ArrayList<FieldWithoutContent> getForm(String channelName)
    {
        String filePath = dbAccess.getFormSpecLocation(channelName); //get spec's path in filesystem
        return fileAccess.getFormSpec(filePath); //retreive form spec
    }
}