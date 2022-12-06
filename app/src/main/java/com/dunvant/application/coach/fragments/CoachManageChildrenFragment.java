package com.dunvant.application.coach.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
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
import com.dunvant.application.beans.AgeGroupBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.CoachingProgramBean;
import com.dunvant.application.beans.DatesResultBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachAgeGroupListingAdapter;
import com.dunvant.application.coach.adapters.CoachCoachingProgramAdapter;
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

public class CoachManageChildrenFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    Spinner coachingProgramSpinner;
    Spinner locationSpinner;
    Spinner sessionSpinner;
    Button search;
    ListView childrenListView;
    LinearLayout searchLinearLayout;

    private final String GET_COACHING_PROGRAM_LISTING = "GET_COACHING_PROGRAM_LISTING";
    private final String GET_LOCATION_LISTING = "GET_LOCATION_LISTING";
    private final String GET_SESSIONS_LISTING = "GET_SESSIONS_LISTING";
    private final String GET_CHILDREN_LISTING = "GET_CHILDREN_LISTING";

    ArrayList<CoachingProgramBean> coachingProgramsList = new ArrayList<>();
    ArrayList<CampLocationBean> locationsList = new ArrayList<>();
    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupBean> ageGroupsList = new ArrayList<>();

    CoachCoachingProgramAdapter coachCoachingProgramAdapter;
    CoachLocationListingAdapter coachLocationListingAdapter;
    CoachSessionListingAdapter coachSessionListingAdapter;
    CoachAgeGroupListingAdapter coachAgeGroupListingAdapter;

    CoachingProgramBean clickedOnCoachingProgram;
    CampLocationBean clickedOnLocation;
    CampDaysBean clickedOnDay;

    public static boolean comingFromMoveChild = false;

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

//        coachLocationListingAdapter = new CoachLocationListingAdapter(getActivity(), locationsList);
//        coachSessionListingAdapter = new CoachSessionListingAdapter(getActivity(), daysListing);
//        coachAgeGroupListingAdapter = new CoachAgeGroupListingAdapter(getActivity(), ageGroupsList);

        comingFromMoveChild = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coach_fragment_manage_children, container, false);

        coachingProgramSpinner = (Spinner) view.findViewById(R.id.coachingProgramSpinner);
        locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner);
        sessionSpinner = (Spinner) view.findViewById(R.id.sessionSpinner);
        search = (Button) view.findViewById(R.id.search);
        childrenListView = (ListView) view.findViewById(R.id.childrenListView);
        searchLinearLayout = (LinearLayout) view.findViewById(R.id.searchLinearLayout);

        coachingProgramSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        locationSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        sessionSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


//        getLocationListing();
        getCoachingProgramListing();
        changeFonts();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Utilities.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    return;
                }

                int coachingProgramPosition = coachingProgramSpinner.getSelectedItemPosition();
                int locationPosition = locationSpinner.getSelectedItemPosition();
                int sessionPosition = sessionSpinner.getSelectedItemPosition();

                if(coachingProgramPosition == 0){
                    Toast.makeText(getActivity(), "Please choose Coaching Program", Toast.LENGTH_SHORT).show();
                } else if (locationPosition == 0) {
                    Toast.makeText(getActivity(), "Please choose Location", Toast.LENGTH_SHORT).show();
                } else if (sessionPosition == 0) {
                    Toast.makeText(getActivity(), "Please choose Session", Toast.LENGTH_SHORT).show();
                } else {
                    clickedOnDay = daysListing.get(sessionSpinner.getSelectedItemPosition());
                    getGroupWiseChildrenListing();
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
//                    coachSessionListingAdapter.notifyDataSetChanged();
                } else {
                    clickedOnLocation = locationsList.get(position);
                    getSessionDaysListing();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(comingFromMoveChild) {
            getGroupWiseChildrenListing();
            comingFromMoveChild = false;
        }
    }

    private void getCoachingProgramListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "coach/coaching_program_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_COACHING_PROGRAM_LISTING, CoachManageChildrenFragment.this, headers);
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

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_LOCATION_LISTING, CoachManageChildrenFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);*/

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("program_id", clickedOnCoachingProgram.getCoachingProgramId()));

            String webServiceUrl = Utilities.BASE_URL + "coach/get_location_list_for_coach_by_program_ID";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_LOCATION_LISTING, CoachManageChildrenFragment.this, headers);
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

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_SESSIONS_LISTING, CoachManageChildrenFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getGroupWiseChildrenListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("program_id", clickedOnCoachingProgram.getCoachingProgramId()));
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocation.getLocationId()));
            nameValuePairList.add(new BasicNameValuePair("session_day", clickedOnDay.getDay()));

            String webServiceUrl = Utilities.BASE_URL + "coach/children_booked_session_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_CHILDREN_LISTING, CoachManageChildrenFragment.this, headers);
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

                // 0th Element Choose Coaching Program
                CoachingProgramBean coachingProgramBean = new CoachingProgramBean();
                coachingProgramBean.setCoachingProgramId("-1");
                coachingProgramBean.setCoachinProgramName("Choose Coaching Program");

                coachingProgramsList.add(coachingProgramBean);

                if(response == null){
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

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

                // 0th Element Choose Location
                CampLocationBean locationBean = new CampLocationBean();
                locationBean.setLocationId("-1");
                locationBean.setLocationName("Choose Location");

                locationsList.add(locationBean);

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
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
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

//                coachLocationListingAdapter.notifyDataSetChanged();

                coachLocationListingAdapter = new CoachLocationListingAdapter(getActivity(), locationsList);
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
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

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

//                coachSessionListingAdapter.notifyDataSetChanged();
                coachSessionListingAdapter = new CoachSessionListingAdapter(getActivity(), daysListing);
                sessionSpinner.setAdapter(coachSessionListingAdapter);

                break;

            case GET_CHILDREN_LISTING:
                ageGroupsList.clear();
                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            searchLinearLayout.setVisibility(View.GONE);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            AgeGroupBean ageGroupBean;
                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject ageGroupObject = dataArray.getJSONObject(i);

                                ageGroupBean = new AgeGroupBean();
                                ageGroupBean.setAgeGroupId(ageGroupObject.getString("age_groups_id"));
                                ageGroupBean.setGroupName(ageGroupObject.getString("group_name"));

                                JSONArray datesResultArray = ageGroupObject.getJSONArray("b_dates_result");

                                ArrayList<DatesResultBean> datesResultList = new ArrayList<>();
                                DatesResultBean datesResultBean;
                                for(int j=0;j<datesResultArray.length();j++) {

                                    JSONObject dateResultObject = datesResultArray.getJSONObject(j);

                                    datesResultBean = new DatesResultBean();

                                    datesResultBean.setSessionId(dateResultObject.getString("sessions_id"));
                                    datesResultBean.setCoachingProgramId(dateResultObject.getString("coaching_programs_id"));
                                    datesResultBean.setCoachingProgramName(dateResultObject.getString("coaching_programs_name"));
                                    datesResultBean.setTermsId(dateResultObject.getString("terms_id"));
                                    datesResultBean.setTermsName(dateResultObject.getString("terms_name"));
                                    datesResultBean.setLocationId(dateResultObject.getString("locations_id"));
                                    datesResultBean.setLocationName(dateResultObject.getString("locations_name"));
                                    datesResultBean.setAgeGroupId(dateResultObject.getString("age_groups_id"));
                                    datesResultBean.setGroupName(dateResultObject.getString("group_name"));
                                    datesResultBean.setFromAge(dateResultObject.getInt("from_age"));
                                    datesResultBean.setToAge(dateResultObject.getInt("to_age"));
                                    datesResultBean.setDay(dateResultObject.getInt("day"));
                                    datesResultBean.setUsersId(dateResultObject.getString("users_id"));
                                    datesResultBean.setChildName(dateResultObject.getString("child_name"));
                                    datesResultBean.setOrdersIds(dateResultObject.getString("orders_ids"));
                                    datesResultBean.setBookingDates(dateResultObject.getString("booking_dates"));
                                    datesResultBean.setDayLabel(dateResultObject.getString("day_label"));
                                    datesResultBean.setMovedData(dateResultObject.getString("moved_data"));
                                    datesResultBean.setIsTrial(dateResultObject.getString("is_trial"));

                                    datesResultList.add(datesResultBean);
                                }
                                ageGroupBean.setDatesResultList(datesResultList);

                                ageGroupsList.add(ageGroupBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

//                coachAgeGroupListingAdapter.notifyDataSetChanged();
                coachAgeGroupListingAdapter = new CoachAgeGroupListingAdapter(getActivity(), ageGroupsList);
                childrenListView.setAdapter(coachAgeGroupListingAdapter);

                break;
        }
    }

    private void changeFonts(){
        search.setTypeface(linoType);
    }

    public void showSearchLayout(){
        searchLinearLayout.setVisibility(View.VISIBLE);
    }

}