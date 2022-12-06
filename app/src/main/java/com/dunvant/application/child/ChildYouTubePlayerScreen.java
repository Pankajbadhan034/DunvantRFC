//package com.ifasport.application.child;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.google.android.youtube.player.YouTubeBaseActivity;
//import com.google.android.youtube.player.YouTubeInitializationResult;
//import com.google.android.youtube.player.YouTubePlayer;
//import com.google.android.youtube.player.YouTubePlayer.Provider;
//import com.google.android.youtube.player.YouTubePlayerView;
//import com.ifasport.application.R;
//
//public class ChildYouTubePlayerScreen extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
//
//    private static final int RECOVERY_REQUEST = 1;
//    private YouTubePlayerView youTubeView;
//    String youTubeIdStr;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.child_activity_you_tube_player_screen);
//         youTubeIdStr = getIntent().getStringExtra("youTubeId");
//
//        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
//        youTubeView.initialize("AIzaSyCRTP3uhi-sw-viXoZCf36k73n53TkkRH4", this);
//    }
//
//    @Override
//    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
//        if (!wasRestored) {
//            player.cueVideo(youTubeIdStr); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
//        }
//    }
//
//    @Override
//    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
//        if (errorReason.isUserRecoverableError()) {
//            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
//        } else {
//            String error = String.format("ERROR", errorReason.toString());
//            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RECOVERY_REQUEST) {
//            // Retry initialization if user performed a recovery action
//            getYouTubePlayerProvider().initialize("AIzaSyCRTP3uhi-sw-viXoZCf36k73n53TkkRH4", this);
//        }
//    }
//
//    protected Provider getYouTubePlayerProvider() {
//        return youTubeView;
//    }
//}
//
////
////public class ChildYouTubePlayerScreen extends AppCompatActivity {
////    String youTubeIdStr;
////    String apiId = "AIzaSyCRTP3uhi-sw-viXoZCf36k73n53TkkRH4";
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.child_activity_you_tube_player_screen);
////
////        youTubeIdStr = getIntent().getStringExtra("youTubeId");
////        System.out.print("URL_Here::"+youTubeIdStr);
////
////        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
////
////        youTubePlayerView.initialize(new YouTubePlayerInitListener() {
////            @Override
////            public void onInitSuccess(final YouTubePlayer initializedYouTubePlayer) {
////                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
////                    @Override
////                    public void onReady() {
////                        initializedYouTubePlayer.loadVideo(youTubeIdStr, 0);
////                    }
////                });
////            }
////        }, true);
////    }
////}
//
//
//
//
//
