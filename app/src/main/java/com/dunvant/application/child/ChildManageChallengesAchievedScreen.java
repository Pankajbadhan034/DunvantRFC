package com.dunvant.application.child;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChildManageChallengesAchievedScreen extends AppCompatActivity implements IWebServiceCallback {

    boolean scoreValidation;
    String daysStr="", hoursStr="", minutesStr="", secondsStr="";
    ImageView backButton;
    Float fValue;
    int scoreValue;
    int targetValue;
    String selectedImagePath;
    String timerStr;
    Uri selectedUri;
    private final int CHOOSE_IMAGE = 1;
    private final int CHOOSE_VIDEO = 1123;
    RelativeLayout uploadRelative;
    //String spinnerTimeStr="1";
    List<String> timeList = new ArrayList<String>();
    //Spinner spinnerTime;
    Button submit_challenge;
    TextView lblTitle;
    TextView challengeTitle;
    TextView byCoachLbl;
    TextView targetLabel;
    TextView targetTimeLabel;
    TextView yourScoreLabel;
    TextView yourTimeLable;
    TextView attachLable;
    TextView uploadPhotoVideo;
    TextView targetScore;
    TextView targetTime;
    Typeface helvetica;
    Typeface linoType;
    Button plusScore;
    Button minusScore;
    EditText scoreEdit;
    EditText message;
    EditText days;
    EditText hours;
    EditText minutes;
    EditText seconds;

    ImageView thumbnail;

    Dialog dialog;
    Button shareWithFriends;
    String challengeIdStr;
    String byCoachStr;
    String targetScoreStr;
    String targetTimeStr;
    String titleStr;
    String isSharedStr;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    String messageStr;
    String targetTimeNew;
   // ArrayList<MyFriendsBean> myFriendsBeanArrayList = new ArrayList<>();
    private final String SUBMIT_CHALLENGE = "SUBMIT_CHALLENGE";

    // Run time permission
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_manage_challenges_achieved_screen);
        lblTitle = (TextView) findViewById(R.id.lblTitle);
        challengeTitle = (TextView) findViewById(R.id.challengeTitlelbl);
        byCoachLbl = (TextView) findViewById(R.id.byCoachLbl);
        targetLabel = (TextView) findViewById(R.id.targetLabel);
        targetTimeLabel = (TextView) findViewById(R.id.targetTimeLabel);
        yourScoreLabel = (TextView) findViewById(R.id.yourScoreLabel);
        yourTimeLable = (TextView) findViewById(R.id.yourTimeLable);
        attachLable = (TextView) findViewById(R.id.attachLable);
        uploadPhotoVideo = (TextView) findViewById(R.id.uploadPhotoVideo);
        submit_challenge = (Button) findViewById(R.id.submit_challenge);
        shareWithFriends = (Button) findViewById(R.id.shareWithFriend);
        targetScore = (TextView) findViewById(R.id.targetScore);
        targetTime = (TextView) findViewById(R.id.targetTime);
       // spinnerTime = (Spinner) findViewById(R.id.spinnerTime);
        uploadRelative = (RelativeLayout) findViewById(R.id.uploadRelative);
        plusScore = (Button) findViewById(R.id.plusScore);
        minusScore = (Button) findViewById(R.id.minusScore);
        scoreEdit = (EditText) findViewById(R.id.scoreEdit);
        message = (EditText) findViewById(R.id.message);
        backButton = (ImageView) findViewById(R.id.backButton);
        days = (EditText) findViewById(R.id.days);
        hours = (EditText) findViewById(R.id.hours);
        minutes = (EditText) findViewById(R.id.minutes);
        seconds = (EditText) findViewById(R.id.seconds);

        thumbnail = findViewById(R.id.thumbnail);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        challengeIdStr = getIntent().getStringExtra("challengesId");
        titleStr = getIntent().getStringExtra("title");
        byCoachStr = getIntent().getStringExtra("byCoach");
        targetScoreStr = getIntent().getStringExtra("targetScore");
        targetTimeStr = getIntent().getStringExtra("targetTime");
      //  isSharedStr = getIntent().getStringExtra("isShared");
        timerStr = getIntent().getStringExtra("timer");
        targetTimeNew = getIntent().getStringExtra("targetTimeNew");
        //System.out.println("TARGET_TIME::"+targetTimeStr);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");
        lblTitle.setTypeface(linoType);
        challengeTitle.setTypeface(helvetica);
        byCoachLbl.setTypeface(helvetica);
        targetLabel.setTypeface(helvetica);
        targetTimeLabel.setTypeface(helvetica);
        yourScoreLabel.setTypeface(helvetica);
        yourTimeLable.setTypeface(helvetica);
        attachLable.setTypeface(helvetica);
        uploadPhotoVideo.setTypeface(helvetica);
        submit_challenge.setTypeface(helvetica);
        shareWithFriends.setTypeface(helvetica);

        challengeTitle.setText(titleStr);
        byCoachLbl.setText(byCoachStr);
        targetScore.setText(targetScoreStr);
        targetTime.setText(targetTimeStr);

        if(targetScoreStr.equalsIgnoreCase("0.00") || targetScoreStr.equalsIgnoreCase("0")){
            targetScore.setVisibility(View.INVISIBLE);
            targetLabel.setVisibility(View.INVISIBLE);

            scoreEdit.setEnabled(false);
            scoreValidation=false;
        }

        if(targetTimeStr.equalsIgnoreCase("")){
            System.out.println("Here::1");
            targetTime.setVisibility(View.INVISIBLE);
            targetTimeLabel.setVisibility(View.INVISIBLE);
            days.setEnabled(false);
            hours.setEnabled(false);
            minutes.setEnabled(false);
            seconds.setEnabled(false);

        }else{
            try{
                System.out.println("Here::2"+targetTimeNew);
                JSONObject jsonObject = new JSONObject(targetTimeNew);
                String d = jsonObject.getString("d");
                String h = jsonObject.getString("h");
                String m = jsonObject.getString("m");
                String s = jsonObject.getString("s");

                days.setEnabled(false);
                hours.setEnabled(false);
                minutes.setEnabled(false);
                seconds.setEnabled(false);

                if(!d.isEmpty()){
                    System.out.println("d::1");
                    days.setEnabled(true);
                    hours.setEnabled(true);
                    minutes.setEnabled(true);
                    seconds.setEnabled(true);
                }else if(!h.isEmpty()){
                    System.out.println("h::1");
                    hours.setEnabled(true);
                    minutes.setEnabled(true);
                    seconds.setEnabled(true);
                }else if(!m.isEmpty()){
                    System.out.println("m::1");
                    minutes.setEnabled(true);
                    seconds.setEnabled(true);
                }else if(!s.isEmpty()){
                    System.out.println("s::1");
                    seconds.setEnabled(true);
                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if(targetTimeStr.contains("Hours")){
            timeList.clear();
            for(int i=1; i<=24; i++){
                timeList.add(i+"Hour(s)");
            }

        }else if(targetTimeStr.contains("Minutes")){
            timeList.clear();
            for(int i=1; i<=60; i++){
                timeList.add(i+"Minute(s)");
            }
        }else{
            timeList.clear();
            for(int i=1; i<=31; i++){
                timeList.add(i+"Day(s)");
            }
        }

//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeList);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerTime.setAdapter(dataAdapter);
//
//        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                position=position+1;
//                spinnerTimeStr = ""+position;
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        fValue = Float.parseFloat(targetScoreStr);

        plusScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String strValue = scoreEdit.getText().toString();
                    if(strValue.isEmpty()){
                        strValue = "0";
                    }
                    scoreValue = Integer.parseInt(strValue);
                    if (targetValue < fValue) {
                        scoreValue++;
                        scoreEdit.setText("" + scoreValue);
                    }
                } catch(NumberFormatException e){
                    e.printStackTrace();
                }
            }
        });

        minusScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String strValue = scoreEdit.getText().toString();
                    if(strValue.isEmpty()){
                        strValue = "0";
                    }
                    scoreValue = Integer.parseInt(strValue);
                    if (scoreValue > 0) {
                        scoreValue--;
                    }
                    scoreEdit.setText("" + scoreValue);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        });

        uploadRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChildManageChallengesAchievedScreen.this);
                builder1.setMessage("Choose your option:");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Image",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, CHOOSE_IMAGE);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Video",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, CHOOSE_VIDEO);
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        submit_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreValue = Integer.parseInt(scoreEdit.getText().toString());
                //System.out.println("ScoreValue::"+scoreValue);
                messageStr = message.getText().toString();

                daysStr = days.getText().toString();
                hoursStr = hours.getText().toString();
                minutesStr = minutes.getText().toString();
                secondsStr = seconds.getText().toString();
                isSharedStr = "0";

                if(scoreValidation==true){
                    if(scoreValue==0){
                        Toast.makeText(ChildManageChallengesAchievedScreen.this, "Please enter score", Toast.LENGTH_SHORT).show();
                    }else{
                        if(Utilities.isNetworkAvailable(ChildManageChallengesAchievedScreen.this)) {
                            new submitChallenge(ChildManageChallengesAchievedScreen.this).execute();
                        }else{
                            Toast.makeText(ChildManageChallengesAchievedScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    if(Utilities.isNetworkAvailable(ChildManageChallengesAchievedScreen.this)) {
                        new submitChallenge(ChildManageChallengesAchievedScreen.this).execute();
                    }else{
                        Toast.makeText(ChildManageChallengesAchievedScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }

                /*if (messageStr == null || messageStr.isEmpty()) {
                    Toast.makeText(ChildManageChallengesAchievedScreen.this, "Please enter Message", Toast.LENGTH_SHORT).show();
                }else if(selectedImagePath==null || selectedImagePath.isEmpty()){
                    Toast.makeText(ChildManageChallengesAchievedScreen.this, "Please select Image/Video", Toast.LENGTH_SHORT).show();
                }else{
                    if(Utilities.isNetworkAvailable(ChildManageChallengesAchievedScreen.this)) {
                        new submitChallenge(ChildManageChallengesAchievedScreen.this).execute();
                    }else{
                        Toast.makeText(ChildManageChallengesAchievedScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }*/
            }
        });

        shareWithFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                daysStr = days.getText().toString();
                hoursStr = hours.getText().toString();
                minutesStr = minutes.getText().toString();
                secondsStr = seconds.getText().toString();
                isSharedStr = "1";
                // custom dialog
                 dialog = new Dialog(ChildManageChallengesAchievedScreen.this);
                dialog.setContentView(R.layout.child_dialog_challenge_share_with_friends);
                dialog.setTitle(R.string.ifa_dialog);

                // set the custom dialog components - text, image and button
                TextView titleLabel = (TextView) dialog.findViewById(R.id.titleLabel);
                TextView byCoachNameLabel = (TextView) dialog.findViewById(R.id.byCoachNameLabel);
                TextView targetLabel = (TextView) dialog.findViewById(R.id.targetLabel);
                TextView target = (TextView) dialog.findViewById(R.id.target);
                TextView targetTimeLabel = (TextView) dialog.findViewById(R.id.targetTimeLabel);
                TextView time = (TextView) dialog.findViewById(R.id.time);
                ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel);
                final EditText message = (EditText) dialog.findViewById(R.id.message);
                Button submit = (Button) dialog.findViewById(R.id.submit);

                titleLabel.setTypeface(helvetica);
                byCoachNameLabel.setTypeface(helvetica);
                targetLabel.setTypeface(helvetica);
                target.setTypeface(helvetica);
                targetTimeLabel.setTypeface(helvetica);
                time.setTypeface(helvetica);
                submit.setTypeface(linoType);

                titleLabel.setText(titleStr);
                byCoachNameLabel.setText(byCoachStr);
                time.setText(targetTimeStr);
                target.setText(targetScoreStr);

                if(targetScoreStr.equalsIgnoreCase("0.00") || targetScoreStr.equalsIgnoreCase("0")){
                    targetLabel.setVisibility(View.INVISIBLE);
                    target.setVisibility(View.INVISIBLE);
                }

                if(targetTimeStr.equalsIgnoreCase("")){
                    targetTimeLabel.setVisibility(View.INVISIBLE);
                    time.setVisibility(View.INVISIBLE);
                }

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        messageStr = message.getText().toString();

                        if(messageStr==null || messageStr.isEmpty()){
                            Toast.makeText(ChildManageChallengesAchievedScreen.this, "Please enter message", Toast.LENGTH_LONG).show();
                        }
//                        else if(daysStr==null || daysStr.isEmpty()){
//                            Toast.makeText(ChildManageChallengesAchievedScreen.this, "Please enter Days", Toast.LENGTH_LONG).show();
//                        }else if(hoursStr==null || hoursStr.isEmpty()){
//                            Toast.makeText(ChildManageChallengesAchievedScreen.this, "Please enter Hours", Toast.LENGTH_LONG).show();
//                        }else if(minutesStr==null || minutesStr.isEmpty()){
//                            Toast.makeText(ChildManageChallengesAchievedScreen.this, "Please enter Minutes", Toast.LENGTH_LONG).show();
//                        }else if(secondsStr==null || secondsStr.isEmpty()){
//                            Toast.makeText(ChildManageChallengesAchievedScreen.this, "Please enter seconds", Toast.LENGTH_LONG).show();
//                        }
                        else{
                            if(scoreValidation==true){
                                if(scoreValue==0){
                                    Toast.makeText(ChildManageChallengesAchievedScreen.this, "Please enter score", Toast.LENGTH_SHORT).show();
                                }else{
                                    if(Utilities.isNetworkAvailable(ChildManageChallengesAchievedScreen.this)) {
                                        new submitChallenge(ChildManageChallengesAchievedScreen.this).execute();
                                    }else{
                                        Toast.makeText(ChildManageChallengesAchievedScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                if(Utilities.isNetworkAvailable(ChildManageChallengesAchievedScreen.this)) {
                                    new submitChallenge(ChildManageChallengesAchievedScreen.this).execute();
                                }else{
                                    Toast.makeText(ChildManageChallengesAchievedScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                                }
                            }

//                            if(Utilities.isNetworkAvailable(ChildManageChallengesAchievedScreen.this)) {
//                                new submitChallenge(ChildManageChallengesAchievedScreen.this).execute();
//
//                            } else {
//                                Toast.makeText(ChildManageChallengesAchievedScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }


    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case SUBMIT_CHALLENGE:

                if(response == null) {
                    Toast.makeText(ChildManageChallengesAchievedScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                            Toast.makeText(ChildManageChallengesAchievedScreen.this, message, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildManageChallengesAchievedScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

                break;
        }
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
            result = ContextCompat.checkSelfPermission(ChildManageChallengesAchievedScreen.this, p);
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
                    Toast.makeText(ChildManageChallengesAchievedScreen.this, "This feature cannot work without Permissions. \nTry Again and allow permissions or allow permissions in Applications Settings", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case CHOOSE_IMAGE:
                try {
                    selectedUri = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = ChildManageChallengesAchievedScreen.this.getContentResolver().query(selectedUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedImagePath = cursor.getString(columnIndex);
                    cursor.close();
                    File file = new File(selectedImagePath);
                    String nameFile = file.getName();
                    uploadPhotoVideo.setText(nameFile);


                    // showing thumbnail
                    thumbnail.setImageURI(selectedUri);
                    thumbnail.setVisibility(View.VISIBLE);

                }catch (Exception e){
//                    Toast.makeText(ChildMyGalleryAddNewPhotoScreen.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
                break;

            case CHOOSE_VIDEO:
                try {
                    selectedUri = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = ChildManageChallengesAchievedScreen.this.getContentResolver().query(selectedUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedImagePath = cursor.getString(columnIndex);
                    cursor.close();
                    File file = new File(selectedImagePath);
                    String nameFile = file.getName();
                    uploadPhotoVideo.setText(nameFile);

                    // showing thumbnail
                    Bitmap bMap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                    thumbnail.setImageBitmap(bMap);
                    thumbnail.setVisibility(View.VISIBLE);

                }catch (Exception e){
//                    Toast.makeText(ChildMyGalleryAddNewPhotoScreen.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private class submitChallenge extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog;
        Context context;

        public  submitChallenge(Context context){
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

            String uploadUrl = Utilities.BASE_URL + "challenges/save_challenge_score_by_child";
            String strResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(uploadUrl);

            httpPost.addHeader("X-access-uid", loggedInUser.getId());
            httpPost.addHeader("X-access-token", loggedInUser.getToken());

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(context));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            try {

//              String a = "{\"d\":\"" + daysStr + "\",\"h\":\"" + hoursStr + ",\"m\":\"" + minutesStr + ",\"s\":\"" + secondsStr + "\"}";
//                String achievedTime = daysStr+"Days"+""+hoursStr+"Hrs"+""+minutesStr+"Mins"+secondsStr+"Secs";
                String achievedTime = "";
                if(!daysStr.equalsIgnoreCase("")){
                    achievedTime = daysStr+" Days,";
                }

                if(!hoursStr.equalsIgnoreCase("")){
                    achievedTime = achievedTime + hoursStr+" Hrs,";
                }

                if(!minutesStr.equalsIgnoreCase("")){
                    achievedTime = achievedTime + minutesStr+" Mins,";
                }

                if(!secondsStr.equalsIgnoreCase("")){
                    achievedTime = achievedTime + secondsStr+" Secs";
                }

                //System.out.println("TimeFormatted:: "+achievedTime);


                for(int i=0; i<4; i++){
                    if (achievedTime != null && achievedTime.length() > 0 && achievedTime.charAt(achievedTime.length() - 1) == ',') {
                        achievedTime = achievedTime.substring(0, achievedTime.length() - 1);
                    }
                }

                //System.out.println("FinalTimeFormatted:: "+achievedTime);

                StringBody challengeId = new StringBody(challengeIdStr);
                StringBody achievedScore = new StringBody(""+scoreValue);
                StringBody achievedTimeValue = new StringBody(achievedTime);
                StringBody shareMessage = new StringBody(messageStr);
                StringBody isShared = new StringBody(isSharedStr);

//                StringBody dayBody = new StringBody(daysStr);
//                StringBody hourBody = new StringBody(hoursStr);
//                StringBody minBody = new StringBody(minutesStr);
//                StringBody secBody = new StringBody(secondsStr);

//                StringBody isShared;
//                if(isSharedStr.equalsIgnoreCase("true")){
//                     isShared = new StringBody("1");
//                }else{
//                     isShared = new StringBody("0");
//                }

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();

                builder.addPart("challenges_id", challengeId);
                builder.addPart("achieved_scores", achievedScore);
                builder.addPart("achieved_time", achievedTimeValue);
                builder.addPart("is_share", isShared);
                builder.addPart("share_msg", shareMessage);
//                builder.addPart("target_days", dayBody);
//                builder.addPart("target_hours", hourBody);
//                builder.addPart("target_minutes", minBody);
//                builder.addPart("target_seconds", secBody);

                if(selectedUri != null) {
                    String imageMime = getMimeType(selectedUri);
                    File imageFile = new File(selectedImagePath);
                    FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");

                    builder.addPart("share_file", imageFileBody);
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
                Toast.makeText(ChildManageChallengesAchievedScreen.this, "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    if(status){
                        Toast.makeText(ChildManageChallengesAchievedScreen.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(ChildManageChallengesAchievedScreen.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ChildManageChallengesAchievedScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }
}