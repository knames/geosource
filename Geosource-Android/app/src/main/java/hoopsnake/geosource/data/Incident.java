package hoopsnake.geosource.data;

import java.io.Serializable;
import java.util.ArrayList;

//TODO This class will be shared. remove and import instead.

/**
 * Created by wsv759 on 19/02/15.
 */
public class Incident implements Serializable {

    /** The fields for this incident, including their content. Their content may be null. */
    private ArrayList<FieldWithContent> fieldList;

    public Incident(){}

    public Incident(ArrayList<FieldWithContent> fieldList)
    {
        this.fieldList = fieldList;
    }

    public void setFieldList(ArrayList<FieldWithContent> fieldList)
    {
        this.fieldList = fieldList;
    }

    public ArrayList<FieldWithContent> getFieldList() {
        return fieldList;
    }
}

