package com.dunvant.application.startModule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CovidQuestionBean;
import com.dunvant.application.beans.CovidViewReportBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.startModule.adapter.StartModuleCovidAnswersViewAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class StartModuleCovidWellnessViewReportScreen extends AppCompatActivity implements IWebServiceCallback {
    ImageView backButton;
    ListView listView;
    String strJson;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    ArrayList<CovidQuestionBean> covidQuestionBeanArrayList = new ArrayList<>();
    Button submit;
    TextView name;
    String titleStr;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_module_covid_wellness_view_report_screen);
        backButton = findViewById(R.id.backButton);
        listView = findViewById(R.id.listView);
        submit = findViewById(R.id.submit);
        title = findViewById(R.id.title);

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

        titleStr = getIntent().getStringExtra("title");
        title.setText(titleStr);


        ArrayList<CovidViewReportBean> myList = (ArrayList<CovidViewReportBean>) getIntent().getSerializableExtra("covidViewReportBeanArrayList");

        StartModuleCovidAnswersViewAdapter parentOnlineShoppingAdapter = new StartModuleCovidAnswersViewAdapter(StartModuleCovidWellnessViewReportScreen.this, myList);
        listView.setAdapter(parentOnlineShoppingAdapter);
    }



    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {


        }
    }
}