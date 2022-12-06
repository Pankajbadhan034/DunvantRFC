package com.dunvant.application.startModule;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dunvant.application.R;

import androidx.appcompat.app.AppCompatActivity;

public class StartModuleAnalysisDescriptionScreen extends AppCompatActivity {
    TextView title;
    ImageView backButton;
    String descStr;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_analysis_description_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        text = findViewById(R.id.text);

        descStr = getIntent().getStringExtra("desc");
        text.setText(Html.fromHtml(descStr));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}