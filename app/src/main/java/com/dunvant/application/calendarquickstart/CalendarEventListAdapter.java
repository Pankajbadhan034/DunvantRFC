package com.dunvant.application.calendarquickstart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.CalendarListBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CalendarEventListAdapter extends BaseAdapter {

    Context context;
    ArrayList<CalendarListBean> startModuleResourcebeanArrayList;

    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public CalendarEventListAdapter(Context context, ArrayList<CalendarListBean> startModuleResourcebeanArrayList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.calendar_event_list_adapter, null);
        LinearLayout linear = convertView.findViewById(R.id.linear);
        TextView eventName = (TextView) convertView.findViewById(R.id.eventName);
        TextView startDate = (TextView) convertView.findViewById(R.id.startDate);
        TextView endDate = (TextView) convertView.findViewById(R.id.endDate);
        TextView organiser = (TextView) convertView.findViewById(R.id.organiser);

        final CalendarListBean calendarListBean = startModuleResourcebeanArrayList.get(position);

        eventName.setText("EVENT : "+calendarListBean.getEventDetail());
        try {
            JSONObject jsonObject = new JSONObject(calendarListBean.getStartTime());
            String startTime = jsonObject.getString("dateTime");
            startDate.setText("START TIME : "+startTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(calendarListBean.getEndTime());
            String endTime = jsonObject.getString("dateTime");
            endDate.setText("END TIME : "+endTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(calendarListBean.getOragniserName());
            String email = jsonObject.getString("email");
            organiser.setText("ORGANISER : "+email);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(position % 2 == 0) {
            linear.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            eventName.setTextColor(context.getResources().getColor(R.color.white));
            startDate.setTextColor(context.getResources().getColor(R.color.white));
            endDate.setTextColor(context.getResources().getColor(R.color.white));
            organiser.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            linear.setBackgroundColor(context.getResources().getColor(R.color.white));
            eventName.setTextColor(context.getResources().getColor(R.color.black));
            startDate.setTextColor(context.getResources().getColor(R.color.black));
            endDate.setTextColor(context.getResources().getColor(R.color.black));
            organiser.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;

    }
}
