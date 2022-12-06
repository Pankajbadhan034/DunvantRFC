package com.dunvant.application.startModule.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildPostAdditionalTimelineDataBean;
import com.dunvant.application.beans.ChildPostsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildMainScreen;
import com.dunvant.application.startModule.CoachBlankActivityForCreatePostNew;
import com.dunvant.application.startModule.adapter.CoachPostAdapterNew;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
public class CoachAreaFragment extends Fragment implements IWebServiceCallback {
    boolean isExpiredChallenge=false;
    SwipyRefreshLayout mSwipyRefreshLayout;
    int offset;
    ListView postList;
    CoachPostAdapterNew childPostAdapter;
    ChildPostsBean childPostsBean;
    ArrayList<ChildPostsBean> childPostsBeanArrayList = new ArrayList<>();
    private final String CHILD_POST = "CHILD_POST";
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    //String valuePost;
    ImageView createPost;

    public static boolean comingFromCreatePost = false;

    //    public ChildPostFragment(String valuePost){
//        this.valuePost=valuePost;
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_fragment_child_posts, container, false);
        postList = (ListView) view.findViewById(R.id.postList);
        createPost = (ImageView) view.findViewById(R.id.createPost);

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((ChildMainScreen) getActivity()).showCreatePost();

                Intent intent = new Intent(getActivity(), CoachBlankActivityForCreatePostNew.class);
                startActivity(intent);

            }
        });

        mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);

        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {

                offset += 1;
                childpostFeedData();
            }
        });

        childPostAdapter = new CoachPostAdapterNew(getActivity(), childPostsBeanArrayList, CoachAreaFragment.this);
        postList.setAdapter(childPostAdapter);

        childPostsBeanArrayList.clear();
        offset = 0;
        childpostFeedData();

        return view;
    }

    public void childpostFeedData() {
        if (Utilities.isNetworkAvailable(getActivity())) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("offset", "" + offset));
            if (ChildMainScreen.CHILDPOST.equalsIgnoreCase("myIfaCareer")) {
                nameValuePairList.add(new BasicNameValuePair("feed_type", "ifa_feeds"));
            }

            String webServiceUrl = Utilities.BASE_URL + "coach/timeline";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, CHILD_POST, CoachAreaFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);
        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case CHILD_POST:

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
//                                    System.out.println("friend_suggestions::" + challengeData);

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

                                } else if (additionalDataObj.has("challenge")) {

                                    challengeData = additionalDataObj.getString("challenge");
                                    challengeDataObj = new JSONObject(challengeData);
//                                    System.out.println("challengeData::" + challengeData);

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
//                                       // isExpiredChallenge=true;
//
//                                        if(challengeDataObj.getString("achieved_by").equalsIgnoreCase("") ||
//                                                challengeDataObj.getString("achieved_by").equalsIgnoreCase("0")
//                                                ||challengeDataObj.getString("achieved_by").equalsIgnoreCase("null")){
//                                                    isExpiredChallenge=true;
//                                        }else{
//                                                    isExpiredChallenge=false;
//                                        }
//
//                                    }else{
//                                        isExpiredChallenge=false;
//                                    }
                                    if(postEntityDetailDataObj.has("file_url")){
                                        childPostsBean.setFileUrl(postEntityDetailDataObj.getString("file_url"));
                                        childPostsBean.setFileType(postEntityDetailDataObj.getString("file_type"));
                                    }else{
                                        childPostsBean.setFileUrl("");
                                        childPostsBean.setFileType("");
                                    }

                                    if (challengeDataObj.has("approval_status")) {
                                        childPostsBean.setApprovalStatus(challengeDataObj.getString("approval_status"));
                                    } else {
                                        childPostsBean.setApprovalStatus("noValueApproval");
                                    }
                                    if(postEntityDetailDataObj.has("video_thumbnail")){
                                        childPostsBean.setVideoThumbnail(postEntityDetailDataObj.getString("video_thumbnail"));
                                    }

                                } else if (additionalDataObj.has("workout")) {
                                    challengeData = additionalDataObj.getString("workout");
                                    challengeDataObj = new JSONObject(challengeData);
//                                    System.out.println("workOutData::" + challengeData);
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
                                } else if (additionalDataObj.has("image")) {
                                    challengeData = additionalDataObj.getString("image");
                                    challengeDataObj = new JSONObject(challengeData);
//                                    System.out.println("imageData::" + challengeData);
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

                                } else if (additionalDataObj.has("video")) {
                                    challengeData = additionalDataObj.getString("video");
                                    challengeDataObj = new JSONObject(challengeData);
//                                    System.out.println("videoData::" + challengeData);

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

                                } else if (additionalDataObj.has("timeline")) {
                                    challengeData = additionalDataObj.getString("timeline");
                                    challengeDataObj = new JSONObject(challengeData);
//                                    System.out.println("timeLineData::" + challengeData);

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

                                } else if (additionalDataObj.has("friend")) {
                                    challengeData = additionalDataObj.getString("friend");
                                    challengeDataObj = new JSONObject(challengeData);
//                                    System.out.println("friend::" + challengeData);

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

                                }

                                if(isExpiredChallenge==false){
                                    childPostsBeanArrayList.add(childPostsBean);
                                }else{
                                    isExpiredChallenge=false;
                                }


                            }


                            mSwipyRefreshLayout.setRefreshing(false);
                            childPostAdapter.notifyDataSetChanged();
                        } else {
                            mSwipyRefreshLayout.setRefreshing(false);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        mSwipyRefreshLayout.setRefreshing(false);
                    }
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(comingFromCreatePost){
            comingFromCreatePost = false;
            childPostsBeanArrayList.clear();
            offset = 0;
            childpostFeedData();
        }
    }
}