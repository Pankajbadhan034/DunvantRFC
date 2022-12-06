package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.DatesResultBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.CoachMoveChildScreen;
import com.dunvant.application.coach.CoachMovedDetailScreen;
import com.dunvant.application.coach.CoachViewSessionDatesScreen;
import com.dunvant.application.coach.fragments.CoachParentDetailDialogFragment;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class CoachDateResultListingAdapter extends BaseAdapter{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Context context;
    ArrayList<DatesResultBean> dateResultListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public CoachDateResultListingAdapter(Context context, ArrayList<DatesResultBean> dateResultListing) {
        this.context = context;
        this.dateResultListing = dateResultListing;
        this.layoutInflater = LayoutInflater.from(context);
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
    }

    @Override
    public int getCount() {
        return dateResultListing.size();
    }

    @Override
    public Object getItem(int position) {
        return dateResultListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_date_result_item, null);

        LinearLayout parentLinearLayout = convertView.findViewById(R.id.parentLinearLayout);
        TextView childName = convertView.findViewById(R.id.childName);
        ImageView movedData = convertView.findViewById(R.id.movedData);
        final ImageView viewDetails = convertView.findViewById(R.id.viewDetails);
        ImageView move = convertView.findViewById(R.id.move);

        final DatesResultBean datesResultBean = dateResultListing.get(position);
        childName.setText(datesResultBean.getChildName());

        if(position % 2 == 0) {
            parentLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            childName.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            parentLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            childName.setTextColor(context.getResources().getColor(R.color.black));
        }

        if(datesResultBean.getIsTrial().equalsIgnoreCase("1")){
            childName.setTextColor(context.getResources().getColor(R.color.red));
        }

        if(datesResultBean.getMovedData().equalsIgnoreCase("1")){
            movedData.setVisibility(View.VISIBLE);
        } else {
            movedData.setVisibility(View.INVISIBLE);
        }

        if(loggedInUser.getCanMoveChild().equalsIgnoreCase("0")){
            move.setVisibility(View.INVISIBLE);
        } else {
            move.setVisibility(View.VISIBLE);
        }

        childName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoachParentDetailDialogFragment coachParentDetailDialogFragment = new CoachParentDetailDialogFragment();
                Bundle bundle = new Bundle();
//                bundle.putSerializable("clickedOnDateResult", datesResultBean);
                bundle.putString("childId", datesResultBean.getUsersId());
                bundle.putString("childName", datesResultBean.getChildName());
                coachParentDetailDialogFragment.setArguments(bundle);
                coachParentDetailDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "Dialog Fragment");
            }
        });

        movedData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent movedDetailScreen = new Intent(context, CoachMovedDetailScreen.class);
                movedDetailScreen.putExtra("clickedOnDateResult", datesResultBean);
                context.startActivity(movedDetailScreen);
            }
        });

        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewSessionDates = new Intent(context, CoachViewSessionDatesScreen.class);
                viewSessionDates.putExtra("clickedOnDateResult", datesResultBean);
                context.startActivity(viewSessionDates);
            }
        });

        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveChild = new Intent(context, CoachMoveChildScreen.class);
                moveChild.putExtra("clickedOnDateResult", datesResultBean);
                context.startActivity(moveChild);
            }
        });

        childName.setTypeface(helvetica);

        return convertView;
    }
}