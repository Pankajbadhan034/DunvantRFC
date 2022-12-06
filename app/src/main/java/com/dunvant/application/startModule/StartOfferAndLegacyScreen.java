package com.dunvant.application.startModule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.R;
import com.dunvant.application.beans.StartModuleOfferAndLegacyBean;
import com.dunvant.application.startModule.adapter.StartModuleOfferAndLegacyAdapter;
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

public class StartOfferAndLegacyScreen extends AppCompatActivity implements IWebServiceCallback {
    private final String RESOURCE_PAGES_DATA = "RESOURCE_PAGES_DATA";
    ArrayList<StartModuleOfferAndLegacyBean> startModuleResourcebeanArrayList = new ArrayList<>();
    TextView title;
    ImageView backButton;
    String clickIdStr, titleStr;
    ListView listView;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_offer_and_legacy_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        listView = findViewById(R.id.listView);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        clickIdStr = getIntent().getStringExtra("id");
        titleStr = getIntent().getStringExtra("title");
        title.setText(titleStr);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(startModuleResourcebeanArrayList.get(i).getType().equalsIgnoreCase("legacy")){
                    try{
                        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(startModuleResourcebeanArrayList.get(i).getUrl()));
                        startActivity(browserIntent);
                    }catch (Exception e){
                        Toast.makeText(StartOfferAndLegacyScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else{
                    try{
                        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(startModuleResourcebeanArrayList.get(i).getUrl()+""+startModuleResourcebeanArrayList.get(i).getFile()));
                        startActivity(browserIntent);
                    }catch (Exception e){
                        Toast.makeText(StartOfferAndLegacyScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }



//                if(titleStr.equalsIgnoreCase("video")){
//                    String url = startModuleResourcebeanArrayList.get(i).getUrl();
//                    Intent viewImageInFullScreen = new Intent(StartOfferAndLegacyScreen.this, ParentViewVideoInFullScreen.class);
//                    viewImageInFullScreen.putExtra("videoUrl", url);
//                    startActivity(viewImageInFullScreen);
//                }else{
//                    String url = startModuleResourcebeanArrayList.get(i).getUrl();
//
//                    try{
//                        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
//                        startActivity(browserIntent);
//                    }catch (Exception e){
//                        Toast.makeText(StartOfferAndLegacyScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                }


            }
        });

        resourcePages();
    }

    private void resourcePages() {
        if (Utilities.isNetworkAvailable(StartOfferAndLegacyScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));
            nameValuePairList.add(new BasicNameValuePair("type", clickIdStr));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/offer_legacy_pages";

            PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartOfferAndLegacyScreen.this, nameValuePairList, RESOURCE_PAGES_DATA, StartOfferAndLegacyScreen.this);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartOfferAndLegacyScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case RESOURCE_PAGES_DATA:

                if (response == null) {
                    Toast.makeText(StartOfferAndLegacyScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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
                                StartModuleOfferAndLegacyBean ctartModuleOfferAndLegacyBean = new StartModuleOfferAndLegacyBean();
                                ctartModuleOfferAndLegacyBean.setId(jsonObject.getString("id"));
                                ctartModuleOfferAndLegacyBean.setAcademyId(jsonObject.getString("academy_id"));
                                ctartModuleOfferAndLegacyBean.setTitle(jsonObject.getString("title"));
                                ctartModuleOfferAndLegacyBean.setDescription(jsonObject.getString("description"));
                                ctartModuleOfferAndLegacyBean.setFile(jsonObject.getString("file"));
                                ctartModuleOfferAndLegacyBean.setUrl(jsonObject.getString("url"));
                                ctartModuleOfferAndLegacyBean.setType(jsonObject.getString("type"));
                                ctartModuleOfferAndLegacyBean.setState(jsonObject.getString("state"));
                                ctartModuleOfferAndLegacyBean.setCreated(jsonObject.getString("created"));
                                ctartModuleOfferAndLegacyBean.setModified(jsonObject.getString("modified"));

                                startModuleResourcebeanArrayList.add(ctartModuleOfferAndLegacyBean);
                            }

                            StartModuleOfferAndLegacyAdapter parentOnlineShoppingAdapter = new StartModuleOfferAndLegacyAdapter(StartOfferAndLegacyScreen.this, startModuleResourcebeanArrayList, titleStr);
                            listView.setAdapter(parentOnlineShoppingAdapter);

                        } else {
                            Toast.makeText(StartOfferAndLegacyScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartOfferAndLegacyScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}