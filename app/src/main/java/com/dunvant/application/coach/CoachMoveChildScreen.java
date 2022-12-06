package com.dunvant.application.coach;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AcademySessionDateBean;
import com.dunvant.application.beans.AgeGroupBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.DatesResultBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachAvailableDatesAdapter;
import com.dunvant.application.coach.adapters.CoachLocationListingAdapter;
import com.dunvant.application.coach.adapters.CoachOnlyAgeGroupListingAdapter;
import com.dunvant.application.coach.adapters.CoachSessionListingAdapter;
import com.dunvant.application.coach.fragments.CoachManageChildrenFragment;
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

public class CoachMoveChildScreen extends AppCompatActivity implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    TextView childName;
    TextView coachingProgramName;
    TextView lblLocations;
    TextView groupName;
    Spinner locationSpinner;
    Spinner sessionSpinner;
    Spinner ageGroupSpinner;
    CheckBox selectAllDates;
    ListView datesListView;
    Button submit;

    DatesResultBean clickedOnDateResult;

    private final String GET_LOCATION_LISTING = "GET_LOCATION_LISTING";
    private final String GET_SESSIONS_LISTING = "GET_SESSIONS_LISTING";
    private final String GET_AGE_GROUP_LISTING = "GET_AGE_GROUP_LISTING";
    private final String GET_DATES_LISTING = "GET_DATES_LISTING";
    private final String MOVE_CHILD = "MOVE_CHILD";

    ArrayList<CampLocationBean> locationsList = new ArrayList<>();
    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupBean> ageGroupsListing = new ArrayList<>();
    ArrayList<AcademySessionDateBean> availableDatesList = new ArrayList<>();

    CoachLocationListingAdapter coachLocationListingAdapter;
    CoachSessionListingAdapter coachSessionListingAdapter;
    CoachOnlyAgeGroupListingAdapter coachOnlyAgeGroupListingAdapter;
    CoachAvailableDatesAdapter coachAvailableDatesAdapter;

    CampLocationBean clickedOnLocation;
    CampDaysBean clickedOnDay;
    AgeGroupBean clickedOnAgeGroup;
    String strChosenDates = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_activity_move_child_screen);

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
        coachingProgramName = (TextView) findViewById(R.id.coachingProgramName);
        lblLocations = (TextView) findViewById(R.id.lblLocations);
        groupName = (TextView) findViewById(R.id.groupName);
        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        sessionSpinner = (Spinner) findViewById(R.id.sessionSpinner);
        ageGroupSpinner = (Spinner) findViewById(R.id.ageGroupSpinner);
        selectAllDates = (CheckBox) findViewById(R.id.selectAllDates);
        datesListView = (ListView) findViewById(R.id.datesListView);
        submit = (Button) findViewById(R.id.submit);


        locationSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        sessionSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ageGroupSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        changeFonts();

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnDateResult = (DatesResultBean) intent.getSerializableExtra("clickedOnDateResult");

            childName.setText(clickedOnDateResult.getChildName());
            coachingProgramName.setText(clickedOnDateResult.getCoachingProgramName()+", "+clickedOnDateResult.getTermsName()+", "+clickedOnDateResult.getDayLabel());
            lblLocations.setText(clickedOnDateResult.getLocationName()+",");
            groupName.setText(clickedOnDateResult.getGroupName());

            getLocationListing();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int locationPosition = locationSpinner.getSelectedItemPosition();
                int sessionPosition = sessionSpinner.getSelectedItemPosition();
                int ageGroupPosition = ageGroupSpinner.getSelectedItemPosition();

                boolean atleastOneDateSelected = false;
                for (AcademySessionDateBean sessionDateBean: availableDatesList) {
                    if(sessionDateBean.isSelected()) {
                        atleastOneDateSelected = true;
                    }
                }

                if(locationPosition == 0) {
                    Toast.makeText(CoachMoveChildScreen.this, "Please select Location", Toast.LENGTH_SHORT).show();
                } else if (sessionPosition == 0) {
                    Toast.makeText(CoachMoveChildScreen.this, "Please select Session", Toast.LENGTH_SHORT).show();
                } else if (ageGroupPosition == 0) {
                    Toast.makeText(CoachMoveChildScreen.this, "Please select Age Group", Toast.LENGTH_SHORT).show();
                } else if (!atleastOneDateSelected) {
                    Toast.makeText(CoachMoveChildScreen.this, "Please select atleast one date", Toast.LENGTH_SHORT).show();
                } else {
                    for(AcademySessionDateBean sessionDateBean: availableDatesList) {
                        if(sessionDateBean.isSelected()){
                            strChosenDates += sessionDateBean.getDate()+",";
                        }
                    }

                    if (strChosenDates != null && strChosenDates.length() > 0 && strChosenDates.charAt(strChosenDates.length()-1)==',') {
                        strChosenDates = strChosenDates.substring(0, strChosenDates.length()-1);
                    }

                    moveChild();
                }
            }
        });

        selectAllDates.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(availableDatesList != null && !availableDatesList.isEmpty()){
                    if(isChecked) {
                        for (AcademySessionDateBean sessionDateBean: availableDatesList) {
                            sessionDateBean.setSelected(true);
                        }
                    } else {
                        /*for (AcademySessionDateBean sessionDateBean: availableDatesList) {
                            sessionDateBean.setSelected(false);
                        }*/
                    }
                    coachAvailableDatesAdapter.notifyDataSetChanged();
                }
            }
        });

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0) {
                    daysListing.clear();

                    // 0th Element Choose Session
                    CampDaysBean daysBean = new CampDaysBean();
                    daysBean.setDay("-1");
                    daysBean.setDayLabel("Choose Session");

                    daysListing.add(daysBean);

                    coachSessionListingAdapter = new CoachSessionListingAdapter(CoachMoveChildScreen.this, daysListing);
                    sessionSpinner.setAdapter(coachSessionListingAdapter);
                } else {
                    clickedOnLocation = locationsList.get(position);
                    getSessionDaysListing();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    ageGroupsListing.clear();

                    AgeGroupBean ageGroupBean = new AgeGroupBean();
                    ageGroupBean.setAgeGroupId("-1");
                    ageGroupBean.setGroupName("Choose Age Group");

                    ageGroupsListing.add(ageGroupBean);

                    coachOnlyAgeGroupListingAdapter = new CoachOnlyAgeGroupListingAdapter(CoachMoveChildScreen.this, ageGroupsListing);
                    ageGroupSpinner.setAdapter(coachOnlyAgeGroupListingAdapter);
                } else {
                    clickedOnDay = daysListing.get(position);
                    getAgeGroupsListing();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ageGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    availableDatesList.clear();

                    coachAvailableDatesAdapter = new CoachAvailableDatesAdapter(CoachMoveChildScreen.this, availableDatesList, CoachMoveChildScreen.this);
                    datesListView.setAdapter(coachAvailableDatesAdapter);

                } else {
                    clickedOnAgeGroup = ageGroupsListing.get(position);

                    getDatesListing();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void updateSelectAll() {
        boolean allSelected = true;
        for (AcademySessionDateBean sessionDateBean: availableDatesList) {
            if(!sessionDateBean.isSelected()) {
                allSelected = false;
                break;
            }
        }
        if(allSelected) {
            selectAllDates.setChecked(true);
        } else {
            selectAllDates.setChecked(false);
        }
    }

    private void getLocationListing(){
        if(Utilities.isNetworkAvailable(CoachMoveChildScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "coach/locations_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(CoachMoveChildScreen.this, GET_LOCATION_LISTING, CoachMoveChildScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachMoveChildScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getSessionDaysListing(){
        if(Utilities.isNetworkAvailable(CoachMoveChildScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_ids", clickedOnLocation.getLocationId()));
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocation.getLocationId()));

            String webServiceUrl = Utilities.BASE_URL + "coach/session_days_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachMoveChildScreen.this, nameValuePairList, GET_SESSIONS_LISTING, CoachMoveChildScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachMoveChildScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAgeGroupsListing(){
        if(Utilities.isNetworkAvailable(CoachMoveChildScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocation.getLocationId()));
            nameValuePairList.add(new BasicNameValuePair("locations_ids", clickedOnLocation.getLocationId()));
            nameValuePairList.add(new BasicNameValuePair("session_days", clickedOnDay.getDay()));

            String webServiceUrl = Utilities.BASE_URL + "coach/age_group_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachMoveChildScreen.this, nameValuePairList, GET_AGE_GROUP_LISTING, CoachMoveChildScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachMoveChildScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getDatesListing(){
        if(Utilities.isNetworkAvailable(CoachMoveChildScreen.this)) {

            String strSessionDetail = "[{\"sessions_id\":\""+clickedOnAgeGroup.getSessionId()+"\",\"child_ids\":\""+clickedOnDateResult.getUsersId()+"\"}]";

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("sessions_detail", strSessionDetail));

            String webServiceUrl = Utilities.BASE_URL + "sessions/summary";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachMoveChildScreen.this, nameValuePairList, GET_DATES_LISTING, CoachMoveChildScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachMoveChildScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void moveChild(){
        if(Utilities.isNetworkAvailable(CoachMoveChildScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("child_id", clickedOnDateResult.getUsersId()));
            nameValuePairList.add(new BasicNameValuePair("sessions_id", clickedOnDateResult.getSessionId()));
            nameValuePairList.add(new BasicNameValuePair("old_session_id", clickedOnDateResult.getSessionId()));
//            nameValuePairList.add(new BasicNameValuePair("new_sessions_id", clickedOnAgeGroup.getSessionId()));
            nameValuePairList.add(new BasicNameValuePair("new_session_id", clickedOnAgeGroup.getSessionId()));
            nameValuePairList.add(new BasicNameValuePair("move_order_id", clickedOnDateResult.getOrdersIds()));
            nameValuePairList.add(new BasicNameValuePair("new_booking_dates", strChosenDates));

            String webServiceUrl = Utilities.BASE_URL + "coach/move_child";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachMoveChildScreen.this, nameValuePairList, MOVE_CHILD, CoachMoveChildScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachMoveChildScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_LOCATION_LISTING:

                locationsList.clear();

                // 0th Element Choose Location
                CampLocationBean locationBean = new CampLocationBean();
                locationBean.setLocationId("-1");
                locationBean.setLocationName("Choose Location");

                locationsList.add(locationBean);

                if(response == null) {
                    Toast.makeText(CoachMoveChildScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

//                            JSONArray dataArray = responseObject.getJSONArray("data");
                            JSONArray dataArray = responseObject.getJSONArray("data_move_child");
                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject locationObject = dataArray.getJSONObject(i);
                                locationBean = new CampLocationBean();

                                locationBean.setLocationId(locationObject.getString("locations_id"));
                                locationBean.setLocationName(locationObject.getString("locations_name"));
                                locationBean.setDescription(locationObject.getString("description"));
                                locationBean.setLatitude(locationObject.getString("latitude"));
                                locationBean.setLongitude(locationObject.getString("longitude"));

                                locationsList.add(locationBean);
                            }

                        } else {
                            Toast.makeText(CoachMoveChildScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachMoveChildScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachLocationListingAdapter = new CoachLocationListingAdapter(CoachMoveChildScreen.this, locationsList);
                locationSpinner.setAdapter(coachLocationListingAdapter);
                break;

            case GET_SESSIONS_LISTING:

                daysListing.clear();

                // 0th Element Choose Session
                CampDaysBean daysBean = new CampDaysBean();
                daysBean.setDay("-1");
                daysBean.setDayLabel("Choose Session");

                daysListing.add(daysBean);

                if(response == null) {
                    Toast.makeText(CoachMoveChildScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

//                            JSONArray dataArray = responseObject.getJSONArray("data");
                            JSONArray dataArray = responseObject.getJSONArray("data_move_child");
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject daysObject = dataArray.getJSONObject(i);
                                daysBean = new CampDaysBean();
                                daysBean.setDay(daysObject.getString("day"));
                                daysBean.setDayLabel(daysObject.getString("day_label"));

                                daysListing.add(daysBean);
                            }

                        } else {
                            Toast.makeText(CoachMoveChildScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachMoveChildScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachSessionListingAdapter = new CoachSessionListingAdapter(CoachMoveChildScreen.this, daysListing);
                sessionSpinner.setAdapter(coachSessionListingAdapter);

                break;

            case GET_AGE_GROUP_LISTING:

                ageGroupsListing.clear();

                // 0th Element Choose Age Group
                AgeGroupBean ageGroupBean = new AgeGroupBean();
                ageGroupBean.setAgeGroupId("-1");
                ageGroupBean.setGroupName("Choose Age Group");

                ageGroupsListing.add(ageGroupBean);

                if (response == null) {
                    Toast.makeText(CoachMoveChildScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

//                            JSONArray dataArray = responseObject.getJSONArray("data");
                            JSONArray dataArray = responseObject.getJSONArray("data_move_child");
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject ageGroupObject = dataArray.getJSONObject(i);
                                ageGroupBean = new AgeGroupBean();
                                ageGroupBean.setSessionId(ageGroupObject.getString("sessions_id"));
                                ageGroupBean.setAgeGroupId(ageGroupObject.getString("age_groups_id"));
                                ageGroupBean.setGroupName(ageGroupObject.getString("group_name"));

                                ageGroupsListing.add(ageGroupBean);
                            }


                        } else {
                            Toast.makeText(CoachMoveChildScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachMoveChildScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachOnlyAgeGroupListingAdapter = new CoachOnlyAgeGroupListingAdapter(CoachMoveChildScreen.this, ageGroupsListing);
                ageGroupSpinner.setAdapter(coachOnlyAgeGroupListingAdapter);

                break;

            case GET_DATES_LISTING:

                availableDatesList.clear();

                if(response == null) {
                    Toast.makeText(CoachMoveChildScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for (int i=0; i<dataArray.length(); i++) {
                                JSONObject summaryObject = dataArray.getJSONObject(i);

                                JSONArray availableDatesArray = summaryObject.getJSONArray("available_dates");

                                if(availableDatesArray.length() == 0) {
                                    Toast.makeText(CoachMoveChildScreen.this, "No dates available", Toast.LENGTH_SHORT).show();
                                }

                                for (int j=0; j<availableDatesArray.length(); j++) {
                                    JSONObject dateObject = availableDatesArray.getJSONObject(j);
                                    AcademySessionDateBean dateBean = new AcademySessionDateBean();
                                    dateBean.setDate(dateObject.getString("value"));
                                    dateBean.setShowDate(dateObject.getString("readable_date"));
//                                    dateBean.setSelected(false);
                                    dateBean.setSelected(true);
                                    availableDatesList.add(dateBean);
                                }
                            }

                        } else {
                            Toast.makeText(CoachMoveChildScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachMoveChildScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachAvailableDatesAdapter = new CoachAvailableDatesAdapter(CoachMoveChildScreen.this, availableDatesList, CoachMoveChildScreen.this);
                datesListView.setAdapter(coachAvailableDatesAdapter);

                Utilities.setListViewHeightBasedOnChildren(datesListView);

                selectAllDates.setChecked(true);

                break;

            case MOVE_CHILD:

                if(response == null) {
                    Toast.makeText(CoachMoveChildScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(CoachMoveChildScreen.this, message, Toast.LENGTH_SHORT).show();

                        if(status) {
                            finish();
                            CoachManageChildrenFragment.comingFromMoveChild = true;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachMoveChildScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void changeFonts() {
        title.setTypeface(linoType);
        childName.setTypeface(linoType);
        coachingProgramName.setTypeface(helvetica);
        groupName.setTypeface(helvetica);
        selectAllDates.setTypeface(helvetica);
        submit.setTypeface(linoType);
    }
}