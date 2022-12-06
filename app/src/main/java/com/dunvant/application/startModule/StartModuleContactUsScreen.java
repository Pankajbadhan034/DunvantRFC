package com.dunvant.application.startModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CountryBean;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class StartModuleContactUsScreen extends AppCompatActivity implements IWebServiceCallback {

    SharedPreferences sharedPreferences;

    Typeface helvetica;
    Typeface linoType;

//    Spinner coachSpinner;
//    EditText title;
//    EditText comment;

    EditText name;
    EditText email;
    EditText phone;
    EditText message;

    TextView countryCodeOneTextView;

    Button sendButton;
    Button cancelButton;
    ImageView backButton;

//    String strTitle;
//    String strComment;

    //    private final String GET_COACHES_LISTING = "GET_COACHES_LISTING";
    private final String SEND_MESSAGE = "SEND_MESSAGE";

    private final String GET_COUNTRY_CODES = "GET_COUNTRY_CODES";
    ArrayList<CountryBean> countryList = new ArrayList<>();
    String defaultCountryCodeFromServer = "";

//    ArrayList<CoachBean> coachesList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_contact_us_activity);
        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String cUserNameStr = sharedPreferences.getString("cUserName", null);
        String cEmailStr = sharedPreferences.getString("cEmail", null);
        String cPhoneStr = sharedPreferences.getString("cPhone", null);

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        message = (EditText) findViewById(R.id.message);
        backButton = findViewById(R.id.backButton);

        countryCodeOneTextView = findViewById(R.id.countryCodeOneTextView);

//        coachSpinner = (Spinner) view.findViewById(R.id.coachSpinner);
//        title = (EditText) view.findViewById(R.id.title);
//        comment = (EditText) view.findViewById(R.id.comment);
        sendButton = (Button) findViewById(R.id.sendButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        name.setTypeface(helvetica);
        email.setTypeface(helvetica);
        phone.setTypeface(helvetica);
        message.setTypeface(helvetica);
        sendButton.setTypeface(linoType);
        cancelButton.setTypeface(linoType);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        name.setText(cUserNameStr);
        email.setText(cEmailStr);
        phone.setText(cPhoneStr);

//        getCoachesListing();

        getCountryCodes();

        countryCodeOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(StartModuleContactUsScreen.this);
                ListView countriesListView = new ListView(StartModuleContactUsScreen.this);
                alertDialog.setView(countriesListView);
                alertDialog.setTitle("Select Country");
                countriesListView.setAdapter(new ParentCountryCodeAdapter(StartModuleContactUsScreen.this, countryList));

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

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strName = name.getText().toString().trim();
                String strEmail = email.getText().toString().trim();
                String strPhone = phone.getText().toString().trim();
                String strMessage = message.getText().toString().trim();
                String strPhoneCodeOne = countryCodeOneTextView.getText().toString().trim();

                if(strName == null || strName.isEmpty()) {
                    Toast.makeText(StartModuleContactUsScreen.this, "Please enter Name", Toast.LENGTH_SHORT).show();
                } else if (strEmail == null || strEmail.isEmpty()) {
                    Toast.makeText(StartModuleContactUsScreen.this, "Please enter Email", Toast.LENGTH_SHORT).show();
                } else if (strPhone == null || strPhone.isEmpty()) {
                    Toast.makeText(StartModuleContactUsScreen.this, "Please enter Phone", Toast.LENGTH_SHORT).show();
                } else if (strMessage == null || strMessage.isEmpty()) {
                    Toast.makeText(StartModuleContactUsScreen.this, "Please enter Message", Toast.LENGTH_SHORT).show();
                } else {
                    if(Utilities.isNetworkAvailable(StartModuleContactUsScreen.this)) {

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("name", strName));
                        nameValuePairList.add(new BasicNameValuePair("email", strEmail));
                        nameValuePairList.add(new BasicNameValuePair("ph_code", strPhoneCodeOne));
                        nameValuePairList.add(new BasicNameValuePair("phone", strPhone));
                        nameValuePairList.add(new BasicNameValuePair("cmsg", strMessage));
                        nameValuePairList.add(new BasicNameValuePair("academies_id", "1"));

                        String webServiceUrl = Utilities.BASE_URL+"contact/request";


                        ArrayList<String> headers = new ArrayList<>();
                       // headers.add("X-access-uid:"+loggedInUser.getId());
                      //  headers.add("X-access-token:"+loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(StartModuleContactUsScreen.this, nameValuePairList, SEND_MESSAGE, StartModuleContactUsScreen.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(StartModuleContactUsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }



                /*int coachPosition = coachSpinner.getSelectedItemPosition();
                strTitle = title.getText().toString().trim();
                strComment = comment.getText().toString().trim();

                if(coachPosition == 0) {
                    Toast.makeText(getActivity(), "Please select Coach", Toast.LENGTH_SHORT).show();
                } else if (strTitle == null || strTitle.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter Title", Toast.LENGTH_SHORT).show();
                } else if (strComment == null || strComment.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter Comment", Toast.LENGTH_SHORT).show();
                } else {
                    if(Utilities.isNetworkAvailable(getActivity())) {

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("coach_id", coachesList.get(coachPosition).getCoachId()));
                        nameValuePairList.add(new BasicNameValuePair("title", strTitle));
                        nameValuePairList.add(new BasicNameValuePair("comments", strComment));

                        String webServiceUrl = Utilities.BASE_URL + "account/send_message_to_coach";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:"+loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, SEND_MESSAGE, ParentContactCoachFragment.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }*/

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getCountryCodes(){
        if(Utilities.isNetworkAvailable(StartModuleContactUsScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "account/phoneCode_list";

            GetWebServiceAsync getWebServiceWithHeadersAsync = new GetWebServiceAsync(StartModuleContactUsScreen.this, GET_COUNTRY_CODES, StartModuleContactUsScreen.this);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleContactUsScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    /*private void getCoachesListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "account/coaches_for_parents";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_COACHES_LISTING, ParentContactCoachFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            /*case GET_COACHES_LISTING:

                coachesList.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            CoachBean coachBean = new CoachBean();
                            coachBean.setCoachId("-1");
                            coachBean.setFullName("Select a Coach");

                            coachesList.add(coachBean);

                            JSONArray dataArray = responseObject.getJSONArray("data");


                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject coachObject = dataArray.getJSONObject(i);
                                coachBean = new CoachBean();

                                coachBean.setCoachId(coachObject.getString("coach_id"));
                                coachBean.setFullName(coachObject.getString("full_name"));

                                coachesList.add(coachBean);
                            }


                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            CoachBean coachBean = new CoachBean();
                            coachBean.setCoachId("-1");
                            coachBean.setFullName("Select a Coach");

                            coachesList.add(coachBean);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                coachSpinner.setAdapter(new ParentCoachSpinnerAdapter(getActivity(), coachesList));

                break;*/
            case SEND_MESSAGE:

                if(response == null) {
                    Toast.makeText(StartModuleContactUsScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(StartModuleContactUsScreen.this, message, Toast.LENGTH_LONG).show();
                        if(status) {
                            cancelButton.performClick();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleContactUsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case GET_COUNTRY_CODES:

                countryList.clear();

                if(response == null){
                    Toast.makeText(StartModuleContactUsScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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

                           // if(loggedInUser.getPhoneCodeOne() == null || loggedInUser.getPhoneCodeOne().isEmpty() || loggedInUser.getPhoneCodeOne().equalsIgnoreCase("null")){
                                countryCodeOneTextView.setText(defaultCountryCodeFromServer);
                          //  } else {
                          //      countryCodeOneTextView.setText(loggedInUser.getPhoneCodeOne());
                         //   }

                        } else {
                            Toast.makeText(StartModuleContactUsScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleContactUsScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}