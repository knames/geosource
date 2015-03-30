package hoopsnake.geosource;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import ServerClientShared.Geotag;
import hoopsnake.geosource.data.AppGeotagField;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesWithFallbackProvider;

import static junit.framework.Assert.assertNotNull;

//TODO delete the various commented out custom implementations in this class, if the SmartLocation works.
/**
 * Created by wsv759 on 21/03/15.
 */
public class AppGeotagWrapper {
    //change this if and only if a new implementation is incompatible with an old one
    private static final long serialVersionUID = 1L;

    private static final String LOG_TAG = "geosource";

    private Geotag wrappedGeotag;

//    private LocationManager locationManager;
//    private static final int TWO_MINUTES_IN_MILLIS = 120000;

    public void setRegisteredField(AppGeotagField registeredField) {
        this.registeredField = registeredField;
    }

    private AppGeotagField registeredField = null;

    public AppGeotagWrapper()
    {
        wrappedGeotag =  new Geotag();
    }
    /**
     * @precond context is not null.
     * @postcond This geotag's location and timestamp are updated to their last known values.
     */
    public void update(final Context context) {
        assertNotNull(context);
        wrappedGeotag.setTimestamp(System.currentTimeMillis());

        //TODO delete this stuff.
//        LocationProvider provider;
//        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
//        if(status == ConnectionResult.SUCCESS) {
//            provider = new Go
//        }
        Log.v(LOG_TAG, "attempting to determine device's location.");
        SmartLocation.with(context).location()
                .oneFix()
                //reverts to locationManager if no google play services available.
                .provider(new LocationGooglePlayServicesWithFallbackProvider(context))
                .start(new OnLocationUpdatedListener() {

                    @Override
                    public void onLocationUpdated(Location location) {
                        wrappedGeotag.setLatitude(location.getLatitude());
                        wrappedGeotag.setLongitude(location.getLongitude());

                        String logMsg = "Geotag has been set";
                        if (registeredField != null) {
                            registeredField.onContentUpdated();
                            logMsg += " for registered field.";
                        }

                        Log.v(LOG_TAG, logMsg);
                    }
                });
    }

    @Override
    public String toString()
    {
        return wrappedGeotag.toString();
    }

    public boolean exists()
    {
        return wrappedGeotag.exists();
    }

    public Geotag toGeotag()
    {
        return wrappedGeotag;
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
