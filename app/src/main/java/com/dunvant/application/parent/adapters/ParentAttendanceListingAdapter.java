package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.AttendanceDateBean;

import java.util.ArrayList;

public class ParentAttendanceListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<AttendanceDateBean> attendanceDateList;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentAttendanceListingAdapter(Context context, ArrayList<AttendanceDateBean> attendanceDateList) {
        this.context = context;
        this.attendanceDateList = attendanceDateList;
        this.layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return attendanceDateList.size();
    }

    @Override
    public Object getItem(int position) {
        return attendanceDateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_attendance_item, null);

        TextView dayName = (TextView) convertView.findViewById(R.id.dayName);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView catchUpSession = (TextView) convertView.findViewById(R.id.catchUpSession);

        dayName.setTypeface(helvetica);
        catchUpSession.setTypeface(helvetica);

        AttendanceDateBean attendanceDateBean = attendanceDateList.get(position);
        dayName.setText(attendanceDateBean.getShowBookedSessionDate());

        switch (attendanceDateBean.getStatus()) {
            case "0":
                image.setBackgroundResource(R.drawable.dots);
                break;
            case "1":
                image.setBackgroundResource(R.drawable.present);
                break;
            case "2":
                image.setBackgroundResource(R.drawable.absent);
                break;
        }

        return convertView;
    }
}