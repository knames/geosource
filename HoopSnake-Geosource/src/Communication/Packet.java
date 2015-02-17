package Communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * A container for sending incident posts between the android app and the server.
 * Meant to be serialized and sent using an ObjectOutputStream.
 * @author Connor
 */
public class Packet implements Serializable{
    
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;
    
    public static enum listFields{Picture, String, Video, Sound};
    
    private LinkedList<String> fieldNames;
    private LinkedList<listFields> fieldTypes;
    private HashMap<String, Object> packetContents;
    
    public Packet(LinkedList<String> fieldNames, LinkedList<listFields> fieldTypes)
    {
        this.fieldNames = fieldNames;
        this.fieldTypes = fieldTypes;
    }
    
    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException
    {
        fieldNames = (LinkedList<String>)in.readObject();
        fieldTypes = (LinkedList<listFields>)in.readObject();
        packetContents = (HashMap<String, Object>)in.readObject();
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeObject(fieldNames);
        out.writeObject(fieldTypes);
        out.writeObject(packetContents);
    }
    
    
}
