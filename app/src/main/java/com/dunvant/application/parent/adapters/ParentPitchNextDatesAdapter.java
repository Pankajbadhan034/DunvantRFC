package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.PitchDateBean;
import com.dunvant.application.parent.ParentBookPitchScreen;

import java.util.ArrayList;

public class ParentPitchNextDatesAdapter extends BaseAdapter{

    Context context;
    ArrayList<PitchDateBean> pitchDateBeanListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;
    boolean shouldShowTime;
    ParentBookPitchScreen parentBookPitchScreen;

    public ParentPitchNextDatesAdapter(Context context, ArrayList<PitchDateBean> pitchDateBeanListing, boolean shouldShowTime, ParentBookPitchScreen parentBookPitchScreen){
        this.context = context;
        this.pitchDateBeanListing = pitchDateBeanListing;
        this.shouldShowTime = shouldShowTime;
        this.parentBookPitchScreen = parentBookPitchScreen;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return pitchDateBeanListing.size();
    }

    @Override
    public Object getItem(int position) {
        return pitchDateBeanListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_pitch_date_slot, null);

        TextView dateSlot = (TextView) convertView.findViewById(R.id.dateSlot);
        final PitchDateBean pitchDateBean = pitchDateBeanListing.get(position);

        if(!shouldShowTime){
            dateSlot.setText(pitchDateBean.getDate());
        } else {
            if(pitchDateBean.getTimeSlots() != null && !pitchDateBean.getTimeSlots().isEmpty()){
                dateSlot.setText(pitchDateBean.getDate()+" "+pitchDateBean.getTimeSlots().get(0).getTimeSlot());
            } else {
                dateSlot.setText(pitchDateBean.getDate());
            }
        }

        dateSlot.setTypeface(helvetica);

//        dateSlot.setLines(2);

        if(pitchDateBean.isSelected()) {
            dateSlot.setBackgroundColor(context.getResources().getColor(R.color.darkGreen));
            dateSlot.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            dateSlot.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
            dateSlot.setTextColor(context.getResources().getColor(R.color.black));
        }

        dateSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentBookPitchScreen.changePitchDate(pitchDateBean);
                /*pitchDateBean.setSelected(!pitchDateBean.isSelected());
                notifyDataSetChanged();*/
            }
        });

        return convertView;
    }
}