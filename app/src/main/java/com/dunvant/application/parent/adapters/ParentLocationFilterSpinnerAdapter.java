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

public class ParentLocationFilterSpinnerAdapter extends BaseAdapter{

    Context context;
    ArrayList<AcademyLocationBean> academyLocationBean;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentLocationFilterSpinnerAdapter(Context context, ArrayList<AcademyLocationBean> academyLocationBean) {
        this.context = context;
        this.academyLocationBean = academyLocationBean;
        this.layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return academyLocationBean.size();
    }

    @Override
    public Object getItem(int position) {
        return academyLocationBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_location_filter_spinner_item, null);

        TextView locationName = (TextView) convertView.findViewById(R.id.locationName);

        locationName.setText(academyLocationBean.get(position).getLocationName());

        locationName.setTypeface(helvetica);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_location_filter_spinner_dropdown_item, null);

        TextView locationName = (TextView) convertView.findViewById(R.id.locationName);

        locationName.setText(academyLocationBean.get(position).getLocationName());

        locationName.setTypeface(helvetica);

        return convertView;
    }
}