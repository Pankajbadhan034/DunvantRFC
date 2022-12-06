package com.dunvant.application.startModule;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dunvant.application.R;
import com.dunvant.application.coach.fragments.CoachManageScoresFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class StartModuleTab4AttendanceScreen extends AppCompatActivity {
    ImageView backButton;
    ImageView searchImageView;
    String typeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_tab4_attendance_activity);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchImageView = findViewById(R.id.searchImageView);

        typeStr = getIntent().getStringExtra("type");

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout);
                ((CoachManageScoresFragment) f).showSearchLinearLayout();
            }
        });


        if(typeStr.equalsIgnoreCase("IDP's")){
            searchImageView.setVisibility(View.VISIBLE);
        }else{
            searchImageView.setVisibility(View.GONE);
        }

        CoachManageScoresFragment coachManageScoresFragment = new CoachManageScoresFragment();
        StartModuleTab4AttendanceScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, coachManageScoresFragment)
                .commit();

//        CoachManageAttendanceFragment coachManageAttendanceFragment = new CoachManageAttendanceFragment();
//        StartModuleTab4AttendanceScreen.this.getSupportFragmentManager().beginTransaction()
//                .replace(R.id.mainFrameLayout, coachManageAttendanceFragment)
//                .commit();
    }
}