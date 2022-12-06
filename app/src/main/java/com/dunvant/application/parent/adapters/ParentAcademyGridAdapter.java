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
import com.dunvant.application.beans.CoachingAcademyBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class ParentAcademyGridAdapter extends BaseAdapter{

    Context context;
    ArrayList<CoachingAcademyBean> academyList;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Typeface helvetica;
    Typeface linoType;

    public ParentAcademyGridAdapter(Context context, ArrayList<CoachingAcademyBean> academyList) {
        this.context = context;
        this.academyList = academyList;
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
        return academyList.size();
    }

    @Override
    public Object getItem(int position) {
        return academyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_academy_item, null);

        LinearLayout mainLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
        ImageView academyImage = (ImageView) convertView.findViewById(R.id.academyImage);
        TextView academyName = (TextView) convertView.findViewById(R.id.academyName);
        TextView academyShortDescription = (TextView) convertView.findViewById(R.id.academyShortDescription);

        CoachingAcademyBean academyBean = academyList.get(position);

        imageLoader.displayImage(academyBean.getFilePath(), academyImage, options);
        academyName.setText(academyBean.getCoachingProgramName());

        if(academyBean.getDescription().length() >= 125){
            academyShortDescription.setText(academyBean.getDescription().substring(0, 125)+" ...more");
        } else {
            academyShortDescription.setText(academyBean.getDescription()/*+" ...more"*/);
        }

        if(position % 2 == 0) {
            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            academyName.setTextColor(context.getResources().getColor(R.color.white));
            academyShortDescription.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            academyName.setTextColor(context.getResources().getColor(R.color.black));
            academyShortDescription.setTextColor(context.getResources().getColor(R.color.black));
        }

        academyName.setTypeface(linoType);
        academyShortDescription.setTypeface(helvetica);

        return convertView;
    }
}