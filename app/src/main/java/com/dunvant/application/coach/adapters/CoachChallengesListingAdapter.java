package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChallengeListBean;
import com.dunvant.application.coach.CoachAcceptedMembersScreen;
import com.dunvant.application.coach.CoachEditChallengeScreen;
import com.dunvant.application.coach.CoachLeaderboardScreen;
import com.dunvant.application.coach.CoachViewChallengeScreen;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class CoachChallengesListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<ChallengeListBean> challengesList;
    LayoutInflater layoutInflater;
    ListView challengesListView;

    public CoachChallengesListingAdapter(Context context, ArrayList<ChallengeListBean> challengesList, ListView challengesListView){
        this.context = context;
        this.challengesList = challengesList;
        this.challengesListView = challengesListView;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return challengesList.size();
    }

    @Override
    public Object getItem(int position) {
        return challengesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_challenges_item, null);

        LinearLayout topLinearLayout = convertView.findViewById(R.id.topLinearLayout);
        LinearLayout mainLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
        TextView challengeTitle = (TextView) convertView.findViewById(R.id.challengeTitle);
        TextView location = (TextView) convertView.findViewById(R.id.location);
        TextView lblSessions = (TextView) convertView.findViewById(R.id.lblSessions);
        TextView sessionName = (TextView) convertView.findViewById(R.id.sessionName);
        final LinearLayout imagesLinearLayout = (LinearLayout) convertView.findViewById(R.id.imagesLinearLayout);
        ImageView viewIcon = (ImageView) convertView.findViewById(R.id.viewIcon);
        ImageView presentIcon = (ImageView) convertView.findViewById(R.id.presentIcon);
        ImageView editIcon = (ImageView) convertView.findViewById(R.id.editIcon);
        ImageView leaderBoardIcon = (ImageView) convertView.findViewById(R.id.leaderboardIcon);

        final ChallengeListBean challengeListBean = challengesList.get(position);

        challengeTitle.setText(challengeListBean.getTitle());
        location.setText(challengeListBean.getLocationNames());
        sessionName.setText(challengeListBean.getDayLabel());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int eightyPercent = (screenWidth * 80) / 100;
        float textWidthForTitle = sessionName.getPaint().measureText(sessionName.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / eightyPercent) + 1;
        sessionName.setLines(numberOfLines);

        if(position % 2 == 0) {
            topLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            challengeTitle.setTextColor(context.getResources().getColor(R.color.white));
            location.setTextColor(context.getResources().getColor(R.color.white));
            lblSessions.setTextColor(context.getResources().getColor(R.color.white));
            sessionName.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            topLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            challengeTitle.setTextColor(context.getResources().getColor(R.color.black));
            location.setTextColor(context.getResources().getColor(R.color.black));
            lblSessions.setTextColor(context.getResources().getColor(R.color.black));
            sessionName.setTextColor(context.getResources().getColor(R.color.black));
        }

        viewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CoachViewChallengeScreen.class);
                intent.putExtra("clickedOnChallenge", challengeListBean);
                context.startActivity(intent);
            }
        });

        presentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CoachAcceptedMembersScreen.class);
                intent.putExtra("clickedOnChallenge", challengeListBean);
                context.startActivity(intent);
            }
        });

        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CoachEditChallengeScreen.class);
                intent.putExtra("clickedOnChallenge", challengeListBean);
                context.startActivity(intent);
            }
        });

        leaderBoardIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CoachLeaderboardScreen.class);
                intent.putExtra("clickedOnChallenge", challengeListBean);
                context.startActivity(intent);
            }
        });

        mainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesLinearLayout.getVisibility() == View.VISIBLE) {
                    imagesLinearLayout.setVisibility(View.INVISIBLE);
                    Utilities.setListViewHeightBasedOnChildren(challengesListView);
                } else {
                    imagesLinearLayout.setVisibility(View.VISIBLE);
                    Utilities.setListViewHeightBasedOnChildren(challengesListView);
                }
            }
        });

        return convertView;
    }
}