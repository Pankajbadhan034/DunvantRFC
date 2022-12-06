package com.dunvant.application.startModule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.fragments.ChildPostFragment;
import com.dunvant.application.coach.fragments.CoachManageAttendanceFragment;
import com.dunvant.application.coach.fragments.CoachManageScoresFragment;
import com.dunvant.application.coach.fragments.CoachManageTimelineFragment;
import com.dunvant.application.parent.fragments.ParentBookNowFragment;
import com.dunvant.application.parent.fragments.ParentManageChildrenFragment;
import com.dunvant.application.parent.fragments.ParentManageTimelineFragment;
import com.dunvant.application.startModule.fragment.CoachAreaFragment;
import com.dunvant.application.utils.Utilities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class StartModuleTab4NewsFeedScreen extends AppCompatActivity {
    ImageView backButton;
    String typeStr;
    TextView title;
    SharedPreferences sharedPreferences;
    boolean isUserLoggedIn;
    UserBean loggedInUser;
    ImageView searchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_news_feed_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        searchImageView = findViewById(R.id.searchImageView);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false);

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout);
                ((CoachManageAttendanceFragment) f).showSearchLayout();
            }
        });

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

        typeStr = getIntent().getStringExtra("type");

        if(typeStr.equalsIgnoreCase("IDP's")){
            title.setText("IDP'S");
            searchImageView.setVisibility(View.GONE);

            CoachManageScoresFragment coachManageScoresFragment = new CoachManageScoresFragment();
            StartModuleTab4NewsFeedScreen.this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrameLayout, coachManageScoresFragment)
                    .commit();
        }else if(typeStr.equalsIgnoreCase("ATTENDANCE")){
            title.setText("ATTENDANCE");
            searchImageView.setVisibility(View.VISIBLE);
            CoachManageAttendanceFragment coachManageAttendanceFragment = new CoachManageAttendanceFragment();
            StartModuleTab4NewsFeedScreen.this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrameLayout, coachManageAttendanceFragment)
                    .commit();
        }else if(typeStr.equalsIgnoreCase("BOOK NOW")){
            title.setText("ATTENDANCE");
            searchImageView.setVisibility(View.GONE);
            ParentBookNowFragment parentBookNowFragment = new ParentBookNowFragment();
            StartModuleTab4NewsFeedScreen.this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrameLayout, parentBookNowFragment)
                    .commit();
        }else if(typeStr.equalsIgnoreCase("NEWSFEED")){
            title.setText("NEWSFEED");
            searchImageView.setVisibility(View.GONE);

            if(loggedInUser.getRoleCode().equalsIgnoreCase("coach_role")){
                CoachManageTimelineFragment coachManageTimelineFragment = new CoachManageTimelineFragment();
                StartModuleTab4NewsFeedScreen.this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFrameLayout, coachManageTimelineFragment)
                        .commit();
            }else{
                ParentManageTimelineFragment parentManageTimelineFragment = new ParentManageTimelineFragment();
                StartModuleTab4NewsFeedScreen.this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainFrameLayout, parentManageTimelineFragment)
                        .commit();
            }


        }else if(typeStr.equalsIgnoreCase("NEWSFEED + POST")){
            title.setText("NEWSFEED");
            searchImageView.setVisibility(View.GONE);
            ChildPostFragment childPostFragment = new ChildPostFragment();
            StartModuleTab4NewsFeedScreen.this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrameLayout, childPostFragment)
                    .commit();
        }else if(typeStr.equalsIgnoreCase("COACH AREA")){
            title.setText("COACH AREA");
            searchImageView.setVisibility(View.GONE);
            CoachAreaFragment childPostFragment = new CoachAreaFragment();
            StartModuleTab4NewsFeedScreen.this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrameLayout, childPostFragment)
                    .commit();
        }else if(typeStr.equalsIgnoreCase("My Participants")){
            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
            title.setText("MY "+verbiage_plural.toUpperCase());
            ParentManageChildrenFragment parentManageChildrenFragment = new ParentManageChildrenFragment();
            StartModuleTab4NewsFeedScreen.this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrameLayout, parentManageChildrenFragment)
                    .commit();
        }




    }
}