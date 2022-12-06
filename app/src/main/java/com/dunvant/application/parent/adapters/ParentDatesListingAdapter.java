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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.AcademySessionDateBean;
import com.dunvant.application.beans.AcademySessionSummaryBean;
import com.dunvant.application.beans.BookingHistoryDiscountBean;
import com.dunvant.application.parent.ParentBookAcademySummaryScreen;
import com.dunvant.application.utils.Utilities;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ParentDatesListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<AcademySessionDateBean> datesList;
    LayoutInflater layoutInflater;
    ParentAcademySessionSummaryAdapter summaryAdapter;
    AcademySessionSummaryBean summaryBean;
    ParentBookAcademySummaryScreen parentBookAcademySummaryScreen;

    Typeface helvetica;
    Typeface linoType;

    public ParentDatesListingAdapter(Context context, ArrayList<AcademySessionDateBean> datesList, ParentAcademySessionSummaryAdapter summaryAdapter, AcademySessionSummaryBean summaryBean, ParentBookAcademySummaryScreen parentBookAcademySummaryScreen) {
        this.context = context;
        this.datesList = datesList;
        this.summaryAdapter = summaryAdapter;
        this.summaryBean = summaryBean;
        this.parentBookAcademySummaryScreen = parentBookAcademySummaryScreen;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return datesList.size();
    }

    @Override
    public Object getItem(int position) {
        return datesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //System.out.println("Get View "+position);

        convertView = layoutInflater.inflate(R.layout.parent_adapter_date_item, null);

        CheckBox sessionNumber = (CheckBox) convertView.findViewById(R.id.sessionNumber);
        TextView date = (TextView) convertView.findViewById(R.id.sessionDate);
        TextView cost = (TextView) convertView.findViewById(R.id.cost);

        sessionNumber.setText("SESSION "+(position+1));

        date.setText(datesList.get(position).getShowDate()+" ("+summaryBean.getNumberOfHours()+")");

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int eightyPercent = (screenWidth * 80) / 100;
        float textWidthForTitle = date.getPaint().measureText(datesList.get(position).getShowDate()+" ("+summaryBean.getNumberOfHours()+" Hours)");
        int numberOfLines = ((int) textWidthForTitle / eightyPercent) + 1;
        date.setLines(numberOfLines);

        DecimalFormat df = new DecimalFormat("#.00");

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);
        cost.setText(summaryBean.getCost()+" "+academy_currency);

        sessionNumber.setTypeface(linoType);
        date.setTypeface(helvetica);
        cost.setTypeface(helvetica);

        sessionNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //System.out.println("sessionNumber  "+datesList.get(position).getDate()+" "+isChecked);

                if(isChecked) {
                    datesList.get(position).setSelected(true);
                } else {
                    datesList.get(position).setSelected(false);
                }

                calculate();

//                summaryAdapter.calculateSessionCost(summaryBean);

                /*if(isChecked) {
                    datesList.get(position).setSelected(true);

                    for(AcademySessionDateBean db : summaryBean.getAvailableDatesList()) {
                        if (datesList.get(position).getDate().equalsIgnoreCase(db.getDate())) {
                            db.setSelected(true);
                            break;
                        }
                    }
                } else {
                    //System.out.println("in else");
                    datesList.get(position).setSelected(false);

                    for(AcademySessionDateBean db : summaryBean.getAvailableDatesList()) {
                        if (datesList.get(position).getDate().equalsIgnoreCase(db.getDate())) {
                            db.setSelected(false);
                            break;
                        }
                    }

                    //System.out.println("else end");
                }

                summaryAdapter.calculateSessionCost(summaryBean);*/

                //System.out.println("listener end");
            }
        });

        if(summaryBean.getIsSelectiveAllowed().equalsIgnoreCase("0")){
            //System.out.println("is selective allowed 0");
            sessionNumber.setEnabled(false);
            sessionNumber.setChecked(true);
        }

        sessionNumber.setChecked(datesList.get(position).isSelected());

        return convertView;
    }

    private void calculate(){
        double totalCost = 0;

        double totalCostPerChild = 0;

        double totalDiscount = 0;
        double costAfterDiscount = 0;

        for (AcademySessionDateBean dateBean : summaryBean.getAvailableDatesList()) {
            if(dateBean.isSelected()) {
                totalCost += summaryBean.getChildrenList().size() * Double.parseDouble(summaryBean.getCost());
                totalCostPerChild += Double.parseDouble(summaryBean.getCost());
            }
        }

        for(BookingHistoryDiscountBean bookingHistoryDiscountBean : summaryBean.getDiscountList()) {
            double discountForThisChild =  totalCostPerChild * Integer.parseInt(bookingHistoryDiscountBean.getDiscountValue()) / 100;
            totalDiscount += discountForThisChild;
            bookingHistoryDiscountBean.setDiscountGiven(discountForThisChild);
        }

//        if(parentSessionDiscountsAdapter != null) {
//            parentSessionDiscountsAdapter.notifyDataSetChanged();
//        }

        costAfterDiscount = totalCost - totalDiscount;

        summaryBean.setTotalDiscount(totalDiscount);
        summaryBean.setAmountAfterDiscount(costAfterDiscount);
        summaryBean.setSessionCost(totalCost);

        summaryAdapter.notifyDataSetChanged();

//        notifyDataSetChanged();
        parentBookAcademySummaryScreen.updateValues();
    }

}