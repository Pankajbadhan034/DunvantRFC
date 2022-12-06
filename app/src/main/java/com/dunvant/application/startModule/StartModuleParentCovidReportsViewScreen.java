package com.dunvant.application.startModule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
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
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.CoachAssignedLeagueMatchesBean;
import com.dunvant.application.beans.CovidViewReportBean;
import com.dunvant.application.beans.UserBean;
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

import androidx.appcompat.app.AppCompatActivity;

public class StartModuleParentCovidReportsViewScreen extends AppCompatActivity implements IWebServiceCallback {
   ArrayList<CovidViewReportBean> covidViewReportBeanArrayList = new ArrayList<>();
    String reportTypeStr;
    String nameReportStr;
    ImageView backButton;
    private final String REPORT_SERVICE = "REPORT_SERVICE";
    private final String SUBMIT_DATA = "SUBMIT_DATA";

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;


    String idHere;
    LinearLayout searchLinearLayout;
    Spinner choosePlayerSpinner;
    Spinner dateSpinner;
    Button searchButton;
    TextView title;

    private final String GET_CHILDREN_LISTING = "GET_CHILDREN_LISTING";
    private final String SCHEDULER_LIST = "SCHEDULER_LIST";
    private final String SUBMIT = "SUBMIT";

    ArrayList<CampLocationBean> locationsList = new ArrayList<>();
    ArrayList<CampLocationBean> locationsList2 = new ArrayList<>();
    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupBean> ageGroupsListing = new ArrayList<>();
    ArrayList<CoachAssignedLeagueMatchesBean> coachAssignedLeagueMatchesBeanArrayList = new ArrayList<>();

    CoachLocationListingAdapter coachLocationListingAdapter;
    CoachLocationListingAdapter coachLocationListingAdapter2;
    CoachSessionListingAdapter coachSessionListingAdapter;

    CampLocationBean clickedOnLocation;
    String verbiage_singular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_parent_covid_reports_view_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        verbiage_singular = sharedPreferences.getString("verbiage_singular", null);

        reportTypeStr = getIntent().getStringExtra("reportType");
        nameReportStr = getIntent().getStringExtra("nameReport");

        title.setText(nameReportStr);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchLinearLayout = (LinearLayout) findViewById(R.id.searchLinearLayout);
        choosePlayerSpinner = (Spinner) findViewById(R.id.choosePlayerSpinner);
        dateSpinner = (Spinner) findViewById(R.id.dateSpinner);

        choosePlayerSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        dateSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        searchButton = (Button) findViewById(R.id.searchButton);


        getChildrenListing();


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int locationPosition = choosePlayerSpinner.getSelectedItemPosition();
                int sessionPosition = dateSpinner.getSelectedItemPosition();

                if(locationPosition == 0) {
                    Toast.makeText(StartModuleParentCovidReportsViewScreen.this, "Please choose Player", Toast.LENGTH_SHORT).show();
                } else if (sessionPosition == 0) {
                    Toast.makeText(StartModuleParentCovidReportsViewScreen.this, "Please choose date", Toast.LENGTH_SHORT).show();
                } else {
                    submitData();
                }
            }
        });


        choosePlayerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0) {
                    daysListing.clear();

                    // 0th Element Choose Session
                    CampDaysBean daysBean = new CampDaysBean();
                    daysBean.setDay("-1");
                    daysBean.setDayLabel("Choose Date");

                    daysListing.add(daysBean);

                    coachSessionListingAdapter = new CoachSessionListingAdapter(StartModuleParentCovidReportsViewScreen.this, daysListing);
                    dateSpinner.setAdapter(coachSessionListingAdapter);
                } else {
                    clickedOnLocation = locationsList.get(position);
                    reportAPI();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    ageGroupsListing.clear();

                    // 0th Element Choose Session
                    AgeGroupBean ageGroupBean = new AgeGroupBean();
                    ageGroupBean.setAgeGroupId("-1");
                    ageGroupBean.setGroupName("Choose Date");

                    ageGroupsListing.add(ageGroupBean);

                } else {
                    idHere = daysListing.get(position).getCampId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    private void getChildrenListing(){
        if(Utilities.isNetworkAvailable(StartModuleParentCovidReportsViewScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "children/children_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(StartModuleParentCovidReportsViewScreen.this, GET_CHILDREN_LISTING, StartModuleParentCovidReportsViewScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleParentCovidReportsViewScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void reportAPI() {
        if (Utilities.isNetworkAvailable(StartModuleParentCovidReportsViewScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));
            nameValuePairList.add(new BasicNameValuePair("user_id", clickedOnLocation.getLocationId()));
            nameValuePairList.add(new BasicNameValuePair("report_type", reportTypeStr));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/covid_wellness_report_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(StartModuleParentCovidReportsViewScreen.this, nameValuePairList, REPORT_SERVICE, StartModuleParentCovidReportsViewScreen.this,headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleParentCovidReportsViewScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void submitData() {
        if (Utilities.isNetworkAvailable(StartModuleParentCovidReportsViewScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));
            nameValuePairList.add(new BasicNameValuePair("user_id", clickedOnLocation.getLocationId()));
            nameValuePairList.add(new BasicNameValuePair("report_type", reportTypeStr));
            nameValuePairList.add(new BasicNameValuePair("created", idHere));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/get_report";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(StartModuleParentCovidReportsViewScreen.this, nameValuePairList, SUBMIT_DATA, StartModuleParentCovidReportsViewScreen.this,headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleParentCovidReportsViewScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
//            case REPORT_SERVICE:
//
//                System.out.println("RES::" + response);
//
//                if (response == null) {
//                    Toast.makeText(StartModuleParentCovidReportsViewScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
//                } else {
//                    try {
//                        JSONObject responseObject = new JSONObject(response);
//
//                        boolean status = responseObject.getBoolean("status");
//                        String message = responseObject.getString("message");
//                        if (status) {
//                            JSONArray dataArray = responseObject.getJSONArray("data");
//
//                            // 0th Element Choose Location
//                            CampLocationBean locationBean = new CampLocationBean();
//                            locationBean.setLocationId("-1");
//                            locationBean.setLocationName("Choose Date1");
//
//                            locationsList2.add(locationBean);
//
//
//                            for (int i=0; i<dataArray.length(); i++) {
//                                JSONObject locationObject = dataArray.getJSONObject(i);
//                                locationBean = new CampLocationBean();
//                                locationBean.setLocationId(locationObject.getString("user_id"));
//                                locationBean.setLocationName(locationObject.getString("format_date"));
//                                locationBean.setDescription(locationObject.getString("created"));
//                                locationBean.setLongitude(locationObject.getString("report_type"));
//                                locationBean.setLatitude(locationObject.getString("username"));
//                                locationsList2.add(locationBean);
//                            }
//                        } else {
//                            CampLocationBean locationBean = new CampLocationBean();
//                            locationBean.setLocationId("-1");
//                            locationBean.setLocationName("Please select Date2");
//                            locationsList2.add(locationBean);
//                            Toast.makeText(StartModuleParentCovidReportsViewScreen.this, message, Toast.LENGTH_SHORT).show();
//                        }
//
//                        coachLocationListingAdapter2 = new CoachLocationListingAdapter(StartModuleParentCovidReportsViewScreen.this, locationsList2);
//                        choosePlayerSpinner.setAdapter(coachLocationListingAdapter2);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(StartModuleParentCovidReportsViewScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                break;

            case REPORT_SERVICE:

                daysListing.clear();

                if(response == null) {
                    Toast.makeText(StartModuleParentCovidReportsViewScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            // 0th Element Choose Session
                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Choose Date");

                            daysListing.add(daysBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject daysObject = dataArray.getJSONObject(i);
                                daysBean = new CampDaysBean();
                               // daysBean.setCampId(daysObject.getString("user_id"));
                                daysBean.setDayLabel(daysObject.getString("format_date"));
                                daysBean.setCampId(daysObject.getString("created"));
                               // daysBean.setLongitude(daysObject.getString("report_type"));
                                daysBean.setDay(daysObject.getString("username"));

//                                daysBean.setDay(daysObject.getString("id"));
//                                daysBean.setDayLabel(daysObject.getString("title"));

                                daysListing.add(daysBean);
                            }

                        } else {

                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Choose Date");
                            daysListing.add(daysBean);

                            Toast.makeText(StartModuleParentCovidReportsViewScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleParentCovidReportsViewScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachSessionListingAdapter = new CoachSessionListingAdapter(StartModuleParentCovidReportsViewScreen.this, daysListing);
                dateSpinner.setAdapter(coachSessionListingAdapter);

                break;

            case GET_CHILDREN_LISTING:

                locationsList.clear();

                if (response == null) {
                    Toast.makeText(StartModuleParentCovidReportsViewScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");

                            // 0th Element Choose Location
                            CampLocationBean locationBean = new CampLocationBean();
                            locationBean.setLocationId("-1");
                            locationBean.setLocationName("Choose "+verbiage_singular);

                            locationsList.add(locationBean);


                            for (int i=0; i<dataArray.length(); i++) {
                                JSONObject locationObject = dataArray.getJSONObject(i);
                                locationBean = new CampLocationBean();
                                locationBean.setLocationId(locationObject.getString("id"));
                                locationBean.setLocationName(locationObject.getString("full_name"));
                                locationsList.add(locationBean);
                            }

                        } else {
                            Toast.makeText(StartModuleParentCovidReportsViewScreen.this, message, Toast.LENGTH_SHORT).show();

                            CampLocationBean locationBean = new CampLocationBean();
                            locationBean.setLocationId("-1");
                            locationBean.setLocationName("Please select "+verbiage_singular);
                            locationsList.add(locationBean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleParentCovidReportsViewScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                coachLocationListingAdapter = new CoachLocationListingAdapter(StartModuleParentCovidReportsViewScreen.this, locationsList);
                choosePlayerSpinner.setAdapter(coachLocationListingAdapter);
                break;

            case SUBMIT_DATA:

                if (response == null) {
                    Toast.makeText(StartModuleParentCovidReportsViewScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        covidViewReportBeanArrayList.clear();
                        if (status) {
                            if(reportTypeStr.equalsIgnoreCase("1")){
                                JSONArray jsonArray = responseObject.getJSONArray("data");
                                for(int i=0; i<jsonArray.length(); i++){
                                    CovidViewReportBean covidViewReportBean = new CovidViewReportBean();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    covidViewReportBean.setAnswer(jsonObject.getString("answer"));
                                    covidViewReportBean.setQuestion(jsonObject.getString("question"));
                                    covidViewReportBeanArrayList.add(covidViewReportBean);
                                }

                                Intent obj = new Intent(StartModuleParentCovidReportsViewScreen.this, StartModuleCovidWellnessViewReportScreen.class);
                                obj.putExtra("covidViewReportBeanArrayList", covidViewReportBeanArrayList);
                                obj.putExtra("title", "COVID WELLNESS REPORT");
                                startActivity(obj);
                                finish();


                            }else if(reportTypeStr.equalsIgnoreCase("2")){
                                JSONArray jsonArray = responseObject.getJSONArray("data");
                                for(int i=0; i<jsonArray.length(); i++){
                                    CovidViewReportBean covidViewReportBean = new CovidViewReportBean();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    covidViewReportBean.setAnswer(jsonObject.getString("answer"));
                                    covidViewReportBean.setQuestion(jsonObject.getString("question"));
                                    covidViewReportBeanArrayList.add(covidViewReportBean);
                                }

                                Intent obj = new Intent(StartModuleParentCovidReportsViewScreen.this, StartModuleCovidWellnessViewReportScreen.class);
                                obj.putExtra("covidViewReportBeanArrayList", covidViewReportBeanArrayList);
                                obj.putExtra("title", "DAILY MONITORING REPORT");
                                startActivity(obj);
                                finish();
                            }

                        } else {
                            Toast.makeText(StartModuleParentCovidReportsViewScreen.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleParentCovidReportsViewScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                coachLocationListingAdapter = new CoachLocationListingAdapter(StartModuleParentCovidReportsViewScreen.this, locationsList);
                choosePlayerSpinner.setAdapter(coachLocationListingAdapter);
                break;

        }
    }
}