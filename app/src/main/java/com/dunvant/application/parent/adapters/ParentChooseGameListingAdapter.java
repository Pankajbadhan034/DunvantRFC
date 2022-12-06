package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.FacilityBean;

import java.util.ArrayList;

public class ParentChooseGameListingAdapter extends BaseAdapter{

    Typeface helvetica;
    Typeface linoType;

    Context context;
    ArrayList<FacilityBean> facilityListing;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public ParentChooseGameListingAdapter(Context context, ArrayList<FacilityBean> facilityListing) {
        this.context = context;
        this.facilityListing = facilityListing;
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
        return facilityListing.size();
    }

    @Override
    public Object getItem(int position) {
        return facilityListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_choose_game_item, null);

        RelativeLayout mainRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mainRelativeLayout);
        TextView facilityName = (TextView) convertView.findViewById(R.id.facilityName);

        FacilityBean currentFacility = facilityListing.get(position);

        facilityName.setText(currentFacility.getLocationName());

        facilityName.setTypeface(linoType);

        if(position % 2 == 0){
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            facilityName.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            facilityName.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }
}