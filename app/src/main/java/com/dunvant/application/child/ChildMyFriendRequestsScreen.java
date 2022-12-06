package com.dunvant.application.child;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.MyFriendsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.adapters.ChildMyFriendRequestsAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChildMyFriendRequestsScreen extends AppCompatActivity implements IWebServiceCallback {

    private ImageView refresh;
    private ListView list;
    private Button receivedRequest;
    private Button sentRequest;
    private ImageView backButton;

    private final String GET_RECEIVE = "GET_RECEIVE";
    private final String GET_SENT = "GET_SENT";
    ChildMyFriendRequestsAdapter childMyFriendRequestsAdapter;
    ArrayList<MyFriendsBean> myFriendsBeanArrayList = new ArrayList<>();

    Typeface helvetica;
    Typeface linoType;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_my_friend_requests_screen);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        list = (ListView) findViewById(R.id.list);
        receivedRequest = (Button) findViewById(R.id.receivedRequest);
        sentRequest = (Button) findViewById(R.id.sentRequest);
        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        refresh = (ImageView) findViewById(R.id.refresh);

        title.setTypeface(linoType);
        receivedRequest.setTypeface(helvetica);
        sentRequest.setTypeface(helvetica);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFriendsBeanArrayList.clear();
                if (Utilities.isNetworkAvailable(ChildMyFriendRequestsScreen.this)) {

                    String webServiceUrl = Utilities.BASE_URL + "children/received_friend_request_list";
                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    if (Utilities.isNetworkAvailable(ChildMyFriendRequestsScreen.this)) {
                        GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ChildMyFriendRequestsScreen.this, GET_RECEIVE, ChildMyFriendRequestsScreen.this, headers);
                        getWebServiceWithHeadersAsync.execute(webServiceUrl);
                    } else {
                        Toast.makeText(ChildMyFriendRequestsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ChildMyFriendRequestsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (Utilities.isNetworkAvailable(ChildMyFriendRequestsScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "children/received_friend_request_list";
            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            if (Utilities.isNetworkAvailable(ChildMyFriendRequestsScreen.this)) {
                GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ChildMyFriendRequestsScreen.this, GET_RECEIVE, ChildMyFriendRequestsScreen.this, headers);
                getWebServiceWithHeadersAsync.execute(webServiceUrl);
            } else {
                Toast.makeText(ChildMyFriendRequestsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(ChildMyFriendRequestsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }


        receivedRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                receiveTab();

                String webServiceUrl = Utilities.BASE_URL + "children/received_friend_request_list";
                ArrayList<String> headers = new ArrayList<>();
                headers.add("X-access-uid:" + loggedInUser.getId());
                headers.add("X-access-token:" + loggedInUser.getToken());

                if (Utilities.isNetworkAvailable(ChildMyFriendRequestsScreen.this)) {
                    GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ChildMyFriendRequestsScreen.this, GET_RECEIVE, ChildMyFriendRequestsScreen.this, headers);
                    getWebServiceWithHeadersAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(ChildMyFriendRequestsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }

            }
        });

        sentRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sentTab();

                String webServiceUrl = Utilities.BASE_URL + "children/sent_friend_request_list";
                ArrayList<String> headers = new ArrayList<>();
                headers.add("X-access-uid:" + loggedInUser.getId());
                headers.add("X-access-token:" + loggedInUser.getToken());

                if (Utilities.isNetworkAvailable(ChildMyFriendRequestsScreen.this)) {
                    GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ChildMyFriendRequestsScreen.this, GET_SENT, ChildMyFriendRequestsScreen.this, headers);
                    getWebServiceWithHeadersAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(ChildMyFriendRequestsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
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
            case GET_RECEIVE:


//                response = "{\n" +
//                        "\t\"status\": true,\n" +
//                        "\t\"message\": \"Record Found.\",\n" +
//                        "\t\"data\": [{\n" +
//                        "\t\t\"friend_id\": \"4\",\n" +
//                        "\t\t\"full_name\": \"bacha receive two\",\n" +
//                        "\t\t\"created_at\": \"2016-12-02 17:44:02\",\n" +
//                        "\t\t\"created_at_formatted\": \"02 December 2016\",\n" +
//                        "\t\t\"title\": null,\n" +
//                        "\t\t\"profile_picture\": null,\n" +
//                        "\t\t\"profile_pic_url\": null\n" +
//                        "\t}]\n" +
//                        "}";
                if (response == null) {
                    Toast.makeText(ChildMyFriendRequestsScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");


                        if (status) {
                            String parentApproval = responseObject.getString("parent_approval");
                            String approval_age = responseObject.getString("approval_age");
                            MyFriendsBean myFriendsBean;
                            String responseArray = responseObject.getString("data");
                            JSONArray myFriendsArray = new JSONArray(responseArray);
                            for (int i = 0; i < myFriendsArray.length(); i++) {
                                JSONObject myFriendObject = myFriendsArray.getJSONObject(i);
                                myFriendsBean = new MyFriendsBean();
                                myFriendsBean.setFriendId(myFriendObject.getString("friend_id"));
                                myFriendsBean.setFullName(myFriendObject.getString("full_name"));
                                myFriendsBean.setCreatedAt(myFriendObject.getString("created_at"));
                                myFriendsBean.setCreatedAtFormatted(myFriendObject.getString("created_at_formatted"));
                                myFriendsBean.setTitle(myFriendObject.getString("title"));
                                myFriendsBean.setProfilePic(myFriendObject.getString("profile_picture"));
                                myFriendsBean.setPicUrl(myFriendObject.getString("profile_pic_url"));
                                myFriendsBean.setDobFormatted(myFriendObject.getString("dob_formatted"));
                                myFriendsBean.setGender(myFriendObject.getString("gender"));
                                myFriendsBean.setSchool(myFriendObject.getString("school"));
                                myFriendsBean.setFavPlayer(myFriendObject.getString("favourite_player"));
                                myFriendsBean.setFavPlayerPicture(myFriendObject.getString("favourite_player_picture"));
                                myFriendsBean.setFavTeam(myFriendObject.getString("favourite_team"));
                                myFriendsBean.setFavTeamPicture(myFriendObject.getString("favourite_team_picture"));
                                myFriendsBean.setFavPosition(myFriendObject.getString("favourite_position"));
                                myFriendsBean.setHeight(myFriendObject.getString("height_formatted"));
                                myFriendsBean.setWeight(myFriendObject.getString("weight_formatted"));
                                myFriendsBean.setFriendStatus(myFriendObject.getString("friend_status"));
                                myFriendsBean.setBlockBy(myFriendObject.getString("block_by"));
                                myFriendsBean.setFavFootbalBoot(myFriendObject.getString("favourite_football_boot"));
                                myFriendsBean.setNationality(myFriendObject.getString("nationality"));
                                myFriendsBean.setFavFood(myFriendObject.getString("favourite_food"));
                                myFriendsBean.setAgeValue(myFriendObject.getString("age_value"));
                                myFriendsBean.setParentApproval(parentApproval);
                                myFriendsBean.setApprovalAge(approval_age);

                                if (myFriendObject.has("is_private")) {
                                    myFriendsBean.setIsPrivate(myFriendObject.getString("is_private"));
                                } else {
                                    myFriendsBean.setIsPrivate("0");
                                }

                                if (responseObject.has("is_private_show")) {
                                    myFriendsBean.setIsPrivateShow(responseObject.getString("is_private_show"));
                                } else if (myFriendObject.has("is_private_show")) {
                                    myFriendsBean.setIsPrivateShow(myFriendObject.getString("is_private_show"));
                                } else {
                                    myFriendsBean.setIsPrivateShow("0");
                                }

                                myFriendsBeanArrayList.add(myFriendsBean);
                            }
                        } else {
                            Toast.makeText(ChildMyFriendRequestsScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                        childMyFriendRequestsAdapter = new ChildMyFriendRequestsAdapter(ChildMyFriendRequestsScreen.this, myFriendsBeanArrayList, "receive");
                        list.setAdapter(childMyFriendRequestsAdapter);
                        childMyFriendRequestsAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildMyFriendRequestsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case GET_SENT:


//                response = "{\n" +
//                        "\t\"status\": true,\n" +
//                        "\t\"message\": \"Record Found.\",\n" +
//                        "\t\"data\": [{\n" +
//                        "\t\t\"friend_id\": \"4\",\n" +
//                        "\t\t\"full_name\": \"bacha sent two\",\n" +
//                        "\t\t\"created_at\": \"2016-12-02 17:44:02\",\n" +
//                        "\t\t\"created_at_formatted\": \"02 December 2016\",\n" +
//                        "\t\t\"title\": null,\n" +
//                        "\t\t\"profile_picture\": null,\n" +
//                        "\t\t\"profile_pic_url\": null\n" +
//                        "\t}]\n" +
//                        "}";
                if (response == null) {
                    Toast.makeText(ChildMyFriendRequestsScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            MyFriendsBean myFriendsBean;
                            String responseArray = responseObject.getString("data");
                            JSONArray myFriendsArray = new JSONArray(responseArray);
                            for (int i = 0; i < myFriendsArray.length(); i++) {
                                JSONObject myFriendObject = myFriendsArray.getJSONObject(i);
                                myFriendsBean = new MyFriendsBean();

                                if (myFriendObject.getString("request_type").equalsIgnoreCase("2")) {
                                    myFriendsBean.setId(myFriendObject.getString("id"));
                                    myFriendsBean.setSentById(myFriendObject.getString("sent_by_id"));
                                    myFriendsBean.setMessage(myFriendObject.getString("message"));
                                    myFriendsBean.setEmail(myFriendObject.getString("email"));
                                    myFriendsBean.setCreatedAt(myFriendObject.getString("created_at"));
                                    myFriendsBean.setRequestType(myFriendObject.getString("request_type"));
                                    myFriendsBean.setCreatedAtFormatted(myFriendObject.getString("created_at_formatted"));
                                    myFriendsBean.setFullName(myFriendObject.getString("full_name"));
                                } else {
                                    myFriendsBean.setFriendId(myFriendObject.getString("friend_id"));
                                    myFriendsBean.setFullName(myFriendObject.getString("full_name"));
                                    myFriendsBean.setCreatedAt(myFriendObject.getString("created_at"));
                                    myFriendsBean.setCreatedAtFormatted(myFriendObject.getString("created_at_formatted"));
                                    myFriendsBean.setTitle(myFriendObject.getString("title"));
                                    myFriendsBean.setProfilePic(myFriendObject.getString("profile_picture"));
                                    myFriendsBean.setPicUrl(myFriendObject.getString("profile_pic_url"));
                                    myFriendsBean.setDobFormatted(myFriendObject.getString("dob_formatted"));
                                    myFriendsBean.setGender(myFriendObject.getString("gender"));
                                    myFriendsBean.setSchool(myFriendObject.getString("school"));
                                    myFriendsBean.setFavTeam(myFriendObject.getString("favourite_team"));
                                    myFriendsBean.setRequestType(myFriendObject.getString("request_type"));

                                    myFriendsBean.setFavPlayer(myFriendObject.getString("favourite_player"));
                                    myFriendsBean.setFavPlayerPicture(myFriendObject.getString("favourite_player_picture"));
                                    myFriendsBean.setFavTeamPicture(myFriendObject.getString("favourite_team_picture"));
                                    myFriendsBean.setFavPosition(myFriendObject.getString("favourite_position"));
                                    myFriendsBean.setHeight(myFriendObject.getString("height_formatted"));
                                    myFriendsBean.setWeight(myFriendObject.getString("weight_formatted"));
                                    myFriendsBean.setFriendStatus(myFriendObject.getString("friend_status"));
                                    myFriendsBean.setBlockBy(myFriendObject.getString("block_by"));
                                    myFriendsBean.setFavFootbalBoot(myFriendObject.getString("favourite_football_boot"));
                                    myFriendsBean.setNationality(myFriendObject.getString("nationality"));
                                    myFriendsBean.setFavFood(myFriendObject.getString("favourite_food"));

                                    // Added following is_private and is_private_show code - Gene
                                    if (myFriendObject.has("is_private")) {
                                        myFriendsBean.setIsPrivate(myFriendObject.getString("is_private"));
                                    } else {
                                        myFriendsBean.setIsPrivate("0");
                                    }
                                    if (responseObject.has("is_private_show")) {
                                        myFriendsBean.setIsPrivateShow(responseObject.getString("is_private_show"));
                                    } else if (myFriendObject.has("is_private_show")) {
                                        myFriendsBean.setIsPrivateShow(myFriendObject.getString("is_private_show"));
                                    } else {
                                        myFriendsBean.setIsPrivateShow("0");
                                    }
                                }

                                myFriendsBeanArrayList.add(myFriendsBean);
                            }
                        } else {
                            Toast.makeText(ChildMyFriendRequestsScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                        childMyFriendRequestsAdapter = new ChildMyFriendRequestsAdapter(ChildMyFriendRequestsScreen.this, myFriendsBeanArrayList, "sent");
                        list.setAdapter(childMyFriendRequestsAdapter);
                        childMyFriendRequestsAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildMyFriendRequestsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    public void receiveTab() {
        myFriendsBeanArrayList.clear();
        refresh.setVisibility(View.VISIBLE);
        receivedRequest.setBackgroundColor(getResources().getColor(R.color.yellow));
        receivedRequest.setTextColor(Color.parseColor("#333333"));
        sentRequest.setBackgroundColor(Color.parseColor("#333333"));
        sentRequest.setTextColor(Color.parseColor("#ffffff"));
    }

    public void sentTab() {
        myFriendsBeanArrayList.clear();
        refresh.setVisibility(View.GONE);
        sentRequest.setBackgroundColor(getResources().getColor(R.color.yellow));
        sentRequest.setTextColor(Color.parseColor("#333333"));
        receivedRequest.setBackgroundColor(Color.parseColor("#333333"));
        receivedRequest.setTextColor(Color.parseColor("#ffffff"));
    }
}
