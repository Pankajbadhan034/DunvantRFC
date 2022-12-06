package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.AttributesValuesDataBean;

import java.util.ArrayList;

public class ParentDialogBoxShowAdapter extends BaseAdapter{

    Context context;
    ArrayList<AttributesValuesDataBean> pitchDateBeanListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;
    boolean shouldShowTime;

    public ParentDialogBoxShowAdapter(Context context, ArrayList<AttributesValuesDataBean> pitchDateBeanListing){
        this.context = context;
        this.pitchDateBeanListing = pitchDateBeanListing;
        this.shouldShowTime = shouldShowTime;
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
        convertView = layoutInflater.inflate(R.layout.parent_dialog_box_show_adapter, null);

        TextView text = (TextView) convertView.findViewById(R.id.text);
        final AttributesValuesDataBean pitchDateBean = pitchDateBeanListing.get(position);
        text.setText(pitchDateBean.getAttribute_value_name());

        if(position==0){
            text.setBackgroundColor(context.getResources().getColor(R.color.black));
            text.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            text.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
            text.setTextColor(context.getResources().getColor(R.color.black));
        }



        return convertView;
    }
}