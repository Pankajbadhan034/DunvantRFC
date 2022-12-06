package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.FeesBean;

import java.util.ArrayList;

public class ParentFeesListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<FeesBean> feesListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentFeesListingAdapter(Context context, ArrayList<FeesBean> feesListing) {
        this.context = context;
        this.feesListing = feesListing;
        this.layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return feesListing.size();
    }

    @Override
    public Object getItem(int position) {
        return feesListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_fees_item, null);

        LinearLayout mainLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
        TextView feeLabel = (TextView) convertView.findViewById(R.id.feeLabel);
        TextView feeValue = (TextView) convertView.findViewById(R.id.feeValue);

        FeesBean feesBean = feesListing.get(position);
        feeLabel.setText(feesBean.getName());
        feeValue.setText(feesBean.getValue());

        feeLabel.setTypeface(helvetica);
        feeValue.setTypeface(helvetica);

//        if(position % 2 == 0){
//            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
//        } else {
//            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
//        }

        return convertView;
    }
}