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
import com.dunvant.application.beans.BookingHistoryRefundDetailsBean;
import com.dunvant.application.beans.PitchBookingBean;

import java.util.ArrayList;

public class ParentPitchHistoryInnerAdapter extends BaseAdapter{

    Context context;
    ArrayList<PitchBookingBean> pitchBookingList;
    ArrayList<BookingHistoryRefundDetailsBean> refundListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentPitchHistoryInnerAdapter(Context context, ArrayList<PitchBookingBean> pitchBookingList, ArrayList<BookingHistoryRefundDetailsBean> refundListing) {
        this.context = context;
        this.pitchBookingList = pitchBookingList;
        this.refundListing = refundListing;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return pitchBookingList.size();
    }

    @Override
    public Object getItem(int position) {
        return pitchBookingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_pitch_history_inner_item, null);

        LinearLayout mainLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
        TextView lblDate = (TextView) convertView.findViewById(R.id.lblDate);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView lblTime = (TextView) convertView.findViewById(R.id.lblTime);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView lblDuration = (TextView) convertView.findViewById(R.id.lblDuration);
        TextView duration = (TextView) convertView.findViewById(R.id.duration);
        TextView refundId = (TextView) convertView.findViewById(R.id.refundId);

        lblDate.setTypeface(helvetica);
        date.setTypeface(helvetica);
        lblTime.setTypeface(helvetica);
        time.setTypeface(helvetica);
        lblDuration.setTypeface(helvetica);
        duration.setTypeface(helvetica);
        refundId.setTypeface(helvetica);

        PitchBookingBean pitchBookingBean = pitchBookingList.get(position);

        date.setText(pitchBookingBean.getShowBookingDate());
        time.setText(pitchBookingBean.getTime());
        duration.setText(pitchBookingBean.getInterval()+" hours");

        for(BookingHistoryRefundDetailsBean refundDetailsBean : refundListing){
            String csvCancelledSession = refundDetailsBean.getCancelledSession();

            String cancelledSessionsIds[] = csvCancelledSession.split(",");
            for(String cancelledId : cancelledSessionsIds){
                if(cancelledId.trim().equalsIgnoreCase(pitchBookingBean.getId())){
                    refundId.setText("(Refund #"+refundDetailsBean.getId()+")");
                    refundId.setVisibility(View.VISIBLE);
                    mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.refundedSession));
                }
            }
        }

        return convertView;
    }
}