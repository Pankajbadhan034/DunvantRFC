package com.dunvant.application.child.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.R;
import com.dunvant.application.beans.MyFriendsBean;
import com.dunvant.application.child.ChildMyFriendProfileScreen;
import com.dunvant.application.utils.Utilities;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

public class ChildViewYourProfileFriendListAdapter extends BaseAdapter {
    Context context;
    ArrayList<MyFriendsBean> myFriendsBeanArrayList;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    SharedPreferences sharedPreferences;

    Typeface helvetica;
    Typeface linoType;

    public ChildViewYourProfileFriendListAdapter(Context context, ArrayList<MyFriendsBean> myFriendsBeanArrayList){
        this.context = context;
        this.myFriendsBeanArrayList = myFriendsBeanArrayList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_my_friend_request_item, null);

        ImageView picUrl = (ImageView) convertView.findViewById(R.id.picUrl);
        TextView fullName = (TextView) convertView.findViewById(R.id.fullName);
        TextView createdDate = (TextView) convertView.findViewById(R.id.createdDate);
        ImageView cancel = (ImageView) convertView.findViewById(R.id.cancel);
        ImageView accept = (ImageView) convertView.findViewById(R.id.accept);
        LinearLayout profileView = (LinearLayout) convertView.findViewById(R.id.profileView);



        fullName.setTypeface(helvetica);
        createdDate.setTypeface(helvetica);

        cancel.setVisibility(View.GONE);
        accept.setVisibility(View.GONE);

        final MyFriendsBean galleryBean = myFriendsBeanArrayList.get(position);

        fullName.setText(galleryBean.getFullName());
        createdDate.setText(galleryBean.getCreatedAt());
        imageLoader.displayImage(galleryBean.getPicUrl(), picUrl, options);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int eightyPercent = (screenWidth * 80) / 100;
        float textWidthForTitle = createdDate.getPaint().measureText(myFriendsBeanArrayList.get(position).getCreatedAt());
        int numberOfLines = ((int) textWidthForTitle / eightyPercent) + 1;
        createdDate.setLines(numberOfLines);

        profileView.setBackgroundColor(Color.parseColor("#ffffff"));
        fullName.setTextColor(Color.parseColor("#000000"));
        createdDate.setTextColor(Color.parseColor("#000000"));


        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(galleryBean.getIsPrivate().equalsIgnoreCase("1")) {


                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                    Toast.makeText(context, "You cannot view this "+verbiage_singular.toLowerCase()+"'s profile as he has set to private.", Toast.LENGTH_SHORT).show();

                }else{
                    Intent obj = new Intent(context, ChildMyFriendProfileScreen.class);
                    obj.putExtra("friendId", galleryBean.getFriendId());
                    obj.putExtra("fullName", galleryBean.getFullName());
                    obj.putExtra("favTeam", galleryBean.getFavTeam());
                    obj.putExtra("favPos", galleryBean.getFavPosition());
                    obj.putExtra("favPlayer", galleryBean.getFavPlayer());
                    obj.putExtra("gender", galleryBean.getGenderFormatted());
                    obj.putExtra("dob", galleryBean.getDobFormatted());
                    obj.putExtra("height", galleryBean.getHeight());
                    obj.putExtra("weight", galleryBean.getWeight());
                    obj.putExtra("profilePic", galleryBean.getPicUrl());

                    obj.putExtra("favPlayerPic", galleryBean.getFavPlayerPicture());
                    obj.putExtra("favTeamPic", galleryBean.getFavTeamPicture());
                    obj.putExtra("nationality", galleryBean.getNationality());
                    obj.putExtra("school", galleryBean.getSchool());
                    obj.putExtra("footballBoot", galleryBean.getFavFootbalBoot());
                    obj.putExtra("favFood", galleryBean.getFavFood());
                    context.startActivity(obj);
                }
            }
        });

        return convertView;
    }
}
