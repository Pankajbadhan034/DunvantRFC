package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.TermBean;

import java.util.ArrayList;

public class ParentTermsListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<TermBean> termsListing;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public ParentTermsListingAdapter(Context context, ArrayList<TermBean> termsListing){
        this.context = context;
        this.termsListing = termsListing;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return termsListing.size();
    }

    @Override
    public Object getItem(int position) {
        return termsListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_terms_item, null);

        TextView termName = (TextView) convertView.findViewById(R.id.termName);
        TextView termDates = (TextView) convertView.findViewById(R.id.termDates);

        TermBean termBean = termsListing.get(position);

        termName.setText(termBean.getTermName());
        termDates.setText(termBean.getShowFromDate()+" to "+termBean.getShowToDate());

        termName.setTypeface(linoType);
        termDates.setTypeface(helvetica);

        return convertView;
    }
}