package com.dunvant.application.child.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildChooseMusicScreen;
//import com.ifasport.application.child.ChildRecordSlowFastvideoNewScreen;
import com.dunvant.application.child.ChildVideoHistoryScreen;
import com.dunvant.application.utils.Utilities;

/**
 * Created by DEVLABS\pbadhan on 26/4/17.
 */
public class ChildCaptureVideoFragment extends Fragment {
    ImageView importVideo;
    ImageView recordVideo;
    ImageView chooseMusic;
    ImageView viewHistory;
    private final int CHOOSE_IMAGE = 1;
    Uri selectedUri;
    String selectedImagePath;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_fragment_capture_video, container, false);
        importVideo = (ImageView) view.findViewById(R.id.importVideo);
        recordVideo = (ImageView) view.findViewById(R.id.recordVideo);
        chooseMusic = (ImageView) view.findViewById(R.id.chooseMusic);
        viewHistory = (ImageView) view.findViewById(R.id.viewHistory);

        importVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ChildRecordSlowFastvideoNewScreen.VIDEOTYPE="normal";
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, CHOOSE_IMAGE);
                }
                else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("Minimum supported Android version is 6.0");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });

        recordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    // only for newer versions
//                    Intent obj = new Intent(getActivity(), ChildRecordSlowFastvideoNewScreen.class);
//                    startActivity(obj);
                }
                else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("Minimum supported Android version is 6.0");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }


            }
        });

        chooseMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChildRecordSlowFastvideoNewScreen.VIDEOTYPE="normal";
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    // only for newer versions
                    Intent obj = new Intent(getActivity(), ChildChooseMusicScreen.class);
                    obj.putExtra("galleryPath", "noPath");
                    startActivity(obj);
                }
                else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage("Minimum supported Android version is 6.0");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

            }
        });

        viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(getActivity(), ChildVideoHistoryScreen.class);
                startActivity(obj);
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case CHOOSE_IMAGE:
                try {
                    selectedUri = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedImagePath = cursor.getString(columnIndex);
                    cursor.close();
//                    System.out.println("path::"+selectedImagePath);
                    Intent obj = new Intent(getActivity(), ChildChooseMusicScreen.class);
                    obj.putExtra("galleryPath", selectedImagePath);
                    startActivity(obj);
                }catch (Exception e){
//                    Toast.makeText(ChildMyGalleryAddNewPhotoScreen.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }

                break;

        }
    }


}
