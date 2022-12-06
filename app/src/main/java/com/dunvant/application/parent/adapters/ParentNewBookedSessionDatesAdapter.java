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

public class ParentNewBookedSessionDatesAdapter extends BaseAdapter{

    Context context;
    ArrayList<BookingDateBean> datesListing;
    ArrayList<BookingHistoryRefundDetailsBean> refundListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentNewBookedSessionDatesAdapter(Context context, ArrayList<BookingDateBean> datesListing, ArrayList<BookingHistoryRefundDetailsBean> refundListing){
        this.context = context;
        this.datesListing = datesListing;
        this.refundListing = refundListing;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return datesListing.size();
    }

    @Override
    public Object getItem(int position) {
        return datesListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_new_booked_dates_item, null);

        LinearLayout mainLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
        TextView sessionNumber = (TextView) convertView.findViewById(R.id.sessionNumber);
        TextView sessionDate = (TextView) convertView.findViewById(R.id.sessionDate);
        TextView cost = (TextView) convertView.findViewById(R.id.cost);
        TextView sessionHours = (TextView)  convertView.findViewById(R.id.sessionHours);
        TextView refundId = (TextView) convertView.findViewById(R.id.refundId);

        BookingDateBean dateBean = datesListing.get(position);

        sessionNumber.setText("Session "+(position+1));
        sessionDate.setText(dateBean.getBookingDateFormatted());

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);

        cost.setText(dateBean.getCost()+" "+academy_currency);
        sessionHours.setText("("+dateBean.getNumberOfHours()/*+" hours)"*/+")");

        for(BookingHistoryRefundDetailsBean refundDetailsBean : refundListing){
            String csvCancelledSession = refundDetailsBean.getCancelledSession();

            String cancelledSessionsIds[] = csvCancelledSession.split(",");
            for(String cancelledId : cancelledSessionsIds){
                if(cancelledId.trim().equalsIgnoreCase(dateBean.getOrderSessionsId())){
                    refundId.setText("(Refund #"+refundDetailsBean.getId()+")");
                    refundId.setVisibility(View.VISIBLE);
                    mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.refundedSession));
                }
            }
        }

        sessionNumber.setTypeface(linoType);
        sessionDate.setTypeface(helvetica);
        cost.setTypeface(helvetica);
        sessionHours.setTypeface(helvetica);
        refundId.setTypeface(helvetica);

        return convertView;
    }
}