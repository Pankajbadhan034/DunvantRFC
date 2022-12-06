package com.dunvant.application.child;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildAllCommentsLikes;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.adapters.ChildCommentsLikesListTimelineAdapter;
import com.dunvant.application.child.fragments.ChildPostFragment;
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

public class ChildAllCommentsReadScreen extends AppCompatActivity implements IWebServiceCallback {
    Dialog dialog;
    Typeface helvetica;
    Typeface linoType;
    Button comments;
    Button likes;
    Button addComment;
    ChildCommentsLikesListTimelineAdapter childCommentsLikesListTimelineAdapter;
    ArrayList<ChildAllCommentsLikes> childAllCommentsLikesArrayList = new ArrayList<>();
    String entityId;
    String entityType;
    String postId;
    String name;
    ImageView backButton;
    ListView list;
    TextView lblTitle;
    private final String GET_COMMENTS = "GET_COMMENTS";
    private final String GET_LIKES = "GET_LIKES";
    private final String POST_COMMENT = "POST_COMMENT";
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    ChildAllCommentsLikes childAllCommentsLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_all_comments_read_screen);

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        backButton = (ImageView) findViewById(R.id.backButton);
        list = (ListView) findViewById(R.id.list);
        lblTitle = (TextView) findViewById(R.id.lblTitle);
        comments = (Button) findViewById(R.id.comments);
        likes = (Button) findViewById(R.id.likes);
        addComment = (Button) findViewById(R.id.addComment);

        entityId = getIntent().getStringExtra("entityId");
        entityType = getIntent().getStringExtra("entityType");
        postId = getIntent().getStringExtra("postId");
        name = getIntent().getStringExtra("name");
        String whichType = getIntent().getStringExtra("whichType");
        String totalLikes = getIntent().getStringExtra("totalLikes");
        String totalComments = getIntent().getStringExtra("totalComments");

        lblTitle.setText(name);

        likes.setText("LIKES (" + totalLikes + ")");
        comments.setText("COMMENTS (" + totalComments + ")");

        if(whichType.equalsIgnoreCase("comments")){
            if (Utilities.isNetworkAvailable(ChildAllCommentsReadScreen.this)) {
                commentTab();
                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(new BasicNameValuePair("entity_id", entityId));
                nameValuePairList.add(new BasicNameValuePair("entity_type", entityType));

                String webServiceUrl = Utilities.BASE_URL + "children/get_comments_list";

                ArrayList<String> headers = new ArrayList<>();
                headers.add("X-access-uid:" + loggedInUser.getId());
                headers.add("X-access-token:" + loggedInUser.getToken());

                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildAllCommentsReadScreen.this, nameValuePairList, GET_COMMENTS, ChildAllCommentsReadScreen.this, headers);
                postWebServiceAsync.execute(webServiceUrl);
            } else {
                Toast.makeText(ChildAllCommentsReadScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
            }
        }

        if(whichType.equalsIgnoreCase("likes")){
            likeTab();
            if (Utilities.isNetworkAvailable(ChildAllCommentsReadScreen.this)) {
                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(new BasicNameValuePair("entity_id", entityId));
                nameValuePairList.add(new BasicNameValuePair("entity_type", entityType));

                String webServiceUrl = Utilities.BASE_URL + "children/get_likes_list";

                ArrayList<String> headers = new ArrayList<>();
                headers.add("X-access-uid:" + loggedInUser.getId());
                headers.add("X-access-token:" + loggedInUser.getToken());

                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildAllCommentsReadScreen.this, nameValuePairList, GET_LIKES, ChildAllCommentsReadScreen.this, headers);
                postWebServiceAsync.execute(webServiceUrl);
            } else {
                Toast.makeText(ChildAllCommentsReadScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
            }
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               likeTab();
                if (Utilities.isNetworkAvailable(ChildAllCommentsReadScreen.this)) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("entity_id", entityId));
                    nameValuePairList.add(new BasicNameValuePair("entity_type", entityType));

                    String webServiceUrl = Utilities.BASE_URL + "children/get_likes_list";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildAllCommentsReadScreen.this, nameValuePairList, GET_LIKES, ChildAllCommentsReadScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(ChildAllCommentsReadScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentTab();
                if (Utilities.isNetworkAvailable(ChildAllCommentsReadScreen.this)) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("entity_id", entityId));
                    nameValuePairList.add(new BasicNameValuePair("entity_type", entityType));

                    String webServiceUrl = Utilities.BASE_URL + "children/get_comments_list";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildAllCommentsReadScreen.this, nameValuePairList, GET_COMMENTS, ChildAllCommentsReadScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(ChildAllCommentsReadScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(ChildAllCommentsReadScreen.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.child_dialog_post_comment);

                final TextView postcommentLabel = (TextView) dialog.findViewById(R.id.postcommentLabel);
                final EditText comment = (EditText) dialog.findViewById(R.id.comment);
                Button submit = (Button) dialog.findViewById(R.id.submit);
                postcommentLabel.setTypeface(helvetica);
                comment.setTypeface(helvetica);
                submit.setTypeface(linoType);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String commentStr = comment.getText().toString();
                        if(commentStr==null || commentStr.isEmpty()){
                            Toast.makeText(ChildAllCommentsReadScreen.this, "Please enter comment", Toast.LENGTH_LONG).show();
                        }else{

                            if(Utilities.isNetworkAvailable(ChildAllCommentsReadScreen.this)) {

                                List<NameValuePair> nameValuePairList = new ArrayList<>();
                                nameValuePairList.add(new BasicNameValuePair("post_id", postId));
                                nameValuePairList.add(new BasicNameValuePair("comment", commentStr));

                                String webServiceUrl = Utilities.BASE_URL + "user_posts/comment";

                                ArrayList<String> headers = new ArrayList<>();
                                headers.add("X-access-uid:"+loggedInUser.getId());
                                headers.add("X-access-token:"+loggedInUser.getToken());

                                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildAllCommentsReadScreen.this, nameValuePairList, POST_COMMENT, ChildAllCommentsReadScreen.this, headers);
                                postWebServiceAsync.execute(webServiceUrl);
                            }else{
                                Toast.makeText(ChildAllCommentsReadScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


                dialog.show();
            }
        });




    }
    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_COMMENTS:
                if (response == null) {
                    Toast.makeText(ChildAllCommentsReadScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            String responseArray=responseObject.getString("data");
                            JSONArray myFriendsArray=new JSONArray(responseArray);
                            //System.out.println("array__" + myFriendsArray);
                            for(int i=0;i<myFriendsArray.length();i++){
                                JSONObject object = myFriendsArray.getJSONObject(i);
                                childAllCommentsLikes = new ChildAllCommentsLikes();
                                childAllCommentsLikes.setId(object.getString("id"));
                                childAllCommentsLikes.setEntityType(object.getString("entity_type"));
                                childAllCommentsLikes.setEntityId(object.getString("entity_id"));
                                childAllCommentsLikes.setCommentedById(object.getString("commented_by_id"));
                                childAllCommentsLikes.setComment(object.getString("comment"));
                                childAllCommentsLikes.setCreatedAtDate(object.getString("created_at"));
                                childAllCommentsLikes.setCommentedDate(object.getString("commented_date"));
                                childAllCommentsLikes.setCommentedTime(object.getString("commented_time"));
                                childAllCommentsLikes.setFullName(object.getString("full_name"));

                                childAllCommentsLikesArrayList.add(childAllCommentsLikes);


                            }

                        } else {
                            Toast.makeText(ChildAllCommentsReadScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                        childCommentsLikesListTimelineAdapter = new ChildCommentsLikesListTimelineAdapter(ChildAllCommentsReadScreen.this,childAllCommentsLikesArrayList,"comments");
                        list.setAdapter(childCommentsLikesListTimelineAdapter);
                        childCommentsLikesListTimelineAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ChildAllCommentsReadScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case POST_COMMENT:
                if (response == null) {
                    Toast.makeText(ChildAllCommentsReadScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            ChildPostFragment.comingFromCreatePost = true;
                            Toast.makeText(ChildAllCommentsReadScreen.this, message, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        }else {
                            dialog.dismiss();
                            Toast.makeText(ChildAllCommentsReadScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(ChildAllCommentsReadScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                break;

            case GET_LIKES:
                if (response == null) {
                    Toast.makeText(ChildAllCommentsReadScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            String responseArray=responseObject.getString("data");
                            JSONArray myFriendsArray=new JSONArray(responseArray);
                            //System.out.println("array__" + myFriendsArray);
                            for(int i=0;i<myFriendsArray.length();i++){
                                JSONObject object = myFriendsArray.getJSONObject(i);
                                childAllCommentsLikes = new ChildAllCommentsLikes();
                                childAllCommentsLikes.setId(object.getString("id"));
                                childAllCommentsLikes.setEntityType(object.getString("entity_type"));
                                childAllCommentsLikes.setEntityId(object.getString("entity_id"));
                                childAllCommentsLikes.setLikedById(object.getString("liked_by_id"));
                                childAllCommentsLikes.setCreatedAtDate(object.getString("created_at"));
                                childAllCommentsLikes.setCommentedDate(object.getString("commented_date"));
                                childAllCommentsLikes.setCommentedTime(object.getString("commented_time"));
                                childAllCommentsLikes.setFullName(object.getString("full_name"));

                                childAllCommentsLikesArrayList.add(childAllCommentsLikes);


                            }

                        } else {
                            Toast.makeText(ChildAllCommentsReadScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                        childCommentsLikesListTimelineAdapter = new ChildCommentsLikesListTimelineAdapter(ChildAllCommentsReadScreen.this,childAllCommentsLikesArrayList,"likes");
                        list.setAdapter(childCommentsLikesListTimelineAdapter);
                        childCommentsLikesListTimelineAdapter.notifyDataSetChanged();
                    } catch (Exception e) {

                    }

                    break;



                }
        }
    }

    public void likeTab(){
        childAllCommentsLikesArrayList.clear();
        likes.setBackgroundColor(getResources().getColor(R.color.yellow));
        comments.setBackgroundColor(Color.parseColor("#333333"));
        likes.setTextColor(Color.parseColor("#333333"));
        comments.setTextColor(Color.parseColor("#FFFFFF"));
    }

    public void commentTab(){
        childAllCommentsLikesArrayList.clear();
        likes.setBackgroundColor(Color.parseColor("#333333"));
        comments.setBackgroundColor(getResources().getColor(R.color.yellow));
        likes.setTextColor(Color.parseColor("#FFFFFF"));
        comments.setTextColor(Color.parseColor("#333333"));
    }

}
