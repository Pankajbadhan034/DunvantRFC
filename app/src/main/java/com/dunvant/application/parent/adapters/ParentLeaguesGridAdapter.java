package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.LeagueBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class ParentLeaguesGridAdapter extends BaseAdapter{

    Typeface helvetica;
    Typeface linoType;

    Context context;
    ArrayList<LeagueBean> leaguesListing;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public ParentLeaguesGridAdapter(Context context, ArrayList<LeagueBean> leaguesListing) {
        this.context = context;
        this.leaguesListing = leaguesListing;
        layoutInflater = LayoutInflater.from(context);

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return leaguesListing.size();
    }

    @Override
    public Object getItem(int position) {
        return leaguesListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_league_item, null);

        LinearLayout mainLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
        ImageView leagueImage = (ImageView) convertView.findViewById(R.id.leagueImage);
        TextView leagueName = (TextView) convertView.findViewById(R.id.leagueName);
        leagueName.setTypeface(linoType);

        LeagueBean currentLeague = leaguesListing.get(position);

        leagueName.setText(currentLeague.getLeagueName());
        imageLoader.displayImage(currentLeague.getFilePath(), leagueImage, options);

        if(position % 2 == 0){
            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            leagueName.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            leagueName.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }
}