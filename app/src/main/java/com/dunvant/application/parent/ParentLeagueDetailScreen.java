package com.dunvant.application.parent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.LeagueBean;

import androidx.appcompat.app.AppCompatActivity;

public class ParentLeagueDetailScreen extends AppCompatActivity {

    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    ImageView leagueImage;
    TextView leagueName;
    TextView country;
    TextView leagueDates;
    TextView leagueDescription;
    TextView joinLeague;

    LeagueBean clickedOnLeague;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_league_detail_screen);

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        leagueImage = (ImageView) findViewById(R.id.leagueImage);
        leagueName = (TextView) findViewById(R.id.leagueName);
        country = (TextView) findViewById(R.id.country);
        leagueDates = (TextView) findViewById(R.id.leagueDates);
        leagueDescription = (TextView) findViewById(R.id.leagueDescription);
        joinLeague = (TextView) findViewById(R.id.joinLeague);

        changeFonts();

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentLeagueDetailScreen.this));
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
            clickedOnLeague = (LeagueBean) intent.getSerializableExtra("clickedOnLeague");

            imageLoader.displayImage(clickedOnLeague.getFilePath(), leagueImage, options);
            leagueName.setText(clickedOnLeague.getLeagueName());
            leagueDates.setText(clickedOnLeague.getShowFromDate()+" - "+clickedOnLeague.getShowToDate());
            leagueDescription.setText(clickedOnLeague.getLeagueDescription());
            title.setText(clickedOnLeague.getLeagueName());
        }

        joinLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent joinLeagueScreen = new Intent(ParentLeagueDetailScreen.this, ParentJoinLeagueScreen.class);
                joinLeagueScreen.putExtra("clickedOnLeague", clickedOnLeague);
                startActivity(joinLeagueScreen);
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
        title.setTypeface(linoType);
        leagueName.setTypeface(helvetica);
        country.setTypeface(helvetica);
        leagueDates.setTypeface(helvetica);
        leagueDescription.setTypeface(helvetica);
        joinLeague.setTypeface(linoType);
    }

}