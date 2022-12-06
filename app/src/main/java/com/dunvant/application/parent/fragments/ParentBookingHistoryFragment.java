package com.dunvant.application.parent.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.ParentBookedCampsScreen;
import com.dunvant.application.parent.ParentBookedMidWeekScreen;
import com.dunvant.application.parent.ParentBookedPitchesScreen;
import com.dunvant.application.parent.ParentBookedSessionsScreen;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ParentBookingHistoryFragment extends Fragment implements IWebServiceCallback {
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;
    private final String CUSTOM_MIDWEEK_SHOW = "CUSTOM_MIDWEEK_SHOW";

    LinearLayout bookedSessionLinearLayout;
    TextView bookedSessions;
    LinearLayout bookedPitchesLinearLayout;
    TextView bookedPitches;
    LinearLayout bookedCampsLinearLayout;
    TextView bookedCamps;
    LinearLayout midWeekLinearLayout;
    TextView midWeek;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_booking_history, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        bookedSessionLinearLayout = (LinearLayout) view.findViewById(R.id.bookedSessionLinearLayout);
        bookedSessions = (TextView) view.findViewById(R.id.bookedSessions);
        bookedPitchesLinearLayout = (LinearLayout) view.findViewById(R.id.bookedPitchesLinearLayout);
        bookedPitches = (TextView) view.findViewById(R.id.bookedPitches);
        bookedCampsLinearLayout = (LinearLayout) view.findViewById(R.id.bookedCampsLinearLayout);
        bookedCamps = (TextView) view.findViewById(R.id.bookedCamps);
        midWeekLinearLayout = view.findViewById(R.id.midWeekLinearLayout);
        midWeek = view.findViewById(R.id.midWeek);

        bookedSessionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookedSession = new Intent(getActivity(), ParentBookedSessionsScreen.class);
                startActivity(bookedSession);
            }
        });

        bookedPitchesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent bookedSession = new Intent(getActivity(), ParentBookedPitchesScreen.class);
                startActivity(bookedSession);

            }
        });

        bookedCampsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookedSession = new Intent(getActivity(), ParentBookedCampsScreen.class);
                startActivity(bookedSession);
            }
        });

        midWeekLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookedSession = new Intent(getActivity(), ParentBookedMidWeekScreen.class);
                startActivity(bookedSession);
            }
        });

        bookedSessions.setTypeface(linoType);
        bookedPitches.setTypeface(linoType);
        bookedCamps.setTypeface(linoType);
        midWeek.setTypeface(linoType);

        customNavigsation();


        return view;
    }

    private void customNavigsation() {
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("nav_type", "mobileMidweek"));
        String webServiceUrl = Utilities.BASE_URL + "account/navigations_items";

        PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(getActivity(), nameValuePairList, CUSTOM_MIDWEEK_SHOW, ParentBookingHistoryFragment.this);
        postWebServiceAsync.execute(webServiceUrl);
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {

            case CUSTOM_MIDWEEK_SHOW:

                System.out.println("response::"+response);
                if(response == null){
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status){
                            JSONObject dataobj1 = responseObject.getJSONObject("data");
                            JSONObject dataobj2 = dataobj1.getJSONObject("data");
                            JSONArray jsonArray = dataobj2.getJSONArray("main");

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if(jsonObject.getString("status").equalsIgnoreCase("1")){
                                String prefText = jsonObject.getString("preferred_text");
                                midWeek.setText(prefText.toUpperCase());
                                midWeekLinearLayout.setVisibility(View.VISIBLE);
                                bookedSessionLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.25f));
                                bookedPitchesLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.25f));
                                bookedCampsLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.25f));
                                midWeekLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.25f));
                            }else{
                                midWeekLinearLayout.setVisibility(View.GONE);
                                bookedSessionLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.33f));
                                bookedPitchesLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.33f));
                                bookedCampsLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.33f));
                                midWeekLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.1f));
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}