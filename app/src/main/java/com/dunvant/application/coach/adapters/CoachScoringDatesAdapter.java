package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ScoringDateBean;

import java.util.ArrayList;

public class CoachScoringDatesAdapter extends BaseAdapter{

    Context context;
    ArrayList<ScoringDateBean> scoringDatesListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public CoachScoringDatesAdapter(Context context, ArrayList<ScoringDateBean> scoringDatesListing){
        this.context = context;
        this.scoringDatesListing = scoringDatesListing;
        this.layoutInflater = LayoutInflater.from(context);
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return scoringDatesListing.size();
    }

    @Override
    public Object getItem(int i) {
        return scoringDatesListing.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_scoring_date_item, null);

        TextView scoringDate = convertView.findViewById(R.id.scoringDate);

        scoringDate.setText(scoringDatesListing.get(position).getValue());
        scoringDate.setTypeface(helvetica);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_scoring_date_dropdown_item, null);

        TextView scoringDate = convertView.findViewById(R.id.scoringDate);

        scoringDate.setText(scoringDatesListing.get(position).getValue());
        scoringDate.setTypeface(helvetica);

        return convertView;
    }
}