package com.dunvant.application.parent.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.AcademySessionSummaryBean;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.parent.ParentBookAcademySummaryScreen;
import com.dunvant.application.utils.Utilities;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ParentAcademySessionSummaryAdapter extends BaseAdapter{

    Context context;
    ArrayList<AcademySessionSummaryBean> sessionSummaryList;
    LayoutInflater layoutInflater;
//    ArrayList<AcademySessionDateBean> datesListing = new ArrayList<>();
    ParentDatesListingAdapter parentDatesListingAdapter;
    ParentBookAcademySummaryScreen parentBookAcademySummaryScreen;
    ParentSessionDiscountsAdapter parentSessionDiscountsAdapter;
    ListView summaryListView;
//    BookingHistoryDiscountBean discountBean;

    Typeface helvetica;
    Typeface linoType;

    public ParentAcademySessionSummaryAdapter(Context context, ArrayList<AcademySessionSummaryBean> sessionSummaryList, ParentBookAcademySummaryScreen parentBookAcademySummaryScreen, ListView summaryListView/*, BookingHistoryDiscountBean discountBean*/){
        this.context = context;
        this.sessionSummaryList = sessionSummaryList;
        this.parentBookAcademySummaryScreen = parentBookAcademySummaryScreen;
//        this.discountBean = discountBean;
        this.summaryListView = summaryListView;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return sessionSummaryList.size();
    }

    @Override
    public Object getItem(int position) {
        return sessionSummaryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_academy_session_summary_item, null);

        CheckBox isTrial = (CheckBox) convertView.findViewById(R.id.isTrial);
        TextView childrenNames = (TextView) convertView.findViewById(R.id.childrenNames);
        TextView termName = (TextView) convertView.findViewById(R.id.termName);
        TextView timings = (TextView) convertView.findViewById(R.id.timings);
        TextView ageGroup = (TextView) convertView.findViewById(R.id.ageGroup);
        final ListView datesListView = (ListView) convertView.findViewById(R.id.datesListView);
        TextView lblSessionPayment = (TextView) convertView.findViewById(R.id.lblSessionPayment);
        TextView sessionPayment = (TextView) convertView.findViewById(R.id.sessionPayment);
        ListView discountsListView = (ListView) convertView.findViewById(R.id.discountsListView);
        ImageView deleteSessionEntry = (ImageView) convertView.findViewById(R.id.deleteSessionEntry);

        isTrial.setTypeface(helvetica);
        childrenNames.setTypeface(linoType);
        termName.setTypeface(helvetica);
        timings.setTypeface(helvetica);
        ageGroup.setTypeface(helvetica);
        lblSessionPayment.setTypeface(linoType);
        sessionPayment.setTypeface(helvetica);

        final AcademySessionSummaryBean summaryBean = sessionSummaryList.get(position);

        if (summaryBean.getIsTrial().equalsIgnoreCase("1")) {
            // Hiding isTrial
//            isTrial.setVisibility(View.VISIBLE);
            isTrial.setVisibility(View.GONE);
        } else {
            isTrial.setVisibility(View.GONE);
        }

        isTrial.setChecked(summaryBean.isTrialSelected());

        String strChildrenNames = "";
        for(ChildBean childBean : summaryBean.getChildrenList()) {
            strChildrenNames += childBean.getFullName()+",";
        }
        childrenNames.setText(strChildrenNames);

        DecimalFormat df = new DecimalFormat("#.00");

        termName.setText(summaryBean.getCoachingProgramName()+", "+summaryBean.getTermName()+", "+summaryBean.getLocationName());
        timings.setText(summaryBean.getShowStartTime()+" - "+summaryBean.getShowEndTime()+" ");
        ageGroup.setText(summaryBean.getGroupName());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int eightyPercent = (screenWidth * 80) / 100;
        float textWidthForTitle = termName.getPaint().measureText(termName.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / eightyPercent) + 1;
        termName.setLines(numberOfLines);

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);

        if(summaryBean.getSessionCost() == 0) {
            sessionPayment.setText("0.00 "+academy_currency);
        } else {
            sessionPayment.setText(df.format(summaryBean.getSessionCost())+" "+academy_currency);
        }

        // Commenting following functionality because is trial is not needed anymore
        /*datesListing.clear();
        if(summaryBean.isTrialSelected()) {
            for (int i=0;i<summaryBean.getAvailableDatesList().size(); i++) {
                if (i<summaryBean.getTrialCount()) {
                    AcademySessionDateBean tempDateBean = new AcademySessionDateBean();
                    tempDateBean.setDate(summaryBean.getAvailableDatesList().get(i).getDate());
                    tempDateBean.setShowDate(summaryBean.getAvailableDatesList().get(i).getShowDate());
                    tempDateBean.setSelected(summaryBean.getAvailableDatesList().get(i).isSelected());
                    datesListing.add(tempDateBean);
                }
            }
        } else {
            for(AcademySessionDateBean dateBean : summaryBean.getAvailableDatesList()) {
                AcademySessionDateBean tempDateBean = new AcademySessionDateBean();
                tempDateBean.setDate(dateBean.getDate());
                tempDateBean.setShowDate(dateBean.getShowDate());
                tempDateBean.setSelected(dateBean.isSelected());

                datesListing.add(tempDateBean);
            }
        }*/

//        parentDatesListingAdapter = new ParentDatesListingAdapter(context, datesListing, ParentAcademySessionSummaryAdapter.this, summaryBean);
        parentDatesListingAdapter = new ParentDatesListingAdapter(context, summaryBean.getAvailableDatesList(), ParentAcademySessionSummaryAdapter.this, summaryBean, parentBookAcademySummaryScreen);
        datesListView.setAdapter(parentDatesListingAdapter);
        Utilities.setListViewHeightBasedOnChildren(datesListView);

        parentSessionDiscountsAdapter = new ParentSessionDiscountsAdapter(context, summaryBean.getDiscountList());
        discountsListView.setAdapter(parentSessionDiscountsAdapter);
        Utilities.setListViewHeightBasedOnChildren(discountsListView);

        // Commenting is Trial
        /*isTrial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                summaryBean.setTrialSelected(isChecked);

                datesListing.clear();
                if (isChecked) {
                    for (int i=0;i<summaryBean.getAvailableDatesList().size(); i++) {

                        if (i<summaryBean.getTrialCount()) {
                            AcademySessionDateBean tempDateBean = new AcademySessionDateBean();
                            tempDateBean.setDate(summaryBean.getAvailableDatesList().get(i).getDate());
                            tempDateBean.setShowDate(summaryBean.getAvailableDatesList().get(i).getShowDate());
                            tempDateBean.setSelected(summaryBean.getAvailableDatesList().get(i).isSelected());
                            datesListing.add(tempDateBean);
                        } else {
                            summaryBean.getAvailableDatesList().get(i).setSelected(false);
                        }
                    }
                } else {

                    for(AcademySessionDateBean dateBean : summaryBean.getAvailableDatesList()) {
                        AcademySessionDateBean tempDateBean = new AcademySessionDateBean();
                        tempDateBean.setDate(dateBean.getDate());
                        tempDateBean.setShowDate(dateBean.getShowDate());
                        tempDateBean.setSelected(dateBean.isSelected());

                        datesListing.add(tempDateBean);
                    }
                }

                parentDatesListingAdapter.notifyDataSetChanged();
                Utilities.setListViewHeightBasedOnChildren(datesListView);
                calculateSessionCost(summaryBean);
                notifyDataSetChanged();

                parentBookAcademySummaryScreen.calculateFees();
                parentBookAcademySummaryScreen.updateValues();
            }
        });*/

        deleteSessionEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.parent_dialog_delete);

                TextView yes = (TextView) dialog.findViewById(R.id.yes);
                TextView no = (TextView) dialog.findViewById(R.id.no);

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                        ParentBookAcademySummaryScreen.deleteRow = "true";
                        parentBookAcademySummaryScreen.clearRow();

                        sessionSummaryList.remove(position);
                        notifyDataSetChanged();
//                        parentBookAcademySummaryScreen.updateValues();
                        parentBookAcademySummaryScreen.calculateFees();
                        Utilities.setListViewHeightBasedOnChildren(summaryListView);

                    }
                });

                dialog.show();
            }
        });

        return convertView;
    }

//    public void calculateSessionCost(AcademySessionSummaryBean summaryBean) {
//
//        double totalCost = 0;
//
//        double totalCostPerChild = 0;
//
//        double totalDiscount = 0;
//        double costAfterDiscount = 0;
//
//        for (AcademySessionDateBean dateBean : summaryBean.getAvailableDatesList()) {
//            if(dateBean.isSelected()) {
//                totalCost += summaryBean.getChildrenList().size() * Double.parseDouble(summaryBean.getCost());
//                totalCostPerChild += Double.parseDouble(summaryBean.getCost());
//            }
//        }
//
//        for(BookingHistoryDiscountBean sessionDiscountBean : summaryBean.getDiscountList()) {
//            double discountForThisChild =  totalCostPerChild * Integer.parseInt(sessionDiscountBean.getDiscountValue()) / 100;
//            totalDiscount += discountForThisChild;
//            sessionDiscountBean.setDiscountGiven(discountForThisChild);
//        }
//
//        if(parentSessionDiscountsAdapter != null) {
//            parentSessionDiscountsAdapter.notifyDataSetChanged();
//        }
//
//        //Calculate discount here from discount array
//        /*if(!summaryBean.isTrialSelected()) {
//            discount = (totalCost * Double.parseDouble(discountBean.getDiscountValue())) / 100;
//        }*/
//
//        costAfterDiscount = totalCost - totalDiscount;
//
//        summaryBean.setTotalDiscount(totalDiscount);
//        summaryBean.setAmountAfterDiscount(costAfterDiscount);
//
//
//        summaryBean.setSessionCost(totalCost);
//        notifyDataSetChanged();
//
//        parentBookAcademySummaryScreen.updateValues();
//
//    }

}