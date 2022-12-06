package com.dunvant.application.parent;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParentChangePassword extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;

    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    EditText newPassword;
    EditText retypePassword;
    TextView update;

    String strNewPassword;
    String strRetypePassword;

    UserBean loggedInUser;

    private final String CHANGE_PASSWORD = "CHANGE_PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_change_password);

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        newPassword = (EditText) findViewById(R.id.newPassword);
        retypePassword = (EditText) findViewById(R.id.retypePassword);
        update = (TextView) findViewById(R.id.update);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        title.setTypeface(linoType);
        newPassword.setTypeface(helvetica);
        retypePassword.setTypeface(helvetica);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strNewPassword = newPassword.getText().toString().trim();
                strRetypePassword = retypePassword.getText().toString().trim();

                if(strNewPassword == null || strNewPassword.isEmpty()) {
                    Toast.makeText(ParentChangePassword.this, "Please enter New Password", Toast.LENGTH_SHORT).show();
                } else if (strRetypePassword == null || strRetypePassword.isEmpty()) {
                    Toast.makeText(ParentChangePassword.this, "Please enter Retype Password", Toast.LENGTH_SHORT).show();
                } else if (!strNewPassword.equals(strRetypePassword)){
                    Toast.makeText(ParentChangePassword.this, "New Password and Retype Password do not match", Toast.LENGTH_SHORT).show();
                } else {
                    if(Utilities.isNetworkAvailable(ParentChangePassword.this)) {

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("password", strNewPassword));
                        nameValuePairList.add(new BasicNameValuePair("cpassword", strRetypePassword));
                        nameValuePairList.add(new BasicNameValuePair("users_id", loggedInUser.getId()));

                        String webServiceUrl = Utilities.BASE_URL + "account/reset_password";

                        PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(ParentChangePassword.this, nameValuePairList, CHANGE_PASSWORD, ParentChangePassword.this);
                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(ParentChangePassword.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }

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
            case CHANGE_PASSWORD:

                if(response == null) {
                    Toast.makeText(ParentChangePassword.this, "Could not reach server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(ParentChangePassword.this, message, Toast.LENGTH_SHORT).show();

                        if (status) {
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentChangePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}
