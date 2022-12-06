package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CountryBean;

import java.util.ArrayList;

public class ParentCountryCodeAdapter extends BaseAdapter{

    Context context;
    ArrayList<CountryBean> countryList;
    LayoutInflater layoutInflater;

    public ParentCountryCodeAdapter(Context context, ArrayList<CountryBean> countryList){
        this.context = context;
        this.countryList = countryList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public Object getItem(int i) {
        return countryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.parent_adapter_country_item, null);

        TextView countryName = view.findViewById(R.id.countryName);

        CountryBean countryBean = countryList.get(i);
        countryName.setText(countryBean.getCountry()/*+" ("+countryBean.getDialingCode()+")"*/);

        return view;
    }
}