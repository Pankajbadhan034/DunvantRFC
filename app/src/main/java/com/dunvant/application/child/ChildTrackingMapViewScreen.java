package com.dunvant.application.child;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.dunvant.application.R;

public class ChildTrackingMapViewScreen extends AppCompatActivity implements OnMapReadyCallback {

    ImageView backButton;

    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_tracking_map_view_screen);

        backButton = (ImageView) findViewById(R.id.backButton);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ChildLocationService.currentClass = "map";
        ChildLocationService.childTrackingMapViewScreen = ChildTrackingMapViewScreen.this;

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        if(ChildLocationService.mLocationsListing != null && !ChildLocationService.mLocationsListing.isEmpty()) {
//            LatLng position = new LatLng(ChildLocationService.mLocationsListing.get(0).getLatitude(), ChildLocationService.mLocationsListing.get(0).getLongitude());
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
//
//            PolylineOptions options = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
//
//            for(Location location : ChildLocationService.mLocationsListing) {
//                position = new LatLng(location.getLatitude(), location.getLongitude());
//                options.add(position);
//            }
//
//            LatLng lastPosition = new LatLng(ChildLocationService.mLocationsListing.get(ChildLocationService.mLocationsListing.size() -1).getLatitude(), ChildLocationService.mLocationsListing.get(ChildLocationService.mLocationsListing.size() -1).getLongitude());
//            mMap.addMarker(new MarkerOptions()
//                    .position(lastPosition)
//                    .title(""));
//            Polyline line = mMap.addPolyline(options);
//        }
    }

    public void updateMapMarkers(){
        if(mMap != null) {

            mMap.clear();

//            PolylineOptions options = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
//
//            for(Location location : ChildLocationService.mLocationsListing) {
//                LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
//
//
//                options.add(position);
//            }
//
//            LatLng lastPosition = new LatLng(ChildLocationService.mLocationsListing.get(ChildLocationService.mLocationsListing.size() -1).getLatitude(), ChildLocationService.mLocationsListing.get(ChildLocationService.mLocationsListing.size() -1).getLongitude());
//            mMap.addMarker(new MarkerOptions()
//                    .position(lastPosition)
//                    .title(""));
//            Polyline line = mMap.addPolyline(options);

        }
    }
}