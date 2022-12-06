package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.Chapter;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupAttendanceBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.RecycleExpBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentAgeGroupSpinnerAdapter;
import com.dunvant.application.parent.adapters.ParentChildrenListingForSpinnerBlackAdapter;
import com.dunvant.application.parent.adapters.ParentDaysListingAdapter;
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

import androidx.appcompat.app.AppCompatActivity;

public class ParentViewMidweekAttendanceScreen extends AppCompatActivity implements IWebServiceCallback {
    String childId;
    String selectedLocationId;
    String midweek_session;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    TextView title;
    ImageView backButton;
    ImageView searchIcon;
    LinearLayout searchLinearLayout;
    Spinner childrenSpinner;
    Spinner locationSpinner;
    Spinner midweekSpinner;
    Button go;
   // ListView attendanceListView;
   // Button sessionsAttended;
   // Button sessionsRemaining;

    ChildBean currentChild;

    private final String GET_CHILDREN_LISTING = "GET_CHILDREN_LISTING";
    private final String GET_LOCATION_LISTING = "GET_LOCATION_LISTING";
    private final String GET_ATTENDANCE_LISTING = "GET_ATTENDANCE_LISTING";
    private final String GO_BUTTON = "GO_BUTTON";

    ArrayList<ChildBean> childrenListing = new ArrayList<>();
    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupAttendanceBean> ageGroupAttendanceListing = new ArrayList<>();

    ParentChildrenListingForSpinnerBlackAdapter parentChildrenListingForSpinnerAdapter;
    ParentDaysListingAdapter parentDaysListingAdapter;
    ParentAgeGroupSpinnerAdapter parentAgeGroupSpinnerAdapter;

    ChildBean clickedOnChild;
    CampDaysBean clickedOnDay;
    AgeGroupAttendanceBean clickedOnAgeGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_view_midweek_attendance_activity);

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
        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        midweekSpinner = (Spinner) findViewById(R.id.midweekSpinner);
        go = (Button) findViewById(R.id.go);
       // attendanceListView = (ListView) findViewById(R.id.attendanceListView);
       // sessionsAttended = (Button) findViewById(R.id.sessionsAttended);
      //  sessionsRemaining = (Button) findViewById(R.id.sessionsRemaining);

        Intent intent = getIntent();
        if (intent != null) {
            currentChild = (ChildBean) intent.getSerializableExtra("currentChild");
        }

       // attendanceListView.setVisibility(View.GONE);
       // sessionsAttended.setVisibility(View.GONE);
      //  sessionsRemaining.setVisibility(View.GONE);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Utilities.isNetworkAvailable(ParentViewMidweekAttendanceScreen.this)) {
                    Toast.makeText(ParentViewMidweekAttendanceScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    return;
                }

                int childrenPosition = childrenSpinner.getSelectedItemPosition();
                int sessionPosition = locationSpinner.getSelectedItemPosition();
                int ageGroupPosition = midweekSpinner.getSelectedItemPosition();

                if(childrenPosition == 0) {
                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                    Toast.makeText(ParentViewMidweekAttendanceScreen.this, "Please choose "+verbiage_singular, Toast.LENGTH_SHORT).show();

                } else if (sessionPosition == 0) {
                    Toast.makeText(ParentViewMidweekAttendanceScreen.this, "Please choose Location", Toast.LENGTH_SHORT).show();
                }
                else if (ageGroupPosition == 0) {
                    Toast.makeText(ParentViewMidweekAttendanceScreen.this, "Please choose Midweek Package", Toast.LENGTH_SHORT).show();
                }
                else {
//                    clickedOnAgeGroup = ageGroupAttendanceListing.get(ageGroupPosition);
//
                    midweek_session =  ageGroupAttendanceListing.get(ageGroupPosition).getSessionsId();
                   // showAttendanceData();

                    goMethod();

                }
            }
        });

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ageGroupAttendanceListing.clear();

                    AgeGroupAttendanceBean ageGroupAttendanceBean = new AgeGroupAttendanceBean();
                    ageGroupAttendanceBean.setGroupName("Choose Midweek Package");

                    ageGroupAttendanceListing.add(ageGroupAttendanceBean);

                    parentAgeGroupSpinnerAdapter = new ParentAgeGroupSpinnerAdapter(ParentViewMidweekAttendanceScreen.this, ageGroupAttendanceListing);
                    midweekSpinner.setAdapter(parentAgeGroupSpinnerAdapter);
                } else {
                    clickedOnDay = daysListing.get(position);
                    selectedLocationId = clickedOnDay.getDay();
                    getAttendanceListing();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        childrenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    daysListing.clear();

                    CampDaysBean daysBean = new CampDaysBean();
                    daysBean.setDay("-1");
                    daysBean.setDayLabel("Choose Location");

                    daysListing.add(daysBean);

                    parentDaysListingAdapter = new ParentDaysListingAdapter(ParentViewMidweekAttendanceScreen.this, daysListing);
                    locationSpinner.setAdapter(parentDaysListingAdapter);
                } else {
                    clickedOnChild = childrenListing.get(position);
                    childId = clickedOnChild.getId();
                    System.out.println("ID::"+childId+"::FULLname::"+clickedOnChild.getFullName());
                    getLocationListing();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchLinearLayout();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changeFonts();
        getChildrenListing();
    }

    private void showAttendanceData() {
       // sessionsAttended.setText("ATTENDED "+clickedOnAgeGroup.getAttendedSessionsCount()+" OUT OF "+clickedOnAgeGroup.getTotalSessionsCount()+" SESSIONS");
      //  sessionsRemaining.setText(clickedOnAgeGroup.getRemainingSessionsCount()+" SESSIONS REMAINING");

       // attendanceListView.setAdapter(new ParentAttendanceListingAdapter(ParentViewMidweekAttendanceScreen.this, clickedOnAgeGroup.getAttendanceDatesList()));

        searchLinearLayout.setVisibility(View.GONE);
       // attendanceListView.setVisibility(View.VISIBLE);
      //  sessionsAttended.setVisibility(View.VISIBLE);
      //  sessionsRemaining.setVisibility(View.VISIBLE);
    }

    public void showSearchLinearLayout(){
        searchLinearLayout.setVisibility(View.VISIBLE);
    }

    private void goMethod(){
        if(Utilities.isNetworkAvailable(ParentViewMidweekAttendanceScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("selected_child", childId));
            nameValuePairList.add(new BasicNameValuePair("midweek_session", midweek_session));

            String webServiceUrl = Utilities.BASE_URL + "midweek_session/attendance_list_for_parent";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentViewMidweekAttendanceScreen.this, nameValuePairList, GO_BUTTON, ParentViewMidweekAttendanceScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentViewMidweekAttendanceScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAttendanceListing(){
        if(Utilities.isNetworkAvailable(ParentViewMidweekAttendanceScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("selected_child", childId));
            nameValuePairList.add(new BasicNameValuePair("selected_location", selectedLocationId));

            String webServiceUrl = Utilities.BASE_URL + "midweek_session/midweek_package_list_for_parent";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentViewMidweekAttendanceScreen.this, nameValuePairList, GET_ATTENDANCE_LISTING, ParentViewMidweekAttendanceScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentViewMidweekAttendanceScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocationListing() {
        if(Utilities.isNetworkAvailable(ParentViewMidweekAttendanceScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
           // nameValuePairList.add(new BasicNameValuePair("child_id", clickedOnChild.getId()));

            String webServiceUrl = Utilities.BASE_URL + "midweek_session/parent_midweek_locations_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

//            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentViewMidweekAttendanceScreen.this, nameValuePairList, GET_SESSIONS_LISTING, ParentViewMidweekAttendanceScreen.this, headers);
//            postWebServiceAsync.execute(webServiceUrl);

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ParentViewMidweekAttendanceScreen.this, GET_LOCATION_LISTING, ParentViewMidweekAttendanceScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentViewMidweekAttendanceScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getChildrenListing(){
        if(Utilities.isNetworkAvailable(ParentViewMidweekAttendanceScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "children/children_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ParentViewMidweekAttendanceScreen.this, GET_CHILDREN_LISTING, ParentViewMidweekAttendanceScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentViewMidweekAttendanceScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_CHILDREN_LISTING:

                childrenListing.clear();

                if (response == null) {
                    Toast.makeText(ParentViewMidweekAttendanceScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");

                            ChildBean childBean = new ChildBean();
                            childBean.setId("-1");
                            // change 8fb2019
                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                            childBean.setFullName("Choose "+verbiage_singular);

                            childrenListing.add(childBean);

                            for (int i=0; i<dataArray.length(); i++) {
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
                            Toast.makeText(ParentViewMidweekAttendanceScreen.this, message, Toast.LENGTH_SHORT).show();

                            ChildBean childBean = new ChildBean();
                            childBean.setId("-1");

                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                            childBean.setFullName("Choose "+verbiage_singular);
                            childrenListing.add(childBean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentViewMidweekAttendanceScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentChildrenListingForSpinnerAdapter = new ParentChildrenListingForSpinnerBlackAdapter(ParentViewMidweekAttendanceScreen.this, childrenListing);
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

            case GET_LOCATION_LISTING:

                daysListing.clear();

                if(response == null) {
                    Toast.makeText(ParentViewMidweekAttendanceScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");

                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Choose Location");

                            daysListing.add(daysBean);

                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject dayObject = dataArray.getJSONObject(i);
                                daysBean = new CampDaysBean();

                                daysBean.setDay(dayObject.getString("id"));
                                daysBean.setDayLabel(dayObject.getString("name"));

                                daysListing.add(daysBean);
                            }
                        } else {

                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Choose Location");

                            daysListing.add(daysBean);

                            Toast.makeText(ParentViewMidweekAttendanceScreen.this, message, Toast.LENGTH_SHORT).show();
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentViewMidweekAttendanceScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentDaysListingAdapter = new ParentDaysListingAdapter(ParentViewMidweekAttendanceScreen.this, daysListing);
                locationSpinner.setAdapter(parentDaysListingAdapter);

                break;

            case GET_ATTENDANCE_LISTING:

                ageGroupAttendanceListing.clear();

                if(response == null) {
                    Toast.makeText(ParentViewMidweekAttendanceScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            AgeGroupAttendanceBean ageGroupAttendanceBean = new AgeGroupAttendanceBean();
                            ageGroupAttendanceBean.setGroupName("Choose Midweek Package");

                            ageGroupAttendanceListing.add(ageGroupAttendanceBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject ageGroupAttendanceObject = dataArray.getJSONObject(i);

                                String title = ageGroupAttendanceObject.getString("title");
                                midweek_session = ageGroupAttendanceObject.getString("id");
                                ageGroupAttendanceBean = new AgeGroupAttendanceBean();
                                ageGroupAttendanceBean.setGroupName(title);
                                ageGroupAttendanceBean.setSessionsId(midweek_session);
                                ageGroupAttendanceListing.add(ageGroupAttendanceBean);

//                                ageGroupAttendanceBean.setSessionsId(ageGroupAttendanceObject.getString("sessions_id"));
//                                ageGroupAttendanceBean.setAgeGroupId(ageGroupAttendanceObject.getString("age_groups_id"));
//
//                                ageGroupAttendanceBean.setUsersId(ageGroupAttendanceObject.getString("users_id"));
//                                ageGroupAttendanceBean.setChildName(ageGroupAttendanceObject.getString("child_name"));
//                                ageGroupAttendanceBean.setBookingDates(ageGroupAttendanceObject.getString("booking_dates"));
//
//                                JSONArray attendanceDatesArray = ageGroupAttendanceObject.getJSONArray("attendance_dates");
//                                ArrayList<AttendanceDateBean> attendanceDatesList = new ArrayList<>();
//                                AttendanceDateBean attendanceDateBean;
//                                for(int j=0;j<attendanceDatesArray.length();j++) {
//                                    JSONObject attendanceDateObject = attendanceDatesArray.getJSONObject(j);
//                                    attendanceDateBean = new AttendanceDateBean();
//                                    attendanceDateBean.setBookedSessionDate(attendanceDateObject.getString("booked_session_date"));
//                                    attendanceDateBean.setShowBookedSessionDate(attendanceDateObject.getString("booked_session_date_formatted"));
//                                    attendanceDateBean.setStatus(attendanceDateObject.getString("status"));
//
//                                    attendanceDatesList.add(attendanceDateBean);
//                                }
//                                ageGroupAttendanceBean.setAttendanceDatesList(attendanceDatesList);
//
//                                ageGroupAttendanceBean.setAttendedSessionsCount(ageGroupAttendanceObject.getString("attended_sessions_count"));
//                                ageGroupAttendanceBean.setTotalSessionsCount(ageGroupAttendanceObject.getString("total_sessions_count"));
//                                ageGroupAttendanceBean.setRemainingSessionsCount(ageGroupAttendanceObject.getString("remaining_sessions_count"));


                            }
                        } else {
                            Toast.makeText(ParentViewMidweekAttendanceScreen.this, message, Toast.LENGTH_SHORT).show();

                            AgeGroupAttendanceBean ageGroupAttendanceBean = new AgeGroupAttendanceBean();
                            ageGroupAttendanceBean.setGroupName("Choose Midweek Package");

                            ageGroupAttendanceListing.add(ageGroupAttendanceBean);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentViewMidweekAttendanceScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentAgeGroupSpinnerAdapter = new ParentAgeGroupSpinnerAdapter(ParentViewMidweekAttendanceScreen.this, ageGroupAttendanceListing);
                midweekSpinner.setAdapter(parentAgeGroupSpinnerAdapter);

                break;


            case GO_BUTTON:
                if(response == null) {
                    Toast.makeText(ParentViewMidweekAttendanceScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                                JSONObject jsonObject = responseObject.getJSONObject("data");
                                int attendance_dates = jsonObject.getInt("attendance_dates");
                                String group_name = jsonObject.getString("group_name");
                                JSONArray childDetails = jsonObject.getJSONArray("child_details");
                                ArrayList<RecycleExpBean> recycleExpBeanArrayList = new ArrayList<>();

                                for(int i=0; i<childDetails.length(); i++){
                                    JSONObject jsonObject1 = childDetails.getJSONObject(i);
                                    RecycleExpBean recycleExpBean = new RecycleExpBean();
                                    recycleExpBean.setChildName(jsonObject1.getString("child_name"));
                                    recycleExpBean.setId(i);

                                    ArrayList<Chapter>chapterArrayList = new ArrayList<>();
                                    JSONArray attendance = jsonObject1.getJSONArray("attendance");
                                    for(int j=0; j<attendance.length(); j++){
                                        JSONObject jsonObject2 = attendance.getJSONObject(j);
                                        Chapter chapter1 = new Chapter();

                                        chapter1.setChapterName(jsonObject2.getString("attendance_date"));
                                        chapter1.setStatus(jsonObject2.getString("status"));
                                        chapter1.setId(j);
                                        chapter1.setAttendance_id(jsonObject2.getString("attendance_id"));
                                        chapter1.setTermId(jsonObject2.getString("term_id"));
                                        chapter1.setChildId(jsonObject1.getString("users_id"));
                                        chapter1.setMidweekSessionDetailsId(jsonObject2.getString("midweek_session_details_id"));
                                        chapter1.setOrderMidWeekSessionId(jsonObject2.getString("order_midweek_sessions_id"));

                                        chapterArrayList.add(chapter1);
                                        recycleExpBean.setChapters(chapterArrayList);
                                    }

                                    if(attendance.length()==attendance_dates){

                                    }else{
                                        int loopSize = attendance_dates - attendance.length();
                                        for(int k=0; k<loopSize; k++){
                                            Chapter chapter1 = new Chapter();

                                            chapter1.setChapterName("N/A");
                                            chapter1.setStatus("N/A");
                                            chapter1.setId(k+attendance.length());
                                            chapter1.setAttendance_id("N/A");
                                            chapter1.setTermId("N/A");
                                            chapter1.setMidweekSessionDetailsId("N/A");
                                            chapter1.setOrderMidWeekSessionId("N/A");

                                            chapterArrayList.add(chapter1);
                                            recycleExpBean.setChapters(chapterArrayList);
                                        }
                                    }

                                    recycleExpBeanArrayList.add(recycleExpBean);

                                }


                                Intent obj = new Intent(ParentViewMidweekAttendanceScreen.this, ParentMidWeekPackageChildNamesAttendanceActivity.class);
                                obj.putExtra("group_name", group_name);
                                obj.putExtra("recycleExpBeanArrayList", recycleExpBeanArrayList);
                                startActivity(obj);

//                                groupName.setText(group_name);
//                                CoachMidWeekPackageChildNamesAttendanceAdapter coachMidWeekPackageChildNamesAttendanceAdapter = new CoachMidWeekPackageChildNamesAttendanceAdapter(CoachMidWeekPackageChildNamesAttendanceActivity.this, recycleExpBeanArrayList);
//                                listView.setAdapter(coachMidWeekPackageChildNamesAttendanceAdapter);




                        }else{
                            Toast.makeText(ParentViewMidweekAttendanceScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(ParentViewMidweekAttendanceScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void changeFonts(){
        title.setTypeface(linoType);
        go.setTypeface(linoType);
       // sessionsAttended.setTypeface(linoType);
       // sessionsRemaining.setTypeface(linoType);
    }
}
