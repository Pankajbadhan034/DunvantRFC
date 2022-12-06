package com.dunvant.application.startModule;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dunvant.application.R;
import com.dunvant.application.child.fragments.ChildViewMarksFragment;

import androidx.appcompat.app.AppCompatActivity;

public class StartModuleTab4ReportsScreen extends AppCompatActivity {
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_tab4_reports_activity);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ChildViewMarksFragment childViewMarksFragment = new ChildViewMarksFragment();
        StartModuleTab4ReportsScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, childViewMarksFragment)
                .commit();

    }
}