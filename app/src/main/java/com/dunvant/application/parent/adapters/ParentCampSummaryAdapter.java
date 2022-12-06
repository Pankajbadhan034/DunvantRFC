package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CampSummarySelectedChildBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentCampSummaryAdapter extends BaseAdapter{

    Context context;
    ArrayList<CampSummarySelectedChildBean> summaryListing;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public ParentCampSummaryAdapter(Context context, ArrayList<CampSummarySelectedChildBean> summaryListing){
        this.context = context;
        this.summaryListing = summaryListing;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return summaryListing.size();
    }

    @Override
    public Object getItem(int position) {
        return summaryListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_camp_summary_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView lblAmount = (TextView) convertView.findViewById(R.id.lblAmount);
        TextView amount = (TextView) convertView.findViewById(R.id.amount);
        TextView lblDatesBooked = (TextView) convertView.findViewById(R.id.lblDatesBooked);
        TextView datesBooked = (TextView) convertView.findViewById(R.id.datesBooked);
        LinearLayout siblingLinearLayout = (LinearLayout) convertView.findViewById(R.id.siblingLinearLayout);
        TextView lblSiblingDiscount = (TextView) convertView.findViewById(R.id.lblSiblingDiscount);
        TextView siblingDiscount = (TextView) convertView.findViewById(R.id.siblingDiscount);
        View siblingBorder = convertView.findViewById(R.id.siblingBorder);
        TextView lblNetPayable = (TextView) convertView.findViewById(R.id.lblNetPayable);
        TextView netAmount = (TextView) convertView.findViewById(R.id.netAmount);

        CampSummarySelectedChildBean summarySelectedChildBean = summaryListing.get(position);

        name.setText(summarySelectedChildBean.getName());

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);

        amount.setText(summarySelectedChildBean.getTotalCost()+" "+academy_currency);
        datesBooked.setText(summarySelectedChildBean.getBookingDates());
        netAmount.setText(summarySelectedChildBean.getNetPay()+" "+academy_currency);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int sixtyPercent = (screenWidth * 60) / 100;
        float textWidthForTitle = datesBooked.getPaint().measureText(datesBooked.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / sixtyPercent) + 1;
        datesBooked.setLines(numberOfLines);

        if(summarySelectedChildBean.getDiscountBean() == null){
            siblingLinearLayout.setVisibility(View.GONE);
            siblingBorder.setVisibility(View.GONE);
        } else {
            siblingDiscount.setText(summarySelectedChildBean.getDiscountCost()+" "+academy_currency+" ("+summarySelectedChildBean.getDiscountBean().getDiscountValue()+"%)");
        }

        name.setTypeface(helvetica);
        lblAmount.setTypeface(helvetica);
        amount.setTypeface(helvetica);
        lblDatesBooked.setTypeface(helvetica);
        datesBooked.setTypeface(helvetica);
        lblSiblingDiscount.setTypeface(helvetica);
        siblingDiscount.setTypeface(helvetica);
        lblNetPayable.setTypeface(helvetica);
        netAmount.setTypeface(helvetica);

        return convertView;
    }
}