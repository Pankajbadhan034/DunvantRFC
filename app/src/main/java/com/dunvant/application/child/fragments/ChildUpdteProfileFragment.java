package com.dunvant.application.child.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildProfileFieldsDataBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildMainScreen;
import com.dunvant.application.utils.CropOption;
import com.dunvant.application.utils.CropOptionAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.soundcloud.android.crop.Crop;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.dunvant.application.parent.ParentCreatePostScreen.MEDIA_TYPE_IMAGE;
import static com.dunvant.application.parent.ParentCreatePostScreen.MEDIA_TYPE_VIDEO;

/**
 * Created by DEVLABS\pbadhan on 6/12/16.
 */
public class ChildUpdteProfileFragment  extends Fragment implements IWebServiceCallback {
    private static final int CHOOSE_IMAGE_FROM_GALLERY = 1;
    private Uri fileUri = null;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    Uri selectedUri = null;
    String selectedImagePath;


    EditText medCond;
    EditText field1;
    EditText field2;
    EditText field3;
    EditText field5;
    TextInputLayout f1TIL;
    TextInputLayout f2TIL;
    TextInputLayout f3TIL;
    TextInputLayout f5TIL;
    TextInputLayout schoolTIL;
    TextInputLayout classTIL;
    TextInputLayout clubTIL;
    TextInputLayout natTIL;
    TextInputLayout medCondTIL;

    private final String UPDATE_PROFILE_FIELDS_DATA = "UPDATE_PROFILE_FIELDS_DATA";
    String firstImage="",secondImage="";
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private Uri mImageCaptureUri;
    Bitmap myBitmap;
    Bitmap profileBitmap;
    Bitmap playerBitmap;
    Bitmap teamBitmap;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    ImageView profilePic;
    TextView name;
    TextView gender;
    TextView userName;
    TextView uploadLabel;
    TextView nameLabel;
    TextView userNameLabel;
   // EditText userNameUpdate;
    EditText fav_player;
    EditText fav_team;
    EditText fav_position;
    EditText fav_footbal_boot;
    EditText fav_food;
    EditText school;
    EditText nationality;
    EditText club;
    EditText className;
    EditText height;
    EditText weight;
    ImageView favPlayerImage;
    ImageView favTeamImage;
    Button updateProfile;
    Button changePassword;
    String strUserNameUpdate,strFavPlayer,strFavTeam,strFavPosition,strFavFootbalBoot,strFavfood,strSchool,strClassName,strClub, strNationality,strHeight,strWeight,strf1, strf2, strf3, strf5, strMedCond;
    private final String UPDATE_PROFILE = "UPDATE_PROFILE";
    private final String CHANGE_PASSWORD = "CHANGE_PASSWORD";
    private Uri imageOutputUri;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    Typeface helvetica;
    Typeface linoType;
    String clickType;

    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(1000))
                .build();

        imageOutputUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "myImage_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LinotypeOrdinarRegular.ttf");
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_fragment_update_profile, container, false);
       // userNameUpdate = (EditText) view.findViewById(R.id.userNameUpdate);
        fav_player=(EditText) view.findViewById(R.id.fav_player);
        fav_team=(EditText) view.findViewById(R.id.fav_team);
        fav_position=(EditText) view.findViewById(R.id.fav_position);
        fav_footbal_boot = (EditText) view.findViewById(R.id.fav_footbal_boot);
        fav_food = (EditText) view.findViewById(R.id.fav_food);
        school = (EditText) view.findViewById(R.id.school);
        nationality = (EditText) view.findViewById(R.id.nationality);
        club = view.findViewById(R.id.club);
        className = view.findViewById(R.id.className);
        height=(EditText) view.findViewById(R.id.height);
        weight=(EditText) view.findViewById(R.id.weight);
        updateProfile=(Button) view.findViewById(R.id.updateProfile);
        changePassword = (Button) view.findViewById(R.id.changePassword);
        userNameLabel = (TextView) view.findViewById(R.id.userNameLabel);
        uploadLabel = (TextView) view.findViewById(R.id.uploadLabel);
        nameLabel = (TextView) view.findViewById(R.id.nameLabel);
        name = (TextView) view.findViewById(R.id.name);
        gender = (TextView) view.findViewById(R.id.gender);
        userName = (TextView) view.findViewById(R.id.userName);
        profilePic = (ImageView) view.findViewById(R.id.profilePic);
        favPlayerImage = (ImageView) view.findViewById(R.id.favPlayerImage);
        favTeamImage = (ImageView) view.findViewById(R.id.favTeamImage);
        field1 = view.findViewById(R.id.field1);
        field2 = view.findViewById(R.id.field2);
        field3 = view.findViewById(R.id.field3);
        field5 = view.findViewById(R.id.field5);
        medCond = view.findViewById(R.id.medCond);
        f1TIL = view.findViewById(R.id.f1TIL);
        f2TIL = view.findViewById(R.id.f2TIL);
        f3TIL = view.findViewById(R.id.f3TIL);
        f5TIL = view.findViewById(R.id.f5TIL);
        schoolTIL = view.findViewById(R.id.schoolTIL);
        classTIL = view.findViewById(R.id.classTIL);
        clubTIL = view.findViewById(R.id.clubTIL);
        natTIL = view.findViewById(R.id.natTIL);
        medCondTIL = view.findViewById(R.id.medCondTIL);

       // userNameUpdate.setText(loggedInUser.getUsername());
        strUserNameUpdate = loggedInUser.getUsername();
        name.setText(loggedInUser.getFullName());
        gender.setText("(" + loggedInUser.getGender() + ")");
        userName.setText(loggedInUser.getUsername());
//        System.out.println("PROFILE_PIC_URL::" + loggedInUser.getProfilePicPath());
        imageLoader.displayImage(loggedInUser.getProfilePicPath(), profilePic, options);
        fav_footbal_boot.setText(loggedInUser.getFavoriteFootballBoot());
        fav_food.setText(loggedInUser.getFavoritefood());
        school.setText(loggedInUser.getSchool());
        nationality.setText(loggedInUser.getNationality());
        club.setText(loggedInUser.getFieldCLub());
        className.setText(loggedInUser.getClassName());

        imageLoader.displayImage(loggedInUser.getFavoritePlayerPicture(), favPlayerImage, options);
        imageLoader.displayImage(loggedInUser.getFavoriteTemaPicture(), favTeamImage, options);

        fav_player.setTypeface(helvetica);
        fav_team.setTypeface(helvetica);
        fav_position.setTypeface(helvetica);
        height.setTypeface(helvetica);
        weight.setTypeface(helvetica);
        updateProfile.setTypeface(linoType);
        changePassword.setTypeface(linoType);
        userNameLabel.setTypeface(helvetica);
        uploadLabel.setTypeface(helvetica);
        userName.setTypeface(helvetica);

        nameLabel.setTypeface(helvetica);

        favPlayerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickType = "favPlayer";
                new CaptureImage();
            }
        });

        favTeamImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickType = "favTeam";
                new CaptureImage();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickType = "profilePic";
//                new CaptureImage();



                final String[] items = new String[]{"Take from camera", "Select from gallery"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, items);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Choose");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == 0) {

                            final String[] items = new String[]{"Image"};
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, items);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            builder.setTitle("Choose");
                            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {

                                    if (item == 0) {
                                        // Code to get image
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

                                    }
                                }
                            });
                            AlertDialog innerDialog = builder.create();
                            innerDialog.show();


                        } else {
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, CHOOSE_IMAGE_FROM_GALLERY);
                            dialog.cancel();

                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String webServiceUrl = Utilities.BASE_URL + "account/change_password_request_by_child";
                ArrayList<String> headers = new ArrayList<>();
                headers.add("X-access-uid:" + loggedInUser.getId());
                headers.add("X-access-token:" + loggedInUser.getToken());

                if (Utilities.isNetworkAvailable(getActivity())) {
                    GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), CHANGE_PASSWORD, ChildUpdteProfileFragment.this, headers);
                    getWebServiceWithHeadersAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }

            }
        });

        fav_player.setText(loggedInUser.getFavoritePlayer());
        fav_team.setText(loggedInUser.getFavoriteTeam());
        fav_position.setText(loggedInUser.getFavoritePosition());

        if(loggedInUser.getHeight().equalsIgnoreCase("null")){
            height.setText("");

        }else{
            height.setText(loggedInUser.getHeight());
        }

        if(loggedInUser.getWeight().equalsIgnoreCase("null")){
            weight.setText("");
        }else{
            weight.setText(loggedInUser.getWeight());
        }


        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //strUserNameUpdate = userNameUpdate.getText().toString();
                strFavPlayer=fav_player.getText().toString();
                strFavTeam=fav_team.getText().toString();
                strFavPosition=fav_position.getText().toString();
                strFavFootbalBoot = fav_footbal_boot.getText().toString();
                strFavfood = fav_food.getText().toString();
                strClassName = className.getText().toString();
                strHeight=height.getText().toString();
                strWeight=weight.getText().toString();
                strSchool = school.getText().toString();
                strNationality = nationality.getText().toString();
                strClub = club.getText().toString();
                strf1 = field1.getText().toString();
                strf2 = field2.getText().toString();
                strf3 = field3.getText().toString();
                strf5 = field5.getText().toString();
                strMedCond = medCond.getText().toString();

//                if(!(strUserNameUpdate == null || strUserNameUpdate.isEmpty())) {
//                    nameValuePairList.add(new BasicNameValuePair("username", strUserNameUpdate));
//                }
                if(!(strFavPlayer == null || strFavPlayer.isEmpty())) {
                    try {
                        StringBody strFavPlayerBody = new StringBody(strFavPlayer);
                        builder.addPart("favourite_player", strFavPlayerBody);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                if(!(strFavTeam == null || strFavTeam.isEmpty())){
                    try {
                        StringBody strFavTeamBody = new StringBody(strFavTeam);
                        builder.addPart("favourite_team", strFavTeamBody);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(!(strFavPosition == null || strFavPosition.isEmpty())){
                    try {
                        StringBody strFavPositionBody = new StringBody(strFavPosition);
                        builder.addPart("favourite_position", strFavPositionBody);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(!(strFavFootbalBoot == null || strFavFootbalBoot.isEmpty())){
                    try {
                        StringBody strFavFootbalBootBody = new StringBody(strFavFootbalBoot);
                        builder.addPart("favourite_football_boot", strFavFootbalBootBody);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(!(strFavfood == null || strFavfood.isEmpty())){
                    try {
                        StringBody strFavfoodBody = new StringBody(strFavfood);
                        builder.addPart("favourite_food", strFavfoodBody);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(!(strSchool == null || strSchool.isEmpty())){
                    try {
                        StringBody strSchoolBody = new StringBody(strSchool);
                        builder.addPart("school", strSchoolBody);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(!(strNationality == null || strNationality.isEmpty())){
                    try {
                        StringBody strNationalityBody = new StringBody(strNationality);
                        builder.addPart("nationality", strNationalityBody);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                if(!(strClub == null || strClub.isEmpty())){
                    try {
                        StringBody strClubBody = new StringBody(strClub);
                        builder.addPart("club", strClubBody);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                if(!(strClassName == null || strClassName.isEmpty())){
                    try {
                        StringBody strClassBody = new StringBody(strClassName);
                        builder.addPart("classname", strClassBody);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                if(!(strMedCond == null || strMedCond.isEmpty())){
                    try {
                        StringBody strMedCondBody = new StringBody(strMedCond);
                        builder.addPart("medical_conditions", strMedCondBody);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                if(!(strf1 == null || strf1.isEmpty())){
                    try {
                        StringBody strf1Body = new StringBody(strf1);
                        builder.addPart("field_1", strf1Body);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                if(!(strf2 == null || strf2.isEmpty())){
                    try {
                        StringBody strf2Body = new StringBody(strf2);
                        builder.addPart("field_2", strf2Body);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                if(!(strf3 == null || strf3.isEmpty())){
                    try {
                        StringBody strf3Body = new StringBody(strf3);
                        builder.addPart("field_3", strf3Body);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                if(!(strf5 == null || strf5.isEmpty())){
                    try {
                        StringBody strf5Body = new StringBody(strf5);
                        builder.addPart("field_5", strf5Body);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                if(!(strHeight == null || strHeight.isEmpty())){
                    try {
                        StringBody heightBody = new StringBody(strHeight);
                        builder.addPart("height", heightBody);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(!(strWeight == null || strWeight.isEmpty())){
                    try {
                        StringBody strWeightBody = new StringBody(strWeight);
                        builder.addPart("weight", strWeightBody);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }if(!firstImage.equalsIgnoreCase("") || firstImage.equalsIgnoreCase("firstImage")){
//                    System.out.println("firstImage"+firstImage);
                    try{
                        String mime = "image/jpg";
//                        System.out.println("Mime "+mime);

//                File file = new File(documentBean.getFilePath());

                        File file1 = new File(getActivity().getCacheDir(), "image_1"+System.currentTimeMillis()+".jpg");
                        file1.createNewFile();

//                        System.out.println("Cache Dir 1"+getActivity().getCacheDir());

                        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
                        playerBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos1);
                        byte[] bitmapdata1 = bos1.toByteArray();

                        //write the bytes in file
                        FileOutputStream fos1 = new FileOutputStream(file1);
                        fos1.write(bitmapdata1);
                        fos1.flush();
                        fos1.close();

                        FileBody fileBody1 = new FileBody(file1, file1.getName(), mime, "UTF-8");

//                        System.out.println("File Name "+file1.getName());
//                        System.out.println("File Path " + file1.getPath());


                        builder.addPart("favourite_player_picture", fileBody1);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                if( !secondImage.equalsIgnoreCase("") || secondImage.equalsIgnoreCase("secondImage")){
//                    System.out.println("secondImage"+secondImage);
                    try{
                        String mime = "image/jpg";
//                        System.out.println("Mime "+mime);

//                File file = new File(documentBean.getFilePath());

                        File file1 = new File(getActivity().getCacheDir(), "image_1"+System.currentTimeMillis()+".jpg");
                        file1.createNewFile();

//                        System.out.println("Cache Dir 2"+getActivity().getCacheDir());

                        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
                        teamBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos1);
                        byte[] bitmapdata1 = bos1.toByteArray();

                        //write the bytes in file
                        FileOutputStream fos1 = new FileOutputStream(file1);
                        fos1.write(bitmapdata1);
                        fos1.flush();
                        fos1.close();

                        FileBody fileBody1 = new FileBody(file1, file1.getName(), mime, "UTF-8");

//                        System.out.println("File Name "+file1.getName());
//                        System.out.println("File Path " + file1.getPath());



                        builder.addPart("favourite_team_picture", fileBody1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(Utilities.isNetworkAvailable(getActivity())) {
                    new UpdateProfileAsync(getActivity()).execute();

//                        String webServiceUrl = Utilities.BASE_URL + "account/update_child";
//
//                        ArrayList<String> headers = new ArrayList<>();
//                        headers.add("X-access-uid:"+loggedInUser.getId());
//                        headers.add("X-access-token:"+loggedInUser.getToken());
//
//                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, UPDATE_PROFILE, ChildUpdteProfileFragment.this, headers);
//                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }

            }
        });

        updateProfileData();

        return view;
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case UPDATE_PROFILE:
                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            loggedInUser.setFavoritePlayer(strFavPlayer);
                            loggedInUser.setFavoriteTeam(strFavTeam);
                            loggedInUser.setFavoritePosition(strFavPosition);
                            loggedInUser.setHeight(strHeight);
                            loggedInUser.setWeight(strWeight);
                            loggedInUser.setHeightNumeric(strHeight);
                            loggedInUser.setWeightNumeric(strWeight);
                            loggedInUser.setUsername(strUserNameUpdate);
                            loggedInUser.setFavoriteFootballBoot(strFavFootbalBoot);
                            loggedInUser.setFavoritefood(strFavfood);
                            loggedInUser.setSchool(strSchool);
                            loggedInUser.setNationality(strNationality);
                            loggedInUser.setFieldCLub(strClub);
                            loggedInUser.setClassName(strClassName);

                            loggedInUser.setHeightFormatted(strHeight + " cm");
//                            System.out.println("weightHere::" + strWeight + " kg");
                            loggedInUser.setWeightFormatted(strWeight + " kg");

                            Gson gson = new Gson();
                            String jsonParentUserBean = gson.toJson(loggedInUser);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("loggedInUser", jsonParentUserBean);
                            editor.commit();
                        }

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;
            case CHANGE_PASSWORD:
                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case UPDATE_PROFILE_FIELDS_DATA:

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray jsonArray = responseObject.getJSONArray("data_registration");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ChildProfileFieldsDataBean childProfileFieldsDataBean = new ChildProfileFieldsDataBean();
                                childProfileFieldsDataBean.setLabelName(jsonObject.getString("label_name"));
                                childProfileFieldsDataBean.setValue(jsonObject.getString("value"));
                                childProfileFieldsDataBean.setLabelType(jsonObject.getString("label_type"));
                                childProfileFieldsDataBean.setSlug(jsonObject.getString("slug"));

                                if(jsonObject.getString("label_type").equalsIgnoreCase("2")){
                                    childProfileFieldsDataBean.setButtonName(jsonObject.getString("button_name"));
                                    childProfileFieldsDataBean.setLabelMultiField(jsonObject.getString("label_multiple_field"));
                                }else{
                                    childProfileFieldsDataBean.setButtonName("");
                                    childProfileFieldsDataBean.setLabelMultiField("");
                                }

                                if(jsonObject.getString("slug").equalsIgnoreCase("classname")){
                                    classTIL.setVisibility(View.VISIBLE);
                                    classTIL.setHint(jsonObject.getString("label_name"));
                                    className.setText(jsonObject.getString("value"));
                                }else if(jsonObject.getString("slug").equalsIgnoreCase("school")){
                                    schoolTIL.setVisibility(View.VISIBLE);
                                    schoolTIL.setHint(jsonObject.getString("label_name"));
                                    school.setText(jsonObject.getString("value"));
                                }else if(jsonObject.getString("slug").equalsIgnoreCase("club")){
                                    clubTIL.setVisibility(View.VISIBLE);
                                    clubTIL.setHint(jsonObject.getString("label_name"));
                                    club.setText(jsonObject.getString("value"));
                                }else if(jsonObject.getString("slug").equalsIgnoreCase("nationality")){
                                    natTIL.setVisibility(View.VISIBLE);
                                    natTIL.setHint(jsonObject.getString("label_name"));
                                    nationality.setText(jsonObject.getString("value"));
                                }else if(jsonObject.getString("slug").equalsIgnoreCase("medical_conditions")){
                                    medCondTIL.setVisibility(View.VISIBLE);
                                    medCondTIL.setHint(jsonObject.getString("label_name"));
                                    medCond.setText(jsonObject.getString("value"));
                                }else if(jsonObject.getString("slug").equalsIgnoreCase("field_1")){
                                    f1TIL.setVisibility(View.VISIBLE);
                                    f1TIL.setHint(jsonObject.getString("label_name"));
                                    field1.setText(jsonObject.getString("value"));
                                }else if(jsonObject.getString("slug").equalsIgnoreCase("field_2")){
                                    f2TIL.setVisibility(View.VISIBLE);
                                    f2TIL.setHint(jsonObject.getString("label_name"));
                                    field2.setText(jsonObject.getString("value"));
                                }else if(jsonObject.getString("slug").equalsIgnoreCase("field_3")){
                                    f3TIL.setVisibility(View.VISIBLE);
                                    f3TIL.setHint(jsonObject.getString("label_name"));
                                    field3.setText(jsonObject.getString("value"));
                                }else if(jsonObject.getString("slug").equalsIgnoreCase("field_5")){
                                    f5TIL.setVisibility(View.VISIBLE);
                                    f5TIL.setHint(jsonObject.getString("label_name"));
                                    field5.setText(jsonObject.getString("value"));
                                }


                            }
                        } else {
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

    class CaptureImage {
        final String[] items = new String[] { "Take from camera", "Select from gallery" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        CaptureImage() {
            builder.setTitle("Select Image");
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {

                    if (item == 0) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                        try {
                            intent.putExtra("return-data", true);
                            startActivityForResult(intent, PICK_FROM_CAMERA);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {

                        Intent intent;

                        if (Build.VERSION.SDK_INT < 19) {
                            intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("*/*");
                            startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                        } else {
                            intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("*/*");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                            startActivityForResult(Intent.createChooser(intent,"Complete action using"), PICK_FROM_FILE);
                        }
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(getActivity(), "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            if (mImageCaptureUri != null) {
                intent.setData(mImageCaptureUri);
            }

            intent.putExtra("outputX", 500);
            intent.putExtra("outputY", 500);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", false);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getActivity(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageCaptureUri != null) {
                            getActivity().getContentResolver().delete(mImageCaptureUri, null, null);
                            mImageCaptureUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK)
            return;

        switch (requestCode) {
            case CHOOSE_IMAGE_FROM_GALLERY:
                try {
                    selectedUri = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedImagePath = cursor.getString(columnIndex);
                    cursor.close();
                    File file = new File(selectedImagePath);
                    String nameFile = file.getName();


                    // showing thumbnail
                    profilePic.setImageURI(selectedUri);
                    new UpdateImageAsync(getActivity()).execute();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    try {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;

//                        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);

                        // showing thumbnail
                        profilePic.setImageURI(fileUri);

                        new UpdateImageAsync(getActivity()).execute();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
//                    Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
                } else {
                    // failed to capture image
                    Toast.makeText(getActivity(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
                }
                break;

//            case PICK_FROM_CAMERA:
//                doCrop();
//
////                Crop.of(mImageCaptureUri, imageOutputUri).asSquare().start(getActivity());
//
//                break;
            case PICK_FROM_FILE:
                if (Build.VERSION.SDK_INT < 19) {
                    mImageCaptureUri = data.getData();

                } else {
                    mImageCaptureUri = data.getData();
                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(mImageCaptureUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        myBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                doCrop();

//                Crop.of(mImageCaptureUri, imageOutputUri).asSquare().start(getActivity());

                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();

                if (extras != null) {
                    myBitmap = extras.getParcelable("data");
                }

                /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArrayImage = stream.toByteArray();
                encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);*/

               if(clickType.equalsIgnoreCase("favPlayer")){
                   firstImage="firstImage";
                   playerBitmap = myBitmap;
                   favPlayerImage.setImageBitmap(playerBitmap);
               }else if(clickType.equalsIgnoreCase("favTeam")){
                   secondImage="secondImage";
                   teamBitmap = myBitmap;
                   favTeamImage.setImageBitmap(teamBitmap);
               }else{
                   profileBitmap = myBitmap;
                   profilePic.setImageBitmap(profileBitmap);
                   new UploadProfilePicAsync(getActivity()).execute();
               }

                break;
            case Crop.REQUEST_CROP:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(clickType.equalsIgnoreCase("favPlayer")){
                                    favPlayerImage.setImageURI(imageOutputUri);
                                }else if(clickType.equalsIgnoreCase("favTeam")){
                                    favTeamImage.setImageURI(imageOutputUri);
                                }else{
                                    profilePic.setImageURI(imageOutputUri);
                                }


                            }
                        });
                    }
                }, 3000);


                /*try {
                    myBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageOutputUri);
                    profilePhoto.setImageBitmap(myBitmap);
                }catch (Exception e){
                    e.getMessage();
                }*/

                break;

        }
    }

    private class UploadProfilePicAsync extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog;
        Context context;

        public UploadProfilePicAsync(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pDialog = Utilities.createProgressDialog(context);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... voids) {

//            System.out.println("Uploading starting");

            String uploadUrl = Utilities.BASE_URL + "account/change_profile_picture";
            String strResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(uploadUrl);

            httpPost.addHeader("X-access-uid", loggedInUser.getId());
            httpPost.addHeader("X-access-token", loggedInUser.getToken());

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(getActivity()));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            try {

                String mime = "image/jpg";
//                System.out.println("Mime "+mime);

//                File file = new File(documentBean.getFilePath());

                File file = new File(context.getCacheDir(), "image_"+System.currentTimeMillis()+".jpg");
                file.createNewFile();

//                System.out.println("Cache Dir "+context.getCacheDir());

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                profileBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();

                FileBody fileBody = new FileBody(file, file.getName(), mime, "UTF-8");

//                System.out.println("File Name "+file.getName());
//                System.out.println("File Path "+file.getPath());

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                builder.addPart("profile_picture", fileBody);

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

//                System.out.println("File upload end");

            } catch (Exception e) {
                e.printStackTrace();
//                System.out.println("Exception "+e.getMessage());
            }

            return strResponse;

        }

        @Override
        protected void onPostExecute(String response) {

//            System.out.println("Response "+response);

            if(response == null) {
                Toast.makeText(getActivity(), "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    if(status) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        String imagePath = responseObject.getString("profile_picture_path");
                        loggedInUser.setProfilePicPath(imagePath);

                        Gson gson = new Gson();
                        String jsonParentUserBean = gson.toJson(loggedInUser);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("loggedInUser", jsonParentUserBean);
                        editor.commit();

                       // ((ParentMainScreen) getActivity()).updateUser();
                        ((ChildMainScreen) getActivity()).updateUser();

                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if(pDialog != null && pDialog.isShowing()){
                pDialog.dismiss();
            }
        }
    }



    private class UpdateProfileAsync extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog;
        Context context;

        public UpdateProfileAsync(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pDialog = Utilities.createProgressDialog(context);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... voids) {

//            System.out.println("Uploading starting");

            String uploadUrl = Utilities.BASE_URL + "account/update_child";
            String strResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(uploadUrl);

            httpPost.addHeader("X-access-uid", loggedInUser.getId());
            httpPost.addHeader("X-access-token", loggedInUser.getToken());

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(getActivity()));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            try {



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

//                System.out.println("File upload end");

            } catch (Exception e) {
                e.printStackTrace();
//                System.out.println("Exception "+e.getMessage());
            }

            return strResponse;

        }

        @Override
        protected void onPostExecute(String response) {

//            System.out.println("Response "+response);

            if(response == null) {
                Toast.makeText(getActivity(), "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    System.out.println("response::"+responseObject);

                    if (status) {
                        loggedInUser.setFavoritePlayer(strFavPlayer);
                        loggedInUser.setFavoriteTeam(strFavTeam);
                        loggedInUser.setFavoritePosition(strFavPosition);
                        loggedInUser.setHeight(strHeight);
                        loggedInUser.setWeight(strWeight);
                        loggedInUser.setHeightNumeric(strHeight);
                        loggedInUser.setWeightNumeric(strWeight);
                        loggedInUser.setUsername(strUserNameUpdate);
                        loggedInUser.setFavoriteFootballBoot(strFavFootbalBoot);
                        loggedInUser.setFavoritefood(strFavfood);
                        loggedInUser.setSchool(strSchool);
                        loggedInUser.setNationality(strNationality);
                        loggedInUser.setFieldCLub(strClub);
                        loggedInUser.setClassName(strClassName);

                        loggedInUser.setHeightFormatted(strHeight + " cm");
//                        System.out.println("weightHere::" + strWeight + " kg");
                        loggedInUser.setWeightFormatted(strWeight + " kg");

                        JSONObject dataObject = new JSONObject(responseObject.getString("data"));
                        String playerPic = dataObject.getString("favourite_player_picture");
                        String favTeamPic = dataObject.getString("favourite_team_picture");

                        loggedInUser.setFavoritePlayerPicture(playerPic);
                        loggedInUser.setFavoriteTemaPicture(favTeamPic);

                        Gson gson = new Gson();
                        String jsonParentUserBean = gson.toJson(loggedInUser);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("loggedInUser", jsonParentUserBean);
                        editor.commit();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if(pDialog != null && pDialog.isShowing()){
                pDialog.dismiss();
            }
        }
    }


    private void updateProfileData() {
        if (Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "children/child_info_with_reg";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());



            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), UPDATE_PROFILE_FIELDS_DATA, ChildUpdteProfileFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
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

    private class UpdateImageAsync extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog;
        Context context;

        public UpdateImageAsync(Context context) {
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

            String uploadUrl = Utilities.BASE_URL + "account/change_profile_picture";
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


                if (selectedUri != null) {
                    String imageMime = getMimeType(selectedUri);

                    File imageFile = new File(selectedImagePath);

                    //System.out.println("MIME_type__" + imageMime + "--Path__" + selectedImagePath + "--FileName__" + imageFile.getName());

                    if(imageFile != null){
                        FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");
                        builder.addPart("profile_picture", imageFileBody);
                    }

                } else if (fileUri != null) {

                    String fileMime = getMimeType(fileUri);

                    File file = new File(fileUri.getPath());
                    if(file != null){
                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
                        builder.addPart("profile_picture", imageFileBody);
                    }
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
                //System.out.println("Exception " + e.getMessage());
            }

            return strResponse;

        }

        @Override
        protected void onPostExecute(String response) {

            //System.out.println("Response " + response);

            if (response == null) {
                Toast.makeText(getActivity(), "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    if (status) {

//                        getActivity().finish();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        String imagePath = responseObject.getString("profile_picture_path");
                        loggedInUser.setProfilePicPath(imagePath);

                        imageLoader.displayImage(loggedInUser.getProfilePicPath(), profilePic, options);

                        Gson gson = new Gson();
                        String jsonParentUserBean = gson.toJson(loggedInUser);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("loggedInUser", jsonParentUserBean);
                        editor.commit();

                        ((ChildMainScreen) getActivity()).updateUser();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
            ContentResolver cr = getActivity().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

}
