package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChildActivityBean;

import java.util.ArrayList;

public class ParentViewActivitiesAdapter extends BaseAdapter{

    Context context;
    ArrayList<ChildActivityBean> activitiesList;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public ParentViewActivitiesAdapter(Context context, ArrayList<ChildActivityBean> activitiesList) {
        this.context = context;
        this.activitiesList = activitiesList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return activitiesList.size();
    }

    @Override
    public Object getItem(int position) {
        return activitiesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_view_activity_item, null);

        LinearLayout mainLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
        TextView childName = (TextView) convertView.findViewById(R.id.childName);
        TextView dateTime = (TextView) convertView.findViewById(R.id.dateTime);
        TextView activity = (TextView) convertView.findViewById(R.id.activity);

        childName.setTypeface(helvetica);
        dateTime.setTypeface(helvetica);
        activity.setTypeface(helvetica);

        ChildActivityBean activityBean = activitiesList.get(position);

        childName.setText(activityBean.getChildName());
        dateTime.setText(activityBean.getShowCreatedAt());
        activity.setText(activityBean.getActivity());

        if(position % 2 == 0) {
            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            childName.setTextColor(context.getResources().getColor(R.color.white));
            dateTime.setTextColor(context.getResources().getColor(R.color.white));
            activity.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            childName.setTextColor(context.getResources().getColor(R.color.black));
            dateTime.setTextColor(context.getResources().getColor(R.color.black));
            activity.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }
}