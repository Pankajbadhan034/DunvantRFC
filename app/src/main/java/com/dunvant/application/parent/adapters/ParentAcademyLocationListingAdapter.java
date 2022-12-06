package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.AcademyLocationBean;

import java.util.ArrayList;

public class ParentAcademyLocationListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<AcademyLocationBean> locationsListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentAcademyLocationListingAdapter(Context context, ArrayList<AcademyLocationBean> locationsListing) {
        this.context = context;
        this.locationsListing = locationsListing;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return locationsListing.size();
    }

    @Override
    public Object getItem(int position) {
        return locationsListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_academy_location_item, null);

        TextView locationName = (TextView) convertView.findViewById(R.id.locationName);
        TextView locationDescription = (TextView) convertView.findViewById(R.id.locationDescription);
        TextView coachingProgramName = (TextView) convertView.findViewById(R.id.coachingProgramName);


        AcademyLocationBean locationBean = locationsListing.get(position);
        locationName.setText(locationBean.getLocationName());
        locationDescription.setText(locationBean.getDescription());
        locationDescription.setVisibility(View.GONE);
//        coachingProgramName.setText("COACHING PROGRAMMES - "+locationBean.getCoachingProgramNames());
        coachingProgramName.setText(locationBean.getCoachingProgramNames());

        locationName.setTypeface(linoType);
        locationDescription.setTypeface(helvetica);

        return convertView;
    }
}