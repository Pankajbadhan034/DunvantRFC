package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.FacilityBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class ParentFacilityListingAdapter extends BaseAdapter{

    Typeface helvetica;
    Typeface linoType;

    Context context;
    ArrayList<FacilityBean> facilityListing;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public ParentFacilityListingAdapter(Context context, ArrayList<FacilityBean> facilityListing) {
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
        convertView = layoutInflater.inflate(R.layout.parent_adapter_facility_item, null);

        RelativeLayout mainRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mainRelativeLayout);
        ImageView facilityImage = (ImageView) convertView.findViewById(R.id.facilityImage);
        TextView facilityName = (TextView) convertView.findViewById(R.id.facilityName);
        TextView facilityLocation = (TextView) convertView.findViewById(R.id.facilityLocation);

        FacilityBean currentFacility = facilityListing.get(position);

        imageLoader.displayImage(currentFacility.getFilePath(), facilityImage, options);
        facilityName.setText(currentFacility.getLocationName());

        if(currentFacility.getLocationDescription() == null || currentFacility.getLocationDescription().isEmpty() || currentFacility.getLocationDescription().equalsIgnoreCase("null")){
            facilityLocation.setText("");
        } else if(currentFacility.getLocationDescription().length() >= 125){
            facilityLocation.setText(currentFacility.getLocationDescription().substring(0, 125)+" ...more");
        } else {
            facilityLocation.setText(currentFacility.getLocationDescription()/*+" ...more"*/);
        }

        facilityName.setTypeface(linoType);
        facilityLocation.setTypeface(helvetica);

        if(position % 2 == 0){
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            facilityName.setTextColor(context.getResources().getColor(R.color.white));
            facilityLocation.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            facilityName.setTextColor(context.getResources().getColor(R.color.black));
            facilityLocation.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }
}