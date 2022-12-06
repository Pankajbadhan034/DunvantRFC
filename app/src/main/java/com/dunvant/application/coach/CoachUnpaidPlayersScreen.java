package com.dunvant.application.coach;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachChildUnpaidPlayerBean;
import com.dunvant.application.beans.CountryBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachUnpaidPlayersAdapter;
import com.dunvant.application.coach.fragments.CoachManageAttendanceFragment;
import com.dunvant.application.parent.adapters.ParentCountryCodeAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceAsync;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

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

public class CoachUnpaidPlayersScreen extends AppCompatActivity implements IWebServiceCallback {
    String usersID;
    String dobLabelStr,gendername1, gendername2, fNameLabelStr, lNameLabelStr, NameLabelStr, genderLabelStr, genderInputTypeMultFieldStr;
    private final String GET_REG = "GET_REG";
    private final String GET_COUNTRY_CODES = "GET_COUNTRY_CODES";
    Button addNewChild;
    LinearLayout linearForChildFileds;
    String finalDate;
    String child_ids;
    EditText emailET;
    Button searchBT;
    ImageView backButton;
    private final String SEARCH_EMAIL = "SEARCH_EMAIL";
    private final String SESSION_DATE = "SESSION_DATE";
    private final String ADD_UNPAID = "ADD_UNPAID";
    String strEmail;
    String strCheckEmail;
    ArrayList<CoachChildUnpaidPlayerBean> coachChildUnpaidPlayerBeanArrayList;
    GridView gridView;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    String groupName;
    String coachingProgName;
    String coachingProgID;
    String locationName;
    String locationId;
    String sessionId;
    String sessionDate;
    TextView sessionInfo;
    TextView sessionAttendedDate;
    Button submitSessionDate;
    Button resetSessionDate;
    boolean addNewChildBool;
    EditText parentNameET;
    EditText mobileNumberET;
    TextInputLayout parentName;
    TextInputLayout mobileTextInputLayout;
    LinearLayout linearPhone;
    TextView countryCodeOneTextView;
    ArrayList<CountryBean> countryList = new ArrayList<>();
    String defaultCountryCodeFromServer = "";
    EditText fullNameET;
    boolean emailExistBool;
    static TextView dob;
    String strDob;
    TextView sex;
    RadioButton male;
    RadioButton female;
    RadioGroup radioGroup;
    String strGender = "";
    TextInputLayout fullName;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_unpaid_players_screen_activity);
        emailET = findViewById(R.id.emailET);
        searchBT = findViewById(R.id.searchBT);
        backButton = findViewById(R.id.backButton);
        gridView = findViewById(R.id.gridView);
        sessionInfo = findViewById(R.id.sessionInfo);
        sessionAttendedDate = findViewById(R.id.sessionAttendedDate);
        submitSessionDate = findViewById(R.id.submitSessionDate);
        resetSessionDate = findViewById(R.id.resetSessionDate);
        linearForChildFileds = findViewById(R.id.linearForChildFileds);
        addNewChild = findViewById(R.id.addNewChild);
        parentNameET = findViewById(R.id.parentNameET);
        mobileNumberET = findViewById(R.id.mobileNumberET);
        parentName = findViewById(R.id.parentName);
        mobileTextInputLayout = findViewById(R.id.mobileTextInputLayout);
        linearPhone = findViewById(R.id.linearPhone);
        countryCodeOneTextView = findViewById(R.id.countryCodeOneTextView);
        fullNameET = findViewById(R.id.fullNameET);
        dob = findViewById(R.id.dob);
        sex = (TextView) findViewById(R.id.sex);
        radioGroup = findViewById(R.id.radioGroup);
        fullName = findViewById(R.id.fullName);
        title = findViewById(R.id.title);


        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        final String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
        title.setText("ADD UNPAID "+verbiage_singular.toUpperCase());
        addNewChild.setText("Add New "+verbiage_singular);


        groupName = getIntent().getStringExtra("groupName");
        coachingProgName = getIntent().getStringExtra("coachingProgName");
        coachingProgID = getIntent().getStringExtra("coachingProgID");
        locationName = getIntent().getStringExtra("locationName");
        locationId = getIntent().getStringExtra("locationId");
        sessionId = getIntent().getStringExtra("sessionId");
        sessionDate = getIntent().getStringExtra("sessionDate");

        sessionInfo.setText("SESSION INFORMATION:\n\n" +
                "Session Attended Date : "+sessionDate+"\n\n" +
                ""+groupName+"\n\n" +
                ""+coachingProgName+"\n\n" +
                ""+locationName+"");

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CoachUnpaidPlayersScreen.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {

                                month = month+1;

                                String strDay = day < 10 ? "0"+day : day+"";
                                String strMonth = month < 10 ? "0"+month : month+"";
                                strDob = strDay+"-"+strMonth+"-"+year;
                                dob.setText(strDob);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        countryCodeOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CoachUnpaidPlayersScreen.this);
                ListView countriesListView = new ListView(CoachUnpaidPlayersScreen.this);
                alertDialog.setView(countriesListView);
                alertDialog.setTitle("Select Country");
                countriesListView.setAdapter(new ParentCountryCodeAdapter(CoachUnpaidPlayersScreen.this, countryList));

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

        addNewChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearForChildFileds.setVisibility(View.VISIBLE);
                parentName.setVisibility(View.GONE);
                linearPhone.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                addNewChild.setVisibility(View.GONE);
                addNewChildBool = true;
            }
        });


        resetSessionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailET.setText("");
                parentNameET.setText("");
                mobileNumberET.setText("");
                fullNameET.setText("");
                gridView.setAdapter(null);
                gridView.setVisibility(View.GONE);
                linearForChildFileds.setVisibility(View.GONE);
                addNewChild.setVisibility(View.GONE);
                addNewChildBool = false;
                strGender = "";
                strEmail = "";
                strCheckEmail = "";
                strDob = "";

            }
        });

       male.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               strGender = "1";
           }
       });

       female.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               strGender = "2";
           }
       });

        submitSessionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                strEmail = emailET.getText().toString().trim();

                Pattern pattern = Pattern.compile(Utilities.EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(strEmail);

                if (strEmail == null || strEmail.isEmpty()) {
                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Please search parent email to add "+verbiage_singular, Toast.LENGTH_SHORT).show();
                    return;
                }else if (!matcher.matches()) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid Email", Toast.LENGTH_SHORT).show();
                    return;
                }else{

                    if(addNewChildBool==true){

                        if(strCheckEmail.equalsIgnoreCase(strEmail)){
                            if(emailExistBool==true){
                                String fullNameStr = fullNameET.getText().toString().trim();
                                strDob = dob.getText().toString().trim();
                                if(fullNameStr == null || fullNameStr.isEmpty()){
                                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Please enter "+fNameLabelStr, Toast.LENGTH_SHORT).show();
                                } else if (strDob == null || strDob.isEmpty() || strDob.equalsIgnoreCase(dobLabelStr)) {
                                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Please select "+dobLabelStr, Toast.LENGTH_SHORT).show();
                                }else if(strGender == null || strGender.isEmpty()){
                                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Please select "+genderLabelStr, Toast.LENGTH_SHORT).show();
                                }else{
                                    apiAddUnpaidNoChildExistYesParentExists();
                                }
                            }else{
                                String parentNameStr = parentNameET.getText().toString().trim();
                                String phoneNoStr = mobileNumberET.getText().toString().trim();
                                String fullNameStr = fullNameET.getText().toString().trim();
                                strDob = dob.getText().toString().trim();


                                if(parentNameStr == null || parentNameStr.isEmpty()){
                                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Please enter parent name", Toast.LENGTH_SHORT).show();
                                }else if(phoneNoStr == null || phoneNoStr.isEmpty()){
                                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                                }else if(phoneNoStr == null || phoneNoStr.isEmpty()){
                                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                                }else if(fullNameStr == null || fullNameStr.isEmpty()){
                                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Please enter "+fNameLabelStr, Toast.LENGTH_SHORT).show();
                                } else if (strDob == null || strDob.isEmpty() || strDob.equalsIgnoreCase(dobLabelStr)) {
                                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Please select "+dobLabelStr, Toast.LENGTH_SHORT).show();
                                }else if(strGender == null || strGender.isEmpty()){
                                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Please select "+genderLabelStr, Toast.LENGTH_SHORT).show();
                                }else{
                                    apiAddUnpaidNoChildExistNoParentExists("", parentNameStr, phoneNoStr, defaultCountryCodeFromServer);
                                }


                            }

                        }else{
                            Toast.makeText(CoachUnpaidPlayersScreen.this, "Please search again parent email to add "+verbiage_singular, Toast.LENGTH_SHORT).show();
                        }


                    }else{
                        child_ids = "";
                        for(int i=0; i<coachChildUnpaidPlayerBeanArrayList.size(); i++){

                            if(coachChildUnpaidPlayerBeanArrayList.get(i).getCheck().equalsIgnoreCase("true")){
                                if(child_ids.equalsIgnoreCase("")){
                                    child_ids = child_ids + coachChildUnpaidPlayerBeanArrayList.get(i).getId();
                                }else {
                                    child_ids = child_ids + ","+coachChildUnpaidPlayerBeanArrayList.get(i).getId();
                                }

                            }
                        }

                        try{
                            System.out.println("child_IDS::"+child_ids);

                            if(child_ids.equalsIgnoreCase("")){
                                Toast.makeText(CoachUnpaidPlayersScreen.this, "Please select "+verbiage_singular, Toast.LENGTH_SHORT).show();
                            }else{
                                apiAddUnpaidChildExist();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                }


            }
        });

        sessionAttendedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CoachUnpaidPlayersScreen.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {

                                month = month+1;

                                String strDay = day < 10 ? "0"+day : day+"";
                                String strMonth = month < 10 ? "0"+month : month+"";
                                 finalDate = strDay+"-"+strMonth+"-"+year;

                                apiSessionDate(finalDate);

                               // txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, year, month, day);
                datePickerDialog.show();

//                DialogFragment newFragment = new DatePickerFrag();
//                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        searchBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                gridView.setVisibility(View.GONE);
                linearForChildFileds.setVisibility(View.GONE);
                addNewChild.setVisibility(View.GONE);

                strEmail = emailET.getText().toString().trim();

                Pattern pattern = Pattern.compile(Utilities.EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(strEmail);

                if (strEmail == null || strEmail.isEmpty()) {
                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Please enter Email", Toast.LENGTH_SHORT).show();
                } else if (!matcher.matches()) {
                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Please enter a valid Email", Toast.LENGTH_SHORT).show();
                }else{
                    if(Utilities.isNetworkAvailable(CoachUnpaidPlayersScreen.this)) {

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("email", strEmail));

                        String webServiceUrl = Utilities.BASE_URL + "coach/search_email";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:"+loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachUnpaidPlayersScreen.this, nameValuePairList, SEARCH_EMAIL, CoachUnpaidPlayersScreen.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(CoachUnpaidPlayersScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
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
        apiSessionDate(sessionDate);

    }

    private void getCountryCodes(){
        if(Utilities.isNetworkAvailable(CoachUnpaidPlayersScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "account/phoneCode_list";

            GetWebServiceAsync getWebServiceWithHeadersAsync = new GetWebServiceAsync(CoachUnpaidPlayersScreen.this, GET_COUNTRY_CODES, CoachUnpaidPlayersScreen.this);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachUnpaidPlayersScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case SEARCH_EMAIL:

                if(response == null) {
                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try{
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");
                            coachChildUnpaidPlayerBeanArrayList = new ArrayList<>();
                            usersID = responseObject.getString("users_id");
                            for(int i=0; i<dataArray.length(); i++){
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                CoachChildUnpaidPlayerBean coachChildUnpaidPlayerBean = new CoachChildUnpaidPlayerBean();
                                coachChildUnpaidPlayerBean.setEmail(responseObject.getString("email"));
                                coachChildUnpaidPlayerBean.setUserId(responseObject.getString("users_id"));
                                coachChildUnpaidPlayerBean.setId(jsonObject.getString("id"));
                                coachChildUnpaidPlayerBean.setFullName(jsonObject.getString("full_name"));
                                coachChildUnpaidPlayerBean.setGender(jsonObject.getString("gender"));
                                coachChildUnpaidPlayerBean.setAge(jsonObject.getString("age"));
                                coachChildUnpaidPlayerBean.setDob(jsonObject.getString("dob"));
                                coachChildUnpaidPlayerBean.setGender_value(jsonObject.getString("gender_value"));
                                coachChildUnpaidPlayerBean.setDateOfBirth(jsonObject.getString("date_of_birth"));
                                coachChildUnpaidPlayerBean.setFirstName(jsonObject.getString("first_name"));
                                coachChildUnpaidPlayerBean.setLastName(jsonObject.getString("last_name"));
                                coachChildUnpaidPlayerBean.setWeight(jsonObject.getString("weight"));
                                coachChildUnpaidPlayerBean.setHeight(jsonObject.getString("height"));
                                coachChildUnpaidPlayerBean.setCheck("false");

                                coachChildUnpaidPlayerBeanArrayList.add(coachChildUnpaidPlayerBean);
                            }


                            emailExistBool = true;
                            addNewChildBool = false;
                            strCheckEmail = emailET.getText().toString().trim();
                            addNewChild.setVisibility(View.VISIBLE);



                            if(!(coachChildUnpaidPlayerBeanArrayList.size()==0)){
                                CoachUnpaidPlayersAdapter coachUnpaidPlayersAdapter = new CoachUnpaidPlayersAdapter(CoachUnpaidPlayersScreen.this, coachChildUnpaidPlayerBeanArrayList);
                                gridView.setAdapter(coachUnpaidPlayersAdapter);
                                Utilities.setGridViewHeightBasedOnChildren(gridView,2);
                                gridView.setVisibility(View.VISIBLE);
                            }



                        }else{
                            emailExistBool = false;
                            addNewChildBool = true;
                            strCheckEmail = emailET.getText().toString().trim();
                            gridView.setVisibility(View.GONE);
                            linearForChildFileds.setVisibility(View.VISIBLE);
                            parentName.setVisibility(View.VISIBLE);
                            linearPhone.setVisibility(View.VISIBLE);
                           // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                        getCountryCodes();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case SESSION_DATE:
                if(response == null) {
                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try{
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            if(message.equalsIgnoreCase("ALL OK")){

                            }else{
                                // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(CoachUnpaidPlayersScreen.this);
                                builder1.setMessage(""+message);
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });


                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }

                        }else{
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                        getCountryCodes();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case ADD_UNPAID:
                if(response == null) {
                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try{
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//                            Intent mainScreen = new Intent(this, CoachMainScreen.class);
//                            mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(mainScreen);
                            CoachManageAttendanceFragment.variableTrue = true;
                            finish();
                        }else{
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case GET_COUNTRY_CODES:

                countryList.clear();

                if(response == null){
                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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
                                    break;
                                }
                            }

//                            parentCountryCodeAdapter.notifyDataSetChanged();
                            getRegForm();
                        } else {
                            Toast.makeText(CoachUnpaidPlayersScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachUnpaidPlayersScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case GET_REG:

                System.out.println("RES::"+response);

                if (response == null) {
                    Toast.makeText(CoachUnpaidPlayersScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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
//                                    fNameIdStr = jsonObject.getString("id");
                                    fNameLabelStr = jsonObject.getString("label_name");
//                                    fNameSlugStr = jsonObject.getString("slug");
//                                    fNameIsShowStr = jsonObject.getString("is_show");
//                                    fNameIsReqStr = jsonObject.getString("is_required");
//                                    fNameFieldTypeStr = jsonObject.getString("field_type");
//                                    fNameInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");


//                                    if(fNameIsShowStr.equalsIgnoreCase("1")){
                                       // firstNameTIL.setHint(fNameLabelStr);
//                                        firstNameTIL.setVisibility(View.VISIBLE);
//                                    }
                                    fullName.setHint(fNameLabelStr);


                                }else if(jsonObject.getString("slug").equalsIgnoreCase("lname")){
//                                    lNameIdStr = jsonObject.getString("id");
                                    lNameLabelStr = jsonObject.getString("label_name");
//                                    lNameSlugStr = jsonObject.getString("slug");
//                                    lNameIsShowStr = jsonObject.getString("is_show");
//                                    lNameIsReqStr = jsonObject.getString("is_required");
//                                    lNameFieldTypeStr = jsonObject.getString("field_type");
//                                    lNameInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

//                                    if(lNameIsShowStr.equalsIgnoreCase("1")){
//                                        fullName.setHint(fNameLabelStr+" & "+lNameLabelStr);
//                                        lastNameTIL.setVisibility(View.VISIBLE);
//                                    }



                                }
                            else if(jsonObject.getString("slug").equalsIgnoreCase("gender")){
//                                    genderIdStr = jsonObject.getString("id");
                                    genderLabelStr = jsonObject.getString("label_name");
//                                    genderSlugStr = jsonObject.getString("slug");
//                                    genderIsShowStr = jsonObject.getString("is_show");
//                                    genderIsReqStr = jsonObject.getString("is_required");
//                                    genderFieldTypeStr = jsonObject.getString("field_type");
                                    genderInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");


                                  //  if(genderIsShowStr.equalsIgnoreCase("1")){
                                        String[] namesList = genderInputTypeMultFieldStr.split(",");
                                        gendername1 = namesList [0];
                                        gendername2 = namesList [1];

                                        sex.setText(genderLabelStr);
                                      //  genderLinear.setVisibility(View.VISIBLE);
                                        male.setText(gendername1);
                                        female.setText(gendername2);
                                    //}



                                }else if(jsonObject.getString("slug").equalsIgnoreCase("dob")){
//                                    dobIdStr = jsonObject.getString("id");
                                    dobLabelStr = jsonObject.getString("label_name");
//                                    dobSlugStr = jsonObject.getString("slug");
//                                    dobIsShowStr = jsonObject.getString("is_show");
//                                    dobIsReqStr = jsonObject.getString("is_required");
//                                    dobFieldTypeStr = jsonObject.getString("field_type");
//                                    dobInputTypeMultFieldStr = jsonObject.getString("input_type_multible_field");

                                    //if(dobIsShowStr.equalsIgnoreCase("1")){
                                       // if(isEditMode){
                                         //   dateOfBirth.setText(childToEdit.getDateOfBirth());
                                            //dateOfBirth.setVisibility(View.VISIBLE);
                                       // }else{
                                            dob.setText(dobLabelStr);
                                           // dateOfBirth.setVisibility(View.VISIBLE);
                                       // }

                                   // }



                                }



                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachUnpaidPlayersScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }


        }

    public void apiSessionDate(String finalDate){
        //sessionAttendedDate.setText(finalDate);
        if(Utilities.isNetworkAvailable(CoachUnpaidPlayersScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("session_attended", finalDate));
            nameValuePairList.add(new BasicNameValuePair("session_id", sessionId));

            String webServiceUrl = Utilities.BASE_URL + "coach/check_seat_ava";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachUnpaidPlayersScreen.this, nameValuePairList, SESSION_DATE, CoachUnpaidPlayersScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachUnpaidPlayersScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    public void apiAddUnpaidNoChildExistYesParentExists(){
        if(Utilities.isNetworkAvailable(CoachUnpaidPlayersScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("email", emailET.getText().toString().trim()));
            nameValuePairList.add(new BasicNameValuePair("email_exist", "1"));
            nameValuePairList.add(new BasicNameValuePair("child_name", fullNameET.getText().toString().trim()));
            nameValuePairList.add(new BasicNameValuePair("dob", dob.getText().toString().trim()));
            nameValuePairList.add(new BasicNameValuePair("gender", strGender));
            nameValuePairList.add(new BasicNameValuePair("session_attended", sessionDate));
            nameValuePairList.add(new BasicNameValuePair("session_id", sessionId));
            nameValuePairList.add(new BasicNameValuePair("users_id", usersID));
            String webServiceUrl = Utilities.BASE_URL + "coach/add_unpaid_player";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachUnpaidPlayersScreen.this, nameValuePairList, ADD_UNPAID, CoachUnpaidPlayersScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachUnpaidPlayersScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    public void apiAddUnpaidNoChildExistNoParentExists(String emailExits, String parentname,  String phoneNo, String phoneCode){
        if(Utilities.isNetworkAvailable(CoachUnpaidPlayersScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("email", emailET.getText().toString().trim()));
            nameValuePairList.add(new BasicNameValuePair("child_name", fullNameET.getText().toString().trim()));
            nameValuePairList.add(new BasicNameValuePair("dob", dob.getText().toString().trim()));
            nameValuePairList.add(new BasicNameValuePair("gender", strGender));
            nameValuePairList.add(new BasicNameValuePair("session_attended", sessionDate));
            nameValuePairList.add(new BasicNameValuePair("session_id", sessionId));
            nameValuePairList.add(new BasicNameValuePair("parent_name", parentname));
            nameValuePairList.add(new BasicNameValuePair("ph_code", phoneCode));
            nameValuePairList.add(new BasicNameValuePair("phone_1", phoneNo));

            String webServiceUrl = Utilities.BASE_URL + "coach/add_unpaid_player";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachUnpaidPlayersScreen.this, nameValuePairList, ADD_UNPAID, CoachUnpaidPlayersScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachUnpaidPlayersScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    public void apiAddUnpaidChildExist(){
        if(Utilities.isNetworkAvailable(CoachUnpaidPlayersScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("email", emailET.getText().toString().trim()));
            nameValuePairList.add(new BasicNameValuePair("email_exist", "1"));
            nameValuePairList.add(new BasicNameValuePair("check", "1"));
            nameValuePairList.add(new BasicNameValuePair("child_ids", child_ids));
            nameValuePairList.add(new BasicNameValuePair("session_attended", sessionDate));
            nameValuePairList.add(new BasicNameValuePair("session_id", sessionId));
            nameValuePairList.add(new BasicNameValuePair("users_id", usersID));

            String webServiceUrl = Utilities.BASE_URL + "coach/add_unpaid_player";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachUnpaidPlayersScreen.this, nameValuePairList, ADD_UNPAID, CoachUnpaidPlayersScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachUnpaidPlayersScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getRegForm() {
        if (Utilities.isNetworkAvailable(CoachUnpaidPlayersScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "children/get_child_reg_form";
            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(CoachUnpaidPlayersScreen.this, GET_REG, CoachUnpaidPlayersScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachUnpaidPlayersScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }
}