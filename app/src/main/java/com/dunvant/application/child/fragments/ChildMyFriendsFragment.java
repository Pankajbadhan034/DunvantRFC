package com.dunvant.application.child.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.MyFriendsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildMyFriendRequestsScreen;
import com.dunvant.application.child.ChildMyFriendSendInviteScreen;
import com.dunvant.application.child.adapters.ChildMyFriendsExpandableAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DEVLABS\pbadhan on 6/12/16.
 */
public class ChildMyFriendsFragment extends Fragment implements IWebServiceCallback {
    Button sendInvites;
    Button friendRequest;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    ExpandableListView myFriendsList;
    ChildMyFriendsExpandableAdapter childMyFriendsExpandableAdapter;
    ArrayList<MyFriendsBean> myFriendsBeanArrayList = new ArrayList<>();
    private final String MY_FRIENDS = "MY_FRIENDS";
    Typeface helvetica;
    Typeface linoType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_fragment_my_friends, container, false);
        myFriendsList=(ExpandableListView) view.findViewById(R.id.myFriendsList);
        friendRequest = (Button) view.findViewById(R.id.friendRequest);
        sendInvites = (Button) view.findViewById(R.id.sendInvites);

        friendRequest.setTypeface(linoType);
        sendInvites.setTypeface(linoType);


        friendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(getActivity(), ChildMyFriendRequestsScreen.class);
                startActivity(obj);
            }
        });

        sendInvites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utilities.isNetworkAvailable(getActivity())) {
                    Intent obj = new Intent(getActivity(), ChildMyFriendSendInviteScreen.class);
                    startActivity(obj);
                }else {
                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
//            nameValuePairList.add(new BasicNameValuePair("friend_id", ));

            String webServiceUrl = Utilities.BASE_URL + "children/child_friend_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, MY_FRIENDS, ChildMyFriendsFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case MY_FRIENDS:

                myFriendsBeanArrayList.clear();
                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            MyFriendsBean myFriendsBean;
                            String responseArray=responseObject.getString("data");
                            JSONArray myFriendsArray=new JSONArray(responseArray);
//                            System.out.println("array__"+myFriendsArray);
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
                                myFriendsBean.setDobFormatted(myFriendObject.getString("dob_formatted"));
                                myFriendsBean.setGender(myFriendObject.getString("gender"));
                                myFriendsBean.setSchool(myFriendObject.getString("school"));
                                myFriendsBean.setFavTeam(myFriendObject.getString("favourite_team"));
                                myFriendsBean.setFavPlayer(myFriendObject.getString("favourite_player"));
                                myFriendsBean.setFavPlayerPicture(myFriendObject.getString("favourite_player_picture"));
                                myFriendsBean.setFavTeamPicture(myFriendObject.getString("favourite_team_picture"));
                                myFriendsBean.setFavPosition(myFriendObject.getString("favourite_position"));
                                myFriendsBean.setHeight(myFriendObject.getString("height"));
                                myFriendsBean.setWeight(myFriendObject.getString("weight"));
                                myFriendsBean.setHeightFormatted(myFriendObject.getString("height_formatted"));
                                myFriendsBean.setWeightFormatted(myFriendObject.getString("weight_formatted"));
                                myFriendsBean.setFriendStatus(myFriendObject.getString("friend_status"));
                                myFriendsBean.setBlockBy(myFriendObject.getString("block_by"));
                                myFriendsBean.setGenderFormatted(myFriendObject.getString("gender_formatted"));
                                myFriendsBean.setNationality(myFriendObject.getString("nationality"));
                                myFriendsBean.setFavFootbalBoot(myFriendObject.getString("favourite_football_boot"));
                                myFriendsBean.setFavFood(myFriendObject.getString("favourite_food"));
                                myFriendsBean.setIsPrivate(myFriendObject.getString("is_private"));

                                if(responseObject.has("is_private_show")){
                                    myFriendsBean.setIsPrivateShow(responseObject.getString("is_private_show"));
                                }else if(myFriendObject.has("is_private_show")){
                                    myFriendsBean.setIsPrivateShow(myFriendObject.getString("is_private_show"));
                                }else{
                                    myFriendsBean.setIsPrivateShow("0");
                                }

                                myFriendsBeanArrayList.add(myFriendsBean);

                            }
                            childMyFriendsExpandableAdapter = new ChildMyFriendsExpandableAdapter(getActivity(),myFriendsBeanArrayList);
                            myFriendsList.setAdapter(childMyFriendsExpandableAdapter);
                            childMyFriendsExpandableAdapter.notifyDataSetChanged();

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
}
