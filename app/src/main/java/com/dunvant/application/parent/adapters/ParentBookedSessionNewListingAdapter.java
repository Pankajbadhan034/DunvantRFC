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
import com.dunvant.application.beans.OrderSessionBeanNew;
import com.dunvant.application.beans.BookingHistoryRefundDetailsBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentBookedSessionNewListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<OrderSessionBeanNew> orderSessionBeanNewList;
    ArrayList<BookingHistoryRefundDetailsBean> refundListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentBookedSessionNewListingAdapter(Context context, ArrayList<OrderSessionBeanNew> orderSessionBeanNewList, ArrayList<BookingHistoryRefundDetailsBean> refundListing){
        this.context = context;
        this.orderSessionBeanNewList = orderSessionBeanNewList;
        this.refundListing = refundListing;
        this.layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return orderSessionBeanNewList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderSessionBeanNewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_booked_session_new_item, null);

        TextView childName = (TextView) convertView.findViewById(R.id.childName);
        TextView coachingProgramName = (TextView) convertView.findViewById(R.id.coachingProgramName);
        TextView termName = (TextView) convertView.findViewById(R.id.termName);
        TextView timings = (TextView) convertView.findViewById(R.id.timings);
        TextView groupName = (TextView) convertView.findViewById(R.id.groupName);
        ListView bookingDatesListView = (ListView) convertView.findViewById(R.id.bookingDatesListView);

        OrderSessionBeanNew orderSessionBeanNew = orderSessionBeanNewList.get(position);

        childName.setText(orderSessionBeanNew.getChildrenInfoBean().getName());
        coachingProgramName.setText(orderSessionBeanNew.getSessionInfoBean().getCoachingProgramsName());
        termName.setText(orderSessionBeanNew.getSessionInfoBean().getTermsName());
        timings.setText(orderSessionBeanNew.getSessionInfoBean().getStartTimeFormatted() + " - " + orderSessionBeanNew.getSessionInfoBean().getEndTimeFormatted());
        groupName.setText(orderSessionBeanNew.getSessionInfoBean().getGroupName());

        bookingDatesListView.setAdapter(new ParentNewBookedSessionDatesAdapter(context, orderSessionBeanNew.getBookingDatesList(), refundListing));
        Utilities.setListViewHeightBasedOnChildren(bookingDatesListView);

        childName.setTypeface(linoType);
        coachingProgramName.setTypeface(helvetica);
        termName.setTypeface(helvetica);
        timings.setTypeface(helvetica);
        groupName.setTypeface(helvetica);

        return convertView;
    }
}