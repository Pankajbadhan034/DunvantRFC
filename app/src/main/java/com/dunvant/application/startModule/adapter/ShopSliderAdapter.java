package com.dunvant.application.startModule.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.SliderImagesBean;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ShopSliderAdapter extends PagerAdapter {

    Context context;
    ArrayList<SliderImagesBean> stringArrayList;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;



    // Viewpager Constructor
    public ShopSliderAdapter(Context context, ArrayList<SliderImagesBean> stringArrayList) {
        this.context = context;
        this.stringArrayList = stringArrayList;
//        layoutInflater = LayoutInflater.from(context);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
        return stringArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View convertView = layoutInflater.inflate(R.layout.shop_slider_adapter_item, null);

        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        final SliderImagesBean sliderBean = stringArrayList.get(position);
        imageLoader.displayImage(sliderBean.getImage_url()+""+sliderBean.getImage(), image, options);

        // Adding the View
        Objects.requireNonNull(container).addView(convertView);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("IMAGE:: "+sliderBean.getImage_url()+""+sliderBean.getImage()+":: CLICKED:: "+sliderBean.getUrl());
                                try{
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(sliderBean.getUrl()));
                    context.startActivity(browserIntent);
                }catch (Exception e){
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }
}
