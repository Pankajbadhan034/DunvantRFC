package com.dunvant.application.parent.fragments;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
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
import com.dunvant.application.parent.ParentMainScreen;
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

public class ParentDashboardFragment extends Fragment implements IWebServiceCallback {

    private final String BADGE_COUNT = "BADGE_COUNT";
    TextView teamBadge;
    TextView uploadBadge;

    SharedPreferences sharedPreferences;
    Typeface helvetica;
    Typeface linoType;

    UserBean loggedInUser;
    ImageView profilePhoto;
    ImageView profilePhotoSmall;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    private final String DO_LOGOUT = "DO_LOGOUT";
    private final String CHECK_FOR_UPDATES = "CHECK_FOR_UPDATES";
    //TextView logoutHeader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

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
        View view = inflater.inflate(R.layout.parent_fragment_dashboard, container, false);

        profilePhoto = (ImageView) view.findViewById(R.id.profilePhoto);
        profilePhotoSmall = (ImageView) view.findViewById(R.id.profilePhotoSmall);

//        TextView fullName = (TextView) view.findViewById(R.id.fullName);
//        TextView address = (TextView) view.findViewById(R.id.address);
//        TextView country = (TextView) view.findViewById(R.id.country);

        TextView playerNewsfeed = (TextView) view.findViewById(R.id.playerNewsfeed);
        TextView playerCareer = (TextView) view.findViewById(R.id.playerCareer);

        TextView bookNow = (TextView) view.findViewById(R.id.bookNow);
        TextView updateProfile = (TextView) view.findViewById(R.id.updateProfile);
        TextView manageTimeline = (TextView) view.findViewById(R.id.manageTimeline);
        TextView manageChildren = (TextView) view.findViewById(R.id.manageChildren);
        TextView uploadDocuments = (TextView) view.findViewById(R.id.uploadDocuments);
//        TextView paymentDetails = (TextView) view.findViewById(R.id.paymentDetails);
//        TextView bookSession = (TextView) view.findViewById(R.id.bookSession);
//        TextView bookAPitch = (TextView) view.findViewById(R.id.bookAPitch);
//        TextView bookACamp = (TextView) view.findViewById(R.id.bookACamp);
        TextView bookALeague = (TextView) view.findViewById(R.id.bookALeague);
        TextView bookingHistory = (TextView) view.findViewById(R.id.bookingHistory);
//        TextView bookedSessions = (TextView) view.findViewById(R.id.bookedSessions);
//        TextView bookedCamp = (TextView) view.findViewById(R.id.bookedCamp);
//        TextView orderHistory = (TextView) view.findViewById(R.id.orderHistory);
//        TextView viewAttendance = (TextView) view.findViewById(R.id.viewAttendance);
//        TextView viewMarks = (TextView) view.findViewById(R.id.viewMarks);
//        TextView childrenActivities = (TextView) view.findViewById(R.id.childrenActivities);
//        TextView manageChallenge = (TextView) view.findViewById(R.id.manageChallenge);
        TextView contactCoach = (TextView) view.findViewById(R.id.contactCoach);
        LinearLayout addCardLinearLayout = view.findViewById(R.id.addCardLinearLayout);
        TextView addCard = view.findViewById(R.id.addCard);
        Button reportAbuse = (Button) view.findViewById(R.id.reportAbuse);
        Button logoutButton = (Button) view.findViewById(R.id.logoutButton);
        teamBadge = (TextView) view.findViewById(R.id.teamBadge);
        uploadBadge = (TextView) view.findViewById(R.id.uploadBadge);

        if(loggedInUser.getPaymentCard() != null && loggedInUser.getPaymentCard().equalsIgnoreCase("1")){
            addCardLinearLayout.setVisibility(View.VISIBLE);
        } else {
            addCardLinearLayout.setVisibility(View.GONE);
        }

        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

        manageChildren.setText("My "+verbiage_plural);
        playerNewsfeed.setText(verbiage_singular+" Newsfeed");
        playerCareer.setText(verbiage_singular+" Career");

        //logoutHeader = (TextView) view.findViewById(R.id.logoutHeader);

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
                ((ParentMainScreen) getActivity()).showUpdateProfile();
            }
        });

        playerNewsfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ParentMainScreen) getActivity()).showPlayerNewsfeed();
            }
        });

        playerCareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ParentMainScreen) getActivity()).showPlayerCareer();
            }
        });

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ParentMainScreen) getActivity()).showBookNow();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showUpdateProfile();
            }
        });

        manageTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ParentMainScreen) getActivity()).showManageTimeline();
            }
        });

        manageChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showManageChildren();
            }
        });

        uploadDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showDocumentListing();
            }
        });

        /*paymentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showPaymentDetail();
            }
        });

        bookSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showBookSession();
            }
        });

        bookAPitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showBookPitch();
            }
        });

        bookACamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showBookCamp();
            }
        });*/

        bookALeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ParentMainScreen) getActivity()).showBookLeague();
            }
        });

        bookingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ParentMainScreen) getActivity()).showBookingHistory();
            }
        });

        /*bookedSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showSessionHistory();
            }
        });

        bookedCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showCampBookingHistory();
            }
        });

        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showBookedPitches();
            }
        });*/

        /*viewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showViewAttendance();
            }
        });*/

        /*viewMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showViewMarks();
            }
        });*/

        /*childrenActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showViewChildrenActivities();
            }
        });

        manageChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showManageChallenges();
            }
        });*/

        contactCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ParentMainScreen) getActivity()).showContactCoach("CONTACT US");
            }
        });

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ParentMainScreen) getActivity()).showAddCard();
            }
        });

        reportAbuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ParentMainScreen) getActivity()).showContactCoach("REPORT AN ABUSE");
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
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

//        logoutHeader.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(getActivity());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.parent_dialog_logout);
//
//                TextView yes = (TextView) dialog.findViewById(R.id.yes);
//                TextView no = (TextView) dialog.findViewById(R.id.no);
//
//                no.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//
//                yes.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        dialog.dismiss();
//
//                        doLogout();
//
//                    }
//                });
//
//                dialog.show();
//            }
//        });

//        fullName.setTypeface(helvetica);
//        address.setTypeface(helvetica);
//        country.setTypeface(helvetica);
        playerNewsfeed.setTypeface(helvetica);
        playerCareer.setTypeface(helvetica);
        bookNow.setTypeface(helvetica);
        updateProfile.setTypeface(helvetica);
        manageTimeline.setTypeface(helvetica);
        manageChildren.setTypeface(helvetica);
        uploadDocuments.setTypeface(helvetica);
//        paymentDetails.setTypeface(helvetica);
//        bookSession.setTypeface(helvetica);
        bookALeague.setTypeface(helvetica);
//        bookAPitch.setTypeface(helvetica);
//        bookACamp.setTypeface(helvetica);
        bookingHistory.setTypeface(helvetica);
//        bookedSessions.setTypeface(helvetica);
//        bookedCamp.setTypeface(helvetica);
//        orderHistory.setTypeface(helvetica);
//        viewAttendance.setTypeface(helvetica);
//        viewMarks.setTypeface(helvetica);
//        childrenActivities.setTypeface(helvetica);
//        manageChallenge.setTypeface(helvetica);
        contactCoach.setTypeface(helvetica);
        addCard.setTypeface(helvetica);
        reportAbuse.setTypeface(linoType);
        logoutButton.setTypeface(linoType);
        teamBadge.setTypeface(linoType);
        uploadBadge.setTypeface(linoType);

        checkForUpdates();

        return view;
    }

    private void getApprovalCount(){
        if (Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "children/send_approval_count";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), BADGE_COUNT, ParentDashboardFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void doLogout() {
        if (Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "account/logout";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), DO_LOGOUT, ParentDashboardFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
    }

    private void checkForUpdates() {
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("build_version", Utilities.SYSTEM_VERSION));

        String webServiceUrl = Utilities.BASE_URL + "account/get_app_version";

        PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(getActivity(), nameValuePairList, CHECK_FOR_UPDATES, ParentDashboardFragment.this);
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

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
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

                            // Logout Facebook
                            LoginManager.getInstance().logOut();

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

            case BADGE_COUNT:
                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");

                        if (status) {
                            String requestCount = responseObject.getString("total_pending_approval");
                            String challengeCount = responseObject.getString("unread_document");

                            if (requestCount.equalsIgnoreCase("0")) {
                                teamBadge.setVisibility(View.INVISIBLE);
                            } else {
                                teamBadge.setVisibility(View.VISIBLE);
                                teamBadge.setText(requestCount);
                            }

                            if (challengeCount.equalsIgnoreCase("0")) {
                                uploadBadge.setVisibility(View.INVISIBLE);
                            } else {
                                uploadBadge.setVisibility(View.VISIBLE);
                                uploadBadge.setText(challengeCount);
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