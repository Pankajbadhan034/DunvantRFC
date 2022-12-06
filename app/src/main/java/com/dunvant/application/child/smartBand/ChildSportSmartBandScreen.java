package com.dunvant.application.child.smartBand;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.child.smartBand.adapter.ChildSportSmartBandAdapter;
import com.dunvant.application.child.smartBand.bean.ChildSmartBandSegmentDataBean;

import java.util.ArrayList;
import java.util.Collections;

public class ChildSportSmartBandScreen extends AppCompatActivity {
//    {
//        "_uploaded": 0,
//            "calorie": 6.1999999,
//            "complete_progress": 2,
//            "day": 12,
//            "detail_data": "{\"activity\":7,\"count\":0,\"distance\":130.9,\"step\":215}",
//            "end_time": 815,
//            "index": 35,
//            "month": 6,
//            "reserved": 0,
//            "sport_type": 1,
//            "start_time": 794,
//            "uid": 0,
//            "week": 24,
//            "year": 2017
//    }
    ImageView backButton;
    ImageView refresh;

    TextView calories;
    TextView steps;
    TextView distance;
//    TextView caloriesBold;
//    TextView stepsBold;
    TextView bluetoothText;
    TextView date;
    ListView listSport;

    String caloriesStr;
    String stepsStr;
    String distanceStr;
    String bluetoothStatusStr;
    String dateStr;
    ArrayList<ChildSmartBandSegmentDataBean> childSmartBandSegmentDataBeanArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_sport_smart_band_screen);
        backButton = (ImageView) findViewById(R.id.backButton);
        calories = (TextView) findViewById(R.id.calories);
        steps = (TextView) findViewById(R.id.steps);
        distance = (TextView) findViewById(R.id.distance);
//        caloriesBold = (TextView) findViewById(R.id.caloriesBold);
//        stepsBold = (TextView) findViewById(R.id.stepsBold);
        bluetoothText = (TextView) findViewById(R.id.bluetoothText);
        listSport = (ListView) findViewById(R.id.listSport);
        date = (TextView) findViewById(R.id.date);
        refresh = (ImageView) findViewById(R.id.refresh);

        caloriesStr = getIntent().getStringExtra("calories");
        stepsStr = getIntent().getStringExtra("steps");
        distanceStr = getIntent().getStringExtra("distance");
        bluetoothStatusStr = getIntent().getStringExtra("bluetoothStatus");
        dateStr = getIntent().getStringExtra("dateStr");


        calories.setText(caloriesStr);
        steps.setText(stepsStr);
        distance.setText(distanceStr);
//        caloriesBold.setText("Calories "+caloriesStr);
//        stepsBold.setText(stepsStr);
        bluetoothText.setText(bluetoothStatusStr);
        date.setText(dateStr);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(ChildSportSmartBandScreen.this, ChildSportSmartBandHistory.class);
                startActivity(obj);
            }
        });

        try {
            childSmartBandSegmentDataBeanArrayList = (ArrayList<ChildSmartBandSegmentDataBean>) getIntent().getSerializableExtra("childSmartBandSegmentDataBeanArrayList");
            // reverse arraylist
            Collections.reverse(childSmartBandSegmentDataBeanArrayList);
            if(childSmartBandSegmentDataBeanArrayList==null){
//                System.out.println("HERE_EMPTY");
            }else{
                ChildSportSmartBandAdapter childSportSmartBandAdapter = new ChildSportSmartBandAdapter(ChildSportSmartBandScreen.this, childSmartBandSegmentDataBeanArrayList);
                listSport.setAdapter(childSportSmartBandAdapter);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
