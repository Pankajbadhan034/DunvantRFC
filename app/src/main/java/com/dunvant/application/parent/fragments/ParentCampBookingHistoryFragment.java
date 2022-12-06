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
import com.dunvant.application.beans.CampOrderBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.ParentCampBookingHistoryDetailScreen;
import com.dunvant.application.parent.adapters.ParentCampBookingHistoryAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParentCampBookingHistoryFragment extends Fragment implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ListView campBookingHistoryListView;
    ParentCampBookingHistoryAdapter parentCampBookingHistoryAdapter;

    private final String GET_CAMP_ORDER_HISTORY = "GET_CAMP_ORDER_HISTORY";

    ArrayList<CampOrderBean> bookedOrdersListing = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        parentCampBookingHistoryAdapter = new ParentCampBookingHistoryAdapter(getActivity(), bookedOrdersListing);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_camp_booking_history, container, false);

        campBookingHistoryListView = (ListView) view.findViewById(R.id.campBookingHistoryListView);
        campBookingHistoryListView.setAdapter(parentCampBookingHistoryAdapter);

        getCampBookingHistory();

        campBookingHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CampOrderBean clickedOnCampOrder = bookedOrdersListing.get(position);

                Intent detailScreen = new Intent(getActivity(), ParentCampBookingHistoryDetailScreen.class);
                detailScreen.putExtra("clickedOnCampOrder", clickedOnCampOrder);
                startActivity(detailScreen);
            }
        });

        return view;
    }

    private void getCampBookingHistory() {
        if (Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "camps/booking_history";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_CAMP_ORDER_HISTORY, ParentCampBookingHistoryFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_CAMP_ORDER_HISTORY:

                bookedOrdersListing.clear();

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            CampOrderBean orderBean;

                            if (dataArray.length() == 0) {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject orderObject = dataArray.getJSONObject(i);
                                orderBean = new CampOrderBean();

                                orderBean.setOrderId(orderObject.getString("orders_id"));
                                orderBean.setCampId(orderObject.getString("camps_id"));
                                orderBean.setCampName(orderObject.getString("camp_name"));
                                orderBean.setCampFromDate(orderObject.getString("camp_from_date"));
                                orderBean.setCampToDate(orderObject.getString("camp_to_date"));
                                orderBean.setLocationId(orderObject.getString("locations_id"));
                                orderBean.setLocationName(orderObject.getString("location_name"));
                                orderBean.setOrderDate(orderObject.getString("order_date"));
                                orderBean.setShowOrderDate(orderObject.getString("order_date_formatted"));
                                orderBean.setNetAmount(orderObject.getString("net_amount"));
                                orderBean.setCampSessionId(orderObject.getString("camp_sessions_id"));
                                orderBean.setFromTime(orderObject.getString("from_time"));
                                orderBean.setToTime(orderObject.getString("to_time"));
                                orderBean.setShowFromTime(orderObject.getString("from_time_formatted"));
                                orderBean.setShowToTime(orderObject.getString("to_time_formatted"));
                                orderBean.setChildName(orderObject.getString("childname"));
                                orderBean.setTotalBookedDays(orderObject.getString("total_booked_days"));
                                orderBean.setBookingDates(orderObject.getString("booking_dates"));
                                orderBean.setShowBookingDates(orderObject.getString("booking_dates_formatted"));
                                orderBean.setGroupName(orderObject.getString("groups_name"));

                                bookedOrdersListing.add(orderBean);
                            }
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                parentCampBookingHistoryAdapter.notifyDataSetChanged();

                break;
        }
    }
}