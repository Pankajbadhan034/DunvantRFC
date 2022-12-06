package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.FacilityBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentFacilityListingAdapter;
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

public class ParentFacilityListingScreen extends AppCompatActivity implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    TextView title;
    ImageView backButton;
    ListView facilitiesListView;

    private final String GET_PITCHES_LISTING = "GET_PITCHES_LISTING";
    ArrayList<FacilityBean> facilityListing = new ArrayList<>();
    ParentFacilityListingAdapter parentFacilityListingAdapter;
    String typeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_facility_listing_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        typeId = getIntent().getStringExtra("gameID");

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        parentFacilityListingAdapter = new ParentFacilityListingAdapter(ParentFacilityListingScreen.this, facilityListing);

        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);
        facilitiesListView = (ListView) findViewById(R.id.facilitiesListView);
        facilitiesListView.setAdapter(parentFacilityListingAdapter);

        title.setTypeface(linoType);

        facilitiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FacilityBean clickedOnFacility = facilityListing.get(position);

                Intent facilityDetail = new Intent(ParentFacilityListingScreen.this, ParentFacilityDetailScreen.class);
                facilityDetail.putExtra("clickedOnFacility", clickedOnFacility);
                facilityDetail.putExtra("typeId", typeId);
                startActivity(facilityDetail);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getFacilitiesListing();
    }

    private void getFacilitiesListing(){
        if(Utilities.isNetworkAvailable(ParentFacilityListingScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("offset", "0"));

//            String webServiceUrl = Utilities.BASE_URL + "pitch/list";
            String webServiceUrl = Utilities.BASE_URL + Utilities.listAdcService;

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentFacilityListingScreen.this, nameValuePairList, GET_PITCHES_LISTING, ParentFacilityListingScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentFacilityListingScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_PITCHES_LISTING:

                facilityListing.clear();

                if(response == null) {
                    Toast.makeText(ParentFacilityListingScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            FacilityBean facilityBean;
                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject facilityObject = dataArray.getJSONObject(i);
                                facilityBean = new FacilityBean();

                                facilityBean.setFacilityId(facilityObject.getString("id"));
                                facilityBean.setLocationName(facilityObject.getString("location_name"));
                                facilityBean.setLocationDescription(facilityObject.getString("location_desc"));
                                facilityBean.setFileTitle(facilityObject.getString("file_title"));
                                facilityBean.setFilePath(facilityObject.getString("file_path"));
                                facilityBean.setPitchIds(facilityObject.getString("pitch_ids"));

                                facilityListing.add(facilityBean);
                            }

                        } else {
                            Toast.makeText(ParentFacilityListingScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentFacilityListingScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentFacilityListingAdapter.notifyDataSetChanged();

                break;
        }
    }
}
