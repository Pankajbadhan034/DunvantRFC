package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChildDetailScoreBean;

import java.util.ArrayList;

public class ParentSubElementListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<ChildDetailScoreBean> detailedScoreListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentSubElementListingAdapter(Context context, ArrayList<ChildDetailScoreBean> detailedScoreListing) {
        this.context = context;
        this.detailedScoreListing = detailedScoreListing;
        this.layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return detailedScoreListing.size();
    }

    @Override
    public Object getItem(int position) {
        return detailedScoreListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_sub_element_item, null);

        TextView marks = (TextView) convertView.findViewById(R.id.marks);
        TextView subElementName = (TextView) convertView.findViewById(R.id.subElementName);
        TextView percentage = (TextView) convertView.findViewById(R.id.percentage);
//        SeekBar seekBar = (SeekBar) convertView.findViewById(R.id.seekBar);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        TextView comment = (TextView) convertView.findViewById(R.id.comment);

        marks.setTypeface(helvetica);
        subElementName.setTypeface(helvetica);
        percentage.setTypeface(helvetica);
        comment.setTypeface(helvetica);

        ChildDetailScoreBean childDetailScoreBean = detailedScoreListing.get(position);

        marks.setText(childDetailScoreBean.getScore());
        marks.setBackgroundColor(Color.parseColor(childDetailScoreBean.getColorCode()));
        subElementName.setText(childDetailScoreBean.getSubElementName());
//        seekBar.setEnabled(false);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int sixtyPercent = (screenWidth * 60) / 100;
        float textWidthForTitle = subElementName.getPaint().measureText(subElementName.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / sixtyPercent) + 1;
        subElementName.setLines(numberOfLines);

        if (Build.VERSION.SDK_INT >= 21) {
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(childDetailScoreBean.getColorCode())));
        } else {
            progressBar.getProgressDrawable().setColorFilter(Color.parseColor(childDetailScoreBean.getColorCode()), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        /*progressBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });*/

        try {
            progressBar.setProgress(Integer.parseInt(childDetailScoreBean.getScore()));
        } catch(NumberFormatException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        percentage.setText(childDetailScoreBean.getScorePercentage()+"%");

        if(childDetailScoreBean.getComments() == null || childDetailScoreBean.getComments().isEmpty()) {
            comment.setVisibility(View.GONE);
        } else {

            String strComment = "";
            String[] commentArray = childDetailScoreBean.getComments().split("\\|");

            for(String s : commentArray) {
                //System.out.println(s);
                strComment += s+"\n";
            }
            comment.setText(strComment);


            textWidthForTitle = comment.getPaint().measureText(comment.getText().toString());
            numberOfLines = ((int) textWidthForTitle / sixtyPercent) + 1;
            comment.setLines(numberOfLines);
        }

        return convertView;
    }
}