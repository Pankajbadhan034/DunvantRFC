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
import android.widget.ListView;
import android.widget.TextView;

import com.dunvant.application.ApplicationContext;
import com.dunvant.application.R;
import com.dunvant.application.beans.PitchBookingDateBean;
import com.dunvant.application.beans.PitchSummaryDataBean;
import com.dunvant.application.parent.ParentBookPitchSummaryNewForCalendarViewScreen;
import com.dunvant.application.utils.Utilities;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ParentPitchSummaryNewForCalendarViewAdapter extends BaseAdapter {

    Context context;
    ApplicationContext applicationContext;
    ArrayList<PitchSummaryDataBean> summaryDataList;
    LayoutInflater layoutInflater;
    ParentBookPitchSummaryNewForCalendarViewScreen parentBookPitchSummaryScreen;
    ListView pitchesDetailListView;
    Typeface helvetica;
    Typeface linoType;

    public ParentPitchSummaryNewForCalendarViewAdapter(Context context, ArrayList<PitchSummaryDataBean> summaryDataList, ParentBookPitchSummaryNewForCalendarViewScreen parentBookPitchSummaryScreen, ListView pitchesDetailListView) {
        this.context = context;
        this.summaryDataList = summaryDataList;
        this.layoutInflater = LayoutInflater.from(context);
        this.parentBookPitchSummaryScreen = parentBookPitchSummaryScreen;
        this.pitchesDetailListView = pitchesDetailListView;
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        applicationContext = (ApplicationContext) context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return summaryDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return summaryDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_pitch_summary_item, null);

        TextView locationName = (TextView) convertView.findViewById(R.id.locationName);
        TextView costLabel = (TextView) convertView.findViewById(R.id.costLabel);
        TextView unavailableDates = (TextView) convertView.findViewById(R.id.unavailableDates);
        ListView bookingDatesListView = (ListView) convertView.findViewById(R.id.bookingDatesListView);
        TextView lblPitchAmount = (TextView) convertView.findViewById(R.id.lblPitchAmount);
        TextView pitchAmount = (TextView) convertView.findViewById(R.id.pitchAmount);
        TextView lblPitchBulkHourDiscount = (TextView) convertView.findViewById(R.id.lblPitchBulkHourDiscount);
        TextView pitchBulkHourDiscount = (TextView) convertView.findViewById(R.id.pitchBulkHourDiscount);
        TextView lblPitchAdditionalDiscount = (TextView) convertView.findViewById(R.id.lblPitchAdditionalDiscount);
        TextView pitchAdditionalDiscount = (TextView) convertView.findViewById(R.id.pitchAdditionalDiscount);
        TextView additionalDiscountLabel = convertView.findViewById(R.id.additionalDiscountLabel);

        PitchSummaryDataBean summaryDataBean = summaryDataList.get(position);

        locationName.setText(summaryDataBean.getLocationName());
        costLabel.setText(summaryDataBean.getPitchName()/*+" "+summaryDataBean.getShowPrice()*/);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int eightyPercent = (screenWidth * 80) / 100;
        float textWidthForTitle = costLabel.getPaint().measureText(costLabel.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / eightyPercent) + 1;
        costLabel.setLines(numberOfLines);

        textWidthForTitle = locationName.getPaint().measureText(locationName.getText().toString());
        numberOfLines = ((int) textWidthForTitle / eightyPercent) + 1;
        locationName.setLines(numberOfLines);

//        String strUnavailableDates = summaryDataBean.getUnavailableDates();
        String strUnavailableDates = null;
        if(strUnavailableDates == null || strUnavailableDates.isEmpty() || strUnavailableDates.equalsIgnoreCase("null")) {
            unavailableDates.setVisibility(View.GONE);
        } else {
            unavailableDates.setText("Unavailable Dates "+strUnavailableDates);

            float textWidthForTitle1 = unavailableDates.getPaint().measureText("Unavailable Dates "+strUnavailableDates);
            int numberOfLines1 = ((int) textWidthForTitle1 / eightyPercent) + 1;
            unavailableDates.setLines(numberOfLines1);
        }

        bookingDatesListView.setAdapter(new ParentPitchBookingDatesNewForCalendarViewAdapter(context, summaryDataBean.getBookingDatesList(), ParentPitchSummaryNewForCalendarViewAdapter.this, pitchesDetailListView, bookingDatesListView, summaryDataBean.getPitchId()));
        Utilities.setListViewHeightBasedOnChildren(bookingDatesListView);

        if(summaryDataBean.getAdditionalDiscountLabel() == null || summaryDataBean.getAdditionalDiscountLabel().isEmpty() || summaryDataBean.getAdditionalDiscountLabel().equalsIgnoreCase("null")){
            additionalDiscountLabel.setVisibility(View.GONE);
        } else {
            additionalDiscountLabel.setText(summaryDataBean.getAdditionalDiscountLabel());

            float textWidthForTitle1 = additionalDiscountLabel.getPaint().measureText(additionalDiscountLabel.getText().toString());
            int numberOfLines1 = ((int) textWidthForTitle1 / eightyPercent) + 1;
            additionalDiscountLabel.setLines(numberOfLines1);
        }

        DecimalFormat df = new DecimalFormat("#.00");

        double initialAmount = 0;
        for(PitchBookingDateBean pitchBookingDateBean: summaryDataBean.getBookingDatesList()) {
            if (pitchBookingDateBean.getAmount() == null || pitchBookingDateBean.getAmount().isEmpty() || pitchBookingDateBean.getAmount().equalsIgnoreCase("null")) {
                initialAmount += 0;
            } else {
                initialAmount += Double.parseDouble(pitchBookingDateBean.getAmount());
            }
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);

        pitchAmount.setText(df.format(initialAmount)+" "+academy_currency);
        summaryDataBean.setInitialAmountValue(initialAmount);


        pitchBulkHourDiscount.setText(summaryDataBean.getBulkHourDiscountAmount()+" "+academy_currency);
        pitchAdditionalDiscount.setText(summaryDataBean.getAdditionalDiscountAmount()+" "+academy_currency);

        /*double initialAmount = 0;
        double intervalsBooked = 0;
        for(PitchBookingDateBean pitchBookingDateBean: summaryDataBean.getBookingDatesList()) {
            if (pitchBookingDateBean.getAmount() == null || pitchBookingDateBean.getAmount().isEmpty() || pitchBookingDateBean.getAmount().equalsIgnoreCase("null")) {
                initialAmount += 0;
            } else {
                initialAmount += Double.parseDouble(pitchBookingDateBean.getAmount());
            }

            if(pitchBookingDateBean.getInterval() == null || pitchBookingDateBean.getInterval().isEmpty() || pitchBookingDateBean.getInterval().equalsIgnoreCase("null")) {
                intervalsBooked += 0;
            } else {
                intervalsBooked += Integer.parseInt(pitchBookingDateBean.getInterval());
            }
        }
        pitchAmount.setText(df.format(initialAmount)+" AED");

        int bulkHours = 0;
        if(summaryDataBean.getBulkHours() == null || summaryDataBean.getBulkHours().isEmpty() || summaryDataBean.getBulkHours().equalsIgnoreCase("null")) {
            bulkHours = 0;
        } else {
            bulkHours = Integer.parseInt(summaryDataBean.getBulkHours());
        }

//        double discountApplicable = 0;
        double bulkHourDiscount = 0;
        double additionalDiscount = 0;

        if(bulkHours != 0 && intervalsBooked >= bulkHours) {
            // Bulk discount is applicable
            double bulkHourDiscountPercentage = 0;
            if(summaryDataBean.getBulkHoursDiscount() == null || summaryDataBean.getBulkHoursDiscount().isEmpty() || summaryDataBean.getBulkHoursDiscount().equalsIgnoreCase("null")) {
                bulkHourDiscountPercentage = 0;
            } else {
                bulkHourDiscountPercentage = Double.parseDouble(summaryDataBean.getBulkHoursDiscount());
            }

            // Calculate bulkHourDiscountPercentage% of discount
            bulkHourDiscount = (initialAmount * bulkHourDiscountPercentage)/100;
//            discountApplicable += bulkHourDiscount;
        }

        double additionalDiscountPercentage = 0;
        if(summaryDataBean.getAdditionalBookingDiscount() == null || summaryDataBean.getAdditionalBookingDiscount().isEmpty() || summaryDataBean.getAdditionalBookingDiscount().equalsIgnoreCase("null")) {
            additionalDiscountPercentage = 0;
        } else {
            additionalDiscountPercentage = Double.parseDouble(summaryDataBean.getAdditionalBookingDiscount());
        }

        additionalDiscount = (initialAmount * additionalDiscountPercentage)/100;
//        discountApplicable += additionalDiscount;

        if(bulkHourDiscount == 0) {
            pitchBulkHourDiscount.setText("0.00 AED");
        } else {
            pitchBulkHourDiscount.setText(df.format(bulkHourDiscount)+" AED ("+summaryDataBean.getBulkHoursDiscount()+"%)");
        }


        if(additionalDiscount == 0) {
            pitchAdditionalDiscount.setText("0.00 AED");
        } else {
            pitchAdditionalDiscount.setText(df.format(additionalDiscount)+" AED");
        }

        summaryDataBean.setInitialAmountValue(initialAmount);
        summaryDataBean.setBulkHourDiscountValue(bulkHourDiscount);
        summaryDataBean.setAdditionalDiscountValue(additionalDiscount);*/

        parentBookPitchSummaryScreen.updateCalculations();

        // if all dates are removed then delete whole pitch
        if(summaryDataBean.getBookingDatesList().isEmpty()){
            summaryDataList.remove(position);
            notifyDataSetChanged();
            Utilities.setListViewHeightBasedOnChildren(bookingDatesListView);
            Utilities.setListViewHeightBasedOnChildren(pitchesDetailListView);
            parentBookPitchSummaryScreen.updateListView();
        }

        //Change Fonts
        locationName.setTypeface(helvetica);
        costLabel.setTypeface(helvetica);
        unavailableDates.setTypeface(helvetica);
        pitchAmount.setTypeface(helvetica);
        pitchBulkHourDiscount.setTypeface(helvetica);
        pitchAdditionalDiscount.setTypeface(helvetica);
        lblPitchAmount.setTypeface(helvetica);
        lblPitchBulkHourDiscount.setTypeface(helvetica);
        lblPitchAdditionalDiscount.setTypeface(helvetica);

        return convertView;
    }

    public void deleteSlot(String pitchId, PitchBookingDateBean bookingDateBean){
        parentBookPitchSummaryScreen.deleteSlot(pitchId, bookingDateBean);
    }
}