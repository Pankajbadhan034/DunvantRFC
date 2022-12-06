package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dunvant.application.Chapter;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentMidWeekPackageAttendanceListAdapter extends BaseAdapter {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Context context;
    ArrayList<Chapter> membersList;
    LayoutInflater layoutInflater;


    private final String APPROVE_CHALLENGE = "APPROVE_CHALLENGE";

    public ParentMidWeekPackageAttendanceListAdapter(Context context, ArrayList<Chapter> membersList) {
        this.context = context;
        this.membersList = membersList;
        this.layoutInflater = LayoutInflater.from(context);

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
        convertView = layoutInflater.inflate(R.layout.parent_adapter_midweek_package_attendance_item, null);

        TextView childName = (TextView) convertView.findViewById(R.id.childName);
        TextView srNo = convertView.findViewById(R.id.srNo);
        ImageView arrow = convertView.findViewById(R.id.arrow);
        RelativeLayout mainRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mainRelativeLayout);

        final Chapter childBean = membersList.get(position);

        childName.setText(childBean.getChapterName());

        int i = childBean.getId()+1;
        srNo.setText(""+i+".");

        if(childBean.getStatus().equalsIgnoreCase("1")){
            childName.setText(childBean.getChapterName());
            childName.setBackgroundColor(context.getResources().getColor(R.color.white));
            arrow.setVisibility(View.GONE);
        }else if(childBean.getStatus().equalsIgnoreCase("0")){
            childName.setText(childBean.getChapterName());
            childName.setBackgroundColor(context.getResources().getColor(R.color.white));
            arrow.setVisibility(View.GONE);
        }else{
            childName.setText(childBean.getChapterName());
            childName.setBackgroundColor(context.getResources().getColor(R.color.red_trans));
            arrow.setVisibility(View.GONE);
        }

//        childName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//
//            }
//        });


//        if(position % 2 == 0){
//            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
//            childName.setTextColor(context.getResources().getColor(R.color.white));
//        } else {
//            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
//            childName.setTextColor(context.getResources().getColor(R.color.black));
//        }

        return convertView;
    }


}
