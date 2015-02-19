package hoopsnake.geosource.comm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Connor made the initial framework, William filled it in.
 *
 */
public class Packet implements Serializable{

    //TODO this whole class.
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    public static enum ListFields{Picture, String, Video, Sound};

    private LinkedList<String> fieldNames;
    private LinkedList<ListFields> fieldTypes;

    /** this is what I am returning to Connor. */
    private HashMap<String, Object> packetContents;

    public Packet(HashMap<String, Object> packetContents)
    {
        this.packetContents = packetContents;
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException
    {
        fieldNames = (LinkedList<String>)in.readObject();
        fieldTypes = (LinkedList<ListFields>)in.readObject();
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeObject(packetContents);
    }
}
