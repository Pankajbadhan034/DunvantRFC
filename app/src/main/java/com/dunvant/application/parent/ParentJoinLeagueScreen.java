package com.dunvant.application.parent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CountryBean;
import com.dunvant.application.beans.LeagueBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentCountryCodeAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ParentJoinLeagueScreen extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    TextInputLayout nameOfTeamTIL;
    EditText nameOfTeam;
    TextInputLayout emailTIL;
    EditText email;
    TextInputLayout contactNameTIL;
    EditText contactName;
    TextInputLayout contactNumberTIL;
    EditText contactNumber;
    TextInputLayout contactCommentTIL;
    EditText contactComment;
    TextView joinLeague;
    TextView cancel;

    TextView countryCodeOneTextView;

    LeagueBean clickedOnLeague;

    private final String JOIN_LEAGUE = "JOIN_LEAGUE";

    private final String GET_COUNTRY_CODES = "GET_COUNTRY_CODES";
    ArrayList<CountryBean> countryList = new ArrayList<>();
    String defaultCountryCodeFromServer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_join_league_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        nameOfTeamTIL = (TextInputLayout) findViewById(R.id.nameOfTeamTIL);
        nameOfTeam = (EditText) findViewById(R.id.nameOfTeam);
        emailTIL = (TextInputLayout) findViewById(R.id.emailTIL);
        email = (EditText) findViewById(R.id.email);
        contactNameTIL = (TextInputLayout) findViewById(R.id.contactNameTIL);
        contactName = (EditText) findViewById(R.id.contactName);
        contactNumberTIL = (TextInputLayout) findViewById(R.id.contactNumberTIL);
        contactNumber = (EditText) findViewById(R.id.contactNumber);
        contactCommentTIL = (TextInputLayout) findViewById(R.id.contactCommentTIL);
        contactComment = (EditText) findViewById(R.id.contactComment);
        joinLeague = (TextView) findViewById(R.id.joinLeague);
        cancel = (TextView) findViewById(R.id.cancel);

        countryCodeOneTextView = findViewById(R.id.countryCodeOneTextView);

        email.setText(loggedInUser.getEmail());
        contactName.setText(loggedInUser.getFullName());
        contactNumber.setText(loggedInUser.getMobileNumber());

        changeFonts();

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnLeague = (LeagueBean) intent.getSerializableExtra("clickedOnLeague");
        }

        getCountryCodes();

        countryCodeOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ParentJoinLeagueScreen.this);
                ListView countriesListView = new ListView(ParentJoinLeagueScreen.this);
                alertDialog.setView(countriesListView);
                alertDialog.setTitle("Select Country");
                countriesListView.setAdapter(new ParentCountryCodeAdapter(ParentJoinLeagueScreen.this, countryList));

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

        joinLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNameofTeam = nameOfTeam.getText().toString().trim();
                String strEmail = email.getText().toString().trim();
                String strContactName = contactName.getText().toString().trim();
                String strContactNumber = contactNumber.getText().toString().trim();
                String strComments = contactComment.getText().toString().trim();
                String strPhoneCodeOne = countryCodeOneTextView.getText().toString().trim();

                if (strNameofTeam == null || strNameofTeam.isEmpty()) {
                    Toast.makeText(ParentJoinLeagueScreen.this, "Please enter Name of Team", Toast.LENGTH_SHORT).show();
                } else if (strEmail == null || strEmail.isEmpty()) {
                    Toast.makeText(ParentJoinLeagueScreen.this, "Please enter Email", Toast.LENGTH_SHORT).show();
                } else if (strContactName == null || strContactName.isEmpty()) {
                    Toast.makeText(ParentJoinLeagueScreen.this, "Please enter Contact Name", Toast.LENGTH_SHORT).show();
                } else if (strContactNumber == null || strContactNumber.isEmpty()) {
                    Toast.makeText(ParentJoinLeagueScreen.this, "Please enter Contact Number", Toast.LENGTH_SHORT).show();
                }else if(strComments == null || strComments.isEmpty()){
                    Toast.makeText(ParentJoinLeagueScreen.this, "Please enter Comment", Toast.LENGTH_SHORT).show();
                }else {

                    if(Utilities.isNetworkAvailable(ParentJoinLeagueScreen.this)) {

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("academies_id", clickedOnLeague.getAcademiesId()));
                        nameValuePairList.add(new BasicNameValuePair("users_id", loggedInUser.getId()));
                        nameValuePairList.add(new BasicNameValuePair("league_id", clickedOnLeague.getLeagueId()));
                        nameValuePairList.add(new BasicNameValuePair("league_name", clickedOnLeague.getLeagueName()));
                        nameValuePairList.add(new BasicNameValuePair("team_name", strNameofTeam));
                        nameValuePairList.add(new BasicNameValuePair("cemail", strEmail));
                        nameValuePairList.add(new BasicNameValuePair("cname", strContactName));

                        nameValuePairList.add(new BasicNameValuePair("ph_code", strPhoneCodeOne));
                        nameValuePairList.add(new BasicNameValuePair("cphone", strContactNumber));

                        nameValuePairList.add(new BasicNameValuePair("comments", strComments));

                        String webServiceUrl = Utilities.BASE_URL + "leagues/join";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:"+loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentJoinLeagueScreen.this, nameValuePairList, JOIN_LEAGUE, ParentJoinLeagueScreen.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(ParentJoinLeagueScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
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

    }

    private void getCountryCodes(){
        if(Utilities.isNetworkAvailable(ParentJoinLeagueScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "account/phoneCode_list";

            GetWebServiceAsync getWebServiceWithHeadersAsync = new GetWebServiceAsync(ParentJoinLeagueScreen.this, GET_COUNTRY_CODES, ParentJoinLeagueScreen.this);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentJoinLeagueScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case JOIN_LEAGUE:

                if(response == null) {
                    Toast.makeText(ParentJoinLeagueScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(ParentJoinLeagueScreen.this, Html.fromHtml(message), Toast.LENGTH_LONG).show();

                        if(status) {
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentJoinLeagueScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case GET_COUNTRY_CODES:

                countryList.clear();

                if(response == null){
                    Toast.makeText(ParentJoinLeagueScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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
                        } else {
                            Toast.makeText(ParentJoinLeagueScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentJoinLeagueScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void changeFonts(){
        title.setTypeface(linoType);
        nameOfTeamTIL.setTypeface(helvetica);
        nameOfTeam.setTypeface(helvetica);
        emailTIL.setTypeface(helvetica);
        email.setTypeface(helvetica);
        contactNameTIL.setTypeface(helvetica);
        contactName.setTypeface(helvetica);
        contactNumberTIL.setTypeface(helvetica);
        contactNumber.setTypeface(helvetica);
        joinLeague.setTypeface(linoType);
        cancel.setTypeface(linoType);
        contactCommentTIL.setTypeface(helvetica);
        contactComment.setTypeface(helvetica);
    }

}