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
import com.dunvant.application.beans.AcademySessionChildBean;
import com.dunvant.application.beans.AcademySessionDateBean;
import com.dunvant.application.beans.AcademySessionSummaryBean;
import com.dunvant.application.beans.BookingHistoryDiscountBean;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.ExtraFeesBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentAcademySessionSummaryAdapter;
import com.dunvant.application.parent.adapters.WarningAdapter;
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

public class ParentBookAcademySummaryScreen extends AppCompatActivity implements IWebServiceCallback{
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
    ListView summaryListView;
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

    ParentAcademySessionSummaryAdapter parentAcademySessionSummaryAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_book_academy_summary_screen);

        applicationContext = (ApplicationContext) getApplication();
        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        clearAll = findViewById(R.id.clearAll);
        scrollView = findViewById(R.id.scrollView);
        summaryListView = findViewById(R.id.summaryListView);
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

        String academy_currency = sharedPreferences.getString("academy_currency", null);
        promoCodeDiscount.setText("0.00 "+academy_currency);

//        discountDescription = findViewById(R.id.discountDescription);

        changeFonts();

        parentAcademySessionSummaryAdapter = new ParentAcademySessionSummaryAdapter(ParentBookAcademySummaryScreen.this, summaryList, ParentBookAcademySummaryScreen.this, summaryListView /*, discountBean*/);
        summaryListView.setAdapter(parentAcademySessionSummaryAdapter);

        /*Set<String> keys = applicationContext.getBookAcademySummaryData().keySet();
        for (String key : keys){
            //System.out.println(key+" :: "+applicationContext.getBookAcademySummaryData().get(key));
        }*/

        getSummaryDetails();

        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(summaryList == null || summaryList.isEmpty()) {
                    Toast.makeText(ParentBookAcademySummaryScreen.this, "Please select a session", Toast.LENGTH_SHORT).show();
                    return;
                }

                feesAppliedFor.clear();

                //System.out.println("User Id "+loggedInUser.getId());
                //System.out.println("Academies Id "+loggedInUser.getAcademiesId());
                //System.out.println("Address Id "+1);
                //System.out.println("Total Amount "+dblNetAmount);
                //System.out.println("Tax Amount "+0);
                //System.out.println("Discount Amount "+0);
                //System.out.println("Net Amount "+dblNetAmount);

                String strBookingDetails = "[";
                for(AcademySessionSummaryBean summaryBean : summaryList) {
                    String sessionId = summaryBean.getSessionId();

                    String childrenIds = "[";
                    for (ChildBean childBean : summaryBean.getChildrenList()) {

                        childrenIds += "\""+childBean.getId()+"\",";

                        /*if(bookedPitchSummaryBean.isTrialSelected()) {
                            childrenInfo += "{\"child_id\":\""+childBean.getId()+"\",\"reg_fee\":\"0\",\"tournament_fee\":\"0\"},";
                        } else {

                            boolean alreadyApplied = false;
                            for (String childId : feesAppliedFor) {
                                if (childId.equalsIgnoreCase(childBean.getId())) {
                                    alreadyApplied = true;
                                    break;
                                }
                            }

                            if(alreadyApplied) {
                                childrenInfo += "{\"child_id\":\""+childBean.getId()+"\",\"reg_fee\":\"0\",\"tournament_fee\":\"0\"},";
                            } else {
                                childrenInfo += "{\"child_id\":\""+childBean.getId()+"\",\"reg_fee\":\""+childBean.getRegistrationFee()+"\",\"tournament_fee\":\""+childBean.getTournamentFee()+"\"},";
                                feesAppliedFor.add(childBean.getId());
                            }
                        }*/

                    }
                    /*if (childrenInfo != null && childrenInfo.length() > 0 && childrenInfo.charAt(childrenInfo.length()-1)==',') {
                        childrenInfo = childrenInfo.substring(0, childrenInfo.length()-1);
                    }
                    childrenInfo += "]";*/

                    if (childrenIds != null && childrenIds.length() > 0 && childrenIds.charAt(childrenIds.length()-1)==',') {
                        childrenIds = childrenIds.substring(0, childrenIds.length()-1);
                    }
                    childrenIds += "]";


//                    String sessionCost = bookedPitchSummaryBean.getCost();
//                    String sessionPayment = bookedPitchSummaryBean.getSessionCost()+"";
//                    boolean isTrial = bookedPitchSummaryBean.isTrialSelected();

                    String strIsTrial = summaryBean.isTrialSelected() ? "1":"0";

                    String strBookingDates = "[";
                    for(AcademySessionDateBean dateBean : summaryBean.getAvailableDatesList()) {
                        if(dateBean.isSelected()) {
//                            strBookingDates += "{\"booking_date\":\""+dateBean.getSessionDate()+"\"},";
                            strBookingDates += "\""+dateBean.getDate()+"\",";
                        }
                    }

                    if (strBookingDates != null && strBookingDates.length() > 0 && strBookingDates.charAt(strBookingDates.length()-1)==',') {
                        strBookingDates = strBookingDates.substring(0, strBookingDates.length()-1);
                    }

                    strBookingDates += "]";

//                    strBookingDetails += "{\"sessions_id\":\""+sessionId+"\",\"children_details\":"+childrenInfo+",\"session_cost\":\""+sessionCost+"\",\"session_payment\":\""+sessionPayment+"\",\"is_trial\":\""+strIsTrial+"\",\"booking_dates\":"+strBookingDates+"},";

                    strBookingDetails += "{\"sessions_id\": \""+sessionId+"\",\"children_ids\": "+childrenIds+",\"is_trial\": "+strIsTrial+",\"booking_dates\": "+strBookingDates+"},";

                }

                if (strBookingDetails != null && strBookingDetails.length() > 0 && strBookingDetails.charAt(strBookingDetails.length()-1)==',') {
                    strBookingDetails = strBookingDetails.substring(0, strBookingDetails.length()-1);
                }

                strBookingDetails += "]";

                //System.out.println("Booking Details "+strBookingDetails);

                if(Utilities.isNetworkAvailable(ParentBookAcademySummaryScreen.this)) {

//                    String strNetAmount = decimalFormat.format(dblNetAmount);
//                    String strNetAmount = roundedOffNetAmount+"";
                    String strNetAmount = dblNetAmount+"";

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
//                    nameValuePairList.add(new BasicNameValuePair("academies_id", loggedInUser.getAcademiesId()));
//                    nameValuePairList.add(new BasicNameValuePair("users_id", loggedInUser.getId()));
//                    nameValuePairList.add(new BasicNameValuePair("user_addresses_id", "1"));


//                    nameValuePairList.add(new BasicNameValuePair("total_amount", dblNetAmount+""));




//                    nameValuePairList.add(new BasicNameValuePair("tax_amount", "0"));
//                    nameValuePairList.add(new BasicNameValuePair("discount_amount", "0"));
//                    nameValuePairList.add(new BasicNameValuePair("net_amount", dblNetAmount+""));
                    nameValuePairList.add(new BasicNameValuePair("booking_details", strBookingDetails));
                    nameValuePairList.add(new BasicNameValuePair("promo_code", strPromoCode));
                    String academy_currency = sharedPreferences.getString("academy_currency", null);
                    if(netAmount.getText().toString().trim().equals("0.00 "+academy_currency)){
                        nameValuePairList.add(new BasicNameValuePair("payment_mode", "offline"));
                        nameValuePairList.add(new BasicNameValuePair("total_amount", "0"));
                    }else{
                        nameValuePairList.add(new BasicNameValuePair("total_amount", strNetAmount));
                    }

                    String webServiceUrl = Utilities.BASE_URL + Utilities.bookSession;

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookAcademySummaryScreen.this, nameValuePairList, BOOK_SESSION, ParentBookAcademySummaryScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(ParentBookAcademySummaryScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
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

                String academy_currency = sharedPreferences.getString("academy_currency", null);
                promoCodeDiscount.setText("0.00 "+academy_currency);
                if(dblNetAmount == 0) {
                    netAmount.setText("0.00 "+academy_currency);
                } else {
                    netAmount.setText(decimalFormat.format(dblNetAmount)+" "+academy_currency);
                }

                if(strPromoCode == null || strPromoCode.isEmpty()) {
                    Toast.makeText(ParentBookAcademySummaryScreen.this, "Please enter Promo Code", Toast.LENGTH_SHORT).show();
                } else {
                    if(Utilities.isNetworkAvailable(ParentBookAcademySummaryScreen.this)) {

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("amount", dblNetAmount+""));
                        nameValuePairList.add(new BasicNameValuePair("module_type", "1"));
                        nameValuePairList.add(new BasicNameValuePair("promo_code", strPromoCode));

                        nameValuePairList.add(new BasicNameValuePair("sessions_detail", sessionsDetail));

                        String webServiceUrl = Utilities.BASE_URL + "sessions/apply_promo_code";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:"+loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookAcademySummaryScreen.this, nameValuePairList, APPLY_PROMO_CODE, ParentBookAcademySummaryScreen.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(ParentBookAcademySummaryScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
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

                String academy_currency = sharedPreferences.getString("academy_currency", null);
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

                final Dialog dialog = new Dialog(ParentBookAcademySummaryScreen.this);
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
                Intent mainScreen = new Intent(ParentBookAcademySummaryScreen.this, ParentMainScreen.class);
                mainScreen.putExtra("screenName","bookSession");
                mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainScreen);*/

                Intent screenOne = new Intent(ParentBookAcademySummaryScreen.this, ParentAcademyListingWithFiltersScreen.class);
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
        String academy_currency = sharedPreferences.getString("academy_currency", null);
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
        summaryListView.setAdapter(null);
        summaryListView.setVisibility(View.GONE);
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
        String academy_currency = sharedPreferences.getString("academy_currency", null);
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
        if(Utilities.isNetworkAvailable(ParentBookAcademySummaryScreen.this)) {

            sessionsDetail = "[";
//            Set<String> keys = applicationContext.getBookAcademySummaryData().keySet();

            ArrayList<AcademySessionChildBean> beanListing = applicationContext.getAcademySessionChildBeanListing();


            for (AcademySessionChildBean bean : beanListing){
                sessionsDetail += "{\"sessions_id\":\""+bean.getSessionId()+"\",\"child_ids\":\""+bean.getChildrenIdsCSV()+"\",\"is_trial\":\"0\"},";
            }

            if (sessionsDetail != null && sessionsDetail.length() > 0 && sessionsDetail.charAt(sessionsDetail.length()-1)==',') {
                sessionsDetail = sessionsDetail.substring(0, sessionsDetail.length()-1);
            }

            sessionsDetail += "]";

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("sessions_detail", sessionsDetail));

            String webServiceUrl = Utilities.BASE_URL + "sessions/summary";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            System.out.println("TokenHere:: "+loggedInUser.getToken()+" :: uid:: "+loggedInUser.getId());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookAcademySummaryScreen.this, nameValuePairList, GET_SUMMARY_DATA, ParentBookAcademySummaryScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookAcademySummaryScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_SUMMARY_DATA:
                summaryList.clear();
                warningList.clear();

                if (response == null) {
                    Toast.makeText(ParentBookAcademySummaryScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
//                            JSONObject discountObject = responseObject.getJSONObject("discount");

                            AcademySessionSummaryBean summaryBean;
                            for (int i=0; i<dataArray.length(); i++) {
                                JSONObject summaryObject = dataArray.getJSONObject(i);
                                summaryBean = new AcademySessionSummaryBean();

                                summaryBean.setSessionDetailId(summaryObject.getString("session_details_id"));
                                summaryBean.setSessionDetailTitle(summaryObject.getString("session_detail_title"));
                                summaryBean.setSessionId(summaryObject.getString("sessions_id"));
                                summaryBean.setCoachingProgramName(summaryObject.getString("coaching_programs_name"));
                                summaryBean.setTermName(summaryObject.getString("terms_name"));
                                summaryBean.setLocationName(summaryObject.getString("locations_name"));
                                summaryBean.setGroupName(summaryObject.getString("group_name"));
                                summaryBean.setFromAge(summaryObject.getString("from_age"));
                                summaryBean.setToAge(summaryObject.getString("to_age"));
                                summaryBean.setDay(summaryObject.getString("day"));
                                summaryBean.setFromDate(summaryObject.getString("from_date"));
                                summaryBean.setToDate(summaryObject.getString("to_date"));
                                summaryBean.setStartTime(summaryObject.getString("start_time"));
                                summaryBean.setEndTime(summaryObject.getString("end_time"));
                                summaryBean.setShowStartTime(summaryObject.getString("start_time_formatted"));
                                summaryBean.setShowEndTime(summaryObject.getString("end_time_formatted"));
                                summaryBean.setNumberOfHours(summaryObject.getString("number_of_hours"));
                                summaryBean.setCost(summaryObject.getString("cost"));
                                summaryBean.setMaxLimit(summaryObject.getString("max_limit"));
                                summaryBean.setIsSelectiveAllowed(summaryObject.getString("is_selective_allowed"));
                                summaryBean.setIsTrial(summaryObject.getString("is_trial"));
                                summaryBean.setTrialCount(summaryObject.getInt("trial_count"));
                                summaryBean.setDayLabel(summaryObject.getString("day_label"));

                                warningList.add(summaryObject.getString("warning_message"));

                                ArrayList<AcademySessionDateBean> availableDatesList = new ArrayList<>();
                                JSONArray availableDatesArray = summaryObject.getJSONArray("available_dates");
                                for (int j=0; j<availableDatesArray.length(); j++) {
                                    JSONObject dateObject = availableDatesArray.getJSONObject(j);
                                    AcademySessionDateBean dateBean = new AcademySessionDateBean();
                                    dateBean.setDate(dateObject.getString("value"));
                                    dateBean.setShowDate(dateObject.getString("readable_date"));
//                                    dateBean.setSelected(false);
                                    dateBean.setSelected(true);
                                    availableDatesList.add(dateBean);
                                }
                                summaryBean.setAvailableDatesList(availableDatesList);

                                ArrayList<ChildBean> childrenListing = new ArrayList<>();
                                ChildBean childBean;
                                JSONArray childrenArray = summaryObject.getJSONArray("children_register");
                                for(int j=0; j<childrenArray.length(); j++) {
                                    JSONObject childObject = childrenArray.getJSONObject(j);
                                    childBean = new ChildBean();

                                    childBean.setId(childObject.getString("id"));
                                    childBean.setFullName(childObject.getString("name"));
                                    childBean.setRegistrationFee(childObject.getString("registration_fee"));
                                    childBean.setTournamentFee(childObject.getString("tournament_fee"));

                                    JSONArray extraFeesArray = childObject.getJSONArray("extra_fees");
                                    ArrayList<ExtraFeesBean> extraFeesList = new ArrayList<>();
                                    ExtraFeesBean extraFeesBean;
                                    for(int k=0;k<extraFeesArray.length();k++){
                                        JSONObject extraFeesObject = extraFeesArray.getJSONObject(k);
                                        extraFeesBean = new ExtraFeesBean();

                                        extraFeesBean.setFieldName(extraFeesObject.getString("field_name"));
                                        extraFeesBean.setLabel(extraFeesObject.getString("label"));
                                        extraFeesBean.setValue(extraFeesObject.getString("value"));

                                        extraFeesList.add(extraFeesBean);
                                    }
                                    childBean.setExtraFeesList(extraFeesList);

                                    childrenListing.add(childBean);
                                }
                                summaryBean.setChildrenList(childrenListing);

                                ArrayList<BookingHistoryDiscountBean> discountsListing = new ArrayList<>();
                                BookingHistoryDiscountBean bookingHistoryDiscountBean;
                                JSONArray discountArray = summaryObject.getJSONArray("per_child_discount");
                                for(int j=0;j<discountArray.length();j++) {
                                    JSONObject discountObject = discountArray.getJSONObject(j);
                                    bookingHistoryDiscountBean = new BookingHistoryDiscountBean();

                                    bookingHistoryDiscountBean.setDiscountLabel(discountObject.getString("discount_label"));
                                    bookingHistoryDiscountBean.setDiscountDescription(discountObject.getString("discount_desc"));
                                    bookingHistoryDiscountBean.setDiscountValue(discountObject.getString("discount_value"));
                                    bookingHistoryDiscountBean.setDiscountCode(discountObject.getString("discount_code"));
                                    bookingHistoryDiscountBean.setChildId(discountObject.getString("child_id"));

                                    for(ChildBean child : childrenListing) {
                                        if(child.getId().equalsIgnoreCase(bookingHistoryDiscountBean.getChildId())) {
                                            bookingHistoryDiscountBean.setChildName(child.getFullName());
                                            break;
                                        }
                                    }

                                    discountsListing.add(bookingHistoryDiscountBean);
                                }
                                summaryBean.setDiscountList(discountsListing);


                                try{
                                    autoPromo = true;
                                    JSONObject suggestedPromocode = responseObject.getJSONObject("suggested_promocode");
                                    summaryBean.setPromoCodeId(suggestedPromocode.getString("promo_code_id"));
                                    summaryBean.setPromocode(suggestedPromocode.getString("promo_code"));
                                    promoCode = suggestedPromocode.getString("promo_code");
                                    summaryBean.setPromoUsage(suggestedPromocode.getString("promo_usage"));
                                    summaryBean.setPromoUsageCount(suggestedPromocode.getString("promo_usage_count"));
                                    summaryBean.setSids(suggestedPromocode.getString("sids"));

                                }catch (Exception e){
                                    autoPromo = false;
                                    promoCode = "";
                                    summaryBean.setPromoCodeId("");
                                    summaryBean.setPromocode("");
                                    summaryBean.setPromoUsage("");
                                    summaryBean.setPromoUsageCount("");
                                    summaryBean.setSids("");
                                }

                                summaryList.add(summaryBean);
                            }



//                            discountBean.setDiscountLabel(discountObject.getString("discount_label"));
//                            discountBean.setDiscountDescription(discountObject.getString("discount_desc"));
//                            discountBean.setDiscountValue(discountObject.getString("discount_value"));

//                            discountDescription.setText(discountBean.getDiscountDescription());

                        } else {
//                            Toast.makeText(ParentBookAcademySummaryScreen.this, message, Toast.LENGTH_SHORT).show();
                            // Show popup instead

                            final Dialog dialog = new Dialog(ParentBookAcademySummaryScreen.this);
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

                            // hide book more and make a payment
                            bookMoreSessions.setVisibility(View.GONE);
                            makePayment.setVisibility(View.GONE);
                            bottomLinearLayout.setVisibility(View.GONE);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookAcademySummaryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                parentAcademySessionSummaryAdapter.notifyDataSetChanged();
                Utilities.setListViewHeightBasedOnChildren(summaryListView);
                calculateFees();
                warningListView.setAdapter(new WarningAdapter(ParentBookAcademySummaryScreen.this, warningList));
                Utilities.setListViewHeightBasedOnChildren(warningListView);

                if(autoPromo == true){
                    promoCodeEditText.setText(promoCode);
                    applyPromoCode.performClick();
                }else{
                    promoCodeEditText.setText("");
                }

                break;

            case BOOK_SESSION:

                if (response == null) {
                    Toast.makeText(ParentBookAcademySummaryScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            academy_currency = sharedPreferences.getString("academy_currency", null);
                            System.out.println("netAmount::"+netAmount.getText().toString());
                            System.out.println("stored value:: 0.00 "+academy_currency);

                            net_Amount = responseObject.getString("net_amount");
                            ordersId = responseObject.getString("orders_id");

                            if(netAmount.getText().toString().trim().equals(".00 "+academy_currency) || netAmount.getText().toString().trim().equals("0.00 "+academy_currency) || netAmount.getText().toString().trim().equals("0 "+academy_currency)){
                                System.out.println("in_if_condition");
                                //Toast.makeText(ParentBookAcademySummaryScreen.this, message, Toast.LENGTH_SHORT).show();
                                Intent mainScreen = new Intent(ParentBookAcademySummaryScreen.this, ParentNetPaymentZeroScreen.class);
                                mainScreen.putExtra("orderID", ordersId);
                                startActivity(mainScreen);

                            }else{

                                System.out.println("in_else_1_condition");


                                System.out.println("netAmount"+net_Amount+""+ordersId);

//                            Toast.makeText(ParentBookAcademySummaryScreen.this, message, Toast.LENGTH_SHORT).show();

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

                            /*Intent mainScreen = new Intent(ParentBookAcademySummaryScreen.this, ParentMainScreen.class);
                            mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainScreen);*/




//                                if(Utilities.isNetworkAvailable(ParentBookAcademySummaryScreen.this)) {
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
//                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookAcademySummaryScreen.this, nameValuePairList, TELR_STATUS, ParentBookAcademySummaryScreen.this, headers);
//                                    postWebServiceAsync.execute(webServiceUrl);
//
//                                } else {
//                                    Toast.makeText(ParentBookAcademySummaryScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//                                }


                            }



                        } else {
//                            Toast.makeText(ParentBookAcademySummaryScreen.this, message, Toast.LENGTH_SHORT).show();
                            System.out.println("in_else_2_condition");
                            final Dialog dialog = new Dialog(ParentBookAcademySummaryScreen.this);
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
                        Toast.makeText(ParentBookAcademySummaryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;


            case TELR_STATUS:

                if (response == null) {
                    Toast.makeText(ParentBookAcademySummaryScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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
//                            Intent intent = new Intent(ParentBookAcademySummaryScreen.this, WebviewActivity.class);
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

                            /*Intent mainScreen = new Intent(ParentBookAcademySummaryScreen.this, ParentMainScreen.class);
                            mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainScreen);*/

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookAcademySummaryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;


            case APPLY_PROMO_CODE:

                if (response == null) {
                    Toast.makeText(ParentBookAcademySummaryScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(ParentBookAcademySummaryScreen.this, message, Toast.LENGTH_LONG).show();

                        if(status) {

                            String strDeductAmount = responseObject.getString("deduct_amt");
                            try{
                                promoCodeDeductAmount = Double.parseDouble(strDeductAmount);
                                System.out.println("promoCodeDeductAmount::"+promoCodeDeductAmount);

                                String academy_currency = sharedPreferences.getString("academy_currency", null);
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
                                Toast.makeText(ParentBookAcademySummaryScreen.this, "Invalid discount", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookAcademySummaryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
    }

//    private MobileRequest getMobileRequest(String netAmount, String ordersId) {
//
//        MobileRequest mobile = new MobileRequest();
//        mobile.setStore(TelrMainActivity.STORE_ID);                       // Store ID
//        mobile.setKey(TelrMainActivity.KEY);                              // Authentication Key : The Authentication Key will be supplied by Telr as part of the Mobile API setup process after you request that this integration type is enabled for your account. This should not be stored permanently within the App.
//        App app = new App();
//        app.setId(loggedInUser.getId());                          // Application installation ID
//        app.setName("Abu Dhabi Cricket");                    // Application name
//        app.setUser(ordersId);                           // Application user ID : Your reference for the customer/user that is running the App. This should relate to their account within your systems.
//        app.setVersion(Utilities.SYSTEM_VERSION);                         // Application version
//        app.setSdk("Android");
//        mobile.setApp(app);
//        Tran tran = new Tran();
//        tran.setTest("0");                              // Test mode : Test mode of zero indicates a live transaction. If this is set to any other value the transaction will be treated as a test.
//        tran.setType("sale");                           /* Transaction type
//                                                            'auth'   : Seek authorisation from the card issuer for the amount specified. If authorised, the funds will be reserved but will not be debited until such time as a corresponding capture command is made. This is sometimes known as pre-authorisation.
//                                                            'sale'   : Immediate purchase request. This has the same effect as would be had by performing an auth transaction followed by a capture transaction for the full amount. No additional capture stage is required.
//                                                            'verify' : Confirm that the card details given are valid. No funds are reserved or taken from the card.
//                                                        */
//        tran.setClazz("paypage");                       // Transaction class only 'paypage' is allowed on mobile, which means 'use the hosted payment page to capture and process the card details'
//        tran.setCartid(String.valueOf(new BigInteger(128, new Random()))); //// Transaction cart ID : An example use of the cart ID field would be your own transaction or order reference.
//        tran.setDescription("Transaction Android, Order ID : "+ordersId);         // Transaction description
//        tran.setCurrency("AED");                        // Transaction currency : Currency must be sent as a 3 character ISO code. A list of currency codes can be found at the end of this document. For voids or refunds, this must match the currency of the original transaction.
//        tran.setAmount(netAmount);                         // Transaction amount : The transaction amount must be sent in major units, for example 9 dollars 50 cents must be sent as 9.50 not 950. There must be no currency symbol, and no thousands separators. Thedecimal part must be separated using a dot.
//        //tran.setRef(???);                           // (Optinal) Previous transaction reference : The previous transaction reference is required for any continuous authority transaction. It must contain the reference that was supplied in the response for the original transaction.
//        tran.setLangauge("en");                        // (Optinal) default is en -> English
//        mobile.setTran(tran);
//        Billing billing = new Billing();
//        Address address = new Address();
//        address.setCity("");                       // City : the minimum required details for a transaction to be processed
//        address.setCountry("AE");                       // Country : Country must be sent as a 2 character ISO code. A list of country codes can be found at the end of this document. the minimum required details for a transaction to be processed
//        address.setRegion("");                     // Region
//        address.setLine1("");                 // Street address – line 1: the minimum required details for a transaction to be processed
//        //address.setLine2("SIT G=Towe");               // (Optinal)
//        //address.setLine3("SIT G=Towe");               // (Optinal)
//        //address.setZip("SIT G=Towe");                 // (Optinal)
//        billing.setAddress(address);
//        Name name = new Name();
//        name.setFirst(loggedInUser.getFirstName());                          // Forename : the minimum required details for a transaction to be processed
//        name.setLast(loggedInUser.getLastName());                          // Surname : the minimum required details for a transaction to be processed
//        name.setTitle("");                           // Title
//        billing.setName(name);
//        billing.setEmail(loggedInUser.getEmail());                 // TODO: Insert your email here : the minimum required details for a transaction to be processed.
//        billing.setPhone("");                // Phone number, required if enabled in your merchant dashboard.
//        mobile.setBilling(billing);
//        return mobile;
//
//    }


    public void clearRow(){
        if(deleteRow.equals("true")){
            String academy_currency = sharedPreferences.getString("academy_currency", null);
            promoCodeDiscount.setText("0.00 "+academy_currency);


            promoCodeEditText.setText("");
            strPromoCode = "";

            promoCodeDeductAmount = 0;
            deleteRow = "false";

        }
    }
}