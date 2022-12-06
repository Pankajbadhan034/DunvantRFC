package com.dunvant.application.parent;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.dunvant.application.R;
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

public class ParentForgotPassword extends AppCompatActivity implements IWebServiceCallback{

    Typeface helvetica;
    Typeface linoType;

    TextInputLayout emailTextInputLayout;
    LinearLayout backToLoginLinear;
    TextView lblForgotPassword;
    EditText email;
    Button reset;
    TextView backTo;
    TextView login;
    
    String strEmail;
    private final String FORGOT_PASSWORD = "FORGOT_PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_forgot_password);

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        emailTextInputLayout = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        backToLoginLinear = (LinearLayout) findViewById(R.id.backToLoginLinear);
        lblForgotPassword = (TextView) findViewById(R.id.lblForgotPassword);
        email = (EditText) findViewById(R.id.email);
        reset = (Button) findViewById(R.id.reset);
        backTo = (TextView) findViewById(R.id.backTo);
        login = (TextView) findViewById(R.id.login);

        changeFonts();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail = email.getText().toString().trim();

                Pattern pattern = Pattern.compile(Utilities.EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(strEmail);

                if(strEmail == null || strEmail.isEmpty()) {
                    Toast.makeText(ParentForgotPassword.this, "Please enter Email", Toast.LENGTH_SHORT).show();
                } else if (!matcher.matches()) {
                    Toast.makeText(ParentForgotPassword.this, "Please enter a valid Email", Toast.LENGTH_SHORT).show();
                } else {
                    if(Utilities.isNetworkAvailable(ParentForgotPassword.this)) {
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("email", strEmail));

                        String webServiceUrl = Utilities.BASE_URL + "account/forgot_password";

                        PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(ParentForgotPassword.this, nameValuePairList, FORGOT_PASSWORD, ParentForgotPassword.this);
                        postWebServiceAsync.execute(webServiceUrl);
                    } else {
                        Toast.makeText(ParentForgotPassword.this, R.string.internet_not_available, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        backToLoginLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case FORGOT_PASSWORD:

                if(response == null) {
                    Toast.makeText(ParentForgotPassword.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(ParentForgotPassword.this, message, Toast.LENGTH_SHORT).show();

                        if(status){
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }

                break;
        }
    }

    private void changeFonts(){
        lblForgotPassword.setTypeface(linoType);
        emailTextInputLayout.setTypeface(helvetica);
        email.setTypeface(helvetica);
        reset.setTypeface(linoType);
        backTo.setTypeface(helvetica);
        login.setTypeface(linoType);
    }
}
