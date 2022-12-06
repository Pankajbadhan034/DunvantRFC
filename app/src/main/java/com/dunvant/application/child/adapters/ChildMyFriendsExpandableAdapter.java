package com.dunvant.application.child.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.MyFriendsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildMyFriendProfileScreen;
import com.dunvant.application.child.ChildMyFriendSendMessageScreen;
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

/**
 * Created by DEVLABS\pbadhan on 6/12/16.
 */
public class ChildMyFriendsExpandableAdapter extends BaseExpandableListAdapter implements IWebServiceCallback {

    int positionDelete;
    private final String BLOCK_USER = "BLOCK_USER";
    private final String REMOVE_USER = "REMOVE_USER";
    private final String UN_BLOCK_USER = "UN_BLOCK_USER";

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Context context;
    ArrayList<MyFriendsBean> myFriendsBean;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ChildMyFriendsExpandableAdapter(Context context,ArrayList<MyFriendsBean>myFriendsBean){
        this.context = context;
        this.myFriendsBean = myFriendsBean;
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

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");
    }
    @Override
    public int getGroupCount() {
        return myFriendsBean.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)   {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return myFriendsBean.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_expendable_adapter_my_friends_list_header, null);

        RelativeLayout relativeMain = (RelativeLayout) convertView.findViewById(R.id.relativeMain);
        TextView fullname = (TextView) convertView.findViewById(R.id.fullName);
        TextView createdDate = (TextView) convertView.findViewById(R.id.createdDate);
        ImageView indicatorImage = (ImageView) convertView.findViewById(R.id.indicatorImage);
        ImageView picUrl = (ImageView) convertView.findViewById(R.id.picUrl);

        fullname.setTypeface(helvetica);
        createdDate.setTypeface(helvetica);

        MyFriendsBean FriendsBean = myFriendsBean.get(groupPosition);

        imageLoader.displayImage(FriendsBean.getPicUrl(), picUrl, options);
        fullname.setText(FriendsBean.getFullName());
        createdDate.setText(FriendsBean.getCreatedAt());

        if (isExpanded) {
            indicatorImage.setImageResource(R.drawable.dropdown);
        } else {
            indicatorImage.setImageResource(R.drawable.arrow);
        }

        if(groupPosition % 2 == 0) {
            relativeMain.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            fullname.setTextColor(context.getResources().getColor(R.color.white));
            createdDate.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            relativeMain.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            fullname.setTextColor(context.getResources().getColor(R.color.black));
            createdDate.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_expendable_adapter_my_friends_list_child, null);
        ImageView sendMessage = (ImageView) convertView.findViewById(R.id.sendMessage);
        ImageView friendProfile = (ImageView) convertView.findViewById(R.id.friendProfile);
        ImageView block = (ImageView) convertView.findViewById(R.id.block);
        ImageView unBlock = (ImageView) convertView.findViewById(R.id.unBlock);
        ImageView remove = (ImageView) convertView.findViewById(R.id.remove);
        LinearLayout linear = (LinearLayout) convertView.findViewById(R.id.linear);

        final MyFriendsBean friendsBean = myFriendsBean.get(groupPosition);
        positionDelete = groupPosition;

        if(friendsBean.getFriendStatus().equalsIgnoreCase("1")){
            unBlock.setVisibility(View.GONE);
            block.setVisibility(View.VISIBLE);
        }else{
            unBlock.setVisibility(View.VISIBLE);
            block.setVisibility(View.GONE);
        }

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(context, ChildMyFriendSendMessageScreen.class);
                obj.putExtra("friendId", friendsBean.getFriendId());
                obj.putExtra("fullName", friendsBean.getFullName());
                context.startActivity(obj);
            }
        });

        friendProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(friendsBean.getIsPrivate().equalsIgnoreCase("1")) {
                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                    Toast.makeText(context, "You cannot view this "+verbiage_singular+"'s profile as he has set to private.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(context, "You can not view this child's profile as he has set to private.", Toast.LENGTH_SHORT).show();
//                    if(friendsBean.getIsPrivateShow().equalsIgnoreCase("0")){
//                        Toast.makeText(context, "You can not view this child's profile as he has set to private.", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Intent obj = new Intent(context, ChildMyFriendProfileScreen.class);
//                        obj.putExtra("friendId", friendsBean.getFriendId());
//                        obj.putExtra("fullName", friendsBean.getFullName());
//                        obj.putExtra("favTeam", friendsBean.getFavTeam());
//                        obj.putExtra("favPos", friendsBean.getFavPosition());
//                        obj.putExtra("favPlayer", friendsBean.getFavPlayer());
//                        obj.putExtra("gender", friendsBean.getGenderFormatted());
//                        obj.putExtra("dob", friendsBean.getDobFormatted());
//                        obj.putExtra("height", friendsBean.getHeight());
//                        obj.putExtra("weight", friendsBean.getWeight());
//                        obj.putExtra("profilePic", friendsBean.getPicUrl());
//
//                        obj.putExtra("favPlayerPic", friendsBean.getFavPlayerPicture());
//                        obj.putExtra("favTeamPic", friendsBean.getFavTeamPicture());
//                        obj.putExtra("nationality", friendsBean.getNationality());
//                        obj.putExtra("school", friendsBean.getSchool());
//                        obj.putExtra("footballBoot", friendsBean.getFavFootbalBoot());
//                        obj.putExtra("favFood", friendsBean.getFavFood());
//                        context.startActivity(obj);
//                    }
                } else {
                    Intent obj = new Intent(context, ChildMyFriendProfileScreen.class);
                    obj.putExtra("friendId", friendsBean.getFriendId());
                    obj.putExtra("fullName", friendsBean.getFullName());
                    obj.putExtra("favTeam", friendsBean.getFavTeam());
                    obj.putExtra("favPos", friendsBean.getFavPosition());
                    obj.putExtra("favPlayer", friendsBean.getFavPlayer());
                    obj.putExtra("gender", friendsBean.getGenderFormatted());
                    obj.putExtra("dob", friendsBean.getDobFormatted());
                    obj.putExtra("height", friendsBean.getHeight());
                    obj.putExtra("weight", friendsBean.getWeight());
                    obj.putExtra("profilePic", friendsBean.getPicUrl());

                    obj.putExtra("favPlayerPic", friendsBean.getFavPlayerPicture());
                    obj.putExtra("favTeamPic", friendsBean.getFavTeamPicture());
                    obj.putExtra("nationality", friendsBean.getNationality());
                    obj.putExtra("school", friendsBean.getSchool());
                    obj.putExtra("footballBoot", friendsBean.getFavFootbalBoot());
                    obj.putExtra("favFood", friendsBean.getFavFood());
                    context.startActivity(obj);
                }
                
                
            }
        });

        unBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure you want to unblock this person?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if(Utilities.isNetworkAvailable(context)) {

                                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                                    nameValuePairList.add(new BasicNameValuePair("friend_id", friendsBean.getFriendId()));
                                    nameValuePairList.add(new BasicNameValuePair("state", "3"));

                                    String webServiceUrl = Utilities.BASE_URL + "children/change_friend_status";

                                    ArrayList<String> headers = new ArrayList<>();
                                    headers.add("X-access-uid:"+loggedInUser.getId());
                                    headers.add("X-access-token:"+loggedInUser.getToken());

                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, UN_BLOCK_USER, ChildMyFriendsExpandableAdapter.this, headers);
                                    postWebServiceAsync.execute(webServiceUrl);

                                } else {
                                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();




            }
        });


        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure you want to block this person?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if(Utilities.isNetworkAvailable(context)) {

                                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                                    nameValuePairList.add(new BasicNameValuePair("friend_id", friendsBean.getFriendId()));
                                    nameValuePairList.add(new BasicNameValuePair("state", "2"));

                                    String webServiceUrl = Utilities.BASE_URL + "children/change_friend_status";

                                    ArrayList<String> headers = new ArrayList<>();
                                    headers.add("X-access-uid:"+loggedInUser.getId());
                                    headers.add("X-access-token:"+loggedInUser.getToken());

                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, BLOCK_USER, ChildMyFriendsExpandableAdapter.this, headers);
                                    postWebServiceAsync.execute(webServiceUrl);

                                } else {
                                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();



            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure, you want to delete your Friend?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if(Utilities.isNetworkAvailable(context)) {

                                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                                    nameValuePairList.add(new BasicNameValuePair("friend_id", friendsBean.getFriendId()));
                                    nameValuePairList.add(new BasicNameValuePair("state", "-1"));

                                    String webServiceUrl = Utilities.BASE_URL + "children/change_friend_status";

                                    ArrayList<String> headers = new ArrayList<>();
                                    headers.add("X-access-uid:"+loggedInUser.getId());
                                    headers.add("X-access-token:"+loggedInUser.getToken());

                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, REMOVE_USER, ChildMyFriendsExpandableAdapter.this, headers);
                                    postWebServiceAsync.execute(webServiceUrl);

                                } else {
                                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });

        if(groupPosition % 2 == 0) {
            linear.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
        } else {
            linear.setBackgroundColor(context.getResources().getColor(R.color.yellow));
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case BLOCK_USER:

                if(response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if(status){
                           // myFriendsBean.remove(positionDelete);
                            myFriendsBean.get(positionDelete).setFriendStatus("2");
                            notifyDataSetChanged();
                        }
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case UN_BLOCK_USER:
                if(response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if(status){
                            myFriendsBean.get(positionDelete).setFriendStatus("1");
                            notifyDataSetChanged();
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case REMOVE_USER:

                if(response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if(status){
                            myFriendsBean.remove(positionDelete);
                            notifyDataSetChanged();
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}
