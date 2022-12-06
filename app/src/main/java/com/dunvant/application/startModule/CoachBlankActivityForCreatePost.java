package com.dunvant.application.startModule;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dunvant.application.R;
import com.dunvant.application.startModule.fragment.CoachCreatePostFragment;

import androidx.appcompat.app.AppCompatActivity;

public class CoachBlankActivityForCreatePost extends AppCompatActivity {

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_blank_activity_for_create_post);

        backButton = findViewById(R.id.backButton);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, new CoachCreatePostFragment())
                .commit();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}