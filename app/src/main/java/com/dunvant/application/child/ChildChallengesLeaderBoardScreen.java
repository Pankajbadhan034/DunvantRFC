package com.dunvant.application.child;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import com.dunvant.application.beans.ChildLeaderBoardBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.adapters.ChildLeaderBoardAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildChallengesLeaderBoardScreen extends AppCompatActivity implements IWebServiceCallback {
    ChildLeaderBoardAdapter childLeaderBoardAdapter;
    ChildLeaderBoardBean childLeaderBoardBean;
    ArrayList<ChildLeaderBoardBean> childLeaderBoardBeanArrayList = new ArrayList<>();
    private final String LEADERBOARD = "LEADERBOARD";
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    TextView pushups;
    TextView coachName;
    ListView list;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    Typeface helvetica;
    Typeface linoType;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_challenges_leader_board_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        pushups = (TextView) findViewById(R.id.pushups);
        coachName = (TextView) findViewById(R.id.coachName);
        list = (ListView) findViewById(R.id.list);
        backButton = (ImageView) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageLoader.init(ImageLoaderConfiguration.createDefault(ChildChallengesLeaderBoardScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        pushups.setTypeface(helvetica);
        coachName.setTypeface(helvetica);


        String challengeId = getIntent().getStringExtra("challengeId");

        if(Utilities.isNetworkAvailable(ChildChallengesLeaderBoardScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("challenges_id", challengeId));

            String webServiceUrl = Utilities.BASE_URL + "challenges/leaderboard_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildChallengesLeaderBoardScreen.this, nameValuePairList, LEADERBOARD, ChildChallengesLeaderBoardScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ChildChallengesLeaderBoardScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case LEADERBOARD:

                if(response == null) {
                    Toast.makeText(ChildChallengesLeaderBoardScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            String responseArray=responseObject.getString("data");
                            JSONObject datObject=new JSONObject(responseArray);

                                String title = datObject.getString("title");
                                String coach_name = datObject.getString("coach_name");
                                String children_list = datObject.getString("children_list");

                                JSONArray childListArray = new JSONArray(children_list);

                                for(int j=0; j<childListArray.length(); j++){
                                    childLeaderBoardBean = new ChildLeaderBoardBean();
                                    JSONObject jsonObject = childListArray.getJSONObject(j);

                                    childLeaderBoardBean.setChallengeId(jsonObject.getString("challengers_id"));
                                    childLeaderBoardBean.setChild_id(jsonObject.getString("child_id"));
                                    childLeaderBoardBean.setChild_name(jsonObject.getString("child_name"));
                                    childLeaderBoardBean.setChild_dp_url(jsonObject.getString("child_dp_url"));
                                    childLeaderBoardBean.setScores(jsonObject.getString("scores"));
                                    childLeaderBoardBean.setTime_taken(jsonObject.getString("time_taken"));
                                    childLeaderBoardBean.setChallengersMedia(jsonObject.getString("challenge_media"));
                                    childLeaderBoardBean.setVideoThumbnail(jsonObject.getString("video_thumbnail"));

                                    childLeaderBoardBeanArrayList.add(childLeaderBoardBean);
                                    pushups.setText(title);
                                    coachName.setText(coach_name);
                                }




                            childLeaderBoardAdapter = new ChildLeaderBoardAdapter(ChildChallengesLeaderBoardScreen.this,childLeaderBoardBeanArrayList);
                            list.setAdapter(childLeaderBoardAdapter);
                            childLeaderBoardAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ChildChallengesLeaderBoardScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildChallengesLeaderBoardScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }



                break;
        }
    }
}
