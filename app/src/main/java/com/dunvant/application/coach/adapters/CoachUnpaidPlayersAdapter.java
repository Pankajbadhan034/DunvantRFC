package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.dunvant.application.R;
import com.dunvant.application.beans.CoachChildUnpaidPlayerBean;

import java.util.ArrayList;

public class CoachUnpaidPlayersAdapter extends BaseAdapter {

    Context context;
    ArrayList<CoachChildUnpaidPlayerBean> coachChildUnpaidPlayerBeanArrayList;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public CoachUnpaidPlayersAdapter(Context context, ArrayList<CoachChildUnpaidPlayerBean> coachChildUnpaidPlayerBeanArrayList) {
        this.context = context;
        this.coachChildUnpaidPlayerBeanArrayList = coachChildUnpaidPlayerBeanArrayList;
        layoutInflater = LayoutInflater.from(context);
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return coachChildUnpaidPlayerBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return coachChildUnpaidPlayerBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_unpaid_attendance_item, null);

        CheckBox unpaidChildName = convertView.findViewById(R.id.unpaidChildName);
        unpaidChildName.setTypeface(helvetica);

        final CoachChildUnpaidPlayerBean coachChildUnpaidPlayerBean = coachChildUnpaidPlayerBeanArrayList.get(position);
        unpaidChildName.setText(coachChildUnpaidPlayerBean.getFullName());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int sixtyPercent = (screenWidth * 60) / 100;
        float textWidthForTitle = unpaidChildName.getPaint().measureText(unpaidChildName.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / sixtyPercent) + 1;
        unpaidChildName.setLines(numberOfLines);

        if(coachChildUnpaidPlayerBean.getCheck().equalsIgnoreCase("true")){
            unpaidChildName.setChecked(true);
        }else{
            unpaidChildName.setChecked(false);
        }

        unpaidChildName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(coachChildUnpaidPlayerBean.getCheck().equals("true")){
                    coachChildUnpaidPlayerBean.setCheck("false");
                }else{
                    coachChildUnpaidPlayerBean.setCheck("true");
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}