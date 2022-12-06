package com.dunvant.application.startModule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.R;
import com.dunvant.application.beans.StartModuleTeamDetailBean;
import com.dunvant.application.startModule.adapter.StartModuleTeamDetailAdapter;
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

public class StartModuleTeamDetailScreen extends AppCompatActivity implements IWebServiceCallback {
    private final String RESOURCE_PAGES_DATA = "RESOURCE_PAGES_DATA";
    ArrayList<StartModuleTeamDetailBean> startModuleResourcebeanArrayList = new ArrayList<>();
    TextView title;
    ImageView backButton;
    ListView listView;
    String idStr;
    String titleStr;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_team_detail_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        listView = findViewById(R.id.listView);

        idStr = getIntent().getStringExtra("id");
        titleStr = getIntent().getStringExtra("title");
        title.setText(titleStr);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent obj = new Intent(StartModuleTeamDetailScreen.this, StartModuleMatchDetailsScreen.class);
                obj.putExtra("idStr", startModuleResourcebeanArrayList.get(i).getId());
                obj.putExtra("titleStr", startModuleResourcebeanArrayList.get(i).getTitle());
                obj.putExtra("descStr", startModuleResourcebeanArrayList.get(i).getDescription());
                obj.putExtra("mainTeamId", idStr);
                obj.putExtra("imageUrl", startModuleResourcebeanArrayList.get(i).getImage_url());
                obj.putExtra("fileName", startModuleResourcebeanArrayList.get(i).getFileName());
                startActivity(obj);

            }
        });

        resourcePages();
    }

    private void resourcePages() {
        if (Utilities.isNetworkAvailable(StartModuleTeamDetailScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));
            nameValuePairList.add(new BasicNameValuePair("team_id", idStr));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/team_category";

            PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartModuleTeamDetailScreen.this, nameValuePairList, RESOURCE_PAGES_DATA, StartModuleTeamDetailScreen.this);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleTeamDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case RESOURCE_PAGES_DATA:

                if (response == null) {
                    Toast.makeText(StartModuleTeamDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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
                                StartModuleTeamDetailBean ctartModuleOfferAndLegacyBean = new StartModuleTeamDetailBean();
                                ctartModuleOfferAndLegacyBean.setId(jsonObject.getString("id"));
                                ctartModuleOfferAndLegacyBean.setTitle(jsonObject.getString("title"));
                                ctartModuleOfferAndLegacyBean.setDescription(jsonObject.getString("description"));
                                ctartModuleOfferAndLegacyBean.setImage_url(jsonObject.getString("image_url"));
                                ctartModuleOfferAndLegacyBean.setFileName(jsonObject.getString("file_name"));
                                startModuleResourcebeanArrayList.add(ctartModuleOfferAndLegacyBean);
                            }

                            StartModuleTeamDetailAdapter parentOnlineShoppingAdapter = new StartModuleTeamDetailAdapter(StartModuleTeamDetailScreen.this, startModuleResourcebeanArrayList);
                            listView.setAdapter(parentOnlineShoppingAdapter);

                        } else {
                            Toast.makeText(StartModuleTeamDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleTeamDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}