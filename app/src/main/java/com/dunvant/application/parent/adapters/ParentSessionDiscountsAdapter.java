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
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.BookingHistoryDiscountBean;
import com.dunvant.application.utils.Utilities;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ParentSessionDiscountsAdapter extends BaseAdapter{

    Context context;
    ArrayList<BookingHistoryDiscountBean> discountsList;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public ParentSessionDiscountsAdapter(Context context, ArrayList<BookingHistoryDiscountBean> discountsList){
        this.context = context;
        this.discountsList = discountsList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return discountsList.size();
    }

    @Override
    public Object getItem(int position) {
        return discountsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_session_discount_item, null);

        TextView lblDiscount = (TextView) convertView.findViewById(R.id.lblDiscount);
        TextView discount = (TextView) convertView.findViewById(R.id.discount);
        TextView discountDescription = (TextView) convertView.findViewById(R.id.discountDescription);

        lblDiscount.setTypeface(helvetica);
        discount.setTypeface(helvetica);
        discountDescription.setTypeface(helvetica);

        BookingHistoryDiscountBean bookingHistoryDiscountBean = discountsList.get(position);

        if(bookingHistoryDiscountBean.getDiscountCode().equalsIgnoreCase("sibling_discount")) {
            lblDiscount.setText("Sibling Discount ("+ bookingHistoryDiscountBean.getChildName()+")");
        } else {
            lblDiscount.setText("Discount ("+ bookingHistoryDiscountBean.getChildName()+")");
        }

        DecimalFormat df = new DecimalFormat("#.00");

        discountDescription.setText(bookingHistoryDiscountBean.getDiscountDescription());
        if(bookingHistoryDiscountBean.getDiscountDescription() == null || bookingHistoryDiscountBean.getDiscountDescription().isEmpty()){
            discountDescription.setVisibility(View.GONE);
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);

        if(bookingHistoryDiscountBean.getDiscountGiven() == 0) {
            discount.setText("0.00 "+academy_currency);
        } else {
            discount.setText(df.format(bookingHistoryDiscountBean.getDiscountGiven())+" "+academy_currency+" ("+ bookingHistoryDiscountBean.getDiscountValue()+"%)");
        }

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int eightyPercent = (screenWidth * 80) / 100;
        float textWidthForTitle = lblDiscount.getPaint().measureText(lblDiscount.getText().toString());
        int n1 = ((int) textWidthForTitle / eightyPercent) + 1;
        lblDiscount.setLines(n1);

        textWidthForTitle = discount.getPaint().measureText(discount.getText().toString());
        int n2 = ((int) textWidthForTitle / eightyPercent) + 1;
        discount.setLines(n2);

        textWidthForTitle = discountDescription.getPaint().measureText(discountDescription.getText().toString());
        int n3 = ((int) textWidthForTitle / eightyPercent) + 1;
        discountDescription.setLines(n3);

        return convertView;
    }
}