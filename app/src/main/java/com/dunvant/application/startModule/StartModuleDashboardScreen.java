package com.dunvant.application.startModule;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.LoginScreen;
import com.dunvant.application.R;
import com.dunvant.application.beans.SliderImagesBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.calendarquickstart.MainActivity;
import com.dunvant.application.child.StartModuleCategoryArrangeBean;
import com.dunvant.application.child.StartModuleNewsListingBean;
import com.dunvant.application.parent.ParentAcademyListingWithFiltersScreen;
import com.dunvant.application.parent.ParentBookFacilityNewViewScreen;
import com.dunvant.application.parent.ParentBookPitchChooseGameScreen;
import com.dunvant.application.parent.ParentCampsListingScreen;
import com.dunvant.application.parent.ParentForgotPassword;
import com.dunvant.application.parent.ParentSignUpScreen;
import com.dunvant.application.startModule.adapter.ShopSliderAdapter;
import com.dunvant.application.startModule.adapter.StartModuleNewsListingAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.utils.VerticalTextView;
import com.dunvant.application.webservices.GetWebServiceAsync;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceAsync;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class StartModuleDashboardScreen extends AppCompatActivity implements IWebServiceCallback {
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int GOOGLE_CALENDAR_PERMISSION_CODE = 102;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    String viewStr;
    String idT1, idT2,idT3,idT4,idT5,idT6;
    String titleT1,titleT2,titleT3,titleT4,titleT5,titleT6;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    UserBean loggedInUser;
    LinearLayout forgotPasswordLinear;
    LinearLayout signUpLinear;
    Button login;
    EditText email;
    EditText password;
    TextInputLayout emailTextInputLayout;
    TextInputLayout passwordTextInputLayout;
    TextView forgotPassword;
    TextView clickHere;
    TextView notAMember;
    TextView signUpNow;
    String strEmail;
    String strPassword;

    private final String LOGIN_WEB_SERVICE = "LOGIN_WEB_SERVICE";
    private final String DO_LOGOUT = "DO_LOGOUT";
    private final String NEWS_LISTING = "NEWS_LISTING";
    private final String PAGE_POS_SET = "PAGE_POS_SET";
    private final String TEAM_LIST = "TEAM_LIST";

    private final String NEWS_TYPE_CHECK = "NEWS_TYPE_CHECK";
    String news_setting;
    private final String OUR_NEWS_LISTING = "OUR_NEWS_LISTING";

    // Run time permission
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    boolean isGuestUser;
    boolean isUserLoggedIn;
    String typeOfUser;
    SharedPreferences sharedPreferences;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    private final String ONLINE_PRODUCT_DATA = "ONLINE_PRODUCT_DATA";

    LinearLayout homeLinear;
    ImageView home;
    LinearLayout offersLinear;
    ImageView offers;
    LinearLayout strategyLinear;
    ImageView strategy;
    LinearLayout rugbyLinear;
    ImageView rugby;
    TextView title;
    LinearLayout linearView1;
    LinearLayout linearView2;
    LinearLayout linearView3;
    LinearLayout linearView4;
    LinearLayout linearView4Login;
    LinearLayout linearView5;

    // tab 1
    String linkStr;
    ViewPager adImage;
    RelativeLayout relativeSlider;
    ListView listViewNewsTab1;
    ArrayList<StartModuleNewsListingBean>startModuleNewsListingBeanArrayList = new ArrayList<>();

    // tab 3
    TextView linear1Tab1;
    TextView linear2Tab1;
    TextView linear3Tab1;
    TextView linear4Tab1;
    TextView linear5Tab1;
    TextView linear6Tab1;
    String clickIdStr, titleStr;
    ArrayList<StartModuleCategoryArrangeBean> startModuleTeamArrangeBeans = new ArrayList<>();


    // tab 2
    LinearLayout contactUs2;
    LinearLayout linear4Tab2;
    LinearLayout linear5Tab2;
    LinearLayout tickets;
    LinearLayout merchandise;

    // tab 4
    LinearLayout schedule;
    LinearLayout contactUs4;
    LinearLayout newsfeed;
    LinearLayout pic;
    LinearLayout reports;
    VerticalTextView attendSessionTV4;
    TextView reportsTV4;
    TextView newsfeedTV4;
    TextView programsTV4;
    TextView scheduleTV4;
    TextView picTV4;
    TextView contactTV4;

    ImageView logout;


    LinearLayout membership;
    TextView bookRoom;
    private final String GET_PITCH_LAYOUT = "GET_PITCH_LAYOUT";

    // tab 4
    ArrayList<StartModuleCategoryArrangeBean> startModuleCategoryArrangeBeans = new ArrayList<>();

    // tab 5
    LinearLayout teamLinear;
    ImageView teamIV;
    TextView minisTV5;
    TextView juniorsTV5;
    TextView teamTV5;
    TextView seniorsTV5;
    TextView womenTV5;
    TextView otherSportsTV5;
    TextView contactTV5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_dashboard_activity);
        homeLinear = findViewById(R.id.homeLinear);
        home = findViewById(R.id.home);
        offersLinear = findViewById(R.id.offersLinear);
        offers = findViewById(R.id.offers);
        strategyLinear = findViewById(R.id.strategyLinear);
        strategy = findViewById(R.id.strategy);
        rugbyLinear = findViewById(R.id.rugbyLinear);
        rugby = findViewById(R.id.rugby);
        teamLinear = findViewById(R.id.teamLinear);
        teamIV = findViewById(R.id.teamIV);
        title = findViewById(R.id.title);
        linearView1 = findViewById(R.id.linearView1);
        linearView2 = findViewById(R.id.linearView2);
        linearView3 = findViewById(R.id.linearView3);
        linearView4 = findViewById(R.id.linearView4);
        linearView4Login = findViewById(R.id.linearView4Login);
        linearView5 = findViewById(R.id.linearView5);
        logout = findViewById(R.id.logout);



        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false);
        isGuestUser = sharedPreferences.getBoolean("guestUser", false);
        typeOfUser = sharedPreferences.getString("typeOfUser", "");

        if(isGuestUser && isUserLoggedIn){
            logout.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
            if (jsonLoggedInUser != null) {
                loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
            }
        }else{
            logout.setVisibility(View.GONE);
        }

        imageLoader.init(ImageLoaderConfiguration.createDefault(StartModuleDashboardScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(StartModuleDashboardScreen.this);
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

        // tab 1
        linear1Tab1 = findViewById(R.id.linear1Tab1);
        linear2Tab1 = findViewById(R.id.linear2Tab1);
        linear3Tab1 = findViewById(R.id.linear3Tab1);
        linear4Tab1 = findViewById(R.id.linear4Tab1);
        linear5Tab1 = findViewById(R.id.linear5Tab1);
        linear6Tab1 = findViewById(R.id.linear6Tab1);
        adImage = findViewById(R.id.adImage);
        relativeSlider = findViewById(R.id.relativeSlider);
        listViewNewsTab1 = findViewById(R.id.listViewNewsTab1);

        listViewNewsTab1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleNewsFeedDetailScreen.class);
                obj.putExtra("title", startModuleNewsListingBeanArrayList.get(i).getTitle());
                obj.putExtra("newsId", startModuleNewsListingBeanArrayList.get(i).getNewsId());
                obj.putExtra("thumbnail", startModuleNewsListingBeanArrayList.get(i).getThumbnail());
                obj.putExtra("time", startModuleNewsListingBeanArrayList.get(i).getTime());
                obj.putExtra("type", startModuleNewsListingBeanArrayList.get(i).getType());
                startActivity(obj);
            }
        });

        linear1Tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickIdStr = "1";
                titleStr = "ANALYSIS";
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleResourceScreen.class);
                obj.putExtra("nameDynamic",linear1Tab1.getText().toString());
                obj.putExtra("id",clickIdStr);
                obj.putExtra("title",titleStr);
                startActivity(obj);
            }
        });
        linear2Tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickIdStr = "2";
                titleStr = "VIDEO";
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleResourceScreen.class);
                obj.putExtra("nameDynamic",linear2Tab1.getText().toString());
                obj.putExtra("id",clickIdStr);
                obj.putExtra("title",titleStr);
                startActivity(obj);
            }
        });
        linear3Tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickIdStr = "3";
                titleStr = "SESSION PLANS";
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleResourceScreen.class);
                obj.putExtra("nameDynamic",linear3Tab1.getText().toString());
                obj.putExtra("id",clickIdStr);
                obj.putExtra("title",titleStr);
                startActivity(obj);
            }
        });
        linear4Tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickIdStr = "4";
                titleStr = "SKILLS CURRICULUM";
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleResourceScreen.class);
                obj.putExtra("nameDynamic",linear4Tab1.getText().toString());
                obj.putExtra("id",clickIdStr);
                obj.putExtra("title",titleStr);
                startActivity(obj);
            }
        });
        linear5Tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickIdStr = "5";
                titleStr = "HEALTH AND WELL BEING";
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleResourceScreen.class);
                obj.putExtra("nameDynamic",linear5Tab1.getText().toString());
                obj.putExtra("id",clickIdStr);
                obj.putExtra("title",titleStr);
                startActivity(obj);
            }
        });

        linear6Tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickIdStr = "6";
                titleStr = "NUTRITION";
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleResourceScreen.class);
                obj.putExtra("nameDynamic",linear6Tab1.getText().toString());
                obj.putExtra("id",clickIdStr);
                obj.putExtra("title",titleStr);
                startActivity(obj);
            }
        });


        // tab 2
        contactUs2 = findViewById(R.id.contactUs2);
        linear4Tab2 = findViewById(R.id.linear4Tab2);
        tickets = findViewById(R.id.tickets);
        merchandise = findViewById(R.id.merchandise);

        membership = findViewById(R.id.membership);
        bookRoom = findViewById(R.id.bookRoom);


        tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.eticketing.co.uk/ospreysrugby/"));
                    startActivity(browserIntent);
                }catch (Exception e){
                    Toast.makeText(StartModuleDashboardScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


//                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleWebViewScreen.class);
//                obj.putExtra("link", "https://www.eticketing.co.uk/ospreysrugby/");
//                startActivity(obj);

            }
        });
        merchandise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://fituresportswear.com/collections/dunvant-rfc"));
                    startActivity(browserIntent);
                }catch (Exception e){
                    Toast.makeText(StartModuleDashboardScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }



//                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleWebViewScreen.class);
//                obj.putExtra("link", "https://shop.ospreysrugby.com/");
//                startActivity(obj);

            }
        });
        contactUs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleContactUsByAdminScreen.class);
                startActivity(obj);
            }
        });
        linear4Tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickIdStr = "Offers";
                titleStr = "OFFERS";
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartOfferAndLegacyScreen.class);
                obj.putExtra("id",clickIdStr);
                obj.putExtra("title",titleStr);
                startActivity(obj);
            }
        });
        membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                clickIdStr = "Legacy";
//                titleStr = "LEGACY";
//                Intent obj = new Intent(StartModuleDashboardScreen.this, StartOfferAndLegacyScreen.class);
//                obj.putExtra("id",clickIdStr);
//                obj.putExtra("title",titleStr);
//                startActivity(obj);

                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleMembershipActivity.class);
                startActivity(obj);
            }
        });

        bookRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserLoggedIn) {
                    getPitchLayout();

                }else{
                    new AlertDialog.Builder(StartModuleDashboardScreen.this)
                            .setTitle("Login Required")
                            .setMessage("Please login to continue.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent obj = new Intent(StartModuleDashboardScreen.this, LoginScreen.class);
                                    startActivity(obj);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }

            }
        });

        // tab 4 login screen
        forgotPasswordLinear = (LinearLayout) findViewById(R.id.forgotPasswordLinear);
        signUpLinear = (LinearLayout) findViewById(R.id.signUpLinear);
        login = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordTextInputLayout);
        emailTextInputLayout = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        clickHere = (TextView) findViewById(R.id.clickHere);
        notAMember = (TextView) findViewById(R.id.notAMember);
        signUpNow = (TextView) findViewById(R.id.signUpNow);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strEmail = email.getText().toString().trim();
                strPassword = password.getText().toString().trim();
                String fcmToken = sharedPreferences.getString("fcmToken", "");

                Pattern pattern = Pattern.compile(Utilities.EMAIL_PATTERN);
                Matcher matcher = pattern.matcher(strEmail);

                if (strEmail == null || strEmail.isEmpty()) {
                    Toast.makeText(StartModuleDashboardScreen.this, "Please enter Email", Toast.LENGTH_SHORT).show();
                }
//                else if (!matcher.matches()) {
//                    Toast.makeText(getApplicationContext(), "Please enter a valid Email", Toast.LENGTH_SHORT).show();
//                }
                else if (strPassword == null || strPassword.isEmpty()) {
                    Toast.makeText(StartModuleDashboardScreen.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    if (Utilities.isNetworkAvailable(StartModuleDashboardScreen.this)) {
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("lemail", strEmail));
                        nameValuePairList.add(new BasicNameValuePair("lpassword", strPassword));
                        nameValuePairList.add(new BasicNameValuePair("fcm_device_token", fcmToken));
                        nameValuePairList.add(new BasicNameValuePair("device_type", "1"));

                        String webServiceUrl = Utilities.BASE_URL + "account/login";

                        PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartModuleDashboardScreen.this, nameValuePairList, LOGIN_WEB_SERVICE, StartModuleDashboardScreen.this);
                        postWebServiceAsync.execute(webServiceUrl);
                    } else {
                        Toast.makeText(StartModuleDashboardScreen.this, R.string.internet_not_available, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        forgotPasswordLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPassword = new Intent(StartModuleDashboardScreen.this, ParentForgotPassword.class);
                startActivity(forgotPassword);
            }
        });

        signUpLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(StartModuleDashboardScreen.this, ParentSignUpScreen.class);
                startActivity(signUp);
            }
        });





        // tab 4 colorful dashboard
        schedule = findViewById(R.id.schedule);
        contactUs4 = findViewById(R.id.contactUs4);
        newsfeed = findViewById(R.id.newsfeed);
        reports = findViewById(R.id.reports);
        pic = findViewById(R.id.pic);
        attendSessionTV4 = findViewById(R.id.attendSessionTV4);
        reportsTV4 = findViewById(R.id.reportsTV4);
        newsfeedTV4 = findViewById(R.id.newsfeedTV4);
        programsTV4 = findViewById(R.id.programsTV4);
        scheduleTV4 = findViewById(R.id.scheduleTV4);
        picTV4 = findViewById(R.id.picTV4);
        contactTV4 = findViewById(R.id.contactTV4);

      //  if(!isGuestUser){
            if(typeOfUser.equalsIgnoreCase("parent")){
                attendSessionTV4.setText("");
                programsTV4.setText("");
                picTV4.setText("BOOK CAMP");
                contactTV4.setText("CONTACT US");

                attendSessionTV4.setText("BOOK SESSIONS");
                String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                programsTV4.setText("MY "+verbiage_plural.toUpperCase());

                programsTV4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTab4NewsFeedScreen.class);
                        obj.putExtra("type", "My Participants");
                        startActivity(obj);

                    }
                });


                attendSessionTV4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(StartModuleDashboardScreen.this, ParentAcademyListingWithFiltersScreen.class);
                        startActivity(intent);

                    }
                });

                reports.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleParentViewMarksScreen.class);
//                        startActivity(obj);

                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleReportsTypeScreen.class);
                        obj.putExtra("typeViewMarks", "parent");
                        startActivity(obj);


                    }
                });
                newsfeed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTab4NewsFeedScreen.class);
                        obj.putExtra("type", "NEWSFEED");
                        startActivity(obj);
                    }
                });
                schedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        checkPermission(Manifest.permission.READ_CALENDAR, GOOGLE_CALENDAR_PERMISSION_CODE);
                    }
                });

                picTV4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(StartModuleDashboardScreen.this, ParentCampsListingScreen.class);
                        startActivity(intent);
                    }
                });

                contactTV4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleContactUs.class);
                        startActivity(obj);
                    }
                });

            }else if(typeOfUser.equalsIgnoreCase("child")){

                if(loggedInUser.getUser_type().equalsIgnoreCase("5")){
                    picTV4.setText("BOOK CAMP");

                    picTV4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(StartModuleDashboardScreen.this, ParentCampsListingScreen.class);
                            startActivity(intent);
                        }
                    });
                }else{
                    picTV4.setText("LOGIN AS PARENT");
                    picTV4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showLoginAsParent();
                        }
                    });
                }



                attendSessionTV4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(loggedInUser.getUser_type().equalsIgnoreCase("5")){
//                            Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTab4NewsFeedScreen.class);
//                            obj.putExtra("type", "BOOK NOW");
//                            startActivity(obj);
                            Intent intent = new Intent(StartModuleDashboardScreen.this, ParentAcademyListingWithFiltersScreen.class);
                            startActivity(intent);
                        }else{
//                            Intent intent = new Intent(StartModuleDashboardScreen.this, ParentAcademyListingWithFiltersScreen.class);
//                            startActivity(intent);
                            Intent intent = new Intent(StartModuleDashboardScreen.this, ParentAcademyListingWithFiltersScreen.class);
                            startActivity(intent);
//                            Toast.makeText(StartModuleDashboardScreen.this, "You are not authorized to access this functionality", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                programsTV4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTab4MonitorAndCovidQaScreen.class);
                        startActivity(obj);
                    }
                });




                reports.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(loggedInUser.getUser_type().equalsIgnoreCase("5")){

                            Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleReportsTypeScreen.class);
                            obj.putExtra("typeViewMarks", "child");
                            startActivity(obj);

//                            Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleParentViewMarksScreen.class);
//                            startActivity(obj);
                        }else{
                            Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTab4ReportsScreen.class);
                            startActivity(obj);
                        }

                    }
                });
                newsfeed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTab4NewsFeedScreen.class);
                        obj.putExtra("type", "NEWSFEED + POST");
                        startActivity(obj);
//                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTab4NewsFeedScreen.class);
//                        obj.putExtra("type", "NEWSFEED");
//                        startActivity(obj);
                    }
                });
                schedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent obj = new Intent(StartModuleDashboardScreen.this, MainActivity.class);
                        startActivity(obj);
//                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTab4ScheduleScreen.class);
//                        startActivity(obj);
                    }
                });
                pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTab4PicScreen.class);
//                        startActivity(obj);
                    }
                });
                contactUs4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleContactUs.class);
                        startActivity(obj);
                    }
                });
            }else if(typeOfUser.equalsIgnoreCase("coach")){
                reportsTV4.setText("IDP's");
                attendSessionTV4.setText("ATTENDANCE");
                programsTV4.setText("COACH AREA");
                picTV4.setText("");
                contactTV4.setText("CONTACT US");

                attendSessionTV4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTab4NewsFeedScreen.class);
                        obj.putExtra("type", "ATTENDANCE");
                        startActivity(obj);

                    }
                });

                programsTV4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // http://202.164.57.202/ospreysrugby/public/api/v1/coach/timeline
                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTab4NewsFeedScreen.class);
                        obj.putExtra("type", "COACH AREA");
                        startActivity(obj);
                    }
                });

                reportsTV4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTab4AttendanceScreen.class);
                        obj.putExtra("type", "IDP's");
                        startActivity(obj);
                    }
                });

                newsfeed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTab4NewsFeedScreen.class);
                        obj.putExtra("type", "NEWSFEED");
                        startActivity(obj);
                    }
                });
                schedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent obj = new Intent(StartModuleDashboardScreen.this, MainActivity.class);
                        startActivity(obj);
//                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTab4ScheduleScreen.class);
//                        startActivity(obj);
                    }
                });
                contactUs4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                            Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleContactUsScreen.class);
//                            startActivity(obj);
                        Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleContactUs.class);
                        startActivity(obj);


                    }
                });
            }

//        }



        // tab 5

        minisTV5 = findViewById(R.id.minisTV5);
        juniorsTV5 = findViewById(R.id.juniorsTV5);
        teamTV5 = findViewById(R.id.teamTV5);
        seniorsTV5 = findViewById(R.id.seniorsTV5);
        womenTV5 = findViewById(R.id.womenTV5);
        otherSportsTV5 = findViewById(R.id.otherSportsTV5);
        contactTV5 = findViewById(R.id.contactTV5);


        minisTV5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTeamDetailScreen.class);
                obj.putExtra("id", idT1);
                obj.putExtra("title", titleT1);
                startActivity(obj);
            }
        });

        juniorsTV5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTeamDetailScreen.class);
                obj.putExtra("id", idT2);
                obj.putExtra("title", titleT2);
                startActivity(obj);
            }
        });

        teamTV5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTeamDetailScreen.class);
                obj.putExtra("id", idT3);
                obj.putExtra("title", titleT3);
                startActivity(obj);
            }
        });


        seniorsTV5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTeamDetailScreen.class);
                obj.putExtra("id", idT4);
                obj.putExtra("title", titleT4);
                startActivity(obj);
            }
        });

        womenTV5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTeamDetailScreen.class);
                obj.putExtra("id", idT5);
                obj.putExtra("title", titleT5);
                startActivity(obj);
            }
        });


        otherSportsTV5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleTeamDetailScreen.class);
                obj.putExtra("id", idT6);
                obj.putExtra("title", titleT6);
                startActivity(obj);
            }
        });


        contactTV5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleContactUsByAdminScreen.class);
                startActivity(obj);
            }
        });


        adList();

        homeLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adList();
                title.setText("LATEST NEWS");


                linearView1.setVisibility(View.VISIBLE);
                linearView2.setVisibility(View.GONE);
                linearView3.setVisibility(View.GONE);
                linearView4.setVisibility(View.GONE);
                linearView4Login.setVisibility(View.GONE);
                linearView5.setVisibility(View.GONE);

                homeLinear.setBackgroundColor(getResources().getColor(R.color.darkBlueTab));
                offersLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                strategyLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                rugbyLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                teamLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));

                home.setBackgroundResource(R.drawable.home_c_hover);
                offers.setBackgroundResource(R.drawable.offer_c);
                strategy.setBackgroundResource(R.drawable.strategy_c);
                rugby.setBackgroundResource(R.drawable.rugby_c);
                teamIV.setBackgroundResource(R.drawable.group_c);

            }
        });

        offersLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText("OFFERS");

                linearView1.setVisibility(View.GONE);
                linearView2.setVisibility(View.VISIBLE);
                linearView3.setVisibility(View.GONE);
                linearView4.setVisibility(View.GONE);
                linearView4Login.setVisibility(View.GONE);
                linearView5.setVisibility(View.GONE);

                homeLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                offersLinear.setBackgroundColor(getResources().getColor(R.color.darkBlueTab));
                strategyLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                rugbyLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                teamLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));

                home.setBackgroundResource(R.drawable.home_c);
                offers.setBackgroundResource(R.drawable.offer_c_hover);
                strategy.setBackgroundResource(R.drawable.strategy_c);
                rugby.setBackgroundResource(R.drawable.rugby_c);
                teamIV.setBackgroundResource(R.drawable.group_c);

            }
        });

        strategyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pagePosListAPI();

                title.setText("RESOURCES");

                linearView1.setVisibility(View.GONE);
                linearView2.setVisibility(View.GONE);
                linearView3.setVisibility(View.VISIBLE);
                linearView4.setVisibility(View.GONE);
                linearView4Login.setVisibility(View.GONE);
                linearView5.setVisibility(View.GONE);

                homeLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                offersLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                strategyLinear.setBackgroundColor(getResources().getColor(R.color.darkBlueTab));
                rugbyLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                teamLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));

                home.setBackgroundResource(R.drawable.home_c);
                offers.setBackgroundResource(R.drawable.offer_c);
                strategy.setBackgroundResource(R.drawable.strategy_c_hover);
                rugby.setBackgroundResource(R.drawable.rugby_c);
                teamIV.setBackgroundResource(R.drawable.group_c);

            }
        });

        rugbyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearView1.setVisibility(View.GONE);
                linearView2.setVisibility(View.GONE);
                linearView3.setVisibility(View.GONE);
                linearView5.setVisibility(View.GONE);

                homeLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                offersLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                strategyLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                rugbyLinear.setBackgroundColor(getResources().getColor(R.color.darkBlueTab));
                teamLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));

                home.setBackgroundResource(R.drawable.home_c);
                offers.setBackgroundResource(R.drawable.offer_c);
                strategy.setBackgroundResource(R.drawable.strategy_c);
                rugby.setBackgroundResource(R.drawable.rugby_c_hover);
                teamIV.setBackgroundResource(R.drawable.group_c);


                if(isGuestUser && isUserLoggedIn){

                    System.out.println("HERE::"+loggedInUser.getUser_type());

                    if(loggedInUser.getRoleCode().equalsIgnoreCase("parent_role")){
                        title.setText("PERFORM - PARENT VIEW");
                    }else if(loggedInUser.getRoleCode().equalsIgnoreCase("child_role")){

                        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                        title.setText("PERFORM - "+verbiage_singular.toUpperCase()+" VIEW");
                    }else{
                        title.setText("PERFORM - COACH VIEW");
                    }


                    linearView4.setVisibility(View.VISIBLE);
                    linearView4Login.setVisibility(View.GONE);
                    logout.setVisibility(View.VISIBLE);

                    Gson gson = new Gson();
                    String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
                    if (jsonLoggedInUser != null) {
                        loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
                    }

                }else{
                    title.setText("LOGIN");
                    linearView4.setVisibility(View.GONE);
                    linearView4Login.setVisibility(View.VISIBLE);
                    logout.setVisibility(View.GONE);

                }



            }
        });

        teamLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                teamDashboardAPI();

                title.setText("TEAM");

                linearView1.setVisibility(View.GONE);
                linearView2.setVisibility(View.GONE);
                linearView3.setVisibility(View.GONE);
                linearView4.setVisibility(View.GONE);
                linearView4Login.setVisibility(View.GONE);
                linearView5.setVisibility(View.VISIBLE);

                homeLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                offersLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                strategyLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                rugbyLinear.setBackgroundColor(getResources().getColor(R.color.blackTab));
                teamLinear.setBackgroundColor(getResources().getColor(R.color.darkBlueTab));

                home.setBackgroundResource(R.drawable.home_c);
                offers.setBackgroundResource(R.drawable.offer_c);
                strategy.setBackgroundResource(R.drawable.strategy_c);
                rugby.setBackgroundResource(R.drawable.rugby_c);
                teamIV.setBackgroundResource(R.drawable.group_c_hover);

            }
        });

    }

    private void adList() {
        if (Utilities.isNetworkAvailable(StartModuleDashboardScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));
//            nameValuePairList.add(new BasicNameValuePair("academy_id", "1"));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/ad_list";

            PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartModuleDashboardScreen.this, nameValuePairList, ONLINE_PRODUCT_DATA, StartModuleDashboardScreen.this);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleDashboardScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void pagePosListAPI() {
        if (Utilities.isNetworkAvailable(StartModuleDashboardScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));
//            nameValuePairList.add(new BasicNameValuePair("academy_id", "1"));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/cat_list";

            PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartModuleDashboardScreen.this, nameValuePairList, PAGE_POS_SET, StartModuleDashboardScreen.this);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleDashboardScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void teamDashboardAPI() {
        if (Utilities.isNetworkAvailable(StartModuleDashboardScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));
//            nameValuePairList.add(new BasicNameValuePair("academy_id", "1"));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/team_list";

            PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartModuleDashboardScreen.this, nameValuePairList, TEAM_LIST, StartModuleDashboardScreen.this);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleDashboardScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void newsTypeCheckAPI() {
        if (Utilities.isNetworkAvailable(StartModuleDashboardScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/news_settings";

            PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartModuleDashboardScreen.this, nameValuePairList, NEWS_TYPE_CHECK, StartModuleDashboardScreen.this);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleDashboardScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void ourNewsListingAPI() {
        if (Utilities.isNetworkAvailable(StartModuleDashboardScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/news_list";

            PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartModuleDashboardScreen.this, nameValuePairList, OUR_NEWS_LISTING, StartModuleDashboardScreen.this);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleDashboardScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void doLogout() {
        if (Utilities.isNetworkAvailable(StartModuleDashboardScreen.this)) {

//            String webServiceUrl = Utilities.BASE_URL + "children/get_child_reg_form";
            String webServiceUrl = Utilities.BASE_URL + "account/logout";
            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(StartModuleDashboardScreen.this, DO_LOGOUT, StartModuleDashboardScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleDashboardScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void newsListing() {
        if (Utilities.isNetworkAvailable(StartModuleDashboardScreen.this)) {

//            String webServiceUrl = Utilities.BASE_URL + "children/get_child_reg_form";
            String webServiceUrl = Utilities.NEWS_LIST;
            ArrayList<String> headers = new ArrayList<>();
//            headers.add("X-access-uid:" + loggedInUser.getId());
//            headers.add("X-access-token:" + loggedInUser.getToken());

            GetWebServiceAsync getWebServiceWithHeadersAsync = new GetWebServiceAsync(StartModuleDashboardScreen.this, NEWS_LISTING, StartModuleDashboardScreen.this);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleDashboardScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case TEAM_LIST:

                if (response == null) {
                    Toast.makeText(StartModuleDashboardScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        startModuleTeamArrangeBeans.clear();
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if(status){
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StartModuleCategoryArrangeBean startModuleCategoryArrangeBean = new StartModuleCategoryArrangeBean();
                                startModuleCategoryArrangeBean.setName(jsonObject.getString("title"));
                                startModuleCategoryArrangeBean.setId(jsonObject.getString("id"));
                                startModuleCategoryArrangeBean.setState(jsonObject.getString("state"));
                                startModuleTeamArrangeBeans.add(startModuleCategoryArrangeBean);

                            }


                            try{
                                for(int i=0; i<startModuleTeamArrangeBeans.size(); i++){

                                    if(i==0){
                                        if(startModuleTeamArrangeBeans.get(0).getState().equalsIgnoreCase("1")){
                                            minisTV5.setText(startModuleTeamArrangeBeans.get(0).getName());
                                            minisTV5.setVisibility(View.VISIBLE);
                                            idT1 = startModuleTeamArrangeBeans.get(0).getId();
                                            titleT1 = startModuleTeamArrangeBeans.get(0).getName();
                                        }else{
                                            minisTV5.setVisibility(View.GONE);
                                        }

                                    }

                                    if(i==1){
                                        if(startModuleTeamArrangeBeans.get(1).getState().equalsIgnoreCase("1")){
                                            juniorsTV5.setText(startModuleTeamArrangeBeans.get(1).getName());
                                            juniorsTV5.setVisibility(View.VISIBLE);
                                            idT2 = startModuleTeamArrangeBeans.get(1).getId();
                                            titleT2 = startModuleTeamArrangeBeans.get(1).getName();
                                        }else{
                                            juniorsTV5.setVisibility(View.GONE);
                                        }

                                    }

                                    if(i==2){
                                        if(startModuleTeamArrangeBeans.get(2).getState().equalsIgnoreCase("1")){
                                            teamTV5.setText(startModuleTeamArrangeBeans.get(2).getName());
                                            teamTV5.setVisibility(View.VISIBLE);
                                            idT3 = startModuleTeamArrangeBeans.get(2).getId();
                                            titleT3 = startModuleTeamArrangeBeans.get(2).getName();
                                        }else{
                                            teamTV5.setVisibility(View.GONE);
                                        }

                                    }

                                    if(i==3){
                                        if(startModuleTeamArrangeBeans.get(3).getState().equalsIgnoreCase("1")){
                                            seniorsTV5.setText(startModuleTeamArrangeBeans.get(3).getName());
                                            seniorsTV5.setVisibility(View.VISIBLE);
                                            idT4 = startModuleTeamArrangeBeans.get(3).getId();
                                            titleT4 = startModuleTeamArrangeBeans.get(3).getName();
                                        }else{
                                            seniorsTV5.setVisibility(View.GONE);
                                        }

                                    }

                                    if(i==4){
                                        if(startModuleTeamArrangeBeans.get(4).getState().equalsIgnoreCase("1")){
                                            womenTV5.setText(startModuleTeamArrangeBeans.get(4).getName());
                                            womenTV5.setVisibility(View.VISIBLE);
                                            idT5 = startModuleTeamArrangeBeans.get(4).getId();
                                            titleT5 = startModuleTeamArrangeBeans.get(4).getName();
                                        }else{
                                            womenTV5.setVisibility(View.GONE);
                                        }

                                    }

                                    if(i==5){
                                        if(startModuleTeamArrangeBeans.get(5).getState().equalsIgnoreCase("1")){
                                            otherSportsTV5.setText(startModuleTeamArrangeBeans.get(5).getName());
                                            otherSportsTV5.setVisibility(View.VISIBLE);
                                            idT6 = startModuleTeamArrangeBeans.get(5).getId();
                                            titleT6 = startModuleTeamArrangeBeans.get(5).getName();
                                        }else{
                                            otherSportsTV5.setVisibility(View.GONE);
                                        }

                                    }

                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }



                        }else{
                        //    Toast.makeText(StartModuleDashboardScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                     //   Toast.makeText(StartModuleDashboardScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case PAGE_POS_SET:

                if (response == null) {
                    Toast.makeText(StartModuleDashboardScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        startModuleCategoryArrangeBeans.clear();
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if(status){
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StartModuleCategoryArrangeBean startModuleCategoryArrangeBean = new StartModuleCategoryArrangeBean();
                                startModuleCategoryArrangeBean.setName(jsonObject.getString("name"));
                                startModuleCategoryArrangeBean.setId(jsonObject.getString("id"));
                                startModuleCategoryArrangeBean.setState(jsonObject.getString("state"));
                                startModuleCategoryArrangeBeans.add(startModuleCategoryArrangeBean);
                            }

                            for(int i=0; i<startModuleCategoryArrangeBeans.size(); i++){
                                if(i==0){
                                    if(startModuleCategoryArrangeBeans.get(0).getState().equalsIgnoreCase("1")){
                                        linear1Tab1.setText(startModuleCategoryArrangeBeans.get(0).getName());
                                        linear1Tab1.setVisibility(View.VISIBLE);
                                    }else{
                                        linear1Tab1.setVisibility(View.GONE);
                                    }

                                }

                                if(i==1){
                                    if(startModuleCategoryArrangeBeans.get(1).getState().equalsIgnoreCase("1")){
                                        linear2Tab1.setText(startModuleCategoryArrangeBeans.get(1).getName());
                                        linear2Tab1.setVisibility(View.VISIBLE);
                                    }else{
                                        linear2Tab1.setVisibility(View.GONE);
                                    }

                                }

                                if(i==2){
                                    if(startModuleCategoryArrangeBeans.get(2).getState().equalsIgnoreCase("1")){
                                        linear3Tab1.setText(startModuleCategoryArrangeBeans.get(2).getName());
                                        linear3Tab1.setVisibility(View.VISIBLE);
                                    }else{
                                        linear3Tab1.setVisibility(View.GONE);
                                    }

                                }

                                if(i==3){
                                    if(startModuleCategoryArrangeBeans.get(3).getState().equalsIgnoreCase("1")){
                                        linear4Tab1.setText(startModuleCategoryArrangeBeans.get(3).getName());
                                        linear4Tab1.setVisibility(View.VISIBLE);
                                    }else{
                                        linear4Tab1.setVisibility(View.GONE);
                                    }

                                }

                                if(i==4){
                                    if(startModuleCategoryArrangeBeans.get(4).getState().equalsIgnoreCase("1")){
                                        linear5Tab1.setText(startModuleCategoryArrangeBeans.get(4).getName());
                                        linear5Tab1.setVisibility(View.VISIBLE);
                                    }else{
                                        linear5Tab1.setVisibility(View.GONE);
                                    }

                                }

                                if(i==5){
                                    if(startModuleCategoryArrangeBeans.get(5).getState().equalsIgnoreCase("1")){
                                        linear6Tab1.setText(startModuleCategoryArrangeBeans.get(5).getName());
                                        linear6Tab1.setVisibility(View.VISIBLE);
                                    }else{
                                        linear6Tab1.setVisibility(View.GONE);
                                    }

                                }
                            }







                        }else{
                       //     Toast.makeText(StartModuleDashboardScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                     //   Toast.makeText(StartModuleDashboardScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case NEWS_LISTING:

                System.out.println("RES::"+response);

                if (response == null) {
                    Toast.makeText(StartModuleDashboardScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        startModuleNewsListingBeanArrayList.clear();

                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("Success");
                        if(status){
                            JSONArray jsonArray = responseObject.getJSONArray("Data");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StartModuleNewsListingBean startModuleNewsListingBean = new StartModuleNewsListingBean();
                                startModuleNewsListingBean.setNewsId(jsonObject.getString("NewsId"));
                                startModuleNewsListingBean.setTitle(jsonObject.getString("Title"));
                                startModuleNewsListingBean.setThumbnail(jsonObject.getString("Thumbnail"));
                                startModuleNewsListingBean.setTime(jsonObject.getString("Time"));
                                startModuleNewsListingBean.setType(news_setting);
                                startModuleNewsListingBeanArrayList.add(startModuleNewsListingBean);
                            }

                            StartModuleNewsListingAdapter startModuleNewsListingAdapter = new StartModuleNewsListingAdapter(StartModuleDashboardScreen.this, startModuleNewsListingBeanArrayList);
                            listViewNewsTab1.setAdapter(startModuleNewsListingAdapter);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleDashboardScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case NEWS_TYPE_CHECK:

                if (response == null) {
                    Toast.makeText(StartModuleDashboardScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        if(status){
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                news_setting = jsonObject.getString("news_setting");
                            }

                            if(news_setting.equalsIgnoreCase("1")){
                                ourNewsListingAPI();
                            }else{
                                newsListing();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleDashboardScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case OUR_NEWS_LISTING:

                if (response == null) {
                    Toast.makeText(StartModuleDashboardScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        startModuleNewsListingBeanArrayList.clear();

                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("Success");
                        if(status){
                            JSONArray jsonArray = responseObject.getJSONArray("Data");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StartModuleNewsListingBean startModuleNewsListingBean = new StartModuleNewsListingBean();
                                startModuleNewsListingBean.setNewsId(jsonObject.getString("NewsId"));
                                startModuleNewsListingBean.setTitle(jsonObject.getString("Title"));
                                startModuleNewsListingBean.setThumbnail(jsonObject.getString("Thumbnail"));
                                startModuleNewsListingBean.setTime(jsonObject.getString("Time"));
                                startModuleNewsListingBean.setType(news_setting);
                                startModuleNewsListingBeanArrayList.add(startModuleNewsListingBean);
                            }

                            StartModuleNewsListingAdapter startModuleNewsListingAdapter = new StartModuleNewsListingAdapter(StartModuleDashboardScreen.this, startModuleNewsListingBeanArrayList);
                            listViewNewsTab1.setAdapter(startModuleNewsListingAdapter);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleDashboardScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case DO_LOGOUT:

                System.out.println("RES::"+response);

                if (response == null) {
                    Toast.makeText(StartModuleDashboardScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(StartModuleDashboardScreen.this, message, Toast.LENGTH_SHORT).show();

                        if (status) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isUserLoggedIn", false);
                            editor.putBoolean("guestUser", false);
                            editor.commit();

                            Intent loginScreen = new Intent(StartModuleDashboardScreen.this, StartModuleEnterEmailScreen.class);
                            loginScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginScreen);
                        } else {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isUserLoggedIn", false);
                            editor.putBoolean("guestUser", false);
                            editor.commit();

                            Intent loginScreen = new Intent(StartModuleDashboardScreen.this, StartModuleEnterEmailScreen.class);
                            loginScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginScreen);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleDashboardScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case LOGIN_WEB_SERVICE:

                if (response == null) {
                    Toast.makeText(StartModuleDashboardScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        System.out.println("response::"+responseObject);

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
                                userBean.setAge(userData.getString("age"));

                                userBean.setFieldCLub(userData.getString("club"));
                                userBean.setClassName(userData.getString("classname"));

                                // userBean.setCategoryId(userData.getString("category_id"));
                            }

                            // added for Coach Module
                            if(userBean.getRoleCode().equalsIgnoreCase("coach_role")){
                                userBean.setCanMoveChild(userData.getString("can_move_child"));
                                userBean.setReportSubmissionType(userData.getString("report_submission_type"));
                            }

                            if(userBean.getRoleCode().equalsIgnoreCase("parent_role")){
                                userBean.setPaymentCard(userData.getString("payment_card"));
                            }

                            userBean.setPhoneCodeOne(userData.getString("phone_code_1"));
                            userBean.setPhoneCodeTwo(userData.getString("phone_code_2"));

                            if(userData.has("user_type")){
                                if(userData.getString("user_type").equalsIgnoreCase("5")){
                                    userBean.setUser_type(userData.getString("user_type"));
                                    userBean.setRoleCode("child_role");
                                }else{
                                    userBean.setUser_type(userData.getString("user_type"));
                                }

                            }else{
                                userBean.setUser_type("");
                            }

                            Gson gson = new Gson();
                            String jsonParentUserBean = gson.toJson(userBean);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isUserLoggedIn", true);
                            editor.putBoolean("guestUser", true);
                            editor.putString("cUserName", userBean.getFullName());
                            editor.putString("cEmail", userBean.getEmail());
                            editor.putString("cPhone", userBean.getMobileNumber());


                            if (userBean.getRoleCode().equalsIgnoreCase("parent_role")) {
                                editor.putString("typeOfUser", "parent");
                                if(userData.has("academy_currency")){
                                    editor.putString("academy_currency", userData.getString("academy_currency"));
                                    editor.putString("verbiage_singular", userData.getString("verbiage_singular"));
                                    editor.putString("verbiage_plural", userData.getString("verbiage_plural"));
                                    editor.putString("day_start_num", userData.getString("day_start_num"));
                                }else{
                                    editor.putString("academy_currency", "AED");
                                    editor.putString("verbiage_singular", userData.getString("Child"));
                                    editor.putString("verbiage_plural", userData.getString("Children"));
                                    editor.putString("day_start_num", "1");
                                }
                            } else if (userBean.getRoleCode().equalsIgnoreCase("child_role")) {
                                editor.putString("typeOfUser", "child");
                                if(userData.has("club")){
                                    editor.putString("club", userData.getString("club"));
                                    editor.putString("classname", userData.getString("classname"));
                                }
                                if(userData.has("academy_currency")){
                                    editor.putString("academy_currency", userData.getString("academy_currency"));
                                    editor.putString("verbiage_singular", userData.getString("verbiage_singular"));
                                    editor.putString("verbiage_plural", userData.getString("verbiage_plural"));
                                    editor.putString("day_start_num", userData.getString("day_start_num"));
                                }else{
                                    editor.putString("academy_currency", "AED");
                                    editor.putString("verbiage_singular", userData.getString("Child"));
                                    editor.putString("verbiage_plural", userData.getString("Children"));
                                    editor.putString("day_start_num", "1");
                                }
                            } else if (userBean.getRoleCode().equalsIgnoreCase("coach_role")) {
                                editor.putString("typeOfUser", "coach");
                                if(userData.has("academy_currency")){
                                    editor.putString("academy_currency", userData.getString("academy_currency"));
                                    editor.putString("verbiage_singular", userData.getString("verbiage_singular"));
                                    editor.putString("verbiage_plural", userData.getString("verbiage_plural"));
                                    editor.putString("day_start_num", userData.getString("day_start_num"));
                                }else{
                                    editor.putString("academy_currency", "AED");
                                    editor.putString("verbiage_singular", userData.getString("Child"));
                                    editor.putString("verbiage_plural", userData.getString("Children"));
                                    editor.putString("day_start_num", "1");
                                }
                            }

                            editor.putString("loggedInUser", jsonParentUserBean);
                            editor.commit();

                            switch (userBean.getRoleCode()) {
                                case "parent_role":
                                    Intent obj = new Intent(StartModuleDashboardScreen.this, StartModuleDashboardScreen.class);
                                    obj.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(obj);

//                                    Intent parentDashboard = new Intent(LoginScreen.this, ParentMainScreen.class);
//                                    parentDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(parentDashboard);

                                    break;
                                case "child_role":
                                    Intent obj1 = new Intent(StartModuleDashboardScreen.this, StartModuleDashboardScreen.class);
                                    obj1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(obj1);

//                                    Intent childDashboard = new Intent(LoginScreen.this, ChildMainScreen.class);
//                                    childDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(childDashboard);

                                    break;
                                case "coach_role":
                                    Intent obj2 = new Intent(StartModuleDashboardScreen.this, StartModuleDashboardScreen.class);
                                    obj2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(obj2);

//                                    Intent coachDashboard = new Intent(StartModuleDashboardScreen.this, CoachMainScreen.class);
//                                    coachDashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(coachDashboard);

                                    break;
                            }


                        } else {
                            Toast.makeText(StartModuleDashboardScreen.this, message, Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleDashboardScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case ONLINE_PRODUCT_DATA:

                if (response == null) {
                    Toast.makeText(StartModuleDashboardScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            final ArrayList<SliderImagesBean> sliderBeanArrayList = new ArrayList<>();
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                SliderImagesBean sliderImagesBean = new SliderImagesBean();
                                sliderImagesBean.setImage(jsonObject.getString("image"));
                                sliderImagesBean.setImage_url(jsonObject.getString("image_url"));
                                sliderImagesBean.setUrl(jsonObject.getString("url"));
                                linkStr = jsonObject.getString("url");
                                sliderBeanArrayList.add(sliderImagesBean);
                              //  imageLoader.displayImage(imageUrl+""+fileName, adImage, options);
                            }

                            ShopSliderAdapter shopSliderAdapter = new ShopSliderAdapter(StartModuleDashboardScreen.this, sliderBeanArrayList);
                            adImage.setAdapter(shopSliderAdapter);

                            /*After setting the adapter use the timer */
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {
                                    if (currentPage == sliderBeanArrayList.size()) {
                                        currentPage = 0;
                                    }
                                    adImage.setCurrentItem(currentPage++, true);
                                }
                            };

                            timer = new Timer(); // This will create a new Thread
                            timer.schedule(new TimerTask() { // task to be scheduled
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, DELAY_MS, PERIOD_MS);

                            newsTypeCheckAPI();
                            relativeSlider.setVisibility(View.VISIBLE);
                        } else {
                            newsTypeCheckAPI();
                            relativeSlider.setVisibility(View.GONE);
//                            listViewNewsTab1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,1f));
//                            relativeSlider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,0.1f));

                            // Toast.makeText(StartModuleDashboardScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleDashboardScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case GET_PITCH_LAYOUT:

                if(response == null) {
                    Toast.makeText(StartModuleDashboardScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONObject dataObj = responseObject.getJSONObject("data");
                            viewStr = dataObj.getString("pitch_layout");
                            if (viewStr.equalsIgnoreCase("1")) {
                                Intent intent = new Intent(StartModuleDashboardScreen.this, ParentBookPitchChooseGameScreen.class);
                                startActivity(intent);
                            } else if (viewStr.equalsIgnoreCase("2")) {
                                Intent bookedSession = new Intent(StartModuleDashboardScreen.this, ParentBookFacilityNewViewScreen.class);
                                startActivity(bookedSession);
                            }
                        } else {
                            Toast.makeText(StartModuleDashboardScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

        }
    }



    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(StartModuleDashboardScreen.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(StartModuleDashboardScreen.this, new String[]{permission}, requestCode);
        } else {
            if (requestCode == GOOGLE_CALENDAR_PERMISSION_CODE) {
                Intent obj = new Intent(StartModuleDashboardScreen.this, MainActivity.class);
                startActivity(obj);
            }
//            Toast.makeText(StartModuleDashboardScreen.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == GOOGLE_CALENDAR_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent obj = new Intent(StartModuleDashboardScreen.this, MainActivity.class);
                startActivity(obj);
                //     Toast.makeText(StartModuleDashboardScreen.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(StartModuleDashboardScreen.this, "This feature cannot work without Allow Permissions.", Toast.LENGTH_SHORT).show();
            }
        }
    }




    public void showLoginAsParent() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText editText = new EditText(StartModuleDashboardScreen.this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText.setMaxLines(1);
        alert.setMessage("Please enter password to continue");
        alert.setView(editText);

        alert.setPositiveButton("Login As Parent", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String strPassword = editText.getText().toString();
                if (strPassword == null || strPassword.isEmpty()) {
                    Toast.makeText(StartModuleDashboardScreen.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                } else {

                    String fcmToken = sharedPreferences.getString("fcmToken", "");

                    if (Utilities.isNetworkAvailable(StartModuleDashboardScreen.this)) {
                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("lemail", loggedInUser.getParentUsername()));
                        nameValuePairList.add(new BasicNameValuePair("lpassword", strPassword));
                        nameValuePairList.add(new BasicNameValuePair("fcm_device_token", fcmToken));
                        nameValuePairList.add(new BasicNameValuePair("device_type", "1"));

                        String webServiceUrl = Utilities.BASE_URL + "account/login";

                        PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartModuleDashboardScreen.this, nameValuePairList, LOGIN_WEB_SERVICE, StartModuleDashboardScreen.this);
                        postWebServiceAsync.execute(webServiceUrl);
                    } else {
                        Toast.makeText(StartModuleDashboardScreen.this, R.string.internet_not_available, Toast.LENGTH_LONG).show();
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

    private void getPitchLayout(){
        if(Utilities.isNetworkAvailable(StartModuleDashboardScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));

            String webServiceUrl = Utilities.BASE_URL + "front_calendar/pitch_layout";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            System.out.println("X-access-uid: "+loggedInUser.getId() +"X-access-token: "+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(StartModuleDashboardScreen.this, nameValuePairList, GET_PITCH_LAYOUT, StartModuleDashboardScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleDashboardScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }
}