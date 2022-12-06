package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChildScoreBean;

import java.util.ArrayList;

public class CoachChildScoreTextAdapter extends BaseAdapter{

    Context context;
    ArrayList<ChildScoreBean> childScoresListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public CoachChildScoreTextAdapter(Context context, ArrayList<ChildScoreBean> childScoresListing){
        this.context = context;
        this.childScoresListing = childScoresListing;
        layoutInflater = LayoutInflater.from(context);
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return childScoresListing.size();
    }

    @Override
    public Object getItem(int position) {
        return childScoresListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_text_score_item, null);

        TextView scoreName = (TextView) convertView.findViewById(R.id.scoreName);
        TextView scores = (TextView) convertView.findViewById(R.id.scores);

        ChildScoreBean childScoreBean = childScoresListing.get(position);

        scoreName.setText(childScoreBean.getElementName());
        scores.setText(childScoreBean.getScore()+"");

        scoreName.setTypeface(helvetica);
        scores.setTypeface(helvetica);

        return convertView;
    }
}