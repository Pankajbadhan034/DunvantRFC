package com.dunvant.application.coach;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachLeagueDetailOneTopPlayersBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachLeagueViewAllGalleryAdapter;
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

public class CoachLeagueViewAllGalleryScreen extends AppCompatActivity implements IWebServiceCallback {
    ArrayList<CoachLeagueDetailOneTopPlayersBean>coachLeagueDetailOneTopPlayersBeanArrayList = new ArrayList<>();
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
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_league_view_all_gallery_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        gridView = findViewById(R.id.gridView);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(CoachLeagueViewAllGalleryScreen.this));
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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("leagueIDHERE::"+leagueIdStr);
                Intent obj = new Intent(CoachLeagueViewAllGalleryScreen.this, CoachPlayerDetailScreen.class);
                obj.putExtra("player_id", coachLeagueDetailOneTopPlayersBeanArrayList.get(i).getPlayerId());
                obj.putExtra("team_id", coachLeagueDetailOneTopPlayersBeanArrayList.get(i).getTeamId());
                obj.putExtra("academy_id", academyIdStr);
                obj.putExtra("league_id", leagueIdStr);
                obj.putExtra("name", coachLeagueDetailOneTopPlayersBeanArrayList.get(i).getName());
                startActivity(obj);
            }
        });

        getLeaugeNames();
        changeFonts();
    }

    private void changeFonts() {
        title.setTypeface(linoType);

    }

    private void getLeaugeNames() {
        if (Utilities.isNetworkAvailable(CoachLeagueViewAllGalleryScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", academyIdStr));
            nameValuePairList.add(new BasicNameValuePair("league_id", leagueIdStr));

            String webServiceUrl = Utilities.BASE_URL + "league_tournament/league_player_gallery";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            System.out.println("uid::" + loggedInUser.getId() + "token::" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachLeagueViewAllGalleryScreen.this, nameValuePairList, LEAGUE_MATCHES_DETAIL, CoachLeagueViewAllGalleryScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachLeagueViewAllGalleryScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {


            case LEAGUE_MATCHES_DETAIL:

                if (response == null) {
                    Toast.makeText(CoachLeagueViewAllGalleryScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray data = responseObject.getJSONArray("data");
                            for(int i=0; i<data.length(); i++){
                                JSONObject jsonObject = data.getJSONObject(i);
                                CoachLeagueDetailOneTopPlayersBean beanClass = new CoachLeagueDetailOneTopPlayersBean();
                                beanClass.setImageUrl(jsonObject.getString("image_url"));
                                beanClass.setImage(jsonObject.getString("image"));
                                beanClass.setPlayerId(jsonObject.getString("player_id"));
                                beanClass.setName(jsonObject.getString("name"));
                                beanClass.setSquadNumber(jsonObject.getString("squad_number"));
                                beanClass.setGoals(jsonObject.getString("goals"));
                                beanClass.setTeamId(jsonObject.getString("team_id"));
                                beanClass.setPosition(jsonObject.getString("position"));
                                coachLeagueDetailOneTopPlayersBeanArrayList.add(beanClass);
                            }
                            CoachLeagueViewAllGalleryAdapter coachLeagueDetailOneFixtureAdapter = new CoachLeagueViewAllGalleryAdapter(CoachLeagueViewAllGalleryScreen.this, coachLeagueDetailOneTopPlayersBeanArrayList);
                            gridView.setAdapter(coachLeagueDetailOneFixtureAdapter);
                        } else {
                            Toast.makeText(CoachLeagueViewAllGalleryScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachLeagueViewAllGalleryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;


        }
    }
}