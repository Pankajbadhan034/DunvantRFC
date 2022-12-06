package com.dunvant.application.child.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ChildActiveChellngesFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    private final String GET_ACTIVE_CHALLENGES = "GET_ACTIVE_CHALLENGES";

    TextView active;
    ListView activeChallengesListView;
    ChildMyChallengesAdapter childMyChallengesAdapter;
    ArrayList<ChildMyChallengesBean> challengesList = new ArrayList<>();

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

        childMyChallengesAdapter = new ChildMyChallengesAdapter(getActivity(), challengesList, "Active");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_active_challenges, container, false);

        active = (TextView) view.findViewById(R.id.active);
        activeChallengesListView = (ListView) view.findViewById(R.id.activeChallengesListView);
        activeChallengesListView.setAdapter(childMyChallengesAdapter);

        active.setTypeface(linoType);

        getActiveChallengesListing();

//        activeChallengesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                ChildMyChallengesBean clickedOnChallenge = challengesList.get(position);
////                Intent challengeDetail = new Intent(getActivity(), ParentChallengeDetailScreen.class);
////                challengeDetail.putExtra("clickedOnChallenge", clickedOnChallenge);
////                startActivity(challengeDetail);
//            }
//        });

        return view;
    }

    private void getActiveChallengesListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();

            String webServiceUrl = Utilities.BASE_URL + "challenges/list_for_child";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_ACTIVE_CHALLENGES, ChildActiveChellngesFragment.this, headers);
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
                            ChildMyChallengesBean childMyChallengesBean;

                            JSONObject dataObj = new JSONObject(responseObject.getString("data"));
                            JSONArray active_challengeArray = new JSONArray(dataObj.getString("active_challenge"));

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

                                challengesList.add(childMyChallengesBean);
                            }

                            childMyChallengesAdapter = new ChildMyChallengesAdapter(getActivity(), challengesList, "Active");
                            activeChallengesListView.setAdapter(childMyChallengesAdapter);
                            childMyChallengesAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                childMyChallengesAdapter.notifyDataSetChanged();

                break;
        }
    }
}