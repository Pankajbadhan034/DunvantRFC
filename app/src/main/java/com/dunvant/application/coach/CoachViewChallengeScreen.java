package com.dunvant.application.coach;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChallengeListBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CoachViewChallengeScreen extends AppCompatActivity {

    ImageView backButton;
    ImageView challengeImage;
    TextView title;
    TextView locationName;
    TextView session;
    TextView dateTime;
    TextView membersAll;
    TextView targetScore;
    TextView targetTime;
    TextView expiration;
    LinearLayout targetScoreLinearLayout;
    LinearLayout targetTimeLinearLayout;

    ChallengeListBean clickedOnChallenge;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_activity_view_challenge_screen);

        backButton = (ImageView) findViewById(R.id.backButton);
        challengeImage = (ImageView) findViewById(R.id.challengeImage);
        title = (TextView) findViewById(R.id.title);
        locationName = (TextView) findViewById(R.id.locationName);
        session = (TextView) findViewById(R.id.session);
        dateTime = (TextView) findViewById(R.id.dateTime);
        membersAll = (TextView) findViewById(R.id.membersAll);
        targetScore = (TextView) findViewById(R.id.targetScore);
        targetTime = (TextView) findViewById(R.id.targetTime);
        expiration = (TextView) findViewById(R.id.expiration);
        targetScoreLinearLayout = (LinearLayout) findViewById(R.id.targetScoreLinearLayout);
        targetTimeLinearLayout = (LinearLayout) findViewById(R.id.targetTimeLinearLayout);

        imageLoader.init(ImageLoaderConfiguration.createDefault(CoachViewChallengeScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnChallenge = (ChallengeListBean) intent.getSerializableExtra("clickedOnChallenge");
            imageLoader.displayImage(clickedOnChallenge.getChallengeImageUrl(), challengeImage, options);

            if(clickedOnChallenge.getChallengeImageUrl() == null || clickedOnChallenge.getChallengeImageUrl().isEmpty() || clickedOnChallenge.getChallengeImageUrl().equalsIgnoreCase("null")){
                challengeImage.setVisibility(View.GONE);
            }

            title.setText(clickedOnChallenge.getTitle());
            locationName.setText(clickedOnChallenge.getLocationNames());
            session.setText(clickedOnChallenge.getDayLabel());
            dateTime.setText(clickedOnChallenge.getShowExpirationDate()+" "+clickedOnChallenge.getShowExpirationTime());
            targetScore.setText(clickedOnChallenge.getTargetScore());
            targetTime.setText(clickedOnChallenge.getTargetTimeFormatted());
            expiration.setText(clickedOnChallenge.getExpiration());

            float targetScoreFloat = 0;
            try{
                targetScoreFloat = Float.parseFloat(clickedOnChallenge.getTargetScore());
            }catch (NumberFormatException e){
                e.printStackTrace();
            }

            if(clickedOnChallenge.getTargetScore() == null || clickedOnChallenge.getTargetScore().isEmpty()  || targetScoreFloat == 0){
                targetScoreLinearLayout.setVisibility(View.GONE);
            }

            if(clickedOnChallenge.getTargetTimeFormatted() == null || clickedOnChallenge.getTargetTimeFormatted().isEmpty()){
                targetTimeLinearLayout.setVisibility(View.GONE);
            }

//            membersAll.setText();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}