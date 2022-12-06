package com.dunvant.application.startModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.R;
import com.dunvant.application.utils.Utilities;

import androidx.appcompat.app.AppCompatActivity;

public class StartModuleMembershipDetailsScreen extends AppCompatActivity {
    TextView title;
    ImageView backButton;
    TextView description;
    TextView monthlyPrice;
    TextView yearlyPrice;
    TextView purchase;
    String titleStr, descStr, monthlyPriceStr, yearlyPriceStr, idStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_membership_details_activity);
        title = findViewById(R.id.title);
        backButton = findViewById(R.id.backButton);
        description = findViewById(R.id.description);
        monthlyPrice = findViewById(R.id.monthlyPrice);
        yearlyPrice = findViewById(R.id.yearlyPrice);
        purchase = findViewById(R.id.purchase);

        idStr = getIntent().getStringExtra("idStr");
        titleStr = getIntent().getStringExtra("titleStr");
        descStr = getIntent().getStringExtra("descStr");
        monthlyPriceStr = getIntent().getStringExtra("monthlyPriceStr");
        yearlyPriceStr = getIntent().getStringExtra("yearlyPriceStr");

        SharedPreferences sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", "");

        title.setText(titleStr);
        description.setText(Html.fromHtml(descStr));
        monthlyPrice.setText(academy_currency+" "+monthlyPriceStr+"\nMONTHLY");
        yearlyPrice.setText(academy_currency+" "+yearlyPriceStr+"\nYEARLY");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StartModuleMembershipDetailsScreen.this, "Membership plans not availble at the momment.", Toast.LENGTH_SHORT).show();
            }
        });



    }
}