package com.dunvant.application.parent;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.dunvant.application.R;
import com.dunvant.application.beans.ParentMidWeekDetailBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentMidWeekDetailAdapter;
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

public class ParentMidWeekDetailScreen extends AppCompatActivity implements IWebServiceCallback {
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;
    TextView title;
    ImageView backButton;
    ImageView image;
    TextView location;
    TextView description;
    TextView ageGroup;
    TextView timings;
    private final String MIDWEEK_PACKAGE_DETAIL = "MIDWEEK_PACKAGE_DETAIL";
    String package_id;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    ListView listView;
    ArrayList<ParentMidWeekDetailBean>parentMidWeekListingBeanArrayList = new ArrayList<>();
    ParentMidWeekDetailAdapter parentMidWeekDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_mid_week_detail_activity);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        package_id = getIntent().getStringExtra("package_id");

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        imageLoader.init(ImageLoaderConfiguration.createDefault(ParentMidWeekDetailScreen.this));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        image = findViewById(R.id.image);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        ageGroup = findViewById(R.id.ageGroup);
        timings = findViewById(R.id.timings);
        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);
        listView = findViewById(R.id.listView);

        title.setTypeface(linoType);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getMidWeekDetailListing();
    }

    private void getMidWeekDetailListing(){
        if(Utilities.isNetworkAvailable(ParentMidWeekDetailScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("package_id", package_id));

            String webServiceUrl = Utilities.BASE_URL + "midweek_session/midweek_packages_detail";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentMidWeekDetailScreen.this, nameValuePairList, MIDWEEK_PACKAGE_DETAIL, ParentMidWeekDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentMidWeekDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case MIDWEEK_PACKAGE_DETAIL:

                parentMidWeekListingBeanArrayList.clear();

                if(response == null) {
                    Toast.makeText(ParentMidWeekDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONObject dataObj = responseObject.getJSONObject("data");
                            String id = dataObj.getString("id");
                            String title = dataObj.getString("title");
                            String descriptionstr = dataObj.getString("description");
                            String fileUrl = dataObj.getString("file_url");
                            String name = dataObj.getString("name");
                            int from_age = dataObj.getInt("from_age");
                            int to_age = dataObj.getInt("to_age");
                            String group_name = dataObj.getString("group_name");
                            String end_time = dataObj.getString("end_time");
                            String start_time = dataObj.getString("start_time");


                            location.setText("Location - "+name);
                            description.setText(Html.fromHtml(descriptionstr));
                            ageGroup.setText("Age Group - "+from_age+ " to " +to_age);
                            timings.setText("Timings - "+start_time+" - "+end_time);
                            imageLoader.displayImage(fileUrl, image, options);


                            JSONArray dataArray = dataObj.getJSONArray("sessions");
                            ParentMidWeekDetailBean parentMidWeekDetailBean;
                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject midweekListObj = dataArray.getJSONObject(i);
                                parentMidWeekDetailBean = new ParentMidWeekDetailBean();

                                parentMidWeekDetailBean.setId(id);
                                parentMidWeekDetailBean.setTitle(title);
                                parentMidWeekDetailBean.setDescription(descriptionstr);
                                parentMidWeekDetailBean.setFileUrl(fileUrl);
                                parentMidWeekDetailBean.setName(name);
                                parentMidWeekDetailBean.setFromAge(from_age);
                                parentMidWeekDetailBean.setToAge(to_age);
                                parentMidWeekDetailBean.setGroupName(group_name);
                                parentMidWeekDetailBean.setEndTime(end_time);
                                parentMidWeekDetailBean.setStartTime(start_time);

                                parentMidWeekDetailBean.setSessionId(midweekListObj.getString("id"));
                                parentMidWeekDetailBean.setCost(midweekListObj.getString("cost"));
                                parentMidWeekDetailBean.setSessionCount(midweekListObj.getString("session_count"));
                                parentMidWeekDetailBean.setIsLocked(midweekListObj.getString("is_locked"));

                                parentMidWeekListingBeanArrayList.add(parentMidWeekDetailBean);
                            }

                            parentMidWeekDetailAdapter = new ParentMidWeekDetailAdapter(ParentMidWeekDetailScreen.this, parentMidWeekListingBeanArrayList, title, name);
                            listView.setAdapter(parentMidWeekDetailAdapter);
                            Utilities.setListViewHeightBasedOnChildren(listView);


                        } else {
                            Toast.makeText(ParentMidWeekDetailScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentMidWeekDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}