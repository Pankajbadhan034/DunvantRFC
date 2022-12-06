package com.dunvant.application.parent.fragments;

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
import com.dunvant.application.beans.FacilityBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.ParentFacilityDetailScreen;
import com.dunvant.application.parent.adapters.ParentFacilityListingAdapter;
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

public class ParentFacilityListingFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ListView facilitiesListView;

    private final String GET_PITCHES_LISTING = "GET_PITCHES_LISTING";
    ArrayList<FacilityBean> facilityListing = new ArrayList<>();
    ParentFacilityListingAdapter parentFacilityListingAdapter;

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

        parentFacilityListingAdapter = new ParentFacilityListingAdapter(getActivity(), facilityListing);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_facility_listing, container, false);

        facilitiesListView = (ListView) view.findViewById(R.id.facilitiesListView);
        facilitiesListView.setAdapter(parentFacilityListingAdapter);

        facilitiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FacilityBean clickedOnFacility = facilityListing.get(position);

                Intent facilityDetail = new Intent(getActivity(), ParentFacilityDetailScreen.class);
                facilityDetail.putExtra("clickedOnFacility", clickedOnFacility);
                startActivity(facilityDetail);
            }
        });

        getFacilitiesListing();

        return view;
    }

    private void getFacilitiesListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("offset", "0"));

//            String webServiceUrl = Utilities.BASE_URL + "pitch/list";
            String webServiceUrl = Utilities.BASE_URL + Utilities.listAdcService;

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_PITCHES_LISTING, ParentFacilityListingFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_PITCHES_LISTING:

                facilityListing.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            FacilityBean facilityBean;
                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject facilityObject = dataArray.getJSONObject(i);
                                facilityBean = new FacilityBean();

                                facilityBean.setFacilityId(facilityObject.getString("id"));
                                facilityBean.setLocationName(facilityObject.getString("location_name"));
                                facilityBean.setLocationDescription(facilityObject.getString("location_desc"));
                                facilityBean.setFileTitle(facilityObject.getString("file_title"));
                                facilityBean.setFilePath(facilityObject.getString("file_path"));
                                facilityBean.setPitchIds(facilityObject.getString("pitch_ids"));

                                facilityListing.add(facilityBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentFacilityListingAdapter.notifyDataSetChanged();

                break;
        }
    }
}