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
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.smartBand.adapter.ChildSportSmartBandHistoryAdapter;
import com.dunvant.application.child.smartBand.bean.ChildSmartBandSportHistoryBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildSportSmartBandHistory extends AppCompatActivity  implements IWebServiceCallback {
    String from_date="";
    SwipyRefreshLayout  mSwipyRefreshLayout;
    int offset;
    Button ifaSession;
    Button nonIfaSession;
    ListView list;
    ImageView backButton;
    ArrayList<ChildSmartBandSportHistoryBean> childSmartBandSportHistoryBeanArrayList = new ArrayList<>();
    ChildSmartBandSportHistoryBean childSmartBandSportHistoryBean;
    ChildSportSmartBandHistoryAdapter childSportSmartBandHistoryAdapter;

    Typeface helvetica;
    Typeface linoType;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    private final String  GET_HISTORY = "GET_HISTORY";
    String type = "ifa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_sport_smart_band_history);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        list = (ListView) findViewById(R.id.list);
        backButton = (ImageView) findViewById(R.id.backButton);
        ifaSession = (Button) findViewById(R.id.ifaSession);
        nonIfaSession = (Button) findViewById(R.id.nonIfaSession);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        ifaSession.setTypeface(linoType);
        nonIfaSession.setTypeface(linoType);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ifaSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childSmartBandSportHistoryBeanArrayList.clear();
                ifaButton();

                if (Utilities.isNetworkAvailable(ChildSportSmartBandHistory.this)) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("synced_by", "2"));
                    nameValuePairList.add(new BasicNameValuePair("feed_type", "2"));
                    nameValuePairList.add(new BasicNameValuePair("offset", "0"));
                    nameValuePairList.add(new BasicNameValuePair("date_range_type", "5"));
                    nameValuePairList.add(new BasicNameValuePair("from_date", ""));

                    String webServiceUrl = Utilities.BASE_URL + "children/track_workout_list";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildSportSmartBandHistory.this, nameValuePairList, GET_HISTORY, ChildSportSmartBandHistory.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(ChildSportSmartBandHistory.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        nonIfaSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childSmartBandSportHistoryBeanArrayList.clear();
                nonIfaButton();
                offset = 0;
                histoyDataNonIFA();
            }
        });

        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(type.equalsIgnoreCase("nonifa")){
                    offset += 1;
                    histoyDataNonIFA();
                }else{
//                    mSwipyRefreshLayout.setRefreshing(false);
                    histoyDataIFA();
                }
            }
        });

        childSmartBandSportHistoryBeanArrayList.clear();
        offset = 0;
        histoyDataIFA();

        childSportSmartBandHistoryAdapter = new ChildSportSmartBandHistoryAdapter(ChildSportSmartBandHistory.this,childSmartBandSportHistoryBeanArrayList);
        list.setAdapter(childSportSmartBandHistoryAdapter);

    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        if (response == null) {
            Toast.makeText(ChildSportSmartBandHistory.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject responseObject = new JSONObject(response);
                boolean status = responseObject.getBoolean("status");
                String message = responseObject.getString("message");
                if(status){
                    JSONArray data = responseObject.getJSONArray("data");
                    for(int i=0; i<data.length(); i++){
                        JSONObject jsonObject = data.getJSONObject(i);
                        childSmartBandSportHistoryBean = new ChildSmartBandSportHistoryBean();
                        childSmartBandSportHistoryBean.setType(type);
                        childSmartBandSportHistoryBean.setId(jsonObject.getString("id"));
                        childSmartBandSportHistoryBean.setUserId(jsonObject.getString("users_id"));
                        childSmartBandSportHistoryBean.setDuration(jsonObject.getString("duration_formatted"));
                        childSmartBandSportHistoryBean.setDistance(jsonObject.getString("distance"));
                        childSmartBandSportHistoryBean.setSpeed(jsonObject.getString("speed"));
                        childSmartBandSportHistoryBean.setStartTime(jsonObject.getString("start_time"));
                        childSmartBandSportHistoryBean.setEndTime(jsonObject.getString("end_time"));
                        childSmartBandSportHistoryBean.setSteps(jsonObject.getString("steps"));
                        childSmartBandSportHistoryBean.setCalories(jsonObject.getString("calories"));
                        childSmartBandSportHistoryBean.setActivity(jsonObject.getString("activity"));
                        childSmartBandSportHistoryBean.setSportType(jsonObject.getString("sport_type"));
                        childSmartBandSportHistoryBean.setState(jsonObject.getString("state"));
                        childSmartBandSportHistoryBean.setCreatedAt(jsonObject.getString("created_at"));
                        childSmartBandSportHistoryBean.setSyncedBy(jsonObject.getString("synced_by"));
                        childSmartBandSportHistoryBean.setCreatedAtFormatted(jsonObject.getString("created_at_formatted"));
                        childSmartBandSportHistoryBean.setDurationFormatted(jsonObject.getString("duration_formatted"));
                        childSmartBandSportHistoryBean.setDistanceFormatted(jsonObject.getString("distance_formatted"));
                        childSmartBandSportHistoryBean.setCaloriesFormatted(jsonObject.getString("calories_formatted"));
                        childSmartBandSportHistoryBean.setIsShared(jsonObject.getString("is_shared"));

                        from_date = jsonObject.getString("created_at");


                        childSmartBandSportHistoryBeanArrayList.add(childSmartBandSportHistoryBean);

                    }
                    mSwipyRefreshLayout.setRefreshing(false);
                    childSportSmartBandHistoryAdapter.notifyDataSetChanged();
                }else{
                    mSwipyRefreshLayout.setRefreshing(false);
                    Toast.makeText(ChildSportSmartBandHistory.this, message, Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                mSwipyRefreshLayout.setRefreshing(false);
                Toast.makeText(ChildSportSmartBandHistory.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void ifaButton(){
        childSmartBandSportHistoryBeanArrayList.clear();
        type = "ifa";
        ifaSession.setBackgroundColor(getResources().getColor(R.color.yellow));
        nonIfaSession.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ifaSession.setTextColor(Color.parseColor("#333333"));
        nonIfaSession.setTextColor(Color.parseColor("#FFFFFF"));
        childSportSmartBandHistoryAdapter = new ChildSportSmartBandHistoryAdapter(ChildSportSmartBandHistory.this,childSmartBandSportHistoryBeanArrayList);
        list.setAdapter(childSportSmartBandHistoryAdapter);
    }

    public void nonIfaButton(){
        childSmartBandSportHistoryBeanArrayList.clear();
        type = "nonifa";
        ifaSession.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        nonIfaSession.setBackgroundColor(getResources().getColor(R.color.yellow));
        ifaSession.setTextColor(Color.parseColor("#FFFFFF"));
        nonIfaSession.setTextColor(Color.parseColor("#333333"));
        childSportSmartBandHistoryAdapter = new ChildSportSmartBandHistoryAdapter(ChildSportSmartBandHistory.this,childSmartBandSportHistoryBeanArrayList);
        list.setAdapter(childSportSmartBandHistoryAdapter);
    }

    public void histoyDataIFA(){
        if (Utilities.isNetworkAvailable(ChildSportSmartBandHistory.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("synced_by", "2"));
            nameValuePairList.add(new BasicNameValuePair("feed_type", "2"));
            nameValuePairList.add(new BasicNameValuePair("offset", "0"));
            nameValuePairList.add(new BasicNameValuePair("date_range_type", "5"));
            nameValuePairList.add(new BasicNameValuePair("from_date", from_date));
            String webServiceUrl = Utilities.BASE_URL + "children/track_workout_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildSportSmartBandHistory.this, nameValuePairList, GET_HISTORY, ChildSportSmartBandHistory.this, headers);
            postWebServiceAsync.execute(webServiceUrl);
        } else {
            Toast.makeText(ChildSportSmartBandHistory.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    public void histoyDataNonIFA(){
        if (Utilities.isNetworkAvailable(ChildSportSmartBandHistory.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("synced_by", "2"));
            nameValuePairList.add(new BasicNameValuePair("feed_type", "1"));
            nameValuePairList.add(new BasicNameValuePair("offset", "" + offset));
            nameValuePairList.add(new BasicNameValuePair("date_range_type", "0"));
            String webServiceUrl = Utilities.BASE_URL + "children/track_workout_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildSportSmartBandHistory.this, nameValuePairList, GET_HISTORY, ChildSportSmartBandHistory.this, headers);
            postWebServiceAsync.execute(webServiceUrl);
        } else {
            Toast.makeText(ChildSportSmartBandHistory.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

}
