package com.dunvant.application.child.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildMyGalleryBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildMyGalleryDetailScreen;
import com.dunvant.application.child.ChildMyGallerySingleImageScreen;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildMyGalleryAdapter extends BaseAdapter implements IWebServiceCallback {
    private final String VIDEO_DELETE = "VIDEO_DELETE";
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    int positionValue;
    Context context;
    ArrayList<ChildMyGalleryBean> childMyGalleryBeanArrayList;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Typeface helvetica;
    Typeface linoType;

    public ChildMyGalleryAdapter(Context context, ArrayList<ChildMyGalleryBean> childMyGalleryBeanArrayList){
        this.context = context;
        this.childMyGalleryBeanArrayList = childMyGalleryBeanArrayList;
        layoutInflater = LayoutInflater.from(context);

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

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

    }

    @Override
    public int getCount() {
        return childMyGalleryBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childMyGalleryBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_my_gallery_item, null);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        ImageView myImage = (ImageView) convertView.findViewById(R.id.myImage);
        TextView likeGallery = (TextView) convertView.findViewById(R.id.likeGallery);
        TextView commentGallery = (TextView) convertView.findViewById(R.id.commentGallery);
        TextView dateGallery = (TextView) convertView.findViewById(R.id.dateGallery);
        LinearLayout linearfooter = (LinearLayout) convertView.findViewById(R.id.linearfooter);
        ImageView publicPrivte = (ImageView) convertView.findViewById(R.id.publicPrivte);
        ImageView deleteVideo = (ImageView) convertView.findViewById(R.id.deleteVideo);

        title.setTypeface(helvetica);
        likeGallery.setTypeface(helvetica);
        commentGallery.setTypeface(helvetica);
        dateGallery.setTypeface(helvetica);

        final ChildMyGalleryBean galleryBean = childMyGalleryBeanArrayList.get(position);

        if(galleryBean.getFileType().contains("video")){
            imageLoader.displayImage(galleryBean.getThumbnailImage(), myImage, options);
            deleteVideo.setBackgroundResource(R.drawable.delete);
            deleteVideo.setVisibility(View.VISIBLE);
        }else if(galleryBean.getFileType().contains("image")){
            imageLoader.displayImage(galleryBean.getFileUrl(), myImage, options);
            deleteVideo.setBackgroundResource(R.drawable.delete);
            deleteVideo.setVisibility(View.VISIBLE);
        }

        if(galleryBean.getIsPublic().equalsIgnoreCase("0")){
            publicPrivte.setVisibility(View.VISIBLE);
            publicPrivte.setBackgroundResource(R.drawable.privateicon);
//            System.out.println("private Video here");
        }else{
//            System.out.println("public Video here");
            publicPrivte.setVisibility(View.GONE);
        }

        title.setText(galleryBean.getTitle());
        likeGallery.setText(galleryBean.getLike());
        commentGallery.setText(galleryBean.getComments());
        dateGallery.setText(galleryBean.getCreatedAtFormatted());

        deleteVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "--"+galleryBean.getIsPublic(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (Utilities.isNetworkAvailable(context)) {
                                    positionValue = position;
                                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                                    nameValuePairList.add(new BasicNameValuePair("media_id", galleryBean.getSiteMediaId()));

                                    String webServiceUrl = Utilities.BASE_URL + "user_posts/delete_media";

                                    ArrayList<String> headers = new ArrayList<>();
                                    headers.add("X-access-uid:" + loggedInUser.getId());
                                    headers.add("X-access-token:" + loggedInUser.getToken());

                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, VIDEO_DELETE, ChildMyGalleryAdapter.this, headers);
                                    postWebServiceAsync.execute(webServiceUrl);
                                } else {
                                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(context, ChildMyGallerySingleImageScreen.class);
                obj.putExtra("type", galleryBean.getFileType());
                obj.putExtra("url", galleryBean.getFileUrl());
                obj.putExtra("like", galleryBean.getLike());
                obj.putExtra("comment", galleryBean.getComments());
                obj.putExtra("date", galleryBean.getCreatedAtFormatted());
                obj.putExtra("siteMediaId", galleryBean.getSiteMediaId());
                obj.putExtra("title", galleryBean.getTitle());
                obj.putExtra("description", galleryBean.getDescription());
                context.startActivity(obj);
            }
        });



        linearfooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(context, ChildMyGalleryDetailScreen.class);
                obj.putExtra("type", galleryBean.getFileType());
                obj.putExtra("url", galleryBean.getFileUrl());
                obj.putExtra("like", galleryBean.getLike());
                obj.putExtra("comment", galleryBean.getComments());
                obj.putExtra("date", galleryBean.getCreatedAtFormatted());
                obj.putExtra("siteMediaId", galleryBean.getSiteMediaId());
                context.startActivity(obj);
            }
        });

//        likeGallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        commentGallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        return convertView;
    }
    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case VIDEO_DELETE:
                if (response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            childMyGalleryBeanArrayList.remove(positionValue);
                            notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;
        }
    }
}
