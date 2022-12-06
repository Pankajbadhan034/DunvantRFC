package com.dunvant.application.coach;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AcceptedChallengeChildBean;
import com.dunvant.application.beans.ChallengeListBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachLeaderBoardAdapter;
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

public class CoachLeaderboardScreen extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    ImageView backButton;
    TextView challengeTitle;
    TextView coachName;
    ListView leaderboardListView;
    Button back;

    ChallengeListBean clickedOnChallenge;

    private final String GET_LEADERBOARD_DATA = "GET_LEADERBOARD_DATA";

    String strTitle, strCoachName;

    ArrayList<AcceptedChallengeChildBean> childrenListing = new ArrayList<>();

    CoachLeaderBoardAdapter coachLeaderBoardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_activity_leaderboard_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        backButton = (ImageView) findViewById(R.id.backButton);
        challengeTitle = (TextView) findViewById(R.id.challengeTitle);
        coachName = (TextView) findViewById(R.id.coachName);
        leaderboardListView = (ListView) findViewById(R.id.leaderboardListView);
        back = (Button) findViewById(R.id.back);

        coachLeaderBoardAdapter = new CoachLeaderBoardAdapter(CoachLeaderboardScreen.this, childrenListing);
        leaderboardListView.setAdapter(coachLeaderBoardAdapter);

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnChallenge = (ChallengeListBean) intent.getSerializableExtra("clickedOnChallenge");
            getLeaderBoardData();
        }


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton.performClick();
            }
        });
    }

    private void getLeaderBoardData(){
        if(Utilities.isNetworkAvailable(CoachLeaderboardScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("challenges_id", clickedOnChallenge.getChallengeId()));

            String webServiceUrl = Utilities.BASE_URL + "challenges/leaderboard_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachLeaderboardScreen.this, nameValuePairList, GET_LEADERBOARD_DATA, CoachLeaderboardScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachLeaderboardScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_LEADERBOARD_DATA:

                childrenListing.clear();

                if(response == null) {
                    Toast.makeText(CoachLeaderboardScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            JSONObject dataObject = responseObject.getJSONObject("data");
                            strTitle = dataObject.getString("title");
                            strCoachName = dataObject.getString("coach_name");

                            challengeTitle.setText(strTitle);
                            coachName.setText(strCoachName);

                            JSONArray childrenListArray = dataObject.getJSONArray("children_list");
                            AcceptedChallengeChildBean childBean;
                            for(int i=0; i<childrenListArray.length(); i++) {
                                JSONObject childObject = childrenListArray.getJSONObject(i);
                                childBean = new AcceptedChallengeChildBean();
                                childBean.setChallengersId(childObject.getString("challengers_id"));
                                childBean.setChildId(childObject.getString("child_id"));
                                childBean.setChildName(childObject.getString("child_name"));
                                childBean.setChildDpUrl(childObject.getString("child_dp_url"));
                                childBean.setScores(childObject.getString("scores"));
                                childBean.setTimeTaken(childObject.getString("time_taken"));
                                childBean.setChallengersMedia(childObject.getString("challenge_media"));
                                childBean.setVideoThumbnail(childObject.getString("video_thumbnail"));
                                childrenListing.add(childBean);
                            }
                        } else {
                            Toast.makeText(CoachLeaderboardScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachLeaderboardScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachLeaderBoardAdapter.notifyDataSetChanged();
                break;
        }
    }
}