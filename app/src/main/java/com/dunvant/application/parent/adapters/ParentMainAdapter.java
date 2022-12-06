package com.dunvant.application.parent.adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ParentDashboardBean;

import java.util.ArrayList;

public class ParentMainAdapter extends BaseAdapter {

    Context context;
    ArrayList<ParentDashboardBean> parentDashboardBeanArrayList;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public ParentMainAdapter(Context context, ArrayList<ParentDashboardBean> parentDashboardBeanArrayList){
        this.context = context;
        this.parentDashboardBeanArrayList = parentDashboardBeanArrayList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return parentDashboardBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return parentDashboardBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_main_adapter, null);
        TextView name = (TextView) convertView.findViewById(R.id.text);

        ParentDashboardBean parentDashboardBean = parentDashboardBeanArrayList.get(position);
        name.setText(parentDashboardBean.getPreferred_text());


        String label = parentDashboardBean.getLabel();
        if(label.equalsIgnoreCase("Dashboard")){
            name.setCompoundDrawablesWithIntrinsicBounds( R.drawable.dashboardsmall,0, 0, 0);
        }else if(label.equalsIgnoreCase("My Participants")){
            name.setCompoundDrawablesWithIntrinsicBounds( R.drawable.teammenu,0, 0, 0);
        }else if(label.equalsIgnoreCase("Rugby Education")){
            name.setCompoundDrawablesWithIntrinsicBounds( R.drawable.rugby_menu,0, 0, 0);
        }else if(label.equalsIgnoreCase("Book Now")){
            name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.booknowmenu,0,  0, 0);
        }else if(label.equalsIgnoreCase("Participant Newsfeed")){
            name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.timelinemenu, 0, 0, 0);
        }else if(label.equalsIgnoreCase("Participant Career")){
            name.setCompoundDrawablesWithIntrinsicBounds( R.drawable.careermenu, 0, 0, 0);
        }else if(label.equalsIgnoreCase("Posts")){
            name.setCompoundDrawablesWithIntrinsicBounds( R.drawable.writeapostmenu, 0, 0, 0);
        }else if(label.equalsIgnoreCase("Booking History")){
            name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bookinghistorymenu,0,  0, 0);
        }else if(label.equalsIgnoreCase("Join Leauge")){
            name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.leaguemenu, 0, 0, 0);
        }else if(label.equalsIgnoreCase("Parent Profile")){
            name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.profilemenu, 0, 0, 0);
        }else if(label.equalsIgnoreCase("Contact Us")){
            name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.contactmenu,0,  0, 0);
        }else if(label.equalsIgnoreCase("Documents")){
            name.setCompoundDrawablesWithIntrinsicBounds( R.drawable.uploaddocsmenu, 0, 0, 0);
        }else if(label.equalsIgnoreCase("Online Store")){
            name.setCompoundDrawablesWithIntrinsicBounds( R.drawable.cartmenu, 0, 0, 0);
        }else if(label.equalsIgnoreCase("Order History")){
            name.setCompoundDrawablesWithIntrinsicBounds( R.drawable.cartmenu, 0, 0, 0);
        }else if(label.equalsIgnoreCase("Manage League")){
            name.setCompoundDrawablesWithIntrinsicBounds( R.drawable.tournamentmenu, 0, 0, 0);
        }else if(label.equalsIgnoreCase("Help")){
            name.setCompoundDrawablesWithIntrinsicBounds( R.drawable.help,0, 0, 0);
        }



//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        int screenWidth = display.getWidth();
//        int sixtyPercent = (screenWidth * 60) / 100;
//        float textWidthForTitle = datesBooked.getPaint().measureText(datesBooked.getText().toString());
//        int numberOfLines = ((int) textWidthForTitle / sixtyPercent) + 1;
//        datesBooked.setLines(numberOfLines);
//

        return convertView;
    }
}