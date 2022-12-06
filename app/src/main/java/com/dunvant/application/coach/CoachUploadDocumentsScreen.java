package com.dunvant.application.coach;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.DocumentBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachChildNamesSpinnerAdapter;
import com.dunvant.application.coach.adapters.CoachLocationListingAdapter;
import com.dunvant.application.coach.adapters.CoachOnlyAgeGroupListingAdapter;
import com.dunvant.application.coach.adapters.CoachSessionListingAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CoachUploadDocumentsScreen extends AppCompatActivity implements IWebServiceCallback {

    JSONArray childIdArray;
    boolean isEditMode = false;
    DocumentBean documentToEdit;
    DocumentBean documentBean;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    ImageView backButton;
    Spinner locationSpinner;
    Spinner sessionSpinner;
    Spinner ageGroupSpinner;
    Spinner childrenSpinner;
    EditText docTitle;
    TextView showTitle;
    TextView browse;
    TextView addedList;
    TextView submit;
    EditText comment;
    ImageView thumbnail;

    private final String GET_LOCATION_LISTING = "GET_LOCATION_LISTING";
    private final String GET_SESSIONS_LISTING = "GET_SESSIONS_LISTING";
    private final String GET_AGE_GROUP_LISTING = "GET_AGE_GROUP_LISTING";
    private final String GET_ALL_CHILDREN_LISTING = "GET_ALL_CHILDREN_LISTING";

    ArrayList<CampLocationBean> locationsList = new ArrayList<>();
    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupBean> ageGroupsListing = new ArrayList<>();
    ArrayList<ChildBean> childrenListing = new ArrayList<>();

    CoachLocationListingAdapter coachLocationListingAdapter;
    CoachSessionListingAdapter coachSessionListingAdapter;
    CoachOnlyAgeGroupListingAdapter coachOnlyAgeGroupListingAdapter;
    CoachChildNamesSpinnerAdapter coachChildNamesSpinnerAdapter;

    String clickedOnLocationIds;
    String clickedOnDayIds;
    String clickedOnSessionIds;
    String clickedOnChildIds;
    String strDocTitle;
    String strComment;

    private final int CHOOSE_IMAGE_FROM_GALLERY = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 2;
    public static final int MEDIA_TYPE_VIDEO = 3;
    private final int CHOOSE_IMAGE = 10;
    private Uri fileUri = null;
    Uri selectedUri;
    String selectedImagePath;

    boolean isDocSelected = false;

    // Run time permission
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_activity_upload_documents_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        backButton = (ImageView) findViewById(R.id.backButton);
        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        sessionSpinner = (Spinner) findViewById(R.id.sessionSpinner);
        ageGroupSpinner = (Spinner) findViewById(R.id.ageGroupSpinner);
        childrenSpinner = (Spinner) findViewById(R.id.childrenSpinner);
        locationSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        sessionSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        childrenSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ageGroupSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        docTitle = (EditText) findViewById(R.id.docTitle);
        comment = (EditText) findViewById(R.id.comment);
        showTitle = (TextView) findViewById(R.id.showTitle);
        browse = (TextView) findViewById(R.id.browse);
        submit = (TextView) findViewById(R.id.submit);
        addedList = (TextView) findViewById(R.id.addedList);
        thumbnail = findViewById(R.id.thumbnail);

        imageLoader.init(ImageLoaderConfiguration.createDefault(CoachUploadDocumentsScreen.this));

        getLocationListing();

        addedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent = getIntent();
        if (intent != null) {
            isEditMode = intent.getBooleanExtra("isEditMode", false);

            if (isEditMode) {
                documentToEdit = (DocumentBean) intent.getSerializableExtra("documentToEdit");

                docTitle.setText(documentToEdit.getTitle());
                comment.setText(documentToEdit.getComments());
                showTitle.setText(documentToEdit.getFileName());

                documentBean = null;
                selectedUri = null;
                fileUri = null;

                submit.setText("Update");
            }
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utilities.isNetworkAvailable(CoachUploadDocumentsScreen.this)) {
                    Toast.makeText(CoachUploadDocumentsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    return;
                }

                int locationPosition = locationSpinner.getSelectedItemPosition();
                int sessionPosition = sessionSpinner.getSelectedItemPosition();
                int ageGroupPosition = ageGroupSpinner.getSelectedItemPosition();
                int childrenPosition = childrenSpinner.getSelectedItemPosition();
                strComment = comment.getText().toString().trim();
                strDocTitle = docTitle.getText().toString().trim();
                if (strDocTitle == null || strDocTitle.isEmpty()) {
                    Toast.makeText(CoachUploadDocumentsScreen.this, "Please write Title", Toast.LENGTH_SHORT).show();
                } else if (locationPosition == 0) {
                    Toast.makeText(CoachUploadDocumentsScreen.this, "Please select Location", Toast.LENGTH_SHORT).show();
                } else if (sessionPosition == 0) {
                    Toast.makeText(CoachUploadDocumentsScreen.this, "Please select Session", Toast.LENGTH_SHORT).show();
                } else if (ageGroupPosition == 0) {
                    Toast.makeText(CoachUploadDocumentsScreen.this, "Please select Age Group", Toast.LENGTH_SHORT).show();
                } else if (childrenPosition == 0) {
                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                    Toast.makeText(CoachUploadDocumentsScreen.this, "Please select "+verbiage_plural, Toast.LENGTH_SHORT).show();

                } else if (isEditMode) {
                    new EditFileAsync(documentBean, CoachUploadDocumentsScreen.this).execute();
                } else if (strComment == null || strComment.isEmpty()) {
                    Toast.makeText(CoachUploadDocumentsScreen.this, "Please write Comment", Toast.LENGTH_SHORT).show();
                } else if (!isDocSelected) {
                    Toast.makeText(CoachUploadDocumentsScreen.this, "Please select the Document", Toast.LENGTH_SHORT).show();
                } else {
                    new UploadFileAsync(documentBean, CoachUploadDocumentsScreen.this).execute();
                }
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, CHOOSE_IMAGE_FROM_GALLERY);*/

                final String[] items = new String[]{"Take from camera", "Select from gallery"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CoachUploadDocumentsScreen.this, android.R.layout.select_dialog_item, items);
                AlertDialog.Builder builder = new AlertDialog.Builder(CoachUploadDocumentsScreen.this);

                builder.setTitle("Choose");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == 0) {

                            // Code to get image
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);


                        } else {
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                            i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            startActivityForResult(i, CHOOSE_IMAGE);

                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    daysListing.clear();

                    // 0th Element Choose Session
                    CampDaysBean daysBean = new CampDaysBean();
                    daysBean.setDay("-1");
                    daysBean.setDayLabel("Choose Session");

                    daysListing.add(daysBean);

                    coachSessionListingAdapter = new CoachSessionListingAdapter(CoachUploadDocumentsScreen.this, daysListing);
                    sessionSpinner.setAdapter(coachSessionListingAdapter);
                } /*else if (position == 1) {

                    clickedOnLocationIds = "";
                    for(CampLocationBean locationBean : locationsList) {

                        if(Integer.parseInt(locationBean.getLocationId()) > 0) {
                            clickedOnLocationIds += locationBean.getLocationId()+",";
                        }

                    }

                    if (clickedOnLocationIds != null && clickedOnLocationIds.length() > 0 && clickedOnLocationIds.charAt(clickedOnLocationIds.length()-1)==',') {
                        clickedOnLocationIds = clickedOnLocationIds.substring(0, clickedOnLocationIds.length()-1);
                    }

                    getSessionDaysListing();

                }*/ else {
//                    clickedOnLocation = locationsList.get(position);

                    clickedOnLocationIds = locationsList.get(position).getLocationId();

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

                    // 0th Element Choose Session
                    AgeGroupBean ageGroupBean = new AgeGroupBean();
                    ageGroupBean.setAgeGroupId("-1");
                    ageGroupBean.setGroupName("Choose Age Group");

                    ageGroupsListing.add(ageGroupBean);

                    coachOnlyAgeGroupListingAdapter = new CoachOnlyAgeGroupListingAdapter(CoachUploadDocumentsScreen.this, ageGroupsListing);
                    ageGroupSpinner.setAdapter(coachOnlyAgeGroupListingAdapter);
                } /*else if (position == 1) {

                    clickedOnDayIds = "";
                    for(CampDaysBean campDaysBean : daysListing) {
                        if(Integer.parseInt(campDaysBean.getDay()) >= 0) {
                            clickedOnDayIds += campDaysBean.getDay()+",";
                        }
                    }

                    if (clickedOnDayIds != null && clickedOnDayIds.length() > 0 && clickedOnDayIds.charAt(clickedOnDayIds.length()-1)==',') {
                        clickedOnDayIds = clickedOnDayIds.substring(0, clickedOnDayIds.length()-1);
                    }
                    getAgeGroupsListing();

                }*/ else {
//                    clickedOnDay = daysListing.get(position);
                    clickedOnDayIds = daysListing.get(position).getDay();
                    getAgeGroupsListing();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ageGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    childrenListing.clear();

                    ChildBean childBean = new ChildBean();
                    childBean.setId("-1");

                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                    childBean.setFullName("Choose "+verbiage_singular);

                    childrenListing.add(childBean);

                    coachChildNamesSpinnerAdapter = new CoachChildNamesSpinnerAdapter(CoachUploadDocumentsScreen.this, childrenListing);
                    childrenSpinner.setAdapter(coachChildNamesSpinnerAdapter);

                } /*else if (position == 1) {

                    clickedOnSessionIds = "";
                    for(AgeGroupBean ageGroupBean : ageGroupsListing) {

                        if(Integer.parseInt(ageGroupBean.getSessionId()) > 0) {
                            clickedOnSessionIds += ageGroupBean.getSessionId()+",";
                        }

                    }

                    if (clickedOnSessionIds != null && clickedOnSessionIds.length() > 0 && clickedOnSessionIds.charAt(clickedOnSessionIds.length()-1)==',') {
                        clickedOnSessionIds = clickedOnSessionIds.substring(0, clickedOnSessionIds.length()-1);
                    }
                    getAllChildrenListing();

                }*/ else {
//                    clickedOnAgeGroup = ageGroupsListing.get(position);

                    clickedOnSessionIds = ageGroupsListing.get(position).getSessionId();

                    getAllChildrenListing();
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

                    /*categoriesListing.clear();

                    ChallengeCategoryBean categoryBean = new ChallengeCategoryBean();
                    categoryBean.setCategoryId("-1");
                    categoryBean.setName("Choose Category");
                    categoriesListing.add(categoryBean);

                    coachChallengeCategorySpinnerAdapter = new CoachChallengeCategorySpinnerAdapter(CoachCreatePostScreen.this, categoriesListing);
                    categorySpinner.setAdapter(coachChallengeCategorySpinnerAdapter);*/

                } else if (position == 1) {
                    clickedOnChildIds = "";
                    try {
                        childIdArray = new JSONArray();
                        for (int i = 2; i < childrenListing.size(); i++) {
                            childIdArray.put(childrenListing.get(i).getId());
                        }
                    } catch (Exception e) {
                        Toast.makeText(CoachUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                } else {
                    try {
                        childIdArray = new JSONArray();
                        childIdArray.put(childrenListing.get(position).getId());
                    } catch (Exception e) {
                        Toast.makeText(CoachUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissions()) {
//                Toast.makeText(ChildTrackWorkoutScreen.this, "Permissions already granted", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(ChildTrackWorkoutScreen.this, "Initially permission not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(CoachUploadDocumentsScreen.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                } else {
                    // no permissions granted.
                    Toast.makeText(CoachUploadDocumentsScreen.this, "This feature cannot work without Permissions. \nTry Again and allow permissions or allow permissions in Applications Settings", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }

    private void getLocationListing() {
        if (Utilities.isNetworkAvailable(CoachUploadDocumentsScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "coach/locations_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(CoachUploadDocumentsScreen.this, GET_LOCATION_LISTING, CoachUploadDocumentsScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachUploadDocumentsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getSessionDaysListing() {
        if (Utilities.isNetworkAvailable(CoachUploadDocumentsScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_ids", clickedOnLocationIds));
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocationIds));

//            String webServiceUrl = Utilities.BASE_URL + "coach/all_session_days_list";
            String webServiceUrl = Utilities.BASE_URL + "coach/session_days_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachUploadDocumentsScreen.this, nameValuePairList, GET_SESSIONS_LISTING, CoachUploadDocumentsScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachUploadDocumentsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAgeGroupsListing() {
        if (Utilities.isNetworkAvailable(CoachUploadDocumentsScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocationIds));
            nameValuePairList.add(new BasicNameValuePair("locations_ids", clickedOnLocationIds));
            nameValuePairList.add(new BasicNameValuePair("session_days", clickedOnDayIds));

//            String webServiceUrl = Utilities.BASE_URL + "coach/all_age_group_list";
            String webServiceUrl = Utilities.BASE_URL + "coach/age_group_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachUploadDocumentsScreen.this, nameValuePairList, GET_AGE_GROUP_LISTING, CoachUploadDocumentsScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachUploadDocumentsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllChildrenListing() {
        if (Utilities.isNetworkAvailable(CoachUploadDocumentsScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("sessions_ids", clickedOnSessionIds));

            // adding below new params
            /*nameValuePairList.add(new BasicNameValuePair("session_day", clickedOnDayIds));
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocationIds));*/

            String webServiceUrl = Utilities.BASE_URL + "coach/all_children_booked_session_list";
//            String webServiceUrl = Utilities.BASE_URL + "coach/children_booked_session_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachUploadDocumentsScreen.this, nameValuePairList, GET_ALL_CHILDREN_LISTING, CoachUploadDocumentsScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachUploadDocumentsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_LOCATION_LISTING:

                locationsList.clear();

                if (response == null) {
                    Toast.makeText(CoachUploadDocumentsScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();

                    // 0th Element Choose Location
                    CampLocationBean locationBean = new CampLocationBean();
                    locationBean.setLocationId("-1");
                    locationBean.setLocationName("Choose Location");

                    locationsList.add(locationBean);
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

                            /*locationBean = new CampLocationBean();
                            locationBean.setLocationId("-2");
                            locationBean.setLocationName("All");

                            locationsList.add(locationBean);*/

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
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

                            // 0th Element Choose Location
                            CampLocationBean locationBean = new CampLocationBean();
                            locationBean.setLocationId("-1");
                            locationBean.setLocationName("Choose Location");

                            locationsList.add(locationBean);

                            Toast.makeText(CoachUploadDocumentsScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachLocationListingAdapter = new CoachLocationListingAdapter(CoachUploadDocumentsScreen.this, locationsList);
                locationSpinner.setAdapter(coachLocationListingAdapter);
                break;

            case GET_SESSIONS_LISTING:

                daysListing.clear();

                if (response == null) {
                    Toast.makeText(CoachUploadDocumentsScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();

                    // 0th Element Choose Session
                    CampDaysBean daysBean = new CampDaysBean();
                    daysBean.setDay("-1");
                    daysBean.setDayLabel("Choose Session");

                    daysListing.add(daysBean);
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            // 0th Element Choose Session
                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Choose Session");

                            daysListing.add(daysBean);

                            /*daysBean = new CampDaysBean();
                            daysBean.setDay("-2");
                            daysBean.setDayLabel("All");

                            daysListing.add(daysBean);*/

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject daysObject = dataArray.getJSONObject(i);
                                daysBean = new CampDaysBean();
                                daysBean.setDay(daysObject.getString("day"));
                                daysBean.setDayLabel(daysObject.getString("day_label"));

                                daysListing.add(daysBean);
                            }

                        } else {
                            Toast.makeText(CoachUploadDocumentsScreen.this, message, Toast.LENGTH_SHORT).show();

                            // 0th Element Choose Session
                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Choose Session");

                            daysListing.add(daysBean);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachSessionListingAdapter = new CoachSessionListingAdapter(CoachUploadDocumentsScreen.this, daysListing);
                sessionSpinner.setAdapter(coachSessionListingAdapter);

                break;

            case GET_AGE_GROUP_LISTING:

                ageGroupsListing.clear();

                if (response == null) {
                    Toast.makeText(CoachUploadDocumentsScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();

                    // 0th Element Choose Age Group

                    AgeGroupBean ageGroupBean = new AgeGroupBean();
                    ageGroupBean.setAgeGroupId("-1");
                    ageGroupBean.setSessionId("-1");
                    ageGroupBean.setGroupName("Choose Age Group");

                    ageGroupsListing.add(ageGroupBean);
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            // 0th Element Choose Age Group

                            AgeGroupBean ageGroupBean = new AgeGroupBean();
                            ageGroupBean.setAgeGroupId("-1");
                            ageGroupBean.setSessionId("-1");
                            ageGroupBean.setGroupName("Choose Age Group");

                            ageGroupsListing.add(ageGroupBean);

                            /*ageGroupBean = new AgeGroupBean();
                            ageGroupBean.setAgeGroupId("-2");
                            ageGroupBean.setSessionId("-2");
                            ageGroupBean.setGroupName("All");

                            ageGroupsListing.add(ageGroupBean);*/

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject ageGroupObject = dataArray.getJSONObject(i);
                                ageGroupBean = new AgeGroupBean();
                                ageGroupBean.setSessionId(ageGroupObject.getString("sessions_id"));
                                ageGroupBean.setAgeGroupId(ageGroupObject.getString("age_groups_id"));
                                ageGroupBean.setGroupName(ageGroupObject.getString("group_name"));

                                ageGroupsListing.add(ageGroupBean);
                            }


                        } else {

                            // 0th Element Choose Age Group

                            AgeGroupBean ageGroupBean = new AgeGroupBean();
                            ageGroupBean.setAgeGroupId("-1");
                            ageGroupBean.setSessionId("-1");
                            ageGroupBean.setGroupName("Choose Age Group");

                            ageGroupsListing.add(ageGroupBean);

                            Toast.makeText(CoachUploadDocumentsScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachOnlyAgeGroupListingAdapter = new CoachOnlyAgeGroupListingAdapter(CoachUploadDocumentsScreen.this, ageGroupsListing);
                ageGroupSpinner.setAdapter(coachOnlyAgeGroupListingAdapter);

                break;

            case GET_ALL_CHILDREN_LISTING:

                childrenListing.clear();

                if (response == null) {
                    Toast.makeText(CoachUploadDocumentsScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();

                    ChildBean childBean = new ChildBean();
                    childBean.setId("-1");
//                    childBean.setFullName("Choose Child");
                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                    childBean.setFullName("Choose "+verbiage_singular);

                    childrenListing.add(childBean);
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            ChildBean childBean = new ChildBean();
                            childBean.setId("-1");
//                            childBean.setFullName("Choose Child");
                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                            childBean.setFullName("Choose "+verbiage_singular);

                            childrenListing.add(childBean);

                            childBean = new ChildBean();
                            childBean.setId("-2");
                            childBean.setFullName("All");
                            childrenListing.add(childBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject childObject = dataArray.getJSONObject(i);
                                childBean = new ChildBean();

                                childBean.setId(childObject.getString("id"));
                                childBean.setFullName(childObject.getString("full_name"));

                                childrenListing.add(childBean);
                            }

                        } else {
                            Toast.makeText(CoachUploadDocumentsScreen.this, message, Toast.LENGTH_SHORT).show();

                            ChildBean childBean = new ChildBean();
                            childBean.setId("-1");
//                            childBean.setFullName("Choose Child");
                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                            childBean.setFullName("Choose "+verbiage_singular);
                            childrenListing.add(childBean);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachChildNamesSpinnerAdapter = new CoachChildNamesSpinnerAdapter(CoachUploadDocumentsScreen.this, childrenListing);
                childrenSpinner.setAdapter(coachChildNamesSpinnerAdapter);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case CHOOSE_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        selectedUri = data.getData();
                        String[] filePathColumn = {MediaStore.Video.Media.DATA};
                        Cursor cursor = CoachUploadDocumentsScreen.this.getContentResolver().query(selectedUri, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        selectedImagePath = cursor.getString(columnIndex);
                        cursor.close();
                        File file = new File(selectedImagePath);
                        String nameFile = file.getName();
                        isDocSelected = true;
                        showTitle.setText(nameFile);

                        // showing thumbnail
                        thumbnail.setImageURI(selectedUri);
                        thumbnail.setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        Toast.makeText(CoachUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    try {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;

//                        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                        isDocSelected = true;
                        showTitle.setText(fileUri.getPath());

                        // showing thumbnail
                        thumbnail.setImageURI(fileUri);
                        thumbnail.setVisibility(View.VISIBLE);

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
//                    Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
                } else {
                    // failed to capture image
                    Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
                }
                break;

            /*case CHOOSE_IMAGE_FROM_GALLERY:
                try {
                    selectedUri = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = CoachUploadDocumentsScreen.this.getContentResolver().query(selectedUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedImagePath = cursor.getString(columnIndex);
                    cursor.close();
                    File file = new File(selectedImagePath);
                    String nameFile = file.getName();
                    showTitle.setText(nameFile);

                    isDocSelected = true;

                } catch (Exception e) {
                    Toast.makeText(CoachUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                break;

            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    try {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;

//                        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                        showTitle.setText(fileUri.getPath());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
//                    Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
                } else {
                    // failed to capture image
                    Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
                }
                break;

            case CAMERA_CAPTURE_VIDEO_REQUEST_CODE:

                if (resultCode == RESULT_OK) {
                    try {
//                        videoPreview.setVideoPath(fileUri.getPath());
//                        videoPreview.start();
                        showTitle.setText(fileUri.getPath());
                        //System.out.println("Video recorded");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "User cancelled video recording", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry! Failed to record video", Toast.LENGTH_SHORT).show();
                }

                break;*/

        }
    }

//    private class UpdateImageVideoAsync extends AsyncTask<Void, Void, String> {
//
//        ProgressDialog pDialog;
//        Context context;
//
//        public UpdateImageVideoAsync(Context context){
//            this.context = context;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            pDialog = Utilities.createProgressDialog(context);
//        }
//
//        @SuppressWarnings("deprecation")
//        @Override
//        protected String doInBackground(Void... voids) {
//
//            //System.out.println("Uploading starting");
//
//            String uploadUrl = Utilities.BASE_URL + "user_posts/create_new_timeline";
//            String strResponse = null;
//
//            HttpClient httpClient = new DefaultHttpClient();
//            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
//            HttpPost httpPost = new HttpPost(uploadUrl);
//
//            httpPost.addHeader("X-access-uid", loggedInUser.getId());
//            httpPost.addHeader("X-access-token", loggedInUser.getToken());
//
//            try {
//                StringBody titleBody = new StringBody(strDocTitle);
//                StringBody timeline_select_child = new StringBody(clickedOnChildIds);
//                StringBody comment = new StringBody(strComment);
//                StringBody documentId = new StringBody(doc);
//
//                //System.out.println("URL " + Utilities.BASE_URL + "account/upload_document");
//                //System.out.println("clicked on child ids " + clickedOnChildIds);
//
//                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//
//                builder.addPart("doc_title", titleBody);
//                builder.addPart("comments", comment);
//                builder.addPart("shared_with", timeline_select_child);
//
//
//                /*if(selectedUri != null) {
//                    String imageMime = getMimeType(selectedUri);
//                    File imageFile = new File(selectedImagePath);
//                    //System.out.println("MIME_type__"+imageMime+"--Path__"+selectedImagePath+"--FileName__"+imageFile.getName());
//                    FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");
//
//                    builder.addPart("file", imageFileBody);
//                }*/
//
//                if (selectedUri != null) {
//                    String imageMime = getMimeType(selectedUri);
//                    File imageFile = new File(selectedImagePath);
//                    //System.out.println("MIME_type__" + imageMime + "--Path__" + selectedImagePath + "--FileName__" + imageFile.getName());
//                    if(imageFile != null){
//                        FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");
//                        builder.addPart("file", imageFileBody);
//                    }
//
//                } else if (fileUri != null) {
//                    String fileMime = getMimeType(fileUri);
//                    File file = new File(fileUri.getPath());
//                    if(file != null){
//                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
//                        builder.addPart("file", imageFileBody);
//                    }
//                }
//
//                HttpEntity entity = builder.build();
//                httpPost.setEntity(entity);
//
//                HttpResponse response = httpClient.execute(httpPost);
//                HttpEntity resEntity = response.getEntity();
//
//                if (resEntity != null) {
//                    try {
//                        strResponse = EntityUtils.toString(resEntity).trim();
//                    } catch (ParseException e1) {
//                        e1.printStackTrace();
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                    }
//                }
//
//                //System.out.println("File upload end");
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                //System.out.println("Exception "+e.getMessage());
//            }
//
//            return strResponse;
//
//        }
//
//        @Override
//        protected void onPostExecute(String response) {
//
//            //System.out.println("Response "+response);
//
//            if(response == null) {
//                Toast.makeText(CoachUploadDocumentsScreen.this, "Could not connect server", Toast.LENGTH_SHORT).show();
//            } else {
//                try {
//                    JSONObject responseObject = new JSONObject(response);
//
//                    boolean status = responseObject.getBoolean("status");
//                    String message = responseObject.getString("message");
//
//                    Toast.makeText(CoachUploadDocumentsScreen.this, message, Toast.LENGTH_SHORT).show();
//
//                    if(status) {
//                        finish();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(CoachUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            if(pDialog != null && pDialog.isShowing()){
//                pDialog.dismiss();
//            }
//        }
//    }


    private class UploadFileAsync extends AsyncTask<Void, Void, String> {

        DocumentBean documentBean;
        ProgressDialog pDialog;
        Context context;

        public UploadFileAsync(DocumentBean documentBean, Context context) {
            this.documentBean = documentBean;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pDialog = Utilities.createProgressDialog(context);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... voids) {

            //System.out.println("Uploading starting");

            String uploadUrl = Utilities.BASE_URL + "account/upload_document";
            String strResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(uploadUrl);

            httpPost.addHeader("X-access-uid", loggedInUser.getId());
            httpPost.addHeader("X-access-token", loggedInUser.getToken());

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(context));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            try {

                //System.out.println("" + clickedOnChildIds);
                StringBody title = new StringBody(strDocTitle);
                StringBody comments = new StringBody(strComment);
                StringBody childId = new StringBody(childIdArray.toString());

                String mime = null;
                File file = null;

                if (fileUri == null) {
                    mime = getMimeType(documentBean.getUri());
                    file = new File(documentBean.getFilePath());
                } else {
                    mime = getMimeType(fileUri);
                    file = new File(fileUri.getPath());
                }

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();

                if (selectedUri != null) {
                    String imageMime = getMimeType(selectedUri);
                    File imageFile = new File(selectedImagePath);
                    //System.out.println("MIME_type__" + imageMime + "--Path__" + selectedImagePath + "--FileName__" + imageFile.getName());
                    if (imageFile != null) {
                        FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");
                        builder.addPart("file", imageFileBody);
                    }

                } else if (fileUri != null) {
                    String fileMime = getMimeType(fileUri);
                    file = new File(fileUri.getPath());
                    if (file != null) {
                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
                        builder.addPart("file", imageFileBody);
                    }
                }

                // builder.addPart("users_id", userId);
                builder.addPart("shared_with", childId);
                builder.addPart("doc_title", title);
                builder.addPart("comments", comments);


                HttpEntity entity = builder.build();

                httpPost.setEntity(entity);

                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity resEntity = response.getEntity();

                if (resEntity != null) {
                    try {
                        strResponse = EntityUtils.toString(resEntity).trim();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                //System.out.println("File upload end");

            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("Exception " + e.getMessage());
            }

            return strResponse;

        }

        @Override
        protected void onPostExecute(String response) {

            //System.out.println("Response " + response);

            if (response == null) {
                Toast.makeText(CoachUploadDocumentsScreen.this, "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    //System.out.println("RESPONSE:::" + responseObject.toString());

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    if (status) {
                        Toast.makeText(CoachUploadDocumentsScreen.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CoachUploadDocumentsScreen.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CoachUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    public String getMimeType(Uri uri) {
        String mimeType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = CoachUploadDocumentsScreen.this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(Environment.getExternalStorageDirectory(), "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(Environment.getExternalStorageDirectory(), "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private class EditFileAsync extends AsyncTask<Void, Void, String> {

        DocumentBean documentBean;
        ProgressDialog pDialog;
        Context context;

        public EditFileAsync(DocumentBean documentBean, Context context) {
            this.documentBean = documentBean;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pDialog = Utilities.createProgressDialog(context);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... voids) {

            //System.out.println("Uploading starting");

            String uploadUrl = Utilities.BASE_URL + "account/edit_document";
            String strResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(uploadUrl);

            httpPost.addHeader("X-access-uid", loggedInUser.getId());
            httpPost.addHeader("X-access-token", loggedInUser.getToken());

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(context));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            try {

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();

                /*if (documentBean != null) {
                    String mime = getMimeType(documentBean.getUri());
                    //System.out.println("Mime " + mime);

                    File file = new File(documentBean.getFilePath());
                    FileBody fileBody = new FileBody(file, file.getName(), mime, "UTF-8");

                    builder.addPart("file", fileBody);
                }*/

                if (selectedUri != null) {
                    String imageMime = getMimeType(selectedUri);
                    File imageFile = new File(selectedImagePath);
                    //System.out.println("MIME_type__" + imageMime + "--Path__" + selectedImagePath + "--FileName__" + imageFile.getName());
                    if (imageFile != null) {
                        FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");
                        builder.addPart("file", imageFileBody);
                    }

                } else if (fileUri != null) {
                    String fileMime = getMimeType(fileUri);
                    File file = new File(fileUri.getPath());

                    //System.out.println("MIME_type__" + fileMime + "--Path__" + fileUri + "--FileName__" + file.getName());

                    if (file != null) {
                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
                        builder.addPart("file", imageFileBody);
                    }
                }


                StringBody userId = new StringBody(loggedInUser.getId());
                StringBody title = new StringBody(strDocTitle);
                StringBody comments = new StringBody(strComment);
                StringBody documentId = new StringBody(documentToEdit.getDocumentId());

                builder.addPart("users_id", userId);
//                builder.addPart("title", title);
                builder.addPart("doc_title", title);
                builder.addPart("comments", comments);
                builder.addPart("document_id", documentId);

                HttpEntity entity = builder.build();

                httpPost.setEntity(entity);

                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity resEntity = response.getEntity();

                if (resEntity != null) {
                    try {
                        strResponse = EntityUtils.toString(resEntity).trim();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                //System.out.println("File upload end");

            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("Exception " + e.getMessage());
            }

            return strResponse;

        }

        @Override
        protected void onPostExecute(String response) {

            //System.out.println("Response " + response);

            if (response == null) {
                Toast.makeText(CoachUploadDocumentsScreen.this, "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    Toast.makeText(CoachUploadDocumentsScreen.this, message, Toast.LENGTH_SHORT).show();

                    if (status) {
                        if(documentToEdit != null){
                            String documentUrl = Utilities.BASE_URL + "account/download_document/" + loggedInUser.getToken() + "/" + documentToEdit.getDocumentId();
                            MemoryCacheUtils.removeFromCache(documentUrl, imageLoader.getMemoryCache());
                            DiskCacheUtils.removeFromCache(documentUrl, imageLoader.getDiskCache());
                        }
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CoachUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }
}