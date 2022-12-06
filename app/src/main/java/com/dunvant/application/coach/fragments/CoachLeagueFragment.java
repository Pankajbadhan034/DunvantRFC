package com.dunvant.application.coach.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachLeagueBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.CoachLeagueDetailOneScreen;
import com.dunvant.application.coach.adapters.CoachLeagueAdapter;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CoachLeagueFragment extends Fragment implements IWebServiceCallback {
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;
    ArrayList<CoachLeagueBean> coachLeagueBeanArrayList;
    ListView listView;

    private final String LEAGUE_MATCHES = "LEAGUE_MATCHES";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LinotypeOrdinarRegular.ttf");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coach_fragment_league, container, false);
        listView = view.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CoachLeagueBean coachLeagueBean = coachLeagueBeanArrayList.get(i);
                Intent intent = new Intent(getActivity(), CoachLeagueDetailOneScreen.class);
                intent.putExtra("academy_id", coachLeagueBean.getAcademy_id());
                intent.putExtra("league_id", coachLeagueBean.getId());
                intent.putExtra("name", coachLeagueBean.getName());
                startActivity(intent);
            }
        });

        getLeaugeNames();
        return view;
    }

    private void getLeaugeNames() {
        if (Utilities.isNetworkAvailable(getActivity())){

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));

            String webServiceUrl = Utilities.BASE_URL + "league_tournament/league_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            System.out.println("uid::" + loggedInUser.getId() + "token::" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, LEAGUE_MATCHES, CoachLeagueFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else{
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }






    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {


            case LEAGUE_MATCHES:

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");
                            coachLeagueBeanArrayList = new ArrayList<>();

                            for(int i=0; i<dataArray.length(); i++){
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                CoachLeagueBean coachLeagueBean = new CoachLeagueBean();
                                coachLeagueBean.setId(jsonObject.getString("id"));
                                coachLeagueBean.setAcademy_id(jsonObject.getString("academy_id"));
                                coachLeagueBean.setName(jsonObject.getString("name"));
                                coachLeagueBean.setDescription(jsonObject.getString("description"));
                                coachLeagueBean.setFileName(jsonObject.getString("file_name"));
                                coachLeagueBean.setSportsId(jsonObject.getString("sports_id"));
                                coachLeagueBean.setSort(jsonObject.getString("sort"));
                                coachLeagueBean.setCreatedAt(jsonObject.getString("created"));
                                coachLeagueBean.setModified(jsonObject.getString("modified"));
                                coachLeagueBean.setState(jsonObject.getString("state"));
                                if(jsonObject.has("image_url")){
                                    coachLeagueBean.setImageUrl(jsonObject.getString("image_url"));
                                }else{
                                    coachLeagueBean.setImageUrl("no image");
                                }


                                coachLeagueBeanArrayList.add(coachLeagueBean);
                            }



                            CoachLeagueAdapter coachMidWeekPackageChildNamesAttendanceAdapter = new CoachLeagueAdapter(getActivity(), coachLeagueBeanArrayList);
                            listView.setAdapter(coachMidWeekPackageChildNamesAttendanceAdapter);

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;


        }
    }


}
