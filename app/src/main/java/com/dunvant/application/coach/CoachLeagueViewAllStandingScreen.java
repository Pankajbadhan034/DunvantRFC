package com.dunvant.application.coach;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.dunvant.application.beans.CoachLeagueDetailOneTableDataBean;
import com.dunvant.application.beans.UserBean;
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

public class CoachLeagueViewAllStandingScreen extends AppCompatActivity  implements IWebServiceCallback {
    ArrayList<String> teamIdArray = new ArrayList<>();
    ArrayList<String> teamNameArray = new ArrayList<>();
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
    ArrayList<CoachLeagueDetailOneTableDataBean>coachLeagueDetailOneTableDataBeanArrayList = new ArrayList<>();

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_league_view_all_standing_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        linearHorList = findViewById(R.id.linearHorList);
        linear2 = findViewById(R.id.linear2);
        horListView = findViewById(R.id.horListView);
        scrollView = findViewById(R.id.scrollView);
        tableRow = findViewById(R.id.tableRow);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(CoachLeagueViewAllStandingScreen.this));
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        changeFonts();
        getLeaugeNames();
    }

    private void changeFonts(){
        title.setTypeface(linoType);

    }

    private void getLeaugeNames() {
        if (Utilities.isNetworkAvailable(CoachLeagueViewAllStandingScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", academyIdStr));
            nameValuePairList.add(new BasicNameValuePair("league_id", leagueIdStr));

            String webServiceUrl = Utilities.BASE_URL + "league_tournament/league_standings";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            System.out.println("uid::" + loggedInUser.getId() + "token::" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachLeagueViewAllStandingScreen.this, nameValuePairList, LEAGUE_MATCHES_DETAIL, CoachLeagueViewAllStandingScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachLeagueViewAllStandingScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {


            case LEAGUE_MATCHES_DETAIL:

                if (response == null) {
                    Toast.makeText(CoachLeagueViewAllStandingScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status){
                            coachLeagueDetailOneTableDataBeanArrayList.clear();
                            JSONObject data = responseObject.getJSONObject("data");
                            JSONArray tableDataArray = data.getJSONArray("tableData");
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

                            standing();
                        }  else {
                            Toast.makeText(CoachLeagueViewAllStandingScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachLeagueViewAllStandingScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;


        }
    }

    public void standing(){
        try{
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



                    CoachStandingLeagueHorizontalListAdapter adapter = new CoachStandingLeagueHorizontalListAdapter(CoachLeagueViewAllStandingScreen.this, coloumnArrayList);
                    modeList2.setAdapter(adapter);



                    tableRow.addView(modeList2);
                    Utilities.setListViewHeightBasedOnChildren(modeList2);
                    listViewArrayList.add(modeList2);

                    parentCustomListAdapterArrayList.add(adapter);
                    // allcollumnsArrayLists.add(coloumnArrayList);
                    //allPitchSlotsBeanArrayList.add(pitchSlotsBeanArrayList);
                }


                for(int i=0; i<listViewArrayList.size(); i++){
                    listViewArrayList.get(i).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            for(int j=0; j<teamIdArray.size(); j++){
                                if(i==j){
                                    Intent obj = new Intent(CoachLeagueViewAllStandingScreen.this, CoachTeamDetailScreen.class);
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
}