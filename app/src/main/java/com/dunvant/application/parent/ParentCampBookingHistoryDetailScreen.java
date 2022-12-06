package com.dunvant.application.parent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import com.dunvant.application.beans.BookingDateBean;
import com.dunvant.application.beans.BookingHistoryDiscountBean;
import com.dunvant.application.beans.BookingHistoryRefundDetailsBean;
import com.dunvant.application.beans.CampBean;
import com.dunvant.application.beans.CampBookingDetailsBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.CampOrderBean;
import com.dunvant.application.beans.SurplusChargesBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentCampBookingDetailListingAdapter;
import com.dunvant.application.parent.adapters.ParentInlineDiscountsListingAdapter;
import com.dunvant.application.parent.adapters.ParentSessionRefundListingAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParentCampBookingHistoryDetailScreen extends AppCompatActivity implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    ImageView campImage;
    TextView campName;
    TextView lblOrderId;
    TextView orderId;
    TextView lblLocations;
    TextView locationName;
    TextView lblDates;
    TextView dates;
    TextView lblSessions;
    TextView session;
    TextView lblDateOfBooking;
    TextView dateOfBooking;
    TextView lblNumberOfDays;
    TextView numberOfDays;
//    TextView bookedDates;
    TextView lblAgeGroup;
    TextView ageGroup;
    ListView bookingDetailsListView;
    TextView lblTotalAmount;
    TextView totalAmount;
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
    ListView refundListView;
    TextView rebook;
//    TextView ammend;

    CampOrderBean clickedOnCampOrder;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    private final String GET_CAMP_BOOKING_DETAILS = "GET_CAMP_BOOKING_DETAILS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_camp_booking_history_detail_screen);

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
        campImage = (ImageView) findViewById(R.id.campImage);
        campName = (TextView) findViewById(R.id.campName);
        lblOrderId = (TextView) findViewById(R.id.lblOrderId);
        orderId = (TextView) findViewById(R.id.orderId);
        lblLocations = (TextView) findViewById(R.id.lblLocations);
        locationName = (TextView) findViewById(R.id.locationName);
        lblDates = (TextView) findViewById(R.id.lblDates);
        dates = (TextView) findViewById(R.id.dates);
        lblSessions = (TextView) findViewById(R.id.lblSessions);
        session = (TextView) findViewById(R.id.session);
        lblDateOfBooking = (TextView) findViewById(R.id.lblDateOfBooking);
        dateOfBooking = (TextView) findViewById(R.id.dateOfBooking);
        lblNumberOfDays = (TextView) findViewById(R.id.lblNumberOfDays);
        numberOfDays = (TextView) findViewById(R.id.numberOfDays);
//        bookedDates = (TextView) findViewById(R.id.bookedDates);
        lblAgeGroup = (TextView) findViewById(R.id.lblAgeGroup);
        ageGroup = (TextView) findViewById(R.id.ageGroup);
        bookingDetailsListView = (ListView) findViewById(R.id.bookingDetailsListView);
        lblTotalAmount = (TextView) findViewById(R.id.lblTotalAmount);
        totalAmount = (TextView) findViewById(R.id.totalAmount);
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
        refundListView = (ListView) findViewById(R.id.refundListView);
        rebook = (TextView) findViewById(R.id.rebook);
//        ammend = (TextView) findViewById(R.id.ammend);

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentCampBookingHistoryDetailScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        changeFonts();

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnCampOrder = (CampOrderBean) intent.getSerializableExtra("clickedOnCampOrder");

            campName.setText(clickedOnCampOrder.getCampName());
            orderId.setText("#"+clickedOnCampOrder.getOrderId());
            locationName.setText(clickedOnCampOrder.getLocationName());
            dates.setText(clickedOnCampOrder.getCampFromDate()+" - "+clickedOnCampOrder.getCampToDate());
//            session.setText(clickedOnCampOrder.getCampSessionId());
            session.setText(clickedOnCampOrder.getShowFromTime()+" - "+clickedOnCampOrder.getShowToTime());
            dateOfBooking.setText(clickedOnCampOrder.getShowOrderDate());
            numberOfDays.setText(clickedOnCampOrder.getTotalBookedDays()+" Days");

//            bookedDates.setText(clickedOnCampOrder.getShowBookingDates());

            ageGroup.setText(clickedOnCampOrder.getGroupName());
            String academy_currency = sharedPreferences.getString("academy_currency", null);
            netAmount.setText(clickedOnCampOrder.getNetAmount()+" "+academy_currency);

            imageLoader.displayImage(clickedOnCampOrder.getFilePath(), campImage, options);

            if(clickedOnCampOrder.isRebookFlag()) {
                rebook.setVisibility(View.VISIBLE);
            } else {
                rebook.setVisibility(View.GONE);
            }

            getCampHistoryDetails();
        }

        rebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CampBean campBean = new CampBean();
                campBean.setCampId(clickedOnCampOrder.getCampId());

                CampLocationBean locationBean = new CampLocationBean();
                locationBean.setLocationId(clickedOnCampOrder.getLocationId());

                ArrayList<CampLocationBean> locationsList = new ArrayList<>();
                locationsList.add(locationBean);

                campBean.setLocationList(locationsList);

                Intent campDetailScreen = new Intent(ParentCampBookingHistoryDetailScreen.this, ParentCampDetailScreen.class);
                campDetailScreen.putExtra("clickedOnCamp", campBean);
                campDetailScreen.putExtra("locationPosition", 0);
                startActivity(campDetailScreen);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showData() {
        bookingDetailsListView.setAdapter(new ParentCampBookingDetailListingAdapter(ParentCampBookingHistoryDetailScreen.this, clickedOnCampOrder.getCampBookingDetailsList(), clickedOnCampOrder.getRefundDetailsList()));
        Utilities.setListViewHeightBasedOnChildren(bookingDetailsListView);

        String academy_currency = sharedPreferences.getString("academy_currency", null);
        totalAmount.setText(clickedOnCampOrder.getTotal()+" "+academy_currency);

        inlineDiscountsListView.setAdapter(new ParentInlineDiscountsListingAdapter(ParentCampBookingHistoryDetailScreen.this, clickedOnCampOrder.getInlineDiscountsList()));
        Utilities.setListViewHeightBasedOnChildren(inlineDiscountsListView);

        discount.setText(clickedOnCampOrder.getDiscount()+" "+academy_currency);
        orderAmount.setText(clickedOnCampOrder.getOrderAmount()+" "+academy_currency);
        totalRefundAmount.setText(clickedOnCampOrder.getRefundAmount()+" "+academy_currency);

        surplusCharges.setText(clickedOnCampOrder.getSurplusChargesBean().getChargeValue()+" "+academy_currency);

        refundListView.setAdapter(new ParentSessionRefundListingAdapter(ParentCampBookingHistoryDetailScreen.this, clickedOnCampOrder.getRefundDetailsList()));
        Utilities.setListViewHeightBasedOnChildren(refundListView);
    }

    private void getCampHistoryDetails() {
        if(Utilities.isNetworkAvailable(ParentCampBookingHistoryDetailScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("orders_id", clickedOnCampOrder.getOrderId()));

            String webServiceUrl = Utilities.BASE_URL + "camps/booking_history_detail";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentCampBookingHistoryDetailScreen.this, nameValuePairList, GET_CAMP_BOOKING_DETAILS, ParentCampBookingHistoryDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentCampBookingHistoryDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_CAMP_BOOKING_DETAILS:

                if(response == null) {
                    Toast.makeText(ParentCampBookingHistoryDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {

                            JSONObject dataObject = responseObject.getJSONObject("data");

                            clickedOnCampOrder.setTotal(dataObject.getString("total"));
                            clickedOnCampOrder.setDiscount(dataObject.getString("discount"));
                            clickedOnCampOrder.setOrderAmount(dataObject.getString("order_amount"));

                            JSONArray bookingDetailsArray = dataObject.getJSONArray("booking_details");
                            ArrayList<CampBookingDetailsBean> bookingDetailsList = new ArrayList<>();
                            CampBookingDetailsBean campBookingDetailsBean;
                            for(int i=0;i<bookingDetailsArray.length();i++){
                                JSONObject bookingDetailObject = bookingDetailsArray.getJSONObject(i);
                                campBookingDetailsBean = new CampBookingDetailsBean();

                                campBookingDetailsBean.setChildName(bookingDetailObject.getString("child_name"));

                                JSONArray datesArray = bookingDetailObject.getJSONArray("data");
                                ArrayList<BookingDateBean> bookingDatesList = new ArrayList<>();
                                BookingDateBean bookingDateBean;
                                for(int j=0;j<datesArray.length();j++){
                                    JSONObject bookingDateObject = datesArray.getJSONObject(j);
                                    bookingDateBean = new BookingDateBean();
                                    bookingDateBean.setChildName(bookingDateObject.getString("child_name"));
                                    bookingDateBean.setDobFormatted(bookingDateObject.getString("dob_formatted"));
                                    bookingDateBean.setCampsId(bookingDateObject.getString("camps_id"));
                                    bookingDateBean.setBookingDateFormatted(bookingDateObject.getString("booking_date_formatted"));
                                    bookingDateBean.setBookingDate(bookingDateObject.getString("booking_date"));
                                    bookingDateBean.setTotalCost(bookingDateObject.getString("total_cost"));
                                    bookingDateBean.setWeeklyDiscount(bookingDateObject.getString("weekly_discount"));
                                    bookingDateBean.setCost(bookingDateObject.getString("cost"));
                                    bookingDateBean.setAgeValue(bookingDateObject.getString("age_value"));
                                    bookingDateBean.setId(bookingDateObject.getString("id"));

                                    bookingDatesList.add(bookingDateBean);
                                }

                                campBookingDetailsBean.setBookingDatesList(bookingDatesList);

                                bookingDetailsList.add(campBookingDetailsBean);
                            }

                            clickedOnCampOrder.setCampBookingDetailsList(bookingDetailsList);

                            ArrayList<BookingHistoryDiscountBean> inlineDiscountsList = new ArrayList<>();
                            BookingHistoryDiscountBean bookingHistoryDiscountBean;
                            JSONArray inlineDiscountsArray = dataObject.getJSONArray("discount_inline");
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
                            clickedOnCampOrder.setInlineDiscountsList(inlineDiscountsList);

                            SurplusChargesBean surplusChargesBean = new SurplusChargesBean();
                            if(dataObject.getString("surplus_charges").equalsIgnoreCase("null")){
                                surplusChargesBean.setId("");
                                surplusChargesBean.setOrdersId("");
                                surplusChargesBean.setChargeLabel("");
                                surplusChargesBean.setChargeValue("0.00");
                                surplusChargesBean.setCreatedAt("");
                            } else {
                                JSONObject surplusObject = dataObject.getJSONObject("surplus_charges");
                                surplusChargesBean.setId(surplusObject.getString("id"));
                                surplusChargesBean.setOrdersId(surplusObject.getString("orders_id"));
                                surplusChargesBean.setChargeLabel(surplusObject.getString("charge_label"));
                                surplusChargesBean.setChargeValue(surplusObject.getString("charge_value"));
                                surplusChargesBean.setCreatedAt(surplusObject.getString("created_at"));
                            }
                            clickedOnCampOrder.setSurplusChargesBean(surplusChargesBean);

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
                            clickedOnCampOrder.setRefundDetailsList(refundDetailsList);

                            showData();
                        } else {
                            Toast.makeText(ParentCampBookingHistoryDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentCampBookingHistoryDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void changeFonts(){
        title.setTypeface(linoType);
        campName.setTypeface(helvetica);
        orderId.setTypeface(helvetica);
        locationName.setTypeface(helvetica);
        dates.setTypeface(helvetica);
        session.setTypeface(helvetica);
        dateOfBooking.setTypeface(helvetica);
        numberOfDays.setTypeface(helvetica);
//        bookedDates.setTypeface(helvetica);
        ageGroup.setTypeface(helvetica);
        netAmount.setTypeface(helvetica);
        rebook.setTypeface(helvetica);
        lblOrderId.setTypeface(helvetica);
        lblLocations.setTypeface(helvetica);
        lblDates.setTypeface(helvetica);
        lblSessions.setTypeface(helvetica);
        lblDateOfBooking.setTypeface(helvetica);
        lblNumberOfDays.setTypeface(helvetica);
        lblAgeGroup.setTypeface(helvetica);
        lblTotalAmount.setTypeface(helvetica);
        totalAmount.setTypeface(helvetica);
        lblDiscount.setTypeface(helvetica);
        discount.setTypeface(helvetica);
        lblOrderAmount.setTypeface(helvetica);
        orderAmount.setTypeface(helvetica);
        lblTotalRefundAmount.setTypeface(helvetica);
        totalRefundAmount.setTypeface(helvetica);
        lblNetPayable.setTypeface(helvetica);
    }
}