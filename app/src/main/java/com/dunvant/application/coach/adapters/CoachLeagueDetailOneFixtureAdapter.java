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
import com.dunvant.application.beans.CoachLeagueDetailOneFixtureBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class CoachLeagueDetailOneFixtureAdapter extends BaseAdapter {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Context context;
    ArrayList<CoachLeagueDetailOneFixtureBean> coachLeagueDetailOneFixtureBeanArrayList;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions roundOptions;

    public CoachLeagueDetailOneFixtureAdapter(Context context, ArrayList<CoachLeagueDetailOneFixtureBean> coachLeagueDetailOneFixtureBeanArrayList) {
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
        convertView = layoutInflater.inflate(R.layout.coach_adapter_fixture_item, null);

        ImageView team1Image = convertView.findViewById(R.id.team1Image);
        TextView team1Text = (TextView) convertView.findViewById(R.id.team1Text);
        ImageView team2Image = convertView.findViewById(R.id.team2Image);
        TextView team2Text = (TextView) convertView.findViewById(R.id.team2Text);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView vs = (TextView) convertView.findViewById(R.id.vs);

        final CoachLeagueDetailOneFixtureBean coachLeagueDetailOneFixtureBean = coachLeagueDetailOneFixtureBeanArrayList.get(position);

        team1Text.setText(coachLeagueDetailOneFixtureBean.getTeam1Name());
        imageLoader.displayImage(coachLeagueDetailOneFixtureBean.getImageUrl()+""+coachLeagueDetailOneFixtureBean.getTeam1Logo(), team1Image, roundOptions);

        team2Text.setText(coachLeagueDetailOneFixtureBean.getTeam2Name());
        imageLoader.displayImage(coachLeagueDetailOneFixtureBean.getImageUrl()+""+coachLeagueDetailOneFixtureBean.getTeam2Logo(), team2Image, roundOptions);

        date.setText(coachLeagueDetailOneFixtureBean.getMatchDate());
        time.setText(coachLeagueDetailOneFixtureBean.getMatchTime());


        return convertView;
    }


}