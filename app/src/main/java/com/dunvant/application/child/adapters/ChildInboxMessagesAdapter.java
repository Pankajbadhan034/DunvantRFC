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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildMyMessagesBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildMyMessageDetailScreen;
import com.dunvant.application.utils.Utilities;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
//naina
//        test@12
public class ChildInboxMessagesAdapter extends BaseAdapter {
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Context context;
    ArrayList<ChildMyMessagesBean> childMyMessagesBeanArrayList;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Typeface helvetica;
    Typeface linoType;
    String parentId, nameStr;

    public ChildInboxMessagesAdapter(Context context, ArrayList<ChildMyMessagesBean> childMyMessagesBeanArrayList){
        this.context = context;
        this.childMyMessagesBeanArrayList = childMyMessagesBeanArrayList;
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
        return childMyMessagesBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childMyMessagesBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_my_messages_item, null);

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        RelativeLayout nextScreen = (RelativeLayout) convertView.findViewById(R.id.nextScreen);
        ImageView pic = (ImageView) convertView.findViewById(R.id.pic);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView message = (TextView) convertView.findViewById(R.id.message);
        final TextView badgeCount = (TextView) convertView.findViewById(R.id.badgeCount);

        name.setTypeface(helvetica);
        date.setTypeface(helvetica);
        message.setTypeface(helvetica);
        badgeCount.setTypeface(helvetica);

        final ChildMyMessagesBean childMyMessagesBean = childMyMessagesBeanArrayList.get(position);


        date.setText(childMyMessagesBean.getMessageFormattedDate());
        message.setText(childMyMessagesBean.getSubject());
        imageLoader.displayImage(childMyMessagesBean.getReceiverDpUrl(), pic, options);
        parentId = childMyMessagesBean.getParentId();

//       System.out.println("loggedUsedId::"+loggedInUser.getId()+"chatUserId::"+childMyMessagesBean.getUserId());
        if(childMyMessagesBean.getUserId().equalsIgnoreCase(loggedInUser.getId())){
            nameStr = childMyMessagesBean.getRecieverName();
        }else{
            nameStr = childMyMessagesBean.getSenderName();
        }
        name.setText(nameStr);


        if(childMyMessagesBean.getUnreadCount().equalsIgnoreCase("0")){
            badgeCount.setVisibility(View.INVISIBLE);
        }else{
            badgeCount.setVisibility(View.VISIBLE);
            badgeCount.setText(childMyMessagesBean.getUnreadCount());
        }

        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childMyMessagesBeanArrayList.get(position).setUnreadCount("0");
                notifyDataSetChanged();
                Intent obj = new Intent(context, ChildMyMessageDetailScreen.class);
                obj.putExtra("parentId", childMyMessagesBean.getParentId());

                if(childMyMessagesBean.getUserId().equalsIgnoreCase(loggedInUser.getId())){
                    obj.putExtra("name", childMyMessagesBean.getRecieverName());
                }else{
                    obj.putExtra("name", childMyMessagesBean.getSenderName());
                }


                obj.putExtra("friendId", childMyMessagesBean.getFriendId());
                obj.putExtra("unread_msgs_ids", childMyMessagesBean.getUnreadMessagesIds());
                obj.putExtra("subject", childMyMessagesBean.getSubject());
                context.startActivity(obj);
            }
        });

        if(position % 2 == 0) {
            nextScreen.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            name.setTextColor(context.getResources().getColor(R.color.white));
            message.setTextColor(context.getResources().getColor(R.color.white));
            date.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            nextScreen.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            name.setTextColor(context.getResources().getColor(R.color.black));
            message.setTextColor(context.getResources().getColor(R.color.black));
            date.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }
}
