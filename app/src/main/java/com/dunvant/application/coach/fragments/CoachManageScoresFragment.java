package com.dunvant.application.coach.fragments;

import android.content.Context;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.ChildScoreBean;
import com.dunvant.application.beans.CoachingProgramBean;
import com.dunvant.application.beans.ScoresDataBean;
import com.dunvant.application.beans.ScoringDateBean;
import com.dunvant.application.beans.SessionDetailsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachCoachingProgramAdapter;
import com.dunvant.application.coach.adapters.CoachLocationListingAdapter;
import com.dunvant.application.coach.adapters.CoachOnlyAgeGroupListingAdapter;
import com.dunvant.application.coach.adapters.CoachScoresListingAdapter;
import com.dunvant.application.coach.adapters.CoachScoringDatesAdapter;
import com.dunvant.application.coach.adapters.CoachSessionListingAdapter;
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

public class CoachManageScoresFragment extends Fragment implements IWebServiceCallback{
    CoachingProgramBean clickedOnCoachingProgram;
    Spinner coachingProgramSpinner;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    LinearLayout searchLinearLayout;
    Spinner locationSpinner;
    Spinner sessionSpinner;
    Spinner ageGroupSpinner;
    Spinner scoringDatesSpinner;
    Button searchButton;
    ListView scoresDataListView;

    ArrayList<CoachingProgramBean> coachingProgramsList = new ArrayList<>();
    ArrayList<CampLocationBean> locationsList = new ArrayList<>();
    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupBean> ageGroupsListing = new ArrayList<>();
    ArrayList<ScoringDateBean> scoringDatesListing = new ArrayList<>();

    private final String GET_COACHING_PROGRAM_LISTING = "GET_COACHING_PROGRAM_LISTING";
    private final String GET_LOCATION_LISTING = "GET_LOCATION_LISTING";
    private final String GET_SESSIONS_LISTING = "GET_SESSIONS_LISTING";
    private final String GET_AGE_GROUP_LISTING = "GET_AGE_GROUP_LISTING";
    private final String GET_SCORES_LISTING = "GET_SCORES_LISTING";
    private final String GET_SCORING_DATES = "GET_SCORING_DATES";

    CoachCoachingProgramAdapter coachCoachingProgramAdapter;
    CoachLocationListingAdapter coachLocationListingAdapter;
    CoachSessionListingAdapter coachSessionListingAdapter;
    CoachOnlyAgeGroupListingAdapter coachOnlyAgeGroupListingAdapter;
    CoachScoringDatesAdapter coachScoringDatesAdapter;

    CampLocationBean clickedOnLocation;
    CampDaysBean clickedOnDay;
    AgeGroupBean clickedOnAgeGroup;
    ScoringDateBean clickedOnScoringDate;

    ArrayList<ScoresDataBean> scoresDataList = new ArrayList<>();
    SessionDetailsBean sessionDetailsBean;
    CoachScoresListingAdapter coachScoresListingAdapter;

    public static String comingFrom = "";

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
        View view = inflater.inflate(R.layout.coach_fragment_manage_scores, container, false);

        coachingProgramSpinner = (Spinner) view.findViewById(R.id.coachingProgramSpinner);
        searchLinearLayout = (LinearLayout) view.findViewById(R.id.searchLinearLayout);
        locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner);
        sessionSpinner = (Spinner) view.findViewById(R.id.sessionSpinner);
        ageGroupSpinner = (Spinner) view.findViewById(R.id.ageGroupSpinner);
        scoringDatesSpinner = view.findViewById(R.id.scoringDatesSpinner);
        searchButton = (Button) view.findViewById(R.id.searchButton);
        scoresDataListView = (ListView) view.findViewById(R.id.scoresDataListView);

        coachingProgramSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        locationSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        sessionSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ageGroupSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        scoringDatesSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        changeFonts();
       // getLocationListing();
        getCoachingProgramListing();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Utilities.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    return;
                }

                int coachingProgramPosition = coachingProgramSpinner.getSelectedItemPosition();
                int locationPosition = locationSpinner.getSelectedItemPosition();
                int sessionPosition = sessionSpinner.getSelectedItemPosition();
                int ageGroupPosition = ageGroupSpinner.getSelectedItemPosition();
                int scoringDatePosition = scoringDatesSpinner.getSelectedItemPosition();

                if(coachingProgramPosition == 0){
                    Toast.makeText(getActivity(), "Please select Coaching Program", Toast.LENGTH_SHORT).show();
                } else if(locationPosition == 0) {
                    Toast.makeText(getActivity(), "Please select Location", Toast.LENGTH_SHORT).show();
                } else if (sessionPosition == 0) {
                    Toast.makeText(getActivity(), "Please select Session", Toast.LENGTH_SHORT).show();
                } else if (ageGroupPosition == 0) {
                    Toast.makeText(getActivity(), "Please select Age Group", Toast.LENGTH_SHORT).show();
                } else if (scoringDatePosition == 0) {
                    if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("0")){
                        Toast.makeText(getActivity(), "Please select Date", Toast.LENGTH_SHORT).show();
                    } else if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("1")){
                        Toast.makeText(getActivity(), "Please select Month", Toast.LENGTH_SHORT).show();
                    } else if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("2")){
                        Toast.makeText(getActivity(), "Please select Term", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    clickedOnAgeGroup = ageGroupsListing.get(ageGroupPosition);
                    clickedOnScoringDate = scoringDatesListing.get(scoringDatePosition);

                    getScoresListing();
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
                    daysBean.setDayLabel("Select Session");

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
                    ageGroupBean.setGroupName("Select Age Group");

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
                if(position == 0) {
                    scoringDatesListing.clear();

                    // Add 0th element
                    ScoringDateBean scoringDateBean = new ScoringDateBean();
                    scoringDateBean.setKey("-1");
                    if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("0")){
                        scoringDateBean.setValue("Select Date");
                    } else if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("1")){
                        scoringDateBean.setValue("Select Month");
                    } else if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("2")){
                        scoringDateBean.setValue("Select Term");
                    }
                    scoringDatesListing.add(scoringDateBean);

                    coachScoringDatesAdapter = new CoachScoringDatesAdapter(getActivity(), scoringDatesListing);
                    scoringDatesSpinner.setAdapter(coachScoringDatesAdapter);
                } else {
                    clickedOnAgeGroup = ageGroupsListing.get(position);
                    getScoringDates();
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

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_COACHING_PROGRAM_LISTING, CoachManageScoresFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocationListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

//            String webServiceUrl = Utilities.BASE_URL + "coach/locations_list";
//
//            ArrayList<String> headers = new ArrayList<>();
//            headers.add("X-access-uid:"+loggedInUser.getId());
//            headers.add("X-access-token:"+loggedInUser.getToken());
//
//            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_LOCATION_LISTING, CoachManageScoresFragment.this, headers);
//            getWebServiceWithHeadersAsync.execute(webServiceUrl);

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("program_id", clickedOnCoachingProgram.getCoachingProgramId()));

            String webServiceUrl = Utilities.BASE_URL + "coach/get_location_list_for_coach_by_program_ID";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_LOCATION_LISTING, CoachManageScoresFragment.this, headers);
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

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_SESSIONS_LISTING, CoachManageScoresFragment.this, headers);
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

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_AGE_GROUP_LISTING, CoachManageScoresFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getScoringDates(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("sessions_id", clickedOnAgeGroup.getSessionId()));

            String webServiceUrl = Utilities.BASE_URL + "coach/get_scoring_dates";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_SCORING_DATES, CoachManageScoresFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getScoresListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("sessions_id", clickedOnAgeGroup.getSessionId()));
            nameValuePairList.add(new BasicNameValuePair("sessions_date", clickedOnScoringDate.getKey()));
            nameValuePairList.add(new BasicNameValuePair("coaching_program", clickedOnCoachingProgram.getCoachingProgramId()));

            String webServiceUrl = Utilities.BASE_URL + "coach/children_scores_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_SCORES_LISTING, CoachManageScoresFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if(comingFrom.equalsIgnoreCase("DetailScreen")) {
            searchButton.performClick();
            comingFrom = "";
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
                            locationBean.setLocationName("Select Location");

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

                            // 0th Element Choose Location
                            CampLocationBean locationBean = new CampLocationBean();
                            locationBean.setLocationId("-1");
                            locationBean.setLocationName("Select Location");

                            locationsList.add(locationBean);
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

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            // 0th Element Choose Session
                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Select Session");

                            daysListing.add(daysBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject daysObject = dataArray.getJSONObject(i);
                                daysBean = new CampDaysBean();
                                daysBean.setDay(daysObject.getString("day"));
                                daysBean.setDayLabel(daysObject.getString("day_label"));

                                daysListing.add(daysBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            // 0th Element Choose Session
                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Select Session");

                            daysListing.add(daysBean);
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

                        if (status) {

                            // 0th Element Choose Age Group
                            AgeGroupBean ageGroupBean = new AgeGroupBean();
                            ageGroupBean.setAgeGroupId("-1");
                            ageGroupBean.setGroupName("Select Age Group");

                            ageGroupsListing.add(ageGroupBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject ageGroupObject = dataArray.getJSONObject(i);
                                ageGroupBean = new AgeGroupBean();
                                ageGroupBean.setSessionId(ageGroupObject.getString("sessions_id"));
                                ageGroupBean.setAgeGroupId(ageGroupObject.getString("age_groups_id"));
                                ageGroupBean.setGroupName(ageGroupObject.getString("group_name"));

                                ageGroupsListing.add(ageGroupBean);
                            }


                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            // 0th Element Choose Age Group
                            AgeGroupBean ageGroupBean = new AgeGroupBean();
                            ageGroupBean.setAgeGroupId("-1");
                            ageGroupBean.setGroupName("Select Age Group");

                            ageGroupsListing.add(ageGroupBean);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachOnlyAgeGroupListingAdapter = new CoachOnlyAgeGroupListingAdapter(getActivity(), ageGroupsListing);
                ageGroupSpinner.setAdapter(coachOnlyAgeGroupListingAdapter);

                break;

            case GET_SCORING_DATES:

                scoringDatesListing.clear();

                if(response == null){
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();

                    // Add 0th element
                    ScoringDateBean scoringDateBean = new ScoringDateBean();
                    scoringDateBean.setKey("-1");
                    if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("0")){
                        scoringDateBean.setValue("Select Date");
                    } else if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("1")){
                        scoringDateBean.setValue("Select Month");
                    } else if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("2")){
                        scoringDateBean.setValue("Select Term");
                    }
                    scoringDatesListing.add(scoringDateBean);
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            // Add 0th element
                            ScoringDateBean scoringDateBean = new ScoringDateBean();
                            scoringDateBean.setKey("-1");
                            if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("0")){
                                scoringDateBean.setValue("Select Date");
                            } else if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("1")){
                                scoringDateBean.setValue("Select Month");
                            } else if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("2")){
                                scoringDateBean.setValue("Select Term");
                            }
                            scoringDatesListing.add(scoringDateBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for(int i=0; i<dataArray.length(); i++){
                                JSONObject scoringDateObject = dataArray.getJSONObject(i);

                                scoringDateBean = new ScoringDateBean();

                                scoringDateBean.setKey(scoringDateObject.getString("key"));
                                scoringDateBean.setValue(scoringDateObject.getString("value"));

                                scoringDatesListing.add(scoringDateBean);
                            }
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            // Add 0th element
                            ScoringDateBean scoringDateBean = new ScoringDateBean();
                            scoringDateBean.setKey("-1");
                            if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("0")){
                                scoringDateBean.setValue("Select Date");
                            } else if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("1")){
                                scoringDateBean.setValue("Select Month");
                            } else if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("2")){
                                scoringDateBean.setValue("Select Term");
                            }
                            scoringDatesListing.add(scoringDateBean);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        // Add 0th element
                        ScoringDateBean scoringDateBean = new ScoringDateBean();
                        scoringDateBean.setKey("-1");
                        if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("0")){
                            scoringDateBean.setValue("Select Date");
                        } else if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("1")){
                            scoringDateBean.setValue("Select Month");
                        } else if(loggedInUser.getReportSubmissionType().equalsIgnoreCase("2")){
                            scoringDateBean.setValue("Select Term");
                        }
                        scoringDatesListing.add(scoringDateBean);
                    }
                }

                coachScoringDatesAdapter = new CoachScoringDatesAdapter(getActivity(), scoringDatesListing);
                scoringDatesSpinner.setAdapter(coachScoringDatesAdapter);

                break;

            case GET_SCORES_LISTING:

                scoresDataList.clear();
                sessionDetailsBean = new SessionDetailsBean();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            searchLinearLayout.setVisibility(View.GONE);

                            JSONObject dataObject = responseObject.getJSONObject("data");

                            JSONObject sessionDetailsObject = dataObject.getJSONObject("session_details");

                            sessionDetailsBean.setSessionDetailsId(sessionDetailsObject.getString("session_details_id"));
                            sessionDetailsBean.setLocationId(sessionDetailsObject.getString("locations_id"));
                            sessionDetailsBean.setTermsId(sessionDetailsObject.getString("terms_id"));
                            sessionDetailsBean.setCoachingProgramsId(sessionDetailsObject.getString("coaching_programs_id"));
                            sessionDetailsBean.setProgramName(sessionDetailsObject.getString("program_name"));
                            sessionDetailsBean.setSiteMediaId(sessionDetailsObject.getString("site_media_id"));
                            sessionDetailsBean.setAcademiesId(sessionDetailsObject.getString("academies_id"));
                            sessionDetailsBean.setTitle(sessionDetailsObject.getString("title"));
                            sessionDetailsBean.setDescription(sessionDetailsObject.getString("description"));
                            sessionDetailsBean.setState(sessionDetailsObject.getString("state"));
                            sessionDetailsBean.setTermsName(sessionDetailsObject.getString("terms_name"));
                            sessionDetailsBean.setLocationName(sessionDetailsObject.getString("location_name"));
                            sessionDetailsBean.setDay(sessionDetailsObject.getString("day"));
                            sessionDetailsBean.setSessionId(sessionDetailsObject.getString("session_id"));
                            sessionDetailsBean.setFromDate(sessionDetailsObject.getString("from_date"));
                            sessionDetailsBean.setToDate(sessionDetailsObject.getString("to_date"));
                            sessionDetailsBean.setFromDateFormatted(sessionDetailsObject.getString("from_date_formatted"));
                            sessionDetailsBean.setToDateFormatted(sessionDetailsObject.getString("to_date_formatted"));
                            sessionDetailsBean.setNumberOfWeeks(sessionDetailsObject.getString("number_of_weeks"));
                            sessionDetailsBean.setStartTime(sessionDetailsObject.getString("start_time"));
                            sessionDetailsBean.setEndTime(sessionDetailsObject.getString("end_time"));
                            sessionDetailsBean.setStartTimeFormatted(sessionDetailsObject.getString("start_time_formatted"));
                            sessionDetailsBean.setEndTimeFormatted(sessionDetailsObject.getString("end_time_formatted"));
                            sessionDetailsBean.setNumberOfHours(sessionDetailsObject.getString("number_of_hours"));
                            sessionDetailsBean.setCost(sessionDetailsObject.getString("cost"));
                            sessionDetailsBean.setDisplayCost(sessionDetailsObject.getString("display_cost"));
                            sessionDetailsBean.setIsSelectiveAllowed(sessionDetailsObject.getString("is_selective_allowed"));
                            sessionDetailsBean.setBookingClose(sessionDetailsObject.getString("booking_close"));
                            sessionDetailsBean.setSessionState(sessionDetailsObject.getString("session_state"));
                            sessionDetailsBean.setMaxLimit(sessionDetailsObject.getString("max_limit"));
                            sessionDetailsBean.setThreshold(sessionDetailsObject.getString("threshold"));
                            sessionDetailsBean.setCatchUp(sessionDetailsObject.getString("catch_up"));
                            sessionDetailsBean.setGroupName(sessionDetailsObject.getString("group_name"));
                            sessionDetailsBean.setAgeGroupId(sessionDetailsObject.getString("age_group_id"));
                            sessionDetailsBean.setFileName(sessionDetailsObject.getString("file_name"));
                            sessionDetailsBean.setIsTrial(sessionDetailsObject.getString("is_trial"));
                            sessionDetailsBean.setTrialCount(sessionDetailsObject.getString("trial_count"));
                            sessionDetailsBean.setSessionsBooked(sessionDetailsObject.getString("sessions_booked"));
                            sessionDetailsBean.setDayLabel(sessionDetailsObject.getString("day_label"));
                            sessionDetailsBean.setSubmitEnable(sessionDetailsObject.getBoolean("submit_enable"));
                            sessionDetailsBean.setReportDate(sessionDetailsObject.getString("report_date"));

                            JSONArray childScoreArray = dataObject.getJSONArray("child_score");

                            for(int i=0;i<childScoreArray.length();i++){
                                JSONObject scoreDataObject = childScoreArray.getJSONObject(i);

                                ScoresDataBean scoresDataBean = new ScoresDataBean();
                                scoresDataBean.setChildId(scoreDataObject.getString("child_id"));
                                scoresDataBean.setChildName(scoreDataObject.getString("child_name"));
                                scoresDataBean.setTotalScore(scoreDataObject.getString("total_score"));

                                ArrayList<ChildScoreBean> childScoresList = new ArrayList<>();
                                JSONArray childScoresArray = scoreDataObject.getJSONArray("child_scores");
                                for(int j=0;j<childScoresArray.length();j++){
                                    JSONObject scoreObject = childScoresArray.getJSONObject(j);

                                    ChildScoreBean childScoreBean = new ChildScoreBean();
                                    childScoreBean.setScoreLocked(scoreObject.getBoolean("score_locked"));
                                    childScoreBean.setScoresId(scoreObject.getString("scores_id"));
                                    childScoreBean.setScore(scoreObject.getString("score"));
                                    childScoreBean.setCoachId(scoreObject.getString("coach_id"));
                                    childScoreBean.setPerformanceElementId(scoreObject.getString("performance_element_id"));
                                    childScoreBean.setParentId(scoreObject.getString("parent_id"));
                                    childScoreBean.setColorCode(scoreObject.getString("color_code"));
                                    childScoreBean.setElementName(scoreObject.getString("element_name"));

                                    childScoresList.add(childScoreBean);
                                }

                                scoresDataBean.setChildrenScoresListing(childScoresList);
                                scoresDataBean.setAttendanceEnable(scoreDataObject.getBoolean("attendance_enable"));
                                scoresDataBean.setShowEnrollmentDate(scoreDataObject.getString("enrollment_date_formatted"));
                                scoresDataBean.setLatestScoreDate(scoreDataObject.getString("latest_score_date"));

                                scoresDataList.add(scoresDataBean);
                            }

                            /*JSONArray dataArray = responseObject.getJSONArray("data");
                            ScoresDataBean scoresDataBean;

                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject scoreDataObject = dataArray.getJSONObject(i);
                                scoresDataBean = new ScoresDataBean();

                                scoresDataBean.setSessionsId(scoreDataObject.getString("sessions_id"));
                                scoresDataBean.setChildId(scoreDataObject.getString("child_id"));
                                scoresDataBean.setChildName(scoreDataObject.getString("child_name"));
                                scoresDataBean.setLocationName(scoreDataObject.getString("locations_name"));
                                scoresDataBean.setTermName(scoreDataObject.getString("term_name"));
                                scoresDataBean.setTermId(scoreDataObject.getString("terms_id"));
                                scoresDataBean.setDay(scoreDataObject.getString("day"));
                                scoresDataBean.setDayLabel(scoreDataObject.getString("day_label"));
                                scoresDataBean.setGroupName(scoreDataObject.getString("group_name"));
                                scoresDataBean.setEnrollmentDate(scoreDataObject.getString("enrollment_date"));
                                scoresDataBean.setShowEnrollmentDate(scoreDataObject.getString("enrollment_date_formatted"));
                                scoresDataBean.setTotalScore(scoreDataObject.getString("total_score"));

                                JSONArray childScoresArray = scoreDataObject.getJSONArray("child_scores");
                                ArrayList<ChildScoreBean> childScoreList = new ArrayList<>();
                                ChildScoreBean childScoreBean;
                                for(int j=0;j<childScoresArray.length();j++) {
                                    JSONObject childScoreObject = childScoresArray.getJSONObject(j);
                                    childScoreBean = new ChildScoreBean();

                                    childScoreBean.setScoresId(childScoreObject.getString("scores_id"));
                                    childScoreBean.setScore(childScoreObject.getString("score"));
                                    childScoreBean.setPerformanceElementId(childScoreObject.getString("performance_element_id"));
                                    childScoreBean.setParentId(childScoreObject.getString("parent_id"));
                                    childScoreBean.setElementName(childScoreObject.getString("element_name"));
                                    childScoreBean.setCoachId(childScoreObject.getString("coach_id"));
                                    childScoreBean.setScoreLocked(childScoreObject.getBoolean("score_locked"));

                                    childScoreList.add(childScoreBean);
                                }
                                scoresDataBean.setChildrenScoresListing(childScoreList);

                                scoresDataBean.setLatestScoreDate(scoreDataObject.getString("latest_score_date"));

                                scoresDataList.add(scoresDataBean);
                            }*/

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachScoresListingAdapter = new CoachScoresListingAdapter(getActivity(), sessionDetailsBean, scoresDataList);
                scoresDataListView.setAdapter(coachScoresListingAdapter);
                break;
        }
    }

    public void showSearchLinearLayout() {
        searchLinearLayout.setVisibility(View.VISIBLE);
    }

    private void changeFonts() {
        searchButton.setTypeface(linoType);
    }

}
