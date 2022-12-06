package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.ParentMidWeekListingBean;

import java.util.ArrayList;

public class ParentMidWeekListingAdapter extends BaseAdapter {

    Typeface helvetica;
    Typeface linoType;

    Context context;
    ArrayList<ParentMidWeekListingBean> facilityListing;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public ParentMidWeekListingAdapter(Context context, ArrayList<ParentMidWeekListingBean> facilityListing) {
        this.context = context;
        this.facilityListing = facilityListing;
        layoutInflater = LayoutInflater.from(context);

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .displayer(new RoundedBitmapDisplayer(1000))
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
        convertView = layoutInflater.inflate(R.layout.parent_adapter_midweek_listing_item, null);

        RelativeLayout mainRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mainRelativeLayout);
        ImageView image = convertView.findViewById(R.id.image);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView location = (TextView) convertView.findViewById(R.id.location);
        TextView description = (TextView) convertView.findViewById(R.id.description);


        ParentMidWeekListingBean currentFacility = facilityListing.get(position);

        title.setText(currentFacility.getTitle());
        location.setText(currentFacility.getName());
        description.setText(Html.fromHtml(currentFacility.getDescription()));
        imageLoader.displayImage(currentFacility.getFileName(), image, options);

        title.setTypeface(linoType);
        location.setTypeface(helvetica);
        description.setTypeface(helvetica);

        if(position % 2 == 0){
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            title.setTextColor(context.getResources().getColor(R.color.white));
            location.setTextColor(context.getResources().getColor(R.color.white));
            description.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            title.setTextColor(context.getResources().getColor(R.color.black));
            location.setTextColor(context.getResources().getColor(R.color.black));
            description.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }
}