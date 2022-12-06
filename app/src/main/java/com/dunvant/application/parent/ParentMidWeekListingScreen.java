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
import com.dunvant.application.beans.ParentMidWeekListingBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentMidWeekListingAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ParentMidWeekListingScreen extends AppCompatActivity implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    TextView title;
    ImageView backButton;
    ListView trainingListView;

    private final String MIDWEEK_PACKAGES = "MIDWEEK_PACKAGES";
    ArrayList<ParentMidWeekListingBean> parentMidWeekListingBeanArrayList = new ArrayList<>();
    ParentMidWeekListingAdapter parentMidWeekListingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_mid_week_listing_activity);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");


        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);
        trainingListView = (ListView) findViewById(R.id.trainingListView);
        title.setTypeface(linoType);

        trainingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParentMidWeekListingBean clickedOnFacility = parentMidWeekListingBeanArrayList.get(position);

                Intent facilityDetail = new Intent(ParentMidWeekListingScreen.this, ParentMidWeekDetailScreen.class);
                facilityDetail.putExtra("package_id", clickedOnFacility.getId());
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
        if(Utilities.isNetworkAvailable(ParentMidWeekListingScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "midweek_session/midweek_packages";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ParentMidWeekListingScreen.this, MIDWEEK_PACKAGES, ParentMidWeekListingScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentMidWeekListingScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case MIDWEEK_PACKAGES:

                parentMidWeekListingBeanArrayList.clear();

                if(response == null) {
                    Toast.makeText(ParentMidWeekListingScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            ParentMidWeekListingBean parentMidWeekListingBean;
                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject midweekListObj = dataArray.getJSONObject(i);
                                parentMidWeekListingBean = new ParentMidWeekListingBean();

                                parentMidWeekListingBean.setId(midweekListObj.getString("id"));
                                parentMidWeekListingBean.setTitle(midweekListObj.getString("title"));
                                parentMidWeekListingBean.setDescription(midweekListObj.getString("description"));
                                parentMidWeekListingBean.setFileName(midweekListObj.getString("file_url"));
                                parentMidWeekListingBean.setName(midweekListObj.getString("name"));

                                parentMidWeekListingBeanArrayList.add(parentMidWeekListingBean);
                            }

                            parentMidWeekListingAdapter = new ParentMidWeekListingAdapter(ParentMidWeekListingScreen.this, parentMidWeekListingBeanArrayList);
                            trainingListView.setAdapter(parentMidWeekListingAdapter);


                        } else {
                            Toast.makeText(ParentMidWeekListingScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentMidWeekListingScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
}
