package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachLeagueDetailOneTopPlayersBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class CoachLeagueDetailOnePlayersGalleryAdapter extends BaseAdapter {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Context context;
    ArrayList<CoachLeagueDetailOneTopPlayersBean> coachLeagueDetailOneFixtureBeanArrayList;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions roundOptions;

    public CoachLeagueDetailOnePlayersGalleryAdapter(Context context, ArrayList<CoachLeagueDetailOneTopPlayersBean> coachLeagueDetailOneFixtureBeanArrayList) {
        this.context = context;
        this.coachLeagueDetailOneFixtureBeanArrayList = coachLeagueDetailOneFixtureBeanArrayList;
        this.layoutInflater = LayoutInflater.from(context);

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        roundOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public int getCount() {
        return coachLeagueDetailOneFixtureBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return coachLeagueDetailOneFixtureBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_gallery_items, null);

        ImageView image = convertView.findViewById(R.id.image);
        TextView name =  convertView.findViewById(R.id.name);


        final CoachLeagueDetailOneTopPlayersBean coachLeagueDetailOneFixtureBean = coachLeagueDetailOneFixtureBeanArrayList.get(position);

        name.setText(coachLeagueDetailOneFixtureBean.getName());
        imageLoader.displayImage(coachLeagueDetailOneFixtureBean.getImageUrl()+""+coachLeagueDetailOneFixtureBean.getImage(), image, roundOptions);


        return convertView;
    }


}