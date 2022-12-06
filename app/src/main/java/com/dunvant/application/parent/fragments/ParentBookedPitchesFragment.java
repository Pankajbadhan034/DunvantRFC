package com.dunvant.application.parent.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.PitchHistoryBean;
import com.dunvant.application.beans.PitchItemBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParentBookedPitchesFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Typeface helvetica;
    Typeface linoType;

    ListView bookedPitchesListView;

    private final String GET_PITCHES_LISTING = "GET_PITCHES_LISTING";

    ArrayList<PitchHistoryBean> pitchHistoryListing = new ArrayList<>();
//    ParentPitchHistoryListingAdapter parentPitchHistoryListingAdapter;

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

//        parentPitchHistoryListingAdapter = new ParentPitchHistoryListingAdapter(getActivity(), pitchHistoryListing, ParentBookedPitchesFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_booked_pitches, container, false);

        bookedPitchesListView = (ListView) view.findViewById(R.id.bookedPitchesListView);
//        bookedPitchesListView.setAdapter(parentPitchHistoryListingAdapter);

        getBookedPitchesListing();

        /*bookedPitchesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PitchHistoryBean clickedOnPitch = pitchHistoryListing.get(position);

                Intent pitchDetail = new Intent(getActivity(), ParentPitchHistoryDetailScreen.class);
                pitchDetail.putExtra("clickedOnPitch", clickedOnPitch);
                startActivity(pitchDetail);
            }
        });*/

        return view;
    }

    public void getBookedPitchesListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "pitch/booked_pitches_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_PITCHES_LISTING, ParentBookedPitchesFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_PITCHES_LISTING:

                pitchHistoryListing.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");

                            PitchHistoryBean pitchHistoryBean;

                            for(int i=0;i<dataArray.length();i++) {
                                JSONObject pitchHistoryObject = dataArray.getJSONObject(i);

                                pitchHistoryBean = new PitchHistoryBean();
                                pitchHistoryBean.setId(pitchHistoryObject.getString("id"));

                                String strPitches = pitchHistoryObject.getString("pitches");
                                ArrayList<PitchItemBean> pitchItemsListing = new ArrayList<>();
                                PitchItemBean pitchItemBean;
                                String pitches[] = strPitches.split(":");

                                for(String pitch : pitches) {
                                    String pitchDetail[] = pitch.split("\\|");

                                    pitchItemBean = new PitchItemBean();
                                    pitchItemBean.setPitchName(pitchDetail[0]);
                                    pitchItemBean.setPitchAddress(pitchDetail[1]);

                                    pitchItemsListing.add(pitchItemBean);
                                }

                                pitchHistoryBean.setPitchesList(pitchItemsListing);

                                pitchHistoryBean.setDisplayTotalCost(pitchHistoryObject.getString("display_total_cost"));
                                pitchHistoryBean.setOrderDate(pitchHistoryObject.getString("order_date"));
                                pitchHistoryBean.setState(pitchHistoryObject.getString("state"));
                                pitchHistoryBean.setShowCancellation(pitchHistoryObject.getBoolean("show_cancellation"));

                                pitchHistoryListing.add(pitchHistoryBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

//                parentPitchHistoryListingAdapter.notifyDataSetChanged();

                break;
        }
    }
}