package com.dunvant.application.coach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CoachLeagueDetailCareerBeen;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CoachPlayerDetailCareerAdapter extends RecyclerView.Adapter<CoachPlayerDetailCareerAdapter.MyViewHolder> {

private ArrayList<CoachLeagueDetailCareerBeen> coachTeamResultBeanArrayList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView heading;

    public MyViewHolder(View view) {
        super(view);
        heading = (TextView) view.findViewById(R.id.heading);

    }
}


    public CoachPlayerDetailCareerAdapter(ArrayList<CoachLeagueDetailCareerBeen> coachTeamResultBeanArrayList) {
        this.coachTeamResultBeanArrayList = coachTeamResultBeanArrayList;
    }

    @Override
    public CoachPlayerDetailCareerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_adapter_coach_league_result_heading_items, parent, false);

        return new CoachPlayerDetailCareerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CoachPlayerDetailCareerAdapter.MyViewHolder holder, int position) {
        CoachLeagueDetailCareerBeen coachTeamResultBean = coachTeamResultBeanArrayList.get(position);
        holder.heading.setText(coachTeamResultBean.getValue());

    }

    @Override
    public int getItemCount() {
        return coachTeamResultBeanArrayList.size();
    }
}
