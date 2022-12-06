package com.dunvant.application.child;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.dunvant.application.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ChildMyGallerySingleImageScreen extends AppCompatActivity {
    ImageView singleImageGallery;
    VideoView singleVideoGallery;
    TextView likeGallery;
    TextView commentGallery;
    TextView dateGallery;

    TextView title;
    TextView description;

    String type,url,like,comment,date,siteMediaId;
    String strTitle, strDescription;
    ImageView cancel;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    Typeface helvetica;
    Typeface linoType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_my_gallery_single_image_screen);
        singleImageGallery = (ImageView) findViewById(R.id.singleImageGallery);
        singleVideoGallery = (VideoView) findViewById(R.id.singleVideoGallery);
        likeGallery = (TextView) findViewById(R.id.likeGallery);
        commentGallery = (TextView) findViewById(R.id.commentGallery);
        dateGallery = (TextView) findViewById(R.id.dateGallery);
        cancel = (ImageView) findViewById(R.id.cancel);
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        likeGallery.setTypeface(helvetica);
        commentGallery.setTypeface(helvetica);
        dateGallery.setTypeface(helvetica);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageLoader.init(ImageLoaderConfiguration.createDefault(ChildMyGallerySingleImageScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        type = getIntent().getStringExtra("type");
        url = getIntent().getStringExtra("url");
        like = getIntent().getStringExtra("like");
        comment = getIntent().getStringExtra("comment");
        date = getIntent().getStringExtra("date");
        siteMediaId = getIntent().getStringExtra("siteMediaId");
        strTitle = getIntent().getStringExtra("title");
        strDescription = getIntent().getStringExtra("description");

        title.setText(strTitle);
        description.setText(strDescription);
        likeGallery.setText(like);
        commentGallery.setText(comment);
        dateGallery.setText(date);

        //System.out.println("type--"+type+"--url--"+url+"--like--"+like+"--comment--"+comment+"--date"+date);

        if(type.contains("image")){
            singleImageGallery.setVisibility(View.VISIBLE);
            singleVideoGallery.setVisibility(View.GONE);
            imageLoader.displayImage(url, singleImageGallery, options);
        } else if (type.contains("video")){
            singleImageGallery.setVisibility(View.GONE);
            singleVideoGallery.setVisibility(View.VISIBLE);
            singleVideoGallery.setVideoPath(url);
            //display media control buttons
            MediaController mediaController = new
                    MediaController(ChildMyGallerySingleImageScreen.this);
            mediaController.setAnchorView(singleVideoGallery);
            singleVideoGallery.setMediaController(mediaController);

            singleVideoGallery.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    singleVideoGallery.start();
                }
            });
        }
    }
}
