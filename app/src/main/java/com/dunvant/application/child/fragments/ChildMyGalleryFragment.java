package com.dunvant.application.child.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildMyGalleryBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildMainScreen;
import com.dunvant.application.child.ChildMyGalleryAddNewPhotoScreen;
import com.dunvant.application.child.adapters.ChildMyGalleryAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DEVLABS\pbadhan on 29/12/16.
 */
public class ChildMyGalleryFragment extends Fragment implements IWebServiceCallback {
    Button addPicAndVids;

    ChildMyGalleryAdapter childMyGalleryAdapter;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    ChildMyGalleryBean childMyGalleryBean;
    ArrayList<ChildMyGalleryBean> childMyGalleryArrayList = new ArrayList<>();
    GridView myGalleryList;
    private final String GET_MY_GALLERY = "GET_MY_GALLERY";
    Button allGallery;
    Button photosGallery;
    Button videosGallery;
    Typeface helvetica;
    Typeface linoType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_fragment_my_gallery, container, false);
        myGalleryList = (GridView) view.findViewById(R.id.myGalleryList);
        allGallery = (Button) view.findViewById(R.id.allGallery);
        photosGallery = (Button) view.findViewById(R.id.photosGallery);
        videosGallery = (Button) view.findViewById(R.id.videosGallery);
        addPicAndVids = (Button) view.findViewById(R.id.addPicAndVids);

        allGallery.setTypeface(linoType);
        photosGallery.setTypeface(linoType);
        videosGallery.setTypeface(linoType);
        addPicAndVids.setTypeface(linoType);

        addPicAndVids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(getActivity(),ChildMyGalleryAddNewPhotoScreen.class);
                obj.putExtra("GALLERY_TYPE", ChildMainScreen.GALLERY_TYPE);
                startActivity(obj);
            }
        });

//        if(Utilities.isNetworkAvailable(getActivity())) {
//
//            childMyGalleryArrayList.clear();
//            List<NameValuePair> nameValuePairList = new ArrayList<>();
//            nameValuePairList.add(new BasicNameValuePair("offset", "0"));
//            nameValuePairList.add(new BasicNameValuePair("gallery_type", "image"));
//
//            String webServiceUrl = Utilities.BASE_URL + "children/gallery_list";
//
//            ArrayList<String> headers = new ArrayList<>();
//            headers.add("X-access-uid:"+loggedInUser.getId());
//            headers.add("X-access-token:"+loggedInUser.getToken());
//
//            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_MY_GALLERY, ChildMyGalleryFragment.this, headers);
//            postWebServiceAsync.execute(webServiceUrl);
//        }else{
//            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//        }

        allGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChildMainScreen.GALLERY_TYPE = "allGallery";
                allGalleryTabClick();
                if (Utilities.isNetworkAvailable(getActivity())) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("offset", "0"));
                    nameValuePairList.add(new BasicNameValuePair("gallery_type", "all"));

                    String webServiceUrl = Utilities.BASE_URL + "children/gallery_list";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_MY_GALLERY, ChildMyGalleryFragment.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        photosGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChildMainScreen.GALLERY_TYPE = "photos";
                photoTabClick();
                if(Utilities.isNetworkAvailable(getActivity())) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("offset", "0"));
                    nameValuePairList.add(new BasicNameValuePair("gallery_type", "image"));

                    String webServiceUrl = Utilities.BASE_URL + "children/gallery_list";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_MY_GALLERY, ChildMyGalleryFragment.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                }else{
                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        videosGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChildMainScreen.GALLERY_TYPE = "videos";
                videoTabclick();
                if(Utilities.isNetworkAvailable(getActivity())) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("offset", "0"));
                    nameValuePairList.add(new BasicNameValuePair("gallery_type", "video"));

                    String webServiceUrl = Utilities.BASE_URL + "children/gallery_list";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_MY_GALLERY, ChildMyGalleryFragment.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                }else{
                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        photoTabClick();
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("offset", "0"));
            nameValuePairList.add(new BasicNameValuePair("gallery_type", "image"));

            String webServiceUrl = Utilities.BASE_URL + "children/gallery_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_MY_GALLERY, ChildMyGalleryFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);
        }else{
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case GET_MY_GALLERY:
                childMyGalleryArrayList.clear();
                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            Gson gson = new Gson();
                            String jsonParentUserBean = gson.toJson(loggedInUser);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("loggedInUser", jsonParentUserBean);
                            editor.commit();

                            JSONArray myGalleryArray=new JSONArray(responseObject.getString("data"));

                            for(int i = 0; i < myGalleryArray.length(); i++){
                                JSONObject galleryData = myGalleryArray.getJSONObject(i);
                                childMyGalleryBean = new ChildMyGalleryBean();
                                childMyGalleryBean.setId(galleryData.getString("id"));
                                childMyGalleryBean.setUserId(galleryData.getString("users_id"));
                                childMyGalleryBean.setSiteMediaId(galleryData.getString("site_media_id"));
                                childMyGalleryBean.setTitle(galleryData.getString("title"));
                                childMyGalleryBean.setDescription(galleryData.getString("description"));

                                /*JSONObject descriptionObject = galleryData.getJSONObject("description");
                                if(descriptionObject.has("post_description")) {
                                    childMyGalleryBean.setDescription(descriptionObject.getString("post_description"));
                                } else {
                                    childMyGalleryBean.setDescription("");
                                }*/

                                childMyGalleryBean.setFileName(galleryData.getString("file_name"));
                                childMyGalleryBean.setFileType(galleryData.getString("file_type"));
                                childMyGalleryBean.setIsPublic(galleryData.getString("is_public"));
                                childMyGalleryBean.setCreatedAt(galleryData.getString("created_at"));
                                childMyGalleryBean.setCreatedAtFormatted(galleryData.getString("created_at_formatted"));
                                childMyGalleryBean.setFileUrl(galleryData.getString("file_url"));
                                if(galleryData.has("video_thumbnail")){
                                    childMyGalleryBean.setThumbnailImage(galleryData.getString("video_thumbnail"));
                                }else{
                                    childMyGalleryBean.setThumbnailImage("noThumb");
                                }
                                childMyGalleryBean.setLike(galleryData.getString("likes"));
                                childMyGalleryBean.setComments(galleryData.getString("comments"));

                                childMyGalleryArrayList.add(childMyGalleryBean);
                            }


                        }

                      //  Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }

                childMyGalleryAdapter = new ChildMyGalleryAdapter(getActivity(),childMyGalleryArrayList);
                myGalleryList.setAdapter(childMyGalleryAdapter);
                childMyGalleryAdapter.notifyDataSetChanged();
        }

    }

    public void allGalleryTabClick(){
        allGallery.setBackgroundColor(getActivity().getResources().getColor(R.color.yellow));
        photosGallery.setBackgroundColor(Color.parseColor("#333333"));
        videosGallery.setBackgroundColor(Color.parseColor("#333333"));

        allGallery.setTextColor(Color.parseColor("#333333"));
        photosGallery.setTextColor(Color.parseColor("#ffffff"));
        videosGallery.setTextColor(Color.parseColor("#ffffff"));

    }
    public void photoTabClick(){
        allGallery.setBackgroundColor(Color.parseColor("#333333"));
        photosGallery.setBackgroundColor(getActivity().getResources().getColor(R.color.yellow));
        videosGallery.setBackgroundColor(Color.parseColor("#333333"));

        allGallery.setTextColor(Color.parseColor("#ffffff"));
        photosGallery.setTextColor(Color.parseColor("#333333"));
        videosGallery.setTextColor(Color.parseColor("#ffffff"));
    }
    public void videoTabclick(){
        allGallery.setBackgroundColor(Color.parseColor("#333333"));
        photosGallery.setBackgroundColor(Color.parseColor("#333333"));
        videosGallery.setBackgroundColor(getActivity().getResources().getColor(R.color.yellow));

        allGallery.setTextColor(Color.parseColor("#ffffff"));
        photosGallery.setTextColor(Color.parseColor("#ffffff"));
        videosGallery.setTextColor(Color.parseColor("#333333"));
    }

//    @Override
//    public void onResume() {
//        if(Utilities.isNetworkAvailable(getActivity())) {
//            childMyGalleryArrayList.clear();
//            List<NameValuePair> nameValuePairList = new ArrayList<>();
//            nameValuePairList.add(new BasicNameValuePair("offset", "0"));
//            nameValuePairList.add(new BasicNameValuePair("gallery_type", "image"));
//
//            String webServiceUrl = Utilities.BASE_URL + "children/gallery_list";
//
//            ArrayList<String> headers = new ArrayList<>();
//            headers.add("X-access-uid:"+loggedInUser.getId());
//            headers.add("X-access-token:"+loggedInUser.getToken());
//
//            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_MY_GALLERY, ChildMyGalleryFragment.this, headers);
//            postWebServiceAsync.execute(webServiceUrl);
//        }else{
//            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//        }
//        super.onResume();
//    }
}
