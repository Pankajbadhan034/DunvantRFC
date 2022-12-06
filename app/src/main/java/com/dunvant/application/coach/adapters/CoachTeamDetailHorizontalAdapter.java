package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.TeamDetailStatsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class CoachTeamDetailHorizontalAdapter extends BaseAdapter {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Context context;
    ArrayList<TeamDetailStatsBean> teamDetailStatsBeanArrayList;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions roundOptions;

    public CoachTeamDetailHorizontalAdapter(Context context, ArrayList<TeamDetailStatsBean> teamDetailStatsBeanArrayList) {
        this.context = context;
        this.teamDetailStatsBeanArrayList = teamDetailStatsBeanArrayList;
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
        return teamDetailStatsBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return teamDetailStatsBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_team_detail_horizontal_item, null);

        TextView headingTV = (TextView) convertView.findViewById(R.id.headingTV);
        TextView valueTV = (TextView) convertView.findViewById(R.id.valueTV);

        final TeamDetailStatsBean teamDetailStatsBean = teamDetailStatsBeanArrayList.get(position);

        headingTV.setText(teamDetailStatsBean.getHeading());
        valueTV.setText(teamDetailStatsBean.getValue());

        return convertView;
    }


}