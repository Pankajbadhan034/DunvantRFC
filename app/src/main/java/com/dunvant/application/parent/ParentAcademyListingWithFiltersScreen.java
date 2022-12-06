package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AcademyLocationBean;
import com.dunvant.application.beans.AgeGroupFilterBean;
import com.dunvant.application.beans.CoachingAcademyBean;
import com.dunvant.application.beans.CoachingProgramGalleryBean;
import com.dunvant.application.beans.DayFilterBean;
import com.dunvant.application.beans.TermBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentAcademyGridAdapter;
import com.dunvant.application.parent.adapters.ParentAgeGroupFilterSpinnerAdapter;
import com.dunvant.application.parent.adapters.ParentDayFilterSpinnerAdapter;
import com.dunvant.application.parent.adapters.ParentLocationFilterSpinnerAdapter;
import com.dunvant.application.parent.adapters.ParentProgramFilterSpinnerAdapter;
import com.dunvant.application.parent.adapters.ParentTermFilterSpinnerAdapter;
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

public class ParentAcademyListingWithFiltersScreen extends AppCompatActivity implements IWebServiceCallback {
    private final String GET_LOCATION_LISTING = "GET_LOCATION_LISTING";
    private final String GET_COACHING_PROGRAM_LISTING = "GET_COACHING_PROGRAM_LISTING";
    private final String GET_AGE_LISTING = "GET_AGE_LISTING";
    private final String GET_TERM_LISTING = "GET_TERM_LISTING";
    private final String GET_DAY_LISTING = "GET_DAY_LISTING";

    String locationId="";
    String programId="";
    String termId="";
    String ageId="";
    String dayId="";
    TextView locationText;
    TextView selectProgramText;
    TextView selectAgeGroupText;
    TextView selectTermText;
    TextView selectDayText;
    Spinner locationSpinner;
    Spinner programSpinner;
    Spinner ageGroupSpinner;
    Spinner termSpinner;
    Spinner daySpinner;
    Button goButton;
    ArrayList<AcademyLocationBean> academyLocationBeanArrayList = new ArrayList<>();
    ArrayList<AgeGroupFilterBean> ageGroupFilterBeanArrayList = new ArrayList<>();
    ArrayList<TermBean> termFilterBeanArrayList = new ArrayList<>();
    ArrayList<DayFilterBean> dayFilterBeanArrayList = new ArrayList<>();

    ParentLocationFilterSpinnerAdapter parentLocationFilterSpinnerAdapter;
    ParentProgramFilterSpinnerAdapter parentProgramFilterSpinnerAdapter;
    ParentAgeGroupFilterSpinnerAdapter parentAgeGroupFilterSpinnerAdapter;
    ParentTermFilterSpinnerAdapter parentTermFilterSpinnerAdapter;
    ParentDayFilterSpinnerAdapter parentDayFilterSpinnerAdapter;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Typeface helvetica;
    Typeface linoType;

    TextView title;
    ImageView backButton;
    // ListView academiesListView;
    private final String GET_ACADEMIES_LISTING = "GET_ACADEMIES_LISTING";

    ArrayList<CoachingAcademyBean> academiesList = new ArrayList<>();

    ParentAcademyGridAdapter parentAcademyGridAdapter;

    public static boolean comingFromBookMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_academy_listing_with_filters_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        parentAcademyGridAdapter = new ParentAcademyGridAdapter(ParentAcademyListingWithFiltersScreen.this, academiesList);

        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);
        locationSpinner = findViewById(R.id.locationSpinner);
        programSpinner = findViewById(R.id.programSpinner);
        ageGroupSpinner = findViewById(R.id.ageGroupSpinner);
        termSpinner = findViewById(R.id.termSpinner);
        daySpinner = findViewById(R.id.daySpinner);
        goButton = findViewById(R.id.goButton);
        locationText = findViewById(R.id.locationText);
        selectProgramText = findViewById(R.id.selectProgramText);
        selectAgeGroupText = findViewById(R.id.selectAgeGroupText);
        selectTermText = findViewById(R.id.selectTermText);
        selectDayText = findViewById(R.id.selectDayText);

        locationSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        programSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ageGroupSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        termSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        daySpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        //academiesListView = (ListView) findViewById(R.id.academiesListView);
        // academiesListView.setAdapter(parentAcademyGridAdapter);

        title.setTypeface(linoType);
        locationText.setTypeface(linoType);
        selectProgramText.setTypeface(linoType);
        selectAgeGroupText.setTypeface(linoType);
        selectTermText.setTypeface(linoType);
        selectDayText.setTypeface(linoType);


        getLocatoinListAPI();
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                programId="";
                ageId="";
                termId="";
                dayId="";

                academiesList.clear();
                ageGroupFilterBeanArrayList.clear();
                termFilterBeanArrayList.clear();
                dayFilterBeanArrayList.clear();

                CoachingAcademyBean academyBean = new CoachingAcademyBean();
                academyBean.setAcademyId("-1");
                academyBean.setCoachingProgramName("Select Program");
                academyBean.setDescription("-1");
                academyBean.setIsTrial("-1");
                academyBean.setFileTitle("-1");
                academyBean.setFilePath("-1");
                ArrayList<CoachingProgramGalleryBean> galleryListEmpt = new ArrayList<>();
                academyBean.setGalleryList(galleryListEmpt);
                academiesList.add(academyBean);

                AgeGroupFilterBean ageGroupFilterBean = new AgeGroupFilterBean();
                ageGroupFilterBean.setAgeGroupId("-1");
                ageGroupFilterBean.setAcademiesId("-1");
                ageGroupFilterBean.setGroupName("Select Age");
                ageGroupFilterBean.setFromAge("-1");
                ageGroupFilterBean.setToAge("-1");
                ageGroupFilterBean.setAgeGroupType("-1");
                ageGroupFilterBean.setCreatedAt("-1");
                ageGroupFilterBean.setState("-1");
                ageGroupFilterBeanArrayList.add(ageGroupFilterBean);

                TermBean termBean = new TermBean();
                termBean.setTermId("-1");
                termBean.setTermName("Select Term");
                termBean.setFromDate("-1");
                termBean.setToDate("-1");
                termBean.setShowFromDate("-1");
                termBean.setShowToDate("-1");
                termFilterBeanArrayList.add(termBean);

                DayFilterBean dayFilterBean = new DayFilterBean();
                dayFilterBean.setDay("Select Day");
                dayFilterBean.setDayId("-1");
                dayFilterBeanArrayList.add(dayFilterBean);

                parentProgramFilterSpinnerAdapter = new ParentProgramFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, academiesList);
                programSpinner.setAdapter(parentProgramFilterSpinnerAdapter);

                parentAgeGroupFilterSpinnerAdapter = new ParentAgeGroupFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, ageGroupFilterBeanArrayList);
                ageGroupSpinner.setAdapter(parentAgeGroupFilterSpinnerAdapter);

                parentTermFilterSpinnerAdapter = new ParentTermFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, termFilterBeanArrayList);
                termSpinner.setAdapter(parentTermFilterSpinnerAdapter);

                parentDayFilterSpinnerAdapter = new ParentDayFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, dayFilterBeanArrayList);
                daySpinner.setAdapter(parentDayFilterSpinnerAdapter);

                if (position == 0) {
                    locationId="";
                }else{
                    locationId = academyLocationBeanArrayList.get(position).getLocationId();
                    getCoachingProgramListingAPI();
                }

                //      Toast.makeText(ParentAcademyListingWithFiltersScreen.this, locationId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        programSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ageId="";
                termId="";
                dayId="";

                ageGroupFilterBeanArrayList.clear();
                termFilterBeanArrayList.clear();
                dayFilterBeanArrayList.clear();

                AgeGroupFilterBean ageGroupFilterBean = new AgeGroupFilterBean();
                ageGroupFilterBean.setAgeGroupId("-1");
                ageGroupFilterBean.setAcademiesId("-1");
                ageGroupFilterBean.setGroupName("Select Age");
                ageGroupFilterBean.setFromAge("-1");
                ageGroupFilterBean.setToAge("-1");
                ageGroupFilterBean.setAgeGroupType("-1");
                ageGroupFilterBean.setCreatedAt("-1");
                ageGroupFilterBean.setState("-1");
                ageGroupFilterBeanArrayList.add(ageGroupFilterBean);

                TermBean termBean = new TermBean();
                termBean.setTermId("-1");
                termBean.setTermName("Select Term");
                termBean.setFromDate("-1");
                termBean.setToDate("-1");
                termBean.setShowFromDate("-1");
                termBean.setShowToDate("-1");
                termFilterBeanArrayList.add(termBean);

                DayFilterBean dayFilterBean = new DayFilterBean();
                dayFilterBean.setDay("Select Day");
                dayFilterBean.setDayId("-1");
                dayFilterBeanArrayList.add(dayFilterBean);

                parentAgeGroupFilterSpinnerAdapter = new ParentAgeGroupFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, ageGroupFilterBeanArrayList);
                ageGroupSpinner.setAdapter(parentAgeGroupFilterSpinnerAdapter);

                parentTermFilterSpinnerAdapter = new ParentTermFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, termFilterBeanArrayList);
                termSpinner.setAdapter(parentTermFilterSpinnerAdapter);

                parentDayFilterSpinnerAdapter = new ParentDayFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, dayFilterBeanArrayList);
                daySpinner.setAdapter(parentDayFilterSpinnerAdapter);

                if (position == 0) {
                    programId="";
                }else{
                    programId = academiesList.get(position).getAcademyId();
                    getAgeListingAPI();
                }

                //       Toast.makeText(ParentAcademyListingWithFiltersScreen.this, programId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ageGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                termId="";
                dayId="";

                termFilterBeanArrayList.clear();
                dayFilterBeanArrayList.clear();

                TermBean termBean = new TermBean();
                termBean.setTermId("-1");
                termBean.setTermName("Select Term");
                termBean.setFromDate("-1");
                termBean.setToDate("-1");
                termBean.setShowFromDate("-1");
                termBean.setShowToDate("-1");
                termFilterBeanArrayList.add(termBean);

                DayFilterBean dayFilterBean = new DayFilterBean();
                dayFilterBean.setDay("Select Day");
                dayFilterBean.setDayId("-1");
                dayFilterBeanArrayList.add(dayFilterBean);

                parentTermFilterSpinnerAdapter = new ParentTermFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, termFilterBeanArrayList);
                termSpinner.setAdapter(parentTermFilterSpinnerAdapter);

                parentDayFilterSpinnerAdapter = new ParentDayFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, dayFilterBeanArrayList);
                daySpinner.setAdapter(parentDayFilterSpinnerAdapter);

                if (position == 0) {
                    ageId="";
                }else{
                    ageId = ageGroupFilterBeanArrayList.get(position).getAgeGroupId();
                    getTermListingAPI();
                }

                //       Toast.makeText(ParentAcademyListingWithFiltersScreen.this, ageId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//
        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                dayId="";

                dayFilterBeanArrayList.clear();

                DayFilterBean dayFilterBean = new DayFilterBean();
                dayFilterBean.setDay("Select Day");
                dayFilterBean.setDayId("-1");
                dayFilterBeanArrayList.add(dayFilterBean);

                parentDayFilterSpinnerAdapter = new ParentDayFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, dayFilterBeanArrayList);
                daySpinner.setAdapter(parentDayFilterSpinnerAdapter);

                if (position == 0) {
                    termId="";
                }else{
                    termId = termFilterBeanArrayList.get(position).getTermId();
                    getDayAPI();
                }

                //       Toast.makeText(ParentAcademyListingWithFiltersScreen.this, termId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    dayId="";
                }else{
                    dayId = dayFilterBeanArrayList.get(position).getDayId();
                }

                //     Toast.makeText(ParentAcademyListingWithFiltersScreen.this, dayId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        getAcademiesListing();
//        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    locationId="";
//                }else{
//                    locationId = academyLocationBeanArrayList.get(position).getLocationId();
//                }
//
//                //      Toast.makeText(ParentAcademyListingWithFiltersScreen.this, locationId, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        programSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    programId="";
//                }else{
//                    programId = academiesList.get(position).getAcademyId();
//                }
//
//                //       Toast.makeText(ParentAcademyListingWithFiltersScreen.this, programId, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        ageGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    ageId="";
//                }else{
//                    ageId = ageGroupFilterBeanArrayList.get(position).getAgeGroupId();
//                }
//
//                //       Toast.makeText(ParentAcademyListingWithFiltersScreen.this, ageId, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
////
//        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    termId="";
//                }else{
//                    termId = termFilterBeanArrayList.get(position).getTermId();
//                }
//
//                //       Toast.makeText(ParentAcademyListingWithFiltersScreen.this, termId, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    dayId="";
//                }else{
//                    dayId = dayFilterBeanArrayList.get(position).getDayId();
//                }
//
//                //     Toast.makeText(ParentAcademyListingWithFiltersScreen.this, dayId, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(ParentAcademyListingWithFiltersScreen.this, ParentBookAcademyThreeFilter.class);
                obj.putExtra("dayId", dayId);
                obj.putExtra("termId", termId);
                obj.putExtra("locationId", locationId);
                obj.putExtra("ageId", ageId);
                obj.putExtra("programId", programId);
                startActivity(obj);

                System.out.println("dayId--"+dayId+"--termId--"+termId+"--locationId--"+locationId+"--ageId--"+ageId+"--programId--"+programId);

            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// changed by me
//                Intent screenOne = new Intent(ParentAcademyListingWithFiltersScreen.this, StartModuleDashboardScreen.class);
//                screenOne.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(screenOne);

                finish();
            }
        });
    }
    // changed by me
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
//        Intent screenOne = new Intent(ParentAcademyListingWithFiltersScreen.this, StartModuleDashboardScreen.class);
//        screenOne.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(screenOne);
    }

    private void getAcademiesListing(){
        if(Utilities.isNetworkAvailable(ParentAcademyListingWithFiltersScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("offset", "0"));

//            String webServiceUrl = Utilities.BASE_URL + "coaching_programs/list";
            String webServiceUrl = Utilities.BASE_URL + "coaching_programs/get_filters_data";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentAcademyListingWithFiltersScreen.this, nameValuePairList, GET_ACADEMIES_LISTING, ParentAcademyListingWithFiltersScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentAcademyListingWithFiltersScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocatoinListAPI(){
        if(Utilities.isNetworkAvailable(ParentAcademyListingWithFiltersScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "sessions/get_filters_location_data";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ParentAcademyListingWithFiltersScreen.this, GET_LOCATION_LISTING, ParentAcademyListingWithFiltersScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentAcademyListingWithFiltersScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getCoachingProgramListingAPI(){
        if(Utilities.isNetworkAvailable(ParentAcademyListingWithFiltersScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_id", locationId));

//            String webServiceUrl = Utilities.BASE_URL + "coaching_programs/list";
            String webServiceUrl = Utilities.BASE_URL + "sessions/get_filter_coaching_program_data";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentAcademyListingWithFiltersScreen.this, nameValuePairList, GET_COACHING_PROGRAM_LISTING, ParentAcademyListingWithFiltersScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentAcademyListingWithFiltersScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAgeListingAPI(){
        if(Utilities.isNetworkAvailable(ParentAcademyListingWithFiltersScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_id", locationId));
            nameValuePairList.add(new BasicNameValuePair("coaching_program_id", programId));

//            String webServiceUrl = Utilities.BASE_URL + "coaching_programs/list";
            String webServiceUrl = Utilities.BASE_URL + "sessions/get_filter_age_group_data";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentAcademyListingWithFiltersScreen.this, nameValuePairList, GET_AGE_LISTING, ParentAcademyListingWithFiltersScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentAcademyListingWithFiltersScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getTermListingAPI(){
        if(Utilities.isNetworkAvailable(ParentAcademyListingWithFiltersScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_id", locationId));
            nameValuePairList.add(new BasicNameValuePair("coaching_program_id", programId));
            nameValuePairList.add(new BasicNameValuePair("age_group_id", ageId));

//            String webServiceUrl = Utilities.BASE_URL + "coaching_programs/list";
            String webServiceUrl = Utilities.BASE_URL + "sessions/get_filter_term_data";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentAcademyListingWithFiltersScreen.this, nameValuePairList, GET_TERM_LISTING, ParentAcademyListingWithFiltersScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentAcademyListingWithFiltersScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getDayAPI(){
        if(Utilities.isNetworkAvailable(ParentAcademyListingWithFiltersScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_id", locationId));
            nameValuePairList.add(new BasicNameValuePair("coaching_program_id", programId));
            nameValuePairList.add(new BasicNameValuePair("age_group_id", ageId));
            nameValuePairList.add(new BasicNameValuePair("terms_id", termId));

//            String webServiceUrl = Utilities.BASE_URL + "coaching_programs/list";
            String webServiceUrl = Utilities.BASE_URL + "sessions/get_filter_day_data";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentAcademyListingWithFiltersScreen.this, nameValuePairList, GET_DAY_LISTING, ParentAcademyListingWithFiltersScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentAcademyListingWithFiltersScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_LOCATION_LISTING:
                academyLocationBeanArrayList.clear();

                if(response == null) {
                    Toast.makeText(ParentAcademyListingWithFiltersScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONObject coachingList = responseObject.getJSONObject("data");

                            // location list
                            JSONArray locationList = coachingList.getJSONArray("location_list");
                            AcademyLocationBean locationBean;

                            // only for show the default label name in first value
                            locationBean = new AcademyLocationBean();
                            locationBean.setLocationId("-1");
                            locationBean.setLocationName("Select Location");
                            locationBean.setDescription("-1");
                            locationBean.setLatitude(-1);
                            locationBean.setLongitude(-1);
                            academyLocationBeanArrayList.add(locationBean);

                            for(int i=0;i<locationList.length();i++) {
                                JSONObject locationObject = locationList.getJSONObject(i);
                                locationBean = new AcademyLocationBean();

                                locationBean.setLocationId(locationObject.getString("id"));
                                locationBean.setLocationName(locationObject.getString("name"));
                                locationBean.setDescription(locationObject.getString("pitch_description"));
                                locationBean.setLatitude(locationObject.getDouble("latitude"));
                                locationBean.setLongitude(locationObject.getDouble("longitude"));
//                                    locationBean.setCoachingProgramIds(locationObject.getString("coaching_program_ids"));
//                                    locationBean.setCoachingProgramNames(locationObject.getString("coaching_program_names"));

                                academyLocationBeanArrayList.add(locationBean);
                            }
                            parentLocationFilterSpinnerAdapter = new ParentLocationFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, academyLocationBeanArrayList);
                            locationSpinner.setAdapter(parentLocationFilterSpinnerAdapter);

                        } else {
                            Toast.makeText(ParentAcademyListingWithFiltersScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentAcademyListingWithFiltersScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                comingFromBookMore = false;
                //  parentAcademyGridAdapter.notifyDataSetChanged();

                break;

            case GET_COACHING_PROGRAM_LISTING:

                if(response == null) {
                    Toast.makeText(ParentAcademyListingWithFiltersScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONObject coachingList = responseObject.getJSONObject("data");

                            JSONArray programListArray = coachingList.getJSONArray("coaching_program_list");
                            CoachingAcademyBean academyBean;

//                            // only for show the default label name in first value
//                            academyBean = new CoachingAcademyBean();
//                            academyBean.setAcademyId("-1");
//                            academyBean.setCoachingProgramName("Select Program");
//                            academyBean.setDescription("-1");
//                            academyBean.setIsTrial("-1");
//                            academyBean.setFileTitle("-1");
//                            academyBean.setFilePath("-1");
//                            ArrayList<CoachingProgramGalleryBean> galleryListEmpt = new ArrayList<>();
//                            academyBean.setGalleryList(galleryListEmpt);
//                            academiesList.add(academyBean);

                            for (int i=0; i<programListArray.length(); i++) {
                                JSONObject academyObject = programListArray.getJSONObject(i);
                                academyBean = new CoachingAcademyBean();

                                academyBean.setAcademyId(academyObject.getString("id"));
                                academyBean.setCoachingProgramName(academyObject.getString("coaching_program_name"));
                                academyBean.setDescription(academyObject.getString("description"));
                                academyBean.setIsTrial(academyObject.getString("is_trial"));
                                academyBean.setFileTitle(academyObject.getString("file_title"));
                                academyBean.setFilePath(academyObject.getString("file_path"));

                                JSONArray galleryArray = academyObject.getJSONArray("coaching_program_gallery");
                                ArrayList<CoachingProgramGalleryBean> galleryList = new ArrayList<>();
                                CoachingProgramGalleryBean galleryBean;
                                for (int j=0; j<galleryArray.length(); j++) {
                                    JSONObject galleryObject = galleryArray.getJSONObject(j);
                                    galleryBean = new CoachingProgramGalleryBean();

                                    galleryBean.setFileTitle(galleryObject.getString("file_title"));
                                    galleryBean.setFileType(galleryObject.getString("file_type"));
                                    galleryBean.setFilePath(galleryObject.getString("file_path"));

                                    if(!(galleryBean.getFileType().contains("video") || galleryBean.getFileType().contains("youtube"))){
                                        galleryList.add(galleryBean);
                                    }


                                }
                                academyBean.setGalleryList(galleryList);

                                academiesList.add(academyBean);
                            }

                            parentProgramFilterSpinnerAdapter = new ParentProgramFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, academiesList);
                            programSpinner.setAdapter(parentProgramFilterSpinnerAdapter);

                        } else {
                            Toast.makeText(ParentAcademyListingWithFiltersScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentAcademyListingWithFiltersScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case GET_AGE_LISTING:

                if(response == null) {
                    Toast.makeText(ParentAcademyListingWithFiltersScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONObject coachingList = responseObject.getJSONObject("data");

                            JSONArray ageGroupList = coachingList.getJSONArray("age_group_list");
                            AgeGroupFilterBean ageGroupFilterBean;

//                            // only for show the default label name in first value
//                            ageGroupFilterBean = new AgeGroupFilterBean();
//                            ageGroupFilterBean.setAgeGroupId("-1");
//                            ageGroupFilterBean.setAcademiesId("-1");
//                            ageGroupFilterBean.setGroupName("Select Age");
//                            ageGroupFilterBean.setFromAge("-1");
//                            ageGroupFilterBean.setToAge("-1");
//                            ageGroupFilterBean.setAgeGroupType("-1");
//                            ageGroupFilterBean.setCreatedAt("-1");
//                            ageGroupFilterBean.setState("-1");
//                            ageGroupFilterBeanArrayList.add(ageGroupFilterBean);

                            for (int i=0;i<ageGroupList.length();i++) {
                                JSONObject ageObject = ageGroupList.getJSONObject(i);
                                ageGroupFilterBean = new AgeGroupFilterBean();

                                ageGroupFilterBean.setAgeGroupId(ageObject.getString("id"));
                                ageGroupFilterBean.setAcademiesId(ageObject.getString("academies_id"));
                                ageGroupFilterBean.setGroupName(ageObject.getString("group_name"));
                                ageGroupFilterBean.setFromAge(ageObject.getString("from_age"));
                                ageGroupFilterBean.setToAge(ageObject.getString("to_age"));
                                ageGroupFilterBean.setAgeGroupType(ageObject.getString("age_group_type"));
                                ageGroupFilterBean.setCreatedAt(ageObject.getString("created_at"));
                                ageGroupFilterBean.setState(ageObject.getString("state"));

                                ageGroupFilterBeanArrayList.add(ageGroupFilterBean);
                            }
                            parentAgeGroupFilterSpinnerAdapter = new ParentAgeGroupFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, ageGroupFilterBeanArrayList);
                            ageGroupSpinner.setAdapter(parentAgeGroupFilterSpinnerAdapter);

                        } else {
                            Toast.makeText(ParentAcademyListingWithFiltersScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentAcademyListingWithFiltersScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case GET_TERM_LISTING:

                if(response == null) {
                    Toast.makeText(ParentAcademyListingWithFiltersScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONObject coachingList = responseObject.getJSONObject("data");

                            JSONArray termList = coachingList.getJSONArray("term_list");
                            TermBean termBean;

//                            // only for show the default label name in first value
//                            termBean = new TermBean();
//                            termBean.setTermId("-1");
//                            termBean.setTermName("Select Term");
//                            termBean.setFromDate("-1");
//                            termBean.setToDate("-1");
//                            termBean.setShowFromDate("-1");
//                            termBean.setShowToDate("-1");
//                            termFilterBeanArrayList.add(termBean);

                            for (int i=0;i<termList.length();i++) {
                                JSONObject termObject = termList.getJSONObject(i);
                                termBean = new TermBean();

                                termBean.setTermId(termObject.getString("id"));
                                termBean.setTermName(termObject.getString("term_name"));
                                termBean.setFromDate(termObject.getString("from_date"));
                                termBean.setToDate(termObject.getString("to_date"));
                                termBean.setShowFromDate(termObject.getString("from_date_formatted"));
                                termBean.setShowToDate(termObject.getString("to_date_formatted"));

                                termFilterBeanArrayList.add(termBean);
                            }
                            parentTermFilterSpinnerAdapter = new ParentTermFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, termFilterBeanArrayList);
                            termSpinner.setAdapter(parentTermFilterSpinnerAdapter);

                        } else {
                            Toast.makeText(ParentAcademyListingWithFiltersScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentAcademyListingWithFiltersScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case GET_DAY_LISTING:

                if(response == null) {
                    Toast.makeText(ParentAcademyListingWithFiltersScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONObject coachingList = responseObject.getJSONObject("data");

                            JSONArray dayFilterArray = coachingList.getJSONArray("day_filter");
                            DayFilterBean dayFilterBean;

//                            // only for show the default label name in first value
//                            dayFilterBean = new DayFilterBean();
//                            dayFilterBean.setDay("Select Day");
//                            dayFilterBean.setDayId("-1");
//                            dayFilterBeanArrayList.add(dayFilterBean);

                            for (int i=0;i<dayFilterArray.length();i++) {
                                JSONObject dayFilterObject = dayFilterArray.getJSONObject(i);
                                dayFilterBean = new DayFilterBean();

                                dayFilterBean.setDay(dayFilterObject.getString("day"));
                                dayFilterBean.setDayId(dayFilterObject.getString("id"));


                                dayFilterBeanArrayList.add(dayFilterBean);
                            }
                            parentDayFilterSpinnerAdapter = new ParentDayFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, dayFilterBeanArrayList);
                            daySpinner.setAdapter(parentDayFilterSpinnerAdapter);

                        } else {
                            Toast.makeText(ParentAcademyListingWithFiltersScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentAcademyListingWithFiltersScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case GET_ACADEMIES_LISTING:

                academyLocationBeanArrayList.clear();
                academiesList.clear();
                ageGroupFilterBeanArrayList.clear();
                termFilterBeanArrayList.clear();

                if(response == null) {
                    Toast.makeText(ParentAcademyListingWithFiltersScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        System.out.println("RespnseHere2::"+response);
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONObject coachingList = responseObject.getJSONObject("data");

                            JSONObject serachFilters = coachingList.getJSONObject("search_filter_data");

                            String locationFilter = serachFilters.getString("location_filter");
                            String programFilter = serachFilters.getString("program_filter");
                            String ageGroupFilter = serachFilters.getString("age_group_filter");
                            String termFilter = serachFilters.getString("term_filter");
                            String dayFilter = serachFilters.getString("day_filter");

                            // location filter condition
                            if (locationFilter.trim().equalsIgnoreCase("1")){
                                // location list
                                JSONArray locationList = coachingList.getJSONArray("location_list");
                                AcademyLocationBean locationBean;

                                // only for show the default label name in first value
                                locationBean = new AcademyLocationBean();
                                locationBean.setLocationId("-1");
                                locationBean.setLocationName("Select Location");
                                locationBean.setDescription("-1");
                                locationBean.setLatitude(-1);
                                locationBean.setLongitude(-1);
                                academyLocationBeanArrayList.add(locationBean);

                                for(int i=0;i<locationList.length();i++) {
                                    JSONObject locationObject = locationList.getJSONObject(i);
                                    locationBean = new AcademyLocationBean();

                                    locationBean.setLocationId(locationObject.getString("id"));
                                    locationBean.setLocationName(locationObject.getString("name"));
                                    locationBean.setDescription(locationObject.getString("pitch_description"));
                                    locationBean.setLatitude(locationObject.getDouble("latitude"));
                                    locationBean.setLongitude(locationObject.getDouble("longitude"));
//                                    locationBean.setCoachingProgramIds(locationObject.getString("coaching_program_ids"));
//                                    locationBean.setCoachingProgramNames(locationObject.getString("coaching_program_names"));

                                    academyLocationBeanArrayList.add(locationBean);
                                }
                                parentLocationFilterSpinnerAdapter = new ParentLocationFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, academyLocationBeanArrayList);
                                locationSpinner.setAdapter(parentLocationFilterSpinnerAdapter);

                            }else{
                                locationText.setVisibility(View.GONE);
                                locationSpinner.setVisibility(View.GONE);
                            }

                            // program filter condition
                            if (programFilter.trim().equalsIgnoreCase("1")){
                                // coaching program
                                JSONArray programListArray = coachingList.getJSONArray("coaching_program_list");
                                CoachingAcademyBean academyBean;

                                // only for show the default label name in first value
                                academyBean = new CoachingAcademyBean();
                                academyBean.setAcademyId("-1");
                                academyBean.setCoachingProgramName("Select Program");
                                academyBean.setDescription("-1");
                                academyBean.setIsTrial("-1");
                                academyBean.setFileTitle("-1");
                                academyBean.setFilePath("-1");
                                ArrayList<CoachingProgramGalleryBean> galleryListEmpt = new ArrayList<>();
                                academyBean.setGalleryList(galleryListEmpt);
                                academiesList.add(academyBean);

                                for (int i=0; i<programListArray.length(); i++) {
                                    JSONObject academyObject = programListArray.getJSONObject(i);
                                    academyBean = new CoachingAcademyBean();

                                    academyBean.setAcademyId(academyObject.getString("id"));
                                    academyBean.setCoachingProgramName(academyObject.getString("coaching_program_name"));
                                    academyBean.setDescription(academyObject.getString("description"));
                                    academyBean.setIsTrial(academyObject.getString("is_trial"));
                                    academyBean.setFileTitle(academyObject.getString("file_title"));
                                    academyBean.setFilePath(academyObject.getString("file_path"));

                                    JSONArray galleryArray = academyObject.getJSONArray("coaching_program_gallery");
                                    ArrayList<CoachingProgramGalleryBean> galleryList = new ArrayList<>();
                                    CoachingProgramGalleryBean galleryBean;
                                    for (int j=0; j<galleryArray.length(); j++) {
                                        JSONObject galleryObject = galleryArray.getJSONObject(j);
                                        galleryBean = new CoachingProgramGalleryBean();

                                        galleryBean.setFileTitle(galleryObject.getString("file_title"));
                                        galleryBean.setFileType(galleryObject.getString("file_type"));
                                        galleryBean.setFilePath(galleryObject.getString("file_path"));

                                        if(!(galleryBean.getFileType().contains("video") || galleryBean.getFileType().contains("youtube"))){
                                            galleryList.add(galleryBean);
                                        }


                                    }
                                    academyBean.setGalleryList(galleryList);

                                    academiesList.add(academyBean);
                                }
                                parentProgramFilterSpinnerAdapter = new ParentProgramFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, academiesList);
                                programSpinner.setAdapter(parentProgramFilterSpinnerAdapter);
                            }else{
                                selectProgramText.setVisibility(View.GONE);
                                programSpinner.setVisibility(View.GONE);
                            }

                            // age filter condition
                            if (ageGroupFilter.trim().equalsIgnoreCase("1")){
                                // age group list
                                JSONArray ageGroupList = coachingList.getJSONArray("age_group_list");
                                AgeGroupFilterBean ageGroupFilterBean;

                                // only for show the default label name in first value
                                ageGroupFilterBean = new AgeGroupFilterBean();
                                ageGroupFilterBean.setAgeGroupId("-1");
                                ageGroupFilterBean.setAcademiesId("-1");
                                ageGroupFilterBean.setGroupName("Select Age");
                                ageGroupFilterBean.setFromAge("-1");
                                ageGroupFilterBean.setToAge("-1");
                                ageGroupFilterBean.setAgeGroupType("-1");
                                ageGroupFilterBean.setCreatedAt("-1");
                                ageGroupFilterBean.setState("-1");
                                ageGroupFilterBeanArrayList.add(ageGroupFilterBean);

                                for (int i=0;i<ageGroupList.length();i++) {
                                    JSONObject ageObject = ageGroupList.getJSONObject(i);
                                    ageGroupFilterBean = new AgeGroupFilterBean();

                                    ageGroupFilterBean.setAgeGroupId(ageObject.getString("id"));
                                    ageGroupFilterBean.setAcademiesId(ageObject.getString("academies_id"));
                                    ageGroupFilterBean.setGroupName(ageObject.getString("group_name"));
                                    ageGroupFilterBean.setFromAge(ageObject.getString("from_age"));
                                    ageGroupFilterBean.setToAge(ageObject.getString("to_age"));
                                    ageGroupFilterBean.setAgeGroupType(ageObject.getString("age_group_type"));
                                    ageGroupFilterBean.setCreatedAt(ageObject.getString("created_at"));
                                    ageGroupFilterBean.setState(ageObject.getString("state"));

                                    ageGroupFilterBeanArrayList.add(ageGroupFilterBean);
                                }
                                parentAgeGroupFilterSpinnerAdapter = new ParentAgeGroupFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, ageGroupFilterBeanArrayList);
                                ageGroupSpinner.setAdapter(parentAgeGroupFilterSpinnerAdapter);
                            }else{
                                selectAgeGroupText.setVisibility(View.GONE);
                                ageGroupSpinner.setVisibility(View.GONE);

                            }

                            // term filter condition
                            if (termFilter.trim().equalsIgnoreCase("1")){
                                // term list
                                JSONArray termList = coachingList.getJSONArray("term_list");
                                TermBean termBean;

                                // only for show the default label name in first value
                                termBean = new TermBean();
                                termBean.setTermId("-1");
                                termBean.setTermName("Select Term");
                                termBean.setFromDate("-1");
                                termBean.setToDate("-1");
                                termBean.setShowFromDate("-1");
                                termBean.setShowToDate("-1");
                                termFilterBeanArrayList.add(termBean);

                                for (int i=0;i<termList.length();i++) {
                                    JSONObject termObject = termList.getJSONObject(i);
                                    termBean = new TermBean();

                                    termBean.setTermId(termObject.getString("id"));
                                    termBean.setTermName(termObject.getString("term_name"));
                                    termBean.setFromDate(termObject.getString("from_date"));
                                    termBean.setToDate(termObject.getString("to_date"));
                                    termBean.setShowFromDate(termObject.getString("from_date_formatted"));
                                    termBean.setShowToDate(termObject.getString("to_date_formatted"));

                                    termFilterBeanArrayList.add(termBean);
                                }
                                parentTermFilterSpinnerAdapter = new ParentTermFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, termFilterBeanArrayList);
                                termSpinner.setAdapter(parentTermFilterSpinnerAdapter);
                            }else{
                                selectTermText.setVisibility(View.GONE);
                                termSpinner.setVisibility(View.GONE);

                            }

                            // day filter condition
                            if (dayFilter.trim().equalsIgnoreCase("1")){
                                JSONArray dayFilterArray = coachingList.getJSONArray("day_filter");
                                DayFilterBean dayFilterBean;

                                // only for show the default label name in first value
                                dayFilterBean = new DayFilterBean();
                                dayFilterBean.setDay("Select Day");
                                dayFilterBean.setDayId("-1");
                                dayFilterBeanArrayList.add(dayFilterBean);

                                for (int i=0;i<dayFilterArray.length();i++) {
                                    JSONObject dayFilterObject = dayFilterArray.getJSONObject(i);
                                    dayFilterBean = new DayFilterBean();

                                    dayFilterBean.setDay(dayFilterObject.getString("day"));
                                    dayFilterBean.setDayId(dayFilterObject.getString("id"));


                                    dayFilterBeanArrayList.add(dayFilterBean);
                                }
                                parentDayFilterSpinnerAdapter = new ParentDayFilterSpinnerAdapter(ParentAcademyListingWithFiltersScreen.this, dayFilterBeanArrayList);
                                daySpinner.setAdapter(parentDayFilterSpinnerAdapter);
                            }
                            else{
                                selectDayText.setVisibility(View.GONE);
                                daySpinner.setVisibility(View.GONE);
                            }



                        } else {
                            Toast.makeText(ParentAcademyListingWithFiltersScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentAcademyListingWithFiltersScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                comingFromBookMore = false;
                parentAcademyGridAdapter.notifyDataSetChanged();

                break;

        }
    }
}