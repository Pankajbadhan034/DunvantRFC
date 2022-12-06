package com.dunvant.application.child;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.dunvant.application.R;
import com.dunvant.application.utils.Utilities;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ChildPostViewScreen extends AppCompatActivity {
    ProgressDialog pDialog;
    String fileName;
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
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_post_view_screen);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        imageLoader.init(ImageLoaderConfiguration.createDefault(ChildPostViewScreen.this));
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
        webview = findViewById(R.id.webview);
        title.setTypeface(linoType);

        type = getIntent().getStringExtra("type");
        url = getIntent().getStringExtra("url");

        if(type.equalsIgnoreCase("video")){


            if(url.contains("youtube") || url.contains("youtu.be")){
                image.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);

                WebSettings webSettings = webview.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webview.loadUrl(url);
                finish();
            }else{

                image.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);

                WebSettings webSettings = webview.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webview.loadUrl(url);

                //                try {
                //                    String splitFileName[] = url.split("/");
                //                    fileName = splitFileName[splitFileName.length - 1];
                //                    System.out.println("fileName::"+fileName);
                //
                //
                //                    File myFile = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + fileName);
                //                    myFile.delete();
                //                    if (!myFile.exists()) {
                //                        new videoDownload().execute();
                //                        //	Toast.makeText(getApplicationContext(), "file not existed", 1000).show();
                //                    } else {
                //                        String path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + fileName;
                //
                //                        videoPlay(path);
                //                    }
                //                } catch (Exception e) {
                //                    e.printStackTrace();
                //                }


                //                video.setVideoPath(url);
                //                MediaController mediaController = new
                //                        MediaController(ChildPostViewScreen.this);
                //                mediaController.setAnchorView(video);
                //                video.setMediaController(mediaController);
                //
                //                video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                //                    public void onPrepared(MediaPlayer mp) {
                //                        video.start();
                //                    }
                //                });

            }

        }else if(type.equalsIgnoreCase("image")){
            url = getIntent().getStringExtra("url");
            image.setVisibility(View.VISIBLE);
            video.setVisibility(View.GONE);
            webview.setVisibility(View.GONE);

            imageLoader.displayImage(url, image, options);
        }

        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);

                    return true;
                }
                return false;
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    // video download from url
    private class videoDownload extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            pDialog = Utilities.createProgressDialog(ChildPostViewScreen.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String strResponse = null;
            try {
                String rootDir = Environment.getExternalStorageDirectory() + File.separator + "Video";
                File rootFile = new File("/sdcard/DCIM/");
                URL urlDownload = new URL(url);
                HttpURLConnection c = (HttpURLConnection) urlDownload.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();
                FileOutputStream f = new FileOutputStream(new File(rootFile, fileName));
                InputStream in = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = in.read(buffer)) > 0) {
                    f.write(buffer, 0, len1);
                }
                f.close();
                strResponse = "success";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return strResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (result == null) {
                Toast.makeText(getApplicationContext(), "Could not connect to server", Toast.LENGTH_SHORT).show();
            } else {
                if (result.equals("success")) {
                    String path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + fileName;
                    videoPlay(path);
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong, please try after sometime", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // video play method
    public void videoPlay(String path) {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) video.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        video.setLayoutParams(params);

        Uri videoUri = Uri.parse(path);
        video.setVideoURI(videoUri);
        video.setMediaController(new MediaController(ChildPostViewScreen.this));
        video.requestFocus();
        video.start();
    }
}