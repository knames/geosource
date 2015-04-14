package ServerClientShared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wsv759 on 19/02/15.
 */
public class Incident implements Serializable {
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    /** The fields for this incident, including their content. Their content may be null. */
    private ArrayList<FieldWithContent> fieldList;
    private String channelName;
    private String ownerName;
    private String posterName;

    //this is the second position
    public static final int POSITION_GEOTAG_FIELD = 1;
    public Incident(){}
    
    public Incident(ArrayList<FieldWithContent> fieldList, String channel, String owner, String poster)
    {
        if(fieldList==null)
            throw new RuntimeException("fieldList cannot be null");
        else if(fieldList.isEmpty())
            throw new RuntimeException("fieldList cannot be empty");
            
    	this.fieldList = fieldList;
        if(channel==null)
            throw new RuntimeException("channel cannot be null.");
            
        channelName = channel;
        if(owner==null)
            throw new RuntimeException("owner cannot be null.");
        ownerName = owner;
        
        if(poster==null)
            throw new RuntimeException("Poster cannot be null");
        posterName = poster;
    }

    public void setFieldList(ArrayList<FieldWithContent> fieldList)
    {
        if(fieldList==null)
            throw new RuntimeException("fieldList cannot be null");
        else if(fieldList.isEmpty())
            throw new RuntimeException("fieldList cannot be empty");
        this.fieldList = fieldList;
    }

    public ArrayList<FieldWithContent> getFieldList() {
        return fieldList;
    }

    /**
     * @return the channelName
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * @return the ownerName
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * @return the posterName
     */
    public String getPosterName() {
        return posterName;
    }
}
