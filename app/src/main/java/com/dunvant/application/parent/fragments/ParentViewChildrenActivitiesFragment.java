package com.dunvant.application.parent.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildActivityBean;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentChildrenListingForActivitiesAdapter;
import com.dunvant.application.parent.adapters.ParentViewActivitiesAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class ParentViewChildrenActivitiesFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    LinearLayout searchLinearLayout;
    TextInputLayout keywordTIL;
    EditText keyword;
    Spinner childrenSpinner;
    static TextView fromDate;
    static TextView toDate;
    TextView go;
    ListView activitiesListView;

    private final String GET_ACTIVITIES = "GET_ACTIVITIES";
    private final String GET_CHILDREN_LISTING = "GET_CHILDREN_LISTING";

    ArrayList<ChildActivityBean> activitiesList = new ArrayList<>();
    ParentViewActivitiesAdapter parentViewActivitiesAdapter;

    ArrayList<ChildBean> childrenListing = new ArrayList<>();
    ParentChildrenListingForActivitiesAdapter parentChildrenListingForActivitiesAdapter;

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

        parentViewActivitiesAdapter = new ParentViewActivitiesAdapter(getActivity(), activitiesList);
        parentChildrenListingForActivitiesAdapter = new ParentChildrenListingForActivitiesAdapter(getActivity(), childrenListing);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_view_children_activities, container, false);

        searchLinearLayout = (LinearLayout) view.findViewById(R.id.searchLinearLayout);
        keywordTIL = (TextInputLayout) view.findViewById(R.id.keywordTIL);
        keyword = (EditText) view.findViewById(R.id.keyword);
        childrenSpinner = (Spinner) view.findViewById(R.id.childrenSpinner);
        fromDate = (TextView) view.findViewById(R.id.fromDate);
        toDate = (TextView) view.findViewById(R.id.toDate);
        go = (TextView) view.findViewById(R.id.go);
        activitiesListView = (ListView) view.findViewById(R.id.activitiesListView);

        changeFonts();

        activitiesListView.setAdapter(parentViewActivitiesAdapter);
        childrenSpinner.setAdapter(parentChildrenListingForActivitiesAdapter);

        getChildrenListing();

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(childrenSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please select Child", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                if(!Utilities.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    return;
                }

                String strChildId;
                if(childrenSpinner.getSelectedItemPosition() == 0) {
                    strChildId = "";
                } else {
                    strChildId = childrenListing.get(childrenSpinner.getSelectedItemPosition()).getId();
                }

                String strKeyword = keyword.getText().toString().trim();
                String strFromDate = fromDate.getText().toString().trim();
                String strToDate = toDate.getText().toString().trim();

//                if(strKeyword == null || strKeyword.isEmpty()) {
//                    Toast.makeText(getActivity(), "Please enter Keyword", Toast.LENGTH_SHORT).show();
//                } else if (strFromDate == null || strFromDate.isEmpty()) {
//                    Toast.makeText(getActivity(), "Please enter From Date", Toast.LENGTH_SHORT).show();
//                } else if (strToDate == null || strToDate.isEmpty()) {
//                    Toast.makeText(getActivity(), "Please enter To Date", Toast.LENGTH_SHORT).show();
//                } else {
                    if(Utilities.isNetworkAvailable(getActivity())) {

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("child_id", strChildId));
                        nameValuePairList.add(new BasicNameValuePair("search_keyword", strKeyword));
                        nameValuePairList.add(new BasicNameValuePair("from_date", strFromDate));
                        nameValuePairList.add(new BasicNameValuePair("to_date", strToDate));

                        String webServiceUrl = Utilities.BASE_URL + "children/activities_list";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:"+loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_ACTIVITIES, ParentViewChildrenActivitiesFragment.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
//                }
            }
        });

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment2();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        return view;
    }

    private void getChildrenListing(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "children/children_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_CHILDREN_LISTING, ParentViewChildrenActivitiesFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_ACTIVITIES:

                activitiesList.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {

                    searchLinearLayout.setVisibility(View.GONE);

                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            JSONArray dataArray = responseObject.getJSONArray("data");
                            ChildActivityBean activityBean;
                            for (int i=0; i<dataArray.length(); i++) {
                                JSONObject activityObject = dataArray.getJSONObject(i);
                                activityBean = new ChildActivityBean();

                                activityBean.setChildId(activityObject.getString("child_id"));
                                activityBean.setChildName(activityObject.getString("child_name"));
                                activityBean.setActivity(activityObject.getString("activity"));
                                activityBean.setAddiontalData(activityObject.getString("additional_data"));
                                activityBean.setCreatedAt(activityObject.getString("created_at"));
                                activityBean.setShowCreatedAt(activityObject.getString("created_at_formatted"));

                                activitiesList.add(activityBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentViewActivitiesAdapter.notifyDataSetChanged();

                break;

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

                            ChildBean childBean = new ChildBean();
                            childBean.setId("-1");
                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                            childBean.setFirstName("Please select "+verbiage_singular);
                            childBean.setFullName("Please select "+verbiage_singular);

                            childrenListing.add(childBean);


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
                                childBean.setAgeValue(childObject.getInt("age_value"));
                                childBean.setDateOfBirth(childObject.getString("dob"));
                                childBean.setMedicalCondition(childObject.getString("medical_conditions"));

                                childrenListing.add(childBean);
                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            ChildBean childBean = new ChildBean();
                            childBean.setId("-1");
                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                            childBean.setFirstName("Please select "+verbiage_singular);
                            childBean.setFullName("Please select "+verbiage_singular);



                            childrenListing.add(childBean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                parentChildrenListingForActivitiesAdapter.notifyDataSetChanged();

                break;
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            fromDate.setText(day+"-"+(month+1)+"-"+year);
        }
    }

    public static class DatePickerFragment2 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            toDate.setText(day+"-"+(month+1)+"-"+year);
        }
    }

    private void changeFonts() {
        keywordTIL.setTypeface(helvetica);
        keyword.setTypeface(helvetica);
        fromDate.setTypeface(helvetica);
        toDate.setTypeface(helvetica);
        go.setTypeface(linoType);
    }

    public void showSearchLinearLayout(){
        searchLinearLayout.setVisibility(View.VISIBLE);
    }
}