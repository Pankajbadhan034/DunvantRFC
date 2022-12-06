package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupAttendanceBean;
import com.dunvant.application.beans.ScoresDataBean;
import com.dunvant.application.beans.SessionDetailsBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.CoachChildScoreDetailScreen;
import com.dunvant.application.parent.ParentChildActivityStatsScreen;
import com.dunvant.application.parent.ParentChildReportScreen;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CoachScoresListingAdapter extends BaseAdapter implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Context context;
    SessionDetailsBean sessionDetailsBean;
    ArrayList<ScoresDataBean> scoresDataListing;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    private final String SHARE_SCORE = "SHARE_SCORE";
    private final String SHARE_ACTIVITY = "SHARE_ACTIVITY";

    public CoachScoresListingAdapter(Context context, SessionDetailsBean sessionDetailsBean, ArrayList<ScoresDataBean> scoresDataListing) {

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        this.context = context;
        this.sessionDetailsBean = sessionDetailsBean;
        this.scoresDataListing = scoresDataListing;
        this.layoutInflater = LayoutInflater.from(context);
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return scoresDataListing.size();
    }

    @Override
    public Object getItem(int position) {
        return scoresDataListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_scores_data_item, null);

        TextView lblChildName = (TextView) convertView.findViewById(R.id.lblChildName);
        TextView childName = (TextView) convertView.findViewById(R.id.childName);
        TextView lblLocationName = (TextView) convertView.findViewById(R.id.lblLocationName);
        TextView locationName = (TextView) convertView.findViewById(R.id.locationName);
        TextView lblTermName = (TextView) convertView.findViewById(R.id.lblTermName);
        TextView termName = (TextView) convertView.findViewById(R.id.termName);
        TextView lblSessionName = (TextView) convertView.findViewById(R.id.lblSessionName);
        final TextView sessionName = (TextView) convertView.findViewById(R.id.sessionName);
        TextView lblEnrollmentDate = (TextView) convertView.findViewById(R.id.lblEnrollmentDate);
        TextView enrollmentDate = (TextView) convertView.findViewById(R.id.enrollmentDate);
        ListView scoresListView = (ListView) convertView.findViewById(R.id.scoresListView);
        Button viewReport = (Button) convertView.findViewById(R.id.viewReport);
        Button viewActivityStats = (Button) convertView.findViewById(R.id.viewActivityStats);
        Button shareScore = (Button) convertView.findViewById(R.id.shareScore);
        Button shareActivity = (Button) convertView.findViewById(R.id.shareActivity);

        final ScoresDataBean scoresDataBean = scoresDataListing.get(position);

        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
        lblChildName.setText(verbiage_singular.toUpperCase()+" NAME");

        /*childName.setText(scoresDataBean.getChildName());
        locationName.setText(scoresDataBean.getLocationName());
        termName.setText(scoresDataBean.getTermName());
        sessionName.setText(scoresDataBean.getDayLabel());
        enrollmentDate.setText(scoresDataBean.getShowEnrollmentDate());*/

        childName.setText(scoresDataBean.getChildName());
        locationName.setText(sessionDetailsBean.getLocationName());
        termName.setText(sessionDetailsBean.getTermsName());
        sessionName.setText(sessionDetailsBean.getDayLabel());
        enrollmentDate.setText(scoresDataBean.getShowEnrollmentDate());

        scoresListView.setAdapter(new CoachChildScoreTextAdapter(context, scoresDataBean.getChildrenScoresListing()));
        Utilities.setListViewHeightBasedOnChildren(scoresListView);

        scoresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*boolean scoreLocked = false;
                String coachId = "";
                for(ChildScoreBean childScoreBean : scoresDataBean.getChildrenScoresListing()) {
                    if(childScoreBean.isScoreLocked()) {
                        scoreLocked = true;
                        coachId = childScoreBean.getCoachId();
                    }
                }*/

//                if(!scoreLocked) {
                if(!scoresDataBean.getChildrenScoresListing().get(position).isScoreLocked()) {
                    if(sessionDetailsBean.isSubmitEnable() && scoresDataBean.isAttendanceEnable()){
                        Intent intent = new Intent(context, CoachChildScoreDetailScreen.class);
                        intent.putExtra("scoreDataBean", scoresDataBean);
                        intent.putExtra("clickedOnPosition", position);
                        intent.putExtra("sessionDetailsBean", sessionDetailsBean);
//                        intent.putExtra("reportDate", sessionDetailsBean.getReportDate());
                        context.startActivity(intent);
                    } else {
//                        Toast.makeText(context, "Not allowed", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(context, "Please check attendance of this player or if other coach has already submitted score.", Toast.LENGTH_LONG).show();
                        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                        Toast.makeText(context, "Please check attendance of this "+verbiage_singular.toLowerCase()+" or if other coach has already submitted score.", Toast.LENGTH_LONG).show();
                    }
                } else {
//                    if(coachId.equalsIgnoreCase(loggedInUser.getId())) {
                    if(scoresDataBean.getChildrenScoresListing().get(position).getCoachId().equalsIgnoreCase(loggedInUser.getId())) {
                        if(sessionDetailsBean.isSubmitEnable() && scoresDataBean.isAttendanceEnable()){
                            Intent intent = new Intent(context, CoachChildScoreDetailScreen.class);
                            intent.putExtra("scoreDataBean", scoresDataBean);
                            intent.putExtra("clickedOnPosition", position);
                            intent.putExtra("sessionDetailsBean", sessionDetailsBean);
//                            intent.putExtra("reportDate", sessionDetailsBean.getReportDate());
                            context.startActivity(intent);
                        } else {
//                            Toast.makeText(context, "Not allowed", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(context, "Please check attendance of this player or if other coach has already submitted score.", Toast.LENGTH_LONG).show();
                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                            Toast.makeText(context, "Please check attendance of this "+verbiage_singular.toLowerCase()+" or if other coach has already submitted score.", Toast.LENGTH_LONG).show();
                        }
                    } else {
//                        Toast.makeText(context, "You cannot update the score", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(context, "Please check attendance of this player or if other coach has already submitted score.", Toast.LENGTH_LONG).show();
                        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                        Toast.makeText(context, "Please check attendance of this "+verbiage_singular.toLowerCase()+" or if other coach has already submitted score.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        viewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scoresDataBean.getTotalScore() == null || scoresDataBean.getTotalScore().isEmpty() || scoresDataBean.getTotalScore().equalsIgnoreCase("null")) {
                    Toast.makeText(context, "Report not uploaded yet", Toast.LENGTH_SHORT).show();
                } else {
                    AgeGroupAttendanceBean ageGroupAttendanceBean = new AgeGroupAttendanceBean();
                    ageGroupAttendanceBean.setUsersId(scoresDataBean.getChildId());
//                    ageGroupAttendanceBean.setSessionsId(scoresDataBean.getSessionsId());
                    ageGroupAttendanceBean.setSessionsId(sessionDetailsBean.getSessionId());
                    ageGroupAttendanceBean.setBookingDate(sessionDetailsBean.getReportDate());

                    Intent childReport = new Intent(context, ParentChildReportScreen.class);
//                    Intent childReport = new Intent(context, CoachScoreDatesScreen.class);
                    childReport.putExtra("clickedOnAgeGroup", ageGroupAttendanceBean);
                    context.startActivity(childReport);
                }
            }
        });

        viewActivityStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgeGroupAttendanceBean ageGroupAttendanceBean = new AgeGroupAttendanceBean();
                ageGroupAttendanceBean.setUsersId(scoresDataBean.getChildId());
//                ageGroupAttendanceBean.setSessionsId(scoresDataBean.getSessionsId());
                ageGroupAttendanceBean.setSessionsId(sessionDetailsBean.getSessionId());

                Intent childReport = new Intent(context, ParentChildActivityStatsScreen.class);
                childReport.putExtra("clickedOnAgeGroup", ageGroupAttendanceBean);
                context.startActivity(childReport);
            }
        });

        shareScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utilities.isNetworkAvailable(context)) {

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
//                    nameValuePairList.add(new BasicNameValuePair("sessions_id", scoresDataBean.getSessionsId()));
                    nameValuePairList.add(new BasicNameValuePair("sessions_id", sessionDetailsBean.getSessionId()));
                    nameValuePairList.add(new BasicNameValuePair("child_id", scoresDataBean.getChildId()));
                    nameValuePairList.add(new BasicNameValuePair("report_date", sessionDetailsBean.getReportDate()));

                    String webServiceUrl = Utilities.BASE_URL + "coach/share_score";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, SHARE_SCORE, CoachScoresListingAdapter.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

        shareActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(scoresDataBean.getLatestScoreDate() == null || scoresDataBean.getLatestScoreDate().isEmpty() || scoresDataBean.getLatestScoreDate().equalsIgnoreCase("null")){
                if(scoresDataBean.getTotalScore() == null || scoresDataBean.getTotalScore().isEmpty() || scoresDataBean.getTotalScore().equalsIgnoreCase("null")){
                    Toast.makeText(context, "Report not uploaded yet", Toast.LENGTH_SHORT).show();
                } else {
                    if(Utilities.isNetworkAvailable(context)) {

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
//                        nameValuePairList.add(new BasicNameValuePair("sessions_id", scoresDataBean.getSessionsId()));
                        nameValuePairList.add(new BasicNameValuePair("sessions_id", sessionDetailsBean.getSessionId()));
                        nameValuePairList.add(new BasicNameValuePair("child_id", scoresDataBean.getChildId()));
//                        nameValuePairList.add(new BasicNameValuePair("report_date", scoresDataBean.getLatestScoreDate()));
                        nameValuePairList.add(new BasicNameValuePair("report_date", sessionDetailsBean.getReportDate()));

                        String webServiceUrl = Utilities.BASE_URL + "coach/share_score";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:"+loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, SHARE_SCORE, CoachScoresListingAdapter.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }

                /*if(Utilities.isNetworkAvailable(context)) {

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("sessions_id", scoresDataBean.getSessionsId()));
                    nameValuePairList.add(new BasicNameValuePair("child_id", scoresDataBean.getChildId()));

                    String webServiceUrl = Utilities.BASE_URL + "coach/share_activity";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, SHARE_ACTIVITY, CoachScoresListingAdapter.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        lblChildName.setTypeface(linoType);
        childName.setTypeface(helvetica);
        lblLocationName.setTypeface(linoType);
        locationName.setTypeface(helvetica);
        lblTermName.setTypeface(linoType);
        termName.setTypeface(helvetica);
        lblSessionName.setTypeface(linoType);
        sessionName.setTypeface(helvetica);
        lblEnrollmentDate.setTypeface(linoType);
        enrollmentDate.setTypeface(helvetica);
        viewReport.setTypeface(linoType);

        return convertView;
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case SHARE_SCORE:
                if(response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case SHARE_ACTIVITY:
                if(response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}