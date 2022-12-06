package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.AcademySessionDateBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentBookedSessionDatesAdapter extends BaseAdapter{

    Context context;
    ArrayList<AcademySessionDateBean> datesListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentBookedSessionDatesAdapter(Context context, ArrayList<AcademySessionDateBean> datesListing){
        this.context = context;
        this.datesListing = datesListing;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return datesListing.size();
    }

    @Override
    public Object getItem(int position) {
        return datesListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_booked_dates_item, null);

        TextView sessionNumber = (TextView) convertView.findViewById(R.id.sessionNumber);
        TextView date = (TextView) convertView.findViewById(R.id.sessionDate);
        TextView cost = (TextView) convertView.findViewById(R.id.cost);
        TextView sessionHours = (TextView)  convertView.findViewById(R.id.sessionHours);

        AcademySessionDateBean dateBean = datesListing.get(position);

        sessionNumber.setText("Session "+(position+1));
        date.setText(dateBean.getShowDate());

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);
        cost.setText(dateBean.getCost()+" "+academy_currency);

        sessionHours.setText(dateBean.getNumberOfHours()+" hours");

        sessionNumber.setTypeface(linoType);
        date.setTypeface(helvetica);
        cost.setTypeface(helvetica);
        sessionHours.setTypeface(helvetica);

        return convertView;
    }
}