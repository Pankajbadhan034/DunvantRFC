package com.dunvant.application.child.smartBand.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.child.smartBand.bean.ChildSmartBandHeartRateHistoryBean;

import java.util.ArrayList;
public class ChildHeartRateNonIFadpater extends BaseAdapter {
    Context context;
    ArrayList<ChildSmartBandHeartRateHistoryBean> childSmartBandHeartRateHistoryBeanArrayList;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Typeface helvetica;
    Typeface linoType;
    String parentId, nameStr;

    public ChildHeartRateNonIFadpater(Context context, ArrayList<ChildSmartBandHeartRateHistoryBean> childSmartBandHeartRateHistoryBeanArrayList){
        this.context = context;
        this.childSmartBandHeartRateHistoryBeanArrayList = childSmartBandHeartRateHistoryBeanArrayList;
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
        return childSmartBandHeartRateHistoryBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childSmartBandHeartRateHistoryBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_heart_rate_non_ifa_item, null);

        TextView min = (TextView) convertView.findViewById(R.id.min);
        TextView meter = (TextView) convertView.findViewById(R.id.meter);
        TextView max = (TextView) convertView.findViewById(R.id.max);
        TextView session = (TextView) convertView.findViewById(R.id.session);

        final ChildSmartBandHeartRateHistoryBean childSmartBandHeartRateHistoryBean = childSmartBandHeartRateHistoryBeanArrayList.get(position);

        session.setText(childSmartBandHeartRateHistoryBean.getStartTime()+" to "+childSmartBandHeartRateHistoryBean.getEndTime());
        min.setText(childSmartBandHeartRateHistoryBean.getMinVal()+" min");
        max.setText(""+childSmartBandHeartRateHistoryBean.getMaxVal()+" max");
        meter.setText(childSmartBandHeartRateHistoryBean.getType());


        return convertView;
    }
}
