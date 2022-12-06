package com.dunvant.application.child;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.dunvant.application.R;
import com.dunvant.application.utils.Utilities;

import java.io.File;

//import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class ChildVideoFilterScreen extends AppCompatActivity {
    String videoRotatedFinalPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/IFA-filter-output.mp4";
    ProgressDialog pd;
//    FFmpeg ffmpeg;
    ImageView backButton;
    ProgressDialog progressDialog;
    String filePath;
    ImageView play;
    ProgressDialog pDialog;
    Button doneclick;
    VideoView videoView;
    TextView chrome;
    TextView blackAndWhite;
    TextView blur;
    TextView sharpner;
    TextView negative;
    TextView normal;
    String videoOutputPath;
    String outputFilename;
    String inputFileName;
    boolean bln;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_video_filter_screen);
        chrome = (TextView) findViewById(R.id.chrome);
        blur = (TextView) findViewById(R.id.blur);
        sharpner = (TextView) findViewById(R.id.sharpner);
        videoView = (VideoView) findViewById(R.id.videoView);
        doneclick = (Button) findViewById(R.id.doneclick);
        play = (ImageView) findViewById(R.id.play);
        negative = (TextView) findViewById(R.id.negative);
        blackAndWhite =(TextView) findViewById(R.id.blackAndWhite);
        normal = (TextView) findViewById(R.id.normal);
        backButton = (ImageView) findViewById(R.id.backButton);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



//        chrome.setShadowLayer(30, 0, 0, Color.BLACK);
//        blur.setShadowLayer(30, 0, 0, Color.BLACK);
//        sharpner.setShadowLayer(30, 0, 0, Color.BLACK);
//        negative.setShadowLayer(30, 0, 0, Color.BLACK);
//        blackAndWhite.setShadowLayer(30, 0, 0, Color.BLACK);
//        normal.setShadowLayer(30, 0, 0, Color.BLACK);

        videoOutputPath = getIntent().getStringExtra("videoOutputPath");
        filePath = getIntent().getStringExtra("filePath");
        //System.out.println("videoPath::"+videoOutputPath);
        //System.out.println("filePath::"+filePath);
        outputFilename = videoOutputPath;
        videoRotatedFinalPath = videoOutputPath;

        //System.out.println("videoPathHERE::" + videoOutputPath);
        //System.out.println("FilePathHERE::" + filePath);

        videoView.setVideoURI(Uri.parse(videoOutputPath));
        videoView.seekTo(100);


        doneclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(ChildVideoFilterScreen.this, ChildCompleteRecordingScreen.class);
                obj.putExtra("filePath", filePath);
                obj.putExtra("galleryPath", videoRotatedFinalPath);
                obj.putExtra("audioAdd", "no");
                startActivity(obj);
                finish();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bln == true) {
                   // play.setBackgroundResource(R.drawable.pauseimg);

                    play.setBackgroundResource(R.drawable.stopc);
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoView.getLayoutParams();
                    params.width = metrics.widthPixels;
                    params.height = metrics.heightPixels;
                    params.leftMargin = 0;
                    videoView.setLayoutParams(params);
                    //System.out.println("videoRotatedFinalPath::"+videoRotatedFinalPath);
                    videoView.setVideoPath(videoRotatedFinalPath);

                    videoView.start();
                    bln = false;

                } else {
                    bln = true;
                    play.setBackgroundResource(R.drawable.playc);
                    videoView.pause();

                }

                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    public void onCompletion(MediaPlayer mp)
                    {
                        // Do whatever u need to do here
                        play.setBackgroundResource(R.drawable.playc);
                    }
                });


            }
        });

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = Utilities.createProgressDialog(ChildVideoFilterScreen.this);

                // Delete file if already exists
                File file = new File(Environment.getExternalStorageDirectory() + "/normal.mp4");
                if(file.exists()) {
                    //System.out.println("File exists, deleting");
                    file.delete();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            outputFilename = Environment.getExternalStorageDirectory() + "/normal.mp4";
                            inputFileName = videoOutputPath;
                            //System.out.println("FILE_NAME::" + inputFileName);
                            Bitmap bmp = null;
                            //CGEFFmpegNativeLibrary.generateVideoWithFilter(outputFilename, inputFileName, "@beautify face 1 480 640", 1.0f, bmp, CGENativeLibrary.TextureBlendMode.CGE_BLEND_ADDREV, 1.0f, false);
//                            MsgUtil.toastMsg(ChildVideoFilterScreen.this, "Normal filter applied to video");
                        } catch (Exception e) {
                            progressDialog.cancel();
                        } finally {
                            progressDialog.cancel();
                           // videoRotateAndSave(outputFilename, "Normal filter applied to video");
                           // videoRotatedFinalPath = outputFilename;
                            //System.out.println("videoRotatedFinalPath::" + videoRotatedFinalPath);
                        }
                    }
                }, 1000);


            }
        });

        chrome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = Utilities.createProgressDialog(ChildVideoFilterScreen.this);
                // Delete file if already exists
                File file = new File(Environment.getExternalStorageDirectory() + "/chrome.mp4");
                if(file.exists()) {
                    //System.out.println("File exists, deleting");
                    file.delete();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            outputFilename = Environment.getExternalStorageDirectory() + "/chrome.mp4";
                            inputFileName = "/sdcard/output.mp4";
                            //System.out.println("FILE_NAME::" + inputFileName);
                            Bitmap bmp = null;
                            //CGEFFmpegNativeLibrary.generateVideoWithFilter(outputFilename, inputFileName, "@curve R(5, 49)(85, 173)(184, 249)G(23, 35)(65, 76)(129, 145)(255, 199)B(74, 69)(158, 107)(255, 126)", 1.0f, bmp, CGENativeLibrary.TextureBlendMode.CGE_BLEND_ADDREV, 1.0f, false);
//                            MsgUtil.toastMsg(ChildVideoFilterScreen.this, "Chrome filter applied to video");
                        } catch (Exception e) {
                            //System.out.println("FilterExceptionHere::");
                            e.printStackTrace();
                            progressDialog.cancel();
                        } finally {
                            progressDialog.cancel();
                           // videoRotateAndSave(outputFilename,"Chrome filter applied to video");
                           // videoRotatedFinalPath = outputFilename;
                            //System.out.println("videoRotatedFinalPath::" + videoRotatedFinalPath);
                        }
                    }
                }, 1000);


            }
        });

        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = Utilities.createProgressDialog(ChildVideoFilterScreen.this);
                // Delete file if already exists
                File file = new File(Environment.getExternalStorageDirectory() + "/blur.mp4");
                if(file.exists()) {
                    //System.out.println("File exists, deleting");
                    file.delete();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            outputFilename = Environment.getExternalStorageDirectory() + "/blur.mp4";
                            inputFileName = videoOutputPath;
                            //System.out.println("FILE_NAMEBlur::" + inputFileName);
                            Bitmap bmp = null;
                          //  CGEFFmpegNativeLibrary.generateVideoWithFilter(outputFilename, inputFileName, "@blur lerp 1", 1.0f, bmp, CGENativeLibrary.TextureBlendMode.CGE_BLEND_ADDREV, 1.0f, false);
//                            MsgUtil.toastMsg(ChildVideoFilterScreen.this, "Blur filter applied to video");
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.cancel();
                        }
                        finally {
                            progressDialog.cancel();
                          //  videoRotateAndSave(outputFilename,"Blur filter applied to video");
                           // videoRotatedFinalPath = outputFilename;
                            //System.out.println("videoRotatedFinalPath::" + videoRotatedFinalPath);
                        }
                    }
                }, 1000);



            }
        });

        sharpner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = Utilities.createProgressDialog(ChildVideoFilterScreen.this);
                // Delete file if already exists
                File file = new File(Environment.getExternalStorageDirectory() + "/sharpner.mp4");
                if(file.exists()) {
                    //System.out.println("File exists, deleting");
                    file.delete();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            outputFilename = Environment.getExternalStorageDirectory() + "/sharpner.mp4";
                            inputFileName = videoOutputPath;
                            //System.out.println("FILE_NAME::" + inputFileName);
                            Bitmap bmp = null;
                            //CGEFFmpegNativeLibrary.generateVideoWithFilter(outputFilename, inputFileName, "@adjust sharpen 10 1.5 ", 1.0f, bmp, CGENativeLibrary.TextureBlendMode.CGE_BLEND_ADDREV, 1.0f, false);
//                            MsgUtil.toastMsg(ChildVideoFilterScreen.this, "Sharpner filter applied to video");
                        } catch (Exception e) {
                            progressDialog.cancel();
                        } finally {
                            progressDialog.cancel();
                         //   videoRotateAndSave(outputFilename, "Sharpner filter applied to video");
                           // videoRotatedFinalPath = outputFilename;
                            //System.out.println("videoRotatedFinalPath::" + videoRotatedFinalPath);
                        }
                    }
                }, 1000);



            }
        });

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = Utilities.createProgressDialog(ChildVideoFilterScreen.this);
                // Delete file if already exists
                File file = new File(Environment.getExternalStorageDirectory() + "/negative.mp4");
                if(file.exists()) {
                    //System.out.println("File exists, deleting");
                    file.delete();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            outputFilename = Environment.getExternalStorageDirectory() + "/negative.mp4";
                            inputFileName = videoOutputPath;
                            //System.out.println("FILE_NAME::" + inputFileName);
                            Bitmap bmp = null;
                            //CGEFFmpegNativeLibrary.generateVideoWithFilter(outputFilename, inputFileName, "@adjust level 0.66 0.23 0.44 ", 1.0f, bmp, CGENativeLibrary.TextureBlendMode.CGE_BLEND_ADDREV, 1.0f, false);
                          //  MsgUtil.toastMsg(ChildVideoFilterScreen.this, "Negative filter applied to video");
                        } catch (Exception e) {
                            progressDialog.cancel();
                        } finally {
                            progressDialog.cancel();
                          //  videoRotateAndSave(outputFilename,"Negative filter applied to video");
                           // videoRotatedFinalPath = outputFilename;
                            //System.out.println("videoRotatedFinalPath::" + videoRotatedFinalPath);
                        }
                    }
                }, 1000);



            }
        });

        blackAndWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = Utilities.createProgressDialog(ChildVideoFilterScreen.this);
                // Delete file if already exists
                File file = new File(Environment.getExternalStorageDirectory() + "/blackandwhite.mp4");
                if(file.exists()) {
                    //System.out.println("File exists, deleting");
                    file.delete();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            outputFilename = Environment.getExternalStorageDirectory() + "/blackandwhite.mp4";
                            inputFileName = videoOutputPath;
                            //System.out.println("FILE_NAME::" + inputFileName);
                            Bitmap bmp = null;
                          //  CGEFFmpegNativeLibrary.generateVideoWithFilter(outputFilename, inputFileName, "@adjust hsv -1 -1 -1 -1 -1 -1", 1.0f, bmp, CGENativeLibrary.TextureBlendMode.CGE_BLEND_ADDREV, 1.0f, false);
                         //   MsgUtil.toastMsg(ChildVideoFilterScreen.this, "Black and white filter applied to video" + outputFilename);
                        } catch (Exception e) {
                            progressDialog.cancel();
                        } finally {
                            progressDialog.cancel();
                           // videoRotateAndSave(outputFilename,"Black and white filter applied to video");
                           // videoRotatedFinalPath = outputFilename;
                            //System.out.println("videoRotatedFinalPath::" + videoRotatedFinalPath);
                        }
                    }
                }, 1000);

            }
        });

    }

//    public void videoRotateAndSave(String withoutRotateVideoPath, final String toastMessage){
//        pd = Utilities.createProgressDialog(ChildVideoFilterScreen.this);
//
//        ffmpeg = FFmpeg.getInstance(ChildVideoFilterScreen.this);
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
//        videoRotatedFinalPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/IFA-filter-output.mp4";
//        // Delete file if already exists
//        File file = new File(videoRotatedFinalPath);
//        if(file.exists()) {
//            //System.out.println("File exists, deleting");
//            file.delete();
//        }
//
//        // rotate video
////        String[] command1 = new String[5];
////        command1[0] = "-i";
////        command1[1] = withoutRotateVideoPath;
////        command1[2] = "-vf";
////        command1[3] = "\"transpose=1\"";
////        command1[4] = videoRotatedFinalPath;
//
//        String[] command1 = new String[7];
//        command1[0] = "-i";
//        command1[1] = withoutRotateVideoPath;
//        command1[2] = "-c";
//        command1[3] = "copy";
//        command1[4] = "-metadata:s:v:0";
//        command1[5] = "rotate=90";
//        command1[6] = videoRotatedFinalPath;
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
//                    //System.out.println("videoRotatedFinalPath:::"+videoRotatedFinalPath);
//                    pd.dismiss();
//                    Toast.makeText(ChildVideoFilterScreen.this, toastMessage, Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            // Handle if FFmpeg is already running
//            //System.out.println("FFMPEG Exception");
//            e.printStackTrace();
//        }
//    }
}
