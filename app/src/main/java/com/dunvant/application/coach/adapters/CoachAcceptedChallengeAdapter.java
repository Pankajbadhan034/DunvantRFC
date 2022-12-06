package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AcceptedChallengeChildBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.CoachAcceptedMembersScreen;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CoachAcceptedChallengeAdapter extends BaseAdapter implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Context context;
    ArrayList<AcceptedChallengeChildBean> membersList;
    LayoutInflater layoutInflater;

    CoachAcceptedMembersScreen coachAcceptedMembersScreen;

    private final String APPROVE_CHALLENGE = "APPROVE_CHALLENGE";

    public CoachAcceptedChallengeAdapter(Context context, ArrayList<AcceptedChallengeChildBean> membersList, CoachAcceptedMembersScreen coachAcceptedMembersScreen) {
        this.context = context;
        this.membersList = membersList;
        this.layoutInflater = LayoutInflater.from(context);
        this.coachAcceptedMembersScreen = coachAcceptedMembersScreen;

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
    }

    @Override
    public int getCount() {
        return membersList.size();
    }

    @Override
    public Object getItem(int position) {
        return membersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_accepted_challenge_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        ImageView approveChallenge = (ImageView) convertView.findViewById(R.id.approveChallenge);

        final AcceptedChallengeChildBean childBean = membersList.get(position);

        name.setText(childBean.getChildName());
        date.setText(childBean.getShowAcceptedDate());
        time.setText(childBean.getShowAcceptedTime());

        if (childBean.getApprovalStatus() != null && childBean.getApprovalStatus().equalsIgnoreCase("1")) {
            approveChallenge.setVisibility(View.GONE);
        } else {
            if(!childBean.getAchievedScore().equalsIgnoreCase("0.00") || !childBean.getAchievedScore().equalsIgnoreCase("0") || !childBean.getAchievedTime().isEmpty()){
                approveChallenge.setVisibility(View.VISIBLE);
            } else {
                approveChallenge.setVisibility(View.GONE);
            }
        }

        approveChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utilities.isNetworkAvailable(context)) {

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("challengers_id", childBean.getChallengersId()));
                    nameValuePairList.add(new BasicNameValuePair("coach_approved", "true"));

                    String webServiceUrl = Utilities.BASE_URL + "challenges/challenge_approved_by_coach";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, APPROVE_CHALLENGE, CoachAcceptedChallengeAdapter.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }

            }
        });

        return convertView;
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case APPROVE_CHALLENGE:
                if(response == null){
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            coachAcceptedMembersScreen.getMembersList();
                        }

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