package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CampBean;
import com.dunvant.application.beans.CampDateBean;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentAvailableDatesListingAdapter;
import com.dunvant.application.parent.adapters.ParentChildrenSpinnerAdapter;
import com.dunvant.application.parent.paymentGatewayUtilities.AvenuesParams;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class ParentBookCampScreenOLD extends AppCompatActivity implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView campName;
    TextView timings;
    TextView age;
    Spinner childrenSpinner;
    CheckBox selectAllCheckbox;
    GridView availableDatesGridView;
    TextInputLayout commentsTextInputLayout;
    TextView comments;
    TextView lblTotalAmount;
    TextView totalAmount;
    TextView lblDiscount;
    TextView discountAmount;
    TextView lblNetAmount;
    TextView netAmount;
    TextView makePayment;

    private final String GET_CHILDREN_LISTING = "GET_CHILDREN_LISTING";
    private final String GET_DATES_LISTING = "GET_DATES_LISTING";
    private final String VALIDATE_FORM = "VALIDATE_FORM";

    ArrayList<ChildBean> childrenListing = new ArrayList<>();

    ArrayList<CampDateBean> allDatesListing = new ArrayList<>();
    ArrayList<CampDateBean> availableDatesListing = new ArrayList<>();

    ParentChildrenSpinnerAdapter childrenSpinnerAdapter;
    ParentAvailableDatesListingAdapter availableDatesListingAdapter;

    CampBean clickedOnCamp;
    int sessionPosition;
    int locationPosition;

    String strParamsToSend = "";

    float totalAmountValue = 0, discountAmountValue = 0, netAmountValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_book_camp_screen_old);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        childrenSpinner = (Spinner) findViewById(R.id.childrenSpinner);
        campName = (TextView) findViewById(R.id.campName);
        timings = (TextView) findViewById(R.id.timings);
        age = (TextView) findViewById(R.id.age);
        selectAllCheckbox = (CheckBox) findViewById(R.id.selectAllCheckbox);
        availableDatesGridView = (GridView) findViewById(R.id.availableDatesListView);
        commentsTextInputLayout = (TextInputLayout) findViewById(R.id.commentsTextInputLayout);
        comments = (TextView) findViewById(R.id.comments);
        lblTotalAmount = (TextView) findViewById(R.id.lblTotalAmount);
        totalAmount = (TextView) findViewById(R.id.totalAmount);
        lblDiscount = (TextView) findViewById(R.id.lblDiscount);
        discountAmount = (TextView) findViewById(R.id.discountAmount);
        lblNetAmount = (TextView) findViewById(R.id.lblNetAmount);
        netAmount = (TextView) findViewById(R.id.netAmount);
        makePayment = (TextView) findViewById(R.id.makePayment);

        changeFonts();

        childrenSpinnerAdapter = new ParentChildrenSpinnerAdapter(ParentBookCampScreenOLD.this, childrenListing);
        childrenSpinner.setAdapter(childrenSpinnerAdapter);

        availableDatesListingAdapter = new ParentAvailableDatesListingAdapter(ParentBookCampScreenOLD.this, availableDatesListing, ParentBookCampScreenOLD.this);
        availableDatesGridView.setAdapter(availableDatesListingAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            clickedOnCamp = (CampBean) intent.getSerializableExtra("clickedOnCamp");
            sessionPosition = intent.getIntExtra("sessionPosition", -1);
            locationPosition = intent.getIntExtra("locationPosition", -1);

            campName.setText(clickedOnCamp.getCampName());
            timings.setText(clickedOnCamp.getSessionsList().get(sessionPosition).getShowFromTime() + " - " + clickedOnCamp.getSessionsList().get(sessionPosition).getShowToTime());
            age.setText(clickedOnCamp.getSessionsList().get(sessionPosition).getGroupName());
        }

        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (childrenSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(ParentBookCampScreenOLD.this, "Please select child", Toast.LENGTH_SHORT).show();
                    return;
                }

                ChildBean selectedChild = null;
                for (ChildBean childBean : childrenListing) {
                    if (childBean.isSelected()) {
                        selectedChild = childBean;
                        break;
                    }
                }

                ArrayList<CampDateBean> selectedDates = new ArrayList<>();
                for (CampDateBean campDateBean : availableDatesListing) {
                    if (campDateBean.isSelected()) {
                        selectedDates.add(campDateBean);
                    }
                }

                String strComments = comments.getText().toString().trim();

                if (selectedDates.isEmpty()) {
                    Toast.makeText(ParentBookCampScreenOLD.this, "Please choose at least one date", Toast.LENGTH_SHORT).show();
                } else if (strComments == null || strComments.isEmpty()) {
                    Toast.makeText(ParentBookCampScreenOLD.this, "Please enter your comments", Toast.LENGTH_SHORT).show();
                } else {

//                    //System.out.println("Params to send "+strParamsToSend);

                    if (Utilities.isNetworkAvailable(ParentBookCampScreenOLD.this)) {

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("academies_id", loggedInUser.getAcademiesId()));
                        nameValuePairList.add(new BasicNameValuePair("users_id", loggedInUser.getId()));
                        nameValuePairList.add(new BasicNameValuePair("user_addresses_id", "1"));
                        nameValuePairList.add(new BasicNameValuePair("total_amount", totalAmountValue + ""));
                        nameValuePairList.add(new BasicNameValuePair("tax_amount", "0"));
                        nameValuePairList.add(new BasicNameValuePair("discount_amount", discountAmountValue + ""));
                        nameValuePairList.add(new BasicNameValuePair("net_amount", netAmountValue + ""));
                        nameValuePairList.add(new BasicNameValuePair("notes", strComments));
                        nameValuePairList.add(new BasicNameValuePair("child_id", selectedChild.getId()));
                        nameValuePairList.add(new BasicNameValuePair("camps_id", clickedOnCamp.getCampId()));
                        nameValuePairList.add(new BasicNameValuePair("camp_sessions_id", clickedOnCamp.getSessionsList().get(sessionPosition).getSessionId()));
                        nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnCamp.getLocationList().get(locationPosition).getLocationId()));
                        nameValuePairList.add(new BasicNameValuePair("booking_details", strParamsToSend));


                        String webServiceUrl = Utilities.BASE_URL + "camps/book";
//                        String webServiceUrl = Utilities.BASE_URL + "camps/book_validation";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:" + loggedInUser.getId());
                        headers.add("X-access-token:" + loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookCampScreenOLD.this, nameValuePairList, VALIDATE_FORM, ParentBookCampScreenOLD.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(ParentBookCampScreenOLD.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        childrenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                for (ChildBean childBean : childrenListing) {
                    childBean.setSelected(false);
                }
                childrenListing.get(position).setSelected(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Doing nothing
            }

        });

        selectAllCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (CampDateBean campDateBean : availableDatesListing) {
                        campDateBean.setSelected(true);
                    }
                } else {
                    for (CampDateBean campDateBean : availableDatesListing) {
                        campDateBean.setSelected(false);
                    }
                }
                calculateCost();
                availableDatesListingAdapter.notifyDataSetChanged();
            }
        });

        getChildrenListing();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void calculateCost() {

        totalAmountValue = 0;
        discountAmountValue = 0;
        netAmountValue = 0;
        strParamsToSend = "[";


        // Get unique week numbers for all the dates
        HashSet<Integer> allWeekNumbers = new HashSet<>();
        for (CampDateBean dateBean : allDatesListing) {
            allWeekNumbers.add(dateBean.getWeekOfYear());
        }

        // Make listing of selected dates
        ArrayList<CampDateBean> selectedDates = new ArrayList<>();
        for (CampDateBean campDateBean : availableDatesListing) {
            if (campDateBean.isSelected()) {
                selectedDates.add(campDateBean);
            }
        }

        // Get number of days in each week for all weeks
        HashMap<Integer, Integer> daysOfWeekAllDays = new HashMap<>();
        for (int week : allWeekNumbers) {
            int counter = 0;
            for (CampDateBean currentDate : allDatesListing) {
                if (currentDate.getWeekOfYear() == week) {
                    counter++;
                }
            }
            daysOfWeekAllDays.put(week, counter);
        }
//        //System.out.println(daysOfWeekAllDays);


        // Get number of days in each week for selected days only
        HashMap<Integer, Integer> daysOfWeekSelectedDays = new HashMap<>();
        for (int week : allWeekNumbers) {
            int counter = 0;
            for (CampDateBean currentDate : selectedDates) {
                if (currentDate.getWeekOfYear() == week) {
                    counter++;
                }
            }
            daysOfWeekSelectedDays.put(week, counter);
        }
//        //System.out.println(daysOfWeekSelectedDays);


        // Calculate cost

        for (int weekNumber : daysOfWeekSelectedDays.keySet()) {

            if (daysOfWeekAllDays.get(weekNumber) == daysOfWeekSelectedDays.get(weekNumber)) {

                // Per week cost
                netAmountValue += Float.parseFloat(clickedOnCamp.getSessionsList().get(sessionPosition).getPerWeekCost());

                for (CampDateBean campDateBean : selectedDates) {
                    if (campDateBean.getWeekOfYear() == weekNumber) {

                        String bookingDate = campDateBean.getDate();
                        String cost = clickedOnCamp.getSessionsList().get(sessionPosition).getPerDayCost();
                        String weeklyDiscount = String.valueOf(((Float.parseFloat(clickedOnCamp.getSessionsList().get(sessionPosition).getPerDayCost()) * clickedOnCamp.getDaysList().size()) - Float.parseFloat(clickedOnCamp.getSessionsList().get(sessionPosition).getPerWeekCost())) / clickedOnCamp.getDaysList().size());
                        String strTotalCost = String.valueOf(Float.parseFloat(cost) - Float.parseFloat(weeklyDiscount));

                        discountAmountValue += Float.parseFloat(weeklyDiscount);

                        //System.out.println(bookingDate +" "+cost +" "+weeklyDiscount+" "+strTotalCost);
//                        strParamsToSend += "{\"booking_date\":\""+bookingDate+"\",\"cost\":\""+cost+"\",\"weekly_discount\":";

                        String arr[] = bookingDate.split("-");
                        bookingDate = arr[2] + "-" + arr[1] + "-" + arr[0];

                        strParamsToSend += "{\"booking_date\": \"" + bookingDate + "\",\"cost\": \"" + cost + "\",\"weekly_discount\": \"" + weeklyDiscount + "\",\"total_cost\": \"" + strTotalCost + "\"},";

                    }
                }

            } else {

                for (CampDateBean campDateBean : selectedDates) {

                    if (campDateBean.getWeekOfYear() == weekNumber) {

                        // Per day cost
                        netAmountValue += Float.parseFloat(clickedOnCamp.getSessionsList().get(sessionPosition).getPerDayCost());


                        String bookingDate = campDateBean.getDate();
                        String cost = clickedOnCamp.getSessionsList().get(sessionPosition).getPerDayCost();
                        String weeklyDiscount = "0";
                        String strTotalCost = cost;

                        //System.out.println(bookingDate +" "+cost +" "+weeklyDiscount+" "+strTotalCost);

                        String arr[] = bookingDate.split("-");
                        bookingDate = arr[2] + "-" + arr[1] + "-" + arr[0];

                        strParamsToSend += "{\"booking_date\": \"" + bookingDate + "\",\"cost\": \"" + cost + "\",\"weekly_discount\": \"" + weeklyDiscount + "\",\"total_cost\": \"" + strTotalCost + "\"},";
                    }

                }

            }


        }

        if (strParamsToSend != null && strParamsToSend.length() > 0 && strParamsToSend.charAt(strParamsToSend.length() - 1) == ',') {
            strParamsToSend = strParamsToSend.substring(0, strParamsToSend.length() - 1);
        }
        strParamsToSend += "]";

        totalAmountValue = Float.parseFloat(clickedOnCamp.getSessionsList().get(sessionPosition).getPerDayCost()) * selectedDates.size();

        DecimalFormat df = new DecimalFormat("#.00");

        String academy_currency = sharedPreferences.getString("academy_currency", null);
        totalAmount.setText(df.format(totalAmountValue) + " " + academy_currency);
        if (discountAmountValue == 0) {
            discountAmount.setText("0.00 " + academy_currency);
        } else {
            discountAmount.setText(df.format(discountAmountValue) + " " + academy_currency);
        }

        netAmount.setText(df.format(netAmountValue) + " " + academy_currency);
    }

    private void showChildren() {
        childrenSpinnerAdapter.notifyDataSetChanged();
        getAllDatesListing();
    }

    private void updateAvailableDates() {
        availableDatesListingAdapter.notifyDataSetChanged();
    }

    private void getAllDatesListing() {
        if (Utilities.isNetworkAvailable(ParentBookCampScreenOLD.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("camps_id", clickedOnCamp.getCampId()));
            nameValuePairList.add(new BasicNameValuePair("camp_sessions_id", clickedOnCamp.getSessionsList().get(sessionPosition).getSessionId()));

            String webServiceUrl = Utilities.BASE_URL + "camps/get_avail_dates";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookCampScreenOLD.this, nameValuePairList, GET_DATES_LISTING, ParentBookCampScreenOLD.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookCampScreenOLD.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getChildrenListing() {
        if (Utilities.isNetworkAvailable(ParentBookCampScreenOLD.this)) {

            String webServiceUrl = Utilities.BASE_URL + "children/children_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ParentBookCampScreenOLD.this, GET_CHILDREN_LISTING, ParentBookCampScreenOLD.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookCampScreenOLD.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_CHILDREN_LISTING:

                childrenListing.clear();

                if (response == null) {
                    Toast.makeText(ParentBookCampScreenOLD.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");

                            ChildBean childBean = new ChildBean();
                            childBean.setId("-1");
                            childBean.setFirstName("Please select Child");
                            childBean.setFullName("Please select Child");
                            childrenListing.add(childBean);

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject childObject = dataArray.getJSONObject(i);
                                childBean = new ChildBean();

                                childBean.setId(childObject.getString("id"));
                                childBean.setAcademiesId(childObject.getString("academies_id"));
                                childBean.setUsername(childObject.getString("username"));
                                childBean.setEmail(childObject.getString("email"));
                                childBean.setGender(childObject.getString("gender"));
                                childBean.setCreatedAt(childObject.getString("created_at"));
                                childBean.setState(childObject.getString("state"));
                                childBean.setFirstName(childObject.getString("first_name"));
                                childBean.setLastName(childObject.getString("last_name"));
                                childBean.setFullName(childObject.getString("full_name"));
                                childBean.setAge(childObject.getString("age"));
                                childBean.setDateOfBirth(childObject.getString("dob"));
                                childBean.setMedicalCondition(childObject.getString("medical_conditions"));

                                childrenListing.add(childBean);
                            }
                        } else {
                            Toast.makeText(ParentBookCampScreenOLD.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookCampScreenOLD.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                showChildren();

                break;

            case GET_DATES_LISTING:

                allDatesListing.clear();
                availableDatesListing.clear();
                if (response == null) {
                    Toast.makeText(ParentBookCampScreenOLD.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONObject dataObject = responseObject.getJSONObject("data");

                            JSONArray allDatesArray = dataObject.getJSONArray("all_dates_for_session");
                            JSONArray availableDatesArray = dataObject.getJSONArray("available_dates");

                            CampDateBean campDateBean;
                            for (int i = 0; i < allDatesArray.length(); i++) {
                                campDateBean = new CampDateBean();

                                campDateBean.setDate(allDatesArray.getString(i));
                                campDateBean.setSelected(false);

                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    Date date = dateFormat.parse(allDatesArray.getString(i));
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);

                                    campDateBean.setYear(calendar.get(Calendar.YEAR));
                                    campDateBean.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Toast.makeText(ParentBookCampScreenOLD.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                allDatesListing.add(campDateBean);
                            }

                            for (int i = 0; i < availableDatesArray.length(); i++) {
                                campDateBean = new CampDateBean();
                                campDateBean.setDate(availableDatesArray.getString(i));
                                campDateBean.setSelected(false);

                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    Date date = dateFormat.parse(availableDatesArray.getString(i));
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);

                                    campDateBean.setYear(calendar.get(Calendar.YEAR));
                                    campDateBean.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Toast.makeText(ParentBookCampScreenOLD.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                availableDatesListing.add(campDateBean);
                            }
                        } else {
                            Toast.makeText(ParentBookCampScreenOLD.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookCampScreenOLD.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                updateAvailableDates();

                break;

            case VALIDATE_FORM:

                if (response == null) {
                    Toast.makeText(ParentBookCampScreenOLD.this, "Could not reach server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
//                            Toast.makeText(ParentBookCampScreenOLD.this, message, Toast.LENGTH_SHORT).show();
//                            finish();

                            String netAmount = responseObject.getString("net_amount");
                            String ordersId = responseObject.getString("orders_id");

                            Intent intent = new Intent(this, ParentPaymentGatewayWebViewScreen.class);
                            intent.putExtra(AvenuesParams.ACCESS_CODE, Utilities.ACCESS_CODE);
                            intent.putExtra(AvenuesParams.MERCHANT_ID, Utilities.MERCHANT_ID);
                            intent.putExtra(AvenuesParams.ORDER_ID, ordersId);
//                            intent.putExtra(AvenuesParams.CURRENCY, Utilities.CURRENCY);

                            SharedPreferences sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                            String academy_currency = sharedPreferences.getString("academy_currency", null);
                            intent.putExtra(AvenuesParams.CURRENCY, academy_currency);

                            intent.putExtra(AvenuesParams.AMOUNT, netAmount);

//                            intent.putExtra(AvenuesParams.REDIRECT_URL, Utilities.BASE_URL + "payments/response_handler");
                            intent.putExtra(AvenuesParams.REDIRECT_URL, Utilities.REDIRECT_URL);
                            intent.putExtra(AvenuesParams.CANCEL_URL, Utilities.BASE_URL + "payments/response_handler");
                            intent.putExtra(AvenuesParams.RSA_KEY_URL, Utilities.BASE_URL + "payments/get_RSA");

                            startActivity(intent);

                        } else {
                            Toast.makeText(ParentBookCampScreenOLD.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookCampScreenOLD.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void changeFonts() {
        campName.setTypeface(linoType);
        timings.setTypeface(helvetica);
        age.setTypeface(helvetica);
        selectAllCheckbox.setTypeface(helvetica);
        commentsTextInputLayout.setTypeface(helvetica);
        comments.setTypeface(helvetica);
        lblTotalAmount.setTypeface(linoType);
        totalAmount.setTypeface(helvetica);
        lblDiscount.setTypeface(linoType);
        discountAmount.setTypeface(helvetica);
        lblNetAmount.setTypeface(linoType);
        netAmount.setTypeface(helvetica);
        makePayment.setTypeface(linoType);
    }

}