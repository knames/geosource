package hoopsnake.geosource;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

/**
 * Created by wsv759 on 21/03/15.
 */
public class Geotag implements Serializable {
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    private static final String LOG_TAG = "geosource";

    public Long getTimestamp() {
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

    private static final int NONEXISTENT_VAL = -1;
    private long timestamp = NONEXISTENT_VAL;
    private double longitude = NONEXISTENT_VAL, latitude = NONEXISTENT_VAL;

    /**
     * @precond context is not null.
     * @postcond This geotag's location and timestamp are updated to their last known values.
     */
    public void update(Context context) {
        timestamp = System.currentTimeMillis();

        LocationManager lm = null;
        boolean gps_enabled = false,network_enabled = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try{
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){}
        try{
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){}

        if (gps_enabled || network_enabled) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        else
        {
            String msg = context.getString(R.string.failed_to_get_location);
            Log.e(LOG_TAG, msg);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public String toString()
    {
        return "Geotag:\ntime in millis: " + timestamp + "\nlongitude: " + longitude + "\nlatitude: " + latitude;
    }

    public boolean exists()
    {
        return timestamp != NONEXISTENT_VAL && longitude != NONEXISTENT_VAL && latitude != NONEXISTENT_VAL;
    }
}
