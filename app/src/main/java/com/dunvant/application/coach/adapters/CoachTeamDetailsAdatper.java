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
import com.dunvant.application.beans.CoachTeamDetailsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class CoachTeamDetailsAdatper extends BaseAdapter {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Context context;
    ArrayList<CoachTeamDetailsBean> coachTeamDetailsBeanArrayList;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions roundOptions;

    public CoachTeamDetailsAdatper(Context context, ArrayList<CoachTeamDetailsBean> coachTeamDetailsBeanArrayList) {
        this.context = context;
        this.coachTeamDetailsBeanArrayList = coachTeamDetailsBeanArrayList;
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
        return coachTeamDetailsBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return coachTeamDetailsBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_team_detail_item, null);

        ImageView image = convertView.findViewById(R.id.image);
        TextView matchesTV = (TextView) convertView.findViewById(R.id.matchesTV);
        TextView nameTV = (TextView) convertView.findViewById(R.id.nameTV);
        TextView positionTV = (TextView) convertView.findViewById(R.id.positionTV);
        TextView goalsTV = convertView.findViewById(R.id.goalsTV);

        final CoachTeamDetailsBean coachTeamDetailsBean = coachTeamDetailsBeanArrayList.get(position);

        imageLoader.displayImage(coachTeamDetailsBean.getPlayer_image(), image, roundOptions);

        matchesTV.setText(coachTeamDetailsBean.getMatches());
        nameTV.setText(coachTeamDetailsBean.getPlayer_name());
        positionTV.setText(coachTeamDetailsBean.getPosition());
        goalsTV.setText(coachTeamDetailsBean.getGoals());

        return convertView;
    }


}