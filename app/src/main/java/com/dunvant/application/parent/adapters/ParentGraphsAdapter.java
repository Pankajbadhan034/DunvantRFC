package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildScoreBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public class ParentGraphsAdapter extends BaseAdapter{

    Context context;
    ArrayList<ChildScoreBean> scoresListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;
    String enableDisablePercentageStr;

    public ParentGraphsAdapter(Context context, ArrayList<ChildScoreBean> scoresListing, String enableDisablePercentageStr) {
        this.context = context;
        this.scoresListing = scoresListing;
        this.enableDisablePercentageStr = enableDisablePercentageStr;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return scoresListing.size();
    }

    @Override
    public Object getItem(int position) {
        return scoresListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_graph_item, null);

        TextView elementName = (TextView) convertView.findViewById(R.id.elementName);
        DonutProgress donutProgress = (DonutProgress) convertView.findViewById(R.id.donutProgress);
//        ImageView watchVideo = (ImageView) convertView.findViewById(R.id.watchVideo);
        ListView subElementListView = (ListView) convertView.findViewById(R.id.subElementListView);
        TextView areaOfDevelopment = (TextView) convertView.findViewById(R.id.areaOfDevelopment);
        TextView trainingPlan = (TextView) convertView.findViewById(R.id.trainingPlan);
        PieChart pieChart = convertView.findViewById(R.id.pieChart);

        elementName.setTypeface(linoType);
        areaOfDevelopment.setTypeface(helvetica);
        trainingPlan.setTypeface(linoType);

        final ChildScoreBean childScoreBean = scoresListing.get(position);

        elementName.setText(childScoreBean.getElementName());
        elementName.setBackgroundColor(Color.parseColor(childScoreBean.getColorCode()));
        areaOfDevelopment.setText(childScoreBean.getAreaOfDevelopment());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int sixtyPercent = (screenWidth * 60) / 100;
        float textWidthForTitle = areaOfDevelopment.getPaint().measureText(areaOfDevelopment.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / sixtyPercent) + 1;
        areaOfDevelopment.setLines(numberOfLines);

        try {
            donutProgress.setProgress(Float.parseFloat(childScoreBean.getPerformancePercentage()));
            donutProgress.setText(childScoreBean.getPerformancePercentage()+"%");
        }catch(NumberFormatException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        donutProgress.setFinishedStrokeColor(Color.parseColor(childScoreBean.getColorCode()));

        subElementListView.setAdapter(new ParentSubElementListingAdapter(context, childScoreBean.getDetailedScores()));
        Utilities.setListViewHeightBasedOnChildren(subElementListView);

        if(childScoreBean.getVideoUrl() == null || childScoreBean.getVideoUrl().isEmpty()) {
//            watchVideo.setVisibility(View.GONE);
            trainingPlan.setVisibility(View.GONE);
        } else {
//            watchVideo.setVisibility(View.VISIBLE);
            trainingPlan.setVisibility(View.VISIBLE);
        }

        trainingPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("Video URL "+childScoreBean.getVideoUrl());

                if(childScoreBean.getVideoUrl() == null || childScoreBean.getVideoUrl().isEmpty()) {
                    Toast.makeText(context, "Video not available", Toast.LENGTH_SHORT).show();
                } else {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(childScoreBean.getVideoUrl())));
                }

            }
        });


        setUpPieChartData(Float.parseFloat(childScoreBean.getPerformancePercentage()), pieChart, childScoreBean.getColorCode());

        return convertView;
    }

    private void setUpPieChartData(Float floatvalue, PieChart pieChart, String color) {

        Float valuef = 100f - floatvalue;

        System.out.println("floatvalue : "+floatvalue);

        ArrayList yVals = new ArrayList();
        yVals.add(new PieEntry(floatvalue));
        yVals.add(new PieEntry(valuef));

        PieDataSet dataSet = new PieDataSet((List)yVals, "");

       // if(enableDisablePercentageStr.equalsIgnoreCase("1")){
            dataSet.setValueTextSize(0.0F);
       // }

        ArrayList<Integer> colors = new ArrayList();
        colors.add(Color.parseColor(color));
        colors.add(Color.parseColor("#5c5c5c"));

        dataSet.setColors(colors);
        PieData data = new PieData((IPieDataSet)dataSet);
        Intrinsics.checkExpressionValueIsNotNull(pieChart, "pieChart");
        pieChart.setData(data);
        pieChart.setCenterTextRadiusPercent(0.0F);
        pieChart.setDrawHoleEnabled(false);
        Legend var10000 = pieChart.getLegend();
        Intrinsics.checkExpressionValueIsNotNull(var10000, "pieChart.legend");
        var10000.setEnabled(false);
        Description var6 = pieChart.getDescription();
        Intrinsics.checkExpressionValueIsNotNull(var6, "pieChart.description");
        var6.setEnabled(false);


    }
}