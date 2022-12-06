package com.dunvant.application.parent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ActivityStatBean;
import com.dunvant.application.beans.ActivityStatComponentBean;
import com.dunvant.application.beans.ActivityStatComponentDetailBean;
import com.dunvant.application.beans.AgeGroupAttendanceBean;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParentChildActivityStatsScreen extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    TextView txtSession;
    TextView sessionName;
    TextView txtTotalSessions;
    TextView totalSessions;
    TextView txtAttended;
    TextView attendedSessions;
    TextView txtRemaining;
    TextView remainingSessions;
    ImageView academyImage;

    TextView txtDistance;
    DonutProgress distanceDonut;
    TextView averageDistance;
    TextView distanceCategory;
    ProgressBar distanceProgressBar;
    Button viewDetailsDistance;

    TextView txtSteps;
    DonutProgress stepsDonut;
    TextView averageSteps;
    TextView stepsCategory;
    ProgressBar stepsProgressBar;
    Button viewDetailsSteps;

    TextView txtCalories;
    DonutProgress caloriesDonut;
    TextView averageCalories;
    TextView caloriesCategory;
    ProgressBar caloriesProgressBar;
    Button viewDetailsCalories;

    TextView txtHeartRate;
    DonutProgress heartRateDonut;
    TextView averageHeartRate;
    TextView heartRateCategory;
    ProgressBar heartRateProgressBar;
    Button viewDetailsHeartRate;

    AgeGroupAttendanceBean clickedOnAgeGroup;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    private final String GET_ACTIVITY_STATS = "GET_ACTIVITY_STATS";

    ActivityStatBean activityStatBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_child_activity_stats_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        activityStatBean = new ActivityStatBean();
        initViews();
        changeFonts();

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentChildActivityStatsScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnAgeGroup = (AgeGroupAttendanceBean) intent.getSerializableExtra("clickedOnAgeGroup");
            getActivityStats();
        }

        viewDetailsDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityStatBean.getDistanceBean().getDetailBeansList() != null && !activityStatBean.getDistanceBean().getDetailBeansList().isEmpty()){
                    showPopup("distance");
                } else {
                    Toast.makeText(ParentChildActivityStatsScreen.this, "Data not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewDetailsSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityStatBean.getStepsBean().getDetailBeansList() != null && !activityStatBean.getStepsBean().getDetailBeansList().isEmpty()) {
                    showPopup("steps");
                } else {
                    Toast.makeText(ParentChildActivityStatsScreen.this, "Data not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewDetailsCalories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityStatBean.getCaloriesBean().getDetailBeansList() != null && !activityStatBean.getCaloriesBean().getDetailBeansList().isEmpty()) {
                    showPopup("calories");
                } else {
                    Toast.makeText(ParentChildActivityStatsScreen.this, "Data not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewDetailsHeartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityStatBean.getHeartRateBean().getDetailBeansList() != null && !activityStatBean.getHeartRateBean().getDetailBeansList().isEmpty()) {
                    showPopup("heartRate");
                } else {
                    Toast.makeText(ParentChildActivityStatsScreen.this, "Data not available", Toast.LENGTH_SHORT).show();
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

    private void showPopup(String type){

        final Dialog dialog = new Dialog(ParentChildActivityStatsScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.parent_dialog_activity_stat_detail);

        ListView dates = (ListView) dialog.findViewById(R.id.dates);
        Button close = (Button) dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ArrayList<String> datesList = new ArrayList<>();

        switch (type) {
            case "distance":
                for(ActivityStatComponentDetailBean bean : activityStatBean.getDistanceBean().getDetailBeansList()) {
                    datesList.add(bean.getDateFormatted()+" - "+bean.getValue());
                }
                break;
            case "steps":
                for(ActivityStatComponentDetailBean bean : activityStatBean.getStepsBean().getDetailBeansList()) {
                    datesList.add(bean.getDateFormatted()+" - "+bean.getValue());
                }
                break;
            case "calories":
                for(ActivityStatComponentDetailBean bean : activityStatBean.getCaloriesBean().getDetailBeansList()) {
                    datesList.add(bean.getDateFormatted()+" - "+bean.getValue());
                }
                break;
            case "heartRate":
                for(ActivityStatComponentDetailBean bean : activityStatBean.getHeartRateBean().getDetailBeansList()) {
                    datesList.add(bean.getDateFormatted()+" - Min:"+bean.getMin()+" - Max:"+bean.getMax());
                }
                break;
        }

        dates.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datesList));

        dialog.show();
    }

    private void initViews(){
        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        txtSession = (TextView) findViewById(R.id.txtSession);
        sessionName = (TextView) findViewById(R.id.sessionName);
        txtTotalSessions = (TextView) findViewById(R.id.txtTotalSessions);
        totalSessions = (TextView) findViewById(R.id.totalSessions);
        txtAttended = (TextView) findViewById(R.id.txtAttended);
        attendedSessions = (TextView) findViewById(R.id.attendedSessions);
        txtRemaining = (TextView) findViewById(R.id.txtRemaining);
        remainingSessions = (TextView) findViewById(R.id.remainingSessions);
        academyImage = (ImageView) findViewById(R.id.academyImage);

        txtDistance = (TextView) findViewById(R.id.txtDistance);
        distanceDonut = (DonutProgress) findViewById(R.id.distanceDonut);
        averageDistance = (TextView) findViewById(R.id.averageDistance);
        distanceCategory = (TextView) findViewById(R.id.distanceCategory);
        distanceProgressBar = (ProgressBar) findViewById(R.id.distanceProgressBar);
        viewDetailsDistance = (Button) findViewById(R.id.viewDetailsDistance);

        txtSteps = (TextView) findViewById(R.id.txtSteps);
        stepsDonut = (DonutProgress) findViewById(R.id.stepsDonut);
        averageSteps = (TextView) findViewById(R.id.averageSteps);
        stepsCategory = (TextView) findViewById(R.id.stepsCategory);
        stepsProgressBar = (ProgressBar) findViewById(R.id.stepsProgressBar);
        viewDetailsSteps = (Button) findViewById(R.id.viewDetailsSteps);

        txtCalories = (TextView) findViewById(R.id.txtCalories);
        caloriesDonut = (DonutProgress) findViewById(R.id.caloriesDonut);
        averageCalories = (TextView) findViewById(R.id.averageCalories);
        caloriesCategory = (TextView) findViewById(R.id.caloriesCategory);
        caloriesProgressBar = (ProgressBar) findViewById(R.id.caloriesProgressBar);
        viewDetailsCalories = (Button) findViewById(R.id.viewDetailsCalories);

        txtHeartRate = (TextView) findViewById(R.id.txtHeartRate);
        heartRateDonut = (DonutProgress) findViewById(R.id.heartRateDonut);
        averageHeartRate = (TextView) findViewById(R.id.averageHeartRate);
        heartRateCategory = (TextView) findViewById(R.id.heartRateCategory);
        heartRateProgressBar = (ProgressBar) findViewById(R.id.heartRateProgressBar);
        viewDetailsHeartRate = (Button) findViewById(R.id.viewDetailsHeartRate);
    }

    private void updateUI(){
        try {
//            title.setText(activityStatBean.getChildBean().getFullName());

            sessionName.setText(activityStatBean.getSessionDay() /*+ "(" + activityStatBean.getStartTime() + " - " + activityStatBean.getEndTime() + ")"*/);
            totalSessions.setText(activityStatBean.getTotalSessions());
            attendedSessions.setText(activityStatBean.getAttendedSessions());
            remainingSessions.setText(activityStatBean.getRemainingSessions());
            imageLoader.displayImage(activityStatBean.getCoachingProgramImage(), academyImage, options);

            averageDistance.setText("Average: " + activityStatBean.getDistanceBean().getAverage()+" km");
            distanceCategory.setText(activityStatBean.getDistanceBean().getName());
            distanceProgressBar.setMax(Integer.parseInt(activityStatBean.getDistanceBean().getEnd()));
            distanceProgressBar.setProgress((int)Float.parseFloat(activityStatBean.getDistanceBean().getAverage()));
            distanceDonut.setProgress(100.0f);
            distanceDonut.setText(activityStatBean.getDistanceBean().getTotal()+" km");
            if(activityStatBean.getDistanceBean().getColorCode() != null && !activityStatBean.getDistanceBean().getColorCode().isEmpty()) {
                txtDistance.setBackgroundColor(Color.parseColor(activityStatBean.getDistanceBean().getColorCode()));
                distanceDonut.setFinishedStrokeColor(Color.parseColor(activityStatBean.getDistanceBean().getColorCode()));
                viewDetailsDistance.setBackgroundColor(Color.parseColor(activityStatBean.getDistanceBean().getColorCode()));
                if (Build.VERSION.SDK_INT >= 21) {
                    distanceProgressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(activityStatBean.getDistanceBean().getColorCode())));
                } else {
                    distanceProgressBar.getProgressDrawable().setColorFilter(Color.parseColor(activityStatBean.getDistanceBean().getColorCode()), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }

            averageSteps.setText("Average: " + activityStatBean.getStepsBean().getAverage());
            stepsCategory.setText(activityStatBean.getStepsBean().getName());
            stepsProgressBar.setMax(Integer.parseInt(activityStatBean.getStepsBean().getEnd()));
            stepsProgressBar.setProgress((int)Float.parseFloat(activityStatBean.getStepsBean().getAverage()));
            stepsDonut.setProgress(100.0f);
            stepsDonut.setText(activityStatBean.getStepsBean().getTotal());
            if(activityStatBean.getStepsBean().getColorCode() != null && !activityStatBean.getStepsBean().getColorCode().isEmpty()) {
                txtSteps.setBackgroundColor(Color.parseColor(activityStatBean.getStepsBean().getColorCode()));
                stepsDonut.setFinishedStrokeColor(Color.parseColor(activityStatBean.getStepsBean().getColorCode()));
                viewDetailsSteps.setBackgroundColor(Color.parseColor(activityStatBean.getStepsBean().getColorCode()));
                if (Build.VERSION.SDK_INT >= 21) {
                    stepsProgressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(activityStatBean.getStepsBean().getColorCode())));
                } else {
                    stepsProgressBar.getProgressDrawable().setColorFilter(Color.parseColor(activityStatBean.getStepsBean().getColorCode()), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }

            averageCalories.setText("Average: " + activityStatBean.getCaloriesBean().getAverage());
            caloriesCategory.setText(activityStatBean.getCaloriesBean().getName());
            caloriesProgressBar.setMax(Integer.parseInt(activityStatBean.getCaloriesBean().getEnd()));
            caloriesProgressBar.setProgress((int)Float.parseFloat(activityStatBean.getCaloriesBean().getAverage()));
            caloriesDonut.setProgress(100.0f);
            caloriesDonut.setText(activityStatBean.getCaloriesBean().getTotal());
            if(activityStatBean.getCaloriesBean().getColorCode() != null && !activityStatBean.getCaloriesBean().getColorCode().isEmpty()) {
                txtCalories.setBackgroundColor(Color.parseColor(activityStatBean.getCaloriesBean().getColorCode()));
                caloriesDonut.setFinishedStrokeColor(Color.parseColor(activityStatBean.getCaloriesBean().getColorCode()));
                viewDetailsCalories.setBackgroundColor(Color.parseColor(activityStatBean.getCaloriesBean().getColorCode()));
                if (Build.VERSION.SDK_INT >= 21) {
                    caloriesProgressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(activityStatBean.getCaloriesBean().getColorCode())));
                } else {
                    caloriesProgressBar.getProgressDrawable().setColorFilter(Color.parseColor(activityStatBean.getCaloriesBean().getColorCode()), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }

            averageHeartRate.setText("Average: " + activityStatBean.getHeartRateBean().getAverage());
            heartRateCategory.setText(activityStatBean.getHeartRateBean().getName());
            heartRateProgressBar.setMax(Integer.parseInt(activityStatBean.getHeartRateBean().getEnd()));
            heartRateProgressBar.setProgress((int)Float.parseFloat(activityStatBean.getHeartRateBean().getAverage()));
            heartRateDonut.setProgress(100.0f);
            heartRateDonut.setText(activityStatBean.getHeartRateBean().getTotal());
            if(activityStatBean.getHeartRateBean().getColorCode() != null && !activityStatBean.getHeartRateBean().getColorCode().isEmpty()) {
                txtHeartRate.setBackgroundColor(Color.parseColor(activityStatBean.getHeartRateBean().getColorCode()));
                heartRateDonut.setFinishedStrokeColor(Color.parseColor(activityStatBean.getHeartRateBean().getColorCode()));
                viewDetailsHeartRate.setBackgroundColor(Color.parseColor(activityStatBean.getHeartRateBean().getColorCode()));
                if (Build.VERSION.SDK_INT >= 21) {
                    heartRateProgressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(activityStatBean.getHeartRateBean().getColorCode())));
                } else {
                    heartRateProgressBar.getProgressDrawable().setColorFilter(Color.parseColor(activityStatBean.getHeartRateBean().getColorCode()), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ParentChildActivityStatsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getActivityStats() {
        if(Utilities.isNetworkAvailable(ParentChildActivityStatsScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();

//            System.out.println("child_id::"+clickedOnAgeGroup.getUsersId()+"::sessions_id::"+clickedOnAgeGroup.getSessionsId());
            nameValuePairList.add(new BasicNameValuePair("child_id", clickedOnAgeGroup.getUsersId()));
            nameValuePairList.add(new BasicNameValuePair("sessions_id", clickedOnAgeGroup.getSessionsId()));

            String webServiceUrl = Utilities.BASE_URL + "account/child_activity_report";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentChildActivityStatsScreen.this, nameValuePairList, GET_ACTIVITY_STATS, ParentChildActivityStatsScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentChildActivityStatsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case GET_ACTIVITY_STATS:

                if(response == null){
                    Toast.makeText(ParentChildActivityStatsScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            JSONObject dataObject = responseObject.getJSONObject("data");

                            activityStatBean.setCoachingProgramName(dataObject.getString("coaching_program_name"));
                            activityStatBean.setCoachingProgramImage(dataObject.getString("coaching_program_image"));
                            activityStatBean.setSessionDay(dataObject.getString("session_day"));
                            activityStatBean.setStartTime(dataObject.getString("start_time"));
                            activityStatBean.setEndTime(dataObject.getString("end_time"));
                            activityStatBean.setLocationName(dataObject.getString("location_name"));
                            activityStatBean.setTotalSessions(dataObject.getString("total_sessions"));
                            activityStatBean.setRemainingSessions(dataObject.getString("remaining_sessions"));
                            activityStatBean.setAttendedSessions(dataObject.getString("attended_sessions"));

                            JSONObject totalCountObject = dataObject.getJSONObject("total_count");

                            JSONObject distanceObject = totalCountObject.getJSONObject("distance");
                            ActivityStatComponentBean distanceBean = new ActivityStatComponentBean();

                            distanceBean.setTotal(distanceObject.getString("total"));
                            distanceBean.setAverage(distanceObject.getString("average"));

                            JSONObject distanceLevelObject = distanceObject.getJSONObject("level");

                            distanceBean.setStart(distanceLevelObject.getString("start"));
                            distanceBean.setEnd(distanceLevelObject.getString("end"));
                            distanceBean.setName(distanceLevelObject.getString("name"));
                            distanceBean.setColorCode(distanceLevelObject.getString("color_code"));
                            distanceBean.setPercentage(distanceLevelObject.getString("percentage"));

                            activityStatBean.setDistanceBean(distanceBean);

                            JSONObject stepsObject = totalCountObject.getJSONObject("steps");
                            ActivityStatComponentBean stepsBean = new ActivityStatComponentBean();

                            stepsBean.setTotal(stepsObject.getString("total"));
                            stepsBean.setAverage(stepsObject.getString("average"));

                            JSONObject stepLevelObject = stepsObject.getJSONObject("level");

                            stepsBean.setStart(stepLevelObject.getString("start"));
                            stepsBean.setEnd(stepLevelObject.getString("end"));
                            stepsBean.setName(stepLevelObject.getString("name"));
                            stepsBean.setColorCode(stepLevelObject.getString("color_code"));
                            stepsBean.setPercentage(stepLevelObject.getString("percentage"));

                            activityStatBean.setStepsBean(stepsBean);

                            JSONObject caloriesObject = totalCountObject.getJSONObject("calories");
                            ActivityStatComponentBean caloriesBean = new ActivityStatComponentBean();

                            caloriesBean.setTotal(caloriesObject.getString("total"));
                            caloriesBean.setAverage(caloriesObject.getString("average"));

                            JSONObject calorieLevelObject = caloriesObject.getJSONObject("level");

                            caloriesBean.setStart(calorieLevelObject.getString("start"));
                            caloriesBean.setEnd(calorieLevelObject.getString("end"));
                            caloriesBean.setName(calorieLevelObject.getString("name"));
                            caloriesBean.setColorCode(calorieLevelObject.getString("color_code"));
                            caloriesBean.setPercentage(calorieLevelObject.getString("percentage"));

                            activityStatBean.setCaloriesBean(caloriesBean);

                            JSONObject heartRateObject = totalCountObject.getJSONObject("heart rate");
                            ActivityStatComponentBean heartRateBean = new ActivityStatComponentBean();

                            heartRateBean.setTotal(heartRateObject.getString("total"));
                            heartRateBean.setAverage(heartRateObject.getString("average"));

                            JSONObject heartRateLevelObject = heartRateObject.getJSONObject("level");

                            heartRateBean.setStart(heartRateLevelObject.getString("start"));
                            heartRateBean.setEnd(heartRateLevelObject.getString("end"));
                            heartRateBean.setName(heartRateLevelObject.getString("name"));
                            heartRateBean.setColorCode(heartRateLevelObject.getString("color_code"));
                            heartRateBean.setPercentage(heartRateLevelObject.getString("percentage"));

                            activityStatBean.setHeartRateBean(heartRateBean);

                            JSONObject detailsObject = dataObject.getJSONObject("details");

                            JSONArray distanceDetailArray = detailsObject.getJSONArray("distance");
                            ArrayList<ActivityStatComponentDetailBean> distanceDetailList = new ArrayList<>();
                            ActivityStatComponentDetailBean distanceDetailBean;
                            for(int i=0;i<distanceDetailArray.length();i++) {
                                JSONObject distanceDetailObject = distanceDetailArray.getJSONObject(i);

                                distanceDetailBean = new ActivityStatComponentDetailBean();
                                distanceDetailBean.setDateFormatted(distanceDetailObject.getString("date_formatted"));
                                distanceDetailBean.setValue(distanceDetailObject.getString("value"));

                                distanceDetailList.add(distanceDetailBean);
                            }
                            activityStatBean.getDistanceBean().setDetailBeansList(distanceDetailList);

                            JSONArray stepsDetailArray = detailsObject.getJSONArray("steps");
                            ArrayList<ActivityStatComponentDetailBean> stepsDetailList = new ArrayList<>();
                            ActivityStatComponentDetailBean stepDetailBean;
                            for(int i=0;i<stepsDetailArray.length();i++){
                                JSONObject stepDetailObject = stepsDetailArray.getJSONObject(i);

                                stepDetailBean = new ActivityStatComponentDetailBean();
                                stepDetailBean.setDateFormatted(stepDetailObject.getString("date_formatted"));
                                stepDetailBean.setValue(stepDetailObject.getString("value"));

                                stepsDetailList.add(stepDetailBean);
                            }
                            activityStatBean.getStepsBean().setDetailBeansList(stepsDetailList);

                            JSONArray caloriesDetailArray = detailsObject.getJSONArray("calories");
                            ArrayList<ActivityStatComponentDetailBean> caloriesDetailList = new ArrayList<>();
                            ActivityStatComponentDetailBean caloriesDetailBean;
                            for(int i=0;i<caloriesDetailArray.length();i++){
                                JSONObject caloriesDetailObject = caloriesDetailArray.getJSONObject(i);

                                caloriesDetailBean = new ActivityStatComponentDetailBean();
                                caloriesDetailBean.setDateFormatted(caloriesDetailObject.getString("date_formatted"));
                                caloriesDetailBean.setValue(caloriesDetailObject.getString("value"));

                                caloriesDetailList.add(caloriesDetailBean);
                            }
                            activityStatBean.getCaloriesBean().setDetailBeansList(caloriesDetailList);

                            JSONArray heartRateDetailArray = detailsObject.getJSONArray("heart rate");
                            ArrayList<ActivityStatComponentDetailBean> heartRateDetailList = new ArrayList<>();
                            ActivityStatComponentDetailBean heartRateDeatilBean;
                            for(int i=0;i<heartRateDetailArray.length();i++) {
                                JSONObject heartRateDetailObject = heartRateDetailArray.getJSONObject(i);

                                heartRateDeatilBean = new ActivityStatComponentDetailBean();
                                heartRateDeatilBean.setDateFormatted(heartRateDetailObject.getString("date_formatted"));

                                JSONObject heartRateDetailValueObject = heartRateDetailObject.getJSONObject("value");

                                heartRateDeatilBean.setMin(heartRateDetailValueObject.getString("min"));
                                heartRateDeatilBean.setMax(heartRateDetailValueObject.getString("max"));

                                heartRateDetailList.add(heartRateDeatilBean);
                            }
                            activityStatBean.getHeartRateBean().setDetailBeansList(heartRateDetailList);

                            ChildBean childBean = new ChildBean();

                            JSONObject childObject = dataObject.getJSONObject("child_details");
                            childBean.setFullName(childObject.getString("full_name"));

                            activityStatBean.setChildBean(childBean);

                            updateUI();

                        } else {
                            Toast.makeText(ParentChildActivityStatsScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentChildActivityStatsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void changeFonts(){
        title.setTypeface(linoType);
        txtSession.setTypeface(helvetica);
        sessionName.setTypeface(helvetica);
        txtTotalSessions.setTypeface(helvetica);
        totalSessions.setTypeface(helvetica);
        txtAttended.setTypeface(helvetica);
        attendedSessions.setTypeface(helvetica);
        txtRemaining.setTypeface(helvetica);
        remainingSessions.setTypeface(helvetica);

        txtDistance.setTypeface(helvetica);
        averageDistance.setTypeface(helvetica);
        distanceCategory.setTypeface(helvetica);
        viewDetailsDistance.setTypeface(linoType);

        txtSteps.setTypeface(helvetica);
        averageSteps.setTypeface(helvetica);
        stepsCategory.setTypeface(helvetica);
        viewDetailsSteps.setTypeface(linoType);

        txtCalories.setTypeface(helvetica);
        averageCalories.setTypeface(helvetica);
        caloriesCategory.setTypeface(helvetica);
        viewDetailsCalories.setTypeface(linoType);

        txtHeartRate.setTypeface(helvetica);
        averageHeartRate.setTypeface(helvetica);
        heartRateCategory.setTypeface(helvetica);
        viewDetailsHeartRate.setTypeface(linoType);
    }

}