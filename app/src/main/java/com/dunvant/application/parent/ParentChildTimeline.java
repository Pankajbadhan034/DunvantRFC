package com.dunvant.application.parent;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.ChildPostAdditionalTimelineDataBean;
import com.dunvant.application.beans.ChildPostsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentChildPostAdapter;
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

import androidx.appcompat.app.AppCompatActivity;

public class ParentChildTimeline extends AppCompatActivity implements IWebServiceCallback {
    TextView title;
    boolean isExpiredChallenge=false;
    ArrayList<ChildPostsBean> childPostsBeanArrayList = new ArrayList<>();
    ChildPostsBean childPostsBean;
    SwipyRefreshLayout mSwipyRefreshLayout;
    int offset;

    ImageView backButton;
    ListView postList;
    ImageView viewOtherOptions;
    ImageView createPost;


    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    ChildBean currentChild;
    String type;

    private final String CHILD_POST = "CHILD_POST";
    ParentChildPostAdapter parentChildPostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_child_timeline);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        Intent intent = getIntent();
        if (intent != null) {
            currentChild = (ChildBean) intent.getSerializableExtra("currentChild");
            type = intent.getStringExtra("type");
            // getTimeline();
        }

        backButton = (ImageView) findViewById(R.id.backButton);
        postList = (ListView) findViewById(R.id.postList);
        viewOtherOptions = (ImageView) findViewById(R.id.viewOtherOptions);
        title = (TextView) findViewById(R.id.title);
        createPost = (ImageView) findViewById(R.id.createPost);

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createPost = new Intent(ParentChildTimeline.this, ParentCreatePostScreen.class);
                startActivity(createPost);
            }
        });

        title.setText(currentChild.getFullName());

        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);

        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                offset += 1;
                getTimeline();
            }
        });

        parentChildPostAdapter = new ParentChildPostAdapter(ParentChildTimeline.this, childPostsBeanArrayList, ParentChildTimeline.this, currentChild.getId());
        postList.setAdapter(parentChildPostAdapter);

        viewOtherOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ParentChildTimeline.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.parent_dialog_view_options);
                dialog.getWindow().setGravity(Gravity.BOTTOM);

                TextView viewAttendance = (TextView) dialog.findViewById(R.id.viewAttendance);
                TextView viewMarks = (TextView) dialog.findViewById(R.id.viewMarks);
                TextView childrenActivities = (TextView) dialog.findViewById(R.id.childrenActivities);
                TextView manageChallenge = (TextView) dialog.findViewById(R.id.manageChallenge);
                TextView viewMidWeekAttendance = (TextView) dialog.findViewById(R.id.viewMidWeekAttendance);

                String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                childrenActivities.setText("View "+verbiage_singular+" Activities");
                viewMarks.setText(verbiage_singular+" Reports");

                viewMidWeekAttendance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ParentChildTimeline.this, ParentViewMidweekAttendanceScreen.class);
                        intent.putExtra("currentChild", currentChild);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                viewAttendance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ParentChildTimeline.this, ParentViewAttendanceScreen.class);
                        intent.putExtra("currentChild", currentChild);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                viewMarks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ParentChildTimeline.this, ParentViewMarksScreen.class);
                        intent.putExtra("currentChild", currentChild);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                childrenActivities.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ParentChildTimeline.this, ParentViewChildrenActivitiesScreen.class);
                        intent.putExtra("currentChild", currentChild);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                manageChallenge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent manageChallenges = new Intent(ParentChildTimeline.this, ParentManageChallengesScreen.class);
                        startActivity(manageChallenges);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getTimeline() {
        if (Utilities.isNetworkAvailable(ParentChildTimeline.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("child_id", currentChild.getId()));
            nameValuePairList.add(new BasicNameValuePair("offset", offset + ""));

            if (type.equalsIgnoreCase("career")) {
                nameValuePairList.add(new BasicNameValuePair("feed_type", "ifa_feeds"));
                // title.setText("PLAYER CAREER");
            } else {
                // title.setText("PLAYER NEWSFEED");
            }

            String webServiceUrl = Utilities.BASE_URL + "user_posts/child_timeline";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentChildTimeline.this, nameValuePairList, CHILD_POST, ParentChildTimeline.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentChildTimeline.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case CHILD_POST:

                if (response == null) {
                    Toast.makeText(ParentChildTimeline.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
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

                            JSONArray dataArray = new JSONArray(responseObject.getString("data"));

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                childPostsBean = new ChildPostsBean();

                                if (dataObject.has("id")) {
                                    childPostsBean.setId(dataObject.getString("id").trim());

                                    childPostsBean.setState(dataObject.getString("state"));
                                    childPostsBean.setEntityId(dataObject.getString("entity_id"));
                                    childPostsBean.setCreatedAtDate(dataObject.getString("created_at"));
                                    childPostsBean.setPostedDateFormatted(dataObject.getString("posted_date_formatted"));
                                    childPostsBean.setPostedTimeFormatted(dataObject.getString("posted_time_formatted"));
                                    childPostsBean.setPostType(dataObject.getString("post_type"));
                                }

                                childPostsBean.setUserId(dataObject.getString("users_id"));
                                childPostsBean.setActivity(dataObject.getString("activity"));

                                String additionalData = dataObject.getString("additional_data");
                                JSONObject additionalDataObj = new JSONObject(additionalData);
                                String challengeData;
                                JSONObject challengeDataObj;

                                if (additionalDataObj.has("friend_suggestions")) {
                                    challengeData = additionalDataObj.getString("friend_suggestions");
                                    challengeDataObj = new JSONObject(challengeData);
                                    //System.out.println("friend_suggestions::" + challengeData);

                                    childPostsBean.setLocalType("friend_suggestions");
                                    childPostsBean.setChallengePostDateTime(challengeDataObj.getString("post_datetime"));
                                    childPostsBean.setSuggestions(challengeDataObj.getString("suggestions"));
                                    childPostsBean.setActionPerformedProfilePicUrl(challengeDataObj.getString("action_performed_profile_pic_url"));
                                    childPostsBean.setActionPerformFullName(challengeDataObj.getString("action_performed_fullname"));

                                    String postEntityDetailData = challengeDataObj.getString("post_entity_detail");
                                    JSONObject postEntityDetailDataObj = new JSONObject(postEntityDetailData);

                                    childPostsBean.setPostEntityTitle(postEntityDetailDataObj.getString("title"));
                                    childPostsBean.setFileName(postEntityDetailDataObj.getString("file_name"));
                                    childPostsBean.setFileType(postEntityDetailDataObj.getString("file_type"));
                                    childPostsBean.setIsPublic(postEntityDetailDataObj.getString("is_public"));
                                    childPostsBean.setMediaDescription(postEntityDetailDataObj.getString("media_description"));
                                    childPostsBean.setFileUrl(postEntityDetailDataObj.getString("file_url"));
                                    childPostsBean.setSiteMediaId(postEntityDetailDataObj.getString("site_media_id"));

                                    childPostsBean.setChallengePostParentId("1");
                                   // childPostsBeanArrayList.add(childPostsBean);
                                } else if (additionalDataObj.has("challenge")) {

                                    challengeData = additionalDataObj.getString("challenge");
                                    challengeDataObj = new JSONObject(challengeData);
                                    //System.out.println("challengeData::" + challengeData);

                                    childPostsBean.setLocalType("challenge");
                                    childPostsBean.setChallengeEntityId(challengeDataObj.getString("entity_id"));
                                    childPostsBean.setChallengeAction(challengeDataObj.getString("action"));
                                    childPostsBean.setChallengeActionPerformedId(challengeDataObj.getString("action_performed_by_id"));
                                    childPostsBean.setChallengeActionAgainstId(challengeDataObj.getString("action_against_by_id"));
                                    childPostsBean.setChallengePostDateTime(challengeDataObj.getString("post_datetime"));
                                    childPostsBean.setChallengePostParentId(challengeDataObj.getString("post_parent_id"));
                                    childPostsBean.setChallengeIsShared(challengeDataObj.getString("is_shared"));
                                    childPostsBean.setChallengeChallengeStatus(challengeDataObj.getString("challenge_status"));
                                    childPostsBean.setChallengeResultId(challengeDataObj.getString("challenge_result_id"));
                                    childPostsBean.setChallengePostOwnerId(challengeDataObj.getString("post_owner_id"));
                                    childPostsBean.setIsLikeByYou(challengeDataObj.getString("is_liked_by_you"));
                                    childPostsBean.setEntityLikes(challengeDataObj.getString("entity_likes"));
                                    childPostsBean.setEntityComments(challengeDataObj.getString("entity_comments"));
                                    childPostsBean.setActionPerformedProfilePicUrl(challengeDataObj.getString("action_performed_profile_pic_url"));
                                    childPostsBean.setActionPerformFullName(challengeDataObj.getString("action_performed_fullname"));

                                    String postEntityDetailData = challengeDataObj.getString("post_entity_detail");
                                    JSONObject postEntityDetailDataObj = new JSONObject(postEntityDetailData);

                                    childPostsBean.setPostEntitiyId(postEntityDetailDataObj.getString("id"));
                                    childPostsBean.setPostEntityImageId(postEntityDetailDataObj.getString("image_id"));
                                    childPostsBean.setPostEntityVideoId(postEntityDetailDataObj.getString("video_id"));
                                    childPostsBean.setPostEntityOwnerId(postEntityDetailDataObj.getString("owner_id"));
                                    childPostsBean.setPostEntityCategoryId(postEntityDetailDataObj.getString("category_id"));
                                    childPostsBean.setPostEntityTitle(postEntityDetailDataObj.getString("title"));
                                    childPostsBean.setPostEntityDescription(postEntityDetailDataObj.getString("description"));
                                    childPostsBean.setPostEntityTargetScore(postEntityDetailDataObj.getString("target_score"));
                                    childPostsBean.setPostEntityTargetTime(postEntityDetailDataObj.getString("target_time"));
                                    childPostsBean.setPostEntityTargetTimeType(postEntityDetailDataObj.getString("target_time_type"));
                                    childPostsBean.setPostEntityExpiration(postEntityDetailDataObj.getString("expiration"));
                                    childPostsBean.setPostEntityApprovalRequired(postEntityDetailDataObj.getString("approval_required"));
                                    childPostsBean.setPostEntityState(postEntityDetailDataObj.getString("state"));
                                    childPostsBean.setPostEntityIsGlobal(postEntityDetailDataObj.getString("is_global"));
                                    childPostsBean.setPostEntityCreatedAt(postEntityDetailDataObj.getString("created_at"));

                                    if(postEntityDetailDataObj.has("youtube_url")){
                                        childPostsBean.setYoutubeUrl(postEntityDetailDataObj.getString("youtube_url"));
                                        childPostsBean.setYouTubeEmbedId(postEntityDetailDataObj.getString("youtube_embed_id"));
                                    }else{
                                        childPostsBean.setYoutubeUrl("");
                                        childPostsBean.setYouTubeEmbedId("");
                                    }

                                    childPostsBean.setPostEntityExpiration(postEntityDetailDataObj.getString("expiration_formatted"));
                                    childPostsBean.setPostEntityExpirationDateFormatted(postEntityDetailDataObj.getString("expiration_date_formatted"));
                                    childPostsBean.setPostEntityExpirationDate(postEntityDetailDataObj.getString("expiration_date"));
                                    childPostsBean.setPostEntityExpirationTime(postEntityDetailDataObj.getString("expiration_time"));
                                    childPostsBean.setPostEntityExpirationTimeFormatted(postEntityDetailDataObj.getString("expiration_time_formatted"));
                                    childPostsBean.setPostEntityExpirationLocationIds(postEntityDetailDataObj.getString("locations_ids"));
                                    childPostsBean.setPostEntityExpirationLocationName(postEntityDetailDataObj.getString("location_name"));
                                    childPostsBean.setPostEntityExpirationSessionIds(postEntityDetailDataObj.getString("sessions_ids"));
                                    childPostsBean.setPostEntityGroupName(postEntityDetailDataObj.getString("group_names"));
                                    childPostsBean.setPostEntityDays(postEntityDetailDataObj.getString("days"));
                                    childPostsBean.setPostEntityChallengeImageUrl(postEntityDetailDataObj.getString("challenge_image_url"));
                                    childPostsBean.setPostEntityChallengeVideoUrl(postEntityDetailDataObj.getString("challenge_video_url"));
                                    childPostsBean.setPostEntityCategoryName(postEntityDetailDataObj.getString("category_name"));
                                    childPostsBean.setPostEntityTargetTimeTypeFormatted(postEntityDetailDataObj.getString("target_time_type_formatted"));
                                    childPostsBean.setPostEntityAcceptedDateFormatted(postEntityDetailDataObj.getString("accepted_date_formatted"));
                                    childPostsBean.setPostEntityChallengeResult(postEntityDetailDataObj.getString("challenge_result"));
                                    childPostsBean.setPostEntityAchievedScore(postEntityDetailDataObj.getString("achieved_score"));
                                    childPostsBean.setPostEntityAchievedTime(postEntityDetailDataObj.getString("achieved_time"));
                                    childPostsBean.setPostEntityAchievedDate(postEntityDetailDataObj.getString("achieved_date"));
                                    childPostsBean.setPostEntityAchievedDateFormatted(postEntityDetailDataObj.getString("achieved_date_formatted"));
                                    childPostsBean.setApprovedRequiredBy(postEntityDetailDataObj.getString("approval_required_by"));

                                    if(challengeDataObj.getString("challenge_status").equalsIgnoreCase("set_challenge")){
                                        if(postEntityDetailDataObj.getString("is_expired").equalsIgnoreCase("true")){
                                            isExpiredChallenge=true;
                                        }else{
                                            isExpiredChallenge=false;
                                        }
                                    }else{
                                        isExpiredChallenge=false;
                                    }


//                                    if(postEntityDetailDataObj.getString("is_expired").equalsIgnoreCase("true")){
////                                        isExpiredChallenge=true;
//                                    }else{
////                                        isExpiredChallenge=false;
//                                        childPostsBeanArrayList.add(childPostsBean);
//                                    }
                                    if(postEntityDetailDataObj.has("file_url")){
                                        childPostsBean.setFileUrl(postEntityDetailDataObj.getString("file_url"));
                                        childPostsBean.setFileType(postEntityDetailDataObj.getString("file_type"));
                                    }else{
                                        childPostsBean.setFileUrl("");
                                        childPostsBean.setFileType("");
                                    }
                                    if(postEntityDetailDataObj.has("video_thumbnail")){
                                        childPostsBean.setVideoThumbnail(postEntityDetailDataObj.getString("video_thumbnail"));
                                    }
                                    if (challengeDataObj.has("approval_status")) {
                                        childPostsBean.setApprovalStatus(challengeDataObj.getString("approval_status"));
                                    } else {
                                        childPostsBean.setApprovalStatus("noValueApproval");
                                    }

                                }
//                                 else if (additionalDataObj.has("workout")) {
//                                    challengeData = additionalDataObj.getString("workout");
//                                    challengeDataObj = new JSONObject(challengeData);
//                                    //System.out.println("workOutData::"+challengeData);
//                                    childPostsBean.setLocalType("workout");
//                                    childPostsBean.setChallengeEntityId(challengeDataObj.getString("entity_id"));
//                                    childPostsBean.setChallengeAction(challengeDataObj.getString("action"));
//                                    childPostsBean.setChallengeActionPerformedId(challengeDataObj.getString("action_performed_by_id"));
//                                    childPostsBean.setChallengeActionAgainstId(challengeDataObj.getString("action_against_by_id"));
//                                    childPostsBean.setChallengePostDateTime(challengeDataObj.getString("post_datetime"));
//                                    childPostsBean.setChallengePostParentId(challengeDataObj.getString("post_parent_id"));
//                                    childPostsBean.setChallengeIsShared(challengeDataObj.getString("is_shared"));
//                                    childPostsBean.setChallengeChallengeStatus(challengeDataObj.getString("achieved_by"));
//                                    childPostsBean.setChallengePostOwnerId(challengeDataObj.getString("post_owner_id"));
//                                    childPostsBean.setIsLikeByYou(challengeDataObj.getString("is_liked_by_you"));
//                                    childPostsBean.setEntityLikes(challengeDataObj.getString("entity_likes"));
//                                    childPostsBean.setEntityComments(challengeDataObj.getString("entity_comments"));
//                                    childPostsBean.setActionPerformedProfilePicUrl(challengeDataObj.getString("action_performed_profile_pic_url"));
//                                    childPostsBean.setActionPerformFullName(challengeDataObj.getString("action_performed_fullname"));
//
//                                }
                                else if (additionalDataObj.has("workout")) {
                                    challengeData = additionalDataObj.getString("workout");
                                    challengeDataObj = new JSONObject(challengeData);
                                    //System.out.println("workOutData::" + challengeData);
                                    childPostsBean.setLocalType("workout");
                                    childPostsBean.setChallengeEntityId(challengeDataObj.getString("entity_id"));
                                    childPostsBean.setChallengeAction(challengeDataObj.getString("action"));
                                    childPostsBean.setChallengeActionPerformedId(challengeDataObj.getString("action_performed_by_id"));
                                    childPostsBean.setChallengeActionAgainstId(challengeDataObj.getString("action_against_by_id"));
                                    childPostsBean.setChallengePostDateTime(challengeDataObj.getString("post_datetime"));
                                    childPostsBean.setChallengePostParentId(challengeDataObj.getString("post_parent_id"));
                                    childPostsBean.setChallengeIsShared(challengeDataObj.getString("is_shared"));
                                    childPostsBean.setChallengeChallengeStatus(challengeDataObj.getString("achieved_by"));

                                    childPostsBean.setChallengePostOwnerId(challengeDataObj.getString("post_owner_id"));
                                    childPostsBean.setIsLikeByYou(challengeDataObj.getString("is_liked_by_you"));
                                    childPostsBean.setEntityLikes(challengeDataObj.getString("entity_likes"));
                                    childPostsBean.setEntityComments(challengeDataObj.getString("entity_comments"));
                                    childPostsBean.setActionPerformedProfilePicUrl(challengeDataObj.getString("action_performed_profile_pic_url"));
                                    childPostsBean.setActionPerformFullName(challengeDataObj.getString("action_performed_fullname"));

                                }
                                else if (additionalDataObj.has("image")) {
                                    challengeData = additionalDataObj.getString("image");
                                    challengeDataObj = new JSONObject(challengeData);
                                    //System.out.println("imageData::" + challengeData);
                                    childPostsBean.setLocalType("image");

                                    childPostsBean.setEntityComments(challengeDataObj.getString("entity_comments"));
                                    childPostsBean.setNoCtaAction(challengeDataObj.getString("no_CTA_action"));
                                    childPostsBean.setAchievedBy(challengeDataObj.getString("achieved_by"));
                                    childPostsBean.setEntityLikes(challengeDataObj.getString("entity_likes"));
                                    childPostsBean.setChallengeEntityId(challengeDataObj.getString("entity_id"));
                                    childPostsBean.setChallengePostDateTime(challengeDataObj.getString("post_datetime"));
                                    childPostsBean.setActionPerformedProfilePicUrl(challengeDataObj.getString("action_performed_profile_pic_url"));
                                    childPostsBean.setIsLikeByYou(challengeDataObj.getString("is_liked_by_you"));
                                    childPostsBean.setChallengeActionPerformedId(challengeDataObj.getString("action_performed_by_id"));
                                    childPostsBean.setChallengeAction(challengeDataObj.getString("action"));
                                    childPostsBean.setChallengePostParentId(challengeDataObj.getString("post_parent_id"));
                                    childPostsBean.setChallengePostOwnerId(challengeDataObj.getString("post_owner_id"));
                                    childPostsBean.setChallengeIsShared(challengeDataObj.getString("is_shared"));
                                    childPostsBean.setActionPerformFullName(challengeDataObj.getString("action_performed_fullname"));
                                    childPostsBean.setChallengeActionAgainstId(challengeDataObj.getString("action_against_by_id"));

                                    String postEntityDetailData = challengeDataObj.getString("post_entity_detail");
                                    JSONObject postEntityDetailDataObj = new JSONObject(postEntityDetailData);

                                    childPostsBean.setPostEntityTitle(postEntityDetailDataObj.getString("title"));
                                    childPostsBean.setFileName(postEntityDetailDataObj.getString("file_name"));
                                    childPostsBean.setFileType(postEntityDetailDataObj.getString("file_type"));
                                    childPostsBean.setIsPublic(postEntityDetailDataObj.getString("is_public"));
                                    childPostsBean.setMediaDescription(postEntityDetailDataObj.getString("media_description"));
                                    childPostsBean.setFileUrl(postEntityDetailDataObj.getString("file_url"));

                                    if (postEntityDetailDataObj.has("comment_detail")) {
                                        JSONObject commentDetailObject = new JSONObject(postEntityDetailDataObj.getString("comment_detail"));
                                        childPostsBean.setCommentDetailId(commentDetailObject.getString("id"));
                                        childPostsBean.setCommentDetailEntityType(commentDetailObject.getString("entity_type"));
                                        childPostsBean.setCommentDetailEntityId(commentDetailObject.getString("entity_id"));
                                        childPostsBean.setCommmentDetailCommentedById(commentDetailObject.getString("commented_by_id"));
                                        childPostsBean.setCommentDetailComment(commentDetailObject.getString("comment"));
                                        childPostsBean.setCommentDetailCreatedAt(commentDetailObject.getString("created_at"));
                                    }
//                                    childPostsBeanArrayList.add(childPostsBean);
                                } else if (additionalDataObj.has("video")) {
                                    challengeData = additionalDataObj.getString("video");
                                    challengeDataObj = new JSONObject(challengeData);
                                    //System.out.println("videoData::" + challengeData);

                                    childPostsBean.setLocalType("video");
                                    childPostsBean.setChallengeEntityId(challengeDataObj.getString("entity_id"));
                                    childPostsBean.setChallengeAction(challengeDataObj.getString("action"));
                                    childPostsBean.setChallengeActionPerformedId(challengeDataObj.getString("action_performed_by_id"));
                                    childPostsBean.setChallengeActionAgainstId(challengeDataObj.getString("action_against_by_id"));
                                    childPostsBean.setChallengePostDateTime(challengeDataObj.getString("post_datetime"));
                                    childPostsBean.setChallengePostParentId(challengeDataObj.getString("post_parent_id"));
                                    childPostsBean.setChallengeIsShared(challengeDataObj.getString("is_shared"));
                                    childPostsBean.setChallengeChallengeStatus(challengeDataObj.getString("achieved_by"));
                                    childPostsBean.setNoCtaAction(challengeDataObj.getString("no_CTA_action"));
                                    childPostsBean.setChallengePostOwnerId(challengeDataObj.getString("post_owner_id"));
                                    childPostsBean.setIsLikeByYou(challengeDataObj.getString("is_liked_by_you"));
                                    childPostsBean.setEntityLikes(challengeDataObj.getString("entity_likes"));
                                    childPostsBean.setEntityComments(challengeDataObj.getString("entity_comments"));
                                    childPostsBean.setActionPerformedProfilePicUrl(challengeDataObj.getString("action_performed_profile_pic_url"));
                                    childPostsBean.setActionPerformFullName(challengeDataObj.getString("action_performed_fullname"));

                                    String postEntityDetailData = challengeDataObj.getString("post_entity_detail");
                                    JSONObject postEntityDetailDataObj = new JSONObject(postEntityDetailData);

                                    childPostsBean.setPostEntityTitle(postEntityDetailDataObj.getString("title"));
                                    childPostsBean.setFileName(postEntityDetailDataObj.getString("file_name"));
                                    childPostsBean.setFileType(postEntityDetailDataObj.getString("file_type"));
                                    childPostsBean.setIsPublic(postEntityDetailDataObj.getString("is_public"));
                                    childPostsBean.setMediaDescription(postEntityDetailDataObj.getString("media_description"));
                                    childPostsBean.setFileUrl(postEntityDetailDataObj.getString("file_url"));
                                    childPostsBean.setVideoThumbnail(postEntityDetailDataObj.getString("video_thumbnail"));
                                    childPostsBean.setFileUrl(postEntityDetailDataObj.getString("file_url"));

                                    if (postEntityDetailDataObj.has("comment_detail")) {
                                        JSONObject commentDetailObject = new JSONObject(postEntityDetailDataObj.getString("comment_detail"));
                                        childPostsBean.setCommentDetailId(commentDetailObject.getString("id"));
                                        childPostsBean.setCommentDetailEntityType(commentDetailObject.getString("entity_type"));
                                        childPostsBean.setCommentDetailEntityId(commentDetailObject.getString("entity_id"));
                                        childPostsBean.setCommmentDetailCommentedById(commentDetailObject.getString("commented_by_id"));
                                        childPostsBean.setCommentDetailComment(commentDetailObject.getString("comment"));
                                        childPostsBean.setCommentDetailCreatedAt(commentDetailObject.getString("created_at"));
                                    }

                                    if(postEntityDetailDataObj.has("youtube_url")){
                                        childPostsBean.setYoutubeUrl(postEntityDetailDataObj.getString("youtube_url"));
                                        childPostsBean.setYouTubeEmbedId(postEntityDetailDataObj.getString("youtube_embed_id"));
                                    }else{
                                        childPostsBean.setYoutubeUrl("");
                                        childPostsBean.setYouTubeEmbedId("");
                                    }

//                                    childPostsBeanArrayList.add(childPostsBean);
                                } else if (additionalDataObj.has("timeline")) {
                                    challengeData = additionalDataObj.getString("timeline");
                                    challengeDataObj = new JSONObject(challengeData);
                                    //System.out.println("timeLineData::" + challengeData);

                                    childPostsBean.setLocalType("timeline");
                                    childPostsBean.setChallengeEntityId(challengeDataObj.getString("entity_id"));
                                    childPostsBean.setChallengeAction(challengeDataObj.getString("action"));
                                    childPostsBean.setChallengeActionPerformedId(challengeDataObj.getString("action_performed_by_id"));
                                    childPostsBean.setChallengeActionAgainstId(challengeDataObj.getString("action_against_by_id"));
                                    childPostsBean.setChallengePostDateTime(challengeDataObj.getString("post_datetime"));
                                    childPostsBean.setChallengePostParentId(challengeDataObj.getString("post_parent_id"));
                                    childPostsBean.setChallengeIsShared(challengeDataObj.getString("is_shared"));
                                    childPostsBean.setChallengeChallengeStatus(challengeDataObj.getString("achieved_by"));
                                    childPostsBean.setNoCtaAction(challengeDataObj.getString("no_CTA_action"));
                                    if (challengeDataObj.has("approval_status")) {
                                        childPostsBean.setApprovalStatus(challengeDataObj.getString("approval_status"));
                                    } else {
                                        childPostsBean.setApprovalStatus("noValueApproval");
                                    }

                                    // new timeline changes
                                    if (challengeDataObj.has("post_additional_data")) {
                                        JSONObject postAdditionalData = new JSONObject(challengeDataObj.getString("post_additional_data"));
                                        childPostsBean.setPostAddDataType(postAdditionalData.getString("type"));
                                        if(postAdditionalData.has("is_synced")){
                                            childPostsBean.setIsSynced(postAdditionalData.getString("is_synced"));
                                        }else{
                                            childPostsBean.setIsSynced("false");
                                        }
                                        if (postAdditionalData.getString("type").equalsIgnoreCase("post_timeline_score")) {
                                            JSONObject sessionDetailObject = new JSONObject(postAdditionalData.getString("session_details"));
                                            childPostsBean.setSessionDetailTitle(sessionDetailObject.getString("title"));
                                            childPostsBean.setSessionDetailLocation(sessionDetailObject.getString("location"));
                                            childPostsBean.setSessionDetailCoachingProg(sessionDetailObject.getString("coaching_program"));
                                            childPostsBean.setSessionDetailTermName(sessionDetailObject.getString("terms_name"));
                                            childPostsBean.setSessionDetailDay(sessionDetailObject.getString("day"));
                                            childPostsBean.setSessionDetailFromDateFormatted(sessionDetailObject.getString("from_date_formatted"));
                                            childPostsBean.setSessionDetailToDateFormatted(sessionDetailObject.getString("to_date_formatted"));
                                            childPostsBean.setSessionDetailStartTimeFormatted(sessionDetailObject.getString("start_time_formatted"));
                                            childPostsBean.setSessionDetailEndTimeFormatted(sessionDetailObject.getString("end_time_formatted"));
                                            childPostsBean.setSessionDetailNumberOfHours(sessionDetailObject.getString("number_of_hours"));
                                            childPostsBean.setSessionDetailGroupName(sessionDetailObject.getString("group_name"));


                                            JSONArray data = postAdditionalData.getJSONArray("data");
                                            ArrayList<ChildPostAdditionalTimelineDataBean> childPostAdditionalTimelineDataBeans = new ArrayList<>();
                                            for (int k = 0; k < data.length(); k++) {
                                                JSONObject jsonObject = data.getJSONObject(k);
                                                ChildPostAdditionalTimelineDataBean childPostAdditionalTimelineDataBean = new ChildPostAdditionalTimelineDataBean();

                                                childPostAdditionalTimelineDataBean.setDataElementName(jsonObject.getString("element_name"));
                                                childPostAdditionalTimelineDataBean.setDataElementPerformancePercentage(jsonObject.getString("performance_percentage"));
                                                childPostAdditionalTimelineDataBean.setDataElementColorCode(jsonObject.getString("color_code"));
                                                childPostAdditionalTimelineDataBean.setDataElementScore(jsonObject.getString("score"));
                                                childPostAdditionalTimelineDataBean.setDataElementTotalScore(jsonObject.getString("total_score"));
                                                childPostAdditionalTimelineDataBean.setDataElementSessionId(jsonObject.getString("sessions_id"));

                                                if(jsonObject.has("report_date")){
                                                    childPostAdditionalTimelineDataBean.setReportDate(jsonObject.getString("report_date"));
                                                } else {
                                                    childPostAdditionalTimelineDataBean.setReportDate("");
                                                }

                                                childPostAdditionalTimelineDataBeans.add(childPostAdditionalTimelineDataBean);

                                            }
                                            childPostsBean.setChildPostAdditionalTimelineDataBeans(childPostAdditionalTimelineDataBeans);

                                        } else if (postAdditionalData.getString("type").equalsIgnoreCase("post_timeline_activity")) {

                                            if(postAdditionalData.has("session_details")){
                                                childPostsBean.setPostTimelineActivityType("session_details");
                                                JSONObject sessionDetailObject = new JSONObject(postAdditionalData.getString("session_details"));
                                                childPostsBean.setSessionDetailTitle(sessionDetailObject.getString("title"));
                                                childPostsBean.setSessionDetailLocation(sessionDetailObject.getString("location"));
                                                childPostsBean.setSessionDetailCoachingProg(sessionDetailObject.getString("coaching_program"));
                                                childPostsBean.setSessionDetailTermName(sessionDetailObject.getString("terms_name"));
                                                childPostsBean.setSessionDetailDay(sessionDetailObject.getString("day"));
                                                childPostsBean.setSessionDetailFromDateFormatted(sessionDetailObject.getString("from_date_formatted"));
                                                childPostsBean.setSessionDetailToDateFormatted(sessionDetailObject.getString("to_date_formatted"));
                                                childPostsBean.setSessionDetailStartTimeFormatted(sessionDetailObject.getString("start_time_formatted"));
                                                childPostsBean.setSessionDetailEndTimeFormatted(sessionDetailObject.getString("end_time_formatted"));
                                                childPostsBean.setSessionDetailNumberOfHours(sessionDetailObject.getString("number_of_hours"));
                                                childPostsBean.setSessionDetailGroupName(sessionDetailObject.getString("group_name"));
                                                childPostsBean.setSessionDetailStartDate(sessionDetailObject.getString("start_date"));
                                                childPostsBean.setSessionDetailEndDate(sessionDetailObject.getString("end_date"));

                                            }else if(postAdditionalData.has("track_details")){
                                                childPostsBean.setPostTimelineActivityType("track_details");
                                                JSONObject trackDetailObject = new JSONObject(postAdditionalData.getString("track_details"));
                                                childPostsBean.setDateFormatted(trackDetailObject.getString("date_formatted"));
                                                childPostsBean.setStartTime(trackDetailObject.getString("start_time"));
                                                childPostsBean.setEndTime(trackDetailObject.getString("end_time"));
                                            }

//                                            JSONObject sessionDetailObject = new JSONObject(postAdditionalData.getString("session_details"));
//                                            childPostsBean.setSessionDetailTitle(sessionDetailObject.getString("title"));
//                                            childPostsBean.setSessionDetailLocation(sessionDetailObject.getString("location"));
//                                            childPostsBean.setSessionDetailCoachingProg(sessionDetailObject.getString("coaching_program"));
//                                            childPostsBean.setSessionDetailTermName(sessionDetailObject.getString("terms_name"));
//                                            childPostsBean.setSessionDetailDay(sessionDetailObject.getString("day"));
//                                            childPostsBean.setSessionDetailFromDateFormatted(sessionDetailObject.getString("from_date_formatted"));
//                                            childPostsBean.setSessionDetailToDateFormatted(sessionDetailObject.getString("to_date_formatted"));
//                                            childPostsBean.setSessionDetailStartTimeFormatted(sessionDetailObject.getString("start_time_formatted"));
//                                            childPostsBean.setSessionDetailEndTimeFormatted(sessionDetailObject.getString("end_time_formatted"));
//                                            childPostsBean.setSessionDetailNumberOfHours(sessionDetailObject.getString("number_of_hours"));
//                                            childPostsBean.setSessionDetailGroupName(sessionDetailObject.getString("group_name"));
//                                            childPostsBean.setSessionDetailStartDate(sessionDetailObject.getString("start_date"));
//                                            childPostsBean.setSessionDetailEndDate(sessionDetailObject.getString("end_date"));

                                            JSONArray data = postAdditionalData.getJSONArray("data");
                                            ArrayList<ChildPostAdditionalTimelineDataBean> childPostAdditionalTimelineDataBeans = new ArrayList<>();
                                            for (int k = 0; k < data.length(); k++) {
                                                JSONObject jsonObject = data.getJSONObject(k);
                                                ChildPostAdditionalTimelineDataBean childPostAdditionalTimelineDataBean = new ChildPostAdditionalTimelineDataBean();

                                                childPostAdditionalTimelineDataBean.setActivityName(jsonObject.getString("activity_name"));
                                                childPostAdditionalTimelineDataBean.setTotal(jsonObject.getString("total"));
                                                childPostAdditionalTimelineDataBean.setAverage(jsonObject.getString("average"));
                                                childPostAdditionalTimelineDataBean.setLevelName(jsonObject.getString("level_name"));
                                                childPostAdditionalTimelineDataBean.setColorCode(jsonObject.getString("color_code"));
                                                childPostAdditionalTimelineDataBean.setDataElementSessionId(jsonObject.getString("sessions_id"));
                                                childPostAdditionalTimelineDataBean.setSessionId(jsonObject.getString("sessions_id"));

                                                childPostAdditionalTimelineDataBeans.add(childPostAdditionalTimelineDataBean);


                                            }
                                            childPostsBean.setChildPostAdditionalTimelineDataBeans(childPostAdditionalTimelineDataBeans);
                                        }

                                    }


                                    childPostsBean.setChallengePostOwnerId(challengeDataObj.getString("post_owner_id"));
                                    childPostsBean.setIsLikeByYou(challengeDataObj.getString("is_liked_by_you"));
                                    childPostsBean.setEntityLikes(challengeDataObj.getString("entity_likes"));
                                    childPostsBean.setEntityComments(challengeDataObj.getString("entity_comments"));
                                    childPostsBean.setActionPerformedProfilePicUrl(challengeDataObj.getString("action_performed_profile_pic_url"));
                                    childPostsBean.setActionPerformFullName(challengeDataObj.getString("action_performed_fullname"));

                                    String postEntityDetailData = challengeDataObj.getString("post_entity_detail");
                                    JSONObject postEntityDetailDataObj = new JSONObject(postEntityDetailData);

                                    childPostsBean.setPostEntityTitle(postEntityDetailDataObj.getString("title"));
                                    childPostsBean.setFileName(postEntityDetailDataObj.getString("file_name"));
                                    childPostsBean.setFileType(postEntityDetailDataObj.getString("file_type"));
                                    childPostsBean.setIsPublic(postEntityDetailDataObj.getString("is_public"));
                                    childPostsBean.setMediaDescription(postEntityDetailDataObj.getString("media_description"));
                                    childPostsBean.setFileUrl(postEntityDetailDataObj.getString("file_url"));
                                    childPostsBean.setSiteMediaId(postEntityDetailDataObj.getString("site_media_id"));
                                    childPostsBean.setFileUrl(postEntityDetailDataObj.getString("file_url"));

                                    if(postEntityDetailDataObj.has("youtube_url")){
                                        childPostsBean.setYoutubeUrl(postEntityDetailDataObj.getString("youtube_url"));
                                        childPostsBean.setYouTubeEmbedId(postEntityDetailDataObj.getString("youtube_embed_id"));
                                    }else{
                                        childPostsBean.setYoutubeUrl("");
                                        childPostsBean.setYouTubeEmbedId("");
                                    }

                                    if(postEntityDetailDataObj.has("video_thumbnail")){
                                        childPostsBean.setVideoThumbnail(postEntityDetailDataObj.getString("video_thumbnail"));
                                    }else{
                                        childPostsBean.setVideoThumbnail("noVideoThumb");
                                    }

                                    if (postEntityDetailDataObj.has("comment_detail")) {
                                        JSONObject commentDetailObject = new JSONObject(postEntityDetailDataObj.getString("comment_detail"));
                                        childPostsBean.setCommentDetailId(commentDetailObject.getString("id"));
                                        childPostsBean.setCommentDetailEntityType(commentDetailObject.getString("entity_type"));
                                        childPostsBean.setCommentDetailEntityId(commentDetailObject.getString("entity_id"));
                                        childPostsBean.setCommmentDetailCommentedById(commentDetailObject.getString("commented_by_id"));
                                        childPostsBean.setCommentDetailComment(commentDetailObject.getString("comment"));
                                        childPostsBean.setCommentDetailCreatedAt(commentDetailObject.getString("created_at"));
                                    }
//                                    childPostsBeanArrayList.add(childPostsBean);
                                } else if (additionalDataObj.has("friend")) {
                                    challengeData = additionalDataObj.getString("friend");
                                    challengeDataObj = new JSONObject(challengeData);
                                    //System.out.println("friend::" + challengeData);

                                    childPostsBean.setLocalType("friend");
                                    childPostsBean.setChallengeEntityId(challengeDataObj.getString("entity_id"));
                                    childPostsBean.setChallengeAction(challengeDataObj.getString("action"));
                                    childPostsBean.setChallengeActionPerformedId(challengeDataObj.getString("action_performed_by_id"));
                                    childPostsBean.setChallengeActionAgainstId(challengeDataObj.getString("action_against_by_id"));
                                    childPostsBean.setChallengePostDateTime(challengeDataObj.getString("post_datetime"));
                                    childPostsBean.setChallengePostParentId(challengeDataObj.getString("post_parent_id"));
                                    childPostsBean.setChallengeIsShared(challengeDataObj.getString("is_shared"));
                                    childPostsBean.setChallengeChallengeStatus(challengeDataObj.getString("achieved_by"));
                                    childPostsBean.setNoCtaAction(challengeDataObj.getString("no_CTA_action"));
                                    childPostsBean.setChallengePostOwnerId(challengeDataObj.getString("post_owner_id"));
                                    childPostsBean.setIsLikeByYou(challengeDataObj.getString("is_liked_by_you"));
                                    childPostsBean.setEntityLikes(challengeDataObj.getString("entity_likes"));
                                    childPostsBean.setEntityComments(challengeDataObj.getString("entity_comments"));
                                    childPostsBean.setActionPerformedProfilePicUrl(challengeDataObj.getString("action_performed_profile_pic_url"));
                                    childPostsBean.setActionPerformFullName(challengeDataObj.getString("action_performed_fullname"));

                                    if(challengeDataObj.has("approval_status")){
                                        childPostsBean.setApprovalStatus(challengeDataObj.getString("approval_status"));
                                    }else{
                                        childPostsBean.setApprovalStatus("");
                                    }

                                    String postEntityDetailData = challengeDataObj.getString("post_entity_detail");
                                    JSONObject postEntityDetailDataObj = new JSONObject(postEntityDetailData);

                                    childPostsBean.setPostEntityTitle(postEntityDetailDataObj.getString("title"));
                                    childPostsBean.setRequestState(postEntityDetailDataObj.getString("request_state"));
                                    // childPostsBean.setApprovalRequiredByParent(postEntityDetailDataObj.getString("approval_required_by"));
                                    if (postEntityDetailDataObj.has("request_state_label")) {
                                        childPostsBean.setRequestStateLabel(postEntityDetailDataObj.getString("request_state_label"));
                                        childPostsBean.setSentFrom(postEntityDetailDataObj.getString("sent_from"));
                                    }
                                   // childPostsBeanArrayList.add(childPostsBean);
                                }

                                if(isExpiredChallenge==false){
                                    childPostsBeanArrayList.add(childPostsBean);
                                }else{
                                    isExpiredChallenge=false;
                                }



                            }


                            mSwipyRefreshLayout.setRefreshing(false);
                            parentChildPostAdapter.notifyDataSetChanged();
                        } else {
                            mSwipyRefreshLayout.setRefreshing(false);
                            Toast.makeText(ParentChildTimeline.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentChildTimeline.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        mSwipyRefreshLayout.setRefreshing(false);
                    }


                }


        }

    }

    @Override
    public void onResume() {
        super.onResume();
        childPostsBeanArrayList.clear();
        offset = 0;
        getTimeline();

    }
}