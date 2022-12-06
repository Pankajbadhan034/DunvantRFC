package com.dunvant.application.coach;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.DatesResultBean;
import com.dunvant.application.coach.adapters.CoachSessionDatesAdapter;

public class CoachViewSessionDatesScreen extends AppCompatActivity {

    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    TextView childName;
    TextView groupName;
    ListView datesListView;
//    Button move;

    DatesResultBean clickedOnDateResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_activity_view_session_dates_screen);

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        childName = (TextView) findViewById(R.id.childName);
        groupName = (TextView) findViewById(R.id.groupName);
        datesListView = (ListView) findViewById(R.id.datesListView);
//        move = (Button) findViewById(R.id.move);

        changeFonts();

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnDateResult = (DatesResultBean) intent.getSerializableExtra("clickedOnDateResult");

            childName.setText(clickedOnDateResult.getChildName());
            groupName.setText(clickedOnDateResult.getGroupName());
            String datesArray[] = clickedOnDateResult.getBookingDates().split(",");

            datesListView.setAdapter(new CoachSessionDatesAdapter(CoachViewSessionDatesScreen.this, datesArray));
        }

        /*move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveChild = new Intent(CoachViewSessionDatesScreen.this, CoachMoveChildScreen.class);
                moveChild.putExtra("clickedOnDateResult", clickedOnDateResult);
                startActivity(moveChild);
            }
        });*/

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changeFonts(){
        title.setTypeface(linoType);
        childName.setTypeface(linoType);
        groupName.setTypeface(linoType);
//        move.setTypeface(linoType);
    }
}