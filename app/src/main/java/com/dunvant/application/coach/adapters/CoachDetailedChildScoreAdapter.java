package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChildDetailScoreBean;

import java.util.ArrayList;

public class CoachDetailedChildScoreAdapter extends BaseAdapter{

    Context context;
    ArrayList<ChildDetailScoreBean> detailScoresListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;
    int n1;
    int n2;

    public CoachDetailedChildScoreAdapter(Context context, ArrayList<ChildDetailScoreBean> detailScoresListing, int n1, int n2) {
        this.context = context;
        this.detailScoresListing = detailScoresListing;
        this.layoutInflater = LayoutInflater.from(context);
        this.n1 = n1;
        this.n2 = n2;
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return detailScoresListing.size();
    }

    @Override
    public Object getItem(int position) {
        return detailScoresListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_detailed_score_item, null);

        TextView subElementName = (TextView) convertView.findViewById(R.id.subElementName);
        final TextView valueTextView = (TextView) convertView.findViewById(R.id.value);
        final SeekBar seekBar = (SeekBar) convertView.findViewById(R.id.seekBar);

        final ChildDetailScoreBean childDetailScoreBean = detailScoresListing.get(position);

        subElementName.setText(childDetailScoreBean.getSubElementName());
        valueTextView.setText(childDetailScoreBean.getScore()+"");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar arg0) {
            }
            public void onStartTrackingTouch(SeekBar arg0) {
            }
            public void onProgressChanged(SeekBar arg0, int value, boolean arg2) {

                valueTextView.setText(value+"");

                if (value <= n1) {
                    seekBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_red_progress));
                } else if (value>n1 && value<=n2) {
                    seekBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_blue_progress));
                } else if (value>n2) {
                    seekBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_green_progress));
                }

                childDetailScoreBean.setScore(value+"");
            }
        });

        if(!childDetailScoreBean.getScore().isEmpty()) {
            int scoreValue = Integer.parseInt(childDetailScoreBean.getScore());
            seekBar.setProgress(scoreValue);
        }

        subElementName.setTypeface(helvetica);
        valueTextView.setTypeface(helvetica);

        return convertView;
    }
}