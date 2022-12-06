package com.dunvant.application.child.smartBand.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.child.smartBand.ChildSportDetailSmartBandScreen;
import com.dunvant.application.child.smartBand.bean.ChildSmartBandSportHistoryBean;

import java.util.ArrayList;

/**
 * Created by DEVLABS\pbadhan on 29/6/17.
 */
public class ChildSportSmartBandHistoryAdapter extends BaseAdapter{
    ArrayList<ChildSmartBandSportHistoryBean>childSmartBandSportHistoryBeanArrayList;
    Context context;
    LayoutInflater layoutInflater;


    public ChildSportSmartBandHistoryAdapter(Context context, ArrayList<ChildSmartBandSportHistoryBean>childSmartBandSportHistoryBeanArrayList){
        this.context = context;
        this.childSmartBandSportHistoryBeanArrayList = childSmartBandSportHistoryBeanArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return childSmartBandSportHistoryBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childSmartBandSportHistoryBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_sport_smart_band_history, null);
        ImageView sportTypeImage = (ImageView) convertView.findViewById(R.id.sportTypeImage);
        final TextView startTime = (TextView) convertView.findViewById(R.id.startTime);
        final TextView endTime = (TextView) convertView.findViewById(R.id.endTime);
        final TextView distance = (TextView) convertView.findViewById(R.id.distance);
        final TextView calories = (TextView) convertView.findViewById(R.id.calories);
        final TextView duration = (TextView) convertView.findViewById(R.id.duration);
        final TextView historyDate = (TextView) convertView.findViewById(R.id.historyDate);
        LinearLayout linearClick = (LinearLayout) convertView.findViewById(R.id.linearClick);


        final ChildSmartBandSportHistoryBean childSmartBandSportHistoryBean = childSmartBandSportHistoryBeanArrayList.get(position);
        startTime.setText(childSmartBandSportHistoryBean.getStartTime());
        endTime.setText(childSmartBandSportHistoryBean.getEndTime());
        distance.setText(childSmartBandSportHistoryBean.getDistance()+"km");
        calories.setText(childSmartBandSportHistoryBean.getCalories()+"cal");
        duration.setText(childSmartBandSportHistoryBean.getDuration());
        historyDate.setText(childSmartBandSportHistoryBean.getCreatedAtFormatted());

        if(childSmartBandSportHistoryBean.getType().equalsIgnoreCase("nonifa")){
                    //type of sport set into image
        if(childSmartBandSportHistoryBean.getSportType().equalsIgnoreCase("1")){
            // walking
//            System.out.println("inWalking");
            sportTypeImage.setBackgroundResource(R.drawable.stepround);
        }else if(childSmartBandSportHistoryBean.getSportType().equalsIgnoreCase("7")){
            //running
//            System.out.println("inRunning");
            sportTypeImage.setBackgroundResource(R.drawable.running);
        }else if(childSmartBandSportHistoryBean.getSportType().equalsIgnoreCase("130")){
            //football
//            System.out.println("inFootball");
            sportTypeImage.setBackgroundResource(R.drawable.football);
        }else if(childSmartBandSportHistoryBean.getSportType().equalsIgnoreCase("136")){
            //cycle
//            System.out.println("inCycling");
            sportTypeImage.setBackgroundResource(R.drawable.cycling);
        }else{
//            System.out.println("HERE_ELSE_PART");
        }
        }else{
            sportTypeImage.setBackgroundResource(R.drawable.football);
        }



        linearClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(context, ChildSportDetailSmartBandScreen.class);
                obj.putExtra("workoutId", childSmartBandSportHistoryBean.getId());
                obj.putExtra("startTime", startTime.getText().toString());
                obj.putExtra("endTime", endTime.getText().toString());
                obj.putExtra("calories", calories.getText().toString());
                obj.putExtra("distance", distance.getText().toString());
                obj.putExtra("duration", duration.getText().toString());
                obj.putExtra("steps", childSmartBandSportHistoryBean.getSteps());
                obj.putExtra("sportType", childSmartBandSportHistoryBean.getSportType());
                obj.putExtra("date", historyDate.getText().toString());
                obj.putExtra("type", childSmartBandSportHistoryBean.getType());

                context.startActivity(obj);
            }
        });

        return convertView;
    }
}
