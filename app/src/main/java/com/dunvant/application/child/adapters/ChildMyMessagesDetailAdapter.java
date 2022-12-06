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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildMyMessageDetailBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildMessageDetailImageVideoViewScreen;
import com.dunvant.application.utils.Utilities;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

public class ChildMyMessagesDetailAdapter extends BaseAdapter {
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Context context;
    ArrayList<ChildMyMessageDetailBean> childMyMessageDetailBeanArrayList;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Typeface helvetica;
    Typeface linoType;

    public ChildMyMessagesDetailAdapter(Context context, ArrayList<ChildMyMessageDetailBean> childMyMessageDetailBeanArrayList){
        this.context = context;
        this.childMyMessageDetailBeanArrayList = childMyMessageDetailBeanArrayList;
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
        return childMyMessageDetailBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childMyMessageDetailBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_my_messages_detail , null);

        RelativeLayout relative1 = (RelativeLayout) convertView.findViewById(R.id.relative1);
        ImageView myMessagesImage = (ImageView) convertView.findViewById(R.id.myMessagesImage);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView mediaType = (TextView) convertView.findViewById(R.id.mediaType);

        title.setTypeface(helvetica);
        date.setTypeface(helvetica);

//        System.out.println("SIZE::" + childMyMessageDetailBeanArrayList.size());
        final ChildMyMessageDetailBean childMyMessageDetailBean = childMyMessageDetailBeanArrayList.get(position);

        imageLoader.displayImage(childMyMessageDetailBean.getSenderDpUrl(), myMessagesImage, options);
        title.setText(childMyMessageDetailBean.getSubject());
        description.setText(childMyMessageDetailBean.getMessage());
        date.setText(childMyMessageDetailBean.getMessageDateFormatted());

        if(childMyMessageDetailBean.getMediaType().equalsIgnoreCase("video")){
            mediaType.setVisibility(View.VISIBLE);
            mediaType.setText("Tap to View Video");
        }else if(childMyMessageDetailBean.getMediaType().equalsIgnoreCase("image")){
            mediaType.setVisibility(View.VISIBLE);
            mediaType.setText("Tap to View Image");
        }else{
            mediaType.setVisibility(View.GONE);
        }

        if(childMyMessageDetailBean.getUsersId().equalsIgnoreCase(loggedInUser.getId())){
// set blue
            relative1.setBackgroundResource(R.drawable.f_blue);
        }else{
// set yellow
            relative1.setBackgroundResource(R.drawable.f_yellow);
        }

        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(childMyMessageDetailBean.getMediaType().equalsIgnoreCase("video")){
                    Intent obj = new Intent(context, ChildMessageDetailImageVideoViewScreen.class);
                    obj.putExtra("type", "video");
                    obj.putExtra("url", childMyMessageDetailBean.getFileUrl());
                    context.startActivity(obj);
                }else if(childMyMessageDetailBean.getMediaType().equalsIgnoreCase("image")){
                    Intent obj = new Intent(context, ChildMessageDetailImageVideoViewScreen.class);
                    obj.putExtra("type", "image");
                    obj.putExtra("url", childMyMessageDetailBean.getFileUrl());
                    context.startActivity(obj);
                }else{

                }
            }
        });


        return convertView;
    }
}
