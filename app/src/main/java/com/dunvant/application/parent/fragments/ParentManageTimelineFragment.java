package com.dunvant.application.parent.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.PostBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.ParentCreatePostScreen;
import com.dunvant.application.parent.adapters.ParentPostsAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParentManageTimelineFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Typeface helvetica;
    Typeface linoType;

    Button createNewPost;
    ListView postsListView;

    private final String GET_POSTS_LISTING = "GET_POSTS_LISTING";

    ArrayList<PostBean> postsList = new ArrayList<>();
    ParentPostsAdapter parentPostsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        parentPostsAdapter = new ParentPostsAdapter(getActivity(), postsList, ParentManageTimelineFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_manage_timeline, container, false);

        createNewPost = (Button) view.findViewById(R.id.createNewPost);
        postsListView = (ListView) view.findViewById(R.id.postsListView);
        postsListView.setAdapter(parentPostsAdapter);

        createNewPost.setTypeface(linoType);

        createNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createPost = new Intent(getActivity(), ParentCreatePostScreen.class);
                startActivity(createPost);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPostsListing();
    }

    public void getPostsListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "user_posts/status_list?offset=0";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_POSTS_LISTING, ParentManageTimelineFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_POSTS_LISTING:

                postsList.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        JSONArray dataArray = responseObject.getJSONArray("data");
                        PostBean postBean;
                        for(int i=0;i<dataArray.length();i++) {
                            JSONObject postObject = dataArray.getJSONObject(i);
                            postBean = new PostBean();

                            postBean.setPostId(postObject.getString("post_id"));
                            postBean.setTitle(postObject.getString("title"));
                            postBean.setPstatus(postObject.getString("pstatus"));
                            postBean.setPostedDate(postObject.getString("posted_date"));
                            postBean.setFileName(postObject.getString("file_name"));
                            postBean.setFilePath(postObject.getString("file_path"));
                            postBean.setFileType(postObject.getString("file_type"));
                            postBean.setPostedBy(postObject.getString("posted_by"));
                            postBean.setPostedOn(postObject.getString("posted_on"));
                            postBean.setStatusLabel(postObject.getString("status_label"));
                            postBean.setVideoThumb(postObject.getString("video_thumb"));

                            postsList.add(postBean);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentPostsAdapter.notifyDataSetChanged();
                break;
        }
    }
}