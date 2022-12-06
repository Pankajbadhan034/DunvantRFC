package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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
import com.dunvant.application.beans.ParentOnlineStoreProductsBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentOnlineShoppingProductStoreAdapter extends BaseAdapter {

    Context context;
    ArrayList<ParentOnlineStoreProductsBean> parentOnlineStoreProductsBeanArrayList;

    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public ParentOnlineShoppingProductStoreAdapter(Context context, ArrayList<ParentOnlineStoreProductsBean> parentOnlineStoreProductsBeanArrayList){
        this.context = context;
        this.parentOnlineStoreProductsBeanArrayList = parentOnlineStoreProductsBeanArrayList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

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
        return parentOnlineStoreProductsBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return parentOnlineStoreProductsBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_online_shopping_product_adapter, null);

        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        //TextView desc = (TextView) convertView.findViewById(R.id.desc);
        TextView price = convertView.findViewById(R.id.price);

        final ParentOnlineStoreProductsBean parentOnlineStoreProductsBean = parentOnlineStoreProductsBeanArrayList.get(position);
        name.setText(parentOnlineStoreProductsBean.getName());
       // desc.setText(Html.fromHtml(parentOnlineStoreProductsBean.getDescription()));
        imageLoader.displayImage(parentOnlineStoreProductsBean.getImage(), image, options);

        name.setTypeface(linoType);
        //desc.setTypeface(linoType);

        if(parentOnlineStoreProductsBean.getPrice().equalsIgnoreCase("No Price")){
          //  price.setVisibility(View.INVISIBLE);

        }else{
            price.setVisibility(View.VISIBLE);
            SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            String academy_currency = sharedPreferences.getString("academy_currency", null);
            price.setText(academy_currency+" "+parentOnlineStoreProductsBean.getPrice());
        }

//        if(position % 2 == 0){
//            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
//            categoryName.setTextColor(context.getResources().getColor(R.color.white));
//            desc.setTextColor(context.getResources().getColor(R.color.white));
//        } else {
//            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
//            categoryName.setTextColor(context.getResources().getColor(R.color.black));
//            desc.setTextColor(context.getResources().getColor(R.color.black));
//        }

        return convertView;
    }
}