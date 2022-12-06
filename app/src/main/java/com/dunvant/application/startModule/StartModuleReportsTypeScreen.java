package com.dunvant.application.startModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.utils.CircularTextView;

import androidx.appcompat.app.AppCompatActivity;

public class StartModuleReportsTypeScreen extends AppCompatActivity {
    ImageView backButton;
    TextView title;
    CircularTextView image;
    CircularTextView image1;
    CircularTextView image2;
    RelativeLayout relative1;
    RelativeLayout relative2;
    RelativeLayout relative3;
    String typeViewMarksStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_reports_type_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        image = findViewById(R.id.image);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        relative1 = findViewById(R.id.relative1);
        relative2 = findViewById(R.id.relative2);
        relative3 = findViewById(R.id.relative3);

        typeViewMarksStr = getIntent().getStringExtra("typeViewMarks");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //image.setText("D");
        image.setStrokeWidth(1);
        image.setStrokeColor("#333333");
        image.setSolidColor("#dbdbdb");
        image.setTextColor(getResources().getColor(R.color.darkBlue));

       // image1.setText("C");
        image1.setStrokeWidth(1);
        image1.setStrokeColor("#333333");
        image1.setSolidColor("#dbdbdb");
        image1.setTextColor(getResources().getColor(R.color.darkBlue));

       // image.setText("D");
        image2.setStrokeWidth(1);
        image2.setStrokeColor("#333333");
        image2.setSolidColor("#dbdbdb");
        image2.setTextColor(getResources().getColor(R.color.darkBlue));

        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(typeViewMarksStr.equalsIgnoreCase("parent")){
                    Intent obj = new Intent(StartModuleReportsTypeScreen.this, StartModuleParentViewMarksScreen.class);
                    startActivity(obj);
                }else if(typeViewMarksStr.equalsIgnoreCase("child")){
                    Intent obj = new Intent(StartModuleReportsTypeScreen.this, StartModuleParentViewMarksScreen.class);
                    startActivity(obj);
//                    Intent obj = new Intent(StartModuleReportsTypeScreen.this, StartModuleParentCovidReportsViewScreen.class);
//                    startActivity(obj);
                }
            }
        });

        relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Intent obj = new Intent(StartModuleReportsTypeScreen.this, StartModuleParentCovidReportsViewScreen.class);
                        obj.putExtra("reportType", "2");
                        obj.putExtra("nameReport", "COVID WELLNESS REPORTS");
                        startActivity(obj);
            }
        });

        relative3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(StartModuleReportsTypeScreen.this, StartModuleParentCovidReportsViewScreen.class);
                obj.putExtra("reportType", "1");
                obj.putExtra("nameReport", "DAILY MONITORING REPORTS");
                startActivity(obj);
            }
        });
    }
}