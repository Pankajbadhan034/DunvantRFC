package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class CoachStandingLeagueHorizontalListAdapter extends BaseAdapter {
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Context context;
    ArrayList<String> coloumnArrayList;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public CoachStandingLeagueHorizontalListAdapter(Context context, ArrayList<String> coloumnArrayList){
        this.context = context;
        this.coloumnArrayList = coloumnArrayList;
        layoutInflater = LayoutInflater.from(context);


        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(1000))
                .build();

    }

    @Override
    public int getCount() {
        return coloumnArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return coloumnArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_standing_hor_list_item, null);

        final TextView categoryname = (TextView) convertView.findViewById(R.id.categoryname);
        final ImageView image = convertView.findViewById(R.id.image);
        categoryname.setTypeface(helvetica);

        String value = coloumnArrayList.get(position);

        if(value.contains("http")){
            categoryname.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            imageLoader.displayImage(value, image, options);
        }else{
            categoryname.setText(value);
            categoryname.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
        }



        return convertView;
    }
}