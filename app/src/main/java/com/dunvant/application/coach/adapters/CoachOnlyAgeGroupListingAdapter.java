package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupBean;

import java.util.ArrayList;

public class CoachOnlyAgeGroupListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<AgeGroupBean> ageGroupListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public CoachOnlyAgeGroupListingAdapter(Context context, ArrayList<AgeGroupBean> ageGroupListing) {
        this.context = context;
        this.ageGroupListing = ageGroupListing;
        this.layoutInflater = LayoutInflater.from(context);
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return ageGroupListing.size();
    }

    @Override
    public Object getItem(int position) {
        return ageGroupListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_only_age_group_item, null);

        TextView ageGroupName = (TextView) convertView.findViewById(R.id.ageGroupName);

        ageGroupName.setText(ageGroupListing.get(position).getGroupName());
        ageGroupName.setTypeface(helvetica);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_only_age_group_dropdown_item, null);

        TextView ageGroupName = (TextView) convertView.findViewById(R.id.ageGroupName);

        ageGroupName.setText(ageGroupListing.get(position).getGroupName());
        ageGroupName.setTypeface(helvetica);

        return convertView;
    }
}