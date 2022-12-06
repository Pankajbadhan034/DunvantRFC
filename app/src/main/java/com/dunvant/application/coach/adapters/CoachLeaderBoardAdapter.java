package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.AcceptedChallengeChildBean;
import com.dunvant.application.child.ChildPostViewScreen;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

public class CoachLeaderBoardAdapter extends BaseAdapter{

    Context context;
    ArrayList<AcceptedChallengeChildBean> childrenListing;
    LayoutInflater layoutInflater;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    ImageLoader imageLoaderWithoutCircle = ImageLoader.getInstance();
    DisplayImageOptions optionsWithoutCircle;

    public CoachLeaderBoardAdapter(Context context, ArrayList<AcceptedChallengeChildBean> childrenListing) {
        this.context = context;
        this.childrenListing = childrenListing;
        this.layoutInflater = LayoutInflater.from(context);

        imageLoaderWithoutCircle.init(ImageLoaderConfiguration.createDefault(context));
        optionsWithoutCircle = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(1000))
                .build();
    }

    @Override
    public int getCount() {
        return childrenListing.size();
    }

    @Override
    public Object getItem(int position) {
        return childrenListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_leaderboard_item, null);

        ImageView profilePhoto = (ImageView) convertView.findViewById(R.id.profilePhoto);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView score = (TextView) convertView.findViewById(R.id.score);
        RelativeLayout imageLayout = (RelativeLayout) convertView.findViewById(R.id.imageLayout);
        ImageView backgroudImage = (ImageView) convertView.findViewById(R.id.backgroudImage);
        ImageView playVideoPic = (ImageView) convertView.findViewById(R.id.playVideoPic);
       

        final AcceptedChallengeChildBean childBean = childrenListing.get(position);

        imageLoader.displayImage(childBean.getChildDpUrl(), profilePhoto, options);

        name.setText(childBean.getChildName());
        time.setText("Time: "+childBean.getTimeTaken());
        score.setText("SCORE: "+childBean.getScores());

        if(childBean.getChallengeMedia().equalsIgnoreCase("") || childBean.getChallengeMedia().equalsIgnoreCase("null")){
            imageLayout.setVisibility(View.GONE);
        }else{
            imageLayout.setVisibility(View.VISIBLE);
            if(childBean.getChallengeMedia().contains(".jpg") || childBean.getChallengeMedia().contains(".jpeg")
                    || childBean.getChallengeMedia().contains(".png") || childBean.getChallengeMedia().contains(".PNG")
                    || childBean.getChallengeMedia().contains(".gif")){
                backgroudImage.setVisibility(View.VISIBLE);
                playVideoPic.setVisibility(View.GONE);
                imageLoaderWithoutCircle.displayImage(childBean.getChallengeMedia(), backgroudImage, optionsWithoutCircle);
            }else{
                imageLoaderWithoutCircle.displayImage(childBean.getVideoThumbnail(), backgroudImage, optionsWithoutCircle);
                backgroudImage.setVisibility(View.VISIBLE);
                playVideoPic.setVisibility(View.VISIBLE);
                
            }

        }



        backgroudImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(childBean.getChallengeMedia().equalsIgnoreCase("") || childBean.getChallengeMedia().equalsIgnoreCase("null")){
                 //   imageLayout.setVisibility(View.GONE);
                }else{
                  //  imageLayout.setVisibility(View.VISIBLE);
                    if(childBean.getChallengeMedia().contains(".jpg") || childBean.getChallengeMedia().contains(".jpeg")
                            || childBean.getChallengeMedia().contains(".png") || childBean.getChallengeMedia().contains(".PNG")
                            || childBean.getChallengeMedia().contains(".gif")){
                        Intent obj = new Intent(context, ChildPostViewScreen.class);
                        obj.putExtra("type", "image");
                        obj.putExtra("url", childBean.getChallengeMedia());
                        context.startActivity(obj);
                    }else{

                    }

                }
                
            }
        });

        playVideoPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(context, ChildPostViewScreen.class);
                obj.putExtra("type", "video");
                obj.putExtra("url", childBean.getChallengeMedia());
                context.startActivity(obj);
            }
        });

        return convertView;
    }
}