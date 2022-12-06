package com.dunvant.application.parent;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CampBean;
import com.dunvant.application.beans.CampGalleryBean;
import com.dunvant.application.beans.CampLocationBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentCampsListingAdapter;
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

public class ParentCampsListingScreen extends AppCompatActivity implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    TextView title;
    ImageView backButton;
    ListView campsListView;

    private final String GET_CAMPS_LISTING = "GET_CAMPS_LISTING";

    ArrayList<CampBean> campsList = new ArrayList<>();
    ParentCampsListingAdapter parentCampsListingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_camps_listing_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        parentCampsListingAdapter = new ParentCampsListingAdapter(ParentCampsListingScreen.this, campsList);

        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);
        campsListView = (ListView) findViewById(R.id.campsListView);
        campsListView.setAdapter(parentCampsListingAdapter);

        title.setTypeface(linoType);

        getCampsListing();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getCampsListing(){
        if(Utilities.isNetworkAvailable(ParentCampsListingScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("offset", "0"));

            String webServiceUrl = Utilities.BASE_URL + "camps/list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentCampsListingScreen.this, nameValuePairList, GET_CAMPS_LISTING, ParentCampsListingScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentCampsListingScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_CAMPS_LISTING:

                if (response == null) {
                    Toast.makeText(ParentCampsListingScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");

                            CampBean campBean;

                            campsList.clear();
                            for(int i=0; i<dataArray.length(); i++) {

                                JSONObject campObject = dataArray.getJSONObject(i);
                                campBean = new CampBean();

                                campBean.setCampId(campObject.getString("id"));
                                campBean.setCampName(campObject.getString("camp_name"));
                                campBean.setFileTitle(campObject.getString("file_title"));
                                campBean.setFilePath(campObject.getString("file_path"));

                                JSONArray galleryArray = campObject.getJSONArray("camp_gallery");
                                ArrayList<CampGalleryBean> galleryList = new ArrayList<>();
                                CampGalleryBean galleryBean;

                                for (int j=0; j<galleryArray.length(); j++) {
                                    JSONObject galleryObject = galleryArray.getJSONObject(j);
                                    galleryBean = new CampGalleryBean();

                                    galleryBean.setGalleryId(galleryObject.getString("camp_gallery_id"));
                                    galleryBean.setFileTitle(galleryObject.getString("file_title"));
                                    galleryBean.setFilePath(galleryObject.getString("file_path"));

                                    galleryList.add(galleryBean);
                                }
                                campBean.setGalleryList(galleryList);

                                JSONArray locationsArray = campObject.getJSONArray("camp_to_locations");
                                ArrayList<CampLocationBean> locationsList = new ArrayList<>();
                                CampLocationBean locationBean;
                                for(int j=0; j<locationsArray.length(); j++) {
                                    JSONObject locationObject = locationsArray.getJSONObject(j);
                                    locationBean = new CampLocationBean();

                                    locationBean.setLocationId(locationObject.getString("locations_id"));
                                    locationBean.setLocationName(locationObject.getString("l_name"));

                                    locationsList.add(locationBean);
                                }
                                campBean.setLocationList(locationsList);

                                campsList.add(campBean);
                            }

                            parentCampsListingAdapter.notifyDataSetChanged();


                        } else {
                            Toast.makeText(ParentCampsListingScreen.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentCampsListingScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }
}
