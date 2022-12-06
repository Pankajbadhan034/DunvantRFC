package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.PitchItemBean;

import java.util.ArrayList;

public class ParentPitchItemAdapter extends BaseAdapter{

    Context context;
    ArrayList<PitchItemBean> pitchItemsListing;
    String textColor;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public ParentPitchItemAdapter(Context context, ArrayList<PitchItemBean> pitchItemsListing, String textColor) {
        this.context = context;
        this.pitchItemsListing = pitchItemsListing;
        this.textColor = textColor;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return pitchItemsListing.size();
    }

    @Override
    public Object getItem(int position) {
        return pitchItemsListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_pitch_inner_item, null);

//        TextView index = (TextView) convertView.findViewById(R.id.index);
        TextView pitchName = (TextView) convertView.findViewById(R.id.pitchName);
//        TextView pitchAddress = (TextView) convertView.findViewById(R.id.pitchAddress);

//        index.setTypeface(helvetica);
        pitchName.setTypeface(helvetica);
//        pitchAddress.setTypeface(helvetica);

//        index.setText((position+1)+"");
//        index.setText(" - ");
        pitchName.setText(" - "+pitchItemsListing.get(position).getPitchName()+ " | "+pitchItemsListing.get(position).getPitchAddress());
//        pitchAddress.setText(pitchItemsListing.get(position).getPitchAddress());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int eightyPercent = (screenWidth * 80) / 100;
        float textWidthForTitle = pitchName.getPaint().measureText(pitchName.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / eightyPercent) + 1;
        pitchName.setLines(numberOfLines);

        if(textColor.equalsIgnoreCase("white")){
//            index.setTextColor(context.getResources().getColor(R.color.white));
            pitchName.setTextColor(context.getResources().getColor(R.color.white));
//            pitchAddress.setTextColor(context.getResources().getColor(R.color.white));
        } else {
//            index.setTextColor(context.getResources().getColor(R.color.black));
            pitchName.setTextColor(context.getResources().getColor(R.color.black));
//            pitchAddress.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }
}