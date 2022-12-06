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
import com.dunvant.application.beans.ChildAllCommentsLikes;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by DEVLABS\pbadhan on 19/4/17.
 */
public class ChildCommentsLikesListTimelineAdapter extends BaseAdapter {
    Context context;
    ArrayList<ChildAllCommentsLikes> childAllCommentsLikesArrayList;
    String type;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Typeface helvetica;
    Typeface linoType;

    public ChildCommentsLikesListTimelineAdapter(Context context, ArrayList<ChildAllCommentsLikes> childAllCommentsLikesArrayList, String type) {
        this.context = context;
        this.childAllCommentsLikesArrayList = childAllCommentsLikesArrayList;
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

        helvetica = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

    }

    @Override
    public int getCount() {
        return childAllCommentsLikesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childAllCommentsLikesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_comments_likes_timeline_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView comment = (TextView) convertView.findViewById(R.id.comment);

        name.setTypeface(helvetica);
        time.setTypeface(helvetica);
        date.setTypeface(helvetica);
        comment.setTypeface(helvetica);

        final ChildAllCommentsLikes childAllCommentsLikes = childAllCommentsLikesArrayList.get(position);

        name.setText(childAllCommentsLikes.getFullName());
        time.setText(childAllCommentsLikes.getCommentedTime());
        date.setText(childAllCommentsLikes.getCommentedDate());
        if(type.equals("comments")){
            comment.setVisibility(View.VISIBLE);
            comment.setText(childAllCommentsLikes.getComment());
        }else{
            comment.setVisibility(View.GONE);
        }


        return convertView;
    }
}