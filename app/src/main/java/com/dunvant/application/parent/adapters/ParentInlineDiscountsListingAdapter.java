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
import com.dunvant.application.beans.BookingHistoryDiscountBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentInlineDiscountsListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<BookingHistoryDiscountBean> inlineDiscountsListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentInlineDiscountsListingAdapter(Context context, ArrayList<BookingHistoryDiscountBean> inlineDiscountsListing){
        this.context = context;
        this.inlineDiscountsListing = inlineDiscountsListing;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return inlineDiscountsListing.size();
    }

    @Override
    public Object getItem(int position) {
        return inlineDiscountsListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_inline_discount_item, null);

        LinearLayout mainLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
        TextView discountLabel = (TextView) convertView.findViewById(R.id.discountLabel);
        TextView discountValue = (TextView) convertView.findViewById(R.id.discountValue);

        discountLabel.setTypeface(helvetica);
        discountValue.setTypeface(helvetica);

        BookingHistoryDiscountBean bookingHistoryDiscountBean = inlineDiscountsListing.get(position);
        discountLabel.setText(bookingHistoryDiscountBean.getDiscountLabel());

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);

        if(bookingHistoryDiscountBean.getDeductType().equalsIgnoreCase("1")){
            discountValue.setText(bookingHistoryDiscountBean.getDiscountValue()+" "+academy_currency);
        } else if(bookingHistoryDiscountBean.getDeductType().equalsIgnoreCase("2")) {
            discountValue.setText(bookingHistoryDiscountBean.getDiscountValue()+" "+academy_currency/*+"("+bookingHistoryDiscountBean.getDeductValue()+"%)"*/);
        }

//        if(position % 2 == 0){
//            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
//        } else {
//            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
//        }

        return convertView;
    }
}