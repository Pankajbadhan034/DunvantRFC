package com.dunvant.application.child;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.TrackObjectBean;
import com.dunvant.application.beans.TrackingHistoryBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildTrackingHistoryDetailScreen extends AppCompatActivity implements IWebServiceCallback, OnMapReadyCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    ImageView backButton;
    TextView dateTextView;
    TextView activityName;
    TextView durationTextView;
    TextView distanceTextView;
    TextView caloriesTextView;
    TextView speedTextView;
    RelativeLayout shareRelativeLayout;

    TrackingHistoryBean clickedOnHistory;

    private final String SHARE_WORKOUT = "SHARE_WORKOUT";
    private final String GET_WORKOUT_DETAILS = "GET_WORKOUT_DETAILS";

    GoogleMap mMap;
    ArrayList<TrackObjectBean> trackList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_tracking_history_detail_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        backButton = (ImageView) findViewById(R.id.backButton);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        activityName = (TextView) findViewById(R.id.activityName);
        durationTextView = (TextView) findViewById(R.id.durationTextView);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        caloriesTextView = (TextView) findViewById(R.id.caloriesTextView);
        speedTextView = (TextView) findViewById(R.id.speedTextView);
        shareRelativeLayout = (RelativeLayout) findViewById(R.id.shareRelativeLayout);

        Intent intent = getIntent();

        if(intent != null) {
            clickedOnHistory = (TrackingHistoryBean) intent.getSerializableExtra("clickedOnHistory");

            dateTextView.setText(clickedOnHistory.getShowCreatedAt());
            activityName.setText(clickedOnHistory.getActivity());
            durationTextView.setText(clickedOnHistory.getShowDuration());
            distanceTextView.setText(clickedOnHistory.getShowDistance());
            caloriesTextView.setText(clickedOnHistory.getShowCalories());
            speedTextView.setText(clickedOnHistory.getSpeed());

        }

        shareRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utilities.isNetworkAvailable(ChildTrackingHistoryDetailScreen.this)) {

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("workout_id", clickedOnHistory.getId()));

                    String webServiceUrl = Utilities.BASE_URL + "user_posts/share_track_workout";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildTrackingHistoryDetailScreen.this, nameValuePairList, SHARE_WORKOUT, ChildTrackingHistoryDetailScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(ChildTrackingHistoryDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getTrackDetails() {
        if(Utilities.isNetworkAvailable(ChildTrackingHistoryDetailScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("track_workout_id", clickedOnHistory.getId()));

            String webServiceUrl = Utilities.BASE_URL + "children/track_workout_locations";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildTrackingHistoryDetailScreen.this, nameValuePairList, GET_WORKOUT_DETAILS, ChildTrackingHistoryDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ChildTrackingHistoryDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case SHARE_WORKOUT:

                if(response == null) {
                    Toast.makeText(ChildTrackingHistoryDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        String message = responseObject.getString("message");

                        Toast.makeText(ChildTrackingHistoryDetailScreen.this, message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildTrackingHistoryDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case GET_WORKOUT_DETAILS:

                trackList.clear();

                if(response == null) {
                    Toast.makeText(ChildTrackingHistoryDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");

                            TrackObjectBean trackBean;

                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject trackObject = dataArray.getJSONObject(i);

                                trackBean = new TrackObjectBean();
                                trackBean.setId(trackObject.getString("id"));
                                trackBean.setTrackWorkoutId(trackObject.getString("track_workout_id"));
                                trackBean.setLatitude(trackObject.getString("latitude"));
                                trackBean.setLongitude(trackObject.getString("longitude"));
                                trackBean.setTimestamp(trackObject.getString("timestamp"));

                                trackList.add(trackBean);
                            }

                            updateTrack();

                        } else {
                            Toast.makeText(ChildTrackingHistoryDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildTrackingHistoryDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getTrackDetails();
    }

    private void updateTrack() {
        LatLng position = new LatLng(Double.parseDouble(trackList.get(0).getLatitude()), Double.parseDouble(trackList.get(0).getLongitude()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

//        PolylineOptions options = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
//
//        for(TrackObjectBean trackObjectBean: trackList) {
//            position = new LatLng(Double.parseDouble(trackObjectBean.getLatitude()), Double.parseDouble(trackObjectBean.getLongitude()));
//            options.add(position);
//        }
//
//        Polyline line = mMap.addPolyline(options);
    }
}