package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.parent.ParentChildTimeline;

import java.util.ArrayList;

public class ParentChildrenListingAdapter extends BaseAdapter{

    LayoutInflater layoutInflater;
    ArrayList<ChildBean> childrenListing;
    Context context;
    ImageView career;
    ImageView timeline;

    Typeface helvetica;
    Typeface linoType;

    public ParentChildrenListingAdapter (Context context, ArrayList<ChildBean> childrenListing){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.childrenListing = childrenListing;

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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.parent_adapter_child_name_item, null);

        RelativeLayout mainRelativeLayout = (RelativeLayout) view.findViewById(R.id.mainRelativeLayout);
        TextView fullName = (TextView) view.findViewById(R.id.fullName);
        TextView genderAndAge = (TextView) view.findViewById(R.id.genderAndAge);
        TextView badgeCount = (TextView) view.findViewById(R.id.badgeCount);
        ImageView career = (ImageView) view.findViewById(R.id.career);
        ImageView timeline = (ImageView) view.findViewById(R.id.timeline);
        ImageView lockUnlock = view.findViewById(R.id.lockUnlock);

        ChildBean currentChild = childrenListing.get(position);

        fullName.setText(currentChild.getFullName());
        genderAndAge.setText(currentChild.getGender()+", "+currentChild.getAge());

        fullName.setTypeface(linoType);
        genderAndAge.setTypeface(helvetica);

        if(position % 2 == 0){
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            fullName.setTextColor(context.getResources().getColor(R.color.white));
            genderAndAge.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            fullName.setTextColor(context.getResources().getColor(R.color.black));
            genderAndAge.setTextColor(context.getResources().getColor(R.color.black));
        }

        if(currentChild.getBadgeCount().equalsIgnoreCase("0")){
            badgeCount.setVisibility(View.INVISIBLE);
        }else{
            badgeCount.setVisibility(View.VISIBLE);
            badgeCount.setText(currentChild.getBadgeCount());
        }

        if(currentChild.getIsPrivate().equalsIgnoreCase("0")){
            lockUnlock.setBackgroundResource(R.drawable.unlock);
        } else {
            lockUnlock.setBackgroundResource(R.drawable.lock);
        }

        career.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChildBean clickedOnChild = childrenListing.get(position);

                Intent timeline = new Intent(context, ParentChildTimeline.class);
                timeline.putExtra("currentChild", clickedOnChild);
                timeline.putExtra("type", "career");
                context.startActivity(timeline);
            }
        });

        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChildBean clickedOnChild = childrenListing.get(position);

                Intent timeline = new Intent(context, ParentChildTimeline.class);
                timeline.putExtra("currentChild", clickedOnChild);
                timeline.putExtra("type", "timeline");
                context.startActivity(timeline);
            }
        });

        return view;
    }
}