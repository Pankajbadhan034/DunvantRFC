package com.dunvant.application.coach.adapters;

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

import java.util.ArrayList;

public class CoachDashboardAdapter extends BaseAdapter {

    Context context;
    ArrayList<ParentDashboardBean> parentDashboardBeanArrayList;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public CoachDashboardAdapter(Context context, ArrayList<ParentDashboardBean> parentDashboardBeanArrayList){
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
        if(label.equalsIgnoreCase("Profile")){
            image.setBackgroundResource(R.drawable.profile);
        }else if(label.equalsIgnoreCase("Posts")){
            image.setBackgroundResource(R.drawable.writeapost);
        }else if(label.equalsIgnoreCase("Participant Reports")){
            image.setBackgroundResource(R.drawable.scores);
        }else if(label.equalsIgnoreCase("Attendance")){
            image.setBackgroundResource(R.drawable.attendance_new);
        }else if(label.equalsIgnoreCase("Team")){
            image.setBackgroundResource(R.drawable.team);
        }else if(label.equalsIgnoreCase("Set Challenges")){
            image.setBackgroundResource(R.drawable.challenges);
        }else if(label.equalsIgnoreCase("Documents")){
            image.setBackgroundResource(R.drawable.uploaddocs_new);
        }else if(label.equalsIgnoreCase("Rugby Education")){
            image.setBackgroundResource(R.drawable.rugby);
        }else if(label.equalsIgnoreCase("Midweek Attendance")){
            image.setBackgroundResource(R.drawable.mid_week);
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
        int sixtyPercent = (screenWidth * 80) / 100;
        float textWidthForTitle = name.getPaint().measureText(name.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / sixtyPercent) + 1;
        name.setLines(numberOfLines);
//

        return convertView;
    }
}