package com.dunvant.application.coach;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
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
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachLeagueDetailCareerBeen;
import com.dunvant.application.beans.CoachPlayerDetailBean;
import com.dunvant.application.beans.CoachPlayerDetailStatsBean;
import com.dunvant.application.beans.SessionLeagueBean;
import com.dunvant.application.beans.StatsLeagueBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachPlayerDetailCareerAdapter;
import com.dunvant.application.coach.adapters.CoachPlayerDetailCareerHeadingAdapter;
import com.dunvant.application.coach.adapters.CoachPlayerDetailLeagueAdapter;
import com.dunvant.application.coach.adapters.CoachStandingLeagueHorizontalListAdapter;
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

public class CoachPlayerDetailScreen extends AppCompatActivity implements IWebServiceCallback {
    CoachPlayerDetailBean coachPlayerDetailBean;
    ArrayList<CoachPlayerDetailStatsBean> coachPlayerDetailStatsBeanArrayList = new ArrayList<>();
    ArrayList<CoachPlayerDetailBean>coachPlayerDetailBeanArrayList = new ArrayList<>();
    String academyIdStr;
    String leagueIdStr;
    String playerIdStr;
    String teamIdStr;
    String nameStr;
    ImageView backButton;
    Typeface helvetica;
    Typeface linoType;
    TextView title;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    private final String PLAYER_DETAIL = "PLAYER_DETAIL";

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    HorizontalScrollView linearHorList;
    LinearLayout linear2;
    HorizontalListView horListView;
    ScrollView scrollView;
    TableRow tableRow;
    ListView modeList2;
    ArrayList<ListView> listViewArrayList = new ArrayList<>();
    ArrayList<CoachStandingLeagueHorizontalListAdapter> parentCustomListAdapterArrayList = new ArrayList<>();

    ImageView imageTV;
    TextView scoreTV;
    TextView seasonTV;
    TextView positionTV;
    TextView natTV;
    TextView matchTV;
    TextView heightWeightTV;
    TextView ageTV;
    TextView leagues;
    TextView career;
    LinearLayout leagueLinear;
    LinearLayout careerLinear;
    RecyclerView careerHeadingRV;
    RecyclerView careerRV;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_player_detail_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);

        imageTV = findViewById(R.id.imageTV);
        scoreTV = findViewById(R.id.scoreTV);
        seasonTV = findViewById(R.id.seasonTV);
        positionTV = findViewById(R.id.positionTV);
        natTV = findViewById(R.id.natTV);
        matchTV = findViewById(R.id.matchTV);
        heightWeightTV = findViewById(R.id.heightWeightTV);
        ageTV = findViewById(R.id.ageTV);
        leagues = findViewById(R.id.leagues);
        career = findViewById(R.id.career);
        leagueLinear = findViewById(R.id.leagueLinear);
        careerLinear = findViewById(R.id.careerLinear);
        careerHeadingRV = findViewById(R.id.careerHeadingRV);
        careerRV = findViewById(R.id.careerRV);
        listView= findViewById(R.id.listView);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(CoachPlayerDetailScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .displayer(new RoundedBitmapDisplayer(1000))
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();


        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

        academyIdStr = getIntent().getStringExtra("academy_id");
        leagueIdStr = getIntent().getStringExtra("league_id");
        playerIdStr = getIntent().getStringExtra("player_id");
        teamIdStr = getIntent().getStringExtra("team_id");
        nameStr = getIntent().getStringExtra("name");

        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        title.setText(nameStr);
//
//        title.setText(Html.fromHtml("&#xf004;"));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        leagues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                leagues.setBackgroundColor(getResources().getColor(R.color.yellow));
                leagues.setTextColor(getResources().getColor(R.color.black));
                career.setBackgroundColor(getResources().getColor(R.color.blue));
                career.setTextColor(getResources().getColor(R.color.white));
                leagueLinear.setVisibility(View.VISIBLE);
                careerLinear.setVisibility(View.GONE);
                leagueFunction();
            }
        });

        career.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leagues.setBackgroundColor(getResources().getColor(R.color.blue));
                leagues.setTextColor(getResources().getColor(R.color.white));
                career.setBackgroundColor(getResources().getColor(R.color.yellow));
                career.setTextColor(getResources().getColor(R.color.black));
                leagueLinear.setVisibility(View.GONE);
                careerLinear.setVisibility(View.VISIBLE);
                careerFunction();
            }
        });

        changeFonts();
        getPlayerDetails();
    }

    private void changeFonts(){
        title.setTypeface(linoType);

    }

    private void getPlayerDetails() {
        if (Utilities.isNetworkAvailable(CoachPlayerDetailScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", academyIdStr));
            nameValuePairList.add(new BasicNameValuePair("league_id", leagueIdStr));
            nameValuePairList.add(new BasicNameValuePair("player_id", playerIdStr));
            nameValuePairList.add(new BasicNameValuePair("team_id", teamIdStr));

            String webServiceUrl = Utilities.BASE_URL + "league_tournament/player_detail";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            System.out.println("uid::" + loggedInUser.getId() + "token::" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachPlayerDetailScreen.this, nameValuePairList, PLAYER_DETAIL, CoachPlayerDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachPlayerDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {


            case PLAYER_DETAIL:

                if (response == null) {
                    Toast.makeText(CoachPlayerDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        coachPlayerDetailBeanArrayList.clear();

                        if (status){
                            JSONObject dataobj = responseObject.getJSONObject("data");
                            JSONObject data2obj = dataobj.getJSONObject("data");
                            coachPlayerDetailBean = new CoachPlayerDetailBean();
                            coachPlayerDetailBean.setId(data2obj.getString("id"));
                            coachPlayerDetailBean.setAcademy_id(data2obj.getString("academy_id"));
                            coachPlayerDetailBean.setTeam_id(data2obj.getString("team_id"));
                            coachPlayerDetailBean.setSquad_number(data2obj.getString("squad_number"));
                            coachPlayerDetailBean.setNationality(data2obj.getString("nationality"));
                            coachPlayerDetailBean.setPosition(data2obj.getString("position"));
                            coachPlayerDetailBean.setTeam(data2obj.getString("team"));
                            coachPlayerDetailBean.setLeague(data2obj.getString("league"));
                            coachPlayerDetailBean.setSeasons(data2obj.getString("seasons"));
                            coachPlayerDetailBean.setHeight(data2obj.getString("height"));
                            coachPlayerDetailBean.setWeight(data2obj.getString("weight"));
                            coachPlayerDetailBean.setName(data2obj.getString("name"));
                            coachPlayerDetailBean.setDob(data2obj.getString("dob"));
                            coachPlayerDetailBean.setDescription(data2obj.getString("description"));
                            coachPlayerDetailBean.setImage(data2obj.getString("image"));
                            coachPlayerDetailBean.setState(data2obj.getString("state"));
                            coachPlayerDetailBean.setCreated(data2obj.getString("created"));
                            coachPlayerDetailBean.setModified(data2obj.getString("modified"));
                            coachPlayerDetailBean.setImage_url(data2obj.getString("image_url"));
                            coachPlayerDetailBean.setDob_formatted(data2obj.getString("dob_formatted"));
                            coachPlayerDetailBean.setTeam_logo(data2obj.getString("team_logo"));
                            coachPlayerDetailBean.setMatches_played(data2obj.getString("matches_played"));
                            coachPlayerDetailBean.setAcademy_id(data2obj.getString("score"));

                            coachPlayerDetailBean.setPlayer_id(dataobj.getString("player_id"));

                            ArrayList<CoachPlayerDetailStatsBean> coachPlayerDetailStatsBeanArrayList = new ArrayList<>();
                            JSONArray playerStatsArray = dataobj.getJSONArray("playerStats");
                            for(int i=0; i<playerStatsArray.length(); i++){
                                JSONObject playstatsObj = playerStatsArray.getJSONObject(i);
                                CoachPlayerDetailStatsBean coachPlayerDetailStatsBean = new CoachPlayerDetailStatsBean();
                                coachPlayerDetailStatsBean.setLeague_name(playstatsObj.getString("league_name"));

                                JSONArray seasonsArray = playstatsObj.getJSONArray("seasons");
                                ArrayList<SessionLeagueBean> sessionLeagueBeanArrayList = new ArrayList<>();
                                for(int j=0; j<seasonsArray.length(); j++){
                                    JSONObject jsonObject = seasonsArray.getJSONObject(j);

                                    JSONArray statsArray = jsonObject.getJSONArray("stats");
                                    StatsLeagueBean statsLeagueBean1 = new StatsLeagueBean();
                                    ArrayList<StatsLeagueBean>statsLeagueBeanArrayList = new ArrayList<>();
                                    statsLeagueBean1.setHeading("Season");
                                    statsLeagueBean1.setValue(jsonObject.getString("season_name"));
                                    statsLeagueBeanArrayList.add(statsLeagueBean1);

                                    for(int k=0; k<statsArray.length(); k++){
                                        StatsLeagueBean statsLeagueBean = new StatsLeagueBean();
                                        JSONObject jsonObject1 = statsArray.getJSONObject(k);
                                        statsLeagueBean.setHeading(jsonObject1.getString("heading"));
                                        statsLeagueBean.setValue(jsonObject1.getString("value"));
                                        statsLeagueBeanArrayList.add(statsLeagueBean);

                                    }

                                    coachPlayerDetailStatsBean.setStatsLeagueBeanArrayList(statsLeagueBeanArrayList);
                                }


                                coachPlayerDetailStatsBeanArrayList.add(coachPlayerDetailStatsBean);
                            }

                            coachPlayerDetailBean.setCoachPlayerDetailStatsBeanArrayList(coachPlayerDetailStatsBeanArrayList);



                            JSONArray careerStatArray = dataobj.getJSONArray("careerStat");
                            ArrayList<CoachLeagueDetailCareerBeen> coachLeagueDetailCareerBeenArrayList = new ArrayList<>();
                            for(int i=0; i<careerStatArray.length(); i++){
                                JSONObject careerObj = careerStatArray.getJSONObject(i);
                                CoachLeagueDetailCareerBeen coachLeagueDetailCareerBeen = new CoachLeagueDetailCareerBeen();
                                coachLeagueDetailCareerBeen.setHeading(careerObj.getString("heading"));
                                coachLeagueDetailCareerBeen.setValue(careerObj.getString("value"));
                                coachLeagueDetailCareerBeenArrayList.add(coachLeagueDetailCareerBeen);
                            }

                            coachPlayerDetailBean.setCoachLeagueDetailCareerBeenArrayList(coachLeagueDetailCareerBeenArrayList);



//                            for(int i=0; i<coachPlayerDetailBean.getCoachPlayerDetailStatsBeanArrayList().size(); i++){
//                                System.out.println("I::"+i+" "+coachPlayerDetailBean.getCoachPlayerDetailStatsBeanArrayList().get(i).getHeading());
//                            }

                            imageLoader.displayImage(coachPlayerDetailBean.getImage_url()+""+coachPlayerDetailBean.getImage(), imageTV, options);
                            scoreTV.setText(coachPlayerDetailBean.getSquad_number());
                            seasonTV.setText("SEASONS: "+coachPlayerDetailBean.getSeasons());
                            positionTV.setText("POSITION: "+coachPlayerDetailBean.getPosition());
                            natTV.setText("NATIONALITY: "+coachPlayerDetailBean.getNationality());
                            matchTV.setText("MATCH & GOALS: "+coachPlayerDetailBean.getMatches_played()+"| "+coachPlayerDetailBean.getScore());
                            heightWeightTV.setText("HEIGHT-WEIGHT: "+coachPlayerDetailBean.getHeight()+"-"+coachPlayerDetailBean.getWeight());
                            ageTV.setText("DOB: "+coachPlayerDetailBean.getDob_formatted());

                            coachPlayerDetailBeanArrayList.add(coachPlayerDetailBean);

                            leagueFunction();

                        }  else {
                            Toast.makeText(CoachPlayerDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachPlayerDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;


        }
    }

    public void leagueFunction(){
        CoachPlayerDetailLeagueAdapter coachPlayerDetailLeagueAdapter = new CoachPlayerDetailLeagueAdapter(CoachPlayerDetailScreen.this, coachPlayerDetailBeanArrayList.get(0).getCoachPlayerDetailStatsBeanArrayList());
        listView.setAdapter(coachPlayerDetailLeagueAdapter);

    }

    public void careerFunction(){
        CoachPlayerDetailCareerHeadingAdapter coachPlayerDetailCareerHeadingAdapter = new CoachPlayerDetailCareerHeadingAdapter(coachPlayerDetailBean.getCoachLeagueDetailCareerBeenArrayList());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        careerHeadingRV.setLayoutManager(mLayoutManager);
        careerHeadingRV.setAdapter(coachPlayerDetailCareerHeadingAdapter);

        CoachPlayerDetailCareerAdapter coachPlayerDetailCareerAdapter = new CoachPlayerDetailCareerAdapter(coachPlayerDetailBean.getCoachLeagueDetailCareerBeenArrayList());
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        careerRV.setLayoutManager(mLayoutManager2);
        careerRV.setAdapter(coachPlayerDetailCareerAdapter);




    }


}