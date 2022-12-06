package com.dunvant.application.parent;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.PitchHistoryBean;
import com.dunvant.application.beans.PitchItemBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentPitchHistoryListingAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ParentBookedPitchesScreen extends AppCompatActivity implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    ListView bookedPitchesListView;

    private final String GET_PITCHES_LISTING = "GET_PITCHES_LISTING";

    ArrayList<PitchHistoryBean> pitchHistoryListing = new ArrayList<>();
    ParentPitchHistoryListingAdapter parentPitchHistoryListingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_booked_pitches_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        parentPitchHistoryListingAdapter = new ParentPitchHistoryListingAdapter(ParentBookedPitchesScreen.this, pitchHistoryListing, ParentBookedPitchesScreen.this);

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        bookedPitchesListView = (ListView) findViewById(R.id.bookedPitchesListView);
        bookedPitchesListView.setAdapter(parentPitchHistoryListingAdapter);

        title.setTypeface(linoType);

        getBookedPitchesListing();

        /*bookedPitchesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PitchHistoryBean clickedOnPitch = pitchHistoryListing.get(position);

                Intent pitchDetail = new Intent(getActivity(), ParentPitchHistoryDetailScreen.class);
                pitchDetail.putExtra("clickedOnPitch", clickedOnPitch);
                startActivity(pitchDetail);
            }
        });*/

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getBookedPitchesListing() {
        if (Utilities.isNetworkAvailable(ParentBookedPitchesScreen.this)) {

            String webServiceUrl = Utilities.BASE_URL + "pitch/booked_pitches_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(ParentBookedPitchesScreen.this, GET_PITCHES_LISTING, ParentBookedPitchesScreen.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookedPitchesScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_PITCHES_LISTING:

                pitchHistoryListing.clear();

                if (response == null) {
                    Toast.makeText(ParentBookedPitchesScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");

                            PitchHistoryBean pitchHistoryBean;

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject pitchHistoryObject = dataArray.getJSONObject(i);

                                pitchHistoryBean = new PitchHistoryBean();
                                pitchHistoryBean.setId(pitchHistoryObject.getString("id"));

                                String strPitches = pitchHistoryObject.getString("pitches");
                                ArrayList<PitchItemBean> pitchItemsListing = new ArrayList<>();
                                PitchItemBean pitchItemBean;
                                String pitches[] = strPitches.split(":");

                                for (String pitch : pitches) {
                                    String pitchDetail[] = pitch.split("\\|");

                                    pitchItemBean = new PitchItemBean();
                                    pitchItemBean.setPitchName(pitchDetail[0]);
                                    pitchItemBean.setPitchAddress(pitchDetail[1]);

                                    pitchItemsListing.add(pitchItemBean);
                                }

                                pitchHistoryBean.setPitchesList(pitchItemsListing);

                                pitchHistoryBean.setDisplayTotalCost(pitchHistoryObject.getString("display_total_cost"));
                                pitchHistoryBean.setOrderDate(pitchHistoryObject.getString("order_date"));
                                pitchHistoryBean.setState(pitchHistoryObject.getString("state"));
                                pitchHistoryBean.setRefundAmount(pitchHistoryObject.getString("refund_amount"));
                                pitchHistoryBean.setDisplayRefundAmount(pitchHistoryObject.getString("display_refund_amount"));
                                pitchHistoryBean.setShowCancellation(pitchHistoryObject.getBoolean("show_cancellation"));
                                pitchHistoryBean.setDisplayCustomDiscount(pitchHistoryObject.getString("display_custom_discount"));

                                pitchHistoryListing.add(pitchHistoryBean);
                            }

                        } else {
                            Toast.makeText(ParentBookedPitchesScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookedPitchesScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentPitchHistoryListingAdapter.notifyDataSetChanged();

                break;
        }
    }
}
