package com.dunvant.application.parent.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.HorizontalListView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupAttendanceBean;
import com.dunvant.application.beans.ChildPostsBean;
import com.dunvant.application.beans.ChildSuggestionBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildAllCommentsReadScreen;
import com.dunvant.application.child.ChildPostViewScreen;
import com.dunvant.application.child.adapters.ChildPostTimeLineActivityAdapter;
import com.dunvant.application.child.adapters.ChildPostTimeLineScoreAdapter;
import com.dunvant.application.child.adapters.ChildSuggestionAdapter;
import com.dunvant.application.parent.ParentChildActivityStatsScreen;
import com.dunvant.application.parent.ParentChildReportScreen;
import com.dunvant.application.parent.ParentChildTimeline;
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
 * Created by DEVLABS\pbadhan on 6/7/17.
 */
public class ParentChildPostAdapter extends BaseAdapter implements IWebServiceCallback {
    //new code
    TextView yourScoreLabel;
    TextView yourScore;
    TextView yourTimeLabel;
    TextView yourTime;
    private final String APPROVAL_TIMELINE = "APPROVAL_TIMELINE";
    private final String LIKE_UNLIKE = "LIKE_UNLIKE";
    private final String POST_DELETE = "POST_DELETE";
    private final String POST_SHARE = "POST_SHARE";
    private final String ACCEPT_CHALLENGE = "ACCEPT_CHALLENGE";
    private final String IGNORE_CHALLENGE = "IGNORE_CHALLENGE";
    private final String APPROVAL_CHALLENGE = "APPROVAL_CHALLENGE";
    private final String ACCEPT_OR_REJECT_FRIEND = "ACCEPT_OR_REJECT_FRIEND";
    private final String ACCEPT_OR_REJECT_APPROVAL_REQUEST = "ACCEPT_OR_REJECT_APPROVAL_REQUEST";
    ArrayList<ChildSuggestionBean> childSuggestionBeanArrayList = new ArrayList<>();
    String clickFriendButton;
    int friendPosition;
    int positionValue;
    int sharedPosition;
    int likeCountPosition;
    int likeValue;
    int acceptCountPosition;
    int ignoreCountPosition;
    Dialog dialog;
    ImageView delete;
    TextView commentDesc;
    LinearLayout parentLinearLayout;
    RelativeLayout header;
    RelativeLayout challengeLayout;
    RelativeLayout imageLayout;
    RelativeLayout timelineLayout;
    RelativeLayout bottomLayout1;
    RelativeLayout bottomLayout2;
    RelativeLayout suggestionLayout;
    HorizontalListView friendSuggestionsList;
    RelativeLayout friendLayout;
    RelativeLayout timelinePostLayout;
    Context context;
    ArrayList<ChildPostsBean> childPostsBeanArrayList;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    ImageLoader imageLoaderWithoutCircle = ImageLoader.getInstance();
    DisplayImageOptions options;
    DisplayImageOptions optionsWithoutCircle;
    ImageView playVideoPic;

    ShareDialog shareDialog;
    CallbackManager callbackManager;

    Typeface helvetica;
    Typeface linoType;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    String childId;

    public ParentChildPostAdapter(Context context, ArrayList<ChildPostsBean> childPostsBeanArrayList, ParentChildTimeline parentChildTimeline, String childId) {
        this.context = context;
        this.childPostsBeanArrayList = childPostsBeanArrayList;
        this.childId = childId;
        layoutInflater = LayoutInflater.from(context);

        shareDialog = new ShareDialog(parentChildTimeline);
        callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new

                FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {
                    }
                });

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new RoundedBitmapDisplayer(1000))
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        imageLoaderWithoutCircle.init(ImageLoaderConfiguration.createDefault(context));
        optionsWithoutCircle = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        helvetica = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue.ttf");
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
        return childPostsBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return childPostsBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.child_adapter_child_posts_new, null);

        parentLinearLayout = (LinearLayout) convertView.findViewById(R.id.parentLinearLayout);
        header = (RelativeLayout) convertView.findViewById(R.id.header);
        imageLayout = (RelativeLayout) convertView.findViewById(R.id.imageLayout);
        timelineLayout = (RelativeLayout) convertView.findViewById(R.id.timelineLayout);
        bottomLayout1 = (RelativeLayout) convertView.findViewById(R.id.bottomLayout1);
        bottomLayout2 = (RelativeLayout) convertView.findViewById(R.id.bottomLayout2);
        challengeLayout = (RelativeLayout) convertView.findViewById(R.id.challengeLayout);
        suggestionLayout = (RelativeLayout) convertView.findViewById(R.id.suggestionLayout);
        timelinePostLayout = (RelativeLayout) convertView.findViewById(R.id.timelinePostLayout);

        ImageView profilePic = (ImageView) convertView.findViewById(R.id.profilePic);
        TextView titlePost = (TextView) convertView.findViewById(R.id.titlePost);
        TextView datePost = (TextView) convertView.findViewById(R.id.datePost);
        TextView timePost = (TextView) convertView.findViewById(R.id.timePost);
        commentDesc = (TextView) convertView.findViewById(R.id.commentDesc);
        TextView typeText = (TextView) convertView.findViewById(R.id.typeText);
        delete = (ImageView) convertView.findViewById(R.id.delete);
        // include view ids for image and video
        ImageView backgroudImage = (ImageView) convertView.findViewById(R.id.backgroudImage);
        TextView likeClick = (TextView) convertView.findViewById(R.id.likeClick);
        TextView commentClick = (TextView) convertView.findViewById(R.id.commentClick);
        TextView shareClick = (TextView) convertView.findViewById(R.id.shareClick);
        playVideoPic = (ImageView) convertView.findViewById(R.id.playVideoPic);
        shareClick.setVisibility(View.INVISIBLE);

        // old timeline
        TextView timelinePostClick = (TextView) convertView.findViewById(R.id.timelinePostClick);

        // horizontal listview timeline
        HorizontalListView postScoreTimelineList = (HorizontalListView) convertView.findViewById(R.id.postScoreTimelineList);
        TextView sessionLabel = (TextView) convertView.findViewById(R.id.sessionLabel);
        TextView session = (TextView) convertView.findViewById(R.id.session);
        TextView locationLabel = (TextView) convertView.findViewById(R.id.locationLabel);
        TextView location = (TextView) convertView.findViewById(R.id.location);
        TextView coachingProgramLabel = (TextView) convertView.findViewById(R.id.coachingProgramLabel);
        TextView program = (TextView) convertView.findViewById(R.id.program);
        TextView termLabel = (TextView) convertView.findViewById(R.id.termLabel);
        TextView term = (TextView) convertView.findViewById(R.id.term);

        // challenge
        TextView acceptClick = (TextView) convertView.findViewById(R.id.acceptClick);
        TextView ignoreClick = (TextView) convertView.findViewById(R.id.ignoreClick);
        TextView chalShareClick = (TextView) convertView.findViewById(R.id.chalShareClick);

        TextView chalTitle = (TextView) convertView.findViewById(R.id.chalTitle);
        TextView chalDescription = (TextView) convertView.findViewById(R.id.chalDescription);
        TextView chalTargetScoreLabel = (TextView) convertView.findViewById(R.id.chalTargetScoreLabel);
        TextView chalTargetScore = (TextView) convertView.findViewById(R.id.chalTargetScore);
        TextView chalTargetTimeLabel = (TextView) convertView.findViewById(R.id.chalTargetTimeLabel);
        TextView chalTargetTime = (TextView) convertView.findViewById(R.id.chalTargetTime);
        //new code
        yourScoreLabel = (TextView) convertView.findViewById(R.id.yourScoreLabel);
        yourScore = (TextView) convertView.findViewById(R.id.yourScore);
        yourTimeLabel = (TextView) convertView.findViewById(R.id.yourTimeLabel);
        yourTime = (TextView) convertView.findViewById(R.id.yourTime);
        yourScoreLabel.setTypeface(helvetica);
        yourScore.setTypeface(helvetica);
        yourTimeLabel.setTypeface(helvetica);
        yourTime.setTypeface(helvetica);

        // suggestions
        friendSuggestionsList = (HorizontalListView) convertView.findViewById(R.id.friendSuggestionsList);
        TextView titleFriendSug = (TextView) convertView.findViewById(R.id.titleFriendSug);

        //friend layout
        friendLayout = (RelativeLayout) convertView.findViewById(R.id.friendLayout);
        TextView acceptFriend = (TextView) convertView.findViewById(R.id.acceptFriend);
        TextView rejectFriend = (TextView) convertView.findViewById(R.id.rejectFriend);

        titlePost.setTypeface(helvetica);
        datePost.setTypeface(helvetica);
        timePost.setTypeface(helvetica);
        commentDesc.setTypeface(helvetica);
        typeText.setTypeface(helvetica);
        likeClick.setTypeface(helvetica);
        session.setTypeface(helvetica);
        sessionLabel.setTypeface(helvetica);
        locationLabel.setTypeface(helvetica);
        location.setTypeface(helvetica);
        coachingProgramLabel.setTypeface(helvetica);
        program.setTypeface(helvetica);
        termLabel.setTypeface(helvetica);
        term.setTypeface(helvetica);
        acceptClick.setTypeface(helvetica);
        ignoreClick.setTypeface(helvetica);
        chalTitle.setTypeface(helvetica);
        chalDescription.setTypeface(helvetica);
        chalTargetScoreLabel.setTypeface(helvetica);
        chalTargetScore.setTypeface(helvetica);
        chalTargetTimeLabel.setTypeface(helvetica);
        chalTargetTime.setTypeface(helvetica);
        acceptFriend.setTypeface(helvetica);
        rejectFriend.setTypeface(helvetica);
        commentClick.setTypeface(helvetica);
        shareClick.setTypeface(helvetica);
        timelinePostClick.setTypeface(helvetica);
        titleFriendSug.setTypeface(helvetica);

        final ChildPostsBean childPostsBean = childPostsBeanArrayList.get(position);

        //System.out.println("localTYpe::" + childPostsBean.getLocalType());

        if (childPostsBean.getLocalType().equalsIgnoreCase("friend_suggestions")) {
            friendSuggestionVisibilities();
            //System.out.println("friend_suggestions::");

            titleFriendSug.setText("Suggested Friends");
            titlePost.setText(childPostsBean.getActionPerformFullName());
            imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);

            try {
                JSONArray suggestionArray = new JSONArray(childPostsBean.getSuggestions());

                ChildSuggestionBean childSuggestionBean;
                for (int i = 0; i < suggestionArray.length(); i++) {
                    childSuggestionBean = new ChildSuggestionBean();
                    JSONObject suggestionObject = suggestionArray.getJSONObject(i);
                    childSuggestionBean.setUserId(suggestionObject.getString("users_id"));
                    childSuggestionBean.setFullName(suggestionObject.getString("full_name"));
                    childSuggestionBean.setProfilePicUrl(suggestionObject.getString("profile_pic_url"));
                    childSuggestionBean.setAgeValue(suggestionObject.getString("age_value"));
                    childSuggestionBean.setFavouritePlayer(suggestionObject.getString("favourite_player"));
                    childSuggestionBean.setFavouritePosition(suggestionObject.getString("favourite_position"));
                    childSuggestionBean.setFavouriteTeam(suggestionObject.getString("favourite_team"));
                    childSuggestionBean.setCanSendRequest(suggestionObject.getString("can_send_request"));
                    childSuggestionBean.setIsPrivate(suggestionObject.getString("is_private"));
                    //System.out.println("NAMEHERE:::" + suggestionObject.getString("full_name"));

                    childSuggestionBeanArrayList.add(childSuggestionBean);
                }

                ChildSuggestionAdapter childSuggestionAdapter = new ChildSuggestionAdapter(context, childSuggestionBeanArrayList);
                friendSuggestionsList.setAdapter(childSuggestionAdapter);

            } catch (Exception e) {
                //System.out.println("hereExceptionCatch");
                e.printStackTrace();
            }


        } else if (childPostsBean.getLocalType().equalsIgnoreCase("workout")) {
            workoutVisibilities();
            imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
            commentDesc.setText(Html.fromHtml(childPostsBean.getPostEntityTitle()));
            titlePost.setText(childPostsBean.getActionPerformFullName());
            datePost.setText(childPostsBean.getPostedDateFormatted());
            timePost.setText(childPostsBean.getPostedTimeFormatted());
            likeClick.setText("Likes (" + childPostsBean.getEntityLikes() + ")");
            commentClick.setText("Comments (" + childPostsBean.getEntityComments() + ")");
            if (childPostsBean.getActivity().equalsIgnoreCase("")) {
                typeText.setVisibility(View.GONE);
            } else {
                typeText.setText(childPostsBean.getActivity());
            }
        } else if (childPostsBean.getLocalType().equalsIgnoreCase("image")) {
            imageVisibilities();
            //System.out.println("Title" + childPostsBean.getPostEntityTitle());
            imageLoaderWithoutCircle.displayImage(childPostsBean.getFileUrl(), backgroudImage, optionsWithoutCircle);
            imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
            commentDesc.setText(childPostsBean.getCommentDetailComment());
            titlePost.setText(childPostsBean.getActionPerformFullName());
            datePost.setText(childPostsBean.getPostedDateFormatted());
            timePost.setText(childPostsBean.getPostedTimeFormatted());
            likeClick.setText("Likes (" + childPostsBean.getEntityLikes() + ")");
            commentClick.setText("Comments (" + childPostsBean.getEntityComments() + ")");
            if (childPostsBean.getActivity().equalsIgnoreCase("")) {
                typeText.setVisibility(View.GONE);
            } else {
                typeText.setText(childPostsBean.getActivity());
            }

        } else if (childPostsBean.getLocalType().equalsIgnoreCase("video")) {
            videoVisibilities();
            //System.out.println("Title" + childPostsBean.getPostEntityTitle());
            imageLoaderWithoutCircle.displayImage(childPostsBean.getVideoThumbnail(), backgroudImage, optionsWithoutCircle);
            imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
            commentDesc.setText(childPostsBean.getCommentDetailComment());
            titlePost.setText(childPostsBean.getActionPerformFullName());
            datePost.setText(childPostsBean.getPostedDateFormatted());
            timePost.setText(childPostsBean.getPostedTimeFormatted());
            likeClick.setText("Likes (" + childPostsBean.getEntityLikes() + ")");
            commentClick.setText("Comments (" + childPostsBean.getEntityComments() + ")");
            if (childPostsBean.getActivity().equalsIgnoreCase("")) {
                typeText.setVisibility(View.GONE);
            } else {
                typeText.setText(childPostsBean.getActivity());
            }

        } else if (childPostsBean.getLocalType().equalsIgnoreCase("timeline")) {

            if (!(childPostsBean.getPostAddDataType() == null)) {
                if (childPostsBean.getPostAddDataType().equalsIgnoreCase("post_timeline_score")) {
                    timelinePostScoreVisibilities();
                    //System.out.println("post_score");
                    imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
                    titlePost.setText(childPostsBean.getActionPerformFullName());
                    datePost.setText(childPostsBean.getPostedDateFormatted());
                    timePost.setText(childPostsBean.getPostedTimeFormatted());
                    session.setText(childPostsBean.getSessionDetailTitle() + " | " + childPostsBean.getSessionDetailDay() + " | " + childPostsBean.getSessionDetailStartTimeFormatted() + " - " + childPostsBean.getSessionDetailEndTimeFormatted() + " | " + childPostsBean.getSessionDetailFromDateFormatted() + " - " + childPostsBean.getSessionDetailToDateFormatted());
                    location.setText(childPostsBean.getSessionDetailLocation());
                    program.setText(childPostsBean.getSessionDetailCoachingProg());
                    if (childPostsBean.getActivity().equalsIgnoreCase("")) {
                        typeText.setVisibility(View.GONE);
                    } else {
                        typeText.setText(childPostsBean.getActivity());
                    }
                    term.setText(childPostsBean.getSessionDetailTermName());
                    likeClick.setText("Likes (" + childPostsBean.getEntityLikes() + ")");
                    commentClick.setText("Comments (" + childPostsBean.getEntityComments() + ")");

                    //System.out.println("SizeHereList::" + childPostsBean.getChildPostAdditionalTimelineDataBeans().size());

                    ChildPostTimeLineScoreAdapter childPostTimeLineScoreAdapter = new ChildPostTimeLineScoreAdapter(context, childPostsBean.getChildPostAdditionalTimelineDataBeans());
                    postScoreTimelineList.setAdapter(childPostTimeLineScoreAdapter);

                } else if (childPostsBean.getPostAddDataType().equalsIgnoreCase("post_timeline_activity")) {
                    //System.out.println("post_activity");
                    timelinePostScoreVisibilities();

                    if (childPostsBean.getPostTimelineActivityType().equalsIgnoreCase("session_details")) {
                        imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
                        imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
                        titlePost.setText(childPostsBean.getActionPerformFullName());
                        datePost.setText(childPostsBean.getPostedDateFormatted());
                        timePost.setText(childPostsBean.getPostedTimeFormatted());
                        session.setText(childPostsBean.getSessionDetailTitle() + " | " + childPostsBean.getSessionDetailDay() + " | " + childPostsBean.getSessionDetailStartTimeFormatted() + " - " + childPostsBean.getSessionDetailEndTimeFormatted() + " | " + childPostsBean.getSessionDetailFromDateFormatted() + " - " + childPostsBean.getSessionDetailToDateFormatted());
                        location.setText(childPostsBean.getSessionDetailLocation());
                        program.setText(childPostsBean.getSessionDetailCoachingProg());
                        if (childPostsBean.getActivity().equalsIgnoreCase("")) {
                            typeText.setVisibility(View.GONE);
                        } else {
                            typeText.setText(childPostsBean.getActivity());
                        }
                        term.setText(childPostsBean.getSessionDetailTermName());
                        likeClick.setText("Likes (" + childPostsBean.getEntityLikes() + ")");
                        commentClick.setText("Comments (" + childPostsBean.getEntityComments() + ")");

                        ChildPostTimeLineActivityAdapter childPostTimeLineActivityAdapter = new ChildPostTimeLineActivityAdapter(context, childPostsBean.getChildPostAdditionalTimelineDataBeans(), "session_details");
                        postScoreTimelineList.setAdapter(childPostTimeLineActivityAdapter);

                    } else if (childPostsBean.getPostTimelineActivityType().equalsIgnoreCase("track_details")) {
                        shareClick.setVisibility(View.INVISIBLE);
                        sessionLabel.setText("TRACK WORKOUT DATE: ");
                        locationLabel.setText("START TIME: ");
                        coachingProgramLabel.setText("END TIME: ");
                        termLabel.setVisibility(View.GONE);
                        term.setVisibility(View.GONE);

                        if (childPostsBean.getActivity().equalsIgnoreCase("")) {
                            typeText.setVisibility(View.GONE);
                        } else {
                            typeText.setText(childPostsBean.getActivity());
                        }

                        session.setText(childPostsBean.getDateFormatted());
                        location.setText(childPostsBean.getStartTime());
                        program.setText(childPostsBean.getEndTime());

                        imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
                        titlePost.setText(childPostsBean.getActionPerformFullName());
                        datePost.setText(childPostsBean.getPostedDateFormatted());
                        timePost.setText(childPostsBean.getPostedTimeFormatted());

                        likeClick.setText("Likes (" + childPostsBean.getEntityLikes() + ")");
                        commentClick.setText("Comments (" + childPostsBean.getEntityComments() + ")");

                        //System.out.println("SizeHereList::" + childPostsBean.getChildPostAdditionalTimelineDataBeans().size());

                        ChildPostTimeLineActivityAdapter childPostTimeLineActivityAdapter = new ChildPostTimeLineActivityAdapter(context, childPostsBean.getChildPostAdditionalTimelineDataBeans(), "track_details");
                        postScoreTimelineList.setAdapter(childPostTimeLineActivityAdapter);

                    }
//                    //System.out.println("post_activity");
//                    timelinePostScoreVisibilities();
//                    imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
//                    imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
//                    titlePost.setText(childPostsBean.getActionPerformFullName());
//                    datePost.setText(childPostsBean.getPostedDateFormatted());
//                    timePost.setText(childPostsBean.getPostedTimeFormatted());
//                    session.setText(childPostsBean.getSessionDetailTitle()+" | "+childPostsBean.getSessionDetailDay()+" | "+childPostsBean.getSessionDetailStartTimeFormatted()+" - "+childPostsBean.getSessionDetailEndTimeFormatted()+" | "+childPostsBean.getSessionDetailFromDateFormatted()+" - "+childPostsBean.getSessionDetailToDateFormatted());
//                    location.setText(childPostsBean.getSessionDetailLocation());
//                    program.setText(childPostsBean.getSessionDetailCoachingProg());
//                    if(childPostsBean.getActivity().equalsIgnoreCase("")){
//                        typeText.setVisibility(View.GONE);
//                    }else{
//                        typeText.setText(childPostsBean.getActivity());
//                    }
//                    term.setText(childPostsBean.getSessionDetailTermName());
//                    likeClick.setText("Like ("+childPostsBean.getEntityLikes()+")");
//                    commentClick.setText("Comments ("+childPostsBean.getEntityComments()+")");
//
//                    ChildPostTimeLineActivityAdapter childPostTimeLineActivityAdapter = new ChildPostTimeLineActivityAdapter(context, childPostsBean.getChildPostAdditionalTimelineDataBeans(),"session_details");
//                    postScoreTimelineList.setAdapter(childPostTimeLineActivityAdapter);
                    if(childPostsBean.getIsSynced().equalsIgnoreCase("false")){
                        postScoreTimelineList.setVisibility(View.GONE);
                    }
                } else if (childPostsBean.getApprovalStatus().equalsIgnoreCase("approval_required_timeline")) {
                    oldTimelineVisibilities();
                    timelinePostLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                    header.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                    imageLoaderWithoutCircle.displayImage(childPostsBean.getFileUrl(), backgroudImage, optionsWithoutCircle);
                    imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
                    titlePost.setText(childPostsBean.getActionPerformFullName());
                    datePost.setText(childPostsBean.getPostedDateFormatted());
                    timePost.setText(childPostsBean.getPostedTimeFormatted());
                    if (childPostsBean.getActivity().equalsIgnoreCase("")) {
                        typeText.setVisibility(View.GONE);
                    } else {
                        typeText.setText(childPostsBean.getActivity());
                    }
                    timelinePostClick.setEnabled(true);
                    timelinePostClick.setText("Approve");
                } else if (childPostsBean.getApprovalStatus().equalsIgnoreCase("approved_timeline")) {
                    oldTimelineVisibilities();
                    imageLoaderWithoutCircle.displayImage(childPostsBean.getFileUrl(), backgroudImage, optionsWithoutCircle);
                    imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
                    titlePost.setText(childPostsBean.getActionPerformFullName());
                    datePost.setText(childPostsBean.getPostedDateFormatted());
                    timePost.setText(childPostsBean.getPostedTimeFormatted());
                    typeText.setText(childPostsBean.getActivity());
                    timelinePostClick.setEnabled(false);
                    timelinePostClick.setText("Approved");
                    timelinePostClick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greytick, 0, 0, 0);
                } else {
                    imageVisibilities();
                    if (childPostsBean.getActivity().contains("Joined IFA Sport")) {
                        delete.setVisibility(View.INVISIBLE);
                    } else {
//                        delete.setVisibility(View.VISIBLE);
                        delete.setVisibility(View.INVISIBLE);
                    }
                    //System.out.println("urlHEre:" + childPostsBean.getFileUrl());

                    if(childPostsBean.getVideoThumbnail().equalsIgnoreCase("noVideoThumb")){
                        if(childPostsBean.getFileUrl().equalsIgnoreCase("")){
                            //System.out.println("7 else part 1 HERE");
                            imageLoaderWithoutCircle.displayImage(childPostsBean.getFileUrl(), backgroudImage, optionsWithoutCircle);
                        }else{
                            //System.out.println("8 else part 2 HERE");
                            backgroudImage.setVisibility(View.GONE);
                        }
                    }else{
                        playVideoPic.setVisibility(View.VISIBLE);
                        imageLoaderWithoutCircle.displayImage(childPostsBean.getVideoThumbnail(), backgroudImage, optionsWithoutCircle);
                    }


                    imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
                    commentDesc.setText(Html.fromHtml(childPostsBean.getPostEntityTitle()));
                    titlePost.setText(childPostsBean.getActionPerformFullName());
                    datePost.setText(childPostsBean.getPostedDateFormatted());
                    timePost.setText(childPostsBean.getPostedTimeFormatted());
                    likeClick.setText("Likes (" + childPostsBean.getEntityLikes() + ")");
                    commentClick.setText("Comments (" + childPostsBean.getEntityComments() + ")");
                    typeText.setText(childPostsBean.getActivity());
                    //System.out.println("HERE_else_part2:::");
                }
            } else if (childPostsBean.getApprovalStatus().equalsIgnoreCase("approval_required_timeline")) {
                oldTimelineVisibilities();
                //set background color yellow
                timelinePostLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                header.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                imageLoaderWithoutCircle.displayImage(childPostsBean.getFileUrl(), backgroudImage, optionsWithoutCircle);
                imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
                titlePost.setText(childPostsBean.getActionPerformFullName());
                datePost.setText(childPostsBean.getPostedDateFormatted());
                timePost.setText(childPostsBean.getPostedTimeFormatted());
                typeText.setText(childPostsBean.getActivity());
                timelinePostClick.setEnabled(true);
                timelinePostClick.setText("Approve");
            } else if (childPostsBean.getApprovalStatus().equalsIgnoreCase("approved_timeline")) {
                oldTimelineVisibilities();
                imageLoaderWithoutCircle.displayImage(childPostsBean.getFileUrl(), backgroudImage, optionsWithoutCircle);
                imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
                titlePost.setText(childPostsBean.getActionPerformFullName());
                datePost.setText(childPostsBean.getPostedDateFormatted());
                timePost.setText(childPostsBean.getPostedTimeFormatted());
                typeText.setText(childPostsBean.getActivity());
                timelinePostClick.setEnabled(false);
                timelinePostClick.setText("Approved");
                timelinePostClick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greytick, 0, 0, 0);
            } else {
                imageVisibilities();
                if (childPostsBean.getActivity().contains("Joined IFA Sport")) {
                    delete.setVisibility(View.INVISIBLE);
                } else {
//                    delete.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.INVISIBLE);
                }

                if(childPostsBean.getVideoThumbnail().equalsIgnoreCase("noVideoThumb")){

                    if(childPostsBean.getFileUrl().equalsIgnoreCase("")){
                        backgroudImage.setVisibility(View.GONE);
                    }else{
                        imageLoaderWithoutCircle.displayImage(childPostsBean.getFileUrl(), backgroudImage, optionsWithoutCircle);
                    }
                }else{
                    playVideoPic.setVisibility(View.VISIBLE);
                    imageLoaderWithoutCircle.displayImage(childPostsBean.getVideoThumbnail(), backgroudImage, optionsWithoutCircle);
                }

                //imageLoaderWithoutCircle.displayImage(childPostsBean.getFileUrl(), backgroudImage, optionsWithoutCircle);
                imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
                commentDesc.setText(Html.fromHtml(childPostsBean.getPostEntityTitle()));
                titlePost.setText(childPostsBean.getActionPerformFullName());
                datePost.setText(childPostsBean.getPostedDateFormatted());
                timePost.setText(childPostsBean.getPostedTimeFormatted());
                likeClick.setText("Likes (" + childPostsBean.getEntityLikes() + ")");
                commentClick.setText("Comments (" + childPostsBean.getEntityComments() + ")");
                typeText.setText(childPostsBean.getActivity());
                //System.out.println("HERE_else_part22:::");
            }

            if (childPostsBean.getFileUrl().equalsIgnoreCase("")) {
                backgroudImage.setVisibility(View.GONE);
            }

        } else if (childPostsBean.getLocalType().equalsIgnoreCase("challenge")) {
            challengeVisibilities();


            if (childPostsBean.getFileType().equalsIgnoreCase("")) {
                imageLayout.setVisibility(View.GONE);
            } else if (childPostsBean.getFileType().equalsIgnoreCase("image")) {
                imageLayout.setVisibility(View.VISIBLE);
                playVideoPic.setVisibility(View.GONE);
                imageLoaderWithoutCircle.displayImage(childPostsBean.getFileUrl(), backgroudImage, optionsWithoutCircle);
            } else if (childPostsBean.getFileType().equalsIgnoreCase("video")) {
                imageLayout.setVisibility(View.VISIBLE);
                playVideoPic.setVisibility(View.VISIBLE);
                imageLoaderWithoutCircle.displayImage(childPostsBean.getVideoThumbnail(), backgroudImage, optionsWithoutCircle);
            }
//
//
//            if(childPostsBean.getFileUrl().equalsIgnoreCase("")){
//                imageLayout.setVisibility(View.GONE);
//            }else{
//                imageLayout.setVisibility(View.VISIBLE);
//                if(childPostsBean.getPostEntityChallengeVideoUrl().equalsIgnoreCase("null")){
//                    playVideoPic.setVisibility(View.GONE);
//                    imageLoaderWithoutCircle.displayImage(childPostsBean.getFileUrl(), backgroudImage, optionsWithoutCircle);
//                }else {
//                    imageLoaderWithoutCircle.displayImage(childPostsBean.getVideoThumbnail(), backgroudImage, optionsWithoutCircle);
//                    playVideoPic.setVisibility(View.VISIBLE);
//                }
//            }
//            imageLoaderWithoutCircle.displayImage(childPostsBean.getFileUrl(), backgroudImage, optionsWithoutCircle);
            imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
            titlePost.setText(childPostsBean.getActionPerformFullName() + " (" + childPostsBean.getPostEntityCategoryName() + ")");
            datePost.setText(childPostsBean.getPostedDateFormatted());
            timePost.setText(childPostsBean.getPostedTimeFormatted());
            typeText.setText(childPostsBean.getActivity() + "\n" + "Expiration Date: " + childPostsBean.getPostEntityExpiration());
            chalTitle.setText(Html.fromHtml(childPostsBean.getPostEntityTitle()));
            chalDescription.setText(childPostsBean.getPostEntityDescription());
            //new change
//            chalTargetScore.setText(childPostsBean.getPostEntityTargetScore());
//            chalTargetTime.setText(childPostsBean.getPostEntityTargetTime());
            if (childPostsBean.getPostEntityTargetScore().equalsIgnoreCase("0.00") || childPostsBean.getPostEntityTargetScore().equalsIgnoreCase("0") || childPostsBean.getPostEntityTargetScore().equalsIgnoreCase("")) {
                chalTargetScoreLabel.setVisibility(View.GONE);
                chalTargetScore.setVisibility(View.GONE);
            } else {
                chalTargetScoreLabel.setVisibility(View.VISIBLE);
                chalTargetScore.setVisibility(View.VISIBLE);
                chalTargetScore.setText(childPostsBean.getPostEntityTargetScore());
            }

            if (childPostsBean.getPostEntityTargetTime().equalsIgnoreCase("0.00") || childPostsBean.getPostEntityTargetTime().equalsIgnoreCase("0") || childPostsBean.getPostEntityTargetTime().equalsIgnoreCase("")) {
                chalTargetTimeLabel.setVisibility(View.GONE);
                chalTargetTime.setVisibility(View.GONE);
            } else {
                chalTargetTimeLabel.setVisibility(View.VISIBLE);
                chalTargetTime.setVisibility(View.VISIBLE);
                chalTargetTime.setText(childPostsBean.getPostEntityTargetTime());
            }

            if (childPostsBean.getPostEntityAchievedScore().equalsIgnoreCase("0.00") || childPostsBean.getPostEntityAchievedScore().equalsIgnoreCase("0") || childPostsBean.getPostEntityAchievedScore().equalsIgnoreCase("")) {
                yourScoreLabel.setVisibility(View.GONE);
                yourScore.setVisibility(View.GONE);
            } else {
                yourScoreLabel.setVisibility(View.VISIBLE);
                yourScore.setVisibility(View.VISIBLE);
                yourScore.setText(childPostsBean.getPostEntityAchievedScore());
            }

            if (childPostsBean.getPostEntityAchievedTime().equalsIgnoreCase("0.00") || childPostsBean.getPostEntityAchievedTime().equalsIgnoreCase("0") || childPostsBean.getPostEntityAchievedTime().equalsIgnoreCase("")) {
                yourTimeLabel.setVisibility(View.GONE);
                yourTime.setVisibility(View.GONE);
            } else {
                yourTimeLabel.setVisibility(View.VISIBLE);
                yourTime.setVisibility(View.VISIBLE);
                // yourTimeLabel.setText(childPostsBean.getPostEntityAchievedScore());
                yourTime.setText(childPostsBean.getPostEntityAchievedTime());
            }

            if (childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("set_challenge")) {
                bottomLayout1.setVisibility(View.GONE);
                bottomLayout2.setVisibility(View.GONE);
                acceptClick.setText("Accept");
                ignoreClick.setText("Ignore");
                acceptClick.setEnabled(false);
                ignoreClick.setEnabled(false);


            } else if (childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("accepted_challenge")) {
                bottomLayout1.setVisibility(View.GONE);
                bottomLayout2.setVisibility(View.GONE);
                acceptClick.setText("Accepted");
                ignoreClick.setText("Ignore");
                acceptClick.setEnabled(false);
                ignoreClick.setEnabled(false);
                acceptClick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greytick, 0, 0, 0);
                ignoreClick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greycross, 0, 0, 0);

            } else if (childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("ignored_challenge")) {
                bottomLayout1.setVisibility(View.GONE);
                bottomLayout2.setVisibility(View.VISIBLE);
                acceptClick.setText("Accept");
                ignoreClick.setText("Ignored");
                acceptClick.setEnabled(false);
                ignoreClick.setEnabled(false);
                acceptClick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greytick, 0, 0, 0);
                ignoreClick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greycross, 0, 0, 0);
            } else if (childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("achieved_challenge")) {
                bottomLayout1.setVisibility(View.VISIBLE);
                bottomLayout2.setVisibility(View.GONE);
                likeClick.setText("Likes (" + childPostsBean.getEntityLikes() + ")");
                commentClick.setText("Comments (" + childPostsBean.getEntityComments() + ")");

            } else if (childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("approval_required_challenge")) {
                if (childPostsBean.getApprovedRequiredBy().equalsIgnoreCase("parent")) {
                    bottomLayout1.setVisibility(View.GONE);
                    bottomLayout2.setVisibility(View.VISIBLE);

                    //set background color yellow
                    challengeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                    header.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                    bottomLayout2.setBackgroundColor(context.getResources().getColor(R.color.yellow));

                    acceptClick.setText("Approve");
                    ignoreClick.setVisibility(View.INVISIBLE);
                    chalShareClick.setVisibility(View.INVISIBLE);
                    acceptClick.setEnabled(true);
                } else if (childPostsBean.getApprovedRequiredBy().equalsIgnoreCase("coach")) {
                    bottomLayout1.setVisibility(View.GONE);
                    bottomLayout2.setVisibility(View.VISIBLE);
                    acceptClick.setText("Approval required by Coach");
                    ignoreClick.setVisibility(View.INVISIBLE);
                    chalShareClick.setVisibility(View.INVISIBLE);
                    acceptClick.setEnabled(false);
                    acceptClick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greytick, 0, 0, 0);
                }

            } else if (childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("approved_challenge")) {
                bottomLayout1.setVisibility(View.GONE);
                bottomLayout2.setVisibility(View.VISIBLE);
                acceptClick.setText("Approved");
                ignoreClick.setVisibility(View.INVISIBLE);
                chalShareClick.setVisibility(View.INVISIBLE);
                acceptClick.setEnabled(false);
                acceptClick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greytick, 0, 0, 0);
            }
        } else if (childPostsBean.getLocalType().equalsIgnoreCase("friend")) {
            friendLayoutVisibilities();

            if(childPostsBean.getApprovalStatus().equalsIgnoreCase("approval_required_friend_reqst")){
                friendLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                header.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            }
            if(childPostsBean.getApprovalStatus().equalsIgnoreCase("approval_required_friend_reqst_sent")){
                friendLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                header.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            }

            imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
            commentDesc.setText(Html.fromHtml(childPostsBean.getPostEntityTitle()));
            titlePost.setText(childPostsBean.getActionPerformFullName());
            datePost.setText(childPostsBean.getPostedDateFormatted());
            timePost.setText(childPostsBean.getPostedTimeFormatted());
            typeText.setText(childPostsBean.getActivity());

            if (childPostsBean.getRequestState().equalsIgnoreCase("0")) {
                acceptFriend.setText("Accept");
                rejectFriend.setText("Reject");
                acceptFriend.setEnabled(true);
                rejectFriend.setEnabled(true);
            } else if (childPostsBean.getRequestState().equalsIgnoreCase("1")) {
                acceptFriend.setText("Accepted");
                rejectFriend.setText("Reject");
                acceptFriend.setEnabled(false);
                rejectFriend.setEnabled(false);
                acceptFriend.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greytick, 0, 0, 0);
                rejectFriend.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greycross, 0, 0, 0);
            } else if (childPostsBean.getRequestState().equalsIgnoreCase("-1")) {
                acceptFriend.setText("Accept");
                rejectFriend.setText("Rejected");
                acceptFriend.setEnabled(false);
                rejectFriend.setEnabled(false);
                acceptFriend.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greytick, 0, 0, 0);
                rejectFriend.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greycross, 0, 0, 0);
            } else {


                if(childPostsBean.getApprovalStatus().equalsIgnoreCase("approved_friend_reqst_sent") || childPostsBean.getApprovalStatus().equalsIgnoreCase("approved_friend_reqst_sent")
                        || childPostsBean.getApprovalStatus().equalsIgnoreCase("approved_friend_reqst") || childPostsBean.getApprovalStatus().equalsIgnoreCase("approved_friend_reqst")){
                 acceptFriend.setText("Accept");
                rejectFriend.setText("Reject");
                acceptFriend.setEnabled(false);
                rejectFriend.setEnabled(false);
                acceptFriend.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greytick, 0, 0, 0);
                rejectFriend.setCompoundDrawablesWithIntrinsicBounds(R.drawable.greycross, 0, 0, 0);
                }else{
                    acceptFriend.setEnabled(true);
                    rejectFriend.setEnabled(true);

                    acceptFriend.setText("Accept");
                    rejectFriend.setText("Reject");
                }
                }


        }
//        else if(childPostsBean.getLocalType().equalsIgnoreCase("workout")){
//            bottomLayout1.setVisibility(View.GONE);
//            bottomLayout2.setVisibility(View.GONE);
//            header.setVisibility(View.GONE);
//            imageLayout.setVisibility(View.GONE);
//            timelineLayout.setVisibility(View.GONE);
//        }

        timelinePostClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCountPosition = position;
                likeValue = Integer.parseInt(childPostsBean.getEntityLikes());
                final List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(new BasicNameValuePair("post_id", childPostsBean.getId()));

                final ArrayList<String> headers = new ArrayList<>();
                headers.add("X-access-uid:" + loggedInUser.getId());
                headers.add("X-access-token:" + loggedInUser.getToken());

                if (Utilities.isNetworkAvailable(context)) {
                    dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.child_dialog_approve_post_password);
                    dialog.setTitle(R.string.ifa_dialog);

                    // set the custom dialog components - text, image and button
                    final TextView label = (TextView) dialog.findViewById(R.id.label);
                    final EditText parentPassword = (EditText) dialog.findViewById(R.id.parentPassword);
                    Button submit = (Button) dialog.findViewById(R.id.submit);

                    label.setTypeface(helvetica);
                    parentPassword.setTypeface(helvetica);
                    submit.setTypeface(linoType);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String parentPasswordStr = parentPassword.getText().toString();
                            if (parentPasswordStr == null || parentPasswordStr.isEmpty()) {
                                Toast.makeText(context, "Please enter Parent Password", Toast.LENGTH_LONG).show();
                            } else {

                                if (Utilities.isNetworkAvailable(context)) {
                                    nameValuePairList.add(new BasicNameValuePair("entity_id", childPostsBean.getEntityId()));
                                    nameValuePairList.add(new BasicNameValuePair("password", parentPasswordStr));
                                    String webServiceUrl = Utilities.BASE_URL + "user_posts/approve_timeline_by_parent_password";
                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, APPROVAL_TIMELINE, ParentChildPostAdapter.this, headers);
                                    postWebServiceAsync.execute(webServiceUrl);
                                } else {
                                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                    dialog.show();

                } else {
                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        acceptFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utilities.isNetworkAvailable(context)) {
                    if(childPostsBean.getChallengeAction().equalsIgnoreCase("approval_required_friend_reqst")){

                        friendPosition = position;
                        clickFriendButton = "accept";
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("post_id",  childPostsBean.getId()));
                        nameValuePairList.add(new BasicNameValuePair("entity_id",  childPostsBean.getEntityId()));
                        nameValuePairList.add(new BasicNameValuePair("child_id",  childId));
                        nameValuePairList.add(new BasicNameValuePair("friend_id",  childPostsBean.getChallengeActionPerformedId()));
                        nameValuePairList.add(new BasicNameValuePair("state", "1"));

                        String webServiceUrl = Utilities.BASE_URL + "user_posts/approve_friend_rqst_by_parent";


                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:" + loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_OR_REJECT_APPROVAL_REQUEST, ParentChildPostAdapter.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);



                    }else if(childPostsBean.getChallengeAction().equalsIgnoreCase("approval_required_friend_reqst_sent")){

                        friendPosition = position;
                        clickFriendButton = "accept";
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("post_id",  childPostsBean.getId()));
                        nameValuePairList.add(new BasicNameValuePair("entity_id",  childPostsBean.getEntityId()));
                        nameValuePairList.add(new BasicNameValuePair("child_id",  childId));
                        nameValuePairList.add(new BasicNameValuePair("friend_id",  childPostsBean.getChallengeActionPerformedId()));
                        nameValuePairList.add(new BasicNameValuePair("friend_state", "1"));

                        String webServiceUrl = Utilities.BASE_URL + "user_posts/approve_friend_rqst_sent_by_parent";


                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:" + loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_OR_REJECT_APPROVAL_REQUEST, ParentChildPostAdapter.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    }else{
                        friendPosition = position;
                        clickFriendButton = "accept";
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("friend_id",  childPostsBean.getChallengeActionPerformedId()));
                        nameValuePairList.add(new BasicNameValuePair("state", "1"));

                        String webServiceUrl = Utilities.BASE_URL + "children/change_friend_status";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:" + loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_OR_REJECT_FRIEND, ParentChildPostAdapter.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);
                    }

                } else {
                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

//        acceptFriend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Utilities.isNetworkAvailable(context)) {
//                    friendPosition = position;
//                    clickFriendButton = "accept";
//                    List<NameValuePair> nameValuePairList = new ArrayList<>();
//                    nameValuePairList.add(new BasicNameValuePair("friend_id", childPostsBean.getChallengeActionPerformedId()));
//                    nameValuePairList.add(new BasicNameValuePair("state", "1"));
//
//                    String webServiceUrl = Utilities.BASE_URL + "children/change_friend_status";
//
//                    ArrayList<String> headers = new ArrayList<>();
//                    headers.add("X-access-uid:" + loggedInUser.getId());
//                    headers.add("X-access-token:" + loggedInUser.getToken());
//
//                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_OR_REJECT_FRIEND, ParentChildPostAdapter.this, headers);
//                    postWebServiceAsync.execute(webServiceUrl);
//                } else {
//                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        rejectFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utilities.isNetworkAvailable(context)) {
                    if(childPostsBean.getChallengeAction().equalsIgnoreCase("approval_required_friend_reqst")){

                        friendPosition = position;
                        clickFriendButton = "reject";
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("post_id", childPostsBean.getId()));
                        nameValuePairList.add(new BasicNameValuePair("entity_id",  childPostsBean.getEntityId()));
                        nameValuePairList.add(new BasicNameValuePair("child_id",  childId));
                        nameValuePairList.add(new BasicNameValuePair("friend_id",  childPostsBean.getChallengeActionPerformedId()));
                        nameValuePairList.add(new BasicNameValuePair("state", "-1"));

                        String webServiceUrl = Utilities.BASE_URL + "user_posts/approve_friend_rqst_by_parent";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:" + loggedInUser.getId());
                        headers.add("X-access-token:" + loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_OR_REJECT_APPROVAL_REQUEST, ParentChildPostAdapter.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);



                    }else if(childPostsBean.getChallengeAction().equalsIgnoreCase("approval_required_friend_reqst_sent")){

                        friendPosition = position;
                        clickFriendButton = "reject";
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("post_id", childPostsBean.getId()));
                        nameValuePairList.add(new BasicNameValuePair("entity_id",  childPostsBean.getEntityId()));
                        nameValuePairList.add(new BasicNameValuePair("child_id",  childId));
                        nameValuePairList.add(new BasicNameValuePair("friend_id",  childPostsBean.getChallengeActionPerformedId()));
                        nameValuePairList.add(new BasicNameValuePair("friend_state", "-1"));

                        String webServiceUrl = Utilities.BASE_URL + "user_posts/approve_friend_rqst_sent_by_parent";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:" + loggedInUser.getId());
                        headers.add("X-access-token:" + loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_OR_REJECT_APPROVAL_REQUEST, ParentChildPostAdapter.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);



                    }else{
                        friendPosition = position;
                        clickFriendButton = "reject";
                        final List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("friend_id", childPostsBean.getChallengeActionPerformedId()));
                        nameValuePairList.add(new BasicNameValuePair("state", "-1"));

                        String webServiceUrl = Utilities.BASE_URL + "children/change_friend_status";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:"+loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_OR_REJECT_FRIEND, ParentChildPostAdapter.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);
                    }




                }else{
                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }

            }
        });

//        rejectFriend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Utilities.isNetworkAvailable(context)) {
//                    friendPosition = position;
//                    clickFriendButton = "reject";
//                    final List<NameValuePair> nameValuePairList = new ArrayList<>();
//                    nameValuePairList.add(new BasicNameValuePair("friend_id", childPostsBean.getChallengeActionPerformedId()));
//                    nameValuePairList.add(new BasicNameValuePair("state", "-1"));
//
//                    String webServiceUrl = Utilities.BASE_URL + "children/change_friend_status";
//
//                    ArrayList<String> headers = new ArrayList<>();
//                    headers.add("X-access-uid:" + loggedInUser.getId());
//                    headers.add("X-access-token:" + loggedInUser.getToken());
//
//                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_OR_REJECT_FRIEND, ParentChildPostAdapter.this, headers);
//                    postWebServiceAsync.execute(webServiceUrl);
//                } else {
//                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        // image and video clicks
        likeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childPostsBean.getIsLikeByYou().equalsIgnoreCase("false")) {
                    likeCountPosition = position;
                    likeValue = Integer.parseInt(childPostsBean.getEntityLikes());
                    final List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("post_id", childPostsBean.getId()));

                    final ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());
                    if (childPostsBean.getLocalType().equalsIgnoreCase("timeline")) {
                        if (childPostsBean.getApprovalStatus().equalsIgnoreCase("approval_required_timeline")) {

                            dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.child_dialog_approve_post_password);
                            dialog.setTitle(R.string.ifa_dialog);

                            // set the custom dialog components - text, image and button
                            final TextView label = (TextView) dialog.findViewById(R.id.label);
                            final EditText parentPassword = (EditText) dialog.findViewById(R.id.parentPassword);
                            Button submit = (Button) dialog.findViewById(R.id.submit);

                            label.setTypeface(helvetica);
                            parentPassword.setTypeface(helvetica);
                            submit.setTypeface(linoType);

                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String parentPasswordStr = parentPassword.getText().toString();
                                    if (parentPasswordStr == null || parentPasswordStr.isEmpty()) {
                                        Toast.makeText(context, "Please enter Parent Password", Toast.LENGTH_LONG).show();
                                    } else {

                                        if (Utilities.isNetworkAvailable(context)) {
                                            nameValuePairList.add(new BasicNameValuePair("entity_id", childPostsBean.getEntityId()));
                                            nameValuePairList.add(new BasicNameValuePair("password", parentPasswordStr));
                                            String webServiceUrl = Utilities.BASE_URL + "user_posts/approve_timeline_by_parent_password";
                                            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, APPROVAL_TIMELINE, ParentChildPostAdapter.this, headers);
                                            postWebServiceAsync.execute(webServiceUrl);
                                        } else {
                                            Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                            dialog.show();
                        } else {
                            String webServiceUrl = Utilities.BASE_URL + "user_posts/like";
                            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, LIKE_UNLIKE, ParentChildPostAdapter.this, headers);
                            postWebServiceAsync.execute(webServiceUrl);
                        }

                    } else if (childPostsBean.getLocalType().equalsIgnoreCase("image") || childPostsBean.getLocalType().equalsIgnoreCase("video") || childPostsBean.getLocalType().equalsIgnoreCase("challenge") || childPostsBean.getLocalType().equalsIgnoreCase("workout")) {
                        String webServiceUrl = Utilities.BASE_URL + "user_posts/like";
                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, LIKE_UNLIKE, ParentChildPostAdapter.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    }
                } else {
                    if (childPostsBean.getLocalType().equalsIgnoreCase("image") || childPostsBean.getLocalType().equalsIgnoreCase("timeline") || childPostsBean.getLocalType().equalsIgnoreCase("video") || childPostsBean.getLocalType().equalsIgnoreCase("workout")) {
                        Intent obj = new Intent(context, ChildAllCommentsReadScreen.class);
                        obj.putExtra("entityId", childPostsBean.getEntityId());
                        obj.putExtra("entityType", childPostsBean.getLocalType());
                        obj.putExtra("postId", childPostsBean.getId());
                        obj.putExtra("name", childPostsBean.getActionPerformFullName());
                        obj.putExtra("whichType", "likes");
                        // new change
                        obj.putExtra("totalLikes", childPostsBean.getEntityLikes());
                        obj.putExtra("totalComments", childPostsBean.getEntityComments());
                        context.startActivity(obj);
                    } else if (childPostsBean.getLocalType().equalsIgnoreCase("challenge")) {
                        Intent obj = new Intent(context, ChildAllCommentsReadScreen.class);
                        if (childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("achieved_challenge")) {
                            obj.putExtra("entityId", childPostsBean.getChallengeResultId());
                        } else {
                            obj.putExtra("entityId", childPostsBean.getEntityId());
                        }

                        obj.putExtra("entityType", childPostsBean.getLocalType());
                        obj.putExtra("postId", childPostsBean.getId());
                        obj.putExtra("name", childPostsBean.getActionPerformFullName());
                        obj.putExtra("whichType", "likes");
                        // new change
                        obj.putExtra("totalLikes", childPostsBean.getEntityLikes());
                        obj.putExtra("totalComments", childPostsBean.getEntityComments());
                        context.startActivity(obj);
                    }
                }
            }
        });

        commentClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(childPostsBean.getLocalType().equalsIgnoreCase("image")||childPostsBean.getLocalType().equalsIgnoreCase("timeline")||childPostsBean.getLocalType().equalsIgnoreCase("video")||childPostsBean.getLocalType().equalsIgnoreCase("challenge")||childPostsBean.getLocalType().equalsIgnoreCase("workout")) {
//                    Intent obj = new Intent(context, ChildAllCommentsReadScreen.class);
//                    obj.putExtra("entityId", childPostsBean.getEntityId());
//                    obj.putExtra("entityType", childPostsBean.getLocalType());
//                    obj.putExtra("postId", childPostsBean.getId());
//                    obj.putExtra("name", childPostsBean.getActionPerformFullName());
//                    obj.putExtra("whichType", "comments");
//                    // new change
//                    obj.putExtra("totalLikes", childPostsBean.getEntityLikes());
//                    obj.putExtra("totalComments", childPostsBean.getEntityComments());
//                    context.startActivity(obj);
//                }
                if (childPostsBean.getLocalType().equalsIgnoreCase("image") || childPostsBean.getLocalType().equalsIgnoreCase("timeline") || childPostsBean.getLocalType().equalsIgnoreCase("video") || childPostsBean.getLocalType().equalsIgnoreCase("workout")) {
                    Intent obj = new Intent(context, ChildAllCommentsReadScreen.class);
                    obj.putExtra("entityId", childPostsBean.getEntityId());
                    obj.putExtra("entityType", childPostsBean.getLocalType());
                    obj.putExtra("postId", childPostsBean.getId());
                    obj.putExtra("name", childPostsBean.getActionPerformFullName());
                    obj.putExtra("whichType", "comments");
                    // new change
                    obj.putExtra("totalLikes", childPostsBean.getEntityLikes());
                    obj.putExtra("totalComments", childPostsBean.getEntityComments());
                    context.startActivity(obj);
                } else if (childPostsBean.getLocalType().equalsIgnoreCase("challenge")) {
                    Intent obj = new Intent(context, ChildAllCommentsReadScreen.class);
                    if (childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("achieved_challenge")) {
                        obj.putExtra("entityId", childPostsBean.getChallengeResultId());
                    } else {
                        obj.putExtra("entityId", childPostsBean.getEntityId());
                    }

                    obj.putExtra("entityType", childPostsBean.getLocalType());
                    obj.putExtra("postId", childPostsBean.getId());
                    obj.putExtra("name", childPostsBean.getActionPerformFullName());
                    obj.putExtra("whichType", "comments");
                    // new change
                    obj.putExtra("totalLikes", childPostsBean.getEntityLikes());
                    obj.putExtra("totalComments", childPostsBean.getEntityComments());
                    context.startActivity(obj);
                }
            }
        });

        backgroudImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childPostsBean.getLocalType().equalsIgnoreCase("video")) {
                    if(childPostsBean.getYoutubeUrl().equalsIgnoreCase("")){
                        Intent obj = new Intent(context, ChildPostViewScreen.class);
                        obj.putExtra("type", "video");
                        obj.putExtra("url", childPostsBean.getFileUrl());
                        context.startActivity(obj);
                    }else{
                        Intent obj = new Intent(context, ChildPostViewScreen.class);
                        obj.putExtra("type", "video");
                        obj.putExtra("url", childPostsBean.getFileUrl());
//                        obj.putExtra("youTubeId", childPostsBean.getYouTubeEmbedId());
                        context.startActivity(obj);
                    }

                } else if (childPostsBean.getLocalType().equalsIgnoreCase("challenge")) {
                    if (childPostsBean.getFileType().equalsIgnoreCase("image")) {
                        Intent obj = new Intent(context, ChildPostViewScreen.class);
                        obj.putExtra("type", "image");
                        obj.putExtra("url", childPostsBean.getFileUrl());
                    } else if (childPostsBean.getFileType().equalsIgnoreCase("video")) {
                        Intent obj = new Intent(context, ChildPostViewScreen.class);
                        obj.putExtra("type", "video");
                        obj.putExtra("url", childPostsBean.getFileUrl());
                        //obj.putExtra("youTubeId", childPostsBean.getYouTubeEmbedId());
                    }
                } else if (childPostsBean.getLocalType().equalsIgnoreCase("image")) {
                    Intent obj = new Intent(context, ChildPostViewScreen.class);
                    obj.putExtra("type", "image");
                    obj.putExtra("url", childPostsBean.getFileUrl());
                    context.startActivity(obj);
                } else if (childPostsBean.getLocalType().equalsIgnoreCase("timeline")) {
//                    if (childPostsBean.getFileType().contains("image")) {
//                        Intent obj = new Intent(context, ChildPostViewScreen.class);
//                        obj.putExtra("type", "image");
//                        obj.putExtra("url", childPostsBean.getFileUrl());
//                        context.startActivity(obj);
//                    } else if (childPostsBean.getFileType().contains("video")) {
//                        Intent obj = new Intent(context, ChildPostViewScreen.class);
//                        obj.putExtra("type", "video");
//                        obj.putExtra("url", childPostsBean.getFileUrl());
//                       // obj.putExtra("youTubeId", childPostsBean.getYouTubeEmbedId());
//                        context.startActivity(obj);
//                    } else {
//                        //System.out.println("else_FileURL::" + childPostsBean.getFileUrl());
//                    }
                    if (childPostsBean.getFileType().contains("image")) {
                        Intent obj = new Intent(context, ChildPostViewScreen.class);
                        obj.putExtra("type", "image");
                        obj.putExtra("url", childPostsBean.getFileUrl());
                        context.startActivity(obj);
                    }else if (childPostsBean.getFileType().contains("video")) {
                        if(childPostsBean.getYoutubeUrl().equalsIgnoreCase("")){
                            Intent obj = new Intent(context, ChildPostViewScreen.class);
                            obj.putExtra("type", "video");
                            obj.putExtra("url", childPostsBean.getFileUrl());
                            context.startActivity(obj);
                        }else{
                            Intent obj = new Intent(context, ChildPostViewScreen.class);
                            obj.putExtra("type", "video");
                            obj.putExtra("url", childPostsBean.getFileUrl());
                            //  obj.putExtra("youTubeId", childPostsBean.getYouTubeEmbedId());
                            context.startActivity(obj);
                        }

                    }


                }
            }
        });

        shareClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.child_dialog_share_post);

                final Button ifaShare = (Button) dialog.findViewById(R.id.ifaShare);
                final Button facebookShare = (Button) dialog.findViewById(R.id.facebookShare);
                final Button twitterShare = (Button) dialog.findViewById(R.id.twitterShare);
                final String url = childPostsBean.getFileUrl();
                final String title = childPostsBean.getPostEntityTitle();


                ifaShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Utilities.isNetworkAvailable(context)) {
                            sharedPosition = position;
                            if (childPostsBean.getChallengeIsShared().equalsIgnoreCase("1")) {
                                Toast.makeText(context, "Already shared.", Toast.LENGTH_SHORT).show();
                            } else {
                                List<NameValuePair> nameValuePairList = new ArrayList<>();
                                nameValuePairList.add(new BasicNameValuePair("post_id", childPostsBean.getId()));

                                String webServiceUrl = Utilities.BASE_URL + "user_posts/share";

                                ArrayList<String> headers = new ArrayList<>();
                                headers.add("X-access-uid:" + loggedInUser.getId());
                                headers.add("X-access-token:" + loggedInUser.getToken());

                                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, POST_SHARE, ParentChildPostAdapter.this, headers);
                                postWebServiceAsync.execute(webServiceUrl);
                            }

                        } else {
                            Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // automatically click for ifa share, in timeline

                if (childPostsBean.getLocalType().equalsIgnoreCase("timeline") || childPostsBean.getLocalType().equalsIgnoreCase("challenge")) {
                    if (childPostsBean.getChallengeIsShared().equalsIgnoreCase("1")) {
                        Toast.makeText(context, "Already shared.", Toast.LENGTH_SHORT).show();
                    } else {
                        ifaShare.performClick();
                    }
                }


                facebookShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ShareDialog.canShow(ShareLinkContent.class)) {
                            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                    .setContentTitle(childPostsBean.getPostEntityTitle())
                                    .setContentUrl(Uri.parse(childPostsBean.getFileUrl()))
                                    .build();

                            shareDialog.show(linkContent);
                        }
                        dialog.cancel();
                    }
                });

                twitterShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        TweetComposer.Builder builder = new TweetComposer.Builder(context)
//                                .text(childPostsBean.getPostEntityTitle() + "\n" + childPostsBean.getFileUrl());
//                        builder.show();
//                        dialog.cancel();
                    }
                });

                dialog.show();
            }

        });

        postScoreTimelineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ((ChildMainScreen) context).showViewMarks();

                AgeGroupAttendanceBean ageGroupAttendanceBean = new AgeGroupAttendanceBean();
                ageGroupAttendanceBean.setUsersId(childPostsBean.getUserId());
                ageGroupAttendanceBean.setSessionsId(childPostsBean.getChildPostAdditionalTimelineDataBeans().get(position).getDataElementSessionId());

                if(childPostsBean.getChallengePostParentId().equalsIgnoreCase("0")){
                    if (childPostsBean.getPostAddDataType().equalsIgnoreCase("post_timeline_score")) {

                        //Pankaj 19 Jan 2018
                        ageGroupAttendanceBean.setReportDate(childPostsBean.getChildPostAdditionalTimelineDataBeans().get(position).getReportDate());
                        ageGroupAttendanceBean.setBookingDate(childPostsBean.getChildPostAdditionalTimelineDataBeans().get(position).getReportDate());

                        Intent childReport = new Intent(context, ParentChildReportScreen.class);
                        childReport.putExtra("clickedOnAgeGroup", ageGroupAttendanceBean);
                        context.startActivity(childReport);
                    } else if (childPostsBean.getPostAddDataType().equalsIgnoreCase("post_timeline_activity")) {
                        if (!childPostsBean.getPostTimelineActivityType().equalsIgnoreCase("track_details")) {
                            Intent childReport = new Intent(context, ParentChildActivityStatsScreen.class);
                            childReport.putExtra("clickedOnAgeGroup", ageGroupAttendanceBean);
                            context.startActivity(childReport);
                        }
                    }
                }


            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure you want to delete this post?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if (Utilities.isNetworkAvailable(context)) {
                                    positionValue = position;
                                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                                    nameValuePairList.add(new BasicNameValuePair("post_id", childPostsBean.getId()));

                                    String webServiceUrl = Utilities.BASE_URL + "user_posts/delete";

                                    ArrayList<String> headers = new ArrayList<>();
                                    headers.add("X-access-uid:" + loggedInUser.getId());
                                    headers.add("X-access-token:" + loggedInUser.getToken());

                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, POST_DELETE, ParentChildPostAdapter.this, headers);
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


        acceptClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(context)) {

                    acceptCountPosition = position;
                    final List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("child_id", childPostsBean.getChallengeActionPerformedId()));
                    nameValuePairList.add(new BasicNameValuePair("challenges_id", childPostsBean.getChallengeEntityId()));
                    nameValuePairList.add(new BasicNameValuePair("post_id", childPostsBean.getId()));

                    final ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    String webServiceUrl = Utilities.BASE_URL + "challenges/approve_challenge_by_parent";
                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_CHALLENGE, ParentChildPostAdapter.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);



//                    if (childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("set_challenge")) {
//                        String webServiceUrl = Utilities.BASE_URL + "user_posts/accept_challenge";
//                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_CHALLENGE, ParentChildPostAdapter.this, headers);
//                        postWebServiceAsync.execute(webServiceUrl);
//
//                    } else if (childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("approval_required_challenge")) {
//                        if (childPostsBean.getApprovedRequiredBy().equalsIgnoreCase("parent")) {
//                            dialog = new Dialog(context);
//                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            dialog.setContentView(R.layout.child_dialog_approve_post_password);
//                            dialog.setTitle("IFA...");
//
//                            // set the custom dialog components - text, image and button
//                            final TextView label = (TextView) dialog.findViewById(R.id.label);
//                            final EditText parentPassword = (EditText) dialog.findViewById(R.id.parentPassword);
//                            Button submit = (Button) dialog.findViewById(R.id.submit);
//
//                            label.setTypeface(helvetica);
//                            parentPassword.setTypeface(helvetica);
//                            submit.setTypeface(linoType);
//
//                            submit.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    String parentPasswordStr = parentPassword.getText().toString();
//                                    if (parentPasswordStr == null || parentPasswordStr.isEmpty()) {
//                                        Toast.makeText(context, "Please enter Parent Password", Toast.LENGTH_LONG).show();
//                                    } else {
//
//                                        if (Utilities.isNetworkAvailable(context)) {
//                                            nameValuePairList.add(new BasicNameValuePair("challenges_id", childPostsBean.getChallengeEntityId()));
//                                            nameValuePairList.add(new BasicNameValuePair("password", parentPasswordStr));
//                                            String webServiceUrl = Utilities.BASE_URL + "challenges/approve_challenge_by_parent_password";
//                                            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, APPROVAL_CHALLENGE, ParentChildPostAdapter.this, headers);
//                                            postWebServiceAsync.execute(webServiceUrl);
//                                        } else {
//                                            Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//                            });
//
//                            dialog.show();
//                        }
//                    }
                } else {
                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }

            }
        });

        ignoreClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(context)) {
                    ignoreCountPosition = position;
                    //System.out.println("ingored_challenge_called");
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("post_id", childPostsBean.getId()));
                    String webServiceUrl = Utilities.BASE_URL + "user_posts/ignore_challenge";
                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());
                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, IGNORE_CHALLENGE, ParentChildPostAdapter.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    public void imageVisibilities() {
        timelinePostLayout.setVisibility(View.GONE);
        friendLayout.setVisibility(View.GONE);
        suggestionLayout.setVisibility(View.GONE);
        bottomLayout1.setVisibility(View.VISIBLE);
        header.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.VISIBLE);
        timelineLayout.setVisibility(View.GONE);
        playVideoPic.setVisibility(View.GONE);
        bottomLayout2.setVisibility(View.GONE);
        commentDesc.setVisibility(View.VISIBLE);
//        delete.setVisibility(View.VISIBLE);
        delete.setVisibility(View.INVISIBLE);
        challengeLayout.setVisibility(View.GONE);
    }

    public void workoutVisibilities() {
        timelinePostLayout.setVisibility(View.GONE);
        friendLayout.setVisibility(View.GONE);
        suggestionLayout.setVisibility(View.GONE);
        bottomLayout1.setVisibility(View.VISIBLE);
        header.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.GONE);
        timelineLayout.setVisibility(View.GONE);
        playVideoPic.setVisibility(View.GONE);
        bottomLayout2.setVisibility(View.GONE);
        commentDesc.setVisibility(View.VISIBLE);
//        delete.setVisibility(View.VISIBLE);
        delete.setVisibility(View.INVISIBLE);
        challengeLayout.setVisibility(View.GONE);
    }

    public void videoVisibilities() {
        timelinePostLayout.setVisibility(View.GONE);
        friendLayout.setVisibility(View.GONE);
        suggestionLayout.setVisibility(View.GONE);
        bottomLayout1.setVisibility(View.VISIBLE);
        header.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.VISIBLE);
        timelineLayout.setVisibility(View.GONE);
        playVideoPic.setVisibility(View.VISIBLE);
        bottomLayout2.setVisibility(View.GONE);
        commentDesc.setVisibility(View.VISIBLE);
//        delete.setVisibility(View.VISIBLE);
        delete.setVisibility(View.INVISIBLE);
        challengeLayout.setVisibility(View.GONE);
    }

    public void timelinePostScoreVisibilities() {
        timelinePostLayout.setVisibility(View.GONE);
        friendLayout.setVisibility(View.GONE);
        suggestionLayout.setVisibility(View.GONE);
        bottomLayout1.setVisibility(View.VISIBLE);
        header.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.GONE);
        timelineLayout.setVisibility(View.VISIBLE);
        playVideoPic.setVisibility(View.GONE);
        bottomLayout2.setVisibility(View.GONE);
        commentDesc.setVisibility(View.GONE);
//        delete.setVisibility(View.GONE);
        delete.setVisibility(View.INVISIBLE);
        challengeLayout.setVisibility(View.GONE);
    }

    public void oldTimelineVisibilities() {
        timelinePostLayout.setVisibility(View.VISIBLE);
        friendLayout.setVisibility(View.GONE);
        suggestionLayout.setVisibility(View.GONE);
        bottomLayout1.setVisibility(View.GONE);
        header.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.GONE);
        timelineLayout.setVisibility(View.GONE);
        playVideoPic.setVisibility(View.GONE);
        bottomLayout2.setVisibility(View.GONE);
        commentDesc.setVisibility(View.GONE);
//        delete.setVisibility(View.GONE);
        delete.setVisibility(View.INVISIBLE);
        challengeLayout.setVisibility(View.GONE);

    }

    public void challengeVisibilities() {
        timelinePostLayout.setVisibility(View.GONE);
        friendLayout.setVisibility(View.GONE);
        suggestionLayout.setVisibility(View.GONE);
        bottomLayout1.setVisibility(View.GONE);
        header.setVisibility(View.VISIBLE);
        timelineLayout.setVisibility(View.GONE);
        playVideoPic.setVisibility(View.GONE);
        bottomLayout2.setVisibility(View.VISIBLE);
        commentDesc.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        challengeLayout.setVisibility(View.VISIBLE);
    }

    public void friendSuggestionVisibilities() {
        timelinePostLayout.setVisibility(View.GONE);
        friendLayout.setVisibility(View.GONE);
        suggestionLayout.setVisibility(View.VISIBLE);
        bottomLayout1.setVisibility(View.GONE);
        header.setVisibility(View.GONE);
        imageLayout.setVisibility(View.GONE);
        timelineLayout.setVisibility(View.GONE);
        playVideoPic.setVisibility(View.GONE);
        bottomLayout2.setVisibility(View.GONE);
        commentDesc.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        challengeLayout.setVisibility(View.GONE);
    }

    public void friendLayoutVisibilities() {
        timelinePostLayout.setVisibility(View.GONE);
        friendLayout.setVisibility(View.VISIBLE);
        suggestionLayout.setVisibility(View.GONE);
        bottomLayout1.setVisibility(View.GONE);
        header.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.GONE);
        timelineLayout.setVisibility(View.GONE);
        playVideoPic.setVisibility(View.GONE);
        bottomLayout2.setVisibility(View.GONE);
        commentDesc.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        challengeLayout.setVisibility(View.GONE);
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case LIKE_UNLIKE:

                if (response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            if (responseObject.getString("next_action").equalsIgnoreCase("Unlike")) {
                                likeValue = likeValue + 1;
                                childPostsBeanArrayList.get(likeCountPosition).setEntityLikes("" + likeValue);
                                childPostsBeanArrayList.get(likeCountPosition).setIsLikeByYou("true");
                                notifyDataSetChanged();

                            } else if (responseObject.getString("next_action").equalsIgnoreCase("like")) {
                                likeValue = likeValue - 1;
                                childPostsBeanArrayList.get(likeCountPosition).setEntityLikes("" + likeValue);
                                childPostsBeanArrayList.get(likeCountPosition).setIsLikeByYou("true");
                                notifyDataSetChanged();
                            }

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;

            case POST_DELETE:
                if (response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            childPostsBeanArrayList.remove(positionValue);
                            notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;

            case POST_SHARE:
                if (response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            childPostsBeanArrayList.get(sharedPosition).setChallengeIsShared("1");
                            notifyDataSetChanged();
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                        dialog.cancel();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }


                }
                break;

            case ACCEPT_CHALLENGE:
                if (response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            childPostsBeanArrayList.get(acceptCountPosition).setChallengeChallengeStatus("accepted_challenge");
                            notifyDataSetChanged();
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;

            case IGNORE_CHALLENGE:
                if (response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            childPostsBeanArrayList.get(ignoreCountPosition).setChallengeChallengeStatus("ignored_challenge");
                            notifyDataSetChanged();
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;

            case APPROVAL_CHALLENGE:
                if (response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            childPostsBeanArrayList.get(acceptCountPosition).setApprovalStatus("approved_challenge");
                            notifyDataSetChanged();
                            dialog.cancel();
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.cancel();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case ACCEPT_OR_REJECT_FRIEND:
                if (response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if (status) {
                            if (clickFriendButton.equalsIgnoreCase("accept")) {
                                childPostsBeanArrayList.get(friendPosition).setRequestState("1");
                            } else if (clickFriendButton.equalsIgnoreCase("reject")) {
                                childPostsBeanArrayList.get(friendPosition).setRequestState("-1");
                            }
                            notifyDataSetChanged();
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case ACCEPT_OR_REJECT_APPROVAL_REQUEST:
                if(response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if(status){
                            if(clickFriendButton.equalsIgnoreCase("accept")){
                                childPostsBeanArrayList.get(friendPosition).setRequestState("1");
                            }else if(clickFriendButton.equalsIgnoreCase("reject")){
                                childPostsBeanArrayList.get(friendPosition).setRequestState("-1");
                            }
                            notifyDataSetChanged();
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case APPROVAL_TIMELINE:
                if (response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            childPostsBeanArrayList.get(likeCountPosition).setApprovalStatus("approved_timeline");
                            notifyDataSetChanged();
                            dialog.cancel();
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.cancel();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
