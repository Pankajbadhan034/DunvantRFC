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
import com.dunvant.application.parent.ParentAcademyListingWithFiltersScreen;
import com.dunvant.application.parent.ParentBookFacilityNewViewScreen;
import com.dunvant.application.parent.ParentBookPitchChooseGameScreen;
import com.dunvant.application.parent.ParentCampsListingScreen;
import com.dunvant.application.parent.ParentMidWeekListingScreen;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceAsync;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ParentBookNowFragment extends Fragment implements IWebServiceCallback {
    String viewStr;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;
    private final String GET_PITCH_LAYOUT = "GET_PITCH_LAYOUT";
    private final String CUSTOM_MIDWEEK_SHOW = "CUSTOM_MIDWEEK_SHOW";

    LinearLayout bookSessionLinearLayout;
    TextView bookSessions;
    LinearLayout bookPitchLinearLayout;
    TextView bookPitch;
    LinearLayout bookCampLinearLayout;
    TextView bookCamp;

    LinearLayout midWeekLinearLayout;
    TextView midWeek;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_book_now, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        bookSessionLinearLayout = (LinearLayout) view.findViewById(R.id.bookSessionLinearLayout);
        bookSessions = (TextView) view.findViewById(R.id.bookSessions);
        bookPitchLinearLayout = (LinearLayout) view.findViewById(R.id.bookPitchLinearLayout);
        bookPitch = (TextView) view.findViewById(R.id.bookPitch);
        bookCampLinearLayout = (LinearLayout) view.findViewById(R.id.bookCampLinearLayout);
        bookCamp = (TextView) view.findViewById(R.id.bookCamp);
        midWeekLinearLayout = (LinearLayout) view.findViewById(R.id.midWeekLinearLayout);
        midWeek = (TextView) view.findViewById(R.id.midWeek);

        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        bookSessionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ParentAcademyListingScreen.class);
                Intent intent = new Intent(getActivity(), ParentAcademyListingWithFiltersScreen.class);
                startActivity(intent);
            }
        });

        bookPitchLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewStr.equalsIgnoreCase("1")) {
                    Intent intent = new Intent(getActivity(), ParentBookPitchChooseGameScreen.class);
                    startActivity(intent);
                } else if (viewStr.equalsIgnoreCase("2")) {
                    Intent bookedSession = new Intent(getActivity(), ParentBookFacilityNewViewScreen.class);
                    startActivity(bookedSession);
                }
            }
        });

        bookCampLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ParentCampsListingScreen.class);
                startActivity(intent);
            }
        });

        midWeekLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ParentMidWeekListingScreen.class);
                startActivity(intent);
            }
        });

        bookSessions.setTypeface(linoType);
        bookPitch.setTypeface(linoType);
        bookCamp.setTypeface(linoType);

        customNavigsation();

        return view;
    }
    private void getPitchLayout(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));

            String webServiceUrl = Utilities.BASE_URL + "front_calendar/pitch_layout";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            System.out.println("X-access-uid: "+loggedInUser.getId() +"X-access-token: "+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_PITCH_LAYOUT, ParentBookNowFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void customNavigsation() {
        List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("nav_type", "mobileMidweek"));
        String webServiceUrl = Utilities.BASE_URL + "account/navigations_items";

        PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(getActivity(), nameValuePairList, CUSTOM_MIDWEEK_SHOW, ParentBookNowFragment.this);
        postWebServiceAsync.execute(webServiceUrl);
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_PITCH_LAYOUT:

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONObject dataObj = responseObject.getJSONObject("data");
                            viewStr = dataObj.getString("pitch_layout");
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    //    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

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
                                    bookSessionLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.25f));
                                    bookPitchLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.25f));
                                    bookCampLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.25f));
                                    midWeekLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.25f));
                                }else{
                                    midWeekLinearLayout.setVisibility(View.GONE);
                                    bookSessionLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.33f));
                                    bookPitchLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.33f));
                                    bookCampLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.33f));
                                    midWeekLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.1f));
                                }

                            getPitchLayout();

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