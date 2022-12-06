package com.dunvant.application.coach;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AcceptedChallengeChildBean;
import com.dunvant.application.beans.ChallengeListBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachAcceptedChallengeAdapter;
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

public class CoachAcceptedMembersScreen extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    ImageView backButton;
    ListView membersListView;

    ChallengeListBean clickedOnChallenge;

    private final String GET_MEMBERS_LISTING = "GET_MEMBERS_LISTING";
    ArrayList<AcceptedChallengeChildBean> membersListing = new ArrayList<>();

    CoachAcceptedChallengeAdapter coachAcceptedChallengeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_activity_accepted_members_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        backButton = (ImageView) findViewById(R.id.backButton);
        membersListView = (ListView) findViewById(R.id.membersListView);
        coachAcceptedChallengeAdapter = new CoachAcceptedChallengeAdapter(CoachAcceptedMembersScreen.this, membersListing, CoachAcceptedMembersScreen.this);
        membersListView.setAdapter(coachAcceptedChallengeAdapter);

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnChallenge = (ChallengeListBean) intent.getSerializableExtra("clickedOnChallenge");
            getMembersList();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getMembersList(){
        if(Utilities.isNetworkAvailable(CoachAcceptedMembersScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("challenges_id", clickedOnChallenge.getChallengeId()));

            String webServiceUrl = Utilities.BASE_URL + "coach/accepted_challenge_by_children_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachAcceptedMembersScreen.this, nameValuePairList, GET_MEMBERS_LISTING, CoachAcceptedMembersScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachAcceptedMembersScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_MEMBERS_LISTING:

                membersListing.clear();

                if(response == null) {
                    Toast.makeText(CoachAcceptedMembersScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");
                            AcceptedChallengeChildBean childBean;
                            for(int i=0;i<dataArray.length();i++){
                                JSONObject childObject = dataArray.getJSONObject(i);
                                childBean = new AcceptedChallengeChildBean();
                                childBean.setChallengersId(childObject.getString("challengers_id"));
                                childBean.setChildId(childObject.getString("child_id"));
                                childBean.setChildName(childObject.getString("child_name"));
                                childBean.setShowCreatedAt(childObject.getString("created_at_formatted"));
                                childBean.setShowAcceptedDate(childObject.getString("accepted_date_formatted"));
                                childBean.setShowAcceptedTime(childObject.getString("accepted_time_formatted"));
                                childBean.setApprovalStatus(childObject.getString("approval_status"));
                                childBean.setAchievedScore(childObject.getString("achieved_score"));
                                childBean.setAchievedTime(childObject.getString("achieved_time"));

                                membersListing.add(childBean);
                            }
                        } else {
                            Toast.makeText(CoachAcceptedMembersScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachAcceptedMembersScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachAcceptedChallengeAdapter.notifyDataSetChanged();
                break;
        }
    }
}