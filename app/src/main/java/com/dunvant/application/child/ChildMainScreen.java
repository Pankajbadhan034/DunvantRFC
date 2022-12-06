package com.dunvant.application.child;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.LoginScreen;
import com.dunvant.application.R;
import com.dunvant.application.beans.ParentDashboardBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.adapters.ChildMainAdapter;
import com.dunvant.application.child.fragments.ChildCreatePostFragment;
import com.dunvant.application.child.fragments.ChildDashboardFragmentNew;
import com.dunvant.application.child.fragments.ChildMyChallengesFragment;
import com.dunvant.application.child.fragments.ChildMyFriendsFragment;
import com.dunvant.application.child.fragments.ChildMyGalleryFragment;
import com.dunvant.application.child.fragments.ChildMyMessagesFragment;
import com.dunvant.application.child.fragments.ChildPostFragment;
import com.dunvant.application.child.fragments.ChildUpdteProfileFragment;
import com.dunvant.application.child.fragments.ChildViewMarksFragment;
import com.dunvant.application.child.fragments.ChildViewYourProfileFragment;
//import com.dunvant.application.child.smartBand.ChildHomeSmartBandScreen;
import com.dunvant.application.coach.fragments.CoachLeagueFragment;
import com.dunvant.application.parent.ParentMainScreen;
import com.dunvant.application.parent.fragments.ParentContactCoachFragment;
import com.dunvant.application.parent.fragments.ParentHelpDocFragment;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ChildMainScreen extends AppCompatActivity implements IWebServiceCallback {
    ArrayList<ParentDashboardBean> parentDashboardBeanArrayListDuplicate = new ArrayList<>();;
    public static String GALLERY_TYPE = "photos";

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    SlidingMenu slidingMenu;
    TextView title;
    ImageView profilePhoto;
    ImageView menuIcon;
    ImageView adNewPhoto;
    ImageView searchIcon;
    ImageView backButton;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    private final String DO_LOGOUT = "DO_LOGOUT";
    private final String LOGIN_WEB_SERVICE = "LOGIN_WEB_SERVICE";

    public static String CHILDPOST = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_child_dashboard);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(ChildMainScreen.this));
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

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        title = (TextView) findViewById(R.id.title);
        title.setTypeface(linoType);
        menuIcon = (ImageView) findViewById(R.id.slideMenu);
        adNewPhoto = (ImageView) findViewById(R.id.addImageView);
        searchIcon = (ImageView) findViewById(R.id.searchIcon);
        backButton = (ImageView) findViewById(R.id.backButton);

        setUpRightSlidingMenuNew();
//        showDashboardScreen();
//        showChildPost();


        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();

            if (bundle == null) {
//                showChildPost();
                showDashboardScreen();
            } else {

                if (bundle.containsKey("notification_data")) {
                    String jsonData = intent.getStringExtra("notification_data");

                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);

                        JSONObject notificationObject = jsonObject.getJSONObject("notification");

                        String title = notificationObject.getString("title");
                        String body = notificationObject.getString("body");

                        showNotificationDialog(title, body);
                        showDashboardScreen();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildMainScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Added new else block
                    showDashboardScreen();
                }
            }
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDashboardScreen();
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout);
                if (f instanceof ChildViewMarksFragment) {
                    ((ChildViewMarksFragment) f).showSearchLinearLayout();
                }
            }
        });
        adNewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(ChildMainScreen.this, ChildMyGalleryAddNewPhotoScreen.class);
                obj.putExtra("GALLERY_TYPE", GALLERY_TYPE);
                startActivity(obj);
            }
        });
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.toggle();
            }
        });
    }

    private void setUpRightSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.RIGHT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        View view = getLayoutInflater().inflate(R.layout.child_sliding_menu, null);
        slidingMenu.setMenu(view);

        profilePhoto = (ImageView) view.findViewById(R.id.profilePhoto);
        imageLoader.displayImage(loggedInUser.getProfilePicPath(), profilePhoto, options);

        TextView fullName = (TextView) view.findViewById(R.id.fullName);
//        TextView address = (TextView) view.findViewById(R.id.address);
//        TextView country = (TextView) view.findViewById(R.id.country);

        TextView dashboard = (TextView) view.findViewById(R.id.dashboard);
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
        TextView help = (TextView) view.findViewById(R.id.help);
        TextView reportAbuse = (TextView) view.findViewById(R.id.reportAbuse);
        TextView loginAsParent = (TextView) view.findViewById(R.id.loginAsParent);

        fullName.setTypeface(helvetica);
//        address.setTypeface(helvetica);
//        country.setTypeface(helvetica);
        dashboard.setTypeface(helvetica);
        updateProfile.setTypeface(helvetica);
        manageGallery.setTypeface(helvetica);
        viewProfile.setTypeface(helvetica);
        myChallenges.setTypeface(helvetica);
        trackWorkout.setTypeface(helvetica);
        captureVideo.setTypeface(helvetica);
        myMessages.setTypeface(helvetica);
        myFriends.setTypeface(helvetica);
        viewMarks.setTypeface(helvetica);
        childPost.setTypeface(helvetica);
        createNewPost.setTypeface(helvetica);
        myIfaCareer.setTypeface(helvetica);
        help.setTypeface(helvetica);
        reportAbuse.setTypeface(helvetica);
        loginAsParent.setTypeface(helvetica);

        TextView logout = (TextView) view.findViewById(R.id.logout);
        logout.setTypeface(helvetica);

        fullName.setText(loggedInUser.getFullName());
//        address.setText(loggedInUser.getAddress());
//        country.setText(" (Dubai)");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(ChildMainScreen.this);
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

                        /*SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isUserLoggedIn", false);
                        editor.commit();

                        Intent loginScreen = new Intent(ChildMainScreen.this, LoginScreen.class);
                        loginScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginScreen);*/
                        doLogout();

                    }
                });

                dialog.show();
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDashboardScreen();
                slidingMenu.toggle();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateProfile();
                slidingMenu.toggle();
            }
        });

        manageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyGallery();
                slidingMenu.toggle();
            }
        });

        myFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyFriends();
                slidingMenu.toggle();
            }
        });

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showViewYourProfile();
                slidingMenu.toggle();
            }
        });

        myChallenges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyChallenges();
                slidingMenu.toggle();
            }
        });

        viewMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showViewMarks();
                slidingMenu.toggle();
            }
        });

        trackWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTrackWorkout();
                slidingMenu.toggle();
            }
        });

        captureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCaptureVideo();
                slidingMenu.toggle();
            }
        });

        myMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyMessages();
                slidingMenu.toggle();
            }
        });

        childPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChildPost();
                slidingMenu.toggle();
            }
        });

        createNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreatePost();
                slidingMenu.toggle();
            }
        });

        myIfaCareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyIFAcareer();
                slidingMenu.toggle();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelp();
                slidingMenu.toggle();
            }
        });

        reportAbuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactCoach();
                slidingMenu.toggle();
            }
        });

        loginAsParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginAsParent();
                slidingMenu.toggle();
            }
        });
    }


    private void setUpRightSlidingMenuNew() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.RIGHT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        View view = getLayoutInflater().inflate(R.layout.child_sliding_menu_new, null);
        slidingMenu.setMenu(view);

        profilePhoto = (ImageView) view.findViewById(R.id.profilePhoto);
        imageLoader.displayImage(loggedInUser.getProfilePicPath(), profilePhoto, options);

        TextView fullName = (TextView) view.findViewById(R.id.fullName);

        fullName.setTypeface(helvetica);

        TextView logout = (TextView) view.findViewById(R.id.logout);
        final ListView listView = view.findViewById(R.id.listView);
        logout.setTypeface(helvetica);

        fullName.setText(loggedInUser.getFullName());
//        address.setText(loggedInUser.getAddress());
//        country.setText(" (Dubai)");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(ChildMainScreen.this);
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

                        /*SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isUserLoggedIn", false);
                        editor.commit();

                        Intent loginScreen = new Intent(ChildMainScreen.this, LoginScreen.class);
                        loginScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginScreen);*/
                        doLogout();

                    }
                });

                dialog.show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String label = parentDashboardBeanArrayListDuplicate.get(i).getLabel();

                if(label.equalsIgnoreCase("Dashboard")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showDashboardScreen();
                }if(label.equalsIgnoreCase("Newsfeed")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showChildPost();
                }else if(label.equalsIgnoreCase("Performance")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitleForPerformance", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showTrackWorkout();
                }else if(label.equalsIgnoreCase("Chats")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showMyMessages();
                }else if(label.equalsIgnoreCase("Friends")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showMyFriends();
                }else if(label.equalsIgnoreCase("Create Film")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showCaptureVideo();
                }else if(label.equalsIgnoreCase("Gallery")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showMyGallery();
                }else if(label.equalsIgnoreCase("Posts")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showCreatePost();
                }else if(label.equalsIgnoreCase("Challenges")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showMyChallenges();
                }else if(label.equalsIgnoreCase("Career")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showMyIFAcareer();
                }else if(label.equalsIgnoreCase("Reports")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showViewMarks();
                }else if(label.equalsIgnoreCase("About Me")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showUpdateProfile();
                }else if(label.equalsIgnoreCase("Profile")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showViewYourProfile();
                }else if(label.equalsIgnoreCase("Login As Parent")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showLoginAsParent();
                }else if(label.equalsIgnoreCase("Manage League")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    leagueFrag();
                }else if(label.equalsIgnoreCase("Report Abuse")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showContactCoach();
                }else if(label.equalsIgnoreCase("Help")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showHelp();
                }else {
                    Toast.makeText(ChildMainScreen.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
                slidingMenu.toggle();
            }
        });

        slidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                try{
                    parentDashboardBeanArrayListDuplicate.clear();

                    ParentDashboardBean parentDashboardBean = new ParentDashboardBean();
                    parentDashboardBean.setId("-1");
                    parentDashboardBean.setNavigation_type("Dashboard");
                    parentDashboardBean.setLabel("Dashboard");
                    parentDashboardBean.setAcademy_id("-1");
                    parentDashboardBean.setStatus("-1");
                    parentDashboardBean.setPreferred_text("Dashboard");
                    parentDashboardBean.setSort("-1");
                    parentDashboardBeanArrayListDuplicate.add(parentDashboardBean);

                    listView.setAdapter(null);
                    parentDashboardBeanArrayListDuplicate.addAll(ChildDashboardFragmentNew.parentDashboardBeanArrayList);

                    ParentDashboardBean parentDashboardBean2 = new ParentDashboardBean();
                    parentDashboardBean2.setId("-11");
                    parentDashboardBean2.setNavigation_type("Help");
                    parentDashboardBean2.setLabel("Help");
                    parentDashboardBean2.setAcademy_id("-11");
                    parentDashboardBean2.setStatus("-11");
                    parentDashboardBean2.setPreferred_text("Help");
                    parentDashboardBean2.setSort("-11");
                    parentDashboardBeanArrayListDuplicate.add(parentDashboardBean2);

                    //  parentDashboardBeanArrayListDuplicate.remove(parentDashboardBeanArrayListDuplicate.size()-1);
                    //  parentDashboardBeanArrayListDuplicate.remove(parentDashboardBeanArrayListDuplicate.size()-1);
                    listView.setAdapter(new ChildMainAdapter(ChildMainScreen.this, parentDashboardBeanArrayListDuplicate));
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });


    }

    private void doLogout() {
        if (Utilities.isNetworkAvailable(ChildMainScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "account/logout";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());



            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ChildMainScreen.this, DO_LOGOUT, ChildMainScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ChildMainScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    public void showDashboardScreen() {
        title.setText(loggedInUser.getFullName());

        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);

        ChildDashboardFragmentNew childDashboardFragment = new ChildDashboardFragmentNew();

        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, childDashboardFragment)
                .commit();
    }

    public void showUpdateProfile() {

//        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
//        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
//
//        title.setText(verbiage_singular.toUpperCase()+" PROFILE");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);

        ChildViewYourProfileFragment childViewYourProfileFragment = new ChildViewYourProfileFragment();
        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, childViewYourProfileFragment)
                .commit();

    }

    public void showMyFriends() {
//        title.setText("FRIENDS");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);

        ChildMyFriendsFragment childMyFriendsFragment = new ChildMyFriendsFragment();
        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, childMyFriendsFragment)
                .commit();
    }

    public void showMyChallenges() {
//        title.setText("CHALLENGES");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);

        ChildMyChallengesFragment childMyChallengesFragment = new ChildMyChallengesFragment();
        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, childMyChallengesFragment)
                .commit();
    }

    public void showMyGallery() {
//        title.setText("GALLERY");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.VISIBLE);
        searchIcon.setVisibility(View.GONE);

        ChildMyGalleryFragment childMyGalleryFragment = new ChildMyGalleryFragment();
        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, childMyGalleryFragment)
                .commit();
    }

    public void showViewYourProfile() {
//        title.setText("VIEW YOUR PROFILE");
//        title.setText(loggedInUser.getFullName());

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);

        ChildUpdteProfileFragment childUpdateProfileFragment = new ChildUpdteProfileFragment();
        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, childUpdateProfileFragment)
                .commit();
    }

    public void showMyMessages() {
//        title.setText("CHATS");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);

        ChildMyMessagesFragment childMyMessagesFragment = new ChildMyMessagesFragment();
        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, childMyMessagesFragment)
                .commit();
    }

    public void showChildPost() {
        // title.setText("NEWSFEED");
//        title.setText(loggedInUser.getFullName());

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);
        CHILDPOST = "";

        ChildPostFragment childPostFragment = new ChildPostFragment();
        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, childPostFragment)
                .commit();
    }

    public void showMyIFAcareer() {
//        title.setText(loggedInUser.getFullName());

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);
        CHILDPOST = "myIfaCareer";

        ChildPostFragment childPostFragment = new ChildPostFragment();
        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, childPostFragment)
                .commit();
    }

    public void showTrackWorkout() {
//        title.setText(loggedInUser.getFullName());

        String prefTextStr = sharedPreferences.getString("prefTitleForPerformance", null);
       // title.setText(prefTextStr);

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);

        /*ChildTrackWorkoutFragment childTrackWorkoutFragment = new ChildTrackWorkoutFragment();
        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, childTrackWorkoutFragment)
                .commit();*/
//
//        Intent trackWorkout = new Intent(ChildMainScreen.this, ChildHomeSmartBandScreen.class);
//        trackWorkout.putExtra("prefText", prefTextStr);
//        startActivity(trackWorkout);

    }

    public void showContactCoach() {
//        title.setText("REPORT ABUSE");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);

        ParentContactCoachFragment parentContactCoachFragment = new ParentContactCoachFragment();
        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, parentContactCoachFragment)
                .commit();
    }

    public void showViewMarks() {
//        title.setText("STATS");
//        title.setText("REPORTS");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.VISIBLE);

        ChildViewMarksFragment childViewMarksFragment = new ChildViewMarksFragment();
        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, childViewMarksFragment)
                .commit();
    }

    public void showCreatePost() {
//        title.setText("CREATE A POST");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);

        ChildCreatePostFragment childCreatePostFragment = new ChildCreatePostFragment();
        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, childCreatePostFragment)
                .commit();
    }

    public void showCaptureVideo() {
//        title.setText("CAPTURE VIDEO");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);

        Toast.makeText(this, "This function is not compliant with the Google Play 64-bit requirement.", Toast.LENGTH_SHORT).show();

//        ChildCaptureVideoFragment childCaptureVideoFragment = new ChildCaptureVideoFragment();
//        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
//                .replace(R.id.mainFrameLayout, childCaptureVideoFragment)
//                .commit();
    }

    public void showHelp() {
        title.setText("HELP");

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);

        ParentHelpDocFragment parentHelpDocFragment = new ParentHelpDocFragment();
        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, parentHelpDocFragment)
                .commit();
    }

    public void leagueFrag(){
        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        backButton.setVisibility(View.VISIBLE);
        adNewPhoto.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);

        CoachLeagueFragment coachLeagueFragment = new CoachLeagueFragment();
        ChildMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, coachLeagueFragment)
                .commit();
    }

    public void showLoginAsParent() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText editText = new EditText(ChildMainScreen.this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText.setMaxLines(1);
        alert.setMessage("Please enter password to continue");
        alert.setView(editText);

        alert.setPositiveButton("Login As Parent", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String strPassword = editText.getText().toString();
                if (strPassword == null || strPassword.isEmpty()) {
                    Toast.makeText(ChildMainScreen.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                } else {

                    String fcmToken = sharedPreferences.getString("fcmToken", "");

                    if (Utilities.isNetworkAvailable(ChildMainScreen.this)) {
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("lemail", loggedInUser.getParentUsername()));
                        nameValuePairList.add(new BasicNameValuePair("lpassword", strPassword));
                        nameValuePairList.add(new BasicNameValuePair("fcm_device_token", fcmToken));
                        nameValuePairList.add(new BasicNameValuePair("device_type", "1"));

                        String webServiceUrl = Utilities.BASE_URL + "account/login";

                        PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(ChildMainScreen.this, nameValuePairList, LOGIN_WEB_SERVICE, ChildMainScreen.this);
                        postWebServiceAsync.execute(webServiceUrl);
                    } else {
                        Toast.makeText(ChildMainScreen.this, R.string.internet_not_available, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    @Override
    public void onBackPressed() {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout);
        if (f instanceof ChildDashboardFragmentNew) {
            final Dialog dialog = new Dialog(ChildMainScreen.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.parent_dialog_exit_app);

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
                    finish();
                }
            });

            dialog.show();

        } else {
            showDashboardScreen();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case DO_LOGOUT:

                if (response == null) {
                    Toast.makeText(ChildMainScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(ChildMainScreen.this, message, Toast.LENGTH_SHORT).show();

                        if (status) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isUserLoggedIn", false);
                            editor.commit();

                            Intent loginScreen = new Intent(ChildMainScreen.this, LoginScreen.class);
                            loginScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginScreen);
                        } else {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isUserLoggedIn", false);
                            editor.commit();

                            Intent loginScreen = new Intent(ChildMainScreen.this, LoginScreen.class);
                            loginScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginScreen);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildMainScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case LOGIN_WEB_SERVICE:

                if (response == null) {
                    Toast.makeText(ChildMainScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            String token = responseObject.getString("token");
                            JSONObject userData = responseObject.getJSONObject("user_data");

                            UserBean userBean = new UserBean();
                            userBean.setToken(token);
                            userBean.setId(userData.getString("id"));
                            userBean.setAcademiesId(userData.getString("academies_id"));
                            userBean.setUsername(userData.getString("username"));
                            userBean.setEmail(userData.getString("email"));
                            userBean.setGender(userData.getString("gender"));
//                            userBean.setCreatedAt(userData.getString("created_at"));
//                            userBean.setState(userData.getString("state"));
                            userBean.setFirstName(userData.getString("fname"));
                            userBean.setLastName(userData.getString("lname"));
                            userBean.setFullName(userData.getString("full_name"));
                            userBean.setMobileNumber(userData.getString("phone_1"));
                            userBean.setSecondMobileNumber(userData.getString("phone_2"));
//                            userBean.setTotalChildren(userData.getInt("total_children"));
                            userBean.setRoleCode(userData.getString("role_code"));
                            userBean.setAddress(userData.getString("address"));
                            userBean.setProfilePicPath(userData.getString("profile_picture_path"));

                            //added for child module
                            if (userBean.getRoleCode().equalsIgnoreCase("child_role")) {
                                userBean.setFavoritePlayer(userData.getString("favourite_player"));
                                userBean.setFavoriteTeam(userData.getString("favourite_team"));
                                userBean.setFavoritePosition(userData.getString("favourite_position"));
                                userBean.setFavoriteFootballBoot(userData.getString("favourite_football_boot"));
                                userBean.setFavoritefood(userData.getString("favourite_food"));
                                userBean.setSchool(userData.getString("school"));
                                userBean.setNationality(userData.getString("nationality"));
                                userBean.setHeight(userData.getString("height"));
                                userBean.setWeight(userData.getString("weight"));
                                userBean.setDobformatted(userData.getString("dob_formatted"));

                                userBean.setHeightNumeric(userData.getString("height"));
                                userBean.setWeightNumeric(userData.getString("weight"));
                                userBean.setFavoritePlayerPicture(userData.getString("favourite_player_picture"));
                                userBean.setFavoriteTemaPicture(userData.getString("favourite_team_picture"));
                                userBean.setHeightFormatted(userData.getString("height_formatted"));
                                userBean.setWeightFormatted(userData.getString("weight_formatted"));

                                userBean.setParentUsername(userData.getString("parent_username"));

                                // userBean.setCategoryId(userData.getString("category_id"));
                            }

                            // added for Coach Module
                            if (userBean.getRoleCode().equalsIgnoreCase("coach_role")) {
                                userBean.setCanMoveChild(userData.getString("can_move_child"));
                            }

                            userBean.setPhoneCodeOne(userData.getString("phone_code_1"));
                            userBean.setPhoneCodeTwo(userData.getString("phone_code_2"));

                            if(userBean.getRoleCode().equalsIgnoreCase("parent_role")){
                                userBean.setPaymentCard(userData.getString("payment_card"));
                            }

                            Gson gson = new Gson();
                            String jsonParentUserBean = gson.toJson(userBean);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isUserLoggedIn", true);

                            if (userBean.getRoleCode().equalsIgnoreCase("parent_role")) {
                                editor.putString("typeOfUser", "parent");
                                if(userData.has("academy_currency")){
                                    editor.putString("academy_currency", userData.getString("academy_currency"));
                                    editor.putString("verbiage_singular", userData.getString("verbiage_singular"));
                                    editor.putString("verbiage_plural", userData.getString("verbiage_plural"));
                                }
                            } else if (userBean.getRoleCode().equalsIgnoreCase("child_role")) {
                                editor.putString("typeOfUser", "child");
                                if(userData.has("academy_currency")){
                                    editor.putString("academy_currency", userData.getString("academy_currency"));
                                    editor.putString("verbiage_singular", userData.getString("verbiage_singular"));
                                    editor.putString("verbiage_plural", userData.getString("verbiage_plural"));
                                }
                            } else if (userBean.getRoleCode().equalsIgnoreCase("coach_role")) {
                                editor.putString("typeOfUser", "coach");
                                if(userData.has("academy_currency")){
                                    editor.putString("academy_currency", userData.getString("academy_currency"));
                                    editor.putString("verbiage_singular", userData.getString("verbiage_singular"));
                                    editor.putString("verbiage_plural", userData.getString("verbiage_plural"));
                                }
                            }

                            editor.putString("loggedInUser", jsonParentUserBean);
                            editor.commit();

                            switch (userBean.getRoleCode()) {
                                case "parent_role":

                                    Intent parentDashboard = new Intent(ChildMainScreen.this, ParentMainScreen.class);
                                    parentDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(parentDashboard);

                                    break;
                            }


                        } else {
                            Toast.makeText(ChildMainScreen.this, message, Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildMainScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    public void showNotificationDialog(String title, String message) {
        final Dialog dialog = new Dialog(ChildMainScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.common_notification_dialog);

        TextView notificationTitle = (TextView) dialog.findViewById(R.id.notificationTitle);
        TextView notificationBody = (TextView) dialog.findViewById(R.id.notificationBody);
        ImageView close = (ImageView) dialog.findViewById(R.id.close);

        notificationTitle.setText(title);
        notificationBody.setText(message);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void updateUser() {
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
            imageLoader.displayImage(loggedInUser.getProfilePicPath(), profilePhoto, options);
        }
    }

}