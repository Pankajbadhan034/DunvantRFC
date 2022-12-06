package com.dunvant.application.parent;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.HorizontalListView;
import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AcademySessionDateBean;
import com.dunvant.application.beans.AgeGroupBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.PitchSlotsBean;
import com.dunvant.application.beans.PitchSlotsRowBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachAvailableDatesAdapter;
import com.dunvant.application.coach.adapters.CoachLocationListingAdapter;
import com.dunvant.application.coach.adapters.CoachOnlyAgeGroupListingAdapter;
import com.dunvant.application.coach.adapters.CoachSessionListingAdapter;
import com.dunvant.application.parent.adapters.ParentCustomListAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
public class ParentBookFacilityNewViewScreen extends AppCompatActivity implements IWebServiceCallback {
    int dataArraySize=0;
    private final String GET_LOCATION_LISTING = "GET_LOCATION_LISTING";
    private final String GET_SPORTS_LISTING = "GET_SPORTS_LISTING";
    private final String PITCH_SLOTS = "PITCH_SLOTS";

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    TextView title;
    ImageView backButton;

    Spinner locationSpinner;
    Spinner sessionSpinner;
//    Spinner ageGroupSpinner;

    ArrayList<CampLocationBean> locationsList = new ArrayList<>();
    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupBean> ageGroupsListing = new ArrayList<>();
    ArrayList<AcademySessionDateBean> availableDatesList = new ArrayList<>();

    CoachLocationListingAdapter coachLocationListingAdapter;
    CoachSessionListingAdapter coachSessionListingAdapter;
    CoachOnlyAgeGroupListingAdapter coachOnlyAgeGroupListingAdapter;
    CoachAvailableDatesAdapter coachAvailableDatesAdapter;

    CampLocationBean clickedOnLocation;
    CampDaysBean clickedOnDay;
    AgeGroupBean clickedOnAgeGroup;
    String strChosenDates = "";
    static TextView dateSelect;
    Button search;
    ArrayList<PitchSlotsBean>pitchSlotsBeanArrayList = new ArrayList<>();
    String strdateSelect;
    boolean slotDataCheck = false;





    int listViewItemClickPosition;
    String selectedSlot;
    String selectedPitchId;
    private final String SELECT_SLOT = "SELECT_SLOT";
    private final String UNSELECT_SLOT = "UNSELECT_SLOT";
    private final String SUBMIT_SLOTS = "SUBMIT_SLOTS";
    LinearLayout linear2;
    HorizontalListView horListView;
    ScrollView scrollView;
    TableRow tableRow;
    ListView modeList2;
    Button submit;
    ArrayList<ListView> listViewArrayList = new ArrayList<>();
    ArrayList<PitchSlotsRowBean> coloumnArrayList;
    ArrayList<ArrayList<PitchSlotsRowBean>>allcollumnsArrayLists = new ArrayList<>();
    ArrayList<ArrayList<PitchSlotsBean>>allPitchSlotsBeanArrayList = new ArrayList<>();
    ArrayList<ParentCustomListAdapter> parentCustomListAdapterArrayList = new ArrayList<>();
    int JValue;
    ArrayList<PitchSlotsBean> selectedPitchSlotList;
    ArrayList<PitchSlotsRowBean> selectedArrayListTemp = new ArrayList<>();
    String academy_currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_book_facility_new_view_activity);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        academy_currency = sharedPreferences.getString("academy_currency", null);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);
        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        sessionSpinner = (Spinner) findViewById(R.id.sessionSpinner);
        dateSelect = (TextView) findViewById(R.id.dateSelect);
        search = (Button) findViewById(R.id.search);
        submit = (Button) findViewById(R.id.submit);
        linear2 = findViewById(R.id.linear2);
        horListView = findViewById(R.id.horListView);
        scrollView = findViewById(R.id.scrollView);
        tableRow = findViewById(R.id.tableRow);

        locationSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        sessionSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        title.setTypeface(linoType);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try{
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            dateSelect.setText(currentDate);
        }catch (Exception e){
            e.printStackTrace();
        }


        dateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFr();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int locationPosition = locationSpinner.getSelectedItemPosition();
                int sessionPosition = sessionSpinner.getSelectedItemPosition();
                strdateSelect = dateSelect.getText().toString().trim();

                System.out.println("hereDATE::"+strdateSelect);

                if (strdateSelect == null || strdateSelect.isEmpty() || strdateSelect.equalsIgnoreCase("Choose Date")) {
                    Toast.makeText(ParentBookFacilityNewViewScreen.this, "Please select Date", Toast.LENGTH_SHORT).show();
                }else if (dataArraySize != 1 && locationPosition == 0) {
                    Toast.makeText(ParentBookFacilityNewViewScreen.this, "Please select Location", Toast.LENGTH_SHORT).show();
                } else if (sessionPosition == 0) {
                    Toast.makeText(ParentBookFacilityNewViewScreen.this, "Please select Sport", Toast.LENGTH_SHORT).show();
                }  else {
                    pitchSlotsBeanArrayList.clear();
//                    coloumnArrayList.clear();
//                    pitchSlotsBeanArrayList1.clear();
                    allcollumnsArrayLists.clear();
                    allPitchSlotsBeanArrayList.clear();
                    parentCustomListAdapterArrayList.clear();
//                    selectedPitchSlotList.clear();
//                    selectedArrayListTemp.clear();
//                    modeList2.setAdapter(null);
                    // selectedArrayList.clear();
                    listViewArrayList.clear();
                    tableRow.removeAllViews();

                    pitch_slots();
                }
            }
        });

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                    if(dataArraySize==1){
                        clickedOnLocation = locationsList.get(position);
                        getSessionDaysListing();
                    }else{
                        daysListing.clear();

                        // 0th Element Choose Session
                        CampDaysBean daysBean = new CampDaysBean();
                        daysBean.setDay("-1");
                        daysBean.setDayLabel("Choose Sport");

                        daysListing.add(daysBean);

                        coachSessionListingAdapter = new CoachSessionListingAdapter(ParentBookFacilityNewViewScreen.this, daysListing);
                        sessionSpinner.setAdapter(coachSessionListingAdapter);
                    }

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
                if (position == 0) {
                    ageGroupsListing.clear();

                    AgeGroupBean ageGroupBean = new AgeGroupBean();
                    ageGroupBean.setAgeGroupId("-1");
                    ageGroupBean.setGroupName("Choose Age Group");

                    ageGroupsListing.add(ageGroupBean);

//                    coachOnlyAgeGroupListingAdapter = new CoachOnlyAgeGroupListingAdapter(ParentBookFacilityNewViewScreen.this, ageGroupsListing);
//                    ageGroupSpinner.setAdapter(coachOnlyAgeGroupListingAdapter);
                } else {
                    clickedOnDay = daysListing.get(position);
                    //getAgeGroupsListing();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(ParentBookFacilityNewViewScreen.this, ParentBookPitchSummaryNewForCalendarViewScreen.class);
                startActivity(obj);
            }
        });

        getLocationListing();

    }

    private void getLocationListing(){
        if(Utilities.isNetworkAvailable(ParentBookFacilityNewViewScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "front_calendar/location_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ParentBookFacilityNewViewScreen.this, GET_LOCATION_LISTING, ParentBookFacilityNewViewScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookFacilityNewViewScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getSessionDaysListing(){
        if(Utilities.isNetworkAvailable(ParentBookFacilityNewViewScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("location_id", clickedOnLocation.getLocationId()));

            String webServiceUrl = Utilities.BASE_URL + "front_calendar/sport_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookFacilityNewViewScreen.this, nameValuePairList, GET_SPORTS_LISTING, ParentBookFacilityNewViewScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookFacilityNewViewScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void pitch_slots() {
        if (Utilities.isNetworkAvailable(ParentBookFacilityNewViewScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();

            nameValuePairList.add(new BasicNameValuePair("sport_id", clickedOnDay.getDay()));
            nameValuePairList.add(new BasicNameValuePair("location_id", clickedOnLocation.getLocationId()));
            nameValuePairList.add(new BasicNameValuePair("date", strdateSelect));
            nameValuePairList.add(new BasicNameValuePair("user_id", loggedInUser.getId()));

            String webServiceUrl = Utilities.BASE_URL + "front_calendar/pitch_slot";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookFacilityNewViewScreen.this, nameValuePairList, PITCH_SLOTS, ParentBookFacilityNewViewScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookFacilityNewViewScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onWebServiceResponse (String response, String tag){
        switch (tag) {
            case GET_LOCATION_LISTING:

                locationsList.clear();

                // 0th Element Choose Location
                CampLocationBean locationBean = new CampLocationBean();
                locationBean.setLocationId("-1");
                locationBean.setLocationName("Choose Location");

                locationsList.add(locationBean);

                if (response == null) {
                    Toast.makeText(ParentBookFacilityNewViewScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

//                            JSONArray dataArray = responseObject.getJSONArray("data");
                            JSONArray dataArray = responseObject.getJSONArray("data");

                            dataArraySize = dataArray.length();
                            if(dataArray.length()==1){
                                locationsList.clear();

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject locationObject = dataArray.getJSONObject(i);
                                    locationBean = new CampLocationBean();

                                    locationBean.setLocationId(locationObject.getString("id"));
                                    locationBean.setLocationName(locationObject.getString("name"));
                                    locationsList.add(locationBean);
                                }


                                clickedOnLocation = locationsList.get(0);
                                getSessionDaysListing();
                            }else{
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject locationObject = dataArray.getJSONObject(i);
                                    locationBean = new CampLocationBean();

                                    locationBean.setLocationId(locationObject.getString("id"));
                                    locationBean.setLocationName(locationObject.getString("name"));
                                    locationsList.add(locationBean);
                                }

                            }




                        } else {
                            Toast.makeText(ParentBookFacilityNewViewScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookFacilityNewViewScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachLocationListingAdapter = new CoachLocationListingAdapter(ParentBookFacilityNewViewScreen.this, locationsList);
                locationSpinner.setAdapter(coachLocationListingAdapter);
                break;

            case GET_SPORTS_LISTING:

                daysListing.clear();

                // 0th Element Choose Session
                CampDaysBean daysBean = new CampDaysBean();
                daysBean.setDay("-1");
                daysBean.setDayLabel("Choose Sport");

                daysListing.add(daysBean);

                if (response == null) {
                    Toast.makeText(ParentBookFacilityNewViewScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

//                            JSONArray dataArray = responseObject.getJSONArray("data");
                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject daysObject = dataArray.getJSONObject(i);
                                daysBean = new CampDaysBean();
                                daysBean.setDay(daysObject.getString("id"));
                                daysBean.setDayLabel(daysObject.getString("sport_name"));

                                daysListing.add(daysBean);
                            }

                        } else {
                            Toast.makeText(ParentBookFacilityNewViewScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookFacilityNewViewScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachSessionListingAdapter = new CoachSessionListingAdapter(ParentBookFacilityNewViewScreen.this, daysListing);
                sessionSpinner.setAdapter(coachSessionListingAdapter);

                break;


            case PITCH_SLOTS:

                pitchSlotsBeanArrayList.clear();

                if (response == null) {
                    Toast.makeText(ParentBookFacilityNewViewScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                PitchSlotsBean pitchSlotsBean;
                                JSONObject obj = dataArray.getJSONObject(i);
                                pitchSlotsBean = new PitchSlotsBean();
                                pitchSlotsBean.setPitchId(obj.getString("pitch_id"));
                                pitchSlotsBean.setPitchName(obj.getString("pitch_name"));
                                pitchSlotsBean.setPitchPrice(obj.getString("pitch_price"));
                                JSONArray slotArray = obj.getJSONArray("slot");
                                System.out.println("HERE_SLOT_SIZE::"+slotArray.length());
                                ArrayList<PitchSlotsRowBean> slotsArrayList = new ArrayList<>();
                                //PitchSlotsRowBean pitchSlotsRowBean = new PitchSlotsRowBean();
                                // pitchSlotsRowBean.setSlotName(obj.getString("pitch_name")+", \n("+obj.getString("pitch_price")+" "+academy_currency+" per slot)");
                                //  slotsArrayList.add(pitchSlotsRowBean);
                                for(int j=0; j<slotArray.length(); j++){
                                    slotDataCheck = true;
                                    System.out.println("HERE_SLOT::"+slotArray.getString(i));
                                    PitchSlotsRowBean pitchSlotsRowBean2 = new PitchSlotsRowBean();
                                    pitchSlotsRowBean2.setSlotName(slotArray.getString(j));
                                    pitchSlotsRowBean2.setClicked(false);
                                    pitchSlotsRowBean2.setDisabled(false);
                                    slotsArrayList.add(pitchSlotsRowBean2);
                                }
                                pitchSlotsBean.setStringArrayList(slotsArrayList);
                                pitchSlotsBeanArrayList.add(pitchSlotsBean);
                            }




                            JSONArray hiddenSlotArray = responseObject.getJSONArray("hidden_slot");
                            for (int i = 0; i < hiddenSlotArray.length(); i++) {
                                JSONObject hiddenObj = hiddenSlotArray.getJSONObject(i);

                                String pitch_id = hiddenObj.getString("pitch_id");
                                JSONArray idsArray = hiddenObj.getJSONArray("ids");
                                JSONArray datesArray = hiddenObj.getJSONArray("dates");


                                for(int j=0; j<pitchSlotsBeanArrayList.size(); j++){
                                    if(pitch_id.equalsIgnoreCase(pitchSlotsBeanArrayList.get(j).getPitchId())){

                                        for(int k=0; k<datesArray.length(); k++){
                                            JSONObject datesObj = datesArray.getJSONObject(k);
                                            String date = datesObj.getString("date");
                                            JSONArray slotCheckArray = datesObj.getJSONArray("slots");

                                            if(date.equalsIgnoreCase(strdateSelect)){

                                                for(int d=0; d<slotCheckArray.length(); d++){
                                                    String slotcheckStr = slotCheckArray.getString(d);


                                                    for(int m=0; m<pitchSlotsBeanArrayList.get(j).getStringArrayList().size(); m++){

                                                        if(slotcheckStr.equalsIgnoreCase(pitchSlotsBeanArrayList.get(j).getStringArrayList().get(m).getSlotName())){
                                                            pitchSlotsBeanArrayList.get(j).getStringArrayList().get(m).setClicked(true);
                                                        }
                                                    }

                                                    for(int s=0; s<idsArray.length(); s++){
                                                        String idsCheckStr = idsArray.getString(s);

                                                        for(int p=0; p<pitchSlotsBeanArrayList.size(); p++){
                                                            if(idsCheckStr.equalsIgnoreCase(pitchSlotsBeanArrayList.get(p).getPitchId())){
                                                                for(int l=0; l<pitchSlotsBeanArrayList.get(p).getStringArrayList().size(); l++){

                                                                    if(slotcheckStr.equalsIgnoreCase(pitchSlotsBeanArrayList.get(p).getStringArrayList().get(l).getSlotName())){
                                                                        pitchSlotsBeanArrayList.get(p).getStringArrayList().get(l).setDisabled(true);
                                                                    }


                                                                }

                                                            }

                                                        }
                                                    }
                                                }
                                            }

                                        }

                                    }

                                }


                            }



                            if(slotDataCheck){
                                customListsViewItems();
                                submit.setVisibility(View.VISIBLE);
                                slotDataCheck = false;
                            }else{
                                Toast.makeText(this, "No Slot Found.", Toast.LENGTH_SHORT).show();
                            }



                        }else{
                            Toast.makeText(ParentBookFacilityNewViewScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookFacilityNewViewScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case SELECT_SLOT:
                if (response == null) {
                    Toast.makeText(ParentBookFacilityNewViewScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
//                            childPostsBeanArrayList.get(acceptCountPosition).setChallengeChallengeStatus("accepted_challenge");
//                            notifyDataSetChanged();

                            JSONArray jsonArray = responseObject.getJSONArray("data");

                            ArrayList<String> disabledIdsArray = new ArrayList<>();

                            for(int i=0; i<jsonArray.length(); i++){
                                disabledIdsArray.add(jsonArray.getString(i));
                            }

                            for(int j=0; j<disabledIdsArray.size(); j++){



                                if(selectedPitchId.equalsIgnoreCase(disabledIdsArray.get(j))){
                                    System.out.println("listViewItemClickPosition:: "+listViewItemClickPosition+" JValue:: "+JValue+" selectedPitchId:: "+selectedPitchId+" ::disabledArrayID:: "+disabledIdsArray.get(j));
                                    selectedArrayListTemp.get(listViewItemClickPosition).setClicked(true);
                                    parentCustomListAdapterArrayList.get(JValue).notifyDataSetChanged();

                                }else{

                                    for(int s=0; s<selectedPitchSlotList.size(); s++){

                                        if(selectedPitchSlotList.get(s).getPitchId().equalsIgnoreCase(disabledIdsArray.get(j))){
                                            System.out.println("1a::"+selectedPitchSlotList.get(s).getPitchId());
                                            System.out.println("1b::"+disabledIdsArray.get(j));

                                            for(int k=0; k<selectedPitchSlotList.get(s).getStringArrayList().size(); k++){
                                                PitchSlotsRowBean pitchSlotsRowBean = selectedPitchSlotList.get(s).getStringArrayList().get(k);

                                                if(pitchSlotsRowBean.getSlotName().equalsIgnoreCase(selectedSlot)){
                                                    System.out.println("position::"+pitchSlotsRowBean.getSlotName());
                                                    selectedPitchSlotList.get(s).getStringArrayList().get(k).setDisabled(true);
                                                    parentCustomListAdapterArrayList.get(s).notifyDataSetChanged();
                                                }


                                            }


                                        }

                                    }


                                }
                            }








                        }else{
                            Toast.makeText(ParentBookFacilityNewViewScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookFacilityNewViewScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;

            case UNSELECT_SLOT:
                if (response == null) {
                    Toast.makeText(ParentBookFacilityNewViewScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
//                            childPostsBeanArrayList.get(acceptCountPosition).setChallengeChallengeStatus("accepted_challenge");
//                            notifyDataSetChanged();

                            JSONArray jsonArray = responseObject.getJSONArray("data");

                            ArrayList<String> disabledIdsArray = new ArrayList<>();

                            for(int i=0; i<jsonArray.length(); i++){
                                disabledIdsArray.add(jsonArray.getString(i));
                            }







                            for(int j=0; j<disabledIdsArray.size(); j++){

                                if(selectedPitchId.equalsIgnoreCase(disabledIdsArray.get(j))){
                                    selectedArrayListTemp.get(listViewItemClickPosition).setClicked(false);
                                    parentCustomListAdapterArrayList.get(JValue).notifyDataSetChanged();

                                }else{

                                    for(int s=0; s<selectedPitchSlotList.size(); s++){

                                        if(selectedPitchSlotList.get(s).getPitchId().equalsIgnoreCase(disabledIdsArray.get(j))){
                                            System.out.println("1a::"+selectedPitchSlotList.get(s).getPitchId());
                                            System.out.println("1b::"+disabledIdsArray.get(j));

                                            for(int k=0; k<selectedPitchSlotList.get(s).getStringArrayList().size(); k++){
                                                PitchSlotsRowBean pitchSlotsRowBean = selectedPitchSlotList.get(s).getStringArrayList().get(k);

                                                if(pitchSlotsRowBean.getSlotName().equalsIgnoreCase(selectedSlot)){
                                                    System.out.println("position::"+pitchSlotsRowBean.getSlotName());
                                                    selectedPitchSlotList.get(s).getStringArrayList().get(k).setDisabled(false);
                                                    parentCustomListAdapterArrayList.get(s).notifyDataSetChanged();
                                                }


                                            }


                                        }

                                    }


                                }
                            }








                        }else{
                            Toast.makeText(ParentBookFacilityNewViewScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookFacilityNewViewScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;

            case SUBMIT_SLOTS:
                if (response == null) {
                    Toast.makeText(ParentBookFacilityNewViewScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray jsonArray = responseObject.getJSONArray("data");

                        }else{
                            Toast.makeText(ParentBookFacilityNewViewScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookFacilityNewViewScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;
        }
    }


    public static class DatePickerFr extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month++;
            String strDay = day < 10 ? "0"+day : day+"";
            String strMonth = month < 10 ? "0"+month : month+"";
            dateSelect.setText(year+"-"+strMonth+"-"+strDay);
        }
    }






    public void customListsViewItems(){

        ArrayList<String>titleBarList = new ArrayList<>();
        for(int d=0; d<pitchSlotsBeanArrayList.size(); d++){
            PitchSlotsBean pitchSlotsBean = pitchSlotsBeanArrayList.get(d);
            titleBarList.add(pitchSlotsBean.getPitchName()+", \n("+pitchSlotsBean.getPitchPrice()+" "+academy_currency+" per slot)");
        }

        int titleBarListSize = titleBarList.size();

        System.out.println("titleBarListSize::"+titleBarListSize);

//        if(titleBarListSize==1){
//            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.parent_custom_list_title_bar_horizontal_list_single_item, R.id.categoryname, titleBarList);
//            horListView.setAdapter(adapter1);
//        }else{
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.parent_custom_list_title_bar_horizontal_list_item, R.id.categoryname, titleBarList);
        horListView.setAdapter(adapter1);
//        }


        for(int i=0; i<pitchSlotsBeanArrayList.size(); i++){
            modeList2 = new ListView(this);
            coloumnArrayList = new ArrayList<>();
            for(int j=0; j<pitchSlotsBeanArrayList.get(i).getStringArrayList().size(); j++){
                coloumnArrayList.add(pitchSlotsBeanArrayList.get(i).getStringArrayList().get(j));
            }



            ParentCustomListAdapter adapter = new ParentCustomListAdapter(ParentBookFacilityNewViewScreen.this, coloumnArrayList, titleBarListSize);
            modeList2.setAdapter(adapter);



            tableRow.addView(modeList2);
            Utilities.setListViewHeightBasedOnChildren(modeList2);
            listViewArrayList.add(modeList2);

            parentCustomListAdapterArrayList.add(adapter);
            allcollumnsArrayLists.add(coloumnArrayList);
            allPitchSlotsBeanArrayList.add(pitchSlotsBeanArrayList);
        }

        System.out.println("sizeHERE::"+listViewArrayList.size());

        for(int j = 0; j<listViewArrayList.size(); j++){
            final ArrayList<PitchSlotsRowBean> selectedArrayList =  allcollumnsArrayLists.get(j);

            // selectedArrayList   =  allcollumnsArrayLists.get(j);
            selectedPitchSlotList =  allPitchSlotsBeanArrayList.get(j);

            final int finalJ = j;
            listViewArrayList.get(j).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    listViewItemClickPosition = position;

                    if(listViewItemClickPosition==0 || selectedArrayList.get(listViewItemClickPosition).getSlotName().equalsIgnoreCase("--")){

                    }else{
                        JValue = finalJ;
                        selectedSlot = selectedArrayList.get(listViewItemClickPosition).getSlotName();
                        selectedPitchId = selectedPitchSlotList.get(JValue).getPitchId();
                        selectedArrayListTemp = selectedArrayList;



                        if(selectedArrayList.get(listViewItemClickPosition).getSlotName().equalsIgnoreCase("Booked")){

                        }else{
                            if(selectedArrayList.get(listViewItemClickPosition).isClicked()){
                                slotUnselected(selectedSlot,selectedPitchId);
                            }else{
                                if(selectedArrayList.get(listViewItemClickPosition).isDisabled()){

                                }else{
                                    slotSelected(selectedSlot,selectedPitchId);
                                }

                            }
                        }








                    }
                }
            });
        }
    }

    public void slotSelected(String slot, String pitchID){
        if (Utilities.isNetworkAvailable(ParentBookFacilityNewViewScreen.this)) {

            final List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("date", strdateSelect));
            nameValuePairList.add(new BasicNameValuePair("slot", slot));
            nameValuePairList.add(new BasicNameValuePair("pitch_id", pitchID));

            final ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            String webServiceUrl = Utilities.BASE_URL + "front_calendar/select_slot";
            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookFacilityNewViewScreen.this, nameValuePairList, SELECT_SLOT, ParentBookFacilityNewViewScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookFacilityNewViewScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    public void slotUnselected(String slot, String pitchID){
        if (Utilities.isNetworkAvailable(ParentBookFacilityNewViewScreen.this)) {

            final List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("date", strdateSelect));
            nameValuePairList.add(new BasicNameValuePair("slot", slot));
            nameValuePairList.add(new BasicNameValuePair("pitch_id", pitchID));

            final ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            String webServiceUrl = Utilities.BASE_URL + "front_calendar/unselect_slot";
            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookFacilityNewViewScreen.this, nameValuePairList, UNSELECT_SLOT, ParentBookFacilityNewViewScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookFacilityNewViewScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    public void submitSlots(){
        if (Utilities.isNetworkAvailable(ParentBookFacilityNewViewScreen.this)) {

            final List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("user_id", loggedInUser.getId()));
            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));

            final ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            String webServiceUrl = Utilities.BASE_URL + "front_calendar/booked_pitches";
            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookFacilityNewViewScreen.this, nameValuePairList, SUBMIT_SLOTS, ParentBookFacilityNewViewScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookFacilityNewViewScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

}