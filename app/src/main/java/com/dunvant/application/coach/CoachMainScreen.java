package com.dunvant.application.coach;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.dunvant.application.coach.adapters.CoachMainAdapter;
import com.dunvant.application.coach.fragments.CoachDashboardFragmentNew;
import com.dunvant.application.coach.fragments.CoachLeagueEditFragment;
import com.dunvant.application.coach.fragments.CoachManageAttendanceFragment;
import com.dunvant.application.coach.fragments.CoachManageChallengesFragment;
import com.dunvant.application.coach.fragments.CoachManageChildrenFragment;
import com.dunvant.application.coach.fragments.CoachManageDocumentsFragment;
import com.dunvant.application.coach.fragments.CoachManageScoresFragment;
import com.dunvant.application.coach.fragments.CoachManageTimelineFragment;
import com.dunvant.application.coach.fragments.CoachMidWeekAttendanceFragment;
import com.dunvant.application.coach.fragments.CoachRugbyFragment;
import com.dunvant.application.coach.fragments.CoachUpdateProfileFragment;
import com.dunvant.application.parent.fragments.ParentContactCoachFragment;
import com.dunvant.application.parent.fragments.ParentHelpDocFragment;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class CoachMainScreen extends AppCompatActivity implements IWebServiceCallback {
    ArrayList<ParentDashboardBean> parentDashboardBeanArrayListDuplicate = new ArrayList<>();;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    SlidingMenu slidingMenu;
    ImageView backButton;
    TextView title;
    ImageView menuIcon;
    ImageView profilePhoto;
    ImageView searchImageView;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    private final String DO_LOGOUT = "DO_LOGOUT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_activity_coach_dashboard);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        imageLoader.init(ImageLoaderConfiguration.createDefault(CoachMainScreen.this));
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

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(linoType);
        menuIcon = (ImageView) findViewById(R.id.slideMenu);
        searchImageView = (ImageView) findViewById(R.id.searchImageView);

        setUpRightSlidingMenuNew();
//        showDashboardScreen();

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();

            if(bundle == null) {
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
                        Toast.makeText(CoachMainScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Added new else block
                    showDashboardScreen();
                }
            }
        }

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout);
                if (f instanceof CoachManageChildrenFragment){
                    ((CoachManageChildrenFragment) f).showSearchLayout();
                } else if (f instanceof CoachManageAttendanceFragment) {
                    ((CoachManageAttendanceFragment) f).showSearchLayout();
                } else if (f instanceof CoachManageScoresFragment) {
                    ((CoachManageScoresFragment) f).showSearchLinearLayout();
                } else if (f instanceof CoachManageChallengesFragment) {
                    ((CoachManageChallengesFragment) f).showSearchLinearLayout();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDashboardScreen();
            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.toggle();
            }
        });
    }

    public void showNotificationDialog(String title, String message) {
        final Dialog dialog = new Dialog(CoachMainScreen.this);
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

    private void setUpRightSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.RIGHT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(1.0f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        View view = getLayoutInflater().inflate(R.layout.coach_sliding_menu, null);
        slidingMenu.setMenu(view);

        profilePhoto = (ImageView) view.findViewById(R.id.profilePhoto);
        TextView fullName = (TextView) view.findViewById(R.id.fullName);
//        TextView address = (TextView) view.findViewById(R.id.address);
//        TextView country = (TextView) view.findViewById(R.id.country);

        TextView dashboard = (TextView) view.findViewById(R.id.dashboard);
        TextView updateProfile = (TextView) view.findViewById(R.id.updateProfile);
        TextView manageTimeline = (TextView) view.findViewById(R.id.manageTimeline);
        TextView manageScores = (TextView) view.findViewById(R.id.manageScores);
        TextView attendance = (TextView) view.findViewById(R.id.attendance);
        TextView manageChildren = (TextView) view.findViewById(R.id.manageChildren);
        TextView setChallenges = (TextView) view.findViewById(R.id.setChallenges);
        TextView manageDocuments = (TextView) view.findViewById(R.id.manageDocuments);
        TextView help = (TextView) view.findViewById(R.id.help);

        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
        manageScores.setText(verbiage_singular+" Reports");

        fullName.setTypeface(helvetica);
//        address.setTypeface(helvetica);
//        country.setTypeface(helvetica);
        dashboard.setTypeface(helvetica);
        updateProfile.setTypeface(helvetica);
        manageTimeline.setTypeface(helvetica);
        manageScores.setTypeface(helvetica);
        attendance.setTypeface(helvetica);
        manageChildren.setTypeface(helvetica);
        setChallenges.setTypeface(helvetica);
        manageDocuments.setTypeface(helvetica);
        help.setTypeface(helvetica);

        TextView logout = (TextView) view.findViewById(R.id.logout);

        imageLoader.displayImage(loggedInUser.getProfilePicPath(), profilePhoto, options);
        fullName.setText(loggedInUser.getFullName());
//        address.setText(loggedInUser.getAddress());
//        country.setText(" (Dubai)");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(CoachMainScreen.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.parent_dialog_logout);

                TextView yes = (TextView) dialog.findViewById(R.id.yes);
                TextView no = (TextView) dialog.findViewById(R.id.no);

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

        manageTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showManageTimeline();
                slidingMenu.toggle();
            }
        });

        manageScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showManageScores();
                slidingMenu.toggle();
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showManageAttendance();
                slidingMenu.toggle();
            }
        });

        manageChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showManageChildren();
                slidingMenu.toggle();
            }
        });

        setChallenges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showManageChallenges();
                slidingMenu.toggle();
            }
        });

        manageDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showManageDocuments();
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
    }

    private void setUpRightSlidingMenuNew() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.RIGHT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(1.0f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        View view = getLayoutInflater().inflate(R.layout.coach_sliding_menu_new, null);
        slidingMenu.setMenu(view);

        profilePhoto = (ImageView) view.findViewById(R.id.profilePhoto);
        TextView fullName = (TextView) view.findViewById(R.id.fullName);


        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

        fullName.setTypeface(helvetica);
        TextView logout = (TextView) view.findViewById(R.id.logout);
        final ListView listView = view.findViewById(R.id.listView);

        imageLoader.displayImage(loggedInUser.getProfilePicPath(), profilePhoto, options);
        fullName.setText(loggedInUser.getFullName());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(CoachMainScreen.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.parent_dialog_logout);

                TextView yes = (TextView) dialog.findViewById(R.id.yes);
                TextView no = (TextView) dialog.findViewById(R.id.no);

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String label = parentDashboardBeanArrayListDuplicate.get(i).getLabel();

                if(label.equalsIgnoreCase("Dashboard")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showDashboardScreen();
                }else if(label.equalsIgnoreCase("Profile")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showUpdateProfile();
                }else if(label.equalsIgnoreCase("Posts")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showManageTimeline();
                }else if(label.equalsIgnoreCase("Participant Reports")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showManageScores();
                }else if(label.equalsIgnoreCase("Attendance")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showManageAttendance();
                }else if(label.equalsIgnoreCase("Team")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                   showManageChildren();
                }else if(label.equalsIgnoreCase("Set Challenges")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showManageChallenges();
                }else if(label.equalsIgnoreCase("Documents")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showManageDocuments();
                }else if(label.equalsIgnoreCase("Help")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    showHelp();
                }else if(label.equalsIgnoreCase("Rugby Education")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    rugbyEducation();
                }else if(label.equalsIgnoreCase("Midweek Attendance")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    midWeek();
                }else if(label.equalsIgnoreCase("Manage League")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("prefTitle", parentDashboardBeanArrayListDuplicate.get(i).getPreferred_text());
                    editor.commit();
                    leagueFrag();
                }else {
                    Toast.makeText(CoachMainScreen.this, "Server Error", Toast.LENGTH_SHORT).show();
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
                    parentDashboardBeanArrayListDuplicate.addAll(CoachDashboardFragmentNew.parentDashboardBeanArrayList);

                    ParentDashboardBean parentDashboardBean2 = new ParentDashboardBean();
                    parentDashboardBean2.setId("-11");
                    parentDashboardBean2.setNavigation_type("Help");
                    parentDashboardBean2.setLabel("Help");
                    parentDashboardBean2.setAcademy_id("-11");
                    parentDashboardBean2.setStatus("-11");
                    parentDashboardBean2.setPreferred_text("Help");
                    parentDashboardBean2.setSort("-11");
                    parentDashboardBeanArrayListDuplicate.add(parentDashboardBean2);

//                    parentDashboardBeanArrayListDuplicate.remove(parentDashboardBeanArrayListDuplicate.size()-1);
//                    parentDashboardBeanArrayListDuplicate.remove(parentDashboardBeanArrayListDuplicate.size()-1);
                    listView.setAdapter(new CoachMainAdapter(CoachMainScreen.this, parentDashboardBeanArrayListDuplicate));
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
    }

    private void doLogout(){
        if(Utilities.isNetworkAvailable(CoachMainScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "account/logout";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(CoachMainScreen.this, DO_LOGOUT, CoachMainScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachMainScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    public void showDashboardScreen() {
        title.setText("DASHBOARD");

        searchImageView.setVisibility(View.GONE);
        backButton.setVisibility(View.INVISIBLE);

        CoachDashboardFragmentNew coachDashboardFragment = new CoachDashboardFragmentNew();

        CoachMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, coachDashboardFragment)
                .commit();
    }

    public void showUpdateProfile() {
//        title.setText("PROFILE");
        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        searchImageView.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);

        CoachUpdateProfileFragment coachUpdateProfileFragment = new CoachUpdateProfileFragment();

        CoachMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, coachUpdateProfileFragment)
                .commit();
    }

    public void showManageTimeline() {
//        title.setText("POSTS");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        searchImageView.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);

        CoachManageTimelineFragment coachManageTimelineFragment = new CoachManageTimelineFragment();

        CoachMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, coachManageTimelineFragment)
                .commit();
    }

    public void showManageChildren() {
//        title.setText("TEAM");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        searchImageView.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);

        CoachManageChildrenFragment coachManageChildrenFragment = new CoachManageChildrenFragment();

        CoachMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, coachManageChildrenFragment)
                .commit();
    }

    public void showManageAttendance() {
//        title.setText("ATTENDANCE");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        searchImageView.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);

        CoachManageAttendanceFragment coachManageAttendanceFragment = new CoachManageAttendanceFragment();

        CoachMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, coachManageAttendanceFragment)
                .commit();
    }

    public void showManageScores() {

//        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
//        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
//
//        title.setText(verbiage_singular.toUpperCase()+" REPORTS");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        searchImageView.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);

        CoachManageScoresFragment coachManageScoresFragment = new CoachManageScoresFragment();

        CoachMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, coachManageScoresFragment)
                .commit();
    }

    public void showManageChallenges() {
//        title.setText("CHALLENGES");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        searchImageView.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);

        CoachManageChallengesFragment coachManageChallengesFragment = new CoachManageChallengesFragment();

        CoachMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, coachManageChallengesFragment)
                .commit();
    }

    public void showManageDocuments(){
//        title.setText("MANAGE DOCUMENTS");

        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        searchImageView.setVisibility(View.INVISIBLE);
        backButton.setVisibility(View.VISIBLE);

        CoachManageDocumentsFragment coachManageDocumentsFragment = new CoachManageDocumentsFragment();

        CoachMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, coachManageDocumentsFragment)
                .commit();
    }

    public void showHelp(){
        title.setText("HELP");

        searchImageView.setVisibility(View.INVISIBLE);
        backButton.setVisibility(View.VISIBLE);

        ParentHelpDocFragment parentHelpDocFragment = new ParentHelpDocFragment();
        CoachMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, parentHelpDocFragment)
                .commit();
    }

    public void rugbyEducation(){
//        title.setText("RUGBY EDUCATION");
        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        searchImageView.setVisibility(View.INVISIBLE);
        backButton.setVisibility(View.VISIBLE);

        CoachRugbyFragment coachRugbyFragment = new CoachRugbyFragment();
        CoachMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, coachRugbyFragment)
                .commit();
    }

    public void midWeek(){
//        title.setText("RUGBY EDUCATION");
        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        searchImageView.setVisibility(View.INVISIBLE);
        backButton.setVisibility(View.VISIBLE);

        CoachMidWeekAttendanceFragment coachMidWeekAttendanceFragment = new CoachMidWeekAttendanceFragment();
        CoachMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, coachMidWeekAttendanceFragment)
                .commit();
    }

    public void leagueFrag(){
        String prefTextStr = sharedPreferences.getString("prefTitle", null);
        title.setText(prefTextStr);

        searchImageView.setVisibility(View.INVISIBLE);
        backButton.setVisibility(View.VISIBLE);

        CoachLeagueEditFragment coachLeagueFragment = new CoachLeagueEditFragment();
        CoachMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, coachLeagueFragment)
                .commit();
    }

    public void showContactCoach(String strTitle){
//        title.setText("HELP");
        title.setText(strTitle);

        searchImageView.setVisibility(View.INVISIBLE);
        backButton.setVisibility(View.VISIBLE);

        ParentContactCoachFragment parentContactCoachFragment = new ParentContactCoachFragment();
        CoachMainScreen.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrameLayout, parentContactCoachFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout);
        if (f instanceof CoachDashboardFragmentNew) {
            final Dialog dialog = new Dialog(CoachMainScreen.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.parent_dialog_exit_app);

            TextView yes = (TextView) dialog.findViewById(R.id.yes);
            TextView no = (TextView) dialog.findViewById(R.id.no);

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

                if(response == null) {
                    Toast.makeText(CoachMainScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(CoachMainScreen.this, message, Toast.LENGTH_SHORT).show();

                        if(status) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isUserLoggedIn", false);
                            editor.commit();

                            Intent loginScreen = new Intent(CoachMainScreen.this, LoginScreen.class);
                            loginScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginScreen);
                        } else {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isUserLoggedIn", false);
                            editor.commit();

                            Intent loginScreen = new Intent(CoachMainScreen.this, LoginScreen.class);
                            loginScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginScreen);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachMainScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}