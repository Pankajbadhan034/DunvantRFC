package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;


public class CoachSessionDatesAdapter extends BaseAdapter{

    Context context;
    String datesArray[];
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public CoachSessionDatesAdapter(Context context, String datesArray[]) {
        this.context = context;
        this.datesArray = datesArray;
        this.layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return datesArray.length;
    }

    @Override
    public Object getItem(int position) {
        return datesArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_date_item, null);

        TextView date = (TextView) convertView.findViewById(R.id.sessionDate);
        date.setText(datesArray[position]);
        date.setTypeface(helvetica);

        if(position % 2 != 0) {
            date.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
        }

        return convertView;
    }
}