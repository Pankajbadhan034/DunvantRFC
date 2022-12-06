package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CampLocationBean;

import java.util.ArrayList;

public class CoachLocationListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<CampLocationBean> locationsList;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public CoachLocationListingAdapter(Context context, ArrayList<CampLocationBean> locationsList) {
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
        convertView = layoutInflater.inflate(R.layout.coach_adapter_location_item, null);

        TextView locationName = (TextView) convertView.findViewById(R.id.locationName);

        CampLocationBean locationBean = locationsList.get(position);

        locationName.setText(locationBean.getLocationName());
        locationName.setTypeface(helvetica);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_location_dropdown_item, null);

        TextView locationName = (TextView) convertView.findViewById(R.id.locationName);

        CampLocationBean locationBean = locationsList.get(position);

        locationName.setText(locationBean.getLocationName());
        locationName.setTypeface(helvetica);

        return convertView;
    }
}