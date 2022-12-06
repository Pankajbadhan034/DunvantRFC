package com.dunvant.application.startModule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CovidQuestionBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.startModule.adapter.StartModuleDailyWellnessAnswersAdapter;
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

public class StartModuleDailyWellnessScreen extends AppCompatActivity implements IWebServiceCallback {
    ImageView backButton;
    ListView listView;
    String strJson;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    ArrayList<CovidQuestionBean> covidQuestionBeanArrayList = new ArrayList<>();
    Button submit;
    TextView name;
    private final String COVID_SERVICE = "COVID_SERVICE";
    private final String SUBMIT_SERVICE = "SUBMIT_SERVICE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_daily_wellness_activity);
        backButton = findViewById(R.id.backButton);
        listView = findViewById(R.id.listView);
        submit = findViewById(R.id.submit);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        name = findViewById(R.id.name);
        name.setText(loggedInUser.getFullName());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = 0;
                try{
                    JSONArray jsonArray = new JSONArray();
                    for(CovidQuestionBean covidQuestionBean: covidQuestionBeanArrayList){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(covidQuestionBean.getId(), covidQuestionBean.getChecked());
                        jsonArray.put(jsonObject);

                        if(covidQuestionBean.getChecked().equalsIgnoreCase("") || covidQuestionBean.getChecked().equalsIgnoreCase("Choose Time")){
                            a= -1;
                            break;
                        }
                    }


                    if(a==-1){
                        Toast.makeText(StartModuleDailyWellnessScreen.this, "Please select all fields", Toast.LENGTH_SHORT).show();
                    }else{
                        strJson = jsonArray.toString();
                        submitAPI();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        });

        covidQuestionsAPI();
    }

    private void covidQuestionsAPI() {
        if (Utilities.isNetworkAvailable(StartModuleDailyWellnessScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));
//            nameValuePairList.add(new BasicNameValuePair("academy_id", "1"));
            nameValuePairList.add(new BasicNameValuePair("category", "1"));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/covid_wellness_question";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(StartModuleDailyWellnessScreen.this, nameValuePairList, COVID_SERVICE, StartModuleDailyWellnessScreen.this,headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleDailyWellnessScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    private void submitAPI() {
        if (Utilities.isNetworkAvailable(StartModuleDailyWellnessScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));
//            nameValuePairList.add(new BasicNameValuePair("academy_id", "1"));
            nameValuePairList.add(new BasicNameValuePair("user_id", loggedInUser.getId()));
            nameValuePairList.add(new BasicNameValuePair("category", "1"));
            nameValuePairList.add(new BasicNameValuePair("report_type", "1"));
            nameValuePairList.add(new BasicNameValuePair("result", strJson));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/submit_wellness_covid_report";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(StartModuleDailyWellnessScreen.this, nameValuePairList, SUBMIT_SERVICE, StartModuleDailyWellnessScreen.this,headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleDailyWellnessScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case COVID_SERVICE:

                System.out.println("RES::"+response);

                if (response == null) {
                    Toast.makeText(StartModuleDailyWellnessScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        covidQuestionBeanArrayList.clear();
                        if (status) {
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                CovidQuestionBean documentsBean = new CovidQuestionBean();
                                documentsBean.setId(jsonObject.getString("id"));
                                documentsBean.setQuestion(jsonObject.getString("question"));
                                documentsBean.setChecked("");
                                covidQuestionBeanArrayList.add(documentsBean);
                            }

                            StartModuleDailyWellnessAnswersAdapter parentOnlineShoppingAdapter = new StartModuleDailyWellnessAnswersAdapter(StartModuleDailyWellnessScreen.this, covidQuestionBeanArrayList);
                            listView.setAdapter(parentOnlineShoppingAdapter);

                        } else {
                            Toast.makeText(StartModuleDailyWellnessScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleDailyWellnessScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case SUBMIT_SERVICE:

                System.out.println("RES::"+response);

                if (response == null) {
                    Toast.makeText(StartModuleDailyWellnessScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if (status) {
                            Toast.makeText(StartModuleDailyWellnessScreen.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                        } else {
                            Toast.makeText(StartModuleDailyWellnessScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleDailyWellnessScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

        }
    }
}