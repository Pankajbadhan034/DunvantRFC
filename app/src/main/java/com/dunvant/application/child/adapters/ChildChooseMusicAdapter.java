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
import android.widget.TextView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildChooseMusicCategoryBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildChooseSubMusicScreen;
import com.dunvant.application.utils.Utilities;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class ChildChooseMusicAdapter  extends BaseAdapter {
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Context context;
    ArrayList<ChildChooseMusicCategoryBean> childChooseMusicCategoryBeanArrayList;
    String galleryPath;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Typeface helvetica;
    Typeface linoType;

    public ChildChooseMusicAdapter(Context context, ArrayList<ChildChooseMusicCategoryBean> childChooseMusicCategoryBeanArrayList, String galleryPath){
        this.context = context;
        this.childChooseMusicCategoryBeanArrayList = childChooseMusicCategoryBeanArrayList;
        this.galleryPath = galleryPath;
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

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

    }

    @Override
    public int getCount() {
        return childChooseMusicCategoryBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childChooseMusicCategoryBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_choose_music_item, null);
        final TextView categoryname = (TextView) convertView.findViewById(R.id.categoryname);
        final ImageView backImage = (ImageView) convertView.findViewById(R.id.backImage);

        categoryname.setTypeface(helvetica);

        final ChildChooseMusicCategoryBean childChooseMusicCategoryBean = childChooseMusicCategoryBeanArrayList.get(position);
        categoryname.setText(childChooseMusicCategoryBean.getCategoryName());

        imageLoader.displayImage(childChooseMusicCategoryBean.getCategoryUrl(), backImage, options);


        categoryname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(context, ChildChooseSubMusicScreen.class);
                obj.putExtra("category_name", childChooseMusicCategoryBean.getCategoryName());
                obj.putExtra("category_id", childChooseMusicCategoryBean.getCategoryId());
              //  obj.putExtra("subCategoryUrl", childChooseMusicCategoryBean.getSubCategoryUrl());
                obj.putExtra("galleryPath", galleryPath);
                context.startActivity(obj);
        //        ((Activity)context).finish();
            }
        });

        return convertView;
    }


}
