package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.PitchTypeBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentPitchSpecialPricesAdapter extends BaseAdapter {

    Context context;
    ArrayList<PitchTypeBean> specialPricesList;
    LayoutInflater layoutInflater;

    public ParentPitchSpecialPricesAdapter(Context context, ArrayList<PitchTypeBean> specialPricesList){
        this.context = context;
        this.specialPricesList = specialPricesList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return specialPricesList.size();
    }

    @Override
    public Object getItem(int i) {
        return specialPricesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.parent_adapter_pitch_special_item, null);

        TextView pitchPricing = view.findViewById(R.id.pitchPricing);

        PitchTypeBean specialPriceBean = specialPricesList.get(i);

        String strPricing = "";

        if(specialPriceBean.getPitchType().equalsIgnoreCase("0")){
            strPricing = "Pitch Pricing";
        } else if(specialPriceBean.getPitchType().equalsIgnoreCase("1")){
            strPricing = "Full Pitch Pricing";
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);

        pitchPricing.setText(/*strPricing+" | "+*/specialPriceBean.getFromDateFormatted()+" - "+specialPriceBean.getToDateFormatted()+" ("+specialPriceBean.getDayLabel()+") | "+specialPriceBean.getFromTimeFormatted()+" - "+specialPriceBean.getToTimeFormatted()+" | "+specialPriceBean.getHourPrice()+academy_currency);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int ninetyPercent = (screenWidth * 90) / 100;
        float textWidthForTitle = pitchPricing.getPaint().measureText(pitchPricing.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / ninetyPercent) + 1;
        pitchPricing.setLines(numberOfLines);

        return view;
    }
}