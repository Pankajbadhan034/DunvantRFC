package com.dunvant.application.parent.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.LeagueBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.ParentLeagueDetailScreen;
import com.dunvant.application.parent.adapters.ParentLeaguesGridAdapter;
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

public class ParentLeaguesListingFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ListView leaguesListView;
    private final String GET_LEAGUES_LISTING = "GET_LEAGUES_LISTING";
    ArrayList<LeagueBean> leaguesListing = new ArrayList<>();

    ParentLeaguesGridAdapter parentLeaguesGridAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        parentLeaguesGridAdapter = new ParentLeaguesGridAdapter(getActivity(), leaguesListing);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_leagues_listing, container, false);

        leaguesListView = (ListView) view.findViewById(R.id.leaguesListView);
        leaguesListView.setAdapter(parentLeaguesGridAdapter);

        getLeaguesListing();

        leaguesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LeagueBean clickedOnLeague = leaguesListing.get(position);

                Intent detailScreen = new Intent(getActivity(), ParentLeagueDetailScreen.class);
                detailScreen.putExtra("clickedOnLeague", clickedOnLeague);
                startActivity(detailScreen);
            }
        });

        return view;
    }

    private void getLeaguesListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("offset", "0"));

            String webServiceUrl = Utilities.BASE_URL + "leagues/list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_LEAGUES_LISTING, ParentLeaguesListingFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_LEAGUES_LISTING:

                leaguesListing.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            LeagueBean leagueBean;
                            for(int i=0; i< dataArray.length(); i++) {
                                JSONObject leagueObject = dataArray.getJSONObject(i);
                                leagueBean = new LeagueBean();

                                leagueBean.setLeagueId(leagueObject.getString("id"));
                                leagueBean.setAcademiesId(leagueObject.getString("academies_id"));
                                leagueBean.setLeagueName(leagueObject.getString("league_name"));
                                leagueBean.setShowFromDate(leagueObject.getString("from_date_formatted"));
                                leagueBean.setShowToDate(leagueObject.getString("to_date_formatted"));
                                leagueBean.setLeagueDescription(leagueObject.getString("league_desc"));
                                leagueBean.setLeagueState(leagueObject.getString("league_state"));
                                leagueBean.setFileTitle(leagueObject.getString("file_title"));
                                leagueBean.setFilePath(leagueObject.getString("file_path"));

                                leaguesListing.add(leagueBean);
                            }


                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentLeaguesGridAdapter.notifyDataSetChanged();

                break;
        }
    }
}