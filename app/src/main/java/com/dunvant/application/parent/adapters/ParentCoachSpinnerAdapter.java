package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CoachBean;

import java.util.ArrayList;

public class ParentCoachSpinnerAdapter extends BaseAdapter{

    Context context;
    ArrayList<CoachBean> coachesList;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentCoachSpinnerAdapter(Context context, ArrayList<CoachBean> coachesList) {
        this.context = context;
        this.coachesList = coachesList;
        this.layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return coachesList.size();
    }

    @Override
    public Object getItem(int position) {
        return coachesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_coach_name_item, null);

        TextView coachName = (TextView) convertView.findViewById(R.id.coachName);

        coachName.setText(coachesList.get(position).getFullName());
        coachName.setTypeface(helvetica);

        return convertView;
    }
}