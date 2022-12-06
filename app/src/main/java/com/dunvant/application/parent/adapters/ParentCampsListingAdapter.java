package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.R;
import com.dunvant.application.beans.CampBean;
import com.dunvant.application.parent.ParentCampDetailScreen;
import com.dunvant.application.parent.ParentCampGalleryScreen;
import com.dunvant.application.parent.ParentViewImageInFullScreen;
import com.dunvant.application.utils.Utilities;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class ParentCampsListingAdapter extends BaseAdapter {

    Typeface helvetica;
    Typeface linoType;

    Context context;
    ArrayList<CampBean> campsListing;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public ParentCampsListingAdapter(Context context, ArrayList<CampBean> campsListing) {
        this.context = context;
        this.campsListing = campsListing;
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

        helvetica = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return campsListing.size();
    }

    @Override
    public Object getItem(int position) {
        return campsListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_camp_listing_item, null);

        ImageView campImage = (ImageView) convertView.findViewById(R.id.campImage);
        TextView picCount = (TextView) convertView.findViewById(R.id.picCount);
        TextView campName = (TextView) convertView.findViewById(R.id.campName);
        TextView lblLocations = (TextView) convertView.findViewById(R.id.lblLocations);
        ListView locationsListView = (ListView) convertView.findViewById(R.id.locationsListView);

        picCount.setTypeface(linoType);
        campName.setTypeface(helvetica);
        lblLocations.setTypeface(linoType);

        final CampBean campBean = campsListing.get(position);

        /*if (campBean.getGalleryList() != null && campBean.getGalleryList().size() > 0) {
            imageLoader.displayImage(campBean.getGalleryList().get(0).getFilePath(), campImage, options);
        } else {
            imageLoader.displayImage(campBean.getFilePath(), campImage, options);
        }*/

        imageLoader.displayImage(campBean.getFilePath(), campImage, options);

        picCount.setText(campBean.getGalleryList().size() + " PHOTOS");
        campName.setText(campBean.getCampName());

        ParentLocationsListingAdapter parentLocationsListingAdapter = new ParentLocationsListingAdapter(context, campBean.getLocationList());
        locationsListView.setAdapter(parentLocationsListingAdapter);
        Utilities.setListViewHeightBasedOnChildren(locationsListView);

        locationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent campDetailScreen = new Intent(context, ParentCampDetailScreen.class);
                campDetailScreen.putExtra("clickedOnCamp", campBean);
                campDetailScreen.putExtra("locationPosition", position);
                context.startActivity(campDetailScreen);
            }
        });

        campImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (campBean.getGalleryList().isEmpty()) {
                    Toast.makeText(context, "No Photo Found", Toast.LENGTH_SHORT).show();
                } else {
                    Intent galleryScreen = new Intent(context, ParentCampGalleryScreen.class);
                    galleryScreen.putExtra("clickedOnCamp", campBean);
                    context.startActivity(galleryScreen);
                }*/

                Intent viewImageInFullScreen = new Intent(context, ParentViewImageInFullScreen.class);
                viewImageInFullScreen.putExtra("imageUrl", campBean.getFilePath());
                context.startActivity(viewImageInFullScreen);
            }
        });

        picCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (campBean.getGalleryList().isEmpty()) {
                    Toast.makeText(context, "No Photo Found", Toast.LENGTH_SHORT).show();
                } else {
                    Intent galleryScreen = new Intent(context, ParentCampGalleryScreen.class);
                    galleryScreen.putExtra("clickedOnCamp", campBean);
                    context.startActivity(galleryScreen);
                }
            }
        });

        return convertView;
    }
}