package com.dunvant.application.parent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.ApplicationContext;
import com.dunvant.application.R;
import com.dunvant.application.beans.AcademySessionSummaryBean;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.ExtraFeesBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.paymentGatewayUtilities.AvenuesParams;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ParentBookMidWeekProceedScreen extends AppCompatActivity implements IWebServiceCallback {
    String location_id;
    TextView participantsName;
    TextView time;
    TextView duration;
    TextView perPackageInfo;
    TextView numberOfPackages;
    TextView totalPackageAmount;
    String child_ids;
    String session_id;

    public static String deleteRow = "false";
    String net_Amount;
    String ordersId;
    String academy_currency;
    public static final boolean isSecurityEnabled = false;
    boolean autoPromo = false;
    String promoCode = "";
    ApplicationContext applicationContext;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    TextView clearAll;
    ScrollView scrollView;
    LinearLayout bottomLinearLayout;
    TextView lblGrandTotal;
    TextView grandTotal;
    TextView lblRegistration;
    TextView registration;
    TextView lblTournamentFee;
    TextView tournamentFee;
    TextView lblTotal;
    TextView netAmount;
    TextView bookMoreSessions;
    TextView makePayment;
    LinearLayout grandTotalLinear;
    LinearLayout registrationLinear;
    LinearLayout tournamentFeeLinear;
    LinearLayout totalLinear;
    LinearLayout extraFeesOneLinear;
    TextView lblExtraFeesOne;
    TextView extraFeesOne;
    LinearLayout extraFeesTwoLinear;
    TextView lblExtraFeesTwo;
    TextView extraFeesTwo;
    LinearLayout discountLinear;
    TextView discount;
    LinearLayout promoCodeLinear;
    EditText promoCodeEditText;
    Button applyPromoCode;
    Button cancelPromoCode;
    LinearLayout promoCodeDiscountLinear;
    TextView lblPromoCodeDiscount;
    TextView promoCodeDiscount;
    ListView warningListView;

//    TextView discountDescription;

    private final String GET_SUMMARY_DATA = "GET_SUMMARY_DATA";
    private final String BOOK_SESSION = "BOOK_SESSION";
    private final String APPLY_PROMO_CODE = "APPLY_PROMO_CODE";
    private final String TELR_STATUS = "TELR_STATUS";

    ArrayList<AcademySessionSummaryBean> summaryList = new ArrayList<>();
//    BookingHistoryDiscountBean discountBean = new BookingHistoryDiscountBean();

    double dblRegFees = 0;
    double dblTournamentFees = 0;
    double dblExtraFeesOne = 0;
    double dblExtraFeesTwo = 0;
    double dblNetAmount = 0;
    double promoCodeDeductAmount = 0;
    // long roundedOffNetAmount = 0;

    String extraFeesOneLabel = "";
    String extraFeesTwoLabel = "";
    String strPromoCode = "";

    ArrayList<String> feesAppliedFor = new ArrayList<>();

    DecimalFormat decimalFormat = new DecimalFormat("#.00");

    ArrayList<String> warningList = new ArrayList<>();

    String sessionsDetail;
    TextView packageName;
    TextView location;
    String titleStr;
    String nameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_book_mid_week_proceed_activity);

        applicationContext = (ApplicationContext) getApplication();
        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        titleStr = getIntent().getStringExtra("title");
        nameStr = getIntent().getStringExtra("name");

        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        clearAll = findViewById(R.id.clearAll);
        scrollView = findViewById(R.id.scrollView);
        bottomLinearLayout = findViewById(R.id.bottomLinearLayout);
        lblGrandTotal = findViewById(R.id.lblGrandTotal);
        grandTotal = findViewById(R.id.grandTotal);
        lblRegistration = findViewById(R.id.lblRegistration);
        registration = findViewById(R.id.registration);
        lblTournamentFee = findViewById(R.id.lblTournamentFee);
        tournamentFee = findViewById(R.id.tournamentFee);
        lblTotal = findViewById(R.id.lblTotal);
        netAmount = findViewById(R.id.netAmount);
        bookMoreSessions = findViewById(R.id.bookMoreSessions);
        makePayment = findViewById(R.id.makePayment);
        grandTotalLinear = findViewById(R.id.grandTotalLinear);
        registrationLinear = findViewById(R.id.registrationLinear);
        tournamentFeeLinear = findViewById(R.id.tournamentFeeLinear);
        totalLinear = findViewById(R.id.totalLinear);
        extraFeesOneLinear = findViewById(R.id.extraFeesOneLinear);
        lblExtraFeesOne = findViewById(R.id.lblExtraFeesOne);
        extraFeesOne = findViewById(R.id.extraFeesOne);
        extraFeesTwoLinear = findViewById(R.id.extraFeesTwoLinear);
        lblExtraFeesTwo = findViewById(R.id.lblExtraFeesTwo);
        extraFeesTwo = findViewById(R.id.extraFeesTwo);
        discountLinear = findViewById(R.id.discountLinear);
        discount = findViewById(R.id.discount);
        promoCodeLinear = findViewById(R.id.promoCodeLinear);
        promoCodeEditText = findViewById(R.id.promoCodeEditText);
        promoCodeEditText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        applyPromoCode = findViewById(R.id.applyPromoCode);
        cancelPromoCode = findViewById(R.id.cancelPromoCode);
        promoCodeDiscountLinear = findViewById(R.id.promoCodeDiscountLinear);
        lblPromoCodeDiscount = findViewById(R.id.lblPromoCodeDiscount);
        promoCodeDiscount = findViewById(R.id.promoCodeDiscount);
        warningListView = findViewById(R.id.warningListView);

         participantsName = findViewById(R.id.participantsName);
         time = findViewById(R.id.time);
         duration = findViewById(R.id.duration);
         perPackageInfo = findViewById(R.id.perPackageInfo);
         numberOfPackages = findViewById(R.id.numberOfPackages);
         totalPackageAmount = findViewById(R.id.totalPackageAmount);
        packageName = findViewById(R.id.packageName);
        location = findViewById(R.id.location);

         child_ids = getIntent().getStringExtra("child_ids");
         session_id = getIntent().getStringExtra("session_id");

         academy_currency = sharedPreferences.getString("academy_currency", null);
        promoCodeDiscount.setText("0.00 "+academy_currency);

        packageName.setText("PACKAGE NAME : "+titleStr);
        location.setText("LOCATION : "+nameStr);

//        discountDescription = findViewById(R.id.discountDescription);

        changeFonts();


        /*Set<String> keys = applicationContext.getBookAcademySummaryData().keySet();
        for (String key : keys){
            //System.out.println(key+" :: "+applicationContext.getBookAcademySummaryData().get(key));
        }*/

        getSummaryDetails();

        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//
//                if(summaryList == null || summaryList.isEmpty()) {
//                    Toast.makeText(ParentBookMidWeekProceedScreen.this, "Please select a session", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                feesAppliedFor.clear();
//
//                String strBookingDetails = "[";
//                for(AcademySessionSummaryBean summaryBean : summaryList) {
//                    String sessionId = summaryBean.getSessionId();
//
//                    String childrenIds = "[";
//                    for (ChildBean childBean : summaryBean.getChildrenList()) {
//
//                        childrenIds += "\""+childBean.getId()+"\",";
//
//                    }
//
//                    if (childrenIds != null && childrenIds.length() > 0 && childrenIds.charAt(childrenIds.length()-1)==',') {
//                        childrenIds = childrenIds.substring(0, childrenIds.length()-1);
//                    }
//                    childrenIds += "]";
//
//                    String strIsTrial = summaryBean.isTrialSelected() ? "1":"0";
//
//                    String strBookingDates = "[";
//                    for(AcademySessionDateBean dateBean : summaryBean.getAvailableDatesList()) {
//                        if(dateBean.isSelected()) {
//                            strBookingDates += "\""+dateBean.getDate()+"\",";
//                        }
//                    }
//
//                    if (strBookingDates != null && strBookingDates.length() > 0 && strBookingDates.charAt(strBookingDates.length()-1)==',') {
//                        strBookingDates = strBookingDates.substring(0, strBookingDates.length()-1);
//                    }
//
//                    strBookingDates += "]";
//                    strBookingDetails += "{\"sessions_id\": \""+sessionId+"\",\"children_ids\": "+childrenIds+",\"is_trial\": "+strIsTrial+",\"booking_dates\": "+strBookingDates+"},";
//
//                }
//
//                if (strBookingDetails != null && strBookingDetails.length() > 0 && strBookingDetails.charAt(strBookingDetails.length()-1)==',') {
//                    strBookingDetails = strBookingDetails.substring(0, strBookingDetails.length()-1);
//                }
//
//                strBookingDetails += "]";

                if(Utilities.isNetworkAvailable(ParentBookMidWeekProceedScreen.this)) {
                    String strNetAmount = dblNetAmount+"";

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("session_id", session_id));
                    nameValuePairList.add(new BasicNameValuePair("child_ids", child_ids));
                    nameValuePairList.add(new BasicNameValuePair("promo_code", strPromoCode));
                    nameValuePairList.add(new BasicNameValuePair("locations_id", location_id));
                    nameValuePairList.add(new BasicNameValuePair("module_type", "4"));

                    if(netAmount.getText().toString().trim().equals("0.00 "+academy_currency) || netAmount.getText().toString().trim().equals("0 "+academy_currency)){
                        nameValuePairList.add(new BasicNameValuePair("payment_method", "offline"));
                        nameValuePairList.add(new BasicNameValuePair("total_amount", "0"));
                    }else{
                        nameValuePairList.add(new BasicNameValuePair("custom_net_amount", strNetAmount));
                        nameValuePairList.add(new BasicNameValuePair("total_amount", ""+dblNetAmount));
                    }

                    String webServiceUrl = Utilities.BASE_URL + "midweek_session/book_package";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookMidWeekProceedScreen.this, nameValuePairList, BOOK_SESSION, ParentBookMidWeekProceedScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(ParentBookMidWeekProceedScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        applyPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strPromoCode = promoCodeEditText.getText().toString().trim();

                dblNetAmount = dblNetAmount + promoCodeDeductAmount;

                System.out.println("strPromoCode::"+strPromoCode+"::"+"::dblNetAmount::"+dblNetAmount+"::promoCodeDeductAmount::"+promoCodeDeductAmount);

                promoCodeDeductAmount = 0;

                promoCodeDiscount.setText("0.00 "+academy_currency);
                if(dblNetAmount == 0) {
                    netAmount.setText("0.00 "+academy_currency);
                } else {
                    netAmount.setText(decimalFormat.format(dblNetAmount)+" "+academy_currency);
                }

                if(strPromoCode == null || strPromoCode.isEmpty()) {
                    Toast.makeText(ParentBookMidWeekProceedScreen.this, "Please enter Promo Code", Toast.LENGTH_SHORT).show();
                } else {
                    if(Utilities.isNetworkAvailable(ParentBookMidWeekProceedScreen.this)) {

//                        sessionsDetail = "[";
//                        sessionsDetail += "{\"sessions_id\":\""+session_id+"\",\"child_ids\":\""+child_ids+"\",\"is_trial\":\"0\"},";
//                        sessionsDetail += "]";
//

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("session_id", session_id+""));
                        nameValuePairList.add(new BasicNameValuePair("child_ids", child_ids+""));
                        nameValuePairList.add(new BasicNameValuePair("amount", dblNetAmount+""));
                        nameValuePairList.add(new BasicNameValuePair("module_type", "4"));
                        nameValuePairList.add(new BasicNameValuePair("promo_code", strPromoCode));
                        nameValuePairList.add(new BasicNameValuePair("locations_id", location_id));

                   //     nameValuePairList.add(new BasicNameValuePair("sessions_detail", sessionsDetail));

                        String webServiceUrl = Utilities.BASE_URL + "midweek_session/apply_promo_code_midweek";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:"+loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookMidWeekProceedScreen.this, nameValuePairList, APPLY_PROMO_CODE, ParentBookMidWeekProceedScreen.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(ParentBookMidWeekProceedScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cancelPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePayment.setText("MAKE A PAYMENT");
                promoCodeEditText.setText("");
                strPromoCode = promoCodeEditText.getText().toString().trim();

                dblNetAmount = dblNetAmount + promoCodeDeductAmount;
                promoCodeDeductAmount = 0;

                promoCodeDiscount.setText("0.00 "+academy_currency);
                if(dblNetAmount == 0) {
                    netAmount.setText("0.00 "+academy_currency);
                } else {
                    netAmount.setText(decimalFormat.format(dblNetAmount)+" "+academy_currency);
                }
            }
        });

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ParentBookMidWeekProceedScreen.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.parent_dialog_clear_all);

                TextView yes = (TextView) dialog.findViewById(R.id.yes);
                TextView no = (TextView) dialog.findViewById(R.id.no);

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        clearAll();
                    }
                });

                dialog.show();
            }
        });

        bookMoreSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ParentAcademyListingFragment.comingFromBookMore = true; // Old

                /*ParentAcademyListingScreen.comingFromBookMore = true;
                Intent mainScreen = new Intent(ParentBookMidWeekProceedScreen.this, ParentMainScreen.class);
                mainScreen.putExtra("screenName","bookSession");
                mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainScreen);*/

                Intent screenOne = new Intent(ParentBookMidWeekProceedScreen.this, ParentAcademyListingWithFiltersScreen.class);
                screenOne.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(screenOne);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void updateValues() {

        double grandTotalValue = 0;
        double totalDiscount = 0;
        double amountAfterDiscount = 0;

        for (AcademySessionSummaryBean summaryBean : summaryList) {
            grandTotalValue += summaryBean.getSessionCost();
            totalDiscount += summaryBean.getTotalDiscount();
            amountAfterDiscount += summaryBean.getAmountAfterDiscount();
        }

//        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        if(grandTotalValue == 0) {
            grandTotal.setText("0.00 "+academy_currency);
        } else {
            grandTotal.setText(decimalFormat.format(grandTotalValue)+" "+academy_currency);
        }

        if(totalDiscount == 0) {
            discount.setText("0.00 "+academy_currency);
        } else {
            discount.setText("- "+ decimalFormat.format(totalDiscount)+" "+academy_currency);
        }

        dblNetAmount = amountAfterDiscount+dblRegFees+dblTournamentFees+dblExtraFeesOne+dblExtraFeesTwo;
        // roundedOffNetAmount = Math.round(dblNetAmount);

        if(dblNetAmount == 0) {
            netAmount.setText("0.00 "+academy_currency);
        } else {
//            netAmount.setText(decimalFormat.format(dblNetAmount)+" AED");
            //  netAmount.setText(roundedOffNetAmount+" "+academy_currency);
            netAmount.setText(dblNetAmount+" "+academy_currency);
        }

        if(summaryList.isEmpty()){
            clearAll();
        }

//        Utilities.setListViewHeightBasedOnChildren(summaryListView);

    }

    public void clearAll(){
        //                        applicationContext.getBookAcademySummaryData().clear();
        applicationContext.getAcademySessionChildBeanListing().clear();

        summaryList.clear();
        grandTotalLinear.setVisibility(View.GONE);
        registrationLinear.setVisibility(View.GONE);
        tournamentFeeLinear.setVisibility(View.GONE);
        extraFeesOneLinear.setVisibility(View.GONE);
        extraFeesTwoLinear.setVisibility(View.GONE);
        discountLinear.setVisibility(View.GONE);
        totalLinear.setVisibility(View.GONE);
        bottomLinearLayout.setVisibility(View.GONE);

        warningListView.setAdapter(null);
        warningListView.setVisibility(View.GONE);

//                        discountDescription.setVisibility(View.GONE);
        grandTotal.setText("");
        registration.setText("");
        tournamentFee.setText("");
        netAmount.setText("");
//                        discountDescription.setText("");
    }

    public void calculateFees() {
        /*HashMap<String, Double> studentRegFees = new HashMap<>();
        HashMap<String, Double> studentTournamentFees = new HashMap<>();
        HashMap<String, Double> extraFeesOneHashMap = new HashMap<>();
        HashMap<String, Double> extraFeesTwoHashMap = new HashMap<>();*/

        dblRegFees = 0;
        dblTournamentFees = 0;
        dblExtraFeesOne = 0;
        dblExtraFeesTwo = 0;
        for (AcademySessionSummaryBean summaryBean : summaryList) {

            if (!summaryBean.isTrialSelected()) {

                for (ChildBean childBean : summaryBean.getChildrenList()) {
                    /*studentRegFees.put(childBean.getId(), Double.parseDouble(childBean.getRegistrationFee()));
                    studentTournamentFees.put(childBean.getId(), Double.parseDouble(childBean.getTournamentFee()));*/

                    dblRegFees += Double.parseDouble(childBean.getRegistrationFee());
                    dblTournamentFees += Double.parseDouble(childBean.getTournamentFee());

                    ArrayList<ExtraFeesBean> extraFeesList = childBean.getExtraFeesList();
                    if(extraFeesList.size() == 2){
                        ExtraFeesBean extraFeesOneBean = extraFeesList.get(0);
                        ExtraFeesBean extraFeesTwoBean = extraFeesList.get(1);

                        /*extraFeesOneHashMap.put(childBean.getId(), Double.parseDouble(extraFeesOneBean.getValue()));
                        extraFeesTwoHashMap.put(childBean.getId(), Double.parseDouble(extraFeesTwoBean.getValue()));*/

                        dblExtraFeesOne += Double.parseDouble(extraFeesOneBean.getValue());
                        dblExtraFeesTwo += Double.parseDouble(extraFeesTwoBean.getValue());

                        extraFeesOneLabel = extraFeesOneBean.getLabel();
                        extraFeesTwoLabel = extraFeesTwoBean.getLabel();
                    }
                    if(extraFeesList.size() == 1){
                        ExtraFeesBean extraFeesOneBean = extraFeesList.get(0);

//                        extraFeesOneHashMap.put(childBean.getId(), Double.parseDouble(extraFeesOneBean.getValue()));

                        dblExtraFeesOne += Double.parseDouble(extraFeesOneBean.getValue());
                        extraFeesOneLabel = extraFeesOneBean.getLabel();
                    }
                }
            }
        }

//        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        /*Set<String> keys = studentRegFees.keySet();
        dblRegFees = 0;
        dblTournamentFees = 0;
        for(String key : keys) {
            dblRegFees += studentRegFees.get(key);
            dblTournamentFees += studentTournamentFees.get(key);
        }

        Set<String> extraOneKeys = extraFeesOneHashMap.keySet();
        dblExtraFeesOne = 0;
        for(String key: extraOneKeys) {
            dblExtraFeesOne += extraFeesOneHashMap.get(key);
        }

        Set<String> extraTwoKeys = extraFeesTwoHashMap.keySet();
        dblExtraFeesTwo = 0;
        for(String key: extraTwoKeys) {
            dblExtraFeesTwo += extraFeesTwoHashMap.get(key);
        }*/
        if(dblRegFees == 0) {
            registration.setText("0.00 "+academy_currency);
        } else {
            registration.setText(decimalFormat.format(dblRegFees)+" "+academy_currency);
        }

        if(dblTournamentFees == 0) {
            tournamentFee.setText("0.00 "+academy_currency);
        } else {
            tournamentFee.setText(decimalFormat.format(dblTournamentFees)+" "+academy_currency);
        }

        lblExtraFeesOne.setText(extraFeesOneLabel);
        lblExtraFeesTwo.setText(extraFeesTwoLabel);

        extraFeesOne.setText(decimalFormat.format(dblExtraFeesOne)+" "+academy_currency);
        extraFeesTwo.setText(decimalFormat.format(dblExtraFeesTwo)+" "+academy_currency);

        if(dblExtraFeesOne == 0) {
            extraFeesOneLinear.setVisibility(View.GONE);
        } else {
            extraFeesOneLinear.setVisibility(View.VISIBLE);
        }

        if(dblExtraFeesTwo == 0) {
            extraFeesTwoLinear.setVisibility(View.GONE);
        } else {
            extraFeesTwoLinear.setVisibility(View.VISIBLE);
        }

        updateValues();

    }

    private void getSummaryDetails(){
        if(Utilities.isNetworkAvailable(ParentBookMidWeekProceedScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("session_id", session_id));
            nameValuePairList.add(new BasicNameValuePair("child_ids", child_ids));

            String webServiceUrl = Utilities.BASE_URL + "midweek_session/validate_child_ages";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookMidWeekProceedScreen.this, nameValuePairList, GET_SUMMARY_DATA, ParentBookMidWeekProceedScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookMidWeekProceedScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_SUMMARY_DATA:
                summaryList.clear();
                warningList.clear();

                if (response == null) {
                    Toast.makeText(ParentBookMidWeekProceedScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONObject jsonObject = responseObject.getJSONObject("data");
                            String session_details_id = jsonObject.getString("session_details_id");
                            String session_detail_title = jsonObject.getString("session_detail_title");
                            String locations_name = jsonObject.getString("locations_name");
                            location_id = jsonObject.getString("location_id");
                            String group_name = jsonObject.getString("group_name");
                            String from_age = jsonObject.getString("from_age");
                            String to_age = jsonObject.getString("to_age");
                            String package_id = jsonObject.getString("package_id");
                            String start_time = jsonObject.getString("start_time");
                            String end_time = jsonObject.getString("end_time");
                            String start_time_formatted = jsonObject.getString("start_time_formatted");
                            String end_time_formatted = jsonObject.getString("end_time_formatted");
                            String number_of_hours = jsonObject.getString("number_of_hours");
                            double cost = jsonObject.getDouble("cost");
                            String display_cost = jsonObject.getString("display_cost");
                            String session_count = jsonObject.getString("session_count");

                            JSONArray jsonArray = jsonObject.getJSONArray("children_register");
                            String name= "";
                            String id = "";
                            int childCalculate=0;
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                 id = id+" " + jsonObject1.getString("id");
                                 if(i==0){
                                     name =  jsonObject1.getString("name");
                                 }else{
                                     name = name +", "+ jsonObject1.getString("name");
                                 }

                                 childCalculate++;
                            }

                            double costTotal = cost*childCalculate;

                            dblNetAmount = costTotal;
                            DecimalFormat df = new DecimalFormat("#.00");

                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);

                            participantsName.setText(verbiage_singular.toUpperCase()+" NAME : "+name);
                            time.setText("TIME : "+start_time+" to "+end_time);
                            duration.setText("DURATION : "+number_of_hours);

                            if(childCalculate==1){
                                perPackageInfo.setText("PER PACKAGE INFO : "+display_cost+" : "+session_count+" Session");
                            }else{
                                perPackageInfo.setText("PER PACKAGE INFO : "+display_cost+" : "+session_count+" Sessions");
                            }

                            numberOfPackages.setText("NUMBER OF PACKAGES : "+childCalculate);
                            totalPackageAmount.setText("TOTAL PACKAGE AMOUNT : "+df.format(dblNetAmount)+" "+academy_currency);

                            netAmount.setText(""+df.format(dblNetAmount)+" "+academy_currency);






                        } else {
//                            Toast.makeText(ParentBookMidWeekProceedScreen.this, message, Toast.LENGTH_SHORT).show();
                            // Show popup instead

                            final Dialog dialog = new Dialog(ParentBookMidWeekProceedScreen.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.parent_dialog_error);

                            TextView messageTextView = (TextView) dialog.findViewById(R.id.message);
                            TextView okayButton = (TextView) dialog.findViewById(R.id.okayButton);

                            messageTextView.setText(message);

                            okayButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookMidWeekProceedScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
//                calculateFees();
//                warningListView.setAdapter(new WarningAdapter(ParentBookMidWeekProceedScreen.this, warningList));
//                Utilities.setListViewHeightBasedOnChildren(warningListView);
//
//                if(autoPromo == true){
//                    promoCodeEditText.setText(promoCode);
//                    applyPromoCode.performClick();
//                }else{
//                    promoCodeEditText.setText("");
//                }

                break;

            case BOOK_SESSION:

                if (response == null) {
                    Toast.makeText(ParentBookMidWeekProceedScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            net_Amount = responseObject.getString("net_amount");
                            JSONObject dataobj = responseObject.getJSONObject("data");
                            ordersId = dataobj.getString("orders_id");
                           // location_id = dataobj.getString("location_id");

                            if(netAmount.getText().toString().trim().equals("0.00") || netAmount.getText().toString().trim().equals("0") || netAmount.getText().toString().trim().equals(".00 "+academy_currency) || netAmount.getText().toString().trim().equals("0.00 "+academy_currency) || netAmount.getText().toString().trim().equals("0 "+academy_currency)){
                                System.out.println("in_if_condition");
                                //Toast.makeText(ParentBookMidWeekProceedScreen.this, message, Toast.LENGTH_SHORT).show();
                                Intent mainScreen = new Intent(ParentBookMidWeekProceedScreen.this, ParentNetPaymentZeroScreen.class);
                                mainScreen.putExtra("orderID", ordersId);
                                startActivity(mainScreen);

                            }else{

                                System.out.println("in_else_1_condition");


                                System.out.println("netAmount::"+net_Amount+"::OrderID::"+ordersId);

//                            Toast.makeText(ParentBookMidWeekProceedScreen.this, message, Toast.LENGTH_SHORT).show();

                                // clear local stored value
//                            applicationContext.getBookAcademySummaryData().clear();
                                applicationContext.getAcademySessionChildBeanListing().clear();

                                Intent intent = new Intent(this, ParentPaymentGatewayWebViewScreen.class);
                                intent.putExtra(AvenuesParams.ACCESS_CODE, Utilities.ACCESS_CODE);
                                intent.putExtra(AvenuesParams.MERCHANT_ID, Utilities.MERCHANT_ID);
                                intent.putExtra(AvenuesParams.ORDER_ID, ordersId);

//                                intent.putExtra(AvenuesParams.CURRENCY, Utilities.CURRENCY);
                                intent.putExtra(AvenuesParams.CURRENCY, academy_currency);
                                intent.putExtra(AvenuesParams.AMOUNT, net_Amount);

//                            intent.putExtra(AvenuesParams.REDIRECT_URL, Utilities.BASE_URL + "payments/response_handler");
                                intent.putExtra(AvenuesParams.REDIRECT_URL, Utilities.REDIRECT_URL);
//                            intent.putExtra(AvenuesParams.CANCEL_URL, Utilities.BASE_URL + "payments/response_handler");
                                intent.putExtra(AvenuesParams.CANCEL_URL, Utilities.REDIRECT_URL);
                                intent.putExtra(AvenuesParams.RSA_KEY_URL, Utilities.BASE_URL + "payments/get_RSA");

                                startActivity(intent);

                            /*Intent mainScreen = new Intent(ParentBookMidWeekProceedScreen.this, ParentMainScreen.class);
                            mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainScreen);*/




//                                if(Utilities.isNetworkAvailable(ParentBookMidWeekProceedScreen.this)) {
//
//                                    List<NameValuePair> nameValuePairList = new ArrayList<>();
//                                    nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()+""));
//
//                                    String webServiceUrl = Utilities.BASE_URL + "payments/telr_status";
//
//                                    ArrayList<String> headers = new ArrayList<>();
//                                    headers.add("X-access-uid:"+loggedInUser.getId());
//                                    headers.add("X-access-token:"+loggedInUser.getToken());
//
//                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookMidWeekProceedScreen.this, nameValuePairList, TELR_STATUS, ParentBookMidWeekProceedScreen.this, headers);
//                                    postWebServiceAsync.execute(webServiceUrl);
//
//                                } else {
//                                    Toast.makeText(ParentBookMidWeekProceedScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//                                }


                            }



                        } else {
//                            Toast.makeText(ParentBookMidWeekProceedScreen.this, message, Toast.LENGTH_SHORT).show();
                            System.out.println("in_else_2_condition");
                            final Dialog dialog = new Dialog(ParentBookMidWeekProceedScreen.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.parent_dialog_error);

                            TextView messageTextView = (TextView) dialog.findViewById(R.id.message);
                            TextView okayButton = (TextView) dialog.findViewById(R.id.okayButton);

                            messageTextView.setText(message);

                            okayButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookMidWeekProceedScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;


            case TELR_STATUS:

                if (response == null) {
                    Toast.makeText(ParentBookMidWeekProceedScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        System.out.println("REsponseHERE::"+response);
                        JSONObject responseObject = new JSONObject(response);


                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {


//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString("orderID_for_telr", ordersId);
//                            editor.commit();
//
//                            Intent intent = new Intent(ParentBookMidWeekProceedScreen.this, WebviewActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//                            intent.putExtra(WebviewActivity.EXTRA_MESSAGE, getMobileRequest(netAmount.getText().toString(), ordersId));
//                            intent.putExtra(WebviewActivity.SUCCESS_ACTIVTY_CLASS_NAME, "com.abudhabicricket.application.telrSDK.SuccessTransationActivity");
//                            intent.putExtra(WebviewActivity.FAILED_ACTIVTY_CLASS_NAME, "com.abudhabicricket.application.telrSDK.FailedTransationActivity");
//                            intent.putExtra(WebviewActivity.IS_SECURITY_ENABLED, isSecurityEnabled);
//                            startActivity(intent);

                        }else{
                            Intent intent = new Intent(this, ParentPaymentGatewayWebViewScreen.class);
                            intent.putExtra(AvenuesParams.ACCESS_CODE, Utilities.ACCESS_CODE);
                            intent.putExtra(AvenuesParams.MERCHANT_ID, Utilities.MERCHANT_ID);
                            intent.putExtra(AvenuesParams.ORDER_ID, ordersId);

//                                intent.putExtra(AvenuesParams.CURRENCY, Utilities.CURRENCY);
                            intent.putExtra(AvenuesParams.CURRENCY, academy_currency);
                            intent.putExtra(AvenuesParams.AMOUNT, net_Amount);

//                            intent.putExtra(AvenuesParams.REDIRECT_URL, Utilities.BASE_URL + "payments/response_handler");
                            intent.putExtra(AvenuesParams.REDIRECT_URL, Utilities.REDIRECT_URL);
//                            intent.putExtra(AvenuesParams.CANCEL_URL, Utilities.BASE_URL + "payments/response_handler");
                            intent.putExtra(AvenuesParams.CANCEL_URL, Utilities.REDIRECT_URL);
                            intent.putExtra(AvenuesParams.RSA_KEY_URL, Utilities.BASE_URL + "payments/get_RSA");

                            startActivity(intent);

                            /*Intent mainScreen = new Intent(ParentBookMidWeekProceedScreen.this, ParentMainScreen.class);
                            mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainScreen);*/

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookMidWeekProceedScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;


            case APPLY_PROMO_CODE:

                if (response == null) {
                    Toast.makeText(ParentBookMidWeekProceedScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(ParentBookMidWeekProceedScreen.this, message, Toast.LENGTH_LONG).show();

                        if(status) {

                            String strDeductAmount = responseObject.getString("deduct_amt");
                            try{
                                promoCodeDeductAmount = Double.parseDouble(strDeductAmount);
                                System.out.println("promoCodeDeductAmount::"+promoCodeDeductAmount);

                                promoCodeDiscount.setText(strDeductAmount+" "+academy_currency);

                                System.out.println("NET_AMOUNT::"+dblNetAmount+"::PROMO_CODE::"+promoCodeDeductAmount);


                                dblNetAmount = dblNetAmount - promoCodeDeductAmount;




                                if(dblNetAmount == 0) {
                                    netAmount.setText("0.00 "+academy_currency);
                                    makePayment.setText("PROCEED");
                                } else {
//                                    netAmount.setText(decimalFormat.format(dblNetAmount)+" AED");

                                    //     roundedOffNetAmount = Math.round(dblNetAmount);

                                    System.out.println("HereValue::"+dblNetAmount);

                                    netAmount.setText(dblNetAmount+" "+academy_currency);
                                }

                            } catch (NumberFormatException e){
                                Toast.makeText(ParentBookMidWeekProceedScreen.this, "Invalid discount", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookMidWeekProceedScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void changeFonts(){
        title.setTypeface(linoType);
        lblGrandTotal.setTypeface(linoType);
        grandTotal.setTypeface(helvetica);
        lblRegistration.setTypeface(linoType);
        registration.setTypeface(helvetica);
        lblTournamentFee.setTypeface(linoType);
        tournamentFee.setTypeface(helvetica);
        lblTotal.setTypeface(linoType);
        netAmount.setTypeface(helvetica);
        bookMoreSessions.setTypeface(linoType);
        makePayment.setTypeface(linoType);

        participantsName.setTypeface(linoType);
        time.setTypeface(linoType);
        duration.setTypeface(linoType);
        perPackageInfo.setTypeface(linoType);
        numberOfPackages.setTypeface(linoType);
        totalPackageAmount.setTypeface(linoType);
        packageName.setTypeface(linoType);
        location.setTypeface(linoType);
    }



    public void clearRow(){
        if(deleteRow.equals("true")){
            promoCodeDiscount.setText("0.00 "+academy_currency);


            promoCodeEditText.setText("");
            strPromoCode = "";

            promoCodeDeductAmount = 0;
            deleteRow = "false";

        }
    }
}