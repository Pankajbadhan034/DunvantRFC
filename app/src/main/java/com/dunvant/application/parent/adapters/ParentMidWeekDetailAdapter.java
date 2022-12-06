package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.ParentMidWeekDetailBean;
import com.dunvant.application.parent.ParentBookMidWeekChildrenListingScreen;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentMidWeekDetailAdapter  extends BaseAdapter {

    Typeface helvetica;
    Typeface linoType;

    Context context;
    ArrayList<ParentMidWeekDetailBean> facilityListing;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    String titleStr;
    String nameStr;

    public ParentMidWeekDetailAdapter(Context context, ArrayList<ParentMidWeekDetailBean> facilityListing, String titleStr, String nameStr) {
        this.context = context;
        this.facilityListing = facilityListing;
        this.titleStr = titleStr;
        this.nameStr = nameStr;
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
        convertView = layoutInflater.inflate(R.layout.parent_adapter_midweek_detail_item, null);

        RelativeLayout mainRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mainRelativeLayout);
        final TextView title =  convertView.findViewById(R.id.title);
        Button bookNow =  convertView.findViewById(R.id.bookNow);


        final ParentMidWeekDetailBean currentFacility = facilityListing.get(position);

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);


        title.setText(currentFacility.getSessionCount()+" SESSION : "+currentFacility.getCost()+" "+academy_currency);

        title.setTypeface(linoType);
        bookNow.setTypeface(helvetica);


        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentFacility.getIsLocked().equalsIgnoreCase("1")){
                    Toast.makeText(context, "Booking Closed for this package", Toast.LENGTH_SHORT).show();
                } else {
                    Intent bookAcademyFour = new Intent(context, ParentBookMidWeekChildrenListingScreen.class);
                    bookAcademyFour.putExtra("clickedOnSession", currentFacility);
                    bookAcademyFour.putExtra("title", titleStr);
                    bookAcademyFour.putExtra("name", nameStr);
                    context.startActivity(bookAcademyFour);
                }
            }
        });

        return convertView;
    }
}