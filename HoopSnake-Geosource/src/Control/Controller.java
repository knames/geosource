package Control;

import Communication.CommSocket;
import DataBase.DBAccess;
import FileSystem.FileAccess;
import ServerClientShared.FieldWithContent;
import ServerClientShared.FieldWithoutContent;
import ServerClientShared.Incident;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
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
        LinkedList<Future<Incident>> list = new LinkedList();
        for (int x = 0; x < numConnections; x ++)
        {
            list.add(exec.submit(new CommSocket(this))); //fill list of future results
        }
        
        while (true)
        {
            for (Future<Incident> incident : list)
            {
                if (incident.isDone()) //filter out completed socket tasks
                {
                    try
                    {
                        dealWith(incident.get()); //deal with demands
                    }
                    catch (InterruptedException Ie)
                    {
                        //swallowing interrupt, should never happen because of isDone check
                        throw new RuntimeException("Should never receive an Interrupt Exception");
                    }
                    catch (ExecutionException Ee)
                    {
                        throw new RuntimeException("Socket Crashed:" + Ee.getCause().getMessage());
                    }
                    list.remove(incident); //remove completed task
                    list.add(exec.submit(new CommSocket(this))); //replace new socket
                }
            }
        }
    }
    
    /**
     * delegates the parsing of an incident to te file and database systems
     * @param incident the incident to be handled
     */
    private void dealWith(Incident incident)
    {
        if (null == incident) return; //wasn't a real request
        int postNum = dbAccess.newPost(incident.getChannelName(), incident.getOwnerName());
        for (FieldWithContent field : incident.getFieldList())
        {
            switch(field.getType())
            {
                case IMAGE:
                {
                    String filePath = fileAccess.savePicture((byte[])field.getContent());
                    dbAccess.savePictureField(incident.getChannelName(), incident.getOwnerName(), postNum, field.getTitle(), filePath);
                    break;
                }
                case STRING:
                {
                    dbAccess.saveStringField(incident.getChannelName(), incident.getOwnerName(), postNum, field.getTitle(), (String)field.getContent());
                    break;
                }
                case VIDEO:
                {
                    break;
                }
                case AUDIO:
                {
                    break;
                }
                case OPTION_LIST:
                {
                    break;
                }
            }
        }
        //TODO parse incident and save its fields
    }
    
    public ArrayList<FieldWithoutContent> getForm(String channelName)
    {
        String filePath = dbAccess.getFormSpecLocation(channelName); //get spec's path in filesystem
        return fileAccess.getFormSpec(filePath); //retreive form spec
    }
}