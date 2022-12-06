package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AcademyLocationBean;
import com.dunvant.application.beans.AcademySessionBean;
import com.dunvant.application.beans.AcademySessionDetailBean;
import com.dunvant.application.beans.CoachingAcademyBean;
import com.dunvant.application.beans.TermBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentAcademySessionsExpandableAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParentBookAcademyThree extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
//    TextView continueBelow;
//    TextView four;
//    TextView easySteps;
    TextView chooseSession;
    ExpandableListView sessionsExpandableListView;

    CoachingAcademyBean clickedOnAcademy;
    TermBean clickedOnTerm;
    AcademyLocationBean clickedOnLocation;

    private final String GET_SESSIONS_DATA = "GET_SESSIONS_DATA";
    ArrayList<AcademySessionBean> sessionsList = new ArrayList<>();
    ParentAcademySessionsExpandableAdapter parentAcademySessionsExpandableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_book_academy_three);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
//        continueBelow = (TextView) findViewById(R.id.continueBelow);
//        four = (TextView) findViewById(R.id.four);
//        easySteps = (TextView) findViewById(R.id.easySteps);
        chooseSession = (TextView) findViewById(R.id.chooseSession);
        sessionsExpandableListView = (ExpandableListView) findViewById(R.id.sessionsExpandableListView);
        parentAcademySessionsExpandableAdapter = new ParentAcademySessionsExpandableAdapter(ParentBookAcademyThree.this, sessionsList);
        sessionsExpandableListView.setAdapter(parentAcademySessionsExpandableAdapter);

        changeFonts();

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnAcademy = (CoachingAcademyBean) intent.getSerializableExtra("clickedOnAcademy");
            clickedOnTerm = (TermBean) intent.getSerializableExtra("clickedOnTerm");
            clickedOnLocation = (AcademyLocationBean) intent.getSerializableExtra("clickedOnLocation");

            getSessionData();
        }

        sessionsExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                AcademySessionDetailBean clickedOnSession = sessionsList.get(groupPosition).getSessionDetailList().get(childPosition);

                if(clickedOnSession.isBookingClosed()){
                    Toast.makeText(ParentBookAcademyThree.this, "Booking Closed for this session", Toast.LENGTH_SHORT).show();
                } else {
                    Intent bookAcademyFour = new Intent(ParentBookAcademyThree.this, ParentBookAcademyFour.class);
                    bookAcademyFour.putExtra("clickedOnAcademy", clickedOnAcademy);
                    bookAcademyFour.putExtra("clickedOnTerm", clickedOnTerm);
                    bookAcademyFour.putExtra("clickedOnLocation", clickedOnLocation);
                    bookAcademyFour.putExtra("clickedOnSession", clickedOnSession);
                    startActivity(bookAcademyFour);
                }

                return true;
            }
        });

        sessionsExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    sessionsExpandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getSessionData(){
        if(Utilities.isNetworkAvailable(ParentBookAcademyThree.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("coaching_programs_id", clickedOnAcademy.getAcademyId()));
            nameValuePairList.add(new BasicNameValuePair("terms_id", clickedOnTerm.getTermId()));
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocation.getLocationId()));

            String webServiceUrl = Utilities.BASE_URL + "sessions/session_list";


//            nameValuePairList.add(new BasicNameValuePair("coaching_programs_id", ""));
//            nameValuePairList.add(new BasicNameValuePair("terms_id", ""));
//            nameValuePairList.add(new BasicNameValuePair("locations_id", ""));
//            nameValuePairList.add(new BasicNameValuePair("select_day", ""));
//            nameValuePairList.add(new BasicNameValuePair("age_group_id", ""));
//
//            String webServiceUrl = Utilities.BASE_URL + "sessions/get_session_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookAcademyThree.this, nameValuePairList, GET_SESSIONS_DATA, ParentBookAcademyThree.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookAcademyThree.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_SESSIONS_DATA:

                sessionsList.clear();

                if(response == null) {
                    Toast.makeText(ParentBookAcademyThree.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONObject dataObject = responseObject.getJSONObject("data");
                            Iterator<String> keys = dataObject.keys();

                            AcademySessionBean sessionBean;

                            while(keys.hasNext()) {
                                String key = keys.next();

                                sessionBean = new AcademySessionBean();
                                sessionBean.setDayName(key);

                                JSONArray sessionDetailsArray = dataObject.getJSONArray(key);
                                ArrayList<AcademySessionDetailBean> sessionDetailList = new ArrayList<>();
                                AcademySessionDetailBean sessionDetailBean;
                                for (int i=0;i<sessionDetailsArray.length();i++) {
                                    JSONObject sessionDetailObject = sessionDetailsArray.getJSONObject(i);
                                    sessionDetailBean = new AcademySessionDetailBean();

                                    sessionDetailBean.setSessionDetailId(sessionDetailObject.getString("session_details_id"));
                                    sessionDetailBean.setSessionDetailTitle(sessionDetailObject.getString("session_detail_title"));
                                    sessionDetailBean.setSessionId(sessionDetailObject.getString("sessions_id"));
                                    sessionDetailBean.setGroupName(sessionDetailObject.getString("group_name"));
                                    sessionDetailBean.setFromAge(sessionDetailObject.getInt("from_age"));
                                    sessionDetailBean.setToAge(sessionDetailObject.getInt("to_age"));
                                    sessionDetailBean.setDay(sessionDetailObject.getString("day"));
                                    sessionDetailBean.setFromDate(sessionDetailObject.getString("from_date"));
                                    sessionDetailBean.setToDate(sessionDetailObject.getString("to_date"));
                                    sessionDetailBean.setShowFromDate(sessionDetailObject.getString("from_date_formatted"));
                                    sessionDetailBean.setShowToDate(sessionDetailObject.getString("to_date_formatted"));
                                    sessionDetailBean.setNumberOfWeeks(sessionDetailObject.getString("number_of_weeks"));
                                    sessionDetailBean.setStartTime(sessionDetailObject.getString("start_time"));
                                    sessionDetailBean.setEndTime(sessionDetailObject.getString("end_time"));
                                    sessionDetailBean.setShowStartTime(sessionDetailObject.getString("start_time_formatted"));
                                    sessionDetailBean.setShowEndTime(sessionDetailObject.getString("end_time_formatted"));
                                    sessionDetailBean.setNumberOfHours(sessionDetailObject.getString("number_of_hours"));
                                    sessionDetailBean.setCost(sessionDetailObject.getString("cost"));
                                    sessionDetailBean.setIsSelectiveAllowed(sessionDetailObject.getString("is_selective_allowed"));
                                    sessionDetailBean.setDayLabel(sessionDetailObject.getString("day_label"));
                                    sessionDetailBean.setCoachName(sessionDetailObject.getString("coach_name"));
                                    sessionDetailBean.setThresholdCrossed(sessionDetailObject.getBoolean("threshold_crossed"));
                                    sessionDetailBean.setCoachingProgramName(sessionDetailObject.getString("coaching_program_name"));
                                    sessionDetailBean.setSessionGender(sessionDetailObject.getString("session_gender"));
                                    sessionDetailBean.setSessionGenderLabel(sessionDetailObject.getString("session_gender_label"));
                                    sessionDetailBean.setBookingClosed(sessionDetailObject.getBoolean("booking_closed"));
                                    sessionDetailBean.setSessionExpiresInLabel(sessionDetailObject.getString("session_expires_in_label"));

                                    sessionDetailList.add(sessionDetailBean);
                                }
                                sessionBean.setSessionDetailList(sessionDetailList);

                                sessionsList.add(sessionBean);
                            }

                        } else {
                            Toast.makeText(ParentBookAcademyThree.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookAcademyThree.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentAcademySessionsExpandableAdapter.notifyDataSetChanged();

                break;
        }
    }

    private void changeFonts(){
        title.setTypeface(linoType);
//        continueBelow.setTypeface(helvetica);
//        four.setTypeface(helvetica);
//        easySteps.setTypeface(helvetica);
        chooseSession.setTypeface(linoType);
    }

}