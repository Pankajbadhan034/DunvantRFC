package com.dunvant.application.startModule;
import com.google.firebase.iid.FirebaseInstanceId;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dunvant.application.LoginScreen;
import com.dunvant.application.R;
import com.dunvant.application.parent.ParentSignUpScreen;
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

import androidx.appcompat.app.AppCompatActivity;

public class StartModuleEnterEmailScreen extends AppCompatActivity  implements IWebServiceCallback {
    SharedPreferences sharedPreferences;
    Button continueBT;
    Button loginBT;
    Button signUpBT;
    EditText email;
    EditText name;
    private final String GUEST_USER = "GUEST_USER";
    String emailStr, nameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_enter_email_activity);
        continueBT = findViewById(R.id.continueBT);
        loginBT = findViewById(R.id.loginBT);
        signUpBT = findViewById(R.id.signUpBT);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        continueBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameStr = name.getText().toString().trim();
                emailStr = email.getText().toString().trim();

                Pattern pattern = Pattern.compile(Utilities.EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(emailStr);

                if (nameStr == null || nameStr.isEmpty()) {
                    Toast.makeText(StartModuleEnterEmailScreen.this, "Please enter Name", Toast.LENGTH_SHORT).show();
                } else if (emailStr == null || emailStr.isEmpty()) {
                    Toast.makeText(StartModuleEnterEmailScreen.this, "Please enter Email", Toast.LENGTH_SHORT).show();
                } else if (!matcher.matches()) {
                    Toast.makeText(StartModuleEnterEmailScreen.this, "Please enter a valid Email", Toast.LENGTH_SHORT).show();
                } else {
                    emailAPI();
                }

            }
        });

        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(StartModuleEnterEmailScreen.this, LoginScreen.class);
                startActivity(obj);
            }
        });

        signUpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(StartModuleEnterEmailScreen.this, ParentSignUpScreen.class);
                startActivity(obj);
            }
        });

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println("refreshedTokenHERE:: "+refreshedToken);
        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fcmToken", refreshedToken);
        editor.commit();
    }

    private void emailAPI() {
        if (Utilities.isNetworkAvailable(StartModuleEnterEmailScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            nameValuePairList.add(new BasicNameValuePair("name", nameStr));
            nameValuePairList.add(new BasicNameValuePair("email", emailStr));
            nameValuePairList.add(new BasicNameValuePair("type", "1"));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/add_guest_user";

            PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartModuleEnterEmailScreen.this, nameValuePairList, GUEST_USER, StartModuleEnterEmailScreen.this);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleEnterEmailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GUEST_USER:

                if (response == null) {
                    Toast.makeText(StartModuleEnterEmailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if (status) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("guestUser", true);
                            editor.putString("cUserName", nameStr);
                            editor.putString("cEmail", emailStr);
                            editor.putString("cPhone", "");
                            editor.commit();
                            Intent obj = new Intent(StartModuleEnterEmailScreen.this, StartModuleDashboardScreen.class);
                            startActivity(obj);
                            finish();
                        } else {
                            Toast.makeText(StartModuleEnterEmailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleEnterEmailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}