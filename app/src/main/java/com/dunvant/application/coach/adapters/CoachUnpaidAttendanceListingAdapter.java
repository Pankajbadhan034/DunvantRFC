package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChildAttendanceBean;

import java.util.ArrayList;

public class CoachUnpaidAttendanceListingAdapter extends BaseAdapter {

    Context context;
    ArrayList<ChildAttendanceBean> childrenAttendanceListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public CoachUnpaidAttendanceListingAdapter(Context context, ArrayList<ChildAttendanceBean> childrenAttendanceListing) {
        this.context = context;
        this.childrenAttendanceListing = childrenAttendanceListing;
        layoutInflater = LayoutInflater.from(context);
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return childrenAttendanceListing.size();
    }

    @Override
    public Object getItem(int position) {
        return childrenAttendanceListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_unpaid_attendance_item, null);

        TextView unpaidChildName = (TextView) convertView.findViewById(R.id.unpaidChildName);

        unpaidChildName.setTypeface(helvetica);

        final ChildAttendanceBean childAttendanceBean = childrenAttendanceListing.get(position);

        unpaidChildName.setText(childAttendanceBean.getChildName());

        return convertView;
    }
}