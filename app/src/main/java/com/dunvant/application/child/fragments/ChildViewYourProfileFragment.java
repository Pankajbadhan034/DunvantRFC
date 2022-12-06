package com.dunvant.application.child.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
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
import com.dunvant.application.beans.ChildMyGalleryBean;
import com.dunvant.application.beans.DataRegistrationBean;
import com.dunvant.application.beans.MyFriendsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.adapters.ChildViewYourProfileAdapter;
import com.dunvant.application.child.adapters.ChildViewYourProfileFriendListAdapter;
import com.dunvant.application.child.adapters.ChildViewYourProfileGalleryListAdapter;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by DEVLABS\pbadhan on 6/12/16.
 */
public class ChildViewYourProfileFragment  extends Fragment implements IWebServiceCallback {
    ArrayList<DataRegistrationBean> dataRegistrationBeanArrayList = new ArrayList<>();
    ListView listView;
    TextView text;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    private final String GET_PROFILE = "GET_PROFILE";
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    private ImageView profilePic;
    private TextView name;
    private TextView date;
    private TextView genderHeightWeight;
    private TextView fav_team;
    private TextView fav_player;
    private TextView fav_position;
    private TextView nationality;
    private Button friendList;
    private Button gallery;

    private TextView weight;
    private TextView height;
    private TextView favFootballBoot;
    private ImageView favPlayerImage;
    private ImageView favTeamImage;
    TextView favFoodLbl;
    TextView favFood;
    TextView schoolLbl;
    TextView school;


    ChildViewYourProfileGalleryListAdapter childViewYourProfileGalleryListAdapter;
    ArrayList<ChildMyGalleryBean> childMyGalleryArrayList = new ArrayList<>();
    private GridView myGalleryList;
    private final String GET_MY_GALLERY = "GET_MY_GALLERY";

    ChildViewYourProfileFriendListAdapter childViewYourProfileFriendListAdapter;
    ArrayList<MyFriendsBean> myFriendsBeanArrayList = new ArrayList<>();
    private ListView list;
    private final String MY_FRIENDS = "MY_FRIENDS";
    Typeface helvetica;
    Typeface linoType;

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

        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LinotypeOrdinarRegular.ttf");
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_fragment_get_your_profile, container, false);
        profilePic = (ImageView) view.findViewById(R.id.profilePic);
        name =( TextView) view.findViewById(R.id.name);
        date = (TextView) view.findViewById(R.id.date);
        genderHeightWeight = (TextView) view.findViewById(R.id.genderHeightWeight);
        fav_team = (TextView) view.findViewById(R.id.fav_team);
        fav_player = (TextView) view.findViewById(R.id.fav_player);
        fav_position = (TextView) view.findViewById(R.id.fav_position);
        nationality = (TextView) view.findViewById(R.id.nationality);
        friendList = (Button) view.findViewById(R.id.friendList);
        gallery = (Button) view.findViewById(R.id.gallery);
        list = (ListView) view.findViewById(R.id.list);
        myGalleryList = (GridView) view.findViewById(R.id.myGalleryList);
        text = (TextView) view.findViewById(R.id.text);
        favPlayerImage = (ImageView) view.findViewById(R.id.favPlayerImage);
        favTeamImage = (ImageView) view.findViewById(R.id.favTeamImage);
        favFootballBoot = (TextView) view.findViewById(R.id.favFootballBoot);
        weight = (TextView) view.findViewById(R.id.weight);
        height = (TextView) view.findViewById(R.id.height);

        favFoodLbl = (TextView) view.findViewById(R.id.favFoodLbl);
        favFood = (TextView) view.findViewById(R.id.favFood);
        schoolLbl = (TextView) view.findViewById(R.id.schoolLbl);
        school = (TextView) view.findViewById(R.id.school);
        listView = view.findViewById(R.id.listView);

//        private TextView weightLbl;
//        private TextView weight;
//        private TextView heightLbl;
//        private TextView height;
//        private TextView favPlayerLbl;
//        private TextView favTeamLbl;
//        private TextView favPosLbl;
//        private TextView nationalityLbl;
//        private TextView nationality;
//        private TextView favBootLbl;
//        private TextView favFootballBoot;
//        private ImageView favPlayerImage;
//        private ImageView favTeamImage;

        text = (TextView) view.findViewById(R.id.text);

        name.setTypeface(helvetica);
        date.setTypeface(helvetica);
        genderHeightWeight.setTypeface(helvetica);
        fav_team.setTypeface(helvetica);
        fav_player.setTypeface(helvetica);
        fav_position.setTypeface(helvetica);
        nationality.setTypeface(helvetica);
        friendList.setTypeface(linoType);
        gallery.setTypeface(linoType);
        favFootballBoot.setTypeface(helvetica);
        text.setTypeface(helvetica);

        favFood.setTypeface(helvetica);
        school.setTypeface(helvetica);


        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
//            nameValuePairList.add(new BasicNameValuePair("friend_id", ));

            String webServiceUrl = Utilities.BASE_URL + "children/child_friend_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, MY_FRIENDS, ChildViewYourProfileFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }



        friendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendListTab();
                if(Utilities.isNetworkAvailable(getActivity())) {

                    List<NameValuePair> nameValuePairList = new ArrayList<>();

                    String webServiceUrl = Utilities.BASE_URL + "children/child_friend_list";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, MY_FRIENDS, ChildViewYourProfileFragment.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryListTab();
                if (Utilities.isNetworkAvailable(getActivity())) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("offset", "0"));
                    nameValuePairList.add(new BasicNameValuePair("gallery_type", "image"));

                    String webServiceUrl = Utilities.BASE_URL + "children/gallery_list";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_MY_GALLERY, ChildViewYourProfileFragment.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

//        System.out.println("loggedINuserWeight::"+loggedInUser.getWeightFormatted());
        weight.setText(loggedInUser.getWeightFormatted());
        height.setText(loggedInUser.getHeightFormatted());
        name.setText(loggedInUser.getFullName());
        date.setText(loggedInUser.getFullName());
        genderHeightWeight.setText(loggedInUser.getGender()+" "+loggedInUser.getHeight()+" "+loggedInUser.getWeight());
        genderHeightWeight.setVisibility(View.GONE);
        fav_team.setText(loggedInUser.getFavoriteTeam());
        fav_player.setText(loggedInUser.getFavoritePlayer());
        fav_position.setText(loggedInUser.getFavoritePosition());
        imageLoader.displayImage(loggedInUser.getProfilePicPath(), profilePic, options);
        imageLoader.displayImage(loggedInUser.getFavoriteTemaPicture(), favTeamImage, options);
        imageLoader.displayImage(loggedInUser.getFavoritePlayerPicture(), favPlayerImage, options);
        favFootballBoot.setText(loggedInUser.getFavoriteFootballBoot());
        school.setText(loggedInUser.getSchool());
        favFood.setText(loggedInUser.getFavoritefood());
        nationality.setText(loggedInUser.getNationality());
        return view;
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
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
                            ListVisiblilities();
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
                                myFriendsBean.setSchool(myFriendObject.getString("school"));
                                myFriendsBean.setFavPlayer(myFriendObject.getString("favourite_player"));
                                myFriendsBean.setFavPlayerPicture(myFriendObject.getString("favourite_player_picture"));
                                myFriendsBean.setFavTeam(myFriendObject.getString("favourite_team"));
                                myFriendsBean.setFavTeamPicture(myFriendObject.getString("favourite_team_picture"));
                                myFriendsBean.setFavPosition(myFriendObject.getString("favourite_position"));
                                if(myFriendObject.getString("height_formatted").equalsIgnoreCase("null")){
                                    myFriendsBean.setHeight("");
                                }else{
                                    myFriendsBean.setHeight(myFriendObject.getString("height_formatted"));
                                }

                                if(myFriendObject.getString("weight_formatted").equalsIgnoreCase("null")){
                                    myFriendsBean.setWeight("");
                                }else{
                                    myFriendsBean.setWeight(myFriendObject.getString("weight_formatted"));
                                }

                                myFriendsBean.setFriendStatus(myFriendObject.getString("friend_status"));
                                myFriendsBean.setBlockBy(myFriendObject.getString("block_by"));
                                myFriendsBean.setFavFootbalBoot(myFriendObject.getString("favourite_football_boot"));
                                myFriendsBean.setNationality(myFriendObject.getString("nationality"));
                                myFriendsBean.setFavFood(myFriendObject.getString("favourite_food"));
                                myFriendsBean.setGenderFormatted(myFriendObject.getString("gender_formatted"));
                                myFriendsBean.setIsPrivate(myFriendObject.getString("is_private"));

                                myFriendsBeanArrayList.add(myFriendsBean);

                            }

                            childViewYourProfileFriendListAdapter = new ChildViewYourProfileFriendListAdapter(getActivity(),myFriendsBeanArrayList);
                            list.setAdapter(childViewYourProfileFriendListAdapter);
                            childViewYourProfileFriendListAdapter.notifyDataSetChanged();
                            Utilities.setListViewHeightBasedOnChildren(list);

                            JSONArray data_registration = responseObject.getJSONArray("data_registration");
                            for(int i=0; i<data_registration.length(); i++){
                                DataRegistrationBean dataRegistrationBean = new DataRegistrationBean();
                                JSONObject jsonObject = data_registration.getJSONObject(i);
                                dataRegistrationBean.setLabel_name(jsonObject.getString("label_name"));
                                dataRegistrationBean.setValue(jsonObject.getString("value"));

                                dataRegistrationBeanArrayList.add(dataRegistrationBean);
                            }

                            ChildViewYourProfileAdapter childViewYourProfileAdapter = new ChildViewYourProfileAdapter(getActivity(), dataRegistrationBeanArrayList);
                            listView.setAdapter(childViewYourProfileAdapter);
                            Utilities.setListViewHeightBasedOnChildren(listView);


                        }else {
                            textVisibilities();
                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case GET_MY_GALLERY:

                childMyGalleryArrayList.clear();
                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            GalleryVisibility();

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

                            childViewYourProfileGalleryListAdapter = new ChildViewYourProfileGalleryListAdapter(getActivity(),childMyGalleryArrayList);
                            myGalleryList.setAdapter(childViewYourProfileGalleryListAdapter);
                            childViewYourProfileGalleryListAdapter.notifyDataSetChanged();
                            Utilities.setGridViewHeightBasedOnChildren(myGalleryList, 2);

                        }else {
                            textVisibilities();
                           // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;
        }
    }

    public void friendListTab(){
        text.setText("No Friend Found");
        friendList.setBackgroundColor(getActivity().getResources().getColor(R.color.yellow));
        gallery.setBackgroundColor(Color.parseColor("#333333"));
        friendList.setTextColor(Color.parseColor("#333333"));
        gallery.setTextColor(Color.parseColor("#ffffff"));
        list.setVisibility(View.VISIBLE);
        myGalleryList.setVisibility(View.GONE);
    }

    public void galleryListTab(){
        text.setText("No Gallery Found");
        friendList.setBackgroundColor(Color.parseColor("#333333"));
        gallery.setBackgroundColor(getActivity().getResources().getColor(R.color.yellow));
        friendList.setTextColor(Color.parseColor("#ffffff"));
        gallery.setTextColor(Color.parseColor("#333333"));
        list.setVisibility(View.GONE);
        myGalleryList.setVisibility(View.VISIBLE);
    }

    public void GalleryVisibility(){
        list.setVisibility(View.GONE);
        myGalleryList.setVisibility(View.VISIBLE);
        text.setVisibility(View.GONE);
    }

    public void ListVisiblilities(){
        list.setVisibility(View.VISIBLE);
        myGalleryList.setVisibility(View.GONE);
        text.setVisibility(View.GONE);
    }

    public void textVisibilities(){
        list.setVisibility(View.GONE);
        myGalleryList.setVisibility(View.GONE);
        text.setVisibility(View.VISIBLE);
    }

}
