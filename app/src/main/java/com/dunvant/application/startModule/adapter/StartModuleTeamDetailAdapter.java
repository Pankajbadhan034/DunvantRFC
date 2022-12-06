package com.dunvant.application.startModule.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.StartModuleTeamDetailBean;

import java.util.ArrayList;

public class StartModuleTeamDetailAdapter extends BaseAdapter {

    Context context;
    ArrayList<StartModuleTeamDetailBean> startModuleResourcebeanArrayList;

    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;


    public StartModuleTeamDetailAdapter(Context context, ArrayList<StartModuleTeamDetailBean> startModuleResourcebeanArrayList) {
        this.context = context;
        this.startModuleResourcebeanArrayList = startModuleResourcebeanArrayList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

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
        return startModuleResourcebeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return startModuleResourcebeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.start_module_team_detail_adapter, null);

        RelativeLayout mainRelativeLayout = convertView.findViewById(R.id.mainRelativeLayout);
        TextView title = (TextView) convertView.findViewById(R.id.title);

        final StartModuleTeamDetailBean parentOnlineStoreBean = startModuleResourcebeanArrayList.get(position);

        title.setText(parentOnlineStoreBean.getTitle());
        title.setTypeface(linoType);



        return convertView;
    }
}