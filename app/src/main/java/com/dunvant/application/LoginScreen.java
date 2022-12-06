package com.dunvant.application;
import com.google.firebase.iid.FirebaseInstanceId;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.ParentForgotPassword;
import com.dunvant.application.parent.ParentSignUpScreen;
import com.dunvant.application.startModule.StartModuleDashboardScreen;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginScreen extends AppCompatActivity implements IWebServiceCallback, GoogleApiClient.OnConnectionFailedListener {

    SharedPreferences sharedPreferences;
    Typeface helvetica;
    Typeface linoType;

    LinearLayout forgotPasswordLinear;
    LinearLayout signUpLinear;
    Button login;

    EditText email;
    EditText password;
    TextInputLayout emailTextInputLayout;
    TextInputLayout passwordTextInputLayout;

    TextView forgotPassword;
    TextView clickHere;
    TextView notAMember;
    TextView signUpNow;

    ImageView fbLoginButton;
    ImageView googlePlusLoginButton;

    CallbackManager callbackManager;
    LoginButton loginButton;

    String strEmail;
    String strPassword;

    private final String LOGIN_WEB_SERVICE = "LOGIN_WEB_SERVICE";

//    GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 1001;

//    // Run time permission
//    public static final int MULTIPLE_PERMISSIONS = 10;
//    String[] permissions = new String[]{
//            android.Manifest.permission.CAMERA,
//            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            android.Manifest.permission.RECORD_AUDIO
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        // Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

//        // Google+ Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();

        setContentView(R.layout.common_activity_login_screen);

        forgotPasswordLinear = (LinearLayout) findViewById(R.id.forgotPasswordLinear);
        signUpLinear = (LinearLayout) findViewById(R.id.signUpLinear);
        login = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        fbLoginButton = (ImageView) findViewById(R.id.fbLoginButton);
        googlePlusLoginButton = (ImageView) findViewById(R.id.googlePlusLoginButton);

        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordTextInputLayout);
        emailTextInputLayout = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        clickHere = (TextView) findViewById(R.id.clickHere);
        notAMember = (TextView) findViewById(R.id.notAMember);
        signUpNow = (TextView) findViewById(R.id.signUpNow);

        // Fb login button
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        changeFonts();

        googlePlusLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //System.out.println("Facebook Login Success :: " + loginResult.toString());
                AccessToken accessToken = loginResult.getAccessToken();

                //System.out.println("access token :: " + accessToken.getPermissions());

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                //System.out.println("Response :: " + object + "   and  " + response);

                                try {
                                    String emailFromSocial = object.getString("email");
                                    String firstName = object.getString("first_name");
                                    String lastName = object.getString("last_name");
                                    String fbId = object.getString("id");
                                    String fcmToken = sharedPreferences.getString("fcmToken", "");

                                    //System.out.println("id " + fbId + " Email " + emailFromSocial + " name " + firstName + " " + lastName);

                                    // login with facebook
                                    if (Utilities.isNetworkAvailable(LoginScreen.this)) {
                                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                                        nameValuePairList.add(new BasicNameValuePair("name", firstName + " " + lastName));
                                        nameValuePairList.add(new BasicNameValuePair("email", emailFromSocial));
                                        nameValuePairList.add(new BasicNameValuePair("social_id", fbId));
                                        nameValuePairList.add(new BasicNameValuePair("fcm_device_token", fcmToken));
                                        nameValuePairList.add(new BasicNameValuePair("device_type", "1"));

                                        String webServiceUrl = Utilities.BASE_URL + "account/login_fb";

                                        PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(LoginScreen.this, nameValuePairList, LOGIN_WEB_SERVICE, LoginScreen.this);
                                        postWebServiceAsync.execute(webServiceUrl);
                                    } else {
                                        Toast.makeText(LoginScreen.this, R.string.internet_not_available, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    Toast.makeText(LoginScreen.this, "Could not get your information from Facebook", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
//                Toast.makeText(LoginScreen.this, "Fb Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                exception.printStackTrace();
                Toast.makeText(LoginScreen.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        fbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(getApplicationContext())) {
                    loginButton.performClick();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.internet_not_available, Toast.LENGTH_LONG).show();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strEmail = email.getText().toString().trim();
                strPassword = password.getText().toString().trim();
                String fcmToken = sharedPreferences.getString("fcmToken", "");

                Pattern pattern = Pattern.compile(Utilities.EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(strEmail);

                if (strEmail == null || strEmail.isEmpty()) {
                    Toast.makeText(LoginScreen.this, "Please enter Email", Toast.LENGTH_SHORT).show();
                }
//                else if (!matcher.matches()) {
//                    Toast.makeText(getApplicationContext(), "Please enter a valid Email", Toast.LENGTH_SHORT).show();
//                }
                else if (strPassword == null || strPassword.isEmpty()) {
                    Toast.makeText(LoginScreen.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    if (Utilities.isNetworkAvailable(LoginScreen.this)) {
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("lemail", strEmail));
                        nameValuePairList.add(new BasicNameValuePair("lpassword", strPassword));
                        nameValuePairList.add(new BasicNameValuePair("fcm_device_token", fcmToken));
                        nameValuePairList.add(new BasicNameValuePair("device_type", "1"));

                        String webServiceUrl = Utilities.BASE_URL + "account/login";

                        PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(LoginScreen.this, nameValuePairList, LOGIN_WEB_SERVICE, LoginScreen.this);
                        postWebServiceAsync.execute(webServiceUrl);
                    } else {
                        Toast.makeText(LoginScreen.this, R.string.internet_not_available, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        forgotPasswordLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPassword = new Intent(LoginScreen.this, ParentForgotPassword.class);
                startActivity(forgotPassword);
            }
        });

        signUpLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(LoginScreen.this, ParentSignUpScreen.class);
                startActivity(signUp);
            }
        });

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println("refreshedTokenHERE:: "+refreshedToken);
        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fcmToken", refreshedToken);
        editor.commit();

    }

    private void changeFonts() {
        email.setTypeface(helvetica);
        emailTextInputLayout.setTypeface(helvetica);
        password.setTypeface(helvetica);
        passwordTextInputLayout.setTypeface(helvetica);
        login.setTypeface(linoType);
        forgotPassword.setTypeface(helvetica);
        clickHere.setTypeface(linoType);
        notAMember.setTypeface(helvetica);
        signUpNow.setTypeface(linoType);
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case LOGIN_WEB_SERVICE:

                if (response == null) {
                    Toast.makeText(LoginScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        System.out.println("response::"+responseObject);

                        if (status) {

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

                                userBean.setFieldCLub(userData.getString("club"));
                                userBean.setClassName(userData.getString("classname"));

                                // userBean.setCategoryId(userData.getString("category_id"));
                            }

                            // added for Coach Module
                            if(userBean.getRoleCode().equalsIgnoreCase("coach_role")){
                                userBean.setCanMoveChild(userData.getString("can_move_child"));
                                userBean.setReportSubmissionType(userData.getString("report_submission_type"));
                            }

                            if(userBean.getRoleCode().equalsIgnoreCase("parent_role")){
                                userBean.setPaymentCard(userData.getString("payment_card"));
                            }

                            userBean.setPhoneCodeOne(userData.getString("phone_code_1"));
                            userBean.setPhoneCodeTwo(userData.getString("phone_code_2"));

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
                                if(userData.has("club")){
                                    editor.putString("club", userData.getString("club"));
                                    editor.putString("classname", userData.getString("classname"));
                                }
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
                            } else if (userBean.getRoleCode().equalsIgnoreCase("coach_role")) {
                                editor.putString("typeOfUser", "coach");
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

                            switch (userBean.getRoleCode()) {
                                case "parent_role":
                                    Intent obj = new Intent(LoginScreen.this, StartModuleDashboardScreen.class);
                                    obj.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(obj);

//                                    Intent parentDashboard = new Intent(LoginScreen.this, ParentMainScreen.class);
//                                    parentDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(parentDashboard);

                                    break;
                                case "child_role":
                                    Intent obj1 = new Intent(LoginScreen.this, StartModuleDashboardScreen.class);
                                    obj1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(obj1);

//                                    Intent childDashboard = new Intent(LoginScreen.this, ChildMainScreen.class);
//                                    childDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(childDashboard);

                                    break;
                                case "coach_role":
                                    Intent obj2 = new Intent(LoginScreen.this, StartModuleDashboardScreen.class);
                                    obj2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(obj2);

//                                    Intent coachDashboard = new Intent(LoginScreen.this, CoachMainScreen.class);
//                                    coachDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(coachDashboard);

                                    break;
                            }


                        } else {
                            Toast.makeText(LoginScreen.this, message, Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("on activity result::");

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            //System.out.println("rc sign in");

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            int statusCode = result.getStatus().getStatusCode();
            System.out.println("status code ::" + statusCode);

            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {

        //System.out.println("handle sign in result");


        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            //System.out.println(acct.getId() + " " + acct.getDisplayName() + " " + acct.getEmail());

            String fcmToken = sharedPreferences.getString("fcmToken", "");

            if (Utilities.isNetworkAvailable(LoginScreen.this)) {
                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(new BasicNameValuePair("name", acct.getDisplayName()));
                nameValuePairList.add(new BasicNameValuePair("email", acct.getEmail()));
                nameValuePairList.add(new BasicNameValuePair("social_id", acct.getId()));
                nameValuePairList.add(new BasicNameValuePair("fcm_device_token", fcmToken));
                nameValuePairList.add(new BasicNameValuePair("device_type", "1"));

                String webServiceUrl = Utilities.BASE_URL + "account/login_google";

                PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(LoginScreen.this, nameValuePairList, LOGIN_WEB_SERVICE, LoginScreen.this);
                postWebServiceAsync.execute(webServiceUrl);
            } else {
                Toast.makeText(LoginScreen.this, R.string.internet_not_available, Toast.LENGTH_LONG).show();
            }


//            Toast.makeText(LoginScreen.this, acct.getDisplayName() + " " + acct.getEmail(), Toast.LENGTH_SHORT).show();
        } else {
            // Signed out, show unauthenticated UI.
            //System.out.println("Signed Out");
            Toast.makeText(LoginScreen.this, "Google+ login failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(LoginScreen.this, "Google+ Connection Failed", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkPermissions()) {
////                Toast.makeText(ChildTrackWorkoutScreen.this, "Permissions already granted", Toast.LENGTH_SHORT).show();
//            } else {
////                Toast.makeText(ChildTrackWorkoutScreen.this, "Initially permission not available", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }
//
//    private boolean checkPermissions() {
//        int result;
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        for (String p : permissions) {
//            result = ContextCompat.checkSelfPermission(LoginScreen.this, p);
//            if (result != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(p);
//            }
//        }
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int grantResults[]) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case MULTIPLE_PERMISSIONS: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permissions granted.
//                } else {
//                    // no permissions granted.
//                    Toast.makeText(LoginScreen.this, "This feature cannot work without Permissions. \nRelaunch the App or allow permissions in Applications Settings", Toast.LENGTH_LONG).show();
//                    finish();
//                }
//                return;
//            }
//        }
//
//    }
}