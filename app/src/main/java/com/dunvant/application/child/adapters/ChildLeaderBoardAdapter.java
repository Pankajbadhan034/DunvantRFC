package com.dunvant.application.child.adapters;

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
import com.dunvant.application.beans.ChildLeaderBoardBean;
import com.dunvant.application.child.ChildPostViewScreen;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
//naina
// test@12
public class ChildLeaderBoardAdapter extends BaseAdapter {
    Context context;
    ArrayList<ChildLeaderBoardBean>childLeaderBoardBeanArrayList;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoaderWithoutCircle = ImageLoader.getInstance();
    DisplayImageOptions optionsWithoutCircle;

    public ChildLeaderBoardAdapter(Context context, ArrayList<ChildLeaderBoardBean> childLeaderBoardBeanArrayList){
        this.context = context;
        this.childLeaderBoardBeanArrayList = childLeaderBoardBeanArrayList;
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

    }

    @Override
    public int getCount() {
        return childLeaderBoardBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childLeaderBoardBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_leaderboard_item, null);

        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timeTaken = (TextView) convertView.findViewById(R.id.timeTaken);
        TextView score = (TextView) convertView.findViewById(R.id.score);
        RelativeLayout imageLayout = (RelativeLayout) convertView.findViewById(R.id.imageLayout);
        ImageView backgroudImage = (ImageView) convertView.findViewById(R.id.backgroudImage);
        ImageView playVideoPic = (ImageView) convertView.findViewById(R.id.playVideoPic);


        name.setTypeface(helvetica);
        timeTaken.setTypeface(helvetica);
        score.setTypeface(helvetica);

        final ChildLeaderBoardBean childLeaderBoardBean = childLeaderBoardBeanArrayList.get(position);

        name.setText(childLeaderBoardBean.getChild_name());

        if(childLeaderBoardBean.getTime_taken().equalsIgnoreCase("")){
            timeTaken.setVisibility(View.GONE);
        }else{
            timeTaken.setText("TIME TAKEN: "+childLeaderBoardBean.getTime_taken());
        }
        if(childLeaderBoardBean.getScores().equalsIgnoreCase("0.00") || childLeaderBoardBean.getScores().equalsIgnoreCase("0")){
            score.setVisibility(View.GONE);
        }else{
            score.setText("SCORE: "+childLeaderBoardBean.getScores());
        }


        imageLoader.displayImage(childLeaderBoardBean.getChild_dp_url(), image, options);

        if(childLeaderBoardBean.getChallengeMedia().equalsIgnoreCase("") || childLeaderBoardBean.getChallengeMedia().equalsIgnoreCase("null")){
            imageLayout.setVisibility(View.GONE);
        }else{
            imageLayout.setVisibility(View.VISIBLE);
            if(childLeaderBoardBean.getChallengeMedia().contains(".jpg") || childLeaderBoardBean.getChallengeMedia().contains(".jpeg")
                    || childLeaderBoardBean.getChallengeMedia().contains(".png") || childLeaderBoardBean.getChallengeMedia().contains(".PNG")
                    || childLeaderBoardBean.getChallengeMedia().contains(".gif")){
                backgroudImage.setVisibility(View.VISIBLE);
                playVideoPic.setVisibility(View.GONE);

                imageLoaderWithoutCircle.displayImage(childLeaderBoardBean.getChallengeMedia(), backgroudImage, optionsWithoutCircle);
            }else{
                imageLoaderWithoutCircle.displayImage(childLeaderBoardBean.getVideoThumbnail(), backgroudImage, optionsWithoutCircle);
                backgroudImage.setVisibility(View.VISIBLE);
                playVideoPic.setVisibility(View.VISIBLE);

                
            }

        }



        backgroudImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(childLeaderBoardBean.getChallengeMedia().equalsIgnoreCase("") || childLeaderBoardBean.getChallengeMedia().equalsIgnoreCase("null")){
                  //  imageLayout.setVisibility(View.GONE);
                }else{
                   // imageLayout.setVisibility(View.VISIBLE);
                    if(childLeaderBoardBean.getChallengeMedia().contains(".jpg") || childLeaderBoardBean.getChallengeMedia().contains(".jpeg")
                            || childLeaderBoardBean.getChallengeMedia().contains(".png") || childLeaderBoardBean.getChallengeMedia().contains(".PNG")
                            || childLeaderBoardBean.getChallengeMedia().contains(".gif")){
                        Intent obj = new Intent(context, ChildPostViewScreen.class);
                        obj.putExtra("type", "image");
                        obj.putExtra("url", childLeaderBoardBean.getChallengeMedia());
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
                obj.putExtra("url", childLeaderBoardBean.getChallengeMedia());
                context.startActivity(obj);
            }
        });


        return convertView;
    }
}
