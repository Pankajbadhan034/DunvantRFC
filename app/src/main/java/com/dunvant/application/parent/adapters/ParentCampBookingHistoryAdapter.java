package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CampOrderBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentCampBookingHistoryAdapter extends BaseAdapter {

    Context context;
    Typeface helvetica;
    Typeface linoType;
    ArrayList<CampOrderBean> bookedOrdersListing;
    LayoutInflater layoutInflater;

    public ParentCampBookingHistoryAdapter(Context context, ArrayList<CampOrderBean> bookedOrdersListing) {
        this.context = context;
        this.bookedOrdersListing = bookedOrdersListing;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return bookedOrdersListing.size();
    }

    @Override
    public Object getItem(int position) {
        return bookedOrdersListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_camp_order_history_item, null);

        LinearLayout mainLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
        TextView lblOrderId = (TextView) convertView.findViewById(R.id.lblOrderId);
        TextView orderId = (TextView) convertView.findViewById(R.id.orderId);
        TextView lblSessions = (TextView) convertView.findViewById(R.id.lblSessions);
        TextView sessionTimings = (TextView) convertView.findViewById(R.id.sessionTimings);
        TextView lblDate = (TextView) convertView.findViewById(R.id.lblDate);
        TextView orderDate = (TextView) convertView.findViewById(R.id.orderDate);
        TextView totalDays = (TextView) convertView.findViewById(R.id.totalDays);
        TextView totalAmount = (TextView) convertView.findViewById(R.id.totalAmount);
        TextView childName = (TextView) convertView.findViewById(R.id.childName);
        TextView refundAmount = (TextView) convertView.findViewById(R.id.refundAmount);
        TextView customDiscount = (TextView) convertView.findViewById(R.id.customDiscount);
//        ImageView editOrder = (ImageView) convertView.findViewById(R.id.editOrder);

        CampOrderBean currentOrder = bookedOrdersListing.get(position);

        lblOrderId.setTypeface(linoType);
        orderId.setTypeface(helvetica);
        lblSessions.setTypeface(linoType);
        sessionTimings.setTypeface(helvetica);
        lblDate.setTypeface(linoType);
        orderDate.setTypeface(helvetica);
        totalDays.setTypeface(helvetica);
        totalAmount.setTypeface(helvetica);
        childName.setTypeface(linoType);
        refundAmount.setTypeface(helvetica);
        customDiscount.setTypeface(helvetica);

        orderId.setText("#" + currentOrder.getOrderId());
        sessionTimings.setText(currentOrder.getShowFromTime() + " - " + currentOrder.getShowToTime());
        orderDate.setText(currentOrder.getShowOrderDate());
        totalDays.setText(" (" + currentOrder.getTotalBookedDays() + " days)");

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);

        totalAmount.setText(currentOrder.getNetAmount() + " " + academy_currency);
        childName.setText(currentOrder.getChildName());
        refundAmount.setText("REFUND AMOUNT: " + currentOrder.getDisplayRefundAmount());
        customDiscount.setText("CUSTOM DISCOUNT: " + currentOrder.getDisplay_custom_discount());

        if (position % 2 == 0) {
            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            lblOrderId.setTextColor(context.getResources().getColor(R.color.white));
            orderId.setTextColor(context.getResources().getColor(R.color.white));
            lblSessions.setTextColor(context.getResources().getColor(R.color.white));
            sessionTimings.setTextColor(context.getResources().getColor(R.color.white));
            lblDate.setTextColor(context.getResources().getColor(R.color.white));
            orderDate.setTextColor(context.getResources().getColor(R.color.white));
            totalDays.setTextColor(context.getResources().getColor(R.color.white));
            totalAmount.setTextColor(context.getResources().getColor(R.color.white));
            childName.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            lblOrderId.setTextColor(context.getResources().getColor(R.color.black));
            orderId.setTextColor(context.getResources().getColor(R.color.black));
            lblSessions.setTextColor(context.getResources().getColor(R.color.black));
            sessionTimings.setTextColor(context.getResources().getColor(R.color.black));
            lblDate.setTextColor(context.getResources().getColor(R.color.black));
            orderDate.setTextColor(context.getResources().getColor(R.color.black));
            totalDays.setTextColor(context.getResources().getColor(R.color.black));
            totalAmount.setTextColor(context.getResources().getColor(R.color.black));
            childName.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }
}