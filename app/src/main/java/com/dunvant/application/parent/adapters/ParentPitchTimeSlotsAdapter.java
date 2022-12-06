package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.PitchTimeSlotBean;
import com.dunvant.application.parent.ParentBookPitchScreen;

import java.util.ArrayList;

public class ParentPitchTimeSlotsAdapter extends BaseAdapter{

    Context context;
    ArrayList<PitchTimeSlotBean> timeSlotsListing;
    LayoutInflater layoutInflater;
    ParentBookPitchScreen parentBookPitchScreen;
    Typeface helvetica;
    Typeface linoType;

    public ParentPitchTimeSlotsAdapter(Context context, ArrayList<PitchTimeSlotBean> timeSlotsListing, ParentBookPitchScreen parentBookPitchScreen){
        this.context = context;
        this.timeSlotsListing = timeSlotsListing;
        layoutInflater = LayoutInflater.from(context);
        this.parentBookPitchScreen = parentBookPitchScreen;

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return timeSlotsListing.size();
    }

    @Override
    public Object getItem(int position) {
        return timeSlotsListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_pitch_time_slot, null);

        TextView timeSlot = (TextView) convertView.findViewById(R.id.timeSlot);
        final PitchTimeSlotBean timeSlotBean = timeSlotsListing.get(position);
        timeSlot.setText(timeSlotBean.getTimeSlot());

        timeSlot.setTypeface(helvetica);

//        timeSlot.setLines(2);

        if(timeSlotBean.isSelected()) {
            timeSlot.setBackgroundColor(context.getResources().getColor(R.color.darkGreen));
            timeSlot.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            timeSlot.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
            timeSlot.setTextColor(context.getResources().getColor(R.color.black));
        }

        timeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSlotBean.setSelected(!timeSlotBean.isSelected());

                parentBookPitchScreen.changeTimeSlotInAllPitches(timeSlotBean);

                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}