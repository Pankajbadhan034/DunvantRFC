package com.dunvant.application.coach.adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChildAttendanceBean;
import com.dunvant.application.coach.fragments.CoachParentDetailDialogFragment;

import java.util.ArrayList;

public class CoachAttendanceListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<ChildAttendanceBean> childrenAttendanceListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public CoachAttendanceListingAdapter(Context context, ArrayList<ChildAttendanceBean> childrenAttendanceListing) {
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
        convertView = layoutInflater.inflate(R.layout.coach_adapter_child_attendance_item, null);

        TextView childName = (TextView) convertView.findViewById(R.id.childName);
        ImageView tick = (ImageView) convertView.findViewById(R.id.tick);
        ImageView cross = (ImageView) convertView.findViewById(R.id.cross);

        childName.setTypeface(helvetica);

        final ChildAttendanceBean childAttendanceBean = childrenAttendanceListing.get(position);

        childName.setText(childAttendanceBean.getChildName());
        switch (childAttendanceBean.getStatus()) {
            case "0":
                break;
            case "1":
                tick.setBackgroundResource(R.drawable.enabledtick);
                break;
            case "2":
                cross.setBackgroundResource(R.drawable.enabledcross);
                break;
        }

        if(childAttendanceBean.getIsTrial().equalsIgnoreCase("1")) {
            childName.setTextColor(context.getResources().getColor(R.color.red));
        }

        childName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoachParentDetailDialogFragment coachParentDetailDialogFragment = new CoachParentDetailDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("childId", childAttendanceBean.getUserId());
                bundle.putString("childName", childAttendanceBean.getChildName());
                coachParentDetailDialogFragment.setArguments(bundle);
                coachParentDetailDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "Dialog Fragment");
            }
        });

        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childAttendanceBean.setStatus("1");
                notifyDataSetChanged();
            }
        });

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childAttendanceBean.setStatus("2");
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}