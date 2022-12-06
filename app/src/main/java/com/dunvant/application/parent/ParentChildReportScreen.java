package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupAttendanceBean;
import com.dunvant.application.beans.ChildDetailScoreBean;
import com.dunvant.application.beans.ChildDetailsBean;
import com.dunvant.application.beans.ChildScoreBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentGraphsAdapter;
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

import androidx.appcompat.app.AppCompatActivity;
import kotlin.jvm.internal.Intrinsics;

public class ParentChildReportScreen extends AppCompatActivity implements IWebServiceCallback{
    String enableDisablePercentageStr = "0";

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    TextView title;
    ImageView backButton;
    ScrollView scrollView;
    ImageView profilePic;
    ImageView programImage;
    TextView childName;
    TextView termName;
    TextView dob;
    TextView weight;
//    TextView sessionsAttended;
    TextView coachName;
    TextView height;
    TextView attendanceInPercentage;
    DonutProgress overallDonutProgress;
    ListView graphsListView;

    private final String GET_MARKS_DETAIL = "GET_MARKS_DETAIL";

    AgeGroupAttendanceBean clickedOnAgeGroup;

    ChildDetailsBean childDetailsBean;
    ArrayList<ChildScoreBean> childScoresList = new ArrayList<>();

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.parent_activity_child_report_screen);
        setContentView(R.layout.parent_activity_child_report_screen_new);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        profilePic = (ImageView) findViewById(R.id.profilePic);
        programImage = (ImageView) findViewById(R.id.programImage);
        childName = (TextView) findViewById(R.id.childName);
        termName = (TextView) findViewById(R.id.termName);
        dob = (TextView) findViewById(R.id.dob);
        weight = (TextView) findViewById(R.id.weight);
//        sessionsAttended = (TextView) findViewById(R.id.sessionsAttended);
        coachName = (TextView) findViewById(R.id.coachName);
        height = (TextView) findViewById(R.id.height);
        attendanceInPercentage = (TextView) findViewById(R.id.attendanceInPercentage);
        overallDonutProgress = (DonutProgress) findViewById(R.id.overallDonutProgress);
        graphsListView = (ListView) findViewById(R.id.graphsListView);
        pieChart = (PieChart) findViewById(R.id.pieChart);

        changeFonts();

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentChildReportScreen.this));
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

            getMarksDetail();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateUI() {
        childName.setText(childDetailsBean.getChildName());
        termName.setText(childDetailsBean.getTermsName());
        dob.setText(childDetailsBean.getShowDob());
        weight.setText(childDetailsBean.getWeight());
//        sessionsAttended.setText(childDetailsBean.getAttendedSessionsCount());
        coachName.setText(childDetailsBean.getCoachName());
        height.setText(childDetailsBean.getHeight());
        attendanceInPercentage.setText(childDetailsBean.getAttendanceInPercentage()+"%");
        try {
            overallDonutProgress.setProgress(Float.parseFloat(childDetailsBean.getTotalScorePercentage()));
            overallDonutProgress.setText(childDetailsBean.getTotalScorePercentage()+"%");
        }catch(NumberFormatException e) {
            Toast.makeText(ParentChildReportScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        try{
            int totalValues=0;
            for(int j=0; j<childScoresList.size(); j++){
                totalValues = totalValues + Integer.parseInt(childScoresList.get(j).getPerformancePercentage());
            }

            if(totalValues!=0){
                ArrayList yVals = new ArrayList();
                ArrayList<Integer> colors = new ArrayList();

                for(int i=0; i<childScoresList.size(); i++){
                    Float pieValue = Float.parseFloat(childScoresList.get(i).getPerformancePercentage()) * 100 / totalValues;
                    yVals.add(new PieEntry(pieValue));
                    colors.add(Color.parseColor(childScoresList.get(i).getColorCode()));
                }


//                Float valuef = 100f - 60f;
//
//                yVals.add(new PieEntry(30f ));
//                yVals.add(new PieEntry(30f ));
//                yVals.add(new PieEntry(valuef ));
//
//                colors.add(Color.parseColor("#123456"));
//                colors.add(Color.parseColor("#ff0000"));
//                colors.add(Color.parseColor("#000000"));

                setUpPieChartData(yVals, pieChart, colors);

//                setUpPieChartDataTemp(50f, pieChart, "#ff0000");

                System.out.println("HERE:::"+totalValues);
            }

        }catch (Exception e){
            System.out.println("error:::"+e.getMessage());
            Toast.makeText(ParentChildReportScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        imageLoader.displayImage(childDetailsBean.getChildProfilePicUrl(), profilePic, options);
        imageLoader.displayImage(childDetailsBean.getProgramImageUrl(), programImage, options);

        graphsListView.setAdapter(new ParentGraphsAdapter(ParentChildReportScreen.this, childScoresList, enableDisablePercentageStr));
        Utilities.setListViewHeightBasedOnChildren(graphsListView);

        scrollView.smoothScrollTo(0, 0);
    }

    private void getMarksDetail() {
        if(Utilities.isNetworkAvailable(ParentChildReportScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("child_id", clickedOnAgeGroup.getUsersId()));
            nameValuePairList.add(new BasicNameValuePair("sessions_id", clickedOnAgeGroup.getSessionsId()));
            nameValuePairList.add(new BasicNameValuePair("session_date", clickedOnAgeGroup.getBookingDate()));


            String webServiceUrl = Utilities.BASE_URL + "account/child_report";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentChildReportScreen.this, nameValuePairList, GET_MARKS_DETAIL, ParentChildReportScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentChildReportScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_MARKS_DETAIL:

                if (response == null) {
                    Toast.makeText(ParentChildReportScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONObject dataObject = responseObject.getJSONObject("data");
                            enableDisablePercentageStr = responseObject.getString("setting");

                            JSONObject childDetailsObject = dataObject.getJSONObject("child_details");
                            childDetailsBean = new ChildDetailsBean();

                            childDetailsBean.setSessionsId(childDetailsObject.getString("sessions_id"));
                            childDetailsBean.setCoachingProgramsName(childDetailsObject.getString("coaching_programs_name"));
                            childDetailsBean.setTermsName(childDetailsObject.getString("terms_name"));
                            childDetailsBean.setLocationsName(childDetailsObject.getString("locations_name"));
                            childDetailsBean.setTestAG(childDetailsObject.getString("test_AG"));
                            childDetailsBean.setGroupName(childDetailsObject.getString("group_name"));
                            childDetailsBean.setDay(childDetailsObject.getString("day"));
                            childDetailsBean.setUsersId(childDetailsObject.getString("users_id"));
                            childDetailsBean.setChildName(childDetailsObject.getString("child_name"));
                            childDetailsBean.setChildProfilePicUrl(childDetailsObject.getString("child_profile_pic_url"));
                            childDetailsBean.setDob(childDetailsObject.getString("dob"));
                            childDetailsBean.setShowDob(childDetailsObject.getString("dob_formatted"));
                            childDetailsBean.setHeight(childDetailsObject.getString("height"));
                            childDetailsBean.setWeight(childDetailsObject.getString("weight"));
                            childDetailsBean.setOrdersIds(childDetailsObject.getString("orders_ids"));
                            childDetailsBean.setBookingDates(childDetailsObject.getString("booking_dates"));
                            childDetailsBean.setDayLabel(childDetailsObject.getString("day_label"));
                            childDetailsBean.setCoachName(childDetailsObject.getString("coach_name"));
                            childDetailsBean.setAttendedSessionsCount(childDetailsObject.getString("attended_sessions_count"));
                            childDetailsBean.setTotalSessionsCount(childDetailsObject.getString("total_sessions_count"));
                            childDetailsBean.setRemainingSessionsCount(childDetailsObject.getString("remaining_sessions_count"));
                            childDetailsBean.setAttendanceInPercentage(childDetailsObject.getString("attendance_in_percentage"));
                            childDetailsBean.setTotalScorePercentage(childDetailsObject.getString("total_score_percentage"));
                            childDetailsBean.setProgramImageUrl(childDetailsObject.getString("program_image_url"));

                            childScoresList.clear();

                            ChildScoreBean childScoreBean;
                            JSONArray childScoresArray = dataObject.getJSONArray("child_scores");
                            for(int i=0; i<childScoresArray.length(); i++) {
                                JSONObject childScoreObject = childScoresArray.getJSONObject(i);
                                childScoreBean = new ChildScoreBean();
                                childScoreBean.setScoresId(childScoreObject.getString("scores_id"));
                                childScoreBean.setScore(childScoreObject.getString("score"));
                                childScoreBean.setPerformanceElementId(childScoreObject.getString("performance_element_id"));
                                childScoreBean.setParentId(childScoreObject.getString("parent_id"));
                                childScoreBean.setElementName(childScoreObject.getString("element_name"));
                                childScoreBean.setVideoUrl(childScoreObject.getString("videos"));
                                childScoreBean.setColorCode(childScoreObject.getString("color_code"));
                                childScoreBean.setPerformancePercentage(childScoreObject.getString("performance_percentage"));
                                childScoreBean.setAreaOfDevelopment(childScoreObject.getString("area_of_development"));

                                ArrayList<ChildDetailScoreBean> childDetailScoresList = new ArrayList<>();
                                ChildDetailScoreBean childDetailScoreBean;
                                JSONArray childDetailScoresArray = childScoreObject.getJSONArray("child_detailed_scores");
                                for (int j=0; j<childDetailScoresArray.length(); j++) {
                                    JSONObject childDetailedScoreObject = childDetailScoresArray.getJSONObject(j);
                                    childDetailScoreBean = new ChildDetailScoreBean();

                                    childDetailScoreBean.setParentId(childDetailedScoreObject.getString("parent_id"));
                                    childDetailScoreBean.setPerformanceSubElementId(childDetailedScoreObject.getString("performance_sub_element_id"));
                                    childDetailScoreBean.setSubElementName(childDetailedScoreObject.getString("sub_element_name"));
                                    childDetailScoreBean.setScoreSubElementId(childDetailedScoreObject.getString("score_sub_elements_id"));
                                    childDetailScoreBean.setScore(childDetailedScoreObject.getString("score"));
                                    childDetailScoreBean.setType(childDetailedScoreObject.getString("type"));
                                    childDetailScoreBean.setComments(childDetailedScoreObject.getString("comments"));
                                    childDetailScoreBean.setSuggestions(childDetailedScoreObject.getString("suggestions"));
                                    childDetailScoreBean.setScorePercentage(childDetailedScoreObject.getString("score_percent"));
                                    childDetailScoreBean.setColorCode(childDetailedScoreObject.getString("color_code"));

                                    childDetailScoresList.add(childDetailScoreBean);
                                }
                                childScoreBean.setDetailedScores(childDetailScoresList);

                                childScoresList.add(childScoreBean);
                            }

                            updateUI();

                        } else {
                            Toast.makeText(ParentChildReportScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentChildReportScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void changeFonts(){
        title.setTypeface(linoType);
        childName.setTypeface(helvetica);
        termName.setTypeface(helvetica);
        dob.setTypeface(helvetica);
        weight.setTypeface(helvetica);
        coachName.setTypeface(helvetica);
        height.setTypeface(helvetica);
        attendanceInPercentage.setTypeface(helvetica);
    }

    private void setUpPieChartData(ArrayList yVals, PieChart pieChart, ArrayList colors) {

//        System.out.println("HERE:: "+yVals.size()+"HERE:: "+colors.size()+":: valuePercentage:: "+enableDisablePercentageStr);


        PieDataSet dataSet = new PieDataSet(yVals, "");
       // if(enableDisablePercentageStr.equalsIgnoreCase("1")){
             dataSet.setValueTextSize(0.0F);
       // }
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.parseColor("#ffffff"));
        PieData data = new PieData((IPieDataSet)dataSet);
        Intrinsics.checkExpressionValueIsNotNull(pieChart, "pieChart");
        pieChart.setData(data);
        pieChart.setCenterTextRadiusPercent(0.0F);
        pieChart.setDrawHoleEnabled(false);
        Legend var10000 = pieChart.getLegend();
        Intrinsics.checkExpressionValueIsNotNull(var10000, "pieChart.legend");
        var10000.setEnabled(false);
        Description var6 = pieChart.getDescription();
        Intrinsics.checkExpressionValueIsNotNull(var6, "pieChart.description");
        var6.setEnabled(false);
        pieChart.invalidate();

    }
}