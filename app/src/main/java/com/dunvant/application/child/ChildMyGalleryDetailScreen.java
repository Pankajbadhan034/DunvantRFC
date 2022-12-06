package com.dunvant.application.child;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildMyGalleryComments;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.adapters.ChildMyGalleryDetailAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildMyGalleryDetailScreen extends AppCompatActivity implements IWebServiceCallback {
    private TextView title;
    private TextView dubaiAcademyLabel;
    private ImageView backButton;
    private ImageView galleryPic;
    private TextView likeGallery;
    private TextView commentGallery;
    private TextView dateGallery;
    private Button commentTab;
    private Button likeTab;
    private ListView list;
    String type,url,like,comment,date,siteMediaId;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    private final String GET_COMMENTS = "GET_COMMENTS";
    private final String GET_LIKES = "GET_LIKES";
    ChildMyGalleryComments childMyGalleryComments;
    ArrayList<ChildMyGalleryComments> childMyGalleryCommentsArrayList = new ArrayList<>();
    ChildMyGalleryDetailAdapter  childMyGalleryDetailAdapter;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    Typeface helvetica;
    Typeface linoType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_my_gallery_detail_screen);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        title = (TextView) findViewById(R.id.title);
        dubaiAcademyLabel = (TextView) findViewById(R.id.dubaiAcademyLabel);
        backButton = (ImageView) findViewById(R.id.backButton);
        galleryPic = (ImageView) findViewById(R.id.galleryPic);
        likeGallery = (TextView) findViewById(R.id.likeGallery);
        commentGallery = (TextView) findViewById(R.id.commentGallery);
        dateGallery = (TextView) findViewById(R.id.dateGallery);
        commentTab = (Button) findViewById(R.id.commentTab);
        likeTab = (Button) findViewById(R.id.likeTab);
        list = (ListView) findViewById(R.id.list);

        title.setTypeface(linoType);
        dubaiAcademyLabel.setTypeface(helvetica);
        likeGallery.setTypeface(helvetica);
        commentGallery.setTypeface(helvetica);
        dateGallery.setTypeface(helvetica);
        commentTab.setTypeface(helvetica);
        likeTab.setTypeface(helvetica);

        imageLoader.init(ImageLoaderConfiguration.createDefault(ChildMyGalleryDetailScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        type = getIntent().getStringExtra("type");
        url = getIntent().getStringExtra("url");
        like = getIntent().getStringExtra("like");
        comment = getIntent().getStringExtra("comment");
        date = getIntent().getStringExtra("date");
        siteMediaId = getIntent().getStringExtra("siteMediaId");

        if(type.contains("image")){
            imageLoader.displayImage(url, galleryPic, options);
        } else if (type.contains("video")){
            galleryPic.setBackgroundResource(R.drawable.play);
        }

        likeGallery.setText(like);
        commentGallery.setText(comment);
        dateGallery.setText(date);

        if (Utilities.isNetworkAvailable(ChildMyGalleryDetailScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("site_media_id", siteMediaId));

            String webServiceUrl = Utilities.BASE_URL + "children/gallery_comments_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyGalleryDetailScreen.this, nameValuePairList, GET_COMMENTS, ChildMyGalleryDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);
        } else {
            Toast.makeText(ChildMyGalleryDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }

        galleryPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(ChildMyGalleryDetailScreen.this, ChildMyGallerySingleImageScreen.class);
                obj.putExtra("type", type);
                obj.putExtra("url", url);
                obj.putExtra("like", like);
                obj.putExtra("comment", comment);
                obj.putExtra("date", date);
                obj.putExtra("siteMediaId", siteMediaId);
                startActivity(obj);
            }
        });

        commentTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentTb();

                if (Utilities.isNetworkAvailable(ChildMyGalleryDetailScreen.this)) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("site_media_id", siteMediaId));

                    String webServiceUrl = Utilities.BASE_URL + "children/gallery_comments_list";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyGalleryDetailScreen.this, nameValuePairList, GET_COMMENTS, ChildMyGalleryDetailScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(ChildMyGalleryDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }


            }
        });

        likeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeTb();
                if (Utilities.isNetworkAvailable(ChildMyGalleryDetailScreen.this)) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("site_media_id", siteMediaId));

                    String webServiceUrl = Utilities.BASE_URL + "children/gallery_likes_list";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyGalleryDetailScreen.this, nameValuePairList, GET_LIKES, ChildMyGalleryDetailScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(ChildMyGalleryDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case GET_COMMENTS:
//                response = "{\n" +
//                        "\t\"status\": true,\n" +
//                        "\t\"message\": \"Record Found.\",\n" +
//                        "\t\"data\": [{\n" +
//                        "\t\t\"id\": \"1\",\n" +
//                        "\t\t\"entity_type\": \"image\",\n" +
//                        "\t\t\"entity_id\": \"155\",\n" +
//                        "\t\t\"commented_by_id\": \"4\",\n" +
//                        "\t\t\"comment\": \"my comment\",\n" +
//                        "\t\t\"created_at\": \"2016-12-28 17:21:43\",\n" +
//                        "\t\t\"commented_date\": \"28 December 2016\",\n" +
//                        "\t\t\"commented_time\": \"05:21 PM\",\n" +
//                        "\t\t\"full_name\": \"bacha three\"\n" +
//                        "\t}]\n" +
//                        "}";


                childMyGalleryCommentsArrayList.clear();
                if (response == null) {
                    Toast.makeText(ChildMyGalleryDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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
                                childMyGalleryComments = new ChildMyGalleryComments();
                                childMyGalleryComments.setId(galleryData.getString("id"));
                                childMyGalleryComments.setEntityType(galleryData.getString("entity_type"));
                                childMyGalleryComments.setEntitiyId(galleryData.getString("entity_id"));
                                childMyGalleryComments.setCommentedById(galleryData.getString("commented_by_id"));
                                childMyGalleryComments.setComment(galleryData.getString("comment"));
                                childMyGalleryComments.setCreatedAt(galleryData.getString("created_at"));
                                childMyGalleryComments.setCommentedDate(galleryData.getString("commented_date"));
                                childMyGalleryComments.setCommentedTime(galleryData.getString("commented_time"));
                                childMyGalleryComments.setFullName(galleryData.getString("full_name"));

                                childMyGalleryCommentsArrayList.add(childMyGalleryComments);
                            }
                        }else{
                            Toast.makeText(ChildMyGalleryDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                        childMyGalleryDetailAdapter = new ChildMyGalleryDetailAdapter(ChildMyGalleryDetailScreen.this,childMyGalleryCommentsArrayList,"comments");
                        list.setAdapter(childMyGalleryDetailAdapter);
                        childMyGalleryDetailAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildMyGalleryDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;
            case GET_LIKES:
//                response = "{\n" +
//                        "\t\"status\": true,\n" +
//                        "\t\"message\": \"Record Found.\",\n" +
//                        "\t\"data\": [{\n" +
//                        "\t\t\"id\": \"1\",\n" +
//                        "\t\t\"entity_type\": \"image\",\n" +
//                        "\t\t\"entity_id\": \"155\",\n" +
//                        "\t\t\"commented_by_id\": \"4\",\n" +
//                        "\t\t\"comment\": \"my comment\",\n" +
//                        "\t\t\"created_at\": \"2016-12-28 17:21:43\",\n" +
//                        "\t\t\"commented_date\": \"28 December 2017\",\n" +
//                        "\t\t\"commented_time\": \"05:21 PM\",\n" +
//                        "\t\t\"full_name\": \"test\"\n" +
//                        "\t}]\n" +
//                        "}";


                childMyGalleryCommentsArrayList.clear();
                if (response == null) {
                    Toast.makeText(ChildMyGalleryDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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
                                childMyGalleryComments = new ChildMyGalleryComments();
                                childMyGalleryComments.setId(galleryData.getString("id"));
                                childMyGalleryComments.setEntityType(galleryData.getString("entity_type"));
                                childMyGalleryComments.setEntitiyId(galleryData.getString("entity_id"));
                                childMyGalleryComments.setCommentedById(galleryData.getString("liked_by_id"));
                                //childMyGalleryComments.setComment(galleryData.getString("comment"));
                                childMyGalleryComments.setCreatedAt(galleryData.getString("created_at"));
                                childMyGalleryComments.setCommentedDate(galleryData.getString("commented_date"));
                                childMyGalleryComments.setCommentedTime(galleryData.getString("commented_time"));
                                childMyGalleryComments.setFullName(galleryData.getString("full_name"));

                                childMyGalleryCommentsArrayList.add(childMyGalleryComments);
                            }

                        }else{
                              Toast.makeText(ChildMyGalleryDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                        childMyGalleryDetailAdapter = new ChildMyGalleryDetailAdapter(ChildMyGalleryDetailScreen.this,childMyGalleryCommentsArrayList,"likes");
                        list.setAdapter(childMyGalleryDetailAdapter);
                        childMyGalleryDetailAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildMyGalleryDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;


        }

    }

    public void commentTb(){
        commentTab.setBackgroundColor(getResources().getColor(R.color.yellow));
        commentTab.setTextColor(Color.parseColor("#333333"));
        likeTab.setBackgroundColor(Color.parseColor("#333333"));
        likeTab.setTextColor(Color.parseColor("#ffffff"));
    }
    public void likeTb(){
        likeTab.setBackgroundColor(getResources().getColor(R.color.yellow));
        likeTab.setTextColor(Color.parseColor("#333333"));
        commentTab.setBackgroundColor(Color.parseColor("#333333"));
        commentTab.setTextColor(Color.parseColor("#ffffff"));
    }
}
