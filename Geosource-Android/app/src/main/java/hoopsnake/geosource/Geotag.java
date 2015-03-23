package hoopsnake.geosource;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

//TODO delete the various commented out custom implementations in this class, if the SmartLocation works.
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

//    private LocationManager locationManager;
//    private static final int TWO_MINUTES_IN_MILLIS = 120000;

    /**
     * @precond context is not null.
     * @postcond This geotag's location and timestamp are updated to their last known values.
     */
    public void update(final Context context) {
        timestamp = System.currentTimeMillis();
        SmartLocation.with(context).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {

                    @Override
                    public void onLocationUpdated(Location location) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        SmartLocation.with(context).location().stop();
                    }
                });
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

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeLong(timestamp);
        out.writeDouble(longitude);
        out.writeDouble(latitude);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        timestamp = in.readLong();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    private void readObjectNoData() throws ObjectStreamException
    {
        Log.e(LOG_TAG, "no data received on deserialize.");
    }

//    private class TaskUpdateLocation extends AsyncTask<Void, Void, Void> implements LocationListener
//    {
//        private Location prevLoc = null;
//        private Context context;
//        TaskUpdateLocation(Context context)
//        {
//            this.context = context;
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            timestamp = System.currentTimeMillis();
//
//            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//
//            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//                Criteria criteria = new Criteria();
//                criteria.setAccuracy(Criteria.ACCURACY_FINE);
//                String provider = locationManager.getBestProvider(criteria, true);
//                Location location = locationManager.getLastKnownLocation(provider);
//
//                if(location != null && location.getTime() > System.currentTimeMillis() - TWO_MINUTES_IN_MILLIS) {
//                    longitude = location.getLongitude();
//                    latitude = location.getLatitude();
//                }
//                else
//                    locationManager.requestLocationUpdates( 0, 0, criteria, this, Looper.myLooper());
//            }
//            else
//            {
//                String msg = context.getString(R.string.failed_to_get_location);
//                Log.e(LOG_TAG, msg);
//                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//            }
//            return null;
//        }
//
//        @Override
//        public void onLocationChanged(Location location) {
//            if (location != null) {
//                Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
//
//                locationManager.removeUpdates(this);
//
//                longitude = location.getLongitude();
//                latitude = location.getLatitude();
//            }
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {}
//
//        @Override
//        public void onProviderEnabled(String provider) {}
//
//        @Override
//        public void onProviderDisabled(String provider) {}
//    }
}
