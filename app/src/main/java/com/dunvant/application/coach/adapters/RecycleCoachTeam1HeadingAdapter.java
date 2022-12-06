package com.dunvant.application.coach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CoachPlayerStatsbean;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class RecycleCoachTeam1HeadingAdapter extends RecyclerView.Adapter<RecycleCoachTeam1HeadingAdapter.MyViewHolder> {

    private ArrayList<CoachPlayerStatsbean> coachTeamResultBeanArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView heading;

        public MyViewHolder(View view) {
            super(view);
            heading = (TextView) view.findViewById(R.id.heading);

        }
    }


    public RecycleCoachTeam1HeadingAdapter(ArrayList<CoachPlayerStatsbean> coachTeamResultBeanArrayList) {
        this.coachTeamResultBeanArrayList = coachTeamResultBeanArrayList;
    }

    @Override
    public RecycleCoachTeam1HeadingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_adapter_coach_league_result_heading_items, parent, false);

        return new RecycleCoachTeam1HeadingAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecycleCoachTeam1HeadingAdapter.MyViewHolder holder, int position) {
        CoachPlayerStatsbean coachTeamResultBean = coachTeamResultBeanArrayList.get(position);
        holder.heading.setText(coachTeamResultBean.getLabel());

    }

    @Override
    public int getItemCount() {
        return coachTeamResultBeanArrayList.size();
    }
}
