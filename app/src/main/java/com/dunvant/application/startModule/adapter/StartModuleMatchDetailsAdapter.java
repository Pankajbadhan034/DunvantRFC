package com.dunvant.application.startModule.adapter;

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
import com.dunvant.application.beans.StartModuleTeamMatchBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class StartModuleMatchDetailsAdapter extends BaseAdapter {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Context context;
    ArrayList<StartModuleTeamMatchBean> coachLeagueDetailOneFixtureBeanArrayList;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions roundOptions;

    public StartModuleMatchDetailsAdapter(Context context, ArrayList<StartModuleTeamMatchBean> coachLeagueDetailOneFixtureBeanArrayList) {
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
        convertView = layoutInflater.inflate(R.layout.start_module_match_details_adapter, null);

        ImageView team1Image = convertView.findViewById(R.id.team1Image);
        TextView team1Text = (TextView) convertView.findViewById(R.id.team1Text);
        ImageView team2Image = convertView.findViewById(R.id.team2Image);
        TextView team2Text = (TextView) convertView.findViewById(R.id.team2Text);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView score = (TextView) convertView.findViewById(R.id.score);

        final StartModuleTeamMatchBean coachLeagueDetailOneFixtureBean = coachLeagueDetailOneFixtureBeanArrayList.get(position);

        team1Text.setText(coachLeagueDetailOneFixtureBean.getTeam1());
        imageLoader.displayImage(coachLeagueDetailOneFixtureBean.getImageUrl()+""+coachLeagueDetailOneFixtureBean.getTeam1Logo(), team1Image, roundOptions);

        team2Text.setText(coachLeagueDetailOneFixtureBean.getTeam2());
        imageLoader.displayImage(coachLeagueDetailOneFixtureBean.getImageUrl()+""+coachLeagueDetailOneFixtureBean.getTeam2Logo(), team2Image, roundOptions);


        if(coachLeagueDetailOneFixtureBean.getMatchTime().equalsIgnoreCase("")){
            date.setText(coachLeagueDetailOneFixtureBean.getMatchDate());
        }else{
            date.setText(coachLeagueDetailOneFixtureBean.getMatchDate()+" | "+coachLeagueDetailOneFixtureBean.getMatchTime());
        }

        time.setText(coachLeagueDetailOneFixtureBean.getTeam1()+" vs "+coachLeagueDetailOneFixtureBean.getTeam2());

        if(coachLeagueDetailOneFixtureBean.getMatchVanue().equalsIgnoreCase("")){
            score.setText(coachLeagueDetailOneFixtureBean.getMatchType());
        }else{
            score.setText(coachLeagueDetailOneFixtureBean.getMatchType()+" | "+coachLeagueDetailOneFixtureBean.getMatchVanue());
        }


       // score.setText(coachLeagueDetailOneFixtureBean.getTeam1Score()+" - "+coachLeagueDetailOneFixtureBean.getTeam2Score());

        return convertView;
    }


}