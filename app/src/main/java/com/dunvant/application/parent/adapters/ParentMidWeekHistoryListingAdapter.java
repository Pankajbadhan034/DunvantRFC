package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.SessionHistoryBean;

import java.util.ArrayList;

public class ParentMidWeekHistoryListingAdapter extends BaseAdapter {

    Context context;
    ArrayList<SessionHistoryBean> sessionHistoryList;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentMidWeekHistoryListingAdapter(Context context, ArrayList<SessionHistoryBean> sessionHistoryList) {
        this.context = context;
        this.sessionHistoryList = sessionHistoryList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return sessionHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return sessionHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_session_history_item, null);

        LinearLayout mainLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
        TextView txtOrderId = (TextView) convertView.findViewById(R.id.txtOrderId);
        TextView orderId = (TextView) convertView.findViewById(R.id.orderId);
        TextView txtOriginalOrderId = (TextView) convertView.findViewById(R.id.txtOriginalOrderId);
        TextView originalOrderId = (TextView) convertView.findViewById(R.id.originalOrderId);
        TextView txtSession = (TextView) convertView.findViewById(R.id.txtSession);
        TextView sessionName = (TextView) convertView.findViewById(R.id.sessionName);
        TextView txtTotalAmount = (TextView) convertView.findViewById(R.id.txtTotalAmount);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
        TextView date = (TextView) convertView.findViewById(R.id.sessionDate);
        TextView txtRefundAmount = (TextView) convertView.findViewById(R.id.txtRefundAmount);
        TextView CustomDiscount = (TextView) convertView.findViewById(R.id.CustomDiscount);
        TextView refundAmount = (TextView) convertView.findViewById(R.id.refundAmount);
        TextView displayCustomDiscount = (TextView) convertView.findViewById(R.id.displayCustomDiscount);

        txtOrderId.setTypeface(helvetica);
        orderId.setTypeface(helvetica);
        txtOriginalOrderId.setTypeface(helvetica);
        originalOrderId.setTypeface(helvetica);
        txtSession.setTypeface(helvetica);
        sessionName.setTypeface(helvetica);
        txtTotalAmount.setTypeface(helvetica);
        price.setTypeface(helvetica);
        txtDate.setTypeface(helvetica);
        date.setTypeface(helvetica);
        txtRefundAmount.setTypeface(helvetica);
        refundAmount.setTypeface(helvetica);
        CustomDiscount.setTypeface(helvetica);
        displayCustomDiscount.setTypeface(helvetica);

        SessionHistoryBean sessionHistoryBean = sessionHistoryList.get(position);

        orderId.setText("#" + sessionHistoryBean.getOrderId());

        if (sessionHistoryBean.getPreviousOrderId().equalsIgnoreCase(" - ")) {
            originalOrderId.setText(sessionHistoryBean.getPreviousOrderId());
        } else {
            originalOrderId.setText("#" + sessionHistoryBean.getPreviousOrderId());
        }

        sessionName.setText(sessionHistoryBean.getSessionName());
        price.setText(sessionHistoryBean.getShowTotalCost());
        date.setText(sessionHistoryBean.getOrderDate());
        refundAmount.setText(sessionHistoryBean.getDisplayRefundAmount());
        displayCustomDiscount.setText(sessionHistoryBean.getDisplayCustomDiscount());

        if (position % 2 == 0) {
            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            txtOrderId.setTextColor(context.getResources().getColor(R.color.white));
            orderId.setTextColor(context.getResources().getColor(R.color.white));
            txtOriginalOrderId.setTextColor(context.getResources().getColor(R.color.white));
            originalOrderId.setTextColor(context.getResources().getColor(R.color.white));
            txtSession.setTextColor(context.getResources().getColor(R.color.white));
            sessionName.setTextColor(context.getResources().getColor(R.color.white));
            price.setTextColor(context.getResources().getColor(R.color.white));
            txtDate.setTextColor(context.getResources().getColor(R.color.white));
            date.setTextColor(context.getResources().getColor(R.color.white));
            txtTotalAmount.setTextColor(context.getResources().getColor(R.color.white));
            txtRefundAmount.setTextColor(context.getResources().getColor(R.color.white));
            CustomDiscount.setTextColor(context.getResources().getColor(R.color.white));
            refundAmount.setTextColor(context.getResources().getColor(R.color.white));
            displayCustomDiscount.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            txtOrderId.setTextColor(context.getResources().getColor(R.color.black));
            orderId.setTextColor(context.getResources().getColor(R.color.black));
            txtOriginalOrderId.setTextColor(context.getResources().getColor(R.color.black));
            originalOrderId.setTextColor(context.getResources().getColor(R.color.black));
            txtSession.setTextColor(context.getResources().getColor(R.color.black));
            sessionName.setTextColor(context.getResources().getColor(R.color.black));
            price.setTextColor(context.getResources().getColor(R.color.black));
            txtDate.setTextColor(context.getResources().getColor(R.color.black));
            date.setTextColor(context.getResources().getColor(R.color.black));
            txtTotalAmount.setTextColor(context.getResources().getColor(R.color.black));
            txtRefundAmount.setTextColor(context.getResources().getColor(R.color.black));
            refundAmount.setTextColor(context.getResources().getColor(R.color.black));
            CustomDiscount.setTextColor(context.getResources().getColor(R.color.black));
            displayCustomDiscount.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }
}