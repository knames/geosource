package ServerClientShared;

import java.io.Serializable;

/**
 * Created by wsv759 on 24/03/15.
 */
public class Geotag implements Serializable {
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

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

    private static final int NONEXISTENT_VAL = -1;

    private long timestamp = NONEXISTENT_VAL;
    private double longitude = NONEXISTENT_VAL, latitude = NONEXISTENT_VAL;

    public boolean exists()
    {
        return timestamp != NONEXISTENT_VAL && longitude != NONEXISTENT_VAL && latitude != NONEXISTENT_VAL;
    }
}
