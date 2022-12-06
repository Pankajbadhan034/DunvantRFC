package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupAttendanceBean;
import com.dunvant.application.parent.ParentChildActivityStatsScreen;
import com.dunvant.application.parent.ParentChildReportScreen;

import java.util.ArrayList;

public class ParentTermNameListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<AgeGroupAttendanceBean> ageGroupAttendanceListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentTermNameListingAdapter(Context context, ArrayList<AgeGroupAttendanceBean> ageGroupAttendanceListing) {
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
        convertView = layoutInflater.inflate(R.layout.parent_adapter_term_name_item, null);

        TextView termName = (TextView) convertView.findViewById(R.id.termName);
        TextView bookingDate = convertView.findViewById(R.id.bookingDate);
        ImageView activityStats = (ImageView) convertView.findViewById(R.id.activityStats);
        ImageView childReport = (ImageView) convertView.findViewById(R.id.childReport);

        final AgeGroupAttendanceBean ageGroupAttendanceBean = ageGroupAttendanceListing.get(position);

        /*if(ageGroupAttendanceBean.getTotalScore() == null || ageGroupAttendanceBean.getTotalScore().isEmpty()) {
            termName.setText(ageGroupAttendanceBean.getTermsName() + " (Score not uploaded yet)");
            childReport.setVisibility(View.INVISIBLE);
        } else {
            termName.setText(ageGroupAttendanceBean.getTermsName());
            childReport.setVisibility(View.VISIBLE);
        }*/

        if(ageGroupAttendanceBean.getScoreId() == null || ageGroupAttendanceBean.getScoreId().isEmpty() || ageGroupAttendanceBean.getScoreId().equalsIgnoreCase("null")) {
            termName.setText(ageGroupAttendanceBean.getTermsName() + " (Score not uploaded yet)");
            childReport.setVisibility(View.INVISIBLE);
        } else {
            termName.setText(ageGroupAttendanceBean.getTermsName());
            childReport.setVisibility(View.VISIBLE);
        }

        bookingDate.setText(ageGroupAttendanceBean.getBookingDate());

        activityStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent childReport = new Intent(context, ParentChildActivityStatsScreen.class);
                childReport.putExtra("clickedOnAgeGroup", ageGroupAttendanceBean);
                context.startActivity(childReport);
            }
        });

        childReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(ageGroupAttendanceBean.getTotalScore() != null && !ageGroupAttendanceBean.getTotalScore().isEmpty()) {
                    Intent childReport = new Intent(context, ParentChildReportScreen.class);
                    childReport.putExtra("clickedOnAgeGroup", ageGroupAttendanceBean);
                    context.startActivity(childReport);
                } else {
                    Toast.makeText(context, "Score not uploaded yet", Toast.LENGTH_SHORT).show();
                }*/

                if(ageGroupAttendanceBean.getScoreId() != null && !ageGroupAttendanceBean.getScoreId().isEmpty() && !ageGroupAttendanceBean.getScoreId().equalsIgnoreCase("null")) {
                    Intent childReport = new Intent(context, ParentChildReportScreen.class);
                    childReport.putExtra("clickedOnAgeGroup", ageGroupAttendanceBean);
                    context.startActivity(childReport);
                } else {
                    Toast.makeText(context, "Score not uploaded yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        termName.setTypeface(helvetica);
        bookingDate.setTypeface(helvetica);

        return convertView;
    }
}