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
import com.dunvant.application.ApplicationContext;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachingAcademyBean;
import com.dunvant.application.beans.TermBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentTermsListingAdapter;
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

public class ParentBookAcademyOne extends AppCompatActivity implements IWebServiceCallback{

    ApplicationContext applicationContext;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ImageView backButton;
    TextView title;
    //    TextView continueBelow;
//    TextView four;
    TextView selectTerm;
    ListView termsListView;

    CoachingAcademyBean clickedOnAcademy;
    private final String GET_TERMS_DATA = "GET_TERMS_DATA";

    ArrayList<TermBean> termsListing = new ArrayList<>();
    ParentTermsListingAdapter parentTermsListingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_activity_book_academy_one);

        applicationContext = (ApplicationContext) getApplication();
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
        selectTerm = (TextView) findViewById(R.id.selectTerm);
        termsListView = (ListView) findViewById(R.id.termsListView);

        changeFonts();

        parentTermsListingAdapter = new ParentTermsListingAdapter(ParentBookAcademyOne.this, termsListing);
        termsListView.setAdapter(parentTermsListingAdapter);

        Intent intent = getIntent();
        if(intent != null) {
//            clickedOnAcademy = (CoachingAcademyBean) intent.getSerializableExtra("clickedOnAcademy");
            clickedOnAcademy = applicationContext.getClickedOnAcademy();
            getTermsData();
        }

        termsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TermBean clickedOnTerm = termsListing.get(position);
                Intent bookAcademyTwo = new Intent(ParentBookAcademyOne.this, ParentBookAcademyTwo.class);
                bookAcademyTwo.putExtra("clickedOnAcademy", clickedOnAcademy);
                bookAcademyTwo.putExtra("clickedOnTerm", clickedOnTerm);
                startActivity(bookAcademyTwo);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getTermsData(){
        if(Utilities.isNetworkAvailable(ParentBookAcademyOne.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("coaching_programs_id", clickedOnAcademy.getAcademyId()));

            String webServiceUrl = Utilities.BASE_URL + "sessions/term_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentBookAcademyOne.this, nameValuePairList, GET_TERMS_DATA, ParentBookAcademyOne.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentBookAcademyOne.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_TERMS_DATA:

                termsListing.clear();

                if(response == null) {
                    Toast.makeText(ParentBookAcademyOne.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            TermBean termBean;
                            for (int i=0;i<dataArray.length();i++) {
                                JSONObject termObject = dataArray.getJSONObject(i);
                                termBean = new TermBean();

                                termBean.setTermId(termObject.getString("terms_id"));
                                termBean.setTermName(termObject.getString("term_name"));
                                termBean.setFromDate(termObject.getString("from_date"));
                                termBean.setToDate(termObject.getString("to_date"));
                                termBean.setShowFromDate(termObject.getString("from_date_formatted"));
                                termBean.setShowToDate(termObject.getString("to_date_formatted"));

                                termsListing.add(termBean);
                            }

                        } else {
                            Toast.makeText(ParentBookAcademyOne.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentBookAcademyOne.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentTermsListingAdapter.notifyDataSetChanged();

                if(termsListing != null && termsListing.size() == 1){

                    int mActivePosition = 0;

                    termsListView.performItemClick(
                            termsListView.getAdapter().getView(mActivePosition, null, null),
                            mActivePosition,
                            termsListView.getAdapter().getItemId(mActivePosition));
                }

                break;
        }
    }

    private void changeFonts(){
        title.setTypeface(linoType);
//        continueBelow.setTypeface(helvetica);
//        four.setTypeface(helvetica);
        selectTerm.setTypeface(helvetica);
    }

}