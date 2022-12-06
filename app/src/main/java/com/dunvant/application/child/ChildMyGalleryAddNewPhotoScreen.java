package com.dunvant.application.child;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

////import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChildMyGalleryAddNewPhotoScreen extends AppCompatActivity {
    ProgressDialog pd;
    //FFmpeg ffmpeg;
    String typeGallery;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    String selectedImagePath;
    String accessStr;
    Uri selectedUri;
    TextView title;
    TextView accessText;
    ImageView backButton;
    EditText titlePhoto;
    TextView uploadPhotoVideo;
    ImageView attachment;
    RadioButton publicRadio;
    RadioButton privateRadio;
    Button upload;

    ImageView thumbnail;

    private Uri fileUri = null;
    private static final int CHOOSE_IMAGE_FROM_GALLERY = 1;
    private static final int CHOOSE_VIDEO_FROM_GALLERY = 1234;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 2;
    public static final int MEDIA_TYPE_VIDEO = 3;
    String titleStr;
    EditText descriptionPhoto;
    String descriptionStr;
    Typeface helvetica;
    Typeface linoType;

    // Run time permission
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_add_new_photo_screen);

        typeGallery = getIntent().getStringExtra("GALLERY_TYPE");

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        title = (TextView) findViewById(R.id.title);
        accessText = (TextView) findViewById(R.id.accessText);
        backButton = (ImageView) findViewById(R.id.backButton);
        titlePhoto = (EditText) findViewById(R.id.titlePhoto);
        descriptionPhoto = (EditText) findViewById(R.id.descriptionPhoto);
        uploadPhotoVideo = (TextView) findViewById(R.id.uploadPhotoVideo);
        attachment = (ImageView) findViewById(R.id.attachment);
        publicRadio = (RadioButton) findViewById(R.id.publicRadio);
        privateRadio = (RadioButton) findViewById(R.id.privateRadio);
        upload = (Button) findViewById(R.id.upload);
        thumbnail = findViewById(R.id.thumbnail);

        title.setTypeface(linoType);
        accessText.setTypeface(helvetica);
        titlePhoto.setTypeface(helvetica);
        descriptionPhoto.setTypeface(helvetica);
        uploadPhotoVideo.setTypeface(helvetica);
        upload.setTypeface(helvetica);

        imageLoader.init(ImageLoaderConfiguration.createDefault(ChildMyGalleryAddNewPhotoScreen.this));
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

        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = new String[]{"Take from camera", "Select from gallery"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ChildMyGalleryAddNewPhotoScreen.this, android.R.layout.select_dialog_item, items);
                AlertDialog.Builder builder = new AlertDialog.Builder(ChildMyGalleryAddNewPhotoScreen.this);

                builder.setTitle("Choose");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == 0) {

                            final String[] items = new String[]{"Image", "Video"};
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ChildMyGalleryAddNewPhotoScreen.this, android.R.layout.select_dialog_item, items);
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChildMyGalleryAddNewPhotoScreen.this);

                            builder.setTitle("Choose");
                            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {

                                    if (item == 0) {
                                        // Code to get image
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

                                    } else {
                                        // Code to get video
                                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
                                    }
                                }
                            });
                            AlertDialog innerDialog = builder.create();
                            innerDialog.show();


                        } else {
                            /*Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
                            mediaChooser.setType("video*//*, images*//*");
                            startActivityForResult(mediaChooser, CHOOSE_IMAGE_FROM_GALLERY);*/


                            android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(ChildMyGalleryAddNewPhotoScreen.this);
                            builder1.setMessage("Choose your option:");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Image",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            startActivityForResult(i, CHOOSE_IMAGE_FROM_GALLERY);
                                            dialog.cancel();
                                        }
                                    });

                            builder1.setNegativeButton(
                                    "Video",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                            startActivityForResult(i, CHOOSE_VIDEO_FROM_GALLERY);
                                            dialog.cancel();
                                        }
                                    });

                            android.app.AlertDialog alert11 = builder1.create();
                            alert11.show();

                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

//                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChildMyGalleryAddNewPhotoScreen.this);
//                builder1.setMessage("Choose your option:");
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton(
//                        "Image",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                startActivityForResult(i, CHOOSE_IMAGE);
//                                dialog.cancel();
//                            }
//                        });
//
//                builder1.setNegativeButton(
//                        "Video",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//                                startActivityForResult(i, CHOOSE_IMAGE);
//                                dialog.cancel();
//                            }
//                        });
//
//                AlertDialog alert11 = builder1.create();
//                alert11.show();



//                if (Build.VERSION.SDK_INT < 19) {
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.setType("image/* video/*");
//                    startActivityForResult(intent, CHOOSE_IMAGE);
//                } else {
//                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.setType("image/*");
//                    intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});
//                    startActivityForResult(intent, CHOOSE_IMAGE);
//                }


//                if(typeGallery.equalsIgnoreCase("allGallery")){
//                    Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
//                    mediaChooser.setType("video/*, images/*");
//                    startActivityForResult(mediaChooser, CHOOSE_IMAGE);
//                }else if(typeGallery.equalsIgnoreCase("photos")){
//                    Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
//                    mediaChooser.setType("image/*");
//                    startActivityForResult(mediaChooser, CHOOSE_IMAGE);
//                }else if(typeGallery.equalsIgnoreCase("videos")){
//                    Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
//                    mediaChooser.setType("video/*");
//                    startActivityForResult(mediaChooser, CHOOSE_IMAGE);
//                }


            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        titleStr = titlePhoto.getText().toString().trim();
                        descriptionStr = descriptionPhoto.getText().toString().trim();

//                if(titleStr==null || titleStr.isEmpty()){
//                    Toast.makeText(ChildMyGalleryAddNewPhotoScreen.this, "Please enter title", Toast.LENGTH_LONG).show();
//                }else if(descriptionStr==null || descriptionStr.isEmpty()){
//                    Toast.makeText(ChildMyGalleryAddNewPhotoScreen.this, "Please enter Description", Toast.LENGTH_LONG).show();
//                }else
//                if(selectedImagePath==null || selectedImagePath.isEmpty()){
//                    Toast.makeText(ChildMyGalleryAddNewPhotoScreen.this, "Please select Image/Video", Toast.LENGTH_LONG).show();
//                }else{
                    if(publicRadio.isChecked()){
                        accessStr = "1";
                    }else if(privateRadio.isChecked()){
                        accessStr = "0";
                    }
                    new UpdateImageVideoAsync(ChildMyGalleryAddNewPhotoScreen.this).execute();
                }

        //    }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_IMAGE_FROM_GALLERY:
                try {
                    selectedUri = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = ChildMyGalleryAddNewPhotoScreen.this.getContentResolver().query(selectedUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedImagePath = cursor.getString(columnIndex);
                    cursor.close();
                    File file = new File(selectedImagePath);
                    String nameFile = file.getName();
                    uploadPhotoVideo.setText(nameFile);

                    // showing thumbnail
                    imageLoader.displayImage(selectedUri.getPath(), thumbnail, options);
//                    thumbnail.setImageURI(selectedUri);
                    thumbnail.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case CHOOSE_VIDEO_FROM_GALLERY:
                try {
                    selectedUri = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = ChildMyGalleryAddNewPhotoScreen.this.getContentResolver().query(selectedUri, filePathColumn, null, null, null);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    try {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;

//                        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                        uploadPhotoVideo.setText(fileUri.getPath());


                        // showing thumbnail
//                        thumbnail.setImageURI(fileUri);
                        imageLoader.displayImage(fileUri.getPath(), thumbnail, this.options);
                        thumbnail.setVisibility(View.VISIBLE);

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
//                    Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
                } else {
                    // failed to capture image
                    Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
                }
                break;

            case CAMERA_CAPTURE_VIDEO_REQUEST_CODE:

                if (resultCode == RESULT_OK) {
                    try {
//                        videoPreview.setVideoPath(fileUri.getPath());
//                        videoPreview.start();

                        //ffmpegComressVideo();
//                        uploadPhotoVideo.setText(fileUri.getPath());
                        //System.out.println("Video recorded");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "You cancelled video recording", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry! Failed to record video", Toast.LENGTH_SHORT).show();
                }

                break;

//            case CHOOSE_IMAGE:
//                try {
//                    selectedUri = data.getData();
//                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
//                    Cursor cursor = ChildMyGalleryAddNewPhotoScreen.this.getContentResolver().query(selectedUri, filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    selectedImagePath = cursor.getString(columnIndex);
//                    cursor.close();
//                    File file = new File(selectedImagePath);
//                    String nameFile = file.getName();
//                    uploadPhotoVideo.setText(nameFile);
//                }catch (Exception e){
////                    Toast.makeText(ChildMyGalleryAddNewPhotoScreen.this,e.getMessage(),Toast.LENGTH_LONG).show();
//            }
//
//                break;

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

            //System.out.println("Uploading starting");

            String uploadUrl = Utilities.BASE_URL + "user_posts/upload_gallery";
            String strResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(uploadUrl);

            httpPost.addHeader("X-access-uid", loggedInUser.getId());
            httpPost.addHeader("X-access-token", loggedInUser.getToken());

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(context));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            try {
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                StringBody accessBody = new StringBody(accessStr);
                builder.addPart("is_public", accessBody);
                if(titleStr!=null){
                    StringBody titleBody = new StringBody(titleStr);
                    builder.addPart("title", titleBody);
                    //System.out.print("title:"+titleStr);
                }
                if(descriptionStr!=null){
                    StringBody descriptionBody = new StringBody(descriptionStr);
                    builder.addPart("description", descriptionBody);
                    //System.out.print("description:"+descriptionStr);
                }

                if(selectedUri != null) {
                    String imageMime = getMimeType(selectedUri);
                    File imageFile = new File(selectedImagePath);
                    //System.out.println("MIME_type__"+imageMime+"--Path__"+selectedImagePath+"--FileName__"+imageFile.getName());
                    FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");

                    builder.addPart("file", imageFileBody);
                }else if (fileUri != null) {

                    String fileMime = getMimeType(fileUri);

                    File file = new File(fileUri.getPath());
                    if(file != null){
                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
                        builder.addPart("file", imageFileBody);
                    }
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
                Toast.makeText(ChildMyGalleryAddNewPhotoScreen.this, "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    Toast.makeText(ChildMyGalleryAddNewPhotoScreen.this, Html.fromHtml(message), Toast.LENGTH_SHORT).show();

                    if(status) {
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ChildMyGalleryAddNewPhotoScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            ContentResolver cr = ChildMyGalleryAddNewPhotoScreen.this.getContentResolver();
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
            result = ContextCompat.checkSelfPermission(ChildMyGalleryAddNewPhotoScreen.this, p);
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
                    Toast.makeText(ChildMyGalleryAddNewPhotoScreen.this, "This feature cannot work without Permissions. \nTry Again and allow permissions or allow permissions in Applications Settings", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(Environment.getExternalStorageDirectory(), "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(Environment.getExternalStorageDirectory(), "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        /*File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                //System.out.println("Oops! Failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }*/

        return mediaFile;
    }

//    public void ffmpegComressVideo(){
//        pd = new ProgressDialog(ChildMyGalleryAddNewPhotoScreen.this);
//        pd.setMessage("Loading...");
//        pd.show();
//
//        ffmpeg = FFmpeg.getInstance(ChildMyGalleryAddNewPhotoScreen.this);
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
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        final String output = Environment.getExternalStorageDirectory()+"/VIDEO_" + timeStamp + ".mp4";
//
//
//        //System.out.println("input"+fileUri.getPath()+"output::"+output);
//
//
//        // Merge files
//
//        String[] command1 = new String[3];
//        command1[0] = "-i";
//        command1[1] = fileUri.getPath();
//        command1[2] = output;
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
//
//                    File file = new File(output);
//
//                    fileUri = Uri.fromFile(file);
//                    uploadPhotoVideo.setText(fileUri.getPath());
//                    pd.dismiss();
//
//                    // showing thumbnail
//                    Bitmap bMap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
//                    thumbnail.setImageBitmap(bMap);
//                    thumbnail.setVisibility(View.VISIBLE);
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            // Handle if FFmpeg is already running
//            //System.out.println("FFMPEG Exception");
//            e.printStackTrace();
//        }
//    }
}
