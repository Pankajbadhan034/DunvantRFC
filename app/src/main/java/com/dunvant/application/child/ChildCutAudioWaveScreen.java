package com.dunvant.application.child;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dunvant.application.R;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.IOException;

public class ChildCutAudioWaveScreen extends AppCompatActivity {
    boolean firstTimeClick=true;
    String finalFilePath;
    Button doneClick;
    RangeSeekBar seekBar;
    int a,b;
    int duration;
    MediaPlayer mp;
    ProgressDialog progressDialog;
   // FFmpeg ffmpeg;
    ImageView backButton;
    ImageView playClick;
    String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_cut_audio_wave_screen);
        backButton = (ImageView) findViewById(R.id.backButton);
        playClick = (ImageView) findViewById(R.id.playClick);
        seekBar =  (RangeSeekBar) findViewById(R.id.seekBar);
        doneClick = (Button) findViewById(R.id.doneclick);

        seekBar.setRangeValues(0, 100);

        filePath = getIntent().getStringExtra("filePath");

        //System.out.println("FILEPathHERE::"+filePath);

       // cutAudioStarting();

        doneClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mp.stop();
                }catch (Exception e){

                }
                Intent videoRecording = new Intent(ChildCutAudioWaveScreen.this, ChildRecordVideoMergeMp3Screen.class);
                videoRecording.putExtra("filePath", finalFilePath);
                duration = duration/1000;
                //System.out.println("finalDurationHERE::"+duration);
                videoRecording.putExtra("duration", ""+duration);
                startActivity(videoRecording);
                finish();
            }
        });

//        playClick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(firstTimeClick){
//                    Toast.makeText(ChildCutAudioWaveScreen.this, "Please trim sound", Toast.LENGTH_SHORT).show();
//                }else{
//                    //System.out.println(seekBar.getSelectedMinValue() + " : " + seekBar.getSelectedMaxValue());
//
//                    // Delete file if already exists
//
//
//                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/IFA-trimNew.mp3");
//                    if (file.exists()) {
//                        //System.out.println("File exists, deleting");
//                        file.delete();
//                    }
//
//                    //        ffmpeg -i input.mp3 -ss 10 -t 6 -acodec copy output.mp3
//
//                    String[] command1 = new String[9];
//                    command1[0] = "-i";
//                    command1[1] = filePath;
//                    command1[2] = "-ss";
//                    command1[3] = a + "";
//                    command1[4] = "-t";
//                    command1[5] = (b - a) + "";
//                    command1[6] = "-acodec";
//                    command1[7] = "copy";
//                    command1[8] = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IFA-trimNew.mp3";
//
//                    try {
//                        // to execute "ffmpeg -version" command you just need to pass "-version"
//                        ffmpeg.execute(command1, new ExecuteBinaryResponseHandler() {
//
//                            @Override
//                            public void onStart() {
//                                //System.out.println("FFMPEG onStart");
//                            }
//
//                            @Override
//                            public void onProgress(String message) {
//                                //System.out.println("FFMPEG onProgress " + message);
//                            }
//
//                            @Override
//                            public void onFailure(String message) {
//                                //System.out.println("FFMPEG onFailure " + message);
//                            }
//
//                            @Override
//                            public void onSuccess(String message) {
//                                //System.out.println("FFMPEG onSuccess " + message);
//                            }
//
//                            @Override
//                            public void onFinish() {
//                                //System.out.println("FFMPEG onFinish");
//                                finalFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IFA-trimNew.mp3";
//
//                                //System.out.println("PATH::" + finalFilePath);
//                                mp = MediaPlayer.create(ChildCutAudioWaveScreen.this, Uri.parse(finalFilePath));
////                            mp.setLooping(true);
//                                mp.start();
//
//
//                                durationGetSong(finalFilePath);
//
//
//                            }
//                        });
//                    } catch (FFmpegCommandAlreadyRunningException e) {
//                        // Handle if FFmpeg is already running
//                        //System.out.println("FFMPEG Exception");
//                        Toast.makeText(ChildCutAudioWaveScreen.this, "Please Trim audio", Toast.LENGTH_SHORT).show();
//                        e.printStackTrace();
//                    }
//                }
//
//
//
//            }
//        });


        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {

                firstTimeClick=false;

                try{
                    mp.stop();
                }catch (Exception e){

                }


                a = (int) minValue;
                b = (int) maxValue;

                //System.out.println("a " + a + " b = " + b);

                a = a * (duration / 1000) / 100;
                b = b * (duration / 1000) / 100;

                //System.out.println("After calculation a " + a + " b = " + b);

               // min.setText("Min : " + a);
               // max.setText("Max : " + b);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

//    public void cutAudioStarting(){
//        progressDialog = Utilities.createProgressDialog(ChildCutAudioWaveScreen.this);
//        ffmpeg = FFmpeg.getInstance(ChildCutAudioWaveScreen.this);
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
//     File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/IFA-trim.mp3");
//        if(file.exists()) {
//            //System.out.println("File exists, deleting");
//            file.delete();
//        }
//
//        //        ffmpeg -i input.mp3 -ss 10 -t 6 -acodec copy output.mp3
//
//        String[] command1 = new String[9];
//        command1[0] = "-i";
//        command1[1] = filePath;
//        command1[2] = "-ss";
//        command1[3] = "0";
//        command1[4] = "-t";
//        command1[5] = "14";
//        command1[6] = "-acodec";
//        command1[7] = "copy";
//        command1[8] = Environment.getExternalStorageDirectory().getAbsolutePath()+"/IFA-trim.mp3";
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
//                    progressDialog.cancel();
//                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/IFA-trim.mp3";
//                    durationGetSong(filePath);
//
////                    Intent videoRecording = new Intent(ChildCutAudioWaveScreen.this, ChildRecordVideoMergeMp3Screen.class);
////                    videoRecording.putExtra("filePath", Environment.getExternalStorageDirectory().getAbsolutePath()+"/IFA-trim.mp3");
////                    startActivity(videoRecording);
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            // Handle if FFmpeg is already running
//            //System.out.println("FFMPEG Exception");
//            e.printStackTrace();
//            progressDialog.cancel();
//        }
//    }

    public void durationGetSong(String path){
        mp = new  MediaPlayer();
        try {
            mp.setDataSource(path);
            mp.prepare();
            duration = mp.getDuration();
            //System.out.println("Song Duration "+duration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
