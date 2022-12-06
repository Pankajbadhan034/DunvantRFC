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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChallengeBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.ParentChallengeDetailScreen;
import com.dunvant.application.parent.adapters.ParentActiveChallengesAdapter;
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

public class ParentActiveChallengesFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    private final String GET_ACTIVE_CHALLENGES = "GET_ACTIVE_CHALLENGES";

    TextView active;
    ListView activeChallengesListView;
    ParentActiveChallengesAdapter parentActiveChallengesAdapter;
    ArrayList<ChallengeBean> challengesList = new ArrayList<>();

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

        parentActiveChallengesAdapter = new ParentActiveChallengesAdapter(getActivity(), challengesList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_active_challenges, container, false);

        active = (TextView) view.findViewById(R.id.active);
        activeChallengesListView = (ListView) view.findViewById(R.id.activeChallengesListView);
        activeChallengesListView.setAdapter(parentActiveChallengesAdapter);

        active.setTypeface(linoType);

        getActiveChallengesListing();

        activeChallengesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChallengeBean clickedOnChallenge = challengesList.get(position);
                Intent challengeDetail = new Intent(getActivity(), ParentChallengeDetailScreen.class);
                challengeDetail.putExtra("clickedOnChallenge", clickedOnChallenge);
                startActivity(challengeDetail);
            }
        });

        return view;
    }

    private void getActiveChallengesListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("parent_id", loggedInUser.getId()));
            nameValuePairList.add(new BasicNameValuePair("status", "1"));

            String webServiceUrl = Utilities.BASE_URL + "challenges/list_for_parent";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_ACTIVE_CHALLENGES, ParentActiveChallengesFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_ACTIVE_CHALLENGES:

                challengesList.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            ChallengeBean challengeBean;
                            for (int i=0;i<dataArray.length();i++) {
                                JSONObject challengeObject = dataArray.getJSONObject(i);
                                challengeBean = new ChallengeBean();

                                challengeBean.setChildId(challengeObject.getString("child_id"));
                                challengeBean.setChildName(challengeObject.getString("child_name"));
//                                challengeBean.setChildImageUrl(challengeObject.getString("child_image_url"));
                                challengeBean.setChallengeId(challengeObject.getString("challenge_id"));
//                                challengeBean.setChallengeImageUrl(challengeObject.getString("challenge_image_url"));
//                                challengeBean.setChallengeVideoUrl(challengeObject.getString("challenge_video_url"));
                                challengeBean.setChallengeTitle(challengeObject.getString("challenge_title"));
                                challengeBean.setChallengeDescription(challengeObject.getString("challenge_description"));
                                challengeBean.setOwnerId(challengeObject.getString("owner_id"));
                                challengeBean.setCoachName(challengeObject.getString("coach_name"));
                                challengeBean.setCategoryId(challengeObject.getString("category_id"));
                                challengeBean.setCategoryName(challengeObject.getString("category_name"));
                                challengeBean.setChallengersId(challengeObject.getString("challengers_id"));
                                challengeBean.setExpiration(challengeObject.getString("expiration"));
                                challengeBean.setShowExpiration(challengeObject.getString("expiration_formatted"));
                                challengeBean.setTargetScore(challengeObject.getString("target_score"));
                                challengeBean.setTargetTime(challengeObject.getString("target_time"));
                                challengeBean.setTargetTimeType(challengeObject.getString("target_time_type"));
                                challengeBean.setApprovalRequired(challengeObject.getString("approval_required"));
                                challengeBean.setAchievedScore(challengeObject.getString("achieved_score"));
                                challengeBean.setAchievedTime(challengeObject.getString("achieved_time"));
                                challengeBean.setChallengeResult(challengeObject.getString("challenge_result"));
                                challengeBean.setApprovedStatus(challengeObject.getString("approved_status"));

                                challengeBean.setExpirationTimeFormatted(challengeObject.getString("expiration_time_formatted"));
                                challengeBean.setTargetTimeTypeFormatted(challengeObject.getString("target_time_type_formatted"));
                                challengeBean.setChallengeResultFormatted(challengeObject.getString("challenge_result_formatted"));

                                challengesList.add(challengeBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentActiveChallengesAdapter.notifyDataSetChanged();

                break;
        }
    }
}