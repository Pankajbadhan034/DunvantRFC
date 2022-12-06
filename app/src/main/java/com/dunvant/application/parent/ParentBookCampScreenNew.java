package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CampBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.fragments.ParentBookCampDaysFragment;
import com.dunvant.application.parent.fragments.ParentBookCampWeeksFragment;
import com.dunvant.application.utils.Utilities;

public class ParentBookCampScreenNew extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView campName;
    TextView timings;
    TextView age;
    RadioButton bookDays;
    RadioButton bookWeeks;

    CampBean clickedOnCamp;
    int sessionPosition;
    int locationPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_book_camp_screen_new);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = findViewById(R.id.backButton);
        campName = findViewById(R.id.campName);
        timings = findViewById(R.id.timings);
        age = findViewById(R.id.age);
        bookDays = findViewById(R.id.bookDays);
        bookWeeks = findViewById(R.id.bookWeeks);

        changeFonts();

        bookDays.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ParentBookCampDaysFragment parentBookCampDaysFragment = new ParentBookCampDaysFragment(clickedOnCamp, locationPosition, sessionPosition);
                    ParentBookCampScreenNew.this.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainFrameLayout, parentBookCampDaysFragment)
                            .commit();
                }
            }
        });

        bookWeeks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ParentBookCampWeeksFragment parentBookCampWeeksFragment = new ParentBookCampWeeksFragment(clickedOnCamp, locationPosition, sessionPosition);
                    ParentBookCampScreenNew.this.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainFrameLayout, parentBookCampWeeksFragment)
                            .commit();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            clickedOnCamp = (CampBean) intent.getSerializableExtra("clickedOnCamp");
            sessionPosition = intent.getIntExtra("sessionPosition", -1);
            locationPosition = intent.getIntExtra("locationPosition", -1);

            campName.setText(clickedOnCamp.getCampName());
            timings.setText(clickedOnCamp.getSessionsList().get(sessionPosition).getShowFromTime()+" - "+clickedOnCamp.getSessionsList().get(sessionPosition).getShowToTime());
            age.setText(clickedOnCamp.getSessionsList().get(sessionPosition).getGroupName());

            String perDayCost = clickedOnCamp.getSessionsList().get(sessionPosition).getPerDayCost();
            if(perDayCost == null || perDayCost.isEmpty() || perDayCost.equalsIgnoreCase("0.00") || perDayCost.equalsIgnoreCase("0") || perDayCost.equalsIgnoreCase("null")){
                bookDays.setVisibility(View.GONE);
                bookWeeks.setChecked(true);
            } else {
                bookDays.setChecked(true);
            }

            String perWeekCost = clickedOnCamp.getSessionsList().get(sessionPosition).getPerWeekCost();
            if(perWeekCost == null || perWeekCost.isEmpty() || perWeekCost.equalsIgnoreCase("0.00") || perWeekCost.equalsIgnoreCase("0") || perWeekCost.equalsIgnoreCase("null")){
                bookWeeks.setVisibility(View.GONE);
            }
        }
    }

    private void changeFonts(){
        campName.setTypeface(linoType);
        timings.setTypeface(helvetica);
        age.setTypeface(helvetica);
    }
}