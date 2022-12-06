package com.dunvant.application.parent.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.PitchHistoryBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.ParentBookedPitchesScreen;
import com.dunvant.application.parent.ParentPitchHistoryDetailScreen;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParentPitchHistoryListingAdapter extends BaseAdapter implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Typeface helvetica;
    Typeface linoType;

    Context context;
    ArrayList<PitchHistoryBean> pitchHistoryListing;
    LayoutInflater layoutInflater;
    //    ParentBookedPitchesFragment parentBookedPitchesFragment;
    ParentBookedPitchesScreen parentBookedPitchesScreen;

    private final String CANCEL_ORDER = "CANCEL_ORDER";

    public ParentPitchHistoryListingAdapter(Context context, ArrayList<PitchHistoryBean> pitchHistoryListing, ParentBookedPitchesScreen parentBookedPitchesScreen/*ParentBookedPitchesFragment parentBookedPitchesFragment*/) {
        this.context = context;
        this.pitchHistoryListing = pitchHistoryListing;
        this.layoutInflater = LayoutInflater.from(context);
        this.parentBookedPitchesScreen = parentBookedPitchesScreen;
//        this.parentBookedPitchesFragment = parentBookedPitchesFragment;

        helvetica = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
    }

    @Override
    public int getCount() {
        return pitchHistoryListing.size();
    }

    @Override
    public Object getItem(int position) {
        return pitchHistoryListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_pitch_history_item, null);

        LinearLayout mainLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
        TextView lblOrderId = (TextView) convertView.findViewById(R.id.lblOrderId);
        TextView orderId = (TextView) convertView.findViewById(R.id.orderId);
        TextView amount = (TextView) convertView.findViewById(R.id.amount);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView lblPitchAndLocation = (TextView) convertView.findViewById(R.id.lblPitchAndLocation);
        TextView cancelBooking = (TextView) convertView.findViewById(R.id.cancelBooking);
        ListView pitchItemListView = (ListView) convertView.findViewById(R.id.pitchItemListView);
        TextView refundAmount = (TextView) convertView.findViewById(R.id.refundAmount);
        TextView customDiscount = (TextView) convertView.findViewById(R.id.customDiscount);

        orderId.setTypeface(helvetica);
        amount.setTypeface(helvetica);
        date.setTypeface(helvetica);
        cancelBooking.setTypeface(linoType);

        final PitchHistoryBean pitchHistoryBean = pitchHistoryListing.get(position);
        orderId.setText("#" + pitchHistoryBean.getId());
        amount.setText(pitchHistoryBean.getDisplayTotalCost());
        date.setText(pitchHistoryBean.getOrderDate());

        String textColor;
        if (position % 2 == 0) {
            textColor = "white";
        } else {
            textColor = "black";
        }

        pitchItemListView.setAdapter(new ParentPitchItemAdapter(context, pitchHistoryBean.getPitchesList(), textColor));
        Utilities.setListViewHeightBasedOnChildren(pitchItemListView);
        refundAmount.setText("REFUND AMOUNT: " + pitchHistoryBean.getDisplayRefundAmount());
        customDiscount.setText("CUSTOM DISCOUNT: " + pitchHistoryBean.getDisplayCustomDiscount());

        switch (pitchHistoryBean.getState()) {
            case "0":
                orderId.setText(orderId.getText() + " (Failed)");
                break;
            case "1":
                orderId.setText(orderId.getText() + " (Active)");
                break;
            case "2":
                orderId.setText(orderId.getText() + " (Cancelled)");
                break;
        }

        if (pitchHistoryBean.isShowCancellation()) {
            cancelBooking.setVisibility(View.VISIBLE);
        } else {
            cancelBooking.setVisibility(View.GONE);
        }

        mainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pitchDetail = new Intent(context, ParentPitchHistoryDetailScreen.class);
                pitchDetail.putExtra("clickedOnPitch", pitchHistoryBean);
                context.startActivity(pitchDetail);
            }
        });

        cancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.parent_dialog_cancel_pitch);

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

                        if (Utilities.isNetworkAvailable(context)) {

                            List<NameValuePair> nameValuePairList = new ArrayList<>();
                            nameValuePairList.add(new BasicNameValuePair("orders_id", pitchHistoryBean.getId()));

                            String webServiceUrl = Utilities.BASE_URL + "pitch/cancellation";

                            ArrayList<String> headers = new ArrayList<>();
                            headers.add("X-access-uid:" + loggedInUser.getId());
                            headers.add("X-access-token:" + loggedInUser.getToken());

                            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, CANCEL_ORDER, ParentPitchHistoryListingAdapter.this, headers);
                            postWebServiceAsync.execute(webServiceUrl);

                        } else {
                            Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.show();
            }
        });

        if (position % 2 == 0) {
            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            lblOrderId.setTextColor(context.getResources().getColor(R.color.white));
            orderId.setTextColor(context.getResources().getColor(R.color.white));
            amount.setTextColor(context.getResources().getColor(R.color.white));
            date.setTextColor(context.getResources().getColor(R.color.white));
            lblPitchAndLocation.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            lblOrderId.setTextColor(context.getResources().getColor(R.color.black));
            orderId.setTextColor(context.getResources().getColor(R.color.black));
            amount.setTextColor(context.getResources().getColor(R.color.black));
            date.setTextColor(context.getResources().getColor(R.color.black));
            lblPitchAndLocation.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case CANCEL_ORDER:

                if (response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                        if (status) {
                            parentBookedPitchesScreen.getBookedPitchesListing();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}