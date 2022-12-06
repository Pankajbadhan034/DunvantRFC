package com.dunvant.application.parent;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachBean;
import com.dunvant.application.beans.DocumentBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ParentUploadDocumentsScreen extends AppCompatActivity implements IWebServiceCallback {

    private final int CHOOSE_IMAGE = 1;
    Uri selectedUri;
    String selectedImagePath;
    ArrayList<CoachBean> coachesList = new ArrayList<>();
    private final String GET_COACHES_LISTING = "GET_COACHES_LISTING";
    String shareWithCoachStr;
    ArrayList<String> categories = new ArrayList<>();
    //ArrayList<String> categoriesID = new ArrayList<>();
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    Spinner shareWithCoach;
    ImageView backButton;
    TextView title;
    TextInputLayout docTitleTIL;
    EditText docTitle;
    TextView showTitle;
    TextView browse;
    EditText comments;
    TextView submit;
    TextView addedList;
    ImageView thumbnail;

    private static final int FILE_UPLOAD = 101;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 2;
    public static final int MEDIA_TYPE_VIDEO = 3;

    private Uri fileUri = null;

    String strDocTitle = "", strDocComments = "";
    boolean isDocSelected = false;
    DocumentBean documentBean;

    boolean isEditMode = false;
    DocumentBean documentToEdit;

    // Run time permission
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_upload_documents_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        docTitleTIL = (TextInputLayout) findViewById(R.id.docTitleTIL);
        docTitle = (EditText) findViewById(R.id.docTitle);
        showTitle = (TextView) findViewById(R.id.showTitle);
        browse = (TextView) findViewById(R.id.browse);
        comments = (EditText) findViewById(R.id.comments);
        submit = (TextView) findViewById(R.id.submit);
        addedList = (TextView) findViewById(R.id.addedList);
        shareWithCoach = (Spinner) findViewById(R.id.shareWithCoach);
        shareWithCoach.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        thumbnail = findViewById(R.id.thumbnail);

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentUploadDocumentsScreen.this));

        changeFonts();

        Intent intent = getIntent();
        if (intent != null) {
            isEditMode = intent.getBooleanExtra("isEditMode", false);

            if (isEditMode) {
                documentToEdit = (DocumentBean) intent.getSerializableExtra("documentToEdit");

                docTitle.setText(documentToEdit.getTitle());
                comments.setText(documentToEdit.getComments());
                showTitle.setText(documentToEdit.getFileName());

                documentBean = null;
                selectedUri = null;
                fileUri = null;

                submit.setText("Update");
            }
        }

        shareWithCoach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //    shareWithCoachStr = categoriesID.get(position).toString();

                if(position == 0){
                    shareWithCoachStr = "";
                } else {
                    shareWithCoachStr = coachesList.get(position).getCoachId();
                }
//                 shareWithCoachStr = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strDocTitle = docTitle.getText().toString().trim();
                strDocComments = comments.getText().toString().trim();

                if (strDocTitle == null || strDocTitle.isEmpty()) {
                    Toast.makeText(ParentUploadDocumentsScreen.this, "Please enter Document Title", Toast.LENGTH_SHORT).show();
                } else if (strDocComments == null || strDocComments.isEmpty()) {
                    Toast.makeText(ParentUploadDocumentsScreen.this, "Please enter Comment", Toast.LENGTH_SHORT).show();
                } else if (isEditMode) {
                    new EditFileAsync(documentBean, ParentUploadDocumentsScreen.this).execute();
                } else if (shareWithCoachStr == null) {
                    Toast.makeText(ParentUploadDocumentsScreen.this, "Please select coach for share", Toast.LENGTH_SHORT).show();
                } else if (!isDocSelected) {
                    Toast.makeText(ParentUploadDocumentsScreen.this, "Please select the Document", Toast.LENGTH_SHORT).show();
                } else {
                    new UploadFileAsync(documentBean, ParentUploadDocumentsScreen.this).execute();
                }
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] items = new String[]{"Take from camera", "Select from gallery"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ParentUploadDocumentsScreen.this, android.R.layout.select_dialog_item, items);
                AlertDialog.Builder builder = new AlertDialog.Builder(ParentUploadDocumentsScreen.this);

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
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                            i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            startActivityForResult(i, CHOOSE_IMAGE);

                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//                try {
//                    startActivityForResult(
//                            Intent.createChooser(intent, "Select a File to Upload"),
//                            FILE_UPLOAD);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    // Potentially direct the user to the Market with a Dialog
//                    Toast.makeText(ParentUploadDocumentsScreen.this, "Please install a File Manager.",
//                            Toast.LENGTH_SHORT).show();
//                }

//                final String[] items = new String[]{"Take from camera", "Select from gallery"};
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(ParentUploadDocumentsScreen.this, android.R.layout.select_dialog_item, items);
//                AlertDialog.Builder builder = new AlertDialog.Builder(ParentUploadDocumentsScreen.this);
//
//                builder.setTitle("Choose");
//                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int item) {
//
//                        if (item == 0) {
//
//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//
//
//                        } else {
//                            Intent action = new Intent(Intent.ACTION_GET_CONTENT);
//                            action = action.setType("*/*").addCategory(Intent.CATEGORY_OPENABLE);
//                            startActivityForResult(Intent.createChooser(action, "Upload file from..."), FILE_UPLOAD);
//                        }
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
            }
        });

        addedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getCoachesListing();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissions()) {
//                Toast.makeText(ChildTrackWorkoutScreen.this, "Permissions already granted", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(ChildTrackWorkoutScreen.this, "Initially permission not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(ParentUploadDocumentsScreen.this, p);
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
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                } else {
                    // no permissions granted.
                    Toast.makeText(ParentUploadDocumentsScreen.this, "This feature cannot work without Permissions. \nTry Again and allow permissions or allow permissions in Applications Settings", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        selectedUri = data.getData();
                        String[] filePathColumn = {MediaStore.Video.Media.DATA};
                        Cursor cursor = ParentUploadDocumentsScreen.this.getContentResolver().query(selectedUri, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        selectedImagePath = cursor.getString(columnIndex);
                        cursor.close();
                        File file = new File(selectedImagePath);
                        String nameFile = file.getName();
                        isDocSelected = true;
                        showTitle.setText(nameFile);

                        // showing thumbnail
                        thumbnail.setImageURI(selectedUri);
                        thumbnail.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Toast.makeText(ParentUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    try {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;

//                        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                        isDocSelected = true;
                        showTitle.setText(fileUri.getPath());

                        // showing thumbnail
                        thumbnail.setImageURI(fileUri);
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

        }
    }

    private class UploadFileAsync extends AsyncTask<Void, Void, String> {

        DocumentBean documentBean;
        ProgressDialog pDialog;
        Context context;

        public UploadFileAsync(DocumentBean documentBean, Context context) {
            this.documentBean = documentBean;
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

            String uploadUrl = Utilities.BASE_URL + "account/upload_document";
            String strResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(uploadUrl);

            httpPost.addHeader("X-access-uid", loggedInUser.getId());
            httpPost.addHeader("X-access-token", loggedInUser.getToken());

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(context));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            try {

                String mime = null;
                File file = null;

                if (fileUri == null) {
                    mime = getMimeType(documentBean.getUri());
                    file = new File(documentBean.getFilePath());
                } else {
                    mime = getMimeType(fileUri);
                    file = new File(fileUri.getPath());
                }

                //System.out.println("Mime " + mime);

                StringBody userId = new StringBody(loggedInUser.getId());
                StringBody title = new StringBody(strDocTitle);
                StringBody comments = new StringBody(strDocComments);
                StringBody coachId = new StringBody(shareWithCoachStr);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

//                if(file != null) {
//                    FileBody fileBody = new FileBody(file, file.getName(), mime, "UTF-8");
//                    builder.addPart("file", fileBody);
//                }

                if (selectedUri != null) {
                    String imageMime = getMimeType(selectedUri);
                    File imageFile = new File(selectedImagePath);
                    //System.out.println("MIME_type__" + imageMime + "--Path__" + selectedImagePath + "--FileName__" + imageFile.getName());
                    if (imageFile != null) {
                        FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");
                        builder.addPart("file", imageFileBody);
                    }

                } else if (fileUri != null) {
                    String fileMime = getMimeType(fileUri);
                    file = new File(fileUri.getPath());
                    if (file != null) {
                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
                        builder.addPart("file", imageFileBody);
                    }
                }


                // builder.addPart("users_id", userId);
                builder.addPart("shared_with", coachId);
                builder.addPart("doc_title", title);
                builder.addPart("comments", comments);


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
                //System.out.println("Exception " + e.getMessage());
            }

            return strResponse;

        }

        @Override
        protected void onPostExecute(String response) {

            //System.out.println("Response " + response);

            if (response == null) {
                Toast.makeText(ParentUploadDocumentsScreen.this, "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    if (status) {
                        Toast.makeText(ParentUploadDocumentsScreen.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ParentUploadDocumentsScreen.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ParentUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }


    private class EditFileAsync extends AsyncTask<Void, Void, String> {

        DocumentBean documentBean;
        ProgressDialog pDialog;
        Context context;

        public EditFileAsync(DocumentBean documentBean, Context context) {
            this.documentBean = documentBean;
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

            String uploadUrl = Utilities.BASE_URL + "account/edit_document";
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

                /*if (documentBean != null) {
                    String mime = getMimeType(documentBean.getUri());
                    //System.out.println("Mime " + mime);

                    File file = new File(documentBean.getFilePath());
                    FileBody fileBody = new FileBody(file, file.getName(), mime, "UTF-8");

                    builder.addPart("file", fileBody);
                }*/

                if (selectedUri != null) {
                    String imageMime = getMimeType(selectedUri);
                    File imageFile = new File(selectedImagePath);
                    //System.out.println("MIME_type__" + imageMime + "--Path__" + selectedImagePath + "--FileName__" + imageFile.getName());
                    if (imageFile != null) {
                        FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");
                        builder.addPart("file", imageFileBody);
                    }

                } else if (fileUri != null) {
                    String fileMime = getMimeType(fileUri);
                    File file = new File(fileUri.getPath());

                    //System.out.println("MIME_type__" + fileMime + "--Path__" + fileUri + "--FileName__" + file.getName());

                    if (file != null) {
                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
                        builder.addPart("file", imageFileBody);
                    }
                }


                StringBody userId = new StringBody(loggedInUser.getId());
                StringBody title = new StringBody(strDocTitle);
                StringBody comments = new StringBody(strDocComments);
                StringBody documentId = new StringBody(documentToEdit.getDocumentId());
                StringBody coachId = new StringBody(shareWithCoachStr);

                builder.addPart("users_id", userId);
                builder.addPart("shared_with", coachId);
                builder.addPart("doc_title", title);
                builder.addPart("comments", comments);
                builder.addPart("document_id", documentId);

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
                //System.out.println("Exception " + e.getMessage());
            }

            return strResponse;

        }

        @Override
        protected void onPostExecute(String response) {

            //System.out.println("Response " + response);

            if (response == null) {
                Toast.makeText(ParentUploadDocumentsScreen.this, "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    Toast.makeText(ParentUploadDocumentsScreen.this, message, Toast.LENGTH_SHORT).show();

                    if (status) {

                        if(documentToEdit != null){
                            String documentUrl = Utilities.BASE_URL + "account/download_document/" + loggedInUser.getToken() + "/" + documentToEdit.getDocumentId();
                            MemoryCacheUtils.removeFromCache(documentUrl, imageLoader.getMemoryCache());
                            DiskCacheUtils.removeFromCache(documentUrl, imageLoader.getDiskCache());
                        }

                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ParentUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }


    private void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFileExtension(Uri uri) {
        String extension = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();

            MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(cr.getType(uri));
        } else {
            extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        }
        return extension;
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            //System.out.println("Exception in method " + e.getMessage());
            return "exception";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getFileName(Uri uri) {
        String scheme = uri.getScheme();
        String fileName = "";

        if (scheme.equals("file")) {
            fileName = uri.getLastPathSegment();
        } else if (scheme.equals("content")) {
            String[] proj = {MediaStore.Images.Media.TITLE};
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                cursor.moveToFirst();
                fileName = cursor.getString(columnIndex);
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return fileName;
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public String dumpImageMetaData(Uri uri) {

        Cursor cursor = ParentUploadDocumentsScreen.this.getContentResolver().query(uri, null, null, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {

                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                //System.out.println("Display Name :: " + displayName);

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
                //System.out.println("Size " + size);

                return displayName;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }

    private void changeFonts() {
        title.setTypeface(linoType);
        docTitleTIL.setTypeface(helvetica);
        docTitle.setTypeface(helvetica);
        showTitle.setTypeface(helvetica);
        browse.setTypeface(helvetica);
        comments.setTypeface(helvetica);
        submit.setTypeface(linoType);
        addedList.setTypeface(linoType);
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

    private void getCoachesListing() {
        if (Utilities.isNetworkAvailable(ParentUploadDocumentsScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "account/coaches_for_parents";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ParentUploadDocumentsScreen.this, GET_COACHES_LISTING, ParentUploadDocumentsScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentUploadDocumentsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_COACHES_LISTING:

                coachesList.clear();

                if (response == null) {
                    Toast.makeText(ParentUploadDocumentsScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            CoachBean coachBean = new CoachBean();
                            coachBean.setCoachId("-1");
                            coachBean.setFullName("Share with");

                            coachesList.add(coachBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");


                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject coachObject = dataArray.getJSONObject(i);
                                coachBean = new CoachBean();

                                coachBean.setCoachId(coachObject.getString("coach_id"));
                                coachBean.setFullName(coachObject.getString("full_name"));

                                coachesList.add(coachBean);
                            }


                        } else {
//                            Toast.makeText(ParentUploadDocumentsScreen.this, message, Toast.LENGTH_SHORT).show();

                            CoachBean coachBean = new CoachBean();
                            coachBean.setCoachId("-1");
                            coachBean.setFullName("Share with");

                            coachesList.add(coachBean);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentUploadDocumentsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                shareWithCoach.setAdapter(new SpinnerAdapter(ParentUploadDocumentsScreen.this, coachesList));

                if(isEditMode){

                    int existsAt = -1;

                    for(int i=0;i<coachesList.size();i++){
                        CoachBean currentCoach = coachesList.get(i);
                        if(currentCoach.getCoachId().equalsIgnoreCase(documentToEdit.getSharedWithIds())){
                            existsAt = i;
                            break;
                        }
                    }

                    if(existsAt != -1){
                        shareWithCoach.setSelection(existsAt);
                    }
                }

                break;
        }
    }

    public class SpinnerAdapter extends BaseAdapter {

        Context context;
        ArrayList<CoachBean> coachesList;
        LayoutInflater layoutInflater;

        public SpinnerAdapter(Context context, ArrayList<CoachBean> coachesList) {
            this.context = context;
            this.coachesList = coachesList;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return coachesList.size();
        }

        @Override
        public Object getItem(int position) {
            return coachesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.parent_adapter_coach_name_item, null);

            TextView coachName = (TextView) convertView.findViewById(R.id.coachName);

            coachName.setText(coachesList.get(position).getFullName());

//            coachName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                   //  coachId = coachesList.get(position).getCoachId();
//                }
//            });

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.parent_adapter_coach_name_dropdown_item, null);

            TextView coachName = (TextView) convertView.findViewById(R.id.coachName);

            coachName.setText(coachesList.get(position).getFullName());

            return convertView;
        }
    }

}