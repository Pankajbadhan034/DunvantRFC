package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.SessionDateBean;

import java.util.ArrayList;

public class CoachSessionDatesForSpinnerAdapter extends BaseAdapter{

    Context context;
    ArrayList<SessionDateBean> sessionDatesList;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public CoachSessionDatesForSpinnerAdapter(Context context, ArrayList<SessionDateBean> sessionDatesList) {
        this.context = context;
        this.sessionDatesList = sessionDatesList;
        this.layoutInflater = LayoutInflater.from(context);
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return sessionDatesList.size();
    }

    @Override
    public Object getItem(int position) {
        return sessionDatesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_session_date_for_spinner_item, null);

        TextView sessionDate = (TextView) convertView.findViewById(R.id.sessionDate);

        final SessionDateBean sessionDateBean = sessionDatesList.get(position);

        sessionDate.setText(sessionDateBean.getShowSessionDate());

        sessionDate.setTypeface(helvetica);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_session_date_for_spinner_dropdown_item, null);

        TextView sessionDate = (TextView) convertView.findViewById(R.id.sessionDate);

        final SessionDateBean sessionDateBean = sessionDatesList.get(position);

        sessionDate.setText(sessionDateBean.getShowSessionDate());

        sessionDate.setTypeface(helvetica);

        return convertView;
    }
}