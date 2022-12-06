package com.dunvant.application.child.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChildMyGalleryComments;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class ChildMyGalleryDetailAdapter extends BaseAdapter {
    Context context;
    ArrayList<ChildMyGalleryComments> childMyGalleryCommentsArrayList;
    String type;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Typeface helvetica;
    Typeface linoType;

    public ChildMyGalleryDetailAdapter(Context context, ArrayList<ChildMyGalleryComments> childMyGalleryCommentsArrayList, String type){
        this.context = context;
        this.childMyGalleryCommentsArrayList = childMyGalleryCommentsArrayList;
        this.type = type;
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
        return childMyGalleryCommentsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childMyGalleryCommentsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_my_gallery_detail , null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView commentTime = (TextView) convertView.findViewById(R.id.commentTime);
        TextView commentedDate = (TextView) convertView.findViewById(R.id.commentedDate);
        TextView comment = (TextView) convertView.findViewById(R.id.comment);

        name.setTypeface(helvetica);
        commentTime.setTypeface(helvetica);
        commentedDate.setTypeface(helvetica);
        comment.setTypeface(helvetica);

        ChildMyGalleryComments galleryBean = childMyGalleryCommentsArrayList.get(position);

        name.setText(galleryBean.getFullName());
        commentTime.setText(galleryBean.getCommentedTime());
        commentedDate.setText(galleryBean.getCommentedDate());

        if(type.equals("comments")){
            comment.setVisibility(View.VISIBLE);
            comment.setText(galleryBean.getComment());
        }else{
            comment.setVisibility(View.GONE);
        }



        return convertView;
    }
}
