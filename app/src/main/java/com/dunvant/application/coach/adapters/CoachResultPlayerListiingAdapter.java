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

import com.dunvant.application.HorizontalListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.TeamDetailBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class CoachResultPlayerListiingAdapter extends BaseAdapter {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Context context;
    ArrayList<TeamDetailBean> coachLeagueDetailOneFixtureBeanArrayList;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions roundOptions;

    public CoachResultPlayerListiingAdapter(Context context, ArrayList<TeamDetailBean> coachLeagueDetailOneFixtureBeanArrayList) {
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
        convertView = layoutInflater.inflate(R.layout.coach_adapter_match_player_listing_item, null);

        ImageView image = convertView.findViewById(R.id.image);
        TextView rank = (TextView) convertView.findViewById(R.id.rank);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView positionPlayer = (TextView) convertView.findViewById(R.id.positionPlayer);
        HorizontalListView horListView = convertView.findViewById(R.id.horListView);

        final TeamDetailBean teamDetailBean = coachLeagueDetailOneFixtureBeanArrayList.get(position);

        int rankValue = position+1;
        rank.setText(""+rankValue);
        imageLoader.displayImage(teamDetailBean.getImageUrl()+""+teamDetailBean.getImage(), image, roundOptions);
        name.setText(teamDetailBean.getName());
        positionPlayer.setText("no Data");

        System.out.println("arraySize::"+teamDetailBean.getTeamStatsBeanArrayList().size());

        CoachResultPlayerTTFlistAdapter coachMatchResultDetailTeamStatsAdapter = new CoachResultPlayerTTFlistAdapter(context, teamDetailBean.getTeamStatsBeanArrayList());
        horListView.setAdapter(coachMatchResultDetailTeamStatsAdapter);



        return convertView;
    }


}
