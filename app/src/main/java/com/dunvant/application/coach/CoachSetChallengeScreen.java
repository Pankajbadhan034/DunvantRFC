package com.dunvant.application.coach;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupBean;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.ChallengeCategoryBean;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachChallengeCategorySpinnerAdapter;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class CoachSetChallengeScreen extends AppCompatActivity implements IWebServiceCallback{
    ProgressDialog pd;
    //FFmpeg ffmpeg;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    ScrollView scrollView;
    Spinner locationSpinner;
    Spinner sessionSpinner;
    Spinner ageGroupSpinner;
    Spinner childrenSpinner;
    Spinner categorySpinner;
    Button addCategory;
//    Spinner targetSpinner;
//    Spinner valuesSpinner;
    EditText challengeTitle;
    EditText targetScore;
    EditText description;
    TextView uploadImage;
    TextView uploadVideo;
    static TextView expirationDate;
    static TextView expirationTime;
    RadioButton noneRadioButton;
    RadioButton parentRadioButton;
//    RadioButton childRadioButton;
    RadioButton coachRadioButton;
    Button submitButton;
    Button cancelButton;
    EditText youtubeUrl;

    EditText days;
    EditText hours;
    EditText minutes;
    EditText seconds;

    ImageView imageThumbnail;
    ImageView videoThumbnail;

//    String strLocationIds, strSessionIds, strAgeGroupIds, strChildrenIds, strCategoryId;
    String strCategoryId;
    String strTitle, strDescription, /*strTargetTimeType,*/ strTargetScore/*, strTargetTime*/;
    String strExpirationDate, strExpirationTime, strApprovalRequired;
    String strDays, strHours, strMinutes, strSeconds;
    JSONObject targetTimeObject;

    /*String[] targets = {"Seconds", "Minutes", "Hours", "Days"};
    ArrayList<String> seconds = new ArrayList<>();
    ArrayList<String> minutes = new ArrayList<>();
    ArrayList<String> hours = new ArrayList<>();
    ArrayList<String> days = new ArrayList<>();*/

    private final String GET_LOCATION_LISTING = "GET_LOCATION_LISTING";
    private final String GET_SESSIONS_LISTING = "GET_SESSIONS_LISTING";
    private final String GET_AGE_GROUP_LISTING = "GET_AGE_GROUP_LISTING";
    private final String GET_ALL_CHILDREN_LISTING = "GET_ALL_CHILDREN_LISTING";
    private final String GET_CATEGORIES_LIST =  "GET_CATEGORIES_LIST";
    private final String ADD_CATEGORY = "ADD_CATEGORY";

    ArrayList<CampLocationBean> locationsList = new ArrayList<>();
    ArrayList<CampDaysBean> daysListing = new ArrayList<>();
    ArrayList<AgeGroupBean> ageGroupsListing = new ArrayList<>();
    ArrayList<ChildBean> childrenListing = new ArrayList<>();
    ArrayList<ChallengeCategoryBean> categoriesListing = new ArrayList<>();

    CoachLocationListingAdapter coachLocationListingAdapter;
    CoachSessionListingAdapter coachSessionListingAdapter;
    CoachOnlyAgeGroupListingAdapter coachOnlyAgeGroupListingAdapter;
    CoachChildNamesSpinnerAdapter coachChildNamesSpinnerAdapter;
    CoachChallengeCategorySpinnerAdapter coachChallengeCategorySpinnerAdapter;

//    CampLocationBean clickedOnLocation;
//    CampDaysBean clickedOnDay;
//    AgeGroupBean clickedOnAgeGroup;
//    ChildBean clickedOnChild;

    String clickedOnLocationIds;
    String clickedOnDayIds;
    String clickedOnSessionIds;
    String clickedOnChildIds;

    private final int CHOOSE_IMAGE = 1;
    private final int CHOOSE_VIDEO = 2;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 3;
    public static final int MEDIA_TYPE_VIDEO = 4;

    private Uri imageFileUri = null;
    private Uri videoFileUri = null;

    Uri selectedVideoUri = null;
    Uri selectedImageUri = null;
    String selectedImagePath;
    String selectedVideoPath;

    String strYoutubeUrl;

    // Run time permission
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

//    boolean addedNewCategory = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.coach_activity_set_challenge);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        sessionSpinner = (Spinner) findViewById(R.id.sessionSpinner);
        ageGroupSpinner = (Spinner) findViewById(R.id.ageGroupSpinner);
        childrenSpinner = (Spinner) findViewById(R.id.childrenSpinner);
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);

        locationSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        sessionSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ageGroupSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        childrenSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        categorySpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


        addCategory = (Button) findViewById(R.id.addCategory);
//        targetSpinner = (Spinner) findViewById(R.id.targetSpinner);
//        valuesSpinner = (Spinner) findViewById(R.id.valuesSpinner);
        challengeTitle = (EditText) findViewById(R.id.challengeTitle);
        targetScore = (EditText) findViewById(R.id.targetScore);
        description = (EditText) findViewById(R.id.description);
        uploadImage = (TextView) findViewById(R.id.uploadImage);
        uploadVideo = (TextView) findViewById(R.id.uploadVideo);
        expirationDate = (TextView) findViewById(R.id.expirationDate);
        expirationTime = (TextView) findViewById(R.id.expirationTime);
        noneRadioButton = (RadioButton) findViewById(R.id.noneRadioButton);
        parentRadioButton = (RadioButton) findViewById(R.id.parentRadioButton);
//        childRadioButton = (RadioButton) findViewById(R.id.childRadioButton);
        coachRadioButton = (RadioButton) findViewById(R.id.coachRadioButton);
        submitButton = (Button) findViewById(R.id.submitButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        youtubeUrl = findViewById(R.id.youtubeUrl);

        days = (EditText) findViewById(R.id.days);
        hours = (EditText) findViewById(R.id.hours);
        minutes = (EditText) findViewById(R.id.minutes);
        seconds = (EditText) findViewById(R.id.seconds);

        imageThumbnail = findViewById(R.id.imageThumbnail);
        videoThumbnail = findViewById(R.id.videoThumbnail);

        changeFonts();
//        populateTargetsData();
        getLocationListing();

        // by default none selected
        noneRadioButton.setChecked(true);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Utilities.isNetworkAvailable(CoachSetChallengeScreen.this)) {
                    Toast.makeText(CoachSetChallengeScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    return;
                }

                int locationPosition = locationSpinner.getSelectedItemPosition();
                int sessionPosition = sessionSpinner.getSelectedItemPosition();
                int ageGroupPosition = ageGroupSpinner.getSelectedItemPosition();
                int childrenPosition = childrenSpinner.getSelectedItemPosition();
                int categoryPosition = categorySpinner.getSelectedItemPosition();

                strTitle = challengeTitle.getText().toString();
                strTargetScore = targetScore.getText().toString();

                strDays = days.getText().toString().trim();
                strHours = hours.getText().toString().trim();
                strMinutes = minutes.getText().toString().trim();
                strSeconds = seconds.getText().toString().trim();

                strYoutubeUrl = youtubeUrl.getText().toString().trim();

                if(strDays == null) {
                    strDays = "";
                }

                if(strHours == null){
                    strHours = "";
                }

                if(strMinutes == null){
                    strMinutes = "";
                }

                if(strSeconds == null){
                    strSeconds = "";
                }

                targetTimeObject = new JSONObject();
                try{
                    targetTimeObject.put("d", strDays);
                    targetTimeObject.put("h", strHours);
                    targetTimeObject.put("m", strMinutes);
                    targetTimeObject.put("s", strSeconds);
                } catch (JSONException e){
                    Toast.makeText(CoachSetChallengeScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


                /*int valueSpinnerPosition = valuesSpinner.getSelectedItemPosition();
                int targetSpinnerPosition = targetSpinner.getSelectedItemPosition();
                switch (targetSpinnerPosition) {
                    case 0:
                        strTargetTimeType = "4";
                        break;
                    case 1:
                        strTargetTimeType = "1";
                        break;
                    case 2:
                        strTargetTimeType = "2";
                        break;
                    case 3:
                        strTargetTimeType = "3";
                        break;
                }

                //System.out.println("Target Time Type "+strTargetTimeType);

                if(valueSpinnerPosition == 0){
                    strTargetTime = "";
                    strTargetTimeType = "";
                } else {
                    strTargetTime = valuesSpinner.getSelectedItem().toString();
                }*/

                strDescription = description.getText().toString().trim();
                strExpirationDate = expirationDate.getText().toString().trim();
                strExpirationTime = expirationTime.getText().toString().trim();

                if(locationPosition == 0) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Please select Location", Toast.LENGTH_SHORT).show();
                } else if (sessionPosition == 0) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Please select Session", Toast.LENGTH_SHORT).show();
                } else if (ageGroupPosition == 0) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Please select Age Group", Toast.LENGTH_SHORT).show();
                } else if (childrenPosition == 0) {
                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                    Toast.makeText(CoachSetChallengeScreen.this, "Please select "+verbiage_singular, Toast.LENGTH_SHORT).show();
                } else if (categoryPosition == 0) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Please select Category", Toast.LENGTH_SHORT).show();
                } else if (strTitle == null || strTitle.isEmpty()) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Please enter Title", Toast.LENGTH_SHORT).show();
                } /*else if (strTargetScore == null || strTargetScore.isEmpty()) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Please enter Target Score", Toast.LENGTH_SHORT).show();
                } else if (valueSpinnerPosition == 0) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Please select duration", Toast.LENGTH_SHORT).show();
                }*/ else if (strDescription == null || strDescription.isEmpty()) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Please enter Description", Toast.LENGTH_SHORT).show();
                } else if (strExpirationDate.equalsIgnoreCase("Expiration Date")) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Please select Expiration Date", Toast.LENGTH_SHORT).show();
                } else if (strExpirationTime.equalsIgnoreCase("HH:MM")) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Please select Expiration Time", Toast.LENGTH_SHORT).show();
                } else {
                    /*strLocationIds = locationsList.get(locationPosition).getLocationId();
                    strSessionIds = daysListing.get(sessionPosition).getDay();
                    strAgeGroupIds = ageGroupsListing.get(ageGroupPosition).getAgeGroupId();
                    strChildrenIds = childrenListing.get(childrenPosition).getId();
                    */
                    strCategoryId = categoriesListing.get(categoryPosition).getCategoryId();

                    if (noneRadioButton.isChecked()) {
                        strApprovalRequired = "0";
                    } else if (parentRadioButton.isChecked()) {
                        strApprovalRequired = "1";
                    } /*else if (childRadioButton.isChecked()) {
                        strApprovalRequired = "2";
                    }*/ else if (coachRadioButton.isChecked()) {
                        strApprovalRequired = "3";
                    }

                    new SetChallengeAsync(CoachSetChallengeScreen.this).execute();
                }
            }
        });

        youtubeUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!youtubeUrl.getText().toString().trim().isEmpty()){
                    videoThumbnail.setVisibility(View.GONE);
                    selectedVideoUri = null;
                    videoFileUri = null;
                    uploadVideo.setText("Upload Video");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] items = new String[]{"Take from camera", "Select from gallery"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CoachSetChallengeScreen.this, android.R.layout.select_dialog_item, items);
                AlertDialog.Builder builder = new AlertDialog.Builder(CoachSetChallengeScreen.this);

                builder.setTitle("Choose");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            imageFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
                            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                        } else {
                            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, CHOOSE_IMAGE);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] items = new String[]{"Take from camera", "Select from gallery"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CoachSetChallengeScreen.this, android.R.layout.select_dialog_item, items);
                AlertDialog.Builder builder = new AlertDialog.Builder(CoachSetChallengeScreen.this);

                builder.setTitle("Choose");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            videoFileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoFileUri);
                            startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
                        } else {
                            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, CHOOSE_VIDEO);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        expirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(CoachSetChallengeScreen.this.getSupportFragmentManager(), "datePicker");
            }
        });

        expirationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(CoachSetChallengeScreen.this.getSupportFragmentManager(), "timePicker");
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CoachSetChallengeScreen.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.coach_dialog_add_category);

                final EditText categoryName = (EditText) dialog.findViewById(R.id.categoryName);
                Button add = (Button) dialog.findViewById(R.id.add);
                Button cancel = (Button) dialog.findViewById(R.id.cancel);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strCategoryName = categoryName.getText().toString().trim();

                        if(strCategoryName == null || strCategoryName.isEmpty()) {
                            Toast.makeText(CoachSetChallengeScreen.this, "Please enter Category Name", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            addCategory(strCategoryName);
                        }
                    }
                });

                dialog.show();
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

                    coachSessionListingAdapter = new CoachSessionListingAdapter(CoachSetChallengeScreen.this, daysListing);
                    sessionSpinner.setAdapter(coachSessionListingAdapter);
                } else if (position == 1) {

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

                } else {
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
                if(position == 0) {
                    ageGroupsListing.clear();

                    // 0th Element Choose Session
                    AgeGroupBean ageGroupBean = new AgeGroupBean();
                    ageGroupBean.setAgeGroupId("-1");
                    ageGroupBean.setGroupName("Choose Age Group");

                    ageGroupsListing.add(ageGroupBean);

                    coachOnlyAgeGroupListingAdapter = new CoachOnlyAgeGroupListingAdapter(CoachSetChallengeScreen.this, ageGroupsListing);
                    ageGroupSpinner.setAdapter(coachOnlyAgeGroupListingAdapter);
                } else if (position == 1) {

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

                } else {
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

                    coachChildNamesSpinnerAdapter = new CoachChildNamesSpinnerAdapter(CoachSetChallengeScreen.this, childrenListing);
                    childrenSpinner.setAdapter(coachChildNamesSpinnerAdapter);

                } else if (position == 1) {

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

                } else {
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
                if(position == 0) {

                    categoriesListing.clear();

                    ChallengeCategoryBean categoryBean = new ChallengeCategoryBean();
                    categoryBean.setCategoryId("-1");
                    categoryBean.setName("Choose Category");
                    categoriesListing.add(categoryBean);

                    coachChallengeCategorySpinnerAdapter = new CoachChallengeCategorySpinnerAdapter(CoachSetChallengeScreen.this, categoriesListing);
                    categorySpinner.setAdapter(coachChallengeCategorySpinnerAdapter);

                } else if (position == 1) {

                    clickedOnChildIds = "";

                    for(ChildBean childBean : childrenListing) {
                        if(Integer.parseInt(childBean.getId()) > 0) {
                            clickedOnChildIds += childBean.getId()+",";
                        }
                    }

                    if (clickedOnChildIds != null && clickedOnChildIds.length() > 0 && clickedOnChildIds.charAt(clickedOnChildIds.length()-1)==',') {
                        clickedOnChildIds = clickedOnChildIds.substring(0, clickedOnChildIds.length()-1);
                    }

                    getCategoriesListing();

                } else {
//                    clickedOnChild = childrenListing.get(position);

                    clickedOnChildIds = childrenListing.get(position).getId();

                    getCategoriesListing();
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
            if (checkPermissions()){
//                Toast.makeText(ChildTrackWorkoutScreen.this, "Permissions already granted", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(ChildTrackWorkoutScreen.this, "Initially permission not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(CoachSetChallengeScreen.this, p);
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
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permissions granted.
                } else {
                    // no permissions granted.
                    Toast.makeText(CoachSetChallengeScreen.this, "This feature cannot work without Permissions. \nTry Again and allow permissions or allow permissions in Applications Settings", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }

    private void addCategory(String category) {
        if(Utilities.isNetworkAvailable(CoachSetChallengeScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("category_name", category));

            String webServiceUrl = Utilities.BASE_URL + "coach/add_challenge_category";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachSetChallengeScreen.this, nameValuePairList, ADD_CATEGORY, CoachSetChallengeScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachSetChallengeScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocationListing(){
        if(Utilities.isNetworkAvailable(CoachSetChallengeScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "coach/locations_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(CoachSetChallengeScreen.this, GET_LOCATION_LISTING, CoachSetChallengeScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachSetChallengeScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getSessionDaysListing(){
        if(Utilities.isNetworkAvailable(CoachSetChallengeScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_ids", clickedOnLocationIds));
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocationIds));

            String webServiceUrl = Utilities.BASE_URL + "coach/all_session_days_list";
//            String webServiceUrl = Utilities.BASE_URL + "coach/session_days_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachSetChallengeScreen.this, nameValuePairList, GET_SESSIONS_LISTING, CoachSetChallengeScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachSetChallengeScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAgeGroupsListing(){
        if(Utilities.isNetworkAvailable(CoachSetChallengeScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnLocationIds));
            nameValuePairList.add(new BasicNameValuePair("locations_ids", clickedOnLocationIds));
            nameValuePairList.add(new BasicNameValuePair("session_days", clickedOnDayIds));

            String webServiceUrl = Utilities.BASE_URL + "coach/all_age_group_list";
//            String webServiceUrl = Utilities.BASE_URL + "coach/age_group_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachSetChallengeScreen.this, nameValuePairList, GET_AGE_GROUP_LISTING, CoachSetChallengeScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachSetChallengeScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllChildrenListing(){
        if(Utilities.isNetworkAvailable(CoachSetChallengeScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("sessions_ids", clickedOnSessionIds));

            String webServiceUrl = Utilities.BASE_URL + "coach/all_children_booked_session_list";
//            String webServiceUrl = Utilities.BASE_URL + "coach/children_booked_session_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachSetChallengeScreen.this, nameValuePairList, GET_ALL_CHILDREN_LISTING, CoachSetChallengeScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachSetChallengeScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getCategoriesListing(){
        if(Utilities.isNetworkAvailable(CoachSetChallengeScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "coach/challenge_category_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(CoachSetChallengeScreen.this, GET_CATEGORIES_LIST, CoachSetChallengeScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachSetChallengeScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_LOCATION_LISTING:

                locationsList.clear();

                if (response == null) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();

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

                            locationBean = new CampLocationBean();
                            locationBean.setLocationId("-2");
                            locationBean.setLocationName("All");

                            locationsList.add(locationBean);

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

                            Toast.makeText(CoachSetChallengeScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachSetChallengeScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachLocationListingAdapter = new CoachLocationListingAdapter(CoachSetChallengeScreen.this, locationsList);
                locationSpinner.setAdapter(coachLocationListingAdapter);
                break;

            case GET_SESSIONS_LISTING:

                daysListing.clear();

                if(response == null) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();

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

                        if(status) {

                            // 0th Element Choose Session
                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Choose Session");

                            daysListing.add(daysBean);

                            daysBean = new CampDaysBean();
                            daysBean.setDay("-2");
                            daysBean.setDayLabel("All");

                            daysListing.add(daysBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject daysObject = dataArray.getJSONObject(i);
                                daysBean = new CampDaysBean();
                                daysBean.setDay(daysObject.getString("day"));
                                daysBean.setDayLabel(daysObject.getString("day_label"));

                                daysListing.add(daysBean);
                            }

                        } else {

                            // 0th Element Choose Session
                            CampDaysBean daysBean = new CampDaysBean();
                            daysBean.setDay("-1");
                            daysBean.setDayLabel("Choose Session");

                            daysListing.add(daysBean);

                            Toast.makeText(CoachSetChallengeScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachSetChallengeScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachSessionListingAdapter = new CoachSessionListingAdapter(CoachSetChallengeScreen.this, daysListing);
                sessionSpinner.setAdapter(coachSessionListingAdapter);

                break;

            case GET_AGE_GROUP_LISTING:

                ageGroupsListing.clear();

                if (response == null) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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

                        if(status) {

                            // 0th Element Choose Age Group

                            AgeGroupBean ageGroupBean = new AgeGroupBean();
                            ageGroupBean.setAgeGroupId("-1");
                            ageGroupBean.setSessionId("-1");
                            ageGroupBean.setGroupName("Choose Age Group");

                            ageGroupsListing.add(ageGroupBean);

                            ageGroupBean = new AgeGroupBean();
                            ageGroupBean.setAgeGroupId("-2");
                            ageGroupBean.setSessionId("-2");
                            ageGroupBean.setGroupName("All");

                            ageGroupsListing.add(ageGroupBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for(int i=0; i<dataArray.length(); i++) {
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
                            Toast.makeText(CoachSetChallengeScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachSetChallengeScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachOnlyAgeGroupListingAdapter = new CoachOnlyAgeGroupListingAdapter(CoachSetChallengeScreen.this, ageGroupsListing);
                ageGroupSpinner.setAdapter(coachOnlyAgeGroupListingAdapter);

                break;

            case GET_ALL_CHILDREN_LISTING:

                childrenListing.clear();

                if(response == null) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();

                    ChildBean childBean = new ChildBean();
                    childBean.setId("-1");
                   // childBean.setFullName("Choose Child");
                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);


                    childBean.setFullName("Choose "+verbiage_singular);
                    childrenListing.add(childBean);
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

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

                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject childObject = dataArray.getJSONObject(i);
                                childBean = new ChildBean();

                                childBean.setId(childObject.getString("id"));
                                childBean.setFullName(childObject.getString("full_name"));

                                childrenListing.add(childBean);
                            }

                        } else {
                            Toast.makeText(CoachSetChallengeScreen.this, message, Toast.LENGTH_SHORT).show();

                            ChildBean childBean = new ChildBean();
                            childBean.setId("-1");
                            //childBean.setFullName("Choose Child");
                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);


                            childBean.setFullName("Choose "+verbiage_singular);
                            childrenListing.add(childBean);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachSetChallengeScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachChildNamesSpinnerAdapter = new CoachChildNamesSpinnerAdapter(CoachSetChallengeScreen.this, childrenListing);
                childrenSpinner.setAdapter(coachChildNamesSpinnerAdapter);

                break;

            case GET_CATEGORIES_LIST:

                categoriesListing.clear();

                if(response == null) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();

                    ChallengeCategoryBean categoryBean = new ChallengeCategoryBean();
                    categoryBean.setCategoryId("-1");
                    categoryBean.setName("Choose Category");
                    categoriesListing.add(categoryBean);

                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            ChallengeCategoryBean categoryBean = new ChallengeCategoryBean();
                            categoryBean.setCategoryId("-1");
                            categoryBean.setName("Choose Category");
                            categoriesListing.add(categoryBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");

                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject categoryObject = dataArray.getJSONObject(i);
                                categoryBean = new ChallengeCategoryBean();

                                categoryBean.setCategoryId(categoryObject.getString("id"));
                                categoryBean.setParentId(categoryObject.getString("parent_id"));
                                categoryBean.setType(categoryObject.getString("type"));
                                categoryBean.setName(categoryObject.getString("name"));
                                categoryBean.setDescription(categoryObject.getString("description"));
                                categoryBean.setCreatedAt(categoryObject.getString("created_at"));

                                categoriesListing.add(categoryBean);
                            }

                        } else {
                            Toast.makeText(CoachSetChallengeScreen.this, message, Toast.LENGTH_SHORT).show();

                            ChallengeCategoryBean categoryBean = new ChallengeCategoryBean();
                            categoryBean.setCategoryId("-1");
                            categoryBean.setName("Choose Category");
                            categoriesListing.add(categoryBean);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachSetChallengeScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachChallengeCategorySpinnerAdapter = new CoachChallengeCategorySpinnerAdapter(CoachSetChallengeScreen.this, categoriesListing);
                categorySpinner.setAdapter(coachChallengeCategorySpinnerAdapter);

                /*if(addedNewCategory){

                    categorySpinner.setSelection(categoriesListing.size() - 1);

                    addedNewCategory = false;
                }*/

                break;

            case ADD_CATEGORY:

                if(response == null) {
                    Toast.makeText(CoachSetChallengeScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(CoachSetChallengeScreen.this, message, Toast.LENGTH_SHORT).show();

                        if(status) {
//                            addedNewCategory = true;
                            getCategoriesListing();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachSetChallengeScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case CHOOSE_IMAGE:

                if(data != null) {
                    selectedImageUri = data.getData();

                    String[] filePathColumn = { MediaStore.Video.Media.DATA };
                    Cursor cursor = CoachSetChallengeScreen.this.getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedImagePath = cursor.getString(columnIndex);
                    cursor.close();
                    uploadImage.setText(selectedImagePath);

                    // showing thumbnail
                    imageThumbnail.setImageURI(selectedImageUri);
                    imageThumbnail.setVisibility(View.VISIBLE);
                }

                break;

            case CHOOSE_VIDEO:

                if(data != null) {
                    selectedVideoUri = data.getData();

                    String[] filePathColumn1 = { MediaStore.Video.Media.DATA };
                    Cursor cursor1 = CoachSetChallengeScreen.this.getContentResolver().query(selectedVideoUri, filePathColumn1, null, null, null);
                    cursor1.moveToFirst();
                    int columnIndex1 = cursor1.getColumnIndex(filePathColumn1[0]);
                    selectedVideoPath = cursor1.getString(columnIndex1);
                    cursor1.close();
                    uploadVideo.setText(selectedVideoPath);
                    File file = new File(selectedVideoPath);

                    // showing thumbnail
                    Bitmap bMap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                    videoThumbnail.setImageBitmap(bMap);
                    videoThumbnail.setVisibility(View.VISIBLE);

                    // clearing youtube url
                    youtubeUrl.setText("");
                }

                break;

            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    try {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;

//                        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                        uploadImage.setText(imageFileUri.getPath());


                        // showing thumbnail
                        imageThumbnail.setImageURI(imageFileUri);
                        imageThumbnail.setVisibility(View.VISIBLE);


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
                       // ffmpegComressVideo();
                        // uploadVideo.setText(fileUri.getPath());
                        //System.out.println("Video recorded");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "User cancelled video recording", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry! Failed to record video", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /*private void populateTargetsData(){

        seconds.clear();
        minutes.clear();
        hours.clear();
        days.clear();

        seconds.add("Choose Seconds");
        for(int i=1;i<60;i++) {
            seconds.add(i+"");
        }

        minutes.add("Choose Minutes");
        for(int i=1;i<60;i++){
            minutes.add(i+"");
        }

        hours.add("Choose Hours");
        for(int i=1;i<24;i++) {
            hours.add(i+"");
        }

        days.add("Choose Days");
        for(int i=1;i<=31;i++) {
            days.add(i+"");
        }

        ArrayAdapter<String> targetsAdapter = new ArrayAdapter<>(CoachSetChallengeScreen.this, R.layout.coach_adapter_target_item, targets);
        targetsAdapter.setDropDownViewResource(R.layout.coach_adapter_target_dropdown_item);
        targetSpinner.setAdapter(targetsAdapter);

        final ArrayAdapter<String> secondsAdapter = new ArrayAdapter<>(CoachSetChallengeScreen.this, R.layout.coach_adapter_target_item, seconds);
        secondsAdapter.setDropDownViewResource(R.layout.coach_adapter_target_dropdown_item);

        final ArrayAdapter<String> minutesAdapter = new ArrayAdapter<>(CoachSetChallengeScreen.this, R.layout.coach_adapter_target_item, minutes);
        minutesAdapter.setDropDownViewResource(R.layout.coach_adapter_target_dropdown_item);

        final ArrayAdapter<String> hoursAdapter = new ArrayAdapter<>(CoachSetChallengeScreen.this, R.layout.coach_adapter_target_item, hours);
        hoursAdapter.setDropDownViewResource(R.layout.coach_adapter_target_dropdown_item);

        final ArrayAdapter<String> daysAdapter = new ArrayAdapter<>(CoachSetChallengeScreen.this, R.layout.coach_adapter_target_item, days);
        daysAdapter.setDropDownViewResource(R.layout.coach_adapter_target_dropdown_item);

        targetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        valuesSpinner.setAdapter(secondsAdapter);
                        break;
                    case 1:
                        valuesSpinner.setAdapter(minutesAdapter);
                        break;
                    case 2:
                        valuesSpinner.setAdapter(hoursAdapter);
                        break;
                    case 3:
                        valuesSpinner.setAdapter(daysAdapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }*/

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dp = new DatePickerDialog(getActivity(), this, year, month, day);
            dp.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            return dp;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month += 1;

            String strMonth = (month < 10) ? "0"+month : month+"";
            String strDay = (day < 10) ? "0"+day : day+"";

            expirationDate.setText(year+"-"+strMonth+"-"+strDay);
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

            String strHour = (hourOfDay < 10) ? "0"+hourOfDay : hourOfDay+"";
            String strMin = (minute < 10) ? "0"+minute : minute+"";

            expirationTime.setText(strHour+":"+strMin+":00");
        }
    }

    private class SetChallengeAsync extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog;
        Context context;

        public SetChallengeAsync(Context context){
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

            String uploadUrl = Utilities.BASE_URL + "coach/set_challenge";
            String strResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(uploadUrl);

            httpPost.addHeader("X-access-uid", loggedInUser.getId());
            httpPost.addHeader("X-access-token", loggedInUser.getToken());

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(context));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            try {

                StringBody isGlobalBody = new StringBody("0");
//                StringBody locationIdsBody = new StringBody(strLocationIds);
//                StringBody sessionIdsBody = new StringBody(strSessionIds);
//                StringBody childrenIdsBody = new StringBody(strChildrenIds);
//                StringBody categoryIdBody = new StringBody(strCategoryId);

                StringBody locationIdsBody = new StringBody(clickedOnLocationIds);
                StringBody sessionIdsBody = new StringBody(clickedOnSessionIds);
                StringBody childrenIdsBody = new StringBody(clickedOnChildIds);
                StringBody categoryIdBody = new StringBody(strCategoryId);

                StringBody titleBody = new StringBody(strTitle);
                StringBody descriptionBody = new StringBody(strDescription);

                /*StringBody targetTimeTypeBody = new StringBody(strTargetTimeType);
                StringBody targetTimeBody = new StringBody(strTargetTime);*/
                StringBody targetTimeTypeBody = new StringBody("");
                StringBody targetTimeBody = new StringBody(targetTimeObject.toString());

                StringBody targetScoreBody = new StringBody(strTargetScore);
                StringBody expirationDateBody = new StringBody(strExpirationDate);
                StringBody expirationTimeBody = new StringBody(strExpirationTime);
                StringBody approvalRequiredBody = new StringBody(strApprovalRequired);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();

                builder.addPart("is_global", isGlobalBody);
                builder.addPart("locations_ids", locationIdsBody);
                builder.addPart("sessions_ids", sessionIdsBody);
                builder.addPart("children_ids", childrenIdsBody);
                builder.addPart("category_id", categoryIdBody);
                builder.addPart("title", titleBody);
                builder.addPart("description", descriptionBody);
                builder.addPart("target_score", targetScoreBody);

                /*builder.addPart("target_time_type", targetTimeTypeBody);
                builder.addPart("target_time", targetTimeBody);*/

                builder.addPart("target_time_type", targetTimeTypeBody);
                builder.addPart("target_time", targetTimeBody);

                builder.addPart("expiration_date", expirationDateBody);
                builder.addPart("expiration_time", expirationTimeBody);
                builder.addPart("approval_required", approvalRequiredBody);

                // Image
                if(selectedImageUri != null) {
                    String imageMime = getMimeType(selectedImageUri);
                    File imageFile = new File(selectedImagePath);
                    FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");

                    builder.addPart("challenge_image", imageFileBody);
                } else if (imageFileUri != null) {

                    String fileMime = getMimeType(imageFileUri);

                    File file = new File(imageFileUri.getPath());
                    if(file != null){
                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
                        builder.addPart("challenge_image", imageFileBody);
                    }
                }

                // Video
                if(selectedVideoUri != null) {
                    String videoMime = getMimeType(selectedVideoUri);
                    File videoFile = new File(selectedVideoPath);
                    FileBody videoFileBody = new FileBody(videoFile, videoFile.getName(), videoMime, "UTF-8");

                    builder.addPart("challenge_video", videoFileBody);
                } else if (videoFileUri != null) {

                    String fileMime = getMimeType(videoFileUri);

                    File file = new File(videoFileUri.getPath());
                    if(file != null){
                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
                        builder.addPart("challenge_video", imageFileBody);
                    }
                }

                if(strYoutubeUrl != null && !strYoutubeUrl.isEmpty()){
                    StringBody youtubeUrlBody = new StringBody(strYoutubeUrl);
                    builder.addPart("youtube_url", youtubeUrlBody);
                }

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
                //System.out.println("Exception "+e.getMessage());
            }

            return strResponse;

        }

        @Override
        protected void onPostExecute(String response) {

            //System.out.println("Response "+response);

            if(response == null) {
                Toast.makeText(CoachSetChallengeScreen.this, "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    Toast.makeText(CoachSetChallengeScreen.this, Html.fromHtml(message), Toast.LENGTH_SHORT).show();

                    if(status) {
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CoachSetChallengeScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if(pDialog != null && pDialog.isShowing()){
                pDialog.dismiss();
            }
        }
    }

    public String getMimeType(Uri uri) {
        String mimeType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = CoachSetChallengeScreen.this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private void changeFonts() {
        title.setTypeface(linoType);
        addCategory.setTypeface(linoType);
        challengeTitle.setTypeface(helvetica);
        targetScore.setTypeface(helvetica);
        description.setTypeface(helvetica);
        uploadImage.setTypeface(helvetica);
        uploadVideo.setTypeface(helvetica);
        expirationDate.setTypeface(helvetica);
        expirationTime.setTypeface(helvetica);
        noneRadioButton.setTypeface(helvetica);
        parentRadioButton.setTypeface(helvetica);
//        childRadioButton.setTypeface(helvetica);
        coachRadioButton.setTypeface(helvetica);
        submitButton.setTypeface(helvetica);
        cancelButton.setTypeface(helvetica);
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
//    public void ffmpegComressVideo(){
//        pd = new ProgressDialog(CoachSetChallengeScreen.this);
//        pd.setMessage("Loading...");
//        pd.show();
//
//        ffmpeg = FFmpeg.getInstance(CoachSetChallengeScreen.this);
//        try {
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//
//                @Override
//                public void onStart() {
//                    //System.out.println("FFMPEG onStart");
//                }
//
//                @Override
//                public void onFailure() {
//                    //System.out.println("FFMPEG onFailure");
//                }
//
//                @Override
//                public void onSuccess() {
//                    //System.out.println("FFMPEG onSuccess");
//                }
//
//                @Override
//                public void onFinish() {
//                    //System.out.println("FFMPEG onFinish");
//                }
//            });
//        } catch (FFmpegNotSupportedException e) {
//            // Handle if FFmpeg is not supported by device
//            //System.out.println("FFMPEG Exception");
//            e.printStackTrace();
//        }
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        final String output = Environment.getExternalStorageDirectory()+"/VIDEO_" + timeStamp + ".mp4";
//
//
//        //System.out.println("input"+videoFileUri.getPath()+"output::"+output);
//
//
//        // Merge files
//
//        String[] command1 = new String[3];
//        command1[0] = "-i";
//        command1[1] = videoFileUri.getPath();
//        command1[2] = output;
//
//        try {
//            // to execute "ffmpeg -version" command you just need to pass "-version"
//            ffmpeg.execute(command1, new ExecuteBinaryResponseHandler() {
//
//                @Override
//                public void onStart() {
//                    //System.out.println("FFMPEG onStart");
//                }
//
//                @Override
//                public void onProgress(String message) {
//                    //System.out.println("FFMPEG onProgress "+message);
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    //System.out.println("FFMPEG onFailure "+message);
//                }
//
//                @Override
//                public void onSuccess(String message) {
//                    //System.out.println("FFMPEG onSuccess "+message);
//                }
//
//                @Override
//                public void onFinish() {
//                    //System.out.println("FFMPEG onFinish");
//                    File file = new File(output);
//
//                    videoFileUri = Uri.fromFile(file);
//                    uploadVideo.setText(videoFileUri.getPath());
//
//
//                    // showing thumbnail
//                    Bitmap bMap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
//                    videoThumbnail.setImageBitmap(bMap);
//                    videoThumbnail.setVisibility(View.VISIBLE);
//
//                    // clearing youtube url
//                    youtubeUrl.setText("");
//
//                    pd.dismiss();
//
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            // Handle if FFmpeg is already running
//            //System.out.println("FFMPEG Exception");
//            e.printStackTrace();
//        }
//    }
}