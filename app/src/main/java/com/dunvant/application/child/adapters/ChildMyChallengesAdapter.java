package com.dunvant.application.child.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildMyChallengesBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildManageChallengesDetailScreen;
import com.dunvant.application.utils.Utilities;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by DEVLABS\pbadhan on 9/1/17.
 */
public class ChildMyChallengesAdapter  extends BaseAdapter {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    private final String ACCEPT_CHALLENGE = "ACCEPT_CHALLENGE";

    Context context;
    ArrayList<ChildMyChallengesBean> childMyChallengesBeanArrayList;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Typeface helvetica;
    Typeface linoType;

    String typeOfChallenge;

    public ChildMyChallengesAdapter(Context context, ArrayList<ChildMyChallengesBean> childMyChallengesBeanArrayList, String typeOfChallenge){
        this.context = context;
        this.childMyChallengesBeanArrayList = childMyChallengesBeanArrayList;
        this.typeOfChallenge = typeOfChallenge;
        layoutInflater = LayoutInflater.from(context);

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .displayer(new RoundedBitmapDisplayer(1000))
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

    }

    @Override
    public int getCount() {
        return childMyChallengesBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childMyChallengesBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_my_challenges_item, null);

        ImageView chalangeImage = (ImageView) convertView.findViewById(R.id.chalangeImage);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView byCoach = (TextView) convertView.findViewById(R.id.byCoach);
        TextView expireDate = (TextView) convertView.findViewById(R.id.ExpireDate);
        TextView targetScore = (TextView) convertView.findViewById(R.id.targetScore);
        TextView targetTime = (TextView) convertView.findViewById(R.id.targetTime);
        LinearLayout chalangeClick = (LinearLayout) convertView.findViewById(R.id.chalangeClick);
        TextView category = (TextView) convertView.findViewById(R.id.category);
        TextView expirationDate = (TextView) convertView.findViewById(R.id.expirationDate);
        TextView targetScoretext = (TextView) convertView.findViewById(R.id.targetScoretext);
        TextView targetTimeText = (TextView) convertView.findViewById(R.id.targetTimeText);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        title.setTypeface(helvetica);
        byCoach.setTypeface(helvetica);
        expireDate.setTypeface(helvetica);
        targetScore.setTypeface(helvetica);
        targetTime.setTypeface(helvetica);
        category.setTypeface(helvetica);
        expirationDate.setTypeface(helvetica);
        targetScoretext.setTypeface(helvetica);
        targetTimeText.setTypeface(helvetica);
        time.setTypeface(helvetica);

        final ChildMyChallengesBean childMyChallengesBean = childMyChallengesBeanArrayList.get(position);

        imageLoader.displayImage(childMyChallengesBean.getCoachDp(), chalangeImage, options);
        title.setText(childMyChallengesBean.getTitle());
        byCoach.setText(childMyChallengesBean.getCoachName());
        expireDate.setText(childMyChallengesBean.getExpirationFormatted());
        targetScore.setText(childMyChallengesBean.getTargetScore());
//        System.out.println("TIME::" + childMyChallengesBean.getTargetTimeTypeFormatted());
        targetTime.setText(childMyChallengesBean.getTargetTimeTypeFormatted());
        time.setText(childMyChallengesBean.getTargetTimeTypeFormatted());
        category.setText(childMyChallengesBean.getCategoryName());


        if(childMyChallengesBean.getIsChallengeExpired().equalsIgnoreCase("true")){
            if(childMyChallengesBean.getTypeChalange().equalsIgnoreCase("Achieved")){
                time.setText("Achieved");
            }else{
                time.setText("Expired");
            }
        }else if(childMyChallengesBean.getIsChallengeExpired().equalsIgnoreCase("false")){
            if(childMyChallengesBean.getAchievedResult().equalsIgnoreCase("1")){
                time.setText("Expiring time"+childMyChallengesBean.getExpirationFormatted() );

            }else{
                time.setText("Achieved");
            }
        }

        if(childMyChallengesBean.getTargetScore().equalsIgnoreCase("0.00") || childMyChallengesBean.getTargetScore().equalsIgnoreCase("0") || childMyChallengesBean.getTargetScore().equalsIgnoreCase("")){
            targetScoretext.setVisibility(View.GONE);
            targetScore.setVisibility(View.GONE);
        }else{
            targetScoretext.setVisibility(View.VISIBLE);
            targetScore.setVisibility(View.VISIBLE);
        }

        if(childMyChallengesBean.getTargetTimeTypeFormatted().equalsIgnoreCase("0.00") || childMyChallengesBean.getTargetTimeTypeFormatted().equalsIgnoreCase("0") || childMyChallengesBean.getTargetTimeTypeFormatted().equalsIgnoreCase("")){
            targetTimeText.setVisibility(View.GONE);
            targetTime.setVisibility(View.GONE);
        }else{
            targetTimeText.setVisibility(View.VISIBLE);
            targetTime.setVisibility(View.VISIBLE);
        }

        chalangeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(context, ChildManageChallengesDetailScreen.class);
                obj.putExtra("coachDp", childMyChallengesBean.getCoachDp());
                obj.putExtra("title", childMyChallengesBean.getTitle());
                obj.putExtra("byCoach", childMyChallengesBean.getCoachName());
                obj.putExtra("expireDate", childMyChallengesBean.getExpirationFormatted());
                obj.putExtra("targetScore", childMyChallengesBean.getTargetScore());
                obj.putExtra("targetTime", childMyChallengesBean.getTargetTimeTypeFormatted());
                obj.putExtra("imageUrl", childMyChallengesBean.getChalangesImageUrl());
                obj.putExtra("description", childMyChallengesBean.getDescription());
                obj.putExtra("challengesId", childMyChallengesBean.getChallengesId());
                obj.putExtra("isShared", childMyChallengesBean.getIsShared());
                obj.putExtra("timer", childMyChallengesBean.getTargetTimeTypeFormatted());
                obj.putExtra("typeOfChallenge", typeOfChallenge);
                obj.putExtra("CategoryName", childMyChallengesBean.getCategoryName());
                obj.putExtra("challengeResult", childMyChallengesBean.getAchievedResult());
                obj.putExtra("isChallengeExpired", childMyChallengesBean.getIsChallengeExpired());
                obj.putExtra("achievedScore", childMyChallengesBean.getAchievedScore());
                obj.putExtra("achievedTime", childMyChallengesBean.getAchievedTime());
                obj.putExtra("targetTimeNew", childMyChallengesBean.getTargetTime());
                if(typeOfChallenge.equalsIgnoreCase("Expired")){

                }
                context.startActivity(obj);


            }
        });

        return convertView;
    }


}
