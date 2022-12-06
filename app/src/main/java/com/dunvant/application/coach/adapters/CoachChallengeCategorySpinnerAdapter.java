package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChallengeCategoryBean;

import java.util.ArrayList;

public class CoachChallengeCategorySpinnerAdapter extends BaseAdapter{

    Context context;
    ArrayList<ChallengeCategoryBean> categoriesList;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public CoachChallengeCategorySpinnerAdapter(Context context, ArrayList<ChallengeCategoryBean> categoriesList) {
        this.context = context;
        this.categoriesList = categoriesList;
        layoutInflater = LayoutInflater.from(context);
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return categoriesList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoriesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_category_item, null);

        TextView categoryName = (TextView) convertView.findViewById(R.id.categoryName);
        categoryName.setText(categoriesList.get(position).getName());

        categoryName.setTypeface(helvetica);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_category_dropdown_item, null);

        TextView categoryName = (TextView) convertView.findViewById(R.id.categoryName);
        categoryName.setText(categoriesList.get(position).getName());

        categoryName.setTypeface(helvetica);

        return convertView;
    }
}