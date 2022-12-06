package com.dunvant.application.child;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
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
import java.util.Timer;
import java.util.TimerTask;

public class ChildTrackWorkoutScreen extends AppCompatActivity implements IWebServiceCallback{
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    ImageView backButton;
    Button history;
    Button mapView;
    ImageView startTimer;
    ImageView pauseTimer;
    ImageView stopTimer;
    TextView time;
    Spinner chooseActivitySpinner;
    TextView distanceTextView;
    TextView speedTextView;
    TextView caloriesTextView;

    Timer timer;
    int seconds = 0;
    int minutes = 0;
    int hours = 0;
    int secondsForCalculation = 0;
    String showTime;

    double distance = 0;
    double speed = 0;
    double calories = 0;

    private final String SAVE_TRACK = "SAVE_TRACK";

    // Run time permission
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[] {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_track_workout_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        backButton = (ImageView) findViewById(R.id.backButton);
        history = (Button) findViewById(R.id.history);
        mapView = (Button) findViewById(R.id.mapView);
        startTimer = (ImageView) findViewById(R.id.startTimer);
        pauseTimer = (ImageView) findViewById(R.id.pauseTimer);
        stopTimer = (ImageView) findViewById(R.id.stopTimer);
        time = (TextView) findViewById(R.id.time);
        chooseActivitySpinner = (Spinner) findViewById(R.id.chooseActivitySpinner);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        speedTextView = (TextView) findViewById(R.id.speedTextView);
        caloriesTextView = (TextView) findViewById(R.id.caloriesTextView);

        timer = new Timer();

        ArrayList<String> activityTypes = new ArrayList<>();
        activityTypes.add("Select Activity");
        activityTypes.add("Cycling");
        activityTypes.add("Walking");
        activityTypes.add("Running");
        activityTypes.add("Jogging");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activityTypes);
        chooseActivitySpinner.setAdapter(spinnerArrayAdapter);

        chooseActivitySpinner.setEnabled(true);
        startTimer.setEnabled(true);
        startTimer.setAlpha(1f);
        pauseTimer.setEnabled(false);
        pauseTimer.setAlpha(0.5f);
        stopTimer.setEnabled(false);
        stopTimer.setAlpha(0.5f);

        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start location service

                if(chooseActivitySpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(ChildTrackWorkoutScreen.this, "Please select activity", Toast.LENGTH_SHORT).show();
                } else {
                    //System.out.println("Start Timer :: starting service");

                    chooseActivitySpinner.setEnabled(false);
                    startTimer.setEnabled(false);
                    startTimer.setAlpha(0.5f);
                    pauseTimer.setEnabled(true);
                    pauseTimer.setAlpha(1f);
                    stopTimer.setEnabled(true);
                    stopTimer.setAlpha(1f);

                    Intent locationService = new Intent(ChildTrackWorkoutScreen.this, ChildLocationService.class);
                    startService(locationService);

                    if(timer == null) {
                        timer = new Timer();
                    }

                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    secondsForCalculation++;

                                    seconds++;
                                    if(seconds == 60) {
                                        minutes++;
                                        seconds = 0;
                                    }

                                    if(minutes == 60) {
                                        hours++;
                                        minutes = 0;
                                    }

                                    String strSeconds = (seconds < 10)? "0"+seconds:seconds+"";
                                    String strMinutes = (minutes < 10)? "0"+minutes:minutes+"";

                                    showTime = hours+":"+strMinutes+":"+strSeconds;
                                    time.setText(showTime);
                                }
                            });
                        }
                    }, 1000, 1000);
                }
            }
        });

        pauseTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Pause Timer");
                if(timer != null) {
                    timer.cancel();
                    timer = null;
                }

                chooseActivitySpinner.setEnabled(false);
                startTimer.setEnabled(true);
                startTimer.setAlpha(1f);
                pauseTimer.setEnabled(false);
                pauseTimer.setAlpha(0.5f);
                stopTimer.setEnabled(true);
                stopTimer.setAlpha(1f);

            }
        });

        stopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Stop Timer :: stopping service");
                Intent locationService = new Intent(ChildTrackWorkoutScreen.this, ChildLocationService.class);
                stopService(locationService);

                if(timer != null) {
                    timer.cancel();
                    timer = null;
                }

                hours = 0;
                minutes = 0;
                seconds = 0;

                chooseActivitySpinner.setEnabled(true);
                startTimer.setEnabled(true);
                startTimer.setAlpha(1f);
                pauseTimer.setEnabled(false);
                pauseTimer.setAlpha(0.5f);
                stopTimer.setEnabled(false);
                stopTimer.setAlpha(0.5f);

                saveTrackingData();
            }
        });

        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (ChildLocationService.mLocationsListing == null || ChildLocationService.mLocationsListing.isEmpty()) {
                    Toast.makeText(ChildTrackWorkoutScreen.this, "Please start Tracking first", Toast.LENGTH_SHORT).show();
                } else {
                    Intent mapView = new Intent(ChildTrackWorkoutScreen.this, ChildTrackingMapViewScreen.class);
                    startActivity(mapView);
                }*/

                Intent mapView = new Intent(ChildTrackWorkoutScreen.this, ChildTrackingMapViewScreen.class);
                startActivity(mapView);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyListing = new Intent(ChildTrackWorkoutScreen.this, ChildTrackingHistoryScreen.class);
                startActivity(historyListing);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitScreen();
            }
        });
    }

    private void exitScreen(){
        if(secondsForCalculation == 0) {
            finish();
        } else {
            final Dialog dialog = new Dialog(ChildTrackWorkoutScreen.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.child_dialog_exit_workout);

            TextView text1 = (TextView) dialog.findViewById(R.id.text1);
            TextView yes = (TextView) dialog.findViewById(R.id.yes);
            TextView no = (TextView) dialog.findViewById(R.id.no);

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });

            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        exitScreen();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissions()){
//                Toast.makeText(ChildTrackWorkoutScreen.this, "Permissions already granted", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(ChildTrackWorkoutScreen.this, "Initially permission not available", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(ChildTrackWorkoutScreen.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permissions granted.
                } else {
                    // no permissions granted.
                    Toast.makeText(ChildTrackWorkoutScreen.this, "This feature cannot work without Permissions. \nTry Again and allow permissions or allow permissions in Applications Settings", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }

    public void saveTrackingData() {
        if(Utilities.isNetworkAvailable(ChildTrackWorkoutScreen.this)) {

            JSONArray dataArray = new JSONArray();

            for(int i=0;i<ChildLocationService.mLocationsListing.size();i++) {
                Location location = ChildLocationService.mLocationsListing.get(i);
                String timestamp = ChildLocationService.timestampListing.get(i);

                JSONObject object = new JSONObject();
                try {
                    object.put("latitude", location.getLatitude()+"");
                    object.put("longitude", location.getLongitude()+"");
                    object.put("timestamp", timestamp);

                    dataArray.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("duration", showTime);
                jsonObject.put("distance", distance+"");
                jsonObject.put("speed", speed+"");
                jsonObject.put("steps", "");
                jsonObject.put("calories", calories+"");
                jsonObject.put("activity", chooseActivitySpinner.getSelectedItem().toString());
                jsonObject.put("sport_type", "");
                jsonObject.put("created_at", "");
                jsonObject.put("synced_by", "0");
                jsonObject.put("track_locations", dataArray.toString());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);

                nameValuePairList.add(new BasicNameValuePair("segment_data", jsonArray.toString()));

                String webServiceUrl = Utilities.BASE_URL + "children/save_track_workout";

                ArrayList<String> headers = new ArrayList<>();
                headers.add("X-access-uid:"+loggedInUser.getId());
                headers.add("X-access-token:" + loggedInUser.getToken());

                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildTrackWorkoutScreen.this, nameValuePairList, SAVE_TRACK, ChildTrackWorkoutScreen.this, headers);
                postWebServiceAsync.execute(webServiceUrl);

            }catch (Exception e){
                e.printStackTrace();
            }


//            nameValuePairList.add(new BasicNameValuePair("segment_data", jsonArray.toString()));
//
//            nameValuePairList.add(new BasicNameValuePair("duration", showTime));
//            nameValuePairList.add(new BasicNameValuePair("distance", distance+""));
//            nameValuePairList.add(new BasicNameValuePair("speed", speed+""));
//            nameValuePairList.add(new BasicNameValuePair("steps", ""));
//            nameValuePairList.add(new BasicNameValuePair("calories", calories+""));
//            nameValuePairList.add(new BasicNameValuePair("activity", chooseActivitySpinner.getSelectedItem().toString()));
//            nameValuePairList.add(new BasicNameValuePair("sport_type", ""));
//            nameValuePairList.add(new BasicNameValuePair("created_at", ""));
//            nameValuePairList.add(new BasicNameValuePair("synced_by", "0"));
//            nameValuePairList.add(new BasicNameValuePair("track_locations", dataArray.toString()));
//
//            String webServiceUrl = Utilities.BASE_URL + "children/save_track_workout";
//
//            ArrayList<String> headers = new ArrayList<>();
//            headers.add("X-access-uid:"+loggedInUser.getId());
//            headers.add("X-access-token:"+loggedInUser.getToken());
//
//            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildTrackWorkoutScreen.this, nameValuePairList, SAVE_TRACK, ChildTrackWorkoutScreen.this, headers);
//            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ChildTrackWorkoutScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    public void doCalculations() {

        // Calculate distance Covered
        distance = 0;
        speed = 0;
        calories = 0;

        if(ChildLocationService.mLocationsListing.size() >= 2) {
            for(int i=0; i<(ChildLocationService.mLocationsListing.size() - 1); i++) {

                Location currentPoint = ChildLocationService.mLocationsListing.get(i);
                Location nextPoint = ChildLocationService.mLocationsListing.get(i+1);

                distance += currentPoint.distanceTo(nextPoint);
            }
        }

        speed = distance / secondsForCalculation;

        calories = 0.75 * Double.parseDouble(loggedInUser.getWeightNumeric()) * distance * 0.000621371;

        distance = (double) Math.round(distance * 100) / 100;
        speed = (double) Math.round(speed * 100) / 100;
        calories = (double) Math.round(calories * 100) / 100;

        distanceTextView.setText(distance+" m");
        speedTextView.setText(speed+" m/s");
        caloriesTextView.setText(calories+"");
    }

    @Override
    protected void onResume() {
        super.onResume();

        doCalculations();

        ChildLocationService.currentClass = "workout";
        ChildLocationService.childTrackWorkoutScreen = ChildTrackWorkoutScreen.this;
    }

    @Override
    protected void onDestroy() {
        //System.out.println("Track Workout on Destroy");
        super.onDestroy();
        Intent locationService = new Intent(ChildTrackWorkoutScreen.this, ChildLocationService.class);
        stopService(locationService);
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case SAVE_TRACK:
                if(response == null) {
                    Toast.makeText(ChildTrackWorkoutScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        /*if(status) {
                            finish();
                        }*/

                        Intent locationService = new Intent(ChildTrackWorkoutScreen.this, ChildLocationService.class);
                        stopService(locationService);
                        seconds = 0;
                        minutes = 0;
                        hours = 0;
                        secondsForCalculation = 0;
                        distance = 0;
                        speed = 0;
                        calories = 0;

                        String strSeconds = (seconds < 10)? "0"+seconds:seconds+"";
                        String strMinutes = (minutes < 10)? "0"+minutes:minutes+"";

                        showTime = hours+":"+strMinutes+":"+strSeconds;
                        time.setText(showTime);

                        distanceTextView.setText(distance+" m");
                        speedTextView.setText(speed+" m/s");
                        caloriesTextView.setText(calories+"");

                        Toast.makeText(ChildTrackWorkoutScreen.this, message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildTrackWorkoutScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}