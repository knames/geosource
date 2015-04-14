package ServerClientShared;

import java.io.Serializable;

/**
 * Created by wsv759 on 24/03/15.
 */
public class Geotag implements Serializable {
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;
    
    //error value for TimeStamp
    private static final int NONEXISTENT_VAL = -1;

    //A long representing a timeStamp when this geotag was taken.
    private long timestamp = NONEXISTENT_VAL;
    
    //A double representing the longitude of where this geotag is taken.
    private Double longitude;
            
    //A double representing the latitude of where this geotag is taken.
    private Double latitude;

    /**
     * Checks to see if Geotag is valid.
     * @return true if the geotag is valid, false otherwise.
     */
    public boolean exists()
    {
        return timestamp != NONEXISTENT_VAL && longitude != null && latitude != null;
    }
    
    //Getters and Setters
    public long getTimestamp() {
        return timestamp;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }


    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


}
