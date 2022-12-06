package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CampSelectedChildBean;

import java.util.ArrayList;

public class ParentCampChildrenNamesGridAdapter extends BaseAdapter{

    Context context;
    ArrayList<CampSelectedChildBean> childrenListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentCampChildrenNamesGridAdapter(Context context, ArrayList<CampSelectedChildBean> childrenListing){
        this.context = context;
        this.childrenListing = childrenListing;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return childrenListing.size();
    }

    @Override
    public Object getItem(int position) {
        return childrenListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_child_name_grid_item, null);

        TextView childName = (TextView) convertView.findViewById(R.id.childName);
        childName.setTypeface(helvetica);

        CampSelectedChildBean childBean = childrenListing.get(position);
        childName.setText(childBean.getChildBean().getFullName());

        return convertView;
    }
}