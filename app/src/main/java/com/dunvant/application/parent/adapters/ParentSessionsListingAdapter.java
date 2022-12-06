package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CampBean;
import com.dunvant.application.beans.CampSessionBean;
import com.dunvant.application.parent.ParentBookCampScreenNew;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentSessionsListingAdapter extends BaseAdapter{

    Typeface helvetica;
    Typeface linoType;

    Context context;
    ArrayList<CampSessionBean> sessionsList;
    LayoutInflater layoutInflater;
    CampBean clickedOnCamp;
    int locationPosition;

    public ParentSessionsListingAdapter(Context context, ArrayList<CampSessionBean> sessionsList, CampBean clickedOnCamp, int locationPosition) {
        this.context = context;
        this.sessionsList = sessionsList;
        this.clickedOnCamp = clickedOnCamp;
        this.locationPosition = locationPosition;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return sessionsList.size();
    }

    @Override
    public Object getItem(int position) {
        return sessionsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_session_item, null);

        TextView lblSessions = (TextView) convertView.findViewById(R.id.lblSessions);
        TextView sessions = (TextView) convertView.findViewById(R.id.sessions);
        TextView lblPerDay = (TextView) convertView.findViewById(R.id.lblPerDay);
        TextView perDay = (TextView) convertView.findViewById(R.id.perDay);
        TextView lblPerWeek = (TextView) convertView.findViewById(R.id.lblPerWeek);
        TextView perWeek = (TextView) convertView.findViewById(R.id.perWeek);
        TextView lblAge = (TextView) convertView.findViewById(R.id.lblAge);
        TextView age = (TextView) convertView.findViewById(R.id.age);
        TextView bookCamp = convertView.findViewById(R.id.bookCamp);

        LinearLayout perDayLinearLayout = convertView.findViewById(R.id.perDayLinearLayout);
        LinearLayout perWeekLinearLayout = convertView.findViewById(R.id.perWeekLinearLayout);

        CampSessionBean sessionBean = sessionsList.get(position);

        sessions.setText(sessionBean.getShowFromTime()+" - "+sessionBean.getShowToTime());

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);

        perDay.setText(sessionBean.getPerDayCost()+" "+academy_currency);
        perWeek.setText(sessionBean.getPerWeekCost()+" "+academy_currency);
        age.setText(sessionBean.getGroupName());

        if(sessionBean.getPerDayCost() == null || sessionBean.getPerDayCost().isEmpty() || sessionBean.getPerDayCost().equalsIgnoreCase("0.00") || sessionBean.getPerDayCost().equalsIgnoreCase("null")){
            perDayLinearLayout.setVisibility(View.GONE);
        }

        if(sessionBean.getPerWeekCost() == null || sessionBean.getPerWeekCost().isEmpty() || sessionBean.getPerWeekCost().equalsIgnoreCase("0.00") || sessionBean.getPerWeekCost().equalsIgnoreCase("null")){
            perWeekLinearLayout.setVisibility(View.GONE);
        }

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int eightyPercent = (screenWidth * 80) / 100;
        float textWidthForComment = age.getPaint().measureText(sessionBean.getGroupName());
        int numberOfLines = ((int) textWidthForComment/eightyPercent) + 1;
        age.setLines(numberOfLines);

        bookCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookCampScreen = new Intent(context, ParentBookCampScreenNew.class);
                bookCampScreen.putExtra("clickedOnCamp", clickedOnCamp);
                bookCampScreen.putExtra("sessionPosition", position);
                bookCampScreen.putExtra("locationPosition", locationPosition);
                context.startActivity(bookCampScreen);
            }
        });

        if(!sessionBean.isAvailability()){
            bookCamp.setText("BOOKING CLOSED");
            bookCamp.setEnabled(false);
            bookCamp.setAlpha(0.5F);
        }

        lblSessions.setTypeface(linoType);
        sessions.setTypeface(helvetica);
        lblPerDay.setTypeface(linoType);
        perDay.setTypeface(helvetica);
        lblPerWeek.setTypeface(linoType);
        perWeek.setTypeface(helvetica);
        lblAge.setTypeface(linoType);
        age.setTypeface(helvetica);

        return convertView;
    }
}