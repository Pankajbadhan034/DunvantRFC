package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.BookingHistoryRefundDetailsBean;
import com.dunvant.application.beans.CampBookingDetailsBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentCampBookingDetailListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<CampBookingDetailsBean> campBookingDetailsListing;
    ArrayList<BookingHistoryRefundDetailsBean> refundListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentCampBookingDetailListingAdapter(Context context, ArrayList<CampBookingDetailsBean> campBookingDetailsListing, ArrayList<BookingHistoryRefundDetailsBean> refundListing){
        this.context = context;
        this.campBookingDetailsListing = campBookingDetailsListing;
        this.refundListing = refundListing;
        layoutInflater = LayoutInflater.from(context);
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return campBookingDetailsListing.size();
    }

    @Override
    public Object getItem(int position) {
        return campBookingDetailsListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_camp_booking_detail_item, null);

        TextView childName = (TextView) convertView.findViewById(R.id.childName);
        ListView bookingDatesListView = (ListView) convertView.findViewById(R.id.bookingDatesListView);

        CampBookingDetailsBean campBookingDetailsBean = campBookingDetailsListing.get(position);

        childName.setText(campBookingDetailsBean.getChildName());
        bookingDatesListView.setAdapter(new ParentBookedCampDateListingAdapter(context, campBookingDetailsBean.getBookingDatesList(), refundListing));
        Utilities.setListViewHeightBasedOnChildren(bookingDatesListView);

        childName.setTypeface(helvetica);

        return convertView;
    }
}