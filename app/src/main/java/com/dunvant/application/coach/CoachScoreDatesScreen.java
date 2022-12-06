package com.dunvant.application.coach;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupAttendanceBean;
import com.dunvant.application.beans.ScoreDatesCoachBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachScoreDatesAdapter;
import com.dunvant.application.parent.ParentChildReportScreen;
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

public class CoachScoreDatesScreen extends AppCompatActivity implements IWebServiceCallback{

    ArrayList<ScoreDatesCoachBean> scroreDatesCoachBeanArrayList = new ArrayList<>();
    ScoreDatesCoachBean scoreDatesCoachBean;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    ListView datesListView;

    AgeGroupAttendanceBean clickedOnAgeGroup;

    private final String GET_SCORE_DATES_LISTING = "GET_SCORE_DATES_LISTING";

    //ArrayList<String> datesListing = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_activity_score_details_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = findViewById(R.id.backButton);
        datesListView = findViewById(R.id.datesListView);

        Intent intent = getIntent();
        if (intent != null) {
            clickedOnAgeGroup = (AgeGroupAttendanceBean) intent.getSerializableExtra("clickedOnAgeGroup");
            getScoreDatesListing();
        }

        datesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Pankaj 19 Jan 2018
                String date = scroreDatesCoachBeanArrayList.get(i).getSessiondate();
//                String date = datesListing.get(i);

                clickedOnAgeGroup.setBookingDate(date);

                Intent childReport = new Intent(CoachScoreDatesScreen.this, ParentChildReportScreen.class);
                childReport.putExtra("clickedOnAgeGroup", clickedOnAgeGroup);
                startActivity(childReport);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getScoreDatesListing(){
        if(Utilities.isNetworkAvailable(CoachScoreDatesScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("child_id", clickedOnAgeGroup.getUsersId()));
            nameValuePairList.add(new BasicNameValuePair("session_id", clickedOnAgeGroup.getSessionsId()));

            String webServiceUrl = Utilities.BASE_URL + "coach/get_scores_dates";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachScoreDatesScreen.this, nameValuePairList, GET_SCORE_DATES_LISTING, CoachScoreDatesScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachScoreDatesScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case GET_SCORE_DATES_LISTING:

                if (response == null) {
                    Toast.makeText(CoachScoreDatesScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status){

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for(int i=0;i<dataArray.length();i++){
                                scoreDatesCoachBean = new ScoreDatesCoachBean();

                                JSONObject dateObject = dataArray.getJSONObject(i);

                                scoreDatesCoachBean.setSessiondate(dateObject.getString("session_date"));

                                scroreDatesCoachBeanArrayList.add(scoreDatesCoachBean);
                            }

                        } else {
                            Toast.makeText(CoachScoreDatesScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachScoreDatesScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                // Pankaj 19 Jan 2018
                CoachScoreDatesAdapter coachScoreDatesAdapter = new CoachScoreDatesAdapter(CoachScoreDatesScreen.this, scroreDatesCoachBeanArrayList, clickedOnAgeGroup.getUsersId(), clickedOnAgeGroup.getSessionsId());
                datesListView.setAdapter(coachScoreDatesAdapter);
//                datesListView.setAdapter(new ArrayAdapter<>(CoachScoreDatesScreen.this, android.R.layout.simple_list_item_1, datesListing));

                break;
        }
    }
}