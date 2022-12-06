package com.dunvant.application.coach;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;

import uk.co.senab.photoview.PhotoViewAttacher;

public class CoachRugbyScreen extends Activity {
    ProgressDialog pDialog;
    String extStr;
    String fileNameStr;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;
    WebView webView;
    ImageView backButton;
    ImageView imageView;
    PhotoViewAttacher mAttacher;
    VideoView videoViewPortrait, videoViewLandscape;
    RelativeLayout relative;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_rugby_activity);
        webView = (WebView) findViewById(R.id.webView);
        backButton = (ImageView) findViewById(R.id.backButton);
        imageView = (ImageView) findViewById(R.id.imageView);
        videoViewPortrait = findViewById(R.id.videoViewPortrait);
        videoViewLandscape = findViewById(R.id.videoViewLandscape);
        relative = findViewById(R.id.relative);

        extStr = getIntent().getStringExtra("ext");
        fileNameStr = getIntent().getStringExtra("fileName");

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        imageLoader.init(ImageLoaderConfiguration.createDefault(CoachRugbyScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if(extStr.equalsIgnoreCase("video")){
            videoViewPortrait.setVisibility(View.VISIBLE);
            videoViewLandscape.setVisibility(View.GONE);
            relative.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);

            videoViewPortrait.setVideoPath(Utilities.RUGBY_DOC_URL+""+fileNameStr);
            videoViewPortrait.setMediaController(new MediaController(CoachRugbyScreen.this));
            videoViewPortrait.requestFocus();
            videoViewPortrait.start();
        }else if(extStr.equalsIgnoreCase("image")){
            imageView.setVisibility(View.VISIBLE);
            videoViewPortrait.setVisibility(View.GONE);
            videoViewLandscape.setVisibility(View.GONE);
            relative.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);

            imageLoader.displayImage(Utilities.RUGBY_DOC_URL+""+fileNameStr, imageView, options);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mAttacher = new PhotoViewAttacher(imageView);

                        return true;
                    }
                    return false;
                }
            });

        }else{
            webView.setVisibility(View.VISIBLE);
            videoViewPortrait.setVisibility(View.GONE);
            videoViewLandscape.setVisibility(View.GONE);
            relative.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);

            pDialog = Utilities.createProgressDialog(CoachRugbyScreen.this);

        webView.setWebViewClient(new AppWebViewClients());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        String a = "http://docs.google.com/gview?embedded=true&url="+Utilities.RUGBY_DOC_URL+fileNameStr;
        System.out.println("HERE_A::"+a);
        webView.loadUrl(a);



        }

    }
    public class AppWebViewClients extends WebViewClient {



        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            pDialog.dismiss();

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(extStr.equalsIgnoreCase("video")){
          if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
              videoViewPortrait.setVisibility(View.GONE);
              videoViewLandscape.setVisibility(View.VISIBLE);
              relative.setVisibility(View.VISIBLE);
              imageView.setVisibility(View.GONE);
              webView.setVisibility(View.GONE);
              videoViewLandscape.setVideoPath(Utilities.RUGBY_DOC_URL+""+fileNameStr);
              MediaController mc = new MediaController(CoachRugbyScreen.this);
              mc.setPadding(0,0,100,0);
              videoViewLandscape.setMediaController(mc);
              videoViewLandscape.requestFocus();
              videoViewLandscape.start();
            }else{
              videoViewPortrait.setVisibility(View.VISIBLE);
              videoViewLandscape.setVisibility(View.GONE);
              relative.setVisibility(View.GONE);
              imageView.setVisibility(View.GONE);
              webView.setVisibility(View.GONE);
              videoViewPortrait.setVideoPath(Utilities.RUGBY_DOC_URL+""+fileNameStr);
              videoViewPortrait.setMediaController(new MediaController(CoachRugbyScreen.this));
              videoViewPortrait.requestFocus();
              videoViewPortrait.start();
          }

        }
    }
}