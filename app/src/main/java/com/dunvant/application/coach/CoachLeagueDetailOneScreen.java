package com.dunvant.application.coach;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.HorizontalListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachLeagueDetailOneFixtureBean;
import com.dunvant.application.beans.CoachLeagueDetailOneResultMatchBean;
import com.dunvant.application.beans.CoachLeagueDetailOneTableDataBean;
import com.dunvant.application.beans.CoachLeagueDetailOneTableHeadingBean;
import com.dunvant.application.beans.CoachLeagueDetailOneTeamLogobean;
import com.dunvant.application.beans.CoachLeagueDetailOneTopPlayersBean;
import com.dunvant.application.beans.CoachLeagueDetailOneUpcomingMatchBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachLeagueDetailOneFixtureAdapter;
import com.dunvant.application.coach.adapters.CoachLeagueDetailOnePlayersGalleryAdapter;
import com.dunvant.application.coach.adapters.CoachLeagueDetailOneRankingAdapter;
import com.dunvant.application.coach.adapters.CoachLeagueDetailOneResultAdapter;
import com.dunvant.application.coach.adapters.CoachStandingLeagueHorizontalListAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class CoachLeagueDetailOneScreen extends AppCompatActivity implements IWebServiceCallback {
    ArrayList<String> teamIdArray = new ArrayList<>();
    ArrayList<String> teamNameArray = new ArrayList<>();
    String team1IdStr;
    String team2IdStr;
    String matchIDStr;
    String academyIdStr;
    String leagueIdStr;
    String nameStr;
    ImageView backButton;
    Typeface helvetica;
    Typeface linoType;
    TextView title;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    private final String LEAGUE_MATCHES_DETAIL = "LEAGUE_MATCHES_DETAIL";
    private final String TEAM_DETAIL = "TEAM_DETAIL";
    ArrayList<CoachLeagueDetailOneFixtureBean> coachLeagueDetailOneFixtureBeanArrayList = new ArrayList<>();
    ArrayList<CoachLeagueDetailOneResultMatchBean>coachLeagueDetailOneResultMatchBeanArrayList = new ArrayList<>();
    ArrayList<CoachLeagueDetailOneTeamLogobean>coachLeagueDetailOneTeamLogobeanArrayList = new ArrayList<>();
    ArrayList<CoachLeagueDetailOneTableHeadingBean>coachLeagueDetailOneTableHeadingBeanArrayList = new ArrayList<>();
    ArrayList<CoachLeagueDetailOneTableDataBean>coachLeagueDetailOneTableDataBeanArrayList = new ArrayList<>();
    ArrayList<CoachLeagueDetailOneUpcomingMatchBean>coachLeagueDetailOneUpcomingMatchBeanArrayList = new ArrayList<>();
    ArrayList<CoachLeagueDetailOneTopPlayersBean>coachLeagueDetailOneTopPlayersBeanArrayList = new ArrayList<>();

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    TextView upcomingMatchTV;
    TextView upcomingMatch;
    ImageView image1;
    ImageView image2;
    TextView daysTV;
    TextView hrsTV;
    TextView minsTV;
    TextView secsTV;
    TextView date;
    TextView location;
    TextView fixtureTab;
    TextView resultTab;
    TextView leagueStandingTab;
    ListView listview1;
    Button viewAllTabResult;
    TextView playerGalleryTab;
    TextView playerRankingTab;
    ListView listview2;
    HorizontalListView gridView;
    Button viewAllTabRanking;
    HorizontalScrollView linearHorList;
    LinearLayout linear2;
    HorizontalListView horListView;
    ScrollView scrollView;
    TableRow tableRow;
    ListView modeList2;
    ArrayList<ListView> listViewArrayList = new ArrayList<>();
    ArrayList<CoachStandingLeagueHorizontalListAdapter> parentCustomListAdapterArrayList = new ArrayList<>();

    LinearLayout linearClickUpcoming;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_league_detail_one_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        upcomingMatchTV = findViewById(R.id.upcomingMatchTV);
        upcomingMatch = findViewById(R.id.upcomingMatch);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        daysTV = findViewById(R.id.daysTV);
        hrsTV = findViewById(R.id.hrsTV);
        minsTV = findViewById(R.id.minsTV);
        secsTV = findViewById(R.id.secsTV);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        fixtureTab = findViewById(R.id.fixtureTab);
        resultTab = findViewById(R.id.resultTab);
        leagueStandingTab = findViewById(R.id.leagueStandingTab);
        listview1 = findViewById(R.id.listview1);
        viewAllTabResult = findViewById(R.id.viewAllTabResult);
        listview2 = findViewById(R.id.listview2);
        playerGalleryTab = findViewById(R.id.playerGalleryTab);
        playerRankingTab = findViewById(R.id.playerRankingTab);
        gridView = findViewById(R.id.gridView);
        viewAllTabRanking = findViewById(R.id.viewAllTabRanking);
        linearHorList = findViewById(R.id.linearHorList);
        linear2 = findViewById(R.id.linear2);
        horListView = findViewById(R.id.horListView);
        scrollView = findViewById(R.id.scrollView);
        tableRow = findViewById(R.id.tableRow);
        linearClickUpcoming = findViewById(R.id.linearClickUpcoming);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(CoachLeagueDetailOneScreen.this));
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
        title.setText(nameStr);
        System.out.println("LeagueIDSTR::"+leagueIdStr);

        linearClickUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(CoachLeagueDetailOneScreen.this, CoachMatchResultDetailScreen.class);
                obj.putExtra("matchId", coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getMatchId());
                obj.putExtra("academy_id", academyIdStr);
                obj.putExtra("tab", "1");
                startActivity(obj);
            }
        });


        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(CoachLeagueDetailOneScreen.this, CoachTeamDetailScreen.class);
                obj.putExtra("academy_id", leagueIdStr);
                obj.putExtra("league_id", academyIdStr);
                obj.putExtra("team_id", team1IdStr);
                obj.putExtra("teamName", coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getTeam1Name());
                startActivity(obj);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(CoachLeagueDetailOneScreen.this, CoachTeamDetailScreen.class);
                obj.putExtra("academy_id", leagueIdStr);
                obj.putExtra("league_id", academyIdStr);
                obj.putExtra("team_id", team2IdStr);
                obj.putExtra("teamName", coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getTeam2Name());
                startActivity(obj);
            }
        });

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(viewAllTabResult.getText().toString().equalsIgnoreCase("VIEW ALL MATCHES >>")){
                    Intent obj = new Intent(CoachLeagueDetailOneScreen.this, CoachMatchResultDetailScreen.class);
                    obj.putExtra("matchId", coachLeagueDetailOneUpcomingMatchBeanArrayList.get(i).getMatchId());
                    obj.putExtra("academy_id", academyIdStr);
                    obj.putExtra("tab", "1");
                    startActivity(obj);
                }else if(viewAllTabResult.getText().toString().equalsIgnoreCase("VIEW ALL RESULTS >>")){
                    Intent obj = new Intent(CoachLeagueDetailOneScreen.this, CoachMatchResultDetailScreen.class);
                    obj.putExtra("matchId", coachLeagueDetailOneResultMatchBeanArrayList.get(i).getMatchId());
                    obj.putExtra("academy_id", academyIdStr);
                    obj.putExtra("tab", "2");
                    startActivity(obj);

                }


            }
        });

        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent obj = new Intent(CoachLeagueDetailOneScreen.this, CoachPlayerDetailScreen.class);
                obj.putExtra("player_id", coachLeagueDetailOneTopPlayersBeanArrayList.get(i).getPlayerId());
                obj.putExtra("team_id", coachLeagueDetailOneTopPlayersBeanArrayList.get(i).getTeamId());
                obj.putExtra("academy_id", academyIdStr);
                obj.putExtra("league_id", leagueIdStr);
                obj.putExtra("name", coachLeagueDetailOneTopPlayersBeanArrayList.get(i).getName());
                startActivity(obj);

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent obj = new Intent(CoachLeagueDetailOneScreen.this, CoachPlayerDetailScreen.class);
                obj.putExtra("player_id", coachLeagueDetailOneTopPlayersBeanArrayList.get(i).getPlayerId());
                obj.putExtra("team_id", coachLeagueDetailOneTopPlayersBeanArrayList.get(i).getTeamId());
                obj.putExtra("academy_id", academyIdStr);
                obj.putExtra("league_id", leagueIdStr);
                obj.putExtra("name", coachLeagueDetailOneTopPlayersBeanArrayList.get(i).getName());
                startActivity(obj);
            }
        });

        fixtureTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fixtureTab.setBackgroundColor(getResources().getColor(R.color.yellow));
                fixtureTab.setTextColor(getResources().getColor(R.color.black));
                resultTab.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                resultTab.setTextColor(getResources().getColor(R.color.white));
                leagueStandingTab.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                leagueStandingTab.setTextColor(getResources().getColor(R.color.white));
                viewAllTabResult.setText("VIEW ALL MATCHES >>");
                upcomingFixture();

            }
        });

        resultTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fixtureTab.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                fixtureTab.setTextColor(getResources().getColor(R.color.white));
                resultTab.setBackgroundColor(getResources().getColor(R.color.yellow));
                resultTab.setTextColor(getResources().getColor(R.color.black));
                leagueStandingTab.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                leagueStandingTab.setTextColor(getResources().getColor(R.color.white));
                viewAllTabResult.setText("VIEW ALL RESULTS >>");
                result();
            }
        });

        leagueStandingTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fixtureTab.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                fixtureTab.setTextColor(getResources().getColor(R.color.white));
                resultTab.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                resultTab.setTextColor(getResources().getColor(R.color.white));
                leagueStandingTab.setBackgroundColor(getResources().getColor(R.color.yellow));
                leagueStandingTab.setTextColor(getResources().getColor(R.color.black));
                viewAllTabResult.setText("VIEW ALL LEAGUE STANDING >>");
                standing();
            }
        });

        viewAllTabResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewAllTabResult.getText().toString().equalsIgnoreCase("VIEW ALL MATCHES >>")){
                    Intent obj = new Intent(CoachLeagueDetailOneScreen.this, CoachLeagueViewAllMatchesScreen.class);
                    obj.putExtra("academy_id", academyIdStr);
                    obj.putExtra("league_id", leagueIdStr);
                    obj.putExtra("name", nameStr);
                    obj.putExtra("tab", "1");
                    startActivity(obj);
                }else if(viewAllTabResult.getText().toString().equalsIgnoreCase("VIEW ALL RESULTS >>")){
                    Intent obj = new Intent(CoachLeagueDetailOneScreen.this, CoachLeagueViewAllResultScreen.class);
                    obj.putExtra("academy_id", academyIdStr);
                    obj.putExtra("league_id", leagueIdStr);
                    obj.putExtra("name", nameStr);
                    obj.putExtra("tab", "2");
                    startActivity(obj);
                }else if(viewAllTabResult.getText().toString().equalsIgnoreCase("VIEW ALL LEAGUE STANDING >>")){
                    Intent obj = new Intent(CoachLeagueDetailOneScreen.this, CoachLeagueViewAllStandingScreen.class);
                    obj.putExtra("academy_id", academyIdStr);
                    obj.putExtra("league_id", leagueIdStr);
                    obj.putExtra("name", nameStr);
                    obj.putExtra("tab", "3");
                    startActivity(obj);
                }
            }
        });

        playerGalleryTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                gridView.setAdapter(null);
                listview2.setAdapter(null);
                gridView.setVisibility(View.VISIBLE);
                listview2.setVisibility(View.GONE);
                playerGalleryTab.setBackgroundColor(getResources().getColor(R.color.yellow));
                playerGalleryTab.setTextColor(getResources().getColor(R.color.black));
                playerRankingTab.setBackgroundColor(getResources().getColor(R.color.blue));
                playerRankingTab.setTextColor(getResources().getColor(R.color.white));
                viewAllTabRanking.setText("VIEW ALL GALLERY >>");
                galleryList();
            }
        });

        playerRankingTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listview2.setAdapter(null);
              //  gridView.setAdapter(null);
                gridView.setVisibility(View.GONE);
                listview2.setVisibility(View.VISIBLE);
                playerGalleryTab.setBackgroundColor(getResources().getColor(R.color.blue));
                playerGalleryTab.setTextColor(getResources().getColor(R.color.white));
                playerRankingTab.setBackgroundColor(getResources().getColor(R.color.yellow));
                playerRankingTab.setTextColor(getResources().getColor(R.color.black));
                viewAllTabRanking.setText("VIEW ALL RANKING >>");
                rankingList();
            }
        });

        viewAllTabRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewAllTabRanking.getText().toString().equalsIgnoreCase("VIEW ALL GALLERY >>")){
                    Intent obj = new Intent(CoachLeagueDetailOneScreen.this, CoachLeagueViewAllGalleryScreen.class);
                    obj.putExtra("academy_id", academyIdStr);
                    obj.putExtra("league_id", leagueIdStr);
                    obj.putExtra("name", nameStr);
                    startActivity(obj);
                }else if(viewAllTabRanking.getText().toString().equalsIgnoreCase("VIEW ALL RANKING >>")){
                    Intent obj = new Intent(CoachLeagueDetailOneScreen.this, CoachLeagueViewAllRankingScreen.class);
                    obj.putExtra("academy_id", academyIdStr);
                    obj.putExtra("league_id", leagueIdStr);
                    obj.putExtra("name", nameStr);
                    startActivity(obj);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getLeaugeNames();
        changeFonts();


    }

    private void changeFonts(){
        title.setTypeface(linoType);

    }

    private void getLeaugeNames() {
        if (Utilities.isNetworkAvailable(CoachLeagueDetailOneScreen.this)){

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", academyIdStr));
            nameValuePairList.add(new BasicNameValuePair("league_id", leagueIdStr));

            String webServiceUrl = Utilities.BASE_URL + "league_tournament/league_matches";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            System.out.println("uid::" + loggedInUser.getId() + "token::" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachLeagueDetailOneScreen.this, nameValuePairList, LEAGUE_MATCHES_DETAIL, CoachLeagueDetailOneScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else{
            Toast.makeText(CoachLeagueDetailOneScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getTeamDetails() {
        if (Utilities.isNetworkAvailable(CoachLeagueDetailOneScreen.this)){

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", academyIdStr));
            nameValuePairList.add(new BasicNameValuePair("match_id", matchIDStr));

            String webServiceUrl = Utilities.BASE_URL + "league_tournament/league_matches";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            System.out.println("uid::" + loggedInUser.getId() + "token::" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachLeagueDetailOneScreen.this, nameValuePairList, LEAGUE_MATCHES_DETAIL, CoachLeagueDetailOneScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else{
            Toast.makeText(CoachLeagueDetailOneScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {


            case LEAGUE_MATCHES_DETAIL:

                if (response == null) {
                    Toast.makeText(CoachLeagueDetailOneScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONObject dataObj = responseObject.getJSONObject("data");
                            String academyId = dataObj.getString("academy_id");
                            String leqgueId = dataObj.getString("league_id");

                            JSONArray fixtureMatchArray = dataObj.getJSONArray("fixtureMatch");
                            for(int i=0; i<fixtureMatchArray.length(); i++){
                                JSONObject jsonObject = fixtureMatchArray.getJSONObject(i);
                                CoachLeagueDetailOneFixtureBean coachLeagueDetailOneFixtureBean = new CoachLeagueDetailOneFixtureBean();
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
                                coachLeagueDetailOneFixtureBeanArrayList.add(coachLeagueDetailOneFixtureBean);
                            }

                            JSONArray resultMatchArray = dataObj.getJSONArray("resultMatch");
                            for(int i=0; i<resultMatchArray.length(); i++){
                                JSONObject jsonObject = resultMatchArray.getJSONObject(i);
                                CoachLeagueDetailOneResultMatchBean beanClass = new CoachLeagueDetailOneResultMatchBean();
                                beanClass.setImageUrl(jsonObject.getString("image_url"));
                                beanClass.setMatchId(jsonObject.getString("match_id"));
                                beanClass.setTeam1(jsonObject.getString("team_1"));
                                beanClass.setTeam1Name(jsonObject.getString("team_1_name"));
                                beanClass.setTeam1Logo(jsonObject.getString("team_1_logo"));
                                beanClass.setTeam2(jsonObject.getString("team_2"));
                                beanClass.setTeam2Name(jsonObject.getString("team_2_name"));
                                beanClass.setTeam2Logo(jsonObject.getString("team_2_logo"));
                                beanClass.setMatchDate(jsonObject.getString("match_date"));
                                beanClass.setMatchTime(jsonObject.getString("match_time"));
                                beanClass.setTeam1Score(jsonObject.getString("team_1_score"));
                                beanClass.setTeam2Score(jsonObject.getString("team_2_score"));
                                coachLeagueDetailOneResultMatchBeanArrayList.add(beanClass);
                            }

//                            JSONArray teamLogosArray = dataObj.getJSONArray("teamLogos");
//                            for(int i=0; i<teamLogosArray.length(); i++){
//                                JSONObject jsonObject = teamLogosArray.getJSONObject(i);
//                                CoachLeagueDetailOneTeamLogobean beanClass = new CoachLeagueDetailOneTeamLogobean();
//                                beanClass.setFileName(jsonObject.getString("file_name"));
//                                coachLeagueDetailOneTeamLogobeanArrayList.add(beanClass);
//                            }

                            JSONArray tablesHeadingArray = dataObj.getJSONArray("tablesHeading");
                            for(int i=0; i<tablesHeadingArray.length(); i++){
                                JSONObject jsonObject = tablesHeadingArray.getJSONObject(i);
                                CoachLeagueDetailOneTableHeadingBean beanClass = new CoachLeagueDetailOneTableHeadingBean();
                                beanClass.setId(jsonObject.getString("id"));
                                beanClass.setLabel(jsonObject.getString("label"));
                                beanClass.setEquation(jsonObject.getString("equation"));
                                beanClass.setDecimal_places(jsonObject.getString("decimal_places"));
                                coachLeagueDetailOneTableHeadingBeanArrayList.add(beanClass);
                            }

//                            JSONArray tableDataArray = dataObj.getJSONArray("tableData");
//                            for(int i=0; i<tableDataArray.length(); i++){
//                                JSONObject jsonObject = tableDataArray.getJSONObject(i);
//                                CoachLeagueDetailOneTableDataBean beanClass = new CoachLeagueDetailOneTableDataBean();
//                                beanClass.setP(jsonObject.getString("P"));
//                                beanClass.setW(jsonObject.getString("W"));
//                                beanClass.setD(jsonObject.getString("D"));
//                                beanClass.setL(jsonObject.getString("L"));
//                                beanClass.setPts(jsonObject.getString("Pts"));
//                                beanClass.setTeamName(jsonObject.getString("team_name"));
//                                beanClass.setLogo(jsonObject.getString("logo"));
//                                coachLeagueDetailOneTableDataBeanArrayList.add(beanClass);
//                            }


                            JSONArray tableDataArray = dataObj.getJSONArray("tableData");
                            for(int i=0; i<tableDataArray.length(); i++){
                                JSONObject jsonObject = tableDataArray.getJSONObject(i);
                                CoachLeagueDetailOneTableDataBean beanClass = new CoachLeagueDetailOneTableDataBean();
                                beanClass.setLabel(jsonObject.getString("label"));
                                JSONArray valuesArray = jsonObject.getJSONArray("value");
                                ArrayList<String> valuesStringArrayList = new ArrayList<>();
                                valuesStringArrayList.clear();
                                for(int j=0; j<valuesArray.length(); j++){
                                    String valueName = valuesArray.getString(j);
                                    valuesStringArrayList.add(valueName);
                                }


                                if(jsonObject.getString("label").equalsIgnoreCase("team_name")){
                                    teamNameArray.addAll(valuesStringArrayList);
                                }

                                if(jsonObject.getString("label").equalsIgnoreCase("team_id")){
                                    teamIdArray.addAll(valuesStringArrayList);
                                }else{
                                    beanClass.setValues(valuesStringArrayList);
                                    coachLeagueDetailOneTableDataBeanArrayList.add(beanClass);
                                }

                            }


                            JSONArray upComingMatchArray = dataObj.getJSONArray("upComingMatch");
                            for(int i=0; i<upComingMatchArray.length(); i++){
                                JSONObject jsonObject = upComingMatchArray.getJSONObject(i);
                                CoachLeagueDetailOneUpcomingMatchBean beanClass = new CoachLeagueDetailOneUpcomingMatchBean();
                                beanClass.setImageUrl(jsonObject.getString("image_url"));
                                beanClass.setMatchId(jsonObject.getString("match_id"));
                                beanClass.setTeam1(jsonObject.getString("team_1"));
                                beanClass.setTeam1Name(jsonObject.getString("team_1_name"));
                                beanClass.setTeam1Logo(jsonObject.getString("team_1_logo"));
                                beanClass.setTeam2(jsonObject.getString("team_2"));
                                beanClass.setTeam2Name(jsonObject.getString("team_2_name"));
                                beanClass.setTeam2Logo(jsonObject.getString("team_2_logo"));
                                beanClass.setMatchDate(jsonObject.getString("match_date"));
                                beanClass.setMatchTime(jsonObject.getString("match_time"));
                                beanClass.setCounterTime(jsonObject.getString("counter_time"));
                                beanClass.setCounterDate(jsonObject.getString("counter_date"));
                                if(jsonObject.has("ground_name")){
                                    beanClass.setGround_name(jsonObject.getString("ground_name"));
                                }else{
                                    beanClass.setGround_name("not found");
                                }

                                beanClass.setTeam1id(jsonObject.getString("team_1"));
                                beanClass.setTeam2id(jsonObject.getString("team_2"));

                                coachLeagueDetailOneUpcomingMatchBeanArrayList.add(beanClass);
                            }

                            JSONArray topPlayersArray = dataObj.getJSONArray("topPlayers");
                            for(int i=0; i<topPlayersArray.length(); i++){
                                JSONObject jsonObject = topPlayersArray.getJSONObject(i);
                                CoachLeagueDetailOneTopPlayersBean beanClass = new CoachLeagueDetailOneTopPlayersBean();
                                beanClass.setImageUrl(jsonObject.getString("image_url"));
                                beanClass.setImage(jsonObject.getString("image"));
                                beanClass.setPlayerId(jsonObject.getString("player_id"));
                                beanClass.setName(jsonObject.getString("name"));
                                beanClass.setSquadNumber(jsonObject.getString("squad_number"));
                                beanClass.setGoals(jsonObject.getString("goals"));
                                if(jsonObject.has("team_id")){
                                    beanClass.setTeamId(jsonObject.getString("team_id"));
                                }
                                coachLeagueDetailOneTopPlayersBeanArrayList.add(beanClass);
                            }




                            // upcoming matches view
                            try{
                                if(coachLeagueDetailOneUpcomingMatchBeanArrayList.size()>0){
                                    upcomingMatch.setText(coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getTeam1Name()+" VS "+coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getTeam2Name());
                                    date.setText(coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getMatchDate()+" | "+coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getMatchTime());
                                    imageLoader.displayImage(coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getImageUrl()+""+coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getTeam1Logo(), image1, options);
                                    imageLoader.displayImage(coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getImageUrl()+""+coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getTeam2Logo(), image2, options);
                                    location.setText(coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getGround_name());
                                    team1IdStr = coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getTeam1id();
                                    team2IdStr = coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getTeam2id();
                                }else{
                                    linearClickUpcoming.setVisibility(View.GONE);
                                }

                                countDownStart();
                                upcomingFixture();
                                galleryList();
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(CoachLeagueDetailOneScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachLeagueDetailOneScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;


        }
    }

    public void upcomingFixture(){
        try{
            listview1.setVisibility(View.VISIBLE);
            linearHorList.setVisibility(View.GONE);

            listview1.setAdapter(null);
            if(coachLeagueDetailOneFixtureBeanArrayList.size()>0){
                CoachLeagueDetailOneFixtureAdapter coachLeagueDetailOneFixtureAdapter = new CoachLeagueDetailOneFixtureAdapter(CoachLeagueDetailOneScreen.this, coachLeagueDetailOneFixtureBeanArrayList);
                listview1.setAdapter(coachLeagueDetailOneFixtureAdapter);
                Utilities.setListViewHeightBasedOnChildren(listview1);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void result(){
        try{
            listview1.setVisibility(View.VISIBLE);
            linearHorList.setVisibility(View.GONE);

            listview1.setAdapter(null);
            if(coachLeagueDetailOneResultMatchBeanArrayList.size()>0){
                CoachLeagueDetailOneResultAdapter coachLeagueDetailOneFixtureAdapter = new CoachLeagueDetailOneResultAdapter(CoachLeagueDetailOneScreen.this, coachLeagueDetailOneResultMatchBeanArrayList);
                listview1.setAdapter(coachLeagueDetailOneFixtureAdapter);
                Utilities.setListViewHeightBasedOnChildren(listview1);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void standing(){
        try{
            listview1.setVisibility(View.GONE);
            linearHorList.setVisibility(View.VISIBLE);
            listViewArrayList.clear();
            parentCustomListAdapterArrayList.clear();
            tableRow.removeAllViews();

            if(coachLeagueDetailOneTableDataBeanArrayList.size()>0){
                ArrayList<String>titleBarList = new ArrayList<>();
                for(int i=0; i<coachLeagueDetailOneTableDataBeanArrayList.size(); i++){
                    CoachLeagueDetailOneTableDataBean coachLeagueDetailOneTableDataBean = coachLeagueDetailOneTableDataBeanArrayList.get(i);
                    titleBarList.add(coachLeagueDetailOneTableDataBean.getLabel());
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.coach_league_detail_one_custom_list_title_bar_horizontal_list_item, R.id.categoryname, titleBarList);
                horListView.setAdapter(adapter1);




                for(int i=0; i<coachLeagueDetailOneTableDataBeanArrayList.size(); i++){
                    modeList2 = new ListView(this);
                    ArrayList<String> coloumnArrayList = new ArrayList<>();
                    coloumnArrayList.clear();
                    for(int j=0; j<coachLeagueDetailOneTableDataBeanArrayList.get(i).getValues().size(); j++){
                        coloumnArrayList.add(coachLeagueDetailOneTableDataBeanArrayList.get(i).getValues().get(j));
                    }



                    CoachStandingLeagueHorizontalListAdapter adapter = new CoachStandingLeagueHorizontalListAdapter(CoachLeagueDetailOneScreen.this, coloumnArrayList);
                    modeList2.setAdapter(adapter);



                    tableRow.addView(modeList2);
                    Utilities.setListViewHeightBasedOnChildren(modeList2);
                    listViewArrayList.add(modeList2);

                    parentCustomListAdapterArrayList.add(adapter);
                    //allcollumnsArrayLists.add(coloumnArrayList);
                    //allPitchSlotsBeanArrayList.add(pitchSlotsBeanArrayList);
                }


                try{

                }catch (Exception e){

                }

                for(int i=0; i<listViewArrayList.size(); i++){
                    listViewArrayList.get(i).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            for(int j=0; j<teamIdArray.size(); j++){
                                if(i==j){
                                    Intent obj = new Intent(CoachLeagueDetailOneScreen.this, CoachTeamDetailScreen.class);
                                    obj.putExtra("academy_id", leagueIdStr);
                                    obj.putExtra("league_id", academyIdStr);
                                    obj.putExtra("team_id", teamIdArray.get(j));
                                    obj.putExtra("teamName", teamNameArray.get(j));
                                    startActivity(obj);
                                }
                            }
                        }
                    });
                }



            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void galleryList(){
        try{
            if(coachLeagueDetailOneTopPlayersBeanArrayList.size()>0){
                CoachLeagueDetailOnePlayersGalleryAdapter coachLeagueDetailOneFixtureAdapter = new CoachLeagueDetailOnePlayersGalleryAdapter(CoachLeagueDetailOneScreen.this, coachLeagueDetailOneTopPlayersBeanArrayList);
                gridView.setAdapter(coachLeagueDetailOneFixtureAdapter);
               // Utilities.setGridViewHeightBasedOnChildren(gridView,2);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void rankingList(){
        try{
            if(coachLeagueDetailOneTopPlayersBeanArrayList.size()>0){
                CoachLeagueDetailOneRankingAdapter coachLeagueDetailOneTopPlayersBean = new CoachLeagueDetailOneRankingAdapter(CoachLeagueDetailOneScreen.this, coachLeagueDetailOneTopPlayersBeanArrayList);
                listview2.setAdapter(coachLeagueDetailOneTopPlayersBean);
                Utilities.setListViewHeightBasedOnChildren(listview2);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*countDownStart() method for start count down*/
    public void countDownStart() {
       final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
//                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyy hh:mm:ss aa");
                    // Please set date in  YYYY-MM-DD hh:mm:ss format
                    /*parse endDateTime in future date*/
//                    Date futureDate = dateFormat.parse("2021-04-14 15:00:00");
                    String dateHere = coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getCounterDate();
                    String timeHere = coachLeagueDetailOneUpcomingMatchBeanArrayList.get(0).getCounterTime();
                    String finalCountTime = dateHere+" "+timeHere;
//                    Date futureDate = dateFormat.parse("04-13-2021 05:00:00 PM");
                    Date futureDate = dateFormat.parse(finalCountTime);
                    Date currentDate = new Date();
                    /*if current date is not comes after future date*/
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();

                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days *(24  *60 * 60  *1000);
                        long hours = diff / (60 * 60*  1000);
                        diff -= hours * (60*  60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60  *1000);
                        long seconds = diff / 1000;
                        @SuppressLint("DefaultLocale") String dayLeft = "" + String.format("%02d", days);
                        @SuppressLint("DefaultLocale") String hrLeft = "" + String.format("%02d", hours);
                        @SuppressLint("DefaultLocale") String minsLeft = "" + String.format("%02d", minutes);
                        @SuppressLint("DefaultLocale") String secondLeft = "" + String.format("%02d", seconds);
                        daysTV.setText(dayLeft+"");
                        hrsTV.setText(hrLeft+"");
                        minsTV.setText(minsLeft+"");
                        secsTV.setText(secondLeft +"");
                       // daysTV.setText(dayLeft + "D: \n"+ hrLeft + "H: \n"+minsLeft + "M: \n"+secondLeft + "S");
                    } else {
                        //textViewGone();
                        daysTV.setText("expired");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

}
