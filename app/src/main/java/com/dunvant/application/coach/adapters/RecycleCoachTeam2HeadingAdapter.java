package com.dunvant.application.coach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CoachPlayerStatsbean;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class RecycleCoachTeam2HeadingAdapter  extends RecyclerView.Adapter<RecycleCoachTeam2HeadingAdapter.MyViewHolder> {

    private ArrayList<CoachPlayerStatsbean> coachTeamResultBeanArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView heading;

        public MyViewHolder(View view) {
            super(view);
            heading = (TextView) view.findViewById(R.id.heading);

        }
    }


    public RecycleCoachTeam2HeadingAdapter(ArrayList<CoachPlayerStatsbean> coachTeamResultBeanArrayList) {
        this.coachTeamResultBeanArrayList = coachTeamResultBeanArrayList;
    }

    @Override
    public RecycleCoachTeam2HeadingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_adapter_coach_league_result_heading_items, parent, false);

        return new RecycleCoachTeam2HeadingAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecycleCoachTeam2HeadingAdapter.MyViewHolder holder, int position) {
        CoachPlayerStatsbean coachTeamResultBean = coachTeamResultBeanArrayList.get(position);
        holder.heading.setText(coachTeamResultBean.getLabel());

    }

    @Override
    public int getItemCount() {
        return coachTeamResultBeanArrayList.size();
    }
}
