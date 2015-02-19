package hoopsnake.geosource.data;

import java.io.Serializable;
import java.security.InvalidParameterException;

import hoopsnake.geosource.media.SerialBitmap;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by wsv759 on 18/02/15.
 */
public class FieldTypeManagement {

    /**
     * Created by wsv759 on 18/02/15.This enumerates the types of objects (or files) that a field
     * could consist of.
     */
    public static enum FieldType {
        IMAGE,
        STRING,
        VIDEO,
        SOUND
    }

    public static boolean contentMatchesFieldType(Serializable content, FieldType type)
    {
        assertNotNull(type);
        assertNotNull(content);

        switch (type)
        {
            case IMAGE:
                return (content instanceof SerialBitmap);
            case VIDEO:
                throw new RuntimeException("Video object type not yet implemented, sorry!");
            case STRING:
                return (content instanceof String);
            case SOUND:
                throw new RuntimeException("Sound object type not yet implemented, sorry!");
            default:
                throw new InvalidParameterException("field type is invalid.");
        }
    }
}
