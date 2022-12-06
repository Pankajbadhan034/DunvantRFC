package com.dunvant.application.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AcademyLocationBean;
import com.dunvant.application.beans.CoachingAcademyBean;
import com.dunvant.application.beans.TermBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentAcademyLocationListingAdapter;
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

public class ParentBookAcademyTwo extends AppCompatActivity implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
//    TextView continueBelow;
//    TextView four;
//    TextView easySteps;
    TextView chooseLocation;
    ListView locationsListView;
    TextView continueButton;

    CoachingAcademyBean clickedOnAcademy;
    TermBean clickedOnTerm;

    private final String GET_LOCATIONS_DATA = "GET_LOCATIONS_DATA";

    ArrayList<AcademyLocationBean> locationsList = new ArrayList<>();
    ParentAcademyLocationListingAdapter parentAcademyLocationListingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_book_academy_two);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        backButton = (ImageView) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
//        continueBelow = (TextView) findViewById(R.id.continueBelow);
//        four = (TextView) findViewById(R.id.four);
//        easySteps = (TextView) findViewById(R.id.easySteps);
        chooseLocation = (TextView) findViewById(R.id.chooseLocation);
        locationsListView = (ListView) findViewById(R.id.locationsListView);
        continueButton = (TextView) findViewById(R.id.continueButton);

        changeFonts();

        parentAcademyLocationListingAdapter = new ParentAcademyLocationListingAdapter(ParentBookAcademyTwo.this, locationsList);
        locationsListView.setAdapter(parentAcademyLocationListingAdapter);

        Intent intent = getIntent();
        if(intent != null) {
            clickedOnAcademy = (CoachingAcademyBean) intent.getSerializableExtra("clickedOnAcademy");
            clickedOnTerm = (TermBean) intent.getSerializableExtra("clickedOnTerm");

            getLocationsData();
        }

        locationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AcademyLocationBean clickedOnLocation = locationsList.get(position);

                Intent bookAcademyThree = new Intent(ParentBookAcademyTwo.this, ParentBookAcademyThree.class);
                bookAcademyThree.putExtra("clickedOnAcademy", clickedOnAcademy);
                bookAcademyThree.putExtra("clickedOnTerm", clickedOnTerm);
                bookAcademyThree.putExtra("clickedOnLocation", clickedOnLocation);
                startActivity(bookAcademyThree);

            }
        });

        /*continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookAcademyThree = new Intent(ParentBookAcademyTwo.this, ParentBookAcademyThree.class);
                startActivity(bookAcademyThree);
            }
        });*/

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getLocationsData(){
        if(Utilities.isNetworkAvailable(ParentBookAcademyTwo.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("coaching_programs_id", clickedOnAcademy.getAcademyId()));
            nameValuePairList.add(new BasicNameValuePair("terms_id", clickedOnTerm.getTermId()));

            String webServiceUrl = Utilities.BASE_URL + "sessions/location_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookAcademyTwo.this, nameValuePairList, GET_LOCATIONS_DATA, ParentBookAcademyTwo.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookAcademyTwo.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_LOCATIONS_DATA:

                locationsList.clear();

                if(response == null) {
                    Toast.makeText(ParentBookAcademyTwo.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            AcademyLocationBean locationBean;
                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject locationObject = dataArray.getJSONObject(i);
                                locationBean = new AcademyLocationBean();

                                locationBean.setLocationId(locationObject.getString("locations_id"));
                                locationBean.setLocationName(locationObject.getString("locations_name"));
                                locationBean.setDescription(locationObject.getString("description"));
                                locationBean.setLatitude(locationObject.getDouble("latitude"));
                                locationBean.setLongitude(locationObject.getDouble("longitude"));
                                locationBean.setCoachingProgramIds(locationObject.getString("coaching_program_ids"));
                                locationBean.setCoachingProgramNames(locationObject.getString("coaching_program_names"));

                                locationsList.add(locationBean);
                            }

                        } else {
                            Toast.makeText(ParentBookAcademyTwo.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookAcademyTwo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentAcademyLocationListingAdapter.notifyDataSetChanged();

                break;
        }
    }

    private void changeFonts(){
        title.setTypeface(linoType);
//        continueBelow.setTypeface(helvetica);
//        four.setTypeface(helvetica);
//        easySteps.setTypeface(helvetica);
        chooseLocation.setTypeface(helvetica);
        continueButton.setTypeface(linoType);
    }

}


