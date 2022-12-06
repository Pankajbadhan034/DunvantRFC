package com.dunvant.application.parent;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.dunvant.application.R;

public class ParentViewVideoInFullScreen extends AppCompatActivity {

    ImageView backButton;
    VideoView videoView;

    String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_view_video_in_full_screen);

        backButton = (ImageView) findViewById(R.id.backButton);
        videoView = (VideoView) findViewById(R.id.videoView);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoView.start();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if(intent != null){
            videoUrl = intent.getStringExtra("videoUrl");
            Uri video = Uri.parse(videoUrl);
            videoView.setVideoURI(video);
        }
    }
}
