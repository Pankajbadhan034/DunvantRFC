package com.dunvant.application.coach.adapters;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CoachTeamResultBean;
import com.dunvant.application.coach.CoachLeagueMatchResultScreen;

import org.json.JSONObject;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class RecycleCoachLeagueMatchResultColumnAdapter extends RecyclerView.Adapter<RecycleCoachLeagueMatchResultColumnAdapter.MyViewHolder> {

    private ArrayList<CoachTeamResultBean> coachTeamResultBeanArrayList;
    private CoachLeagueMatchResultScreen coachLeagueMatchResultScreen;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView heading;

        public MyViewHolder(View view) {
            super(view);
            heading = (TextView) view.findViewById(R.id.heading);

        }
    }


    public RecycleCoachLeagueMatchResultColumnAdapter(ArrayList<CoachTeamResultBean> coachTeamResultBeanArrayList, CoachLeagueMatchResultScreen coachLeagueMatchResultScreen) {
        this.coachTeamResultBeanArrayList = coachTeamResultBeanArrayList;
        this.coachLeagueMatchResultScreen = coachLeagueMatchResultScreen;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_adapter_coach_league_result_column_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CoachTeamResultBean coachTeamResultBean = coachTeamResultBeanArrayList.get(position);
        holder.heading.setText(coachTeamResultBean.getValue());

        holder.heading.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    coachTeamResultBeanArrayList.get(position).setValue(holder.heading.getText().toString().trim());
                    notifyDataSetChanged();
                    coachLeagueMatchResultScreen.matchWinCalculate();


                    try{
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("attribute_value", holder.heading.getText().toString().trim());
                        jsonObject.put("team_id", coachTeamResultBean.getTeam_id());
                        jsonObject.put("attribute_id", coachTeamResultBean.getAttribute_id());

                        CoachLeagueMatchResultScreen.MatchResultSTR = CoachLeagueMatchResultScreen.MatchResultSTR + jsonObject.toString()+",";
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return coachTeamResultBeanArrayList.size();
    }
}

