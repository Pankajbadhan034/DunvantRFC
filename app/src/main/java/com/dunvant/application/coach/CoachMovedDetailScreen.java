package com.dunvant.application.coach;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AttendanceDateBean;
import com.dunvant.application.beans.DatesResultBean;
import com.dunvant.application.beans.MovedDetailBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachMovedDetailsAdapter;
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

public class CoachMovedDetailScreen extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    ListView movedDetailsListView;

    DatesResultBean clickedOnDateResult;

    private final String GET_MOVED_DETAILS = "GET_MOVED_DETAILS";

    ArrayList<MovedDetailBean> movedDetailsListing = new ArrayList<>();

    CoachMovedDetailsAdapter coachMovedDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_activity_moved_detail_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = findViewById(R.id.backButton);
        movedDetailsListView = findViewById(R.id.movedDetailsListView);

        coachMovedDetailsAdapter = new CoachMovedDetailsAdapter(CoachMovedDetailScreen.this, movedDetailsListing);
        movedDetailsListView.setAdapter(coachMovedDetailsAdapter);

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnDateResult = (DatesResultBean) intent.getSerializableExtra("clickedOnDateResult");
            getMovedDetails();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getMovedDetails(){
        if(Utilities.isNetworkAvailable(CoachMovedDetailScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("users_id", clickedOnDateResult.getUsersId()));
            nameValuePairList.add(new BasicNameValuePair("session_id", clickedOnDateResult.getSessionId()));

            String webServiceUrl = Utilities.BASE_URL + "coach/child_moved_sessions_data";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachMovedDetailScreen.this, nameValuePairList, GET_MOVED_DETAILS, CoachMovedDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachMovedDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_MOVED_DETAILS:
                if(response == null){
                    Toast.makeText(CoachMovedDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");

                            MovedDetailBean movedDetailBean;
                            for(int i=0; i<dataArray.length(); i++){
                                JSONObject movedDetailObject = dataArray.getJSONObject(i);
                                movedDetailBean = new MovedDetailBean();

                                movedDetailBean.setSessionsId(movedDetailObject.getString("sessions_id"));
                                movedDetailBean.setCoachingProgramsId(movedDetailObject.getString("coaching_programs_id"));
                                movedDetailBean.setCoachingProgramsName(movedDetailObject.getString("coaching_programs_name"));
                                movedDetailBean.setTermsId(movedDetailObject.getString("terms_id"));
                                movedDetailBean.setTermsName(movedDetailObject.getString("terms_name"));
                                movedDetailBean.setLocationsId(movedDetailObject.getString("locations_id"));
                                movedDetailBean.setLocationsName(movedDetailObject.getString("locations_name"));
                                movedDetailBean.setAgeGroupsId(movedDetailObject.getString("age_groups_id"));
                                movedDetailBean.setGroupName(movedDetailObject.getString("group_name"));
                                movedDetailBean.setFromAge(movedDetailObject.getString("from_age"));
                                movedDetailBean.setToAge(movedDetailObject.getString("to_age"));
                                movedDetailBean.setDay(movedDetailObject.getString("day"));
                                movedDetailBean.setUsersId(movedDetailObject.getString("users_id"));
                                movedDetailBean.setChildName(movedDetailObject.getString("child_name"));
                                movedDetailBean.setOrdersId(movedDetailObject.getString("orders_ids"));
                                movedDetailBean.setBookingDates(movedDetailObject.getString("booking_dates"));
                                movedDetailBean.setStartTimeFormatted(movedDetailObject.getString("start_time_formatted"));
                                movedDetailBean.setEndTimeFormatted(movedDetailObject.getString("end_time_formatted"));
                                movedDetailBean.setTitle(movedDetailObject.getString("title"));
                                movedDetailBean.setDayLabel(movedDetailObject.getString("day_label"));

                                JSONArray attendanceArray = movedDetailObject.getJSONArray("attendance");
                                ArrayList<AttendanceDateBean> attendanceListing = new ArrayList<>();
                                AttendanceDateBean attendanceDateBean;
                                for(int j=0; j<attendanceArray.length(); j++){
                                    JSONObject attendanceObject = attendanceArray.getJSONObject(j);
                                    attendanceDateBean = new AttendanceDateBean();

                                    attendanceDateBean.setBookedSessionDate(attendanceObject.getString("booked_session_date"));
                                    attendanceDateBean.setStatus(attendanceObject.getString("status"));

                                    attendanceListing.add(attendanceDateBean);
                                }
                                movedDetailBean.setAttendanceListing(attendanceListing);

                                movedDetailsListing.add(movedDetailBean);
                            }

                            coachMovedDetailsAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(CoachMovedDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachMovedDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}