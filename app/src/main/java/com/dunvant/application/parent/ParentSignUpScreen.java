package com.dunvant.application.parent;
import com.google.firebase.iid.FirebaseInstanceId;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CountryBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentCountryCodeAdapter;
import com.dunvant.application.startModule.StartModuleDashboardScreen;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class ParentSignUpScreen extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView registerTextView;
    TextInputLayout fullNameTextInputLayout;
    TextInputLayout lastNameTextInputLayout;
    EditText fullName;
    EditText lName;
    TextInputLayout emailTextInputLayout;
    EditText email;
    TextInputLayout passwordTextInputLayout;
    EditText password;
    TextInputLayout retypePasswordTextInputLayout;
    EditText retypePassword;
    static TextView dob;
    TextInputLayout mobileTextInputLayout;
    EditText mobileNumber;
    TextInputLayout secondTextInputLayout;
    EditText secondMobileNumber;
    TextInputLayout referralCodeTextInputLayout;
    EditText referralCode;
    Button createAccount;
    TextView bySigningUp;
    TextView termsOfUse;
    TextView and;
    TextView privacyPolicy;
    TextView alreadyHave;
    TextView login;

        Spinner countryCodeOne;
    Spinner countryCodeTwo;
    TextView countryCodeOneTextView;
    TextView countryCodeTwoTextView;

    String strFullName, strLName, strEmail, strPassword;
    String strRetypePassword, strMobileNumber, strSecondMobileNumber;
    String strDob;
    String strReferralCode;
    String strCountryCodeOne, strCountryCodeTwo;

    private final String SIGN_UP_WEB_SERVICE = "SIGN_UP_WEB_SERVICE";
    private final String GET_COUNTRY_CODES = "GET_COUNTRY_CODES";

    ArrayList<CountryBean> countryList = new ArrayList<>();
    String defaultCountryCodeFromServer = "";

//    ParentCountryCodeAdapter parentCountryCodeAdapter;

        RadioButton r1;
    RadioButton r2;
    String valueRadio="1";
    RadioButton male;
    RadioButton female;
    String valueRadioGender="0";
    CheckBox newsletter;
    int newsletterInt = 0;
    LinearLayout genderLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_sign_up_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        registerTextView = (TextView) findViewById(R.id.registerTextView);
        fullNameTextInputLayout = (TextInputLayout) findViewById(R.id.fullNameTextInputLayout);
        lastNameTextInputLayout = findViewById(R.id.lastNameTextInputLayout);
        fullName = (EditText) findViewById(R.id.fullName);
        lName = findViewById(R.id.lName);
        emailTextInputLayout = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        email = (EditText) findViewById(R.id.email);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordTextInputLayout);
        password = (EditText) findViewById(R.id.password);
        retypePasswordTextInputLayout = (TextInputLayout) findViewById(R.id.retypePasswordTextInputLayout);
        retypePassword = (EditText) findViewById(R.id.retypePassword);
        dob = (TextView) findViewById(R.id.dob);
        mobileTextInputLayout = (TextInputLayout) findViewById(R.id.mobileTextInputLayout);
        mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        secondTextInputLayout = (TextInputLayout) findViewById(R.id.secondTextInputLayout);
        secondMobileNumber = (EditText) findViewById(R.id.secondMobileNumber);
        referralCodeTextInputLayout = (TextInputLayout) findViewById(R.id.referralCodeTextInputLayout);
        referralCode = (EditText) findViewById(R.id.referralCode);
        createAccount = (Button) findViewById(R.id.createAccount);
        bySigningUp = (TextView) findViewById(R.id.bySigningUp);
        termsOfUse = (TextView) findViewById(R.id.termsOfuse);
        and = (TextView) findViewById(R.id.and);
        privacyPolicy = (TextView) findViewById(R.id.privacyPolicy);
        alreadyHave = (TextView) findViewById(R.id.alreadyHave);
        login = (TextView) findViewById(R.id.login);
        newsletter = findViewById(R.id.newsletter);

        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        genderLinearLayout = findViewById(R.id.genderLinearLayout);

        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        r2.setText("Are you a Adult "+verbiage_singular+"?");

        newsletter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    newsletterInt = 1;
                }else{
                    newsletterInt = 0;
                }
            }
        });



//       if(r1.isChecked()){
//           valueRadio = "1";
//       }
//
//       if(r2.isChecked()){
//           valueRadio = "5";
//       }

       r1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               valueRadio = "1";
               r1.setChecked(true);
               r2.setChecked(false);
               genderLinearLayout.setVisibility(View.GONE);
               dob.setVisibility(View.GONE);
           }
       });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueRadio = "5";
                r1.setChecked(false);
                r2.setChecked(true);
                genderLinearLayout.setVisibility(View.VISIBLE);
                dob.setVisibility(View.VISIBLE);
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueRadioGender = "0";
                male.setChecked(true);
                female.setChecked(false);
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueRadioGender = "1";
                male.setChecked(false);
                female.setChecked(true);
            }
        });


//        countryCodeOne = findViewById(R.id.countryCodeOne);
//        countryCodeTwo = findViewById(R.id.countryCodeTwo);
        countryCodeOneTextView = findViewById(R.id.countryCodeOneTextView);
        countryCodeTwoTextView = findViewById(R.id.countryCodeTwoTextView);

//        parentCountryCodeAdapter = new ParentCountryCodeAdapter(ParentSignUpScreen.this, countryList);

//        countryCodeOne.setAdapter(parentCountryCodeAdapter);
//        countryCodeTwo.setAdapter(parentCountryCodeAdapter);

        countryCodeOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ParentSignUpScreen.this);
                ListView countriesListView = new ListView(ParentSignUpScreen.this);
                alertDialog.setView(countriesListView);
                alertDialog.setTitle("Select Country");
                countriesListView.setAdapter(new ParentCountryCodeAdapter(ParentSignUpScreen.this, countryList));

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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ParentSignUpScreen.this);
                ListView countriesListView = new ListView(ParentSignUpScreen.this);
                alertDialog.setView(countriesListView);
                alertDialog.setTitle("Select Country");
                countriesListView.setAdapter(new ParentCountryCodeAdapter(ParentSignUpScreen.this, countryList));

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

        changeFonts();

        getCountryCodes();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strFullName = fullName.getText().toString().trim();
                strLName = lName.getText().toString().trim();
                strEmail = email.getText().toString().trim();
                strPassword = password.getText().toString().trim();
                strRetypePassword = retypePassword.getText().toString().trim();
                strCountryCodeOne = countryCodeOneTextView.getText().toString().trim();
                strMobileNumber = mobileNumber.getText().toString().trim();
                strCountryCodeTwo = countryCodeTwoTextView.getText().toString().trim();
                strSecondMobileNumber = secondMobileNumber.getText().toString().trim();
                strDob = dob.getText().toString().trim();
                strReferralCode = referralCode.getText().toString().trim();

                String fcmToken = sharedPreferences.getString("fcmToken", "");

                Pattern pattern = Pattern.compile(Utilities.EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(strEmail);

                if(strFullName == null || strFullName.isEmpty()) {
                    Toast.makeText(ParentSignUpScreen.this, "Please enter First Name", Toast.LENGTH_SHORT).show();
                }else if(strLName == null || strLName.isEmpty()) {
                    Toast.makeText(ParentSignUpScreen.this, "Please enter Last Name", Toast.LENGTH_SHORT).show();
                } else if (strEmail == null || strEmail.isEmpty()) {
                    Toast.makeText(ParentSignUpScreen.this, "Please enter Email", Toast.LENGTH_SHORT).show();
                } else if (!matcher.matches()) {
                    Toast.makeText(ParentSignUpScreen.this, "Please enter a valid Email", Toast.LENGTH_SHORT).show();
                } else if (strPassword == null || strPassword.isEmpty()) {
                    Toast.makeText(ParentSignUpScreen.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                }
//                else if (strPassword.length() < 8) {
//                    Toast.makeText(ParentSignUpScreen.this, "Password should be minimum of 8 characters", Toast.LENGTH_SHORT).show();
//                }
                else if (strRetypePassword == null || strRetypePassword.isEmpty()) {
                    Toast.makeText(ParentSignUpScreen.this, "Please enter Confirm Password", Toast.LENGTH_SHORT).show();
                } else if (!strPassword.equals(strRetypePassword)) {
                    Toast.makeText(ParentSignUpScreen.this, "Password and Confirm Password do not match", Toast.LENGTH_LONG).show();
                }
//                else if (strDob == null || strDob.isEmpty() || strDob.equalsIgnoreCase("Date of Birth")) {
//                    Toast.makeText(ParentSignUpScreen.this, "Please select Date of Birth", Toast.LENGTH_SHORT).show();
//                }
                else if (strMobileNumber == null || strMobileNumber.isEmpty()) {
                    Toast.makeText(ParentSignUpScreen.this, "Please enter Mobile Number", Toast.LENGTH_SHORT).show();
                } /*else if (strMobileNumber.length() < 9) {
                    Toast.makeText(ParentSignUpScreen.this, "Mobile number should be of 9 digits", Toast.LENGTH_SHORT).show();
                }*/ /*else if (strSecondMobileNumber == null || strSecondMobileNumber.isEmpty()) {
                    Toast.makeText(ParentSignUpScreen.this, "Please enter Second Mobile Number", Toast.LENGTH_SHORT).show();
                } */ /*else if (!strSecondMobileNumber.isEmpty() && strSecondMobileNumber.length() < 9) {
                    Toast.makeText(ParentSignUpScreen.this, "Second Mobile number should be of 9 digits", Toast.LENGTH_SHORT).show();
                }*/ else {
                    if(dob.getVisibility()==View.VISIBLE){
                        if (strDob == null || strDob.isEmpty() || strDob.equalsIgnoreCase("Date of Birth")) {
                            Toast.makeText(ParentSignUpScreen.this, "Please select Date of Birth", Toast.LENGTH_SHORT).show();
                        }else{
                            registerAPI(fcmToken, strDob, valueRadioGender);
                        }
                    }else{
                        registerAPI(fcmToken, "", "");
                    }

                }
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        termsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://202.164.57.202/ifa_football/public/terms_and_conditions"));
                startActivity(browserIntent);*/

                Intent termsAndConditions = new Intent(ParentSignUpScreen.this, ParentViewTermsAndConditionsScreen.class);
                startActivity(termsAndConditions);

            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://202.164.57.202/ifa_football/public/terms_and_conditions"));
                startActivity(intent);*/

                Intent termsAndConditions = new Intent(ParentSignUpScreen.this, ParentViewTermsAndConditionsScreen.class);
                startActivity(termsAndConditions);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println("refreshedTokenHERE:: "+refreshedToken);
        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fcmToken", refreshedToken);
        editor.commit();
    }

    public void registerAPI(String fcmToken, String dob, String gender){
        if(Utilities.isNetworkAvailable(ParentSignUpScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("fname", strFullName));
            nameValuePairList.add(new BasicNameValuePair("lname", strLName));
            nameValuePairList.add(new BasicNameValuePair("email", strEmail));
            nameValuePairList.add(new BasicNameValuePair("password", strPassword));
            nameValuePairList.add(new BasicNameValuePair("cpassword", strRetypePassword));
            nameValuePairList.add(new BasicNameValuePair("user_dob", dob));

            nameValuePairList.add(new BasicNameValuePair("ph_code", strCountryCodeOne));
            nameValuePairList.add(new BasicNameValuePair("phone_1", strMobileNumber));

            nameValuePairList.add(new BasicNameValuePair("ph_code2", strCountryCodeTwo));
            nameValuePairList.add(new BasicNameValuePair("phone_2", strSecondMobileNumber));

            nameValuePairList.add(new BasicNameValuePair("referral_code", strReferralCode));
            nameValuePairList.add(new BasicNameValuePair("fu_role_id", "parent_role"));
            nameValuePairList.add(new BasicNameValuePair("fcm_device_token", fcmToken));
            nameValuePairList.add(new BasicNameValuePair("device_type", "1"));
            nameValuePairList.add(new BasicNameValuePair("user_type", valueRadio));
            nameValuePairList.add(new BasicNameValuePair("gender", gender));
            nameValuePairList.add(new BasicNameValuePair("newsletter", ""+newsletterInt));

            String webServiceUrl = Utilities.BASE_URL + "account/signup";

            PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(ParentSignUpScreen.this, nameValuePairList, SIGN_UP_WEB_SERVICE, ParentSignUpScreen.this);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentSignUpScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getCountryCodes(){
        if(Utilities.isNetworkAvailable(ParentSignUpScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "account/phoneCode_list";

            GetWebServiceAsync getWebServiceWithHeadersAsync = new GetWebServiceAsync(ParentSignUpScreen.this, GET_COUNTRY_CODES, ParentSignUpScreen.this);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentSignUpScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case SIGN_UP_WEB_SERVICE:

                if(response == null) {
                    Toast.makeText(ParentSignUpScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            String token = responseObject.getString("token");
                            JSONObject userData = responseObject.getJSONObject("user_data");

                            UserBean userBean = new UserBean();
                            userBean.setToken(token);
                            userBean.setId(userData.getString("id"));
                            userBean.setAcademiesId(userData.getString("academies_id"));
                            userBean.setUsername(userData.getString("username"));
                            userBean.setEmail(userData.getString("email"));
                            userBean.setGender(userData.getString("gender"));
//                            userBean.setCreatedAt(userData.getString("created_at"));
//                            userBean.setState(userData.getString("state"));
                            userBean.setFirstName(userData.getString("fname"));
                            userBean.setLastName(userData.getString("lname"));
                            userBean.setFullName(userData.getString("full_name"));
                            userBean.setMobileNumber(userData.getString("phone_1"));
                            userBean.setSecondMobileNumber(userData.getString("phone_2"));
//                            userBean.setTotalChildren(userData.getInt("total_children"));
                            userBean.setRoleCode(userData.getString("role_code"));
                            userBean.setAddress(userData.getString("address"));
                            userBean.setProfilePicPath(userData.getString("profile_picture_path"));

                            //added for child module
                            if (userBean.getRoleCode().equalsIgnoreCase("child_role")) {
                                userBean.setFavoritePlayer(userData.getString("favourite_player"));
                                userBean.setFavoriteTeam(userData.getString("favourite_team"));
                                userBean.setFavoritePosition(userData.getString("favourite_position"));
                                userBean.setFavoriteFootballBoot(userData.getString("favourite_football_boot"));
                                userBean.setFavoritefood(userData.getString("favourite_food"));
                                userBean.setSchool(userData.getString("school"));
                                userBean.setNationality(userData.getString("nationality"));
                                userBean.setHeight(userData.getString("height"));
                                userBean.setWeight(userData.getString("weight"));
                                userBean.setDobformatted(userData.getString("dob_formatted"));

                                userBean.setHeightNumeric(userData.getString("height"));
                                userBean.setWeightNumeric(userData.getString("weight"));
                                userBean.setFavoritePlayerPicture(userData.getString("favourite_player_picture"));
                                userBean.setFavoriteTemaPicture(userData.getString("favourite_team_picture"));
                                userBean.setHeightFormatted(userData.getString("height_formatted"));
                                userBean.setWeightFormatted(userData.getString("weight_formatted"));

                                userBean.setParentUsername(userData.getString("parent_username"));
                                userBean.setAge(userData.getString("age"));

                                // userBean.setCategoryId(userData.getString("category_id"));
                            }

                            userBean.setPhoneCodeOne(userData.getString("phone_code_1"));
                            userBean.setPhoneCodeTwo(userData.getString("phone_code_2"));

                            if(userBean.getRoleCode().equalsIgnoreCase("parent_role")){
                                userBean.setPaymentCard(userData.getString("payment_card"));
                            }

//                            if(userData.has("user_type")){
//                                userBean.setUser_type(userData.getString("user_type"));
//                            }else{
//                                userBean.setUser_type("");
//                            }

                            if(userData.has("user_type")){
                                if(userData.getString("user_type").equalsIgnoreCase("5")){
                                    userBean.setUser_type(userData.getString("user_type"));
                                    userBean.setRoleCode("child_role");
                                }else{
                                    userBean.setUser_type(userData.getString("user_type"));
                                }

                            }else{
                                userBean.setUser_type("");
                            }

                            Gson gson = new Gson();
                            String jsonParentUserBean = gson.toJson(userBean);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isUserLoggedIn", true);
                            editor.putBoolean("guestUser", true);
                            editor.putString("cUserName", userBean.getFullName());
                            editor.putString("cEmail", userBean.getEmail());
                            editor.putString("cPhone", userBean.getMobileNumber());

                           // editor.putString("user_type", userData.getString("user_type"));

                            /*editor.putString("typeOfUser", "parent");*/
                            if (userBean.getRoleCode().equalsIgnoreCase("parent_role")) {
                                editor.putString("typeOfUser", "parent");
                                if(userData.has("academy_currency")){
                                    editor.putString("academy_currency", userData.getString("academy_currency"));
                                    editor.putString("verbiage_singular", userData.getString("verbiage_singular"));
                                    editor.putString("verbiage_plural", userData.getString("verbiage_plural"));
                                    editor.putString("day_start_num", userData.getString("day_start_num"));
                                }else{
                                    editor.putString("academy_currency", "AED");
                                    editor.putString("verbiage_singular", userData.getString("Child"));
                                    editor.putString("verbiage_plural", userData.getString("Children"));
                                    editor.putString("day_start_num", "1");
                                }
                            } else if (userBean.getRoleCode().equalsIgnoreCase("child_role")) {
                                editor.putString("typeOfUser", "child");
                                if(userData.has("academy_currency")){
                                    editor.putString("academy_currency", userData.getString("academy_currency"));
                                    editor.putString("verbiage_singular", userData.getString("verbiage_singular"));
                                    editor.putString("verbiage_plural", userData.getString("verbiage_plural"));
                                    editor.putString("day_start_num", userData.getString("day_start_num"));
                                }else{
                                    editor.putString("academy_currency", "AED");
                                    editor.putString("verbiage_singular", userData.getString("Child"));
                                    editor.putString("verbiage_plural", userData.getString("Children"));
                                    editor.putString("day_start_num", "1");
                                }
                            }

                            editor.putString("loggedInUser", jsonParentUserBean);
                            editor.commit();

                            Toast.makeText(ParentSignUpScreen.this, Html.fromHtml(message), Toast.LENGTH_LONG).show();

                            /*Intent parentDashboard = new Intent(ParentSignUpScreen.this, ParentMainScreen.class);
                            parentDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(parentDashboard);*/
                            switch (userBean.getRoleCode()) {
                                case "parent_role":
                                    Intent obj = new Intent(ParentSignUpScreen.this, StartModuleDashboardScreen.class);
                                    obj.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(obj);

//                                    Intent parentDashboard = new Intent(ParentSignUpScreen.this, ParentMainScreen.class);
//                                    parentDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(parentDashboard);

                                    break;
                                case "child_role":
                                    Intent obj1 = new Intent(ParentSignUpScreen.this, StartModuleDashboardScreen.class);
                                    obj1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(obj1);

//                                    Intent childDashboard = new Intent(ParentSignUpScreen.this, ChildMainScreen.class);
//                                    childDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(childDashboard);

                                    break;
                                case "coach_role":
                                    Intent obj2 = new Intent(ParentSignUpScreen.this, StartModuleDashboardScreen.class);
                                    obj2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(obj2);

//                                    Intent coachDashboard = new Intent(ParentSignUpScreen.this, CoachMainScreen.class);
//                                    coachDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(coachDashboard);

                                    break;
                            }

                        } else {
                            Toast.makeText(ParentSignUpScreen.this, Html.fromHtml(message), Toast.LENGTH_LONG).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentSignUpScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                break;

            case GET_COUNTRY_CODES:

                countryList.clear();

                if(response == null){
                    Toast.makeText(ParentSignUpScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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

                                    countryCodeOneTextView.setText(country.getDialingCode());
                                    countryCodeTwoTextView.setText(country.getDialingCode());
                                    break;
                                }
                            }

//                            parentCountryCodeAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ParentSignUpScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentSignUpScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void changeFonts() {
        registerTextView.setTypeface(linoType);
        fullNameTextInputLayout.setTypeface(helvetica);
        lastNameTextInputLayout.setTypeface(helvetica);
        fullName.setTypeface(helvetica);
        emailTextInputLayout.setTypeface(helvetica);
        email.setTypeface(helvetica);
        passwordTextInputLayout.setTypeface(helvetica);
        password.setTypeface(helvetica);
        retypePasswordTextInputLayout.setTypeface(helvetica);
        retypePassword.setTypeface(helvetica);
        dob.setTypeface(helvetica);
        mobileTextInputLayout.setTypeface(helvetica);
        mobileNumber.setTypeface(helvetica);
        secondTextInputLayout.setTypeface(helvetica);
        secondMobileNumber.setTypeface(helvetica);
        referralCodeTextInputLayout.setTypeface(helvetica);
        referralCode.setTypeface(helvetica);
        createAccount.setTypeface(linoType);
        bySigningUp.setTypeface(helvetica);
        termsOfUse.setTypeface(helvetica);
        and.setTypeface(helvetica);
        privacyPolicy.setTypeface(helvetica);
        alreadyHave.setTypeface(linoType);
        login.setTypeface(linoType);
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
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month++;
            String strDay = day < 10 ? "0"+day : day+"";
            String strMonth = month < 10 ? "0"+month : month+"";
            dob.setText(strDay+"-"+strMonth+"-"+year);
        }
    }
}