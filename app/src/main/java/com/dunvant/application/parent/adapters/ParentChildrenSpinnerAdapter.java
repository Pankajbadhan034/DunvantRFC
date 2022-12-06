package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChildBean;

import java.util.ArrayList;

public class ParentChildrenSpinnerAdapter extends BaseAdapter{

    Context context;
    ArrayList<ChildBean> childrenListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentChildrenSpinnerAdapter(Context context, ArrayList<ChildBean> childrenListing){
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
        convertView = layoutInflater.inflate(R.layout.parent_adapter_children_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(childrenListing.get(position).getFullName());

        name.setTypeface(helvetica);

        return convertView;
    }
}