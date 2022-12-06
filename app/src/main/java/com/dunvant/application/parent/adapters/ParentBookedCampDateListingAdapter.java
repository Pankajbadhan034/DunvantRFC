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
import com.dunvant.application.beans.BookingDateBean;
import com.dunvant.application.beans.BookingHistoryRefundDetailsBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentBookedCampDateListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<BookingDateBean> bookingDatesListing;
    ArrayList<BookingHistoryRefundDetailsBean> refundListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentBookedCampDateListingAdapter(Context context, ArrayList<BookingDateBean> bookingDatesListing, ArrayList<BookingHistoryRefundDetailsBean> refundListing){
        this.context = context;
        this.bookingDatesListing = bookingDatesListing;
        this.refundListing = refundListing;
        layoutInflater = LayoutInflater.from(context);
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return bookingDatesListing.size();
    }

    @Override
    public Object getItem(int position) {
        return bookingDatesListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_booking_dates_item, null);

        LinearLayout mainLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView cost = (TextView) convertView.findViewById(R.id.cost);
        TextView refundId = (TextView) convertView.findViewById(R.id.refundId);

        BookingDateBean bookingDateBean = bookingDatesListing.get(position);

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);

        date.setText(bookingDateBean.getBookingDateFormatted());
        cost.setText(bookingDateBean.getCost()+" "+academy_currency);

        for(BookingHistoryRefundDetailsBean refundDetailsBean : refundListing){
            String csvCancelledSession = refundDetailsBean.getCancelledSession();

            String cancelledSessionsIds[] = csvCancelledSession.split(",");
            for(String cancelledId : cancelledSessionsIds){
                if(cancelledId.trim().equalsIgnoreCase(bookingDateBean.getId())){
                    refundId.setText("(Refund #"+refundDetailsBean.getId()+")");
                    refundId.setVisibility(View.VISIBLE);
                    mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.refundedSession));
                }
            }
        }

        date.setTypeface(helvetica);
        cost.setTypeface(helvetica);
        refundId.setTypeface(helvetica);

        return convertView;
    }
}