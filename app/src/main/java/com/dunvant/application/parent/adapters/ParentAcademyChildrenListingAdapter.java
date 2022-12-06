package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChildBean;

import java.util.ArrayList;

public class ParentAcademyChildrenListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<ChildBean> childrenListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentAcademyChildrenListingAdapter(Context context, ArrayList<ChildBean> childrenListing){
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.parent_adapter_academy_children_listing_item, null);

        LinearLayout mainLinearLayout = (LinearLayout) view.findViewById(R.id.mainLinearLayout);
        CheckBox childName = (CheckBox) view.findViewById(R.id.childName);
        childName.getBackground().setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        TextView genderAndAge = (TextView) view.findViewById(R.id.genderAndAge);

        final ChildBean currentChild = childrenListing.get(position);

        childName.setText(currentChild.getFullName());
        genderAndAge.setText(currentChild.getGender()+", "+currentChild.getAge());

        if(currentChild.isSelected()) {
            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            childName.setChecked(true);
        } else {
            mainLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            childName.setChecked(false);
        }

        childName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    currentChild.setSelected(true);
                } else {
                    currentChild.setSelected(false);
                }
                notifyDataSetChanged();
            }
        });

        childName.setTypeface(linoType);
        genderAndAge.setTypeface(helvetica);

        return view;
    }
}