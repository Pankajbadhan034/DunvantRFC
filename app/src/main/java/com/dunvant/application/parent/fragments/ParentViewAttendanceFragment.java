package com.dunvant.application.parent.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.dunvant.application.beans.AgeGroupAttendanceBean;
import com.dunvant.application.beans.AttendanceDateBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentAgeGroupSpinnerAdapter;
import com.dunvant.application.parent.adapters.ParentAttendanceListingAdapter;
import com.dunvant.application.parent.adapters.ParentChildrenListingForSpinnerAdapter;
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

public class ParentViewAttendanceFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    LinearLayout searchLinearLayout;
    Spinner childrenSpinner;
    Spinner sessionSpinner;
    Spinner ageGroupSpinner;
    Button go;
    ListView attendanceListView;
    Button sessionsAttended;
    Button sessionsRemaining;

    private final String GET_CHILDREN_LISTING = "GET_CHILDREN_LISTING";
    private final String GET_SESSIONS_LISTING = "GET_SESSIONS_LISTING";
    private final String GET_ATTENDANCE_LISTING = "GET_ATTENDANCE_LISTING";

    ArrayList<ChildBean> childrenListing = new ArrayList<>();
    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupAttendanceBean> ageGroupAttendanceListing = new ArrayList<>();

    ParentChildrenListingForSpinnerAdapter parentChildrenListingForSpinnerAdapter;
    ParentDaysListingAdapter parentDaysListingAdapter;
    ParentAgeGroupSpinnerAdapter parentAgeGroupSpinnerAdapter;

    ChildBean clickedOnChild;
    CampDaysBean clickedOnDay;
    AgeGroupAttendanceBean clickedOnAgeGroup;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_view_attendance, container, false);

        searchLinearLayout = (LinearLayout) view.findViewById(R.id.searchLinearLayout);
        childrenSpinner = (Spinner) view.findViewById(R.id.childrenSpinner);
        sessionSpinner = (Spinner) view.findViewById(R.id.sessionSpinner);
        ageGroupSpinner = (Spinner) view.findViewById(R.id.ageGroupSpinner);
        go = (Button) view.findViewById(R.id.go);
        attendanceListView = (ListView) view.findViewById(R.id.attendanceListView);
        sessionsAttended = (Button) view.findViewById(R.id.sessionsAttended);
        sessionsRemaining = (Button) view.findViewById(R.id.sessionsRemaining);

        changeFonts();
        getChildrenListing();

        attendanceListView.setVisibility(View.GONE);
        sessionsAttended.setVisibility(View.GONE);
        sessionsRemaining.setVisibility(View.GONE);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Utilities.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    return;
                }

                int childrenPosition = childrenSpinner.getSelectedItemPosition();
                int sessionPosition = sessionSpinner.getSelectedItemPosition();
                int ageGroupPosition = ageGroupSpinner.getSelectedItemPosition();

                if(childrenPosition == 0) {
                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                    Toast.makeText(getActivity(), "Please choose "+verbiage_singular, Toast.LENGTH_SHORT).show();

                } else if (sessionPosition == 0) {
                    Toast.makeText(getActivity(), "Please choose Session Day", Toast.LENGTH_SHORT).show();
                } else if (ageGroupPosition == 0) {
                    Toast.makeText(getActivity(), "Please choose Age Group", Toast.LENGTH_SHORT).show();
                } else {
                    clickedOnAgeGroup = ageGroupAttendanceListing.get(ageGroupPosition);
                    showAttendanceData();
                }
            }
        });

        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ageGroupAttendanceListing.clear();

                    AgeGroupAttendanceBean ageGroupAttendanceBean = new AgeGroupAttendanceBean();
                    ageGroupAttendanceBean.setGroupName("Choose Age Group");

                    ageGroupAttendanceListing.add(ageGroupAttendanceBean);

                    parentAgeGroupSpinnerAdapter = new ParentAgeGroupSpinnerAdapter(getActivity(), ageGroupAttendanceListing);
                    ageGroupSpinner.setAdapter(parentAgeGroupSpinnerAdapter);
                } else {
                    clickedOnDay = daysListing.get(position);
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
                    daysBean.setDayLabel("Choose Session Day");

                    daysListing.add(daysBean);

                    parentDaysListingAdapter = new ParentDaysListingAdapter(getActivity(), daysListing);
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

        return view;
    }

    private void showAttendanceData() {
        sessionsAttended.setText("ATTENDED "+clickedOnAgeGroup.getAttendedSessionsCount()+" OUT OF "+clickedOnAgeGroup.getTotalSessionsCount()+" SESSIONS");
        sessionsRemaining.setText(clickedOnAgeGroup.getRemainingSessionsCount()+" SESSIONS REMAINING");

        attendanceListView.setAdapter(new ParentAttendanceListingAdapter(getActivity(), clickedOnAgeGroup.getAttendanceDatesList()));

        searchLinearLayout.setVisibility(View.GONE);
        attendanceListView.setVisibility(View.VISIBLE);
        sessionsAttended.setVisibility(View.VISIBLE);
        sessionsRemaining.setVisibility(View.VISIBLE);
    }

    public void showSearchLinearLayout(){
        searchLinearLayout.setVisibility(View.VISIBLE);
    }

    private void getAttendanceListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("child_id", clickedOnChild.getId()));
            nameValuePairList.add(new BasicNameValuePair("session_day", clickedOnDay.getDay()));

            String webServiceUrl = Utilities.BASE_URL + "sessions/child_attendance_list_for_parent";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_ATTENDANCE_LISTING, ParentViewAttendanceFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getSessionsListing() {
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("child_id", clickedOnChild.getId()));

            String webServiceUrl = Utilities.BASE_URL + "sessions/session_days_list_for_parent";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_SESSIONS_LISTING, ParentViewAttendanceFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getChildrenListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "children/children_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_CHILDREN_LISTING, ParentViewAttendanceFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_CHILDREN_LISTING:

                childrenListing.clear();

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            ChildBean childBean = new ChildBean();
                            childBean.setId("-1");
                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                            childBean.setFullName("Choose "+verbiage_singular);

                            childrenListing.add(childBean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentChildrenListingForSpinnerAdapter = new ParentChildrenListingForSpinnerAdapter(getActivity(), childrenListing);
                childrenSpinner.setAdapter(parentChildrenListingForSpinnerAdapter);
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

                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Choose Session Day");

                            daysListing.add(daysBean);

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentDaysListingAdapter = new ParentDaysListingAdapter(getActivity(), daysListing);
                sessionSpinner.setAdapter(parentDaysListingAdapter);

                break;

            case GET_ATTENDANCE_LISTING:

                ageGroupAttendanceListing.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            AgeGroupAttendanceBean ageGroupAttendanceBean = new AgeGroupAttendanceBean();
                            ageGroupAttendanceBean.setGroupName("Choose Age Group");

                            ageGroupAttendanceListing.add(ageGroupAttendanceBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject ageGroupAttendanceObject = dataArray.getJSONObject(i);
                                ageGroupAttendanceBean = new AgeGroupAttendanceBean();

                                ageGroupAttendanceBean.setSessionsId(ageGroupAttendanceObject.getString("sessions_id"));
                                ageGroupAttendanceBean.setAgeGroupId(ageGroupAttendanceObject.getString("age_groups_id"));
                                ageGroupAttendanceBean.setGroupName(ageGroupAttendanceObject.getString("group_name"));
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
                                ageGroupAttendanceBean.setRemainingSessionsCount(ageGroupAttendanceObject.getString("remaining_sessions_count"));

                                ageGroupAttendanceListing.add(ageGroupAttendanceBean);
                            }
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            AgeGroupAttendanceBean ageGroupAttendanceBean = new AgeGroupAttendanceBean();
                            ageGroupAttendanceBean.setGroupName("Choose Age Group");

                            ageGroupAttendanceListing.add(ageGroupAttendanceBean);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentAgeGroupSpinnerAdapter = new ParentAgeGroupSpinnerAdapter(getActivity(), ageGroupAttendanceListing);
                ageGroupSpinner.setAdapter(parentAgeGroupSpinnerAdapter);

                break;
        }
    }

    private void changeFonts(){
        go.setTypeface(linoType);
        sessionsAttended.setTypeface(linoType);
        sessionsRemaining.setTypeface(linoType);
    }

}