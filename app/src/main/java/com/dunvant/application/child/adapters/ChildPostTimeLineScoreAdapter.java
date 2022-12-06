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
public class ChildPostTimeLineScoreAdapter extends BaseAdapter {
    Context context;
    ArrayList<ChildPostAdditionalTimelineDataBean> childPostAdditionalTimelineDataBeanArrayList;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ChildPostTimeLineScoreAdapter(Context context, ArrayList<ChildPostAdditionalTimelineDataBean> childPostAdditionalTimelineDataBeanArrayList){
        this.context = context;
        this.childPostAdditionalTimelineDataBeanArrayList = childPostAdditionalTimelineDataBeanArrayList;
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
        convertView = layoutInflater.inflate(R.layout.child_post_timeline_score_adapter , null);
        TextView circleText = (TextView) convertView.findViewById(R.id.circleText);
        DonutProgress donut_progress = (DonutProgress) convertView.findViewById(R.id.donut_progress);

        circleText.setTypeface(helvetica);

        final ChildPostAdditionalTimelineDataBean childPostAdditionalTimelineDataBean = childPostAdditionalTimelineDataBeanArrayList.get(position);

//        System.out.println("SizeHere::" + childPostAdditionalTimelineDataBeanArrayList.size());
        circleText.setText(childPostAdditionalTimelineDataBean.getDataElementName());

        Double perc = Double.parseDouble(childPostAdditionalTimelineDataBean.getDataElementPerformancePercentage());
        int valuePer = perc.intValue();

        donut_progress.setText(valuePer+"%");
        donut_progress.setDonut_progress("" + valuePer);
        donut_progress.setFinishedStrokeColor(Color.parseColor(childPostAdditionalTimelineDataBean.getDataElementColorCode()));
        donut_progress.setTextColor(Color.parseColor(childPostAdditionalTimelineDataBean.getDataElementColorCode()));

        return convertView;
    }
}
