package com.dunvant.application.parent.adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ParentDashboardBean;
import com.dunvant.application.parent.fragments.ParentDashboardFragmentNew;

import java.util.ArrayList;

public class ParentDashboardAdapter extends BaseAdapter {

    Context context;
    ArrayList<ParentDashboardBean> parentDashboardBeanArrayList;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public ParentDashboardAdapter(Context context, ArrayList<ParentDashboardBean> parentDashboardBeanArrayList){
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
        convertView = layoutInflater.inflate(R.layout.parent_dashboard_adapter, null);
        TextView name = (TextView) convertView.findViewById(R.id.text);
        ImageView image = convertView.findViewById(R.id.image);
        RelativeLayout relative = convertView.findViewById(R.id.relative);
        Button button = convertView.findViewById(R.id.button);
        TextView badge = convertView.findViewById(R.id.badge);

       final ParentDashboardBean parentDashboardBean = parentDashboardBeanArrayList.get(position);

//        if(parentDashboardBean.getLabel().equalsIgnoreCase("REPORT ABUSE") || parentDashboardBean.getLabel().equalsIgnoreCase("LOGOUT")){
//            button.setVisibility(View.VISIBLE);
//            relative.setVisibility(View.GONE);
//            button.setText(parentDashboardBean.getPreferred_text());
//        }else{
            button.setVisibility(View.GONE);
            relative.setVisibility(View.VISIBLE);
            name.setText(parentDashboardBean.getPreferred_text());
            name.setSelected(true);
//        }



        String label = parentDashboardBean.getLabel();
        if(label.equalsIgnoreCase("My Participants")){
            image.setBackgroundResource(R.drawable.team);
            try{
                if(ParentDashboardFragmentNew.requestCount.equalsIgnoreCase("0")){
                    badge.setVisibility(View.INVISIBLE);
                }else{
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(ParentDashboardFragmentNew.requestCount);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(label.equalsIgnoreCase("Book Now")){
            image.setBackgroundResource(R.drawable.booknow);
        }else if(label.equalsIgnoreCase("Participant Newsfeed")){
            image.setBackgroundResource(R.drawable.timeline);
        }else if(label.equalsIgnoreCase("Participant Career")){
            image.setBackgroundResource(R.drawable.career);
        }else if(label.equalsIgnoreCase("Posts")){
            image.setBackgroundResource(R.drawable.writeapost);
        }else if(label.equalsIgnoreCase("Booking History")){
            image.setBackgroundResource(R.drawable.bookinghistory);
        }else if(label.equalsIgnoreCase("Join Leauge")){
            image.setBackgroundResource(R.drawable.league);
        }else if(label.equalsIgnoreCase("Parent Profile")){
            image.setBackgroundResource(R.drawable.profile);
        }else if(label.equalsIgnoreCase("Contact Us")){
            image.setBackgroundResource(R.drawable.contact_new);
        }else if(label.equalsIgnoreCase("Documents")){
            image.setBackgroundResource(R.drawable.uploaddocs_new);
            try{
                if(ParentDashboardFragmentNew.challengeCount.equalsIgnoreCase("0")){
                    badge.setVisibility(View.INVISIBLE);
                }else{
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(ParentDashboardFragmentNew.challengeCount);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }else if(label.equalsIgnoreCase("Rugby Education")){
            image.setBackgroundResource(R.drawable.rugby);
        }else if(label.equalsIgnoreCase("Book Midweek Package")){
            image.setBackgroundResource(R.drawable.mid_week);
        }else if(label.equalsIgnoreCase("Online Store")){
            image.setBackgroundResource(R.drawable.cart_d);
        }else if(label.equalsIgnoreCase("Order History")){
            image.setBackgroundResource(R.drawable.cart_d);
        }else if(label.equalsIgnoreCase("Manage League")){
            image.setBackgroundResource(R.drawable.tournament);
        }


//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(parentDashboardBean.getLabel().equalsIgnoreCase("REPORT ABUSE")){
//                    ((ParentMainScreen) context).showContactCoach("REPORT AN ABUSE");
//                }else{
//                    ((ParentMainScreen) context).showLogoutDialog();
//                }
//
//            }
//        });


        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int sixtyPercent = (screenWidth * 60) / 100;
        float textWidthForTitle = name.getPaint().measureText(name.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / sixtyPercent) + 1;
        name.setLines(numberOfLines);
//

        return convertView;
    }
}