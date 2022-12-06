package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.PitchSlotsRowBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentCustomListAdapter extends BaseAdapter{
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Context context;
    ArrayList<PitchSlotsRowBean> coloumnArrayList;
    int arraySize;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;



    public ParentCustomListAdapter(Context context, ArrayList<PitchSlotsRowBean> coloumnArrayList, int arraySize){
        this.context = context;
        this.coloumnArrayList = coloumnArrayList;
        this.arraySize = arraySize;
        layoutInflater = LayoutInflater.from(context);


        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

    }

    @Override
    public int getCount() {
        return coloumnArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return coloumnArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        if(position==0){
//            convertView = layoutInflater.inflate(R.layout.list_adapters_title_item, null);
//            final TextView categoryname = (TextView) convertView.findViewById(R.id.categoryname);
//            categoryname.setTypeface(helvetica);
//
//            PitchSlotsRowBean pitchSlotsRowBean = coloumnArrayList.get(position);
//            categoryname.setText(pitchSlotsRowBean.getSlotName());
//
//        }else{

//        if(arraySize==1){
//            convertView = layoutInflater.inflate(R.layout.list_adapters_single_item, null);
//        }else{
        convertView = layoutInflater.inflate(R.layout.list_adapters_item, null);
//        }


        final TextView categoryname = (TextView) convertView.findViewById(R.id.categoryname);
        final LinearLayout linearBack = convertView.findViewById(R.id.linearBack);
        categoryname.setTypeface(helvetica);

        PitchSlotsRowBean pitchSlotsRowBean = coloumnArrayList.get(position);
        categoryname.setText(pitchSlotsRowBean.getSlotName());



        if(pitchSlotsRowBean.getSlotName().equalsIgnoreCase("booked")){
            categoryname.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
            categoryname.setTextColor(context.getResources().getColor(R.color.grey1));
            linearBack.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else{
            if(pitchSlotsRowBean.isClicked()){
                categoryname.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                categoryname.setTextColor(context.getResources().getColor(R.color.calendarClick));
                linearBack.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                categoryname.setText("Incart");
            }else{
                if(pitchSlotsRowBean.isDisabled()){
                    categoryname.setBackgroundColor(context.getResources().getColor(R.color.lightGrey2));
                    categoryname.setTextColor(context.getResources().getColor(R.color.grey1));
                    linearBack.setBackgroundColor(context.getResources().getColor(R.color.grey1));
                }else{
                    categoryname.setBackgroundColor(context.getResources().getColor(R.color.white));
                    categoryname.setTextColor(context.getResources().getColor(R.color.calendarClick));
                    linearBack.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
                }
            }
        }




        // }

        return convertView;
    }
}