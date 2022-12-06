package com.dunvant.application.child.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildChooseSubMuscBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildCompleteRecordingScreen;
import com.dunvant.application.child.ChildRecordVideoMergeMp3Screen;
import com.dunvant.application.utils.Utilities;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by DEVLABS\pbadhan on 27/4/17.
 */
public class ChildChooseSubMusicAdapter extends BaseAdapter {
    MediaPlayer mediaPlayer;
    ProgressDialog pDialog;
    String titleStr;
    String urlStr;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Context context;
    ArrayList<ChildChooseSubMuscBean> childChooseSubMuscBeanArrayList;
    String galleryPath;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Typeface helvetica;
    Typeface linoType;

    public ChildChooseSubMusicAdapter(MediaPlayer mediaPlayer, Context context, ArrayList<ChildChooseSubMuscBean> childChooseSubMuscBeanArrayList, String galleryPath){
        this.mediaPlayer = mediaPlayer;
        this.context = context;
        this.childChooseSubMuscBeanArrayList = childChooseSubMuscBeanArrayList;
        this.galleryPath = galleryPath;
        layoutInflater = LayoutInflater.from(context);

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

    }

    @Override
    public int getCount() {
        return childChooseSubMuscBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childChooseSubMuscBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_choose_sub_music_item, null);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView subTitle = (TextView) convertView.findViewById(R.id.subTitle);
        ImageView thumb = (ImageView) convertView.findViewById(R.id.thumb);
        final ImageView playPause = (ImageView) convertView.findViewById(R.id.playPause);
        final ImageView downlodImage = (ImageView) convertView.findViewById(R.id.downlodImage);
        RelativeLayout nextScreen = (RelativeLayout) convertView.findViewById(R.id.nextScreen);

        title.setTypeface(helvetica);
        subTitle.setTypeface(helvetica);

        final ChildChooseSubMuscBean childChooseSubMuscBean = childChooseSubMuscBeanArrayList.get(position);

        title.setText(childChooseSubMuscBean.getTitle());
        subTitle.setText(childChooseSubMuscBean.getSubTitle());
        imageLoader.displayImage(childChooseSubMuscBean.getThumbnailUrl(), thumb, options);

        class soundDownloadAsync extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                pDialog = Utilities.createProgressDialog(context);

            }

            @Override
            protected String doInBackground(String... params) {
                String strResponse = null;
                try{

                    File cacheDir = new File(Environment.getExternalStorageDirectory().getPath()+"/DCIM/");
                    if(!cacheDir.exists())
                        cacheDir.mkdirs();

                    titleStr = titleStr.replace(" ", "_");
                    File f=new File(cacheDir,titleStr);
                    URL url = new URL(urlStr);

                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(f);

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count=0;
                    while ((count = input.read(data)) != -1) {
                        total++;
                        Log.e("while", "A" + total);


                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                    strResponse = "success";
                }catch(Exception e){
                    e.printStackTrace();
                    strResponse = "null";
                }


                return strResponse;
            }

            @Override
            protected void onPostExecute(String result) {
                if(result.equals("success")){
                    pDialog.dismiss();
                    String path = Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+titleStr;
//                    System.out.println("PathHere::"+path);
                    // Toast.makeText(context, "Congratulations! Ringtone saved in your device storage" + path, Toast.LENGTH_LONG).show();
                    playPause.setVisibility(View.VISIBLE);
                    downlodImage.setVisibility(View.GONE);
                }else {
                    pDialog.dismiss();
                    Toast.makeText(context, "Something went wrong, please try after sometime", Toast.LENGTH_LONG ).show();
                }
            }
        }


        if(childChooseSubMuscBean.getLocalCheck().equalsIgnoreCase("0")){
            playPause.setBackgroundResource(R.drawable.playimg);
            String checkFile = childChooseSubMuscBean.getTitle().replace(" ", "_");
            checkFile = checkFile+".mp3";
            File cacheDir = new File(Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+checkFile);
            if(cacheDir.exists()){
//                System.out.println("fileExistsHere::");
                playPause.setVisibility(View.VISIBLE);
                downlodImage.setVisibility(View.GONE);
            }else{
                playPause.setVisibility(View.GONE);
                downlodImage.setVisibility(View.VISIBLE);
            }
        }else{
            playPause.setBackgroundResource(R.drawable.pauseimg);
            playPause.setVisibility(View.VISIBLE);
            downlodImage.setVisibility(View.GONE);

        }

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleStr = childChooseSubMuscBean.getTitle().replace(" ", "_");
                titleStr = titleStr+".mp3";
                urlStr = childChooseSubMuscBean.getSongUrl();
                if(childChooseSubMuscBean.getLocalCheck().equalsIgnoreCase("0")){
//                    System.out.println("HEREif");
                    File cacheDir = new File(Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+titleStr);
                    if(cacheDir.exists()){
//                        System.out.println("fileExists::");
                        DefaultPlaySong();
                    }else {
//                        System.out.println("fileNotExists::");
                        if (Utilities.isNetworkAvailable(context)) {
                            new soundDownloadAsync().execute();
                        } else {
                            Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                        }
                    }
                    childChooseSubMuscBeanArrayList.get(position).setLocalCheck("1");
                    notifyDataSetChanged();
                }else{
//                    System.out.println("HEREelse");
                    mediaPlayer.stop();
                    childChooseSubMuscBeanArrayList.get(position).setLocalCheck("0");
                    notifyDataSetChanged();
                }



            }
        });

        downlodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleStr = childChooseSubMuscBean.getTitle().replace(" ", "_");
                titleStr = titleStr+".mp3";
                urlStr = childChooseSubMuscBean.getSongUrl();
//                System.out.println("downloadFileName::"+titleStr);

                File cacheDir = new File(Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+titleStr);
                if(cacheDir.exists()){
//                    System.out.println("fileExists::downloadButton");
                    playPause.setVisibility(View.VISIBLE);
                    downlodImage.setVisibility(View.GONE);
                }else {
//                    System.out.println("fileNotExists::downloadButton");
                    if (Utilities.isNetworkAvailable(context)) {
                        new soundDownloadAsync().execute();
                    } else {
                        Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.stop();
                }catch (Exception e){

                }
                titleStr = childChooseSubMuscBean.getTitle()+".mp3";
                titleStr = titleStr.replace(" ","_");
//                System.out.println("TitleHeader::"+titleStr);
                File cacheDir = new File(Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+titleStr);
                if(cacheDir.exists()){
                    if(galleryPath.equalsIgnoreCase("noPath")){
                        Intent obj = new Intent(context, ChildRecordVideoMergeMp3Screen.class);
                        obj.putExtra("filePath", Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + titleStr);
                        obj.putExtra("duration", "15");
                        context.startActivity(obj);
                   //     ((Activity)context).finish();
                    }else{
                        Intent obj = new Intent(context, ChildCompleteRecordingScreen.class);
                        obj.putExtra("galleryPath", galleryPath);
                        obj.putExtra("filePath", Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + titleStr);
                        obj.putExtra("audioAdd", "yes");
                        context.startActivity(obj);
                    //    ((Activity)context).finish();
                    }
                }else{
                    Toast.makeText(context, "Please download Music file", Toast.LENGTH_SHORT).show();

                }

            }
        });




        return convertView;
    }

    public void stopMedia(){
        mediaPlayer.stop();
    }



    public void DefaultPlaySong(){
        try {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+titleStr));
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }catch (Exception e){
            Toast.makeText(context, "Sorry! Player not Working", Toast.LENGTH_SHORT).show();
        }

    }
}