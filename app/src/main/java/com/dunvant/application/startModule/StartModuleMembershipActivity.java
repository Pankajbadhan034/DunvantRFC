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
import com.dunvant.application.beans.StartModuleMembershipBean;
import com.dunvant.application.startModule.adapter.StartModuleMembershipAdapter;
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

public class StartModuleMembershipActivity extends AppCompatActivity implements IWebServiceCallback {
    private final String RESOURCE_PAGES_DATA = "RESOURCE_PAGES_DATA";
    ArrayList<StartModuleMembershipBean> startModuleResourcebeanArrayList = new ArrayList<>();
    TextView title;
    ImageView backButton;
    ListView listView;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_membership_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        listView = findViewById(R.id.listView);
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

                Intent obj = new Intent(StartModuleMembershipActivity.this, StartModuleMembershipDetailsScreen.class);
                obj.putExtra("idStr", startModuleResourcebeanArrayList.get(i).getId());
                obj.putExtra("titleStr", startModuleResourcebeanArrayList.get(i).getTitle());
                obj.putExtra("descStr", startModuleResourcebeanArrayList.get(i).getDescription());
                obj.putExtra("monthlyPriceStr", startModuleResourcebeanArrayList.get(i).getMonthly_price());
                obj.putExtra("yearlyPriceStr", startModuleResourcebeanArrayList.get(i).getYearly_price());
                startActivity(obj);

            }
        });

        resourcePages();
    }

    private void resourcePages() {
        if (Utilities.isNetworkAvailable(StartModuleMembershipActivity.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/membership";

            PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartModuleMembershipActivity.this, nameValuePairList, RESOURCE_PAGES_DATA, StartModuleMembershipActivity.this);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleMembershipActivity.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case RESOURCE_PAGES_DATA:

                if (response == null) {
                    Toast.makeText(StartModuleMembershipActivity.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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
                                StartModuleMembershipBean ctartModuleOfferAndLegacyBean = new StartModuleMembershipBean();
                                ctartModuleOfferAndLegacyBean.setId(jsonObject.getString("id"));
                                ctartModuleOfferAndLegacyBean.setAcademy_id(jsonObject.getString("academy_id"));
                                ctartModuleOfferAndLegacyBean.setTitle(jsonObject.getString("title"));
                                ctartModuleOfferAndLegacyBean.setDescription(jsonObject.getString("description"));
                                ctartModuleOfferAndLegacyBean.setMonthly_price(jsonObject.getString("monthly_price"));
                                ctartModuleOfferAndLegacyBean.setYearly_price(jsonObject.getString("yearly_price"));
                                ctartModuleOfferAndLegacyBean.setState(jsonObject.getString("state"));
                                ctartModuleOfferAndLegacyBean.setCreated(jsonObject.getString("created"));
                                ctartModuleOfferAndLegacyBean.setModified(jsonObject.getString("modified"));

                                if(jsonObject.getString("state").equalsIgnoreCase("")){

                                }

                                startModuleResourcebeanArrayList.add(ctartModuleOfferAndLegacyBean);
                            }

                            StartModuleMembershipAdapter parentOnlineShoppingAdapter = new StartModuleMembershipAdapter(StartModuleMembershipActivity.this, startModuleResourcebeanArrayList);
                            listView.setAdapter(parentOnlineShoppingAdapter);

                        } else {
                            Toast.makeText(StartModuleMembershipActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleMembershipActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}