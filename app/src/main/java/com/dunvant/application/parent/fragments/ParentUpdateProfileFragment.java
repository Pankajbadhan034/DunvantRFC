package com.dunvant.application.parent.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.CountryBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.ParentMainScreen;
import com.dunvant.application.parent.adapters.ParentCountryCodeAdapter;
import com.dunvant.application.utils.CropOption;
import com.dunvant.application.utils.CropOptionAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceAsync;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.dunvant.application.parent.ParentCreatePostScreen.MEDIA_TYPE_IMAGE;
import static com.dunvant.application.parent.ParentCreatePostScreen.MEDIA_TYPE_VIDEO;

public class ParentUpdateProfileFragment extends Fragment implements IWebServiceCallback{
    private static final int CHOOSE_IMAGE_FROM_GALLERY = 1;
    private Uri fileUri = null;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    Uri selectedUri = null;
    String selectedImagePath;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView profilePhoto;
    EditText fullName;
    EditText userName;
    EditText email;
    TextView countryCodeOneTextView;
    EditText mobileNumber;
    TextView countryCodeTwoTextView;
    EditText secondMobileNumber;
    EditText fullNameEditable;
    EditText lNameEditable;

    Button changePassword;
    Button update;

    private final String UPDATE_PROFILE = "UPDATE_PROFILE";
    private final String CHANGE_PASSWORD = "CHANGE_PASSWORD";

    String strFullName, strLname, strEmail, strMobileNumber, strSecondMobileNumber;
    String strCountryCodeOne, strCountryCodeTwo;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private Uri mImageCaptureUri;
//    private Uri imageOutputUri;
    Bitmap myBitmap;
//    String encodedImage = null;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    private final String GET_COUNTRY_CODES = "GET_COUNTRY_CODES";
    ArrayList<CountryBean> countryList = new ArrayList<>();
    String defaultCountryCodeFromServer = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(1000))
                .build();

        helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

//        imageOutputUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "myImage_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_update_profile, container, false);

        profilePhoto = (ImageView) view.findViewById(R.id.profilePhoto);
        fullName = (EditText) view.findViewById(R.id.fullName);
        fullNameEditable = (EditText) view.findViewById(R.id.fullNameEditable);
        userName = (EditText) view.findViewById(R.id.userName);
        email = (EditText) view.findViewById(R.id.email);
        countryCodeOneTextView = (TextView) view.findViewById(R.id.countryCodeOneTextView);
        mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
        countryCodeTwoTextView = (TextView) view.findViewById(R.id.countryCodeTwoTextView);
        secondMobileNumber = (EditText) view.findViewById(R.id.secondMobileNumber);
        changePassword = (Button) view.findViewById(R.id.changePassword);
        update = (Button) view.findViewById(R.id.update);
        lNameEditable = view.findViewById(R.id.lNameEditable);

        fullName.setTypeface(helvetica);
        fullNameEditable.setTypeface(helvetica);
        lNameEditable.setTypeface(helvetica);
        email.setTypeface(helvetica);
        countryCodeOneTextView.setTypeface(helvetica);
        mobileNumber.setTypeface(helvetica);
        countryCodeTwoTextView.setTypeface(helvetica);
        secondMobileNumber.setTypeface(helvetica);
        changePassword.setTypeface(linoType);
        update.setTypeface(linoType);
        userName.setTypeface(helvetica);

        fullName.setText(loggedInUser.getFirstName()+" "+loggedInUser.getLastName());
        fullNameEditable.setText(loggedInUser.getFirstName());
        lNameEditable.setText(loggedInUser.getLastName());
        email.setText(loggedInUser.getEmail());
        mobileNumber.setText(loggedInUser.getMobileNumber().trim());
        secondMobileNumber.setText(loggedInUser.getSecondMobileNumber().trim());
        userName.setText(loggedInUser.getUsername());

        imageLoader.displayImage(loggedInUser.getProfilePicPath(), profilePhoto, options);

        getCountryCodes();

        countryCodeOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                ListView countriesListView = new ListView(getActivity());
                alertDialog.setView(countriesListView);
                alertDialog.setTitle("Select Country");
                countriesListView.setAdapter(new ParentCountryCodeAdapter(getActivity(), countryList));

                final AlertDialog dialog = alertDialog.create();

                countriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        CountryBean clickedOnCountry = countryList.get(i);
                        countryCodeOneTextView.setText(clickedOnCountry.getDialingCode());
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        countryCodeTwoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                ListView countriesListView = new ListView(getActivity());
                alertDialog.setView(countriesListView);
                alertDialog.setTitle("Select Country");
                countriesListView.setAdapter(new ParentCountryCodeAdapter(getActivity(), countryList));

                final AlertDialog dialog = alertDialog.create();

                countriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        CountryBean clickedOnCountry = countryList.get(i);
                        countryCodeTwoTextView.setText(clickedOnCountry.getDialingCode());
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new CaptureImage();


                final String[] items = new String[]{"Take from camera", "Select from gallery"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, items);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Choose");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == 0) {

                            final String[] items = new String[]{"Image"};
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, items);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            builder.setTitle("Choose");
                            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {

                                    if (item == 0) {
                                        // Code to get image
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

                                    }
                                }
                            });
                            AlertDialog innerDialog = builder.create();
                            innerDialog.show();


                        } else {
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, CHOOSE_IMAGE_FROM_GALLERY);
                            dialog.cancel();

                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                strFullName = fullName.getText().toString().trim();
                strFullName = fullNameEditable.getText().toString().trim();
                strEmail = email.getText().toString().trim();
                strLname = lNameEditable.getText().toString().trim();

                strCountryCodeOne = countryCodeOneTextView.getText().toString().trim();
                strMobileNumber = mobileNumber.getText().toString().trim();

                strCountryCodeTwo = countryCodeTwoTextView.getText().toString().trim();
                strSecondMobileNumber = secondMobileNumber.getText().toString().trim();

                Pattern pattern = Pattern.compile(Utilities.EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(strEmail);

                if(strFullName == null || strFullName.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter First Name", Toast.LENGTH_SHORT).show();
                } else if(strLname == null || strLname.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter Last Name", Toast.LENGTH_SHORT).show();
                } else if (strEmail == null || strEmail.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter Email", Toast.LENGTH_SHORT).show();
                } else if (!matcher.matches()) {
                    Toast.makeText(getActivity(), "Please enter a valid Email", Toast.LENGTH_SHORT).show();
                } else if (strMobileNumber == null || strMobileNumber.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter Mobile Number", Toast.LENGTH_SHORT).show();
                }

                /*else if (strMobileNumber.length() < 9) {
                    Toast.makeText(getActivity(), "Mobile number should be of 9 digits", Toast.LENGTH_SHORT).show();
                } else if (strSecondMobileNumber == null || strSecondMobileNumber.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter Second Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (!strSecondMobileNumber.isEmpty() && strSecondMobileNumber.length()<9) {
                    Toast.makeText(getActivity(), "Second mobile number should be of 9 digits", Toast.LENGTH_SHORT).show();
                }*/ else {
                    if(Utilities.isNetworkAvailable(getActivity())) {

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("fname", strFullName));
                        nameValuePairList.add(new BasicNameValuePair("lname", strLname));
                        nameValuePairList.add(new BasicNameValuePair("email", strEmail));

                        nameValuePairList.add(new BasicNameValuePair("ph_code", strCountryCodeOne));
                        nameValuePairList.add(new BasicNameValuePair("phone_1", strMobileNumber));

                        nameValuePairList.add(new BasicNameValuePair("ph_code2", strCountryCodeTwo));
                        nameValuePairList.add(new BasicNameValuePair("phone_2", strSecondMobileNumber));

                        nameValuePairList.add(new BasicNameValuePair("users_id", loggedInUser.getId()));

                        String webServiceUrl = Utilities.BASE_URL + "account/update_profile";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:"+loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, UPDATE_PROFILE, ParentUpdateProfileFragment.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent changePassword = new Intent(getActivity(), ParentChangePassword.class);
//                startActivity(changePassword);

                changePassword();

            }
        });

        return view;
    }

    private void getCountryCodes(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "account/phoneCode_list";

            GetWebServiceAsync getWebServiceWithHeadersAsync = new GetWebServiceAsync(getActivity(), GET_COUNTRY_CODES, ParentUpdateProfileFragment.this);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void changePassword(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "account/change_password_request_by_user";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), CHANGE_PASSWORD, ParentUpdateProfileFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case UPDATE_PROFILE:

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            System.out.println("ResponseHere::"+responseObject);
                            loggedInUser.setFullName(strFullName+" "+strLname);
                            loggedInUser.setFirstName(strFullName);
                            loggedInUser.setLastName(strLname);
                            loggedInUser.setEmail(strEmail);
                            loggedInUser.setMobileNumber(strMobileNumber);
                            loggedInUser.setSecondMobileNumber(strSecondMobileNumber);
                            loggedInUser.setUsername(strEmail);

                            loggedInUser.setPhoneCodeOne(countryCodeOneTextView.getText().toString().trim());
                            loggedInUser.setPhoneCodeTwo(countryCodeTwoTextView.getText().toString().trim());

                            Gson gson = new Gson();
                            String jsonParentUserBean = gson.toJson(loggedInUser);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("loggedInUser", jsonParentUserBean);
                            editor.commit();

                            ((ParentMainScreen)getActivity()).updateUserName();
                            ((ParentMainScreen)getActivity()).showDashboardScreen();
                        }

                        Toast.makeText(getActivity(), Html.fromHtml(message), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }

                break;

            case CHANGE_PASSWORD:

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case GET_COUNTRY_CODES:

                countryList.clear();

                if(response == null){
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status){
                            JSONArray dataArray = responseObject.getJSONArray("data");
                            CountryBean countryBean;
                            for(int i=0;i<dataArray.length();i++){
                                JSONObject countryObject = dataArray.getJSONObject(i);
                                countryBean = new CountryBean();

                                countryBean.setId(countryObject.getString("id"));
                                countryBean.setCountry(countryObject.getString("country"));
//                                countryBean.setCountryCode(countryObject.getString("country_code"));
                                countryBean.setDialingCode(countryObject.getString("dialing_code"));

                                countryList.add(countryBean);
                            }

                            String defaultCodeId = responseObject.getString("default_codeId");

                            for(CountryBean country : countryList){
                                if(country.getId().equalsIgnoreCase(defaultCodeId)){

                                    defaultCountryCodeFromServer = country.getDialingCode();

                                    break;
                                }
                            }

                            if(loggedInUser.getPhoneCodeOne() == null || loggedInUser.getPhoneCodeOne().isEmpty() || loggedInUser.getPhoneCodeOne().equalsIgnoreCase("null")){
                                countryCodeOneTextView.setText(defaultCountryCodeFromServer);
                            } else {
                                countryCodeOneTextView.setText(loggedInUser.getPhoneCodeOne());
                            }

                            if(loggedInUser.getPhoneCodeTwo() == null || loggedInUser.getPhoneCodeTwo().isEmpty() || loggedInUser.getPhoneCodeTwo().equalsIgnoreCase("null")){
                                countryCodeTwoTextView.setText(defaultCountryCodeFromServer);
                            } else {
                                countryCodeTwoTextView.setText(loggedInUser.getPhoneCodeTwo());
                            }

//                            parentCountryCodeAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    class CaptureImage {
        final String[] items = new String[] { "Take from camera", "Select from gallery" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        CaptureImage() {
            builder.setTitle("Select Image");
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {

                    if (item == 0) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                        try {
                            intent.putExtra("return-data", true);
                            startActivityForResult(intent, PICK_FROM_CAMERA);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {

                        openGallery();

                        /*Intent intent;

                        if (Build.VERSION.SDK_INT < 19) {
                            intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image*//*");
                            startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                        } else {
                            intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image*//*");
                            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                            startActivityForResult(Intent.createChooser(intent,"Complete action using"), PICK_FROM_FILE);
                        }*/
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void openGallery() {
        final ArrayList<CropOption> cropOptions = new ArrayList<>();

        Intent intent;

        if (Build.VERSION.SDK_INT < 19){
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        }


        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(getActivity(), "Can not find gallery app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    String name = (String) co.title;

                    if(name.contains("Gallery")) {
                        cropOptions.add(co);
                    }


                }

                CropOptionAdapter adapter = new CropOptionAdapter(getActivity(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose App");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(cropOptions.get(item).appIntent, PICK_FROM_FILE);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageCaptureUri != null) {
                            getActivity().getContentResolver().delete(mImageCaptureUri, null, null);
                            mImageCaptureUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.show();
            }
        }
    }

    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(getActivity(), "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            if (mImageCaptureUri != null) {
                intent.setData(mImageCaptureUri);
            }

            intent.putExtra("outputX", 500);
            intent.putExtra("outputY", 500);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", false);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    String name = (String) co.title;

                    if(name.contains("Gallery")) {
                        cropOptions.add(co);
                    }


                }

                CropOptionAdapter adapter = new CropOptionAdapter(getActivity(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageCaptureUri != null) {
                            getActivity().getContentResolver().delete(mImageCaptureUri, null, null);
                            mImageCaptureUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.show();
            }
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK)
            return;

        switch (requestCode) {

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


                    // showing thumbnail
                    profilePhoto.setImageURI(selectedUri);
                    new UpdateImageAsync(getActivity()).execute();

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

                        // showing thumbnail
                        profilePhoto.setImageURI(fileUri);

                        new UpdateImageAsync(getActivity()).execute();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
//                    Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
                } else {
                    // failed to capture image
                    Toast.makeText(getActivity(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
                }
                break;

//            case PICK_FROM_CAMERA:
//                doCrop();
//                break;
            case PICK_FROM_FILE:
                if (Build.VERSION.SDK_INT < 19) {
                    mImageCaptureUri = data.getData();

                } else {
                    mImageCaptureUri = data.getData();
                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(mImageCaptureUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        myBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                doCrop();
                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();

                if (extras != null) {
                    myBitmap = extras.getParcelable("data");
                }

                /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArrayImage = stream.toByteArray();
                encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);*/

                profilePhoto.setImageBitmap(myBitmap);

                new UploadProfilePicAsync(getActivity()).execute();

                break;
        }
    }

    private class UploadProfilePicAsync extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog;
        Context context;

        public UploadProfilePicAsync(Context context){
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

            String uploadUrl = Utilities.BASE_URL + "account/change_profile_picture";
            String strResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(uploadUrl);

            httpPost.addHeader("X-access-uid", loggedInUser.getId());
            httpPost.addHeader("X-access-token", loggedInUser.getToken());

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(context));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            try {

                String mime = "image/jpg";
                //System.out.println("Mime "+mime);

//                File file = new File(documentBean.getFilePath());

                File file = new File(context.getCacheDir(), "image_"+System.currentTimeMillis()+".jpg");
                file.createNewFile();

                //System.out.println("Cache Dir "+context.getCacheDir());

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();

                FileBody fileBody = new FileBody(file, file.getName(), mime, "UTF-8");

                //System.out.println("File Name "+file.getName());
                //System.out.println("File Path "+file.getPath());

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                builder.addPart("profile_picture", fileBody);

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
            } catch (NoClassDefFoundError e){
                e.printStackTrace();
            }

            return strResponse;

        }

        @Override
        protected void onPostExecute(String response) {

            //System.out.println("Response "+response);

            if(response == null) {
                Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    if(status) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        String imagePath = responseObject.getString("profile_picture_path");
                        loggedInUser.setProfilePicPath(imagePath);

                        imageLoader.displayImage(loggedInUser.getProfilePicPath(), profilePhoto, options);

                        Gson gson = new Gson();
                        String jsonParentUserBean = gson.toJson(loggedInUser);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("loggedInUser", jsonParentUserBean);
                        editor.commit();

                        ((ParentMainScreen) getActivity()).updateUser();

                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if(pDialog != null && pDialog.isShowing()){
                pDialog.dismiss();
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
        return mediaFile;
    }

    private class UpdateImageAsync extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog;
        Context context;

        public UpdateImageAsync(Context context) {
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

            String uploadUrl = Utilities.BASE_URL + "account/change_profile_picture";
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


                if (selectedUri != null) {
                    String imageMime = getMimeType(selectedUri);

                    File imageFile = new File(selectedImagePath);

                    //System.out.println("MIME_type__" + imageMime + "--Path__" + selectedImagePath + "--FileName__" + imageFile.getName());

                    if(imageFile != null){
                        FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");
                        builder.addPart("profile_picture", imageFileBody);
                    }

                } else if (fileUri != null) {

                    String fileMime = getMimeType(fileUri);

                    File file = new File(fileUri.getPath());
                    if(file != null){
                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
                        builder.addPart("profile_picture", imageFileBody);
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
                //System.out.println("Exception " + e.getMessage());
            }

            return strResponse;

        }

        @Override
        protected void onPostExecute(String response) {

            //System.out.println("Response " + response);

            if (response == null) {
                Toast.makeText(getActivity(), "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    if (status) {
//                        getActivity().finish();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        String imagePath = responseObject.getString("profile_picture_path");
                        loggedInUser.setProfilePicPath(imagePath);

                        imageLoader.displayImage(loggedInUser.getProfilePicPath(), profilePhoto, options);

                        Gson gson = new Gson();
                        String jsonParentUserBean = gson.toJson(loggedInUser);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("loggedInUser", jsonParentUserBean);
                        editor.commit();

                        ((ParentMainScreen) getActivity()).updateUser();
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
}