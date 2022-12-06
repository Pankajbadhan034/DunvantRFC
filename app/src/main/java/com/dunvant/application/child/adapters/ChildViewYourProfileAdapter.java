package com.dunvant.application.child.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.DataRegistrationBean;

import java.util.ArrayList;

public class ChildViewYourProfileAdapter extends BaseAdapter {

    Context context;
    ArrayList<DataRegistrationBean> dataRegistrationBeanArrayList;

    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public ChildViewYourProfileAdapter(Context context, ArrayList<DataRegistrationBean> dataRegistrationBeanArrayList){
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
        convertView = layoutInflater.inflate(R.layout.child_view_your_profile_adapter, null);

        TextView labelName = (TextView) convertView.findViewById(R.id.labelName);
        TextView value = (TextView) convertView.findViewById(R.id.value);

        final DataRegistrationBean dataRegistrationBean = dataRegistrationBeanArrayList.get(position);

        labelName.setText(dataRegistrationBean.getLabel_name());
        value.setText(dataRegistrationBean.getValue());

        labelName.setTypeface(helvetica);
        value.setTypeface(linoType);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int sixtyPercent = (screenWidth * 60) / 100;
        float textWidthForTitle = labelName.getPaint().measureText(labelName.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / sixtyPercent) + 1;
        labelName.setLines(numberOfLines);

        return convertView;
    }
}