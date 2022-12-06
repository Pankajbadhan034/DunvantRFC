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

public class ChildMessageDetailImageVideoViewScreen extends AppCompatActivity {
    String type;
    String url;
    ImageView image;
    VideoView video;
    ImageView backButton;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    TextView title;
    Typeface helvetica;
    Typeface linoType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_message_detail_image_video_view_screen);
        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        imageLoader.init(ImageLoaderConfiguration.createDefault(ChildMessageDetailImageVideoViewScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        image = (ImageView) findViewById(R.id.image);
        video = (VideoView) findViewById(R.id.video);
        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(linoType);

        type = getIntent().getStringExtra("type");
        if(type.equalsIgnoreCase("video")){
            url = getIntent().getStringExtra("url");
            image.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);

            video.setVideoPath(url);
            MediaController mediaController = new
                    MediaController(ChildMessageDetailImageVideoViewScreen.this);
            mediaController.setAnchorView(video);
            video.setMediaController(mediaController);

            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    video.start();
                }
            });
        }else if(type.equalsIgnoreCase("image")){
            url = getIntent().getStringExtra("url");
            image.setVisibility(View.VISIBLE);
            video.setVisibility(View.GONE);

            imageLoader.displayImage(url, image, options);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
