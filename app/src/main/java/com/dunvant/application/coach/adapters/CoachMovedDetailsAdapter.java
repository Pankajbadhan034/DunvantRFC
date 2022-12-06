package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.AttendanceDateBean;
import com.dunvant.application.beans.MovedDetailBean;

import java.util.ArrayList;

public class CoachMovedDetailsAdapter extends BaseAdapter{

    Context context;
    ArrayList<MovedDetailBean> movedDetailsListing;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public CoachMovedDetailsAdapter(Context context, ArrayList<MovedDetailBean> movedDetailsListing){
        this.context = context;
        this.movedDetailsListing = movedDetailsListing;
        layoutInflater = LayoutInflater.from(context);
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return movedDetailsListing.size();
    }

    @Override
    public Object getItem(int i) {
        return movedDetailsListing.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.coach_adapter_moved_details_item, null);

        TextView lblLocation = view.findViewById(R.id.lblLocation);
        TextView location = view.findViewById(R.id.location);
        TextView lblCoachingProgramName = view.findViewById(R.id.lblCoachingProgramName);
        TextView coachingProgramName = view.findViewById(R.id.coachingProgramName);
        TextView lblSession = view.findViewById(R.id.lblSession);
        TextView session = view.findViewById(R.id.session);
        TextView lblAgeGroup = view.findViewById(R.id.lblAgeGroup);
        TextView ageGroup = view.findViewById(R.id.ageGroup);
        TextView lblDaysAttended = view.findViewById(R.id.lblDaysAttended);
        TextView daysAttended = view.findViewById(R.id.daysAttended);

        MovedDetailBean movedDetailBean = movedDetailsListing.get(i);

        location.setText(movedDetailBean.getLocationsName());
        coachingProgramName.setText(movedDetailBean.getCoachingProgramsName());
        session.setText(movedDetailBean.getDayLabel()+" | "+movedDetailBean.getStartTimeFormatted()+" - "+movedDetailBean.getEndTimeFormatted());
        ageGroup.setText(movedDetailBean.getGroupName());

        String strDaysAttended = "";
        for(AttendanceDateBean attendanceDateBean : movedDetailBean.getAttendanceListing()){
            strDaysAttended += attendanceDateBean.getBookedSessionDate()+", ";
        }
        if (strDaysAttended != null && strDaysAttended.length() > 0 && strDaysAttended.charAt(strDaysAttended.length() - 1) == ',') {
            strDaysAttended = strDaysAttended.substring(0, strDaysAttended.length() - 1);
        }

        if(strDaysAttended == null || strDaysAttended.isEmpty()){
            daysAttended.setText("---");
        } else {
            daysAttended.setText(strDaysAttended);
        }

        lblLocation.setTypeface(helvetica);
        location.setTypeface(helvetica);
        lblCoachingProgramName.setTypeface(helvetica);
        coachingProgramName.setTypeface(helvetica);
        lblSession.setTypeface(helvetica);
        session.setTypeface(helvetica);
        lblAgeGroup.setTypeface(helvetica);
        ageGroup.setTypeface(helvetica);
        lblDaysAttended.setTypeface(helvetica);
        daysAttended.setTypeface(helvetica);

        return view;
    }
}