package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.BookingHistoryRefundDetailsBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentSessionRefundListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<BookingHistoryRefundDetailsBean> refundListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentSessionRefundListingAdapter(Context context, ArrayList<BookingHistoryRefundDetailsBean> refundListing){
        this.context = context;
        this.refundListing = refundListing;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return refundListing.size();
    }

    @Override
    public Object getItem(int position) {
        return refundListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_session_refund_item, null);

        TextView refundId = (TextView) convertView.findViewById(R.id.refundId);
        TextView lblRefundAmount = (TextView) convertView.findViewById(R.id.lblRefundAmount);
        TextView refundAmount = (TextView) convertView.findViewById(R.id.refundAmount);
        TextView lblRefundFee = (TextView) convertView.findViewById(R.id.lblRefundFee);
        TextView refundFee = (TextView) convertView.findViewById(R.id.refundFee);
        TextView lblActualRefundedAmount = (TextView) convertView.findViewById(R.id.lblActualRefundedAmount);
        TextView actualRefundedAmount = (TextView) convertView.findViewById(R.id.actualRefundedAmount);

        BookingHistoryRefundDetailsBean refundDetailsBean = refundListing.get(position);

        refundId.setText("REFUND #"+refundDetailsBean.getId()+" ("+refundDetailsBean.getCreatedAt()+")");

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);

        refundAmount.setText(refundDetailsBean.getInitialAmount()+" "+academy_currency);
        refundFee.setText(refundDetailsBean.getRefundFee()+" "+academy_currency);
        actualRefundedAmount.setText(refundDetailsBean.getAmount()+" "+academy_currency);

        refundId.setTypeface(helvetica);
        lblRefundAmount.setTypeface(helvetica);
        refundAmount.setTypeface(helvetica);
        lblRefundFee.setTypeface(helvetica);
        refundFee.setTypeface(helvetica);
        lblActualRefundedAmount.setTypeface(helvetica);
        actualRefundedAmount.setTypeface(helvetica);

        return convertView;
    }
}