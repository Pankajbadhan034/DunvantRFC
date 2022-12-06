package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.DataRegistrationBean;

import java.util.ArrayList;

public class CoachParentDetailDialogAdapter extends BaseAdapter{

    Context context;
    ArrayList<DataRegistrationBean> dataRegistrationBeanArrayList;

    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public CoachParentDetailDialogAdapter(Context context, ArrayList<DataRegistrationBean> dataRegistrationBeanArrayList){
        this.context = context;
        this.dataRegistrationBeanArrayList = dataRegistrationBeanArrayList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return dataRegistrationBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataRegistrationBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_parent_detail_dialog_adapter, null);

        TextView text = (TextView) convertView.findViewById(R.id.text);

        final DataRegistrationBean dataRegistrationBean = dataRegistrationBeanArrayList.get(position);

        text.setText(dataRegistrationBean.getLabel_name()+": "+ dataRegistrationBean.getValue());

        //text.setTypeface(linoType);


        return convertView;
    }
}