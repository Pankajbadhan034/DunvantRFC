package com.dunvant.application.coach;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.HorizontalListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachTeamDetailsBean;
import com.dunvant.application.beans.TeamDetailStatsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachTeamDetailHorizontalAdapter;
import com.dunvant.application.coach.adapters.CoachTeamDetailsAdatper;
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

public class CoachTeamDetailScreen extends AppCompatActivity implements IWebServiceCallback {
    String teamRankStr="";
    ArrayList<TeamDetailStatsBean> teamDetailStatsBeanArrayList = new ArrayList<>();
    ArrayList<CoachTeamDetailsBean> coachTeamDetailsBeanArrayList = new ArrayList<>();
    private final String TEAM_DETAIL = "TEAM_DETAIL";
    Typeface helvetica;
    Typeface linoType;
    ImageView backButton;
    TextView title;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    String academyIdStr;
    String leagueIdStr;
    String teamNameStr;
    String teamidStr;

    TextView leaguePosition;
    ImageView image;
    HorizontalListView horListView;
    TextView description;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_team_detail_screen);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        leaguePosition = findViewById(R.id.leaguePosition);
        image = findViewById(R.id.image);
        horListView = findViewById(R.id.horListView);
        description = findViewById(R.id.description);
        listView = findViewById(R.id.listView);


        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(CoachTeamDetailScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(1000))
                .build();


        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

        academyIdStr = getIntent().getStringExtra("academy_id");
        leagueIdStr = getIntent().getStringExtra("league_id");
        teamNameStr = getIntent().getStringExtra("teamName");
        teamidStr = getIntent().getStringExtra("team_id");
        title.setText(teamNameStr);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getTeamDetails();

    }

    private void getTeamDetails() {
        if (Utilities.isNetworkAvailable(CoachTeamDetailScreen.this)){

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", academyIdStr));
            nameValuePairList.add(new BasicNameValuePair("league_id", leagueIdStr));
            nameValuePairList.add(new BasicNameValuePair("team_id", teamidStr));

            String webServiceUrl = Utilities.BASE_URL + "league_tournament/team_detail";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            System.out.println("uid::" + loggedInUser.getId() + "token::" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachTeamDetailScreen.this, nameValuePairList, TEAM_DETAIL, CoachTeamDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else{
            Toast.makeText(CoachTeamDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {


            case TEAM_DETAIL:

                if (response == null) {
                    Toast.makeText(CoachTeamDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status){
                            coachTeamDetailsBeanArrayList.clear();
                            teamDetailStatsBeanArrayList.clear();
                            JSONObject data = responseObject.getJSONObject("data");
                            teamRankStr = data.getString("team_rank");
                            JSONArray tableDataArray = data.getJSONArray("player_detail");
                            for(int i=0; i<tableDataArray.length(); i++){
                                JSONObject jsonObject = tableDataArray.getJSONObject(i);
                                CoachTeamDetailsBean coachTeamDetailsBean = new CoachTeamDetailsBean();
                                coachTeamDetailsBean.setId(jsonObject.getString("id"));
                                coachTeamDetailsBean.setAcademy_id(jsonObject.getString("academy_id"));
                                coachTeamDetailsBean.setTeam_id(jsonObject.getString("team_id"));
                                coachTeamDetailsBean.setMatch_id(jsonObject.getString("match_id"));
                                coachTeamDetailsBean.setPlayer_id(jsonObject.getString("player_id"));
                                coachTeamDetailsBean.setLeague_id(jsonObject.getString("league_id"));
                                coachTeamDetailsBean.setSeason_id(jsonObject.getString("season_id"));
                                coachTeamDetailsBean.setState(jsonObject.getString("state"));
                                coachTeamDetailsBean.setPlayer_name(jsonObject.getString("player_name"));
                                coachTeamDetailsBean.setPosition(jsonObject.getString("position"));
                                coachTeamDetailsBean.setGoals(jsonObject.getString("goals"));
                                coachTeamDetailsBean.setMatches(jsonObject.getString("matches"));
                                coachTeamDetailsBean.setPlayer_image(jsonObject.getString("player_image"));

                                coachTeamDetailsBeanArrayList.add(coachTeamDetailsBean);
                            }

                            JSONArray teamDetailArr = data.getJSONArray("team_detail");
                            String imageStr = null;
                            String nameStr;
                            String descriptionStr = null;
                            for(int i=0; i<teamDetailArr.length(); i++){
                                JSONObject jsonObject = teamDetailArr.getJSONObject(i);
                                imageStr = jsonObject.getString("image_url")+""+jsonObject.getString("file_name");
                                nameStr = jsonObject.getString("name");
                                descriptionStr = jsonObject.getString("description");
                            }


                            JSONArray teamStatArr = data.getJSONArray("team_stat");
                            for(int i=0; i<teamStatArr.length(); i++){
                                JSONObject jsonObject = teamStatArr.getJSONObject(i);
                                TeamDetailStatsBean teamDetailStatsBean = new TeamDetailStatsBean();
                                teamDetailStatsBean.setHeading(jsonObject.getString("heading"));
                                teamDetailStatsBean.setValue(jsonObject.getString("value"));
                                teamDetailStatsBeanArrayList.add(teamDetailStatsBean);
                            }


                            leaguePosition.setText("LEAGUE POSITION: "+teamRankStr);
                            imageLoader.displayImage(imageStr, image, options);
                            description.setText(Html.fromHtml(descriptionStr));


                            CoachTeamDetailsAdatper coachTeamDetailsAdatper = new CoachTeamDetailsAdatper(CoachTeamDetailScreen.this, coachTeamDetailsBeanArrayList);
                            listView.setAdapter(coachTeamDetailsAdatper);


                            CoachTeamDetailHorizontalAdapter coachTeamDetailHorizontalAdapter = new CoachTeamDetailHorizontalAdapter(CoachTeamDetailScreen.this, teamDetailStatsBeanArrayList);
                            horListView.setAdapter(coachTeamDetailHorizontalAdapter);




                        }  else {
                            Toast.makeText(CoachTeamDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachTeamDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;


        }
    }

}