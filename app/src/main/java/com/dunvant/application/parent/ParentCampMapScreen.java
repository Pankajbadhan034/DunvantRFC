package com.dunvant.application.parent;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.dunvant.application.R;
import com.dunvant.application.beans.CampBean;

public class ParentCampMapScreen extends AppCompatActivity implements OnMapReadyCallback {

    Typeface helvetica;
    Typeface linoType;

    TextView title;
    ImageView backButton;

    GoogleMap mMap;
    CampBean clickedOnCamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_camp_map_screen);

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);

        title.setTypeface(linoType);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnCamp = (CampBean) intent.getSerializableExtra("clickedOnCamp");
        }

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

        LatLng position = new LatLng(Double.parseDouble(clickedOnCamp.getLocationList().get(0).getLatitude()), Double.parseDouble(clickedOnCamp.getLocationList().get(0).getLongitude()));

//        mMap.addMarker(new MarkerOptions()
//                .position(position)
//                .title(clickedOnCamp.getCampName()));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }
}
