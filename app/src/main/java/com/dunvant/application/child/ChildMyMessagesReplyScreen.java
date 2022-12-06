package com.dunvant.application.child;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

public class ChildMyMessagesReplyScreen extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    String selectedImagePath;
    String accessStr;
    Uri selectedUri;
    TextView title;
    ImageView backButton;
    EditText titlePhoto;
    EditText message;
    TextView uploadPhotoVideo;
    ImageView attachment;
    Button upload;
    ImageView thumbnail;

    private final int CHOOSE_IMAGE = 1;
    private final int CHOOSE_VIDEO = 2;
    String titleStr;
    String messageStr;
    String friendIdStr;
    String parentIdStr;
    Typeface helvetica;
    Typeface linoType;
    //String fullNameStr;

    // Run time permission
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_my_messages_reply_screen);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        friendIdStr = getIntent().getStringExtra("friendId");
        parentIdStr = getIntent().getStringExtra("parentId");
        //fullNameStr = getIntent().getStringExtra("fullName");


        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);
        titlePhoto = (EditText) findViewById(R.id.titlePhoto);
        message = (EditText) findViewById(R.id.message);
        uploadPhotoVideo = (TextView) findViewById(R.id.uploadPhotoVideo);
        attachment = (ImageView) findViewById(R.id.attachment);
        upload = (Button) findViewById(R.id.upload);
        thumbnail = findViewById(R.id.thumbnail);

        title.setTypeface(linoType);
        uploadPhotoVideo.setTypeface(helvetica);
        upload.setTypeface(helvetica);

        String subject = getIntent().getStringExtra("subject");
        titlePhoto.setText(subject);

       // title.setText(fullNameStr);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChildMyMessagesReplyScreen.this);
                builder1.setMessage("Choose your option:");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Image",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, CHOOSE_IMAGE);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Video",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, CHOOSE_VIDEO);
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleStr = titlePhoto.getText().toString().trim();
                messageStr = message.getText().toString().trim();

                if(titleStr==null || titleStr.isEmpty()){
                    Toast.makeText(ChildMyMessagesReplyScreen.this, "Please enter title", Toast.LENGTH_LONG).show();
                }else if(messageStr==null || messageStr.isEmpty()){
                    Toast.makeText(ChildMyMessagesReplyScreen.this, "Please enter message", Toast.LENGTH_LONG).show();
                }/*else if(selectedImagePath==null || selectedImagePath.isEmpty()){
                    Toast.makeText(ChildMyMessagesReplyScreen.this, "Please select Image/Video", Toast.LENGTH_LONG).show();
                }*/else{
                    new UpdateImageVideoAsync(ChildMyMessagesReplyScreen.this).execute();
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case CHOOSE_IMAGE:
                try {
                    selectedUri = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = ChildMyMessagesReplyScreen.this.getContentResolver().query(selectedUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedImagePath = cursor.getString(columnIndex);
                    cursor.close();
                    File file = new File(selectedImagePath);
                    String nameFile = file.getName();
                    uploadPhotoVideo.setText(nameFile);

                    // showing thumbnail
                    thumbnail.setImageURI(selectedUri);
                    thumbnail.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    Toast.makeText(ChildMyMessagesReplyScreen.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }

                break;

            case CHOOSE_VIDEO:
                try {
                    selectedUri = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = ChildMyMessagesReplyScreen.this.getContentResolver().query(selectedUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedImagePath = cursor.getString(columnIndex);
                    cursor.close();
                    File file = new File(selectedImagePath);
                    String nameFile = file.getName();
                    uploadPhotoVideo.setText(nameFile);

                    // showing thumbnail
                    Bitmap bMap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                    thumbnail.setImageBitmap(bMap);
                    thumbnail.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    Toast.makeText(ChildMyMessagesReplyScreen.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

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


            String uploadUrl = Utilities.BASE_URL + "inbox/reply_by_child";
            String strResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(uploadUrl);

            httpPost.addHeader("X-access-uid", loggedInUser.getId());
            httpPost.addHeader("X-access-token", loggedInUser.getToken());

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(context));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            try {

                StringBody friendIdBody = new StringBody(friendIdStr);
                StringBody titleBody = new StringBody(titleStr);
                StringBody messageBody = new StringBody(messageStr);
                StringBody parentIdBody = new StringBody(parentIdStr);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();

                builder.addPart("friend_id", friendIdBody);
                builder.addPart("title", titleBody);
                builder.addPart("message", messageBody);
                builder.addPart("parent_id", parentIdBody);

                //System.out.println("Friend id " + friendIdStr);
                //System.out.println("Parent id "+parentIdStr);

                if(selectedUri != null) {
                    String imageMime = getMimeType(selectedUri);
                    File imageFile = new File(selectedImagePath);
                    //System.out.println("MIME_type__"+imageMime+"--Path__"+selectedImagePath+"--FileName__"+imageFile.getName());
                    FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");

                    builder.addPart("file", imageFileBody);
                }


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
                Toast.makeText(ChildMyMessagesReplyScreen.this, "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    Toast.makeText(ChildMyMessagesReplyScreen.this, message, Toast.LENGTH_SHORT).show();

                    if(status) {
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ChildMyMessagesReplyScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            ContentResolver cr = ChildMyMessagesReplyScreen.this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissions()){
//                Toast.makeText(ChildTrackWorkoutScreen.this, "Permissions already granted", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(ChildTrackWorkoutScreen.this, "Initially permission not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(ChildMyMessagesReplyScreen.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permissions granted.
                } else {
                    // no permissions granted.
                    Toast.makeText(ChildMyMessagesReplyScreen.this, "This feature cannot work without Permissions. \nTry Again and allow permissions or allow permissions in Applications Settings", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }
}
