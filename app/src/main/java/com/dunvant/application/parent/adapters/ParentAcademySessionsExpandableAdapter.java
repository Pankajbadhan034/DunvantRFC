package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.AcademySessionBean;
import com.dunvant.application.beans.AcademySessionDetailBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentAcademySessionsExpandableAdapter extends BaseExpandableListAdapter{

    Context context;
    ArrayList<AcademySessionBean> sessionsList;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentAcademySessionsExpandableAdapter(Context context, ArrayList<AcademySessionBean> sessionsList){
        this.context = context;
        this.sessionsList = sessionsList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getGroupCount() {
        return sessionsList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return sessionsList.get(groupPosition).getSessionDetailList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return sessionsList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return sessionsList.get(groupPosition).getSessionDetailList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_expandable_adapter_academy_session_header_item, null);

        TextView dayName = (TextView) convertView.findViewById(R.id.dayName);

        AcademySessionBean sessionBean = sessionsList.get(groupPosition);

        dayName.setText(sessionBean.getDayName());

        dayName.setTypeface(linoType);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_expandable_adapter_academy_session_child_item, null);

        TextView ageGroup = (TextView) convertView.findViewById(R.id.ageGroup);
        TextView dates = (TextView) convertView.findViewById(R.id.dates);
        TextView hours = (TextView) convertView.findViewById(R.id.hours);
        TextView fillingFast = (TextView) convertView.findViewById(R.id.fillingFast);
        TextView bookingClosed = (TextView) convertView.findViewById(R.id.bookingClosed);
        TextView coachName = (TextView) convertView.findViewById(R.id.coachName);
        TextView coachingProgramName = (TextView) convertView.findViewById(R.id.coachingProgramName);
        TextView genderLabel = (TextView) convertView.findViewById(R.id.genderLabel);
        TextView sessionExpiresIn = convertView.findViewById(R.id.sessionExpiresIn);

        AcademySessionDetailBean sessionDetailBean = sessionsList.get(groupPosition).getSessionDetailList().get(childPosition);

        ageGroup.setText(sessionDetailBean.getGroupName());
        dates.setText(sessionDetailBean.getShowFromDate()+" - "+sessionDetailBean.getShowToDate()+" ("+sessionDetailBean.getNumberOfWeeks()+")");
//        hours.setText(sessionDetailBean.getNumberOfHours()+" Hours - "+sessionDetailBean.getCost()+" AED ("+sessionDetailBean.getShowStartTime()+" - "+sessionDetailBean.getShowEndTime()+")");

            SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            String academy_currency = sharedPreferences.getString("academy_currency", null);

            hours.setText(sessionDetailBean.getNumberOfHours()+" - "+sessionDetailBean.getCost()+" "+academy_currency+" ("+sessionDetailBean.getShowStartTime()+" - "+sessionDetailBean.getShowEndTime()+")");

        coachName.setText("Coach Name : "+sessionDetailBean.getCoachName());
        coachingProgramName.setText("Coaching Program : "+sessionDetailBean.getCoachingProgramName());
        genderLabel.setText(sessionDetailBean.getSessionGenderLabel());
        sessionExpiresIn.setText(sessionDetailBean.getSessionExpiresInLabel());

        if(sessionDetailBean.isThresholdCrossed()) {
            fillingFast.setVisibility(View.VISIBLE);
        } else {
            fillingFast.setVisibility(View.GONE);
        }

        if(sessionDetailBean.isBookingClosed()){
            bookingClosed.setVisibility(View.VISIBLE);
        } else {
            bookingClosed.setVisibility(View.GONE);
        }

        ageGroup.setTypeface(helvetica);
        dates.setTypeface(helvetica);
        hours.setTypeface(helvetica);
        sessionExpiresIn.setTypeface(helvetica);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}