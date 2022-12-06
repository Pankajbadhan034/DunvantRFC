package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupAttendanceBean;

import java.util.ArrayList;

public class ParentAgeGroupSpinnerAdapter extends BaseAdapter{

    Context context;
    ArrayList<AgeGroupAttendanceBean> ageGroupAttendanceListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentAgeGroupSpinnerAdapter(Context context, ArrayList<AgeGroupAttendanceBean> ageGroupAttendanceListing) {
        this.context = context;
        this.ageGroupAttendanceListing = ageGroupAttendanceListing;
        this.layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return ageGroupAttendanceListing.size();
    }

    @Override
    public Object getItem(int position) {
        return ageGroupAttendanceListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_age_group_item, null);

        TextView ageGroupName = (TextView) convertView.findViewById(R.id.ageGroupName);

        ageGroupName.setText(ageGroupAttendanceListing.get(position).getGroupName());
        ageGroupName.setTypeface(helvetica);

        return convertView;
    }
}