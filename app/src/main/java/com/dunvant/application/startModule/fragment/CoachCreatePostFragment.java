package com.dunvant.application.startModule.fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CampDaysBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildBlankActivityForCreatePost;
import com.dunvant.application.child.ChildMainScreen;
import com.dunvant.application.child.fragments.ChildPostFragment;
import com.dunvant.application.coach.adapters.CoachLocationListingAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CoachCreatePostFragment extends Fragment implements IWebServiceCallback {
    private final String GET_LOCATION_LISTING = "GET_LOCATION_LISTING";
    ArrayList<CampLocationBean> locationsList = new ArrayList<>();
    CoachLocationListingAdapter coachLocationListingAdapter;
    Spinner coachesSpinner;
    String coachesSpinnerStrID = "";

    ProgressDialog pd;
    //    FFmpeg ffmpeg;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    EditText postText;
    TextView browse;
    EditText youtubeUrl;
    Button submit;

    //private final int CHOOSE_IMAGE = 1;
    Uri selectedUri;
    String selectedImagePath;

    String title;
    String strYoutubeUrl;

    private static final int CHOOSE_IMAGE_FROM_GALLERY = 1;
    private static final int CHOOSE_VIDEO_FROM_GALLERY = 1234;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 2;
    public static final int MEDIA_TYPE_VIDEO = 3;
    private Uri fileUri = null;
    ImageView thumbnail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coach_fragment_create_post_new, container, false);

        postText = (EditText) view.findViewById(R.id.postText);
        youtubeUrl = view.findViewById(R.id.youtubeUrl);
        browse = (TextView) view.findViewById(R.id.browse);
        submit = (Button) view.findViewById(R.id.submit);
        thumbnail = view.findViewById(R.id.thumbnail);
        coachesSpinner = view.findViewById(R.id.coachesSpinner);

        postText.setHintTextColor(getResources().getColor(R.color.white));
        youtubeUrl.setHintTextColor(getResources().getColor(R.color.white));

        coachesSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = postText.getText().toString().trim();
                strYoutubeUrl = youtubeUrl.getText().toString().trim();

                if (coachesSpinnerStrID.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please choose coach", Toast.LENGTH_SHORT).show();
                } else if (title == null || title.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter post", Toast.LENGTH_SHORT).show();
                } else {
                    new com.dunvant.application.startModule.fragment.CoachCreatePostFragment.UpdateImageVideoAsync(getActivity()).execute();
                }
            }
        });

        youtubeUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!youtubeUrl.getText().toString().trim().isEmpty()) {
                    thumbnail.setVisibility(View.GONE);
                    selectedUri = null;
                    fileUri = null;
                    browse.setText("Upload Photo/Video");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                final String[] items = new String[]{"Take from camera", "Select from gallery"};
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, items);
//                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
//
//                builder.setTitle("Choose");
//                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int item) {
//
//                        if (item == 0) {
//
//                            final String[] items = new String[]{"Image", "Video"};
//                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, items);
//                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
//
//                            builder.setTitle("Choose");
//                            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int item) {
//
//                                    if (item == 0) {
//                                        // Code to get image
//                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                                        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//
//                                    } else {
//                                        // Code to get video
//                                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
//                                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//                                        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 12582912L);
//                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                                        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
//                                    }
//                                }
//                            });
//                            androidx.appcompat.app.AlertDialog innerDialog = builder.create();
//                            innerDialog.show();
//
//
//                        } else {
//                            /*Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
//                            mediaChooser.setType("video*//*, images*//*");
//                            startActivityForResult(mediaChooser, CHOOSE_IMAGE_FROM_GALLERY);*/
//
//
                            android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(getActivity());
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
//
//                        }
//                    }
//                });
//                androidx.appcompat.app.AlertDialog dialog = builder.create();
//                dialog.show();


            }
        });

        getLocationListing();

        coachesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    CampDaysBean daysBean = new CampDaysBean();
                    daysBean.setDay("-1");
                    daysBean.setDayLabel("Choose Coach");
                    coachesSpinnerStrID = "";
                } else {
                    coachesSpinnerStrID = locationsList.get(position).getLocationId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

//            case CHOOSE_IMAGE:
//                try {
//                    selectedUri = data.getData();
//                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
//                    Cursor cursor = getActivity().getContentResolver().query(selectedUri, filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    selectedImagePath = cursor.getString(columnIndex);
//                    cursor.close();
//                    File file = new File(selectedImagePath);
//                    String nameFile = file.getName();
//                    browse.setText(nameFile);
//                }catch (Exception e){
//                  //  Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//                break;


            case CHOOSE_IMAGE_FROM_GALLERY:
                try {
                    selectedUri = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedImagePath = cursor.getString(columnIndex);
                    cursor.close();
                    File file = new File(selectedImagePath);
                    String nameFile = file.getName();
                    browse.setText(nameFile);

                    // showing thumbnail
                    thumbnail.setImageURI(selectedUri);
                    thumbnail.setVisibility(View.VISIBLE);

                    // clearing youtube url
                    youtubeUrl.setText("");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case CHOOSE_VIDEO_FROM_GALLERY:
                try {
                    selectedUri = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedImagePath = cursor.getString(columnIndex);
                    cursor.close();
                    File file = new File(selectedImagePath);
                    String nameFile = file.getName();
                    browse.setText(nameFile);


                    // showing thumbnail
                    Bitmap bMap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                    thumbnail.setImageBitmap(bMap);
                    thumbnail.setVisibility(View.VISIBLE);

                    // clearing youtube url
                    youtubeUrl.setText("");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == getActivity().RESULT_OK) {

                    try {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;

//                        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                        browse.setText(fileUri.getPath());

                        // showing thumbnail
                        thumbnail.setImageURI(fileUri);
                        thumbnail.setVisibility(View.VISIBLE);

                        // clearing youtube url
                        youtubeUrl.setText("");

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                } else if (resultCode == getActivity().RESULT_CANCELED) {
                    // user cancelled Image capture
//                    Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
                } else {
                    // failed to capture image
                    Toast.makeText(getActivity(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
                }
                break;

            case CAMERA_CAPTURE_VIDEO_REQUEST_CODE:

                if (resultCode == getActivity().RESULT_OK) {
                    try {
//                        videoPreview.setVideoPath(fileUri.getPath());
//                        videoPreview.start();
//                        ffmpegComressVideo();
                        // browse.setText(fileUri.getPath());
//                        System.out.println("Video recorded");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == getActivity().RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "You cancelled video recording", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Sorry! Failed to record video", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    private class UpdateImageVideoAsync extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog;
        Context context;

        public UpdateImageVideoAsync(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pDialog = Utilities.createProgressDialog(context);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(Void... voids) {

//            System.out.println("Uploading starting");

            String uploadUrl = Utilities.BASE_URL + "coach/create_new_timeline";
            String strResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(uploadUrl);

            httpPost.addHeader("X-access-uid", loggedInUser.getId());
            httpPost.addHeader("X-access-token", loggedInUser.getToken());

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(getActivity()));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            try {

                StringBody coachBody = new StringBody(coachesSpinnerStrID);

                StringBody titleBody = new StringBody(title);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.addPart("timeline_select_coach", coachBody);
                builder.addPart("timeline_description", titleBody);

                if (selectedUri != null) {
                    String imageMime = getMimeType(selectedUri);
                    File imageFile = new File(selectedImagePath);
//                    System.out.println("MIME_type__" + imageMime + "--Path__" + selectedImagePath + "--FileName__" + imageFile.getName());
                    FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");

                    builder.addPart("file", imageFileBody);
                } else if (fileUri != null) {

                    String fileMime = getMimeType(fileUri);

                    File file = new File(fileUri.getPath());
                    if (file != null) {
                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
                        builder.addPart("file", imageFileBody);
                    }
                }

                if (strYoutubeUrl != null && !strYoutubeUrl.isEmpty()) {
                    StringBody youtubeUrlBody = new StringBody(strYoutubeUrl);
                    builder.addPart("youtube_url", youtubeUrlBody);
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

//                System.out.println("File upload end");

            } catch (Exception e) {
                e.printStackTrace();
//                System.out.println("Exception " + e.getMessage());
            }

            return strResponse;

        }

        @Override
        protected void onPostExecute(String response) {

//            System.out.println("Response " + response);

            if (response == null) {
                Toast.makeText(getActivity(), "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    if (status) {
                        if (getActivity() instanceof ChildMainScreen) {
//                        ((ChildMainScreen) getActivity()).showChildPost();
                            ((ChildMainScreen) getActivity()).showDashboardScreen();
                        } else if (getActivity() instanceof ChildBlankActivityForCreatePost) {
                            ChildPostFragment.comingFromCreatePost = true;
                            getActivity().finish();
                        }else{
                            getActivity().finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    public String getMimeType(Uri uri) {
        String mimeType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getActivity().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
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
        return mediaFile;
    }
//    public void ffmpegComressVideo(){
//        pd = new ProgressDialog(getActivity());
//        pd.setMessage("Loading...");
//        pd.show();
//
//        ffmpeg = FFmpeg.getInstance(getActivity());
//        try {
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//
//                @Override
//                public void onStart() {
////                    System.out.println("FFMPEG onStart");
//                }
//
//                @Override
//                public void onFailure() {
////                    System.out.println("FFMPEG onFailure");
//                }
//
//                @Override
//                public void onSuccess() {
////                    System.out.println("FFMPEG onSuccess");
//                }
//
//                @Override
//                public void onFinish() {
////                    System.out.println("FFMPEG onFinish");
//                }
//            });
//        } catch (FFmpegNotSupportedException e) {
//            // Handle if FFmpeg is not supported by device
////            System.out.println("FFMPEG Exception");
//            e.printStackTrace();
//        }
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        final String output = Environment.getExternalStorageDirectory()+"/VIDEO_" + timeStamp + ".mp4";
//
//
////        System.out.println("input"+fileUri.getPath()+"output::"+output);
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
////                    System.out.println("FFMPEG onStart");
//                }
//
//                @Override
//                public void onProgress(String message) {
////                    System.out.println("FFMPEG onProgress "+message);
//                }
//
//                @Override
//                public void onFailure(String message) {
////                    System.out.println("FFMPEG onFailure "+message);
//                }
//
//                @Override
//                public void onSuccess(String message) {
////                    System.out.println("FFMPEG onSuccess "+message);
//                }
//
//                @Override
//              public void onFinish() {
//                    File file = new File(output);
//                    fileUri = Uri.fromFile(file);
//                    browse.setText(fileUri.getPath());
//                    pd.dismiss();
//
//
//                    // showing thumbnail
//                    Bitmap bMap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
//                    thumbnail.setImageBitmap(bMap);
//                    thumbnail.setVisibility(View.VISIBLE);
//
//                    // clearing youtube url
//                    youtubeUrl.setText("");
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            // Handle if FFmpeg is already running
////            System.out.println("FFMPEG Exception");
//            e.printStackTrace();
//        }
//    }

    private void getLocationListing() {
        if (Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "coach/get_coaches_list";
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_LOCATION_LISTING, CoachCreatePostFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_LOCATION_LISTING:

                locationsList.clear();

                // 0th Element Choose Location
                CampLocationBean locationBean = new CampLocationBean();
                locationBean.setLocationId("-1");
                locationBean.setLocationName("Choose Coach");

                locationsList.add(locationBean);

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

//                            JSONArray dataArray = responseObject.getJSONArray("data");
                            JSONArray dataArray = responseObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject locationObject = dataArray.getJSONObject(i);
                                locationBean = new CampLocationBean();

                                locationBean.setLocationId(locationObject.getString("id"));
                                locationBean.setLocationName(locationObject.getString("full_name"));


                                locationsList.add(locationBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachLocationListingAdapter = new CoachLocationListingAdapter(getActivity(), locationsList);
                coachesSpinner.setAdapter(coachLocationListingAdapter);
                break;

        }
    }
}
