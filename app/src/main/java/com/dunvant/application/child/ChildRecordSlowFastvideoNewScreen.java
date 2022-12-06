//package com.ifasport.application.child;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.ContentUris;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.res.Configuration;
//import android.database.Cursor;
//import android.graphics.Typeface;
//import android.hardware.Camera;
//import android.media.AudioManager;
//import android.media.CamcorderProfile;
//import android.media.MediaRecorder;
//import android.media.MediaScannerConnection;
//import android.media.SoundPool;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Environment;
//import android.provider.DocumentsContract;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AlertDialog;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.coremedia.iso.boxes.Container;
//import com.googlecode.mp4parser.authoring.Track;
//import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
//import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
//import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
//import com.ifasport.application.R;
//import com.ifasport.application.beans.ChildChunkVideoListBean;
//import com.ifasport.application.child.adapters.ChildChunkVideoAdapter;
//import com.ifasport.application.utils.Utilities;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.nio.channels.FileChannel;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
////import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
////import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
////import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
////import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
////import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
//
//@SuppressWarnings("deprecation")
//public class ChildRecordSlowFastvideoNewScreen extends Activity {
//    TextView alertMessage;
//    boolean checkTouch=true;
//    public static String VIDEOTYPE = "normal";
//    String textValue;
//    int tempDuration=15;
//    String tempPathVideo;
//    ChildChunkVideoAdapter childChunkVideoAdapter;
//    private final String TAG = this.getClass().getSimpleName();
//    ArrayList<ChildChunkVideoListBean>childChunkVideoListBeanArrayList = new ArrayList<>();
//    int videoCount = 1;
//    String output;
//    Typeface helvetica;
//    Typeface linoType;
//    String videoType = "normal";
//    ProgressDialog progressDialog;
//    //FFmpeg ffmpeg;
//    TextView fast;
//    TextView slow;
//    TextView normal;
//    ImageView mergeVideo;
//
//    int duration = 15;
//    ImageView startRecording;
//    FrameLayout camera_preview;
//    TextView timer;
//    ImageView frontBackCamera;
//    ImageView backButton;
//
//    String videoPath;
//    private MediaRecorder mediaRecorder;
//    CountDownTimer recordingTimer = null;
//    private Camera myCamera;
//    Camera.Size mPreviewSize;
//    private MyCameraSurfaceView myCameraSurfaceView;
//    boolean isRecording = false;
//    String mode_check = "0";
//    HorizontalListView list;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.child_activity_record_slow_fastvideo_new_screen);
//
//        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
//        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");
//
//        startRecording = (ImageView) findViewById(R.id.startRecording);
//        camera_preview = (FrameLayout) findViewById(R.id.camera_preview);
//        frontBackCamera = (ImageView) findViewById(R.id.frontBackCamera);
//        backButton = (ImageView) findViewById(R.id.backButton);
//        fast = (TextView) findViewById(R.id.fast);
//        slow = (TextView) findViewById(R.id.slow);
//        normal = (TextView) findViewById(R.id.normal);
//        mergeVideo = (ImageView) findViewById(R.id.mergeVideo);
//        list = (HorizontalListView) findViewById(R.id.list);
//        timer = (TextView) findViewById(R.id.timer);
//        alertMessage = (TextView) findViewById(R.id.alertMessage);
//
//        slow.setTypeface(helvetica);
//        normal.setTypeface(helvetica);
//        fast.setTypeface(helvetica);
//
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                if (position==childChunkVideoListBeanArrayList.size()-1 && childChunkVideoListBeanArrayList.size()>1){
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ChildRecordSlowFastvideoNewScreen.this);
//                    builder1.setMessage("Are you sure you want to Delete this video?");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Yes",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    String durationChunk = childChunkVideoListBeanArrayList.get(position).getVideoDuration();
//                                    //System.out.println("chunkDuration::"+durationChunk+"duration::"+duration);
//
//                                    //int calculateSingleChunkDuration = tempDuration - Integer.parseInt(durationChunk);
//                                   // //System.out.println("calculateSingleChunkDuration::"+calculateSingleChunkDuration);
//
//
//                                    duration = duration + Integer.parseInt(durationChunk);
//                                    //System.out.println("durationFinal::"+duration);
//                                    timer.setText(""+duration);
////
//                                    childChunkVideoListBeanArrayList.remove(position);
//                                    childChunkVideoAdapter.notifyDataSetChanged();
//                                    videoCount--;
//                                    dialog.cancel();
//
//                                    alertMessage.setVisibility(View.GONE);
//                                }
//                            });
//
//                    builder1.setNegativeButton(
//                            "No",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//
//                }
//            }
//        });
//
////        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
////            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 1);
////            return;
////        } else {
////            try {
////                showCameraSurface();
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle(null);
//        progressDialog.setCancelable(false);
//     //   loadFFMpegBinary();
//
//        final float playbackSpeed=1.5f;
//        final SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
//
//        final int soundId = soundPool.load(Environment.getExternalStorageDirectory()
//                + "/t1.mp4".toString(), 1);
//        AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        final float volume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//
//        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
//                soundPool.play(soundId, volume, volume, 1, 0, playbackSpeed);
//            }
//        });
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogExit();
//            }
//        });
//
//        normal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                normalClickVisibility();
//                VIDEOTYPE = "normal";
//            }
//        });
//
//        fast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fastClickVisibility();
//                VIDEOTYPE = "fast";
//            }
//        });
//
//        slow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                slowClickVisibility();
//                VIDEOTYPE = "slow";
//            }
//        });
//
//        mergeVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    appendVideo(childChunkVideoListBeanArrayList);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                //     finish();
//            }
//        });
//
//        timer.setText("" + duration);
//
//        frontBackCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myCamera.release();
//                if (mode_check.equals("1")) {
//                    mode_check = "0";
//                    showCameraSurface();
//                } else {
//                    mode_check = "1";
//                    myCamera.release();
//                    showCameraSurface();
//                }
//            }
//        });
//
//
//        startRecording.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                //System.out.println("TouchListener::");
//
//                if(timer.getText().toString().equalsIgnoreCase("1")){
//                    alertMessage.setVisibility(View.VISIBLE);
//
////                    if(checkTouch){
////            // Toast.makeText(ChildRecordSlowFastvideoNewScreen.this, "You cannot record more than 15 seconds", Toast.LENGTH_SHORT).show();
////                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ChildRecordSlowFastvideoNewScreen.this);
////                        builder1.setMessage("You cannot record more than 15 seconds");
////                        builder1.setCancelable(false);
////
////                        builder1.setPositiveButton(
////                                "OK",
////                                new DialogInterface.OnClickListener() {
////                                    public void onClick(DialogInterface dialog, int id) {
////                                        dialog.cancel();
////                                    }
////                                });
////                        AlertDialog alert = builder1.create();
////
////                        alert.show();
////
////                        checkTouch=false;
////                    }else{
////                        //System.out.println("elseCaseHere:::");
////                    }
//
//
//
//                    return false;
//
//                }else{
//                    final int actionPeformed = event.getAction();
//
//                    switch (actionPeformed) {
//
//                        case MotionEvent.ACTION_DOWN: {
//                            textValue = timer.getText().toString();
//                            //System.out.println("action_down_recordingStart");
//                            recordVisibility();
//                            startRecording();
//                            if(timer.getText().toString().equalsIgnoreCase("1")){
//                                return false;
//                            }
//
//                            break;
//                        }
//
//                        case MotionEvent.ACTION_UP: {
//                            //System.out.println("action_UP_recordingStop");
//
//                            stopVisibility();
//                            recordingTimer.cancel();
//                            try {
//                                mediaRecorder.stop();
//                                releaseMediaRecorder();
//                                isRecording = false;
//                                if(videoType.equalsIgnoreCase("fast")){
////                                executeFastMotionVideoCommand();
//                                    videoAddChunkList("fast");
//                                }else if(videoType.equalsIgnoreCase("slow")){
////                                executeSlowMotionVideoCommand();
//                                    videoAddChunkList("slow");
//                                }else if(videoType.equalsIgnoreCase("normal")){
//                                    videoAddChunkList("normal");
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            break;
//                        }
//                    }
//                }
//
//
//
//                return true;
//            }
//        });
//    }
//
////    @Override
////    protected void onResume() {
////        super.onResume();
////        videoCount = 1;
////        childChunkVideoListBeanArrayList.clear();
////         childChunkVideoAdapter = new ChildChunkVideoAdapter(ChildRecordSlowFastvideoNewScreen.this, childChunkVideoListBeanArrayList);
////        list.setAdapter(childChunkVideoAdapter);
////        childChunkVideoAdapter.notifyDataSetChanged();
////        normalClickVisibility();
////        //System.out.println("onRESUME::");
////        try {
////            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
////                requestPermissions(new String[]{android.Manifest.permission.CAMERA},1);
////                return;
////            } else {
////                try {
////                    showCameraSurface();
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////
////    }
//
//    private void showCameraSurface() {
//        myCamera = getCameraInstance();
//        if (myCamera == null) {
//            Toast.makeText(getApplicationContext(), "Failed to get Camera", Toast.LENGTH_LONG).show();
//        } else {
//            myCameraSurfaceView = new MyCameraSurfaceView(ChildRecordSlowFastvideoNewScreen.this, myCamera);
//            camera_preview.removeAllViews();
//            camera_preview.addView(myCameraSurfaceView);
//        }
//    }
//
//    private void scanFile(String path) {
//        MediaScannerConnection.scanFile(ChildRecordSlowFastvideoNewScreen.this, new String[]{path},
//                null, new MediaScannerConnection.OnScanCompletedListener() {
//                    public void onScanCompleted(String path, Uri uri) {
//                    }
//                });
//    }
//    private Camera getCameraInstance() {
//        Camera c = null;
//        try {
//            if (mode_check.equals("1")) {
//                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//
//                int cameraCount = Camera.getNumberOfCameras();
//
//                for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
//                    Camera.getCameraInfo(camIdx, cameraInfo);
//                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                        try {
//                            c = Camera.open(camIdx);
//                            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                                c.setDisplayOrientation(90);
//                            }
//                            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                                c.setDisplayOrientation(0);
//                            }
//                        } catch (RuntimeException e) {
//                            Log.e("Error", "Camera failed to open: " + e.getLocalizedMessage());
//                        }
//                        break;
//                    }
//                }
//            } else {
//                c = Camera.open();
//                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                    c.setDisplayOrientation(90);
//                }
//                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    c.setDisplayOrientation(0);
//                }
//            }
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        return c;
//    }
//
//    private boolean prepareMediaRecorder() {
//        Camera cam = null;
//
//
//        mediaRecorder = new MediaRecorder();
//        myCamera.unlock();
//        mediaRecorder.setCamera(myCamera);
////        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
////        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//
//
//        if (mode_check.equals("1")) {
//            int front_camera_id = -10;
//            Camera.CameraInfo cInfo = new Camera.CameraInfo();
//            int cameraCount = Camera.getNumberOfCameras();
//            //System.out.println("CameraCount___"+cameraCount);
//            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
//                Camera.getCameraInfo(camIdx, cInfo);
//                if (cInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                    front_camera_id = camIdx;
//                    //System.out.println("FrontID__"+front_camera_id);
//                    break;
//                }
//                if (cInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
//                    front_camera_id = camIdx;
//                    //System.out.println("BackID__"+front_camera_id);
//                    break;
//                }
//            }
//            mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());
//
//            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
//
//            CamcorderProfile profile = CamcorderProfile.get(front_camera_id, CamcorderProfile.QUALITY_LOW);
//            mediaRecorder.setProfile(profile);
//            // Step 2: Set sources
//            // mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//
//            // Step 3: Set all values contained in profile except audio settings
//
//
//
//            // mediaRecorder.setOutputFormat(profile.fileFormat);
//            //  mediaRecorder.setVideoEncoder(profile.videoCodec);
////            mediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
////            mediaRecorder.setVideoFrameRate(profile.videoFrameRate);
////            mediaRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
//
//        }else if(mode_check.equals("0")){
//            int back_camera_id = -10;
//            Camera.CameraInfo cInfo = new Camera.CameraInfo();
//            int cameraCount = Camera.getNumberOfCameras();
//            //System.out.println("CameraCount___"+cameraCount);
//            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
//                Camera.getCameraInfo(camIdx, cInfo);
//                if (cInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                    back_camera_id = camIdx;
//                    //System.out.println("FrontID__"+back_camera_id);
//                    break;
//                }
//                if (cInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
//                    back_camera_id = camIdx;
//                    //System.out.println("BackID__"+back_camera_id);
//                    break;
//                }
//            }
//            mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());
//
//            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
//
//            CamcorderProfile profile = CamcorderProfile.get(back_camera_id, CamcorderProfile.QUALITY_LOW);
//            mediaRecorder.setProfile(profile);
//            // Step 2: Set sources
////            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//
//            // Step 3: Set all values contained in profile except audio settings
//
////            mediaRecorder.setOutputFormat(profile.fileFormat);
////            mediaRecorder.setVideoEncoder(profile.videoCodec);
////            mediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
////            mediaRecorder.setVideoFrameRate(profile.videoFrameRate);
////            mediaRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
//
//
//
//        }else {
//            cam = Camera.open();
//            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                cam.setDisplayOrientation(90);
//            }
//            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                cam.setDisplayOrientation(0);
//            }
//            mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());
//        }
//
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date currenTimeZone = c.getTime();
//
//
//        String videoCountString = ""+videoCount;
//
//                if(videoType.equalsIgnoreCase("normal")){
//                    videoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +  "normal"+videoCountString+".mp4".toString();
//                }else if(videoType.equalsIgnoreCase("slow")){
//                    videoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +  "slow"+videoCountString+".mp4".toString();
//                }else if(videoType.equalsIgnoreCase("fast")){
//                    videoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +  "fast"+videoCountString+".mp4".toString();
//
//                }
//
//
//        mediaRecorder.setOutputFile(videoPath);
//
//        mediaRecorder.setMaxDuration(900000); // Set max duration 60 sec.
//        mediaRecorder.setMaxFileSize(1000000000); // Set max file size 5M
//
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            mediaRecorder.setOrientationHint(0);
//        } else {
//            if (mode_check.equals("1") && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                //System.out.println("270Rotation_here__");
//                mediaRecorder.setOrientationHint(270);
//            } else if (mode_check.equals("0") && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                //System.out.println("90Rotation_here__");
//                mediaRecorder.setOrientationHint(90);
//            }
//        }
//
//
//
//        try {
//            mediaRecorder.prepare();
//        } catch (IllegalStateException e) {
//            releaseMediaRecorder();
//            return false;
//        } catch (IOException e) {
//            releaseMediaRecorder();
//            return false;
//        }
//        return true;
//    }
//
//    private void releaseMediaRecorder() {
//        if (mediaRecorder != null) {
//            mediaRecorder.reset(); // clear recorder configuration
//            mediaRecorder.release(); // release the recorder object
//            mediaRecorder = null;
//            myCamera.lock(); // lock camera for later use
//        }
//    }
//
//    private void releaseCamera() {
//        if (myCamera != null) {
//            myCamera.release(); // release the cameraString Play_video_path; for other applications
//            myCamera = null;
//        }
//    }
//
//    public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
//
//        private SurfaceHolder mHolder;
//        private Camera mCamera;
//
//        public MyCameraSurfaceView(Context context, Camera camera) {
//            super(context);
//            mCamera = camera;
//            mHolder = getHolder();
//            mHolder.addCallback(this);
//            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        }
//
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int weight, int height) {
//
//            //System.out.println("Surface Changed");
//
//            if (mHolder.getSurface() == null) {
//                return;
//            }
//            mCamera.stopPreview();
//            try {
//                mCamera.setPreviewDisplay(mHolder);
//                mCamera.startPreview();
//            } catch (IOException e) {
//                Toast.makeText(getApplicationContext(), "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void surfaceCreated(SurfaceHolder holder) {
//
//            //System.out.println("Surface Created");
//            if (mCamera == null) {
//                Toast.makeText(getApplicationContext(), "Camera not working", Toast.LENGTH_LONG).show();
//            } else {
//                //System.out.println("Holder_check_" + holder);
//                try {
//
//                    //  mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
//
//                    Camera.Parameters parameters = mCamera.getParameters();
//                    parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
//                    // mCamera.setParameters(parameters);
//                    //mCamera.startPreview();
//
//
//                    mCamera.setPreviewDisplay(holder);
//                    mCamera.startPreview();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    //  Toast.makeText(getActivity(), "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//            //System.out.println("Surface destroyed*******");
//        }
//        @Override
//        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//            final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
//            setMeasuredDimension(width, height);
//
//            if (myCameraSurfaceView != null) {
//                mPreviewSize = getOptimalPreviewSize(mCamera.getParameters().getSupportedPreviewSizes(), width, height);
//
//            }
//        }
//
//    }
//
//    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
//        final double ASPECT_TOLERANCE = 0.1;
//        double targetRatio=(double)h / w;
//
//        if (sizes == null) return null;
//
//        Camera.Size optimalSize = null;
//        double minDiff = Double.MAX_VALUE;
//
//        int targetHeight = h;
//
//        for (Camera.Size size : sizes) {
//            double ratio = (double) size.width / size.height;
//            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
//            if (Math.abs(size.height - targetHeight) < minDiff) {
//                optimalSize = size;
//                minDiff = Math.abs(size.height - targetHeight);
//            }
//        }
//
//        if (optimalSize == null) {
//            minDiff = Double.MAX_VALUE;
//            for (Camera.Size size : sizes) {
//                if (Math.abs(size.height - targetHeight) < minDiff) {
//                    optimalSize = size;
//                    minDiff = Math.abs(size.height - targetHeight);
//                }
//            }
//        }
//        return optimalSize;
//    }
//
//    public void startRecording() {
//        if (!prepareMediaRecorder()) {
//               timer.setText("0");
//            return;
//        }
//        mediaRecorder.start();
////          mediaPlayer.start();
//
//        int video_time = duration * 1000;
//        recordingTimer = new CountDownTimer(video_time, 1000) {
//            public void onTick(long millisUntilFinished) {
//                duration = Integer.parseInt("" + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
//                timer.setText("" + duration);
//                //System.out.println("mill_seconds___" + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
//                isRecording = true;
//            }
//            public void onFinish() {
//                //System.out.println("finish_method_call");
//                timer.setText(""+ duration);
//                mediaRecorder.stop();
//                releaseMediaRecorder();
//                scanFile(videoPath);
//
//                isRecording = false;
//                stopVisibility();
//                if(videoType.equalsIgnoreCase("fast")){
////                    executeFastMotionVideoCommand();
//                    videoAddChunkList("fast");
//                }else if(videoType.equalsIgnoreCase("slow")){
////                    executeSlowMotionVideoCommand();
//                    videoAddChunkList("slow");
//                }else if(videoType.equalsIgnoreCase("normal")){
//                    videoAddChunkList("normal");
//
//                }
////                new CountDownTimer(1000, 1000) {
////                    @Override
////                    public void onTick(long arg0) {
////                    }
////                    @Override
////                    public void onFinish() {
////                        isRecording = false;
////                        stopVisibility();
////                        Intent obj = new Intent(ChildRecordSlowFastvideoNewScreen.this, ChildChooseMusicScreen.class);
////                        obj.putExtra("galleryPath", videoPath);
//////                        startActivity(obj);
////                    }
////                }.start();
//
//            }
//        }.start();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        releaseMediaRecorder(); // if you are using MediaRecorder, release it
//        releaseCamera(); // release the camera immediately on pause event
//
//        if (recordingTimer != null) {
//            recordingTimer.cancel();
//            if (mediaRecorder != null) {
//                mediaRecorder.stop(); // stop the recording
//                releaseMediaRecorder(); // release the MediaRecorder object
//            }
//        }
//
//        isRecording = false;
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        camera_preview.removeView(myCameraSurfaceView);
//    }
//
////    public void showButtons(){
////        //   trim.setVisibility(View.VISIBLE);
////       // autoRecord.setVisibility(View.VISIBLE);
////        frontBackCamera.setVisibility(View.VISIBLE);
////       // timer.setVisibility(View.INVISIBLE);
////        startRecording.setBackgroundResource(R.drawable.videoc);
////    }
////    public void hideButtons(){
////        //  trim.setVisibility(View.INVISIBLE);
////        //autoRecord.setVisibility(View.INVISIBLE);
////        frontBackCamera.setVisibility(View.INVISIBLE);
////       // timer.setVisibility(View.VISIBLE);
////        startRecording.setBackgroundResource(R.drawable.stopc);
////    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 0: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    try {
//                        showCameraSurface();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    //permission denied.
//                }
//                return;
//            }
//        }
//
//    }
//
//    public void normalClickVisibility(){
//        videoType = "normal";
//        normal.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.normalyellow, 0, 0);
//        slow.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.slowg, 0, 0);
//        fast.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.fastg, 0, 0);
//        normal.setVisibility(View.VISIBLE);
//        slow.setVisibility(View.VISIBLE);
//        fast.setVisibility(View.VISIBLE);
//        frontBackCamera.setVisibility(View.VISIBLE);
//        mergeVideo.setVisibility(View.GONE);
//
//    }
//
//    public void slowClickVisibility(){
//        videoType = "slow";
//        normal.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.normalg, 0, 0);
//        slow.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.slow, 0, 0);
//        fast.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.fastg, 0, 0);
//        normal.setVisibility(View.VISIBLE);
//        slow.setVisibility(View.VISIBLE);
//        fast.setVisibility(View.VISIBLE);
//        frontBackCamera.setVisibility(View.VISIBLE);
//        mergeVideo.setVisibility(View.GONE);
//    }
//
//    public void fastClickVisibility(){
//        videoType = "fast";
//        normal.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.normalg, 0, 0);
//        slow.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.slowg, 0, 0);
//        fast.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.fast, 0, 0);
//
//
//    }
//
//    public void recordVisibility(){
//        startRecording.setBackgroundResource(R.drawable.vid);
//        normal.setVisibility(View.GONE);
//        slow.setVisibility(View.GONE);
//        fast.setVisibility(View.GONE);
//        frontBackCamera.setVisibility(View.GONE);
//        mergeVideo.setVisibility(View.GONE);
//        timer.setVisibility(View.VISIBLE);
//    }
//
//    public void stopVisibility(){
//        startRecording.setBackgroundResource(R.drawable.videoc);
//        frontBackCamera.setVisibility(View.GONE);
//        normal.setVisibility(View.GONE);
//        slow.setVisibility(View.GONE);
//        fast.setVisibility(View.GONE);
//        mergeVideo.setVisibility(View.VISIBLE);
//        timer.setVisibility(View.INVISIBLE);
//    }
//
////    private void loadFFMpegBinary() {
////        try {
////            //System.out.println("loadFFMpegBinary1");
////            if (ffmpeg == null) {
////                Log.d("", "ffmpeg : era nulo");
////                ffmpeg = FFmpeg.getInstance(this);
////            }
////            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
////                @Override
////                public void onFailure() {
////                    showUnsupportedExceptionDialog();
////                }
////
////                @Override
////                public void onSuccess() {
////                    Log.d("", "ffmpeg : correct Loaded");
////                }
////            });
////        } catch (FFmpegNotSupportedException e) {
////            //System.out.println("loadFFMpegBinary2");
////            showUnsupportedExceptionDialog();
////        } catch (Exception e) {
////            Log.d("", "EXception no controlada : " + e);
////        }
////    }
//
//    private void showUnsupportedExceptionDialog() {
//        new AlertDialog.Builder(ChildRecordSlowFastvideoNewScreen.this)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("Not Supported")
//                .setMessage("Device Not Supported")
//                .setCancelable(false)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ChildRecordSlowFastvideoNewScreen.this.finish();
//                    }
//                })
//                .create()
//                .show();
//    }
//
//
//    //fast motion code
//    private void executeFastMotionVideoCommandTEMP( String yourRealPath, String outputPath) {
//
//
//// Not sure if the / is on the path or not
//        File f = new File(yourRealPath);
//        //System.out.println("FileHere::"+f);
//        if(f.exists()){
//            //System.out.println("availableHere");
//        }else{
//            //System.out.println("notAvailableHere");
//        }
//
//
//        //System.out.println("inputPath:::" + yourRealPath);
//       // videoAddChunkList("fast");
//
//
//        //System.out.println("hereOutputPath1::" + outputPath);
//        tempPathVideo = outputPath;
////        File dest = new File(outputPath);
////
////        outputPath = dest.getAbsolutePath();
////        //System.out.println("hereOutputPath2::"+outputPath);
//
//
//
////         speed increase by 2x
////        String[] command = {"-y", "-i", yourRealPath, "-filter_complex", "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", outputPath};
//
////         speed increase by 4x
//        String[] command = {"-y", "-i", yourRealPath, "-filter_complex", "[0:v]setpts=0.25*PTS[v];[0:a]atempo=2.0[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", outputPath};
//
//
//        //System.out.println("here3");
//        //execFFmpegBinary(command);
//        //System.out.println("here4");
//
//    }
//
//    private void executeSlowMotionVideoCommandTEMP( String yourRealPath, String outputPath) {
//
//
//// Not sure if the / is on the path or not
//        File f = new File(yourRealPath);
//        //System.out.println("FileHere::"+f);
//        if(f.exists()){
//            //System.out.println("availableHere");
//        }else{
//            //System.out.println("notAvailableHere");
//        }
//
//
//        //System.out.println("inputPath:::" + yourRealPath);
//       // videoAddChunkList("slow");
//
//
//        //System.out.println("hereOutputPath1::" + outputPath);
//        tempPathVideo = outputPath;
//        File dest = new File(outputPath);
//
//        outputPath = dest.getAbsolutePath();
//        //System.out.println("hereOutputPath2::"+outputPath);
//
//
//        String[] command = {"-y", "-i", yourRealPath, "-filter_complex", "[0:v]setpts=2.0*PTS[v];[0:a]atempo=0.5[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", outputPath};
//
//
//        //System.out.println("here3");
//        //execFFmpegBinary(command);
//        //System.out.println("here4");
//
//    }
//
//
//    //fast motion code
//    private void executeFastMotionVideoCommand() {
//        //System.out.println("here1");
////        output = Environment.getExternalStorageDirectory().getAbsolutePath()+"/mergeVideo.mp4";
//
//        String videoCountString = ""+videoCount;
//        String video1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +  "input"+videoCountString+".mp4".toString();
//
//
//        String yourRealPath;
//        String outputPath;
//// Not sure if the / is on the path or not
//        File f = new File(video1);
//        //System.out.println("FileHere::"+f);
//        if(f.exists()){
//            //System.out.println("availableHere");
//        }else{
//            //System.out.println("notAvailableHere");
//        }
//
//
////        yourRealPath = getPath(ChildRecordSlowFastvideoNewScreen.this, Uri.fromFile(f));
//        yourRealPath = video1;
//        //System.out.println("inputPath:::" + yourRealPath);
//        videoAddChunkList("fast");
//
//
//        File dest = new File(output);
//
//        outputPath = dest.getAbsolutePath();
//        //System.out.println("here2");
//
////         speed increase by 2x
////        String[] command = {"-y", "-i", yourRealPath, "-filter_complex", "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", outputPath};
//
////         speed increase by 4x
//        String[] command = {"-y", "-i", yourRealPath, "-filter_complex", "[0:v]setpts=0.25*PTS[v];[0:a]atempo=2.0[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", outputPath};
//
//
//        //System.out.println("here3");
//       // execFFmpegBinary(command);
//        //System.out.println("here4");
//
//    }
//
//    private void executeSlowMotionVideoCommand() {
//        //  output = Environment.getExternalStorageDirectory().getAbsolutePath()+"/mergeVideo.mp4";
//        String yourRealPath;
//        String outputPath;
//
//        String videoCountString = ""+videoCount;
//        yourRealPath = getPath(ChildRecordSlowFastvideoNewScreen.this, Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/input"+videoCountString+".mp4".toString())));
//
//        videoAddChunkList("slow");
//        File dest = new File(output);
//
//        Log.d("HERE1", "startTrim: src: " + yourRealPath);
//        Log.d("HERE2", "startTrim: dest: " + dest.getAbsolutePath());
//        outputPath = dest.getAbsolutePath();
//
//        //slow video by 2x
//        String[] command = {"-y", "-i", yourRealPath, "-filter_complex", "[0:v]setpts=2.0*PTS[v];[0:a]atempo=0.5[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", outputPath};
//
////        //slow video by 3x
////        String[] command = {"-y", "-i", yourRealPath, "-filter_complex", "[0:v]setpts=3.0*PTS[v];[0:a]atempo=0.5[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", outputPath};
//
////        //slow video by 4x
////        String[] command = {"-y", "-i", yourRealPath, "-filter_complex", "[0:v]setpts=4.0*PTS[v];[0:a]atempo=0.5[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", outputPath};
//
//
//
//
//     //   execFFmpegBinary(command);
//
//    }
////    private void execFFmpegBinary(final String[] command) {
////        try {
////            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
////                @Override
////                public void onFailure(String s) {
////                    //System.out.println("onFailure::"+s);
////                    Log.d("", "FAILED with output : " + s);
////                }
////
////                @Override
////                public void onSuccess(String s) {
////                    //System.out.println("onSuccess::");
////                    Log.d("", "SUCCESS with output : " + s);
////
////                }
////
////                @Override
////                public void onProgress(String s) {
////                    if(videoType.equalsIgnoreCase("fast")){
////                        progressDialog.setMessage("Converting to Fast Motion... :\n " + s);
////                    }else if(videoType.equalsIgnoreCase("slow")){
////                        progressDialog.setMessage("Converting to Slow Motion... :\n " + s);
////                    }
////
////                    Log.d("", "progress : " + s);
////                }
////
////                @Override
////                public void onStart() {
////                    //System.out.println("onStart::");
////                    Log.d("", "Started command : ffmpeg " + command);
////                    progressDialog.setMessage("Converting...");
////                    progressDialog.show();
////                }
////
////                @Override
////                public void onFinish() {
////                    //System.out.println("onFinishCalled::");
////
////                    if(videoCount>2){
////                       // mergeVideo.performClick();
////                    }
////                    progressDialog.dismiss();
////                    Toast.makeText(ChildRecordSlowFastvideoNewScreen.this, "Video converted successfully", Toast.LENGTH_SHORT).show();
////
////            Intent obj = new Intent(ChildRecordSlowFastvideoNewScreen.this, ChildChooseMusicScreen.class);
////                    //System.out.println("tempPathVideo::"+tempPathVideo);
////            obj.putExtra("galleryPath", tempPathVideo);
////            startActivity(obj);
////            finish();
////                }
////            });
////        } catch (FFmpegCommandAlreadyRunningException e) {
////            // do nothing for now
////            e.printStackTrace();
////        }
////    }
//
//    /**
//     * Get a file path from a Uri. This will get the the path for Storage Access
//     * Framework Documents, as well as the _data field for the MediaStore and
//     * other file-based ContentProviders.
//     */
//    private String getPath(final Context context, final Uri uri) {
//
//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//
//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//
//                // TODO handle non-primary volumes
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[]{
//                        split[1]
//                };
//
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is ExternalStorageProvider.
//     */
//    private boolean isExternalStorageDocument(Uri uri) {
//        return "com.android.externalstorage.documents".equals(uri.getAuthority());
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is DownloadsProvider.
//     */
//    private boolean isDownloadsDocument(Uri uri) {
//        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is MediaProvider.
//     */
//    private boolean isMediaDocument(Uri uri) {
//        return "com.android.providers.media.documents".equals(uri.getAuthority());
//    }
//
//    /**
//     * Get the value of the data column for this Uri.
//     */
//    private String getDataColumn(Context context, Uri uri, String selection,
//                                 String[] selectionArgs) {
//
//        Cursor cursor = null;
//        final String column = "_data";
//        final String[] projection = {
//                column
//        };
//
//        try {
//            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
//                    null);
//            if (cursor != null && cursor.moveToFirst()) {
//                final int column_index = cursor.getColumnIndexOrThrow(column);
//                return cursor.getString(column_index);
//            }
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return null;
//    }
//
//    @Override
//    public void onBackPressed() {
//        dialogExit();
//    }
//
//    public  void dialogExit(){
//
//        AlertDialog.Builder builder1 = new AlertDialog.Builder(ChildRecordSlowFastvideoNewScreen.this);
//        builder1.setMessage("Are you sure you want to Exit?");
//        builder1.setCancelable(true);
//
//        builder1.setPositiveButton(
//                "Yes",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
//                        dialog.cancel();
//                    }
//                });
//
//        builder1.setNegativeButton(
//                "No",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog alert11 = builder1.create();
//        alert11.show();
//    }
//
//    public void appendVideo(ArrayList<ChildChunkVideoListBean> childChunkVideoListBeans) throws IOException{
//        String videoCombinePath="";
//        String message = "";
//        progressDialog = Utilities.createProgressDialog(ChildRecordSlowFastvideoNewScreen.this);
//        try{
//            message = "Video Merging Completed.";
//            Log.v(TAG, "in appendVideo() videos length is " + childChunkVideoListBeans.size());
//            com.googlecode.mp4parser.authoring.Movie[] inMovies = new com.googlecode.mp4parser.authoring.Movie[childChunkVideoListBeans.size()];
//            int index = 0;
//            for(ChildChunkVideoListBean video: childChunkVideoListBeans)
//            {
//                Log.i(TAG, "    in appendVideo one video path = " + video);
//                inMovies[index] = MovieCreator.build(video.getPath());
//                index++;
//            }
//            List<Track> videoTracks = new LinkedList<Track>();
//            List<Track> audioTracks = new LinkedList<Track>();
//            for (com.googlecode.mp4parser.authoring.Movie m : inMovies) {
//                for (Track t : m.getTracks()) {
//                    if (t.getHandler().equals("soun")) {
//                        audioTracks.add(t);
//                    }
//                    if (t.getHandler().equals("vide")) {
//                        videoTracks.add(t);
//                    }
//                }
//            }
//
//            com.googlecode.mp4parser.authoring.Movie result = new com.googlecode.mp4parser.authoring.Movie();
//            Log.v(TAG, "audioTracks size = " + audioTracks.size()
//                    + " videoTracks size = " + videoTracks.size());
//            if (audioTracks.size() > 0) {
//                result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
//            }
//            if (videoTracks.size() > 0) {
//                result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
//            }
//            //String videoCombinePath = RecordUtil.createFinalPath(MainActivity.this);
//            videoCombinePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "TestingCase.mp4".toString();
//            Container out = new DefaultMp4Builder().build(result);
//            FileChannel fc = new RandomAccessFile(videoCombinePath, "rw").getChannel();
//            out.writeContainer(fc);
//            fc.close();
//            Log.v(TAG, "after combine videoCombine path = " + videoCombinePath);
//            progressDialog.cancel();
//        }catch(Exception e){
//            progressDialog.cancel();
//            message = "Something went wrong. Please try after some time";
//            e.printStackTrace();
//
//        }finally {
//            progressDialog.cancel();
////            Toast.makeText(ChildRecordSlowFastvideoNewScreen.this, message, Toast.LENGTH_SHORT).show();
////            Intent obj = new Intent(ChildRecordSlowFastvideoNewScreen.this, ChildChooseMusicScreen.class);
////            obj.putExtra("galleryPath", videoCombinePath);
////            startActivity(obj);
////            finish();
//
//            if(videoType.equalsIgnoreCase("normal")){
//                //System.out.println("normalType");
//                //System.out.println("videoCombinePath::"+videoCombinePath);
//                Intent obj = new Intent(ChildRecordSlowFastvideoNewScreen.this, ChildChooseMusicScreen.class);
//                obj.putExtra("galleryPath", videoCombinePath);
//                startActivity(obj);
//                finish();
//            }else if(videoType.equalsIgnoreCase("fast")){
//                //System.out.println("fastType");
//                String outputPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +  "outputMp4Parser.mp4".toString();
//                executeFastMotionVideoCommandTEMP(videoCombinePath, outputPath);
//            }else if(videoType.equalsIgnoreCase("slow")){
//                //System.out.println("slowType");
//                String outputPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +  "outputMp4Parser.mp4".toString();
//                executeSlowMotionVideoCommandTEMP(videoCombinePath, outputPath);
//            }
//
//
//
//
//
//        }
//
//
//
//    }
//
//    public void videoAddChunkList(String videoType){
//        //System.out.println("TimerTextValue::"+ textValue);
//        int newTemValue = Integer.parseInt(textValue) - duration;
//        //System.out.println("newTemValue::"+ newTemValue);
//        String videoCountString = ""+videoCount;
//        output = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + videoType+""+videoCountString+".mp4".toString();
//        ChildChunkVideoListBean childChunkVideoListBean = new ChildChunkVideoListBean();
//        //System.out.println("videoCount::" + videoCount + "::output::" + output + "::duration::" + duration);
//        childChunkVideoListBean.setId("" + videoCount);
//        childChunkVideoListBean.setPath(output);
//
//
//        childChunkVideoListBean.setVideoDuration("" + newTemValue);
//        //System.out.println("DurationHere::" + newTemValue);
//        childChunkVideoListBeanArrayList.add(childChunkVideoListBean);
//
////
////        if(videoCount==1){
////            tempDuration = 15;
////            //System.out.println("tempDuration1::"+tempDuration);
////        }else{
////            tempDuration = duration;
////            //System.out.println("tempDuration2::"+tempDuration);
////        }
//
//        videoCount++;
//         childChunkVideoAdapter = new ChildChunkVideoAdapter(ChildRecordSlowFastvideoNewScreen.this, childChunkVideoListBeanArrayList);
//        list.setAdapter(childChunkVideoAdapter);
//        childChunkVideoAdapter.notifyDataSetChanged();
//    }
//
//
//
//
//
//}