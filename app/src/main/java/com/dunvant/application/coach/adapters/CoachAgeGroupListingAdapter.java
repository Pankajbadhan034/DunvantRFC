package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class CoachAgeGroupListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<AgeGroupBean> ageGroupListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public CoachAgeGroupListingAdapter(Context context, ArrayList<AgeGroupBean> ageGroupListing) {
        this.context = context;
        this.ageGroupListing = ageGroupListing;
        this.layoutInflater = LayoutInflater.from(context);
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return ageGroupListing.size();
    }

    @Override
    public Object getItem(int position) {
        return ageGroupListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_age_group_item, null);

        TextView ageGroupName = (TextView) convertView.findViewById(R.id.ageGroupName);
        ListView dateResultListing = (ListView) convertView.findViewById(R.id.dateResultListing);

        AgeGroupBean ageGroupBean = ageGroupListing.get(position);

        ageGroupName.setText(ageGroupBean.getGroupName());
        dateResultListing.setAdapter(new CoachDateResultListingAdapter(context, ageGroupBean.getDatesResultList()));
        Utilities.setListViewHeightBasedOnChildren(dateResultListing);

        ageGroupName.setTypeface(helvetica);

        return convertView;
    }
}