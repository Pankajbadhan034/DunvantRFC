package com.dunvant.application.coach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CoachTeamResultBean;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class RecycleCoachLeagueMatchResultHeadingAdapter extends RecyclerView.Adapter<RecycleCoachLeagueMatchResultHeadingAdapter.MyViewHolder> {

    private ArrayList<CoachTeamResultBean> coachTeamResultBeanArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView heading;

        public MyViewHolder(View view) {
            super(view);
            heading = (TextView) view.findViewById(R.id.heading);

        }
    }


    public RecycleCoachLeagueMatchResultHeadingAdapter(ArrayList<CoachTeamResultBean> coachTeamResultBeanArrayList) {
        this.coachTeamResultBeanArrayList = coachTeamResultBeanArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_adapter_coach_league_result_heading_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CoachTeamResultBean coachTeamResultBean = coachTeamResultBeanArrayList.get(position);
        holder.heading.setText(coachTeamResultBean.getLabel());

    }

    @Override
    public int getItemCount() {
        return coachTeamResultBeanArrayList.size();
    }
}
