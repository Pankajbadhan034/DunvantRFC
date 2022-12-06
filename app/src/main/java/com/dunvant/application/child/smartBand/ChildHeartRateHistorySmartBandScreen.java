package com.dunvant.application.child.smartBand;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.smartBand.adapter.ChildHeartRateNonIFadpater;
import com.dunvant.application.child.smartBand.bean.ChildSmartBandHeartRateHistoryBean;
import com.dunvant.application.child.smartBand.bean.ChildSmartBandLevelsBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChildHeartRateHistorySmartBandScreen extends AppCompatActivity implements IWebServiceCallback {
    ArrayList<ChildSmartBandLevelsBean>heartRateArrayList = new ArrayList<>();
    GraphView graphView;
    ImageView prev;
    ImageView next;
    TextView dateText;

    ImageView prevNew;
    ImageView nextNew;
    TextView dateTextNew;

    int amateurValue, semiProValue, professionalValue, worldClassValue, legendaryValue;
    TextView amateur;
    TextView semiPro;
    TextView professional;
    TextView worldClass;
    TextView legendary;
    ImageView backButton;
    ChildSmartBandHeartRateHistoryBean childSmartBandHeartRateHistoryBean;
    ArrayList<ChildSmartBandHeartRateHistoryBean> childSmartBandHeartRateHistoryBeanArrayList = new ArrayList<>();
    String timeStamp;
    String timeStampNonIfa;

    Typeface helvetica;
    Typeface linoType;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    private final String  IFA_MEMBER = "IFA_MEMBER";
    private final String NON_IFA_SESSION = "NON_IFA_SESSION";
    private final String GET_LEVELS = "GET_LEVELS";

    Button ifaSession;
    Button nonIfaSession;
    ListView list;
    ScrollView scrollView;
    LinearLayout linear;

    TextView first;
    TextView second;
    TextView third;
    TextView fourth;
    TextView fifth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_heart_rate_history_smart_band_screen);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        graphView = (GraphView) findViewById(R.id.graph);
        amateur = (TextView) findViewById(R.id.amateur);
        semiPro = (TextView) findViewById(R.id.semiPro);
        professional = (TextView) findViewById(R.id.professional);
        worldClass = (TextView) findViewById(R.id.worldClass);
        legendary = (TextView) findViewById(R.id.legendary);
        backButton = (ImageView) findViewById(R.id.backButton);
        prev = (ImageView) findViewById(R.id.prev);
        next = (ImageView) findViewById(R.id.next);
        dateText = (TextView) findViewById(R.id.dateText);
        ifaSession = (Button) findViewById(R.id.ifaSession);
        nonIfaSession = (Button) findViewById(R.id.nonIfaSession);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        list = (ListView) findViewById(R.id.list);
        linear = (LinearLayout) findViewById(R.id.linear);
        prevNew = (ImageView) findViewById(R.id.prevNew);
        nextNew = (ImageView) findViewById(R.id.nextNew);
        dateTextNew = (TextView) findViewById(R.id.dateTextNew);

        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);
        fourth = findViewById(R.id.fourth);
        fifth = findViewById(R.id.fifth);


        nonIfaSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nonIfaSessionTab();
                if (Utilities.isNetworkAvailable(ChildHeartRateHistorySmartBandScreen.this)) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("required_date", timeStampNonIfa));
                    nameValuePairList.add(new BasicNameValuePair("feed_type", "1"));

                    String webServiceUrl = Utilities.BASE_URL + "children/get_heart_rate_history";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildHeartRateHistorySmartBandScreen.this, nameValuePairList, NON_IFA_SESSION, ChildHeartRateHistorySmartBandScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ifaSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifaSessionTab();
                if (Utilities.isNetworkAvailable(ChildHeartRateHistorySmartBandScreen.this)) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("required_date", timeStamp));
                    nameValuePairList.add(new BasicNameValuePair("feed_type", "2"));

                    String webServiceUrl = Utilities.BASE_URL + "children/get_heart_rate_history";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildHeartRateHistorySmartBandScreen.this, nameValuePairList, IFA_MEMBER, ChildHeartRateHistorySmartBandScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        timeStampNonIfa = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
//        System.out.println("TimeStamp:::" + timeStamp);
        dateText.setText(timeStamp);

        dateTextNew.setText(timeStampNonIfa);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    change(timeStamp, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    change(timeStamp, +1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        prevNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    changeNew(timeStampNonIfa, -1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        nextNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    changeNew(timeStampNonIfa, +1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (Utilities.isNetworkAvailable(ChildHeartRateHistorySmartBandScreen.this)) {
            String webServiceUrl = Utilities.BASE_URL + "children/get_level";
            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            if(Utilities.isNetworkAvailable(ChildHeartRateHistorySmartBandScreen.this)) {
                GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ChildHeartRateHistorySmartBandScreen.this, GET_LEVELS, ChildHeartRateHistorySmartBandScreen.this, headers);
                getWebServiceWithHeadersAsync.execute(webServiceUrl);
            }else{
                Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_LEVELS:
                if (response == null) {
                    Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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

                                if(i==0){
                                    first.setText(jsonObject.getString("name"));
                                }else if(i==1){
                                    second.setText(jsonObject.getString("name"));
                                }else if(i==2){
                                    third.setText(jsonObject.getString("name"));
                                }else if(i==3){
                                    fourth.setText(jsonObject.getString("name"));
                                }else if(i==4){
                                    fifth.setText(jsonObject.getString("name"));
                                }
                            }

                            if (Utilities.isNetworkAvailable(ChildHeartRateHistorySmartBandScreen.this)) {
                                List<NameValuePair> nameValuePairList = new ArrayList<>();
                                nameValuePairList.add(new BasicNameValuePair("required_date", timeStamp));
                                nameValuePairList.add(new BasicNameValuePair("feed_type", "2"));

                                String webServiceUrl = Utilities.BASE_URL + "children/get_heart_rate_history";

                                ArrayList<String> headers = new ArrayList<>();
                                headers.add("X-access-uid:" + loggedInUser.getId());
                                headers.add("X-access-token:" + loggedInUser.getToken());

                                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildHeartRateHistorySmartBandScreen.this, nameValuePairList, IFA_MEMBER, ChildHeartRateHistorySmartBandScreen.this, headers);
                                postWebServiceAsync.execute(webServiceUrl);
                            } else {
                                Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case IFA_MEMBER:
                childSmartBandHeartRateHistoryBeanArrayList.clear();
                if (response == null) {
                    Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if(status){
                            JSONObject data = new JSONObject(responseObject.getString("data"));
                            //String dateStr = data.getString("date");

                            JSONArray jsonArray = data.getJSONArray("value");

                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                childSmartBandHeartRateHistoryBean = new ChildSmartBandHeartRateHistoryBean();
                                childSmartBandHeartRateHistoryBean.setDate(data.getString("date"));

                                childSmartBandHeartRateHistoryBean.setMaxVal(jsonObject.getInt("max_val"));
                                childSmartBandHeartRateHistoryBean.setMinVal(jsonObject.getInt("min_val"));
                                childSmartBandHeartRateHistoryBean.setHours(jsonObject.getInt("hours"));

                                childSmartBandHeartRateHistoryBeanArrayList.add(childSmartBandHeartRateHistoryBean);
                            }
                            // set values to levels
                            heartRateLevelMatch();
                            initGraph();

                        }else{
                            Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case NON_IFA_SESSION:
                childSmartBandHeartRateHistoryBeanArrayList.clear();
                if (response == null) {
                    Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if(status){
                            JSONObject data = new JSONObject(responseObject.getString("data"));
                            //String dateStr = data.getString("date");

                            JSONArray jsonArray = data.getJSONArray("value");

                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                childSmartBandHeartRateHistoryBean = new ChildSmartBandHeartRateHistoryBean();
                                childSmartBandHeartRateHistoryBean.setDate(data.getString("date"));

                                childSmartBandHeartRateHistoryBean.setMaxVal(jsonObject.getInt("max_val"));
                                childSmartBandHeartRateHistoryBean.setMinVal(jsonObject.getInt("min_val"));
                                childSmartBandHeartRateHistoryBean.setHours(jsonObject.getInt("hours"));
                                childSmartBandHeartRateHistoryBean.setStartTime(jsonObject.getString("start_time"));
                                childSmartBandHeartRateHistoryBean.setEndTime(jsonObject.getString("end_time"));


                                for(int j=0; j<heartRateArrayList.size(); j++){
                                    final ChildSmartBandLevelsBean childSmartBandLevelsBean = heartRateArrayList.get(j);

                                    if(j==0){
                                        if(childSmartBandHeartRateHistoryBean.getMaxVal()>=childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<=childSmartBandLevelsBean.getEndVal()){
                                            childSmartBandHeartRateHistoryBean.setType("Amateur");
                                        }
                                    }else if(j==1){
                                        if(childSmartBandHeartRateHistoryBean.getMaxVal()>=childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<=childSmartBandLevelsBean.getEndVal()){
                                            childSmartBandHeartRateHistoryBean.setType("Semi-pro");
                                        }
                                    }else if(j==2){
                                        if(childSmartBandHeartRateHistoryBean.getMaxVal()>=childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<=childSmartBandLevelsBean.getEndVal()){
                                            childSmartBandHeartRateHistoryBean.setType("Professional");
                                        }
                                    }else if(j==3){
                                        if(childSmartBandHeartRateHistoryBean.getMaxVal()>childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<=childSmartBandLevelsBean.getEndVal()){
                                            childSmartBandHeartRateHistoryBean.setType("World Class");
                                        }
                                    }else if(j==4){
                                        if(childSmartBandHeartRateHistoryBean.getMaxVal()>=childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<=childSmartBandLevelsBean.getEndVal()){
                                            childSmartBandHeartRateHistoryBean.setType("Legendary");
                                        }
                                    }

                                }




                                childSmartBandHeartRateHistoryBeanArrayList.add(childSmartBandHeartRateHistoryBean);
                            }
                            // set values to levels
                            //heartRateLevelMatch();
                            //initGraph();
                            ChildHeartRateNonIFadpater childHeartRateNonIFadpater = new ChildHeartRateNonIFadpater(ChildHeartRateHistorySmartBandScreen.this,childSmartBandHeartRateHistoryBeanArrayList);
                            list.setAdapter(childHeartRateNonIFadpater);
                            childHeartRateNonIFadpater.notifyDataSetChanged();

                        }else{
                            Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

    }

    public void heartRateLevelMatch(){
        amateurValue=0;
        semiProValue=0;
        professionalValue=0;
        worldClassValue=0;
        legendaryValue=0;
        for(int i=0; i<childSmartBandHeartRateHistoryBeanArrayList.size(); i++){
            final ChildSmartBandHeartRateHistoryBean childSmartBandHeartRateHistoryBean = childSmartBandHeartRateHistoryBeanArrayList.get(i);

            for(int j=0; j<heartRateArrayList.size(); j++){
                final ChildSmartBandLevelsBean childSmartBandLevelsBean = heartRateArrayList.get(j);

                if(j==0){
                    if(childSmartBandHeartRateHistoryBean.getMaxVal()>=childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<=childSmartBandLevelsBean.getEndVal()){
                        amateurValue++;
                    }
                }else if(j==1){
                    if(childSmartBandHeartRateHistoryBean.getMaxVal()>=childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<=childSmartBandLevelsBean.getEndVal()){
                        semiProValue++;
                    }
                }else if(j==2){
                    if(childSmartBandHeartRateHistoryBean.getMaxVal()>=childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<=childSmartBandLevelsBean.getEndVal()){
                        professionalValue++;
                    }
                }else if(j==3){
                    if(childSmartBandHeartRateHistoryBean.getMaxVal()>=childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<=childSmartBandLevelsBean.getEndVal()){
                        worldClassValue++;
                    }
                }else if(j==4){
                    if(childSmartBandHeartRateHistoryBean.getMaxVal()>=childSmartBandLevelsBean.getStartVal() && childSmartBandHeartRateHistoryBean.getMaxVal()<=childSmartBandLevelsBean.getEndVal()){
                        legendaryValue++;
                    }
                }

            }
        }

        amateur.setText(amateurValue+" hrs");
        semiPro.setText(semiProValue+" hrs");
        professional.setText(professionalValue+" hrs");
        worldClass.setText(worldClassValue+" hrs");
        legendary.setText(legendaryValue+" hrs");

//        System.out.println("AmValue::"+amateurValue+"::semProValue::"+semiProValue);
    }

    public void initGraph() {

        BarGraphSeries<DataPoint> series;
        BarGraphSeries<DataPoint> series2;
        DataPoint[] arr;
        DataPoint[] arr2;

        if(childSmartBandHeartRateHistoryBeanArrayList.size()==1){
            arr = new DataPoint[2];
            arr2 = new DataPoint[2];

            arr[0] = new DataPoint(0, 0);
            arr2[0] = new DataPoint(0, 0);

            for(int i=0; i<childSmartBandHeartRateHistoryBeanArrayList.size(); i++){
                final ChildSmartBandHeartRateHistoryBean childSmartBandHeartRateHistoryBeannew = childSmartBandHeartRateHistoryBeanArrayList.get(i);

                int maxnew = childSmartBandHeartRateHistoryBeannew.getMaxVal();
                int minnew = childSmartBandHeartRateHistoryBeannew.getMinVal();
                int hournew = childSmartBandHeartRateHistoryBeannew.getHours();

                arr[i+1] = new DataPoint(hournew, maxnew);
                arr2[i+1] = new DataPoint(hournew, minnew);

            }

        }else{
            arr = new DataPoint[childSmartBandHeartRateHistoryBeanArrayList.size()];
            arr2 = new DataPoint[childSmartBandHeartRateHistoryBeanArrayList.size()];

            for(int i=0; i<childSmartBandHeartRateHistoryBeanArrayList.size(); i++){
                final ChildSmartBandHeartRateHistoryBean childSmartBandHeartRateHistoryBean = childSmartBandHeartRateHistoryBeanArrayList.get(i);

                int max = childSmartBandHeartRateHistoryBean.getMaxVal();
                int min = childSmartBandHeartRateHistoryBean.getMinVal();
                int hour = childSmartBandHeartRateHistoryBean.getHours();

                arr[i] = new DataPoint(hour, max);
                arr2[i] = new DataPoint(hour, min);

            }

        }


        series = new BarGraphSeries<>(arr2);
        series2 = new BarGraphSeries<>(arr);

//        series.setTitle("Max");
//        series.setAnimated(true);
//        series.setColor(Color.parseColor("#009fd9"));
//       // series.setDrawDataPoints(true);
//        graphView.addSeries(series);
//
//        series2.setTitle("Min");
//        series2.setAnimated(true);
//        series2.setColor(Color.parseColor("#ffe02e"));
//       // series2.setDrawDataPoints(true);
//        graphView.addSeries(series2);

        series.setTitle("Min");
        series.setAnimated(true);
        series.setColor(getResources().getColor(R.color.yellow));
        // series.setDrawDataPoints(true);
        graphView.addSeries(series);

        series2.setTitle("Max");
        series2.setAnimated(true);
        series2.setColor(Color.parseColor("#009fd9"));
        // series2.setDrawDataPoints(true);
        graphView.addSeries(series2);

        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(25);
        graphView.setBackgroundColor(Color.parseColor("#ffffff"));


    }

    public void change(String date, int days) throws Exception {
        graphView.removeAllSeries();
        childSmartBandHeartRateHistoryBeanArrayList.clear();
        amateurValue=0;
        semiProValue=0;
        professionalValue=0;
        worldClassValue=0;
        legendaryValue=0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d = df.parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, days);
        d = c.getTime();
        timeStamp = df.format(d);
//        System.out.println("inChangeMethod" + timeStamp);
        dateText.setText(timeStamp);

        if (Utilities.isNetworkAvailable(ChildHeartRateHistorySmartBandScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("required_date", timeStamp));
            nameValuePairList.add(new BasicNameValuePair("feed_type", "2"));

            String webServiceUrl = Utilities.BASE_URL + "children/get_heart_rate_history";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildHeartRateHistorySmartBandScreen.this, nameValuePairList, IFA_MEMBER, ChildHeartRateHistorySmartBandScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);
        } else {
            Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    public void changeNew(String date, int days) throws Exception {
        graphView.removeAllSeries();
        childSmartBandHeartRateHistoryBeanArrayList.clear();
        amateurValue=0;
        semiProValue=0;
        professionalValue=0;
        worldClassValue=0;
        legendaryValue=0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d = df.parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, days);
        d = c.getTime();
        timeStampNonIfa = df.format(d);
//        System.out.println("inChangeMethod" + timeStampNonIfa);
        dateTextNew.setText(timeStampNonIfa);

        if (Utilities.isNetworkAvailable(ChildHeartRateHistorySmartBandScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("required_date", timeStampNonIfa));
            nameValuePairList.add(new BasicNameValuePair("feed_type", "1"));

            String webServiceUrl = Utilities.BASE_URL + "children/get_heart_rate_history";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildHeartRateHistorySmartBandScreen.this, nameValuePairList, NON_IFA_SESSION, ChildHeartRateHistorySmartBandScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);
        } else {
            Toast.makeText(ChildHeartRateHistorySmartBandScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    public void ifaSessionTab(){
//        graphView.removeAllSeries();
//        amateur.setText("0 hrs");
//        semiPro.setText("0 hrs");
//        professional.setText("0 hrs");
//        worldClass.setText("0 hrs");
//        legendary.setText("0 hrs");
        graphView.removeAllSeries();
        childSmartBandHeartRateHistoryBeanArrayList.clear();
        ifaSession.setBackgroundColor(getResources().getColor(R.color.blue));
        nonIfaSession.setBackgroundColor(getResources().getColor(R.color.yellow));
        scrollView.setVisibility(View.VISIBLE);
        linear.setVisibility(View.GONE);
    }

    public void nonIfaSessionTab(){
        graphView.removeAllSeries();
        childSmartBandHeartRateHistoryBeanArrayList.clear();
        ifaSession.setBackgroundColor(getResources().getColor(R.color.yellow));
        nonIfaSession.setBackgroundColor(getResources().getColor(R.color.blue));
        scrollView.setVisibility(View.GONE);
        linear.setVisibility(View.VISIBLE);
    }
}
