package com.dunvant.application.child.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildMyChallengesBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.adapters.ChildMyChallengesAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildMyChallengesFragment extends Fragment implements IWebServiceCallback {
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    LinearLayout activeClick;
    LinearLayout achievedClick;
    LinearLayout expiredClick;
    TextView chalangeStatus;
    ListView listView;
    TextView text;
    ArrayList<ChildMyChallengesBean> childMyChallengesBeanArrayList = new ArrayList<>();
    boolean activeBln = true;
    boolean achievedBln = false;
    boolean expriedBln = false;
    ChildMyChallengesAdapter childMyChallengesAdapter;
    private final String MY_CHALLENGES = "MY_CHALLENGES";
    Typeface helvetica;
    Typeface linoType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_fragment_my_challenges, container, false);
        listView = (ListView) view.findViewById(R.id.challengeList);
        text = (TextView) view.findViewById(R.id.text);
        activeClick = (LinearLayout) view.findViewById(R.id.activeClick);
        achievedClick = (LinearLayout) view.findViewById(R.id.achievedClick);
        expiredClick = (LinearLayout) view.findViewById(R.id.expiredClick);
        chalangeStatus = (TextView) view.findViewById(R.id.chalangeStatus);

        chalangeStatus.setTypeface(helvetica);
        text.setTypeface(helvetica);

       // chalangeAsyncCall();

        activeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeClickTab();
            }
        });

        achievedClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                achievedClickTab();
            }
        });

        expiredClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expiredClickTab();
            }
        });

        return view;
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case MY_CHALLENGES:
                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            ChildMyChallengesBean childMyChallengesBean;
                            if(activeBln){
                                childMyChallengesBeanArrayList.clear();
                                JSONObject dataObj = new JSONObject(responseObject.getString("data"));
                                JSONArray active_challengeArray = new JSONArray(dataObj.getString("active_challenge"));
                                if(active_challengeArray.length()==0||active_challengeArray==null){
                                   noDataRecieved();
                                }else {
                                    dataRecieved();
                                    for (int i = 0; i < active_challengeArray.length(); i++) {
                                        JSONObject chalangeData = active_challengeArray.getJSONObject(i);
                                        childMyChallengesBean = new ChildMyChallengesBean();
                                        childMyChallengesBean.setTypeChalange("active");
                                        childMyChallengesBean.setChallengesId(chalangeData.getString("challenge_id"));
                                        childMyChallengesBean.setCategoryId(chalangeData.getString("category_id"));
                                        childMyChallengesBean.setTitle(chalangeData.getString("challenge_title"));
                                        childMyChallengesBean.setDescription(chalangeData.getString("challenge_description"));
                                        childMyChallengesBean.setCoachId(chalangeData.getString("owner_id"));
                                        childMyChallengesBean.setCoachName(chalangeData.getString("coach_name"));
                                        childMyChallengesBean.setCategoryName(chalangeData.getString("category_name"));
                                        childMyChallengesBean.setCoachDp(chalangeData.getString("coach_dp"));
                                        childMyChallengesBean.setChalangesImageUrl(chalangeData.getString("challenge_image_url"));
                                        childMyChallengesBean.setChalangesVideoUrl(chalangeData.getString("challenge_video_url"));
                                        childMyChallengesBean.setTargetScore(chalangeData.getString("target_score"));
                                        childMyChallengesBean.setTargetTime(chalangeData.getString("target_time"));
                                        childMyChallengesBean.setTaregetTimeType(chalangeData.getString("target_time_type"));
                                        childMyChallengesBean.setExpirationStr(chalangeData.getString("expiration"));
                                        childMyChallengesBean.setExpirationFormatted(chalangeData.getString("expiration_formatted"));
                                        childMyChallengesBean.setAcceptedDateFormatted(chalangeData.getString("accepted_date_formatted"));
                                        childMyChallengesBean.setAchievedResult(chalangeData.getString("challenge_result"));
                                        childMyChallengesBean.setAchievedScore(chalangeData.getString("achieved_score"));
                                        childMyChallengesBean.setAchievedTime(chalangeData.getString("achieved_time"));
                                        childMyChallengesBean.setAchievedDate(chalangeData.getString("achieved_date"));
                                        childMyChallengesBean.setAchievedDateFormatted(chalangeData.getString("achieved_date_formatted"));
                                        childMyChallengesBean.setTargetTimeTypeFormatted(chalangeData.getString("target_time_type_formatted"));
                                        childMyChallengesBean.setAchievedTimeFormatted(chalangeData.getString("achieved_time_formatted"));
                                        childMyChallengesBean.setIsShared(chalangeData.getString("is_shared"));
                                        childMyChallengesBean.setIsChallengeExpired(chalangeData.getString("is_challenge_expired"));

                                        childMyChallengesBeanArrayList.add(childMyChallengesBean);
                                    }


                                }
                                childMyChallengesAdapter = new ChildMyChallengesAdapter(getActivity(), childMyChallengesBeanArrayList, "Active");
                                listView.setAdapter(childMyChallengesAdapter);
                                childMyChallengesAdapter.notifyDataSetChanged();
                            }else if(achievedBln){
                                childMyChallengesBeanArrayList.clear();
                                JSONObject dataObj = new JSONObject(responseObject.getString("data"));
                                JSONArray achieved_challengeArray = new JSONArray(dataObj.getString("achieved_challenge"));
                                if(achieved_challengeArray.length()==0||achieved_challengeArray==null){
                                    noDataRecieved();
                                }else {
                                    dataRecieved();
                                    for (int i = 0; i < achieved_challengeArray.length(); i++) {
                                        JSONObject chalangeData = achieved_challengeArray.getJSONObject(i);
                                        childMyChallengesBean = new ChildMyChallengesBean();
                                        childMyChallengesBean.setTypeChalange("achieved");
                                        childMyChallengesBean.setChallengesId(chalangeData.getString("challenge_id"));
                                        childMyChallengesBean.setCategoryId(chalangeData.getString("category_id"));
                                        childMyChallengesBean.setTitle(chalangeData.getString("challenge_title"));
                                        childMyChallengesBean.setDescription(chalangeData.getString("challenge_description"));
                                        childMyChallengesBean.setCoachId(chalangeData.getString("owner_id"));
                                        childMyChallengesBean.setCoachName(chalangeData.getString("coach_name"));
                                        childMyChallengesBean.setCategoryName(chalangeData.getString("category_name"));
                                        childMyChallengesBean.setCoachDp(chalangeData.getString("coach_dp"));
                                        childMyChallengesBean.setChalangesImageUrl(chalangeData.getString("challenge_image_url"));
                                        childMyChallengesBean.setChalangesVideoUrl(chalangeData.getString("challenge_video_url"));
                                        childMyChallengesBean.setTargetScore(chalangeData.getString("target_score"));
                                        childMyChallengesBean.setTargetTime(chalangeData.getString("target_time"));
                                        childMyChallengesBean.setTaregetTimeType(chalangeData.getString("target_time_type"));
                                        childMyChallengesBean.setExpirationStr(chalangeData.getString("expiration"));
                                        childMyChallengesBean.setExpirationFormatted(chalangeData.getString("expiration_formatted"));
                                        childMyChallengesBean.setAcceptedDateFormatted(chalangeData.getString("accepted_date_formatted"));
                                        childMyChallengesBean.setAchievedResult(chalangeData.getString("challenge_result"));
                                        childMyChallengesBean.setAchievedScore(chalangeData.getString("achieved_score"));
                                        childMyChallengesBean.setAchievedTime(chalangeData.getString("achieved_time"));
                                        childMyChallengesBean.setAchievedDate(chalangeData.getString("achieved_date"));
                                        childMyChallengesBean.setAchievedDateFormatted(chalangeData.getString("achieved_date_formatted"));
                                        childMyChallengesBean.setTargetTimeTypeFormatted(chalangeData.getString("target_time_type_formatted"));
                                        childMyChallengesBean.setAchievedTimeFormatted(chalangeData.getString("achieved_time_formatted"));
                                        childMyChallengesBean.setIsShared(chalangeData.getString("is_shared"));
                                        childMyChallengesBean.setIsChallengeExpired(chalangeData.getString("is_challenge_expired"));

                                        childMyChallengesBeanArrayList.add(childMyChallengesBean);
                                    }


                                }
                                childMyChallengesAdapter = new ChildMyChallengesAdapter(getActivity(), childMyChallengesBeanArrayList, "Achieved");
                                listView.setAdapter(childMyChallengesAdapter);
                                childMyChallengesAdapter.notifyDataSetChanged();
                            }else if(expriedBln) {
                                childMyChallengesBeanArrayList.clear();
                                JSONObject dataObj = new JSONObject(responseObject.getString("data"));
                                JSONArray expired_challengeArray = new JSONArray(dataObj.getString("expired_challenge"));
                                if (expired_challengeArray.length() == 0 || expired_challengeArray == null) {
                                    noDataRecieved();
                                } else {
                                    dataRecieved();
                                    for (int i = 0; i < expired_challengeArray.length(); i++) {
                                        JSONObject chalangeData = expired_challengeArray.getJSONObject(i);
                                        childMyChallengesBean = new ChildMyChallengesBean();
                                        childMyChallengesBean.setTypeChalange("expired");
                                        childMyChallengesBean.setChallengesId(chalangeData.getString("challenge_id"));
                                        childMyChallengesBean.setCategoryId(chalangeData.getString("category_id"));
                                        childMyChallengesBean.setTitle(chalangeData.getString("challenge_title"));
                                        childMyChallengesBean.setDescription(chalangeData.getString("challenge_description"));
                                        childMyChallengesBean.setCoachId(chalangeData.getString("owner_id"));
                                        childMyChallengesBean.setCoachName(chalangeData.getString("coach_name"));
                                        childMyChallengesBean.setCategoryName(chalangeData.getString("category_name"));
                                        childMyChallengesBean.setCoachDp(chalangeData.getString("coach_dp"));
                                        childMyChallengesBean.setChalangesImageUrl(chalangeData.getString("challenge_image_url"));
                                        childMyChallengesBean.setChalangesVideoUrl(chalangeData.getString("challenge_video_url"));
                                        childMyChallengesBean.setTargetScore(chalangeData.getString("target_score"));
                                        childMyChallengesBean.setTargetTime(chalangeData.getString("target_time"));
                                        childMyChallengesBean.setTaregetTimeType(chalangeData.getString("target_time_type"));
                                        childMyChallengesBean.setExpirationStr(chalangeData.getString("expiration"));
                                        childMyChallengesBean.setExpirationFormatted(chalangeData.getString("expiration_formatted"));
                                        childMyChallengesBean.setAcceptedDateFormatted(chalangeData.getString("accepted_date_formatted"));
                                        childMyChallengesBean.setAchievedResult(chalangeData.getString("challenge_result"));
                                        childMyChallengesBean.setAchievedScore(chalangeData.getString("achieved_score"));
                                        childMyChallengesBean.setAchievedTime(chalangeData.getString("achieved_time"));
                                        childMyChallengesBean.setAchievedDate(chalangeData.getString("achieved_date"));
                                        childMyChallengesBean.setAchievedDateFormatted(chalangeData.getString("achieved_date_formatted"));
                                        childMyChallengesBean.setTargetTimeTypeFormatted(chalangeData.getString("target_time_type_formatted"));
                                        childMyChallengesBean.setAchievedTimeFormatted(chalangeData.getString("achieved_time_formatted"));
                                        childMyChallengesBean.setIsShared(chalangeData.getString("is_shared"));
                                        childMyChallengesBean.setIsChallengeExpired(chalangeData.getString("is_challenge_expired"));

                                        childMyChallengesBeanArrayList.add(childMyChallengesBean);
                                    }


                                }
                                childMyChallengesAdapter = new ChildMyChallengesAdapter(getActivity(), childMyChallengesBeanArrayList, "Expired");
                                listView.setAdapter(childMyChallengesAdapter);
                                childMyChallengesAdapter.notifyDataSetChanged();
                            }

                        } else {
                            noDataRecieved();
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

    public void activeClickTab(){
        text.setText("No Active Challenge");
        achievedClick.setBackgroundColor(Color.parseColor("#dbdbdb"));
        activeClick.setBackgroundColor(getActivity().getResources().getColor(R.color.yellow));
        expiredClick.setBackgroundColor(Color.parseColor("#dbdbdb"));
        activeBln = true;
        achievedBln = false;
        expriedBln = false;
        chalangeStatus.setText("Active");
        chalangeAsyncCall();
    }
    public void achievedClickTab(){
        text.setText("No Achieved Challenge");
        achievedClick.setBackgroundColor(getActivity().getResources().getColor(R.color.yellow));
        activeClick.setBackgroundColor(Color.parseColor("#dbdbdb"));
        expiredClick.setBackgroundColor(Color.parseColor("#dbdbdb"));
        activeBln = false;
        achievedBln = true;
        expriedBln = false;
        chalangeStatus.setText("Achieved");
        chalangeAsyncCall();
    }
    public void expiredClickTab(){
        text.setText("No Expired Challenge");
        achievedClick.setBackgroundColor(Color.parseColor("#dbdbdb"));
        activeClick.setBackgroundColor(Color.parseColor("#dbdbdb"));
        expiredClick.setBackgroundColor(getActivity().getResources().getColor(R.color.yellow));
        activeBln = false;
        achievedBln = false;
        expriedBln = true;
        chalangeStatus.setText("Expired");
        chalangeAsyncCall();
    }

    public void dataRecieved(){
        text.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    public void noDataRecieved(){
        text.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }

    public  void chalangeAsyncCall(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();

            String webServiceUrl = Utilities.BASE_URL + "challenges/list_for_child";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, MY_CHALLENGES, ChildMyChallengesFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);
        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        chalangeAsyncCall();
    }
}
