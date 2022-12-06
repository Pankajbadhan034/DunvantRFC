
package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dunvant.application.R;
import com.dunvant.application.beans.AcademySessionDateBean;
import com.dunvant.application.coach.CoachMoveChildScreen;

import java.util.ArrayList;

public class CoachAvailableDatesAdapter extends BaseAdapter{

    Context context;
    ArrayList<AcademySessionDateBean> availableDatesListing;
    CoachMoveChildScreen coachMoveChildScreen;

    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public CoachAvailableDatesAdapter(Context context, ArrayList<AcademySessionDateBean> availableDatesListing, CoachMoveChildScreen coachMoveChildScreen) {
        this.context = context;
        this.availableDatesListing = availableDatesListing;
        this.layoutInflater = LayoutInflater.from(context);
        this.coachMoveChildScreen = coachMoveChildScreen;
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
        convertView = layoutInflater.inflate(R.layout.coach_adapter_available_date_item, null);

        CheckBox availableDate = (CheckBox) convertView.findViewById(R.id.availableDate);

        final AcademySessionDateBean availableDateBean = availableDatesListing.get(position);

        availableDate.setText(availableDateBean.getDate());

        availableDate.setChecked(availableDateBean.isSelected());
        availableDate.setEnabled(false);

        availableDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    availableDateBean.setSelected(true);
                } else {
                    availableDateBean.setSelected(false);
                }
                coachMoveChildScreen.updateSelectAll();
            }
        });

        availableDate.setTypeface(helvetica);

        return convertView;
    }
}