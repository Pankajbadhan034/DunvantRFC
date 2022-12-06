package com.dunvant.application.child.adapters;

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
import com.dunvant.application.child.fragments.ChildDashboardFragmentNew;
import com.dunvant.application.parent.ParentMainScreen;

import java.util.ArrayList;

public class ChildDashboardAdapter extends BaseAdapter {

    Context context;
    ArrayList<ParentDashboardBean> parentDashboardBeanArrayList;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public ChildDashboardAdapter(Context context, ArrayList<ParentDashboardBean> parentDashboardBeanArrayList){
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
        if(label.equalsIgnoreCase("Newsfeed")){
            image.setBackgroundResource(R.drawable.timeline);
        }else if(label.equalsIgnoreCase("Performance")){
            image.setBackgroundResource(R.drawable.workout);
        }else if(label.equalsIgnoreCase("Chats")){
            image.setBackgroundResource(R.drawable.messages);
            if(ChildDashboardFragmentNew.messageCount.equalsIgnoreCase("0")){
                badge.setVisibility(View.INVISIBLE);
            }else{
                badge.setVisibility(View.VISIBLE);
                badge.setText(ChildDashboardFragmentNew.messageCount);
            }
        }else if(label.equalsIgnoreCase("Friends")){
            image.setBackgroundResource(R.drawable.friends);
            if(ChildDashboardFragmentNew.requestCount.equalsIgnoreCase("0")){
                badge.setVisibility(View.INVISIBLE);
            }else{
                badge.setVisibility(View.VISIBLE);
                badge.setText(ChildDashboardFragmentNew.requestCount);
            }
        }else if(label.equalsIgnoreCase("Create Film")){
            image.setBackgroundResource(R.drawable.video);
        }else if(label.equalsIgnoreCase("Gallery")){
            image.setBackgroundResource(R.drawable.gallery);
        }else if(label.equalsIgnoreCase("Posts")){
            image.setBackgroundResource(R.drawable.writeapost);
        }else if(label.equalsIgnoreCase("Challenges")){
            image.setBackgroundResource(R.drawable.challenges);
            if(ChildDashboardFragmentNew.challengesCount.equalsIgnoreCase("0")){
                badge.setVisibility(View.INVISIBLE);
            }else{
                badge.setVisibility(View.VISIBLE);
                badge.setText(ChildDashboardFragmentNew.challengesCount);
            }
        }else if(label.equalsIgnoreCase("Career")){
            image.setBackgroundResource(R.drawable.career);
        }else if(label.equalsIgnoreCase("Reports")){
            image.setBackgroundResource(R.drawable.scores);
        }else if(label.equalsIgnoreCase("About Me")){
            image.setBackgroundResource(R.drawable.updateprofile);
        }else if(label.equalsIgnoreCase("Profile")){
            image.setBackgroundResource(R.drawable.viewprofile);
        }else if(label.equalsIgnoreCase("Login As Parent")){
            image.setBackgroundResource(R.drawable.parent);
        }else if(label.equalsIgnoreCase("Manage League")){
            image.setBackgroundResource(R.drawable.tournament);
        }else if(label.equalsIgnoreCase("Report Abuse")){
            image.setBackgroundResource(R.drawable.abuse);
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(parentDashboardBean.getLabel().equalsIgnoreCase("REPORT ABUSE")){
                    ((ParentMainScreen) context).showContactCoach("REPORT AN ABUSE");
                }else{
                    ((ParentMainScreen) context).showLogoutDialog();
                }

            }
        });


//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        int screenWidth = display.getWidth();
//        int sixtyPercent = (screenWidth * 60) / 100;
//        float textWidthForTitle = name.getPaint().measureText(name.getText().toString());
//        int numberOfLines = ((int) textWidthForTitle / sixtyPercent) + 1;
//        name.setLines(numberOfLines);
//
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int eightyPercent = (screenWidth * 80) / 100;
        float textWidthForTitle = name.getPaint().measureText(name.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / eightyPercent) + 1;
        name.setLines(numberOfLines);

        return convertView;
    }
}