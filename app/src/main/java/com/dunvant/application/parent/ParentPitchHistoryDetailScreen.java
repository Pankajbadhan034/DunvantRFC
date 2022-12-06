package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.BookingHistoryDiscountBean;
import com.dunvant.application.beans.BookingHistoryRefundDetailsBean;
import com.dunvant.application.beans.PitchBookingBean;
import com.dunvant.application.beans.PitchHistoryBean;
import com.dunvant.application.beans.PitchHistoryDetailBean;
import com.dunvant.application.beans.BookedPitchSummaryBean;
import com.dunvant.application.beans.SurplusChargesBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentInlineDiscountsListingAdapter;
import com.dunvant.application.parent.adapters.ParentPitchHistoryDetailAdapter;
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

public class ParentPitchHistoryDetailScreen extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    TextView title;
    ImageView backButton;
    ListView pitchHistoryDetailListView;
    TextView lblAmount;
    TextView overallAmount;
    ListView inlineDiscountsListView;
//    TextView overallBulkHourDiscount;
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
    ListView refundListView;

    PitchHistoryBean clickedOnPitch;

    private final String GET_DETAILS = "GET_DETAILS";

    BookedPitchSummaryBean bookedPitchSummaryBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_pitch_history_detail_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);
        pitchHistoryDetailListView = (ListView) findViewById(R.id.pitchHistoryDetailListView);
        lblAmount = (TextView) findViewById(R.id.lblAmount);
        overallAmount = (TextView) findViewById(R.id.overallAmount);
        inlineDiscountsListView = (ListView) findViewById(R.id.inlineDiscountsListView);
//        overallBulkHourDiscount = (TextView) findViewById(R.id.overallBulkHourDiscount);
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
        refundListView = (ListView) findViewById(R.id.refundListView);

        title.setTypeface(linoType);
        lblAmount.setTypeface(helvetica);
        overallAmount.setTypeface(helvetica);
//        overallBulkHourDiscount.setTypeface(helvetica);
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

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnPitch = (PitchHistoryBean) intent.getSerializableExtra("clickedOnPitch");
            getDetails();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updateUI() {
        pitchHistoryDetailListView.setAdapter(new ParentPitchHistoryDetailAdapter(ParentPitchHistoryDetailScreen.this, bookedPitchSummaryBean.getPitchHistoryDetailListing(), bookedPitchSummaryBean.getRefundDetailsList()));
        Utilities.setListViewHeightBasedOnChildren(pitchHistoryDetailListView);

        String academy_currency = sharedPreferences.getString("academy_currency", null);
        overallAmount.setText(bookedPitchSummaryBean.getTotalCost()+" "+academy_currency);
        discount.setText(bookedPitchSummaryBean.getDiscountCost()+" "+academy_currency);
        orderAmount.setText(bookedPitchSummaryBean.getOrderAmount()+" "+academy_currency);
        totalRefundAmount.setText(bookedPitchSummaryBean.getRefundAmount()+" "+academy_currency);
        surplusCharges.setText(bookedPitchSummaryBean.getSurplusChargesBean().getChargeValue()+" "+academy_currency);
        netAmount.setText(bookedPitchSummaryBean.getNetAmount()+" "+academy_currency);

        //        overallBulkHourDiscount.setText(bookedPitchSummaryBean.getDiscountCost()+" AED");

        inlineDiscountsListView.setAdapter(new ParentInlineDiscountsListingAdapter(ParentPitchHistoryDetailScreen.this, bookedPitchSummaryBean.getInlineDiscountsList()));
        Utilities.setListViewHeightBasedOnChildren(inlineDiscountsListView);

        refundListView.setAdapter(new ParentSessionRefundListingAdapter(ParentPitchHistoryDetailScreen.this, bookedPitchSummaryBean.getRefundDetailsList()));
        Utilities.setListViewHeightBasedOnChildren(refundListView);
    }

    private void getDetails(){
        if(Utilities.isNetworkAvailable(ParentPitchHistoryDetailScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("orders_id", clickedOnPitch.getId()));

            String webServiceUrl = Utilities.BASE_URL + "pitch/booked_pitches_details";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentPitchHistoryDetailScreen.this, nameValuePairList, GET_DETAILS, ParentPitchHistoryDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentPitchHistoryDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_DETAILS:

                bookedPitchSummaryBean = new BookedPitchSummaryBean();

                if(response == null) {
                    Toast.makeText(ParentPitchHistoryDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");
                            JSONObject summaryObject = responseObject.getJSONObject("summary");

                            ArrayList<PitchHistoryDetailBean> pitchHistoryDetailListing = new ArrayList<>();

                            PitchHistoryDetailBean pitchHistoryDetailBean;
                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject pitchHistoryDetailObject = dataArray.getJSONObject(i);
                                pitchHistoryDetailBean = new PitchHistoryDetailBean();

                                pitchHistoryDetailBean.setPitchName(pitchHistoryDetailObject.getString("pitch_name"));
                                pitchHistoryDetailBean.setPitchId(pitchHistoryDetailObject.getString("pitch_id"));
                                pitchHistoryDetailBean.setLocationName(pitchHistoryDetailObject.getString("location_name"));
                                pitchHistoryDetailBean.setPitchPrices(pitchHistoryDetailObject.getString("pitch_prices"));
//                                pitchHistoryDetailBean.setPricesDisplay(pitchHistoryDetailObject.getString("price_display"));

                                JSONArray bookingsArray = pitchHistoryDetailObject.getJSONArray("bookings");
                                ArrayList<PitchBookingBean> pitchBookingList = new ArrayList<>();
                                PitchBookingBean pitchBookingBean;
                                for(int j=0;j<bookingsArray.length();j++){
                                    JSONObject pitchBookingObject = bookingsArray.getJSONObject(j);
                                    pitchBookingBean = new PitchBookingBean();
                                    pitchBookingBean.setId(pitchBookingObject.getString("id"));
                                    pitchBookingBean.setOrdersId(pitchBookingObject.getString("orders_id"));
                                    pitchBookingBean.setPitchesId(pitchBookingObject.getString("pitches_id"));
                                    pitchBookingBean.setBookingDate(pitchBookingObject.getString("booking_date"));
                                    pitchBookingBean.setFromTime(pitchBookingObject.getString("from_time"));
                                    pitchBookingBean.setToTime(pitchBookingObject.getString("to_time"));
                                    pitchBookingBean.setCost(pitchBookingObject.getString("cost"));
                                    pitchBookingBean.setTotalCost(pitchBookingObject.getString("total_cost"));
                                    pitchBookingBean.setCreatedAt(pitchBookingObject.getString("created_at"));
                                    pitchBookingBean.setShowBookingDate(pitchBookingObject.getString("booking_date_formatted"));
                                    pitchBookingBean.setTime(pitchBookingObject.getString("time"));
                                    pitchBookingBean.setInterval(pitchBookingObject.getString("interval"));

                                    pitchBookingList.add(pitchBookingBean);
                                }

                                pitchHistoryDetailBean.setBookingsList(pitchBookingList);

                                pitchHistoryDetailListing.add(pitchHistoryDetailBean);
                            }

                            bookedPitchSummaryBean.setPitchHistoryDetailListing(pitchHistoryDetailListing);
                            bookedPitchSummaryBean.setId(summaryObject.getString("id"));
                            bookedPitchSummaryBean.setTotalCost(summaryObject.getString("total_cost"));
                            bookedPitchSummaryBean.setDiscountCost(summaryObject.getString("discount_cost"));
                            bookedPitchSummaryBean.setNetAmount(summaryObject.getString("net_amount"));
                            bookedPitchSummaryBean.setOrderDate(summaryObject.getString("order_date"));
                            bookedPitchSummaryBean.setState(summaryObject.getString("state"));
                            bookedPitchSummaryBean.setRefundAmount(summaryObject.getString("refund_amount"));
                            bookedPitchSummaryBean.setOrderAmount(summaryObject.getString("order_amount"));

                            ArrayList<BookingHistoryDiscountBean> inlineDiscountsList = new ArrayList<>();
                            BookingHistoryDiscountBean bookingHistoryDiscountBean;
                            JSONArray inlineDiscountsArray = summaryObject.getJSONArray("discount_inline");
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
                            bookedPitchSummaryBean.setInlineDiscountsList(inlineDiscountsList);

                            SurplusChargesBean surplusChargesBean = new SurplusChargesBean();
                            if(summaryObject.getString("surplus_charges").equalsIgnoreCase("null")){
                                surplusChargesBean.setId("");
                                surplusChargesBean.setOrdersId("");
                                surplusChargesBean.setChargeLabel("");
                                surplusChargesBean.setChargeValue("0.00");
                                surplusChargesBean.setCreatedAt("");
                            } else {
                                JSONObject surplusObject = summaryObject.getJSONObject("surplus_charges");
                                surplusChargesBean.setId(surplusObject.getString("id"));
                                surplusChargesBean.setOrdersId(surplusObject.getString("orders_id"));
                                surplusChargesBean.setChargeLabel(surplusObject.getString("charge_label"));
                                surplusChargesBean.setChargeValue(surplusObject.getString("charge_value"));
                                surplusChargesBean.setCreatedAt(surplusObject.getString("created_at"));
                            }
                            bookedPitchSummaryBean.setSurplusChargesBean(surplusChargesBean);

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
                            bookedPitchSummaryBean.setRefundDetailsList(refundDetailsList);

                            updateUI();

                        } else {
                            Toast.makeText(ParentPitchHistoryDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentPitchHistoryDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
