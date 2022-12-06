package com.dunvant.application.child;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ChildManageChallengesDetailScreen extends AppCompatActivity {
    LinearLayout linearTargetScore;
    LinearLayout linearTargetTime;
    TextView timer;
    TextView lblExpirationDate;
    TextView lblTarget;
    TextView lblTargetTime;
    TextView lblTitle;
    ImageView profileImage;
    TextView title;
    TextView challengeBy;
    TextView expirationDate;
    TextView targetScore;
    TextView targetTime;
    ImageView challengeImage;
    TextView description;
    Button achievedClick;
    ImageView backButton;
    Typeface helvetica;
    Typeface linoType;

    String achievedScore, achievedTime;
    String strTypeOfChallenge, challengeResult, isChallengeExpired;
    String targetTimeNew, CategoryName, timerStr, isSharedStr, challengesIdStr, profileImageStr, titleStr, challengeByStr, expirationDateStr, targetScroreStr, targetTimeStr, challengeImaeStr, descriptionStr;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    Button leaderBoardClick;

    TextView yourScore;
    TextView yourTime;
    TextView yourScoreLabel;
    TextView yourTimeLabel;
    LinearLayout linearYourScore;
    LinearLayout linearYourTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_manage_challenges_detail_screen);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        profileImageStr = getIntent().getStringExtra("coachDp");
        titleStr = getIntent().getStringExtra("title");
        challengeByStr = getIntent().getStringExtra("byCoach");
        expirationDateStr = getIntent().getStringExtra("expireDate");
        targetScroreStr = getIntent().getStringExtra("targetScore");
        targetTimeStr = getIntent().getStringExtra("targetTime");
        challengeImaeStr = getIntent().getStringExtra("imageUrl");
        descriptionStr = getIntent().getStringExtra("description");
        challengesIdStr = getIntent().getStringExtra("challengesId");
        isSharedStr = getIntent().getStringExtra("isShared");
        timerStr = getIntent().getStringExtra("timer");
        strTypeOfChallenge = getIntent().getStringExtra("typeOfChallenge");
        CategoryName = getIntent().getStringExtra("CategoryName");
        challengeResult = getIntent().getStringExtra("challengeResult");

        isChallengeExpired = getIntent().getStringExtra("isChallengeExpired");

        achievedTime = getIntent().getStringExtra("achievedTime");
        achievedScore = getIntent().getStringExtra("achievedScore");
        targetTimeNew = getIntent().getStringExtra("targetTimeNew");


        leaderBoardClick = (Button) findViewById(R.id.leaderBoardClick);

        imageLoader.init(ImageLoaderConfiguration.createDefault(ChildManageChallengesDetailScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        lblTitle = (TextView) findViewById(R.id.lblTitle);
        lblTarget = (TextView) findViewById(R.id.lblTarget);
        lblTargetTime = (TextView) findViewById(R.id.lblTargetTime);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        title = (TextView) findViewById(R.id.title);
        challengeBy = (TextView) findViewById(R.id.challengeBy);
        expirationDate = (TextView) findViewById(R.id.expirationDate);
        targetScore = (TextView) findViewById(R.id.targetScore);
        targetTime = (TextView) findViewById(R.id.targetTime);
        challengeImage = (ImageView) findViewById(R.id.challengeImage);
        description = (TextView) findViewById(R.id.description);
        achievedClick = (Button) findViewById(R.id.achievedClick);
        backButton = (ImageView) findViewById(R.id.backButton);
        lblExpirationDate = (TextView) findViewById(R.id.lblExpirationDate);
        timer = (TextView) findViewById(R.id.timer);
        linearTargetTime = (LinearLayout) findViewById(R.id.linearTargetTime);
        linearTargetScore = (LinearLayout) findViewById(R.id.linearTargetScore);
        yourScore = (TextView) findViewById(R.id.yourScore);
        yourTime = (TextView) findViewById(R.id.yourTime);
        yourScoreLabel = (TextView) findViewById(R.id.yourScoreLabel);
        yourTimeLabel = (TextView) findViewById(R.id.yourTimeLabel);
        linearYourScore = (LinearLayout) findViewById(R.id.linearYourScore);
        linearYourTime = (LinearLayout) findViewById(R.id.linearYourTime);

        lblTitle.setTypeface(linoType);
        title.setTypeface(helvetica);
        challengeBy.setTypeface(helvetica);
        expirationDate.setTypeface(helvetica);
        targetScore.setTypeface(helvetica);
        targetTime.setTypeface(helvetica);
        description.setTypeface(helvetica);
        achievedClick.setTypeface(helvetica);
        lblExpirationDate.setTypeface(helvetica);
        lblTarget.setTypeface(helvetica);
        lblTargetTime.setTypeface(helvetica);
        timer.setTypeface(helvetica);
        leaderBoardClick.setTypeface(linoType);
        yourScore.setTypeface(helvetica);
        yourTime.setTypeface(helvetica);
        yourScoreLabel.setTypeface(helvetica);
        yourTimeLabel.setTypeface(helvetica);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       // lblTitle.setText(strTypeOfChallenge);
        title.setText(titleStr);
        challengeBy.setText(challengeByStr+ " (" + CategoryName+")");
        expirationDate.setText(expirationDateStr);
        targetScore.setText(targetScroreStr);
        targetTime.setText(targetTimeStr);
        description.setText(descriptionStr);
        timer.setText(timerStr);
        yourScore.setText(achievedScore);
        yourTime.setText(achievedTime);

        imageLoader.displayImage(profileImageStr, profileImage, options);

        if(challengeImaeStr.equalsIgnoreCase("null")){
            challengeImage.setVisibility(View.GONE);
        }else{
            challengeImage.setVisibility(View.VISIBLE);
            imageLoader.displayImage(challengeImaeStr, challengeImage, options);
        }


        if(isChallengeExpired.equalsIgnoreCase("true")){
            achievedClick.setVisibility(View.GONE);
        }else{
            if(challengeResult.equalsIgnoreCase("1")){
                achievedClick.setVisibility(View.VISIBLE);
                achievedClick.setText("ACHIEVED");
            }else{
                achievedClick.setVisibility(View.VISIBLE);
                achievedClick.setText("RETAKE CHALLENGE");
            }

        }

        if(strTypeOfChallenge.equalsIgnoreCase("Expired")){
            achievedClick.setVisibility(View.GONE);
        }

        if(targetScroreStr.equalsIgnoreCase("0.00") || targetScroreStr.equalsIgnoreCase("0") || targetScroreStr.equalsIgnoreCase("")){
            linearTargetScore.setVisibility(View.GONE);
        }

        if(targetTimeStr.equalsIgnoreCase("0.00") || targetTimeStr.equalsIgnoreCase("0") || targetTimeStr.equalsIgnoreCase("")){
            linearTargetTime.setVisibility(View.GONE);
        }

        if(achievedTime.equalsIgnoreCase("0.00") || achievedTime.equalsIgnoreCase("0") || achievedTime.equalsIgnoreCase("")){
            linearYourTime.setVisibility(View.GONE);
        }

        if(achievedScore.equalsIgnoreCase("0.00") || achievedScore.equalsIgnoreCase("0") || achievedScore.equalsIgnoreCase("")){
            linearYourScore.setVisibility(View.GONE);
        }


        achievedClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(ChildManageChallengesDetailScreen.this, ChildManageChallengesAchievedScreen.class);
                obj.putExtra("challengesId", challengesIdStr);
                obj.putExtra("title", titleStr);
                obj.putExtra("byCoach", challengeByStr);
                obj.putExtra("targetScore", targetScroreStr);
                obj.putExtra("targetTime", targetTimeStr);
                obj.putExtra("isShared", isSharedStr);
                obj.putExtra("timer", timerStr);
                obj.putExtra("targetTimeNew", targetTimeNew);

                startActivity(obj);
                finish();
            }
        });


        leaderBoardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(ChildManageChallengesDetailScreen.this, ChildChallengesLeaderBoardScreen.class);
                obj.putExtra("challengeId", challengesIdStr);
                startActivity(obj);
            }
        });


    }
}
