package com.dunvant.application.child;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.dunvant.application.beans.IfaMemberListBean;
import com.dunvant.application.beans.UserBean;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class ChildMyFriendSendInviteScreen extends AppCompatActivity implements IWebServiceCallback {
    String isPrivateShow;
    String friendUserNameStr;
    EditText friendUserName;
    ScrollView scrollView;
    Dialog dialog;
    String friendId;
    String fullName,dob,gender,height,weight,favTeam,favPos,favPlayer,profilePic,isPrivate="";

    Button viewProfile;
    Button sendRequest;

    TextView selectLocation;
    TextView selectDay;
    TextView selectIfaMemberLabel;
    TextView nonIfaMemberLabel;

    TextView ifaFriendName;
    ImageView backButton;

    String nameStr,emailStr,messageStr;
    EditText friendname;
    EditText friendEmail;
    EditText friendMessage;
    Button sendinvite;
    TextView title;

    private final String GET_IFA_MEMBER = "GET_IFA_MEMBER";
    private final String GET_IFA_MEMBER_DAY = "GET_IFA_MEMBER_DAY";
    private final String SEND_REQUEST_IFA_MEMBER = "SEND_REQUEST_IFA_MEMBER";
    private final String SEND_REQUEST_NON_IFA_MEMBER = "SEND_REQUEST_NON_IFA_MEMBER";

    ArrayList<IfaMemberListBean> ifaMemberBeanArrayList = new ArrayList<>();
    ArrayList<IfaMemberListBean> ifaDayListArrayList = new ArrayList<>();
    ArrayList<IfaMemberListBean> ifaLocationListArrayList = new ArrayList<>();
    Typeface helvetica;
    Typeface linoType;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    CheckBox autoCheck;
    String autoCheckStr="1";
    TextView textcheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_my_friend_send_invite_screen);
        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);
        selectLocation = (TextView) findViewById(R.id.selectLocation);
        selectDay = (TextView) findViewById(R.id.selectDay);
        ifaFriendName = (TextView) findViewById(R.id.ifaFriendName);
        sendRequest = (Button) findViewById(R.id.sendRequest);
        viewProfile = (Button) findViewById(R.id.viewProfile);
        friendname = (EditText) findViewById(R.id.friendname);
        friendEmail = (EditText) findViewById(R.id.friendEmail);
        friendMessage = (EditText) findViewById(R.id.friendMessage);
        sendinvite = (Button) findViewById(R.id.sendinvite);
        nonIfaMemberLabel = (TextView) findViewById(R.id.nonIfaMemberLabel);
        selectIfaMemberLabel = (TextView) findViewById(R.id.selectIfaMemberLabel);
        friendUserName = findViewById(R.id.friendUserName);
        autoCheck = findViewById(R.id.autoCheck);
        textcheckBox = findViewById(R.id.textcheckBox);

        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

        autoCheck.setText("Auto "+verbiage_singular.toLowerCase()+" Create(the invitee will need to get a "+verbiage_singular.toLowerCase()+" automatically created or he will use this app as a parent and will add "+verbiage_plural.toLowerCase()+" themselves)");
        textcheckBox.setText("Auto "+verbiage_singular.toLowerCase()+" Create(the invitee will need to get a "+verbiage_singular.toLowerCase()+" automatically created or he will use this app as a parent and will add "+verbiage_plural.toLowerCase()+" themselves)");


        autoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    autoCheckStr="1";
                }else{
                    autoCheckStr="0";
                }
            }
        });

        friendUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("beforeTextChanged::");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("onTextChanged::"+count);
                if(count>0){
                    ifaFriendName.setText("Friend's Username");
                    selectLocation.setText("Select A Location");
                    selectDay.setText("Select A Session");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged::");
            }
        });
//        scrollView = (ScrollView) findViewById(R.id.scrollView);
//
//        scrollView.fullScroll(ScrollView.FOCUS_UP);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        title.setTypeface(linoType);
        selectDay.setTypeface(helvetica);
        selectLocation.setTypeface(helvetica);
        ifaFriendName.setTypeface(helvetica);
        sendRequest.setTypeface(helvetica);
        viewProfile.setTypeface(helvetica);
        sendinvite.setTypeface(helvetica);
        nonIfaMemberLabel.setTypeface(helvetica);
        selectIfaMemberLabel.setTypeface(helvetica);

        sendinvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameStr = friendname.getText().toString().trim();
                emailStr = friendEmail.getText().toString().trim();
                messageStr = friendMessage.getText().toString().trim();

                Pattern pattern = Pattern.compile(Utilities.EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(emailStr);

                if(nameStr==null||nameStr.isEmpty()){
                    Toast.makeText(ChildMyFriendSendInviteScreen.this, "Please enter friend's name", Toast.LENGTH_SHORT).show();
                }else if(emailStr==null||emailStr.isEmpty()){
                    Toast.makeText(ChildMyFriendSendInviteScreen.this, "Please enter friend's email", Toast.LENGTH_SHORT).show();
                }else if (!matcher.matches()) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid Email", Toast.LENGTH_SHORT).show();
                }else if(messageStr==null||messageStr.isEmpty()){
                    Toast.makeText(ChildMyFriendSendInviteScreen.this, "Please enter message", Toast.LENGTH_SHORT).show();
                }else{
                    if (Utilities.isNetworkAvailable(ChildMyFriendSendInviteScreen.this)) {
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("friend_name", nameStr));
                        nameValuePairList.add(new BasicNameValuePair("friend_email", emailStr));
                        nameValuePairList.add(new BasicNameValuePair("friend_msg", messageStr));
                        nameValuePairList.add(new BasicNameValuePair("auto_player_create", autoCheckStr));

                        String webServiceUrl = Utilities.BASE_URL + "account/send_request_to_non_ifa";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:" + loggedInUser.getId());
                        headers.add("X-access-token:" + loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyFriendSendInviteScreen.this, nameValuePairList, SEND_REQUEST_NON_IFA_MEMBER, ChildMyFriendSendInviteScreen.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);
                    } else {
                        Toast.makeText(ChildMyFriendSendInviteScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if(Utilities.isNetworkAvailable(ChildMyFriendSendInviteScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("location_filter", "0"));
            nameValuePairList.add(new BasicNameValuePair("day_filter", "0"));

            String webServiceUrl = Utilities.BASE_URL + "children/ifa_members_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyFriendSendInviteScreen.this, nameValuePairList, GET_IFA_MEMBER, ChildMyFriendSendInviteScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);
        } else {
            Toast.makeText(ChildMyFriendSendInviteScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }

        ifaFriendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendUserName.setText("");
                friendUserNameStr="";
                if (ifaMemberBeanArrayList.size() == 1) {
                    Toast.makeText(ChildMyFriendSendInviteScreen.this, R.string.ifa_member_list_empty, Toast.LENGTH_SHORT).show();
                } else {
                    if (ifaMemberBeanArrayList == null || ifaMemberBeanArrayList.isEmpty()) {
                        Toast.makeText(ChildMyFriendSendInviteScreen.this, "No Record Found.", Toast.LENGTH_LONG).show();
                    } else {

                        dialog = new Dialog(ChildMyFriendSendInviteScreen.this);
                        dialog.setContentView(R.layout.child_dialog_ifa_members_search);
                        dialog.setTitle(R.string.select_ifa_member);

                        final EditText search = (EditText) dialog.findViewById(R.id.search);
                        final ListView list = (ListView) dialog.findViewById(R.id.list);

                        IfaMemberAdapter childMyFriendRequestsAdapter = new IfaMemberAdapter(ChildMyFriendSendInviteScreen.this, ifaMemberBeanArrayList);
                        list.setAdapter(childMyFriendRequestsAdapter);

                        search.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String searchStr = search.getText().toString();
                                IfaMemberAdapter childMyFriendRequestsAdapter = new IfaMemberAdapter(ChildMyFriendSendInviteScreen.this, ifaMemberBeanArrayList);
                                list.setAdapter(childMyFriendRequestsAdapter);

//                                String text = searchStr.toLowerCase(Locale.getDefault());
//                                childMyFriendRequestsAdapter.filter(text);


                                ArrayList<IfaMemberListBean> searchIfaMemberList = new ArrayList<>();

                                for (int i = 0; i < ifaMemberBeanArrayList.size(); i++) {
                                    IfaMemberListBean ifaMemberListBean = ifaMemberBeanArrayList.get(i);
                                    if (ifaMemberListBean.getFullName().toLowerCase().contains(searchStr.toLowerCase())) {
                                        //System.out.println("FULLname::"+ifaMemberListBean.getFullName());
                                        searchIfaMemberList.add(ifaMemberBeanArrayList.get(i));
                                    }
                                }

                                //System.out.println("ListSize::"+searchIfaMemberList.size());
                                childMyFriendRequestsAdapter = new IfaMemberAdapter(ChildMyFriendSendInviteScreen.this, searchIfaMemberList);
                                list.setAdapter(childMyFriendRequestsAdapter);


                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });


                        dialog.show();
                    }
                }

            }
        });

        selectDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendUserName.setText("");
                friendUserNameStr="";
                if(ifaMemberBeanArrayList.size()==1){
                    Toast.makeText(ChildMyFriendSendInviteScreen.this, R.string.ifa_member_list_empty, Toast.LENGTH_SHORT).show();
                }else{
                    if (ifaDayListArrayList == null || ifaDayListArrayList.isEmpty()) {
                        Toast.makeText(ChildMyFriendSendInviteScreen.this, "No Record Found.", Toast.LENGTH_LONG).show();
                    } else {

                        dialog = new Dialog(ChildMyFriendSendInviteScreen.this);
                        dialog.setContentView(R.layout.child_dialog_ifa_members);
                        dialog.setTitle("Select A Session");

                        ListView list = (ListView) dialog.findViewById(R.id.list);
                        IfaDayListAdapter childMyFriendRequestsAdapter = new IfaDayListAdapter(ChildMyFriendSendInviteScreen.this, ifaDayListArrayList);
                        list.setAdapter(childMyFriendRequestsAdapter);

                        dialog.show();
                    }
                }



            }
        });

        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendUserName.setText("");
                friendUserNameStr="";
                if(ifaMemberBeanArrayList.size()==1){
                    Toast.makeText(ChildMyFriendSendInviteScreen.this, R.string.ifa_member_list_empty, Toast.LENGTH_SHORT).show();
                }else{
                    if (ifaLocationListArrayList == null || ifaLocationListArrayList.isEmpty()) {
                        Toast.makeText(ChildMyFriendSendInviteScreen.this, "No Record Found.", Toast.LENGTH_LONG).show();
                    } else {

                        dialog = new Dialog(ChildMyFriendSendInviteScreen.this);
                        dialog.setContentView(R.layout.child_dialog_ifa_members);
                        dialog.setTitle("Select A Location");

                        ListView list = (ListView) dialog.findViewById(R.id.list);

                        IfaLocationAdapter childMyFriendRequestsAdapter = new IfaLocationAdapter(ChildMyFriendSendInviteScreen.this, ifaLocationListArrayList);
                        list.setAdapter(childMyFriendRequestsAdapter);

                        dialog.show();
                    }
                }



            }
        });


        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendUserNameStr = friendUserName.getText().toString();

                if(friendUserNameStr==null || friendUserNameStr.isEmpty()){
                    if(ifaMemberBeanArrayList.size()==1){
                        Toast.makeText(ChildMyFriendSendInviteScreen.this, "R.string.ifa_member_list_empty", Toast.LENGTH_SHORT).show();
                    }else{
                        if (friendId == null || friendId.isEmpty() || fullName == null || fullName.isEmpty()) {
                            Toast.makeText(ChildMyFriendSendInviteScreen.this, "Please select Friend's Name", Toast.LENGTH_LONG).show();
                        } else {
                            if (Utilities.isNetworkAvailable(ChildMyFriendSendInviteScreen.this)) {

//                                if(isPrivate.equalsIgnoreCase("0")){
                                if(isPrivate.equalsIgnoreCase("1")){
                                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                                    Toast.makeText(ChildMyFriendSendInviteScreen.this, "You cannot view this "+verbiage_singular.toLowerCase()+"'s profile as he has set to private.", Toast.LENGTH_SHORT).show();

                                  //  Toast.makeText(ChildMyFriendSendInviteScreen.this, "You can not view this child's profile as he has set to private.", Toast.LENGTH_SHORT).show();
                                    // changing 0 to 1 in above condition - Gene

//                                    if(isPrivateShow.equalsIgnoreCase("0")){
//                                        Toast.makeText(ChildMyFriendSendInviteScreen.this, "You can not view this child's profile as he has set to private.", Toast.LENGTH_SHORT).show();
//                                    }else if(isPrivateShow.equalsIgnoreCase("1")){
//                                        Intent obj = new Intent(ChildMyFriendSendInviteScreen.this, ChildMyFriendProfileScreen.class);
//                                        obj.putExtra("friendId", friendId);
//                                        obj.putExtra("fullName", fullName);
//                                        obj.putExtra("dob", dob);
//                                        obj.putExtra("gender", gender);
//                                        obj.putExtra("height", height);
//                                        obj.putExtra("weight", weight);
//                                        obj.putExtra("favTeam", favTeam);
//                                        obj.putExtra("favPos", favPos);
//                                        obj.putExtra("favPlayer", favPlayer);
//                                        obj.putExtra("profilePic", profilePic);
//                                        obj.putExtra("favFood", "");
//                                        startActivity(obj);
                                  //  }

                                }else{
                                    Intent obj = new Intent(ChildMyFriendSendInviteScreen.this, ChildMyFriendProfileScreen.class);
                                    obj.putExtra("friendId", friendId);
                                    obj.putExtra("fullName", fullName);
                                    obj.putExtra("dob", dob);
                                    obj.putExtra("gender", gender);
                                    obj.putExtra("height", height);
                                    obj.putExtra("weight", weight);
                                    obj.putExtra("favTeam", favTeam);
                                    obj.putExtra("favPos", favPos);
                                    obj.putExtra("favPlayer", favPlayer);
                                    obj.putExtra("profilePic", profilePic);
                                    obj.putExtra("favFood", "");
                                    startActivity(obj);
                                }

                            } else {
                                Toast.makeText(ChildMyFriendSendInviteScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }else{
                    Toast.makeText(ChildMyFriendSendInviteScreen.this, "Please select friend from IFA member list", Toast.LENGTH_SHORT).show();
                }



            }
        });

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<NameValuePair> nameValuePairList = new ArrayList<>();

                friendUserNameStr = friendUserName.getText().toString();

                if(friendUserNameStr==null || friendUserNameStr.isEmpty()){
                    if(ifaMemberBeanArrayList.size()==1){
                        Toast.makeText(ChildMyFriendSendInviteScreen.this, R.string.ifa_member_list_empty, Toast.LENGTH_SHORT).show();
                    }else{
                        if(ifaFriendName.getText().toString().equalsIgnoreCase("Friend's Name")){
                            Toast.makeText(ChildMyFriendSendInviteScreen.this,"Please select A Friend",Toast.LENGTH_LONG).show();
                        }else{
                            if(friendId==null||friendId.isEmpty()){
                                if(ifaFriendName.getText().toString().equalsIgnoreCase("All")){

                                    // Changing 0 to array - Gene
//                                    nameValuePairList.add(new BasicNameValuePair("friend_id", "0"));
                                    nameValuePairList.add(new BasicNameValuePair("friend_id", "[\"0\"]"));

                                    String webServiceUrl = Utilities.BASE_URL + "account/send_request_to_ifa";

                                    ArrayList<String> headers = new ArrayList<>();
                                    headers.add("X-access-uid:" + loggedInUser.getId());
                                    headers.add("X-access-token:" + loggedInUser.getToken());

                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyFriendSendInviteScreen.this, nameValuePairList, SEND_REQUEST_IFA_MEMBER, ChildMyFriendSendInviteScreen.this, headers);
                                    postWebServiceAsync.execute(webServiceUrl);
                                }else{
                                    Toast.makeText(ChildMyFriendSendInviteScreen.this,"Please select A Friend",Toast.LENGTH_LONG).show();
                                }
                            }else {
                                if (Utilities.isNetworkAvailable(ChildMyFriendSendInviteScreen.this)) {

                                    if(ifaFriendName.getText().toString().equalsIgnoreCase("All")){

                                        // Changing 0 to array - Gene

//                                        nameValuePairList.add(new BasicNameValuePair("friend_id", "0"));
                                        nameValuePairList.add(new BasicNameValuePair("friend_id", "[\"0\"]"));
                                    }else{
                                        String friendID = "[\""+friendId+"\"]";
                                        nameValuePairList.add(new BasicNameValuePair("friend_id", friendID));
                                    }


                                    String webServiceUrl = Utilities.BASE_URL + "account/send_request_to_ifa";

                                    ArrayList<String> headers = new ArrayList<>();
                                    headers.add("X-access-uid:" + loggedInUser.getId());
                                    headers.add("X-access-token:" + loggedInUser.getToken());

                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyFriendSendInviteScreen.this, nameValuePairList, SEND_REQUEST_IFA_MEMBER, ChildMyFriendSendInviteScreen.this, headers);
                                    postWebServiceAsync.execute(webServiceUrl);
                                } else {
                                    Toast.makeText(ChildMyFriendSendInviteScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }else{
                    nameValuePairList.add(new BasicNameValuePair("friend_username", friendUserNameStr));

                    String webServiceUrl = Utilities.BASE_URL + "account/send_request_to_ifa";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyFriendSendInviteScreen.this, nameValuePairList, SEND_REQUEST_IFA_MEMBER, ChildMyFriendSendInviteScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
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
        switch (tag) {
            case GET_IFA_MEMBER:


                if(response == null) {
                    Toast.makeText(ChildMyFriendSendInviteScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            ifaMemberBeanArrayList.clear();
                            ifaDayListArrayList.clear();
                            ifaLocationListArrayList.clear();

                            IfaMemberListBean ifaMemberListBean;
                            String responseArray=responseObject.getString("data");
                            isPrivateShow = responseObject.getString("is_private_show");
                            JSONObject memberListObject = new JSONObject(responseArray);
                            JSONArray memberListArray= memberListObject.getJSONArray("members_list");
                            ifaMemberListBean = new IfaMemberListBean();
                            ifaMemberListBean.setFullName("All");
                            ifaMemberBeanArrayList.add(ifaMemberListBean);
                            for(int i=0;i<memberListArray.length();i++){
                                JSONObject myFriendObject = memberListArray.getJSONObject(i);
                                ifaMemberListBean = new IfaMemberListBean();

                                ifaMemberListBean.setFriendId(myFriendObject.getString("friend_id"));
                                ifaMemberListBean.setRoleId(myFriendObject.getString("role_id"));
                                ifaMemberListBean.setRoleName(myFriendObject.getString("role_name"));
                                ifaMemberListBean.setRoleCode(myFriendObject.getString("role_code"));
                                ifaMemberListBean.setFullName(myFriendObject.getString("full_name"));
                                ifaMemberListBean.setTitle(myFriendObject.getString("title"));
                                ifaMemberListBean.setProfilePicture(myFriendObject.getString("profile_picture"));
                                ifaMemberListBean.setProfilePicUrl(myFriendObject.getString("profile_picture_url"));
                                ifaMemberListBean.setDob(myFriendObject.getString("dob"));
                                ifaMemberListBean.setDobFormatted(myFriendObject.getString("dob_formatted"));
                                ifaMemberListBean.setGender(myFriendObject.getString("gender"));
                                ifaMemberListBean.setFavoritePlayer(myFriendObject.getString("favourite_player"));
                                ifaMemberListBean.setIsPlayer(myFriendObject.getString("is_private"));
                                ifaMemberListBean.setFavoriteTeam(myFriendObject.getString("favourite_team"));
                                ifaMemberListBean.setFavoritePosition(myFriendObject.getString("favourite_position"));
                                ifaMemberListBean.setHeightFormatted(myFriendObject.getString("height_formatted"));
                                ifaMemberListBean.setWeightFormatted(myFriendObject.getString("weight_formatted"));


                                ifaMemberBeanArrayList.add(ifaMemberListBean);

                            }


                            JSONArray dayListArray = memberListObject.getJSONArray("days_list");

                            ifaMemberListBean = new IfaMemberListBean();
                            ifaMemberListBean.setDayName("Select A Session");
                            ifaMemberListBean.setDayValue("01");
                            ifaDayListArrayList.add(ifaMemberListBean);

                            for(int i=0; i<dayListArray.length(); i++){
                                JSONObject dayListObject = dayListArray.getJSONObject(i);
                                ifaMemberListBean = new IfaMemberListBean();

                                ifaMemberListBean.setDayName(dayListObject.getString("day_name"));
                                ifaMemberListBean.setDayValue(dayListObject.getString("day_value"));

                                ifaDayListArrayList.add(ifaMemberListBean);
                            }

                            JSONArray locationListArray = memberListObject.getJSONArray("locations_list");
                            ifaMemberListBean = new IfaMemberListBean();
                            ifaMemberListBean.setId("01");
                            ifaMemberListBean.setAcademicsId("01");
                            ifaMemberListBean.setName("Select A Location");
                            ifaMemberListBean.setPitchDescription("01");
                            ifaMemberListBean.setSessionDescription("01");
                            ifaMemberListBean.setSiteMediaId("01");
                            ifaMemberListBean.setLatitude("01");
                            ifaMemberListBean.setLongitude("01");
                            ifaMemberListBean.setState("01");
                            ifaMemberListBean.setCreatedAt("01");
                            ifaMemberListBean.setDescription("01");
                            ifaLocationListArrayList.add(ifaMemberListBean);

                            for(int i=0; i<locationListArray.length(); i++){
                                JSONObject locationListObject = locationListArray.getJSONObject(i);
                                ifaMemberListBean = new IfaMemberListBean();

                                ifaMemberListBean.setId(locationListObject.getString("id"));
                                ifaMemberListBean.setAcademicsId(locationListObject.getString("academies_id"));
                                ifaMemberListBean.setName(locationListObject.getString("name"));
                                ifaMemberListBean.setPitchDescription(locationListObject.getString("pitch_description"));
                                ifaMemberListBean.setSessionDescription(locationListObject.getString("session_description"));
                                ifaMemberListBean.setSiteMediaId(locationListObject.getString("site_media_id"));
                                ifaMemberListBean.setLatitude(locationListObject.getString("latitude"));
                                ifaMemberListBean.setLongitude(locationListObject.getString("longitude"));
                                ifaMemberListBean.setState(locationListObject.getString("state"));
                                ifaMemberListBean.setCreatedAt(locationListObject.getString("created_at"));
                                ifaMemberListBean.setDescription(locationListObject.getString("description"));

                                ifaLocationListArrayList.add(ifaMemberListBean);
                            }

                        } else {
                            ifaMemberBeanArrayList.clear();
                            ifaDayListArrayList.clear();
                            Toast.makeText(ChildMyFriendSendInviteScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildMyFriendSendInviteScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case GET_IFA_MEMBER_DAY:
                if(response == null) {
                    Toast.makeText(ChildMyFriendSendInviteScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            ifaMemberBeanArrayList.clear();
                            ifaDayListArrayList.clear();

                            IfaMemberListBean ifaMemberListBean;
                            String responseArray=responseObject.getString("data");
                            JSONObject memberListObject = new JSONObject(responseArray);
                            JSONArray memberListArray= memberListObject.getJSONArray("members_list");
                            ifaMemberListBean = new IfaMemberListBean();
                            ifaMemberListBean.setFullName("All");
                            ifaMemberBeanArrayList.add(ifaMemberListBean);

                            for(int i=0;i<memberListArray.length();i++){
                                JSONObject myFriendObject = memberListArray.getJSONObject(i);
                                ifaMemberListBean = new IfaMemberListBean();

                                ifaMemberListBean.setFriendId(myFriendObject.getString("friend_id"));
                                ifaMemberListBean.setRoleId(myFriendObject.getString("role_id"));
                                ifaMemberListBean.setRoleName(myFriendObject.getString("role_name"));
                                ifaMemberListBean.setRoleCode(myFriendObject.getString("role_code"));
                                ifaMemberListBean.setFullName(myFriendObject.getString("full_name"));
                                ifaMemberListBean.setTitle(myFriendObject.getString("title"));
                                ifaMemberListBean.setProfilePicture(myFriendObject.getString("profile_picture"));
                                ifaMemberListBean.setProfilePicUrl(myFriendObject.getString("profile_picture_url"));
                                ifaMemberListBean.setDob(myFriendObject.getString("dob"));
                                ifaMemberListBean.setDobFormatted(myFriendObject.getString("dob_formatted"));
                                ifaMemberListBean.setGender(myFriendObject.getString("gender"));
                                ifaMemberListBean.setFavoritePlayer(myFriendObject.getString("favourite_player"));
                                ifaMemberListBean.setIsPlayer(myFriendObject.getString("is_private"));
                                ifaMemberListBean.setFavoriteTeam(myFriendObject.getString("favourite_team"));
                                ifaMemberListBean.setFavoritePosition(myFriendObject.getString("favourite_position"));
                                ifaMemberListBean.setHeightFormatted(myFriendObject.getString("height_formatted"));
                                ifaMemberListBean.setWeightFormatted(myFriendObject.getString("weight_formatted"));

                                ifaMemberBeanArrayList.add(ifaMemberListBean);

                            }

                            JSONArray dayListArray = memberListObject.getJSONArray("days_list");
                            for(int i=0; i<dayListArray.length(); i++){
                                JSONObject dayListObject = dayListArray.getJSONObject(i);
                                ifaMemberListBean = new IfaMemberListBean();

                                ifaMemberListBean.setDayName(dayListObject.getString("day_name"));
                                ifaMemberListBean.setDayValue(dayListObject.getString("day_value"));

                                ifaDayListArrayList.add(ifaMemberListBean);
                            }

                        } else {
                            ifaMemberBeanArrayList.clear();
                            Toast.makeText(ChildMyFriendSendInviteScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildMyFriendSendInviteScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                break;

            case SEND_REQUEST_IFA_MEMBER:

                if (response == null) {
                    Toast.makeText(ChildMyFriendSendInviteScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if(status){
                            finish();
                        }
                        Toast.makeText(ChildMyFriendSendInviteScreen.this, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildMyFriendSendInviteScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case SEND_REQUEST_NON_IFA_MEMBER:
                if (response == null) {
                    Toast.makeText(ChildMyFriendSendInviteScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if(status){
                            friendname.setText("");
                            friendEmail.setText("");
                            friendMessage.setText("");
                            finish();
                        }

                        Toast.makeText(ChildMyFriendSendInviteScreen.this, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildMyFriendSendInviteScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;


        }
    }



    public class IfaMemberAdapter extends BaseAdapter {
        Context context;
        ArrayList<IfaMemberListBean> ifaMemberListBeanArrayList;
        LayoutInflater layoutInflater;
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options;

        Typeface helvetica;
        Typeface linoType;

        public IfaMemberAdapter(Context context, ArrayList<IfaMemberListBean> ifaMemberListBeanArrayList){
            this.context = context;
            this.ifaMemberListBeanArrayList = ifaMemberListBeanArrayList;
            layoutInflater = LayoutInflater.from(context);

            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.placeholder)
                    .showImageForEmptyUri(R.drawable.placeholder)
                    .showImageOnFail(R.drawable.placeholder)
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
            linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        }

        @Override
        public int getCount() {
            return ifaMemberListBeanArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return ifaMemberListBeanArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.child_adapter_ifa_member_list_item, null);

            final TextView ifaMemberName = (TextView) convertView.findViewById(R.id.ifaMemberName);

            final IfaMemberListBean ifaMemberListBean = ifaMemberListBeanArrayList.get(position);

            ifaMemberName.setText(ifaMemberListBean.getFullName());

            // createdDate.setText(galleryBean.getCreatedAt());
            //imageLoader.displayImage(galleryBean.getPicUrl(), picUrl, options);
            ifaMemberName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friendId = ifaMemberListBean.getFriendId();
                    fullName = ifaMemberListBean.getFullName();
                    dob = ifaMemberListBean.getDobFormatted();
                    gender = ifaMemberListBean.getGender();
                    height = ifaMemberListBean.getHeightFormatted();
                    weight = ifaMemberListBean.getWeightFormatted();
                    favTeam = ifaMemberListBean.getFavoriteTeam();
                    favPos = ifaMemberListBean.getFavoritePosition();
                    favPlayer = ifaMemberListBean.getFavoritePlayer();
                    profilePic = ifaMemberListBean.getProfilePicUrl();
                    isPrivate = ifaMemberListBean.getIsPlayer();
                    dialog.dismiss();
                    ifaFriendName.setText(ifaMemberListBean.getFullName());

                }
            });

            return convertView;
        }

//        // Filter Class
//        public void filter(String charText) {
//            charText = charText.toLowerCase(Locale.getDefault());
//            ifaMemberListBeanArrayList.clear();
//            if (charText.length() == 0) {
//                ifaMemberListBeanArrayList.addAll(arraylist);
//            } else {
//                for (IfaMemberListBean wp : arraylist) {
//                    if (wp.getFullName().toLowerCase(Locale.getDefault())
//                            .contains(charText)) {
//                        ifaMemberListBeanArrayList.add(wp);
//                    }
//                }
//            }
//            notifyDataSetChanged();
//        }
    }


    public class IfaDayListAdapter extends BaseAdapter {
        Context context;
        ArrayList<IfaMemberListBean> ifaMemberListBeanArrayList;
        LayoutInflater layoutInflater;
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options;

        Typeface helvetica;
        Typeface linoType;

        public IfaDayListAdapter(Context context, ArrayList<IfaMemberListBean> ifaMemberListBeanArrayList){
            this.context = context;
            this.ifaMemberListBeanArrayList = ifaMemberListBeanArrayList;
            layoutInflater = LayoutInflater.from(context);

            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.placeholder)
                    .showImageForEmptyUri(R.drawable.placeholder)
                    .showImageOnFail(R.drawable.placeholder)
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
            linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        }

        @Override
        public int getCount() {
            return ifaMemberListBeanArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return ifaMemberListBeanArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.child_adapter_ifa_member_list_item, null);

            final TextView ifaMemberName = (TextView) convertView.findViewById(R.id.ifaMemberName);

            final IfaMemberListBean ifaMemberListBean = ifaMemberListBeanArrayList.get(position);

            ifaMemberName.setText(ifaMemberListBean.getDayName());
           // ifaMemberName.setText(ifaMemberListBean.getFullName());


            // createdDate.setText(galleryBean.getCreatedAt());
            //imageLoader.displayImage(galleryBean.getPicUrl(), picUrl, options);
            ifaMemberName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    selectDay.setText(ifaMemberListBean.getDayName());

                    if(Utilities.isNetworkAvailable(ChildMyFriendSendInviteScreen.this)) {
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("location_filter", "0"));
                        nameValuePairList.add(new BasicNameValuePair("day_filter", ifaMemberListBean.getDayValue()));

                        String webServiceUrl = Utilities.BASE_URL + "children/ifa_members_list";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:" + loggedInUser.getId());
                        headers.add("X-access-token:" + loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyFriendSendInviteScreen.this, nameValuePairList, GET_IFA_MEMBER_DAY, ChildMyFriendSendInviteScreen.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);
                    } else {
                        Toast.makeText(ChildMyFriendSendInviteScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return convertView;
        }
    }

    public class IfaLocationAdapter extends BaseAdapter {
        Context context;
        ArrayList<IfaMemberListBean> ifaMemberListBeanArrayList;
        LayoutInflater layoutInflater;
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options;

        Typeface helvetica;
        Typeface linoType;

        public IfaLocationAdapter(Context context, ArrayList<IfaMemberListBean> ifaMemberListBeanArrayList){
            this.context = context;
            this.ifaMemberListBeanArrayList = ifaMemberListBeanArrayList;
            layoutInflater = LayoutInflater.from(context);

            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.placeholder)
                    .showImageForEmptyUri(R.drawable.placeholder)
                    .showImageOnFail(R.drawable.placeholder)
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
            linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        }

        @Override
        public int getCount() {
            return ifaMemberListBeanArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return ifaMemberListBeanArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.child_adapter_ifa_member_list_item, null);

            final TextView ifaMemberName = (TextView) convertView.findViewById(R.id.ifaMemberName);

            final IfaMemberListBean ifaMemberListBean = ifaMemberListBeanArrayList.get(position);

            ifaMemberName.setText(ifaMemberListBean.getName());

            // createdDate.setText(galleryBean.getCreatedAt());
            //imageLoader.displayImage(galleryBean.getPicUrl(), picUrl, options);
            ifaMemberName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    selectLocation.setText(ifaMemberListBean.getName());

                    if(Utilities.isNetworkAvailable(ChildMyFriendSendInviteScreen.this)) {
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("location_filter", ifaMemberListBean.getId()));
                        nameValuePairList.add(new BasicNameValuePair("day_filter", "0"));

                        String webServiceUrl = Utilities.BASE_URL + "children/ifa_members_list";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:" + loggedInUser.getId());
                        headers.add("X-access-token:" + loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyFriendSendInviteScreen.this, nameValuePairList, GET_IFA_MEMBER, ChildMyFriendSendInviteScreen.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);
                    } else {
                        Toast.makeText(ChildMyFriendSendInviteScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return convertView;
        }
    }



}

