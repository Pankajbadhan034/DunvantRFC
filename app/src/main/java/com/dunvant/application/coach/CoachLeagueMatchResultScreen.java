package com.dunvant.application.coach;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachLeagueMatchResultBean;
import com.dunvant.application.beans.CoachPlayerStatsbean;
import com.dunvant.application.beans.CoachTeamResultBean;
import com.dunvant.application.beans.CoachTeamStatsBean;
import com.dunvant.application.beans.ConfigueMatchListBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.RecycleCoachLeagueMatchResultColumnAdapter;
import com.dunvant.application.coach.adapters.RecycleCoachLeagueMatchResultHeadingAdapter;
import com.dunvant.application.coach.adapters.RecycleCoachLeagueTeam1Adapter;
import com.dunvant.application.coach.adapters.RecycleCoachLeagueTeam2Adapter;
import com.dunvant.application.coach.adapters.RecycleCoachTeam1HeadingAdapter;
import com.dunvant.application.coach.adapters.RecycleCoachTeam2HeadingAdapter;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CoachLeagueMatchResultScreen extends AppCompatActivity implements IWebServiceCallback {
    String leagueNameStr;
    ImageView team1Image;
    ImageView team2Image;
    TextView matchName;
    TextView league;
    TextView date;
    TextView location;
    TextView teamvs;

    Typeface helvetica;
    Typeface linoType;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    TextView title;
    ImageView backButton;

    String idStr;

    private final String LEAGUE_MATCHES_DETAIL = "LEAGUE_MATCHES_DETAIL";

    private final String SUBMIT_DETAIL = "SUBMIT_DETAIL";

    TextView team1TV;
    TextView team2TV;
    TextView team1ResultTV;
    TextView team2ResultTV;
    LinearLayout linearLayoutForRV;
    CoachLeagueMatchResultBean coachLeagueMatchResultBean;
    RecyclerView recycler_viewHeading;
    RecyclerView recycler_viewTeam1;
    RecyclerView recycler_viewTeam2;

    private RecycleCoachLeagueMatchResultHeadingAdapter recycleCoachLeagueMatchResultHeadingAdapter;
    private RecycleCoachLeagueMatchResultColumnAdapter recycleCoachLeagueMatchResultColumnAdapter;
    private RecycleCoachLeagueMatchResultColumnAdapter recycleCoachLeagueMatchResultHeadingAdapter2;

    TextView team1Title;
    RecyclerView recycler_team1HeadingDynamicView;
    LinearLayout linearLayoutTeam1;
    private RecycleCoachTeam1HeadingAdapter recycleCoachTeam1HeadingAdapter;
    ArrayList<RecyclerView> recyclerViewArrayList1 = new ArrayList<>();
    ArrayList<RecycleCoachLeagueTeam1Adapter> recycleCoachLeagueTeam1AdapterArrayList = new ArrayList<>();

    TextView team2Title;
    RecyclerView recycler_team2HeadingDynamicView;
    LinearLayout linearLayoutTeam2;
    private RecycleCoachTeam2HeadingAdapter recycleCoachTeam2HeadingAdapter;
    ArrayList<RecyclerView> recyclerViewArrayList2 = new ArrayList<>();
    ArrayList<RecycleCoachLeagueTeam2Adapter> recycleCoachLeagueTeam2AdapterArrayList = new ArrayList<>();

    Button submit;
    String team1IdStr="", team2IdStr="", schedulerIdStr="";
    public static String MatchResultSTR = "";
    public static String PlayerPerformanceDataSTR = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_league_match_result_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);

        team1Image = findViewById(R.id.team1Image);
        team2Image = findViewById(R.id.team2Image);
        matchName = findViewById(R.id.matchName);
        league = findViewById(R.id.league);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        teamvs = findViewById(R.id.teamvs);


        team1TV = findViewById(R.id.team1TV);
        team2TV = findViewById(R.id.team2TV);
        linearLayoutForRV = findViewById(R.id.linearLayoutForRV);
        recycler_viewHeading = findViewById(R.id.recycler_viewHeading);
        recycler_viewTeam1 = findViewById(R.id.recycler_viewTeam1);
        recycler_viewTeam2 = findViewById(R.id.recycler_viewTeam2);
        team1ResultTV = findViewById(R.id.team1ResultTV);
        team2ResultTV = findViewById(R.id.team2ResultTV);

        team1Title = findViewById(R.id.team1Title);
        recycler_team1HeadingDynamicView = findViewById(R.id.recycler_team1HeadingDynamicView);
        linearLayoutTeam1 = findViewById(R.id.linearLayoutTeam1);

        team2Title = findViewById(R.id.team2Title);
        recycler_team2HeadingDynamicView = findViewById(R.id.recycler_team2HeadingDynamicView);
        linearLayoutTeam2 = findViewById(R.id.linearLayoutTeam2);

        submit = findViewById(R.id.submit);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        leagueNameStr = getIntent().getStringExtra("leagueName");

        imageLoader.init(ImageLoaderConfiguration.createDefault(CoachLeagueMatchResultScreen.this));
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

        idStr = getIntent().getStringExtra("id");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        changeFonts();
        saveMatchData();

    }

    private void changeFonts() {
        title.setTypeface(linoType);

    }

    private void saveMatchData() {
        if (Utilities.isNetworkAvailable(CoachLeagueMatchResultScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));
            nameValuePairList.add(new BasicNameValuePair("user_id", loggedInUser.getId()));
            nameValuePairList.add(new BasicNameValuePair("match_id", idStr));

            String webServiceUrl = Utilities.BASE_URL + "league_tournament/coach_save_match_result";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            System.out.println("uid::" + loggedInUser.getId() + "token::" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachLeagueMatchResultScreen.this, nameValuePairList, LEAGUE_MATCHES_DETAIL, CoachLeagueMatchResultScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachLeagueMatchResultScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void submitData() {
        if (Utilities.isNetworkAvailable(CoachLeagueMatchResultScreen.this)) {



            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));
            nameValuePairList.add(new BasicNameValuePair("user_id", loggedInUser.getId()));
            nameValuePairList.add(new BasicNameValuePair("match_id", idStr));
            nameValuePairList.add(new BasicNameValuePair("team1_id", team1IdStr));
            nameValuePairList.add(new BasicNameValuePair("team2_id", team2IdStr));
            nameValuePairList.add(new BasicNameValuePair("scheduler_id", schedulerIdStr));


            if(!MatchResultSTR.equalsIgnoreCase("")){
                MatchResultSTR = MatchResultSTR.substring(0, MatchResultSTR.length() -1);
                nameValuePairList.add(new BasicNameValuePair("match_result_data", "["+MatchResultSTR+"]"));
            }else{
                nameValuePairList.add(new BasicNameValuePair("match_result_data", ""));
            }

            if(!PlayerPerformanceDataSTR.equalsIgnoreCase("")){
                PlayerPerformanceDataSTR = PlayerPerformanceDataSTR.substring(0, PlayerPerformanceDataSTR.length() -1);
                nameValuePairList.add(new BasicNameValuePair("player_performance_data", "["+PlayerPerformanceDataSTR+"]"));
            }else{
                nameValuePairList.add(new BasicNameValuePair("player_performance_data", ""));
            }


            String webServiceUrl = Utilities.BASE_URL + "league_tournament/match_result_process";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            System.out.println("uid::" + loggedInUser.getId() + "token::" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachLeagueMatchResultScreen.this, nameValuePairList, SUBMIT_DETAIL, CoachLeagueMatchResultScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachLeagueMatchResultScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {

            case LEAGUE_MATCHES_DETAIL:

                if (response == null) {
                    Toast.makeText(CoachLeagueMatchResultScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONObject dataObj = responseObject.getJSONObject("data");
                            coachLeagueMatchResultBean = new CoachLeagueMatchResultBean();
                            coachLeagueMatchResultBean.setId(dataObj.getString("id"));
                            coachLeagueMatchResultBean.setAcademy_id(dataObj.getString("academy_id"));
                            coachLeagueMatchResultBean.setScheduler_id(dataObj.getString("scheduler_id"));
                            schedulerIdStr = dataObj.getString("scheduler_id");
                            coachLeagueMatchResultBean.setMatch_name(dataObj.getString("match_name"));
                            coachLeagueMatchResultBean.setDescription(dataObj.getString("description"));
                            coachLeagueMatchResultBean.setRound(dataObj.getString("round"));
                            coachLeagueMatchResultBean.setTeam_1(dataObj.getString("team_1"));
                            coachLeagueMatchResultBean.setTeam_2(dataObj.getString("team_2"));
                            coachLeagueMatchResultBean.setTeam_1_result(dataObj.getString("team_1_result"));
                            coachLeagueMatchResultBean.setTeam_2_result(dataObj.getString("team_2_result"));
                            coachLeagueMatchResultBean.setTeam_1_score(dataObj.getString("team_1_score"));
                            coachLeagueMatchResultBean.setTeam_2_score(dataObj.getString("team_2_score"));
                            coachLeagueMatchResultBean.setTeam1_ref_id(dataObj.getString("team1_ref_id"));
                            coachLeagueMatchResultBean.setTeam2_ref_id(dataObj.getString("team2_ref_id"));
                            coachLeagueMatchResultBean.setMatch_date(dataObj.getString("match_date"));
                            coachLeagueMatchResultBean.setMatch_time(dataObj.getString("match_time"));
                            coachLeagueMatchResultBean.setMatch_date_time(dataObj.getString("match_date_time"));
                            coachLeagueMatchResultBean.setSeason(dataObj.getString("season"));
                            coachLeagueMatchResultBean.setMatch_round(dataObj.getString("match_round"));
                            coachLeagueMatchResultBean.setMatch_type(dataObj.getString("match_type"));
                            coachLeagueMatchResultBean.setGround(dataObj.getString("ground"));
                            coachLeagueMatchResultBean.setFile_name(dataObj.getString("file_name"));
                            coachLeagueMatchResultBean.setUrl(dataObj.getString("url"));
                            coachLeagueMatchResultBean.setCreated(dataObj.getString("created"));
                            coachLeagueMatchResultBean.setModified(dataObj.getString("modified"));
                            coachLeagueMatchResultBean.setState(dataObj.getString("state"));
                            coachLeagueMatchResultBean.setImage_url(dataObj.getString("image_url"));
                            coachLeagueMatchResultBean.setLeague_id(dataObj.getString("league_id"));
                            coachLeagueMatchResultBean.setSeason_id(dataObj.getString("season_id"));
                            coachLeagueMatchResultBean.setGround_id(dataObj.getString("ground_id"));
                            coachLeagueMatchResultBean.setAddress(dataObj.getString("address"));
                            coachLeagueMatchResultBean.setTeam_1_logo(dataObj.getString("team_1_logo"));
                            coachLeagueMatchResultBean.setTeam_2_logo(dataObj.getString("team_2_logo"));
                            coachLeagueMatchResultBean.setMatch_date_formatted(dataObj.getString("match_date_formatted"));
                            coachLeagueMatchResultBean.setMatch_time_formatted(dataObj.getString("match_time_formatted"));
                            coachLeagueMatchResultBean.setTeam1Name(dataObj.getString("team1_name"));
                            coachLeagueMatchResultBean.setTeam2Name(dataObj.getString("team2_name"));

                            JSONArray configMatchResulArray = dataObj.getJSONArray("config_match_result");
                            ArrayList<ConfigueMatchListBean> configueMatchListBeanArrayList = new ArrayList<>();
                            for(int i=0; i<configMatchResulArray.length(); i++){
                                JSONObject jsonObject = configMatchResulArray.getJSONObject(i);
                                ConfigueMatchListBean configueMatchListBean = new ConfigueMatchListBean();
                                configueMatchListBean.setId(jsonObject.getString("id"));
                                configueMatchListBean.setLabel(jsonObject.getString("label"));
                                configueMatchListBeanArrayList.add(configueMatchListBean);
                            }
                            coachLeagueMatchResultBean.setConfigueMatchListArrayList(configueMatchListBeanArrayList);


                            JSONArray team1ResultArray = dataObj.getJSONArray("team1_result");
                            ArrayList<CoachTeamResultBean> coachTeam1ResultBeanArrayList = new ArrayList<>();
                            for(int i=0; i<team1ResultArray.length(); i++){
                                JSONObject jsonObject = team1ResultArray.getJSONObject(i);
                                CoachTeamResultBean coachTeamResultBean = new CoachTeamResultBean();
                                coachTeamResultBean.setValue(jsonObject.getString("value"));
                                coachTeamResultBean.setAttribute_id(jsonObject.getString("attribute_id"));
                                coachTeamResultBean.setTeam_id(jsonObject.getString("team_id"));
                                coachTeamResultBean.setLabel(jsonObject.getString("label"));
                                coachTeam1ResultBeanArrayList.add(coachTeamResultBean);

                                team1IdStr = jsonObject.getString("team_id");
                            }
                            coachLeagueMatchResultBean.setCoachTeamResult1BeanArrayList(coachTeam1ResultBeanArrayList);


                            JSONArray team2ResultArray = dataObj.getJSONArray("team2_result");
                            ArrayList<CoachTeamResultBean> coachTeam2ResultBeanArrayList = new ArrayList<>();
                            for(int i=0; i<team2ResultArray.length(); i++){
                                JSONObject jsonObject = team2ResultArray.getJSONObject(i);
                                CoachTeamResultBean coachTeamResultBean = new CoachTeamResultBean();
                                coachTeamResultBean.setValue(jsonObject.getString("value"));
                                coachTeamResultBean.setAttribute_id(jsonObject.getString("attribute_id"));
                                coachTeamResultBean.setTeam_id(jsonObject.getString("team_id"));
                                coachTeamResultBean.setLabel(jsonObject.getString("label"));
                                coachTeam2ResultBeanArrayList.add(coachTeamResultBean);

                                team2IdStr = jsonObject.getString("team_id");
                            }
                            coachLeagueMatchResultBean.setCoachTeamResult2BeanArrayList(coachTeam2ResultBeanArrayList);

                            JSONObject team1StatsObj = dataObj.getJSONObject("team1_stats");
                            JSONArray stats1 = team1StatsObj.getJSONArray("stats");
                            ArrayList<CoachTeamStatsBean> coachTeam1StatsBeanArrayList = new ArrayList<>();
                            for(int i=0; i<stats1.length(); i++){
                                JSONObject jsonObject = stats1.getJSONObject(i);
                                CoachTeamStatsBean coachTeamStatsBean = new CoachTeamStatsBean();
                                coachTeamStatsBean.setTeam_id(jsonObject.getString("team_id"));
                                coachTeamStatsBean.setMatch_id(jsonObject.getString("match_id"));
                                coachTeamStatsBean.setPlayer_id(jsonObject.getString("player_id"));

                              //  player1IDStr = jsonObject.getString("player_id");

                                JSONArray playerStatsArray = jsonObject.getJSONArray("player_stats");
                                ArrayList<CoachPlayerStatsbean> coachPlayerStats1beanArrayList = new ArrayList<>();
                                for(int j=0; j<playerStatsArray.length(); j++){
                                    JSONObject jsonObject1 = playerStatsArray.getJSONObject(j);
                                    CoachPlayerStatsbean coachPlayerStatsbean = new CoachPlayerStatsbean();
                                    coachPlayerStatsbean.setScore(jsonObject1.getString("score"));
                                    coachPlayerStatsbean.setAttribute_id(jsonObject1.getString("attribute_id"));
                                    coachPlayerStatsbean.setTimed_value(jsonObject1.getString("timed_value"));
                                    coachPlayerStatsbean.setTimed_display(jsonObject1.getString("timed_display"));
                                    coachPlayerStatsbean.setLabel(jsonObject1.getString("label"));
                                    coachPlayerStatsbean.setIcon_name(jsonObject1.getString("icon_name"));
                                    coachPlayerStatsbean.setIcon_type(jsonObject1.getString("icon_type"));
                                    coachPlayerStatsbean.setIcon_color(jsonObject1.getString("icon_color"));
                                    coachPlayerStats1beanArrayList.add(coachPlayerStatsbean);
                                }
                                coachTeamStatsBean.setCoachPlayerStatsbeanArrayList(coachPlayerStats1beanArrayList);
                                coachTeam1StatsBeanArrayList.add(coachTeamStatsBean);
                            }
                            coachLeagueMatchResultBean.setCoachTeam1StatsBeanArrayList(coachTeam1StatsBeanArrayList);


                            JSONObject team2StatsObj = dataObj.getJSONObject("team2_stats");
                            JSONArray stats2 = team2StatsObj.getJSONArray("stats");
                            ArrayList<CoachTeamStatsBean> coachTeam2StatsBeanArrayList = new ArrayList<>();
                            for(int i=0; i<stats2.length(); i++){
                                JSONObject jsonObject = stats2.getJSONObject(i);
                                CoachTeamStatsBean coachTeamStatsBean = new CoachTeamStatsBean();
                                coachTeamStatsBean.setTeam_id(jsonObject.getString("team_id"));
                                coachTeamStatsBean.setMatch_id(jsonObject.getString("match_id"));
                                coachTeamStatsBean.setPlayer_id(jsonObject.getString("player_id"));

                               // player2IDStr = jsonObject.getString("player_id");

                                JSONArray playerStatsArray = jsonObject.getJSONArray("player_stats");
                                ArrayList<CoachPlayerStatsbean> coachPlayerStats2beanArrayList = new ArrayList<>();
                                for(int j=0; j<playerStatsArray.length(); j++){
                                    JSONObject jsonObject1 = playerStatsArray.getJSONObject(j);
                                    CoachPlayerStatsbean coachPlayerStatsbean = new CoachPlayerStatsbean();
                                    coachPlayerStatsbean.setScore(jsonObject1.getString("score"));
                                    coachPlayerStatsbean.setAttribute_id(jsonObject1.getString("attribute_id"));
                                    coachPlayerStatsbean.setTimed_value(jsonObject1.getString("timed_value"));
                                    coachPlayerStatsbean.setTimed_display(jsonObject1.getString("timed_display"));
                                    coachPlayerStatsbean.setLabel(jsonObject1.getString("label"));
                                    coachPlayerStatsbean.setIcon_name(jsonObject1.getString("icon_name"));
                                    coachPlayerStatsbean.setIcon_type(jsonObject1.getString("icon_type"));
                                    coachPlayerStatsbean.setIcon_color(jsonObject1.getString("icon_color"));
                                    coachPlayerStats2beanArrayList.add(coachPlayerStatsbean);
                                }
                                coachTeamStatsBean.setCoachPlayerStatsbeanArrayList(coachPlayerStats2beanArrayList);
                                coachTeam2StatsBeanArrayList.add(coachTeamStatsBean);
                            }
                            coachLeagueMatchResultBean.setCoachTeam2StatsBeanArrayList(coachTeam2StatsBeanArrayList);


                            team1TV.setText(coachLeagueMatchResultBean.getTeam1Name());
                            team2TV.setText(coachLeagueMatchResultBean.getTeam2Name());

                            team1Title.setText("BOX SCORE ("+coachLeagueMatchResultBean.getTeam1Name()+")");
                            team2Title.setText("BOX SCORE ("+coachLeagueMatchResultBean.getTeam2Name()+")");

                            imageLoader.displayImage(coachLeagueMatchResultBean.getImage_url() + "" + coachLeagueMatchResultBean.getTeam_1_logo(), team1Image);
                            imageLoader.displayImage(coachLeagueMatchResultBean.getImage_url() + "" + coachLeagueMatchResultBean.getTeam_2_logo(), team2Image);

                            matchName.setText(coachLeagueMatchResultBean.getMatch_name());
                            league.setText(leagueNameStr);
                            date.setText(coachLeagueMatchResultBean.getMatch_date_formatted()+" | "+coachLeagueMatchResultBean.getMatch_time_formatted());
                            location.setText(coachLeagueMatchResultBean.getAddress());
                            teamvs.setText(coachLeagueMatchResultBean.getTeam1Name()+" VS "+coachLeagueMatchResultBean.getTeam2Name());


                            recycleCoachLeagueMatchResultHeadingAdapter = new RecycleCoachLeagueMatchResultHeadingAdapter(coachLeagueMatchResultBean.getCoachTeamResult1BeanArrayList());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                            recycler_viewHeading.setLayoutManager(mLayoutManager);
                            recycler_viewHeading.setAdapter(recycleCoachLeagueMatchResultHeadingAdapter);

                            matchWinCalculate();

                            recycleCoachLeagueMatchResultColumnAdapter = new RecycleCoachLeagueMatchResultColumnAdapter(coachLeagueMatchResultBean.getCoachTeamResult1BeanArrayList(), this);
                            RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                            recycler_viewTeam1.setLayoutManager(mLayoutManager2);
                            recycler_viewTeam1.setAdapter(recycleCoachLeagueMatchResultColumnAdapter);

                            recycleCoachLeagueMatchResultHeadingAdapter2 = new RecycleCoachLeagueMatchResultColumnAdapter(coachLeagueMatchResultBean.getCoachTeamResult2BeanArrayList(), this);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                            recycler_viewTeam2.setLayoutManager(mLayoutManager3);
                            recycler_viewTeam2.setAdapter(recycleCoachLeagueMatchResultHeadingAdapter2);


                            try{

                                // team1
                                System.out.println("SIZE HERE::"+coachLeagueMatchResultBean.getCoachTeam1StatsBeanArrayList().get(0).getCoachPlayerStatsbeanArrayList().size());
                                recycleCoachTeam1HeadingAdapter = new RecycleCoachTeam1HeadingAdapter(coachLeagueMatchResultBean.getCoachTeam1StatsBeanArrayList().get(0).getCoachPlayerStatsbeanArrayList());
                                RecyclerView.LayoutManager rv1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                recycler_team1HeadingDynamicView.setLayoutManager(rv1);
                                recycler_team1HeadingDynamicView.setAdapter(recycleCoachTeam1HeadingAdapter);
                                RecyclerView modeList2;
                                for(int i=0; i<coachLeagueMatchResultBean.getCoachTeam1StatsBeanArrayList().size(); i++){
                                    modeList2 = new RecyclerView(this);
                                    RecycleCoachLeagueTeam1Adapter adapter = new RecycleCoachLeagueTeam1Adapter(coachLeagueMatchResultBean.getCoachTeam1StatsBeanArrayList().get(i).getCoachPlayerStatsbeanArrayList(), CoachLeagueMatchResultScreen.this, team1IdStr, coachLeagueMatchResultBean.getCoachTeam1StatsBeanArrayList().get(i).getPlayer_id());
                                    RecyclerView.LayoutManager rv2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                    modeList2.setLayoutManager(rv2);
                                    modeList2.setAdapter(adapter);
                                    linearLayoutTeam1.addView(modeList2);
                                    recycleCoachLeagueTeam1AdapterArrayList.add(adapter);
                                    System.out.println("SIZE i::"+i);

                                    View v = new View(this);
                                    v.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            1
                                    ));
                                    v.setBackgroundColor(Color.parseColor("#ffffff"));

                                    linearLayoutTeam1.addView(v);
                                }

                                // team 1 on item click



                                // team2
                                System.out.println("SIZE HERE::"+coachLeagueMatchResultBean.getCoachTeam2StatsBeanArrayList().get(0).getCoachPlayerStatsbeanArrayList().size());
                                recycleCoachTeam2HeadingAdapter = new RecycleCoachTeam2HeadingAdapter(coachLeagueMatchResultBean.getCoachTeam2StatsBeanArrayList().get(0).getCoachPlayerStatsbeanArrayList());
                                RecyclerView.LayoutManager rv2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                recycler_team2HeadingDynamicView.setLayoutManager(rv2);
                                recycler_team2HeadingDynamicView.setAdapter(recycleCoachTeam2HeadingAdapter);
                                RecyclerView modeList22;
                                for(int i=0; i<coachLeagueMatchResultBean.getCoachTeam2StatsBeanArrayList().size(); i++){
                                    modeList22 = new RecyclerView(this);
                                    RecycleCoachLeagueTeam2Adapter adapter2 = new RecycleCoachLeagueTeam2Adapter(coachLeagueMatchResultBean.getCoachTeam2StatsBeanArrayList().get(i).getCoachPlayerStatsbeanArrayList(), CoachLeagueMatchResultScreen.this, team2IdStr, coachLeagueMatchResultBean.getCoachTeam2StatsBeanArrayList().get(i).getPlayer_id());
                                    RecyclerView.LayoutManager rv22 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                    modeList22.setLayoutManager(rv22);
                                    modeList22.setAdapter(adapter2);
                                    linearLayoutTeam2.addView(modeList22);
                                    recycleCoachLeagueTeam2AdapterArrayList.add(adapter2);
                                    System.out.println("SIZE i::"+i);

                                    View v = new View(this);
                                    v.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            1
                                    ));
                                    v.setBackgroundColor(Color.parseColor("#ffffff"));
                                }




                            }catch (Exception e){
                                e.printStackTrace();
                            }



                        } else {
                            Toast.makeText(CoachLeagueMatchResultScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachLeagueMatchResultScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case SUBMIT_DETAIL:

                if (response == null) {
                    Toast.makeText(CoachLeagueMatchResultScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            MatchResultSTR = "";
                            PlayerPerformanceDataSTR = "";
                            finish();
                        } else {
                            Toast.makeText(CoachLeagueMatchResultScreen.this, message, Toast.LENGTH_SHORT).show();
                            MatchResultSTR = "";
                            PlayerPerformanceDataSTR = "";
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachLeagueMatchResultScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;


        }
    }

    public void matchWinCalculate(){
        int team1total=0;
        int team1ValueTotal=0;
        int team1AttributeTotal=0;

        String valueAvailable1 = "";
        String valueAvailable2 = "";

        if(coachLeagueMatchResultBean.getCoachTeamResult1BeanArrayList().size()>0){
            for(int i =0; i<coachLeagueMatchResultBean.getCoachTeamResult1BeanArrayList().size(); i++){

                if(!coachLeagueMatchResultBean.getCoachTeamResult1BeanArrayList().get(i).getValue().equalsIgnoreCase("")){
                    valueAvailable1 = "yes";
                    team1ValueTotal = team1ValueTotal + Integer.parseInt(coachLeagueMatchResultBean.getCoachTeamResult1BeanArrayList().get(i).getValue());
                }
                if(!coachLeagueMatchResultBean.getCoachTeamResult1BeanArrayList().get(i).getAttribute_id().equalsIgnoreCase("")){
                    team1AttributeTotal = team1AttributeTotal + Integer.parseInt(coachLeagueMatchResultBean.getCoachTeamResult1BeanArrayList().get(i).getAttribute_id());
                }
            }

            team1total = team1ValueTotal + team1AttributeTotal;
        }

        int team2total=0;
        int team2ValueTotal=0;
        int team2AttributeTotal=0;

        if(coachLeagueMatchResultBean.getCoachTeamResult2BeanArrayList().size()>0){
            for(int i =0; i<coachLeagueMatchResultBean.getCoachTeamResult2BeanArrayList().size(); i++){
                if(!coachLeagueMatchResultBean.getCoachTeamResult2BeanArrayList().get(i).getValue().equalsIgnoreCase("")){
                    valueAvailable2 = "yes";
                    team2ValueTotal = team2ValueTotal + Integer.parseInt(coachLeagueMatchResultBean.getCoachTeamResult2BeanArrayList().get(i).getValue());
                }
                if(!coachLeagueMatchResultBean.getCoachTeamResult1BeanArrayList().get(i).getAttribute_id().equalsIgnoreCase("")){
                    team2AttributeTotal = team2AttributeTotal + Integer.parseInt(coachLeagueMatchResultBean.getCoachTeamResult2BeanArrayList().get(i).getAttribute_id());
                }
            }

            team2total = team2ValueTotal + team2AttributeTotal;
        }


        if(valueAvailable1.equalsIgnoreCase("") && valueAvailable2.equalsIgnoreCase("")){
            team1ResultTV.setText("");
            team2ResultTV.setText("");
        }else{
            if(team1total == team2total){
                team1ResultTV.setText("Draw");
                team2ResultTV.setText("Draw");
            }else if(team1total>team2total){
                team1ResultTV.setText("Win");
                team2ResultTV.setText("Loss");
            }else if(team1total<team2total){
                team1ResultTV.setText("Loss");
                team2ResultTV.setText("Win");
            }
        }



    }
}