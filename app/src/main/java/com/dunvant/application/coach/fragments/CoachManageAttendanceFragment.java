package com.dunvant.application.coach.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.ChildAttendanceBean;
import com.dunvant.application.beans.CoachingProgramBean;
import com.dunvant.application.beans.SessionDateBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.CoachUnpaidPlayersScreen;
import com.dunvant.application.coach.adapters.CoachAttendanceListingAdapter;
import com.dunvant.application.coach.adapters.CoachCoachingProgramAdapter;
import com.dunvant.application.coach.adapters.CoachLocationListingAdapter;
import com.dunvant.application.coach.adapters.CoachOnlyAgeGroupListingAdapter;
import com.dunvant.application.coach.adapters.CoachSessionDatesForSpinnerAdapter;
import com.dunvant.application.coach.adapters.CoachSessionListingAdapter;
import com.dunvant.application.coach.adapters.CoachUnpaidAttendanceListingAdapter;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CoachManageAttendanceFragment extends Fragment implements IWebServiceCallback {
    public static boolean variableTrue = false;
    Button addPlayer;
    ListView unpaidPlayersListView;
    ArrayList<ChildAttendanceBean> unpaidAttendanceListing = new ArrayList<>();
    CoachUnpaidAttendanceListingAdapter coachUnpaidAttendanceListingAdapter;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    LinearLayout searchLinearLayout;
    Spinner coachingProgramSpinner;
    Spinner locationSpinner;
    Spinner sessionSpinner;
    Spinner ageGroupSpinner;
    Spinner dateSpinner;
    Button searchButton;
    RelativeLayout attendanceRelativeLayout;
    TextView ageGroupName;
    ListView childrenListView;
    Button submitButton;

    private final String GET_COACHING_PROGRAM_LISTING = "GET_COACHING_PROGRAM_LISTING";
    private final String GET_LOCATION_LISTING = "GET_LOCATION_LISTING";
    private final String GET_SESSIONS_LISTING = "GET_SESSIONS_LISTING";
    private final String GET_AGE_GROUP_LISTING = "GET_AGE_GROUP_LISTING";
    private final String GET_DATES_LISTING = "GET_DATES_LISTING";
    private final String GET_CHILDREN_LISTING =  "GET_CHILDREN_LISTING";
    private final String MARK_ATTENDANCE = "MARK_ATTENDANCE";

    ArrayList<CoachingProgramBean> coachingProgramsList = new ArrayList<>();
    ArrayList<CampLocationBean> locationsList = new ArrayList<>();
    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupBean> ageGroupsListing = new ArrayList<>();
    ArrayList<SessionDateBean> sessionDatesListing = new ArrayList<>();

    CoachCoachingProgramAdapter coachCoachingProgramAdapter;
    CoachLocationListingAdapter coachLocationListingAdapter;
    CoachSessionListingAdapter coachSessionListingAdapter;
    CoachOnlyAgeGroupListingAdapter coachOnlyAgeGroupListingAdapter;
    CoachSessionDatesForSpinnerAdapter coachSessionDatesForSpinnerAdapter;

    CoachingProgramBean clickedOnCoachingProgram;
    CampLocationBean clickedOnLocation;
    CampDaysBean clickedOnDay;
    AgeGroupBean clickedOnAgeGroup;
    SessionDateBean clickedOnDate;

    String ageGroupId;
    String groupName;
    ArrayList<ChildAttendanceBean> childrenAttendanceListing = new ArrayList<>();
    CoachAttendanceListingAdapter coachAttendanceListingAdapter;
    CoachAttendanceListingAdapter coachAttendanceListingAdapter2;
    String strChildrenAttendance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coach_fragment_manage_attendance, container, false);

        searchLinearLayout = (LinearLayout) view.findViewById(R.id.searchLinearLayout);
        coachingProgramSpinner = (Spinner) view.findViewById(R.id.coachingProgramSpinner);
        locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner);
        sessionSpinner = (Spinner) view.findViewById(R.id.sessionSpinner);
        ageGroupSpinner = (Spinner) view.findViewById(R.id.ageGroupSpinner);
        dateSpinner = (Spinner) view.findViewById(R.id.dateSpinner);

        coachingProgramSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        locationSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        sessionSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ageGroupSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        dateSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        searchButton = (Button) view.findViewById(R.id.searchButton);
        attendanceRelativeLayout = (RelativeLayout) view.findViewById(R.id.attendanceRelativeLayout);
        ageGroupName = (TextView) view.findViewById(R.id.ageGroupName);
        childrenListView = (ListView) view.findViewById(R.id.childrenListView);
        submitButton = (Button) view.findViewById(R.id.submitButton);
        addPlayer = view.findViewById(R.id.addPlayer);
        unpaidPlayersListView = view.findViewById(R.id.unpaidPlayersListView);

        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
        addPlayer.setText("Add Unpaid "+verbiage_singular);



        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(getActivity(), CoachUnpaidPlayersScreen.class);
                obj.putExtra("groupName", groupName);
                obj.putExtra("coachingProgName", clickedOnCoachingProgram.getCoachinProgramName());
                obj.putExtra("coachingProgID", clickedOnCoachingProgram.getCoachingProgramId());
                obj.putExtra("locationName", clickedOnLocation.getLocationName());
                obj.putExtra("locationId", clickedOnLocation.getLocationId());
                obj.putExtra("sessionId", clickedOnAgeGroup.getSessionId());
                obj.putExtra("sessionDate", clickedOnDate.getSessionDate());
                startActivity(obj);
            }
        });

        changeFonts();
//        getLocationListing();
        getCoachingProgramListing();
        attendanceRelativeLayout.setVisibility(View.GONE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(childrenAttendanceListing.isEmpty()){
                    Toast.makeText(getActivity(), "No Children available", Toast.LENGTH_SHORT).show();
                    return;
                }

               strChildrenMethod();

                markAttendance();

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int coachingProgramPosition = coachingProgramSpinner.getSelectedItemPosition();
                int locationPosition = locationSpinner.getSelectedItemPosition();
                int sessionPosition = sessionSpinner.getSelectedItemPosition();
                int ageGroupPosition = ageGroupSpinner.getSelectedItemPosition();
                int datePosition = dateSpinner.getSelectedItemPosition();

                if(coachingProgramPosition == 0){
                    Toast.makeText(getActivity(), "Please choose Coaching Program", Toast.LENGTH_SHORT).show();
                } else if(locationPosition == 0) {
                    Toast.makeText(getActivity(), "Please choose Location", Toast.LENGTH_SHORT).show();
                } else if (sessionPosition == 0) {
                    Toast.makeText(getActivity(), "Please choose Session", Toast.LENGTH_SHORT).show();
                } else if (ageGroupPosition == 0) {
                    Toast.makeText(getActivity(), "Please choose Age Group", Toast.LENGTH_SHORT).show();
                } else if (datePosition == 0 || datePosition == -1) {
                    Toast.makeText(getActivity(), "Please choose Date", Toast.LENGTH_SHORT).show();
                } else {
                    clickedOnDate = sessionDatesListing.get(datePosition);

                    getChildrenListingForAttendance();
                }
            }
        });

        coachingProgramSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0) {
                    locationsList.clear();

                    // 0th Element Choose Location
                    CampLocationBean locationBean = new CampLocationBean();
                    locationBean.setLocationId("-1");
                    locationBean.setLocationName("Choose Location");

                    locationsList.add(locationBean);

                    coachLocationListingAdapter = new CoachLocationListingAdapter(getActivity(), locationsList);
                    locationSpinner.setAdapter(coachLocationListingAdapter);
                } else {
                    clickedOnCoachingProgram = coachingProgramsList.get(position);
                    getLocationListing();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

                    coachSessionListingAdapter = new CoachSessionListingAdapter(getActivity(), daysListing);
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

                    // 0th Element Choose Session
                    AgeGroupBean ageGroupBean = new AgeGroupBean();
                    ageGroupBean.setAgeGroupId("-1");
                    ageGroupBean.setGroupName("Choose Age Group");

                    ageGroupsListing.add(ageGroupBean);

                    coachOnlyAgeGroupListingAdapter = new CoachOnlyAgeGroupListingAdapter(getActivity(), ageGroupsListing);
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
                    sessionDatesListing.clear();

                    SessionDateBean sessionDateBean = new SessionDateBean();
                    sessionDateBean.setSessionDate("-1");
                    sessionDateBean.setShowSessionDate("Choose Date");

                    sessionDatesListing.add(sessionDateBean);

                    coachSessionDatesForSpinnerAdapter = new CoachSessionDatesForSpinnerAdapter(getActivity(), sessionDatesListing);
                    dateSpinner.setAdapter(coachSessionDatesForSpinnerAdapter);

                } else {
                    clickedOnAgeGroup = ageGroupsListing.get(position);
                    getDatesListing();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void getCoachingProgramListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "coach/coaching_program_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_COACHING_PROGRAM_LISTING, CoachManageAttendanceFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocationListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            /*String webServiceUrl = Utilities.BASE_URL + "coach/locations_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_LOCATION_LISTING, CoachManageAttendanceFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);*/

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("program_id", clickedOnCoachingProgram.getCoachingProgramId()));

            String webServiceUrl = Utilities.BASE_URL + "coach/get_location_list_for_coach_by_program_ID";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_LOCATION_LISTING, CoachManageAttendanceFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getSessionDaysListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_ids", clickedOnLocation.getLocationId()));
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocation.getLocationId()));

            String webServiceUrl = Utilities.BASE_URL + "coach/session_days_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_SESSIONS_LISTING, CoachManageAttendanceFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAgeGroupsListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocation.getLocationId()));
            nameValuePairList.add(new BasicNameValuePair("locations_ids", clickedOnLocation.getLocationId()));
            nameValuePairList.add(new BasicNameValuePair("session_days", clickedOnDay.getDay()));

            String webServiceUrl = Utilities.BASE_URL + "coach/age_group_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_AGE_GROUP_LISTING, CoachManageAttendanceFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getDatesListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocation.getLocationId()));
            nameValuePairList.add(new BasicNameValuePair("session_day", clickedOnDay.getDay()));
            nameValuePairList.add(new BasicNameValuePair("sessions_id", clickedOnAgeGroup.getSessionId()));

            String webServiceUrl = Utilities.BASE_URL + "coach/session_dates_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_DATES_LISTING, CoachManageAttendanceFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getChildrenListingForAttendance() {
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("program_id", clickedOnCoachingProgram.getCoachingProgramId()));
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocation.getLocationId()));
            nameValuePairList.add(new BasicNameValuePair("session_day", clickedOnDay.getDay()));
            nameValuePairList.add(new BasicNameValuePair("sessions_id", clickedOnAgeGroup.getSessionId()));
            nameValuePairList.add(new BasicNameValuePair("attendance_date", clickedOnDate.getSessionDate()));

            String webServiceUrl = Utilities.BASE_URL + "coach/child_wise_attendance_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_CHILDREN_LISTING, CoachManageAttendanceFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void markAttendance() {
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("sessions_id", childrenAttendanceListing.get(0).getSessionId()));
            nameValuePairList.add(new BasicNameValuePair("session_date", childrenAttendanceListing.get(0).getBookedSessionDate()));
            nameValuePairList.add(new BasicNameValuePair("children_attendance", strChildrenAttendance));

            String webServiceUrl = Utilities.BASE_URL + "coach/save_children_attendance";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, MARK_ATTENDANCE, CoachManageAttendanceFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_COACHING_PROGRAM_LISTING:

                coachingProgramsList.clear();

                if(response == null){
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            // 0th Element Choose Coaching Program
                            CoachingProgramBean coachingProgramBean = new CoachingProgramBean();
                            coachingProgramBean.setCoachingProgramId("-1");
                            coachingProgramBean.setCoachinProgramName("Choose Coaching Program");

                            coachingProgramsList.add(coachingProgramBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for(int i=0;i<dataArray.length();i++){
                                JSONObject coachingProgramObject = dataArray.getJSONObject(i);
                                coachingProgramBean = new CoachingProgramBean();

                                coachingProgramBean.setCoachingProgramId(coachingProgramObject.getString("co_pram_id"));
                                coachingProgramBean.setCoachinProgramName(coachingProgramObject.getString("co_pram_name"));

                                coachingProgramsList.add(coachingProgramBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                coachCoachingProgramAdapter = new CoachCoachingProgramAdapter(getActivity(), coachingProgramsList);
                coachingProgramSpinner.setAdapter(coachCoachingProgramAdapter);
                break;

            case GET_LOCATION_LISTING:

                locationsList.clear();

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            // 0th Element Choose Location
                            CampLocationBean locationBean = new CampLocationBean();
                            locationBean.setLocationId("-1");
                            locationBean.setLocationName("Choose Location");

                            locationsList.add(locationBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
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
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachLocationListingAdapter = new CoachLocationListingAdapter(getActivity(), locationsList);
                locationSpinner.setAdapter(coachLocationListingAdapter);
                break;

            case GET_SESSIONS_LISTING:

                daysListing.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            // 0th Element Choose Session
                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Choose Session");

                            daysListing.add(daysBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject daysObject = dataArray.getJSONObject(i);
                                daysBean = new CampDaysBean();
                                daysBean.setDay(daysObject.getString("day"));
                                daysBean.setDayLabel(daysObject.getString("day_label"));

                                daysListing.add(daysBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachSessionListingAdapter = new CoachSessionListingAdapter(getActivity(), daysListing);
                sessionSpinner.setAdapter(coachSessionListingAdapter);

                break;

            case GET_AGE_GROUP_LISTING:

                ageGroupsListing.clear();

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            // 0th Element Choose Age Group

                            AgeGroupBean ageGroupBean = new AgeGroupBean();
                            ageGroupBean.setAgeGroupId("-1");
                            ageGroupBean.setGroupName("Choose Age Group");

                            ageGroupsListing.add(ageGroupBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject ageGroupObject = dataArray.getJSONObject(i);
                                ageGroupBean = new AgeGroupBean();
                                ageGroupBean.setSessionId(ageGroupObject.getString("sessions_id"));
                                ageGroupBean.setAgeGroupId(ageGroupObject.getString("age_groups_id"));
                                ageGroupBean.setGroupName(ageGroupObject.getString("group_name"));

                                ageGroupsListing.add(ageGroupBean);
                            }


                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachOnlyAgeGroupListingAdapter = new CoachOnlyAgeGroupListingAdapter(getActivity(), ageGroupsListing);
                ageGroupSpinner.setAdapter(coachOnlyAgeGroupListingAdapter);

                break;

            case GET_DATES_LISTING:

                sessionDatesListing.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            SessionDateBean sessionDateBean = new SessionDateBean();
                            sessionDateBean.setSessionDate("-1");
                            sessionDateBean.setShowSessionDate("Choose Date");

                            sessionDatesListing.add(sessionDateBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject sessionDateObject = dataArray.getJSONObject(i);
                                sessionDateBean = new SessionDateBean();

                                sessionDateBean.setSessionDate(sessionDateObject.getString("session_date"));
                                sessionDateBean.setShowSessionDate(sessionDateObject.getString("session_dates_formatted"));
                                sessionDateBean.setDayLabel(sessionDateObject.getString("day_label"));
                                sessionDateBean.setCanMarkAttendance(sessionDateObject.getBoolean("can_mark_attendance"));

                                // initially not selected
                                sessionDateBean.setSelected(false);

                                sessionDatesListing.add(sessionDateBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            sessionDatesListing.clear();

                            SessionDateBean sessionDateBean = new SessionDateBean();
                            sessionDateBean.setSessionDate("-1");
                            sessionDateBean.setShowSessionDate("Choose Date");

                            sessionDatesListing.add(sessionDateBean);

                            coachSessionDatesForSpinnerAdapter = new CoachSessionDatesForSpinnerAdapter(getActivity(), sessionDatesListing);
                            dateSpinner.setAdapter(coachSessionDatesForSpinnerAdapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                coachSessionDatesForSpinnerAdapter = new CoachSessionDatesForSpinnerAdapter(getActivity(), sessionDatesListing);
                dateSpinner.setAdapter(coachSessionDatesForSpinnerAdapter);

                break;

            case GET_CHILDREN_LISTING:

                childrenAttendanceListing.clear();
                unpaidAttendanceListing.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            searchLinearLayout.setVisibility(View.GONE);
                            attendanceRelativeLayout.setVisibility(View.VISIBLE);

                            JSONArray dataArray = responseObject.getJSONArray("data");

                            // Get the first element only
                            JSONObject attendanceObject = dataArray.getJSONObject(0);

                            ageGroupId = attendanceObject.getString("age_groups_id");
                            groupName = attendanceObject.getString("group_name");

                            JSONArray childrenDetailArray = attendanceObject.getJSONArray("children_details");

                            if(childrenDetailArray.length() == 0) {
                                Toast.makeText(getActivity(), "No children available", Toast.LENGTH_SHORT).show();
                            }

                            ChildAttendanceBean childAttendanceBean;
                            for (int i=0; i<childrenDetailArray.length(); i++) {
                                JSONObject childAttendanceObject = childrenDetailArray.getJSONObject(i);
                                childAttendanceBean = new ChildAttendanceBean();

                                childAttendanceBean.setSessionId(childAttendanceObject.getString("sessions_id"));
                                childAttendanceBean.setUserId(childAttendanceObject.getString("users_id"));
                                childAttendanceBean.setChildName(childAttendanceObject.getString("child_name"));
                                childAttendanceBean.setIsTrial(childAttendanceObject.getString("is_trial"));

                                JSONArray attendanceDatesArray = childAttendanceObject.getJSONArray("attendance_dates");

                                // Get the first element only
                                JSONObject attendanceDateObject = attendanceDatesArray.getJSONObject(0);

                                childAttendanceBean.setBookedSessionDate(attendanceDateObject.getString("booked_session_date"));
                                childAttendanceBean.setStatus(attendanceDateObject.getString("status"));

                                childAttendanceBean.setUnpaid(childAttendanceObject.getString("unpaid"));


                                if(childAttendanceObject.getString("unpaid").equalsIgnoreCase("1")){
                                    unpaidAttendanceListing.add(childAttendanceBean);
                                }else{
                                    childrenAttendanceListing.add(childAttendanceBean);
                                }


                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                ageGroupName.setText(groupName);

                coachAttendanceListingAdapter = new CoachAttendanceListingAdapter(getActivity(), childrenAttendanceListing);
                childrenListView.setAdapter(coachAttendanceListingAdapter);
                Utilities.setListViewHeightBasedOnChildren(childrenListView);

                coachAttendanceListingAdapter2 = new CoachAttendanceListingAdapter(getActivity(), unpaidAttendanceListing);
                unpaidPlayersListView.setAdapter(coachAttendanceListingAdapter2);
                Utilities.setListViewHeightBasedOnChildren(unpaidPlayersListView);

                break;

            case MARK_ATTENDANCE:

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    public void showSearchLayout(){
        searchLinearLayout.setVisibility(View.VISIBLE);
    }

    private void changeFonts(){
        searchButton.setTypeface(linoType);
        ageGroupName.setTypeface(helvetica);
        submitButton.setTypeface(linoType);
        addPlayer.setTypeface(linoType);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(variableTrue){
            searchLinearLayout.setVisibility(View.VISIBLE);
            attendanceRelativeLayout.setVisibility(View.GONE);
            variableTrue=false;
        }


    }
    public void strChildrenMethod(){
        strChildrenAttendance = "[";

        for (ChildAttendanceBean childAttendanceBean : childrenAttendanceListing) {
            strChildrenAttendance += "{\"child_id\":\""+childAttendanceBean.getUserId()+"\", \"status\":\""+childAttendanceBean.getStatus()+"\"},";
        }

        for (ChildAttendanceBean childAttendanceBean : unpaidAttendanceListing) {
            strChildrenAttendance += "{\"child_id\":\""+childAttendanceBean.getUserId()+"\", \"status\":\""+childAttendanceBean.getStatus()+"\"},";
        }


        if (strChildrenAttendance != null && strChildrenAttendance.length() > 0 && strChildrenAttendance.charAt(strChildrenAttendance.length()-1)==',') {
            strChildrenAttendance = strChildrenAttendance.substring(0, strChildrenAttendance.length()-1);
        }

        strChildrenAttendance += "]";
    }
}