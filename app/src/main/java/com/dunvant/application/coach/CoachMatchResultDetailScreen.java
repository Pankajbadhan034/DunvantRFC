package com.dunvant.application.coach;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.HorizontalListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachLeagueDetailOneFixtureBean;
import com.dunvant.application.beans.CoachMatchResultDetailBean;
import com.dunvant.application.beans.TableHeadingBean;
import com.dunvant.application.beans.TeamDetailBean;
import com.dunvant.application.beans.TeamStatsBean;
import com.dunvant.application.beans.TeamTotalStatsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachMatchResultDetailTeamStatsAdapter;
import com.dunvant.application.coach.adapters.CoachResultPlayerListiingAdapter;
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

public class CoachMatchResultDetailScreen extends AppCompatActivity implements IWebServiceCallback {
    ArrayList<CoachMatchResultDetailBean> coachMatchResultDetailBeanArrayList = new ArrayList<>();
    ArrayList<TableHeadingBean> tableHeadingBeanArrayList = new ArrayList<>();
    ArrayList<TeamDetailBean> teamDetail1BeanArrayList = new ArrayList<>();
    ArrayList<TeamDetailBean> teamDetail2BeanArrayList = new ArrayList<>();
    ArrayList<TeamTotalStatsBean> team1TotalStatsBeanArrayList = new ArrayList<>();
    ArrayList<TeamTotalStatsBean> team2TotalStatsBeanArrayList = new ArrayList<>();

    ArrayList<CoachLeagueDetailOneFixtureBean> coachLeagueDetailOneFixtureBeanArrayList = new ArrayList<>();
    String academyIdStr;
    String matchId;
    String tabStr;
    //String nameStr;
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
    TextView match;
    TextView date;
    TextView location;
    TextView score1;
    TextView score2;
    ImageView image1;
    ImageView image2;
    Button team1Tab;
    Button team2Tab;
    HorizontalListView horListView1;
    HorizontalListView horListView2;
    LinearLayout scoreLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_match_result_detail_activity);

        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        listView = findViewById(R.id.listView);
        match = findViewById(R.id.match);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        score1 = findViewById(R.id.score1);
        score2 = findViewById(R.id.score2);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        team1Tab = findViewById(R.id.team1Tab);
        team2Tab = findViewById(R.id.team2Tab);
        horListView1 = findViewById(R.id.horListView1);
        horListView2 = findViewById(R.id.horListView2);
        scoreLayout = findViewById(R.id.scoreLayout);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(CoachMatchResultDetailScreen.this));
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
        matchId = getIntent().getStringExtra("matchId");
        tabStr = getIntent().getStringExtra("tab");

        if(tabStr.equalsIgnoreCase("1")){
            scoreLayout.setVisibility(View.GONE);
            horListView1.setVisibility(View.GONE);
            title.setText("MATCH DETAIL");
        }else{
            scoreLayout.setVisibility(View.VISIBLE);
            horListView1.setVisibility(View.VISIBLE);
            title.setText("MATCH RESULT");
        }

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
                Intent obj = new Intent(CoachMatchResultDetailScreen.this, CoachMatchResultDetailScreen.class);
                startActivity(obj);
            }
        });

        team1Tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horListView1.setVisibility(View.VISIBLE);
                horListView2.setVisibility(View.GONE);
                listView.setAdapter(null);
                team1Tab.setBackgroundColor(getResources().getColor(R.color.yellow));
                team1Tab.setTextColor(getResources().getColor(R.color.black));
                team2Tab.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                team2Tab.setTextColor(getResources().getColor(R.color.white));
                playerListing1();
                teamStats1();
            }
        });

        team2Tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horListView1.setVisibility(View.GONE);
                horListView2.setVisibility(View.VISIBLE);
                listView.setAdapter(null);
                team2Tab.setBackgroundColor(getResources().getColor(R.color.yellow));
                team2Tab.setTextColor(getResources().getColor(R.color.black));
                team1Tab.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                team1Tab.setTextColor(getResources().getColor(R.color.white));
                playerListing2();
                teamStats2();
            }
        });
    }
    private void changeFonts() {
        title.setTypeface(linoType);

    }

    private void getLeaugeNames() {
        if (Utilities.isNetworkAvailable(CoachMatchResultDetailScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", academyIdStr));
            nameValuePairList.add(new BasicNameValuePair("match_id", matchId));

            String webServiceUrl = Utilities.BASE_URL + "league_tournament/match_result";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            System.out.println("uid::" + loggedInUser.getId() + "token::" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachMatchResultDetailScreen.this, nameValuePairList, RESULT_DETAIL, CoachMatchResultDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachMatchResultDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {


            case RESULT_DETAIL:

                if (response == null) {
                    Toast.makeText(CoachMatchResultDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        CoachMatchResultDetailBean coachMatchResultDetailBean = new CoachMatchResultDetailBean();

                        if (status) {
                            JSONObject data = responseObject.getJSONObject("data");
                            JSONObject resultData = data.getJSONObject("result");
                            coachMatchResultDetailBean.setImageUrl(resultData.getString("image_url"));
                            coachMatchResultDetailBean.setMatchId(resultData.getString("match_id"));
                            coachMatchResultDetailBean.setTeam1(resultData.getString("team_1"));
                            coachMatchResultDetailBean.setTeam1Name(resultData.getString("team_1_name"));
                            coachMatchResultDetailBean.setTeam1Logo(resultData.getString("team_1_logo"));
                            coachMatchResultDetailBean.setTeam1Score(resultData.getString("team_1_score"));
                            coachMatchResultDetailBean.setTeam2(resultData.getString("team_2"));
                            coachMatchResultDetailBean.setTeam2Name(resultData.getString("team_2_name"));
                            coachMatchResultDetailBean.setTeam2Logo(resultData.getString("team_2_logo"));
                            coachMatchResultDetailBean.setTeam2Score(resultData.getString("team_2_score"));
                            coachMatchResultDetailBean.setMatchDate(resultData.getString("match_date"));
                            coachMatchResultDetailBean.setMatchTime(resultData.getString("match_time"));
                            coachMatchResultDetailBean.setAddress(resultData.getString("address"));
                            coachMatchResultDetailBean.setLeaqueId(resultData.getString("league_id"));

                            // table heading
                            try{
                                JSONArray tableHeadingArray = data.getJSONArray("table_heading");

                                tableHeadingBeanArrayList.clear();
                                for(int i=0; i<tableHeadingArray.length(); i++){
                                    JSONObject jsonObject = tableHeadingArray.getJSONObject(i);
                                    TableHeadingBean tableHeadingBean = new TableHeadingBean();
                                    tableHeadingBean.setId(jsonObject.getString("id"));
                                    tableHeadingBean.setLabel(jsonObject.getString("label"));
                                    tableHeadingBean.setIconName(jsonObject.getString("icon_name"));
                                    tableHeadingBean.setIconType(jsonObject.getString("icon_type"));
                                    tableHeadingBean.setColor(jsonObject.getString("color"));
                                    tableHeadingBeanArrayList.add(tableHeadingBean);
                                }
                                coachMatchResultDetailBean.setTableHeadingBeanArrayList(tableHeadingBeanArrayList);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            // team1
                            try{
                                JSONObject team1Obj = data.getJSONObject("team_1");
                                JSONArray team1Array = team1Obj.getJSONArray("stats");
                                teamDetail1BeanArrayList.clear();
                                for(int i=0; i<team1Array.length(); i++){
                                    JSONObject jsonObject = team1Array.getJSONObject(i);
                                    TeamDetailBean teamDetailBean = new TeamDetailBean();
                                    teamDetailBean.setTeamId(jsonObject.getString("team_id"));
                                    teamDetailBean.setMatchId(jsonObject.getString("match_id"));
                                    teamDetailBean.setPlayerId(jsonObject.getString("player_id"));
                                    teamDetailBean.setName(jsonObject.getString("name"));
                                    teamDetailBean.setImageUrl(jsonObject.getString("image_url"));
                                    teamDetailBean.setImage(jsonObject.getString("image"));



                                    JSONArray statsArray = jsonObject.getJSONArray("stats");
                                    ArrayList<TeamStatsBean> teamStatsBeanArrayList = new ArrayList<>();
                                    for(int j=0; j<statsArray.length(); j++){
                                        JSONObject jsonObject1 = statsArray.getJSONObject(j);
                                        TeamStatsBean teamStatsBean = new TeamStatsBean();
                                        teamStatsBean.setLabel(jsonObject1.getString("label"));
                                        teamStatsBean.setIcon_name(jsonObject1.getString("icon_name"));
                                        teamStatsBean.setIcon_type(jsonObject1.getString("icon_type"));
                                        teamStatsBean.setColor(jsonObject1.getString("color"));
                                        teamStatsBean.setImage_url(jsonObject1.getString("image_url"));
                                        teamStatsBean.setId(jsonObject1.getString("id"));
                                        teamStatsBean.setValue(jsonObject1.getString("value"));
                                        teamStatsBeanArrayList.add(teamStatsBean);

                                    }
                                    teamDetailBean.setTeamStatsBeanArrayList(teamStatsBeanArrayList);

                                    teamDetail1BeanArrayList.add(teamDetailBean);
                                }
                                coachMatchResultDetailBean.setTeamDetail1BeanArrayList(teamDetail1BeanArrayList);

                                JSONArray totalStasTeam1 = team1Obj.getJSONArray("totalStats");
                                TeamTotalStatsBean teamTotalStatsBean = null;
                                for(int j=0; j<totalStasTeam1.length(); j++){
                                    JSONObject jsonObject = totalStasTeam1.getJSONObject(j);
                                    teamTotalStatsBean = new TeamTotalStatsBean();
                                    teamTotalStatsBean.setValue(jsonObject.getString("value"));
                                    teamTotalStatsBean.setLabel(jsonObject.getString("label"));
                                    teamTotalStatsBean.setIcon_name(jsonObject.getString("icon_name"));
                                    teamTotalStatsBean.setIcon_type(jsonObject.getString("icon_type"));
                                    teamTotalStatsBean.setColor(jsonObject.getString("color"));
                                    teamTotalStatsBean.setImage_url(jsonObject.getString("image_url"));
                                    team1TotalStatsBeanArrayList.add(teamTotalStatsBean);
                                }


                                coachMatchResultDetailBean.setTeam1TotalStatsBeanArrayList(team1TotalStatsBeanArrayList);



                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            // team2
                            try{
                                JSONObject team2Obj = data.getJSONObject("team_2");
                                JSONArray team2Array = team2Obj.getJSONArray("stats");
                                teamDetail2BeanArrayList.clear();
                                for(int i=0; i<team2Array.length(); i++){
                                    JSONObject jsonObject = team2Array.getJSONObject(i);
                                    TeamDetailBean teamDetailBean = new TeamDetailBean();
                                    teamDetailBean.setTeamId(jsonObject.getString("team_id"));
                                    teamDetailBean.setMatchId(jsonObject.getString("match_id"));
                                    teamDetailBean.setPlayerId(jsonObject.getString("player_id"));
                                    teamDetailBean.setName(jsonObject.getString("name"));
                                    teamDetailBean.setImageUrl(jsonObject.getString("image_url"));
                                    teamDetailBean.setImage(jsonObject.getString("image"));

                                    JSONArray statsArray = jsonObject.getJSONArray("stats");
                                    ArrayList<TeamStatsBean> teamStatsBeanArrayList = new ArrayList<>();
                                    for(int j=0; j<statsArray.length(); j++){
                                        JSONObject jsonObject1 = statsArray.getJSONObject(j);
                                        TeamStatsBean teamStatsBean = new TeamStatsBean();
                                        teamStatsBean.setLabel(jsonObject1.getString("label"));
                                        teamStatsBean.setIcon_name(jsonObject1.getString("icon_name"));
                                        teamStatsBean.setIcon_type(jsonObject1.getString("icon_type"));
                                        teamStatsBean.setColor(jsonObject1.getString("color"));
                                        teamStatsBean.setImage_url(jsonObject1.getString("image_url"));
                                        teamStatsBean.setId(jsonObject1.getString("id"));
                                        teamStatsBean.setValue(jsonObject1.getString("value"));

                                        teamStatsBeanArrayList.add(teamStatsBean);

                                    }
                                    teamDetailBean.setTeamStatsBeanArrayList(teamStatsBeanArrayList);

                                    teamDetail2BeanArrayList.add(teamDetailBean);
                                }
                                coachMatchResultDetailBean.setTeamDetail2BeanArrayList(teamDetail2BeanArrayList);

                                JSONArray totalStasTeam2 = team2Obj.getJSONArray("totalStats");
                                TeamTotalStatsBean teamTotalStatsBean = null;
                                for(int j=0; j<totalStasTeam2.length(); j++){
                                    JSONObject jsonObject = totalStasTeam2.getJSONObject(j);
                                    teamTotalStatsBean = new TeamTotalStatsBean();
                                    teamTotalStatsBean.setValue(jsonObject.getString("value"));
                                    teamTotalStatsBean.setLabel(jsonObject.getString("label"));
                                    teamTotalStatsBean.setIcon_name(jsonObject.getString("icon_name"));
                                    teamTotalStatsBean.setIcon_type(jsonObject.getString("icon_type"));
                                    teamTotalStatsBean.setColor(jsonObject.getString("color"));
                                    teamTotalStatsBean.setImage_url(jsonObject.getString("image_url"));
                                    team2TotalStatsBeanArrayList.add(teamTotalStatsBean);
                                }

                                coachMatchResultDetailBean.setTeam2TotalStatsBeanArrayList(team2TotalStatsBeanArrayList);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            coachMatchResultDetailBeanArrayList.add(coachMatchResultDetailBean);

                            match.setText(coachMatchResultDetailBean.getTeam1Name()+" VS "+coachMatchResultDetailBean.getTeam2Name());
                            date.setText(coachMatchResultDetailBean.getMatchDate()+" | "+coachMatchResultDetailBean.getMatchTime());
                            location.setText(coachMatchResultDetailBean.getAddress());
                            team1Tab.setText(coachMatchResultDetailBean.getTeam1Name());
                            team2Tab.setText(coachMatchResultDetailBean.getTeam2Name());
                            imageLoader.displayImage(coachMatchResultDetailBean.getImageUrl()+""+coachMatchResultDetailBean.getTeam1Logo(), image1, options);
                            imageLoader.displayImage(coachMatchResultDetailBean.getImageUrl()+""+coachMatchResultDetailBean.getTeam2Logo(), image2, options);
                            score1.setText(coachMatchResultDetailBean.getTeam1Score());
                            score2.setText(coachMatchResultDetailBean.getTeam2Score());
                            playerListing1();
                            teamStats1();


                        } else {
                            Toast.makeText(CoachMatchResultDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachMatchResultDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;


        }
    }

    public void playerListing1(){

        if(coachMatchResultDetailBeanArrayList.get(0).getTeamDetail1BeanArrayList()!=null){
            CoachResultPlayerListiingAdapter coachLeagueDetailOneFixtureAdapter = new CoachResultPlayerListiingAdapter(CoachMatchResultDetailScreen.this, coachMatchResultDetailBeanArrayList.get(0).getTeamDetail1BeanArrayList());
            listView.setAdapter(coachLeagueDetailOneFixtureAdapter);
        }
    }

    public void playerListing2(){
        if(coachMatchResultDetailBeanArrayList.get(0).getTeamDetail2BeanArrayList()!=null){
            CoachResultPlayerListiingAdapter coachLeagueDetailOneFixtureAdapter = new CoachResultPlayerListiingAdapter(CoachMatchResultDetailScreen.this, coachMatchResultDetailBeanArrayList.get(0).getTeamDetail2BeanArrayList());
            listView.setAdapter(coachLeagueDetailOneFixtureAdapter);
        }
    }

    public void teamStats1(){
        System.out.println("sizeHERE::"+coachMatchResultDetailBeanArrayList.get(0).getTeam1TotalStatsBeanArrayList().size());
        if(coachMatchResultDetailBeanArrayList.get(0).getTeam1TotalStatsBeanArrayList()!=null){
            CoachMatchResultDetailTeamStatsAdapter coachMatchResultDetailTeamStatsAdapter = new CoachMatchResultDetailTeamStatsAdapter(CoachMatchResultDetailScreen.this, coachMatchResultDetailBeanArrayList.get(0).getTeam1TotalStatsBeanArrayList());
            horListView1.setAdapter(coachMatchResultDetailTeamStatsAdapter);
        }

    }

    public void teamStats2(){
        if(coachMatchResultDetailBeanArrayList.get(0).getTeam2TotalStatsBeanArrayList()!=null){
            CoachMatchResultDetailTeamStatsAdapter coachMatchResultDetailTeamStatsAdapter = new CoachMatchResultDetailTeamStatsAdapter(CoachMatchResultDetailScreen.this, coachMatchResultDetailBeanArrayList.get(0).getTeam2TotalStatsBeanArrayList());
            horListView2.setAdapter(coachMatchResultDetailTeamStatsAdapter);
        }

    }
}