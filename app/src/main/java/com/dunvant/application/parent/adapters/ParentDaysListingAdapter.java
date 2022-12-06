package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CampDaysBean;

import java.util.ArrayList;

public class ParentDaysListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<CampDaysBean> daysListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentDaysListingAdapter(Context context, ArrayList<CampDaysBean> daysListing) {
        this.context = context;
        this.daysListing = daysListing;
        this.layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return daysListing.size();
    }

    @Override
    public Object getItem(int position) {
        return daysListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_days_item, null);

        TextView dayName = (TextView) convertView.findViewById(R.id.dayName);
        dayName.setText(daysListing.get(position).getDayLabel());
        dayName.setTypeface(helvetica);

        return convertView;
    }
}