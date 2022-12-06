package com.dunvant.application.child.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChildChunkVideoListBean;

import java.util.ArrayList;

/**
 * Created by DEVLABS\pbadhan on 26/4/17.
 */
public class ChildChunkVideoAdapter  extends BaseAdapter {
    Context context;
    ArrayList<ChildChunkVideoListBean> childChunkVideoListBeanArrayList;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public ChildChunkVideoAdapter(Context context, ArrayList<ChildChunkVideoListBean> childChunkVideoListBeanArrayList){
        this.context = context;
        this.childChunkVideoListBeanArrayList = childChunkVideoListBeanArrayList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");


    }

    @Override
    public int getCount() {
        return childChunkVideoListBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childChunkVideoListBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_chunk_video_item, null);
        final TextView textCount = (TextView) convertView.findViewById(R.id.textCount);
        final ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        final LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout);

        textCount.setTypeface(helvetica);

        final ChildChunkVideoListBean childChunkVideoListBean = childChunkVideoListBeanArrayList.get(position);
        textCount.setText(childChunkVideoListBean.getId());

//        System.out.println("Position::" + position + "Size::" + childChunkVideoListBeanArrayList.size());
        if (position==childChunkVideoListBeanArrayList.size()-1 && childChunkVideoListBeanArrayList.size()>1){
            delete.setVisibility(View.VISIBLE);
        }else{
            delete.setVisibility(View.GONE);
        }


//        layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (position==childChunkVideoListBeanArrayList.size()-1){
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//                    builder1.setMessage("Are you sure you want to Delete this video?");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Yes",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    childChunkVideoListBeanArrayList.remove(position);
//                                    notifyDataSetChanged();
//                                    dialog.cancel();
//                                }
//                            });
//
//                    builder1.setNegativeButton(
//                            "No",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//
//                }
//
//
//
//            }
//        });



        return convertView;
    }


}
