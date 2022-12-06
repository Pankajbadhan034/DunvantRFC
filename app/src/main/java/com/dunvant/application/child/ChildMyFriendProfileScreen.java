package com.dunvant.application.child;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildMyGalleryBean;
import com.dunvant.application.beans.MyFriendsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.adapters.ChildViewYourProfileFriendListAdapter;
import com.dunvant.application.child.adapters.ChildViewYourProfileGalleryListAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildMyFriendProfileScreen extends AppCompatActivity  implements IWebServiceCallback {
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    private final String GET_PROFILE = "GET_PROFILE";
    private final String FRIEND_PROFILE = "FRIEND_PROFILE";
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    private ImageView backButton;
    private ImageView profilePic;
    private TextView name;
    private TextView date;
    private TextView genderHeightWeight;
    private TextView fav_team;
    private TextView fav_player;
    private TextView teamLabel;
    private TextView playerLebel;
    private TextView positionLabel;
    private Button friendList;
    private Button gallery;
    private String friendIdStr;
    private String fullNameStr,dob,gender,heightStr,weightStr,favTeam,favPos,favPlayer,profilePicStr;
    String favPlayerPicStr;
    String favTeamPicStr;
    String nationalityStr;
    String schoolStr;
    String footballBootStr;
    String favFoodStr;
    Typeface helvetica;
    Typeface linoType;
    TextView weightLbl;

    TextView weight;
    TextView height;
    ImageView favPlayerPic;
    ImageView favTeamPic;
    TextView nationality;
    TextView favFood;
    TextView school;
    TextView favBoot;


    TextView heightLbl,favPositionLbl,nationalityLbl,fabFoodLbl,schoolLbl,favFootbalBoot;
    ChildViewYourProfileGalleryListAdapter childViewYourProfileGalleryListAdapter;
    ArrayList<ChildMyGalleryBean> childMyGalleryArrayList = new ArrayList<>();
    private GridView myGalleryList;
    private final String GET_MY_GALLERY = "GET_MY_GALLERY";

    ChildViewYourProfileFriendListAdapter childViewYourProfileFriendListAdapter;
    ArrayList<MyFriendsBean> myFriendsBeanArrayList = new ArrayList<>();
    private ListView list;
    private final String MY_FRIENDS = "MY_FRIENDS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_my_friend_profile_screen);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(ChildMyFriendProfileScreen.this));
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
        profilePic = (ImageView) findViewById(R.id.profilePic);
        name =( TextView) findViewById(R.id.name);
        date = (TextView) findViewById(R.id.date);
        genderHeightWeight = (TextView) findViewById(R.id.genderHeightWeight);
        fav_team = (TextView) findViewById(R.id.fav_team);
        fav_player = (TextView) findViewById(R.id.fav_player);
        teamLabel = (TextView) findViewById(R.id.teamLabel);
        playerLebel = (TextView) findViewById(R.id.playerLebel);
        positionLabel = (TextView) findViewById(R.id.positionLabel);
        friendList = (Button) findViewById(R.id.friendList);
        gallery = (Button) findViewById(R.id.gallery);
        list = (ListView) findViewById(R.id.list);
        myGalleryList = (GridView) findViewById(R.id.myGalleryList);
        weightLbl = (TextView) findViewById(R.id.weightLbl);
        heightLbl = (TextView) findViewById(R.id.heightLbl);
        height = (TextView) findViewById(R.id.height);
        weight = (TextView) findViewById(R.id.weight);
        favPositionLbl = (TextView) findViewById(R.id.favPositionLbl);
        nationalityLbl = (TextView) findViewById(R.id.nationalityLbl);
        nationality = (TextView) findViewById(R.id.nationality);
        fabFoodLbl = (TextView) findViewById(R.id.fabFoodLbl);
        schoolLbl = (TextView) findViewById(R.id.schoolLbl);
        favFootbalBoot = (TextView) findViewById(R.id.favFootbalBoot);
        favPlayerPic = (ImageView) findViewById(R.id.favPlayerPic);
        favTeamPic = (ImageView) findViewById(R.id.favTeamPic);
        favFood = (TextView) findViewById(R.id.favFood);
        school = (TextView) findViewById(R.id.school);
        favBoot = (TextView) findViewById(R.id.favBoot);




        name.setTypeface(linoType);
        date.setTypeface(linoType);
        genderHeightWeight.setTypeface(linoType);
        //fav_team.setTypeface(linoType);
        //fav_player.setTypeface(linoType);
        friendList.setTypeface(linoType);
        gallery.setTypeface(linoType);
        teamLabel.setTypeface(linoType);
        playerLebel.setTypeface(linoType);
       // positionLabel.setTypeface(linoType);
        heightLbl.setTypeface(linoType);
        favPositionLbl.setTypeface(linoType);
        nationalityLbl.setTypeface(linoType);
        fabFoodLbl.setTypeface(linoType);
        schoolLbl.setTypeface(linoType);
        weightLbl.setTypeface(linoType);
        favFootbalBoot.setTypeface(linoType);
       // favBoot.setTypeface(linoType);
       // favFood.setTypeface(linoType);


        friendIdStr = getIntent().getStringExtra("friendId");
        fullNameStr = getIntent().getStringExtra("fullName");
        dob = getIntent().getStringExtra("dob");
        gender = getIntent().getStringExtra("gender");
        heightStr = getIntent().getStringExtra("height");
        weightStr = getIntent().getStringExtra("weight");
        favTeam = getIntent().getStringExtra("favTeam");
        favPos = getIntent().getStringExtra("favPos");
        favPlayer = getIntent().getStringExtra("favPlayer");
        profilePicStr = getIntent().getStringExtra("profilePic");

         favPlayerPicStr = getIntent().getStringExtra("favPlayerPic");
         favTeamPicStr = getIntent().getStringExtra("favTeamPic");
         nationalityStr = getIntent().getStringExtra("nationality");
         schoolStr = getIntent().getStringExtra("school");
         footballBootStr = getIntent().getStringExtra("footballBoot");
         favFoodStr = getIntent().getStringExtra("favFood");

        if(heightStr == null || heightStr.equalsIgnoreCase("null")){
            heightStr="";
        }

        if(weightStr == null || weightStr.equalsIgnoreCase("null")){
            weightStr="";
        }


        name.setText(fullNameStr);
        date.setText(dob);
        genderHeightWeight.setText(gender+" "+heightStr+" "+weightStr);
        fav_team.setText(favTeam);
        fav_player.setText(favPlayer);
        positionLabel.setText(favPos);
        favFood.setText(favFoodStr);
        imageLoader.displayImage(profilePicStr, profilePic, options);

        /*if(heightStr.equalsIgnoreCase("null")){
            height.setText("");
            //System.out.println("hereIF");
        }else{
            //System.out.println("hereElse"+heightStr);
            height.setText(heightStr);
        }*/

        height.setText(heightStr);

        /*if(weightStr.equalsIgnoreCase("null")){
            weight.setText("");
        }else{
            weight.setText(weightStr);
        }*/

        weight.setText(weightStr);


        imageLoader.displayImage(favPlayerPicStr, favPlayerPic, options);
        imageLoader.displayImage(favTeamPicStr, favTeamPic, options);
        nationality.setText(nationalityStr);
        favBoot.setText(footballBootStr);
        school.setText(schoolStr);


//        if(Utilities.isNetworkAvailable(ChildMyFriendProfileScreen.this)) {
//
//            List<NameValuePair> nameValuePairList = new ArrayList<>();
//            nameValuePairList.add(new BasicNameValuePair("friend_id", friendIdStr));
//
//            String webServiceUrl = Utilities.BASE_URL + "children/child_friend_list";
//
//            ArrayList<String> headers = new ArrayList<>();
//            headers.add("X-access-uid:"+loggedInUser.getId());
//            headers.add("X-access-token:"+loggedInUser.getToken());
//
//            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyFriendProfileScreen.this, nameValuePairList, FRIEND_PROFILE, ChildMyFriendProfileScreen.this, headers);
//            postWebServiceAsync.execute(webServiceUrl);
//
//        } else {
//            Toast.makeText(ChildMyFriendProfileScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//        }


        friendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendListTab();
                if(Utilities.isNetworkAvailable(ChildMyFriendProfileScreen.this)) {

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("friend_id",friendIdStr ));
                    String webServiceUrl = Utilities.BASE_URL + "children/child_friend_list";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyFriendProfileScreen.this, nameValuePairList, MY_FRIENDS, ChildMyFriendProfileScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(ChildMyFriendProfileScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryListTab();
                if(Utilities.isNetworkAvailable(ChildMyFriendProfileScreen.this)) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("friend_id", friendIdStr));
                    nameValuePairList.add(new BasicNameValuePair("offset", "0"));
                    nameValuePairList.add(new BasicNameValuePair("gallery_type", "image"));

                    String webServiceUrl = Utilities.BASE_URL + "children/gallery_list";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyFriendProfileScreen.this, nameValuePairList, GET_MY_GALLERY, ChildMyFriendProfileScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                }else{
                    Toast.makeText(ChildMyFriendProfileScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
//            case FRIEND_PROFILE:
//
//                if(response == null) {
//                    Toast.makeText(ChildMyFriendProfileScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
//                } else {
//                    try {
//                        JSONObject responseObject = new JSONObject(response);
//
//                        boolean status = responseObject.getBoolean("status");
//                        String message = responseObject.getString("message");
//
//                        if (status) {
//
//                        } else {
//                            Toast.makeText(ChildMyFriendProfileScreen.this, message, Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(ChildMyFriendProfileScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;

            case MY_FRIENDS:

                myFriendsBeanArrayList.clear();
                if(response == null) {
                    Toast.makeText(ChildMyFriendProfileScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            MyFriendsBean myFriendsBean;
                            String responseArray=responseObject.getString("data");
                            JSONArray myFriendsArray=new JSONArray(responseArray);
                            //System.out.println("array__"+myFriendsArray);
                            for(int i=0;i<myFriendsArray.length();i++){
                                JSONObject myFriendObject = myFriendsArray.getJSONObject(i);
                                myFriendsBean = new MyFriendsBean();
                                myFriendsBean.setFriendId(myFriendObject.getString("friend_id"));
                                myFriendsBean.setFullName(myFriendObject.getString("full_name"));
                                myFriendsBean.setCreatedAt(myFriendObject.getString("created_at"));
                                myFriendsBean.setCreatedAtFormatted(myFriendObject.getString("created_at_formatted"));
                                myFriendsBean.setTitle(myFriendObject.getString("title"));
                                myFriendsBean.setProfilePic(myFriendObject.getString("profile_picture"));
                                myFriendsBean.setPicUrl(myFriendObject.getString("profile_pic_url"));
                                myFriendsBean.setIsPrivate(myFriendObject.getString("is_private"));

                                myFriendsBean.setDobFormatted(myFriendObject.getString("dob_formatted"));
                                myFriendsBean.setSchool(myFriendObject.getString("school"));
                                myFriendsBean.setFavPlayer(myFriendObject.getString("favourite_player"));
                                myFriendsBean.setFavPlayerPicture(myFriendObject.getString("favourite_player_picture"));
                                myFriendsBean.setFavTeam(myFriendObject.getString("favourite_team"));
                                myFriendsBean.setFavTeamPicture(myFriendObject.getString("favourite_team_picture"));
                                myFriendsBean.setFavPosition(myFriendObject.getString("favourite_position"));
                                myFriendsBean.setHeightFormatted(myFriendObject.getString("height_formatted"));
                                myFriendsBean.setHeight(myFriendObject.getString("height"));
                                myFriendsBean.setWeightFormatted(myFriendObject.getString("weight_formatted"));
                                myFriendsBean.setWeight(myFriendObject.getString("weight"));
                                myFriendsBean.setFavFootbalBoot(myFriendObject.getString("favourite_football_boot"));
                                myFriendsBean.setNationality(myFriendObject.getString("nationality"));
                                myFriendsBean.setFavFood(myFriendObject.getString("favourite_food"));

                                myFriendsBeanArrayList.add(myFriendsBean);

                            }
                            childViewYourProfileFriendListAdapter = new ChildViewYourProfileFriendListAdapter(ChildMyFriendProfileScreen.this,myFriendsBeanArrayList);
                            list.setAdapter(childViewYourProfileFriendListAdapter);
                            childViewYourProfileFriendListAdapter.notifyDataSetChanged();
                            Utilities.setListViewHeightBasedOnChildren(list);

                        } else {
                        //    Toast.makeText(ChildMyFriendProfileScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildMyFriendProfileScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case GET_MY_GALLERY:

                childMyGalleryArrayList.clear();
                if (response == null) {
                    Toast.makeText(ChildMyFriendProfileScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            Gson gson = new Gson();
                            String jsonParentUserBean = gson.toJson(loggedInUser);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("loggedInUser", jsonParentUserBean);
                            editor.commit();
                            ChildMyGalleryBean childMyGalleryBean;
                            JSONArray myGalleryArray=new JSONArray(responseObject.getString("data"));

                            for(int i = 0; i < myGalleryArray.length(); i++){
                                JSONObject galleryData = myGalleryArray.getJSONObject(i);
                                childMyGalleryBean = new ChildMyGalleryBean();
                                childMyGalleryBean.setFileUrl(galleryData.getString("file_url"));

                                childMyGalleryArrayList.add(childMyGalleryBean);
                            }

                            childViewYourProfileGalleryListAdapter = new ChildViewYourProfileGalleryListAdapter(ChildMyFriendProfileScreen.this,childMyGalleryArrayList);
                            myGalleryList.setAdapter(childViewYourProfileGalleryListAdapter);
                            childViewYourProfileGalleryListAdapter.notifyDataSetChanged();
                            Utilities.setGridViewHeightBasedOnChildren(myGalleryList, 2);
                        }

                     //   Toast.makeText(ChildMyFriendProfileScreen.this, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildMyFriendProfileScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;
        }
    }

    public void friendListTab(){
        friendList.setBackgroundColor(getResources().getColor(R.color.yellow));
        gallery.setBackgroundColor(Color.parseColor("#333333"));
        friendList.setTextColor(Color.parseColor("#333333"));
        gallery.setTextColor(Color.parseColor("#ffffff"));
        list.setVisibility(View.VISIBLE);
        myGalleryList.setVisibility(View.GONE);
    }

    public void galleryListTab(){
        friendList.setBackgroundColor(Color.parseColor("#333333"));
        gallery.setBackgroundColor(getResources().getColor(R.color.yellow));
        friendList.setTextColor(Color.parseColor("#ffffff"));
        gallery.setTextColor(Color.parseColor("#333333"));
        list.setVisibility(View.GONE);
        myGalleryList.setVisibility(View.VISIBLE);
    }
}
