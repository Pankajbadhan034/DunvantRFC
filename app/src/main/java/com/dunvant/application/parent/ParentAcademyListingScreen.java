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
import com.dunvant.application.beans.CoachingAcademyBean;
import com.dunvant.application.beans.CoachingProgramGalleryBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentAcademyGridAdapter;
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

public class ParentAcademyListingScreen extends AppCompatActivity implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Typeface helvetica;
    Typeface linoType;

    TextView title;
    ImageView backButton;
//    GridView academiesGridView;
    ListView academiesListView;
    private final String GET_ACADEMIES_LISTING = "GET_ACADEMIES_LISTING";

    ArrayList<CoachingAcademyBean> academiesList = new ArrayList<>();

    ParentAcademyGridAdapter parentAcademyGridAdapter;

    public static boolean comingFromBookMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_academy_listing_screen);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        parentAcademyGridAdapter = new ParentAcademyGridAdapter(ParentAcademyListingScreen.this, academiesList);

        title = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.backButton);
        academiesListView = (ListView) findViewById(R.id.academiesListView);
        academiesListView.setAdapter(parentAcademyGridAdapter);

        title.setTypeface(linoType);

        getAcademiesListing();

        academiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CoachingAcademyBean clickedOnAcademy = academiesList.get(position);
                Intent academyDetail = new Intent(ParentAcademyListingScreen.this, ParentAcademyDetailScreen.class);
                academyDetail.putExtra("clickedOnAcademy", clickedOnAcademy);
                startActivity(academyDetail);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getAcademiesListing(){
        if(Utilities.isNetworkAvailable(ParentAcademyListingScreen.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("offset", "0"));

            String webServiceUrl = Utilities.BASE_URL + "coaching_programs/list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentAcademyListingScreen.this, nameValuePairList, GET_ACADEMIES_LISTING, ParentAcademyListingScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentAcademyListingScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_ACADEMIES_LISTING:

                academiesList.clear();


                if(response == null) {
                    Toast.makeText(ParentAcademyListingScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        System.out.println("RespnseHere2::"+response);
                            JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            CoachingAcademyBean academyBean;
                            for (int i=0; i<dataArray.length(); i++) {
                                JSONObject academyObject = dataArray.getJSONObject(i);
                                academyBean = new CoachingAcademyBean();

                                academyBean.setAcademyId(academyObject.getString("id"));
                                academyBean.setCoachingProgramName(academyObject.getString("coaching_program_name"));
                                academyBean.setDescription(academyObject.getString("description"));
                                academyBean.setIsTrial(academyObject.getString("is_trial"));
                                academyBean.setFileTitle(academyObject.getString("file_title"));
                                academyBean.setFilePath(academyObject.getString("file_path"));

                                JSONArray galleryArray = academyObject.getJSONArray("coaching_program_gallery");
                                ArrayList<CoachingProgramGalleryBean> galleryList = new ArrayList<>();
                                CoachingProgramGalleryBean galleryBean;
                                for (int j=0; j<galleryArray.length(); j++) {
                                    JSONObject galleryObject = galleryArray.getJSONObject(j);
                                    galleryBean = new CoachingProgramGalleryBean();

                                    galleryBean.setFileTitle(galleryObject.getString("file_title"));
                                    galleryBean.setFileType(galleryObject.getString("file_type"));
                                    galleryBean.setFilePath(galleryObject.getString("file_path"));

                                    if(!(galleryBean.getFileType().contains("video") || galleryBean.getFileType().contains("youtube"))){
                                        galleryList.add(galleryBean);
                                    }


                                }
                                academyBean.setGalleryList(galleryList);


                                if(comingFromBookMore) {

                                    // Add is_trail = 0
                                    if(academyBean.getIsTrial().equalsIgnoreCase("0")) {
                                        academiesList.add(academyBean);
                                    }

                                } else {
                                    academiesList.add(academyBean);
                                }
                            }

                        } else {
                            Toast.makeText(ParentAcademyListingScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentAcademyListingScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                comingFromBookMore = false;
                parentAcademyGridAdapter.notifyDataSetChanged();

                break;
        }
    }
}
