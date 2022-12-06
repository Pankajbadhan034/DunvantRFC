package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.BookingHistoryRefundDetailsBean;
import com.dunvant.application.beans.PitchHistoryDetailBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentPitchHistoryDetailAdapter extends BaseAdapter {

    Context context;
    ArrayList<PitchHistoryDetailBean> pitchHistoryDetailListing;
    ArrayList<BookingHistoryRefundDetailsBean> refundListing;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public ParentPitchHistoryDetailAdapter(Context context, ArrayList<PitchHistoryDetailBean> pitchHistoryDetailListing, ArrayList<BookingHistoryRefundDetailsBean> refundListing) {
        this.context = context;
        this.pitchHistoryDetailListing = pitchHistoryDetailListing;
        this.refundListing = refundListing;
        this.layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return pitchHistoryDetailListing.size();
    }

    @Override
    public Object getItem(int position) {
        return pitchHistoryDetailListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_pitch_history_detail, null);

        TextView pitchAddress = (TextView) convertView.findViewById(R.id.pitchAddress);
        TextView pitchName = (TextView) convertView.findViewById(R.id.pitchName);
        ListView pitchInnerDetailsListView = (ListView) convertView.findViewById(R.id.pitchInnerDetailsListView);

        pitchAddress.setTypeface(helvetica);
        pitchName.setTypeface(helvetica);

        PitchHistoryDetailBean pitchHistoryDetailBean = pitchHistoryDetailListing.get(position);

        pitchAddress.setText(pitchHistoryDetailBean.getLocationName());
        pitchName.setText(pitchHistoryDetailBean.getPitchName()/*+" "+pitchHistoryDetailBean.getPricesDisplay()*/);


        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int eightyPercent = (screenWidth * 80) / 100;
        float textWidthForTitle = pitchName.getPaint().measureText(pitchName.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / eightyPercent) + 1;
        pitchName.setLines(numberOfLines);

        pitchInnerDetailsListView.setAdapter(new ParentPitchHistoryInnerAdapter(context, pitchHistoryDetailBean.getBookingsList(), refundListing));
        Utilities.setListViewHeightBasedOnChildren(pitchInnerDetailsListView);

        return convertView;
    }
}