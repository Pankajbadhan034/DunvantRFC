package com.dunvant.application.coach;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildDetailScoreBean;
import com.dunvant.application.beans.DetailedScoreDataBean;
import com.dunvant.application.beans.ScoresDataBean;
import com.dunvant.application.beans.SessionDetailsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachDetailedChildScoreAdapter;
import com.dunvant.application.coach.fragments.CoachManageScoresFragment;
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

public class CoachChildScoreDetailScreen extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    TextView childName;
    TextView termName;
    ListView scoresListView;
    Button submit;
    TextView low;
    TextView middle;
    TextView top;

    ScoresDataBean scoresDataBean;
    int clickedOnPosition;
    SessionDetailsBean sessionDetailsBean;
//    String reportDate;

    private final String GET_DETAILED_SCORES = "GET_DETAILED_SCORES";
    private final String SAVE_SCORES = "SAVE_SCORES";

    DetailedScoreDataBean detailedScoreDataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_activity_child_score_detail_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        childName = (TextView) findViewById(R.id.childName);
        termName = (TextView) findViewById(R.id.termName);
        scoresListView = (ListView) findViewById(R.id.scoresListView);
        submit = (Button) findViewById(R.id.submit);
        low = (TextView) findViewById(R.id.low);
        middle = (TextView) findViewById(R.id.middle);
        top = (TextView) findViewById(R.id.top);

        changeFonts();

        Intent intent = getIntent();
        if (intent != null) {
            scoresDataBean = (ScoresDataBean) intent.getSerializableExtra("scoreDataBean");
            clickedOnPosition = intent.getIntExtra("clickedOnPosition", -1);
            sessionDetailsBean = (SessionDetailsBean) intent.getSerializableExtra("sessionDetailsBean");
//            reportDate = intent.getStringExtra("reportDate");

            childName.setText(scoresDataBean.getChildName());
//            termName.setText(scoresDataBean.getLocationName()+", "+scoresDataBean.getTermName()+", "+scoresDataBean.getDayLabel());
            termName.setText(sessionDetailsBean.getLocationName()+", "+sessionDetailsBean.getTermsName()+", "+sessionDetailsBean.getDayLabel());

//            getDetailedScores();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailedScoreDataBean.getChildDetailScoresListBean().get(0).isAskConfirm()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CoachChildScoreDetailScreen.this);
                    builder.setMessage("Report for this session has already been submitted. Do you wish to resubmit?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveScores();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                } else {
                    saveScores();
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

    @Override
    protected void onResume() {
        super.onResume();
        getDetailedScores();
    }

    private void updateUI() {
        low.setText(detailedScoreDataBean.getLowLabel());
        middle.setText(detailedScoreDataBean.getMiddleLabel());
        top.setText(detailedScoreDataBean.getTopLabel());

        try {
            String strLow = detailedScoreDataBean.getLowLabel();
            String arr[] = strLow.split("-");
            String str = arr[1].trim();
            String arr1[] = str.split(" ");
            int n1 = Integer.parseInt(arr1[0]);

            String strMiddle = detailedScoreDataBean.getMiddleLabel();
            arr = strMiddle.split("-");
            str = arr[1].trim();
            arr1 = str.split(" ");
            int n2 = Integer.parseInt(arr1[0]);

            scoresListView.setAdapter(new CoachDetailedChildScoreAdapter(CoachChildScoreDetailScreen.this, detailedScoreDataBean.getChildDetailScoresListBean(), n1, n2));

        }catch (Exception e) {
            Toast.makeText(CoachChildScoreDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getDetailedScores(){
        if(Utilities.isNetworkAvailable(CoachChildScoreDetailScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("scores_id", scoresDataBean.getChildrenScoresListing().get(clickedOnPosition).getScoresId()));
            nameValuePairList.add(new BasicNameValuePair("child_id", scoresDataBean.getChildId()));
            nameValuePairList.add(new BasicNameValuePair("performance_element_id", scoresDataBean.getChildrenScoresListing().get(clickedOnPosition).getPerformanceElementId()));
//            nameValuePairList.add(new BasicNameValuePair("sessions_id", scoresDataBean.getSessionsId()));
            nameValuePairList.add(new BasicNameValuePair("sessions_id", sessionDetailsBean.getSessionId()));
            nameValuePairList.add(new BasicNameValuePair("session_date", sessionDetailsBean.getReportDate()));

            String webServiceUrl = Utilities.BASE_URL + "coach/child_detailed_score";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachChildScoreDetailScreen.this, nameValuePairList, GET_DETAILED_SCORES, CoachChildScoreDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachChildScoreDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveScores() {

        String strScoreDetails = "[";

        for(ChildDetailScoreBean childDetailScoreBean : detailedScoreDataBean.getChildDetailScoresListBean()){

            String strScore = childDetailScoreBean.getScore();

            if(strScore.isEmpty()) {
                strScore = "0";
            }

            strScoreDetails += "{\"performance_sub_element_id\":\""+ childDetailScoreBean.getPerformanceSubElementId()+"\", \"score\":\""+strScore+"\"},";
        }

        if (strScoreDetails != null && strScoreDetails.length() > 0 && strScoreDetails.charAt(strScoreDetails.length()-1)==',') {
            strScoreDetails = strScoreDetails.substring(0, strScoreDetails.length()-1);
        }

        strScoreDetails += "]";

        String scoresId = scoresDataBean.getChildrenScoresListing().get(clickedOnPosition).getScoresId();
        if (scoresId.isEmpty()) {
            scoresId = "";
        }

        if(Utilities.isNetworkAvailable(CoachChildScoreDetailScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("scores_id", scoresId));
            nameValuePairList.add(new BasicNameValuePair("child_id", scoresDataBean.getChildId()));
            nameValuePairList.add(new BasicNameValuePair("performance_element_id", scoresDataBean.getChildrenScoresListing().get(clickedOnPosition).getPerformanceElementId()));
//            nameValuePairList.add(new BasicNameValuePair("sessions_id", scoresDataBean.getSessionsId()));
            nameValuePairList.add(new BasicNameValuePair("sessions_id", sessionDetailsBean.getSessionId()));
            nameValuePairList.add(new BasicNameValuePair("score_details", strScoreDetails));
//            nameValuePairList.add(new BasicNameValuePair("terms_id", scoresDataBean.getTermId()));
            nameValuePairList.add(new BasicNameValuePair("terms_id", sessionDetailsBean.getTermsId()));
            nameValuePairList.add(new BasicNameValuePair("session_date", sessionDetailsBean.getReportDate()));

            String webServiceUrl = Utilities.BASE_URL + "coach/save_children_scores";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachChildScoreDetailScreen.this, nameValuePairList, SAVE_SCORES, CoachChildScoreDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachChildScoreDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_DETAILED_SCORES:
                if(response == null) {
                    Toast.makeText(CoachChildScoreDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");

                            // Get only the 0th element
                            JSONObject detailedScoreObject = dataArray.getJSONObject(0);

                            detailedScoreDataBean = new DetailedScoreDataBean();

                            detailedScoreDataBean.setChildName(detailedScoreObject.getString("child_name"));
                            detailedScoreDataBean.setSessionsId(detailedScoreObject.getString("sessions_id"));
                            detailedScoreDataBean.setLocationsName(detailedScoreObject.getString("locations_name"));
                            detailedScoreDataBean.setTermName(detailedScoreObject.getString("term_name"));
                            detailedScoreDataBean.setDay(detailedScoreObject.getString("day"));
                            detailedScoreDataBean.setGroupName(detailedScoreObject.getString("group_name"));
                            detailedScoreDataBean.setScoreId(detailedScoreObject.getString("scores_id"));
                            detailedScoreDataBean.setPerformanceElementId(detailedScoreObject.getString("performance_element_id"));
                            detailedScoreDataBean.setElementName(detailedScoreObject.getString("element_name"));
                            detailedScoreDataBean.setChildId(detailedScoreObject.getString("child_id"));
                            detailedScoreDataBean.setLowLabel(detailedScoreObject.getString("low_label"));
                            detailedScoreDataBean.setMiddleLabel(detailedScoreObject.getString("middle_label"));
                            detailedScoreDataBean.setTopLabel(detailedScoreObject.getString("top_label"));
                            detailedScoreDataBean.setDayLabel(detailedScoreObject.getString("day_label"));

                            JSONArray childDetailScoreArray = detailedScoreObject.getJSONArray("child_detailed_scores");
                            ArrayList<ChildDetailScoreBean> childDetailScoresListBean = new ArrayList<>();
                            ChildDetailScoreBean childDetailScoreBean;
                            for (int i=0; i<childDetailScoreArray.length(); i++) {
                                JSONObject childDetailScoreObject = childDetailScoreArray.getJSONObject(i);

                                childDetailScoreBean = new ChildDetailScoreBean();

                                childDetailScoreBean.setParentId(childDetailScoreObject.getString("parent_id"));
                                childDetailScoreBean.setPerformanceSubElementId(childDetailScoreObject.getString("performance_sub_element_id"));
                                childDetailScoreBean.setSubElementName(childDetailScoreObject.getString("sub_element_name"));
                                childDetailScoreBean.setScoreSubElementId(childDetailScoreObject.getString("score_sub_elements_id"));
                                childDetailScoreBean.setScore(childDetailScoreObject.getString("score"));
                                childDetailScoreBean.setType(childDetailScoreObject.getString("type"));
                                childDetailScoreBean.setComments(childDetailScoreObject.getString("comments"));
                                childDetailScoreBean.setAskConfirm(childDetailScoreObject.getBoolean("ask_crm"));

                                childDetailScoresListBean.add(childDetailScoreBean);
                            }

                            detailedScoreDataBean.setChildDetailScoresListBean(childDetailScoresListBean);

                            updateUI();

                        } else {
                            Toast.makeText(CoachChildScoreDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachChildScoreDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case SAVE_SCORES:

                if (response == null) {
                    Toast.makeText(CoachChildScoreDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(CoachChildScoreDetailScreen.this, message, Toast.LENGTH_SHORT).show();

                        if (status) {
                            CoachManageScoresFragment.comingFrom = "DetailScreen";
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachChildScoreDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void changeFonts() {
        title.setTypeface(linoType);
        childName.setTypeface(helvetica);
        termName.setTypeface(helvetica);
        submit.setTypeface(linoType);
        low.setTypeface(helvetica);
        middle.setTypeface(helvetica);
        top.setTypeface(helvetica);
    }

}