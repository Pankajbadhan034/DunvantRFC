package com.dunvant.application.parent.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.SessionHistoryBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.ParentBookedSessionHistoryDetailScreen;
import com.dunvant.application.parent.adapters.ParentSessionHistoryListingAdapter;
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

public class ParentBookedSessionsHistoryFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ListView bookedSessionsListView;

    private final String GET_SESSION_HISTORY = "GET_SESSION_HISTORY";

    ArrayList<SessionHistoryBean> sessionHistoryList = new ArrayList<>();
    ParentSessionHistoryListingAdapter parentSessionHistoryListingAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        parentSessionHistoryListingAdapter = new ParentSessionHistoryListingAdapter(getActivity(), sessionHistoryList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_booked_sessions_history, container, false);

        bookedSessionsListView = (ListView) view.findViewById(R.id.bookedSessionsListView);
        bookedSessionsListView.setAdapter(parentSessionHistoryListingAdapter);

        getSessionHistory();

        bookedSessionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SessionHistoryBean clickedOnSession = sessionHistoryList.get(position);

                Intent historyDetail = new Intent(getActivity(), ParentBookedSessionHistoryDetailScreen.class);
                historyDetail.putExtra("clickedOnSession", clickedOnSession);
                startActivity(historyDetail);
            }
        });

        return view;
    }

    private void getSessionHistory(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("offset", "0"));

            String webServiceUrl = Utilities.BASE_URL + "sessions/get_booked_session";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_SESSION_HISTORY, ParentBookedSessionsHistoryFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_SESSION_HISTORY:

                sessionHistoryList.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");
                            SessionHistoryBean sessionHistoryBean;
                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject sessionHistoryObject = dataArray.getJSONObject(i);
                                sessionHistoryBean = new SessionHistoryBean();

                                sessionHistoryBean.setOrderId(sessionHistoryObject.getString("id"));
                                sessionHistoryBean.setSessionName(sessionHistoryObject.getString("session_name"));
                                sessionHistoryBean.setShowTotalCost(sessionHistoryObject.getString("display_total_cost"));
                                sessionHistoryBean.setOrderDate(sessionHistoryObject.getString("order_date"));
                                sessionHistoryBean.setState(sessionHistoryObject.getString("state"));

                                sessionHistoryList.add(sessionHistoryBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    }catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentSessionHistoryListingAdapter.notifyDataSetChanged();

                break;
        }
    }
}