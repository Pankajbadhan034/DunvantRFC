package com.dunvant.application.child.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.LoginScreen;
import com.dunvant.application.R;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.ChildMainScreen;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class ChildDashboardFragment extends Fragment implements IWebServiceCallback{
    TextView chatBadge;
    TextView friendBadge;
    TextView challengeBadge;

    private final String BADGE_COUNT = "BADGE_COUNT";
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Typeface helvetica;
    Typeface linoType;

    ImageView profilePhoto;
    ImageView profilePhotoSmall;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    private final String DO_LOGOUT = "DO_LOGOUT";
    private final String CHECK_FOR_UPDATES = "CHECK_FOR_UPDATES";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(1000))
                .build();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_fragment_dashboard, container, false);

        profilePhoto = (ImageView) view.findViewById(R.id.profilePhoto);
        profilePhotoSmall = (ImageView) view.findViewById(R.id.profilePhotoSmall);

//        TextView fullName = (TextView) view.findViewById(R.id.fullName);
//        TextView address = (TextView) view.findViewById(R.id.address);
//        TextView country = (TextView) view.findViewById(R.id.country);

        TextView updateProfile = (TextView) view.findViewById(R.id.updateProfile);
        TextView manageGallery = (TextView) view.findViewById(R.id.manageGallery);
        TextView myFriends = (TextView) view.findViewById(R.id.myFriends);
        TextView viewProfile = (TextView) view.findViewById(R.id.viewProfile);
        TextView myChallenges = (TextView) view.findViewById(R.id.myChallenges);
        TextView viewMarks = (TextView) view.findViewById(R.id.viewMarks);
        TextView trackWorkout = (TextView) view.findViewById(R.id.trackWorkout);
        TextView captureVideo = (TextView) view.findViewById(R.id.captureVideo);
        TextView myMessages = (TextView) view.findViewById(R.id.myMessages);
        TextView childPost = (TextView) view.findViewById(R.id.childPost);
        TextView createNewPost = (TextView) view.findViewById(R.id.createNewPost);
        TextView myIfaCareer = (TextView) view.findViewById(R.id.myIfaCareer);
//        Button reportAbuse = (Button) view.findViewById(R.id.reportAbuse);
        TextView reportAbuse = view.findViewById(R.id.reportAbuse);
        TextView loginAsParent = view.findViewById(R.id.loginAsParent);
        Button logoutButton = (Button) view.findViewById(R.id.logoutButton);
        chatBadge = (TextView) view.findViewById(R.id.chatBadge);
        friendBadge = (TextView) view.findViewById(R.id.friendBadge);
        challengeBadge = (TextView) view.findViewById(R.id.challengeBadge);

//        fullName.setTypeface(helvetica);
//        address.setTypeface(helvetica);
//        country.setTypeface(helvetica);
        updateProfile.setTypeface(helvetica);
        manageGallery.setTypeface(helvetica);
        myFriends.setTypeface(helvetica);
        viewProfile.setTypeface(helvetica);
        myChallenges.setTypeface(helvetica);
        viewMarks.setTypeface(helvetica);
        trackWorkout.setTypeface(helvetica);
        captureVideo.setTypeface(helvetica);
        myMessages.setTypeface(helvetica);
        childPost.setTypeface(helvetica);
        createNewPost.setTypeface(helvetica);
        myIfaCareer.setTypeface(helvetica);
        reportAbuse.setTypeface(helvetica);
        loginAsParent.setTypeface(helvetica);
        logoutButton.setTypeface(linoType);
        chatBadge.setTypeface(linoType);
        friendBadge.setTypeface(linoType);
        challengeBadge.setTypeface(linoType);

//        fullName.setText(loggedInUser.getFullName());
//        address.setText(loggedInUser.getAddress());
//        country.setText(" (Dubai)");

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing);
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        imageLoader.displayImage(loggedInUser.getProfilePicPath(), profilePhoto, options);
        imageLoader.displayImage(loggedInUser.getProfilePicPath(), profilePhotoSmall, options);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(loggedInUser.getFullName());
                    toolbar.setBackgroundResource(R.drawable.imgbg);
                    profilePhotoSmall.setVisibility(View.VISIBLE);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profilePhotoSmall.setVisibility(View.GONE);
                    isShow = false;
                }
            }
        });

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChildMainScreen) getActivity()).showViewYourProfile();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((ChildMainScreen) getActivity()).showUpdateProfile();

            }
        });

        manageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChildMainScreen) getActivity()).showMyGallery();
            }
        });

        myFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChildMainScreen) getActivity()).showMyFriends();
            }
        });

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChildMainScreen) getActivity()).showViewYourProfile();
            }
        });

        myChallenges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChildMainScreen) getActivity()).showMyChallenges();
            }
        });
        myMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChildMainScreen) getActivity()).showMyMessages();
            }
        });
        childPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChildMainScreen) getActivity()).showChildPost();
            }
        });
        myIfaCareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChildMainScreen) getActivity()).showMyIFAcareer();
            }
        });
        viewMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChildMainScreen) getActivity()).showViewMarks();
            }
        });

        trackWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChildMainScreen) getActivity()).showTrackWorkout();
            }
        });

        captureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChildMainScreen) getActivity()).showCaptureVideo();
            }
        });

        createNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChildMainScreen) getActivity()).showCreatePost();
            }
        });

        reportAbuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChildMainScreen) getActivity()).showContactCoach();
            }
        });

        loginAsParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChildMainScreen) getActivity()).showLoginAsParent();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.parent_dialog_logout);

                TextView text1 = (TextView) dialog.findViewById(R.id.text1);
                TextView text2 = (TextView) dialog.findViewById(R.id.text2);
                TextView yes = (TextView) dialog.findViewById(R.id.yes);
                TextView no = (TextView) dialog.findViewById(R.id.no);
                text1.setTypeface(helvetica);
                text2.setTypeface(helvetica);
                yes.setTypeface(linoType);
                no.setTypeface(linoType);

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        doLogout();
                    }
                });

                dialog.show();
            }
        });

        checkForUpdates();

        return view;
    }

    private void getApprovalCount(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "children/send_chat_request_count";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), BADGE_COUNT, ChildDashboardFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkForUpdates() {
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("build_version", Utilities.SYSTEM_VERSION));

        String webServiceUrl = Utilities.BASE_URL + "account/get_app_version";

        PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(getActivity(), nameValuePairList, CHECK_FOR_UPDATES, ChildDashboardFragment.this);
        postWebServiceAsync.execute(webServiceUrl);
    }

    private void showUpdateDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String appPackageName = getActivity().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void doLogout() {
        if (Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "account/logout";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), DO_LOGOUT, ChildDashboardFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case BADGE_COUNT:

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");

                        if (status) {
                            String requestCount = responseObject.getString("resquest_count");
                            String messageCount = responseObject.getString("message_count");
                            String challengesCount = responseObject.getString("challenges_count");

                            if(requestCount.equalsIgnoreCase("0")){
                                friendBadge.setVisibility(View.INVISIBLE);
                            }else{
                                friendBadge.setVisibility(View.VISIBLE);
                                friendBadge.setText(requestCount);
                            }

                            if(messageCount.equalsIgnoreCase("0")){
                                chatBadge.setVisibility(View.INVISIBLE);
                            }else{
                                chatBadge.setVisibility(View.VISIBLE);
                                chatBadge.setText(messageCount);
                            }

                            if(challengesCount.equalsIgnoreCase("0")){
                                challengeBadge.setVisibility(View.INVISIBLE);
                            }else{
                                challengeBadge.setVisibility(View.VISIBLE);
                                challengeBadge.setText(challengesCount);
                            }


                        } else {
                          //  Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case DO_LOGOUT:

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        if (status) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isUserLoggedIn", false);
                            editor.commit();

                            Intent loginScreen = new Intent(getActivity(), LoginScreen.class);
                            loginScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginScreen);
                        }else{
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isUserLoggedIn", false);
                            editor.commit();

                            Intent loginScreen = new Intent(getActivity(), LoginScreen.class);
                            loginScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginScreen);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case CHECK_FOR_UPDATES:

                if(response == null){
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status){
                            String currentSystemUpdateLevel = responseObject.getString("CURRENT_SYS_UPDATE_LEVEL");

                            if(currentSystemUpdateLevel.equalsIgnoreCase("1")){
                                // Show dialog
                                showUpdateDialog(message);
                            } else {
                                getApprovalCount();
                            }

                        } else {
                            getApprovalCount();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}