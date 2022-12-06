package com.dunvant.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.beans.UserBean;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewExp extends AppCompatActivity implements IWebServiceCallback {
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    private final String MARK_ATTENDANCE = "MARK_ATTENDANCE";
    private RecyclerView rvSubject;
    private SubjectAdapter subjectAdapter;
    private ArrayList<Subject> subjects;
    String midweek_sessionIntent;
    ImageView backButton;
    TextView title;
    Typeface helvetica;
    Typeface linoType;
    TextView groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_exp);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        midweek_sessionIntent = getIntent().getStringExtra("midweek_session");
        System.out.println("midweek_session:: "+midweek_sessionIntent);

        rvSubject = findViewById(R.id.rvSubject);
        backButton = findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.title);
        groupName = findViewById(R.id.groupName);

        //rvSubject.setNestedScrollingEnabled(false);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getChildrenListingForAttendance();

    }


    private void getChildrenListingForAttendance() {
        if(Utilities.isNetworkAvailable(RecycleViewExp.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("midweek_session", midweek_sessionIntent));

            String webServiceUrl = Utilities.BASE_URL + "coach/coach_midweek_attendance_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            System.out.println("uid::"+loggedInUser.getId()+"token::"+loggedInUser.getToken());

            String fcmToken = sharedPreferences.getString("fcmToken", "");
            System.out.println("TOKEN:: "+fcmToken);

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(RecycleViewExp.this, nameValuePairList, MARK_ATTENDANCE, RecycleViewExp.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(RecycleViewExp.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case MARK_ATTENDANCE:

                if(response == null) {
                    Toast.makeText(RecycleViewExp.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status==true){
                            JSONObject jsonObject = responseObject.getJSONObject("data");
                            int attendance_dates = jsonObject.getInt("attendance_dates");
                            String group_name = jsonObject.getString("group_name");
                            JSONArray childDetails = jsonObject.getJSONArray("child_details");
                            subjects = new ArrayList<>();

                            for(int i=0; i<childDetails.length(); i++){
                                JSONObject jsonObject1 = childDetails.getJSONObject(i);
                                Subject physics = new Subject();
                                physics.id = i;
                                physics.subjectName = jsonObject1.getString("child_name");
                                physics.chapters = new ArrayList<Chapter>();

                                JSONArray attendance = jsonObject1.getJSONArray("attendance");
                                for(int j=0; j<attendance.length(); j++){
                                    JSONObject jsonObject2 = attendance.getJSONObject(j);
                                    Chapter chapter1 = new Chapter();
                                    chapter1.chapterName = jsonObject2.getString("attendance_date");
                                    chapter1.status = jsonObject2.getString("status");
                                    chapter1.id = j;
                                    chapter1.imageUrl = "http://ashishkudale.com/images/maths/circle.png";
                                    physics.chapters.add(chapter1);
                                }

                                if(attendance.length()==attendance_dates){

                                }else{
                                    int loopSize = attendance_dates - attendance.length();
                                    for(int k=0; k<loopSize; k++){
                                        Chapter chapter1 = new Chapter();
                                        chapter1.chapterName = "N/A";
                                        chapter1.status = "N/A";
                                        chapter1.id = k+attendance.length();
                                        chapter1.imageUrl = "http://ashishkudale.com/images/maths/circle.png";
                                        physics.chapters.add(chapter1);
                                    }
                                }

                                subjects.add(physics);

                            }

                            subjectAdapter = new SubjectAdapter(subjects, RecycleViewExp.this);
                            LinearLayoutManager manager = new LinearLayoutManager(RecycleViewExp.this);
                            rvSubject.setLayoutManager(manager);
                            rvSubject.setAdapter(subjectAdapter);

//                            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                                    ViewGroup.LayoutParams.WRAP_CONTENT);
//                            rvSubject.setLayoutParams(params);;
//




                            groupName.setText(group_name);
                        }else{
                            Toast.makeText(RecycleViewExp.this, message, Toast.LENGTH_SHORT).show();
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RecycleViewExp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
    private void changeFonts() {
        title.setTypeface(linoType);
    }
 }