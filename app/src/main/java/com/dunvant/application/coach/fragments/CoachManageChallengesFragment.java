package com.dunvant.application.coach.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.ChallengeListBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.CoachSetChallengeScreen;
import com.dunvant.application.coach.adapters.CoachChallengesListingAdapter;
import com.dunvant.application.coach.adapters.CoachLocationListingAdapter;
import com.dunvant.application.coach.adapters.CoachOnlyAgeGroupListingAdapter;
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

public class CoachManageChallengesFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Button addNewChallenge;
    LinearLayout searchLinearLayout;
    EditText searchByKeyword;
    Spinner locationSpinner;
    Spinner sessionSpinner;
    Spinner ageGroupSpinner;
    RadioButton activeRadioButton;
    RadioButton expiredRadioButton;
    CheckBox ownChallenges;
    Button goButton;
    ListView challengesListView;

    private final String GET_CHALLENGES_LIST = "GET_CHALLENGES_LIST";
    private final String GET_LOCATION_LISTING = "GET_LOCATION_LISTING";
    private final String GET_SESSIONS_LISTING = "GET_SESSIONS_LISTING";
    private final String GET_AGE_GROUP_LISTING = "GET_AGE_GROUP_LISTING";

    ArrayList<ChallengeListBean> challengeList = new ArrayList<>();
    ArrayList<CampLocationBean> locationsList = new ArrayList<>();
    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupBean> ageGroupsListing = new ArrayList<>();

    CoachChallengesListingAdapter coachChallengesListingAdapter;
    CoachLocationListingAdapter coachLocationListingAdapter;
    CoachSessionListingAdapter coachSessionListingAdapter;
    CoachOnlyAgeGroupListingAdapter coachOnlyAgeGroupListingAdapter;

    String strKeyword;
    String activeOrExpired;
    String clickedOnLocationIds;
    String clickedOnDayIds;
    String clickedOnSessionIds;
    String strOwnChallenges;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coach_fragment_manage_challenges, container, false);

        addNewChallenge = (Button) view.findViewById(R.id.addNewChallenge);
        searchLinearLayout = (LinearLayout) view.findViewById(R.id.searchLinearLayout);
        searchByKeyword = (EditText) view.findViewById(R.id.searchByKeyword);
        locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner);
        sessionSpinner = (Spinner) view.findViewById(R.id.sessionSpinner);
        ageGroupSpinner = (Spinner) view.findViewById(R.id.ageGroupSpinner);
        activeRadioButton = (RadioButton) view.findViewById(R.id.activeRadioButton);
        expiredRadioButton = (RadioButton) view.findViewById(R.id.expiredRadioButton);
        ownChallenges = view.findViewById(R.id.ownChallenges);
        goButton = (Button) view.findViewById(R.id.goButton);
        challengesListView = (ListView) view.findViewById(R.id.challengesListView);

        coachChallengesListingAdapter = new CoachChallengesListingAdapter(getActivity(), challengeList, challengesListView);
        challengesListView.setAdapter(coachChallengesListingAdapter);

        searchLinearLayout.setVisibility(View.GONE);
        activeRadioButton.setChecked(true);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Utilities.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    return;
                }

                strKeyword = searchByKeyword.getText().toString().trim();
                int locationPosition = locationSpinner.getSelectedItemPosition();
                int sessionPosition = sessionSpinner.getSelectedItemPosition();
                int ageGroupPosition = ageGroupSpinner.getSelectedItemPosition();

                if(activeRadioButton.isChecked()) {
                    activeOrExpired = "active";
                } else {
                    activeOrExpired = "expire";
                }

                /*if(strKeyword == null || strKeyword.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Keyword", Toast.LENGTH_SHORT).show();
                } else if (locationPosition == 0) {
                    Toast.makeText(getActivity(), "Please select Location", Toast.LENGTH_SHORT).show();
                } else if (sessionPosition == 0) {
                    Toast.makeText(getActivity(), "Please select Session", Toast.LENGTH_SHORT).show();
                } else if (ageGroupPosition == 0) {
                    Toast.makeText(getActivity(), "Please select Age Group", Toast.LENGTH_SHORT).show();
                } else {
                    if(locationPosition == 1) {
                        clickedOnLocationIds = "";
                    }
                    if(ageGroupPosition == 1) {
                        clickedOnSessionIds = "";
                    }

                    getFilteredChallengesList();
                }*/

                if(strKeyword == null || strKeyword.isEmpty()){
                    strKeyword = "";
                }

                if(locationPosition == 0 || locationPosition == 1){
                    clickedOnLocationIds = "";
                }

                if(sessionPosition == 0){

                }

                if(ageGroupPosition == 0 || ageGroupPosition == 1){
                    clickedOnSessionIds = "";
                }

                if(ownChallenges.isChecked()){
                    strOwnChallenges = "1";
                } else {
                    strOwnChallenges = "0";
                }

                getFilteredChallengesList();

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
                } else if (position == 1) {

                    clickedOnLocationIds = "";
                    for(CampLocationBean locationBean : locationsList) {

                        if(Integer.parseInt(locationBean.getLocationId()) > 0) {
                            clickedOnLocationIds += locationBean.getLocationId()+",";
                        }

                    }

                    if (clickedOnLocationIds != null && clickedOnLocationIds.length() > 0 && clickedOnLocationIds.charAt(clickedOnLocationIds.length()-1)==',') {
                        clickedOnLocationIds = clickedOnLocationIds.substring(0, clickedOnLocationIds.length()-1);
                    }

                    getSessionDaysListing();

                } else {
//                    clickedOnLocation = locationsList.get(position);

                    clickedOnLocationIds = locationsList.get(position).getLocationId();

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
                } else if (position == 1) {

                    clickedOnDayIds = "";
                    for(CampDaysBean campDaysBean : daysListing) {
                        if(Integer.parseInt(campDaysBean.getDay()) >= 0) {
                            clickedOnDayIds += campDaysBean.getDay()+",";
                        }
                    }

                    if (clickedOnDayIds != null && clickedOnDayIds.length() > 0 && clickedOnDayIds.charAt(clickedOnDayIds.length()-1)==',') {
                        clickedOnDayIds = clickedOnDayIds.substring(0, clickedOnDayIds.length()-1);
                    }
                    getAgeGroupsListing();

                } else {
//                    clickedOnDay = daysListing.get(position);
                    clickedOnDayIds = daysListing.get(position).getDay();
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
                    /*childrenListing.clear();

                    ChildBean childBean = new ChildBean();
                    childBean.setId("-1");
                    childBean.setFullName("Choose Child");
                    childrenListing.add(childBean);

                    coachChildNamesSpinnerAdapter = new CoachChildNamesSpinnerAdapter(CoachSetChallengeScreen.this, childrenListing);
                    childrenSpinner.setAdapter(coachChildNamesSpinnerAdapter);*/

                } else if (position == 1) {

                    clickedOnSessionIds = "";
                    for(AgeGroupBean ageGroupBean : ageGroupsListing) {

                        if(Integer.parseInt(ageGroupBean.getSessionId()) > 0) {
                            clickedOnSessionIds += ageGroupBean.getSessionId()+",";
                        }

                    }

                    if (clickedOnSessionIds != null && clickedOnSessionIds.length() > 0 && clickedOnSessionIds.charAt(clickedOnSessionIds.length()-1)==',') {
                        clickedOnSessionIds = clickedOnSessionIds.substring(0, clickedOnSessionIds.length()-1);
                    }
//                    getAllChildrenListing();

                } else {
                    clickedOnSessionIds = ageGroupsListing.get(position).getSessionId();
//                    getAllChildrenListing();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addNewChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewChallenge = new Intent(getActivity(), CoachSetChallengeScreen.class);
                startActivity(addNewChallenge);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getChallengesList();
    }

    private void getChallengesList(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("search_keyword", ""));
            nameValuePairList.add(new BasicNameValuePair("locations_id", ""));
            nameValuePairList.add(new BasicNameValuePair("sessions_id", ""));
            nameValuePairList.add(new BasicNameValuePair("status", "active"));
            nameValuePairList.add(new BasicNameValuePair("own_challenges", "0"));

            String webServiceUrl = Utilities.BASE_URL + "coach/challenges_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_CHALLENGES_LIST, CoachManageChallengesFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getFilteredChallengesList() {
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("search_keyword", strKeyword));
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocationIds));
            nameValuePairList.add(new BasicNameValuePair("sessions_id", clickedOnSessionIds));
            nameValuePairList.add(new BasicNameValuePair("status", activeOrExpired));
            nameValuePairList.add(new BasicNameValuePair("own_challenges", strOwnChallenges));

            String webServiceUrl = Utilities.BASE_URL + "coach/challenges_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_CHALLENGES_LIST, CoachManageChallengesFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocationListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "coach/locations_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_LOCATION_LISTING, CoachManageChallengesFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getSessionDaysListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_ids", clickedOnLocationIds));
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocationIds));

//            String webServiceUrl = Utilities.BASE_URL + "coach/all_session_days_list";
            String webServiceUrl = Utilities.BASE_URL + "coach/session_days_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_SESSIONS_LISTING, CoachManageChallengesFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAgeGroupsListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocationIds));
            nameValuePairList.add(new BasicNameValuePair("locations_ids", clickedOnLocationIds));
            nameValuePairList.add(new BasicNameValuePair("session_days", clickedOnDayIds));

//            String webServiceUrl = Utilities.BASE_URL + "coach/all_age_group_list";
            String webServiceUrl = Utilities.BASE_URL + "coach/age_group_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_AGE_GROUP_LISTING, CoachManageChallengesFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_CHALLENGES_LIST:

                challengeList.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            ChallengeListBean challengeListBean;
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject challengeListObject = dataArray.getJSONObject(i);
                                challengeListBean = new ChallengeListBean();

                                challengeListBean.setChallengeId(challengeListObject.getString("id"));
                                challengeListBean.setImageId(challengeListObject.getString("image_id"));
                                challengeListBean.setVideoId(challengeListObject.getString("video_id"));
                                challengeListBean.setOwnerId(challengeListObject.getString("owner_id"));
                                challengeListBean.setCategoryId(challengeListObject.getString("category_id"));
                                challengeListBean.setTitle(challengeListObject.getString("title"));
                                challengeListBean.setDescription(challengeListObject.getString("description"));
                                challengeListBean.setTargetScore(challengeListObject.getString("target_score"));
                                challengeListBean.setTargetTime(challengeListObject.getString("target_time"));
                                challengeListBean.setTargetTimeType(challengeListObject.getString("target_time_type"));
                                challengeListBean.setExpiration(challengeListObject.getString("expiration"));
                                challengeListBean.setApprovalRequired(challengeListObject.getString("approval_required"));
                                challengeListBean.setState(challengeListObject.getString("state"));
                                challengeListBean.setIsGlobal(challengeListObject.getString("is_global"));
                                challengeListBean.setCreatedAt(challengeListObject.getString("created_at"));
                                challengeListBean.setShowExpiration(challengeListObject.getString("expiration_formatted"));
                                challengeListBean.setExpirationDate(challengeListObject.getString("expiration_date"));
                                challengeListBean.setShowExpirationDate(challengeListObject.getString("expiration_date_formatted"));
                                challengeListBean.setExpirationTime(challengeListObject.getString("expiration_time"));
                                challengeListBean.setShowExpirationTime(challengeListObject.getString("expiration_time_formatted"));
                                challengeListBean.setLocationIds(challengeListObject.getString("locations_ids"));
                                challengeListBean.setLocationNames(challengeListObject.getString("location_name"));
                                challengeListBean.setSessionIds(challengeListObject.getString("sessions_ids"));
                                challengeListBean.setAgeGroupNames(challengeListObject.getString("group_names"));
                                challengeListBean.setDays(challengeListObject.getString("days"));
                                challengeListBean.setChallengeImageUrl(challengeListObject.getString("challenge_image_url"));
                                challengeListBean.setChallengeVideoUrl(challengeListObject.getString("challenge_video_url"));
                                challengeListBean.setCategoryName(challengeListObject.getString("category_name"));
                                challengeListBean.setDayLabel(challengeListObject.getString("day_label"));
                                challengeListBean.setTargetTimeFormatted(challengeListObject.getString("target_time_formatted"));

                                challengeList.add(challengeListBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachChallengesListingAdapter.notifyDataSetChanged();
                Utilities.setListViewHeightBasedOnChildren(challengesListView);
                searchLinearLayout.setVisibility(View.GONE);

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

                            locationBean = new CampLocationBean();
                            locationBean.setLocationId("-2");
                            locationBean.setLocationName("All");

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
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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

                            daysBean = new CampDaysBean();
                            daysBean.setDay("-2");
                            daysBean.setDayLabel("All");

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
                            ageGroupBean.setSessionId("-1");
                            ageGroupBean.setGroupName("Choose Age Group");

                            ageGroupsListing.add(ageGroupBean);

                            ageGroupBean = new AgeGroupBean();
                            ageGroupBean.setAgeGroupId("-2");
                            ageGroupBean.setSessionId("-2");
                            ageGroupBean.setGroupName("All");

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
        }
    }

    public void showSearchLinearLayout() {
        searchLinearLayout.setVisibility(View.VISIBLE);

        getLocationListing();
    }
}