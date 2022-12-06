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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.ParentAddChildScreen;
import com.dunvant.application.parent.ParentChildDetailScreen;
import com.dunvant.application.parent.adapters.ParentChildrenListingAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ParentManageChildrenFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    ListView childrenListView;
    TextView noChildrenAdded;
    TextView addChild;
    TextView text1;
    TextView text2;
    LinearLayout linear;

    private final String GET_CHILDREN_LISTING = "GET_CHILDREN_LISTING";

    ArrayList<ChildBean> childrenListing = new ArrayList<>();
    ParentChildrenListingAdapter parentChildrenListingAdapter;

    String defaultChecked = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        parentChildrenListingAdapter = new ParentChildrenListingAdapter(getActivity(), childrenListing);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_manage_children, container, false);

        childrenListView = (ListView) view.findViewById(R.id.childrenListView);
        noChildrenAdded = (TextView) view.findViewById(R.id.noChildrenAdded);
        addChild = (TextView) view.findViewById(R.id.addChild);
        linear = (LinearLayout) view.findViewById(R.id.linear);
        text1 = view.findViewById(R.id.text1);
        text2 = view.findViewById(R.id.text2);

        changeFonts();

        String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
        String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

        noChildrenAdded.setText("No "+verbiage_singular+" Added");
        addChild.setText("ADD "+verbiage_singular.toUpperCase());
//        text1.setText("This icon will show you all of your "+verbiage_plural.toLowerCase()+" history with Community, their development, progress, performance, challenges and achievements.");
//        text2.setText("This icon will show you all your "+verbiage_plural.toLowerCase()+" activity on their social media feed, including posts, friends and picture and videos.");

        //        text1.setText("This icon will show you all of your "+verbiage_plural.toLowerCase()+" history with Ospreys, their development, progress, performance, challenges and achievements.");
        text2.setText("This icon will show you all your "+verbiage_plural.toLowerCase()+" activity on their social media feed, including posts, friends and picture and videos.");

        text1.setText("This icon will show your "+verbiage_plural.toLowerCase()+" history, their development, progress, performance, challenges and achievements.");
        //text2.setText("This icon will show your "+verbiage_plural.toLowerCase()+" activity on their social media feed, including posts, friends and picture and videos.");



        childrenListView.setAdapter(parentChildrenListingAdapter);

        childrenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ChildBean clickedOnChild = childrenListing.get(position);

                Intent childDetailScreen = new Intent(getActivity(), ParentChildDetailScreen.class);
                childDetailScreen.putExtra("currentChild", clickedOnChild);
                startActivity(childDetailScreen);

            }
        });

        addChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addChild = new Intent(getActivity(), ParentAddChildScreen.class);
                addChild.putExtra("isEditMode", false);
                addChild.putExtra("defaultChecked", defaultChecked);
                startActivity(addChild);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getChildrenListing();
    }

    private void getChildrenListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "children/children_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_CHILDREN_LISTING, ParentManageChildrenFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_CHILDREN_LISTING:

                childrenListing.clear();

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray dataArray = responseObject.getJSONArray("data");
                            defaultChecked = responseObject.getString("default_checked");

                            ChildBean childBean;
                            for (int i=0; i<dataArray.length(); i++) {
                                JSONObject childObject = dataArray.getJSONObject(i);
                                childBean = new ChildBean();

                                childBean.setId(childObject.getString("id"));
                                childBean.setAcademiesId(childObject.getString("academies_id"));
                                childBean.setUsername(childObject.getString("username"));
                                childBean.setEmail(childObject.getString("email"));
                                childBean.setGender(childObject.getString("gender"));
                                childBean.setCreatedAt(childObject.getString("created_at"));
                                childBean.setState(childObject.getString("state"));
                                childBean.setFirstName(childObject.getString("first_name"));
                                childBean.setLastName(childObject.getString("last_name"));
                                childBean.setFullName(childObject.getString("full_name"));
                                childBean.setAge(childObject.getString("age"));
                                childBean.setDateOfBirth(childObject.getString("dob"));
                                childBean.setMedicalCondition(childObject.getString("medical_conditions"));
                                childBean.setIsPrivate(childObject.getString("is_private"));
                                childBean.setSchool(childObject.getString("school"));
                                childBean.setFavPlayer(childObject.getString("favourite_player"));
                                childBean.setFavTeam(childObject.getString("favourite_team"));
                                childBean.setFavPosition(childObject.getString("favourite_position"));
                                childBean.setFavFootballBoot(childObject.getString("favourite_football_boot"));
                                childBean.setFavFood(childObject.getString("favourite_food"));
                                childBean.setNationality(childObject.getString("nationality"));
                                childBean.setHeight(childObject.getString("height"));
                                childBean.setWeight(childObject.getString("weight"));
                                childBean.setBadgeCount(childObject.getString("badge_count"));

                                childrenListing.add(childBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                updateListing();

                break;
        }
    }

    private void updateListing() {
        if (childrenListing == null || childrenListing.isEmpty()) {
            childrenListView.setVisibility(View.GONE);
            noChildrenAdded.setVisibility(View.VISIBLE);
//            linear.setVisibility(View.GONE);
        } else {
            noChildrenAdded.setVisibility(View.GONE);
            childrenListView.setVisibility(View.VISIBLE);
            linear.setVisibility(View.VISIBLE);
            parentChildrenListingAdapter.notifyDataSetChanged();
        }
    }

    private void changeFonts() {
        noChildrenAdded.setTypeface(helvetica);
        addChild.setTypeface(linoType);
    }
}