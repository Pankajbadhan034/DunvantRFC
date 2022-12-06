package com.dunvant.application.parent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.BookingHistoryDiscountBean;
import com.dunvant.application.beans.BookingHistoryRefundDetailsBean;
import com.dunvant.application.beans.FeesBean;
import com.dunvant.application.beans.SessionHistoryBean;
import com.dunvant.application.beans.SurplusChargesBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentBookedSessionNewListingAdapter;
import com.dunvant.application.parent.adapters.ParentFeesListingAdapter;
import com.dunvant.application.parent.adapters.ParentInlineDiscountsListingAdapter;
import com.dunvant.application.parent.adapters.ParentSessionRefundListingAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ParentBookedMidWeekHistoryDetailScreen extends AppCompatActivity implements IWebServiceCallback {
    String childConverted="";
    String numberOfPackage;
    String orderAmountStr;
    String midweek_sessions_id;
    String start_time_formatted;
    String end_time_formatted;
    String session_cost;
    String package_title;
    String terms_name;
    String locations_name;
    String group_name;
    String from_age;
    String is_trial;
    String session_count;
    String childnames;
    TextView detail;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    TextView title;
    ImageView backButton;
    TextView orderId;
    ListView bookedSessionsListView;
    TextView lblSessionPayment;
    TextView grandTotal;
    ListView feesListView;
    TextView lblTotal;
    TextView totalIncludingFees;
    ListView inlineDiscountsListView;
    TextView lblDiscount;
    TextView discount;
    TextView lblOrderAmount;
    TextView orderAmount;
    TextView lblTotalRefundAmount;
    TextView totalRefundAmount;
    TextView lblSurplusCharges;
    TextView surplusCharges;
    TextView lblNetPayable;
    TextView netAmount;
    LinearLayout latestOrderDetailsLinear;
    TextView latestOrderId;
    TextView lblNewSessionAmount;
    TextView latestOrderAmount;
    ListView refundListView;

    SessionHistoryBean clickedOnSession;

    private final String GET_SESSION_DETAIL = "GET_SESSION_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_booked_mid_week_history_detail_activity);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);
        orderId = (TextView) findViewById(R.id.orderId);
        bookedSessionsListView = (ListView) findViewById(R.id.bookedSessionsListView);
        lblSessionPayment = (TextView) findViewById(R.id.lblSessionPayment);
        grandTotal = (TextView) findViewById(R.id.grandTotal);
        feesListView = (ListView) findViewById(R.id.feesListView);
        lblTotal = (TextView) findViewById(R.id.lblTotal);
        totalIncludingFees = (TextView) findViewById(R.id.totalIncludingFees);
        inlineDiscountsListView = (ListView) findViewById(R.id.inlineDiscountsListView);
        lblDiscount = (TextView) findViewById(R.id.lblDiscount);
        discount = (TextView) findViewById(R.id.discount);
        lblOrderAmount = (TextView) findViewById(R.id.lblOrderAmount);
        orderAmount = (TextView) findViewById(R.id.orderAmount);
        lblTotalRefundAmount = (TextView) findViewById(R.id.lblTotalRefundAmount);
        totalRefundAmount = (TextView) findViewById(R.id.totalRefundAmount);
        lblSurplusCharges = findViewById(R.id.lblSurplusCharges);
        surplusCharges = findViewById(R.id.surplusCharges);
        lblNetPayable = (TextView) findViewById(R.id.lblNetPayable);
        netAmount = (TextView) findViewById(R.id.netAmount);
        latestOrderDetailsLinear = (LinearLayout) findViewById(R.id.latestOrderDetailsLinear);
        latestOrderId = (TextView) findViewById(R.id.latestOrderId);
        lblNewSessionAmount = (TextView) findViewById(R.id.lblNewSessionAmount);
        latestOrderAmount = (TextView) findViewById(R.id.latestOrderAmount);
        refundListView = (ListView) findViewById(R.id.refundListView);
        detail = findViewById(R.id.detail);

        title.setTypeface(linoType);
        orderId.setTypeface(helvetica);
        lblSessionPayment.setTypeface(helvetica);
        grandTotal.setTypeface(helvetica);
        lblTotal.setTypeface(helvetica);
        totalIncludingFees.setTypeface(helvetica);
        lblDiscount.setTypeface(helvetica);
        discount.setTypeface(helvetica);
        lblOrderAmount.setTypeface(helvetica);
        orderAmount.setTypeface(helvetica);
        lblTotalRefundAmount.setTypeface(helvetica);
        totalRefundAmount.setTypeface(helvetica);
        lblSurplusCharges.setTypeface(helvetica);
        surplusCharges.setTypeface(helvetica);
        lblNetPayable.setTypeface(helvetica);
        netAmount.setTypeface(helvetica);
        latestOrderId.setTypeface(helvetica);
        lblNewSessionAmount.setTypeface(helvetica);
        latestOrderAmount.setTypeface(helvetica);
        detail.setTypeface(linoType);

        Intent intent = getIntent();
        if (intent != null) {
            clickedOnSession = (SessionHistoryBean) intent.getSerializableExtra("clickedOnSession");
            getSessionDetail();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showData() {
        orderId.setText("ORDER ID : #"+clickedOnSession.getOrderId());

//        bookedSessionsListView.setAdapter(new ParentBookedSessionsListingAdapter(ParentBookedMidWeekHistoryDetailScreen.this, clickedOnSession.getOrderSessionsList()));

        bookedSessionsListView.setAdapter(new ParentBookedSessionNewListingAdapter(ParentBookedMidWeekHistoryDetailScreen.this, clickedOnSession.getOrderSessionsListNew(), clickedOnSession.getRefundDetailsList()));
        Utilities.setListViewHeightBasedOnChildren(bookedSessionsListView);

        String academy_currency = sharedPreferences.getString("academy_currency", null);
        grandTotal.setText(clickedOnSession.getTotalAmount()+" "+academy_currency);
//        registration.setText(clickedOnSession.getRegistrationFee()+" AED");
//        tournamentFee.setText(clickedOnSession.getTournamentFee()+" AED");

        feesListView.setAdapter(new ParentFeesListingAdapter(ParentBookedMidWeekHistoryDetailScreen.this, clickedOnSession.getFeesList()));
        Utilities.setListViewHeightBasedOnChildren(feesListView);

//        totalIncludingFees.setText(clickedOnSession.getTotalIncludingFees()+" "+academy_currency);
        totalIncludingFees.setText(clickedOnSession.getTotalAmount()+" "+academy_currency);

        inlineDiscountsListView.setAdapter(new ParentInlineDiscountsListingAdapter(ParentBookedMidWeekHistoryDetailScreen.this, clickedOnSession.getInlineDiscountsList()));
        Utilities.setListViewHeightBasedOnChildren(inlineDiscountsListView);

        discount.setText(clickedOnSession.getDiscountAmount()+" "+academy_currency);
        orderAmount.setText(orderAmountStr+" "+academy_currency);
        totalRefundAmount.setText(clickedOnSession.getRefundAmount()+" "+academy_currency);
        surplusCharges.setText(clickedOnSession.getSurplusChargesBean().getChargeValue()+" "+academy_currency);
        netAmount.setText(clickedOnSession.getNetAmount()+" "+academy_currency);

        if(clickedOnSession.isChildMoved()){
            latestOrderId.setText("ORDER #"+clickedOnSession.getLatestOrderId()+" DETAILS");
            latestOrderAmount.setText(clickedOnSession.getLatestTotal()+" "+academy_currency);
        } else {
            latestOrderDetailsLinear.setVisibility(View.GONE);
        }

        refundListView.setAdapter(new ParentSessionRefundListingAdapter(ParentBookedMidWeekHistoryDetailScreen.this, clickedOnSession.getRefundDetailsList()));
        Utilities.setListViewHeightBasedOnChildren(refundListView);
    }

    private void getSessionDetail() {
        if(Utilities.isNetworkAvailable(ParentBookedMidWeekHistoryDetailScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("order_id", clickedOnSession.getOrderId()));

            String webServiceUrl = Utilities.BASE_URL + "midweek_session/bsession_detail";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookedMidWeekHistoryDetailScreen.this, nameValuePairList, GET_SESSION_DETAIL, ParentBookedMidWeekHistoryDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookedMidWeekHistoryDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_SESSION_DETAIL:

                if (response == null) {
                    Toast.makeText(ParentBookedMidWeekHistoryDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONObject sessionObject = responseObject.getJSONObject("data");
                            clickedOnSession.setOrderId(sessionObject.getString("id"));
                            clickedOnSession.setAcademiesId(sessionObject.getString("academies_id"));
                            clickedOnSession.setOrderParentId(sessionObject.getString("orders_parent_id"));
                            clickedOnSession.setPaymentId(sessionObject.getString("payment_id"));
                            clickedOnSession.setUserId(sessionObject.getString("users_id"));
                            clickedOnSession.setUserAddressId(sessionObject.getString("user_addresses_id"));
                            clickedOnSession.setTotalAmount(sessionObject.getString("total"));
                            clickedOnSession.setTaxAmount(sessionObject.getString("tax"));
                            clickedOnSession.setDiscountAmount(sessionObject.getString("discount"));
                            clickedOnSession.setNetAmount(sessionObject.getString("net_amount"));
                            clickedOnSession.setOrderDate(sessionObject.getString("order_date"));
                            clickedOnSession.setNotes(sessionObject.getString("notes"));
                            clickedOnSession.setState(sessionObject.getString("state"));
                            clickedOnSession.setCreatedAt(sessionObject.getString("created_at"));
                            clickedOnSession.setDiscountPercentage(sessionObject.getString("discount_percentage"));
                            clickedOnSession.setOriginalAmount(sessionObject.getString("original_amount"));
//                            clickedOnSession.setTotalIncludingFees(sessionObject.getString("total_including_fees"));
                            clickedOnSession.setOrderAmount(sessionObject.getString("order_amount"));
                            orderAmountStr = sessionObject.getString("order_amount");

                            clickedOnSession.setChildMoved(false);
                            clickedOnSession.setLatestOrderId(sessionObject.getString("id"));
                            clickedOnSession.setLatestTotal(sessionObject.getString("total"));

                            ArrayList<FeesBean> feesList1 = new ArrayList<>();
                            FeesBean feesBean1;
                            JSONObject feesArray1 = sessionObject.getJSONObject("order_fees");

                                feesBean1 = new FeesBean();
                                feesBean1.setName(feesArray1.getString("registration_fee"));
                                feesBean1.setValue(feesArray1.getString("tournament_fee"));

                                feesList1.add(feesBean1);

                            clickedOnSession.setFeesList(feesList1);

                            JSONObject orderSessionsObj = sessionObject.getJSONObject("order_sessions");
                            JSONObject bookedSessions = orderSessionsObj.getJSONObject("booked_sessions");

                             midweek_sessions_id = bookedSessions.getString("midweek_sessions_id");
                            start_time_formatted = bookedSessions.getString("start_time_formatted");
                            end_time_formatted = bookedSessions.getString("end_time_formatted");
                            session_cost = bookedSessions.getString("session_cost");
                            package_title = bookedSessions.getString("package_title");
                            terms_name = bookedSessions.getString("terms_name");
                            locations_name = bookedSessions.getString("locations_name");
                            group_name = bookedSessions.getString("group_name");
                            from_age = bookedSessions.getString("from_age");
                            is_trial = bookedSessions.getString("is_trial");
                            session_count = bookedSessions.getString("session_count");
                            childnames = bookedSessions.getString("childnames");

                            JSONArray orderSessionArray = bookedSessions.getJSONArray("childnames_converted");

                            numberOfPackage = ""+orderSessionArray.length();
                            for(int i=0;i<orderSessionArray.length();i++){
                                JSONObject orderSessionObject = orderSessionArray.getJSONObject(i);
                                if(i==0){
                                    childConverted = childConverted + orderSessionObject.getString("name");
                                }else{
                                    childConverted = childConverted+", " + orderSessionObject.getString("name");
                                }

                            }



//                            ArrayList<OrderSessionBeanNew> orderSessionsListNew = new ArrayList<>();
//                            OrderSessionBeanNew orderSessionBeanNew;
//                            JSONArray orderSessionArray = sessionObject.getJSONArray("order_sessions");
//                            for(int i=0;i<orderSessionArray.length();i++){
//                                JSONObject orderSessionObject = orderSessionArray.getJSONObject(i);
//                                orderSessionBeanNew = new OrderSessionBeanNew();
//
//                                ChildrenInfoBean childrenInfoBean = new ChildrenInfoBean();
//                                JSONObject childrenInfoObject = orderSessionObject.getJSONObject("children_info");
//                                childrenInfoBean.setId(childrenInfoObject.getString("id"));
//                                childrenInfoBean.setName(childrenInfoObject.getString("name"));
//                                childrenInfoBean.setAge(childrenInfoObject.getString("age"));
//
//                                orderSessionBeanNew.setChildrenInfoBean(childrenInfoBean);
//
//                                JSONObject bookingDatesObject = orderSessionObject.getJSONObject("booking_dates");
//
//                                JSONObject sessionInfoObject = bookingDatesObject.getJSONObject("session_info");
//                                SessionInfoBean sessionInfoBean = new SessionInfoBean();
//                                sessionInfoBean.setSessionId(sessionInfoObject.getString("sessions_id"));
//                                sessionInfoBean.setStartTimeFormatted(sessionInfoObject.getString("start_time_formatted"));
//                                sessionInfoBean.setEndTimeFormatted(sessionInfoObject.getString("end_time_formatted"));
//                                sessionInfoBean.setSessionCost(sessionInfoObject.getString("session_cost"));
//                                sessionInfoBean.setCoachingProgramsName(sessionInfoObject.getString("coaching_programs_name"));
//                                sessionInfoBean.setTermsName(sessionInfoObject.getString("terms_name"));
//                                sessionInfoBean.setLocationsName(sessionInfoObject.getString("locations_name"));
//                                sessionInfoBean.setGroupName(sessionInfoObject.getString("group_name"));
//                                sessionInfoBean.setFromAge(sessionInfoObject.getString("from_age"));
//                                sessionInfoBean.setIsTrial(sessionInfoObject.getString("is_trial"));
//
//                                orderSessionBeanNew.setSessionInfoBean(sessionInfoBean);
//
//                                JSONArray bookingDatesArray = bookingDatesObject.getJSONArray("booking_dates");
//                                ArrayList<BookingDateBean> bookingDatesList = new ArrayList<>();
//                                BookingDateBean bookingDateBean;
//                                for(int j=0;j<bookingDatesArray.length();j++){
//                                    JSONObject datesObject = bookingDatesArray.getJSONObject(j);
//                                    bookingDateBean = new BookingDateBean();
//
//                                    bookingDateBean.setOrderSessionsId(datesObject.getString("order_sessions_id"));
//                                    bookingDateBean.setBookingDate(datesObject.getString("booking_date"));
//                                    bookingDateBean.setBookingDateFormatted(datesObject.getString("booking_date_formatted"));
//                                    bookingDateBean.setBookingDateDay(datesObject.getString("booking_date_day"));
//                                    bookingDateBean.setCost(datesObject.getString("cost"));
//                                    bookingDateBean.setTotalCost(datesObject.getString("total_cost"));
//                                    bookingDateBean.setNumberOfHours(datesObject.getString("number_of_hours"));
//                                    bookingDateBean.setSessionCost(datesObject.getString("session_cost"));
//                                    bookingDateBean.setChildName(datesObject.getString("childname"));
//                                    bookingDateBean.setChildId(datesObject.getString("child_id"));
//                                    bookingDateBean.setChildAge(datesObject.getString("child_age"));
//
//                                    bookingDatesList.add(bookingDateBean);
//                                }
//
//                                orderSessionBeanNew.setBookingDatesList(bookingDatesList);
//
//                                orderSessionsListNew.add(orderSessionBeanNew);
//                            }
//                            clickedOnSession.setOrderSessionsListNew(orderSessionsListNew);

                            // New code below

                            ArrayList<BookingHistoryDiscountBean> inlineDiscountsList = new ArrayList<>();
                            BookingHistoryDiscountBean bookingHistoryDiscountBean;
                            JSONArray inlineDiscountsArray = sessionObject.getJSONArray("discount_inline");
                            for(int i=0;i<inlineDiscountsArray.length();i++){
                                JSONObject discountObject = inlineDiscountsArray.getJSONObject(i);
                                bookingHistoryDiscountBean = new BookingHistoryDiscountBean();

                                bookingHistoryDiscountBean.setId(discountObject.getString("id"));
                                bookingHistoryDiscountBean.setOrdersId(discountObject.getString("orders_id"));
                                bookingHistoryDiscountBean.setDiscountLabel(discountObject.getString("discount_label"));
                                bookingHistoryDiscountBean.setDiscountValue(discountObject.getString("discount_value"));
                                bookingHistoryDiscountBean.setDeductType(discountObject.getString("deduct_type"));
                                bookingHistoryDiscountBean.setDeductValue(discountObject.getString("deduct_value"));
                                bookingHistoryDiscountBean.setCreatedAt(discountObject.getString("created_at"));

                                inlineDiscountsList.add(bookingHistoryDiscountBean);
                            }
                            clickedOnSession.setInlineDiscountsList(inlineDiscountsList);

                            SurplusChargesBean surplusChargesBean = new SurplusChargesBean();
                            if(sessionObject.getString("surplus_charges").equalsIgnoreCase("null")){
                                surplusChargesBean.setId("");
                                surplusChargesBean.setOrdersId("");
                                surplusChargesBean.setChargeLabel("");
                                surplusChargesBean.setChargeValue("0.00");
                                surplusChargesBean.setCreatedAt("");
                            } else {
                                JSONObject surplusObject = sessionObject.getJSONObject("surplus_charges");
                                surplusChargesBean.setId(surplusObject.getString("id"));
                                surplusChargesBean.setOrdersId(surplusObject.getString("orders_id"));
                                surplusChargesBean.setChargeLabel(surplusObject.getString("charge_label"));
                                surplusChargesBean.setChargeValue(surplusObject.getString("charge_value"));
                                surplusChargesBean.setCreatedAt(surplusObject.getString("created_at"));
                            }
                            clickedOnSession.setSurplusChargesBean(surplusChargesBean);

                            // Previous Order Summary

                            if(sessionObject.has("parent_order_summary")){
                                JSONObject parentOrderSummaryObject = sessionObject.getJSONObject("parent_order_summary");

                                clickedOnSession.setOrderId(parentOrderSummaryObject.getString("id"));
                                clickedOnSession.setAcademiesId(parentOrderSummaryObject.getString("academies_id"));
                                clickedOnSession.setOrderParentId(parentOrderSummaryObject.getString("orders_parent_id"));
                                clickedOnSession.setPaymentId(parentOrderSummaryObject.getString("payment_id"));
                                clickedOnSession.setUserId(parentOrderSummaryObject.getString("users_id"));
                                clickedOnSession.setUserAddressId(parentOrderSummaryObject.getString("user_addresses_id"));
                                clickedOnSession.setTotalAmount(parentOrderSummaryObject.getString("total"));
                                clickedOnSession.setTaxAmount(parentOrderSummaryObject.getString("tax"));
                                clickedOnSession.setDiscountAmount(parentOrderSummaryObject.getString("discount"));
                                clickedOnSession.setNetAmount(parentOrderSummaryObject.getString("net_amount"));
                                clickedOnSession.setOrderDate(parentOrderSummaryObject.getString("order_date"));
                                clickedOnSession.setNotes(parentOrderSummaryObject.getString("notes"));
                                clickedOnSession.setState(parentOrderSummaryObject.getString("state"));
                                clickedOnSession.setCreatedAt(parentOrderSummaryObject.getString("created_at"));
                                clickedOnSession.setDiscountPercentage(parentOrderSummaryObject.getString("discount_percentage"));
                                clickedOnSession.setOriginalAmount(parentOrderSummaryObject.getString("original_amount"));
                                if(parentOrderSummaryObject.has("total_including_fees")){
                                    clickedOnSession.setTotalIncludingFees(parentOrderSummaryObject.getString("total_including_fees"));
                                }else{
                                    clickedOnSession.setTotalIncludingFees(parentOrderSummaryObject.getString("total"));
                                }

                                clickedOnSession.setOrderAmount(parentOrderSummaryObject.getString("order_amount"));

                                clickedOnSession.setChildMoved(true);

//                                feesList = new ArrayList<>();
//                                feesArray = parentOrderSummaryObject.getJSONArray("order_fees");
//                                for(int i=0;i<feesArray.length();i++){
//                                    JSONObject feesObject = feesArray.getJSONObject(i);
//                                    feesBean = new FeesBean();
//                                    feesBean.setName(feesObject.getString("name"));
//                                    feesBean.setValue(feesObject.getString("value"));
//
//                                    feesList.add(feesBean);
//                                }
//                                clickedOnSession.setFeesList(feesList);

                                inlineDiscountsList = new ArrayList<>();
                                inlineDiscountsArray = parentOrderSummaryObject.getJSONArray("discount_inline");
                                for(int i=0;i<inlineDiscountsArray.length();i++){
                                    JSONObject discountObject = inlineDiscountsArray.getJSONObject(i);
                                    bookingHistoryDiscountBean = new BookingHistoryDiscountBean();

                                    bookingHistoryDiscountBean.setId(discountObject.getString("id"));
                                    bookingHistoryDiscountBean.setOrdersId(discountObject.getString("orders_id"));
                                    bookingHistoryDiscountBean.setDiscountLabel(discountObject.getString("discount_label"));
                                    bookingHistoryDiscountBean.setDiscountValue(discountObject.getString("discount_value"));
                                    bookingHistoryDiscountBean.setDeductType(discountObject.getString("deduct_type"));
                                    bookingHistoryDiscountBean.setDeductValue(discountObject.getString("deduct_value"));
                                    bookingHistoryDiscountBean.setCreatedAt(discountObject.getString("created_at"));

                                    inlineDiscountsList.add(bookingHistoryDiscountBean);
                                }
                                clickedOnSession.setInlineDiscountsList(inlineDiscountsList);
                            }
                            // Previous Order Summary ends

                            ArrayList<BookingHistoryRefundDetailsBean> refundDetailsList = new ArrayList<>();
                            BookingHistoryRefundDetailsBean refundDetailsBean;
                            JSONArray refundDetailsArray = responseObject.getJSONArray("refund_details");
                            for(int i=0;i<refundDetailsArray.length();i++){
                                JSONObject refundObject = refundDetailsArray.getJSONObject(i);
                                refundDetailsBean = new BookingHistoryRefundDetailsBean();

                                refundDetailsBean.setId(refundObject.getString("id"));
                                refundDetailsBean.setOrdersId(refundObject.getString("orders_id"));
                                refundDetailsBean.setAmount(refundObject.getString("amount"));
                                refundDetailsBean.setDeductType(refundObject.getString("deduct_type"));
                                refundDetailsBean.setDeductValue(refundObject.getString("deduct_value"));
                                refundDetailsBean.setNetAmount(refundObject.getString("net_amount"));
                                refundDetailsBean.setCreatedAt(refundObject.getString("created_at"));
                                refundDetailsBean.setCancelledSession(refundObject.getString("cancelled_session"));
                                refundDetailsBean.setRefundFee(refundObject.getString("refund_fee"));
                                refundDetailsBean.setInitialAmount(refundObject.getString("initial_amount"));

                                refundDetailsList.add(refundDetailsBean);
                            }
                            clickedOnSession.setRefundDetailsList(refundDetailsList);

                            // New code ends

                            showData();

//                            String midweek_sessions_id;
//                            String start_time_formatted;
//                            String end_time_formatted;
//                            String session_cost;
//                            String package_title;
//                            String terms_name;
//                            String locations_name;
//                            String group_name;
//                            String from_age;
//                            String is_trial;
//                            String session_count;
//                            String childnames;

                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                            detail.setText(verbiage_singular.toUpperCase()+" NAME : "+childConverted+"\n\n"
                                    +"PACKAGE NAME : "+package_title+"\n\n"
                                    +"LOCATION : "+locations_name+"\n\n"
                                    +"TERM : "+terms_name+"\n\n"
                                    +"TIME : "+start_time_formatted+" to "+end_time_formatted+"\n\n"
                            +"PER PACKAGE INFO : "+session_cost+" : "+session_count+" Sessions"+"\n\n"
                            +"NUMBER OF PACKAGES : "+numberOfPackage);

                        } else {
                            Toast.makeText(ParentBookedMidWeekHistoryDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookedMidWeekHistoryDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}