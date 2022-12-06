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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.ChildAttendanceBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.CoachMidWeekPackageChildNamesAttendanceActivity;
import com.dunvant.application.coach.adapters.CoachLocationListingAdapter;
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

public class CoachMidWeekAttendanceFragment extends Fragment implements IWebServiceCallback {
    String idHere;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    LinearLayout searchLinearLayout;
    Spinner locationSpinner;
    Spinner sessionSpinner;
    Button searchButton;

    private final String GET_LOCATION_LISTING = "GET_LOCATION_LISTING";
    private final String GET_SESSIONS_LISTING = "GET_SESSIONS_LISTING";
    private final String MARK_ATTENDANCE = "MARK_ATTENDANCE";

    ArrayList<CampLocationBean> locationsList = new ArrayList<>();
    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupBean> ageGroupsListing = new ArrayList<>();

    CoachLocationListingAdapter coachLocationListingAdapter;
    CoachSessionListingAdapter coachSessionListingAdapter;

    CampLocationBean clickedOnLocation;
    ArrayList<ChildAttendanceBean> childrenAttendanceListing = new ArrayList<>();
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
        View view = inflater.inflate(R.layout.coach_fragment_midweek_attendance, container, false);

        searchLinearLayout = (LinearLayout) view.findViewById(R.id.searchLinearLayout);
        locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner);
        sessionSpinner = (Spinner) view.findViewById(R.id.sessionSpinner);
        locationSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        sessionSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        searchButton = (Button) view.findViewById(R.id.searchButton);

        changeFonts();
        getLocationListing();



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int locationPosition = locationSpinner.getSelectedItemPosition();
                int sessionPosition = sessionSpinner.getSelectedItemPosition();

           if(locationPosition == 0) {
                    Toast.makeText(getActivity(), "Please choose Location", Toast.LENGTH_SHORT).show();
                } else if (sessionPosition == 0) {
                    Toast.makeText(getActivity(), "Please choose Midweek Session", Toast.LENGTH_SHORT).show();
                }else {


               Intent obj = new Intent(getActivity(), CoachMidWeekPackageChildNamesAttendanceActivity.class);
               obj.putExtra("midweek_session",idHere);
               startActivity(obj);
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
                    daysBean.setDayLabel("Choose Midweek Session");

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

                } else {
                     idHere = daysListing.get(position).getDay();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
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

            String webServiceUrl = Utilities.BASE_URL + "coach/coach_midweek_locations_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_LOCATION_LISTING, CoachMidWeekAttendanceFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);


        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getSessionDaysListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("selected_location", clickedOnLocation.getLocationId()));

            String webServiceUrl = Utilities.BASE_URL + "coach/coach_midweek_packages_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_SESSIONS_LISTING, CoachMidWeekAttendanceFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }





    private void getChildrenListingForAttendance() {
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("midweek_session", "782"));

            String webServiceUrl = Utilities.BASE_URL + "coach/coach_midweek_attendance_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, MARK_ATTENDANCE, CoachMidWeekAttendanceFragment.this, headers);
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

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, MARK_ATTENDANCE, CoachMidWeekAttendanceFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {


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

                                locationBean.setLocationId(locationObject.getString("id"));
                                locationBean.setLocationName(locationObject.getString("name"));
                                locationBean.setDescription(locationObject.getString("session_description"));
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
                            daysBean.setDayLabel("Choose Midweek Session");

                            daysListing.add(daysBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject daysObject = dataArray.getJSONObject(i);
                                daysBean = new CampDaysBean();
                                daysBean.setDay(daysObject.getString("id"));
                                daysBean.setDayLabel(daysObject.getString("title"));

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

    private void changeFonts(){
        searchButton.setTypeface(linoType);

    }
}