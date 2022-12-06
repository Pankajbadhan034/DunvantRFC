package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChallengeBean;
import com.dunvant.application.utils.Utilities;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ParentActiveChallengesAdapter extends BaseAdapter{

    Typeface helvetica;
    Typeface linoType;

    Context context;
    ArrayList<ChallengeBean> challengesList;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public ParentActiveChallengesAdapter(Context context, ArrayList<ChallengeBean> challengesList) {
        this.context = context;
        this.challengesList = challengesList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
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
        convertView = layoutInflater.inflate(R.layout.parent_adapter_active_challege_item, null);

        LinearLayout targetScoreLinearLayout = (LinearLayout) convertView.findViewById(R.id.targetScoreLinearLayout);
        LinearLayout targetTimeLinearLayout = (LinearLayout) convertView.findViewById(R.id.targetTimeLinearLayout);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView timer = (TextView) convertView.findViewById(R.id.timer);
        TextView challengeBy = (TextView) convertView.findViewById(R.id.challengeBy);
        TextView categoryName = (TextView) convertView.findViewById(R.id.categoryName);
        TextView expirationDate = (TextView) convertView.findViewById(R.id.expirationDate);
        TextView targetScore = (TextView) convertView.findViewById(R.id.targetScore);
        TextView targetTime = (TextView) convertView.findViewById(R.id.targetTime);

        title.setTypeface(linoType);
        timer.setTypeface(linoType);
        challengeBy.setTypeface(helvetica);
        categoryName.setTypeface(helvetica);
        expirationDate.setTypeface(helvetica);
        targetScore.setTypeface(helvetica);
        targetTime.setTypeface(helvetica);

        ChallengeBean challengeBean = challengesList.get(position);

        imageLoader.displayImage(challengeBean.getChildImageUrl(), image, options);
        title.setText(challengeBean.getChallengeTitle());
        challengeBy.setText(challengeBean.getCoachName());
        categoryName.setText(challengeBean.getCategoryName());
        expirationDate.setText(challengeBean.getShowExpiration());
        targetScore.setText(challengeBean.getTargetScore());
        targetTime.setText(challengeBean.getTargetTimeTypeFormatted());

        float targetScoreFloat = 0;
        try{
            targetScoreFloat = Float.parseFloat(challengeBean.getTargetScore());
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        if(challengeBean.getTargetScore() == null || challengeBean.getTargetScore().isEmpty() || targetScoreFloat == 0) {
            targetScoreLinearLayout.setVisibility(View.GONE);
        }

        if(challengeBean.getTargetTimeTypeFormatted() == null || challengeBean.getTargetTimeTypeFormatted().isEmpty()){
            targetTimeLinearLayout.setVisibility(View.GONE);
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = dateFormat.parse(challengeBean.getExpiration());
            String strTimer = Utilities.getDifferenceBtwTime(date);

            timer.setText(strTimer);
        } catch (ParseException e) {
            e.printStackTrace();
            timer.setText("Not Available");
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }
}