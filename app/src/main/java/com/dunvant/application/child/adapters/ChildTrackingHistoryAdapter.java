package com.dunvant.application.child.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.TrackingHistoryBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildTrackingHistoryAdapter extends BaseAdapter implements IWebServiceCallback{

    Context context;
    ArrayList<TrackingHistoryBean> trackingHistoryListing;
    LayoutInflater layoutInflater;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    private final String SHARE_WORKOUT = "SHARE_WORKOUT";

    public ChildTrackingHistoryAdapter(Context context, ArrayList<TrackingHistoryBean> trackingHistoryListing) {
        this.context = context;
        this.trackingHistoryListing = trackingHistoryListing;
        layoutInflater = LayoutInflater.from(context);

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
    }

    @Override
    public int getCount() {
        return trackingHistoryListing.size();
    }

    @Override
    public Object getItem(int position) {
        return trackingHistoryListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_tracking_history_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView activity = (TextView) convertView.findViewById(R.id.activity);
        TextView data = (TextView) convertView.findViewById(R.id.data);
        ImageView share = (ImageView) convertView.findViewById(R.id.share);

        final TrackingHistoryBean historyBean = trackingHistoryListing.get(position);

        name.setText(historyBean.getShowCreatedAt());
        activity.setText(historyBean.getActivity());
        data.setText(historyBean.getShowDistance()+" | "+historyBean.getShowDuration()+" | "+historyBean.getShowCalories());

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utilities.isNetworkAvailable(context)) {

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("workout_id", historyBean.getId()));

                    String webServiceUrl = Utilities.BASE_URL + "user_posts/share_track_workout";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, SHARE_WORKOUT, ChildTrackingHistoryAdapter.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case SHARE_WORKOUT:

                if(response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        String message = responseObject.getString("message");

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}