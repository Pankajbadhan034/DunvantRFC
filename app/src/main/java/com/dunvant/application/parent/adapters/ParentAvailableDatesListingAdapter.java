package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dunvant.application.R;
import com.dunvant.application.beans.CampDateBean;
import com.dunvant.application.parent.ParentBookCampScreenOLD;

import java.util.ArrayList;

public class ParentAvailableDatesListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<CampDateBean> availableDatesListing;
    LayoutInflater layoutInflater;
    ParentBookCampScreenOLD parentBookCampScreenOLD;
    Typeface helvetica;
    Typeface linoType;

    public ParentAvailableDatesListingAdapter(Context context, ArrayList<CampDateBean> availableDatesListing, ParentBookCampScreenOLD parentBookCampScreenOLD) {
        this.context = context;
        this.availableDatesListing = availableDatesListing;
        this.parentBookCampScreenOLD = parentBookCampScreenOLD;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return availableDatesListing.size();
    }

    @Override
    public Object getItem(int position) {
        return availableDatesListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_available_date_item, null);

        CheckBox dateCheckBox = (CheckBox) convertView.findViewById(R.id.dateCheckBox);

        final CampDateBean campDateBean = availableDatesListing.get(position);

        dateCheckBox.setText(campDateBean.getDate());
        dateCheckBox.setChecked(campDateBean.isSelected());

        dateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    campDateBean.setSelected(true);
                } else {
                    campDateBean.setSelected(false);
                }
                parentBookCampScreenOLD.calculateCost();
            }
        });

        dateCheckBox.setTypeface(helvetica);

        return convertView;
    }
}