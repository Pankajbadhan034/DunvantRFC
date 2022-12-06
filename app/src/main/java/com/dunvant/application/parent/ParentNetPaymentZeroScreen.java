package com.dunvant.application.parent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.startModule.StartModuleDashboardScreen;

import androidx.appcompat.app.AppCompatActivity;

public class ParentNetPaymentZeroScreen extends AppCompatActivity {
    Button goToDashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_net_payment_zero_activity);
        goToDashboard = (Button) findViewById(R.id.goToDashboard);
        TextView orderID = (TextView) findViewById(R.id.orderID);

        String orderIdStr = getIntent().getStringExtra("orderID");
        orderID.setText("Order ID : "+orderIdStr);

        goToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent mainScreen = new Intent(ParentNetPaymentZeroScreen.this, ParentMainScreen.class);
//                mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(mainScreen);

                Intent mainScreen = new Intent(ParentNetPaymentZeroScreen.this, StartModuleDashboardScreen.class);
                mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainScreen);

            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
//        Intent mainScreen = new Intent(ParentNetPaymentZeroScreen.this, ParentMainScreen.class);
//        mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(mainScreen);

        Intent mainScreen = new Intent(ParentNetPaymentZeroScreen.this, StartModuleDashboardScreen.class);
        mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainScreen);
    }
}