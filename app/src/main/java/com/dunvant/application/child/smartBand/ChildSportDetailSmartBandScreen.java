package com.dunvant.application.child.smartBand;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildMainScreen;
import com.dunvant.application.child.smartBand.bean.ChildSmartBandLevelsBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildSportDetailSmartBandScreen extends AppCompatActivity implements IWebServiceCallback {
    TextView firstCal;
    TextView secondCal;
    TextView thirdCal;
    TextView fourthCal;
    TextView fifthCal;

    TextView firstSteps;
    TextView secondSteps;
    TextView thirdSteps;
    TextView fourthSteps;
    TextView fifthSteps;

    TextView firstDis;
    TextView secondDis;
    TextView thirdDis;
    TextView fourthDis;
    TextView fifthDis;

    Button shareWithFriend;
    LinearLayout steps1;
    LinearLayout steps2;
    LinearLayout steps3;
    LinearLayout steps4;
    LinearLayout steps5;

    LinearLayout distance1;
    LinearLayout distance2;
    LinearLayout distance3;
    LinearLayout distance4;
    LinearLayout distance5;

    LinearLayout cal1;
    LinearLayout cal2;
    LinearLayout cal3;
    LinearLayout cal4;
    LinearLayout cal5;

    String type;
    String startTime;
    String endTime;
    double calories;
    double distance;
    String duration;
    int steps;
    String sportType;
    String date;
    TextView caloriesText;
    TextView stepsText;
    TextView distancetext;
    TextView sportName;
    ImageView sportImage;
    TextView startTimeText;
    TextView endTimeText;
    TextView durationText;
    ImageView backButton;
    TextView dateText;
    ArrayList<ChildSmartBandLevelsBean>distanceArrayList = new ArrayList<>();
    ArrayList<ChildSmartBandLevelsBean>caloriesArrayList = new ArrayList<>();
    ArrayList<ChildSmartBandLevelsBean>stepsArrayList = new ArrayList<>();
    ArrayList<ChildSmartBandLevelsBean>heartRateArrayList = new ArrayList<>();

    Typeface helvetica;
    Typeface linoType;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    private final String  GET_LEVELS = "GET_LEVELS";
    private final String SHARE_WITH_FRIENDS = "SHARE_WITH_FRIENDS";
    String workoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_sport_detail_smart_band_screen);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        caloriesText = (TextView) findViewById(R.id.caloriesText);
        stepsText = (TextView) findViewById(R.id.stepsText);
        distancetext = (TextView) findViewById(R.id.distancetext);
        sportName = (TextView) findViewById(R.id.sportName);
        sportImage = (ImageView) findViewById(R.id.sportImage);
        startTimeText = (TextView) findViewById(R.id.startTimeText);
        endTimeText = (TextView) findViewById(R.id.endTimeText);
        durationText = (TextView) findViewById(R.id.durationText);
        backButton = (ImageView) findViewById(R.id.backButton);
        dateText = (TextView) findViewById(R.id.dateText);
        steps1 = (LinearLayout) findViewById(R.id.steps1);
        steps2 = (LinearLayout) findViewById(R.id.steps2);
        steps3 = (LinearLayout) findViewById(R.id.steps3);
        steps4 = (LinearLayout) findViewById(R.id.steps4);
        steps5 = (LinearLayout) findViewById(R.id.steps5);
        distance1 = (LinearLayout) findViewById(R.id.distance1);
        distance2 = (LinearLayout) findViewById(R.id.distance2);
        distance3 = (LinearLayout) findViewById(R.id.distance3);
        distance4 = (LinearLayout) findViewById(R.id.distance4);
        distance5 = (LinearLayout) findViewById(R.id.distance5);
        cal1 = (LinearLayout) findViewById(R.id.cal1);
        cal2 = (LinearLayout) findViewById(R.id.cal2);
        cal3 = (LinearLayout) findViewById(R.id.cal3);
        cal4 = (LinearLayout) findViewById(R.id.cal4);
        cal5 = (LinearLayout) findViewById(R.id.cal5);
        shareWithFriend = (Button) findViewById(R.id.shareWithFriend);

        firstCal = findViewById(R.id.firstCal);
        secondCal = findViewById(R.id.secondCal);
        thirdCal = findViewById(R.id.thirdCal);
        fourthCal = findViewById(R.id.fourthCal);
        fifthCal = findViewById(R.id.fifthCal);

        firstSteps = findViewById(R.id.firstSteps);
        secondSteps = findViewById(R.id.secondSteps);
        thirdSteps = findViewById(R.id.thirdSteps);
        fourthSteps = findViewById(R.id.fourthSteps);
        fifthSteps = findViewById(R.id.fifthSteps);

        firstDis = findViewById(R.id.firstDis);
        secondDis = findViewById(R.id.secondDis);
        thirdDis = findViewById(R.id.thirdDis);
        fourthDis = findViewById(R.id.fourthDis);
        fifthDis = findViewById(R.id.fifthDis);

        workoutId = getIntent().getStringExtra("workoutId");

        shareWithFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utilities.isNetworkAvailable(ChildSportDetailSmartBandScreen.this)) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    String webServiceUrl = Utilities.BASE_URL + "user_posts/share_track_workout";
                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    if(type.equalsIgnoreCase("nonifa")){
                        nameValuePairList.add(new BasicNameValuePair("workout_id", workoutId));
                    }else{
                        nameValuePairList.add(new BasicNameValuePair("session_date", date));
                        nameValuePairList.add(new BasicNameValuePair("session_end_time", endTime));
                        nameValuePairList.add(new BasicNameValuePair("session_start_time", startTime));
                    }

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildSportDetailSmartBandScreen.this, nameValuePairList, SHARE_WITH_FRIENDS, ChildSportDetailSmartBandScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(ChildSportDetailSmartBandScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (Utilities.isNetworkAvailable(ChildSportDetailSmartBandScreen.this)) {
            String webServiceUrl = Utilities.BASE_URL + "children/get_level";
            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            if(Utilities.isNetworkAvailable(ChildSportDetailSmartBandScreen.this)) {
                GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ChildSportDetailSmartBandScreen.this, GET_LEVELS, ChildSportDetailSmartBandScreen.this, headers);
                getWebServiceWithHeadersAsync.execute(webServiceUrl);
            }else{
                Toast.makeText(ChildSportDetailSmartBandScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ChildSportDetailSmartBandScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        duration = getIntent().getStringExtra("duration");
        type = getIntent().getStringExtra("type");

        String caloriesToInt = getIntent().getStringExtra("calories");
        caloriesToInt = caloriesToInt.replace("kcal", "");
        caloriesToInt = caloriesToInt.replace("cal", "");
        calories = Double.parseDouble(caloriesToInt);

        String distanceToInt;
        distanceToInt = getIntent().getStringExtra("distance");
        distanceToInt = distanceToInt.replace("km", "");
//        System.out.println("distance::"+distanceToInt);
        distance = Double.parseDouble(distanceToInt);


        steps = Integer.parseInt(getIntent().getStringExtra("steps"));
        sportType = getIntent().getStringExtra("sportType");
        date = getIntent().getStringExtra("date");

        startTimeText.setText(startTime);
        endTimeText.setText(endTime);
        caloriesText.setText(""+calories);
        distancetext.setText(""+distance);
        durationText.setText(duration);
        stepsText.setText(""+steps);
        dateText.setText(date);

        if(type.equalsIgnoreCase("nonifa")){
            //type of sport set into image
            if(sportType.equalsIgnoreCase("1")){
                // walking
//                System.out.println("inWalking");
                sportName.setText("Walking");
                sportImage.setBackgroundResource(R.drawable.stepround);
            }else if(sportType.equalsIgnoreCase("7")){
                //running
//                System.out.println("inRunning");
                sportName.setText("Running");
                sportImage.setBackgroundResource(R.drawable.running);
            }else if(sportType.equalsIgnoreCase("130")){
                //football
//                System.out.println("inFootball");
                sportName.setText("Football");
                sportImage.setBackgroundResource(R.drawable.football);
            }else if(sportType.equalsIgnoreCase("136")) {
                //cycle
//                System.out.println("inCycling");
                sportName.setText("Cycling");
                sportImage.setBackgroundResource(R.drawable.cycling);
            }else{
//                System.out.println("HERE_ELSE_PART");
            }
        }else{
            sportName.setText("Football");
            sportImage.setBackgroundResource(R.drawable.football);
        }




    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case SHARE_WITH_FRIENDS:
                if (response == null) {
                    Toast.makeText(ChildSportDetailSmartBandScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("state");
                        String message = responseObject.getString("message");
                        if (status) {
                            Toast.makeText(ChildSportDetailSmartBandScreen.this, message, Toast.LENGTH_SHORT).show();
//                            ((ChildMainScreen) getApplicationContext()).showChildPost();
                            Intent mainScreen = new Intent(ChildSportDetailSmartBandScreen.this, ChildMainScreen.class);
                            mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainScreen);
                            finish();
                        }else {
                            Toast.makeText(ChildSportDetailSmartBandScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(ChildSportDetailSmartBandScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case GET_LEVELS:
                if (response == null) {
                    Toast.makeText(ChildSportDetailSmartBandScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if (status) {
                            JSONObject data = new JSONObject(responseObject.getString("data"));

                            // for distance data
                            JSONArray distanceArray = data.getJSONArray("Distance");
                            for (int i = 0; i < distanceArray.length(); i++) {
                                JSONObject jsonObject = distanceArray.getJSONObject(i);
                                ChildSmartBandLevelsBean childSmartBandLevelsBean = new ChildSmartBandLevelsBean();
                                childSmartBandLevelsBean.setId(jsonObject.getString("id"));
                                childSmartBandLevelsBean.setName(jsonObject.getString("name"));
                                childSmartBandLevelsBean.setStartVal(jsonObject.getInt("start_val"));
                                childSmartBandLevelsBean.setEndVal(jsonObject.getInt("end_val"));
                                childSmartBandLevelsBean.setLevelType(jsonObject.getInt("level_type"));
                                distanceArrayList.add(childSmartBandLevelsBean);

                                if(i==0){
                                    firstDis.setText(jsonObject.getString("name"));
                                }else if(i==1){
                                    secondDis.setText(jsonObject.getString("name"));
                                }else if(i==2){
                                    thirdDis.setText(jsonObject.getString("name"));
                                }else if(i==3){
                                    fourthDis.setText(jsonObject.getString("name"));
                                }else if(i==4){
                                    fifthDis.setText(jsonObject.getString("name"));
                                }
                            }

                            // for calories data
                            JSONArray caloriesArray = data.getJSONArray("Calories");
                            for (int i = 0; i < caloriesArray.length(); i++) {
                                JSONObject jsonObject = caloriesArray.getJSONObject(i);
                                ChildSmartBandLevelsBean childSmartBandLevelsBean = new ChildSmartBandLevelsBean();
                                childSmartBandLevelsBean.setId(jsonObject.getString("id"));
                                childSmartBandLevelsBean.setName(jsonObject.getString("name"));
                                childSmartBandLevelsBean.setStartVal(jsonObject.getInt("start_val"));
                                childSmartBandLevelsBean.setEndVal(jsonObject.getInt("end_val"));
                                childSmartBandLevelsBean.setLevelType(jsonObject.getInt("level_type"));
                                caloriesArrayList.add(childSmartBandLevelsBean);

                                if(i==0){
                                    firstCal.setText(jsonObject.getString("name"));
                                }else if(i==1){
                                    secondCal.setText(jsonObject.getString("name"));
                                }else if(i==2){
                                    thirdCal.setText(jsonObject.getString("name"));
                                }else if(i==3){
                                    fourthCal.setText(jsonObject.getString("name"));
                                }else if(i==4){
                                    fifthCal.setText(jsonObject.getString("name"));
                                }
                            }

                            // for steps data
                            JSONArray stepsArray = data.getJSONArray("Steps");
                            for (int i = 0; i < stepsArray.length(); i++) {
                                JSONObject jsonObject = stepsArray.getJSONObject(i);
                                ChildSmartBandLevelsBean childSmartBandLevelsBean = new ChildSmartBandLevelsBean();
                                childSmartBandLevelsBean.setId(jsonObject.getString("id"));
                                childSmartBandLevelsBean.setName(jsonObject.getString("name"));
                                childSmartBandLevelsBean.setStartVal(jsonObject.getInt("start_val"));
                                childSmartBandLevelsBean.setEndVal(jsonObject.getInt("end_val"));
                                childSmartBandLevelsBean.setLevelType(jsonObject.getInt("level_type"));
                                stepsArrayList.add(childSmartBandLevelsBean);

                                if(i==0){
                                    firstSteps.setText(jsonObject.getString("name"));
                                }else if(i==1){
                                    secondSteps.setText(jsonObject.getString("name"));
                                }else if(i==2){
                                    thirdSteps.setText(jsonObject.getString("name"));
                                }else if(i==3){
                                    fourthSteps.setText(jsonObject.getString("name"));
                                }else if(i==4){
                                    fifthSteps.setText(jsonObject.getString("name"));
                                }
                            }

                            // for Heart rate Data
                            JSONArray heartRateArray = data.getJSONArray("Heart Rate");
                            for (int i = 0; i < heartRateArray.length(); i++) {
                                JSONObject jsonObject = heartRateArray.getJSONObject(i);
                                ChildSmartBandLevelsBean childSmartBandLevelsBean = new ChildSmartBandLevelsBean();
                                childSmartBandLevelsBean.setId(jsonObject.getString("id"));
                                childSmartBandLevelsBean.setName(jsonObject.getString("name"));
                                childSmartBandLevelsBean.setStartVal(jsonObject.getInt("start_val"));
                                childSmartBandLevelsBean.setEndVal(jsonObject.getInt("end_val"));
                                childSmartBandLevelsBean.setLevelType(jsonObject.getInt("level_type"));
                                heartRateArrayList.add(childSmartBandLevelsBean);

                            }

                            // set progress bar for steps
                            stepsProgress();
                            // set progress bar for distance
                            distanceProgress();
                            //set progress bar for calories
                            caloriesProgress();
                        } else {
                            Toast.makeText(ChildSportDetailSmartBandScreen.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildSportDetailSmartBandScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    //calories method
    public void caloriesProgress(){
        for(int i=0; i<caloriesArrayList.size();i++){
            final ChildSmartBandLevelsBean childSmartBandLevelsBean = caloriesArrayList.get(i);
            if(i==0){
                if(calories>childSmartBandLevelsBean.getStartVal() && calories<childSmartBandLevelsBean.getEndVal()){
                    cal1.setBackgroundResource(R.drawable.calories_box_detail);
                }
            }else if(i==1){
                if(calories>childSmartBandLevelsBean.getStartVal() && calories<childSmartBandLevelsBean.getEndVal()){
                    cal1.setBackgroundResource(R.drawable.calories_box_detail);
                    cal2.setBackgroundResource(R.drawable.calories_box_detail);
                }
            }else if(i==2){
                if(calories>childSmartBandLevelsBean.getStartVal() && calories<childSmartBandLevelsBean.getEndVal()){
                    cal1.setBackgroundResource(R.drawable.calories_box_detail);
                    cal2.setBackgroundResource(R.drawable.calories_box_detail);
                    cal3.setBackgroundResource(R.drawable.calories_box_detail);
                }
            }else if(i==3){
                if(calories>childSmartBandLevelsBean.getStartVal() && calories<childSmartBandLevelsBean.getEndVal()){
                    cal1.setBackgroundResource(R.drawable.calories_box_detail);
                    cal2.setBackgroundResource(R.drawable.calories_box_detail);
                    cal3.setBackgroundResource(R.drawable.calories_box_detail);
                    cal4.setBackgroundResource(R.drawable.calories_box_detail);

                }
            }else if(i==4){
                if(calories>childSmartBandLevelsBean.getStartVal() && calories<childSmartBandLevelsBean.getEndVal()){
                    cal1.setBackgroundResource(R.drawable.calories_box_detail);
                    cal2.setBackgroundResource(R.drawable.calories_box_detail);
                    cal3.setBackgroundResource(R.drawable.calories_box_detail);
                    cal4.setBackgroundResource(R.drawable.calories_box_detail);
                    cal5.setBackgroundResource(R.drawable.calories_box_detail);
                }
            }
        }
    }

    //distance method
    public void distanceProgress(){
        for(int i=0; i<distanceArrayList.size();i++){
            final ChildSmartBandLevelsBean childSmartBandLevelsBean = distanceArrayList.get(i);
            if(i==0){
                if(distance>childSmartBandLevelsBean.getStartVal() && distance<childSmartBandLevelsBean.getEndVal()){
                    distance1.setBackgroundResource(R.drawable.distance_box_detail);
                }
            }else if(i==1){
                if(distance>childSmartBandLevelsBean.getStartVal() && distance<childSmartBandLevelsBean.getEndVal()){
                    distance1.setBackgroundResource(R.drawable.distance_box_detail);
                    distance2.setBackgroundResource(R.drawable.distance_box_detail);
                }
            }else if(i==2){
                if(distance>childSmartBandLevelsBean.getStartVal() && distance<childSmartBandLevelsBean.getEndVal()){
                    distance1.setBackgroundResource(R.drawable.distance_box_detail);
                    distance2.setBackgroundResource(R.drawable.distance_box_detail);
                    distance3.setBackgroundResource(R.drawable.distance_box_detail);
                }
            }else if(i==3){
                if(distance>childSmartBandLevelsBean.getStartVal() && distance<childSmartBandLevelsBean.getEndVal()){
                    distance1.setBackgroundResource(R.drawable.distance_box_detail);
                    distance2.setBackgroundResource(R.drawable.distance_box_detail);
                    distance3.setBackgroundResource(R.drawable.distance_box_detail);
                    distance4.setBackgroundResource(R.drawable.distance_box_detail);
                }
            }else if(i==4){
                if(distance>childSmartBandLevelsBean.getStartVal() && distance<childSmartBandLevelsBean.getEndVal()){
                    distance1.setBackgroundResource(R.drawable.distance_box_detail);
                    distance2.setBackgroundResource(R.drawable.distance_box_detail);
                    distance3.setBackgroundResource(R.drawable.distance_box_detail);
                    distance4.setBackgroundResource(R.drawable.distance_box_detail);
                    distance5.setBackgroundResource(R.drawable.distance_box_detail);
                }
            }
        }
    }
    //steps method
    public void stepsProgress(){
        for(int i=0; i<stepsArrayList.size();i++){
            final ChildSmartBandLevelsBean childSmartBandLevelsBean = stepsArrayList.get(i);
            if(i==0){
                if(steps>childSmartBandLevelsBean.getStartVal() && steps<childSmartBandLevelsBean.getEndVal()){
                    steps1.setBackgroundResource(R.drawable.step_box_detail);
                }
            }else if(i==1){
                if(steps>childSmartBandLevelsBean.getStartVal() && steps<childSmartBandLevelsBean.getEndVal()){
                    steps1.setBackgroundResource(R.drawable.step_box_detail);
                    steps2.setBackgroundResource(R.drawable.step_box_detail);
                }
            }else if(i==2){
                if(steps>childSmartBandLevelsBean.getStartVal() && steps<childSmartBandLevelsBean.getEndVal()){
                    steps1.setBackgroundResource(R.drawable.step_box_detail);
                    steps2.setBackgroundResource(R.drawable.step_box_detail);
                    steps3.setBackgroundResource(R.drawable.step_box_detail);
                }
            }else if(i==3){
                if(steps>childSmartBandLevelsBean.getStartVal() && steps<childSmartBandLevelsBean.getEndVal()){
                    steps1.setBackgroundResource(R.drawable.step_box_detail);
                    steps2.setBackgroundResource(R.drawable.step_box_detail);
                    steps3.setBackgroundResource(R.drawable.step_box_detail);
                    steps4.setBackgroundResource(R.drawable.step_box_detail);
                }
            }else if(i==4){
                if(steps>childSmartBandLevelsBean.getStartVal() && steps<childSmartBandLevelsBean.getEndVal()){
                    steps1.setBackgroundResource(R.drawable.step_box_detail);
                    steps2.setBackgroundResource(R.drawable.step_box_detail);
                    steps3.setBackgroundResource(R.drawable.step_box_detail);
                    steps4.setBackgroundResource(R.drawable.step_box_detail);
                    steps5.setBackgroundResource(R.drawable.step_box_detail);
                }
            }
        }
    }
}
