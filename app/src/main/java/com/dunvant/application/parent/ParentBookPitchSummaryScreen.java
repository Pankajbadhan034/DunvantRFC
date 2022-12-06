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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.ApplicationContext;
import com.dunvant.application.R;
import com.dunvant.application.beans.PitchBookingDateBean;
import com.dunvant.application.beans.PitchBookingSlotsDataBean;
import com.dunvant.application.beans.PitchDateBean;
import com.dunvant.application.beans.PitchSummaryDataBean;
import com.dunvant.application.beans.PitchTimeSlotBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentPitchSummaryAdapter;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ParentBookPitchSummaryScreen extends AppCompatActivity implements IWebServiceCallback{
    String net_Amount;
    String ordersId;
    ApplicationContext applicationContext;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    TextView clearAll;
    ListView pitchesDetailListView;
    TextView lblAmount;
    TextView overallAmount;
    TextView lblBulkHourDiscount;
    TextView overallBulkHourDiscount;
    TextView lblAdditionalDiscount;
    TextView overallAdditionalDiscount;
    TextView lblNetAmount;
    TextView overallNetAmount;
    Button bookMoreButton;
    Button makePaymentButton;
    RelativeLayout amountRelative;
    RelativeLayout bulkHourDiscountRelative;
    RelativeLayout additionalDiscountRelative;
    RelativeLayout netAmountRelative;
    LinearLayout calculationsLinearLayout;

    LinearLayout promoCodeLinear;
    EditText promoCodeEditText;
    Button applyPromoCode;
    Button cancelPromoCode;
    LinearLayout promoCodeDiscountLinear;
    TextView lblPromoCodeDiscount;
    TextView promoCodeDiscount;

    private final String GET_PITCH_SUMMARY_DATA = "GET_PITCH_SUMMARY_DATA";
    private final String BOOK_PITCH = "BOOK_PITCH";
    private final String CLEAR_ALL = "CLEAR_ALL";

    ArrayList<PitchSummaryDataBean> summaryDataList = new ArrayList<>();

    ParentPitchSummaryAdapter parentPitchSummaryAdapter;

    String strPromoCode = "";
    double promoCodeDeductAmount = 0;
    private final String APPLY_PROMO_CODE = "APPLY_PROMO_CODE";

    double overallInitialAmountValue = 0;
    double overallBulkDiscountValue = 0;
    double overallAdditionalDiscountValue = 0;
    double overallNetAmountValue;
    long roundedOffNetAmount = 0;

    DecimalFormat decimalFormat = new DecimalFormat("#.00");

    boolean isBookMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_book_pitch_summary_screen);

        applicationContext = (ApplicationContext) getApplication();
        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        clearAll = (TextView) findViewById(R.id.clearAll);
        pitchesDetailListView = (ListView) findViewById(R.id.pitchesDetailListView);
        lblAmount = (TextView) findViewById(R.id.lblAmount);
        overallAmount = (TextView) findViewById(R.id.overallAmount);
        lblBulkHourDiscount = (TextView) findViewById(R.id.lblBulkHourDiscount);
        overallBulkHourDiscount = (TextView) findViewById(R.id.overallBulkHourDiscount);
        lblAdditionalDiscount = (TextView) findViewById(R.id.lblAdditionalDiscount);
        overallAdditionalDiscount = (TextView) findViewById(R.id.overallAdditionalDiscount);
        lblNetAmount = (TextView) findViewById(R.id.lblNetAmount);
        overallNetAmount = (TextView) findViewById(R.id.overallNetAmount);
        bookMoreButton = (Button) findViewById(R.id.bookMoreButton);
        makePaymentButton = (Button) findViewById(R.id.makePaymentButton);
        amountRelative = (RelativeLayout) findViewById(R.id.amountRelative);
        bulkHourDiscountRelative = (RelativeLayout) findViewById(R.id.bulkHourDiscountRelative);
        additionalDiscountRelative = (RelativeLayout) findViewById(R.id.additionalDiscountRelative);
        netAmountRelative = (RelativeLayout) findViewById(R.id.netAmountRelative);
        calculationsLinearLayout = findViewById(R.id.calculationsLinearLayout);

        promoCodeLinear = findViewById(R.id.promoCodeLinear);
        promoCodeEditText = findViewById(R.id.promoCodeEditText);
        promoCodeEditText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        applyPromoCode = findViewById(R.id.applyPromoCode);
        cancelPromoCode = findViewById(R.id.cancelPromoCode);
        promoCodeDiscountLinear = findViewById(R.id.promoCodeDiscountLinear);
        lblPromoCodeDiscount = findViewById(R.id.lblPromoCodeDiscount);
        promoCodeDiscount = findViewById(R.id.promoCodeDiscount);

        final String academy_currency = sharedPreferences.getString("academy_currency", null);
        promoCodeDiscount.setText("0.00 "+academy_currency);

        parentPitchSummaryAdapter = new ParentPitchSummaryAdapter(ParentBookPitchSummaryScreen.this, summaryDataList, ParentBookPitchSummaryScreen.this, pitchesDetailListView);
        pitchesDetailListView.setAdapter(parentPitchSummaryAdapter);

        changeFonts();
        getPitchDetailSummaryData();
//        getPitchDetailData();

        makePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(summaryDataList == null || summaryDataList.isEmpty()) {
                    Toast.makeText(ParentBookPitchSummaryScreen.this, "Please select a Pitch", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONArray pitchArray = new JSONArray();
                JSONObject pitchObject;
                for(PitchSummaryDataBean pitchSummaryDataBean: summaryDataList) {
                    pitchObject = new JSONObject();

                    try {
                        pitchObject.put("pitch_id", pitchSummaryDataBean.getPitchId());

//                        pitchObject.put("is_full_pitch", pitchSummaryDataBean.getIsFullPitch());

                        if(pitchSummaryDataBean.getIsFullPitch().equalsIgnoreCase("true") || pitchSummaryDataBean.getIsFullPitch().equalsIgnoreCase("1")){
                            pitchObject.put("is_full_pitch", "1");
                        } else {
                            pitchObject.put("is_full_pitch", "0");
                        }

                        JSONArray bookingsArray = new JSONArray();
                        JSONObject bookingObject;
                        for(PitchBookingDateBean pitchBookingDateBean: pitchSummaryDataBean.getBookingDatesList()){
                            bookingObject = new JSONObject();
                            bookingObject.put("booking_date", pitchBookingDateBean.getBookingDate());
                            bookingObject.put("from_time", pitchBookingDateBean.getFromTime());
                            bookingObject.put("to_time", pitchBookingDateBean.getToTime());

                            bookingsArray.put(bookingObject);
                        }
                        pitchObject.put("bookings", bookingsArray);

                        pitchArray.put(pitchObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookPitchSummaryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                if(Utilities.isNetworkAvailable(ParentBookPitchSummaryScreen.this)) {

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("pitch_booking", pitchArray.toString()));
                    nameValuePairList.add(new BasicNameValuePair("promo_code", strPromoCode));

                    if(overallNetAmount.getText().toString().trim().equals("0.00 "+academy_currency)){
                        nameValuePairList.add(new BasicNameValuePair("payment_mode", "offline"));
                    }else{
                        nameValuePairList.add(new BasicNameValuePair("payment_mode", ""));
                    }


                    String webServiceUrl = Utilities.BASE_URL + "pitch/book";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookPitchSummaryScreen.this, nameValuePairList, BOOK_PITCH, ParentBookPitchSummaryScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(ParentBookPitchSummaryScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }

            }
        });

        applyPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strPromoCode = promoCodeEditText.getText().toString().trim();

                overallNetAmountValue = overallNetAmountValue + promoCodeDeductAmount;
                promoCodeDeductAmount = 0;

                String academy_currency = sharedPreferences.getString("academy_currency", null);
                promoCodeDiscount.setText("0.00 "+academy_currency);
                if(overallNetAmountValue == 0) {
                    overallNetAmount.setText("0.00 "+academy_currency);
                } else {
                    overallNetAmount.setText(decimalFormat.format(overallNetAmountValue)+" "+academy_currency);
                }

                if(strPromoCode == null || strPromoCode.isEmpty()) {
                    Toast.makeText(ParentBookPitchSummaryScreen.this, "Please enter Promo Code", Toast.LENGTH_SHORT).show();
                } else {
                    if(Utilities.isNetworkAvailable(ParentBookPitchSummaryScreen.this)) {

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("amount", overallNetAmountValue+""));
                        nameValuePairList.add(new BasicNameValuePair("module_type", "3"));
                        nameValuePairList.add(new BasicNameValuePair("promo_code", strPromoCode));

                        String webServiceUrl = Utilities.BASE_URL + "sessions/apply_promo_code";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:"+loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookPitchSummaryScreen.this, nameValuePairList, APPLY_PROMO_CODE, ParentBookPitchSummaryScreen.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(ParentBookPitchSummaryScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cancelPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promoCodeEditText.setText("");
                strPromoCode = promoCodeEditText.getText().toString().trim();

                overallNetAmountValue = overallNetAmountValue + promoCodeDeductAmount;
                promoCodeDeductAmount = 0;

                String academy_currency = sharedPreferences.getString("academy_currency", null);
                promoCodeDiscount.setText("0.00 "+academy_currency);
                if(overallNetAmountValue == 0) {
                    overallNetAmount.setText("0.00 "+academy_currency);
                } else {
                    overallNetAmount.setText(decimalFormat.format(overallNetAmountValue)+" "+academy_currency);
                }
            }
        });

        bookMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBookMore = true;
                clearWebService();
            }
        });

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ParentBookPitchSummaryScreen.this);
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

//                        clearAllLocalData();
                        isBookMore = false;
                        clearWebService();
                    }
                });

                dialog.show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void clearWebService(){
        if(Utilities.isNetworkAvailable(ParentBookPitchSummaryScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "pitch/cache_clear_all";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ParentBookPitchSummaryScreen.this, CLEAR_ALL, ParentBookPitchSummaryScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookPitchSummaryScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void clearAllLocalData(){
        applicationContext.getPitchBookingSlotsDataBeanListing().clear();
        summaryDataList.clear();

        pitchesDetailListView.setAdapter(null);
        pitchesDetailListView.setVisibility(View.GONE);

        calculationsLinearLayout.setVisibility(View.GONE);

        /*amountRelative.setVisibility(View.GONE);
        bulkHourDiscountRelative.setVisibility(View.GONE);
        additionalDiscountRelative.setVisibility(View.GONE);
        promoCodeLinear.setVisibility(View.GONE);
        promoCodeDiscountLinear.setVisibility(View.GONE);
        netAmountRelative.setVisibility(View.GONE);*/
    }

    public void updateListView(){
        if(summaryDataList.isEmpty()){
            applicationContext.getPitchBookingSlotsDataBeanListing().clear();

            pitchesDetailListView.setAdapter(null);
            pitchesDetailListView.setVisibility(View.GONE);

            calculationsLinearLayout.setVisibility(View.GONE);

            /*amountRelative.setVisibility(View.GONE);
            bulkHourDiscountRelative.setVisibility(View.GONE);
            additionalDiscountRelative.setVisibility(View.GONE);
            promoCodeLinear.setVisibility(View.GONE);
            promoCodeDiscountLinear.setVisibility(View.GONE);
            netAmountRelative.setVisibility(View.GONE);*/

        }
    }

    public void updateCalculations() {
        overallInitialAmountValue = 0;
        overallBulkDiscountValue = 0;
        overallAdditionalDiscountValue = 0;
        overallNetAmountValue = 0;

        for(PitchSummaryDataBean pitchSummaryDataBean: summaryDataList) {
            overallInitialAmountValue += pitchSummaryDataBean.getInitialAmountValue();
            overallBulkDiscountValue += Double.parseDouble(pitchSummaryDataBean.getBulkHourDiscountAmount());
            overallAdditionalDiscountValue += Double.parseDouble(pitchSummaryDataBean.getAdditionalDiscountAmount());

//            overallBulkDiscountValue += pitchSummaryDataBean.getBulkHourDiscountValue();
//            overallAdditionalDiscountValue += pitchSummaryDataBean.getAdditionalDiscountValue();
        }

        overallNetAmountValue = overallInitialAmountValue - overallBulkDiscountValue - overallAdditionalDiscountValue;

        String academy_currency = sharedPreferences.getString("academy_currency", null);
        overallAmount.setText(decimalFormat.format(overallInitialAmountValue)+" "+academy_currency);

        if(overallBulkDiscountValue == 0) {
            overallBulkHourDiscount.setText("0.00 "+academy_currency);
        } else {
            overallBulkHourDiscount.setText(decimalFormat.format(overallBulkDiscountValue)+" "+academy_currency);
        }

        if(overallAdditionalDiscountValue == 0){
            overallAdditionalDiscount.setText("0.00 "+academy_currency);
        } else {
            overallAdditionalDiscount.setText(decimalFormat.format(overallAdditionalDiscountValue)+" "+academy_currency);
        }

//        overallNetAmount.setText(decimalFormat.format(overallNetAmountValue)+" AED");
//        roundedOffNetAmount = Math.round(overallNetAmountValue);
//        overallNetAmount.setText(roundedOffNetAmount+" "+academy_currency);

        if(decimalFormat.format(overallNetAmountValue).equals(".00")){
            overallNetAmount.setText("0.00 "+academy_currency);
        }else{
            overallNetAmount.setText(decimalFormat.format(overallNetAmountValue)+" "+academy_currency);
        }
    }

    private void getPitchDetailSummaryData(){
        if(Utilities.isNetworkAvailable(ParentBookPitchSummaryScreen.this)) {
            ArrayList<PitchBookingSlotsDataBean> pitchBookingSlotsListing = applicationContext.getPitchBookingSlotsDataBeanListing();

            try{
                JSONArray mainArray = new JSONArray();

                for(PitchBookingSlotsDataBean pitchBookingSlotsDataBean: pitchBookingSlotsListing) {

                    JSONObject mainObject = new JSONObject();
//                    mainObject.put("pitch_id", pitchBookingSlotsDataBean.getPitchId());
                    JSONObject timeSlotsObject = new JSONObject();

                    for(PitchDateBean pitchDateBean: pitchBookingSlotsDataBean.getPitchDateBeenList()) {

                        JSONArray timeSlotsArray = new JSONArray();

                        for(PitchTimeSlotBean pitchTimeSlotBean : pitchDateBean.getTimeSlots()) {

                            JSONObject timeSlotObject = new JSONObject();

                            if(pitchTimeSlotBean.isSelected()) {
//                                timeSlotsArray.put(pitchTimeSlotBean.getTimeSlot());
                                timeSlotObject.put("time", pitchTimeSlotBean.getTimeSlot());
                                timeSlotObject.put("pitch_id", pitchTimeSlotBean.getPitchId());
                                timeSlotsArray.put(timeSlotObject);
                            }
                        }

                        timeSlotsObject.put(pitchDateBean.getDate(), timeSlotsArray);

                    }

                    mainObject.put("time_slots", timeSlotsObject);
                    if(pitchBookingSlotsDataBean.isFullPitch()){
                        mainObject.put("is_full_pitch", "1");
                    } else {
                        mainObject.put("is_full_pitch", "0");
                    }

                    mainArray.put(mainObject);
                }

                //System.out.println("array "+mainArray.toString());

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(new BasicNameValuePair("temp_pitch_data", mainArray.toString()));
                String webServiceUrl = Utilities.BASE_URL + "pitch/book_summary";

                ArrayList<String> headers = new ArrayList<>();
                headers.add("X-access-uid:"+loggedInUser.getId());
                headers.add("X-access-token:"+loggedInUser.getToken());

                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookPitchSummaryScreen.this, nameValuePairList, GET_PITCH_SUMMARY_DATA, ParentBookPitchSummaryScreen.this, headers);
                postWebServiceAsync.execute(webServiceUrl);

            } catch(Exception e) {
                Toast.makeText(ParentBookPitchSummaryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ParentBookPitchSummaryScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    /*private void getPitchDetailData() {
        if(Utilities.isNetworkAvailable(ParentBookPitchSummaryScreen.this)) {

            ArrayList<PitchBookingData> pitchBookingDataList = applicationContext.getPitchBookingDataList();

            JSONArray pitchesDataArray = new JSONArray();
            JSONObject pitchDataObject;

            for (PitchBookingData pitchBookingData : pitchBookingDataList) {
                pitchDataObject = new JSONObject();
                try {
                    pitchDataObject.put("pitch_id", pitchBookingData.getPitchId());
                    pitchDataObject.put("is_multiple", pitchBookingData.isMultiple() ? "1":"0");
                    pitchDataObject.put("from_date", pitchBookingData.getFromDate());
                    pitchDataObject.put("to_date", pitchBookingData.getToDate());
                    pitchDataObject.put("is_daily", pitchBookingData.isDaily() ? "1" : "0");
                    if (!pitchBookingData.isDaily()) {
                        pitchDataObject.put("weekdays", pitchBookingData.getWeekdays());
                    }
                    pitchDataObject.put("from_time", pitchBookingData.getFromTime());
                    pitchDataObject.put("to_time", pitchBookingData.getToTime());

                    pitchesDataArray.put(pitchDataObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ParentBookPitchSummaryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("pitch_detail", pitchesDataArray.toString()));

            String webServiceUrl = Utilities.BASE_URL + "pitch/availability";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookPitchSummaryScreen.this, nameValuePairList, GET_PITCH_SUMMARY_DATA, ParentBookPitchSummaryScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookPitchSummaryScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }*/

    public void deleteSlot(String pitchId, PitchBookingDateBean bookingDateBean){
        if(Utilities.isNetworkAvailable(ParentBookPitchSummaryScreen.this)) {
            try{
                JSONArray mainArray = new JSONArray();

                JSONObject jObject = new JSONObject();
                jObject.put("pitch_id", pitchId);
                jObject.put("bdate", bookingDateBean.getBookingDate());
                jObject.put("from_time", bookingDateBean.getFromTime());
                jObject.put("to_time", bookingDateBean.getToTime());

                mainArray.put(jObject);

                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(new BasicNameValuePair("remove_tslots", mainArray.toString()));

                String webServiceUrl = Utilities.BASE_URL + "pitch/remove_slots";

                ArrayList<String> headers = new ArrayList<>();
                headers.add("X-access-uid:"+loggedInUser.getId());
                headers.add("X-access-token:"+loggedInUser.getToken());

                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookPitchSummaryScreen.this, nameValuePairList, GET_PITCH_SUMMARY_DATA, ParentBookPitchSummaryScreen.this, headers);
                postWebServiceAsync.execute(webServiceUrl);
            } catch(JSONException e) {
                Toast.makeText(ParentBookPitchSummaryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(ParentBookPitchSummaryScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_PITCH_SUMMARY_DATA:

                summaryDataList.clear();

                if (response == null) {
                    Toast.makeText(ParentBookPitchSummaryScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            PitchSummaryDataBean summaryBean;
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject summaryObject = dataArray.getJSONObject(i);
                                summaryBean = new PitchSummaryDataBean();

                                summaryBean.setPitchId(summaryObject.getString("pitch_id"));
                                summaryBean.setPitchName(summaryObject.getString("pitch_name"));
                                summaryBean.setLocationName(summaryObject.getString("location_name"));
                                summaryBean.setFromDate(summaryObject.getString("from_date"));
                                summaryBean.setToDate(summaryObject.getString("to_date"));
//                                summaryBean.setPrices(summaryObject.getString("prices"));
//                                summaryBean.setBulkHoursDiscount(summaryObject.getString("bulk_hours_discount"));
//                                summaryBean.setBulkHours(summaryObject.getString("bulk_hours"));
//                                summaryBean.setAdditionalBookingDiscount(summaryObject.getString("additional_booking_discount"));
//                                summaryBean.setShowPrice(summaryObject.getString("price_display"));
                                summaryBean.setIsFullPitch(summaryObject.getString("is_full_pitch"));

                                JSONObject discountsObject = summaryObject.getJSONObject("discounts");
                                summaryBean.setBulkHourDiscountAmount(discountsObject.getString("bulk_hour_discount"));
                                summaryBean.setAdditionalDiscountAmount(discountsObject.getString("additional_discount"));
                                summaryBean.setAdditionalDiscountLabel(discountsObject.getString("additional_discount_label"));

                                JSONArray bookingsArray = summaryObject.getJSONArray("bookings");
                                ArrayList<PitchBookingDateBean> bookingDatesList = new ArrayList<>();
                                PitchBookingDateBean bookingDateBean;
                                for(int j=0; j<bookingsArray.length(); j++) {
                                    JSONObject bookingObject = bookingsArray.getJSONObject(j);
                                    bookingDateBean = new PitchBookingDateBean();

                                    bookingDateBean.setShowBookingDate(bookingObject.getString("booking_date_formatted"));
                                    bookingDateBean.setBookingDate(bookingObject.getString("booking_date"));
                                    bookingDateBean.setFromTime(bookingObject.getString("from_time"));
                                    bookingDateBean.setToTime(bookingObject.getString("to_time"));
                                    bookingDateBean.setTime(bookingObject.getString("time"));
                                    bookingDateBean.setInterval(bookingObject.getString("interval"));
                                    bookingDateBean.setAmount(bookingObject.getString("amount"));

                                    bookingDatesList.add(bookingDateBean);
                                }

                                summaryBean.setBookingDatesList(bookingDatesList);
//                                bookedPitchSummaryBean.setUnavailableDates(summaryObject.getString("unavailable"));

                                summaryDataList.add(summaryBean);
                            }

                            updateListView();

                        } else {

                            final Dialog dialog = new Dialog(ParentBookPitchSummaryScreen.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.parent_dialog_no_dates_found);

                            TextView text1 = (TextView) dialog.findViewById(R.id.text1);
                            TextView ok = (TextView) dialog.findViewById(R.id.ok);

                            text1.setText(message);

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();

                                    finish();

                                }
                            });

                            dialog.show();


//                            Toast.makeText(ParentBookPitchSummaryScreen.this, message, Toast.LENGTH_SHORT).show();
//                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookPitchSummaryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentPitchSummaryAdapter.notifyDataSetChanged();
                Utilities.setListViewHeightBasedOnChildren(pitchesDetailListView);

                break;

            case BOOK_PITCH:

                if(response == null) {
                    Toast.makeText(ParentBookPitchSummaryScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            String academy_currency = sharedPreferences.getString("academy_currency", null);
                            System.out.println("netAmount::"+overallNetAmount.getText().toString());
                            System.out.println("stored value:: 0.00 "+academy_currency);

                            net_Amount = responseObject.getString("net_amount");
                            ordersId = responseObject.getString("orders_id");
                            applicationContext.getPitchBookingSlotsDataBeanListing().clear();

                            if(overallNetAmount.getText().toString().trim().equals(".00 "+academy_currency) || overallNetAmount.getText().toString().trim().equals("0.00 "+academy_currency) || overallNetAmount.getText().toString().trim().equals("0 "+academy_currency)){
                                System.out.println("in_if_condition");
                                //Toast.makeText(ParentBookPitchSummaryScreen.this, message, Toast.LENGTH_SHORT).show();
                                Intent mainScreen = new Intent(ParentBookPitchSummaryScreen.this, ParentNetPaymentZeroScreen.class);
                                mainScreen.putExtra("orderID", ordersId);
                                startActivity(mainScreen);

                            }else {
                                Intent intent = new Intent(this, ParentPaymentGatewayWebViewScreen.class);
                                intent.putExtra(AvenuesParams.ACCESS_CODE, Utilities.ACCESS_CODE);
                                intent.putExtra(AvenuesParams.MERCHANT_ID, Utilities.MERCHANT_ID);
                                intent.putExtra(AvenuesParams.ORDER_ID, ordersId);

                                intent.putExtra(AvenuesParams.CURRENCY, academy_currency);

//                            intent.putExtra(AvenuesParams.CURRENCY, Utilities.CURRENCY);
                                intent.putExtra(AvenuesParams.AMOUNT, net_Amount);

//                            intent.putExtra(AvenuesParams.REDIRECT_URL, Utilities.BASE_URL + "payments/response_handler");
                                intent.putExtra(AvenuesParams.REDIRECT_URL, Utilities.REDIRECT_URL);
//                            intent.putExtra(AvenuesParams.CANCEL_URL, Utilities.BASE_URL + "payments/response_handler");
                                intent.putExtra(AvenuesParams.CANCEL_URL, Utilities.REDIRECT_URL);
                                intent.putExtra(AvenuesParams.RSA_KEY_URL, Utilities.BASE_URL + "payments/get_RSA");

                                startActivity(intent);

                            /*Intent mainScreen = new Intent(ParentBookPitchSummaryScreen.this, ParentMainScreen.class);
                            mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainScreen);*/
                            }



                        } else {
                            Toast.makeText(ParentBookPitchSummaryScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookPitchSummaryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case CLEAR_ALL:
                if(response == null) {
                    Toast.makeText(ParentBookPitchSummaryScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            if(isBookMore){
                                Intent mainScreen = new Intent(ParentBookPitchSummaryScreen.this, ParentMainScreen.class);
                                mainScreen.putExtra("screenName","bookPitch");
                                mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainScreen);
                            } else {
                                Toast.makeText(ParentBookPitchSummaryScreen.this, message, Toast.LENGTH_SHORT).show();
                                clearAllLocalData();
                            }
                        } else {
                            Toast.makeText(ParentBookPitchSummaryScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookPitchSummaryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case APPLY_PROMO_CODE:

                if (response == null) {
                    Toast.makeText(ParentBookPitchSummaryScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(ParentBookPitchSummaryScreen.this, message, Toast.LENGTH_LONG).show();

                        if(status) {

                            String strDeductAmount = responseObject.getString("deduct_amt");
                            try{
                                promoCodeDeductAmount = Double.parseDouble(strDeductAmount);

                                String academy_currency = sharedPreferences.getString("academy_currency", null);
                                promoCodeDiscount.setText(strDeductAmount+" "+academy_currency);
                                overallNetAmountValue = overallNetAmountValue - promoCodeDeductAmount;

                                if(overallNetAmountValue == 0) {
                                    overallNetAmount.setText("0.00 "+academy_currency);
                                } else {
//                                    overallNetAmount.setText(decimalFormat.format(overallNetAmountValue)+" AED");
//                                    roundedOffNetAmount = Math.round(overallNetAmountValue);

                                    DecimalFormat df2 = new DecimalFormat("#.##");
                                    overallNetAmountValue =Double.parseDouble( df2.format(overallNetAmountValue));

                                    overallNetAmount.setText(decimalFormat.format(overallNetAmountValue)+" "+academy_currency);
                                }

                            } catch (NumberFormatException e){
                                Toast.makeText(ParentBookPitchSummaryScreen.this, "Invalid discount", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookPitchSummaryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void changeFonts() {
        title.setTypeface(linoType);
        lblAmount.setTypeface(helvetica);
        overallAmount.setTypeface(helvetica);
        lblBulkHourDiscount.setTypeface(helvetica);
        overallBulkHourDiscount.setTypeface(helvetica);
        lblAdditionalDiscount.setTypeface(helvetica);
        overallAdditionalDiscount.setTypeface(helvetica);
        lblNetAmount.setTypeface(helvetica);
        overallNetAmount.setTypeface(helvetica);
        bookMoreButton.setTypeface(linoType);
        makePaymentButton.setTypeface(linoType);
    }
}