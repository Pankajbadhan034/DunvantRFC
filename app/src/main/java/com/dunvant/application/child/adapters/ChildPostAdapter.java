package com.dunvant.application.child.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildPostsBean;
import com.dunvant.application.beans.ChildSuggestionBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildAllCommentsReadScreen;
import com.dunvant.application.child.ChildPostViewScreen;
import com.dunvant.application.child.fragments.ChildPostFragment;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildPostAdapter extends BaseAdapter implements IWebServiceCallback {
    private CallbackManager fb_callbackManager;
    private ShareDialog fb_shareDialog;
    ChildSuggestionAdapter childSuggestionAdapter;
    ChildSuggestionBean childSuggestionBean;
    ArrayList<ChildSuggestionBean> childSuggestionBeanArrayList = new ArrayList<>();
    TextView likeCount;
    TextView likeClick;
    TextView commentClick;

    int positionValue;
    int likeCountPosition;
    int commentCountPosition;
    int sharedPosition;
    int likeValue;
    int commentValue;
    Dialog dialog;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    private final String LIKE_UNLIKE = "LIKE_UNLIKE";
    private final String POST_COMMENT = "POST_COMMENT";
    private final String POST_SHARE = "POST_SHARE";
    private final String POST_DELETE = "POST_DELETE";
    private final String ACCEPT_CHALLENGE = "ACCEPT_CHALLENGE";
    private final String IGNORE_CHALLENGE = "IGNORE_CHALLENGE";
    private final String APPROVAL_TIMELINE = "APPROVAL_TIMELINE";
    private final String APPROVAL_CHALLENGE = "APPROVAL_CHALLENGE";
    Context context;
    ArrayList<ChildPostsBean> childPostsBeanArrayList;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    ImageLoader imageLoaderWithoutCircle = ImageLoader.getInstance();
    DisplayImageOptions options;
    DisplayImageOptions optionsWithoutCircle;

    Typeface helvetica;
    Typeface linoType;

    ShareDialog shareDialog;
    CallbackManager callbackManager;

    public ChildPostAdapter(Context context, ArrayList<ChildPostsBean> childPostsBeanArrayList, ChildPostFragment childPostFragment){
        this.context = context;
        this.childPostsBeanArrayList = childPostsBeanArrayList;
        layoutInflater = LayoutInflater.from(context);

        shareDialog = new ShareDialog(childPostFragment);
        callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new

                FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {}

                    @Override
                    public void onCancel() {}

                    @Override
                    public void onError(FacebookException error) {}
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

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
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
        convertView = layoutInflater.inflate(R.layout.child_adapter_child_posts , null);

        TextView commentDesc = (TextView) convertView.findViewById(R.id.commentDesc);
        ImageView profilePic = (ImageView) convertView.findViewById(R.id.profilePic);
        TextView titlePost = (TextView) convertView.findViewById(R.id.titlePost);
        TextView datePost = (TextView) convertView.findViewById(R.id.datePost);
        ImageView imageType = (ImageView) convertView.findViewById(R.id.type);
        TextView typeText = (TextView) convertView.findViewById(R.id.typeText);
        final ImageView backgroudImage = (ImageView) convertView.findViewById(R.id.backgroudImage);
        ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        likeClick = (TextView) convertView.findViewById(R.id.likeClick);
        likeCount = (TextView) convertView.findViewById(R.id.likeCount);
        commentClick = (TextView) convertView.findViewById(R.id.commentClick);
        TextView commentCount = (TextView) convertView.findViewById(R.id.commentCount);
        TextView shareClick = (TextView) convertView.findViewById(R.id.shareClick);
        TextView timePost = (TextView) convertView.findViewById(R.id.timePost);
        RelativeLayout relative1 = (RelativeLayout) convertView.findViewById(R.id.relative1);
        LinearLayout actionButtonsLinear =(LinearLayout) convertView.findViewById(R.id.actionButtonsLinear);
        HorizontalListView listHorizontal = (HorizontalListView) convertView.findViewById(R.id.listHorizontal);
        RelativeLayout relative = (RelativeLayout) convertView.findViewById(R.id.relative);
        TextView divider = (TextView) convertView.findViewById(R.id.divider);
        ImageView type = (ImageView) convertView.findViewById(R.id.type);

        titlePost.setTypeface(helvetica);
        datePost.setTypeface(helvetica);
        typeText.setTypeface(helvetica);
        likeClick.setTypeface(helvetica);
        likeCount.setTypeface(helvetica);
        commentClick.setTypeface(helvetica);
        commentCount.setTypeface(helvetica);
        shareClick.setTypeface(helvetica);


        final ChildPostsBean childPostsBean = childPostsBeanArrayList.get(position);

//        System.out.println("LocalTypeName::" + childPostsBean.getLocalType());

        if(childPostsBean.getLocalType().equalsIgnoreCase("friend_suggestions")){
//            System.out.println("friend_suggestions::");
            listHorizontal.setVisibility(View.VISIBLE);
            relative1.setVisibility(View.GONE);
            actionButtonsLinear.setVisibility(View.GONE);
            datePost.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
            timePost.setVisibility(View.GONE);

            typeText.setText("Shared suggestions with you.");
            titlePost.setText(childPostsBean.getActionPerformFullName());
            imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);

            try {
                JSONArray suggestionArray = new JSONArray(childPostsBean.getSuggestions());


                for(int i=0; i<suggestionArray.length(); i++){
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
//                    System.out.println("NAMEHERE:::" + suggestionObject.getString("full_name"));

                    childSuggestionBeanArrayList.add(childSuggestionBean);
                }

                childSuggestionAdapter = new ChildSuggestionAdapter(context, childSuggestionBeanArrayList);
                listHorizontal.setAdapter(childSuggestionAdapter);

            }catch (Exception e){

            }


        }else if(childPostsBean.getLocalType().equalsIgnoreCase("friend")){
            relative1.setVisibility(View.GONE);
            imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
            commentDesc.setText(childPostsBean.getPostEntityTitle());
            titlePost.setText(childPostsBean.getPostEntityTitle());
            datePost.setText(childPostsBean.getPostedDateFormatted());
            timePost.setText(childPostsBean.getPostedTimeFormatted());
            likeCount.setText(childPostsBean.getEntityLikes());
            commentCount.setText(childPostsBean.getEntityComments());
            imageType.setBackgroundResource(R.drawable.friendssmall);
            typeText.setText(childPostsBean.getActivity());

            if(childPostsBean.getNoCtaAction().equalsIgnoreCase("false")){
                if( childPostsBean.getRequestState().equalsIgnoreCase("0")) {
                    actionButtonsLinear.setVisibility(View.VISIBLE);
                    likeCount.setVisibility(View.GONE);
                    commentCount.setVisibility(View.GONE);
                    shareClick.setVisibility(View.GONE);
                    likeClick.setText("Accept");
                    likeClick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.present, 0, 0, 0);
                    commentClick.setText("Reject");
                    commentClick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.absent, 0, 0, 0);
                    likeClick.setEnabled(true);
                }else if(childPostsBean.getRequestState().equalsIgnoreCase("2")){
                    actionButtonsLinear.setVisibility(View.VISIBLE);
                    likeCount.setVisibility(View.GONE);
                    commentCount.setVisibility(View.GONE);
                    shareClick.setVisibility(View.GONE);
                    likeClick.setText("dis-Accept");
                    commentClick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.present, 0, 0, 0);
                    commentClick.setText("dis-Reject");
                    likeClick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.absent, 0, 0, 0);
                    likeClick.setEnabled(false);
                }
            }
            else{
                actionButtonsLinear.setVisibility(View.GONE);
            }


        }else if(childPostsBean.getLocalType().equalsIgnoreCase("workout")){
            relative1.setVisibility(View.GONE);
            imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
            //imageLoader.displayImage(childPostsBean.getFileUrl(), backgroudImage, options);
            commentDesc.setText(childPostsBean.getPostEntityTitle());
            titlePost.setText(loggedInUser.getFullName());
            datePost.setText(childPostsBean.getPostedDateFormatted());
            timePost.setText(childPostsBean.getPostedTimeFormatted());
            likeCount.setText(childPostsBean.getEntityLikes());
            commentCount.setText(childPostsBean.getEntityComments());
            imageType.setBackgroundResource(R.drawable.challenge1);
            typeText.setText(childPostsBean.getActivity());
        }else if(childPostsBean.getLocalType().equalsIgnoreCase("challenge")){
            imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
            imageLoaderWithoutCircle.displayImage(childPostsBean.getPostEntityChallengeImageUrl(), backgroudImage, optionsWithoutCircle);
            commentDesc.setText(childPostsBean.getPostEntityTitle());
            titlePost.setText(childPostsBean.getActionPerformFullName());
            datePost.setText(childPostsBean.getPostedDateFormatted());
            timePost.setText(childPostsBean.getPostedTimeFormatted());
            likeCount.setText(childPostsBean.getEntityLikes());
            commentCount.setText(childPostsBean.getEntityComments());
            imageType.setBackgroundResource(R.drawable.challenge1);
            typeText.setText(childPostsBean.getActivity());
            if(childPostsBean.getApprovalStatus().equalsIgnoreCase("approval_required_challenge")){
                likeClick.setText("Approve");
                likeClick.setEnabled(true);
                commentClick.setVisibility(View.INVISIBLE);
                shareClick.setVisibility(View.INVISIBLE);
                likeCount.setVisibility(View.INVISIBLE);
                commentCount.setVisibility(View.INVISIBLE);
            }else if(childPostsBean.getApprovalStatus().equalsIgnoreCase("approved_challenge")){
                likeClick.setText("Approved");
                likeClick.setEnabled(false);
                likeClick.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                commentClick.setVisibility(View.INVISIBLE);
                shareClick.setVisibility(View.INVISIBLE);
                likeCount.setVisibility(View.INVISIBLE);
                commentCount.setVisibility(View.INVISIBLE);
            }else{
                if(childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("accepted_challenge")){
                    likeClick.setText("Accepted");
                    commentClick.setText("Ignore");
                    likeClick.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    commentClick.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    likeCount.setVisibility(View.INVISIBLE);
                    commentCount.setVisibility(View.INVISIBLE);
                    likeClick.setEnabled(false);
                    commentClick.setEnabled(false);

                }else if(childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("set_challenge")){
                    likeClick.setText("Accept");
                    commentClick.setText("Ignore");
                    likeClick.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    commentClick.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    likeCount.setVisibility(View.INVISIBLE);
                    commentCount.setVisibility(View.INVISIBLE);
                    likeClick.setEnabled(true);
                    commentClick.setEnabled(true);
                }else if(childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("ignored_challenge")){
                    likeClick.setText("Accept");
                    commentClick.setText("Ignored");
                    likeClick.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    commentClick.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    likeCount.setVisibility(View.INVISIBLE);
                    commentCount.setVisibility(View.INVISIBLE);
                    likeClick.setEnabled(false);
                    commentClick.setEnabled(false);
                }else if(childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("achieved_challenge")){
                    likeClick.setText("Like");
                    commentClick.setText("Comment");
                    likeClick.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    commentClick.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    likeCount.setVisibility(View.INVISIBLE);
                    commentCount.setVisibility(View.INVISIBLE);
                }
            }

        }else if(childPostsBean.getLocalType().equalsIgnoreCase("image")){


            if(childPostsBean.getFileUrl().contains("http")){
                imageLoaderWithoutCircle.displayImage(childPostsBean.getFileUrl(), backgroudImage, optionsWithoutCircle);
            }else{
                relative1.setVisibility(View.GONE);
            }
            imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
            commentDesc.setText(childPostsBean.getPostEntityTitle());
            titlePost.setText(childPostsBean.getActionPerformFullName());
            datePost.setText(childPostsBean.getPostedDateFormatted());
            timePost.setText(childPostsBean.getPostedTimeFormatted());
//            System.out.println("comments--" + childPostsBean.getEntityComments());
//            System.out.println("likes--"+childPostsBean.getEntityLikes());
            likeCount.setText(childPostsBean.getEntityLikes());
            commentCount.setText(childPostsBean.getEntityComments());
            imageType.setBackgroundResource(R.drawable.photo1);
            typeText.setText(childPostsBean.getActivity());

        }else if(childPostsBean.getLocalType().equalsIgnoreCase("video")){
//            imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
//            backgroudImage.setBackgroundResource(R.drawable.play);
//            titlePost.setText(childPostsBean.getPostEntityTitle());
//            datePost.setText(childPostsBean.getPostedDateFormatted());
//            timePost.setText(childPostsBean.getPostedTimeFormatted());
//            likeCount.setText(childPostsBean.getEntityLikes());
//            imageType.setBackgroundResource(R.drawable.video1);
//            typeText.setText(childPostsBean.getActivity());

            if(childPostsBean.getFileUrl().contains("http")){
                imageLoaderWithoutCircle.displayImage(childPostsBean.getFileUrl(), backgroudImage, optionsWithoutCircle);
            }else{
                relative1.setVisibility(View.GONE);
            }
            imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
            commentDesc.setText(childPostsBean.getPostEntityTitle());
            titlePost.setText(childPostsBean.getActionPerformFullName());
            datePost.setText(childPostsBean.getPostedDateFormatted());
            timePost.setText(childPostsBean.getPostedTimeFormatted());
//            System.out.println("comments--" + childPostsBean.getEntityComments());
//            System.out.println("likes--"+childPostsBean.getEntityLikes());
            likeCount.setText(childPostsBean.getEntityLikes());
            commentCount.setText(childPostsBean.getEntityComments());
            imageType.setBackgroundResource(R.drawable.video1);
            typeText.setText(childPostsBean.getActivity());

        }else if(childPostsBean.getLocalType().equalsIgnoreCase("timeline")){
            if(childPostsBean.getFileUrl().contains("http")){
                imageLoaderWithoutCircle.displayImage(childPostsBean.getFileUrl(), backgroudImage, optionsWithoutCircle);
            }else{
                relative1.setVisibility(View.GONE);
            }
            imageLoader.displayImage(childPostsBean.getActionPerformedProfilePicUrl(), profilePic, options);
            commentDesc.setText(childPostsBean.getPostEntityTitle());
            titlePost.setText(childPostsBean.getActionPerformFullName());
            datePost.setText(childPostsBean.getPostedDateFormatted());
            timePost.setText(childPostsBean.getPostedTimeFormatted());
//            System.out.println("comments--" + childPostsBean.getEntityComments());
//            System.out.println("likes--"+childPostsBean.getEntityLikes());
            likeCount.setText(childPostsBean.getEntityLikes());
            commentCount.setText(childPostsBean.getEntityComments());
            imageType.setBackgroundResource(R.drawable.timeline_small);
            typeText.setText(childPostsBean.getActivity());

            if(childPostsBean.getApprovalStatus().equalsIgnoreCase("approval_required_timeline")){
                likeClick.setText("Approve");
                likeClick.setEnabled(true);
                commentClick.setVisibility(View.INVISIBLE);
                shareClick.setVisibility(View.INVISIBLE);
                likeCount.setVisibility(View.INVISIBLE);
                commentCount.setVisibility(View.INVISIBLE);
            }else if(childPostsBean.getApprovalStatus().equalsIgnoreCase("approved_timeline")){
                likeClick.setText("Approved");
                likeClick.setEnabled(false);
                commentClick.setVisibility(View.INVISIBLE);
                shareClick.setVisibility(View.INVISIBLE);
                likeCount.setVisibility(View.INVISIBLE);
                commentCount.setVisibility(View.INVISIBLE);
            }


        }


        if(childPostsBean.getChallengePostParentId().equalsIgnoreCase("0")||childPostsBean.getChallengePostParentId().equalsIgnoreCase(null)){
                delete.setVisibility(View.VISIBLE);
        }else{
            delete.setVisibility(View.GONE);
        }

        backgroudImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(childPostsBean.getLocalType().equalsIgnoreCase("video")){
                    Intent obj = new Intent(context, ChildPostViewScreen.class);
                    obj.putExtra("type", "video");
                    obj.putExtra("url", childPostsBean.getFileUrl());
                    context.startActivity(obj);
                }else if(childPostsBean.getLocalType().equalsIgnoreCase("challenge")){
                    Intent obj = new Intent(context, ChildPostViewScreen.class);
                    obj.putExtra("type", "image");
                    obj.putExtra("url", childPostsBean.getPostEntityChallengeImageUrl());
                    context.startActivity(obj);
                }else if(childPostsBean.getLocalType().equalsIgnoreCase("image")){
                    Intent obj = new Intent(context, ChildPostViewScreen.class);
                    obj.putExtra("type", "image");
                    obj.putExtra("url", childPostsBean.getFileUrl());
                    context.startActivity(obj);
                }else if(childPostsBean.getLocalType().equalsIgnoreCase("timeline")){
                   if(childPostsBean.getFileType().contains("image")){
                       Intent obj = new Intent(context, ChildPostViewScreen.class);
                       obj.putExtra("type", "image");
                       obj.putExtra("url", childPostsBean.getFileUrl());
                       context.startActivity(obj);
                   }else if(childPostsBean.getFileType().contains("video")){
                         Intent obj = new Intent(context, ChildPostViewScreen.class);
                       obj.putExtra("type", "video");
                       obj.putExtra("url", childPostsBean.getFileUrl());
                       context.startActivity(obj);
                   }else{
//                       System.out.println("else_FileURL::" + childPostsBean.getFileUrl());
                   }
                }
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(childPostsBean.getId().equalsIgnoreCase(childPostsBean.getpos))

                if(Utilities.isNetworkAvailable(context)) {
                    positionValue = position;
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("post_id", childPostsBean.getId()));

                    String webServiceUrl = Utilities.BASE_URL + "user_posts/delete";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, POST_DELETE, ChildPostAdapter.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                }else{
                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        likeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utilities.isNetworkAvailable(context)) {
                    likeCountPosition=position;
//                    System.out.println("likeCountPositionValue::"+likeCountPosition);
                    likeValue = Integer.parseInt(childPostsBean.getEntityLikes());
                    final List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("post_id", childPostsBean.getId()));

                   final ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    //new changes
                    if(childPostsBean.getLocalType().equalsIgnoreCase("timeline")){
                        if(childPostsBean.getApprovalStatus().equalsIgnoreCase("approval_required_timeline")){

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
                                    if(parentPasswordStr==null || parentPasswordStr.isEmpty()){
                                        Toast.makeText(context, "Please enter Parent Password", Toast.LENGTH_LONG).show();
                                    }else{

                                        if(Utilities.isNetworkAvailable(context)) {
                                            nameValuePairList.add(new BasicNameValuePair("entity_id", childPostsBean.getEntityId()));
                                            nameValuePairList.add(new BasicNameValuePair("password", parentPasswordStr));
                                            String webServiceUrl = Utilities.BASE_URL + "user_posts/approve_timeline_by_parent_password";
                                            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, APPROVAL_TIMELINE, ChildPostAdapter.this, headers);
                                            postWebServiceAsync.execute(webServiceUrl);
                                        }else{
                                            Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                            dialog.show();
                        }else{
                            // old code
                            if(childPostsBean.getLocalType().equalsIgnoreCase("image")||childPostsBean.getLocalType().equalsIgnoreCase("timeline")||childPostsBean.getLocalType().equalsIgnoreCase("video")||childPostsBean.getLocalType().equalsIgnoreCase("workout")){
                                String webServiceUrl = Utilities.BASE_URL + "user_posts/like";
                                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, LIKE_UNLIKE, ChildPostAdapter.this, headers);
                                postWebServiceAsync.execute(webServiceUrl);
                            }else if(childPostsBean.getLocalType().equalsIgnoreCase("challenge")){
                                if(childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("achieved_challenge")){
                                    String webServiceUrl = Utilities.BASE_URL + "user_posts/like";
                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, LIKE_UNLIKE, ChildPostAdapter.this, headers);
                                    postWebServiceAsync.execute(webServiceUrl);
                                }else{
                                    String webServiceUrl = Utilities.BASE_URL + "user_posts/accept_challenge";
                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_CHALLENGE, ChildPostAdapter.this, headers);
                                    postWebServiceAsync.execute(webServiceUrl);

                                }

                            }else if(childPostsBean.getLocalType().equalsIgnoreCase("friend")){
                                String webServiceUrl = Utilities.BASE_URL + "user_posts/accept_challenge";
                                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_CHALLENGE, ChildPostAdapter.this, headers);
                                postWebServiceAsync.execute(webServiceUrl);
                            }
                        }
                    }else if(childPostsBean.getLocalType().equalsIgnoreCase("challenge")) {
                        if(childPostsBean.getApprovalStatus().equalsIgnoreCase("approval_required_challenge")){

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
                                    if(parentPasswordStr==null || parentPasswordStr.isEmpty()){
                                        Toast.makeText(context, "Please enter Parent Password", Toast.LENGTH_LONG).show();
                                    }else{

                                        if(Utilities.isNetworkAvailable(context)) {
                                            nameValuePairList.add(new BasicNameValuePair("challenges_id", childPostsBean.getChallengeEntityId()));
                                            nameValuePairList.add(new BasicNameValuePair("password", parentPasswordStr));
                                            String webServiceUrl = Utilities.BASE_URL + "challenges/approve_challenge_by_parent_password";
                                            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, APPROVAL_CHALLENGE, ChildPostAdapter.this, headers);
                                            postWebServiceAsync.execute(webServiceUrl);
                                        }else{
                                            Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                            dialog.show();




                        }else{
                            // old code
                            if(childPostsBean.getLocalType().equalsIgnoreCase("image")||childPostsBean.getLocalType().equalsIgnoreCase("timeline")||childPostsBean.getLocalType().equalsIgnoreCase("video")||childPostsBean.getLocalType().equalsIgnoreCase("workout")){
                                String webServiceUrl = Utilities.BASE_URL + "user_posts/like";
                                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, LIKE_UNLIKE, ChildPostAdapter.this, headers);
                                postWebServiceAsync.execute(webServiceUrl);
                            }else if(childPostsBean.getLocalType().equalsIgnoreCase("challenge")){
                                if(childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("achieved_challenge")){
                                    String webServiceUrl = Utilities.BASE_URL + "user_posts/like";
                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, LIKE_UNLIKE, ChildPostAdapter.this, headers);
                                    postWebServiceAsync.execute(webServiceUrl);
                                }else{
                                    String webServiceUrl = Utilities.BASE_URL + "user_posts/accept_challenge";
                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_CHALLENGE, ChildPostAdapter.this, headers);
                                    postWebServiceAsync.execute(webServiceUrl);

                                }

                            }else if(childPostsBean.getLocalType().equalsIgnoreCase("friend")){
                                String webServiceUrl = Utilities.BASE_URL + "user_posts/accept_challenge";
                                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_CHALLENGE, ChildPostAdapter.this, headers);
                                postWebServiceAsync.execute(webServiceUrl);
                            }
                        }
                    }else{
                            // old code
                            if(childPostsBean.getLocalType().equalsIgnoreCase("image")||childPostsBean.getLocalType().equalsIgnoreCase("timeline")||childPostsBean.getLocalType().equalsIgnoreCase("video")||childPostsBean.getLocalType().equalsIgnoreCase("workout")){
                                String webServiceUrl = Utilities.BASE_URL + "user_posts/like";
                                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, LIKE_UNLIKE, ChildPostAdapter.this, headers);
                                postWebServiceAsync.execute(webServiceUrl);
                            }else if(childPostsBean.getLocalType().equalsIgnoreCase("challenge")){
                                if(childPostsBean.getChallengeChallengeStatus().equalsIgnoreCase("achieved_challenge")){
                                    String webServiceUrl = Utilities.BASE_URL + "user_posts/like";
                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, LIKE_UNLIKE, ChildPostAdapter.this, headers);
                                    postWebServiceAsync.execute(webServiceUrl);
                                }else{
                                    String webServiceUrl = Utilities.BASE_URL + "user_posts/accept_challenge";
                                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_CHALLENGE, ChildPostAdapter.this, headers);
                                    postWebServiceAsync.execute(webServiceUrl);

                                }

                            }else if(childPostsBean.getLocalType().equalsIgnoreCase("friend")){
                                String webServiceUrl = Utilities.BASE_URL + "user_posts/accept_challenge";
                                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, ACCEPT_CHALLENGE, ChildPostAdapter.this, headers);
                                postWebServiceAsync.execute(webServiceUrl);
                            }

                        }




                }else{
                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        commentClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(childPostsBean.getLocalType().equalsIgnoreCase("image")||childPostsBean.getLocalType().equalsIgnoreCase("timeline")||childPostsBean.getLocalType().equalsIgnoreCase("video")||childPostsBean.getLocalType().equalsIgnoreCase("workout")){
                    Intent obj = new Intent(context, ChildAllCommentsReadScreen.class);
                    obj.putExtra("entityId", childPostsBean.getEntityId());
                    obj.putExtra("entityType", childPostsBean.getLocalType());
                    obj.putExtra("postId", childPostsBean.getId());
                    obj.putExtra("name", childPostsBean.getActionPerformFullName());
                    context.startActivity(obj);


//                    // custom dialog
//                    dialog = new Dialog(context);
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setContentView(R.layout.child_dialog_post_comment);
//                    dialog.setTitle("IFA...");
//
//                    // set the custom dialog components - text, image and button
//                    final TextView postcommentLabel = (TextView) dialog.findViewById(R.id.postcommentLabel);
//                    final EditText comment = (EditText) dialog.findViewById(R.id.comment);
//                    Button submit = (Button) dialog.findViewById(R.id.submit);
//                    Button allComments = (Button) dialog.findViewById(R.id.allComments);
//                    Button allLikes = (Button) dialog.findViewById(R.id.allLikes);
//
//                    postcommentLabel.setTypeface(helvetica);
//                    comment.setTypeface(helvetica);
//                    submit.setTypeface(linoType);
//                    allComments.setTypeface(linoType);

//                    allComments.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.cancel();
//                            Intent obj = new Intent(context, ChildAllCommentsReadScreen.class);
//                            obj.putExtra("entityId", childPostsBean.getEntityId());
//                            obj.putExtra("entityType", childPostsBean.getLocalType());
//                            obj.putExtra("buttonName", "comments");
//                            context.startActivity(obj);
//                        }
//                    });
//
//                    allLikes.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.cancel();
//                            Intent obj = new Intent(context, ChildAllCommentsReadScreen.class);
//                            obj.putExtra("entityId", childPostsBean.getEntityId());
//                            obj.putExtra("entityType", childPostsBean.getLocalType());
//                            obj.putExtra("buttonName", "likes");
//                            context.startActivity(obj);
//                        }
//                    });

//                    submit.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });

//                    dialog.show();
                }else if(childPostsBean.getLocalType().equalsIgnoreCase("challenge")||childPostsBean.getLocalType().equalsIgnoreCase("friend")){
                    if(Utilities.isNetworkAvailable(context)) {
                        commentCountPosition=position;
//                        System.out.println("ingored_challenge_called");
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("post_id", childPostsBean.getId()));
                        String webServiceUrl = Utilities.BASE_URL + "user_posts/ignore_challenge";
                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:" + loggedInUser.getToken());
                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, IGNORE_CHALLENGE, ChildPostAdapter.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    }else{
                        Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
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

                                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, POST_SHARE, ChildPostAdapter.this, headers);
                                postWebServiceAsync.execute(webServiceUrl);
                            }


                        } else {
                            Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                facebookShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ShareDialog.canShow(ShareLinkContent.class)) {
                            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                    .setContentTitle(childPostsBean.getPostEntityTitle())
//                                    .setImageUrl(Uri.parse(url))
//                                    .setContentDescription(url)
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
//                                .text(childPostsBean.getPostEntityTitle()+"\n"+childPostsBean.getFileUrl());
//                        builder.show();
//
//                        dialog.cancel();
                    }
                });

                dialog.show();


            }
        });




        return convertView;
    }


    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
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
                            if(responseObject.getString("next_action").equalsIgnoreCase("Unlike")){
                                likeValue=likeValue+1;
                                childPostsBeanArrayList.get(likeCountPosition).setEntityLikes(""+likeValue);
                                notifyDataSetChanged();

                            }else if(responseObject.getString("next_action").equalsIgnoreCase("like")){
                                likeValue=likeValue-1;
                                childPostsBeanArrayList.get(likeCountPosition).setEntityLikes(""+likeValue);
                                notifyDataSetChanged();
                            }

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }else{
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
                            childPostsBeanArrayList.get(likeCountPosition).setApprovalStatus("approved_challenge");
                            notifyDataSetChanged();
                            dialog.cancel();
                        }else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.cancel();
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
                        }else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.cancel();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case POST_COMMENT:
                if (response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            commentValue=commentValue+1;
                            childPostsBeanArrayList.get(commentCountPosition).setEntityComments(""+commentValue);
                            notifyDataSetChanged();
                        }else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
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
                        }else{
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

            case ACCEPT_CHALLENGE:
                if (response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            childPostsBeanArrayList.get(likeCountPosition).setChallengeChallengeStatus("accepted_challenge");
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
                            childPostsBeanArrayList.get(commentCountPosition).setChallengeChallengeStatus("ignored_challenge");
                            notifyDataSetChanged();
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;


        }

    }

//    protected void share(){
//        String shareText = "text here";
//        ShareDialog shareDialog = new ShareDialog(context);
//        if (ShareDialog.canShow(SharePhotoContent.class)) {
//            shareDialog.registerCallback(fb_callbackManager, new FacebookCallback<Sharer.Result>() {
//                @Override
//                public void onSuccess(Sharer.Result result) {
//                    Toast.makeText(context, "Share Success", Toast.LENGTH_SHORT).show();
//                    Log.d("DEBUG", "SHARE SUCCESS");
//                }
//
//                @Override
//                public void onCancel() {
//                    Toast.makeText(context, "Share Cancelled", Toast.LENGTH_SHORT).show();
//                    Log.d("DEBUG", "SHARE CANCELLED");
//                }
//
//                @Override
//                public void onError(FacebookException exception) {
//                    Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
//                    Log.e("DEBUG", "Share: " + exception.getMessage());
//                    exception.printStackTrace();
//                }
//            });
//
//        }
//
//    }

}
