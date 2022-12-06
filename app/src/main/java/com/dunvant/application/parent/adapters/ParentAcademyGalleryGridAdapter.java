package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CoachingProgramGalleryBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class ParentAcademyGalleryGridAdapter extends BaseAdapter{

    Context context;
    ArrayList<CoachingProgramGalleryBean> galleryList;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public ParentAcademyGalleryGridAdapter(Context context, ArrayList<CoachingProgramGalleryBean> galleryList){
        this.context = context;
        this.galleryList = galleryList;
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
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return galleryList.size();
    }

    @Override
    public Object getItem(int position) {
        return galleryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_academy_gallery_item, null);

        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        CoachingProgramGalleryBean galleryBean = galleryList.get(position);

        imageLoader.displayImage(galleryBean.getFilePath(), image, options);

        if(galleryBean.getFileType().contains("video") || galleryBean.getFileType().contains("youtube")){
            image.setVisibility(View.GONE);
        }

        return convertView;
    }
}