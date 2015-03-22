package hoopsnake.geosource;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by wsv759 on 21/03/15.
 */
public class Geotag {
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

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

    private Long timestamp;
    private double longitude, latitude;

    /**
     * @precond activity is not null.
     * @postcond This geotag's location and timestamp are updated to their last known values.
     */
    public void update(Activity activity) {
        timestamp = System.currentTimeMillis();

        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }
}
