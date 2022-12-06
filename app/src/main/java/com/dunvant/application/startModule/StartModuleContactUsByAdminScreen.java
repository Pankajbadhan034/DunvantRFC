package com.dunvant.application.startModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dunvant.application.R;
import com.dunvant.application.beans.StartModuleContactUsByAdminBean;
import com.dunvant.application.startModule.adapter.StartModuleContactUsByAdminAdapter;
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

public class StartModuleContactUsByAdminScreen extends AppCompatActivity implements IWebServiceCallback {
    ArrayList<StartModuleContactUsByAdminBean> startModuleContactUsByAdminBeanArrayList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    ImageView backButton;
    private final String SEND_MESSAGE = "SEND_MESSAGE";
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_contact_us_by_admin_screen);
        backButton = findViewById(R.id.backButton);
        listView = findViewById(R.id.listView);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        contactUsAPI();

    }

    public void contactUsAPI(){
        if(Utilities.isNetworkAvailable(StartModuleContactUsByAdminScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));
//            nameValuePairList.add(new BasicNameValuePair("academy_id", "1"));

            String webServiceUrl = Utilities.BASE_URL+"osp_aca/contact_detail";

            PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartModuleContactUsByAdminScreen.this, nameValuePairList, SEND_MESSAGE, StartModuleContactUsByAdminScreen.this);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleContactUsByAdminScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case SEND_MESSAGE:

                if(response == null) {
                    Toast.makeText(StartModuleContactUsByAdminScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        startModuleContactUsByAdminBeanArrayList.clear();
                        if(status) {
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StartModuleContactUsByAdminBean startModuleContactUsByAdminBean = new StartModuleContactUsByAdminBean();
                                startModuleContactUsByAdminBean.setId(jsonObject.getString("id"));
                                startModuleContactUsByAdminBean.setAcademy_id(jsonObject.getString("academy_id"));
                                startModuleContactUsByAdminBean.setLabel(jsonObject.getString("label"));
                                startModuleContactUsByAdminBean.setPreferred_text(jsonObject.getString("preferred_text"));
                                startModuleContactUsByAdminBean.setContact_info(jsonObject.getString("contact_info"));
                                startModuleContactUsByAdminBean.setState(jsonObject.getString("state"));
                                startModuleContactUsByAdminBean.setCreated(jsonObject.getString("created"));
                                startModuleContactUsByAdminBean.setModified(jsonObject.getString("modified"));

                                startModuleContactUsByAdminBeanArrayList.add(startModuleContactUsByAdminBean);
                            }

                            StartModuleContactUsByAdminAdapter parentOnlineShoppingAdapter = new StartModuleContactUsByAdminAdapter(StartModuleContactUsByAdminScreen.this, startModuleContactUsByAdminBeanArrayList);
                            listView.setAdapter(parentOnlineShoppingAdapter);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleContactUsByAdminScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}