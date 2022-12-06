package com.dunvant.application.parent;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.ApplicationContext;
import com.dunvant.application.R;
import com.dunvant.application.beans.FacilityBean;
import com.dunvant.application.beans.PitchBean;
import com.dunvant.application.beans.PitchBookingSlotsDataBean;
import com.dunvant.application.beans.PitchDateBean;
import com.dunvant.application.beans.PitchTimeSlotBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentPitchDatesAdapter;
import com.dunvant.application.parent.adapters.ParentPitchNextDatesAdapter;
import com.dunvant.application.parent.adapters.ParentPitchTimeSlotsAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class ParentBookPitchScreen extends AppCompatActivity implements IWebServiceCallback {
    String day_start_num = "1";

    String monStr = "1";
    String tueStr = "2";
    String wedStr = "3";
    String thuStr = "4";
    String friStr = "5";
    String satStr = "6";
    String sunStr = "7";

    ApplicationContext applicationContext;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    TextView pitchName;
    //    TextView monFriCost;
//    TextView satCost;
//    TextView sunCost;
    TextView lblDaysType;

    TextView singleDay;
    TextView multiDays;
    String strSingleOrMulti;
    CheckBox fullPitch;
    CheckBox singlePitch;

    LinearLayout fromDateLinear;
    LinearLayout toDateLinear;
    static TextView fromDate;
    static TextView toDate;

    TextView lblRecurrenceType;
    TextView daily;
    TextView weekly;
    TextView monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    TextView lblTime;
    LinearLayout timeLinearLayout;

//    static TextView fromTime;
//    static TextView toTime;

    Spinner fromTimeSpinner;
    Spinner toTimeSpinner;

    Button continueButton;

    GridView pitchSlotsGridView;
    GridView nextAvailablePitchSlotsGridView;

    RelativeLayout recurrenceRelativeLayout;

    TextView weekdaysTextView;
    LinearLayout weekDaysLinearLayout;

    TextView txtAvailableSlots;
    TextView txtNextAvailableSlots;
    //TextView txtSelectDate;

    Button checkAvailability;

    FacilityBean clickedOnFacility;
    PitchBean clickedOnPitch;

    boolean isMonSelected, isTueSelected, isWedSelected;
    boolean isThuSelected, isFriSelected, isSatSelected;
    boolean isSunSelected;

    boolean isDaily;
    boolean isFullPitch;

    private final String GET_AVAILABLE_SLOTS_SINGLE = "GET_AVAILABLE_SLOTS_SINGLE";
    private final String GET_SUGGESTION_TIME_SLOTS = "GET_SUGGESTION_TIME_SLOTS";

    ArrayList<PitchDateBean> pitchDatesListing = new ArrayList<>();
    ArrayList<PitchDateBean> suggestionPitchDatesListing = new ArrayList<>();
    ArrayList<PitchDateBean> nextAvailableDatesListing = new ArrayList<>();


    private String suggestionStartDate = "", suggestionEndDate = "";
    ArrayList<PitchTimeSlotBean> uniqueTimeSlots = new ArrayList<>();

    PitchBookingSlotsDataBean selectedPitchBookingSlotsDataBean = new PitchBookingSlotsDataBean();
    ArrayList<PitchDateBean> selectedPitchDateBean = new ArrayList<>();

//    String fromTimeSlotsArray[] = {"From Time", "00:00 AM", "1:00 AM", "2:00 AM", "3:00 AM", "4:00 AM", "5:00 AM", "6:00 AM", "7:00 AM", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM", "9:00 PM", "10:00 PM", "11:00 PM"};
    String fromTimeSlotsArray[] = {"From Time", "12:00 AM", "1:00 AM", "2:00 AM", "3:00 AM", "4:00 AM", "5:00 AM", "6:00 AM", "7:00 AM", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM", "9:00 PM", "10:00 PM", "11:00 PM"};
//    String toTimeSlotsArray[] = {"To Time", "00:00 AM", "1:00 AM", "2:00 AM", "3:00 AM", "4:00 AM", "5:00 AM", "6:00 AM", "7:00 AM", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM", "9:00 PM", "10:00 PM", "11:00 PM"};
    String toTimeSlotsArray[] = {"To Time", "12:00 AM", "1:00 AM", "2:00 AM", "3:00 AM", "4:00 AM", "5:00 AM", "6:00 AM", "7:00 AM", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM", "9:00 PM", "10:00 PM", "11:00 PM"};
    String timeSlotsValues[] = {"", "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_book_pitch_screen);

        applicationContext = (ApplicationContext) getApplication();
        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        pitchName = (TextView) findViewById(R.id.pitchName);
//        monFriCost = (TextView) findViewById(R.id.monFriCost);
//        satCost = (TextView) findViewById(R.id.satCost);
//        sunCost = (TextView) findViewById(R.id.sunCost);
        lblDaysType = (TextView) findViewById(R.id.lblDaysType);

        singleDay = (TextView) findViewById(R.id.singleDay);
        multiDays = (TextView) findViewById(R.id.multiDays);
        fullPitch = (CheckBox) findViewById(R.id.fullPitch);
        singlePitch = (CheckBox) findViewById(R.id.singlePitch);

        fromDateLinear = (LinearLayout) findViewById(R.id.fromDateLinear);
        toDateLinear = (LinearLayout) findViewById(R.id.toDateLinear);
        fromDate = (TextView) findViewById(R.id.fromDate);
        toDate = (TextView) findViewById(R.id.toDate);

        lblRecurrenceType = (TextView) findViewById(R.id.lblRecurrenceType);

        daily = (TextView) findViewById(R.id.daily);
        weekly = (TextView) findViewById(R.id.weekly);

        monday = (TextView) findViewById(R.id.monday);
        tuesday = (TextView) findViewById(R.id.tuesday);
        wednesday = (TextView) findViewById(R.id.wednesday);
        thursday = (TextView) findViewById(R.id.thursday);
        friday = (TextView) findViewById(R.id.friday);
        saturday = (TextView) findViewById(R.id.saturday);
        sunday = (TextView) findViewById(R.id.sunday);

        lblTime = (TextView) findViewById(R.id.lblTime);
        timeLinearLayout = (LinearLayout) findViewById(R.id.timeLinearLayout);

//        fromTime = (TextView) findViewById(R.id.fromTime);
//        toTime = (TextView) findViewById(R.id.toTime);
        fromTimeSpinner = (Spinner) findViewById(R.id.fromTimeSpinner);
        toTimeSpinner = (Spinner) findViewById(R.id.toTimeSpinner);

        continueButton = (Button) findViewById(R.id.continueButton);
        recurrenceRelativeLayout = (RelativeLayout) findViewById(R.id.recurrenceRelativeLayout);
        weekdaysTextView = (TextView) findViewById(R.id.weekdaysTextView);
        weekDaysLinearLayout = (LinearLayout) findViewById(R.id.weekDaysLinearLayout);
        checkAvailability = (Button) findViewById(R.id.checkAvailability);
        pitchSlotsGridView = (GridView) findViewById(R.id.pitchSlotsGridView);
        nextAvailablePitchSlotsGridView = (GridView) findViewById(R.id.nextAvailablePitchSlotsGridView);

        txtAvailableSlots = (TextView) findViewById(R.id.txtAvailableSlots);
        txtNextAvailableSlots = (TextView) findViewById(R.id.txtNextAvailableSlots);

        txtAvailableSlots.setVisibility(View.GONE);
        txtNextAvailableSlots.setVisibility(View.GONE);

        //txtSelectDate = (TextView) findViewById(R.id.txtSelectDate);

        changeFonts();

         day_start_num = sharedPreferences.getString("day_start_num", null);

        if(day_start_num.trim().equalsIgnoreCase("1")){
            monday.setText("MON");
            tuesday.setText("TUE");
            wednesday.setText("WED");
            thursday.setText("THU");
            friday.setText("FRI");
            saturday.setText("SAT");
            sunday.setText("SUN");

            monStr = "1";
            tueStr = "2";
            wedStr = "3";
            thuStr = "4";
            friStr = "5";
            satStr = "6";
            sunStr = "7";

        }else if(day_start_num.trim().equalsIgnoreCase("2")){

            monday.setText("TUE");
            tuesday.setText("WED");
            wednesday.setText("THU");
            thursday.setText("FRI");
            friday.setText("SAT");
            saturday.setText("SUN");
            sunday.setText("MON");

            monStr = "2";
            tueStr = "3";
            wedStr = "4";
            thuStr = "5";
            friStr = "6";
            satStr = "7";
            sunStr = "1";

        }else if(day_start_num.trim().equalsIgnoreCase("3")){

            monday.setText("WED");
            tuesday.setText("THU");
            wednesday.setText("FRI");
            thursday.setText("SAT");
            friday.setText("SUN");
            saturday.setText("MON");
            sunday.setText("TUE");

            monStr = "3";
            tueStr = "4";
            wedStr = "5";
            thuStr = "6";
            friStr = "7";
            satStr = "1";
            sunStr = "2";

        }else if(day_start_num.trim().equalsIgnoreCase("4")){

            monday.setText("THU");
            tuesday.setText("FRI");
            wednesday.setText("SAT");
            thursday.setText("SUN");
            friday.setText("MON");
            saturday.setText("TUE");
            sunday.setText("WED");

            monStr = "4";
            tueStr = "5";
            wedStr = "6";
            thuStr = "7";
            friStr = "1";
            satStr = "2";
            sunStr = "3";

        }else if(day_start_num.trim().equalsIgnoreCase("5")){

            monday.setText("FRI");
            tuesday.setText("SAT");
            wednesday.setText("SUN");
            thursday.setText("MON");
            friday.setText("TUE");
            saturday.setText("WED");
            sunday.setText("THU");

            monStr = "5";
            tueStr = "6";
            wedStr = "7";
            thuStr = "1";
            friStr = "2";
            satStr = "3";
            sunStr = "4";

        }else if(day_start_num.trim().equalsIgnoreCase("6")){

            monday.setText("SAT");
            tuesday.setText("SUN");
            wednesday.setText("MON");
            thursday.setText("TUE");
            friday.setText("WED");
            saturday.setText("THU");
            sunday.setText("FRI");

            monStr = "6";
            tueStr = "7";
            wedStr = "1";
            thuStr = "2";
            friStr = "3";
            satStr = "4";
            sunStr = "5";

        }else if(day_start_num.trim().equalsIgnoreCase("7")){

            monday.setText("SUN");
            tuesday.setText("MON");
            wednesday.setText("TUE");
            thursday.setText("WED");
            friday.setText("THU");
            saturday.setText("FRI");
            sunday.setText("SAT");

            monStr = "7";
            tueStr = "1";
            wedStr = "2";
            thuStr = "3";
            friStr = "4";
            satStr = "5";
            sunStr = "6";
        }

        Intent intent = getIntent();
        if (intent != null) {
            clickedOnFacility = (FacilityBean) intent.getSerializableExtra("clickedOnFacility");

            pitchName.setText(clickedOnFacility.getLocationName());
            selectedPitchBookingSlotsDataBean.setLocationId(clickedOnFacility.getFacilityId());
            clickedOnPitch = (PitchBean) intent.getSerializableExtra("clickedOnPitch");

            /*
            pitchName.setText(clickedOnPitch.getPitchName());

            double weekDaysPrice = clickedOnPitch.getPricePerHourDailyBasis().get(0);
            double saturdayPrice = clickedOnPitch.getPricePerHourDailyBasis().get(4);
            double sundayPrice = clickedOnPitch.getPricePerHourDailyBasis().get(5);

            monFriCost.setText(weekDaysPrice+" AED Per Hour");
            satCost.setText(saturdayPrice+" AED Per Hour");
            sunCost.setText(sundayPrice+" AED Per Hour");

            selectedPitchBookingSlotsDataBean.setPitchId(clickedOnPitch.getPitchId());*/

            /*monFriCost.setText("SUN - THU "+weekDaysPrice+" AED Per Hour");
            satCost.setText("FRI "+saturdayPrice+" AED Per Hour");
            sunCost.setText("SAT "+sundayPrice+" AED Per Hour");*/

            /*if(weekDaysPrice == saturdayPrice && weekDaysPrice == sundayPrice) {
                monFriCost.setText("SUN - THU "+weekDaysPrice+" AED Per Hour");
                satCost.setVisibility(View.GONE);
                sunCost.setVisibility(View.GONE);
            } else if (weekDaysPrice == saturdayPrice) {
                monFriCost.setText("SUN - THU "+weekDaysPrice+" AED Per Hour");
                satCost.setVisibility(View.GONE);
                sunCost.setText("SAT "+sundayPrice+" AED Per Hour");
            } else {
                monFriCost.setText("SUN - THU "+weekDaysPrice+" AED Per Hour");
                satCost.setText("FRI "+saturdayPrice+" AED Per Hour");
                sunCost.setText("SAT "+sundayPrice+" AED Per Hour");
            }*/
        }

        fullPitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    singlePitch.setChecked(false);
                }else{
                    singlePitch.setChecked(true);
                }
            }
        });

        singlePitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    fullPitch.setChecked(false);
                }else{
                    fullPitch.setChecked(true);
                }
            }
        });

        checkAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedPitchDateBean.clear();
                //isFullPitch = fullPitch.isChecked();

                if(fullPitch.isChecked()==true) {
                    isFullPitch = true;
                } else if(singlePitch.isChecked()==true) {
                    isFullPitch = false;
                }

                if (strSingleOrMulti.equalsIgnoreCase("single")) {
                    String strFromDate = fromDate.getText().toString().trim();

                    if (strFromDate == null || strFromDate.isEmpty() || strFromDate.equalsIgnoreCase("Date")) {
                        Toast.makeText(ParentBookPitchScreen.this, "Please select Date", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            if(clickedOnPitch != null) {
                                jsonObject.put("pitch_id", clickedOnPitch.getPitchId());
                            } else {
                                jsonObject.put("pitch_id", "");
                            }

                            try{
                                jsonObject.put("parent_id", clickedOnPitch.getParentId());
                            }catch (Exception e){
                                e.printStackTrace();
                                jsonObject.put("parent_id", "");
                            }

                            jsonObject.put("location_id", clickedOnFacility.getFacilityId());
                            jsonObject.put("from_date", strFromDate);
                            jsonObject.put("to_date", "");
                            jsonObject.put("is_daily", "0");
                            jsonObject.put("weekdays", "[]");
                            jsonObject.put("from_time", "");
                            jsonObject.put("to_time", "");
                            jsonObject.put("is_multi", "0");
                            if (isFullPitch) {
                                jsonObject.put("is_full_pitch", "1");
                            } else {
                                jsonObject.put("is_full_pitch", "0");
                            }

                            jsonArray.put(jsonObject);

                            String strPitchDetails = jsonArray.toString();

                            // hit web service
                            if (Utilities.isNetworkAvailable(ParentBookPitchScreen.this)) {

                                List<NameValuePair> nameValuePairList = new ArrayList<>();
                                nameValuePairList.add(new BasicNameValuePair("pitch_details", strPitchDetails));

//                                String webServiceUrl = Utilities.BASE_URL + "pitch/get_available_slots";
                                String webServiceUrl = Utilities.BASE_URL + Utilities.getAvailabileSlotesSerivce;
                                ArrayList<String> headers = new ArrayList<>();
                                headers.add("X-access-uid:" + loggedInUser.getId());
                                headers.add("X-access-token:" + loggedInUser.getToken());

                                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookPitchScreen.this, nameValuePairList, GET_AVAILABLE_SLOTS_SINGLE, ParentBookPitchScreen.this, headers);
                                postWebServiceAsync.execute(webServiceUrl);

                            } else {
                                Toast.makeText(ParentBookPitchScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ParentBookPitchScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else if (strSingleOrMulti.equalsIgnoreCase("multi")) {
                    String strFromDate = fromDate.getText().toString().trim();
                    String strToDate = toDate.getText().toString().trim();

//                    String strFromTime = fromTime.getText().toString().trim();
//                    String strToTime = toTime.getText().toString().trim();

                    //System.out.println("fromTimeSlotsArray "+fromTimeSlotsArray.length);
                    //System.out.println("toTimeSlotsArray "+toTimeSlotsArray.length);
                    //System.out.println("timeSlotsValues "+timeSlotsValues.length);

                    int fromTimeSelectedPosition = fromTimeSpinner.getSelectedItemPosition();
                    String strFromTime = fromTimeSlotsArray[fromTimeSelectedPosition];

                    int toTimeSelectedPosition = toTimeSpinner.getSelectedItemPosition();
                    String strToTime = toTimeSlotsArray[toTimeSelectedPosition];


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    Date dtFromDate = null;
                    Date dtToDate = null;

                    try {
                        dtFromDate = sdf.parse(strFromDate);
                        dtToDate = sdf.parse(strToDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (strFromDate == null || strFromDate.isEmpty() || strFromDate.equalsIgnoreCase("From Date")) {
                        Toast.makeText(ParentBookPitchScreen.this, "Please select From Date", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (strToDate == null || strToDate.isEmpty() || strToDate.equalsIgnoreCase("To Date")) {
                        Toast.makeText(ParentBookPitchScreen.this, "Please select To Date", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (dtFromDate != null && dtToDate != null && dtToDate.before(dtFromDate)) {
                        Toast.makeText(ParentBookPitchScreen.this, "From Date should be before To Date", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (strFromTime == null || strFromTime.isEmpty() || strFromTime.equalsIgnoreCase("From Time")) {
                        Toast.makeText(ParentBookPitchScreen.this, "Please select From Time", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (strToTime == null || strToTime.isEmpty() || strToTime.equalsIgnoreCase("To Time")) {
                        Toast.makeText(ParentBookPitchScreen.this, "Please select To Time", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!isMonSelected && !isTueSelected && !isWedSelected && !isThuSelected && !isFriSelected && !isSatSelected && !isSunSelected) {
                        Toast.makeText(ParentBookPitchScreen.this, "Please select atleast one day", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonObject = new JSONObject();
                        try {

                            JSONArray weekdaysArray = new JSONArray();
                            if (isMonSelected) {
                                weekdaysArray.put(monStr);
                            }
                            if (isTueSelected) {
                                weekdaysArray.put(tueStr);
                            }
                            if (isWedSelected) {
                                weekdaysArray.put(wedStr);
                            }
                            if (isThuSelected) {
                                weekdaysArray.put(thuStr);
                            }
                            if (isFriSelected) {
                                weekdaysArray.put(friStr);
                            }
                            if (isSatSelected) {
                                weekdaysArray.put(satStr);
                            }
                            if (isSunSelected) {
                                weekdaysArray.put(sunStr);
                            }

                            if(clickedOnPitch != null) {
                                jsonObject.put("pitch_id", clickedOnPitch.getPitchId());
                            } else {
                                jsonObject.put("pitch_id", "");
                            }


                            jsonObject.put("parent_id", clickedOnPitch.getParentId());
                            jsonObject.put("location_id", clickedOnFacility.getFacilityId());
                            jsonObject.put("from_date", strFromDate);
                            jsonObject.put("to_date", strToDate);
                            if (isDaily) {
                                jsonObject.put("is_daily", "1");
                            } else {
                                jsonObject.put("is_daily", "0");
                            }
                            jsonObject.put("weekdays", weekdaysArray);
                            jsonObject.put("from_time", strFromTime);
                            jsonObject.put("to_time", strToTime);
                            jsonObject.put("is_multi", "1");
                            if (isFullPitch) {
                                jsonObject.put("is_full_pitch", "1");
                            } else {
                                jsonObject.put("is_full_pitch", "0");
                            }

                            jsonArray.put(jsonObject);

                            String strPitchDetails = jsonArray.toString();

                            // hit web service
                            if (Utilities.isNetworkAvailable(ParentBookPitchScreen.this)) {

                                List<NameValuePair> nameValuePairList = new ArrayList<>();
                                nameValuePairList.add(new BasicNameValuePair("pitch_details", strPitchDetails));

//                                String webServiceUrl = Utilities.BASE_URL + "pitch/get_available_slots";
                                String webServiceUrl = Utilities.BASE_URL + Utilities.getAvailabileSlotesSerivce;

                                ArrayList<String> headers = new ArrayList<>();
                                headers.add("X-access-uid:" + loggedInUser.getId());
                                headers.add("X-access-token:" + loggedInUser.getToken());

                                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookPitchScreen.this, nameValuePairList, GET_AVAILABLE_SLOTS_SINGLE, ParentBookPitchScreen.this, headers);
                                postWebServiceAsync.execute(webServiceUrl);

                            } else {
                                Toast.makeText(ParentBookPitchScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ParentBookPitchScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pitchDatesListing.isEmpty() && nextAvailableDatesListing.isEmpty()) {
                    Toast.makeText(ParentBookPitchScreen.this, "Please select date/time slot", Toast.LENGTH_SHORT).show();
                    return;
                }

                selectedPitchBookingSlotsDataBean.setFullPitch(isFullPitch);


                if (strSingleOrMulti.equalsIgnoreCase("single")) {

                    if (!pitchDatesListing.isEmpty()) {
                        PitchDateBean pitchDateBean = pitchDatesListing.get(0);

                        boolean atLeastOneSelected = false;

                        for (PitchTimeSlotBean pitchTimeSlotBean : pitchDateBean.getTimeSlots()) {
                            if (pitchTimeSlotBean.isSelected()) {
                                atLeastOneSelected = true;
                                break;
                            }
                        }

                        if (!atLeastOneSelected) {
                            Toast.makeText(ParentBookPitchScreen.this, "Please choose at least one time slot", Toast.LENGTH_SHORT).show();
                        } else {
                            // Add to array list
                            selectedPitchDateBean.add(pitchDateBean);

                            // hit web service for suggestions
                            JSONArray timeSlotsArray = new JSONArray();
                            for (PitchTimeSlotBean pitchTimeSlotBean : pitchDateBean.getTimeSlots()) {
                                if (pitchTimeSlotBean.isSelected()) {
                                    timeSlotsArray.put(pitchTimeSlotBean.getTimeSlot());
//                                    break;
                                }
                            }

                            if (Utilities.isNetworkAvailable(ParentBookPitchScreen.this)) {

                                List<NameValuePair> nameValuePairList = new ArrayList<>();

                                if(clickedOnPitch != null) {
                                    nameValuePairList.add(new BasicNameValuePair("pitch_id", clickedOnPitch.getPitchId()));
                                } else {
                                    nameValuePairList.add(new BasicNameValuePair("pitch_id", ""));
                                }

                                nameValuePairList.add(new BasicNameValuePair("location_id", clickedOnFacility.getFacilityId()));
                                nameValuePairList.add(new BasicNameValuePair("selected_date", pitchDateBean.getDate()));
                                nameValuePairList.add(new BasicNameValuePair("selected_time", timeSlotsArray.toString()));
                                if (isFullPitch) {
                                    nameValuePairList.add(new BasicNameValuePair("is_full_pitch", "1"));
                                } else {
                                    nameValuePairList.add(new BasicNameValuePair("is_full_pitch", "0"));
                                }

                                String webServiceUrl = Utilities.BASE_URL + "pitch/get_suggestion_slots";

                                ArrayList<String> headers = new ArrayList<>();
                                headers.add("X-access-uid:" + loggedInUser.getId());
                                headers.add("X-access-token:" + loggedInUser.getToken());

                                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookPitchScreen.this, nameValuePairList, GET_SUGGESTION_TIME_SLOTS, ParentBookPitchScreen.this, headers);
                                postWebServiceAsync.execute(webServiceUrl);

                            } else {
                                Toast.makeText(ParentBookPitchScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {

                        boolean atLeastOneSelected = false;

                        for (PitchDateBean pDateBean : nextAvailableDatesListing) {
                            if (pDateBean.isSelected()) {
                                atLeastOneSelected = true;
                                break;
                            }
                        }

                        if (!atLeastOneSelected) {
                            Toast.makeText(ParentBookPitchScreen.this, "Please choose at least one date slot", Toast.LENGTH_SHORT).show();
                        } else {
                            for (PitchDateBean pDateBean : nextAvailableDatesListing) {
                                if (pDateBean.isSelected()) {
                                    for (PitchTimeSlotBean timeSlotBean : pDateBean.getTimeSlots()) {
                                        timeSlotBean.setSelected(true);
                                    }
                                    selectedPitchDateBean.add(pDateBean);
                                }
                            }

                            selectedPitchBookingSlotsDataBean.setPitchDateBeenList(selectedPitchDateBean);

                            // Add this object to application context's array list
                            int alreadyExistsAt = -1;
                            for (int i = 0; i < applicationContext.getPitchBookingSlotsDataBeanListing().size(); i++) {
                                PitchBookingSlotsDataBean currentBookingSlot = applicationContext.getPitchBookingSlotsDataBeanListing().get(i);
//                              if(currentBookingSlot.getPitchId().equalsIgnoreCase(selectedPitchBookingSlotsDataBean.getPitchId())) {
                                if (currentBookingSlot.getLocationId().equalsIgnoreCase(selectedPitchBookingSlotsDataBean.getLocationId())) {
                                    alreadyExistsAt = i;
                                    break;
                                }
                            }

                            // Saving data to Application Context
                            if (alreadyExistsAt == -1) {
                                //System.out.println("does not exist, adding new");
                                applicationContext.getPitchBookingSlotsDataBeanListing().add(selectedPitchBookingSlotsDataBean);
                            } else {
                                //System.out.println("already exists, replacing");
                                applicationContext.getPitchBookingSlotsDataBeanListing().set(alreadyExistsAt, selectedPitchBookingSlotsDataBean);
                            }

                            Intent summaryScreen = new Intent(ParentBookPitchScreen.this, ParentBookPitchSummaryScreen.class);
                            startActivity(summaryScreen);
                        }
                    }
                } else if (strSingleOrMulti.equalsIgnoreCase("multi")) {

                    boolean atLeastOneSelected = false;

                    for (PitchDateBean pitchDateBean : pitchDatesListing) {
                        if (pitchDateBean.isSelected()) {
                            atLeastOneSelected = true;
                            break;
                        }
                    }

                    for (PitchDateBean pitchDateBean : nextAvailableDatesListing) {
                        if (pitchDateBean.isSelected()) {
                            atLeastOneSelected = true;
                            break;
                        }
                    }

                    if (!atLeastOneSelected) {
                        Toast.makeText(ParentBookPitchScreen.this, "Please choose at least one date slot", Toast.LENGTH_SHORT).show();
                    } else {
                        // Move to next screen

                        for (PitchDateBean pitchDateBean : pitchDatesListing) {
                            if (pitchDateBean.isSelected()) {
                                for (PitchTimeSlotBean timeSlotBean : pitchDateBean.getTimeSlots()) {
                                    timeSlotBean.setSelected(true);
                                }
                                selectedPitchDateBean.add(pitchDateBean);
                            }
                        }

                        for (PitchDateBean pitchDateBean : nextAvailableDatesListing) {
                            if (pitchDateBean.isSelected()) {
                                for (PitchTimeSlotBean timeSlotBean : pitchDateBean.getTimeSlots()) {
                                    timeSlotBean.setSelected(true);
                                }
                                selectedPitchDateBean.add(pitchDateBean);
                            }
                        }

                        selectedPitchBookingSlotsDataBean.setPitchDateBeenList(selectedPitchDateBean);

                        // Add this object to application context's array list

                        int alreadyExistsAt = -1;

                        for (int i = 0; i < applicationContext.getPitchBookingSlotsDataBeanListing().size(); i++) {
                            PitchBookingSlotsDataBean currentBookingSlot = applicationContext.getPitchBookingSlotsDataBeanListing().get(i);
//                            if(currentBookingSlot.getPitchId().equalsIgnoreCase(selectedPitchBookingSlotsDataBean.getPitchId())) {
                            if (currentBookingSlot.getLocationId().equalsIgnoreCase(selectedPitchBookingSlotsDataBean.getLocationId())) {
                                alreadyExistsAt = i;
                                break;
                            }
                        }

                        // Saving data to Application Context
                        if (alreadyExistsAt == -1) {
                            //System.out.println("does not exist, adding new");
                            applicationContext.getPitchBookingSlotsDataBeanListing().add(selectedPitchBookingSlotsDataBean);
                        } else {
                            //System.out.println("already exists, replacing");
                            applicationContext.getPitchBookingSlotsDataBeanListing().set(alreadyExistsAt, selectedPitchBookingSlotsDataBean);
                        }

                        Intent summaryScreen = new Intent(ParentBookPitchScreen.this, ParentBookPitchSummaryScreen.class);
                        startActivity(summaryScreen);
                    }

                }
            }
        });

        singleDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleDay.setBackgroundColor(getResources().getColor(R.color.blue));
                singleDay.setTextColor(getResources().getColor(R.color.white));
                multiDays.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                multiDays.setTextColor(getResources().getColor(R.color.black));

                strSingleOrMulti = "single";

                pitchSlotsGridView.setVisibility(View.GONE);

                fromDate.setText("Date");

//                toDate.setVisibility(View.GONE);
                toDateLinear.setVisibility(View.GONE);
                lblTime.setVisibility(View.GONE);
                timeLinearLayout.setVisibility(View.GONE);
                recurrenceRelativeLayout.setVisibility(View.GONE);
                weekdaysTextView.setVisibility(View.GONE);
                weekDaysLinearLayout.setVisibility(View.GONE);
                txtAvailableSlots.setVisibility(View.GONE);
                txtNextAvailableSlots.setVisibility(View.GONE);
            }
        });

        multiDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiDays.setBackgroundColor(getResources().getColor(R.color.blue));
                multiDays.setTextColor(getResources().getColor(R.color.white));
                singleDay.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                singleDay.setTextColor(getResources().getColor(R.color.black));

                strSingleOrMulti = "multi";

                fromDate.setText("From Date");

                pitchSlotsGridView.setVisibility(View.GONE);

//                toDate.setVisibility(View.VISIBLE);
                toDateLinear.setVisibility(View.VISIBLE);
                lblTime.setVisibility(View.VISIBLE);
                timeLinearLayout.setVisibility(View.VISIBLE);
                recurrenceRelativeLayout.setVisibility(View.VISIBLE);
                weekdaysTextView.setVisibility(View.VISIBLE);
                weekDaysLinearLayout.setVisibility(View.VISIBLE);
                txtAvailableSlots.setVisibility(View.GONE);
                txtNextAvailableSlots.setVisibility(View.GONE);
                weekly.performClick();
            }
        });

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment2();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        /*fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment2();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });*/

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(this, R.layout.parent_adapter_time_slot_white, fromTimeSlotsArray);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromTimeSpinner.setAdapter(fromAdapter);

        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(this, R.layout.parent_adapter_time_slot_white, toTimeSlotsArray);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toTimeSpinner.setAdapter(toAdapter);

        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily.setBackgroundColor(getResources().getColor(R.color.blue));
                daily.setTextColor(getResources().getColor(R.color.white));
                weekly.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                weekly.setTextColor(getResources().getColor(R.color.black));

                isMonSelected = isTueSelected = isWedSelected = isThuSelected = isFriSelected = isSatSelected = isSunSelected = true;
                showDaysSelection();
            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekly.setBackgroundColor(getResources().getColor(R.color.blue));
                weekly.setTextColor(getResources().getColor(R.color.white));
                daily.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                daily.setTextColor(getResources().getColor(R.color.black));
            }
        });

        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMonSelected = !isMonSelected;
                showDaysSelection();
            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTueSelected = !isTueSelected;
                showDaysSelection();
            }
        });

        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWedSelected = !isWedSelected;
                showDaysSelection();
            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isThuSelected = !isThuSelected;
                showDaysSelection();
            }
        });

        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFriSelected = !isFriSelected;
                showDaysSelection();
            }
        });

        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSatSelected = !isSatSelected;
                showDaysSelection();
            }
        });

        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSunSelected = !isSunSelected;
                showDaysSelection();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        multiDays.performClick();
        singleDay.performClick();
    }

    public void changePitchDate(PitchDateBean pitchDateBean) {
        fromDate.setText(pitchDateBean.getDate());
        checkAvailability.performClick();
    }

    private void showDaysSelection() {
        if (isMonSelected) {
            monday.setBackgroundColor(getResources().getColor(R.color.blue));
            monday.setTextColor(getResources().getColor(R.color.white));
        } else {
            monday.setBackgroundColor(getResources().getColor(R.color.white));
            monday.setTextColor(getResources().getColor(R.color.black));

            weekly.setBackgroundColor(getResources().getColor(R.color.blue));
            weekly.setTextColor(getResources().getColor(R.color.white));
            daily.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            daily.setTextColor(getResources().getColor(R.color.black));
        }

        if (isTueSelected) {
            tuesday.setBackgroundColor(getResources().getColor(R.color.blue));
            tuesday.setTextColor(getResources().getColor(R.color.white));
        } else {
            tuesday.setBackgroundColor(getResources().getColor(R.color.white));
            tuesday.setTextColor(getResources().getColor(R.color.black));

            weekly.setBackgroundColor(getResources().getColor(R.color.blue));
            weekly.setTextColor(getResources().getColor(R.color.white));
            daily.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            daily.setTextColor(getResources().getColor(R.color.black));
        }

        if (isWedSelected) {
            wednesday.setBackgroundColor(getResources().getColor(R.color.blue));
            wednesday.setTextColor(getResources().getColor(R.color.white));
        } else {
            wednesday.setBackgroundColor(getResources().getColor(R.color.white));
            wednesday.setTextColor(getResources().getColor(R.color.black));

            weekly.setBackgroundColor(getResources().getColor(R.color.blue));
            weekly.setTextColor(getResources().getColor(R.color.white));
            daily.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            daily.setTextColor(getResources().getColor(R.color.black));
        }

        if (isThuSelected) {
            thursday.setBackgroundColor(getResources().getColor(R.color.blue));
            thursday.setTextColor(getResources().getColor(R.color.white));
        } else {
            thursday.setBackgroundColor(getResources().getColor(R.color.white));
            thursday.setTextColor(getResources().getColor(R.color.black));

            weekly.setBackgroundColor(getResources().getColor(R.color.blue));
            weekly.setTextColor(getResources().getColor(R.color.white));
            daily.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            daily.setTextColor(getResources().getColor(R.color.black));
        }

        if (isFriSelected) {
            friday.setBackgroundColor(getResources().getColor(R.color.blue));
            friday.setTextColor(getResources().getColor(R.color.white));
        } else {
            friday.setBackgroundColor(getResources().getColor(R.color.white));
            friday.setTextColor(getResources().getColor(R.color.black));

            weekly.setBackgroundColor(getResources().getColor(R.color.blue));
            weekly.setTextColor(getResources().getColor(R.color.white));
            daily.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            daily.setTextColor(getResources().getColor(R.color.black));
        }

        if (isSatSelected) {
            saturday.setBackgroundColor(getResources().getColor(R.color.blue));
            saturday.setTextColor(getResources().getColor(R.color.white));
        } else {
            saturday.setBackgroundColor(getResources().getColor(R.color.white));
            saturday.setTextColor(getResources().getColor(R.color.black));

            weekly.setBackgroundColor(getResources().getColor(R.color.blue));
            weekly.setTextColor(getResources().getColor(R.color.white));
            daily.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            daily.setTextColor(getResources().getColor(R.color.black));
        }

        if (isSunSelected) {
            sunday.setBackgroundColor(getResources().getColor(R.color.blue));
            sunday.setTextColor(getResources().getColor(R.color.white));
        } else {
            sunday.setBackgroundColor(getResources().getColor(R.color.white));
            sunday.setTextColor(getResources().getColor(R.color.black));

            weekly.setBackgroundColor(getResources().getColor(R.color.blue));
            weekly.setTextColor(getResources().getColor(R.color.white));
            daily.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            daily.setTextColor(getResources().getColor(R.color.black));
        }

        if (isMonSelected && isTueSelected && isWedSelected && isThuSelected && isFriSelected && isSatSelected && isSunSelected) {

            isDaily = true;

            daily.setBackgroundColor(getResources().getColor(R.color.blue));
            daily.setTextColor(getResources().getColor(R.color.white));
            weekly.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            weekly.setTextColor(getResources().getColor(R.color.black));
        } else {
            isDaily = false;
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_AVAILABLE_SLOTS_SINGLE:

                pitchDatesListing.clear();
                uniqueTimeSlots.clear();
                nextAvailableDatesListing.clear();

                if (response == null) {
                    Toast.makeText(ParentBookPitchScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");
                            PitchDateBean pitchDateBean;

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject pitchDateObject = dataArray.getJSONObject(i);
                                pitchDateBean = new PitchDateBean();

                                pitchDateBean.setDate(pitchDateObject.getString("date"));

                                ArrayList<PitchTimeSlotBean> timeSlotsListing = new ArrayList<>();
                                PitchTimeSlotBean timeSlotBean;
                                JSONArray timeSlotsArray = pitchDateObject.getJSONArray("time_slots");
                                for (int j = 0; j < timeSlotsArray.length(); j++) {

                                    JSONObject timeSlotObject = timeSlotsArray.getJSONObject(j);
//                                    String timeSlot = timeSlotsArray.getString(j);

                                    String pitchId = timeSlotObject.getString("pitch_id");
                                    JSONArray slotsArray = timeSlotObject.getJSONArray("slots");

                                    for (int k = 0; k < slotsArray.length(); k++) {

                                        timeSlotBean = new PitchTimeSlotBean();
                                        timeSlotBean.setTimeSlot(slotsArray.getString(k));
                                        timeSlotBean.setPitchId(pitchId);
//                                        timeSlotBean.setTimeSlot(timeSlot);

                                        timeSlotsListing.add(timeSlotBean);
                                    }
                                }
                                pitchDateBean.setTimeSlots(timeSlotsListing);

                                if (timeSlotsArray.length() != 0) {
                                    pitchDatesListing.add(pitchDateBean);
                                }
                            }

                            JSONArray noTSlotAvailableDates = responseObject.getJSONArray("no_tslot_avail_dates");
                            for (int i = 0; i < noTSlotAvailableDates.length(); i++) {
                                JSONObject pitchDateObject = noTSlotAvailableDates.getJSONObject(i);
                                pitchDateBean = new PitchDateBean();

                                pitchDateBean.setDate(pitchDateObject.getString("date"));

                                ArrayList<PitchTimeSlotBean> timeSlotsListing = new ArrayList<>();
                                PitchTimeSlotBean timeSlotBean;
                                JSONArray timeSlotsArray = pitchDateObject.getJSONArray("time_slots");
                                for (int j = 0; j < timeSlotsArray.length(); j++) {

                                    JSONObject timeSlotObject = timeSlotsArray.getJSONObject(j);
//                                    String timeSlot = timeSlotsArray.getString(j);

                                    String pitchId = timeSlotObject.getString("pitch_id");
                                    JSONArray slotsArray = timeSlotObject.getJSONArray("slots");

                                    for (int k = 0; k < slotsArray.length(); k++) {

                                        timeSlotBean = new PitchTimeSlotBean();
                                        timeSlotBean.setTimeSlot(slotsArray.getString(k));
                                        timeSlotBean.setPitchId(pitchId);
//                                        timeSlotBean.setTimeSlot(timeSlot);

                                        timeSlotsListing.add(timeSlotBean);
                                    }
                                }
                                pitchDateBean.setTimeSlots(timeSlotsListing);

                                if (timeSlotsArray.length() != 0) {
                                    nextAvailableDatesListing.add(pitchDateBean);
                                }
                            }

                            if (strSingleOrMulti.equalsIgnoreCase("single")) {
                                if (pitchDatesListing.size() != 0) {

                                    // Find unique Time Slots

                                    for (int i = 0; i < pitchDatesListing.get(0).getTimeSlots().size(); i++) {

                                        PitchTimeSlotBean currentTimeSlotBean = pitchDatesListing.get(0).getTimeSlots().get(i);

                                        boolean alreadyExists = false;

                                        for (PitchTimeSlotBean pitchTimeSlotBean : uniqueTimeSlots) {
                                            if (currentTimeSlotBean.getTimeSlot().equalsIgnoreCase(pitchTimeSlotBean.getTimeSlot())) {
                                                alreadyExists = true;
                                                break;
                                            }
                                        }

                                        if (!alreadyExists) {
                                            uniqueTimeSlots.add(currentTimeSlotBean);
                                        }
                                    }

//                                    pitchSlotsGridView.setAdapter(new ParentPitchTimeSlotsAdapter(ParentBookPitchScreen.this, pitchDatesListing.get(0).getTimeSlots()));

                                    pitchSlotsGridView.setAdapter(new ParentPitchTimeSlotsAdapter(ParentBookPitchScreen.this, uniqueTimeSlots, ParentBookPitchScreen.this));
                                    Utilities.setGridViewHeightBasedOnChildren(pitchSlotsGridView, 3);
                                    pitchSlotsGridView.setVisibility(View.VISIBLE);

                                    txtAvailableSlots.setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(ParentBookPitchScreen.this, "No slots available", Toast.LENGTH_SHORT).show();
                                    pitchSlotsGridView.setVisibility(View.GONE);
                                    txtAvailableSlots.setVisibility(View.GONE);
                                }

                                if (nextAvailableDatesListing.size() != 0) {
                                    txtNextAvailableSlots.setVisibility(View.VISIBLE);

                                    // new change - Gene
//                                    nextAvailablePitchSlotsGridView.setAdapter(new ParentPitchDatesAdapter(ParentBookPitchScreen.this, nextAvailableDatesListing, false));
                                    nextAvailablePitchSlotsGridView.setAdapter(new ParentPitchNextDatesAdapter(ParentBookPitchScreen.this, nextAvailableDatesListing, false, ParentBookPitchScreen.this));
                                    Utilities.setGridViewHeightBasedOnChildren(nextAvailablePitchSlotsGridView, 3);
                                } else {
                                    txtNextAvailableSlots.setVisibility(View.GONE);

                                    nextAvailablePitchSlotsGridView.setAdapter(null);
                                    Utilities.setGridViewHeightBasedOnChildren(nextAvailablePitchSlotsGridView, 3);
                                }

                            } else if (strSingleOrMulti.equalsIgnoreCase("multi")) {
                                if (pitchDatesListing.size() != 0) {
                                    txtAvailableSlots.setVisibility(View.VISIBLE);

//                                    pitchSlotsGridView.setAdapter(new ParentPitchDatesAdapter(ParentBookPitchScreen.this, pitchDatesListing, true));
                                    pitchSlotsGridView.setAdapter(new ParentPitchDatesAdapter(ParentBookPitchScreen.this, pitchDatesListing, false));
                                    Utilities.setGridViewHeightBasedOnChildren(pitchSlotsGridView, 3);
                                    pitchSlotsGridView.setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(ParentBookPitchScreen.this, "No slots available", Toast.LENGTH_SHORT).show();
                                    pitchSlotsGridView.setVisibility(View.GONE);
                                    txtAvailableSlots.setVisibility(View.GONE);
                                }

                                if (nextAvailableDatesListing.size() != 0) {
                                    txtNextAvailableSlots.setVisibility(View.VISIBLE);
//                                    nextAvailablePitchSlotsGridView.setAdapter(new ParentPitchDatesAdapter(ParentBookPitchScreen.this, nextAvailableDatesListing, true));

                                    // new change - Gene
//                                    nextAvailablePitchSlotsGridView.setAdapter(new ParentPitchDatesAdapter(ParentBookPitchScreen.this, nextAvailableDatesListing, false));
                                    nextAvailablePitchSlotsGridView.setAdapter(new ParentPitchNextDatesAdapter(ParentBookPitchScreen.this, nextAvailableDatesListing, false, ParentBookPitchScreen.this));
                                    Utilities.setGridViewHeightBasedOnChildren(nextAvailablePitchSlotsGridView, 3);
                                } else {
                                    txtNextAvailableSlots.setVisibility(View.GONE);
                                    nextAvailablePitchSlotsGridView.setAdapter(null);
                                    Utilities.setGridViewHeightBasedOnChildren(nextAvailablePitchSlotsGridView, 3);
                                }


                            }

                        } else {
                            Toast.makeText(ParentBookPitchScreen.this, message, Toast.LENGTH_SHORT).show();

                            txtAvailableSlots.setVisibility(View.GONE);
                            txtNextAvailableSlots.setVisibility(View.GONE);

                            pitchSlotsGridView.setAdapter(null);
                            Utilities.setGridViewHeightBasedOnChildren(pitchSlotsGridView, 3);
                            pitchSlotsGridView.setVisibility(View.VISIBLE);

                            nextAvailablePitchSlotsGridView.setAdapter(null);
                            Utilities.setGridViewHeightBasedOnChildren(nextAvailablePitchSlotsGridView, 3);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookPitchScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case GET_SUGGESTION_TIME_SLOTS:

                suggestionPitchDatesListing.clear();

                if (response == null) {
                    Toast.makeText(ParentBookPitchScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            PitchDateBean pitchDateBean;

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject pitchDateObject = dataArray.getJSONObject(i);
                                pitchDateBean = new PitchDateBean();

                                pitchDateBean.setDate(pitchDateObject.getString("date"));

                                ArrayList<PitchTimeSlotBean> timeSlotsListing = new ArrayList<>();
                                PitchTimeSlotBean timeSlotBean;
                                JSONArray timeSlotsArray = pitchDateObject.getJSONArray("time_slots");
                                for (int j = 0; j < timeSlotsArray.length(); j++) {


                                    JSONObject timeSlotObject = timeSlotsArray.getJSONObject(j);
//                                    String timeSlot = timeSlotsArray.getString(j);

                                    String pitchId = timeSlotObject.getString("pitch_id");
                                    JSONArray slotsArray = timeSlotObject.getJSONArray("slots");

                                    for (int k = 0; k < slotsArray.length(); k++) {

                                        timeSlotBean = new PitchTimeSlotBean();
                                        timeSlotBean.setTimeSlot(slotsArray.getString(k));
                                        timeSlotBean.setPitchId(pitchId);
//                                        timeSlotBean.setTimeSlot(timeSlot);

                                        timeSlotsListing.add(timeSlotBean);
                                    }
                                }
                                pitchDateBean.setTimeSlots(timeSlotsListing);

                                suggestionPitchDatesListing.add(pitchDateBean);
                            }

//                            suggestionStartDate = responseObject.getString("state_date");
                            suggestionStartDate = responseObject.getString("start_date");
                            suggestionEndDate = responseObject.getString("end_date");

                        } else {
                            Toast.makeText(ParentBookPitchScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookPitchScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                showSuggestionDialog();
                break;
        }
    }

    public void changeTimeSlotInAllPitches(PitchTimeSlotBean pitchTimeSlotBean) {
        for (PitchTimeSlotBean currentTimeSlot : pitchDatesListing.get(0).getTimeSlots()) {
            if (currentTimeSlot.getTimeSlot().equalsIgnoreCase(pitchTimeSlotBean.getTimeSlot())) {
                currentTimeSlot.setSelected(pitchTimeSlotBean.isSelected());
            }
        }
    }

    private void showSuggestionDialog() {
        final Dialog dialog = new Dialog(ParentBookPitchScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.parent_dialog_pitch_suggestion);

        TextView message = (TextView) dialog.findViewById(R.id.message);
        GridView suggestionGridView = (GridView) dialog.findViewById(R.id.suggestionGridView);
        Button continueButton = (Button) dialog.findViewById(R.id.continueButton);

        message.setText("Selected slot is also available for coming weeks from " + suggestionStartDate + " to " + suggestionEndDate);
        suggestionGridView.setAdapter(new ParentPitchDatesAdapter(ParentBookPitchScreen.this, suggestionPitchDatesListing, false));

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (PitchDateBean pitchDateBean : suggestionPitchDatesListing) {
                    if (pitchDateBean.isSelected()) {
                        for (PitchTimeSlotBean timeSlotBean : pitchDateBean.getTimeSlots()) {
                            timeSlotBean.setSelected(true);
                        }
                        selectedPitchDateBean.add(pitchDateBean);
                    }
                }

                selectedPitchBookingSlotsDataBean.setPitchDateBeenList(selectedPitchDateBean);

                // Add this object to application context's array list
                int alreadyExistsAt = -1;
                for (int i = 0; i < applicationContext.getPitchBookingSlotsDataBeanListing().size(); i++) {
                    PitchBookingSlotsDataBean currentBookingSlot = applicationContext.getPitchBookingSlotsDataBeanListing().get(i);
//                    if(currentBookingSlot.getPitchId().equalsIgnoreCase(selectedPitchBookingSlotsDataBean.getPitchId())) {
                    if (currentBookingSlot.getLocationId().equalsIgnoreCase(selectedPitchBookingSlotsDataBean.getLocationId())) {
                        alreadyExistsAt = i;
                        break;
                    }
                }

                // Saving data to Application Context
                if (alreadyExistsAt == -1) {
                    //System.out.println("does not exist, adding new");
                    applicationContext.getPitchBookingSlotsDataBeanListing().add(selectedPitchBookingSlotsDataBean);
                } else {
                    //System.out.println("already exists, replacing");
                    applicationContext.getPitchBookingSlotsDataBeanListing().set(alreadyExistsAt, selectedPitchBookingSlotsDataBean);
                }

                Intent summaryScreen = new Intent(ParentBookPitchScreen.this, ParentBookPitchSummaryScreen.class);
                startActivity(summaryScreen);
            }
        });

        if(suggestionPitchDatesListing.isEmpty()){
            continueButton.performClick();
        }

        dialog.show();
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
            DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return pickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month += 1;

            String strMonth = (month < 10) ? "0" + month : month + "";
            String strDay = (day < 10) ? "0" + day : day + "";

            fromDate.setText(year + "-" + strMonth + "-" + strDay);
        }
    }

    public static class DatePickerFragment2 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return pickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month += 1;

            String strMonth = (month < 10) ? "0" + month : month + "";
            String strDay = (day < 10) ? "0" + day : day + "";

            toDate.setText(year + "-" + strMonth + "-" + strDay);
        }
    }

//    public static class TimePickerFragment extends DialogFragment
//            implements TimePickerDialog.OnTimeSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current time as the default values for the picker
//            final Calendar c = Calendar.getInstance();
//            int hour = c.get(Calendar.HOUR_OF_DAY);
//            int minute = c.get(Calendar.MINUTE);
//
//            // Create a new instance of TimePickerDialog and return it
//            return new TimePickerDialog(getActivity(), this, hour, minute,
//                    DateFormat.is24HourFormat(getActivity()));
//        }
//
//        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            // Do something with the time chosen by the user
//
//            String strHour = (hourOfDay < 10) ? "0" + hourOfDay : hourOfDay + "";
//            String strMin = (minute < 10) ? "0" + minute : minute + "";
//
//            fromTime.setText(strHour + ":" + strMin + ":00");
//        }
//    }
//
//    public static class TimePickerFragment2 extends DialogFragment
//            implements TimePickerDialog.OnTimeSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current time as the default values for the picker
//            final Calendar c = Calendar.getInstance();
//            int hour = c.get(Calendar.HOUR_OF_DAY);
//            int minute = c.get(Calendar.MINUTE);
//
//            // Create a new instance of TimePickerDialog and return it
//            return new TimePickerDialog(getActivity(), this, hour, minute,
//                    DateFormat.is24HourFormat(getActivity()));
//        }
//
//        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            // Do something with the time chosen by the user
//            String strHour = (hourOfDay < 10) ? "0" + hourOfDay : hourOfDay + "";
//            String strMin = (minute < 10) ? "0" + minute : minute + "";
//
//            toTime.setText(strHour + ":" + strMin + ":00");
//        }
//    }

    private void changeFonts() {
        title.setTypeface(linoType);
        pitchName.setTypeface(helvetica);
//        monFriCost.setTypeface(helvetica);
//        satCost.setTypeface(helvetica);
//        sunCost.setTypeface(helvetica);
        lblDaysType.setTypeface(helvetica);
        singleDay.setTypeface(helvetica);
        multiDays.setTypeface(helvetica);
        fromDate.setTypeface(helvetica);
        toDate.setTypeface(helvetica);
        lblRecurrenceType.setTypeface(helvetica);
        daily.setTypeface(helvetica);
        weekly.setTypeface(helvetica);
        monday.setTypeface(helvetica);
        tuesday.setTypeface(helvetica);
        wednesday.setTypeface(helvetica);
        thursday.setTypeface(helvetica);
        friday.setTypeface(helvetica);
        saturday.setTypeface(helvetica);
        sunday.setTypeface(helvetica);
        lblTime.setTypeface(helvetica);
//        fromTime.setTypeface(helvetica);
//        toTime.setTypeface(helvetica);
        continueButton.setTypeface(linoType);
        weekdaysTextView.setTypeface(helvetica);
        txtAvailableSlots.setTypeface(helvetica);
        txtNextAvailableSlots.setTypeface(helvetica);
        checkAvailability.setTypeface(linoType);
      //  txtSelectDate.setTypeface(helvetica);
    }
}