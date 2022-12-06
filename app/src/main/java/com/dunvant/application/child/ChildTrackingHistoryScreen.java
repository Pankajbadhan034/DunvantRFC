package com.dunvant.application.child;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.TrackingHistoryBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.adapters.ChildTrackingHistoryAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChildTrackingHistoryScreen extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    ImageView backButton;
    ListView historyListView;

    private final String GET_HISTORY_LISTING = "GET_HISTORY_LISTING";

    ArrayList<TrackingHistoryBean> trackingHistoryList = new ArrayList<>();

    ChildTrackingHistoryAdapter childTrackingHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_tracking_history_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        backButton = (ImageView) findViewById(R.id.backButton);
        historyListView = (ListView) findViewById(R.id.historyListView);

        childTrackingHistoryAdapter = new ChildTrackingHistoryAdapter(ChildTrackingHistoryScreen.this, trackingHistoryList);
        historyListView.setAdapter(childTrackingHistoryAdapter);

        getHistoryListing();

        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrackingHistoryBean clickedOnHistory = trackingHistoryList.get(position);

                Intent historyDetail = new Intent(ChildTrackingHistoryScreen.this, ChildTrackingHistoryDetailScreen.class);
                historyDetail.putExtra("clickedOnHistory", clickedOnHistory);
                startActivity(historyDetail);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getHistoryListing(){
        if(Utilities.isNetworkAvailable(ChildTrackingHistoryScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "children/track_workout_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ChildTrackingHistoryScreen.this, GET_HISTORY_LISTING, ChildTrackingHistoryScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ChildTrackingHistoryScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_HISTORY_LISTING:

                trackingHistoryList.clear();

                if(response == null) {
                    Toast.makeText(ChildTrackingHistoryScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            TrackingHistoryBean historyBean;
                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject historyObject = dataArray.getJSONObject(i);
                                historyBean = new TrackingHistoryBean();

                                historyBean.setId(historyObject.getString("id"));
                                historyBean.setUsersId(historyObject.getString("users_id"));
                                historyBean.setDuration(historyObject.getString("duration"));
                                historyBean.setDistance(historyObject.getString("distance"));
                                historyBean.setSpeed(historyObject.getString("speed"));
                                historyBean.setCalories(historyObject.getString("calories"));
                                historyBean.setActivity(historyObject.getString("activity"));
                                historyBean.setState(historyObject.getString("state"));
                                historyBean.setCreatedAt(historyObject.getString("created_at"));
                                historyBean.setShowCreatedAt(historyObject.getString("created_at_formatted"));
                                historyBean.setShowDuration(historyObject.getString("duration_formatted"));
                                historyBean.setShowDistance(historyObject.getString("distance_formatted"));
                                historyBean.setShowCalories(historyObject.getString("calories_formatted"));
                                historyBean.setIsShared(historyObject.getString("is_shared"));

                                trackingHistoryList.add(historyBean);
                            }

                        } else {
                            Toast.makeText(ChildTrackingHistoryScreen.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildTrackingHistoryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                childTrackingHistoryAdapter.notifyDataSetChanged();
                break;
        }
    }
}