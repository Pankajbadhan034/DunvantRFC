package com.dunvant.application.startModule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;

public class StartModuleNewsFeedDetailScreen extends AppCompatActivity implements IWebServiceCallback {
    ImageView backButton;
    TextView title;
    String titleStr;
    String newsIdStr;
    String thumbnailStr;
    String timeStr;
    ImageView image;
    TextView heading;
    TextView dateTime;
    WebView description;
    private final String NEWS_DETAIL = "NEWS_DETAIL";
    private final String OUR_NEWS_DETAIL = "OUR_NEWS_DETAIL";
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    Typeface helvetica;
    Typeface linoType;
    RelativeLayout relativeVideo;
    ImageView videothumb;
    String videoStr;
    String typeIntent;
    SharedPreferences sharedPreferences;
    boolean isGuestUser;
    boolean isUserLoggedIn;
    String typeOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_news_feed_detail_activity);

        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        image = findViewById(R.id.image);
        heading = findViewById(R.id.heading);
        dateTime = findViewById(R.id.dateTime);
        description = findViewById(R.id.description);
        relativeVideo = findViewById(R.id.relativeVideo);
        videothumb = findViewById(R.id.videothumb);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false);
        isGuestUser = sharedPreferences.getBoolean("guestUser", false);
        typeOfUser = sharedPreferences.getString("typeOfUser", "");

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        imageLoader.init(ImageLoaderConfiguration.createDefault(StartModuleNewsFeedDetailScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        titleStr = getIntent().getStringExtra("title");
        newsIdStr = getIntent().getStringExtra("newsId");
        thumbnailStr = getIntent().getStringExtra("thumbnail");
        timeStr = getIntent().getStringExtra("time");
        typeIntent = getIntent().getStringExtra("type");


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        videothumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(videoStr));
                    startActivity(browserIntent);
                }catch (Exception e){
                    Toast.makeText(StartModuleNewsFeedDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(typeIntent.equalsIgnoreCase("1")){
            ourNewsListingAPI();
        }else{
            newsDetail();
        }



    }

    private void ourNewsListingAPI() {
        if (Utilities.isNetworkAvailable(StartModuleNewsFeedDetailScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));
            nameValuePairList.add(new BasicNameValuePair("news_id", newsIdStr));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/news_details";

            PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartModuleNewsFeedDetailScreen.this, nameValuePairList, OUR_NEWS_DETAIL, StartModuleNewsFeedDetailScreen.this);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleNewsFeedDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void newsDetail() {
        if (Utilities.isNetworkAvailable(StartModuleNewsFeedDetailScreen.this)) {

//            String webServiceUrl = Utilities.BASE_URL + "children/get_child_reg_form";
            String webServiceUrl = Utilities.NEWS_LIST_DETAIL+""+newsIdStr;
            ArrayList<String> headers = new ArrayList<>();
//            headers.add("X-access-uid:" + loggedInUser.getId());
//            headers.add("X-access-token:" + loggedInUser.getToken());

            GetWebServiceAsync getWebServiceWithHeadersAsync = new GetWebServiceAsync(StartModuleNewsFeedDetailScreen.this, NEWS_DETAIL, StartModuleNewsFeedDetailScreen.this);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleNewsFeedDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case NEWS_DETAIL:

                System.out.println("RES::"+response);

                if (response == null) {
                    Toast.makeText(StartModuleNewsFeedDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {

                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("Success");
                        if(status){
                            JSONObject jsonObject = responseObject.getJSONObject("Data");
                            String title = jsonObject.getString("Title");
                            String thumbnail = jsonObject.getString("Thumbnail");
                            String Image = jsonObject.getString("Image");
                            String time = jsonObject.getString("Time");
                             videoStr = jsonObject.getString("Video");
                            String Description = jsonObject.getString("Description");

                            heading.setText(title);

                            if(videoStr.equalsIgnoreCase("")){
                                imageLoader.displayImage(Image, image, options);
                                relativeVideo.setVisibility(View.GONE);
                                image.setVisibility(View.VISIBLE);
                            }else{
                                imageLoader.displayImage(thumbnail, videothumb, options);
                                relativeVideo.setVisibility(View.VISIBLE);
                                image.setVisibility(View.GONE);
                            }

//                                dateTime.setText(getDate(Long.parseLong(time), "dd MMM yyyy"));
//
                            try{
                                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                                cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                                cal.setTimeInMillis(Long.parseLong(time) * 1000);
                                String date = DateFormat.format("dd MMM yyyy", cal).toString();
                                dateTime.setText(date);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

//                            description.setText(Html.fromHtml(Description));

                            description.requestFocus();
                            description.getSettings().setJavaScriptEnabled(true);
                            description.setBackgroundColor(Color.TRANSPARENT);
                            description.loadData("<html><body>   <font color='white'>"+Description+"</font>  </body></html>",
                                    "text/html", "UTF-8");


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleNewsFeedDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case OUR_NEWS_DETAIL:

                if (response == null) {
                    Toast.makeText(StartModuleNewsFeedDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {

                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("Success");
                        if(status){
                            JSONObject jsonObject = responseObject.getJSONObject("Data");
                            String title = jsonObject.getString("Title");
                            String thumbnail = jsonObject.getString("Thumbnail");
                            String Image = jsonObject.getString("Image");
                            String time = jsonObject.getString("Time");
                            videoStr = jsonObject.getString("Video");
                            String Description = jsonObject.getString("Description");

                            heading.setText(title);

                            if(videoStr.equalsIgnoreCase("")){
                                imageLoader.displayImage(Image, image, options);
                                relativeVideo.setVisibility(View.GONE);
                                image.setVisibility(View.VISIBLE);
                            }else{
                                imageLoader.displayImage(thumbnail, videothumb, options);
                                relativeVideo.setVisibility(View.VISIBLE);
                                image.setVisibility(View.GONE);
                            }

//                                dateTime.setText(getDate(Long.parseLong(time), "dd MMM yyyy"));

//
                            dateTime.setText(time);

//                            description.setText(Html.fromHtml(Description));

                            description.requestFocus();
                            description.getSettings().setJavaScriptEnabled(true);
                            description.setBackgroundColor(Color.TRANSPARENT);
                            description.loadData("<html><body>   <font color='white'>"+Description+"</font>  </body></html>",
                                    "text/html", "UTF-8");


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleNewsFeedDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}