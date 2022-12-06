package com.dunvant.application.parent.adapters;

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
import com.dunvant.application.R;
import com.dunvant.application.beans.AttributesValuesBean;
import com.dunvant.application.parent.ParentOnlineShoppingProductDetailScreen;

import java.util.ArrayList;

public class ParentOnlineShoppingSelectOptionsCartAdapter extends BaseAdapter {
    boolean check = false;
    Context context;
    ArrayList<AttributesValuesBean> attributesValuesBeanArrayList;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    ParentOnlineShoppingProductDetailScreen parentOnlineShoppingProductDetailScreen;

    public ParentOnlineShoppingSelectOptionsCartAdapter(Context context, ArrayList<AttributesValuesBean> attributesValuesBeanArrayList, ParentOnlineShoppingProductDetailScreen parentOnlineShoppingProductDetailScreen){
        this.context = context;
        this.attributesValuesBeanArrayList = attributesValuesBeanArrayList;
        this.parentOnlineShoppingProductDetailScreen = parentOnlineShoppingProductDetailScreen;
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
        return attributesValuesBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return attributesValuesBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_online_shopping_select_option_cart_item, null);

        final TextView text =  convertView.findViewById(R.id.text);
        final TextView title = convertView.findViewById(R.id.title);

        final AttributesValuesBean attributesValuesBean = attributesValuesBeanArrayList.get(position);

        for(int i=0; i<attributesValuesBean.getAttributesValuesDataBeanArrayList().size(); i++){
            if(attributesValuesBean.getAttributesValuesDataBeanArrayList().get(i).getClickedBool().equalsIgnoreCase("true")){
                title.setText(attributesValuesBean.getAttributesValuesDataBeanArrayList().get(i).getAttribute_name());
                text.setText(attributesValuesBean.getAttributesValuesDataBeanArrayList().get(i).getAttribute_value_name());
            }
        }



//        if(attributesValuesBean.getAttributesValuesDataBeanArrayList().get(position).getClickedBool().equalsIgnoreCase("true")){
//            text.setText(attributesValuesBean.getAttributesValuesDataBeanArrayList().get(position).getAttribute_value_name());
//        }

//        text.setText(attributesValuesBean.getAttributesValuesDataBeanArrayList().get(0).getAttribute_value_name());


//        text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final Dialog dialog = new Dialog(context);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.parent_dialog_box_show);
//
//                ListView listView = (ListView) dialog.findViewById(R.id.listView);
//
//                ParentDialogBoxShowAdapter parentDialogBoxShowAdapter = new ParentDialogBoxShowAdapter(context, attributesValuesBean.getAttributesValuesDataBeanArrayList());
//                listView.setAdapter(parentDialogBoxShowAdapter);
//
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        parentOnlineShoppingProductDetailScreen.funcUpdateBool(position, i);
//                        text.setText(""+attributesValuesBean.getAttributesValuesDataBeanArrayList().get(i).getAttribute_value_name());
//                        dialog.dismiss();
//                    }
//                });
//
//                dialog.show();
//
//            }
//        });

        return convertView;
    }



}