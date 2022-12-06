package com.dunvant.application.child;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

;

public class ChildLocationService extends Service implements LocationListener/*GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener*/ {

    /*GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;*/

    private static int TIME_INTERVAL = 1000 * 10;
    public LocationManager locationManager;
    private String provider;
    public static Location currentBestLocation;

//    static Location mLastLocation;
    static ArrayList<Location> mLocationsListing = new ArrayList<>();
    static ArrayList<String> timestampListing = new ArrayList<>();

    static String currentClass = "";
    static ChildTrackWorkoutScreen childTrackWorkoutScreen;
    static ChildTrackingMapViewScreen childTrackingMapViewScreen;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //System.out.println("Location Service on create");
        super.onCreate();
//        mLastLocation = null;
//        mLocationsListing.clear();
//        timestampListing.clear();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        //System.out.println("Location Service on Start");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initBestProvider();


        /*if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        mGoogleApiClient.connect();*/

    }

    private void initBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        provider = locationManager.getBestProvider(criteria, true);

        if (provider == null) {
            provider = LocationManager.GPS_PROVIDER;
            if (provider == null){

                //System.out.println("GPS also not available");
            }
        }else{
            //System.out.println("Got provider "+provider);
        }

//        locationManager.requestLocationUpdates(provider, 60000, 5, this);
        locationManager.requestLocationUpdates(provider, 10000, 10, this);
        if (currentBestLocation == null) {
            //System.out.println("Current location is null");
            Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (gpsLocation != null) {
                //System.out.println("Got first location "+gpsLocation.getLatitude()+"  "+gpsLocation.getLongitude());

                currentBestLocation = gpsLocation;
                mLocationsListing.add(currentBestLocation);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                timestampListing.add(sdf.format(new Date()));

            }else{
                //System.out.println("First Location is null");
            }

        }

    }

    /*@Override
    public void onConnected(@Nullable Bundle bundle) {
        //System.out.println("Location Service :: On Connected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null) {
            //System.out.println("On Connected "+mLastLocation.getLatitude()+" "+mLastLocation.getLongitude());
            mLocationsListing.add(mLastLocation);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            timestampListing.add(sdf.format(new Date()));
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }*/

    @Override
    public void onLocationChanged(Location location) {
        //System.out.println("On Location Changed "+location.getLatitude()+" "+location.getLongitude());

        if(location == null){
            return;
        }
        if(isBetterLocation(location, currentBestLocation)){
            currentBestLocation = location;

            mLocationsListing.add(currentBestLocation);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            timestampListing.add(sdf.format(new Date()));

            switch (currentClass) {
                case "workout":
                    childTrackWorkoutScreen.doCalculations();
                    break;
                case "map":
                    childTrackingMapViewScreen.updateMapMarkers();
                    break;
            }
        }



        /*//System.out.println("Difference is "+location.distanceTo(mLastLocation));

        if(location.distanceTo(mLastLocation) >= 10) {
            mLastLocation = location;

            if(mLastLocation != null) {
                //System.out.println("on Location changed :: "+mLastLocation.getLatitude()+" "+mLastLocation.getLongitude());
                mLocationsListing.add(mLastLocation);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                timestampListing.add(sdf.format(new Date()));

                switch (currentClass) {
                    case "workout":
                        childTrackWorkoutScreen.doCalculations();
                        break;
                    case "map":
                        childTrackingMapViewScreen.updateMapMarkers();
                        break;
                }


            } else {
                //System.out.println("on Location changed :: location is null");
            }
        } else {
            //System.out.println("Difference is less than 2");
        }*/
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {



        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }
        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TIME_INTERVAL;
        boolean isSignificantlyOlder = timeDelta < -TIME_INTERVAL;
        boolean isNewer = timeDelta > 0;

        // If it's been more than desired time interval since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be
            // worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onDestroy() {

        //System.out.println("Location service on destroy");

        super.onDestroy();
//        mLastLocation = null;
        mLocationsListing.clear();
        timestampListing.clear();
        locationManager.removeUpdates(this);
        currentBestLocation = null;
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
}