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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChildMyGalleryBean;
import com.dunvant.application.child.ChildViewProfileGalleryImageViewScreen;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class ChildViewYourProfileGalleryListAdapter extends BaseAdapter {
    Context context;
    ArrayList<ChildMyGalleryBean> childMyGalleryBeanArrayList;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Typeface helvetica;
    Typeface linoType;

    public ChildViewYourProfileGalleryListAdapter(Context context, ArrayList<ChildMyGalleryBean> childMyGalleryBeanArrayList){
        this.context = context;
        this.childMyGalleryBeanArrayList = childMyGalleryBeanArrayList;
        layoutInflater = LayoutInflater.from(context);

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

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

    }

    @Override
    public int getCount() {
        return childMyGalleryBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childMyGalleryBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_my_gallery_item, null);

        ImageView myImage = (ImageView) convertView.findViewById(R.id.myImage);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        LinearLayout linearfooter = (LinearLayout) convertView.findViewById(R.id.linearfooter);

        linearfooter.setVisibility(View.GONE);
        title.setVisibility(View.GONE);

        final ChildMyGalleryBean galleryBean = childMyGalleryBeanArrayList.get(position);

        imageLoader.displayImage(galleryBean.getFileUrl(), myImage, options);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(context, ChildViewProfileGalleryImageViewScreen.class);
                obj.putExtra("type", galleryBean.getFileUrl());
                context.startActivity(obj);
            }
        });


        return convertView;
    }
}
