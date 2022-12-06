package com.dunvant.application.child;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildChooseMusicCategoryBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.adapters.ChildChooseMusicAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChildChooseMusicScreen extends AppCompatActivity implements IWebServiceCallback {
    String galleryPath;
    GridView musicGrid;
    ImageView backButton;
    private final String GET_MUSIC = "GET_MUSIC";
    ChildChooseMusicCategoryBean childChooseMusicCategoryBean;
    ArrayList<ChildChooseMusicCategoryBean> childChooseMusicCategoryBeanArrayList = new ArrayList<>();
    ChildChooseMusicAdapter childChooseMusicAdapter;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_choose_music_screen);
        galleryPath = getIntent().getStringExtra("galleryPath");
        //System.out.println("GalleryPath::"+galleryPath);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        musicGrid = (GridView) findViewById(R.id.musicGrid);
        backButton = (ImageView) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(Utilities.isNetworkAvailable(ChildChooseMusicScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "music_library/categories_list";
            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            if(Utilities.isNetworkAvailable(ChildChooseMusicScreen.this)) {
                GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ChildChooseMusicScreen.this, GET_MUSIC, ChildChooseMusicScreen.this, headers);
                getWebServiceWithHeadersAsync.execute(webServiceUrl);
            }else{
                Toast.makeText(ChildChooseMusicScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(ChildChooseMusicScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case GET_MUSIC:
                if (response == null) {
                    Toast.makeText(ChildChooseMusicScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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

                            JSONArray musicArray=new JSONArray(responseObject.getString("data"));

                            for(int i = 0; i < musicArray.length(); i++){
                                JSONObject musicData = musicArray.getJSONObject(i);
                                childChooseMusicCategoryBean = new ChildChooseMusicCategoryBean();

                                childChooseMusicCategoryBean.setCategoryId(musicData.getString("category_id"));
                                childChooseMusicCategoryBean.setCategoryName(musicData.getString("category_name"));
                                childChooseMusicCategoryBean.setCategoryDescription(musicData.getString("category_description"));
                                childChooseMusicCategoryBean.setCategoryUrl(musicData.getString("category_file_path"));
                                childChooseMusicCategoryBean.setSiteMediaId(musicData.getString("site_media_id"));

//                                JSONArray subMusicArray = new JSONArray(musicData.getString("sub_category_details"));
//                                for(int j=0; j<subMusicArray.length(); j++){
//                                    JSONObject subMusicData = subMusicArray.getJSONObject(j);
//                                    childChooseMusicCategoryBean.setSubCategoryId(subMusicData.getString("sub_category_id"));
//                                    childChooseMusicCategoryBean.setSubCategoryName(subMusicData.getString("sub_category_name"));
//                                    childChooseMusicCategoryBean.setSubCategoryUrl(subMusicData.getString("subcategory_file_path"));
//                                    childChooseMusicCategoryBean.setSubCategoryDescription(subMusicData.getString("sub_category_description"));
//                                }

                                childChooseMusicCategoryBeanArrayList.add(childChooseMusicCategoryBean);
                            }

                            childChooseMusicAdapter = new ChildChooseMusicAdapter(ChildChooseMusicScreen.this, childChooseMusicCategoryBeanArrayList, galleryPath);
                            musicGrid.setAdapter(childChooseMusicAdapter);
                        } else {
                            Toast.makeText(ChildChooseMusicScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildChooseMusicScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }


        }

    }
}
