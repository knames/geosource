package hoopsnake.geosource.data;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

//TODO All the Field classes are now shared. remove them and import instead.

/**
 * Created by wsv759 on 18/02/15.
 * A single field within an incident.
 * Includes title, type, and a content field. The content field may be null.
 */
public class FieldWithoutContent extends Field implements Serializable {

    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    /**
     * A parameter sent along with the field without content, for instance a list of options
     * if the field is an option select list.
     */
    Serializable param = null;

    public FieldWithoutContent(String title, FieldType type, boolean isRequired, Serializable param)
    {
        super(title, type, isRequired);

        param = this.param;
    }

    /** Serializable implementation. */
    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException
    {
        title = (String) in.readObject();
        type = (FieldType) in.readObject();
        isRequired = in.readBoolean();
        switch(type)
        {
            //TODO make these make sense. Honestly the whole implementation here needs to change to stop relying on these switch statements. Sort of like within the app.
            case IMAGE:
                //probably no parameters here.
                break;
            case STRING:
                //Probably the only parameter is units? Really there should be just a different type for
                //different string fields having different parameters.
                param = (String) in.readObject();
                break;
            case VIDEO:
                //probably no parameters here.
                break;
            case AUDIO:
                //probably no parameters here.
                break;
            case OPTION_LIST:
                //This probably needs to change too.
                param = (ArrayList<String>) in.readObject();
                break;
        }
        param = (Serializable) in.readObject();
    }

    /** Serializable implementation. */
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeObject(title);
        out.writeObject(type);
        out.writeBoolean(isRequired);
        //TODO write out a param properly. This whole implementation is weird.
        out.writeObject(param);
    }

    /** Serializable implementation. */
    private void readObjectNoData() throws InvalidObjectException {
        throw new InvalidObjectException("Stream data required");
    }
}
