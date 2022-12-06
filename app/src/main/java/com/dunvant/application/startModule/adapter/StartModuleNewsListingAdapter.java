package com.dunvant.application.startModule.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.child.StartModuleNewsListingBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class StartModuleNewsListingAdapter extends BaseAdapter {

    Context context;
    ArrayList<StartModuleNewsListingBean> startModuleNewsListingBeanArrayList;

    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public StartModuleNewsListingAdapter(Context context, ArrayList<StartModuleNewsListingBean> startModuleNewsListingBeanArrayList) {
        this.context = context;
        this.startModuleNewsListingBeanArrayList = startModuleNewsListingBeanArrayList;
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
                .build();

    }

    @Override
    public int getCount() {
        return startModuleNewsListingBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return startModuleNewsListingBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.start_module_news_listing_adapter, null);

    //    RelativeLayout mainRelativeLayout = convertView.findViewById(R.id.mainRelativeLayout);
        ImageView image =  convertView.findViewById(R.id.image);
        TextView title = convertView.findViewById(R.id.title);
        TextView time = convertView.findViewById(R.id.time);
        //TextView desc = convertView.findViewById(R.id.desc);

        final StartModuleNewsListingBean startModuleNewsListingBean = startModuleNewsListingBeanArrayList.get(position);

        imageLoader.displayImage(startModuleNewsListingBean.getThumbnail(), image, options);

        title.setText(startModuleNewsListingBean.getTitle());
//        time.setText(getDate(Long.parseLong(startModuleNewsListingBean.getTime()), "dd MMM yyyy, hh:mm:ss"));


      //  time.setText(getDate(Long.parseLong(startModuleNewsListingBean.getTime()), "dd MMM yyyy"));


        if(startModuleNewsListingBean.getType().equalsIgnoreCase("1")){
            time.setText(startModuleNewsListingBean.getTime());
        }else{
            try{
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                cal.setTimeInMillis(Long.parseLong(startModuleNewsListingBean.getTime()) * 1000);
                String date = DateFormat.format("dd MMM yyyy", cal).toString();
                time.setText(date);
            }catch (Exception e){
                e.printStackTrace();
            }
        }







        // desc.setText(Html.fromHtml(parentOnlineStoreBean.getDescription()));

        title.setTypeface(linoType);
        time.setTypeface(helvetica);

//
//        if(position % 2 == 0) {
//            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
//            title.setTextColor(context.getResources().getColor(R.color.black));
//            time.setTextColor(context.getResources().getColor(R.color.black));
//        } else {
//            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
//            title.setTextColor(context.getResources().getColor(R.color.white));
//            time.setTextColor(context.getResources().getColor(R.color.white));
//        }


        return convertView;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setTimeZone(TimeZone.getDefault());

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
