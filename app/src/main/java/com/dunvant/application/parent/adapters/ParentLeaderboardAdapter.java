package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.LeaderboardChildBean;
import com.dunvant.application.child.ChildPostViewScreen;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

public class ParentLeaderboardAdapter extends BaseAdapter{

    Context context;
    ArrayList<LeaderboardChildBean> childrenListing;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    ImageLoader imageLoaderWithoutCircle = ImageLoader.getInstance();
    DisplayImageOptions optionsWithoutCircle;

    public ParentLeaderboardAdapter(Context context, ArrayList<LeaderboardChildBean> childrenListing){
        this.context = context;
        this.childrenListing = childrenListing;
        this.layoutInflater = LayoutInflater.from(context);

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

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
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
        convertView = layoutInflater.inflate(R.layout.parent_adapter_leaderboard_item, null);

        ImageView childImage = (ImageView) convertView.findViewById(R.id.childImage);
        TextView childName = (TextView) convertView.findViewById(R.id.childName);
        TextView timeTaken = (TextView) convertView.findViewById(R.id.timeTaken);
        TextView txtScore = (TextView) convertView.findViewById(R.id.txtScore);
        TextView score = (TextView) convertView.findViewById(R.id.score);
        RelativeLayout imageLayout = (RelativeLayout) convertView.findViewById(R.id.imageLayout);
        ImageView backgroudImage = (ImageView) convertView.findViewById(R.id.backgroudImage);
        ImageView playVideoPic = (ImageView) convertView.findViewById(R.id.playVideoPic);

       final LeaderboardChildBean childBean = childrenListing.get(position);

        imageLoader.displayImage(childBean.getChildDpUrl(), childImage, options);
        childName.setText(childBean.getChildName());
        timeTaken.setText("TIME TAKEN: "+childBean.getTimeTaken());
        score.setText(childBean.getScores());

        childName.setTypeface(linoType);
        timeTaken.setTypeface(helvetica);
        txtScore.setTypeface(linoType);
        score.setTypeface(helvetica);

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