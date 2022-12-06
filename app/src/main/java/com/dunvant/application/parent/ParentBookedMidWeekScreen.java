package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.SessionHistoryBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentMidWeekHistoryListingAdapter;
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

import androidx.appcompat.app.AppCompatActivity;

public class ParentBookedMidWeekScreen extends AppCompatActivity implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    ListView bookedSessionsListView;

    private final String GET_SESSION_HISTORY = "GET_SESSION_HISTORY";

    ArrayList<SessionHistoryBean> sessionHistoryList = new ArrayList<>();
    ParentMidWeekHistoryListingAdapter parentMidWeekHistoryListingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_booked_mid_week_activity);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        parentMidWeekHistoryListingAdapter = new ParentMidWeekHistoryListingAdapter(ParentBookedMidWeekScreen.this, sessionHistoryList);

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        bookedSessionsListView = (ListView) findViewById(R.id.bookedSessionsListView);
        bookedSessionsListView.setAdapter(parentMidWeekHistoryListingAdapter);

        title.setTypeface(linoType);

        getSessionHistory();

        bookedSessionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SessionHistoryBean clickedOnSession = sessionHistoryList.get(position);

                Intent historyDetail = new Intent(ParentBookedMidWeekScreen.this, ParentBookedMidWeekHistoryDetailScreen.class);
                historyDetail.putExtra("clickedOnSession", clickedOnSession);
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

    private void getSessionHistory() {
        if (Utilities.isNetworkAvailable(ParentBookedMidWeekScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("offset", "0"));

            String webServiceUrl = Utilities.BASE_URL + "midweek_session/get_booked_package";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookedMidWeekScreen.this, nameValuePairList, GET_SESSION_HISTORY, ParentBookedMidWeekScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookedMidWeekScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_SESSION_HISTORY:

                sessionHistoryList.clear();

                if (response == null) {
                    Toast.makeText(ParentBookedMidWeekScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");
                            SessionHistoryBean sessionHistoryBean;
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject sessionHistoryObject = dataArray.getJSONObject(i);
                                sessionHistoryBean = new SessionHistoryBean();

                                sessionHistoryBean.setOrderId(sessionHistoryObject.getString("id"));
                                sessionHistoryBean.setSessionName(sessionHistoryObject.getString("session_name"));
                                sessionHistoryBean.setShowTotalCost(sessionHistoryObject.getString("display_total_cost"));
                                sessionHistoryBean.setOrderDate(sessionHistoryObject.getString("order_date"));
                                sessionHistoryBean.setState(sessionHistoryObject.getString("state"));
                                sessionHistoryBean.setRefundAmount(sessionHistoryObject.getString("refund_amount"));
                                sessionHistoryBean.setDisplayRefundAmount(sessionHistoryObject.getString("display_refund_amount"));
                                sessionHistoryBean.setDisplayCustomDiscount(sessionHistoryObject.getString("display_custom_discount"));

                                JSONObject notesObject = new JSONObject(sessionHistoryObject.getString("notes"));
                                if (notesObject.has("previous_orders_id")) {

                                    JSONArray previousOrderIdsArray = notesObject.getJSONArray("previous_orders_id");
                                    sessionHistoryBean.setPreviousOrderId(previousOrderIdsArray.getString(0));

                                } else {
                                    sessionHistoryBean.setPreviousOrderId(" - ");
                                }

                                sessionHistoryList.add(sessionHistoryBean);
                            }

                        } else {
                            Toast.makeText(ParentBookedMidWeekScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookedMidWeekScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentMidWeekHistoryListingAdapter.notifyDataSetChanged();

                break;
        }
    }
}
