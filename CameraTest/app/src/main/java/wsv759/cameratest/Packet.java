package wsv759.cameratest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.HashMap;

/**
 *
 * @author Connor made the initial framework, William filled it in.
 *
 */
public class Packet implements Serializable{

    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    private HashMap<String, Object> packetContents;

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException
    {
        packetContents = (HashMap<String, Object>) in.readObject();
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeObject(packetContents);
    }


}
