package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.RecycleExpBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.CoachAcceptedMembersScreen;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class CoachMidWeekPackageChildNamesAttendanceAdapter extends BaseAdapter {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Context context;
    ArrayList<RecycleExpBean> membersList;
    LayoutInflater layoutInflater;

    CoachAcceptedMembersScreen coachAcceptedMembersScreen;

    private final String APPROVE_CHALLENGE = "APPROVE_CHALLENGE";

    public CoachMidWeekPackageChildNamesAttendanceAdapter(Context context, ArrayList<RecycleExpBean> membersList) {
        this.context = context;
        this.membersList = membersList;
        this.layoutInflater = LayoutInflater.from(context);
        this.coachAcceptedMembersScreen = coachAcceptedMembersScreen;

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
    }

    @Override
    public int getCount() {
        return membersList.size();
    }

    @Override
    public Object getItem(int position) {
        return membersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_midweek_child_attendance_item, null);

        TextView childName = (TextView) convertView.findViewById(R.id.childName);
        RelativeLayout mainRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mainRelativeLayout);

        final RecycleExpBean childBean = membersList.get(position);



        if(childBean.getTotal_credit().equalsIgnoreCase("")){
            childName.setText(childBean.getChildName());
        }else{
            childName.setText(childBean.getChildName() +" ("+childBean.getUsed_credit()+"/"+childBean.getTotal_credit()+")");
        }


        if(position % 2 == 0){
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            childName.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            childName.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }


}