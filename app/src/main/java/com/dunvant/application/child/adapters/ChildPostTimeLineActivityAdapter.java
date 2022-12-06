package com.dunvant.application.child.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildPostAdditionalTimelineDataBean;

import java.util.ArrayList;

/**
 * Created by DEVLABS\pbadhan on 7/7/17.
 */
public class ChildPostTimeLineActivityAdapter extends BaseAdapter {
    Context context;
    ArrayList<ChildPostAdditionalTimelineDataBean> childPostAdditionalTimelineDataBeanArrayList;
    LayoutInflater layoutInflater;
    String typestr;
    Typeface helvetica;
    Typeface linoType;

    public ChildPostTimeLineActivityAdapter(Context context, ArrayList<ChildPostAdditionalTimelineDataBean> childPostAdditionalTimelineDataBeanArrayList, String typestr){
        this.context = context;
        this.childPostAdditionalTimelineDataBeanArrayList = childPostAdditionalTimelineDataBeanArrayList;
        this.typestr = typestr;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");
    }
    @Override
    public int getCount() {
        return childPostAdditionalTimelineDataBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childPostAdditionalTimelineDataBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_post_timeline_activity_adapter , null);
        TextView circleText = (TextView) convertView.findViewById(R.id.circleText);
        TextView average = (TextView) convertView.findViewById(R.id.average);
        TextView type = (TextView) convertView.findViewById(R.id.type);
        DonutProgress donut_progress = (DonutProgress) convertView.findViewById(R.id.donut_progress);

        circleText.setTypeface(helvetica);
        average.setTypeface(helvetica);
        type.setTypeface(helvetica);

        final ChildPostAdditionalTimelineDataBean childPostAdditionalTimelineDataBean = childPostAdditionalTimelineDataBeanArrayList.get(position);

//        System.out.println("SizeHere::" + childPostAdditionalTimelineDataBeanArrayList.size());


        donut_progress.setText(childPostAdditionalTimelineDataBean.getTotal());

        donut_progress.setFinishedStrokeColor(Color.parseColor(childPostAdditionalTimelineDataBean.getColorCode()));
        donut_progress.setTextColor(Color.parseColor(childPostAdditionalTimelineDataBean.getColorCode()));


        type.setText(childPostAdditionalTimelineDataBean.getLevelName());

        if(typestr.equalsIgnoreCase("session_details")){
            average.setVisibility(View.VISIBLE);
            average.setText(childPostAdditionalTimelineDataBean.getAverage());
            circleText.setText(childPostAdditionalTimelineDataBean.getActivityName());
        }else{
            average.setVisibility(View.GONE);
            circleText.setText(childPostAdditionalTimelineDataBean.getActivityName().toUpperCase());
        }

        return convertView;
    }
}
