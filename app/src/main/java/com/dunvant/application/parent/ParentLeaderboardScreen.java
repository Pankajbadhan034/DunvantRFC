package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChallengeBean;
import com.dunvant.application.beans.LeaderboardChildBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentLeaderboardAdapter;
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

public class ParentLeaderboardScreen extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    TextView coachName;
    ListView leaderboardListView;

    final String GET_LEADERBOARD_DATA = "GET_LEADERBOARD_DATA";

    ChallengeBean clickedOnChallenge;

    ArrayList<LeaderboardChildBean> leaderboardChildrenListing = new ArrayList<>();
    ParentLeaderboardAdapter parentLeaderboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_leaderboard_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        coachName = (TextView) findViewById(R.id.coachName);
        leaderboardListView = (ListView) findViewById(R.id.leaderboardListView);

        parentLeaderboardAdapter = new ParentLeaderboardAdapter(ParentLeaderboardScreen.this, leaderboardChildrenListing);
        leaderboardListView.setAdapter(parentLeaderboardAdapter);

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnChallenge = (ChallengeBean) intent.getSerializableExtra("clickedOnChallenge");

            title.setText(clickedOnChallenge.getChallengeTitle());
            coachName.setText(clickedOnChallenge.getCoachName());

            getLeaderboardData();
        }

        title.setTypeface(linoType);
        coachName.setTypeface(helvetica);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getLeaderboardData(){
        if(Utilities.isNetworkAvailable(ParentLeaderboardScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("challenges_id", clickedOnChallenge.getChallengeId()));

            String webServiceUrl = Utilities.BASE_URL + "challenges/leaderboard_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentLeaderboardScreen.this, nameValuePairList, GET_LEADERBOARD_DATA, ParentLeaderboardScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentLeaderboardScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_LEADERBOARD_DATA:

                leaderboardChildrenListing.clear();

                if(response == null) {
                    Toast.makeText(ParentLeaderboardScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            JSONObject dataObject = responseObject.getJSONObject("data");

                            JSONArray childrenArray = dataObject.getJSONArray("children_list");
                            LeaderboardChildBean childBean;
                            for(int i=0;i<childrenArray.length();i++){
                                JSONObject childObject = childrenArray.getJSONObject(i);
                                childBean = new LeaderboardChildBean();

                                childBean.setChallengersId(childObject.getString("challengers_id"));
                                childBean.setChildId(childObject.getString("child_id"));
                                childBean.setChildName(childObject.getString("child_name"));
                                childBean.setChildDpUrl(childObject.getString("child_dp_url"));
                                childBean.setScores(childObject.getString("scores"));
                                childBean.setTimeTaken(childObject.getString("time_taken"));
                                childBean.setChallengersMedia(childObject.getString("challenge_media"));
                                childBean.setVideoThumbnail(childObject.getString("video_thumbnail"));

                                leaderboardChildrenListing.add(childBean);
                            }

                        } else {
                            Toast.makeText(ParentLeaderboardScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentLeaderboardScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentLeaderboardAdapter.notifyDataSetChanged();

                break;
        }
    }
}