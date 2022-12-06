package com.dunvant.application.coach;

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
import com.dunvant.application.Chapter;
import com.dunvant.application.R;
import com.dunvant.application.beans.RecycleExpBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachMidWeekPackageChildNamesAttendanceAdapter;
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

public class CoachMidWeekPackageChildNamesAttendanceActivity extends AppCompatActivity implements IWebServiceCallback {
    String group_name;
    ImageView backButton;
    ListView listView;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    private final String MARK_ATTENDANCE = "MARK_ATTENDANCE";
    private ArrayList<RecycleExpBean> recycleExpBeanArrayList;
    String midweek_sessionIntent;
    Typeface helvetica;
    Typeface linoType;
    TextView groupName;
    public static boolean finishCoachMidPackageCHildNamesAtttendanceActivity=false;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_mid_week_package_child_names_attendance_screen);
        listView = findViewById(R.id.listView);
        backButton = findViewById(R.id.backButton);
        groupName = findViewById(R.id.groupName);
        title = findViewById(R.id.title);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

        title.setText(verbiage_singular.toUpperCase()+" NAMES");

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        midweek_sessionIntent = getIntent().getStringExtra("midweek_session");
        System.out.println("midweek_session:: "+midweek_sessionIntent);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecycleExpBean recycleExpBean = recycleExpBeanArrayList.get(i);
                Intent intent = new Intent(CoachMidWeekPackageChildNamesAttendanceActivity.this, CoachMidWeekPackageAttendanceListActivity.class);
                intent.putExtra("recycleExpBean", recycleExpBean);
                intent.putExtra("group_name", group_name);
                startActivity(intent);
//                finish();
            }
        });

        getChildrenListingForAttendance();

    }


    private void getChildrenListingForAttendance() {
        if(Utilities.isNetworkAvailable(CoachMidWeekPackageChildNamesAttendanceActivity.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("midweek_session", midweek_sessionIntent));

            String webServiceUrl = Utilities.BASE_URL + "coach/coach_midweek_attendance_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            System.out.println("uid::"+loggedInUser.getId()+"token::"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(CoachMidWeekPackageChildNamesAttendanceActivity.this, nameValuePairList, MARK_ATTENDANCE, CoachMidWeekPackageChildNamesAttendanceActivity.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(CoachMidWeekPackageChildNamesAttendanceActivity.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case MARK_ATTENDANCE:

                if(response == null) {
                    Toast.makeText(CoachMidWeekPackageChildNamesAttendanceActivity.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status==true){
                            JSONObject jsonObject = responseObject.getJSONObject("data");
                            int attendance_dates = jsonObject.getInt("attendance_dates");
                             group_name = jsonObject.getString("group_name");
                            JSONArray childDetails = jsonObject.getJSONArray("child_details");
                            recycleExpBeanArrayList = new ArrayList<>();

                            for(int i=0; i<childDetails.length(); i++){
                                JSONObject jsonObject1 = childDetails.getJSONObject(i);
//                                Subject physics = new Subject();
//                                physics.id = i;
//                                physics.subjectName = jsonObject1.getString("child_name");
//                                physics.chapters = new ArrayList<Chapter>();

                                RecycleExpBean recycleExpBean = new RecycleExpBean();
                                recycleExpBean.setChildName(jsonObject1.getString("child_name"));
                                if(jsonObject1.has("used_credit")){
                                    recycleExpBean.setUsed_credit(jsonObject1.getString("used_credit"));
                                    recycleExpBean.setTotal_credit(jsonObject1.getString("total_credit"));
                                }else{
                                    recycleExpBean.setUsed_credit("");
                                    recycleExpBean.setTotal_credit("");
                                }
                                recycleExpBean.setId(i);
                               // recycleExpBean.setChapters(new ArrayList<Chapter>());

                                ArrayList<Chapter>chapterArrayList = new ArrayList<>();
                                JSONArray attendance = jsonObject1.getJSONArray("attendance");
                                for(int j=0; j<attendance.length(); j++){
                                    JSONObject jsonObject2 = attendance.getJSONObject(j);
                                    Chapter chapter1 = new Chapter();
//                                    chapter1.chapterName = jsonObject2.getString("attendance_date");
//                                    chapter1.status = jsonObject2.getString("status");
//                                    chapter1.id = j;
//                                    chapter1.imageUrl = "http://ashishkudale.com/images/maths/circle.png";
//                                    physics.chapters.add(chapter1);

                                    chapter1.setChapterName(jsonObject2.getString("attendance_date"));
                                    chapter1.setStatus(jsonObject2.getString("status"));
                                    chapter1.setId(j);
                                    chapter1.setAttendance_id(jsonObject2.getString("attendance_id"));
                                    chapter1.setTermId(jsonObject2.getString("term_id"));
                                    chapter1.setChildId(jsonObject1.getString("users_id"));

                                    chapter1.setMidweekSessionDetailsId(jsonObject2.getString("midweek_session_details_id"));
                                    chapter1.setOrderMidWeekSessionId(jsonObject2.getString("order_midweek_sessions_id"));



                                    chapterArrayList.add(chapter1);

                                    recycleExpBean.setChapters(chapterArrayList);
                                }

                                if(attendance.length()==attendance_dates){

                                }else{
                                    int loopSize = attendance_dates - attendance.length();
                                    for(int k=0; k<loopSize; k++){
                                        Chapter chapter1 = new Chapter();
//                                        chapter1.chapterName = "N/A";
//                                        chapter1.status = "N/A";
//                                        chapter1.id = k+attendance.length();
//                                        chapter1.imageUrl = "http://ashishkudale.com/images/maths/circle.png";
//                                        physics.chapters.add(chapter1);

                                        chapter1.setChapterName("N/A");
                                        chapter1.setStatus("N/A");
                                        chapter1.setId(k+attendance.length());
                                        chapter1.setAttendance_id("N/A");
                                        chapter1.setTermId("N/A");
                                        chapter1.setMidweekSessionDetailsId("N/A");
                                        chapter1.setOrderMidWeekSessionId("N/A");

                                        chapterArrayList.add(chapter1);
                                        recycleExpBean.setChapters(chapterArrayList);
                                    }
                                }

                                recycleExpBeanArrayList.add(recycleExpBean);

                            }

//                            subjectAdapter = new SubjectAdapter(subjects, CoachMidWeekPackageChildNamesAttendanceActivity.this);
//                            LinearLayoutManager manager = new LinearLayoutManager(CoachMidWeekPackageChildNamesAttendanceActivity.this);
//                            rvSubject.setLayoutManager(manager);
//                            rvSubject.setAdapter(subjectAdapter);

                            groupName.setText(group_name);
                            CoachMidWeekPackageChildNamesAttendanceAdapter coachMidWeekPackageChildNamesAttendanceAdapter = new CoachMidWeekPackageChildNamesAttendanceAdapter(CoachMidWeekPackageChildNamesAttendanceActivity.this, recycleExpBeanArrayList);
                            listView.setAdapter(coachMidWeekPackageChildNamesAttendanceAdapter);


                        }else{
                            Toast.makeText(CoachMidWeekPackageChildNamesAttendanceActivity.this, message, Toast.LENGTH_SHORT).show();
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CoachMidWeekPackageChildNamesAttendanceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(finishCoachMidPackageCHildNamesAtttendanceActivity==true){
            finish();
            finishCoachMidPackageCHildNamesAtttendanceActivity = false;
        }

    }
}