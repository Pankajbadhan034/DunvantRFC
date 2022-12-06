package com.dunvant.application.child;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
//https://github.com/philiiiiiipp/Android-Screen-to-Face-Distance-Measurement
public class ChildRecordVideoScreen extends Activity {
//    // Run time permission
//    public static final int MULTIPLE_PERMISSIONS = 10;
//    String[] permissions = new String[] {
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.CAMERA
//    };
    ImageView trim;
    TextView timer;
    ImageView autoRecord;
    TextView autoRecordText;
    private String TAG = "VIDEO_RECORDING";
    String filePath="";
    //    MediaPlayer mediaPlayer;
    int duration=15;
    ImageView startRecording;
    FrameLayout camera_preview;
    TextView timerText;
    ImageView frontBackCamera;
    ImageView backButton;

    String videoPath;
    private MediaRecorder mediaRecorder;
    CountDownTimer recordingTimer = null;
    private Camera myCamera;
    Camera.Size mPreviewSize;
    private MyCameraSurfaceView myCameraSurfaceView;
    boolean isRecording = false;
    String mode_check="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_record_video_screen);

        startRecording = (ImageView) findViewById(R.id.startRecording);
        camera_preview=(FrameLayout) findViewById(R.id.camera_preview);
        timerText=(TextView) findViewById(R.id.timer);
        frontBackCamera=(ImageView)findViewById(R.id.frontBackCamera);
        backButton = (ImageView) findViewById(R.id.backButton);
        trim = (ImageView) findViewById(R.id.trim);
        timer = (TextView) findViewById(R.id.timer);
        autoRecord = (ImageView) findViewById(R.id.autoRecord);
        autoRecordText = (TextView) findViewById(R.id.autoRecordText);

        trim.setVisibility(View.INVISIBLE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        filePath = getIntent().getStringExtra("filePath");

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA},1);
            return;
        } else {
            try {
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setDataSource(filePath);
//            mediaPlayer.prepare();
//            duration = mediaPlayer.getDuration();
//            duration = duration / 1000;
//            //System.out.println("Song Duration " + duration);
                showCameraSurface();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




        timerText.setText("" + duration);

        frontBackCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCamera.release();
                if (mode_check.equals("1")) {
                    mode_check = "0";
                    //frontBackCamera.setText("Front Camera");
                    showCameraSurface();

                } else {
                    mode_check = "1";
                    myCamera.release();
                    //frontBackCamera.setText("Back Camera");
                    showCameraSurface();

                }

            }
        });

        autoRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoRecordText.setVisibility(View.VISIBLE);
                recordingTimer = new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        autoRecordText.setText(""+TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
                        //System.out.println("mill_seconds_s__" + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
                        isRecording = true;
                    }
                    public void onFinish() {
                        autoRecordText.setVisibility(View.INVISIBLE);
                        hideButtons();
                        startRecording();
                    }
                }.start();


            }
        });


//        showCameraSurface();
        startRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    hideButtons();
                    final ProgressDialog pd = new ProgressDialog(ChildRecordVideoScreen.this);
                    pd.setMessage("Saving Video...");
                    pd.setCancelable(false);
                    pd.show();
                    recordingTimer.cancel();
                    try {
                        mediaRecorder.stop();
                        releaseMediaRecorder();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new CountDownTimer(2000, 1000) {
                        @Override
                        public void onTick(long arg0) {
                        }

                        @Override
                        public void onFinish() {
                            pd.dismiss();
                            isRecording = false;
                            showButtons();

                            Intent obj = new Intent(ChildRecordVideoScreen.this, ChildChooseMusicScreen.class);
                            obj.putExtra("galleryPath", videoPath);
                            startActivity(obj);
                        }
                    }.start();

                }else{
                    startRecording();
                }
            }
        });





    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},1);
                return;
            } else {
                try {
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setDataSource(filePath);
//            mediaPlayer.prepare();
//            duration = mediaPlayer.getDuration();
//            duration = duration / 1000;
//            //System.out.println("Song Duration " + duration);
                    showCameraSurface();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showCameraSurface() {
        myCamera = getCameraInstance();
        if (myCamera == null) {
            Toast.makeText(getApplicationContext(), "Failed to get Camera", Toast.LENGTH_LONG).show();
        } else {
            myCameraSurfaceView = new MyCameraSurfaceView(ChildRecordVideoScreen.this, myCamera);
            camera_preview.removeAllViews();
            camera_preview.addView(myCameraSurfaceView);
        }
    }

    private void scanFile(String path) {
        MediaScannerConnection.scanFile(ChildRecordVideoScreen.this, new String[]{path},
                null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }
    private Camera getCameraInstance() {
        Camera c = null;
        try {
            if (mode_check.equals("1")) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

                int cameraCount = Camera.getNumberOfCameras();

                for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                    Camera.getCameraInfo(camIdx, cameraInfo);
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        try {
                            c = Camera.open(camIdx);
                            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                c.setDisplayOrientation(90);
                            }
                            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                c.setDisplayOrientation(0);
                            }
                        } catch (RuntimeException e) {
                            Log.e("Error", "Camera failed to open: " + e.getLocalizedMessage());
                        }
                        break;
                    }
                }
            } else {
                c = Camera.open();
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    c.setDisplayOrientation(90);
                }
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    c.setDisplayOrientation(0);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return c;
    }

    private boolean prepareMediaRecorder() {
        Camera cam = null;


        mediaRecorder = new MediaRecorder();
        myCamera.unlock();
        mediaRecorder.setCamera(myCamera);
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);


        if (mode_check.equals("1")) {
            int front_camera_id = -10;
            Camera.CameraInfo cInfo = new Camera.CameraInfo();
            int cameraCount = Camera.getNumberOfCameras();
            //System.out.println("CameraCount___"+cameraCount);
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cInfo);
                if (cInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    front_camera_id = camIdx;
                    //System.out.println("FrontID__"+front_camera_id);
                    break;
                }
                if (cInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    front_camera_id = camIdx;
                    //System.out.println("BackID__"+front_camera_id);
                    break;
                }
            }


            CamcorderProfile profile = CamcorderProfile.get(front_camera_id, CamcorderProfile.QUALITY_LOW);

            // Step 2: Set sources
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            // Step 3: Set all values contained in profile except audio settings
            mediaRecorder.setOutputFormat(profile.fileFormat);
            mediaRecorder.setVideoEncoder(profile.videoCodec);
            mediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
            mediaRecorder.setVideoFrameRate(profile.videoFrameRate);
            mediaRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);





//            mediaRecorder.setProfile(CamcorderProfile.get(front_camera_id, CamcorderProfile.QUALITY_LOW));
            // mediaRecorder.setVideoSize(320,240);
        }else if(mode_check.equals("0")){
            int back_camera_id = -10;
            Camera.CameraInfo cInfo = new Camera.CameraInfo();
            int cameraCount = Camera.getNumberOfCameras();
            //System.out.println("CameraCount___"+cameraCount);
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cInfo);
                if (cInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    back_camera_id = camIdx;
                    //System.out.println("FrontID__"+back_camera_id);
                    break;
                }
                if (cInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    back_camera_id = camIdx;
                    //System.out.println("BackID__"+back_camera_id);
                    break;
                }
            }

            CamcorderProfile profile = CamcorderProfile.get(back_camera_id, CamcorderProfile.QUALITY_LOW);

            // Step 2: Set sources
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            // Step 3: Set all values contained in profile except audio settings
            mediaRecorder.setOutputFormat(profile.fileFormat);
            mediaRecorder.setVideoEncoder(profile.videoCodec);
            mediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
            mediaRecorder.setVideoFrameRate(profile.videoFrameRate);
            mediaRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);



//            mediaRecorder.setProfile(CamcorderProfile.get(back_camera_id, CamcorderProfile.QUALITY_LOW));
            // mediaRecorder.setVideoSize(320,240);
        }
        else {
            cam = Camera.open();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                cam.setDisplayOrientation(90);
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                cam.setDisplayOrientation(0);
            }
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date currenTimeZone = c.getTime();

        videoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "/ ffmpegIFA.mp4";
        mediaRecorder.setOutputFile(videoPath);

        mediaRecorder.setMaxDuration(900000); // Set max duration 60 sec.
        mediaRecorder.setMaxFileSize(1000000000); // Set max file size 5M

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mediaRecorder.setOrientationHint(0);
        } else {
            if (mode_check.equals("1") && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                //System.out.println("270Rotation_here__");
                mediaRecorder.setOrientationHint(90);
            } else if (mode_check.equals("0") && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                //System.out.println("90Rotation_here__");
                mediaRecorder.setOrientationHint(90);
            }
        }

        mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            myCamera.lock(); // lock camera for later use
        }
    }

    private void releaseCamera() {
        if (myCamera != null) {
            myCamera.release(); // release the cameraString Play_video_path; for other applications
            myCamera = null;
        }
    }

    public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

        private SurfaceHolder mHolder;
        private Camera mCamera;

        public MyCameraSurfaceView(Context context, Camera camera) {
            super(context);
            mCamera = camera;
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int weight, int height) {

            //System.out.println("Surface Changed");

            if (mHolder.getSurface() == null) {
                return;
            }
            mCamera.stopPreview();
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            //System.out.println("Surface Created");
            if (mCamera == null) {
                Toast.makeText(getApplicationContext(), "Camera not working", Toast.LENGTH_LONG).show();
            } else {
                //System.out.println("Holder_check_" + holder);
                try {

                    //  mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();

                    Camera.Parameters parameters = mCamera.getParameters();
                    parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
                    // mCamera.setParameters(parameters);
                    //mCamera.startPreview();


                    mCamera.setPreviewDisplay(holder);
                    mCamera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                    //  Toast.makeText(getActivity(), "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            //System.out.println("Surface destroyed*******");
        }
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            setMeasuredDimension(width, height);

            if (myCameraSurfaceView != null) {
                mPreviewSize = getOptimalPreviewSize(mCamera.getParameters().getSupportedPreviewSizes(), width, height);

            }
        }

    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public void startRecording() {

        if (!prepareMediaRecorder()) {
            timerText.setText("0");
            return;
        }
        mediaRecorder.start();

//        mediaPlayer.start();

        int video_time = duration * 1000;
        recordingTimer = new CountDownTimer(video_time, 1000) {
            public void onTick(long millisUntilFinished) {
                hideButtons();
                timerText.setText("" + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
//                timerText.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
//                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                //System.out.println("mill_seconds___" + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
                isRecording = true;
            }
            public void onFinish() {
                //System.out.println("finish_method_call");
                timerText.setText("0");
                mediaRecorder.stop();
                releaseMediaRecorder();
                scanFile(videoPath);
                final ProgressDialog pd = new ProgressDialog(ChildRecordVideoScreen.this);
                pd.setMessage("Saving Video...");
                pd.show();
                new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long arg0) {
                    }
                    @Override
                    public void onFinish() {
                        isRecording = false;
                        showButtons();

                        pd.dismiss();

                        Intent obj = new Intent(ChildRecordVideoScreen.this, ChildChooseMusicScreen.class);
                        obj.putExtra("galleryPath", videoPath);
                        startActivity(obj);
                    }
                }.start();

            }
        }.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseMediaRecorder(); // if you are using MediaRecorder, release it
        releaseCamera(); // release the camera immediately on pause event

        if (recordingTimer != null) {
            recordingTimer.cancel();
            if (mediaRecorder != null) {
                mediaRecorder.stop(); // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
            }
        }

        isRecording = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        camera_preview.removeView(myCameraSurfaceView);
    }

    public void showButtons(){
     //   trim.setVisibility(View.VISIBLE);
        autoRecord.setVisibility(View.VISIBLE);
        frontBackCamera.setVisibility(View.VISIBLE);
        timer.setVisibility(View.INVISIBLE);
        startRecording.setBackgroundResource(R.drawable.videoc);
    }
    public void hideButtons(){
      //  trim.setVisibility(View.INVISIBLE);
        autoRecord.setVisibility(View.INVISIBLE);
        frontBackCamera.setVisibility(View.INVISIBLE);
        timer.setVisibility(View.VISIBLE);
        startRecording.setBackgroundResource(R.drawable.stopc);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkPermissions()){
////                Toast.makeText(ChildTrackWorkoutScreen.this, "Permissions already granted", Toast.LENGTH_SHORT).show();
//            } else {
////                Toast.makeText(ChildTrackWorkoutScreen.this, "Initially permission not available", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private boolean checkPermissions() {
//        int result;
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        for (String p:permissions) {
//            result = ContextCompat.checkSelfPermission(ChildRecordVideoScreen.this, p);
//            if (result != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(p);
//            }
//        }
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MULTIPLE_PERMISSIONS:{
//                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    // permissions granted.
//                } else {
//                    // no permissions granted.
//                    Toast.makeText(ChildRecordVideoScreen.this, "This feature cannot work without Permissions. \nRelaunch the App or allow permissions in Applications Settings", Toast.LENGTH_LONG).show();
//                    finish();
//                }
//                return;
//            }
//        }
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission accepted.
                    try {
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setDataSource(filePath);
//            mediaPlayer.prepare();
//            duration = mediaPlayer.getDuration();
//            duration = duration / 1000;
//            //System.out.println("Song Duration " + duration);
                        showCameraSurface();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //permission denied.
                }
                return;
            }
        }
    }
}