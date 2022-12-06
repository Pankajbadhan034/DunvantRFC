package com.dunvant.application.child.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.MyFriendsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildMyFriendProfileScreen;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildMyFriendRequestsAdapter extends BaseAdapter implements IWebServiceCallback {
    Dialog dialog;
    int positionValue;
    private final String CANCEL_OR_ACCEPT = "CANCEL_OR_ACCEPT";
    private final String CANCEL_OR_ACCEPT_BY_APPROVAL = "CANCEL_OR_ACCEPT_BY_APPROVAL";
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Context context;
    ArrayList<MyFriendsBean> myFriendsBeanArrayList;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    String strGender = "";
    Typeface helvetica;
    Typeface linoType;
    String checkRequestType;

    public ChildMyFriendRequestsAdapter(Context context, ArrayList<MyFriendsBean> myFriendsBeanArrayList, String checkRequestType){
        this.context = context;
        this.myFriendsBeanArrayList = myFriendsBeanArrayList;
        this.checkRequestType = checkRequestType;
        layoutInflater = LayoutInflater.from(context);

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .displayer(new RoundedBitmapDisplayer(1000))
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

    }

    @Override
    public int getCount() {
        return myFriendsBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return myFriendsBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_my_friend_request_item, null);

        ImageView picUrl = (ImageView) convertView.findViewById(R.id.picUrl);
        TextView fullName = (TextView) convertView.findViewById(R.id.fullName);
        TextView createdDate = (TextView) convertView.findViewById(R.id.createdDate);
        ImageView cancel = (ImageView) convertView.findViewById(R.id.cancel);
        ImageView accept = (ImageView) convertView.findViewById(R.id.accept);
        TextView dob =(TextView) convertView.findViewById(R.id.dob);
        LinearLayout profileView = (LinearLayout) convertView.findViewById(R.id.profileView);

        fullName.setTypeface(helvetica);
        createdDate.setTypeface(helvetica);
        dob.setTypeface(helvetica);



        final MyFriendsBean myFriendsBean = myFriendsBeanArrayList.get(position);

        fullName.setText(myFriendsBean.getFullName());
        createdDate.setText(myFriendsBean.getCreatedAtFormatted());

        if(position % 2 == 0) {
            profileView.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            fullName.setTextColor(context.getResources().getColor(R.color.white));
            createdDate.setTextColor(context.getResources().getColor(R.color.white));
            dob.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            profileView.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            fullName.setTextColor(context.getResources().getColor(R.color.black));
            createdDate.setTextColor(context.getResources().getColor(R.color.black));
            dob.setTextColor(context.getResources().getColor(R.color.black));
        }

        if(checkRequestType.equalsIgnoreCase("sent")){
            accept.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            if(myFriendsBean.getRequestType().equalsIgnoreCase("1")){
                switch (myFriendsBean.getGender()) {
                    case "0":
                        strGender = "Male";
                        break;
                    case "1":
                        strGender = "Female";
                        break;
                }
                imageLoader.displayImage(myFriendsBean.getPicUrl(), picUrl, options);
            }else{
                imageLoader.displayImage("noPic", picUrl, options);
            }
        }else if(checkRequestType.equalsIgnoreCase("receive")){
            accept.setVisibility(View.VISIBLE);
            switch (myFriendsBean.getGender()) {
                case "0":
                    strGender = "Male";
                    break;
                case "1":
                    strGender = "Female";
                    break;
            }
            imageLoader.displayImage(myFriendsBean.getPicUrl(), picUrl, options);
        }





        dob.setText("DOB: "+myFriendsBean.getDobFormatted()+" | GENDER: "+strGender+" | SCHOOL: "+myFriendsBean.getSchool()+" | FAV TEAM: "+myFriendsBean.getFavTeam());

        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkRequestType.equalsIgnoreCase("sent")){
                    if(myFriendsBean.getRequestType().equalsIgnoreCase("1")){

                        // Changing following 0 to 1 - Gene
//                        if(myFriendsBean.getIsPrivate().equalsIgnoreCase("0")){

                        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                        if(myFriendsBean.getIsPrivate().equalsIgnoreCase("1")){
                            Toast.makeText(context, "You cannot view this "+verbiage_singular+"'s profile as he has set to private.", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(context, "You can not view this child's profile as he has set to private.", Toast.LENGTH_SHORT).show();

//                            if(myFriendsBean.getIsPrivateShow().equalsIgnoreCase("0")){
//                                Toast.makeText(context, "You can not view this child's profile as he has set to private.", Toast.LENGTH_SHORT).show();
//                            }else{
//                                Intent obj = new Intent(context, ChildMyFriendProfileScreen.class);
//                                obj.putExtra("friendId", myFriendsBean.getFriendId());
//                                obj.putExtra("fullName", myFriendsBean.getFullName());
//                                obj.putExtra("dob", myFriendsBean.getDobFormatted());
//                                obj.putExtra("gender", strGender);
//                                obj.putExtra("height", myFriendsBean.getHeight());
//                                obj.putExtra("weight", myFriendsBean.getWeight());
//                                obj.putExtra("favTeam", myFriendsBean.getFavTeam());
//                                obj.putExtra("favPos", myFriendsBean.getFavPosition());
//                                obj.putExtra("favPlayer", myFriendsBean.getFavPlayer());
//                                obj.putExtra("profilePic", myFriendsBean.getPicUrl());
//
//                                obj.putExtra("favPlayerPic", myFriendsBean.getFavPlayerPicture());
//                                obj.putExtra("favTeamPic", myFriendsBean.getFavTeamPicture());
//                                obj.putExtra("nationality", myFriendsBean.getNationality());
//                                obj.putExtra("school", myFriendsBean.getSchool());
//                                obj.putExtra("footballBoot", myFriendsBean.getFavFootbalBoot());
//                                obj.putExtra("favFood", myFriendsBean.getFavFood());
//                                context.startActivity(obj);
//                            }

                        }else{
                            Intent obj = new Intent(context, ChildMyFriendProfileScreen.class);
                            obj.putExtra("friendId", myFriendsBean.getFriendId());
                            obj.putExtra("fullName", myFriendsBean.getFullName());
                            obj.putExtra("dob", myFriendsBean.getDobFormatted());
                            obj.putExtra("gender", strGender);
                            obj.putExtra("height", myFriendsBean.getHeight());
                            obj.putExtra("weight", myFriendsBean.getWeight());
                            obj.putExtra("favTeam", myFriendsBean.getFavTeam());
                            obj.putExtra("favPos", myFriendsBean.getFavPosition());
                            obj.putExtra("favPlayer", myFriendsBean.getFavPlayer());
                            obj.putExtra("profilePic", myFriendsBean.getPicUrl());

                            obj.putExtra("favPlayerPic", myFriendsBean.getFavPlayerPicture());
                            obj.putExtra("favTeamPic", myFriendsBean.getFavTeamPicture());
                            obj.putExtra("nationality", myFriendsBean.getNationality());
                            obj.putExtra("school", myFriendsBean.getSchool());
                            obj.putExtra("footballBoot", myFriendsBean.getFavFootbalBoot());
                            obj.putExtra("favFood", myFriendsBean.getFavFood());
                            context.startActivity(obj);
                        }

                    }else{
                        Toast.makeText(context, R.string.non_ifa_member, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    try{
                        // Changing following 0 to 1
//                        if(myFriendsBean.getIsPrivate().equalsIgnoreCase("0")) {
                        if(myFriendsBean.getIsPrivate().equalsIgnoreCase("1")) {
                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                            Toast.makeText(context, "You cannot view this "+verbiage_singular+"'s profile as he has set to private.", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(context, "You can not view this child's profile as he has set to private.", Toast.LENGTH_SHORT).show();

//                            if (myFriendsBean.getIsPrivateShow().equalsIgnoreCase("1")) {
//
//                            } else {
//                                Intent obj = new Intent(context, ChildMyFriendProfileScreen.class);
//                                obj.putExtra("friendId", myFriendsBean.getFriendId());
//                                obj.putExtra("fullName", myFriendsBean.getFullName());
//                                obj.putExtra("dob", myFriendsBean.getDobFormatted());
//                                obj.putExtra("gender", strGender);
//                                obj.putExtra("height", myFriendsBean.getHeight());
//                                obj.putExtra("weight", myFriendsBean.getWeight());
//                                obj.putExtra("favTeam", myFriendsBean.getFavTeam());
//                                obj.putExtra("favPos", myFriendsBean.getFavPosition());
//                                obj.putExtra("favPlayer", myFriendsBean.getFavPlayer());
//                                obj.putExtra("profilePic", myFriendsBean.getPicUrl());
//
//                                obj.putExtra("favPlayerPic", myFriendsBean.getFavPlayerPicture());
//                                obj.putExtra("favTeamPic", myFriendsBean.getFavTeamPicture());
//                                obj.putExtra("nationality", myFriendsBean.getNationality());
//                                obj.putExtra("school", myFriendsBean.getSchool());
//                                obj.putExtra("footballBoot", myFriendsBean.getFavFootbalBoot());
//                                obj.putExtra("favFood", myFriendsBean.getFavFood());
//                                context.startActivity(obj);
//                            }
                        }else{
                            Intent obj = new Intent(context, ChildMyFriendProfileScreen.class);
                            obj.putExtra("friendId", myFriendsBean.getFriendId());
                            obj.putExtra("fullName", myFriendsBean.getFullName());
                            obj.putExtra("dob", myFriendsBean.getDobFormatted());
                            obj.putExtra("gender", strGender);
                            obj.putExtra("height", myFriendsBean.getHeight());
                            obj.putExtra("weight", myFriendsBean.getWeight());
                            obj.putExtra("favTeam", myFriendsBean.getFavTeam());
                            obj.putExtra("favPos", myFriendsBean.getFavPosition());
                            obj.putExtra("favPlayer", myFriendsBean.getFavPlayer());
                            obj.putExtra("profilePic", myFriendsBean.getPicUrl());

                            obj.putExtra("favPlayerPic", myFriendsBean.getFavPlayerPicture());
                            obj.putExtra("favTeamPic", myFriendsBean.getFavTeamPicture());
                            obj.putExtra("nationality", myFriendsBean.getNationality());
                            obj.putExtra("school", myFriendsBean.getSchool());
                            obj.putExtra("footballBoot", myFriendsBean.getFavFootbalBoot());
                            obj.putExtra("favFood", myFriendsBean.getFavFood());
                            context.startActivity(obj);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }




            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(myFriendsBean.getParentApproval().equalsIgnoreCase("1")){
                    int approvalAge = Integer.parseInt(myFriendsBean.getApprovalAge());
                    int childAge = Integer.parseInt(loggedInUser.getAge());

                    if(childAge <= approvalAge){
                        dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.child_dialog_approve_post_password);
                        dialog.setTitle(R.string.ifa_dialog);

                        // set the custom dialog components - text, image and button
                        final TextView label = (TextView) dialog.findViewById(R.id.label);
                        final EditText parentPassword = (EditText) dialog.findViewById(R.id.parentPassword);
                        Button submit = (Button) dialog.findViewById(R.id.submit);

                        label.setTypeface(helvetica);
                        parentPassword.setTypeface(helvetica);
                        submit.setTypeface(linoType);

                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String parentPasswordStr = parentPassword.getText().toString();
                                if(parentPasswordStr==null || parentPasswordStr.isEmpty()){
                                    Toast.makeText(context, "Please enter Parent Password", Toast.LENGTH_LONG).show();
                                }else{

                                    if(Utilities.isNetworkAvailable(context)) {
                                        positionValue = position;
                                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                                        nameValuePairList.add(new BasicNameValuePair("friend_id",  myFriendsBean.getFriendId()));
                                        nameValuePairList.add(new BasicNameValuePair("state", "-1"));
                                        nameValuePairList.add(new BasicNameValuePair("password", parentPasswordStr));
                                        String webServiceUrl = Utilities.BASE_URL + "user_posts/approve_friend_rqst_by_parent_password_without_feed";
                                        ArrayList<String> headers = new ArrayList<>();
                                        headers.add("X-access-uid:"+loggedInUser.getId());
                                        headers.add("X-access-token:" + loggedInUser.getToken());
                                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, CANCEL_OR_ACCEPT_BY_APPROVAL, ChildMyFriendRequestsAdapter.this, headers);
                                        postWebServiceAsync.execute(webServiceUrl);
                                    }else{
                                        Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

                        dialog.show();
                    }else{
                        if(Utilities.isNetworkAvailable(context)) {
                            positionValue = position;

                            List<NameValuePair> nameValuePairList = new ArrayList<>();
                            nameValuePairList.add(new BasicNameValuePair("friend_id",  myFriendsBean.getFriendId()));
                            nameValuePairList.add(new BasicNameValuePair("state", "-1"));

                            String webServiceUrl = Utilities.BASE_URL + "children/change_friend_status";

                            ArrayList<String> headers = new ArrayList<>();
                            headers.add("X-access-uid:"+loggedInUser.getId());
                            headers.add("X-access-token:" + loggedInUser.getToken());

                            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, CANCEL_OR_ACCEPT, ChildMyFriendRequestsAdapter.this, headers);
                            postWebServiceAsync.execute(webServiceUrl);
                        } else {
                            Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    positionValue = position;

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("friend_id",  myFriendsBean.getFriendId()));
                    nameValuePairList.add(new BasicNameValuePair("state", "-1"));

                    String webServiceUrl = Utilities.BASE_URL + "children/change_friend_status";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, CANCEL_OR_ACCEPT, ChildMyFriendRequestsAdapter.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                }

            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myFriendsBean.getParentApproval().equalsIgnoreCase("1")){
                    int approvalAge = Integer.parseInt(myFriendsBean.getApprovalAge());
                    int childAge = Integer.parseInt(loggedInUser.getAge());

                    if(childAge <= approvalAge){
                        dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.child_dialog_approve_post_password);
                        dialog.setTitle(R.string.ifa_dialog);

                        // set the custom dialog components - text, image and button
                        final TextView label = (TextView) dialog.findViewById(R.id.label);
                        final EditText parentPassword = (EditText) dialog.findViewById(R.id.parentPassword);
                        Button submit = (Button) dialog.findViewById(R.id.submit);

                        label.setTypeface(helvetica);
                        parentPassword.setTypeface(helvetica);
                        submit.setTypeface(linoType);

                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String parentPasswordStr = parentPassword.getText().toString();
                                if(parentPasswordStr==null || parentPasswordStr.isEmpty()){
                                    Toast.makeText(context, "Please enter Parent Password", Toast.LENGTH_LONG).show();
                                }else{

                                    if(Utilities.isNetworkAvailable(context)) {
                                        positionValue = position;
                                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                                        nameValuePairList.add(new BasicNameValuePair("friend_id",  myFriendsBean.getFriendId()));
                                        nameValuePairList.add(new BasicNameValuePair("state", "1"));
                                        nameValuePairList.add(new BasicNameValuePair("password", parentPasswordStr));
                                        String webServiceUrl = Utilities.BASE_URL + "user_posts/approve_friend_rqst_by_parent_password_without_feed";
                                        ArrayList<String> headers = new ArrayList<>();
                                        headers.add("X-access-uid:"+loggedInUser.getId());
                                        headers.add("X-access-token:" + loggedInUser.getToken());
                                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, CANCEL_OR_ACCEPT_BY_APPROVAL, ChildMyFriendRequestsAdapter.this, headers);
                                        postWebServiceAsync.execute(webServiceUrl);
                                    }else{
                                        Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

                        dialog.show();
                    }else{
                        if(Utilities.isNetworkAvailable(context)) {
                            positionValue = position;

                            List<NameValuePair> nameValuePairList = new ArrayList<>();
                            nameValuePairList.add(new BasicNameValuePair("friend_id",  myFriendsBean.getFriendId()));
                            nameValuePairList.add(new BasicNameValuePair("state", "1"));

                            String webServiceUrl = Utilities.BASE_URL + "children/change_friend_status";

                            ArrayList<String> headers = new ArrayList<>();
                            headers.add("X-access-uid:"+loggedInUser.getId());
                            headers.add("X-access-token:" + loggedInUser.getToken());

                            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, CANCEL_OR_ACCEPT, ChildMyFriendRequestsAdapter.this, headers);
                            postWebServiceAsync.execute(webServiceUrl);
                            ((Activity) context).finish();
                        } else {
                            Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    if(Utilities.isNetworkAvailable(context)) {
                        positionValue = position;

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("friend_id",  myFriendsBean.getFriendId()));
                        nameValuePairList.add(new BasicNameValuePair("state", "1"));

                        String webServiceUrl = Utilities.BASE_URL + "children/change_friend_status";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:" + loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, CANCEL_OR_ACCEPT, ChildMyFriendRequestsAdapter.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);
                        ((Activity) context).finish();
                    } else {
                        Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        return convertView;
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case CANCEL_OR_ACCEPT:

                if(response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if(status){
                            myFriendsBeanArrayList.remove(positionValue);
                            notifyDataSetChanged();
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case CANCEL_OR_ACCEPT_BY_APPROVAL:
                if(response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if(status){
                            myFriendsBeanArrayList.remove(positionValue);
                            notifyDataSetChanged();
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        dialog.cancel();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}
