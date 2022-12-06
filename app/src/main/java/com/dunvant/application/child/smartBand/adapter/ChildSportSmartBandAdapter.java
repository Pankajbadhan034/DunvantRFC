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
import com.dunvant.application.child.smartBand.bean.ChildSmartBandSegmentDataBean;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by DEVLABS\pbadhan on 14/6/17.
 */
public class ChildSportSmartBandAdapter extends BaseAdapter {


    ArrayList<ChildSmartBandSegmentDataBean>childSmartBandSegmentDataBeanArrayList = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;

    public  ChildSportSmartBandAdapter(Context context, ArrayList<ChildSmartBandSegmentDataBean>childSmartBandSegmentDataBeanArrayList){
        this.context = context;
        this.childSmartBandSegmentDataBeanArrayList = childSmartBandSegmentDataBeanArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return childSmartBandSegmentDataBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childSmartBandSegmentDataBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_sport_smart_band_item, null);
        ImageView sportTypeImage = (ImageView) convertView.findViewById(R.id.sportTypeImage);
        final TextView startTime = (TextView) convertView.findViewById(R.id.startTime);
        final TextView endTime = (TextView) convertView.findViewById(R.id.endTime);
        final TextView distance = (TextView) convertView.findViewById(R.id.distance);
        final TextView calories = (TextView) convertView.findViewById(R.id.calories);
        final TextView duration = (TextView) convertView.findViewById(R.id.duration);
        LinearLayout linearClick = (LinearLayout) convertView.findViewById(R.id.linearClick);

        final ChildSmartBandSegmentDataBean childSmartBandSegmentDataBean = childSmartBandSegmentDataBeanArrayList.get(position);

        //start time
        int hourStartTime = Integer.parseInt(childSmartBandSegmentDataBean.getStartTime());
        int minStartTime = Integer.parseInt(childSmartBandSegmentDataBean.getStartTime());
        hourStartTime = hourStartTime/60;
        minStartTime = minStartTime%60;


        if(hourStartTime<10 && minStartTime<10){
            startTime.setText("0"+hourStartTime+":0"+minStartTime);
        }else if(hourStartTime>=10 && minStartTime<10){
            startTime.setText(hourStartTime+":0"+minStartTime);
        }else if(hourStartTime<10 && minStartTime>=10){
            startTime.setText("0"+hourStartTime+":"+minStartTime);
        }else{
            startTime.setText(hourStartTime+":"+minStartTime);
        }


//        int hourlength = Integer.valueOf(hourStartTime).toString().length();
//        int minlength = Integer.valueOf(minStartTime).toString().length();
//
//        if(hourlength==1 && minlength==1){
//            startTime.setText("0"+hourStartTime+":0"+minStartTime);
//        }else if(hourlength==1 && minlength>1){
//            startTime.setText("0"+hourStartTime+":"+minStartTime);
//        }else if(hourlength>1 && minlength==1){
//            startTime.setText(hourStartTime+":0"+minStartTime);
//        }else{
//            startTime.setText(hourStartTime+":"+minStartTime);
//        }

        //end time
        int hourEndTime = Integer.parseInt(childSmartBandSegmentDataBean.getEndTime());
        int minEndTime = Integer.parseInt(childSmartBandSegmentDataBean.getEndTime());
        hourEndTime = hourEndTime/60;
        minEndTime = minEndTime%60;

        if(hourEndTime<10 && minEndTime<10){
            endTime.setText("0"+hourEndTime+":0"+minEndTime);
        }else if(hourEndTime>=10 && minEndTime<10){
            endTime.setText(hourEndTime+":0"+minEndTime);
        }else if(hourEndTime<10 && minEndTime>=10){
            endTime.setText("0"+hourEndTime+":"+minEndTime);
        }else{
            endTime.setText(hourEndTime+":"+minEndTime);
        }

//        int endHourlength = Integer.valueOf(hourEndTime).toString().length();
//        int endMinlength = Integer.valueOf(minEndTime).toString().length();
//
//        if(endHourlength==1 && endMinlength==1){
//            endTime.setText("0"+hourEndTime+":0"+minEndTime);
//        }else if(endHourlength==1 && endMinlength>1){
//            endTime.setText("0"+hourEndTime+":"+minEndTime);
//        }else if(endHourlength>1 && endMinlength==1){
//            endTime.setText(hourEndTime+":0"+minEndTime);
//        }else{
//            endTime.setText(hourEndTime+":"+minEndTime);
//        }

        //distance
        double km = Double.parseDouble(childSmartBandSegmentDataBean.getDistance());
        km = km/1000;
        distance.setText(new DecimalFormat("##.##").format(km)+" km");

        //calories
        double caloriesDouble = Double.parseDouble(childSmartBandSegmentDataBean.getCalories());
        calories.setText(new DecimalFormat("##.##").format(caloriesDouble)+"kcal");

        //duration
//        hourStartTime = hourStartTime/60;
//        minStartTime = minStartTime%60;
        double startTimeLong = Double.parseDouble(hourStartTime + "." + minStartTime);
        double endTimeLong = Double.parseDouble(hourEndTime + "." + minEndTime);
        double durationFinal = endTimeLong-startTimeLong;
        double values = Double.parseDouble(new DecimalFormat("##.##").format(durationFinal)) ;
        String durationStr = Double.toString(values);
        durationStr.replace(".",":");

        duration.setText(durationStr);
//        System.out.println("Type_of_sport::"+childSmartBandSegmentDataBean.getSportType());

        //type of sport set into image
        if(childSmartBandSegmentDataBean.getSportType().equalsIgnoreCase("1")){
            // walking
//            System.out.println("inWalking");
            sportTypeImage.setBackgroundResource(R.drawable.stepround);
        }else if(childSmartBandSegmentDataBean.getSportType().equalsIgnoreCase("7")){
            //running
//            System.out.println("inRunning");
            sportTypeImage.setBackgroundResource(R.drawable.running);
        }else if(childSmartBandSegmentDataBean.getSportType().equalsIgnoreCase("130")){
            //football
//            System.out.println("inFootball");
            sportTypeImage.setBackgroundResource(R.drawable.football);
        }else if(childSmartBandSegmentDataBean.getSportType().equalsIgnoreCase("136")){
            //cycle
//            System.out.println("inCycling");
            sportTypeImage.setBackgroundResource(R.drawable.cycling);
        }else{
//            System.out.println("HERE_ELSE_PART");
        }

        linearClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(context, ChildSportDetailSmartBandScreen.class);
                obj.putExtra("startTime", startTime.getText().toString());
                obj.putExtra("endTime", endTime.getText().toString());
                obj.putExtra("calories", calories.getText().toString());
                obj.putExtra("distance", distance.getText().toString());
                obj.putExtra("duration", duration.getText().toString());
                obj.putExtra("steps", childSmartBandSegmentDataBean.getSteps());
                obj.putExtra("sportType", childSmartBandSegmentDataBean.getSportType());
                obj.putExtra("date", "");

                context.startActivity(obj);
            }
        });

        return convertView;
    }
}
