package com.dunvant.application.startModule;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dunvant.application.R;

import androidx.appcompat.app.AppCompatActivity;

public class StartModuleContactUs extends AppCompatActivity {
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_module_contact_us);
                backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}