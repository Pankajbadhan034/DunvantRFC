package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.FacilityBean;
import com.dunvant.application.beans.PitchAvailabilityBean;
import com.dunvant.application.beans.PitchBean;
import com.dunvant.application.beans.PitchBookedDataBean;
import com.dunvant.application.beans.PitchExcludedDatesBean;
import com.dunvant.application.beans.PitchTypeBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentPitchListingAdapter;
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

import androidx.appcompat.app.AppCompatActivity;

public class ParentFacilityDetailScreen extends AppCompatActivity implements IWebServiceCallback{
    String typeId;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    ScrollView scrollView;
    ImageView facilityImage;
    TextView facilityName;
    TextView facilityDescription;
    TextView lblLocations;
    TextView facilityLocation;
    ListView pitchesListView;
    Button bookNow;

    FacilityBean clickedOnFacility;

    private final String GET_LOCATION_DETAILS = "GET_LOCATION_DETAILS";

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_facility_detail_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
        typeId = getIntent().getStringExtra("typeId");

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentFacilityDetailScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        facilityImage = (ImageView) findViewById(R.id.facilityImage);
        facilityName = (TextView) findViewById(R.id.facilityName);
        facilityDescription = (TextView) findViewById(R.id.facilityDescription);
        lblLocations = (TextView) findViewById(R.id.lblLocations);
        facilityLocation = (TextView) findViewById(R.id.facilityLocation);
        pitchesListView = (ListView) findViewById(R.id.pitchesListView);
        bookNow = (Button) findViewById(R.id.bookNow);

        changeFonts();

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnFacility = (FacilityBean) intent.getSerializableExtra("clickedOnFacility");
            getFacilityDetails();
        }

        /*pitchesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PitchBean clickedOnPitch = clickedOnFacility.getPitchesList().get(position);
                Intent bookPitchScreen = new Intent(ParentFacilityDetailScreen.this, ParentBookPitchScreen.class);
                bookPitchScreen.putExtra("clickedOnFacility", clickedOnFacility);
                bookPitchScreen.putExtra("clickedOnPitch", clickedOnPitch);
                startActivity(bookPitchScreen);
            }
        });*/

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookPitchScreen = new Intent(ParentFacilityDetailScreen.this, ParentBookPitchScreen.class);
                bookPitchScreen.putExtra("clickedOnFacility", clickedOnFacility);
                startActivity(bookPitchScreen);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateUI() {
        imageLoader.displayImage(clickedOnFacility.getFilePath(), facilityImage, options);
        facilityName.setText(clickedOnFacility.getLocationName());

        if(clickedOnFacility.getLocationDescription() == null || clickedOnFacility.getLocationDescription().isEmpty() || clickedOnFacility.getLocationDescription().equalsIgnoreCase("null")){
            facilityDescription.setText("");
        } else {
            facilityDescription.setText(clickedOnFacility.getLocationDescription());
        }

        if(clickedOnFacility.getShowPitch().equalsIgnoreCase("1")) {
            pitchesListView.setAdapter(new ParentPitchListingAdapter(ParentFacilityDetailScreen.this, clickedOnFacility.getPitchesList(), clickedOnFacility));
            Utilities.setListViewHeightBasedOnChildren(pitchesListView);

            bookNow.setVisibility(View.GONE);
        } else {
            pitchesListView.setAdapter(null);
            bookNow.setVisibility(View.VISIBLE);
        }

        scrollView.smoothScrollTo(0,0);
    }

    private void getFacilityDetails(){
        if(Utilities.isNetworkAvailable(ParentFacilityDetailScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("location_id", clickedOnFacility.getFacilityId()));

//            String webServiceUrl = Utilities.BASE_URL + "pitch/detail";
            String webServiceUrl = Utilities.BASE_URL + Utilities.detailAdcService;
            nameValuePairList.add(new BasicNameValuePair("typeid", typeId));

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentFacilityDetailScreen.this, nameValuePairList, GET_LOCATION_DETAILS, ParentFacilityDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentFacilityDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_LOCATION_DETAILS:

                if(response == null) {
                    Toast.makeText(ParentFacilityDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONObject locationObject = responseObject.getJSONObject("data");

                            clickedOnFacility.setFacilityId(locationObject.getString("id"));
                            clickedOnFacility.setLocationName(locationObject.getString("location_name"));
                            clickedOnFacility.setLocationDescription(locationObject.getString("location_desc"));
                            clickedOnFacility.setFileTitle(locationObject.getString("file_title"));
                            clickedOnFacility.setFilePath(locationObject.getString("file_path"));
                            clickedOnFacility.setPitchIds(locationObject.getString("pitches_id"));
                            clickedOnFacility.setShowPitch(locationObject.getString("show_pitch"));

                            JSONArray pitchesArray = locationObject.getJSONArray("pitches");
                            ArrayList<PitchBean> pitchBeenList = new ArrayList<>();
                            PitchBean pitchBean;

                            for (int i=0;i<pitchesArray.length(); i++) {
                                JSONObject pitchObject = pitchesArray.getJSONObject(i);
                                pitchBean = new PitchBean();

                                pitchBean.setPitchId(pitchObject.getString("pitch_id"));

                                if(pitchObject.has("parent_id")){
                                    pitchBean.setParentId(pitchObject.getString("parent_id"));
                                }else{
                                    pitchBean.setParentId("0");
                                }

                                pitchBean.setAcademiesId(pitchObject.getString("academies_id"));
                                pitchBean.setPitchName(pitchObject.getString("pitch_name"));
                                pitchBean.setFromDateFormatted(pitchObject.getString("from_date_formatted"));
                                pitchBean.setToDateFormatted(pitchObject.getString("to_date_formatted"));
                                pitchBean.setFromDate(pitchObject.getString("from_date"));
                                pitchBean.setToDate(pitchObject.getString("to_date"));
                                pitchBean.setIsHalfHour(pitchObject.getString("is_half_hour"));
                                pitchBean.setLeagueState(pitchObject.getString("league_state"));
                                pitchBean.setLocationsId(pitchObject.getString("locations_id"));
                                pitchBean.setLocationName(pitchObject.getString("location_name"));
                                pitchBean.setLocationDescription(pitchObject.getString("location_desc"));
                                pitchBean.setFileTitle(pitchObject.getString("file_title"));
                                pitchBean.setFilePath(pitchObject.getString("file_path"));
                                pitchBean.setNoPitch(pitchObject.getString("no_pitch"));
                                pitchBean.setPitchDurationLabel(pitchObject.getString("pitch_duration_label"));

                                JSONArray pitchAvailabilityArray = pitchObject.getJSONArray("pitch_availability");
                                ArrayList<PitchAvailabilityBean> pitchAvailabilityList = new ArrayList<>();
                                for(int j=0;j<pitchAvailabilityArray.length();j++){
                                    JSONObject pitchAvailabilityObject = pitchAvailabilityArray.getJSONObject(j);
                                    PitchAvailabilityBean pitchAvailabilityBean = new PitchAvailabilityBean();

                                    pitchAvailabilityBean.setDay(pitchAvailabilityObject.getString("day"));
                                    pitchAvailabilityBean.setAvailabilityFromTime(pitchAvailabilityObject.getString("availability_from_time"));
                                    pitchAvailabilityBean.setAvailabilityToTime(pitchAvailabilityObject.getString("availability_to_time"));
                                    pitchAvailabilityBean.setAvailabilityIsTimeOff(pitchAvailabilityObject.getString("availability_is_time_off"));

                                    pitchAvailabilityList.add(pitchAvailabilityBean);
                                }
                                pitchBean.setPitchAvailabilityListing(pitchAvailabilityList);

                                JSONArray pitchExcludedDatesArray = pitchObject.getJSONArray("pitch_excluded_dates");
                                ArrayList<PitchExcludedDatesBean> pitchExcludedDatesList = new ArrayList<>();
                                for(int j=0;j<pitchExcludedDatesArray.length();j++){
                                    JSONObject pitchExcludedDateObject = pitchExcludedDatesArray.getJSONObject(j);
                                    PitchExcludedDatesBean pitchExcludedDatesBean = new PitchExcludedDatesBean();

                                    pitchExcludedDatesBean.setExcludedDate(pitchExcludedDateObject.getString("excluded_date"));
                                    pitchExcludedDatesBean.setExcludedFromTime(pitchExcludedDateObject.getString("excluded_from_time"));
                                    pitchExcludedDatesBean.setExcludedToTime(pitchExcludedDateObject.getString("excluded_to_time"));

                                    pitchExcludedDatesList.add(pitchExcludedDatesBean);
                                }
                                pitchBean.setPitchExcludedDatesBeanListing(pitchExcludedDatesList);

                                JSONArray pitchBookedDataArray = pitchObject.getJSONArray("pitch_booked_data");
                                ArrayList<PitchBookedDataBean> pitchBookedDataList = new ArrayList<>();
                                for(int j=0;j<pitchBookedDataArray.length();j++){
                                    JSONObject pitchBookedDataObject = pitchBookedDataArray.getJSONObject(j);
                                    PitchBookedDataBean pitchBookedDataBean = new PitchBookedDataBean();

                                    pitchBookedDataBean.setOrdersId(pitchBookedDataObject.getString("orders_id"));
                                    pitchBookedDataBean.setBookingDate(pitchBookedDataObject.getString("booking_date"));
                                    pitchBookedDataBean.setBookedFromTime(pitchBookedDataObject.getString("booked_from_time"));
                                    pitchBookedDataBean.setBookedToTime(pitchBookedDataObject.getString("booked_to_time"));

                                    pitchBookedDataList.add(pitchBookedDataBean);
                                }
                                pitchBean.setPitchBookedDataBeanListing(pitchBookedDataList);

                                JSONObject pitchPrices = pitchObject.getJSONObject("pitch_prices");

                                if(pitchPrices.has("simple_pitch")){
                                    ArrayList<PitchTypeBean> simplePitchList = new ArrayList<>();
                                    JSONArray simplePitchArray = pitchPrices.getJSONArray("simple_pitch");
                                    for(int j=0;j<simplePitchArray.length();j++){
                                        JSONObject simplePitchObject = simplePitchArray.getJSONObject(j);
                                        PitchTypeBean simplePitchBean = new PitchTypeBean();

                                        simplePitchBean.setPitchesId(simplePitchObject.getString("pitches_id"));
                                        simplePitchBean.setFromDate(simplePitchObject.getString("from_date"));
                                        simplePitchBean.setToDate(simplePitchObject.getString("to_date"));
                                        simplePitchBean.setDay(simplePitchObject.getString("day"));
                                        simplePitchBean.setFromTime(simplePitchObject.getString("from_time"));
                                        simplePitchBean.setToTime(simplePitchObject.getString("to_time"));
                                        simplePitchBean.setHourPrice(simplePitchObject.getString("hour_price"));
                                        simplePitchBean.setPitchType(simplePitchObject.getString("pitch_type"));
                                        simplePitchBean.setFromTimeFormatted(simplePitchObject.getString("from_time_formatted"));
                                        simplePitchBean.setToTimeFormatted(simplePitchObject.getString("to_time_formatted"));
                                        simplePitchBean.setFromDateFormatted(simplePitchObject.getString("from_date_formatted"));
                                        simplePitchBean.setToDateFormatted(simplePitchObject.getString("to_date_formatted"));
                                        simplePitchBean.setDayLabel(simplePitchObject.getString("day_label"));

                                        simplePitchList.add(simplePitchBean);
                                    }
                                    pitchBean.setSimplePitchList(simplePitchList);
                                } else {
                                    pitchBean.setSimplePitchList(null);
                                }

                                if(pitchPrices.has("full_pitch")){
                                    ArrayList<PitchTypeBean> fullPitchList = new ArrayList<>();
                                    JSONArray fullPitchArray = pitchPrices.getJSONArray("full_pitch");
                                    for(int j=0;j<fullPitchArray.length();j++){
                                        JSONObject fullPitchObject = fullPitchArray.getJSONObject(j);
                                        PitchTypeBean fullPitchBean = new PitchTypeBean();

                                        fullPitchBean.setPitchesId(fullPitchObject.getString("pitches_id"));
                                        fullPitchBean.setFromDate(fullPitchObject.getString("from_date"));
                                        fullPitchBean.setToDate(fullPitchObject.getString("to_date"));
                                        fullPitchBean.setDay(fullPitchObject.getString("day"));
                                        fullPitchBean.setFromTime(fullPitchObject.getString("from_time"));
                                        fullPitchBean.setToTime(fullPitchObject.getString("to_time"));
                                        fullPitchBean.setHourPrice(fullPitchObject.getString("hour_price"));
                                        fullPitchBean.setPitchType(fullPitchObject.getString("pitch_type"));
                                        fullPitchBean.setFromTimeFormatted(fullPitchObject.getString("from_time_formatted"));
                                        fullPitchBean.setToTimeFormatted(fullPitchObject.getString("to_time_formatted"));
                                        fullPitchBean.setFromDateFormatted(fullPitchObject.getString("from_date_formatted"));
                                        fullPitchBean.setToDateFormatted(fullPitchObject.getString("to_date_formatted"));
                                        fullPitchBean.setDayLabel(fullPitchObject.getString("day_label"));

                                        fullPitchList.add(fullPitchBean);
                                    }
                                    pitchBean.setFullPitchList(fullPitchList);
                                } else {
                                    pitchBean.setFullPitchList(null);
                                }

                                if(pitchPrices.has("special_price")){
                                    ArrayList<PitchTypeBean> specialPriceList = new ArrayList<>();
                                    JSONArray specialPriceArray = pitchPrices.getJSONArray("special_price");
                                    for(int j=0;j<specialPriceArray.length();j++){
                                        JSONObject specialPriceObject = specialPriceArray.getJSONObject(j);
                                        PitchTypeBean specialPriceBean = new PitchTypeBean();

                                        specialPriceBean.setPitchesId(specialPriceObject.getString("pitches_id"));
                                        specialPriceBean.setFromDate(specialPriceObject.getString("from_date"));
                                        specialPriceBean.setToDate(specialPriceObject.getString("to_date"));
                                        specialPriceBean.setDay(specialPriceObject.getString("day"));
                                        specialPriceBean.setFromTime(specialPriceObject.getString("from_time"));
                                        specialPriceBean.setToTime(specialPriceObject.getString("to_time"));
                                        specialPriceBean.setHourPrice(specialPriceObject.getString("hour_price"));
                                        specialPriceBean.setPitchType(specialPriceObject.getString("pitch_type"));
                                        specialPriceBean.setFromTimeFormatted(specialPriceObject.getString("from_time_formatted"));
                                        specialPriceBean.setToTimeFormatted(specialPriceObject.getString("to_time_formatted"));
                                        specialPriceBean.setFromDateFormatted(specialPriceObject.getString("from_date_formatted"));
                                        specialPriceBean.setToDateFormatted(specialPriceObject.getString("to_date_formatted"));
                                        specialPriceBean.setDayLabel(specialPriceObject.getString("day_label"));

                                        specialPriceList.add(specialPriceBean);
                                    }
                                    pitchBean.setSpecialPriceList(specialPriceList);
                                } else {
                                    pitchBean.setSpecialPriceList(null);
                                }

                                /*if(pitchPrices.has("simple_pitch")){

                                    JSONObject simplePitchObject = pitchPrices.getJSONObject("simple_pitch");
                                    PitchTypeBean simplePitchBean = new PitchTypeBean();

                                    JSONObject offRateObject = simplePitchObject.getJSONObject("off_rate");
                                    PitchTimeTypeBean offRateBean = new PitchTimeTypeBean();

                                    offRateBean.setTimeRange(offRateObject.getString("time_range"));

                                    JSONObject fridayObject = offRateObject.getJSONObject("friday");
                                    PitchPriceDetailBean fridayBean = new PitchPriceDetailBean();

                                    fridayBean.setPitchesId(fridayObject.getString("pitches_id"));
                                    fridayBean.setDay(fridayObject.getString("day"));
                                    fridayBean.setFromTime(fridayObject.getString("from_time"));
                                    fridayBean.setToTime(fridayObject.getString("to_time"));
                                    fridayBean.setHourPrice(fridayObject.getString("hour_price"));
                                    fridayBean.setPitchType(fridayObject.getString("pitch_type"));
                                    fridayBean.setHourType(fridayObject.getString("hour_type"));
                                    fridayBean.setFromTimeFormatted(fridayObject.getString("from_time_formatted"));
                                    fridayBean.setToTimeFormatted(fridayObject.getString("to_time_formatted"));
                                    offRateBean.setFriday(fridayBean);

                                    JSONObject saturdayObject = offRateObject.getJSONObject("sat");
                                    PitchPriceDetailBean saturdayBean = new PitchPriceDetailBean();
                                    saturdayBean.setPitchesId(saturdayObject.getString("pitches_id"));
                                    saturdayBean.setDay(saturdayObject.getString("day"));
                                    saturdayBean.setFromTime(saturdayObject.getString("from_time"));
                                    saturdayBean.setToTime(saturdayObject.getString("to_time"));
                                    saturdayBean.setHourPrice(saturdayObject.getString("hour_price"));
                                    saturdayBean.setPitchType(saturdayObject.getString("pitch_type"));
                                    saturdayBean.setHourType(saturdayObject.getString("hour_type"));
                                    saturdayBean.setFromTimeFormatted(saturdayObject.getString("from_time_formatted"));
                                    saturdayBean.setToTimeFormatted(saturdayObject.getString("to_time_formatted"));
                                    offRateBean.setSaturday(saturdayBean);

                                    JSONObject sundayThursdayObject = offRateObject.getJSONObject("sun_thu");
                                    PitchPriceDetailBean sundayThursdayBean = new PitchPriceDetailBean();
                                    sundayThursdayBean.setPitchesId(sundayThursdayObject.getString("pitches_id"));
                                    sundayThursdayBean.setDay(sundayThursdayObject.getString("day"));
                                    sundayThursdayBean.setFromTime(sundayThursdayObject.getString("from_time"));
                                    sundayThursdayBean.setToTime(sundayThursdayObject.getString("to_time"));
                                    sundayThursdayBean.setHourPrice(sundayThursdayObject.getString("hour_price"));
                                    sundayThursdayBean.setPitchType(sundayThursdayObject.getString("pitch_type"));
                                    sundayThursdayBean.setHourType(sundayThursdayObject.getString("hour_type"));
                                    sundayThursdayBean.setFromTimeFormatted(sundayThursdayObject.getString("from_time_formatted"));
                                    sundayThursdayBean.setToTimeFormatted(sundayThursdayObject.getString("to_time_formatted"));
                                    offRateBean.setSundayToThursday(sundayThursdayBean);

                                    simplePitchBean.setOffRate(offRateBean);

                                    JSONObject normalRateObject = simplePitchObject.getJSONObject("normal_rate");
                                    PitchTimeTypeBean normalRateBean = new PitchTimeTypeBean();

                                    normalRateBean.setTimeRange(normalRateObject.getString("time_range"));

                                    fridayObject = normalRateObject.getJSONObject("friday");
                                    fridayBean = new PitchPriceDetailBean();

                                    fridayBean.setPitchesId(fridayObject.getString("pitches_id"));
                                    fridayBean.setDay(fridayObject.getString("day"));
                                    fridayBean.setFromTime(fridayObject.getString("from_time"));
                                    fridayBean.setToTime(fridayObject.getString("to_time"));
                                    fridayBean.setHourPrice(fridayObject.getString("hour_price"));
                                    fridayBean.setPitchType(fridayObject.getString("pitch_type"));
                                    fridayBean.setHourType(fridayObject.getString("hour_type"));
                                    fridayBean.setFromTimeFormatted(fridayObject.getString("from_time_formatted"));
                                    fridayBean.setToTimeFormatted(fridayObject.getString("to_time_formatted"));
                                    normalRateBean.setFriday(fridayBean);

                                    saturdayObject = normalRateObject.getJSONObject("sat");
                                    saturdayBean = new PitchPriceDetailBean();
                                    saturdayBean.setPitchesId(saturdayObject.getString("pitches_id"));
                                    saturdayBean.setDay(saturdayObject.getString("day"));
                                    saturdayBean.setFromTime(saturdayObject.getString("from_time"));
                                    saturdayBean.setToTime(saturdayObject.getString("to_time"));
                                    saturdayBean.setHourPrice(saturdayObject.getString("hour_price"));
                                    saturdayBean.setPitchType(saturdayObject.getString("pitch_type"));
                                    saturdayBean.setHourType(saturdayObject.getString("hour_type"));
                                    saturdayBean.setFromTimeFormatted(saturdayObject.getString("from_time_formatted"));
                                    saturdayBean.setToTimeFormatted(saturdayObject.getString("to_time_formatted"));
                                    normalRateBean.setSaturday(saturdayBean);

                                    sundayThursdayObject = normalRateObject.getJSONObject("sun_thu");
                                    sundayThursdayBean = new PitchPriceDetailBean();
                                    sundayThursdayBean.setPitchesId(sundayThursdayObject.getString("pitches_id"));
                                    sundayThursdayBean.setDay(sundayThursdayObject.getString("day"));
                                    sundayThursdayBean.setFromTime(sundayThursdayObject.getString("from_time"));
                                    sundayThursdayBean.setToTime(sundayThursdayObject.getString("to_time"));
                                    sundayThursdayBean.setHourPrice(sundayThursdayObject.getString("hour_price"));
                                    sundayThursdayBean.setPitchType(sundayThursdayObject.getString("pitch_type"));
                                    sundayThursdayBean.setHourType(sundayThursdayObject.getString("hour_type"));
                                    sundayThursdayBean.setFromTimeFormatted(sundayThursdayObject.getString("from_time_formatted"));
                                    sundayThursdayBean.setToTimeFormatted(sundayThursdayObject.getString("to_time_formatted"));
                                    normalRateBean.setSundayToThursday(sundayThursdayBean);

                                    simplePitchBean.setNormalRate(normalRateBean);

                                    JSONObject peakRateObject = simplePitchObject.getJSONObject("peak_rate");
                                    PitchTimeTypeBean peakRateBean = new PitchTimeTypeBean();

                                    peakRateBean.setTimeRange(peakRateObject.getString("time_range"));

                                    fridayObject = peakRateObject.getJSONObject("friday");
                                    fridayBean = new PitchPriceDetailBean();

                                    fridayBean.setPitchesId(fridayObject.getString("pitches_id"));
                                    fridayBean.setDay(fridayObject.getString("day"));
                                    fridayBean.setFromTime(fridayObject.getString("from_time"));
                                    fridayBean.setToTime(fridayObject.getString("to_time"));
                                    fridayBean.setHourPrice(fridayObject.getString("hour_price"));
                                    fridayBean.setPitchType(fridayObject.getString("pitch_type"));
                                    fridayBean.setHourType(fridayObject.getString("hour_type"));
                                    fridayBean.setFromTimeFormatted(fridayObject.getString("from_time_formatted"));
                                    fridayBean.setToTimeFormatted(fridayObject.getString("to_time_formatted"));
                                    peakRateBean.setFriday(fridayBean);

                                    saturdayObject = peakRateObject.getJSONObject("sat");
                                    saturdayBean = new PitchPriceDetailBean();
                                    saturdayBean.setPitchesId(saturdayObject.getString("pitches_id"));
                                    saturdayBean.setDay(saturdayObject.getString("day"));
                                    saturdayBean.setFromTime(saturdayObject.getString("from_time"));
                                    saturdayBean.setToTime(saturdayObject.getString("to_time"));
                                    saturdayBean.setHourPrice(saturdayObject.getString("hour_price"));
                                    saturdayBean.setPitchType(saturdayObject.getString("pitch_type"));
                                    saturdayBean.setHourType(saturdayObject.getString("hour_type"));
                                    saturdayBean.setFromTimeFormatted(saturdayObject.getString("from_time_formatted"));
                                    saturdayBean.setToTimeFormatted(saturdayObject.getString("to_time_formatted"));
                                    peakRateBean.setSaturday(saturdayBean);

                                    sundayThursdayObject = peakRateObject.getJSONObject("sun_thu");
                                    sundayThursdayBean = new PitchPriceDetailBean();
                                    sundayThursdayBean.setPitchesId(sundayThursdayObject.getString("pitches_id"));
                                    sundayThursdayBean.setDay(sundayThursdayObject.getString("day"));
                                    sundayThursdayBean.setFromTime(sundayThursdayObject.getString("from_time"));
                                    sundayThursdayBean.setToTime(sundayThursdayObject.getString("to_time"));
                                    sundayThursdayBean.setHourPrice(sundayThursdayObject.getString("hour_price"));
                                    sundayThursdayBean.setPitchType(sundayThursdayObject.getString("pitch_type"));
                                    sundayThursdayBean.setHourType(sundayThursdayObject.getString("hour_type"));
                                    sundayThursdayBean.setFromTimeFormatted(sundayThursdayObject.getString("from_time_formatted"));
                                    sundayThursdayBean.setToTimeFormatted(sundayThursdayObject.getString("to_time_formatted"));
                                    peakRateBean.setSundayToThursday(sundayThursdayBean);

                                    simplePitchBean.setPeakRate(peakRateBean);

                                    pitchBean.setSimplePitch(simplePitchBean);
                                } else {
                                    pitchBean.setSimplePitch(null);
                                }

                                if(pitchPrices.has("full_pitch")){
                                    JSONObject fullPitchObject = pitchPrices.getJSONObject("full_pitch");
                                    PitchTypeBean fullPitchBean = new PitchTypeBean();

                                    JSONObject offRateObject = fullPitchObject.getJSONObject("off_rate");
                                    PitchTimeTypeBean offRateBean = new PitchTimeTypeBean();

                                    offRateBean.setTimeRange(offRateObject.getString("time_range"));

                                    JSONObject fridayObject = offRateObject.getJSONObject("friday");
                                    PitchPriceDetailBean fridayBean = new PitchPriceDetailBean();

                                    fridayBean.setPitchesId(fridayObject.getString("pitches_id"));
                                    fridayBean.setDay(fridayObject.getString("day"));
                                    fridayBean.setFromTime(fridayObject.getString("from_time"));
                                    fridayBean.setToTime(fridayObject.getString("to_time"));
                                    fridayBean.setHourPrice(fridayObject.getString("hour_price"));
                                    fridayBean.setPitchType(fridayObject.getString("pitch_type"));
                                    fridayBean.setHourType(fridayObject.getString("hour_type"));
                                    fridayBean.setFromTimeFormatted(fridayObject.getString("from_time_formatted"));
                                    fridayBean.setToTimeFormatted(fridayObject.getString("to_time_formatted"));
                                    offRateBean.setFriday(fridayBean);

                                    JSONObject saturdayObject = offRateObject.getJSONObject("sat");
                                    PitchPriceDetailBean saturdayBean = new PitchPriceDetailBean();
                                    saturdayBean.setPitchesId(saturdayObject.getString("pitches_id"));
                                    saturdayBean.setDay(saturdayObject.getString("day"));
                                    saturdayBean.setFromTime(saturdayObject.getString("from_time"));
                                    saturdayBean.setToTime(saturdayObject.getString("to_time"));
                                    saturdayBean.setHourPrice(saturdayObject.getString("hour_price"));
                                    saturdayBean.setPitchType(saturdayObject.getString("pitch_type"));
                                    saturdayBean.setHourType(saturdayObject.getString("hour_type"));
                                    saturdayBean.setFromTimeFormatted(saturdayObject.getString("from_time_formatted"));
                                    saturdayBean.setToTimeFormatted(saturdayObject.getString("to_time_formatted"));
                                    offRateBean.setSaturday(saturdayBean);

                                    JSONObject sundayThursdayObject = offRateObject.getJSONObject("sun_thu");
                                    PitchPriceDetailBean sundayThursdayBean = new PitchPriceDetailBean();
                                    sundayThursdayBean.setPitchesId(sundayThursdayObject.getString("pitches_id"));
                                    sundayThursdayBean.setDay(sundayThursdayObject.getString("day"));
                                    sundayThursdayBean.setFromTime(sundayThursdayObject.getString("from_time"));
                                    sundayThursdayBean.setToTime(sundayThursdayObject.getString("to_time"));
                                    sundayThursdayBean.setHourPrice(sundayThursdayObject.getString("hour_price"));
                                    sundayThursdayBean.setPitchType(sundayThursdayObject.getString("pitch_type"));
                                    sundayThursdayBean.setHourType(sundayThursdayObject.getString("hour_type"));
                                    sundayThursdayBean.setFromTimeFormatted(sundayThursdayObject.getString("from_time_formatted"));
                                    sundayThursdayBean.setToTimeFormatted(sundayThursdayObject.getString("to_time_formatted"));
                                    offRateBean.setSundayToThursday(sundayThursdayBean);

                                    fullPitchBean.setOffRate(offRateBean);

                                    JSONObject normalRateObject = fullPitchObject.getJSONObject("normal_rate");
                                    PitchTimeTypeBean normalRateBean = new PitchTimeTypeBean();

                                    normalRateBean.setTimeRange(normalRateObject.getString("time_range"));

                                    fridayObject = normalRateObject.getJSONObject("friday");
                                    fridayBean = new PitchPriceDetailBean();

                                    fridayBean.setPitchesId(fridayObject.getString("pitches_id"));
                                    fridayBean.setDay(fridayObject.getString("day"));
                                    fridayBean.setFromTime(fridayObject.getString("from_time"));
                                    fridayBean.setToTime(fridayObject.getString("to_time"));
                                    fridayBean.setHourPrice(fridayObject.getString("hour_price"));
                                    fridayBean.setPitchType(fridayObject.getString("pitch_type"));
                                    fridayBean.setHourType(fridayObject.getString("hour_type"));
                                    fridayBean.setFromTimeFormatted(fridayObject.getString("from_time_formatted"));
                                    fridayBean.setToTimeFormatted(fridayObject.getString("to_time_formatted"));
                                    normalRateBean.setFriday(fridayBean);

                                    saturdayObject = normalRateObject.getJSONObject("sat");
                                    saturdayBean = new PitchPriceDetailBean();
                                    saturdayBean.setPitchesId(saturdayObject.getString("pitches_id"));
                                    saturdayBean.setDay(saturdayObject.getString("day"));
                                    saturdayBean.setFromTime(saturdayObject.getString("from_time"));
                                    saturdayBean.setToTime(saturdayObject.getString("to_time"));
                                    saturdayBean.setHourPrice(saturdayObject.getString("hour_price"));
                                    saturdayBean.setPitchType(saturdayObject.getString("pitch_type"));
                                    saturdayBean.setHourType(saturdayObject.getString("hour_type"));
                                    saturdayBean.setFromTimeFormatted(saturdayObject.getString("from_time_formatted"));
                                    saturdayBean.setToTimeFormatted(saturdayObject.getString("to_time_formatted"));
                                    normalRateBean.setSaturday(saturdayBean);

                                    sundayThursdayObject = normalRateObject.getJSONObject("sun_thu");
                                    sundayThursdayBean = new PitchPriceDetailBean();
                                    sundayThursdayBean.setPitchesId(sundayThursdayObject.getString("pitches_id"));
                                    sundayThursdayBean.setDay(sundayThursdayObject.getString("day"));
                                    sundayThursdayBean.setFromTime(sundayThursdayObject.getString("from_time"));
                                    sundayThursdayBean.setToTime(sundayThursdayObject.getString("to_time"));
                                    sundayThursdayBean.setHourPrice(sundayThursdayObject.getString("hour_price"));
                                    sundayThursdayBean.setPitchType(sundayThursdayObject.getString("pitch_type"));
                                    sundayThursdayBean.setHourType(sundayThursdayObject.getString("hour_type"));
                                    sundayThursdayBean.setFromTimeFormatted(sundayThursdayObject.getString("from_time_formatted"));
                                    sundayThursdayBean.setToTimeFormatted(sundayThursdayObject.getString("to_time_formatted"));
                                    normalRateBean.setSundayToThursday(sundayThursdayBean);

                                    fullPitchBean.setNormalRate(normalRateBean);

                                    JSONObject peakRateObject = fullPitchObject.getJSONObject("peak_rate");
                                    PitchTimeTypeBean peakRateBean = new PitchTimeTypeBean();

                                    peakRateBean.setTimeRange(peakRateObject.getString("time_range"));

                                    fridayObject = peakRateObject.getJSONObject("friday");
                                    fridayBean = new PitchPriceDetailBean();

                                    fridayBean.setPitchesId(fridayObject.getString("pitches_id"));
                                    fridayBean.setDay(fridayObject.getString("day"));
                                    fridayBean.setFromTime(fridayObject.getString("from_time"));
                                    fridayBean.setToTime(fridayObject.getString("to_time"));
                                    fridayBean.setHourPrice(fridayObject.getString("hour_price"));
                                    fridayBean.setPitchType(fridayObject.getString("pitch_type"));
                                    fridayBean.setHourType(fridayObject.getString("hour_type"));
                                    fridayBean.setFromTimeFormatted(fridayObject.getString("from_time_formatted"));
                                    fridayBean.setToTimeFormatted(fridayObject.getString("to_time_formatted"));
                                    peakRateBean.setFriday(fridayBean);

                                    saturdayObject = peakRateObject.getJSONObject("sat");
                                    saturdayBean = new PitchPriceDetailBean();
                                    saturdayBean.setPitchesId(saturdayObject.getString("pitches_id"));
                                    saturdayBean.setDay(saturdayObject.getString("day"));
                                    saturdayBean.setFromTime(saturdayObject.getString("from_time"));
                                    saturdayBean.setToTime(saturdayObject.getString("to_time"));
                                    saturdayBean.setHourPrice(saturdayObject.getString("hour_price"));
                                    saturdayBean.setPitchType(saturdayObject.getString("pitch_type"));
                                    saturdayBean.setHourType(saturdayObject.getString("hour_type"));
                                    saturdayBean.setFromTimeFormatted(saturdayObject.getString("from_time_formatted"));
                                    saturdayBean.setToTimeFormatted(saturdayObject.getString("to_time_formatted"));
                                    peakRateBean.setSaturday(saturdayBean);

                                    sundayThursdayObject = peakRateObject.getJSONObject("sun_thu");
                                    sundayThursdayBean = new PitchPriceDetailBean();
                                    sundayThursdayBean.setPitchesId(sundayThursdayObject.getString("pitches_id"));
                                    sundayThursdayBean.setDay(sundayThursdayObject.getString("day"));
                                    sundayThursdayBean.setFromTime(sundayThursdayObject.getString("from_time"));
                                    sundayThursdayBean.setToTime(sundayThursdayObject.getString("to_time"));
                                    sundayThursdayBean.setHourPrice(sundayThursdayObject.getString("hour_price"));
                                    sundayThursdayBean.setPitchType(sundayThursdayObject.getString("pitch_type"));
                                    sundayThursdayBean.setHourType(sundayThursdayObject.getString("hour_type"));
                                    sundayThursdayBean.setFromTimeFormatted(sundayThursdayObject.getString("from_time_formatted"));
                                    sundayThursdayBean.setToTimeFormatted(sundayThursdayObject.getString("to_time_formatted"));
                                    peakRateBean.setSundayToThursday(sundayThursdayBean);

                                    fullPitchBean.setPeakRate(peakRateBean);

                                    pitchBean.setFullPitch(fullPitchBean);
                                } else {
                                    pitchBean.setFullPitch(null);
                                }*/

                                pitchBeenList.add(pitchBean);
                            }

                            clickedOnFacility.setPitchesList(pitchBeenList);

                            updateUI();

                        } else {
                            Toast.makeText(ParentFacilityDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentFacilityDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void changeFonts(){
        title.setTypeface(linoType);
        facilityName.setTypeface(linoType);
        facilityDescription.setTypeface(helvetica);
        lblLocations.setTypeface(helvetica);
        facilityLocation.setTypeface(helvetica);
    }

}