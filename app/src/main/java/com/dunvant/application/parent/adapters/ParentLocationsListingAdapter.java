package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CampLocationBean;

import java.util.ArrayList;

public class ParentLocationsListingAdapter extends BaseAdapter{

    Typeface helvetica;
    Typeface linoType;

    Context context;
    ArrayList<CampLocationBean> locationsList;
    LayoutInflater layoutInflater;

    public ParentLocationsListingAdapter(Context context, ArrayList<CampLocationBean> locationsList) {
        this.context = context;
        this.locationsList = locationsList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return locationsList.size();
    }

    @Override
    public Object getItem(int position) {
        return locationsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_location_item, null);

        TextView locationName = (TextView) convertView.findViewById(R.id.locationName);
        locationName.setTypeface(helvetica);

        CampLocationBean locationBean = locationsList.get(position);

        locationName.setText(locationBean.getLocationName());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int eightyPercent = (screenWidth * 80) / 100;
        float textWidthForTitle = locationName.getPaint().measureText(locationBean.getLocationName());
        int numberOfLines = ((int) textWidthForTitle / eightyPercent) + 1;
        locationName.setLines(numberOfLines);

        return convertView;
    }
}