package com.dunvant.application.parent;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildBean;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class ParentAddChildScreen extends AppCompatActivity implements IWebServiceCallback {
    LinearLayout field4Linear;
    TextInputLayout f1TIL;
    EditText f1;
    TextInputLayout f2TIL;
    EditText f2;
    TextInputLayout f3TIL;
    EditText f3;
    TextInputLayout f5TIL;
    EditText f5;
    TextView field4;
    RadioButton r1;
    RadioButton r2;
    String r1Str;
    String r2Str;

    String gendername1, gendername2;
    LinearLayout genderLinear;
    String fNameIdStr, fNameLabelStr, fNameSlugStr, fNameIsShowStr, fNameIsReqStr, fNameFieldTypeStr, fNameInputTypeMultFieldStr;
    String lNameIdStr, lNameLabelStr, lNameSlugStr, lNameIsShowStr, lNameIsReqStr, lNameFieldTypeStr, lNameInputTypeMultFieldStr;
    String uNameIdStr,childIdStr, uNameLabelStr,childLabelStr, uNameSlugStr, childSlugStr, uNameIsShowStr, childIsShowStr, uNameIsReqStr, childIsReqStr, uNameFieldTypeStr, childFieldTypeStr, uNameInputTypeMultFieldStr, childInputTypeMultFieldStr;
    String classIdStr, classLabelStr, classSlugStr, classIsShowStr, classIsReqStr, classFieldTypeStr, classInputTypeMultFieldStr;
    String schoolIdStr, schoolLabelStr, schoolSlugStr, schoolIsShowStr, schoolIsReqStr, schoolFieldTypeStr, schoolInputTypeMultFieldStr;
    String passwordIdStr, passwordLabelStr, passwordSlugStr, passwordIsShowStr, passwordIsReqStr, passwordFieldTypeStr, passwordInputTypeMultFieldStr;
    String cpasswordIdStr, cpasswordLabelStr, cpasswordSlugStr, cpasswordIsShowStr, cpasswordIsReqStr, cpasswordFieldTypeStr, cpasswordInputTypeMultFieldStr;
    String genderIdStr, genderLabelStr, genderSlugStr, genderIsShowStr, genderIsReqStr, genderFieldTypeStr, genderInputTypeMultFieldStr;
    String medCondIdStr, medCondLabelStr, medCondSlugStr, medCondIsShowStr, medCondIsReqStr, medCondFieldTypeStr, medCondInputTypeMultFieldStr;
    String f1IdStr, f1LabelStr, f1SlugStr, f1IsShowStr, f1IsReqStr, f1FieldTypeStr, f1InputTypeMultFieldStr;
    String f2IdStr, f2LabelStr, f2SlugStr, f2IsShowStr, f2IsReqStr, f2FieldTypeStr, f2InputTypeMultFieldStr;
    String f3IdStr, f3LabelStr, f3SlugStr, f3IsShowStr, f3IsReqStr, f3FieldTypeStr, f3InputTypeMultFieldStr;
    String f4IdStr, f4LabelStr, f4SlugStr, f4IsShowStr, f4IsReqStr, f4FieldTypeStr, f4InputTypeMultFieldStr;
    String f5IdStr, f5LabelStr, f5SlugStr, f5IsShowStr, f5IsReqStr, f5FieldTypeStr, f5InputTypeMultFieldStr;
    String dobIdStr, dobLabelStr, dobSlugStr, dobIsShowStr, dobIsReqStr, dobFieldTypeStr, dobInputTypeMultFieldStr;
    String clubIdStr, clubLabelStr, clubSlugStr, clubIsShowStr, clubIsReqStr, clubFieldTypeStr, clubInputTypeMultFieldStr;
    String natIdStr, natLabelStr, natSlugStr, natIsShowStr, natIsReqStr, natFieldTypeStr, natInputTypeMultFieldStr;
    private final String GET_REG = "GET_REG";
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    ImageView backButton;
    TextView title;
    TextInputLayout firstNameTIL;
    EditText firstName;
    TextInputLayout lastNameTIL;
    EditText lastName;
    TextInputLayout emailTIL;
    EditText childEmail;
    TextInputLayout childEmailTIL;
    EditText email;
    TextInputLayout passwordTIL;
    EditText password;
    TextInputLayout retypeTIL;
    EditText retypePassword;
    static TextView dateOfBirth;
    TextView sex;
    RadioButton male;
    RadioButton female;
    TextInputLayout medicalTIL;
    EditText medicalCondition;
    TextInputLayout schoolTIL;
    EditText school;

    TextInputLayout favPlayerTIL;
    EditText favPlayer;
    TextInputLayout favTeamTIL;
    EditText favTeam;
    TextInputLayout favPositionTIL;
    EditText favPosition;
    TextInputLayout favFootballBootTIL;
    EditText favFootballBoot;
    TextInputLayout favFoodTIL;
    EditText favFood;
    TextInputLayout nationalityTIL;
    EditText nationality;
    TextInputLayout heightTIL;
    EditText height;
    TextInputLayout weightTIL;
    EditText weight;
    CheckBox isPrivate;
    TextView addChild;
    TextView listingOfChildren;
    TextView userNameLabel;
    ImageView favPlayerImage;
    ImageView favTeamImage;
    TextView childFieldLabel;

    String strFirstName;
    String strLastName;
    String strEmail;
    String childEmailStr;
    String strPassword;
    String strRetypePassword;
    String strGender;
    String strMedicalCondition;
    String strSchool;
    String strFavPlayer;
    String strFavTeam;
    String strFavPosition;
    String strFavFootballBoot;
    String strFavFood;
    String strNationality;
    String strHeight;
    String strWeight;
    String strIsPrivate;
    String strDateOfBirth;
    String strClass;
    String strFieldCLub;
    String strF1;
    String strF2;
    String strF3;
    String strF4;
    String strF5;

    private final String ADD_CHILD = "ADD_CHILD";
    private final String EDIT_CHILD = "EDIT_CHILD";

    boolean isEditMode = false;
    boolean comingFromBookAcademy = false;
    ChildBean childToEdit = null;

    private final int CHOOSE_IMAGE_1 = 1;
    private final int CHOOSE_IMAGE_2 = 2;


    public static final int MEDIA_TYPE_IMAGE = 3;
    public static final int MEDIA_TYPE_VIDEO = 4;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE2 = 200;

    private Uri fileUri1 = null;
    private Uri fileUri2 = null;

    Uri selectedUri1;
    String selectedImagePath1;
    Uri selectedUri2;
    String selectedImagePath2;

    String defaultChecked = "";
    EditText className;
    TextInputLayout classNameTIL;
    EditText fieldClub;
    TextInputLayout fieldClubTIL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_add_child_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

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

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        firstNameTIL = (TextInputLayout) findViewById(R.id.firstNameTIL);
        firstName = (EditText) findViewById(R.id.firstName);
        lastNameTIL = (TextInputLayout) findViewById(R.id.lastNameTIL);
        lastName = (EditText) findViewById(R.id.lastName);
        emailTIL = (TextInputLayout) findViewById(R.id.emailTIL);
        email = (EditText) findViewById(R.id.email);
        passwordTIL = (TextInputLayout) findViewById(R.id.passwordTIL);
        password = (EditText) findViewById(R.id.password);
        retypeTIL = (TextInputLayout) findViewById(R.id.retypeTIL);
        retypePassword = (EditText) findViewById(R.id.retypePassword);
        dateOfBirth = (TextView) findViewById(R.id.dateOfBirth);
        sex = (TextView) findViewById(R.id.sex);
        genderLinear = findViewById(R.id.genderLinear);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        medicalTIL = (TextInputLayout) findViewById(R.id.medicalTIL);
        medicalCondition = (EditText) findViewById(R.id.medicalCondition);
        schoolTIL = (TextInputLayout) findViewById(R.id.schoolTIL);
        school = (EditText) findViewById(R.id.school);
        favPlayerTIL = (TextInputLayout) findViewById(R.id.favPlayerTIL);
        favPlayer = (EditText) findViewById(R.id.favPlayer);
        favTeamTIL = (TextInputLayout) findViewById(R.id.favTeamTIL);
        favTeam = (EditText) findViewById(R.id.favTeam);
        favPositionTIL = (TextInputLayout) findViewById(R.id.favPositionTIL);
        favPosition = (EditText) findViewById(R.id.favPosition);
        favFootballBootTIL = (TextInputLayout) findViewById(R.id.favFootballBootTIL);
        favFootballBoot = (EditText) findViewById(R.id.favFootballBoot) ;
        favFoodTIL = (TextInputLayout) findViewById(R.id.favFoodTIL);
        favFood = (EditText) findViewById(R.id.favFood);
        nationalityTIL = (TextInputLayout) findViewById(R.id.nationalityTIL);
        nationality = (EditText) findViewById(R.id.nationality);
        heightTIL = (TextInputLayout) findViewById(R.id.heightTIL);
        height = (EditText) findViewById(R.id.height);
        weightTIL = (TextInputLayout) findViewById(R.id.weightTIL);
        weight = (EditText) findViewById(R.id.weight);
        isPrivate = (CheckBox) findViewById(R.id.isPrivate);
        addChild = (TextView) findViewById(R.id.addChild);
        listingOfChildren = (TextView) findViewById(R.id.listingOfChildren);
        userNameLabel = (TextView) findViewById(R.id.userNameLabel);
        favTeamImage = (ImageView) findViewById(R.id.favTeamImage);
        favPlayerImage = (ImageView) findViewById(R.id.favPlayerImage);
        childFieldLabel = (TextView) findViewById(R.id.childFieldLabel);
        className = findViewById(R.id.className);
        fieldClub = findViewById(R.id.fieldClub);
        classNameTIL = findViewById(R.id.classNameTIL);
        fieldClubTIL = findViewById(R.id.fieldClubTIL);
        childEmail = findViewById(R.id.childEmail);
        childEmailTIL = findViewById(R.id.childEmailTIL);

        f1 = findViewById(R.id.f1);
        f1TIL = findViewById(R.id.f1TIL);
        f2 = findViewById(R.id.f2);
        f2TIL = findViewById(R.id.f2TIL);
        f3 = findViewById(R.id.f3);
        f3TIL = findViewById(R.id.f3TIL);
        f5 = findViewById(R.id.f5);
        f5TIL = findViewById(R.id.f5TIL);
        field4 = findViewById(R.id.field4);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);

        field4Linear = findViewById(R.id.field4Linear);


        changeFonts();
        getRegForm();

        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

        title.setText("ADD "+verbiage_singular.toUpperCase());
//        userNameLabel.setText("This is your "+verbiage_singular.toLowerCase()+"'s user name and password to allow them to use the funky parts of the app."+" "+verbiage_singular.toLowerCase()+"'s ");
        userNameLabel.setText("This is your child's user name and password to allow them to use the funky parts of the app."+" "+verbiage_singular.toLowerCase()+"'s ");
//        childFieldLabel.setText("The following information can be added now or later by you or your "+verbiage_singular.toLowerCase());
        childFieldLabel.setText("The following information can be added now or later by you or your child");
        Intent intent = getIntent();
        if(intent != null) {
            isEditMode = intent.getBooleanExtra("isEditMode", false);
            comingFromBookAcademy = intent.getBooleanExtra("comingFromBookAcademy", false);

            defaultChecked = intent.getStringExtra("defaultChecked");
            if(defaultChecked != null){
                if(defaultChecked.equalsIgnoreCase("1")){
                    isPrivate.setChecked(true);
                } else {
                    isPrivate.setChecked(false);
                }
            }

            if(isEditMode) {
                childToEdit = (ChildBean) intent.getSerializableExtra("currentChild");
               // title.setText("EDIT PLAYER");

                title.setText("EDIT "+verbiage_singular.toUpperCase());

                addChild.setText("UPDATE");

                firstName.setText(childToEdit.getFirstName());
                lastName.setText(childToEdit.getLastName());
                email.setText(childToEdit.getUsername());
                childEmail.setText(childToEdit.getChildEmail());
                password.setText("");
                retypePassword.setText("");
                dateOfBirth.setText(childToEdit.getDateOfBirth());
                className.setText(childToEdit.getChildClass());
                fieldClub.setText(childToEdit.getFieldClub());

                f1.setText(childToEdit.getField1());
                f2.setText(childToEdit.getField2());
                f3.setText(childToEdit.getField3());
                f5.setText(childToEdit.getField5());

               if(childToEdit.getField4().equalsIgnoreCase("0")){
                   r1.setChecked(true);
               }else if(childToEdit.getField4().equalsIgnoreCase("1")){
                   r2.setChecked(true);
               }else{
                  // r2.setChecked(true);
               }

                switch (childToEdit.getGender()) {
                    case "Male":
                    case "male":

                        male.setChecked(true);

                        break;

                    case "Female":
                    case "female":

                        female.setChecked(true);

                        break;
                }

                medicalCondition.setText(childToEdit.getMedicalCondition());
                school.setText(childToEdit.getSchool());
                favPlayer.setText(childToEdit.getFavPlayer());
                favTeam.setText(childToEdit.getFavTeam());
                favPosition.setText(childToEdit.getFavPosition());
                favFootballBoot.setText(childToEdit.getFavFootballBoot());
                favFood.setText(childToEdit.getFavFood());
                nationality.setText(childToEdit.getNationality());
                height.setText(childToEdit.getHeight());
                weight.setText(childToEdit.getWeight());
                imageLoader.displayImage(childToEdit.getFavPlayerPicture(), favPlayerImage, options);
                imageLoader.displayImage(childToEdit.getFavTeamPicture(), favTeamImage, options);

                switch (childToEdit.getIsPrivate()) {
                    case "0":
                        isPrivate.setChecked(false);
                        break;
                    case "1":
                        isPrivate.setChecked(true);
                        break;
                }

            } else {
                //title.setText("ADD PLAYER");
                title.setText("ADD "+verbiage_singular.toUpperCase());

                addChild.setText("SUBMIT");
            }

        }

        isPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(!checked){
                    final Dialog dialog = new Dialog(ParentAddChildScreen.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.parent_dialog_error);

                    TextView messageTextView = (TextView) dialog.findViewById(R.id.message);
                    TextView okayButton = (TextView) dialog.findViewById(R.id.okayButton);
                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                    messageTextView.setText("If you set this "+verbiage_singular.toLowerCase()+" profile public then other "+verbiage_singular.toLowerCase()+" can also view his/her profile.");

                    okayButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            }
        });

        favPlayerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String[] items = new String[]{"Take from camera", "Select from gallery"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ParentAddChildScreen.this, android.R.layout.select_dialog_item, items);
                AlertDialog.Builder builder = new AlertDialog.Builder(ParentAddChildScreen.this);

                builder.setTitle("Choose");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == 0) {

                            // Code to get image
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            fileUri1 = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);
                            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);


                        } else {
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, CHOOSE_IMAGE_1);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        favTeamImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] items = new String[]{"Take from camera", "Select from gallery"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ParentAddChildScreen.this, android.R.layout.select_dialog_item, items);
                AlertDialog.Builder builder = new AlertDialog.Builder(ParentAddChildScreen.this);

                builder.setTitle("Choose");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == 0) {

                            // Code to get image
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            fileUri2 = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri2);
                            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE2);

                        } else {
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, CHOOSE_IMAGE_2);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });
//        addChild.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                strFirstName = firstName.getText().toString().trim();
//                strLastName = lastName.getText().toString().trim();
//                strEmail = email.getText().toString().trim();
//                strPassword = password.getText().toString().trim();
//                strRetypePassword = retypePassword.getText().toString().trim();
//                strDateOfBirth = dateOfBirth.getText().toString().trim();
//                strClass = className.getText().toString().trim();
//                strFieldCLub = fieldClub.getText().toString().trim();
//
//                if (male.isChecked()) {
//                    strGender = "0";
//                } else if (female.isChecked()) {
//                    strGender = "1";
//                }
//
//                if(isPrivate.isChecked()) {
//                    strIsPrivate = "1";
//                } else {
//                    strIsPrivate = "0";
//                }
//
//                strMedicalCondition = medicalCondition.getText().toString().trim();
//                strSchool = school.getText().toString().trim();
//                strFavPlayer = favPlayer.getText().toString().trim();
//                strFavTeam = favTeam.getText().toString().trim();
//                strFavPosition = favPosition.getText().toString().trim();
//                strFavFootballBoot = favFootballBoot.getText().toString().trim();
//                strFavFood = favFood.getText().toString().trim();
//                strNationality = nationality.getText().toString().trim();
//                strHeight = height.getText().toString().trim();
//                strWeight = weight.getText().toString().trim();
//
//                if(strFirstName == null || strFirstName.isEmpty()) {
//                    Toast.makeText(ParentAddChildScreen.this, "Please enter First Name", Toast.LENGTH_SHORT).show();
//                } else if (strLastName == null || strLastName.isEmpty()) {
//                    Toast.makeText(ParentAddChildScreen.this, "Please enter Last Name", Toast.LENGTH_SHORT).show();
//                } else if (strEmail == null || strEmail.isEmpty()) {
//                    Toast.makeText(ParentAddChildScreen.this, "Please enter Username", Toast.LENGTH_SHORT).show();
//                } else if (strDateOfBirth == null || strDateOfBirth.isEmpty() || strDateOfBirth.equalsIgnoreCase("Date of Birth")) {
//                    Toast.makeText(ParentAddChildScreen.this, "Please select Date of Birth", Toast.LENGTH_SHORT).show();
//                }else if (strClass == null || strClass.isEmpty()) {
//                    Toast.makeText(ParentAddChildScreen.this, "Please enter Class Name", Toast.LENGTH_SHORT).show();
//                }
////                else if (strFieldCLub == null || strFieldCLub.isEmpty()) {
////                    Toast.makeText(ParentAddChildScreen.this, "Please enter Field Club", Toast.LENGTH_SHORT).show();
////                }
////                else if(strDateOfBirth){
////
////                }
//                else if (strGender == null || strGender.isEmpty()) {
//                    Toast.makeText(ParentAddChildScreen.this, "Please choose Gender", Toast.LENGTH_SHORT).show();
//                } else if (strMedicalCondition == null || strMedicalCondition.isEmpty()) {
//                    Toast.makeText(ParentAddChildScreen.this, "Please enter Medical Condition", Toast.LENGTH_SHORT).show();
//                }
////                else if (strSchool == null || strSchool.isEmpty()) {
////                    Toast.makeText(ParentAddChildScreen.this, "Please enter School", Toast.LENGTH_SHORT).show();
////                } else if (strHeight == null || strHeight.isEmpty()) {
////                    Toast.makeText(ParentAddChildScreen.this, "Please enter Height", Toast.LENGTH_SHORT).show();
////                } else if (strWeight == null || strWeight.isEmpty()) {
////                    Toast.makeText(ParentAddChildScreen.this, "Please enter Weight", Toast.LENGTH_SHORT).show();
////                }
//                else {
//
//                    if(isEditMode) {
//
//                        if (!strPassword.isEmpty() || !strRetypePassword.isEmpty()) {
//                            if (!strPassword.equals(strRetypePassword)) {
//                                Toast.makeText(ParentAddChildScreen.this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        }
//
//                        if(Utilities.isNetworkAvailable(ParentAddChildScreen.this)) {
//
//                            new EditAsync(ParentAddChildScreen.this).execute();
//
//
////                            List<NameValuePair> nameValuePairList = new ArrayList<>();
////                            nameValuePairList.add(new BasicNameValuePair("fname", strFirstName));
////                            nameValuePairList.add(new BasicNameValuePair("lname", strLastName));
////                            nameValuePairList.add(new BasicNameValuePair("email", strEmail));
////                            nameValuePairList.add(new BasicNameValuePair("password", strPassword));
////                            nameValuePairList.add(new BasicNameValuePair("cpassword", strRetypePassword));
////                            nameValuePairList.add(new BasicNameValuePair("dob", strDateOfBirth));
////                            nameValuePairList.add(new BasicNameValuePair("medical_conditions", strMedicalCondition));
////                            nameValuePairList.add(new BasicNameValuePair("school", strSchool));
////                            nameValuePairList.add(new BasicNameValuePair("gender", strGender));
////                            nameValuePairList.add(new BasicNameValuePair("private_profile", strIsPrivate));
////                            nameValuePairList.add(new BasicNameValuePair("child_id", childToEdit.getId()));
////
////                            nameValuePairList.add(new BasicNameValuePair("favourite_player", strFavPlayer));
////                            nameValuePairList.add(new BasicNameValuePair("favourite_team", strFavTeam));
////                            nameValuePairList.add(new BasicNameValuePair("favourite_position", strFavPosition));
////                            nameValuePairList.add(new BasicNameValuePair("favourite_football_boot", strFavFootballBoot));
////                            nameValuePairList.add(new BasicNameValuePair("favourite_food", strFavFood));
////                            nameValuePairList.add(new BasicNameValuePair("nationality", strNationality));
////                            nameValuePairList.add(new BasicNameValuePair("height", strHeight));
////                            nameValuePairList.add(new BasicNameValuePair("weight", strWeight));
////
////                            String webServiceUrl = Utilities.BASE_URL + "children/edit";
////
////                            ArrayList<String> headers = new ArrayList<>();
////                            headers.add("X-access-uid:"+loggedInUser.getId());
////                            headers.add("X-access-token:"+loggedInUser.getToken());
////
////                            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentAddChildScreen.this, nameValuePairList, EDIT_CHILD, ParentAddChildScreen.this, headers);
////                            postWebServiceAsync.execute(webServiceUrl);
//
//                        } else {
//                            Toast.makeText(ParentAddChildScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//                        }
//
//                    } else {
//                        if (strPassword == null || strPassword.isEmpty()) {
//                            Toast.makeText(ParentAddChildScreen.this, "Please enter Password", Toast.LENGTH_SHORT).show();
//                        } else if (strRetypePassword == null || strRetypePassword.isEmpty()) {
//                            Toast.makeText(ParentAddChildScreen.this, "Please enter Confirm Password", Toast.LENGTH_SHORT).show();
//                        } else if (!strPassword.equals(strRetypePassword)) {
//                            Toast.makeText(ParentAddChildScreen.this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
//                        } else {
//                            if(Utilities.isNetworkAvailable(ParentAddChildScreen.this)) {
//
//                                new AddAsync(ParentAddChildScreen.this).execute();
////
////                                List<NameValuePair> nameValuePairList = new ArrayList<>();
////                                nameValuePairList.add(new BasicNameValuePair("fname", strFirstName));
////                                nameValuePairList.add(new BasicNameValuePair("lname", strLastName));
////                                nameValuePairList.add(new BasicNameValuePair("email", strEmail));
////                                nameValuePairList.add(new BasicNameValuePair("password", strPassword));
////                                nameValuePairList.add(new BasicNameValuePair("cpassword", strRetypePassword));
////                                nameValuePairList.add(new BasicNameValuePair("fu_role_id", "child_role"));
////                                nameValuePairList.add(new BasicNameValuePair("dob", strDateOfBirth));
////                                nameValuePairList.add(new BasicNameValuePair("medical_conditions", strMedicalCondition));
////                                nameValuePairList.add(new BasicNameValuePair("school", strSchool));
////                                nameValuePairList.add(new BasicNameValuePair("gender", strGender));
////                                nameValuePairList.add(new BasicNameValuePair("private_profile", strIsPrivate));
////
////
////                                nameValuePairList.add(new BasicNameValuePair("favourite_player", strFavPlayer));
////                                nameValuePairList.add(new BasicNameValuePair("favourite_team", strFavTeam));
////                                nameValuePairList.add(new BasicNameValuePair("favourite_position", strFavPosition));
////                                nameValuePairList.add(new BasicNameValuePair("favourite_football_boot", strFavFootballBoot));
////                                nameValuePairList.add(new BasicNameValuePair("favourite_food", strFavFood));
////                                nameValuePairList.add(new BasicNameValuePair("nationality", strNationality));
////                                nameValuePairList.add(new BasicNameValuePair("height", strHeight));
////                                nameValuePairList.add(new BasicNameValuePair("weight", strWeight));
////
////
////
////                                String webServiceUrl = Utilities.BASE_URL + "children/add";
////
////                                ArrayList<String> headers = new ArrayList<>();
////                                headers.add("X-access-uid:"+loggedInUser.getId());
////                                headers.add("X-access-token:"+loggedInUser.getToken());
////
////                                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentAddChildScreen.this, nameValuePairList, ADD_CHILD, ParentAddChildScreen.this, headers);
////                                postWebServiceAsync.execute(webServiceUrl);
//
//                            } else {
//                                Toast.makeText(ParentAddChildScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                }
//            }
//        });

        addChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strFirstName = firstName.getText().toString().trim();
                strLastName = lastName.getText().toString().trim();
                strEmail = email.getText().toString().trim();
                childEmailStr = childEmail.getText().toString().trim();
                strPassword = password.getText().toString().trim();
                strRetypePassword = retypePassword.getText().toString().trim();
                strDateOfBirth = dateOfBirth.getText().toString().trim();
                strClass = className.getText().toString().trim();
                strFieldCLub = fieldClub.getText().toString().trim();
                strMedicalCondition = medicalCondition.getText().toString().trim();
                strSchool = school.getText().toString().trim();
                strNationality = nationality.getText().toString().trim();
                strF1 = f1.getText().toString().trim();
                strF2 = f2.getText().toString().trim();
                strF3 = f3.getText().toString().trim();
                strF5 = f5.getText().toString().trim();

                strFavPlayer = favPlayer.getText().toString().trim();
                strFavTeam = favTeam.getText().toString().trim();
                strFavPosition = favPosition.getText().toString().trim();
                strFavFootballBoot = favFootballBoot.getText().toString().trim();
                strFavFood = favFood.getText().toString().trim();
                strHeight = height.getText().toString().trim();
                strWeight = weight.getText().toString().trim();


                if(fNameIsShowStr.equalsIgnoreCase("1")){
                    if(fNameIsReqStr.equalsIgnoreCase("1") && (strFirstName == null || strFirstName.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+fNameLabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(lNameIsShowStr.equalsIgnoreCase("1")){
                    if(lNameIsReqStr.equalsIgnoreCase("1") && (strLastName == null || strLastName.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+lNameLabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(uNameIsShowStr.equalsIgnoreCase("1")){
                    if(uNameIsReqStr.equalsIgnoreCase("1") && (strEmail == null || strEmail.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+uNameLabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(childIsShowStr.equalsIgnoreCase("1")){
                    if(childIsReqStr.equalsIgnoreCase("1") && (childEmailStr == null || childEmailStr.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+childLabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Pattern pattern = Pattern.compile(Utilities.EMAIL_PATTERN);
                    Matcher matcher = pattern.matcher(childEmailStr);

                    if (!matcher.matches()) {
                        Toast.makeText(ParentAddChildScreen.this, "Please enter a valid "+childLabelStr, Toast.LENGTH_SHORT).show();
                        return;
                      }

                }

//                if(passwordIsShowStr.equalsIgnoreCase("1")){
//                    if(passwordIsReqStr.equalsIgnoreCase("1") && (strPassword == null || strPassword.isEmpty())){
//                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+passwordLabelStr, Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//
//                if(cpasswordIsShowStr.equalsIgnoreCase("1")){
//                    if(cpasswordIsReqStr.equalsIgnoreCase("1") && (strRetypePassword == null || strRetypePassword.isEmpty())){
//                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+cpasswordLabelStr, Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//
//                if (!strPassword.equals(strRetypePassword)) {
//                    Toast.makeText(ParentAddChildScreen.this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if(classIsShowStr.equalsIgnoreCase("1")){
                    if(classIsReqStr.equalsIgnoreCase("1") && (strClass == null || strClass.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+classLabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(clubIsShowStr.equalsIgnoreCase("1")){
                    if(clubIsReqStr.equalsIgnoreCase("1") && (strFieldCLub == null || strFieldCLub.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+clubLabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                System.out.println("strDateOfBirth::"+strDateOfBirth);

                if(dobIsShowStr.equalsIgnoreCase("1")){
                    if(dobIsReqStr.equalsIgnoreCase("1") && (strDateOfBirth == null || strDateOfBirth.isEmpty() ||  strDateOfBirth.equalsIgnoreCase(dobLabelStr))){
                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+dobLabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(genderIsShowStr.equalsIgnoreCase("1")){
                    if (male.isChecked()) {
                        strGender = "0";
                    } else if (female.isChecked()) {
                        strGender = "1";
                    }
                    if(genderIsReqStr.equalsIgnoreCase("1") && (strGender == null || strGender.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please select "+genderLabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        if (male.isChecked()) {
                            strGender = "0";
                        } else if (female.isChecked()) {
                            strGender = "1";
                        }else{
                            strGender = "";
                        }
                    }
                }


                if(medCondIsShowStr.equalsIgnoreCase("1")){
                    if(medCondIsReqStr.equalsIgnoreCase("1") && (strMedicalCondition == null || strMedicalCondition.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+medCondLabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(schoolIsShowStr.equalsIgnoreCase("1")){
                    if(schoolIsReqStr.equalsIgnoreCase("1") && (strSchool == null || strSchool.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+schoolLabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(natIsShowStr.equalsIgnoreCase("1")){
                    if(natIsReqStr.equalsIgnoreCase("1") && (strNationality == null || strNationality.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+natLabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(f1IsShowStr.equalsIgnoreCase("1")){
                    if(f1IsReqStr.equalsIgnoreCase("1") && (strF1 == null || strF1.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+f1LabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(f2IsShowStr.equalsIgnoreCase("1")){
                    if(f2IsReqStr.equalsIgnoreCase("1") && (strF2 == null || strF2.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+f2LabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(f3IsShowStr.equalsIgnoreCase("1")){
                    if(f3IsReqStr.equalsIgnoreCase("1") && (strF3 == null || strF3.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+f3LabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                if(f4IsShowStr.equalsIgnoreCase("1")){
                    if (r1.isChecked()) {
                        strF4 = "0";
                    } else if (r2.isChecked()) {
                        strF4 = "1";
                    }
                    if(f4IsReqStr.equalsIgnoreCase("1") && (strF4 == null || strF4.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please select "+f4LabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        if (r1.isChecked()) {
                            strF4 = "0";
                        } else if (r2.isChecked()) {
                            strF4 = "1";
                        }else{
                            strF4 = "";
                        }

                    }
                }

                if(f5IsShowStr.equalsIgnoreCase("1")){
                    if(f5IsReqStr.equalsIgnoreCase("1") && (strF5 == null || strF5.isEmpty())){
                        Toast.makeText(ParentAddChildScreen.this, "Please enter "+f5LabelStr, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }



                if(isPrivate.isChecked()) {
                    strIsPrivate = "1";
                } else {
                    strIsPrivate = "0";
                }











//                if(strFirstName == null || strFirstName.isEmpty()) {
//                    Toast.makeText(ParentAddChildScreen.this, "Please enter First Name", Toast.LENGTH_SHORT).show();
//                } else if (strLastName == null || strLastName.isEmpty()) {
//                    Toast.makeText(ParentAddChildScreen.this, "Please enter Last Name", Toast.LENGTH_SHORT).show();
//                } else if (strEmail == null || strEmail.isEmpty()) {
//                    Toast.makeText(ParentAddChildScreen.this, "Please enter Username", Toast.LENGTH_SHORT).show();
//                } else if (strDateOfBirth == null || strDateOfBirth.isEmpty() || strDateOfBirth.equalsIgnoreCase("Date of Birth")) {
//                    Toast.makeText(ParentAddChildScreen.this, "Please select Date of Birth", Toast.LENGTH_SHORT).show();
//                }else if (strClass == null || strClass.isEmpty()) {
//                    Toast.makeText(ParentAddChildScreen.this, "Please enter Class Name", Toast.LENGTH_SHORT).show();
//                }
////                else if (strFieldCLub == null || strFieldCLub.isEmpty()) {
////                    Toast.makeText(ParentAddChildScreen.this, "Please enter Field Club", Toast.LENGTH_SHORT).show();
////                }
////                else if(strDateOfBirth){
////
////                }
//                else if (strGender == null || strGender.isEmpty()) {
//                    Toast.makeText(ParentAddChildScreen.this, "Please choose Gender", Toast.LENGTH_SHORT).show();
//                } else if (strMedicalCondition == null || strMedicalCondition.isEmpty()) {
//                    Toast.makeText(ParentAddChildScreen.this, "Please enter Medical Condition", Toast.LENGTH_SHORT).show();
//                }
////                else if (strSchool == null || strSchool.isEmpty()) {
////                    Toast.makeText(ParentAddChildScreen.this, "Please enter School", Toast.LENGTH_SHORT).show();
////                } else if (strHeight == null || strHeight.isEmpty()) {
////                    Toast.makeText(ParentAddChildScreen.this, "Please enter Height", Toast.LENGTH_SHORT).show();
////                } else if (strWeight == null || strWeight.isEmpty()) {
////                    Toast.makeText(ParentAddChildScreen.this, "Please enter Weight", Toast.LENGTH_SHORT).show();
////                }
//                else {
//
                    if(isEditMode) {

                        if (!strPassword.isEmpty() || !strRetypePassword.isEmpty()) {
                            if (!strPassword.equals(strRetypePassword)) {
                                Toast.makeText(ParentAddChildScreen.this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if(male.isChecked()){
                            strGender = "0";
                        }

                        if(female.isChecked()){
                            strGender = "1";
                        }

                        if(r1.isChecked()){
                            strF4 = "0";
                        }

                        if(r2.isChecked()){
                            strF4 = "1";
                        }

                        if(Utilities.isNetworkAvailable(ParentAddChildScreen.this)) {

                            new EditAsync(ParentAddChildScreen.this).execute();

                        } else {
                            Toast.makeText(ParentAddChildScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if(passwordIsShowStr.equalsIgnoreCase("1")){
                            if(passwordIsReqStr.equalsIgnoreCase("1") && (strPassword == null || strPassword.isEmpty())){
                                Toast.makeText(ParentAddChildScreen.this, "Please enter "+passwordLabelStr, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if(cpasswordIsShowStr.equalsIgnoreCase("1")){
                            if(cpasswordIsReqStr.equalsIgnoreCase("1") && (strRetypePassword == null || strRetypePassword.isEmpty())){
                                Toast.makeText(ParentAddChildScreen.this, "Please enter "+cpasswordLabelStr, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if (!strPassword.equals(strRetypePassword)) {
                            Toast.makeText(ParentAddChildScreen.this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
                            return;
                        }

//                        if (strPassword == null || strPassword.isEmpty()) {
//                            Toast.makeText(ParentAddChildScreen.this, "Please enter Password", Toast.LENGTH_SHORT).show();
//                        } else if (strRetypePassword == null || strRetypePassword.isEmpty()) {
//                            Toast.makeText(ParentAddChildScreen.this, "Please enter Confirm Password", Toast.LENGTH_SHORT).show();
//                        } else if (!strPassword.equals(strRetypePassword)) {
//                            Toast.makeText(ParentAddChildScreen.this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
//                        } else {
                            if(Utilities.isNetworkAvailable(ParentAddChildScreen.this)) {

                                new AddAsync(ParentAddChildScreen.this).execute();
                            } else {
                                Toast.makeText(ParentAddChildScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                            }
                   //     }
                    }
//                }
            }
        });

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        listingOfChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_REG:

                System.out.println("RES::"+response);

                if (response == null) {
                    Toast.makeText(ParentAddChildScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray jsonArray = new JSONArray(responseObject.getString("data"));
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if(jsonObject.getString("slug").equalsIgnoreCase("fname")){
                                    fNameIdStr = jsonObject.getString("id");
                                    fNameLabelStr = jsonObject.getString("label_name");
                                    fNameSlugStr = jsonObject.getString("slug");
                                    fNameIsShowStr = jsonObject.getString("is_show");
                                    fNameIsReqStr = jsonObject.getString("is_required");
                                    fNameFieldTypeStr = jsonObject.getString("field_type");
                                    fNameInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");


                                    if(fNameIsShowStr.equalsIgnoreCase("1")){
                                        firstNameTIL.setHint(fNameLabelStr);
                                        firstNameTIL.setVisibility(View.VISIBLE);
                                    }



                                }else if(jsonObject.getString("slug").equalsIgnoreCase("lname")){
                                    lNameIdStr = jsonObject.getString("id");
                                    lNameLabelStr = jsonObject.getString("label_name");
                                    lNameSlugStr = jsonObject.getString("slug");
                                    lNameIsShowStr = jsonObject.getString("is_show");
                                    lNameIsReqStr = jsonObject.getString("is_required");
                                    lNameFieldTypeStr = jsonObject.getString("field_type");
                                    lNameInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    if(lNameIsShowStr.equalsIgnoreCase("1")){
                                        lastNameTIL.setHint(lNameLabelStr);
                                        lastNameTIL.setVisibility(View.VISIBLE);
                                    }



                                }else if(jsonObject.getString("slug").equalsIgnoreCase("email")){
                                    uNameIdStr = jsonObject.getString("id");
                                    uNameLabelStr = jsonObject.getString("label_name");
                                    uNameSlugStr = jsonObject.getString("slug");
                                    uNameIsShowStr = jsonObject.getString("is_show");
                                    uNameIsReqStr = jsonObject.getString("is_required");
                                    uNameFieldTypeStr = jsonObject.getString("field_type");
                                    uNameInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    if(uNameIsShowStr.equalsIgnoreCase("1")){
                                        emailTIL.setHint(uNameLabelStr);
                                        emailTIL.setVisibility(View.VISIBLE);
                                        userNameLabel.setVisibility(View.VISIBLE);
                                    }



                                }else if(jsonObject.getString("slug").equalsIgnoreCase("email_address")){
                                    childIdStr = jsonObject.getString("id");
                                    childLabelStr = jsonObject.getString("label_name");
                                    childSlugStr = jsonObject.getString("slug");
                                    childIsShowStr = jsonObject.getString("is_show");
                                    childIsReqStr = jsonObject.getString("is_required");
                                    childFieldTypeStr = jsonObject.getString("field_type");
                                    childInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    if(childIsShowStr.equalsIgnoreCase("1")){
                                        childEmailTIL.setHint(childLabelStr);
                                        childEmailTIL.setVisibility(View.VISIBLE);

                                    }



                                }else if(jsonObject.getString("slug").equalsIgnoreCase("classname")){
                                    classIdStr = jsonObject.getString("id");
                                    classLabelStr = jsonObject.getString("label_name");
                                    classSlugStr = jsonObject.getString("slug");
                                    classIsShowStr = jsonObject.getString("is_show");
                                    classIsReqStr = jsonObject.getString("is_required");
                                    classFieldTypeStr = jsonObject.getString("field_type");
                                    classInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");


                                    if(classIsShowStr.equalsIgnoreCase("1")){
                                        classNameTIL.setHint(classLabelStr);
                                        classNameTIL.setVisibility(View.VISIBLE);
                                    }


                                }else if(jsonObject.getString("slug").equalsIgnoreCase("school")){
                                    schoolIdStr = jsonObject.getString("id");
                                    schoolLabelStr = jsonObject.getString("label_name");
                                    schoolSlugStr = jsonObject.getString("slug");
                                    schoolIsShowStr = jsonObject.getString("is_show");
                                    schoolIsReqStr = jsonObject.getString("is_required");
                                    schoolFieldTypeStr = jsonObject.getString("field_type");
                                    schoolInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    if(schoolIsShowStr.equalsIgnoreCase("1")){
                                        schoolTIL.setHint(schoolLabelStr);
                                        schoolTIL.setVisibility(View.VISIBLE);
                                    }



                                }else if(jsonObject.getString("slug").equalsIgnoreCase("password")){
                                    passwordIdStr = jsonObject.getString("id");
                                    passwordLabelStr = jsonObject.getString("label_name");
                                    passwordSlugStr = jsonObject.getString("slug");
                                    passwordIsShowStr = jsonObject.getString("is_show");
                                    passwordIsReqStr = jsonObject.getString("is_required");
                                    passwordFieldTypeStr = jsonObject.getString("field_type");
                                    passwordInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    if(passwordIsShowStr.equalsIgnoreCase("1")){
                                        passwordTIL.setHint(passwordLabelStr);
                                        passwordTIL.setVisibility(View.VISIBLE);
                                    }



                                }else if(jsonObject.getString("slug").equalsIgnoreCase("cpassword")){
                                    cpasswordIdStr = jsonObject.getString("id");
                                    cpasswordLabelStr = jsonObject.getString("label_name");
                                    cpasswordSlugStr = jsonObject.getString("slug");
                                    cpasswordIsShowStr = jsonObject.getString("is_show");
                                    cpasswordIsReqStr = jsonObject.getString("is_required");
                                    cpasswordFieldTypeStr = jsonObject.getString("field_type");
                                    cpasswordInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    if(cpasswordIsShowStr.equalsIgnoreCase("1")){
                                        retypeTIL.setHint(cpasswordLabelStr);
                                        retypeTIL.setVisibility(View.VISIBLE);

                                    }


                                }else if(jsonObject.getString("slug").equalsIgnoreCase("gender")){
                                    genderIdStr = jsonObject.getString("id");
                                    genderLabelStr = jsonObject.getString("label_name");
                                    genderSlugStr = jsonObject.getString("slug");
                                    genderIsShowStr = jsonObject.getString("is_show");
                                    genderIsReqStr = jsonObject.getString("is_required");
                                    genderFieldTypeStr = jsonObject.getString("field_type");
                                    genderInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");


                                    if(genderIsShowStr.equalsIgnoreCase("1")){
                                        String[] namesList = genderInputTypeMultFieldStr.split(",");
                                        gendername1 = namesList [0];
                                        gendername2 = namesList [1];

                                        sex.setText(genderLabelStr);
                                        genderLinear.setVisibility(View.VISIBLE);
                                        male.setText(gendername1);
                                        female.setText(gendername2);
                                    }



                                }else if(jsonObject.getString("slug").equalsIgnoreCase("medical_conditions")){
                                    medCondIdStr = jsonObject.getString("id");
                                    medCondLabelStr = jsonObject.getString("label_name");
                                    medCondSlugStr = jsonObject.getString("slug");
                                    medCondIsShowStr = jsonObject.getString("is_show");
                                    medCondIsReqStr = jsonObject.getString("is_required");
                                    medCondFieldTypeStr = jsonObject.getString("field_type");
                                    medCondInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    if(medCondIsShowStr.equalsIgnoreCase("1")){
                                        medicalTIL.setHint(medCondLabelStr);
                                        medicalTIL.setVisibility(View.VISIBLE);
                                    }



                                }else if(jsonObject.getString("slug").equalsIgnoreCase("field_1")){
                                    f1IdStr = jsonObject.getString("id");
                                    f1LabelStr = jsonObject.getString("label_name");
                                    f1SlugStr = jsonObject.getString("slug");
                                    f1IsShowStr = jsonObject.getString("is_show");
                                    f1IsReqStr = jsonObject.getString("is_required");
                                    f1FieldTypeStr = jsonObject.getString("field_type");
                                    f1InputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    if(f1IsShowStr.equalsIgnoreCase("1")){
                                        f1TIL.setHint(f1LabelStr);
                                        f1TIL.setVisibility(View.VISIBLE);
                                    }


                                }else if(jsonObject.getString("slug").equalsIgnoreCase("field_2")){
                                    f2IdStr = jsonObject.getString("id");
                                    f2LabelStr = jsonObject.getString("label_name");
                                    f2SlugStr = jsonObject.getString("slug");
                                    f2IsShowStr = jsonObject.getString("is_show");
                                    f2IsReqStr = jsonObject.getString("is_required");
                                    f2FieldTypeStr = jsonObject.getString("field_type");
                                    f2InputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    if(f2IsShowStr.equalsIgnoreCase("1")){
                                        f2TIL.setHint(f2LabelStr);
                                        f2TIL.setVisibility(View.VISIBLE);
                                    }



                                }else if(jsonObject.getString("slug").equalsIgnoreCase("field_3")){
                                    f3IdStr = jsonObject.getString("id");
                                    f3LabelStr = jsonObject.getString("label_name");
                                    f3SlugStr = jsonObject.getString("slug");
                                    f3IsShowStr = jsonObject.getString("is_show");
                                    f3IsReqStr = jsonObject.getString("is_required");
                                    f3FieldTypeStr = jsonObject.getString("field_type");
                                    f3InputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    if(f3IsShowStr.equalsIgnoreCase("1")){
                                        f3TIL.setHint(f3LabelStr);
                                        f3TIL.setVisibility(View.VISIBLE);
                                    }



                                }else if(jsonObject.getString("slug").equalsIgnoreCase("field_4")){
                                    f4IdStr = jsonObject.getString("id");
                                    f4LabelStr = jsonObject.getString("label_name");
                                    f4SlugStr = jsonObject.getString("slug");
                                    f4IsShowStr = jsonObject.getString("is_show");
                                    f4IsReqStr = jsonObject.getString("is_required");
                                    f4FieldTypeStr = jsonObject.getString("field_type");
                                    f4InputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");


                                   try{
                                       if(f4IsShowStr.equalsIgnoreCase("1")){
                                           String[] namesList = f4InputTypeMultFieldStr.split(",");
                                           r1Str = namesList [0];
                                           r2Str = namesList [1];

                                           field4.setText(f4LabelStr);
                                           field4Linear.setVisibility(View.VISIBLE);
                                           System.out.println("HERE::"+r1Str);
                                           r1.setText(r1Str);
                                           r2.setText(r2Str);
                                       }
                                   }catch (Exception e){
                                       e.printStackTrace();
                                   }



                                }else if(jsonObject.getString("slug").equalsIgnoreCase("field_5")){
                                    f5IdStr = jsonObject.getString("id");
                                    f5LabelStr = jsonObject.getString("label_name");
                                    f5SlugStr = jsonObject.getString("slug");
                                    f5IsShowStr = jsonObject.getString("is_show");
                                    f5IsReqStr = jsonObject.getString("is_required");
                                    f5FieldTypeStr = jsonObject.getString("field_type");
                                    f5InputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    if(f5IsShowStr.equalsIgnoreCase("1")){
                                        f5TIL.setHint(f5LabelStr);
                                        f5TIL.setVisibility(View.VISIBLE);
                                    }



                                }else if(jsonObject.getString("slug").equalsIgnoreCase("dob")){
                                    dobIdStr = jsonObject.getString("id");
                                    dobLabelStr = jsonObject.getString("label_name");
                                    dobSlugStr = jsonObject.getString("slug");
                                    dobIsShowStr = jsonObject.getString("is_show");
                                    dobIsReqStr = jsonObject.getString("is_required");
                                    dobFieldTypeStr = jsonObject.getString("field_type");
                                    dobInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    if(dobIsShowStr.equalsIgnoreCase("1")){
                                        if(isEditMode){
                                            dateOfBirth.setText(childToEdit.getDateOfBirth());
                                            dateOfBirth.setVisibility(View.VISIBLE);
                                        }else{
                                            dateOfBirth.setText(dobLabelStr);
                                            dateOfBirth.setVisibility(View.VISIBLE);
                                        }

                                    }



                                }else if(jsonObject.getString("slug").equalsIgnoreCase("club")){
                                    clubIdStr = jsonObject.getString("id");
                                    clubLabelStr = jsonObject.getString("label_name");
                                    clubSlugStr = jsonObject.getString("slug");
                                    clubIsShowStr = jsonObject.getString("is_show");
                                    clubIsReqStr = jsonObject.getString("is_required");
                                    clubFieldTypeStr = jsonObject.getString("field_type");
                                    clubInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    if(clubIsShowStr.equalsIgnoreCase("1")){
                                        fieldClubTIL.setHint(clubLabelStr);
                                        fieldClubTIL.setVisibility(View.VISIBLE);
                                    }


                                }else if(jsonObject.getString("slug").equalsIgnoreCase("nationality")){
                                    natIdStr = jsonObject.getString("id");
                                    natLabelStr = jsonObject.getString("label_name");
                                    natSlugStr = jsonObject.getString("slug");
                                    natIsShowStr = jsonObject.getString("is_show");
                                    natIsReqStr = jsonObject.getString("is_required");
                                    natFieldTypeStr = jsonObject.getString("field_type");
                                    natInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    if(natIsShowStr.equalsIgnoreCase("1")){
                                        nationalityTIL.setHint(natLabelStr);
                                        nationalityTIL.setVisibility(View.VISIBLE);
                                    }



                                }



                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentAddChildScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case ADD_CHILD:

                if (response == null) {
                    Toast.makeText(ParentAddChildScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(ParentAddChildScreen.this, Html.fromHtml(message), Toast.LENGTH_SHORT).show();
                        if (status) {
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentAddChildScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case EDIT_CHILD:

                if (response == null) {
                    Toast.makeText(ParentAddChildScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(ParentAddChildScreen.this, message, Toast.LENGTH_SHORT).show();
                        if (status) {
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentAddChildScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());

            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            dateOfBirth.setText(day+"-"+(month+1)+"-"+year);
        }
    }

    private void changeFonts() {
        title.setTypeface(linoType);
        firstNameTIL.setTypeface(helvetica);
        firstName.setTypeface(helvetica);
        lastNameTIL.setTypeface(helvetica);
        lastName.setTypeface(helvetica);
        emailTIL.setTypeface(helvetica);
        email.setTypeface(helvetica);
        childEmailTIL.setTypeface(helvetica);
        childEmail.setTypeface(helvetica);
        passwordTIL.setTypeface(helvetica);
        password.setTypeface(helvetica);
        retypeTIL.setTypeface(helvetica);
        retypePassword.setTypeface(helvetica);
        dateOfBirth.setTypeface(helvetica);
        sex.setTypeface(helvetica);
        male.setTypeface(helvetica);
        female.setTypeface(helvetica);
        medicalTIL.setTypeface(helvetica);
        medicalCondition.setTypeface(helvetica);
        schoolTIL.setTypeface(helvetica);
        school.setTypeface(helvetica);
        addChild.setTypeface(linoType);
        listingOfChildren.setTypeface(linoType);
        userNameLabel.setTypeface(helvetica);
        childFieldLabel.setTypeface(helvetica);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case CHOOSE_IMAGE_1:
                try {
                    selectedUri1 = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedUri1, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedImagePath1 = cursor.getString(columnIndex);
                    cursor.close();
                    favPlayerImage.setImageURI(selectedUri1);
                }catch (Exception e){
                    //  Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

                break;

            case CHOOSE_IMAGE_2:
                try {
                    selectedUri2 = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedUri2, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedImagePath2 = cursor.getString(columnIndex);
                    cursor.close();
                    favTeamImage.setImageURI(selectedUri2);
                }catch (Exception e){
                    //  Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

                break;

            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    try {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;

                        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri1.getPath(), options);
                        favPlayerImage.setImageBitmap(bitmap);
//                        showTitle.setText(fileUri.getPath());
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

            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE2:
                if (resultCode == RESULT_OK) {

                    try {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;

                        final Bitmap bitmap = BitmapFactory.decodeFile(fileUri2.getPath(), options);
                        favTeamImage.setImageBitmap(bitmap);
//                        showTitle.setText(fileUri.getPath());
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

    private class EditAsync extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog;
        Context context;

        public EditAsync(Context context){
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

            String webServiceUrl = Utilities.BASE_URL + "children/edit";
            String strResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(webServiceUrl);

            httpPost.addHeader("X-access-uid", loggedInUser.getId());
            httpPost.addHeader("X-access-token", loggedInUser.getToken());

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(context));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            try {

                StringBody fNameBody = new StringBody(strFirstName);
                StringBody lNameBody = new StringBody(strLastName);
                StringBody emailBody = new StringBody(strEmail);
                StringBody childEmailBody = new StringBody(childEmailStr);
                StringBody classBody = new StringBody(strClass);
                StringBody clubBody = new StringBody(strFieldCLub);
                StringBody passwordBody = new StringBody(strPassword);
                StringBody cpasswordBody = new StringBody(strRetypePassword);
                StringBody dobBody = new StringBody(strDateOfBirth);
                StringBody medicalBody = new StringBody(strMedicalCondition);
                StringBody schoolBody = new StringBody(strSchool);
                StringBody genderBody = new StringBody(strGender);
                StringBody isPrivateBody = new StringBody(strIsPrivate);
                StringBody childIdBody = new StringBody(childToEdit.getId());
                StringBody favPlayerBody = new StringBody(strFavPlayer);
                StringBody favTeamBody = new StringBody(strFavTeam);
                StringBody favPositionBody = new StringBody(strFavPosition);
                StringBody favFootballBootBody = new StringBody(strFavFootballBoot);
                StringBody favFoodBody = new StringBody(strFavFood);
                StringBody nationalityBody = new StringBody(strNationality);
                StringBody heightBody = new StringBody(strHeight);
                StringBody weightBody = new StringBody(strWeight);

                StringBody f1Body = new StringBody(strF1);
                StringBody f2Body = new StringBody(strF2);
                StringBody f3Body = new StringBody(strF3);

                if(f4IsShowStr.equalsIgnoreCase("0")){
                    strF4 = "";
                }

                StringBody f4Body = new StringBody(strF4);
                StringBody f5Body = new StringBody(strF5);



                MultipartEntityBuilder builder = MultipartEntityBuilder.create();

                builder.addPart("fname", fNameBody);
                builder.addPart("lname", lNameBody);
                builder.addPart("email", emailBody);
                builder.addPart("email_address", childEmailBody);
                builder.addPart("classname", classBody);
                builder.addPart("club", clubBody);
                builder.addPart("password", passwordBody);
                builder.addPart("cpassword", cpasswordBody);
                builder.addPart("dob", dobBody);
                builder.addPart("medical_conditions", medicalBody);
                builder.addPart("school", schoolBody);
                System.out.println("genderBody:: "+genderBody+" ::genderStr:: "+strGender);
                builder.addPart("gender", genderBody);
                builder.addPart("private_profile", isPrivateBody);
                builder.addPart("child_id", childIdBody);


                builder.addPart("favourite_player", favPlayerBody);
                builder.addPart("favourite_team", favTeamBody);
                builder.addPart("favourite_position", favPositionBody);
                builder.addPart("favourite_football_boot", favFootballBootBody);
                builder.addPart("favourite_food", favFoodBody);
                builder.addPart("nationality", nationalityBody);
                builder.addPart("height", heightBody);
                builder.addPart("weight", weightBody);

                builder.addPart("field_1", f1Body);
                builder.addPart("field_2", f2Body);
                builder.addPart("field_3", f3Body);
                builder.addPart("field_4", f4Body);
                builder.addPart("field_5", f5Body);

//                System.out.println("fNameBody"+fNameBody+"-lname-"+lNameBody+"-email-"+emailBody+"-password-"+passwordBody
//                +"-cpassword-"+cpasswordBody+"-dob-"+dobBody+"-medical_conditions-"+medicalBody+"-school-"+schoolBody+"-gender-"+genderBody+"-private_profile-"+isPrivateBody+
//                "-child_id-"+childIdBody+"-favourite_player-"+favPlayerBody+"-favourite_team-"+favTeamBody+"-favourite_position-"+favPositionBody+"-favourite_team-"+favTeamBody
//                +"-favourite_position-"+favPositionBody+"-favourite_football_boot-"+favFootballBootBody+"-favourite_food-"+favFoodBody+"-nationality-"+nationalityBody+
//                "-height-"+heightBody+"-weight-"+weightBody);

                if(selectedUri1 != null) {
                    String imageMime = getMimeType(selectedUri1);
                    File imageFile = new File(selectedImagePath1);
                    //System.out.println("MIME_type_1_"+imageMime+"--Path__"+selectedImagePath1+"--FileName__"+imageFile.getName());
                    FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");

                    builder.addPart("favourite_player_picture", imageFileBody);
                } else if (fileUri1 != null) {

                    String fileMime = getMimeType(fileUri1);

                    File file = new File(fileUri1.getPath());
                    if(file != null){
                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
                        builder.addPart("favourite_player_picture", imageFileBody);
                    }
                }

                if(selectedUri2 != null) {
                    String imageMime = getMimeType(selectedUri2);
                    File imageFile = new File(selectedImagePath2);
                    System.out.println("MIME_type_2_"+imageMime+"--Path__"+selectedImagePath2+"--FileName__"+imageFile.getName());
                    FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");

                    builder.addPart("favourite_team_picture", imageFileBody);
                } else if (fileUri2 != null) {

                    String fileMime = getMimeType(fileUri2);

                    File file = new File(fileUri2.getPath());
                    if(file != null){
                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
                        builder.addPart("favourite_team_picture", imageFileBody);
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

                System.out.println("File upload end");

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception "+e.getMessage());
            }

            return strResponse;

        }

        @Override
        protected void onPostExecute(String response) {

            System.out.println("Response "+response);

            if(response == null) {
                Toast.makeText(ParentAddChildScreen.this, "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                   if(status){
                       finish();
                   }else{
                       Toast.makeText(ParentAddChildScreen.this, message, Toast.LENGTH_SHORT).show();
                   }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ParentAddChildScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if(pDialog != null && pDialog.isShowing()){
                pDialog.dismiss();
            }
        }
    }

    private class AddAsync extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog;
        Context context;

        public AddAsync(Context context){
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

            String webServiceUrl = Utilities.BASE_URL + "children/add";
            String strResponse = null;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(webServiceUrl);

            httpPost.addHeader("X-access-uid", loggedInUser.getId());
            httpPost.addHeader("X-access-token", loggedInUser.getToken());

            httpPost.addHeader("x-access-did", Utilities.getDeviceId(context));
            httpPost.addHeader("x-access-dtype", Utilities.DEVICE_TYPE);

            try {

                StringBody fNameBody = new StringBody(strFirstName);
                StringBody lNameBody = new StringBody(strLastName);
                StringBody emailBody = new StringBody(strEmail);
                StringBody childEmailBody = new StringBody(childEmailStr);
                StringBody classBody = new StringBody(strClass);
                StringBody clubBody = new StringBody(strFieldCLub);
                StringBody passwordBody = new StringBody(strPassword);
                StringBody cpasswordBody = new StringBody(strRetypePassword);
                StringBody fuRoleBody = new StringBody("child_role");
                StringBody dobBody = new StringBody(strDateOfBirth);
                StringBody medicalBody = new StringBody(strMedicalCondition);
                StringBody schoolBody = new StringBody(strSchool);
                System.out.println("strGender::"+strGender);
                StringBody genderBody = new StringBody(strGender);
                StringBody isPrivateBody = new StringBody(strIsPrivate);

                StringBody favPlayerBody = new StringBody(strFavPlayer);
                StringBody favTeamBody = new StringBody(strFavTeam);
                StringBody favPositionBody = new StringBody(strFavPosition);
                StringBody favFootballBootBody = new StringBody(strFavFootballBoot);
                StringBody favFoodBody = new StringBody(strFavFood);
                StringBody nationalityBody = new StringBody(strNationality);
                StringBody heightBody = new StringBody(strHeight);
                StringBody weightBody = new StringBody(strWeight);

                StringBody f1Body = new StringBody(strF1);
                StringBody f2Body = new StringBody(strF2);
                StringBody f3Body = new StringBody(strF3);

                if(f4IsShowStr.equalsIgnoreCase("0")){
                    strF4 = "";
                }

                StringBody f4Body = new StringBody(strF4);
                StringBody f5Body = new StringBody(strF5);



                MultipartEntityBuilder builder = MultipartEntityBuilder.create();

                builder.addPart("fname", fNameBody);
                builder.addPart("lname", lNameBody);
                builder.addPart("email", emailBody);
                builder.addPart("email_address", childEmailBody);
                builder.addPart("classname", classBody);
                builder.addPart("club", clubBody);
                builder.addPart("password", passwordBody);
                builder.addPart("cpassword", cpasswordBody);
                builder.addPart("dob", dobBody);
                builder.addPart("medical_conditions", medicalBody);
                builder.addPart("school", schoolBody);
                builder.addPart("gender", genderBody);
                builder.addPart("private_profile", isPrivateBody);
                builder.addPart("fu_role_id", fuRoleBody);


                builder.addPart("favourite_player", favPlayerBody);
                builder.addPart("favourite_team", favTeamBody);
                builder.addPart("favourite_position", favPositionBody);
                builder.addPart("favourite_football_boot", favFootballBootBody);
                builder.addPart("favourite_food", favFoodBody);
                builder.addPart("nationality", nationalityBody);
                builder.addPart("height", heightBody);
                builder.addPart("weight", weightBody);

                builder.addPart("field_1", f1Body);
                builder.addPart("field_2", f2Body);
                builder.addPart("field_3", f3Body);
                builder.addPart("field_4", f4Body);
                builder.addPart("field_5", f5Body);

//                System.out.println("fNameBody"+fNameBody+"-lname-"+lNameBody+"-email-"+emailBody+"-password-"+passwordBody
//                        +"-cpassword-"+cpasswordBody+"-dob-"+dobBody+"-medical_conditions-"+medicalBody+"-school-"+schoolBody+"-gender-"+genderBody+"-private_profile-"+isPrivateBody+
//                        "-fu_role_id-"+fuRoleBody+"-favourite_player-"+favPlayerBody+"-favourite_team-"+favTeamBody+"-favourite_position-"+favPositionBody+"-favourite_team-"+favTeamBody
//                        +"-favourite_position-"+favPositionBody+"-favourite_football_boot-"+favFootballBootBody+"-favourite_food-"+favFoodBody+"-nationality-"+nationalityBody+
//                        "-height-"+heightBody+"-weight-"+weightBody+"--f1Body--"+f1Body+"--f2Body--"+f2Body+"--f3Body--"+f3Body+"--f4Body--"+f4Body+"--f5Body--"+f5Body);

                if(selectedUri1 != null) {
                    String imageMime = getMimeType(selectedUri1);
                    File imageFile = new File(selectedImagePath1);
                    //System.out.println("MIME_type_1_"+imageMime+"--Path__"+selectedImagePath1+"--FileName__"+imageFile.getName());
                    FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");

                    builder.addPart("favourite_player_picture", imageFileBody);
                } else if (fileUri1 != null) {

                    String fileMime = getMimeType(fileUri1);

                    File file = new File(fileUri1.getPath());
                    if(file != null){
                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
                        builder.addPart("favourite_player_picture", imageFileBody);
                    }
                }

                if(selectedUri2 != null) {
                    String imageMime = getMimeType(selectedUri2);
                    File imageFile = new File(selectedImagePath2);
                    System.out.println("MIME_type_2_"+imageMime+"--Path__"+selectedImagePath2+"--FileName__"+imageFile.getName());
                    FileBody imageFileBody = new FileBody(imageFile, imageFile.getName(), imageMime, "UTF-8");

                    builder.addPart("favourite_team_picture", imageFileBody);
                } else if (fileUri2 != null) {

                    String fileMime = getMimeType(fileUri2);

                    File file = new File(fileUri2.getPath());
                    if(file != null){
                        FileBody imageFileBody = new FileBody(file, file.getName(), fileMime, "UTF-8");
                        builder.addPart("favourite_team_picture", imageFileBody);
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
                System.out.println("Exception "+e.getMessage());
            }

            return strResponse;

        }

        @Override
        protected void onPostExecute(String response) {

            System.out.println("Response "+response);

            if(response == null) {
                Toast.makeText(ParentAddChildScreen.this, "Could not connect server", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    boolean status = responseObject.getBoolean("status");
                    String message = responseObject.getString("message");

                    if(status){
                        finish();
                        if(comingFromBookAcademy){
                            ParentBookAcademyFour.shouldRefreshChildrenListing = true;
                        }
                    }else{
                        Toast.makeText(ParentAddChildScreen.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ParentAddChildScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            ContentResolver cr = ParentAddChildScreen.this.getContentResolver();
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

    File getOutputMediaFile(int type) {

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


    private void getRegForm() {
        if (Utilities.isNetworkAvailable(ParentAddChildScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "children/get_child_reg_form";
            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ParentAddChildScreen.this, GET_REG, ParentAddChildScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentAddChildScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

}