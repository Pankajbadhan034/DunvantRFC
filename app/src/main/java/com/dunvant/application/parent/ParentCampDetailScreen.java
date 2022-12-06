package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CampBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.CampGalleryBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.CampSessionBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentSessionsListingAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParentCampDetailScreen extends AppCompatActivity implements IWebServiceCallback, OnMapReadyCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    ImageView campImage;
    TextView picCount;
    TextView campName;
    TextView campDescription;
    TextView lblLocations;
    TextView locationName;
    TextView lblDates;
    TextView dates;
    TextView lblDays;
    TextView days;
    TextView ageGroup;
    TextView lblAdditionalInformation;
    TextView additionalInformation;
    TextView rules;
    TextView lblRules;
    ListView sessionsListView;

    CampBean clickedOnCamp;
    int locationPosition;

    private final String GET_CAMP_DETAIL = "GET_CAMP_DETAIL";

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_camp_detail_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        campImage = (ImageView) findViewById(R.id.campImage);
        picCount = (TextView) findViewById(R.id.picCount);
        campName = (TextView) findViewById(R.id.campName);
        campDescription = (TextView) findViewById(R.id.campDescription);
        lblLocations = (TextView) findViewById(R.id.lblLocations);
        locationName = (TextView) findViewById(R.id.locationName);
        lblDates = (TextView) findViewById(R.id.lblDates);
        dates = (TextView) findViewById(R.id.dates);
        lblDays = (TextView) findViewById(R.id.lblDays);
        days = (TextView) findViewById(R.id.days);
        ageGroup = (TextView) findViewById(R.id.ageGroup);
        lblAdditionalInformation = (TextView) findViewById(R.id.lblAdditionalInformation);
        additionalInformation = (TextView) findViewById(R.id.additionalInformation);
        lblRules = (TextView) findViewById(R.id.lblRules);
        rules = (TextView) findViewById(R.id.rules);
        sessionsListView = (ListView) findViewById(R.id.sessionsListView);

        changeFonts();

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentCampDetailScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        if (intent != null) {
            clickedOnCamp = (CampBean) intent.getSerializableExtra("clickedOnCamp");
            locationPosition = intent.getIntExtra("locationPosition", -1);

            getCampDetail();
        }

        /*sessionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent bookCampScreen = new Intent(ParentCampDetailScreen.this, ParentBookCampScreenOLD.class);
                Intent bookCampScreen = new Intent(ParentCampDetailScreen.this, ParentBookCampScreenNew.class);
                bookCampScreen.putExtra("clickedOnCamp", clickedOnCamp);
                bookCampScreen.putExtra("sessionPosition", position);
                bookCampScreen.putExtra("locationPosition", locationPosition);
                startActivity(bookCampScreen);
            }
        });*/

        campImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent galleryScreen = new Intent(ParentCampDetailScreen.this, ParentCampGalleryScreen.class);
                galleryScreen.putExtra("clickedOnCamp", clickedOnCamp);
                startActivity(galleryScreen);*/

                Intent viewImageInFullScreen = new Intent(ParentCampDetailScreen.this, ParentViewImageInFullScreen.class);
                viewImageInFullScreen.putExtra("imageUrl", clickedOnCamp.getFilePath());
                startActivity(viewImageInFullScreen);
            }
        });

        picCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickedOnCamp.getGalleryList().size() == 0) {
                    Toast.makeText(ParentCampDetailScreen.this, "No Photo Found", Toast.LENGTH_SHORT).show();
                } else {
                    Intent galleryScreen = new Intent(ParentCampDetailScreen.this, ParentCampGalleryScreen.class);
                    galleryScreen.putExtra("clickedOnCamp", clickedOnCamp);
                    startActivity(galleryScreen);
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showCampDetails() {
        imageLoader.displayImage(clickedOnCamp.getFilePath(), campImage, options);

        /*if(clickedOnCamp.getGalleryList() != null && clickedOnCamp.getGalleryList().size() > 0){
            imageLoader.displayImage(clickedOnCamp.getGalleryList().get(0).getFilePath(), campImage, options);
        } else {
            imageLoader.displayImage(clickedOnCamp.getFilePath(), campImage, options);
        }*/

        picCount.setText(clickedOnCamp.getGalleryList().size() + " PHOTOS");
        campName.setText(clickedOnCamp.getCampName());
        campDescription.setText(Html.fromHtml(clickedOnCamp.getCampDescription()));
        locationName.setText(clickedOnCamp.getLocationList().get(0).getLocationName());
        dates.setText(clickedOnCamp.getShowFromDate() + " - " + clickedOnCamp.getShowToDate());
        ageGroup.setText(clickedOnCamp.getSessionsList().get(0).getGroupName());

        String strDays = "";
        for (CampDaysBean campDaysBean : clickedOnCamp.getDaysList()) {
            strDays += campDaysBean.getDayLabel() + ",";
        }
        if (strDays != null && strDays.length() > 0 && strDays.charAt(strDays.length() - 1) == ',') {
            strDays = strDays.substring(0, strDays.length() - 1);
        }
        days.setText(strDays);

        additionalInformation.setText(clickedOnCamp.getAdditionalInformation());
        rules.setText(clickedOnCamp.getRules());
        sessionsListView.setAdapter(new ParentSessionsListingAdapter(ParentCampDetailScreen.this, clickedOnCamp.getSessionsList(), clickedOnCamp, locationPosition));
        Utilities.setListViewHeightBasedOnChildren(sessionsListView);

        LatLng position = new LatLng(Double.parseDouble(clickedOnCamp.getLocationList().get(0).getLatitude()), Double.parseDouble(clickedOnCamp.getLocationList().get(0).getLongitude()));

//        mMap.addMarker(new MarkerOptions()
//                .position(position)
//                .title(clickedOnCamp.getCampName()));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }

    private void getCampDetail() {
        if (Utilities.isNetworkAvailable(ParentCampDetailScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("camps_id", clickedOnCamp.getCampId()));
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnCamp.getLocationList().get(locationPosition).getLocationId()));

            String webServiceUrl = Utilities.BASE_URL + "camps/detail";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentCampDetailScreen.this, nameValuePairList, GET_CAMP_DETAIL, ParentCampDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentCampDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_CAMP_DETAIL:

                if (response == null) {
                    Toast.makeText(ParentCampDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONObject campObject = responseObject.getJSONObject("data");
                            clickedOnCamp.setCampId(campObject.getString("id"));
                            clickedOnCamp.setAcademiesId(campObject.getString("academies_id"));
                            clickedOnCamp.setCampName(campObject.getString("camp_name"));
                            clickedOnCamp.setCampDescription(campObject.getString("camp_desc"));
                            clickedOnCamp.setFromDate(campObject.getString("from_date"));
                            clickedOnCamp.setToDate(campObject.getString("to_date"));
                            clickedOnCamp.setShowFromDate(campObject.getString("show_from_date"));
                            clickedOnCamp.setShowToDate(campObject.getString("show_to_date"));
                            clickedOnCamp.setAdditionalInformation(campObject.getString("additional_information"));
                            clickedOnCamp.setRules(campObject.getString("rules"));
                            clickedOnCamp.setCampIsLocked(campObject.getString("camp_is_locked"));
                            clickedOnCamp.setCampState(campObject.getString("camp_state"));
                            clickedOnCamp.setFileTitle(campObject.getString("file_title"));
                            clickedOnCamp.setFilePath(campObject.getString("file_path"));

                            JSONArray galleryArray = campObject.getJSONArray("camp_gallery");
                            ArrayList<CampGalleryBean> galleryList = new ArrayList<>();
                            CampGalleryBean galleryBean;

                            for (int j = 0; j < galleryArray.length(); j++) {
                                JSONObject galleryObject = galleryArray.getJSONObject(j);
                                galleryBean = new CampGalleryBean();

                                galleryBean.setGalleryId(galleryObject.getString("camp_gallery_id"));
                                galleryBean.setFileTitle(galleryObject.getString("file_title"));
                                galleryBean.setFilePath(galleryObject.getString("file_path"));

                                galleryList.add(galleryBean);
                            }
                            clickedOnCamp.setGalleryList(galleryList);

                            JSONArray locationsArray = campObject.getJSONArray("camp_to_locations");
                            ArrayList<CampLocationBean> locationsList = new ArrayList<>();
                            CampLocationBean locationBean;
                            for (int j = 0; j < locationsArray.length(); j++) {
                                JSONObject locationObject = locationsArray.getJSONObject(j);
                                locationBean = new CampLocationBean();

                                locationBean.setLocationId(locationObject.getString("locations_id"));
                                locationBean.setLocationName(locationObject.getString("l_name"));
                                locationBean.setLatitude(locationObject.getString("latitude"));
                                locationBean.setLongitude(locationObject.getString("longitude"));

                                locationsList.add(locationBean);
                            }
                            clickedOnCamp.setLocationList(locationsList);

                            JSONArray campDaysArray = campObject.getJSONArray("camp_days");
                            ArrayList<CampDaysBean> daysList = new ArrayList<>();
                            CampDaysBean daysBean;
                            for (int j = 0; j < campDaysArray.length(); j++) {
                                JSONObject dayObject = campDaysArray.getJSONObject(j);
                                daysBean = new CampDaysBean();

                                daysBean.setCampId(dayObject.getString("camps_id"));
                                daysBean.setDay(dayObject.getString("day"));
                                daysBean.setDayLabel(dayObject.getString("day_label"));

                                daysList.add(daysBean);
                            }
                            clickedOnCamp.setDaysList(daysList);

                            JSONArray campSessionsArray = campObject.getJSONArray("camp_sessions");
                            ArrayList<CampSessionBean> sessionsList = new ArrayList<>();
                            CampSessionBean sessionBean;

                            for (int j = 0; j < campSessionsArray.length(); j++) {
                                JSONObject sessionObject = campSessionsArray.getJSONObject(j);
                                sessionBean = new CampSessionBean();

                                sessionBean.setSessionId(sessionObject.getString("camp_session_id"));
                                sessionBean.setFromTime(sessionObject.getString("from_time"));
                                sessionBean.setToTime(sessionObject.getString("to_time"));
                                sessionBean.setPerDayCost(sessionObject.getString("per_day_cost"));
                                sessionBean.setPerWeekCost(sessionObject.getString("per_week_cost"));
                                sessionBean.setGroupName(sessionObject.getString("groups_name"));
                                sessionBean.setShowFromTime(sessionObject.getString("show_from_time"));
                                sessionBean.setShowToTime(sessionObject.getString("show_to_time"));
                                sessionBean.setAvailability(sessionObject.getBoolean("availabilty"));

                                sessionsList.add(sessionBean);
                            }
                            clickedOnCamp.setSessionsList(sessionsList);

                            showCampDetails();

                        } else {
                            Toast.makeText(ParentCampDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentCampDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent mapScreen = new Intent(ParentCampDetailScreen.this, ParentCampMapScreen.class);
                mapScreen.putExtra("clickedOnCamp", clickedOnCamp);
                startActivity(mapScreen);
            }
        });
    }

    private void changeFonts() {
        title.setTypeface(linoType);
        picCount.setTypeface(linoType);
        campName.setTypeface(helvetica);
        campDescription.setTypeface(helvetica);
        lblLocations.setTypeface(linoType);
        locationName.setTypeface(helvetica);
        lblDates.setTypeface(linoType);
        dates.setTypeface(helvetica);
        lblDays.setTypeface(linoType);
        days.setTypeface(helvetica);
        lblAdditionalInformation.setTypeface(linoType);
        additionalInformation.setTypeface(helvetica);
        lblRules.setTypeface(linoType);
        rules.setTypeface(helvetica);
    }

}