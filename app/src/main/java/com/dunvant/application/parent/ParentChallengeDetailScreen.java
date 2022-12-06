package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChallengeBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParentChallengeDetailScreen extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView lblTitle;
    ImageView profileImage;
    TextView title;
    TextView timer;
    TextView challengeBy;
    TextView categoryName;
    TextView lblExpirationDate;
    TextView expirationDate;
    TextView lblTarget;
    TextView targetScore;
    TextView lblTargetTime;
    TextView targetTime;
    ImageView challengeImage;
    TextView description;
    Button approveButton;
    Button leaderboardButton;

    LinearLayout yourScoreLinear;
    LinearLayout yourTimeLinear;

    ChallengeBean clickedOnChallenge;

    CountDownTimer countDownTimer;

    final String APPROVE_CHALLENGE = "APPROVE_CHALLENGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_challege_detail_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        lblTitle = (TextView) findViewById(R.id.lblTitle);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        title = (TextView) findViewById(R.id.title);
        timer = (TextView) findViewById(R.id.timer);
        challengeBy = (TextView) findViewById(R.id.challengeBy);
        categoryName = (TextView) findViewById(R.id.categoryName);
        lblExpirationDate = (TextView) findViewById(R.id.lblExpirationDate);
        expirationDate = (TextView) findViewById(R.id.expirationDate);
        lblTarget = (TextView) findViewById(R.id.lblTarget);
        targetScore = (TextView) findViewById(R.id.targetScore);
        lblTargetTime = (TextView) findViewById(R.id.lblTargetTime);
        targetTime = (TextView) findViewById(R.id.targetTime);
        challengeImage = (ImageView) findViewById(R.id.challengeImage);
        description = (TextView) findViewById(R.id.description);
        approveButton = (Button) findViewById(R.id.approveButton);
        leaderboardButton = (Button) findViewById(R.id.leaderboardButton);

        yourScoreLinear = (LinearLayout) findViewById(R.id.yourScoreLinear);
        yourTimeLinear = (LinearLayout) findViewById(R.id.yourTimeLinear);

        changeFonts();

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnChallenge = (ChallengeBean) intent.getSerializableExtra("clickedOnChallenge");

            title.setText(clickedOnChallenge.getChallengeTitle());
            challengeBy.setText(clickedOnChallenge.getCoachName());
            categoryName.setText(clickedOnChallenge.getCategoryName());
            expirationDate.setText(clickedOnChallenge.getShowExpiration());
//            targetScore.setText(clickedOnChallenge.getTargetScore());
            targetScore.setText(clickedOnChallenge.getAchievedScore());
//            targetTime.setText(clickedOnChallenge.getTargetTime());
            targetTime.setText(clickedOnChallenge.getAchievedTime());
            description.setText(clickedOnChallenge.getChallengeDescription());

            float targetScoreFloat = 0;
            try{
                targetScoreFloat = Float.parseFloat(clickedOnChallenge.getTargetScore());
            }catch (NumberFormatException e){
                e.printStackTrace();
            }

            if(clickedOnChallenge.getTargetScore() == null || clickedOnChallenge.getTargetScore().isEmpty()  || targetScoreFloat == 0){
                yourScoreLinear.setVisibility(View.GONE);
            }

            if(clickedOnChallenge.getTargetTimeTypeFormatted() == null || clickedOnChallenge.getTargetTimeTypeFormatted().isEmpty()){
                yourTimeLinear.setVisibility(View.GONE);
            }

            if(clickedOnChallenge.getChallengeResult() != null) {
                if(clickedOnChallenge.getChallengeResult().equalsIgnoreCase("0")){
                    approveButton.setVisibility(View.GONE);
                } else if (clickedOnChallenge.getChallengeResult().equalsIgnoreCase("1")){
                    if(clickedOnChallenge.getApprovalRequired() != null){
                        if(clickedOnChallenge.getApprovalRequired().equalsIgnoreCase("1")){
                            approveButton.setVisibility(View.VISIBLE);
                        } else {
                            approveButton.setVisibility(View.GONE);
                        }
                    }
                } else if (clickedOnChallenge.getChallengeResult().equalsIgnoreCase("2")){
                    approveButton.setVisibility(View.GONE);
                } else {
                    approveButton.setVisibility(View.GONE);
                }
            }

            /*DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                Date date = dateFormat.parse(clickedOnChallenge.getExpiration());

                long timeDifferenceMilliseconds = date.getTime() - new Date().getTime();
                long diffMinutes = timeDifferenceMilliseconds / (60 * 1000);

                if (diffMinutes <= 59) {


                    countDownTimer = new CountDownTimer(timeDifferenceMilliseconds, 1000) {
                        public void onTick(long millisUntilFinished) {
                            timer.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                        }
                        @Override
                        public void onFinish() {

                        }
                    }.start();


                } else {
                    String strTimer = Utilities.getDifferenceBtwTime(date);
                    timer.setText(strTimer);
                }

            } catch (ParseException e) {
                e.printStackTrace();
                timer.setText("Not Available");
                Toast.makeText(ParentChallengeDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }*/

        }

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utilities.isNetworkAvailable(ParentChallengeDetailScreen.this)) {

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("challengers_id", clickedOnChallenge.getChallengersId()));

                    String webServiceUrl = Utilities.BASE_URL + "challenges/approve_challenge_by_parent";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentChallengeDetailScreen.this, nameValuePairList, APPROVE_CHALLENGE, ParentChallengeDetailScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(ParentChallengeDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent leaderboardScreen = new Intent(ParentChallengeDetailScreen.this, ParentLeaderboardScreen.class);
                leaderboardScreen.putExtra("clickedOnChallenge", clickedOnChallenge);
                startActivity(leaderboardScreen);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changeFonts(){
        lblTitle.setTypeface(linoType);
        title.setTypeface(linoType);
        timer.setTypeface(linoType);
        challengeBy.setTypeface(helvetica);
        lblExpirationDate.setTypeface(linoType);
        expirationDate.setTypeface(helvetica);
        lblTarget.setTypeface(linoType);
        targetScore.setTypeface(helvetica);
        lblTargetTime.setTypeface(linoType);
        targetTime.setTypeface(helvetica);
        description.setTypeface(helvetica);
        approveButton.setTypeface(linoType);
        leaderboardButton.setTypeface(linoType);
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case APPROVE_CHALLENGE:

                if(response == null) {
                    Toast.makeText(ParentChallengeDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(ParentChallengeDetailScreen.this, message, Toast.LENGTH_SHORT).show();

                        if(status) {
                            approveButton.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentChallengeDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}