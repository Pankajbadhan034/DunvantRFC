package com.dunvant.application.child;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildChooseSubMuscBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.adapters.ChildChooseSubMusicAdapter;
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

public class ChildChooseSubMusicScreen extends AppCompatActivity implements IWebServiceCallback {
    MediaPlayer mediaPlayer;
    String category_name;
    String sub_category_id;
    ListView subMusicList;
    ImageView backButton;
    TextView lblTitle;
    String galleryPath;
    String subCategoryUrl;

    private final String GET_SUB_MUSIC = "GET_SUB_MUSIC";
    ChildChooseSubMuscBean childChooseSubMuscBean;
    ArrayList<ChildChooseSubMuscBean> childChooseSubMuscBeanArrayList = new ArrayList<>();
    ChildChooseSubMusicAdapter childChooseSubMusicAdapter;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_choose_sub_music_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        subMusicList = (ListView) findViewById(R.id.subMusicList);
        backButton = (ImageView) findViewById(R.id.backButton);
        lblTitle = (TextView) findViewById(R.id.lblTitle);

        category_name = getIntent().getStringExtra("category_name");
        sub_category_id = getIntent().getStringExtra("category_id");
        galleryPath = getIntent().getStringExtra("galleryPath");
      //  subCategoryUrl = getIntent().getStringExtra("subCategoryUrl");

        lblTitle.setText(category_name);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    childChooseSubMusicAdapter.stopMedia();
                }catch (Exception e){

                }
                finish();
            }
        });

        if (Utilities.isNetworkAvailable(ChildChooseSubMusicScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("category_id", sub_category_id));

            String webServiceUrl = Utilities.BASE_URL + "music_library/music_files_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildChooseSubMusicScreen.this, nameValuePairList, GET_SUB_MUSIC , ChildChooseSubMusicScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);
        } else {
            Toast.makeText(ChildChooseSubMusicScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case GET_SUB_MUSIC:
                if (response == null) {
                    Toast.makeText(ChildChooseSubMusicScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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
                                JSONObject subData = musicArray.getJSONObject(i);
                                childChooseSubMuscBean = new ChildChooseSubMuscBean();

                                childChooseSubMuscBean.setLocalCheck("0");
                                childChooseSubMuscBean.setId(subData.getString("id"));
                                childChooseSubMuscBean.setCategoryId(subData.getString("category_id"));
                                childChooseSubMuscBean.setSubCategoryId(subData.getString("sub_category_id"));
                                childChooseSubMuscBean.setAcademiesId(subData.getString("academies_id"));
                                childChooseSubMuscBean.setTitle(subData.getString("title"));
                                childChooseSubMuscBean.setSubTitle(subData.getString("sub_title"));
                                childChooseSubMuscBean.setThumbnailId(subData.getString("thumbnail_id"));
                                childChooseSubMuscBean.setSongId(subData.getString("song_id"));
                                childChooseSubMuscBean.setCreatedAt(subData.getString("created_at"));
                                childChooseSubMuscBean.setState(subData.getString("state"));
                                childChooseSubMuscBean.setSongType(subData.getString("song_type"));
                                childChooseSubMuscBean.setSongUrl(subData.getString("song_url"));
                                childChooseSubMuscBean.setThumbnailType(subData.getString("thumbnail_type"));
                                childChooseSubMuscBean.setThumbnailUrl(subData.getString("thumbnail_url"));

                                childChooseSubMuscBeanArrayList.add(childChooseSubMuscBean);
                            }
                            childChooseSubMusicAdapter = new ChildChooseSubMusicAdapter(mediaPlayer, ChildChooseSubMusicScreen.this, childChooseSubMuscBeanArrayList, galleryPath);
                            subMusicList.setAdapter(childChooseSubMusicAdapter);

                        } else {
                            Toast.makeText(ChildChooseSubMusicScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildChooseSubMusicScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            childChooseSubMusicAdapter.stopMedia();
        }catch (Exception e){

        }
    }


}
