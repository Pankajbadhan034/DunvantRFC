package com.dunvant.application.parent;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.DataRegistrationBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentChildDetailAdapter;
import com.dunvant.application.startModule.StartModuleDashboardScreen;
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

public class ParentChildDetailScreen extends AppCompatActivity implements IWebServiceCallback{
    ArrayList<DataRegistrationBean> dataRegistrationBeanArrayList = new ArrayList<>();
    ListView list;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    TextView lblUsername;
    TextView username;
    TextView lblFullName;
    TextView fullName;
    TextView lblDOB;
    TextView dateOfBirth;
    TextView lblGender;
    TextView gender;

    TextView lblSchool;
    TextView school;
    TextView lblHeight;
    TextView height;
    TextView lblWeight;
    TextView weight;
    TextView lblFavPos;
    TextView favPos;
    TextView lblFootballBoot;
    TextView favFootballBoot;
    TextView lblFavFood;
    TextView favFood;
    TextView lblFavPlayer;
    TextView favPlayer;
    ImageView favPlayerImage;
    TextView lblFavTeam;
    TextView favTeam;
    ImageView favTeamImage;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    TextView lblMedicalCondition;
    TextView medicalCondition;
    TextView className;
    TextView lblClassName;
    TextView lblFieldClub;
    TextView fieldClub;
    ImageView edit;
    ImageView delete;
    TextView loginAsChild;
    TextView goToTimeline;
    TextView goToIFACareer;

    ChildBean currentChild;

    private final String GET_CHILD_DETAIL = "GET_CHILD_DETAIL";
    private final String DELETE_CHILD = "DELETE_CHILD";
    private final String LOGIN_AS_CHILD = "LOGIN_AS_CHILD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_child_detail_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentChildDetailScreen.this));
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

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        lblUsername = findViewById(R.id.lblUsername);
        username = findViewById(R.id.username);
        lblFullName = (TextView) findViewById(R.id.lblFullName);
        fullName = (TextView) findViewById(R.id.fullName);
        lblDOB = (TextView) findViewById(R.id.lblDOB);
        dateOfBirth = (TextView) findViewById(R.id.dateOfBirth);
        lblGender = (TextView) findViewById(R.id.lblGender);
        gender = (TextView) findViewById(R.id.gender);
        lblMedicalCondition = (TextView) findViewById(R.id.lblMedicalCondition);
        medicalCondition = (TextView) findViewById(R.id.medicalCondition);
        edit = (ImageView) findViewById(R.id.edit);
        delete = (ImageView) findViewById(R.id.delete);
        loginAsChild = findViewById(R.id.loginAsChild);
        goToTimeline = (TextView) findViewById(R.id.goToTimeline);
        goToIFACareer = (TextView) findViewById(R.id.goToIFACareer);
        className = findViewById(R.id.className);
        lblClassName = findViewById(R.id.lblClassName);
        fieldClub = findViewById(R.id.fieldClub);
        lblFieldClub = findViewById(R.id.lblFieldClub);

        lblSchool = (TextView) findViewById(R.id.lblSchool);
        school = (TextView) findViewById(R.id.school);
        lblHeight = (TextView) findViewById(R.id.lblHeight);
        height = (TextView) findViewById(R.id.height);
        lblWeight = (TextView) findViewById(R.id.lblWeight);
        weight = (TextView) findViewById(R.id.weight);
        lblFavPos = (TextView) findViewById(R.id.lblFavPos);
        favPos = (TextView) findViewById(R.id.favPos);
        lblFootballBoot = (TextView) findViewById(R.id.lblFootballBoot);
        favFootballBoot = (TextView) findViewById(R.id.favFootballBoot);
        lblFavFood = (TextView) findViewById(R.id.lblFavFood);
        favFood = (TextView) findViewById(R.id.favFood);
        lblFavPlayer = (TextView) findViewById(R.id.lblFavPlayer);
        favPlayer = (TextView) findViewById(R.id.favPlayer);
        favPlayerImage = (ImageView) findViewById(R.id.favPlayerImage);
        lblFavTeam = (TextView) findViewById(R.id.lblFavTeam);
        favTeam = (TextView) findViewById(R.id.favTeam);
        favTeamImage = (ImageView) findViewById(R.id.favTeamImage);
        list = findViewById(R.id.list);

        changeFonts();

        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
        title.setText(verbiage_singular.toUpperCase()+" INFO");
        loginAsChild.setText("LOGIN AS "+verbiage_singular.toUpperCase());

        Intent intent = getIntent();
        if(intent != null){
            currentChild = (ChildBean) intent.getSerializableExtra("currentChild");
            username.setText(currentChild.getUsername());
            fullName.setText(currentChild.getFullName());
            dateOfBirth.setText(currentChild.getDateOfBirth());
            gender.setText(currentChild.getGender());
            medicalCondition.setText(currentChild.getMedicalCondition());
        } else {
            fullName.setText("");
            dateOfBirth.setText("");
            gender.setText("");
            medicalCondition.setText("");
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editChild = new Intent(ParentChildDetailScreen.this, ParentAddChildScreen.class);
                editChild.putExtra("isEditMode", true);
                editChild.putExtra("currentChild", currentChild);
                startActivity(editChild);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ParentChildDetailScreen.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.parent_dialog_delete_child);

                TextView areYouSure = (TextView) dialog.findViewById(R.id.areYouSure);
                TextView youWantToDelete = (TextView) dialog.findViewById(R.id.youWantToDelete);
                TextView yes = (TextView) dialog.findViewById(R.id.yes);
                TextView no = (TextView) dialog.findViewById(R.id.no);

                areYouSure.setTypeface(linoType);
                youWantToDelete.setTypeface(helvetica);
                yes.setTypeface(linoType);
                no.setTypeface(linoType);

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        if(Utilities.isNetworkAvailable(ParentChildDetailScreen.this)) {

                            List<NameValuePair> nameValuePairList = new ArrayList<>();
                            nameValuePairList.add(new BasicNameValuePair("child_id", currentChild.getId()));

                            String webServiceUrl = Utilities.BASE_URL + "children/delete";

                            ArrayList<String> headers = new ArrayList<>();
                            headers.add("X-access-uid:"+loggedInUser.getId());
                            headers.add("X-access-token:"+loggedInUser.getToken());

                            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentChildDetailScreen.this, nameValuePairList, DELETE_CHILD, ParentChildDetailScreen.this, headers);
                            postWebServiceAsync.execute(webServiceUrl);

                        } else {
                            Toast.makeText(ParentChildDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }
        });

        loginAsChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utilities.isNetworkAvailable(ParentChildDetailScreen.this)) {

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("child_id", currentChild.getId()));

                    String webServiceUrl = Utilities.BASE_URL + "account/login_as_child";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());


                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentChildDetailScreen.this, nameValuePairList, LOGIN_AS_CHILD, ParentChildDetailScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(ParentChildDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        goToTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timeline = new Intent(ParentChildDetailScreen.this, ParentChildTimeline.class);
                timeline.putExtra("currentChild", currentChild);
                timeline.putExtra("type", "timeline");
                startActivity(timeline);
            }
        });

        goToIFACareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timeline = new Intent(ParentChildDetailScreen.this, ParentChildTimeline.class);
                timeline.putExtra("currentChild", currentChild);
                timeline.putExtra("type", "career");
                startActivity(timeline);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Utilities.isNetworkAvailable(ParentChildDetailScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("child_id", currentChild.getId()));

            String webServiceUrl = Utilities.BASE_URL + "children/single_child";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentChildDetailScreen.this, nameValuePairList, GET_CHILD_DETAIL, ParentChildDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentChildDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_CHILD_DETAIL:

                if (response == null) {
                    Toast.makeText(ParentChildDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONObject childObject = responseObject.getJSONObject("data");

                            currentChild.setId(childObject.getString("id"));
                            currentChild.setAcademiesId(childObject.getString("academies_id"));
                            currentChild.setUsername(childObject.getString("username"));
                            currentChild.setEmail(childObject.getString("email"));
                            currentChild.setChildEmail(childObject.getString("email_address"));
                            currentChild.setGender(childObject.getString("gender"));
                            currentChild.setCreatedAt(childObject.getString("created_at"));
                            currentChild.setState(childObject.getString("state"));
                            currentChild.setFirstName(childObject.getString("first_name"));
                            currentChild.setLastName(childObject.getString("last_name"));
                            currentChild.setFullName(childObject.getString("full_name"));
                            currentChild.setAge(childObject.getString("age"));
                            currentChild.setDateOfBirth(childObject.getString("dob"));
                            currentChild.setMedicalCondition(childObject.getString("medical_conditions"));
                            currentChild.setIsPrivate(childObject.getString("is_private"));
                            currentChild.setProfilePicture(childObject.getString("profile_picture"));
                            currentChild.setProfilePicUrl(childObject.getString("profile_pic_url"));

                            currentChild.setSchool(childObject.getString("school"));
                            currentChild.setFavPlayer(childObject.getString("favourite_player"));
                            currentChild.setFavTeam(childObject.getString("favourite_team"));
                            currentChild.setFavPosition(childObject.getString("favourite_position"));
                            currentChild.setFavFootballBoot(childObject.getString("favourite_football_boot"));
                            currentChild.setFavFood(childObject.getString("favourite_food"));
                            currentChild.setNationality(childObject.getString("nationality"));

                            currentChild.setFavPlayerPicture(childObject.getString("favourite_player_picture"));
                            currentChild.setFavTeamPicture(childObject.getString("favourite_team_picture"));
                            currentChild.setHeight(childObject.getString("height"));
                            currentChild.setWeight(childObject.getString("weight"));

                            currentChild.setGenderValue(childObject.getString("gender_value"));
                            currentChild.setDateOfBirthFormatted(childObject.getString("date_of_birth"));

                            currentChild.setChildClass(childObject.getString("classname"));
                            currentChild.setFieldClub(childObject.getString("club"));
                            currentChild.setField1(childObject.getString("field_1"));
                            currentChild.setField2(childObject.getString("field_2"));
                            currentChild.setField3(childObject.getString("field_3"));
                            currentChild.setField4(childObject.getString("field_4"));
                            currentChild.setField5(childObject.getString("field_5"));


                            username.setText(currentChild.getUsername());
                            fullName.setText(currentChild.getFullName());
                            dateOfBirth.setText(currentChild.getDateOfBirthFormatted());
                            gender.setText(currentChild.getGender());
                            medicalCondition.setText(currentChild.getMedicalCondition());
                            className.setText(currentChild.getChildClass());
                            fieldClub.setText(currentChild.getFieldClub());

                            school.setText(currentChild.getSchool());
                            height.setText(currentChild.getHeight());
                            weight.setText(currentChild.getWeight());
                            favPos.setText(currentChild.getFavPosition());
                            favFootballBoot.setText(currentChild.getFavFootballBoot());
                            favFood.setText(currentChild.getFavFood());
                            favPlayer.setText(currentChild.getFavPlayer());
                            favTeam.setText(currentChild.getFavTeam());



                            imageLoader.displayImage(currentChild.getFavPlayerPicture(), favPlayerImage, options);
                            imageLoader.displayImage(currentChild.getFavTeamPicture(), favTeamImage, options);

                            dataRegistrationBeanArrayList.clear();
                            JSONArray data_registration = responseObject.getJSONArray("data_registration");
                            for(int i=0; i<data_registration.length(); i++){
                                DataRegistrationBean dataRegistrationBean = new DataRegistrationBean();
                                JSONObject jsonObject = data_registration.getJSONObject(i);
                                dataRegistrationBean.setLabel_name(jsonObject.getString("label_name"));
                                dataRegistrationBean.setValue(jsonObject.getString("value"));

                                dataRegistrationBeanArrayList.add(dataRegistrationBean);
                            }

                            ParentChildDetailAdapter parentChildDetailAdapter = new ParentChildDetailAdapter(ParentChildDetailScreen.this, dataRegistrationBeanArrayList);
                            list.setAdapter(parentChildDetailAdapter);
                            Utilities.setListViewHeightBasedOnChildren(list);

                        } else {
                            Toast.makeText(ParentChildDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentChildDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case DELETE_CHILD:

                if (response == null) {
                    Toast.makeText(ParentChildDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(ParentChildDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        if (status) {
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentChildDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case LOGIN_AS_CHILD:

                if (response == null) {
                    Toast.makeText(ParentChildDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            String token = responseObject.getString("token");
                            JSONObject userData = responseObject.getJSONObject("user_data");

                            UserBean userBean = new UserBean();
                            userBean.setToken(token);
                            userBean.setId(userData.getString("id"));
                            userBean.setAcademiesId(userData.getString("academies_id"));
                            userBean.setUsername(userData.getString("username"));
                            userBean.setEmail(userData.getString("email"));
                            userBean.setGender(userData.getString("gender"));
//                            userBean.setCreatedAt(userData.getString("created_at"));
//                            userBean.setState(userData.getString("state"));
                            userBean.setFirstName(userData.getString("fname"));
                            userBean.setLastName(userData.getString("lname"));
                            userBean.setFullName(userData.getString("full_name"));
                            userBean.setMobileNumber(userData.getString("phone_1"));
                            userBean.setSecondMobileNumber(userData.getString("phone_2"));
//                            userBean.setTotalChildren(userData.getInt("total_children"));
                            userBean.setRoleCode(userData.getString("role_code"));
                            userBean.setAddress(userData.getString("address"));
                            userBean.setProfilePicPath(userData.getString("profile_picture_path"));

                            //added for child module
                            if (userBean.getRoleCode().equalsIgnoreCase("child_role")) {
                                userBean.setFavoritePlayer(userData.getString("favourite_player"));
                                userBean.setFavoriteTeam(userData.getString("favourite_team"));
                                userBean.setFavoritePosition(userData.getString("favourite_position"));
                                userBean.setFavoriteFootballBoot(userData.getString("favourite_football_boot"));
                                userBean.setFavoritefood(userData.getString("favourite_food"));
                                userBean.setSchool(userData.getString("school"));
                                userBean.setNationality(userData.getString("nationality"));
                                userBean.setHeight(userData.getString("height"));
                                userBean.setWeight(userData.getString("weight"));
                                userBean.setDobformatted(userData.getString("dob_formatted"));

                                userBean.setHeightNumeric(userData.getString("height"));
                                userBean.setWeightNumeric(userData.getString("weight"));
                                userBean.setFavoritePlayerPicture(userData.getString("favourite_player_picture"));
                                userBean.setFavoriteTemaPicture(userData.getString("favourite_team_picture"));
                                userBean.setHeightFormatted(userData.getString("height_formatted"));
                                userBean.setWeightFormatted(userData.getString("weight_formatted"));

                                userBean.setParentUsername(userData.getString("parent_username"));
                                userBean.setAge(userData.getString("age"));

                                userBean.setFieldCLub(userData.getString("club"));
                                userBean.setClassName(userData.getString("classname"));

                                // userBean.setCategoryId(userData.getString("category_id"));
                            }

                            // added for Coach Module
                            if(userBean.getRoleCode().equalsIgnoreCase("coach_role")){
                                userBean.setCanMoveChild(userData.getString("can_move_child"));
                            }

                            userBean.setPhoneCodeOne(userData.getString("phone_code_1"));
                            userBean.setPhoneCodeTwo(userData.getString("phone_code_2"));

                            if(userData.has("user_type")){
                                if(userData.getString("user_type").equalsIgnoreCase("5")){
                                    userBean.setUser_type(userData.getString("user_type"));
                                    userBean.setRoleCode("child_role");
                                }else{
                                    userBean.setUser_type(userData.getString("user_type"));
                                }

                            }else{
                                userBean.setUser_type("");
                            }

                            Gson gson = new Gson();
                            String jsonParentUserBean = gson.toJson(userBean);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isUserLoggedIn", true);
                            editor.putBoolean("guestUser", true);
                            editor.putString("cUserName", userBean.getFullName());
                            editor.putString("cEmail", userBean.getEmail());
                            editor.putString("cPhone", userBean.getMobileNumber());

                            if (userBean.getRoleCode().equalsIgnoreCase("parent_role")) {
                                editor.putString("typeOfUser", "parent");
                                if(userData.has("academy_currency")){
                                    editor.putString("academy_currency", userData.getString("academy_currency"));
                                    editor.putString("verbiage_singular", userData.getString("verbiage_singular"));
                                    editor.putString("verbiage_plural", userData.getString("verbiage_plural"));
                                    editor.putString("day_start_num", userData.getString("day_start_num"));
                                }
                            } else if (userBean.getRoleCode().equalsIgnoreCase("child_role")) {
                                editor.putString("typeOfUser", "child");
                                if(userData.has("club")){
                                    editor.putString("club", userData.getString("club"));
                                    editor.putString("classname", userData.getString("classname"));
                                }
                                if(userData.has("academy_currency")){
                                    editor.putString("academy_currency", userData.getString("academy_currency"));
                                    editor.putString("verbiage_singular", userData.getString("verbiage_singular"));
                                    editor.putString("verbiage_plural", userData.getString("verbiage_plural"));
                                    editor.putString("day_start_num", userData.getString("day_start_num"));
                                }
                            } else if (userBean.getRoleCode().equalsIgnoreCase("coach_role")) {
                                editor.putString("typeOfUser", "coach");
                                if(userData.has("academy_currency")){
                                    editor.putString("academy_currency", userData.getString("academy_currency"));
                                    editor.putString("verbiage_singular", userData.getString("verbiage_singular"));
                                    editor.putString("verbiage_plural", userData.getString("verbiage_plural"));
                                    editor.putString("day_start_num", userData.getString("day_start_num"));
                                }
                            }

                            editor.putString("loggedInUser", jsonParentUserBean);
                            editor.commit();

                            switch (userBean.getRoleCode()) {
                                case "child_role":

//                                    Intent childDashboard = new Intent(ParentChildDetailScreen.this, ChildMainScreen.class);
//                                    childDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(childDashboard);

                                    Intent childDashboard = new Intent(ParentChildDetailScreen.this, StartModuleDashboardScreen.class);
                                    childDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(childDashboard);

                                    break;
                            }


                        } else {
                            Toast.makeText(ParentChildDetailScreen.this, message, Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentChildDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void changeFonts(){
        title.setTypeface(linoType);
        lblUsername.setTypeface(linoType);
        username.setTypeface(helvetica);
        lblFullName.setTypeface(linoType);
        fullName.setTypeface(helvetica);
        lblDOB.setTypeface(linoType);
        dateOfBirth.setTypeface(helvetica);
        lblGender.setTypeface(linoType);
        gender.setTypeface(helvetica);
        lblMedicalCondition.setTypeface(linoType);
        medicalCondition.setTypeface(helvetica);
        lblClassName.setTypeface(linoType);
        lblFieldClub.setTypeface(linoType);
        goToTimeline.setTypeface(linoType);

        lblSchool.setTypeface(linoType);
        school.setTypeface(linoType);
        lblHeight.setTypeface(linoType);
        height.setTypeface(linoType);
        lblWeight.setTypeface(linoType);
        weight.setTypeface(linoType);
        lblFavPos.setTypeface(linoType);
        favPos.setTypeface(linoType);
        lblFootballBoot.setTypeface(linoType);
        lblFavFood.setTypeface(linoType);
        favFood.setTypeface(linoType);
        lblFavPlayer.setTypeface(linoType);
        favPlayer.setTypeface(linoType);
        lblFavTeam.setTypeface(linoType);
        favTeam.setTypeface(linoType);
    }

}