package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupAttendanceBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentChildrenListingForSpinnerBlackAdapter;
import com.dunvant.application.parent.adapters.ParentDaysListingAdapter;
import com.dunvant.application.parent.adapters.ParentTermNameListingAdapter;
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

public class ParentViewMarksScreen extends AppCompatActivity implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    TextView title;
    ImageView backButton;
    ImageView searchIcon;
    LinearLayout searchLinearLayout;
    Spinner childrenSpinner;
    Spinner sessionSpinner;
    Button goButton;
    ListView termNameListView;

    ChildBean currentChild;

    private final String GET_CHILDREN_LISTING = "GET_CHILDREN_LISTING";
    private final String GET_SESSIONS_LISTING = "GET_SESSIONS_LISTING";
    private final String GET_ATTENDANCE_LIST = "GET_ATTENDANCE_LIST";

    ArrayList<ChildBean> childrenListing = new ArrayList<>();
    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupAttendanceBean> ageGroupAttendanceListing = new ArrayList<>();

//    ParentChildrenListingForSpinnerAdapter parentChildrenListingForSpinnerAdapter;
    ParentChildrenListingForSpinnerBlackAdapter parentChildrenListingForSpinnerAdapter;
    ParentDaysListingAdapter parentDaysListingAdapter;
    ParentTermNameListingAdapter parentTermNameListingAdapter;

    ChildBean clickedOnChild;
    CampDaysBean clickedOnDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_view_marks_screen);

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
        searchIcon = (ImageView) findViewById(R.id.searchIcon);
        searchLinearLayout = (LinearLayout) findViewById(R.id.searchLinearLayout);
        childrenSpinner = (Spinner) findViewById(R.id.childrenSpinner);
        sessionSpinner = (Spinner) findViewById(R.id.sessionSpinner);
        goButton = (Button) findViewById(R.id.goButton);
        termNameListView = (ListView) findViewById(R.id.termNameListView);

        title.setTypeface(linoType);
        goButton.setTypeface(linoType);

        Intent intent = getIntent();
        if (intent != null) {
            currentChild = (ChildBean) intent.getSerializableExtra("currentChild");
        }

        /*termNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AgeGroupAttendanceBean clickedOnAgeGroup = ageGroupAttendanceListing.get(position);

                if(clickedOnAgeGroup.getTotalScore() != null && !clickedOnAgeGroup.getTotalScore().isEmpty()) {
                    Intent childReport = new Intent(ParentViewMarksScreen.this, ParentChildReportScreen.class);
                    childReport.putExtra("clickedOnAgeGroup", clickedOnAgeGroup);
                    startActivity(childReport);
                } else {
                    Toast.makeText(ParentViewMarksScreen.this, "Score not uploaded yet", Toast.LENGTH_SHORT).show();
                }


            }
        });*/

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childPosition = childrenSpinner.getSelectedItemPosition();
                int sessionPosition = sessionSpinner.getSelectedItemPosition();

                if (childPosition == 0) {
                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                    Toast.makeText(ParentViewMarksScreen.this, "Please select "+verbiage_singular, Toast.LENGTH_SHORT).show();

                } else if (sessionPosition == 0) {
                    Toast.makeText(ParentViewMarksScreen.this, "Please select Session Day", Toast.LENGTH_SHORT).show();
                } else {
                    getAttendanceListing();
                }
            }
        });

        childrenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    daysListing.clear();

                    CampDaysBean daysBean = new CampDaysBean();
                    daysBean.setDay("-1");
                    daysBean.setDayLabel("Choose Session Day");

                    daysListing.add(daysBean);

                    parentDaysListingAdapter = new ParentDaysListingAdapter(ParentViewMarksScreen.this, daysListing);
                    sessionSpinner.setAdapter(parentDaysListingAdapter);
                } else {
                    clickedOnChild = childrenListing.get(position);
                    getSessionsListing();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    clickedOnDay = daysListing.get(position);
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

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchLinearLayout();
            }
        });

        getChildrenListing();
    }


    private void getAttendanceListing() {
        if(Utilities.isNetworkAvailable(ParentViewMarksScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("child_id", clickedOnChild.getId()));
            nameValuePairList.add(new BasicNameValuePair("session_day", clickedOnDay.getDay()));

//            String webServiceUrl = Utilities.BASE_URL + "sessions/child_attendance_list_for_parent";
            String webServiceUrl = Utilities.BASE_URL + "sessions/child_score_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentViewMarksScreen.this, nameValuePairList, GET_ATTENDANCE_LIST, ParentViewMarksScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentViewMarksScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getSessionsListing() {
        if(Utilities.isNetworkAvailable(ParentViewMarksScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("child_id", clickedOnChild.getId()));

            String webServiceUrl = Utilities.BASE_URL + "sessions/session_days_list_for_parent";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentViewMarksScreen.this, nameValuePairList, GET_SESSIONS_LISTING, ParentViewMarksScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentViewMarksScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getChildrenListing(){
        if(Utilities.isNetworkAvailable(ParentViewMarksScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "children/children_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ParentViewMarksScreen.this, GET_CHILDREN_LISTING, ParentViewMarksScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentViewMarksScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_CHILDREN_LISTING:

                childrenListing.clear();

                if (response == null) {
                    Toast.makeText(ParentViewMarksScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();

                    ChildBean childBean = new ChildBean();
                    childBean.setId("-1");

                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                    childBean.setFullName("Choose "+verbiage_singular);

                    childrenListing.add(childBean);

                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");

                            ChildBean childBean = new ChildBean();
                            childBean.setId("-1");
                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                            childBean.setFullName("Choose "+verbiage_singular);
                            childrenListing.add(childBean);

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject childObject = dataArray.getJSONObject(i);
                                childBean = new ChildBean();

                                childBean.setId(childObject.getString("id"));
                                childBean.setAcademiesId(childObject.getString("academies_id"));
                                childBean.setUsername(childObject.getString("username"));
                                childBean.setEmail(childObject.getString("email"));
                                childBean.setGender(childObject.getString("gender"));
                                childBean.setCreatedAt(childObject.getString("created_at"));
                                childBean.setState(childObject.getString("state"));
                                childBean.setFirstName(childObject.getString("first_name"));
                                childBean.setLastName(childObject.getString("last_name"));
                                childBean.setFullName(childObject.getString("full_name"));
                                childBean.setAge(childObject.getString("age"));
                                childBean.setDateOfBirth(childObject.getString("dob"));
                                childBean.setMedicalCondition(childObject.getString("medical_conditions"));

                                childrenListing.add(childBean);
                            }

                        } else {
                            Toast.makeText(ParentViewMarksScreen.this, message, Toast.LENGTH_SHORT).show();

                            ChildBean childBean = new ChildBean();
                            childBean.setId("-1");
                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                            childBean.setFullName("Choose "+verbiage_singular);

                            childrenListing.add(childBean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        ChildBean childBean = new ChildBean();
                        childBean.setId("-1");
                        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                        childBean.setFullName("Choose "+verbiage_singular);

                        childrenListing.add(childBean);

                        Toast.makeText(ParentViewMarksScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

//                parentChildrenListingForSpinnerAdapter = new ParentChildrenListingForSpinnerAdapter(ParentViewMarksScreen.this, childrenListing);
                parentChildrenListingForSpinnerAdapter = new ParentChildrenListingForSpinnerBlackAdapter(ParentViewMarksScreen.this, childrenListing);
                childrenSpinner.setAdapter(parentChildrenListingForSpinnerAdapter);

                int existsAt = -1;

                for(int i=0;i<childrenListing.size();i++){
                    ChildBean childBean = childrenListing.get(i);
                    if(childBean.getId().equalsIgnoreCase(currentChild.getId())){
                        existsAt = i;
                        break;
                    }
                }

                if(existsAt != -1){
                    childrenSpinner.setSelection(existsAt);
                }
                childrenSpinner.setEnabled(false);

                break;

            case GET_SESSIONS_LISTING:

                daysListing.clear();

                if(response == null) {
                    Toast.makeText(ParentViewMarksScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");

                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Choose Session Day");

                            daysListing.add(daysBean);

                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject dayObject = dataArray.getJSONObject(i);
                                daysBean = new CampDaysBean();

                                daysBean.setDay(dayObject.getString("day"));
                                daysBean.setDayLabel(dayObject.getString("day_label"));

                                daysListing.add(daysBean);
                            }
                        } else {
                            Toast.makeText(ParentViewMarksScreen.this, message, Toast.LENGTH_SHORT).show();
                            daysListing.clear();

                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Choose Session Day");

                            daysListing.add(daysBean);

                            parentDaysListingAdapter = new ParentDaysListingAdapter(ParentViewMarksScreen.this, daysListing);
                            sessionSpinner.setAdapter(parentDaysListingAdapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        CampDaysBean daysBean = new CampDaysBean();
                        daysBean.setDay("-1");
                        daysBean.setDayLabel("Choose Session Day");

                        daysListing.add(daysBean);

                        Toast.makeText(ParentViewMarksScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentDaysListingAdapter = new ParentDaysListingAdapter(ParentViewMarksScreen.this, daysListing);
                sessionSpinner.setAdapter(parentDaysListingAdapter);

                break;

            case GET_ATTENDANCE_LIST:

                ageGroupAttendanceListing.clear();

                if (response == null) {
                    Toast.makeText(ParentViewMarksScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {

                    searchLinearLayout.setVisibility(View.GONE);

                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            AgeGroupAttendanceBean ageGroupAttendanceBean;

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject ageGroupAttendanceObject = dataArray.getJSONObject(i);
                                ageGroupAttendanceBean = new AgeGroupAttendanceBean();

                                ageGroupAttendanceBean.setSessionsId(ageGroupAttendanceObject.getString("session_id"));
                                ageGroupAttendanceBean.setDay(ageGroupAttendanceObject.getString("day"));
                                ageGroupAttendanceBean.setChildName(ageGroupAttendanceObject.getString("child_name"));
                                ageGroupAttendanceBean.setBookingDate(ageGroupAttendanceObject.getString("booking_date"));
                                ageGroupAttendanceBean.setAgeGroupsId(ageGroupAttendanceObject.getString("age_groups_id"));
                                ageGroupAttendanceBean.setGroupName(ageGroupAttendanceObject.getString("group_name"));
                                ageGroupAttendanceBean.setTermsName(ageGroupAttendanceObject.getString("terms_name"));
                                ageGroupAttendanceBean.setTermFromDate(ageGroupAttendanceObject.getString("term_from_date"));
                                ageGroupAttendanceBean.setTermToDate(ageGroupAttendanceObject.getString("term_to_date"));
                                ageGroupAttendanceBean.setUsersId(ageGroupAttendanceObject.getString("child_id"));
                                ageGroupAttendanceBean.setDayLabel(ageGroupAttendanceObject.getString("day_label"));
                                ageGroupAttendanceBean.setScoreId(ageGroupAttendanceObject.getString("score_id"));

                                /*ageGroupAttendanceBean.setTotalScore(ageGroupAttendanceObject.getString("total_score"));
                                ageGroupAttendanceBean.setSessionsId(ageGroupAttendanceObject.getString("sessions_id"));
                                ageGroupAttendanceBean.setAgeGroupId(ageGroupAttendanceObject.getString("age_groups_id"));
                                ageGroupAttendanceBean.setGroupName(ageGroupAttendanceObject.getString("group_name"));
                                ageGroupAttendanceBean.setTermsName(ageGroupAttendanceObject.getString("terms_name"));
                                ageGroupAttendanceBean.setUsersId(ageGroupAttendanceObject.getString("users_id"));
                                ageGroupAttendanceBean.setChildName(ageGroupAttendanceObject.getString("child_name"));
                                ageGroupAttendanceBean.setBookingDates(ageGroupAttendanceObject.getString("booking_dates"));

                                JSONArray attendanceDatesArray = ageGroupAttendanceObject.getJSONArray("attendance_dates");
                                ArrayList<AttendanceDateBean> attendanceDatesList = new ArrayList<>();
                                AttendanceDateBean attendanceDateBean;
                                for(int j=0;j<attendanceDatesArray.length();j++) {
                                    JSONObject attendanceDateObject = attendanceDatesArray.getJSONObject(j);
                                    attendanceDateBean = new AttendanceDateBean();
                                    attendanceDateBean.setBookedSessionDate(attendanceDateObject.getString("booked_session_date"));
                                    attendanceDateBean.setShowBookedSessionDate(attendanceDateObject.getString("booked_session_date_formatted"));
                                    attendanceDateBean.setStatus(attendanceDateObject.getString("status"));

                                    attendanceDatesList.add(attendanceDateBean);
                                }
                                ageGroupAttendanceBean.setAttendanceDatesList(attendanceDatesList);

                                ageGroupAttendanceBean.setAttendedSessionsCount(ageGroupAttendanceObject.getString("attended_sessions_count"));
                                ageGroupAttendanceBean.setTotalSessionsCount(ageGroupAttendanceObject.getString("total_sessions_count"));
                                ageGroupAttendanceBean.setRemainingSessionsCount(ageGroupAttendanceObject.getString("remaining_sessions_count"));*/

                                ageGroupAttendanceListing.add(ageGroupAttendanceBean);
                            }
                        } else {
                            Toast.makeText(ParentViewMarksScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentViewMarksScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentTermNameListingAdapter = new ParentTermNameListingAdapter(ParentViewMarksScreen.this, ageGroupAttendanceListing);
                termNameListView.setAdapter(parentTermNameListingAdapter);

                break;
        }
    }

    public void showSearchLinearLayout(){
        searchLinearLayout.setVisibility(View.VISIBLE);
    }
}
