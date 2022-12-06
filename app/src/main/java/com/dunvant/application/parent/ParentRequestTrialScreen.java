package com.dunvant.application.parent;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachingAcademyBean;
import com.dunvant.application.beans.CountryBean;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ParentRequestTrialScreen extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    CoachingAcademyBean clickedOnAcademy;

    ImageView backButton;
    TextView title;
    EditText childName;
    static TextView childDob;
    EditText parentName;
    EditText parentContactNumber;
    EditText parentEmail;
    EditText remarks;
    Button submit;
    Button cancel;

    private final String REQUEST_TRIAL =  "REQUEST_TRIAL";

    TextView countryCodeOneTextView;
    private final String GET_COUNTRY_CODES = "GET_COUNTRY_CODES";
    ArrayList<CountryBean> countryList = new ArrayList<>();
    String defaultCountryCodeFromServer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_request_trial_screen);

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
        childName = (EditText) findViewById(R.id.childName);
        childDob = (TextView) findViewById(R.id.childDob);
        parentName = (EditText) findViewById(R.id.parentName);
        parentContactNumber = (EditText) findViewById(R.id.parentContactNumber);
        parentEmail = (EditText) findViewById(R.id.parentEmail);
        remarks = (EditText) findViewById(R.id.remarks);
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);

        countryCodeOneTextView = findViewById(R.id.countryCodeOneTextView);

        changeFonts();

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnAcademy = (CoachingAcademyBean) intent.getSerializableExtra("clickedOnAcademy");
        }

        getCountryCodes();

        countryCodeOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ParentRequestTrialScreen.this);
                ListView countriesListView = new ListView(ParentRequestTrialScreen.this);
                alertDialog.setView(countriesListView);
                alertDialog.setTitle("Select Country");
                countriesListView.setAdapter(new ParentCountryCodeAdapter(ParentRequestTrialScreen.this, countryList));

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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strChildName = childName.getText().toString().trim();
                String strChildDob = childDob.getText().toString().trim();
                String strParentName = parentName.getText().toString().trim();
                String strParentContactNumber = parentContactNumber.getText().toString().trim();
                String strParentEmail = parentEmail.getText().toString().trim();
                String strRemarks = remarks.getText().toString().trim();
                String strPhoneCodeOne = countryCodeOneTextView.getText().toString().trim();

                if(strChildName == null || strChildName.isEmpty()) {
                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                    Toast.makeText(ParentRequestTrialScreen.this, "Please enter "+verbiage_singular+" Name", Toast.LENGTH_SHORT).show();
                } else if (strChildDob.equalsIgnoreCase("Child's Date of Birth")) {
                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                    Toast.makeText(ParentRequestTrialScreen.this, "Please enter "+verbiage_singular+" Name", Toast.LENGTH_SHORT).show();

                } else if (strParentName == null || strParentName.isEmpty()) {
                    Toast.makeText(ParentRequestTrialScreen.this, "Please enter Parent Name", Toast.LENGTH_SHORT).show();
                } else if (strParentContactNumber == null || strParentContactNumber.isEmpty()) {
                    Toast.makeText(ParentRequestTrialScreen.this, "Please enter Parent's Contact Number", Toast.LENGTH_SHORT).show();
                } else if (strParentEmail == null || strParentEmail.isEmpty()) {
                    Toast.makeText(ParentRequestTrialScreen.this, "Please enter Parent's Email", Toast.LENGTH_SHORT).show();
                } else {
                    if(Utilities.isNetworkAvailable(ParentRequestTrialScreen.this)) {

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("p_id", clickedOnAcademy.getAcademyId()));
                        nameValuePairList.add(new BasicNameValuePair("cname", strChildName));
                        nameValuePairList.add(new BasicNameValuePair("dob", strChildDob));
                        nameValuePairList.add(new BasicNameValuePair("pname", strParentName));

                        nameValuePairList.add(new BasicNameValuePair("ph_code", strPhoneCodeOne));
                        nameValuePairList.add(new BasicNameValuePair("pcontact", strParentContactNumber));

                        nameValuePairList.add(new BasicNameValuePair("pemail", strParentEmail));
                        nameValuePairList.add(new BasicNameValuePair("remarks", strRemarks));

                        String webServiceUrl = Utilities.BASE_URL + "coaching_programs/trial_request";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:"+loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentRequestTrialScreen.this, nameValuePairList, REQUEST_TRIAL, ParentRequestTrialScreen.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(ParentRequestTrialScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        childDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
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
        if(Utilities.isNetworkAvailable(ParentRequestTrialScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "account/phoneCode_list";

            GetWebServiceAsync getWebServiceWithHeadersAsync = new GetWebServiceAsync(ParentRequestTrialScreen.this, GET_COUNTRY_CODES, ParentRequestTrialScreen.this);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentRequestTrialScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case REQUEST_TRIAL:

                if(response == null) {
                    Toast.makeText(ParentRequestTrialScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(ParentRequestTrialScreen.this, message, Toast.LENGTH_SHORT).show();

                        if(status) {
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentRequestTrialScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case GET_COUNTRY_CODES:

                countryList.clear();

                if(response == null){
                    Toast.makeText(ParentRequestTrialScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ParentRequestTrialScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentRequestTrialScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            childDob.setText(day+"-"+(month+1)+"-"+year);
        }
    }

    private void changeFonts(){
        title.setTypeface(helvetica);
        childName.setTypeface(helvetica);
        childDob.setTypeface(helvetica);
        parentName.setTypeface(helvetica);
        parentContactNumber.setTypeface(helvetica);
        parentEmail.setTypeface(helvetica);
        remarks.setTypeface(helvetica);
        submit.setTypeface(linoType);
        cancel.setTypeface(linoType);
    }
}