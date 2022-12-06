package com.dunvant.application.startModule;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.StartModuleTeamMatchBean;
import com.dunvant.application.startModule.adapter.StartModuleMatchDetailsAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class StartModuleMatchDetailsScreen extends AppCompatActivity implements IWebServiceCallback {
    private final String RESOURCE_PAGES_DATA = "RESOURCE_PAGES_DATA";
    ArrayList<StartModuleTeamMatchBean> startModuleResourcebeanArrayList = new ArrayList<>();
    TextView title;
    TextView description;
    ImageView backButton;
    ListView listView;
    String idStr;
    String titleStr;
    String descStr;
    String mainTeamId;
    String imageUrlStr;
    String fileNameStr;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions roundOptions;
    ImageView image;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_match_details_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        listView = findViewById(R.id.listView);
        image = findViewById(R.id.image);
        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        imageLoader.init(ImageLoaderConfiguration.createDefault(StartModuleMatchDetailsScreen.this));
        roundOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        idStr = getIntent().getStringExtra("idStr");
        titleStr = getIntent().getStringExtra("titleStr");
        descStr = getIntent().getStringExtra("descStr");
        mainTeamId = getIntent().getStringExtra("mainTeamId");
        imageUrlStr = getIntent().getStringExtra("imageUrl");
        fileNameStr = getIntent().getStringExtra("fileName");
        title.setText(titleStr);
        description.setText(Html.fromHtml(descStr));
        System.out.println("HERURL:: "+imageUrlStr+""+fileNameStr);
        imageLoader.displayImage(imageUrlStr+""+fileNameStr, image, roundOptions);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        resourcePages();
    }

    private void resourcePages() {
        if (Utilities.isNetworkAvailable(StartModuleMatchDetailsScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));
//            nameValuePairList.add(new BasicNameValuePair("academy_id", "1"));
            nameValuePairList.add(new BasicNameValuePair("team_id", mainTeamId));
            nameValuePairList.add(new BasicNameValuePair("sub_team_id", idStr));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/match_list";

            PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartModuleMatchDetailsScreen.this, nameValuePairList, RESOURCE_PAGES_DATA, StartModuleMatchDetailsScreen.this);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleMatchDetailsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case RESOURCE_PAGES_DATA:

                if (response == null) {
                    Toast.makeText(StartModuleMatchDetailsScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        startModuleResourcebeanArrayList.clear();
                        if (status) {
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StartModuleTeamMatchBean ctartModuleOfferAndLegacyBean = new StartModuleTeamMatchBean();
                                ctartModuleOfferAndLegacyBean.setImageUrl(jsonObject.getString("image_url"));
                                ctartModuleOfferAndLegacyBean.setTeam1(jsonObject.getString("team_1"));
                                ctartModuleOfferAndLegacyBean.setTeam2(jsonObject.getString("team_2"));
                                ctartModuleOfferAndLegacyBean.setTeam1Logo(jsonObject.getString("team_1_logo"));
                                ctartModuleOfferAndLegacyBean.setTeam2Logo(jsonObject.getString("team_2_logo"));
                                ctartModuleOfferAndLegacyBean.setMatchDate(jsonObject.getString("match_date"));
                                ctartModuleOfferAndLegacyBean.setMatchTime(jsonObject.getString("match_time"));
                                ctartModuleOfferAndLegacyBean.setMatchType(jsonObject.getString("match_type"));
                                ctartModuleOfferAndLegacyBean.setMatchVanue(jsonObject.getString("match_venue"));
                                startModuleResourcebeanArrayList.add(ctartModuleOfferAndLegacyBean);
                            }

                            StartModuleMatchDetailsAdapter parentOnlineShoppingAdapter = new StartModuleMatchDetailsAdapter(StartModuleMatchDetailsScreen.this, startModuleResourcebeanArrayList);
                            listView.setAdapter(parentOnlineShoppingAdapter);
                            Utilities.setListViewHeightBasedOnChildren(listView);

                        } else {
                            Toast.makeText(StartModuleMatchDetailsScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleMatchDetailsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}