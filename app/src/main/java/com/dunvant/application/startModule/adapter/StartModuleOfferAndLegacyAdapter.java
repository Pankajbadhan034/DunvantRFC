package com.dunvant.application.startModule.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.StartModuleOfferAndLegacyBean;

import java.util.ArrayList;

public class StartModuleOfferAndLegacyAdapter extends BaseAdapter {

    Context context;
    ArrayList<StartModuleOfferAndLegacyBean> startModuleResourcebeanArrayList;

    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    String titleStr;

    public StartModuleOfferAndLegacyAdapter(Context context, ArrayList<StartModuleOfferAndLegacyBean> startModuleResourcebeanArrayList, String titleStr) {
        this.context = context;
        this.startModuleResourcebeanArrayList = startModuleResourcebeanArrayList;
        this.titleStr = titleStr;
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
        convertView = layoutInflater.inflate(R.layout.start_module_offers_legacy_adapter, null);

        RelativeLayout mainRelativeLayout = convertView.findViewById(R.id.mainRelativeLayout);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView desc = (TextView) convertView.findViewById(R.id.desc);

        final StartModuleOfferAndLegacyBean parentOnlineStoreBean = startModuleResourcebeanArrayList.get(position);

       // imageLoader.displayImage(parentOnlineStoreBean.getImage_url(), image, options);
        name.setText(parentOnlineStoreBean.getTitle());

        desc.setText(Html.fromHtml(parentOnlineStoreBean.getDescription()));

        name.setTypeface(linoType);
        desc.setTypeface(linoType);

        if(parentOnlineStoreBean.getType().equalsIgnoreCase("Legacy")){
            image.setVisibility(View.GONE);
        }else{
            image.setVisibility(View.VISIBLE);
        }



        if(position % 2 == 0) {
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            name.setTextColor(context.getResources().getColor(R.color.black));
            desc.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            name.setTextColor(context.getResources().getColor(R.color.white));
            desc.setTextColor(context.getResources().getColor(R.color.white));

        }


        return convertView;
    }
}