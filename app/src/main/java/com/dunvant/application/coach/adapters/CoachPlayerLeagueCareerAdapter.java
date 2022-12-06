package com.dunvant.application.coach.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.StatsLeagueBean;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CoachPlayerLeagueCareerAdapter extends RecyclerView.Adapter<CoachPlayerLeagueCareerAdapter.MyViewHolder> {

    private ArrayList<StatsLeagueBean> coachTeamResultBeanArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView heading;

        public MyViewHolder(View view) {
            super(view);
            heading = (TextView) view.findViewById(R.id.heading);

        }
    }


    public CoachPlayerLeagueCareerAdapter(ArrayList<StatsLeagueBean> coachTeamResultBeanArrayList) {
        this.coachTeamResultBeanArrayList = coachTeamResultBeanArrayList;
    }

    @Override
    public CoachPlayerLeagueCareerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coach_player_league_career_adapter, parent, false);

        return new CoachPlayerLeagueCareerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CoachPlayerLeagueCareerAdapter.MyViewHolder holder, int position) {
        StatsLeagueBean coachTeamResultBean = coachTeamResultBeanArrayList.get(position);

        holder.heading.setText(coachTeamResultBean.getHeading()+"\n\n"+coachTeamResultBean.getValue());

    }

    @Override
    public int getItemCount() {
        return coachTeamResultBeanArrayList.size();
    }
}
