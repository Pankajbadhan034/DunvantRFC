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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.CoachAssignedLeagueMatchesBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.CoachLeagueMatchResultScreen;
import com.dunvant.application.coach.adapters.CoachLeagueEditAdapter;
import com.dunvant.application.coach.adapters.CoachLocationListingAdapter;
import com.dunvant.application.coach.adapters.CoachSessionListingAdapter;
import com.dunvant.application.utils.Utilities;
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

public class CoachLeagueEditFragment extends Fragment implements IWebServiceCallback {
    String idHere;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    LinearLayout searchLinearLayout;
    Spinner locationSpinner;
    Spinner sessionSpinner;
    Button searchButton;

    private final String LEAGUE_LIST = "LEAGUE_LIST";
    private final String SCHEDULER_LIST = "SCHEDULER_LIST";
    private final String SUBMIT = "SUBMIT";

    ArrayList<CampLocationBean> locationsList = new ArrayList<>();
    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupBean> ageGroupsListing = new ArrayList<>();
    ArrayList<CoachAssignedLeagueMatchesBean> coachAssignedLeagueMatchesBeanArrayList = new ArrayList<>();

    CoachLocationListingAdapter coachLocationListingAdapter;
    CoachSessionListingAdapter coachSessionListingAdapter;

    CampLocationBean clickedOnLocation;
    ListView listView;

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
        View view = inflater.inflate(R.layout.coach_fragment_edit_league, container, false);

        searchLinearLayout = (LinearLayout) view.findViewById(R.id.searchLinearLayout);
        locationSpinner = (Spinner) view.findViewById(R.id.locationSpinner);
        sessionSpinner = (Spinner) view.findViewById(R.id.sessionSpinner);
        listView = view.findViewById(R.id.listView);

        locationSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        sessionSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        searchButton = (Button) view.findViewById(R.id.searchButton);

        changeFonts();
        getLocationListing();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent obj = new Intent(getActivity(), CoachLeagueMatchResultScreen.class);
                obj.putExtra("id", coachAssignedLeagueMatchesBeanArrayList.get(i).getId());
                obj.putExtra("leagueName", clickedOnLocation.getLocationName());
                startActivity(obj);

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int locationPosition = locationSpinner.getSelectedItemPosition();
                int sessionPosition = sessionSpinner.getSelectedItemPosition();

                if(locationPosition == 0) {
                    Toast.makeText(getActivity(), "Please choose League", Toast.LENGTH_SHORT).show();
                } else if (sessionPosition == 0) {
                    Toast.makeText(getActivity(), "Please choose Scheduler", Toast.LENGTH_SHORT).show();
                }else {
                    listData();
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
                    daysBean.setDayLabel("Choose Scheduler");

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



    private void listData(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("user_id", loggedInUser.getId()));
            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));
            nameValuePairList.add(new BasicNameValuePair("scheduler_id", idHere));

            String webServiceUrl = Utilities.BASE_URL + "league_tournament/coach_assigned_league_matches";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, SUBMIT, CoachLeagueEditFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);



        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getSessionDaysListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));
            nameValuePairList.add(new BasicNameValuePair("user_id", loggedInUser.getId()));
            nameValuePairList.add(new BasicNameValuePair("league_id", clickedOnLocation.getLocationId()));

            String webServiceUrl = Utilities.BASE_URL + "league_tournament/coach_league_scheduler_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, SCHEDULER_LIST, CoachLeagueEditFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocationListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));


            String webServiceUrl = Utilities.BASE_URL + "league_tournament/league_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, LEAGUE_LIST, CoachLeagueEditFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {


            case LEAGUE_LIST:

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
                            locationBean.setLocationName("Choose League");

                            locationsList.add(locationBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject locationObject = dataArray.getJSONObject(i);
                                locationBean = new CampLocationBean();

                                locationBean.setLocationId(locationObject.getString("id"));
                                locationBean.setLocationName(locationObject.getString("name"));

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

            case SCHEDULER_LIST:

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
                            daysBean.setDayLabel("Choose Scheduler");

                            daysListing.add(daysBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject daysObject = dataArray.getJSONObject(i);
                                daysBean = new CampDaysBean();
                                daysBean.setDay(daysObject.getString("id"));
                                daysBean.setDayLabel(daysObject.getString("schedule_name"));

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



            case SUBMIT:

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        coachAssignedLeagueMatchesBeanArrayList.clear();
                        if(status){
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                CoachAssignedLeagueMatchesBean coachAssignedLeagueMatchesBean = new CoachAssignedLeagueMatchesBean();
                                coachAssignedLeagueMatchesBean.setImageUrl(jsonObject.getString("image_url"));
                                coachAssignedLeagueMatchesBean.setTeam1Logo(jsonObject.getString("team_1_logo"));
                                coachAssignedLeagueMatchesBean.setTeam2Logo(jsonObject.getString("team_2_logo"));
                                coachAssignedLeagueMatchesBean.setTeam1Name(jsonObject.getString("team1_name"));
                                coachAssignedLeagueMatchesBean.setTeam2Name(jsonObject.getString("team2_name"));
                                coachAssignedLeagueMatchesBean.setId(jsonObject.getString("id"));
                                coachAssignedLeagueMatchesBean.setMatchDateFormatted(jsonObject.getString("match_date_formatted"));
                                coachAssignedLeagueMatchesBean.setMatchTime(jsonObject.getString("match_time"));

                                coachAssignedLeagueMatchesBeanArrayList.add(coachAssignedLeagueMatchesBean);
                            }
                            CoachLeagueEditAdapter coachLeagueEditAdapter = new CoachLeagueEditAdapter(getActivity(), coachAssignedLeagueMatchesBeanArrayList);
                            listView.setAdapter(coachLeagueEditAdapter);

                        }else{
                            listView.setAdapter(null);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

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