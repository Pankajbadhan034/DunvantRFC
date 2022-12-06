package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.RecycleExpBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentMidWeekPackageChildNamesAttendanceAdapter;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ParentMidWeekPackageChildNamesAttendanceActivity extends AppCompatActivity  {
    String group_name;
    ImageView backButton;
    ListView listView;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    String midweek_sessionIntent;
    Typeface helvetica;
    Typeface linoType;
    TextView groupName;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_mid_week_package_child_names_attendance_activity);
        listView = findViewById(R.id.listView);
        backButton = findViewById(R.id.backButton);
        groupName = findViewById(R.id.groupName);
        title = findViewById(R.id.title);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        group_name = getIntent().getStringExtra("group_name");
        final ArrayList<RecycleExpBean> recycleExpBeanArrayList =  (ArrayList<RecycleExpBean>)getIntent().getSerializableExtra("recycleExpBeanArrayList");

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        midweek_sessionIntent = getIntent().getStringExtra("midweek_session");
        System.out.println("midweek_session:: "+midweek_sessionIntent);

        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
        title.setText(verbiage_singular.toUpperCase()+" NAMES");


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecycleExpBean recycleExpBean = recycleExpBeanArrayList.get(i);
                Intent intent = new Intent(ParentMidWeekPackageChildNamesAttendanceActivity.this, ParentMidWeekPackageAttendanceListActivity.class);
                intent.putExtra("recycleExpBean", recycleExpBean);
                intent.putExtra("group_name", group_name);
                startActivity(intent);
            }
        });

        groupName.setText(group_name);
        ParentMidWeekPackageChildNamesAttendanceAdapter coachMidWeekPackageChildNamesAttendanceAdapter = new ParentMidWeekPackageChildNamesAttendanceAdapter(ParentMidWeekPackageChildNamesAttendanceActivity.this, recycleExpBeanArrayList);
        listView.setAdapter(coachMidWeekPackageChildNamesAttendanceAdapter);

    }
}