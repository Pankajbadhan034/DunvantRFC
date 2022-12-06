package com.dunvant.application.child.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupAttendanceBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.UserBean;
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

import androidx.fragment.app.Fragment;

public class ChildViewMarksFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

//    LinearLayout searchLinearLayout;
    RelativeLayout searchRelativeLayout;
    Spinner sessionSpinner;
    Button goButton;
    ListView termNameListView;

    private final String GET_SESSIONS_LISTING = "GET_SESSIONS_LISTING";
    private final String GET_ATTENDANCE_LIST = "GET_ATTENDANCE_LIST";

    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupAttendanceBean> ageGroupAttendanceListing = new ArrayList<>();

    ParentDaysListingAdapter parentDaysListingAdapter;
    ParentTermNameListingAdapter parentTermNameListingAdapter;

    CampDaysBean clickedOnDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_fragment_view_marks, container, false);

//        searchLinearLayout = (LinearLayout) view.findViewById(R.id.searchLinearLayout);
        searchRelativeLayout = (RelativeLayout) view.findViewById(R.id.searchRelativeLayout);
        sessionSpinner = (Spinner) view.findViewById(R.id.sessionSpinner);
        goButton = (Button) view.findViewById(R.id.goButton);
        termNameListView = (ListView) view.findViewById(R.id.termNameListView);

        /*termNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AgeGroupAttendanceBean clickedOnAgeGroup = ageGroupAttendanceListing.get(position);

//                if(clickedOnAgeGroup.getTotalScore() != null && !clickedOnAgeGroup.getTotalScore().isEmpty()){
                if(clickedOnAgeGroup.getScoreId() != null && !clickedOnAgeGroup.getScoreId().isEmpty() && !clickedOnAgeGroup.getScoreId().equalsIgnoreCase("null")){
                    Intent childReport = new Intent(getActivity(), ParentChildReportScreen.class);
                    childReport.putExtra("clickedOnAgeGroup", clickedOnAgeGroup);
                    startActivity(childReport);
                } else {
                    Toast.makeText(getActivity(), "Score not uploaded yet", Toast.LENGTH_SHORT).show();
                }

            }
        });*/

        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                clickedOnDay = daysListing.get(position);

                /*if (position == 0) {

                } else {
                    clickedOnDay = daysListing.get(position);
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickedOnDay = daysListing.get(sessionSpinner.getSelectedItemPosition());
                getAttendanceListing();

                /*int sessionPosition = sessionSpinner.getSelectedItemPosition();

                if (sessionPosition == 0) {
                    Toast.makeText(getActivity(), "Please select Session Day", Toast.LENGTH_SHORT).show();
                } else {
                    getAttendanceListing();
                }*/
            }
        });

        getSessionsListing();

        return view;
    }

    private void getSessionsListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

//
//            String webServiceUrl = Utilities.BASE_URL + "sessions/session_days_list_for_child";
//
//            ArrayList<String> headers = new ArrayList<>();
//            headers.add("X-access-uid:"+loggedInUser.getId());
//            headers.add("X-access-token:"+loggedInUser.getToken());
//
//            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_SESSIONS_LISTING, ChildViewMarksFragment.this, headers);
//            getWebServiceWithHeadersAsync.execute(webServiceUrl);



            String webServiceUrl = Utilities.BASE_URL + "sessions/session_days_list_for_child";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_SESSIONS_LISTING, ChildViewMarksFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAttendanceListing() {
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("session_day", clickedOnDay.getDay()));

//            String webServiceUrl = Utilities.BASE_URL + "sessions/child_attendance_list";
            String webServiceUrl = Utilities.BASE_URL + "sessions/child_score_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_ATTENDANCE_LIST, ChildViewMarksFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_SESSIONS_LISTING:

                daysListing.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");

                            /*CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Choose Session Day");*/
                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("0");
                            daysBean.setDayLabel("All");


                            daysListing.add(daysBean);

                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject dayObject = dataArray.getJSONObject(i);
                                daysBean = new CampDaysBean();

                                daysBean.setDay(dayObject.getString("day"));
                                daysBean.setDayLabel(dayObject.getString("day_label"));

                                daysListing.add(daysBean);
                            }
                        } else {

                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("0");
                            daysBean.setDayLabel("All");
                            /*daysBean.setDay("-1");
                            daysBean.setDayLabel("Choose Session Day");*/

                            daysListing.add(daysBean);

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {

                        CampDaysBean daysBean = new CampDaysBean();
                        daysBean.setDay("0");
                        daysBean.setDayLabel("All");
                        /*daysBean.setDay("-1");
                        daysBean.setDayLabel("Choose Session Day");*/

                        daysListing.add(daysBean);

                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentDaysListingAdapter = new ParentDaysListingAdapter(getActivity(), daysListing);
                sessionSpinner.setAdapter(parentDaysListingAdapter);

                goButton.performClick();

                break;

            case GET_ATTENDANCE_LIST:

                ageGroupAttendanceListing.clear();

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {

                    searchRelativeLayout.setVisibility(View.GONE);

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
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentTermNameListingAdapter = new ParentTermNameListingAdapter(getActivity(), ageGroupAttendanceListing);
                termNameListView.setAdapter(parentTermNameListingAdapter);

                break;
        }
    }

    public void showSearchLinearLayout(){
        searchRelativeLayout.setVisibility(View.VISIBLE);
    }
}