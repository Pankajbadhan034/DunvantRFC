package com.dunvant.application.child.smartBand;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.smartBand.bean.ChildSmartBandHeartRateHistoryBean;
import com.dunvant.application.child.smartBand.bean.ChildSmartBandHeartRateHourDataBean;
import com.dunvant.application.child.smartBand.bean.ChildSmartBandLevelsBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class ChildHeartRateSmartBandScreen extends AppCompatActivity implements IWebServiceCallback {
//    TextView minimum;
//    TextView maximum;
//    TextView bluetoothText;
int amateurValue, semiProValue, professionalValue, worldClassValue, legendaryValue;
    String heartRateMin;
    String heartRateMax;
    String bluetoothStatusStr;
    ImageView backButton;
    ArrayList<ChildSmartBandHeartRateHourDataBean> childSmartBandHeartRateHourDataBeanArrayList = new ArrayList<>();
    GraphView graphView;
    public static ArrayList<ChildSmartBandLevelsBean>heartRateArrayList = new ArrayList<>();
    ArrayList<ChildSmartBandHeartRateHistoryBean> childSmartBandHeartRateHistoryBeanArrayList = new ArrayList<>();
    ImageView history;
    TextView amateur;
    TextView semiPro;
    TextView professional;
    TextView worldClass;
    TextView legendary;

    Typeface helvetica;
    Typeface linoType;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    private final String  GET_LEVELS = "GET_LEVELS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_heart_rate_smart_band_screen);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

//        minimum = (TextView) findViewById(R.id.minimum);
//        maximum = (TextView) findViewById(R.id.maximum);
//        bluetoothText = (TextView) findViewById(R.id.bluetoothText);
        backButton = (ImageView) findViewById(R.id.backButton);
        graphView = (GraphView) findViewById(R.id.graph);
        history = (ImageView) findViewById(R.id.history);
        amateur = (TextView) findViewById(R.id.amateur);
        semiPro = (TextView) findViewById(R.id.semiPro);
        professional = (TextView) findViewById(R.id.professional);
        worldClass = (TextView) findViewById(R.id.worldClass);
        legendary = (TextView) findViewById(R.id.legendary);

        heartRateMax = getIntent().getStringExtra("heartRateMax");
        heartRateMin = getIntent().getStringExtra("heartRateMin");
        bluetoothStatusStr = getIntent().getStringExtra("bluetoothStatus");

//        minimum.setText(heartRateMin);
//        maximum.setText(heartRateMax);
//        bluetoothText.setText(bluetoothStatusStr);

        try {
            childSmartBandHeartRateHourDataBeanArrayList = (ArrayList<ChildSmartBandHeartRateHourDataBean>) getIntent().getSerializableExtra("childSmartBandHeartRateHourDataBeanArrayList");
            // reverse arraylist
//            Collections.reverse(childSmartBandHeartRateHourDataBeanArrayList);
            if(childSmartBandHeartRateHourDataBeanArrayList==null){
//                System.out.println("HERE_EMPTY");
            }else{
                //ChildSportSmartBandAdapter childSportSmartBandAdapter = new ChildSportSmartBandAdapter(ChildHeartRateSmartBandScreen.this, childSmartBandHeartRateHourDataBeanArrayList);
                //listSport.setAdapter(childSportSmartBandAdapter);
//                System.out.println("SizeHERE::::"+childSmartBandHeartRateHourDataBeanArrayList.size());
                for(int i=0; i<childSmartBandHeartRateHourDataBeanArrayList.size(); i++){
                    ChildSmartBandHeartRateHourDataBean childSmartBandHeartRateHourDataBean = childSmartBandHeartRateHourDataBeanArrayList.get(i);
//                    System.out.println("hours::"+childSmartBandHeartRateHourDataBean.getHours());
//                    System.out.println("detailData::"+childSmartBandHeartRateHourDataBean.getDetailData());

                    String detail_data = childSmartBandHeartRateHourDataBean.getDetailData();

                    try{
                        detail_data = detail_data.replace("[", "");
                        detail_data = detail_data.replace("]", "");

                        ArrayList<Integer> heartRatevalue = new ArrayList<>();
                        String[] heartRate = detail_data.split(",");
                        for (String single : heartRate) {
//                            System.out.println("singleValue::" + single);
                            if (single.equalsIgnoreCase("[255") || single.equalsIgnoreCase("255") || single.equalsIgnoreCase("255]")) {

                            } else {
                                heartRatevalue.add(Integer.parseInt(single));
                            }
                        }
//                        System.out.println("MaxHeartRate::" + Collections.max(heartRatevalue));
//                        System.out.println("MinHeartRate::" + Collections.min(heartRatevalue));

                        int hours = Integer.parseInt(childSmartBandHeartRateHourDataBean.getHours());
                        int max = Collections.max(heartRatevalue);
                        int min = Collections.min(heartRatevalue);

                        ChildSmartBandHeartRateHistoryBean  childSmartBandHeartRateHistoryBean = new ChildSmartBandHeartRateHistoryBean();
                        childSmartBandHeartRateHistoryBean.setHours(hours);
                        childSmartBandHeartRateHistoryBean.setMaxVal(max);
                        childSmartBandHeartRateHistoryBean.setMinVal(min);

                        childSmartBandHeartRateHistoryBeanArrayList.add(childSmartBandHeartRateHistoryBean);

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
                initGraph();
                heartRateLevelMatch();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        if (Utilities.isNetworkAvailable(ChildHeartRateSmartBandScreen.this)) {
            String webServiceUrl = Utilities.BASE_URL + "children/get_level";
            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            if(Utilities.isNetworkAvailable(ChildHeartRateSmartBandScreen.this)) {
                GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ChildHeartRateSmartBandScreen.this, GET_LEVELS, ChildHeartRateSmartBandScreen.this, headers);
                getWebServiceWithHeadersAsync.execute(webServiceUrl);
            }else{
                Toast.makeText(ChildHeartRateSmartBandScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ChildHeartRateSmartBandScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(ChildHeartRateSmartBandScreen.this, ChildHeartRateHistorySmartBandScreen.class);
                startActivity(obj);
            }
        });

    }

//    public void initGraph(GraphView graph) {
//        // first series
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 71),
//                new DataPoint(1, 75),
//                new DataPoint(2, 63),
//                new DataPoint(3, 62),
//                new DataPoint(4, 96),
//                new DataPoint(5, 71),
//                new DataPoint(6, 75),
//                new DataPoint(7, 63),
//                new DataPoint(8, 62),
//                new DataPoint(9, 71),
//                new DataPoint(10, 75),
//                new DataPoint(11, 63),
//                new DataPoint(12, 62),
//                new DataPoint(13, 71),
//                new DataPoint(14, 75),
//                new DataPoint(15, 63),
//                new DataPoint(16, 62),
//                new DataPoint(17, 71),
//                new DataPoint(18, 75),
//                new DataPoint(19, 63),
//                new DataPoint(20, 62),
//                new DataPoint(21, 71),
//                new DataPoint(22, 75),
//                new DataPoint(23, 50)
//
//        });
//        series.setTitle("Max");
//        series.setAnimated(true);
//        //series.setDrawBackground(true);
//        series.setColor(Color.parseColor("#ff0000"));
//        //series.setBackgroundColor(Color.parseColor("#ffccff"));
//        //series.setDrawDataPoints(true);
//        graph.addSeries(series);
//
//        // second series
//        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 40),
//                new DataPoint(1, 49),
//                new DataPoint(2, 67),
//                new DataPoint(3, 45),
//                new DataPoint(4, 47)
//        });
//        series2.setTitle("Min");
//        series2.setAnimated(true);
//        // series2.setDrawBackground(true);
//        series2.setColor(Color.parseColor("#123456"));
//        // series2.setBackgroundColor(Color.parseColor("#ff0000"));
//        // series2.setDrawDataPoints(true);
//        graph.addSeries(series2);
//
//        // legend
//        graph.getLegendRenderer().setVisible(true);
//        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
//
//        graph.getViewport().setXAxisBoundsManual(true);
//        // graph.getViewport().setMinX(histories.get(0).dateToTimestamp());
//        graph.getViewport().setMaxX(25);
//        graph.setBackgroundColor(Color.parseColor("#ffffff"));
//        graph.getViewport().setBorderColor(Color.parseColor("#ff0000"));
//
//    }

    public void heartRateLevelMatch(){
        for(int i=0; i<childSmartBandHeartRateHistoryBeanArrayList.size(); i++){
            final ChildSmartBandHeartRateHistoryBean childSmartBandHeartRateHistoryBean = childSmartBandHeartRateHistoryBeanArrayList.get(i);

            for(int j=0; j<ChildHeartRateSmartBandScreen.heartRateArrayList.size(); j++){
                final ChildSmartBandLevelsBean childSmartBandLevelsBean = heartRateArrayList.get(j);

                if(j==0){
                    if(childSmartBandHeartRateHistoryBean.getMaxVal()>childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<childSmartBandLevelsBean.getEndVal()){
                        amateurValue++;
                    }
                }else if(j==1){
                    if(childSmartBandHeartRateHistoryBean.getMaxVal()>childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<childSmartBandLevelsBean.getEndVal()){
                        semiProValue++;
                    }
                }else if(j==2){
                    if(childSmartBandHeartRateHistoryBean.getMaxVal()>childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<childSmartBandLevelsBean.getEndVal()){
                        professionalValue++;
                    }
                }else if(j==3){
                    if(childSmartBandHeartRateHistoryBean.getMaxVal()>childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<childSmartBandLevelsBean.getEndVal()){
                        worldClassValue++;
                    }
                }else if(j==4){
                    if(childSmartBandHeartRateHistoryBean.getMaxVal()>childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<childSmartBandLevelsBean.getEndVal()){
                        legendaryValue++;
                    }
                }

            }
        }

        amateur.setText(""+amateurValue);
        semiPro.setText(""+semiProValue);
        professional.setText(""+professionalValue);
        worldClass.setText(""+worldClassValue);
        legendary.setText(""+legendaryValue);
    }

    public void initGraph() {

        LineGraphSeries<DataPoint> series;
        LineGraphSeries<DataPoint> series2;
        DataPoint[] arr = new DataPoint[childSmartBandHeartRateHistoryBeanArrayList.size()];
        DataPoint[] arr2 = new DataPoint[childSmartBandHeartRateHistoryBeanArrayList.size()];

//        System.out.println("SizeHere::"+childSmartBandHeartRateHistoryBeanArrayList.size());

        for(int i=0; i<childSmartBandHeartRateHistoryBeanArrayList.size(); i++){
            final ChildSmartBandHeartRateHistoryBean childSmartBandHeartRateHistoryBean = childSmartBandHeartRateHistoryBeanArrayList.get(i);

            int max = childSmartBandHeartRateHistoryBean.getMaxVal();
            int min = childSmartBandHeartRateHistoryBean.getMinVal();
            int hour = childSmartBandHeartRateHistoryBean.getHours();

//            System.out.println("max--"+max+"--min--"+min+"--hour--"+hour);

            arr[i] = new DataPoint(hour, max);
            arr2[i] = new DataPoint(hour, min);

        }

        series = new LineGraphSeries<>(arr);
        series2 = new LineGraphSeries<>(arr2);

        series.setTitle("Max");
        series.setAnimated(true);
        series.setColor(Color.parseColor("#ff0000"));
        series.setDrawDataPoints(true);
        graphView.addSeries(series);

        series2.setTitle("Min");
        series2.setAnimated(true);
        series2.setColor(Color.parseColor("#123456"));
        series2.setDrawDataPoints(true);
        graphView.addSeries(series2);

        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(25);
        graphView.setBackgroundColor(Color.parseColor("#ffffff"));


    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        if (response == null) {
            Toast.makeText(ChildHeartRateSmartBandScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject responseObject = new JSONObject(response);
                boolean status = responseObject.getBoolean("status");
                String message = responseObject.getString("message");
                if(status){
                    JSONObject data = new JSONObject(responseObject.getString("data"));

                    // for Heart rate Data
                    JSONArray heartRateArray = data.getJSONArray("Heart Rate");
                    for(int i=0; i<heartRateArray.length(); i++){
                        JSONObject jsonObject = heartRateArray.getJSONObject(i);
                        ChildSmartBandLevelsBean childSmartBandLevelsBean = new ChildSmartBandLevelsBean();
                        childSmartBandLevelsBean.setId(jsonObject.getString("id"));
                        childSmartBandLevelsBean.setName(jsonObject.getString("name"));
                        childSmartBandLevelsBean.setStartVal(jsonObject.getInt("start_val"));
                        childSmartBandLevelsBean.setEndVal(jsonObject.getInt("end_val"));
                        childSmartBandLevelsBean.setLevelType(jsonObject.getInt("level_type"));
                        heartRateArrayList.add(childSmartBandLevelsBean);
                    }

                }else{
                    Toast.makeText(ChildHeartRateSmartBandScreen.this, message, Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ChildHeartRateSmartBandScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
