package com.dunvant.application.child;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildMyGalleryBean;
import com.dunvant.application.beans.UserBean;
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

public class ChildVideoHistoryScreen extends AppCompatActivity implements IWebServiceCallback {
    ChildMyGalleryBean childMyGalleryBean;
    ArrayList<ChildMyGalleryBean> childMyGalleryArrayList = new ArrayList<>();
    ChildMyGalleryAdapter childMyGalleryAdapter;
    GridView myGalleryList;
    ImageView backButton;
    private final String GET_MY_GALLERY = "GET_MY_GALLERY";

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_video_history_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        myGalleryList = (GridView) findViewById(R.id.myGalleryList);
        backButton = (ImageView) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if(Utilities.isNetworkAvailable(ChildVideoHistoryScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("offset", "0"));
            nameValuePairList.add(new BasicNameValuePair("gallery_type", "video"));

            String webServiceUrl = Utilities.BASE_URL + "children/gallery_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildVideoHistoryScreen.this, nameValuePairList, GET_MY_GALLERY, ChildVideoHistoryScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);
        }else{
            Toast.makeText(ChildVideoHistoryScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case GET_MY_GALLERY:
                childMyGalleryArrayList.clear();
                if (response == null) {
                    Toast.makeText(ChildVideoHistoryScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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
                                childMyGalleryBean.setFileName(galleryData.getString("file_name"));
                                childMyGalleryBean.setFileType(galleryData.getString("file_type"));
                                childMyGalleryBean.setIsPublic(galleryData.getString("is_public"));
                                childMyGalleryBean.setCreatedAt(galleryData.getString("created_at"));
                                childMyGalleryBean.setCreatedAtFormatted(galleryData.getString("created_at_formatted"));
                                childMyGalleryBean.setFileUrl(galleryData.getString("file_url"));
                                childMyGalleryBean.setLike(galleryData.getString("likes"));
                                childMyGalleryBean.setComments(galleryData.getString("comments"));
                                if(galleryData.has("video_thumbnail")){
                                    childMyGalleryBean.setThumbnailImage(galleryData.getString("video_thumbnail"));
                                }else{
                                    childMyGalleryBean.setThumbnailImage("noThumb");
                                }


                                childMyGalleryArrayList.add(childMyGalleryBean);
                            }

                            childMyGalleryAdapter = new ChildMyGalleryAdapter(ChildVideoHistoryScreen.this,childMyGalleryArrayList);
                            myGalleryList.setAdapter(childMyGalleryAdapter);
                            childMyGalleryAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(ChildVideoHistoryScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildVideoHistoryScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }


        }

    }
}
