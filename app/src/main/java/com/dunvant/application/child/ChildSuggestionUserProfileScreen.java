package com.dunvant.application.child;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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

public class ChildSuggestionUserProfileScreen extends AppCompatActivity implements IWebServiceCallback {
    ChildViewYourProfileGalleryListAdapter childViewYourProfileGalleryListAdapter;
    ArrayList<ChildMyGalleryBean> childMyGalleryArrayList = new ArrayList<>();
    ChildViewYourProfileFriendListAdapter childViewYourProfileFriendListAdapter;
    ArrayList<MyFriendsBean> myFriendsBeanArrayList = new ArrayList<>();
    private final String SEND_REQUEST_IFA_MEMBER = "SEND_REQUEST_IFA_MEMBER";
    private final String GET_MY_GALLERY = "GET_MY_GALLERY";
    private final String MY_FRIENDS = "MY_FRIENDS";
    Button sendRequest;
    ImageView backButton;
    ImageView profilePic;
    TextView name;
    TextView fav_team;
    TextView fav_player;
    TextView fav_position;
    String fullName;
    String favTeam;
    String favPlayer;
    String favPosition;
    String profilePicUrl;
    String canSendRequest;
    String userId;
    private Button friendList;
    private Button gallery;
    private ListView list;
    private GridView myGalleryList;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_suggestion_user_profile_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        profilePic = (ImageView) findViewById(R.id.profilePic);
        name = (TextView) findViewById(R.id.name);
        fav_team = (TextView) findViewById(R.id.fav_team);
        fav_player = (TextView) findViewById(R.id.fav_player);
        fav_position = (TextView) findViewById(R.id.fav_position);
        backButton = (ImageView) findViewById(R.id.backButton);
        sendRequest = (Button) findViewById(R.id.sendRequest);
        friendList = (Button) findViewById(R.id.friendList);
        gallery = (Button) findViewById(R.id.gallery);
        list = (ListView) findViewById(R.id.list);
        myGalleryList = (GridView) findViewById(R.id.myGalleryList);

        profilePicUrl = getIntent().getStringExtra("profilePicUrl");
        fullName = getIntent().getStringExtra("fullName");
        favTeam = getIntent().getStringExtra("favTeam");
        favPlayer = getIntent().getStringExtra("favPlayer");
        favPosition = getIntent().getStringExtra("favPosition");
        canSendRequest = getIntent().getStringExtra("canSendRequest");
        userId = getIntent().getStringExtra("userId");

        if(canSendRequest.equalsIgnoreCase("true")){
            sendRequest.setVisibility(View.VISIBLE);
        }else{
            sendRequest.setVisibility(View.GONE);
        }

        name.setText(fullName);
        fav_team.setText(favTeam);
        fav_position.setText(favPosition);
        fav_player.setText(favPlayer);

        imageLoader.init(ImageLoaderConfiguration.createDefault(ChildSuggestionUserProfileScreen.this));
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

        imageLoader.displayImage(profilePicUrl, profilePic, options);

        if(Utilities.isNetworkAvailable(ChildSuggestionUserProfileScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("friend_id", userId));
            String webServiceUrl = Utilities.BASE_URL + "children/child_friend_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildSuggestionUserProfileScreen.this, nameValuePairList, MY_FRIENDS, ChildSuggestionUserProfileScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ChildSuggestionUserProfileScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(ChildSuggestionUserProfileScreen.this)) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();

                    // Changing the following to array
                    userId = "[\""+userId+"\"]";
                    nameValuePairList.add(new BasicNameValuePair("friend_id", userId));

                    String webServiceUrl = Utilities.BASE_URL + "account/send_request_to_ifa";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildSuggestionUserProfileScreen.this, nameValuePairList, SEND_REQUEST_IFA_MEMBER, ChildSuggestionUserProfileScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(ChildSuggestionUserProfileScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        friendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendListTab();
                if(Utilities.isNetworkAvailable(ChildSuggestionUserProfileScreen.this)) {

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("friend_id", userId));
                    String webServiceUrl = Utilities.BASE_URL + "children/child_friend_list";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildSuggestionUserProfileScreen.this, nameValuePairList, MY_FRIENDS, ChildSuggestionUserProfileScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(ChildSuggestionUserProfileScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryListTab();
                if(Utilities.isNetworkAvailable(ChildSuggestionUserProfileScreen.this)) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("offset", "0"));
                    nameValuePairList.add(new BasicNameValuePair("gallery_type", "image"));
                    nameValuePairList.add(new BasicNameValuePair("friend_id", userId));

                    String webServiceUrl = Utilities.BASE_URL + "children/gallery_list";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildSuggestionUserProfileScreen.this, nameValuePairList, GET_MY_GALLERY, ChildSuggestionUserProfileScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                }else{
                    Toast.makeText(ChildSuggestionUserProfileScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {

            case SEND_REQUEST_IFA_MEMBER:

                if (response == null) {
                    Toast.makeText(ChildSuggestionUserProfileScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if(status){
                            finish();
                        }
                        Toast.makeText(ChildSuggestionUserProfileScreen.this, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildSuggestionUserProfileScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case MY_FRIENDS:

                myFriendsBeanArrayList.clear();
                if(response == null) {
                    Toast.makeText(ChildSuggestionUserProfileScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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

                                myFriendsBeanArrayList.add(myFriendsBean);

                            }
                            childViewYourProfileFriendListAdapter = new ChildViewYourProfileFriendListAdapter(ChildSuggestionUserProfileScreen.this,myFriendsBeanArrayList);
                            list.setAdapter(childViewYourProfileFriendListAdapter);
                            childViewYourProfileFriendListAdapter.notifyDataSetChanged();
                            Utilities.setListViewHeightBasedOnChildren(list);

                        }
//                        else {
//                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildSuggestionUserProfileScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case GET_MY_GALLERY:

                childMyGalleryArrayList.clear();
                if (response == null) {
                    Toast.makeText(ChildSuggestionUserProfileScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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

                            childViewYourProfileGalleryListAdapter = new ChildViewYourProfileGalleryListAdapter(ChildSuggestionUserProfileScreen.this,childMyGalleryArrayList);
                            myGalleryList.setAdapter(childViewYourProfileGalleryListAdapter);
                            childViewYourProfileGalleryListAdapter.notifyDataSetChanged();
                            Utilities.setGridViewHeightBasedOnChildren(myGalleryList, 2);

                        }
//                        else {
//                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildSuggestionUserProfileScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
