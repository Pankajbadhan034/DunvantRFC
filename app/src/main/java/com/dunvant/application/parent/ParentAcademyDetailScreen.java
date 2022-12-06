package com.dunvant.application.parent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dunvant.application.ApplicationContext;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachingAcademyBean;
import com.dunvant.application.parent.adapters.ParentAcademyGalleryGridAdapter;
import com.dunvant.application.utils.Utilities;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ParentAcademyDetailScreen extends AppCompatActivity {

    ApplicationContext applicationContext;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    TextView ifaFootballAcademy;
    ImageView academyImage;
    TextView academyName;
    //    TextView walkingRange;
    TextView ageGroup;
    TextView academyDescription;
    TextView gallery;
    GridView academyGallery;
    TextView bookTrial;
    TextView bookTraining;
    TextView approvalBookingText;

    CoachingAcademyBean clickedOnAcademy;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    boolean bln = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_academy_detail_screen);

        applicationContext = (ApplicationContext) getApplication();

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentAcademyDetailScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        ifaFootballAcademy = (TextView) findViewById(R.id.ifaFootballAcademy);
        academyImage = (ImageView) findViewById(R.id.academyImage);
        academyName = (TextView) findViewById(R.id.academyName);
//        walkingRange = (TextView) findViewById(R.id.walkingRange);
        ageGroup = (TextView) findViewById(R.id.ageGroup);
        academyDescription = (TextView) findViewById(R.id.academyDescription);
        gallery = (TextView) findViewById(R.id.gallery);
        academyGallery = (GridView) findViewById(R.id.academyGallery);
        bookTrial = (TextView) findViewById(R.id.bookTrial);
        bookTraining = (TextView) findViewById(R.id.bookTraining);
        approvalBookingText = (TextView) findViewById(R.id.approvalBookingText);

        changeFonts();

        Intent intent = getIntent();
        if (intent != null) {
            clickedOnAcademy = (CoachingAcademyBean) intent.getSerializableExtra("clickedOnAcademy");

            imageLoader.displayImage(clickedOnAcademy.getFilePath(), academyImage, options);
            academyName.setText(clickedOnAcademy.getCoachingProgramName());
            ageGroup.setText("");
            academyDescription.setText(Html.fromHtml(clickedOnAcademy.getDescription()));

            if (clickedOnAcademy.getDescription().length() >= 125) {
                academyDescription.setText(clickedOnAcademy.getDescription().substring(0, 125) + " ...more");
                bln = false;
            } else {
//                academyDescription.setText(clickedOnAcademy.getDescription()+" ...less");
                academyDescription.setText(clickedOnAcademy.getDescription());
                bln = true;
            }

            academyDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (clickedOnAcademy.getDescription().length() >= 125) {
                        if (bln) {
                            academyDescription.setText(clickedOnAcademy.getDescription().substring(0, 125) + " ...more");
                            bln = false;
                        } else {
                            academyDescription.setText(clickedOnAcademy.getDescription());
                            bln = true;
                        }
                    }

                }
            });

            academyGallery.setAdapter(new ParentAcademyGalleryGridAdapter(ParentAcademyDetailScreen.this, clickedOnAcademy.getGalleryList()));
            try {
                Utilities.setGridViewHeightBasedOnChildren(academyGallery, 3);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (clickedOnAcademy.getIsTrial().equalsIgnoreCase("1")) {
                approvalBookingText.setVisibility(View.VISIBLE);
                bookTrial.setVisibility(View.VISIBLE);
                bookTraining.setVisibility(View.GONE);
            } else {
                approvalBookingText.setVisibility(View.GONE);
                bookTraining.setVisibility(View.VISIBLE);
                bookTrial.setVisibility(View.GONE);
            }
        }

        academyGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent galleryScreen = new Intent(ParentAcademyDetailScreen.this, ParentAcademyGalleryScreen.class);
                galleryScreen.putExtra("clickedOnAcademy", clickedOnAcademy);
                galleryScreen.putExtra("imagePosition", position);
                startActivity(galleryScreen);
            }
        });

        bookTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryScreen = new Intent(ParentAcademyDetailScreen.this, ParentRequestTrialScreen.class);
                galleryScreen.putExtra("clickedOnAcademy", clickedOnAcademy);
                startActivity(galleryScreen);
            }
        });

        bookTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                applicationContext.setClickedOnAcademy(clickedOnAcademy);

                Intent bookAcademyOne = new Intent(ParentAcademyDetailScreen.this, ParentBookAcademyOne.class);
//                bookAcademyOne.putExtra("clickedOnAcademy", clickedOnAcademy);
                startActivity(bookAcademyOne);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void changeFonts() {
        title.setTypeface(linoType);
        ifaFootballAcademy.setTypeface(helvetica);
        academyName.setTypeface(helvetica);
//        walkingRange.setTypeface(helvetica);
        ageGroup.setTypeface(helvetica);
        academyDescription.setTypeface(helvetica);
        gallery.setTypeface(helvetica);
        bookTrial.setTypeface(linoType);
        bookTraining.setTypeface(linoType);
        approvalBookingText.setTypeface(linoType);
    }

}