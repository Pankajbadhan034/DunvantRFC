package com.dunvant.application.startModule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.DocumentsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.startModule.adapter.StartModuleTab4MonitorAndCovidAdapter;
import com.dunvant.application.utils.CircularTextView;
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

public class StartModuleTab4MonitorAndCovidQaScreen extends AppCompatActivity implements IWebServiceCallback {
    ImageView backButton;
    ListView listView;
    Button submit;
    TextView title;
    TextView dailyWellness;
    TextView covidWellness;
    ArrayList<DocumentsBean> documentsBeanArrayList = new ArrayList<>();

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    CircularTextView image;
    CircularTextView image1;
    RelativeLayout relative1;
    RelativeLayout relative2;

    private final String DOCUMENT_SERVICE = "DOCUMENT_SERVICE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_tab4_monitor_and_covid_qa_activity);
        backButton = findViewById(R.id.backButton);
        dailyWellness = findViewById(R.id.dailyWellness);
        covidWellness = findViewById(R.id.covidWellness);
        listView = findViewById(R.id.listView);
        submit = findViewById(R.id.submit);
        title = findViewById(R.id.title);
        image = findViewById(R.id.image);
        image1 = findViewById(R.id.image1);
        relative1 = findViewById(R.id.relative1);
        relative2 = findViewById(R.id.relative2);

        image.setText("D");
        image.setStrokeWidth(1);
        image.setStrokeColor("#333333");
        image.setSolidColor("#dbdbdb");
        image.setTextColor(getResources().getColor(R.color.darkBlue));

        image1.setText("C");
        image1.setStrokeWidth(1);
        image1.setStrokeColor("#333333");
        image1.setSolidColor("#dbdbdb");
        image1.setTextColor(getResources().getColor(R.color.darkBlue));


        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(StartModuleTab4MonitorAndCovidQaScreen.this, StartModuleDailyWellnessScreen.class);
                startActivity(obj);

            }
        });

        relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(StartModuleTab4MonitorAndCovidQaScreen.this, StartModuleCovidWellnessScreen.class);
                startActivity(obj);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try{

                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(documentsBeanArrayList.get(i).getImage_url()+""+documentsBeanArrayList.get(i).getFile_name()));
                    startActivity(browserIntent);
                }catch (Exception e){
                    Toast.makeText(StartModuleTab4MonitorAndCovidQaScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }



//                Intent obj = new Intent(StartModuleTab4MonitorAndCovidQaScreen.this, StartModuleWebViewScreen.class);
//                obj.putExtra("link", documentsBeanArrayList.get(i).getImage_url()+""+documentsBeanArrayList.get(i).getFile_name());
//                startActivity(obj);
            }
        });

        doucmentAPI();
    }

//
//    private void progQuestionsAPI() {
//        if (Utilities.isNetworkAvailable(StartModuleTab4MonitorAndCovidQaScreen.this)) {
//            List<NameValuePair> nameValuePairList = new ArrayList<>();
//
//            nameValuePairList.add(new BasicNameValuePair("academy_id", "1"));
//            nameValuePairList.add(new BasicNameValuePair("category", "1"));
//
//            String webServiceUrl = Utilities.BASE_URL + "osp_aca/covid_wellness_question";
//
//            ArrayList<String> headers = new ArrayList<>();
//            headers.add("X-access-uid:" + loggedInUser.getId());
//            headers.add("X-access-token:" + loggedInUser.getToken());
//
//            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(StartModuleTab4MonitorAndCovidQaScreen.this, nameValuePairList, PROGRAM_SERVICE, StartModuleTab4MonitorAndCovidQaScreen.this,headers);
//            postWebServiceAsync.execute(webServiceUrl);
//
//        } else {
//            Toast.makeText(StartModuleTab4MonitorAndCovidQaScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//        }
//    }

    private void doucmentAPI() {
        if (Utilities.isNetworkAvailable(StartModuleTab4MonitorAndCovidQaScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/document_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(StartModuleTab4MonitorAndCovidQaScreen.this, nameValuePairList, DOCUMENT_SERVICE, StartModuleTab4MonitorAndCovidQaScreen.this,headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleTab4MonitorAndCovidQaScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case DOCUMENT_SERVICE:

                System.out.println("RES::"+response);

                if (response == null) {
                    Toast.makeText(StartModuleTab4MonitorAndCovidQaScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        documentsBeanArrayList.clear();
                        if (status) {
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                DocumentsBean documentsBean = new DocumentsBean();
                                documentsBean.setImage_url(jsonObject.getString("image_url"));
                                documentsBean.setTitle(jsonObject.getString("title"));
                                documentsBean.setAcademy_id(jsonObject.getString("academy_id"));
                                documentsBean.setFile_name(jsonObject.getString("file_name"));
                                documentsBean.setReport_type(jsonObject.getString("report_type"));
                                documentsBeanArrayList.add(documentsBean);
                            }

                            StartModuleTab4MonitorAndCovidAdapter parentOnlineShoppingAdapter = new StartModuleTab4MonitorAndCovidAdapter(StartModuleTab4MonitorAndCovidQaScreen.this, documentsBeanArrayList);
                            listView.setAdapter(parentOnlineShoppingAdapter);

                        } else {
                            Toast.makeText(StartModuleTab4MonitorAndCovidQaScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleTab4MonitorAndCovidQaScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

        }
    }
}