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

    public Incident(){}
    
    public Incident(ArrayList<FieldWithContent> fieldList, String channel)
    {
    	this.fieldList = fieldList;
        channelName = channel;
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
     * @param channelName the channelName to set
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
