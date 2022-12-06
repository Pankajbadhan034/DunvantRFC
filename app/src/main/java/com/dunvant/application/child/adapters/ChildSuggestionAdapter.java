package com.dunvant.application.child.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildSuggestionBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildSuggestionUserProfileScreen;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DEVLABS\pbadhan on 10/5/17.
 */
public class ChildSuggestionAdapter extends BaseAdapter implements IWebServiceCallback {
    int position=-1;
    private final String SEND_REQUEST_IFA_MEMBER = "SEND_REQUEST_IFA_MEMBER";
    ArrayList<ChildSuggestionBean> childSuggestionBeanArrayList;
    Context context;
    LayoutInflater layoutInflater;
    ImageLoader imageLoaderWithoutCircle = ImageLoader.getInstance();
    DisplayImageOptions optionsWithoutCircle;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    public  ChildSuggestionAdapter(Context context, ArrayList<ChildSuggestionBean>childSuggestionBeanArrayList){
        this.context = context;
        this.childSuggestionBeanArrayList = childSuggestionBeanArrayList;
        layoutInflater = LayoutInflater.from(context);

        imageLoaderWithoutCircle.init(ImageLoaderConfiguration.createDefault(context));
        optionsWithoutCircle = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .displayer(new RoundedBitmapDisplayer(1000))
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

    }

    @Override
    public int getCount() {
        return childSuggestionBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childSuggestionBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_suggestions_timeline , null);
        ImageView picUrl = (ImageView) convertView.findViewById(R.id.picUrl);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView sendRequest = convertView.findViewById(R.id.sendRequest);
        ImageView privatePublic = convertView.findViewById(R.id.privatePublic);


        final ChildSuggestionBean childSuggestionBean = childSuggestionBeanArrayList.get(position);
        title.setText(childSuggestionBean.getFullName());
        System.out.println("childSuggestionBeanTitle::" + childSuggestionBean.getFullName());
        imageLoaderWithoutCircle.displayImage(childSuggestionBean.getProfilePicUrl(), picUrl, optionsWithoutCircle);


        if(childSuggestionBean.getIsPrivate().equalsIgnoreCase("0")){
            privatePublic.setBackgroundResource(R.drawable.unlock_black);
        }else{
            privatePublic.setBackgroundResource(R.drawable.lock_black);
        }

        picUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childSuggestionBean.getIsPrivate().equalsIgnoreCase("0")) {
                    Intent obj = new Intent(context, ChildSuggestionUserProfileScreen.class);
                    obj.putExtra("fullName", childSuggestionBean.getFullName());
                    obj.putExtra("favTeam", childSuggestionBean.getFavouriteTeam());
                    obj.putExtra("favPlayer", childSuggestionBean.getFavouritePlayer());
                    obj.putExtra("favPosition", childSuggestionBean.getFavouritePosition());
                    obj.putExtra("profilePicUrl", childSuggestionBean.getProfilePicUrl());
                    obj.putExtra("canSendRequest", childSuggestionBean.getCanSendRequest());
                    obj.putExtra("userId", childSuggestionBean.getUserId());
                    context.startActivity(obj);
                } else {
                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                    Toast.makeText(context, "You cannot view this "+verbiage_singular.toLowerCase()+"'s profile as he has set to private.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChildSuggestionAdapter.this.position = position;

                if (Utilities.isNetworkAvailable(context)) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();

                    // Changing the following to array
                    String userId = "[\""+childSuggestionBean.getUserId()+"\"]";
                    nameValuePairList.add(new BasicNameValuePair("friend_id", userId));

                    String webServiceUrl = Utilities.BASE_URL + "account/send_request_to_ifa";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, SEND_REQUEST_IFA_MEMBER, ChildSuggestionAdapter.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case SEND_REQUEST_IFA_MEMBER:

                if (response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status){
                            if(position!=-1){
                                childSuggestionBeanArrayList.remove(position);
                                notifyDataSetChanged();
                            }
                        }

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
