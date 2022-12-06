package com.dunvant.application.child;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

//import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class ChildCompleteRecordingScreen extends AppCompatActivity {
    ImageView micro;
    boolean bln;
    ImageView backButton;
    int seconds;
    String videoOutputPath=Environment.getExternalStorageDirectory()+"/output.mp4";
    String audioAdd;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;
    ImageView filter;
    ImageView play;
    VideoView videoView;
    Button savePrivate;
    Button share;
    EditText title;
    ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    //FFmpeg ffmpeg;
    String filePath;
    String galleryPath;
    ProgressDialog pd;
    String titleStr;
    String isShare="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_complete_recording_screen);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        filePath = getIntent().getStringExtra("filePath");
        galleryPath = getIntent().getStringExtra("galleryPath");
        audioAdd = getIntent().getStringExtra("audioAdd");


        //System.out.println("FilePath::"+filePath);
        //System.out.println("GalleryPath::"+galleryPath);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        savePrivate = (Button) findViewById(R.id.savePrivate);
        videoView = (VideoView) findViewById(R.id.videoView);
//        videoView.setVideoURI(Uri.parse(galleryPath));
//        videoView.seekTo(100);
        play = (ImageView) findViewById(R.id.play);
        filter = (ImageView) findViewById(R.id.filter);
        title = (EditText) findViewById(R.id.title);
        share = (Button) findViewById(R.id.share);
        backButton = (ImageView) findViewById(R.id.backButton);
        micro = (ImageView) findViewById(R.id.micro);
//
//        if(ChildRecordSlowFastvideoNewScreen.VIDEOTYPE.equalsIgnoreCase("fast") || ChildRecordSlowFastvideoNewScreen.VIDEOTYPE.equalsIgnoreCase("slow")){
//            filter.setVisibility(View.INVISIBLE);
//        }else{
//            filter.setVisibility(View.VISIBLE);
//        }

        micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(ChildCompleteRecordingScreen.this, ChildCutAudioWaveScreen.class);
                startActivity(obj);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if(audioAdd.equalsIgnoreCase("no")){
            //System.out.println("noCompress");
        }else{
            //System.out.println("yes");
            //ffmpegSoundVideoMix();
        }


        savePrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleStr = title.getText().toString().trim();
                isShare="0";
                if(titleStr==null || titleStr.isEmpty()){
                    Toast.makeText(ChildCompleteRecordingScreen.this, "Please enter title", Toast.LENGTH_LONG).show();
                }else{
                    if(Utilities.isNetworkAvailable(ChildCompleteRecordingScreen.this)) {
                        new UpdateImageVideoAsync(ChildCompleteRecordingScreen.this).execute();
                    }else{
                        Toast.makeText(ChildCompleteRecordingScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleStr = title.getText().toString().trim();
                isShare="1";
                if(titleStr==null || titleStr.isEmpty()){
                    Toast.makeText(ChildCompleteRecordingScreen.this, "Please enter title", Toast.LENGTH_LONG).show();
                }else{
                    if(Utilities.isNetworkAvailable(ChildCompleteRecordingScreen.this)) {
                        new UpdateImageVideoAsync(ChildCompleteRecordingScreen.this).execute();
                    }else{
                        Toast.makeText(ChildCompleteRecordingScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bln == true) {
                    //play.setBackgroundResource(R.drawable.stopc);
                    progressStatus = 0;
                    progressBarHeader();
                    // set full screen
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoView.getLayoutParams();
                    params.width = metrics.widthPixels;
                    params.height = metrics.heightPixels;
                    params.leftMargin = 0;
                    videoView.setLayoutParams(params);

                    //System.out.println("galleryPathInPlay::" + galleryPath);
                    videoView.setVideoPath(galleryPath);
                    videoView.seekTo(100);
                    videoView.start();
                    bln = false;
                }else{
                    bln = true;
                   // play.setBackgroundResource(R.drawable.playc);
                    videoView.pause();
                }

                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    public void onCompletion(MediaPlayer mp)
                    {
                        // Do whatever u need to do here
                       // play.setBackgroundResource(R.drawable.playc);
                    }
                });

            }
        });
//        https://github.com/nekocode/CameraFilter

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("videoOutPutPathIntent::"+videoOutputPath);
                //System.out.println("filePathIntent::"+filePath);
                Intent obj = new Intent(ChildCompleteRecordingScreen.this, ChildVideoFilterScreen.class);
                obj.putExtra("videoOutputPath", videoOutputPath);
                obj.putExtra("filePath", filePath);
                startActivity(obj);
            }
        });

    }

    public void progressBarHeader(){
        //System.out.println("Seconds::" + seconds);

        final long totalLength = seconds * 1000;
        progressBar.setMax(seconds);
        progressStatus = 0;
        play.setVisibility(View.INVISIBLE);
        filter.setVisibility(View.INVISIBLE);


        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                progressStatus++;
                progressBar.setProgress(progressStatus);
                if(progressStatus == seconds){
                    timer.cancel();
                    progressBar.setProgress(0);
                    progressStatus = 0;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            play.setVisibility(View.VISIBLE);

//                            if(ChildRecordSlowFastvideoNewScreen.VIDEOTYPE.equalsIgnoreCase("fast") || ChildRecordSlowFastvideoNewScreen.VIDEOTYPE.equalsIgnoreCase("slow")){
//                                filter.setVisibility(View.INVISIBLE);
//                            }else{
//                                filter.setVisibility(View.VISIBLE);
//                            }
                        }
                    });

                }
            }
        };
        timer.schedule(task, 1000, 1000);

        /*new CountDownTimer(totalLength, 1000) {

            public void onTick(long millisUntilFinished) {

                long progress = totalLength - millisUntilFinished;

                //System.out.println("Total: "+totalLength+" millis "+millisUntilFinished+" progress "+progress);

                progressBar.setProgress((int) progress/1000);
            }

            public void onFinish() {
                progressBar.setProgress(seconds);
            }

        }.start();*/

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (progressStatus <= seconds) {
//                    // Update the progress status
//                    progressStatus += 1;
//
//                    //System.out.println("ProgressStatus::"+progressStatus);
//                    // Try to sleep the thread for 15 milliseconds
//                    try {
//                        Thread.sleep(1000);
//                        //System.out.println("ThreadSleepHere");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    //System.out.println("ThreadSleepEnd");
//                    // Update the progress bar
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            //System.out.println("ProgressValueHere::"+progressStatus);
//                            progressBar.setProgress(progressStatus);
//                        }
//                    });
//                }
//            }
//        }).start(); // Start the operation
    }

//    public void ffmpegSoundVideoMix(){
//        pd = new ProgressDialog(ChildCompleteRecordingScreen.this);
//        pd.setMessage("Saving Video...");
//        pd.show();
//
//        ffmpeg = FFmpeg.getInstance(ChildCompleteRecordingScreen.this);
//        try {
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//
//                @Override
//                public void onStart() {
//                    //System.out.println("FFMPEG onStart");
//                }
//
//                @Override
//                public void onFailure() {
//                    //System.out.println("FFMPEG onFailure");
//                }
//
//                @Override
//                public void onSuccess() {
//                    //System.out.println("FFMPEG onSuccess");
//                }
//
//                @Override
//                public void onFinish() {
//                    //System.out.println("FFMPEG onFinish");
//                }
//            });
//        } catch (FFmpegNotSupportedException e) {
//            // Handle if FFmpeg is not supported by device
//            //System.out.println("FFMPEG Exception");
//            e.printStackTrace();
//        }
//
//        // Delete file if already exists
//        File file = new File(videoOutputPath);
//        if(file.exists()) {
//            //System.out.println("File exists, deleting");
//            file.delete();
//        }
//
//        //System.out.println("FilePath::"+filePath);
//        // Merge files
//        String[] command1 = new String[12];
//        command1[0] = "-i";
//        command1[1] = galleryPath;
//        command1[2] = "-i";
//        command1[3] = filePath;
//        command1[4] = "-c:v";
//        command1[5] = "copy";
//        command1[6] = "-c:a";
//        command1[7] = "aac";
//        command1[8] = "-shortest";
//        command1[9] = "-strict";
//        command1[10] = "experimental";
//        command1[11] = videoOutputPath;
//
//        try {
//            // to execute "ffmpeg -version" command you just need to pass "-version"
//            ffmpeg.execute(command1, new ExecuteBinaryResponseHandler() {
//
//                @Override
//                public void onStart() {
//                    //System.out.println("FFMPEG onStart");
//                }
//
//                @Override
//                public void onProgress(String message) {
//                    //System.out.println("FFMPEG onProgress "+message);
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    //System.out.println("FFMPEG onFailure "+message);
//                }
//
//                @Override
//                public void onSuccess(String message) {
//                    //System.out.println("FFMPEG onSuccess "+message);
//                }
//
//                @Override
//                public void onFinish() {
//                    //System.out.println("FFMPEG onFinish");
//                  //  Toast.makeText(ChildCompleteRecordingScreen.this, "FFMPEG FINISH", Toast.LENGTH_SHORT).show();
//                    galleryPath = videoOutputPath;
//                    // get total duration of video
//                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                    retriever.setDataSource(ChildCompleteRecordingScreen.this, Uri.fromFile(new File(videoOutputPath)));
//                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                    long timeInMillisec = Long.parseLong(time );
//                    seconds = (int) timeInMillisec / 1000;
//                    pd.dismiss();
//
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            // Handle if FFmpeg is already running
//            //System.out.println("FFMPEG Exception");
//            e.printStackTrace();
//        }
//    }



    private class UpdateImageVideoAsync extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog;
        Context context;

        public UpdateImageVideoAsync(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pDialog = Utilities.createProgressDialog(context);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... voids) {

            //System.out.println("Uploading starting");

            String uploadUrl = Utilities.BASE_URL + "user_posts/upload_gallery";
            String strResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(uploadUrl);

            httpPost.addHeader("X-access-uid", loggedInUser.getId());
            httpPost.addHeader("X-access-token", loggedInUser.getToken());

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(ChildCompleteRecordingScreen.this));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);
            try {


                StringBody titleBody = new StringBody(titleStr);
                StringBody isPublic = new StringBody("0");
                StringBody isVideoCaptured = new StringBody("1");
                StringBody isShared = new StringBody(isShare);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();

                builder.addPart("title", titleBody);
                builder.addPart("is_public", isPublic);
                builder.addPart("is_video_captured", isVideoCaptured);
                builder.addPart("is_share", isShared);
                builder.addPart("is_public", isShared);


                    File videoFile = new File(galleryPath);
                    FileBody videoFileBody = new FileBody(videoFile, videoFile.getName(), "video/mp4", "UTF-8");

                    builder.addPart("file", videoFileBody);



                HttpEntity entity = builder.build();
                httpPost.setEntity(entity);

                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity resEntity = response.getEntity();

                if (resEntity != null) {
                    try {
                        strResponse = EntityUtils.toString(resEntity).trim();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                //System.out.println("File upload end");

            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("Exception "+e.getMessage());
            }

            return strResponse;

        }

        @Override
        protected void onPostExecute(String response) {

            //System.out.println("Response "+response);

            if(response == null) {
                Toast.makeText(ChildCompleteRecordingScreen.this, "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    Toast.makeText(ChildCompleteRecordingScreen.this, message, Toast.LENGTH_SHORT).show();

                    if(status) {
                        Intent obj = new Intent(ChildCompleteRecordingScreen.this, ChildMainScreen.class);
                        startActivity(obj);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ChildCompleteRecordingScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if(pDialog != null && pDialog.isShowing()){
                pDialog.dismiss();
            }
        }
    }

    public String getMimeType(Uri uri) {
        String mimeType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = ChildCompleteRecordingScreen.this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        videoView.setVideoURI(Uri.parse(galleryPath));
       // videoView.seekTo(200);
    }
}
