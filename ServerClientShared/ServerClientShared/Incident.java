package ServerClientShared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wsv759 on 19/02/15.
 */
public class Incident implements Serializable {

    /** The fields for this incident, including their content. Their content may be null. */
    private ArrayList<FieldWithContent> fieldList;
    private String channelName;
    private String ownerName;

    public Incident(){}
    
    public Incident(ArrayList<FieldWithContent> fieldList, String channel, String owner)
    {
    	this.fieldList = fieldList;
        if(channel==null)
            throw new RuntimeException("channel cannot be null.");
            
        channelName = channel;
        if(owner==null)
            throw new RuntimeException("owner cannot be null.");
        ownerName = owner;
    }

    public void setFieldList(ArrayList<FieldWithContent> fieldList)
    {
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
     * @precond channelName cannot be null
     * @param channelName the channelName to set
     */
    public void setChannelName(String channelName) {
        if(channelName==null)
            throw new RuntimeException("channel cannot be null.");
        this.channelName = channelName;
    }

    /**
     * @return the ownerName
     */
    public String getOwnerName() {
        return ownerName;
    }
}
