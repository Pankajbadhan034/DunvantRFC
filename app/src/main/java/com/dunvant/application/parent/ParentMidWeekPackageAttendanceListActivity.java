package com.dunvant.application.parent;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.Chapter;
import com.dunvant.application.R;
import com.dunvant.application.beans.RecycleExpBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.CoachMainScreen;
import com.dunvant.application.parent.adapters.ParentMidWeekPackageAttendanceListAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class ParentMidWeekPackageAttendanceListActivity extends AppCompatActivity implements IWebServiceCallback {
    String childIdStrSend;
    String attendanceId;
    String attendance_date;
    String attendance_state;
    String orderId;
    String sessionIdStr;
    String  termIdStr, strChildrenAttendance="";
    Button submit;
    ImageView backButton;
    ListView listView;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    private final String MARK_ATTENDANCE = "MARK_ATTENDANCE";
    // private ArrayList<RecycleExpBean> recycleExpBeanArrayList;
    public static ArrayList<Chapter> chapterArrayList;
    public static ParentMidWeekPackageAttendanceListAdapter parentMidWeekPackageAttendanceListAdapter;
    Typeface helvetica;
    Typeface linoType;
    TextView childName;
    RecycleExpBean recycleExpBean;
    String group_name;
    TextView groupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_mid_week_package_attendance_list_activity);
        listView = findViewById(R.id.listView);
        backButton = findViewById(R.id.backButton);
        childName = findViewById(R.id.childName);
        submit = findViewById(R.id.submit);
        groupName = findViewById(R.id.groupName);

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        Intent intent = getIntent();
        if (intent != null) {
            recycleExpBean = (RecycleExpBean) intent.getSerializableExtra("recycleExpBean");
            group_name = getIntent().getStringExtra("group_name");
            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

            childName.setText(verbiage_singular+" Name : "+recycleExpBean.getChildName());
           // childName.setText("Child Name : "+recycleExpBean.getChildName());
            groupName.setText(group_name);
            chapterArrayList = recycleExpBean.getChapters();
            parentMidWeekPackageAttendanceListAdapter = new ParentMidWeekPackageAttendanceListAdapter(ParentMidWeekPackageAttendanceListActivity.this, chapterArrayList);
            listView.setAdapter(parentMidWeekPackageAttendanceListAdapter);

        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//
//
//                if(chapterArrayList.get(position).getChapterName().equalsIgnoreCase("N/A")){
//                    //    Toast.makeText(ParentMidWeekPackageAttendanceListActivity.this, "N/A", Toast.LENGTH_SHORT).show();
//                }else {
//
//
//                    for (int i = 0; i <= position; i++) {
//
//                        if (chapterArrayList.get(i).getChapterName().equalsIgnoreCase("")) {
//                            if(i==position){
//                                DialogFragment newFragment = new ParentMidWeekPackageAttendanceListActivity.DatePickerFragment(position);
//                                newFragment.show(getSupportFragmentManager(), "datePicker");
//                                break;
//                            }else{
//                                Toast.makeText(ParentMidWeekPackageAttendanceListActivity.this, "Please submit attendance of previous credit first", Toast.LENGTH_SHORT).show();
//                                break;
//                            }
//                        }
//
//                        if(i==position){
//                            DialogFragment newFragment = new ParentMidWeekPackageAttendanceListActivity.DatePickerFragment(position);
//                            newFragment.show(getSupportFragmentManager(), "datePicker");
//                        }
//
//                    }
//                }
//            }
//        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                strChildrenAttendance = "[";

                for(int i=0; i<chapterArrayList.size(); i++){
                    attendance_date = ""+chapterArrayList.get(i).getChapterName();
                    if(attendance_date.equalsIgnoreCase("") || attendance_date.equalsIgnoreCase("N/A")){
                        System.out.println("HERE_N/A");
                    }else{
                        termIdStr = chapterArrayList.get(i).getTermId();
                        childIdStrSend = ""+chapterArrayList.get(i).getChildId();
                        attendanceId = ""+chapterArrayList.get(i).getAttendance_id();
                        attendance_state = ""+chapterArrayList.get(i).getStatus();
                        orderId = chapterArrayList.get(i).getOrderMidWeekSessionId();
                        sessionIdStr = chapterArrayList.get(i).getMidweekSessionDetailsId();

                        if(i==0){
                            strChildrenAttendance += "{\"child_id\":\""+childIdStrSend+"\", \"attendance_id\":\""+attendanceId +"\", \"attendance_date\":\""+attendance_date +"\", \"attendance_state\":\""+attendance_state+"\", \"order_id\":\""+orderId+"\"}";
                        }else{
                            strChildrenAttendance += ",{\"child_id\":\""+childIdStrSend+"\", \"attendance_id\":\""+attendanceId +"\", \"attendance_date\":\""+attendance_date +"\", \"attendance_state\":\""+attendance_state+"\", \"order_id\":\""+orderId+"\"}";
                        }


                    }

                }

                strChildrenAttendance += "]";


                System.out.println("HERE"+strChildrenAttendance);

                midWeekAttendance();

//                if(childrenAttendanceListing.isEmpty()){
//                    Toast.makeText(getActivity(), "No Children available", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//



            }
        });


    }


    private void midWeekAttendance() {
        if(Utilities.isNetworkAvailable(ParentMidWeekPackageAttendanceListActivity.this)) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("term_id", termIdStr));
            nameValuePairList.add(new BasicNameValuePair("session_id", sessionIdStr));
//            nameValuePairList.add(new BasicNameValuePair("term_id", "105"));
//            nameValuePairList.add(new BasicNameValuePair("session_id", "782"));
            nameValuePairList.add(new BasicNameValuePair("children_attendance", strChildrenAttendance));

            String webServiceUrl = Utilities.BASE_URL + "coach/save_children_midweek_attendance";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            System.out.println("uid::"+loggedInUser.getId()+"token::"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentMidWeekPackageAttendanceListActivity.this, nameValuePairList, MARK_ATTENDANCE, ParentMidWeekPackageAttendanceListActivity.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentMidWeekPackageAttendanceListActivity.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case MARK_ATTENDANCE:

                if(response == null) {
                    Toast.makeText(ParentMidWeekPackageAttendanceListActivity.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status==true){
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            Intent mainScreen = new Intent(this, CoachMainScreen.class);
                            mainScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainScreen);
                        }else{
                            Toast.makeText(ParentMidWeekPackageAttendanceListActivity.this, message, Toast.LENGTH_SHORT).show();
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentMidWeekPackageAttendanceListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        int position;

        public DatePickerFragment(int position){
            this.position = position;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month++;
            String strDay = day < 10 ? "0"+day : day+"";
            String strMonth = month < 10 ? "0"+month : month+"";
//            dob.setText(strDay+"-"+strMonth+"-"+year);

            String dateEdit =  year+"-"+strMonth+"-"+day;
            chapterArrayList.get(position).setChapterName(dateEdit);
            chapterArrayList.get(position).setStatus("1");
            parentMidWeekPackageAttendanceListAdapter.notifyDataSetChanged();
        }





    }


}