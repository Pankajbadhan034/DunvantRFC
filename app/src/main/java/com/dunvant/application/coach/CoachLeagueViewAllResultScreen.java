package com.dunvant.application.coach;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachLeagueDetailOneResultMatchBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachLeagueDetailOneResultAdapter;
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

import androidx.appcompat.app.AppCompatActivity;

public class CoachLeagueViewAllResultScreen extends AppCompatActivity implements IWebServiceCallback {
    ArrayList<CoachLeagueDetailOneResultMatchBean> coachLeagueDetailOneFixtureBeanArrayList = new ArrayList<>();
    String academyIdStr;
    String leagueIdStr;
    String nameStr;
    ImageView backButton;
    Typeface helvetica;
    Typeface linoType;
    TextView title;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    private final String RESULT_DETAIL = "RESULT_DETAIL";
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    ListView listView;
    String tabIdStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_league_view_all_result_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        listView = findViewById(R.id.listView);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(CoachLeagueViewAllResultScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

        academyIdStr = getIntent().getStringExtra("academy_id");
        leagueIdStr = getIntent().getStringExtra("league_id");
        nameStr = getIntent().getStringExtra("name");
        tabIdStr = getIntent().getStringExtra("tab");
        title.setText(nameStr);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getLeaugeNames();
        changeFonts();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent obj = new Intent(CoachLeagueViewAllResultScreen.this, CoachMatchResultDetailScreen.class);
                obj.putExtra("matchId", coachLeagueDetailOneFixtureBeanArrayList.get(i).getMatchId());
                obj.putExtra("academy_id", academyIdStr);
                obj.putExtra("tab", tabIdStr);
                startActivity(obj);
            }
        });
    }

    private void changeFonts() {
        title.setTypeface(linoType);

    }

    private void getLeaugeNames() {
        if (Utilities.isNetworkAvailable(CoachLeagueViewAllResultScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", academyIdStr));
            nameValuePairList.add(new BasicNameValuePair("league_id", leagueIdStr));
            nameValuePairList.add(new BasicNameValuePair("type", "Result"));

            String webServiceUrl = Utilities.BASE_URL + "league_tournament/league_all_matches";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            System.out.println("uid::" + loggedInUser.getId() + "token::" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachLeagueViewAllResultScreen.this, nameValuePairList, RESULT_DETAIL, CoachLeagueViewAllResultScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachLeagueViewAllResultScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {


            case RESULT_DETAIL:

                if (response == null) {
                    Toast.makeText(CoachLeagueViewAllResultScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray fixtureMatchArray = responseObject.getJSONArray("data");
                            for(int i=0; i<fixtureMatchArray.length(); i++){
                                JSONObject jsonObject = fixtureMatchArray.getJSONObject(i);
                                CoachLeagueDetailOneResultMatchBean coachLeagueDetailOneFixtureBean = new CoachLeagueDetailOneResultMatchBean();
                                coachLeagueDetailOneFixtureBean.setImageUrl(jsonObject.getString("image_url"));
                                coachLeagueDetailOneFixtureBean.setMatchId(jsonObject.getString("match_id"));
                                coachLeagueDetailOneFixtureBean.setTeam1(jsonObject.getString("team_1"));
                                coachLeagueDetailOneFixtureBean.setTeam1Name(jsonObject.getString("team_1_name"));
                                coachLeagueDetailOneFixtureBean.setTeam1Logo(jsonObject.getString("team_1_logo"));
                                coachLeagueDetailOneFixtureBean.setTeam2(jsonObject.getString("team_2"));
                                coachLeagueDetailOneFixtureBean.setTeam2Name(jsonObject.getString("team_2_name"));
                                coachLeagueDetailOneFixtureBean.setTeam2Logo(jsonObject.getString("team_2_logo"));
                                coachLeagueDetailOneFixtureBean.setMatchDate(jsonObject.getString("match_date"));
                                coachLeagueDetailOneFixtureBean.setMatchTime(jsonObject.getString("match_time"));
                                coachLeagueDetailOneFixtureBean.setTeam1Score(jsonObject.getString("team_1_score"));
                                coachLeagueDetailOneFixtureBean.setTeam2Score(jsonObject.getString("team_2_score"));
                                coachLeagueDetailOneFixtureBeanArrayList.add(coachLeagueDetailOneFixtureBean);
                            }
//                            CoachLeagueDetailOneFixtureAdapter coachLeagueDetailOneFixtureAdapter = new CoachLeagueDetailOneFixtureAdapter(CoachLeagueViewAllResultScreen.this, coachLeagueDetailOneFixtureBeanArrayList);
//                            listView.setAdapter(coachLeagueDetailOneFixtureAdapter);

                            CoachLeagueDetailOneResultAdapter coachLeagueDetailOneFixtureAdapter = new CoachLeagueDetailOneResultAdapter(CoachLeagueViewAllResultScreen.this, coachLeagueDetailOneFixtureBeanArrayList);
                            listView.setAdapter(coachLeagueDetailOneFixtureAdapter);
                            Utilities.setListViewHeightBasedOnChildren(listView);
                        } else {
                            Toast.makeText(CoachLeagueViewAllResultScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachLeagueViewAllResultScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;


        }
    }
}