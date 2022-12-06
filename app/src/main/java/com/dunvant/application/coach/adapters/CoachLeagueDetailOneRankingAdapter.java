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
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachLeagueDetailOneTopPlayersBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class CoachLeagueDetailOneRankingAdapter extends BaseAdapter {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Context context;
    ArrayList<CoachLeagueDetailOneTopPlayersBean> coachLeagueDetailOneFixtureBeanArrayList;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions roundOptions;

    public CoachLeagueDetailOneRankingAdapter(Context context, ArrayList<CoachLeagueDetailOneTopPlayersBean> coachLeagueDetailOneFixtureBeanArrayList) {
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
                .displayer(new RoundedBitmapDisplayer(1000))
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
        convertView = layoutInflater.inflate(R.layout.coach_adapter_top_player_items, null);

        TextView rank = (TextView) convertView.findViewById(R.id.rank);
        ImageView image = convertView.findViewById(R.id.image);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView pos = (TextView) convertView.findViewById(R.id.pos);
        TextView goals = (TextView) convertView.findViewById(R.id.goals);


        final CoachLeagueDetailOneTopPlayersBean coachLeagueDetailOneFixtureBean = coachLeagueDetailOneFixtureBeanArrayList.get(position);

        final int rankInt = position+1;
        rank.setText(""+rankInt);
        name.setText(coachLeagueDetailOneFixtureBean.getName());
        pos.setText(coachLeagueDetailOneFixtureBean.getSquadNumber());
        goals.setText(coachLeagueDetailOneFixtureBean.getGoals());
        imageLoader.displayImage(coachLeagueDetailOneFixtureBean.getImageUrl()+""+coachLeagueDetailOneFixtureBean.getImage(), image, roundOptions);


        return convertView;
    }


}