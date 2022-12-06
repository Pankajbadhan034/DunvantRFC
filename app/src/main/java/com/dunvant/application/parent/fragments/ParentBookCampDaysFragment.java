package com.dunvant.application.parent.fragments;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CampBean;
import com.dunvant.application.beans.CampDateBean;
import com.dunvant.application.beans.CampSelectedChildBean;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.ParentBookCampSummaryScreen;
import com.dunvant.application.parent.ParentPaymentGatewayWebViewScreen;
import com.dunvant.application.parent.adapters.ParentCampChildrenNamesGridAdapter;
import com.dunvant.application.parent.adapters.ParentCampChooseDatesListingAdapter;
import com.dunvant.application.parent.adapters.ParentChildrenSpinnerAdapter;
import com.dunvant.application.parent.paymentGatewayUtilities.AvenuesParams;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentBookCampDaysFragment extends Fragment implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;

    Spinner childrenSpinner;
    GridView selectedChildrenGridView;
    ListView chooseDaysListView;
    EditText comments;
    TextView lblTotalAmount;
    TextView totalAmount;
    Button makePayment;

    CampBean clickedOnCamp;
    int locationPosition;
    int sessionPosition;

    private final String GET_CHILDREN_LISTING = "GET_CHILDREN_LISTING";
    private final String GET_ALL_AVAILABLE_DATES = "GET_ALL_AVAILABLE_DATES";
    private final String BOOK_CAMP = "BOOK_CAMP";
    private ArrayList<String> runs_on_days = new ArrayList<>();

    ArrayList<ChildBean> childrenListing = new ArrayList<>();
    ArrayList<CampDateBean> availableDatesListing = new ArrayList<>();
    ArrayList<CampSelectedChildBean> selectedChildrenListing = new ArrayList<>();


    ParentChildrenSpinnerAdapter childrenSpinnerAdapter;
    ParentCampChildrenNamesGridAdapter parentCampChildrenNamesGridAdapter;
    ParentCampChooseDatesListingAdapter parentCampChooseDatesListingAdapter;

    float totalAmountValue = 0/*, discountAmountValue = 0, netAmountValue = 0*/;

    public ParentBookCampDaysFragment() {

    }

    public ParentBookCampDaysFragment(CampBean clickedOnCamp, int locationPosition, int sessionPosition) {
        this.clickedOnCamp = clickedOnCamp;
        this.locationPosition = locationPosition;
        this.sessionPosition = sessionPosition;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_book_camp_days, container, false);

        childrenSpinner = (Spinner) view.findViewById(R.id.childrenSpinner);
        selectedChildrenGridView = (GridView) view.findViewById(R.id.selectedChildrenGridView);
        chooseDaysListView = (ListView) view.findViewById(R.id.chooseDaysListView);
        comments = (EditText) view.findViewById(R.id.comments);
        lblTotalAmount = (TextView) view.findViewById(R.id.lblTotalAmount);
        totalAmount = (TextView) view.findViewById(R.id.totalAmount);
        String academy_currency = sharedPreferences.getString("academy_currency", null);
        totalAmount.setText("0.00 " + academy_currency);

//        discountAmount = (TextView) view.findViewById(R.id.discountAmount);
//        netAmount = (TextView) view.findViewById(R.id.netAmount);
        makePayment = (Button) view.findViewById(R.id.makePayment);

        comments.setTypeface(helvetica);
        lblTotalAmount.setTypeface(helvetica);
        totalAmount.setTypeface(helvetica);
        makePayment.setTypeface(linoType);

        childrenSpinnerAdapter = new ParentChildrenSpinnerAdapter(getActivity(), childrenListing);
        childrenSpinner.setAdapter(childrenSpinnerAdapter);

        parentCampChildrenNamesGridAdapter = new ParentCampChildrenNamesGridAdapter(getActivity(), selectedChildrenListing);
        selectedChildrenGridView.setAdapter(parentCampChildrenNamesGridAdapter);

        parentCampChooseDatesListingAdapter = new ParentCampChooseDatesListingAdapter(getActivity(), selectedChildrenListing, availableDatesListing, chooseDaysListView, ParentBookCampDaysFragment.this);
        chooseDaysListView.setAdapter(parentCampChooseDatesListingAdapter);

        getChildrenListing();

        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalAmountValue == 0) {
                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                    Toast.makeText(getActivity(), "Please choose at least one date for one "+verbiage_singular.toLowerCase(), Toast.LENGTH_SHORT).show();

                    return;
                }
                String strComments = comments.getText().toString().trim();
                /*if(strComments == null || strComments.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter Comment", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                String strBookingType = "1";

                JSONArray selectedChildrenArray = new JSONArray();

                for (CampSelectedChildBean childBean : selectedChildrenListing) {

                    for (CampDateBean campDateBean : childBean.getSelectedDatesList()) {
                        JSONObject childObject = new JSONObject();
                        try {
                            childObject.put("child_id", childBean.getChildBean().getId());
                            childObject.put("bdate", campDateBean.getDate());

                            selectedChildrenArray.put(childObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    /*strChildIds += childBean.getChildBean().getId()+",";

                    String strParamName = "camp_dates_"+childBean.getChildBean().getId();
                    String strDates = "";
                    for(CampDateBean campDateBean : childBean.getSelectedDatesList()){
                        strDates += campDateBean.getDate()+",";
                    }
                    if (strDates != null && strDates.length() > 0 && strDates.charAt(strDates.length() - 1) == ',') {
                        strDates = strDates.substring(0, strDates.length() - 1);
                    }

                    if(!childBean.getSelectedDatesList().isEmpty()){
                        paramNames.add(strParamName);
                        dateCSV.add(strDates);

                    }

                    //System.out.println(childBean.getChildBean().getId()+" and "+strDates);*/
                }

                Intent bookCampSummary = new Intent(getActivity(), ParentBookCampSummaryScreen.class);
                bookCampSummary.putExtra("clickedOnCamp", clickedOnCamp);
                bookCampSummary.putExtra("sessionPosition", sessionPosition);
                bookCampSummary.putExtra("locationPosition", locationPosition);
                bookCampSummary.putExtra("strBookingType", strBookingType);
                bookCampSummary.putExtra("selectedChildrenArray", selectedChildrenArray.toString());
                bookCampSummary.putExtra("strComments", strComments);
                startActivity(bookCampSummary);

                /*if (strChildIds != null && strChildIds.length() > 0 && strChildIds.charAt(strChildIds.length() - 1) == ',') {
                    strChildIds = strChildIds.substring(0, strChildIds.length() - 1);
                }*/

                /*if(Utilities.isNetworkAvailable(getActivity())) {

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("child", strChildIds));
                    nameValuePairList.add(new BasicNameValuePair("booking_type", strBookingType));
                    nameValuePairList.add(new BasicNameValuePair("camps_id", clickedOnCamp.getCampId()));
                    nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnCamp.getLocationList().get(locationPosition).getLocationId()));
                    nameValuePairList.add(new BasicNameValuePair("camp_sessions_id", clickedOnCamp.getSessionsList().get(sessionPosition).getSessionId()));

                    for(int i=0;i<paramNames.size();i++){
                        nameValuePairList.add(new BasicNameValuePair(paramNames.get(i), dateCSV.get(i)));
                    }

                    nameValuePairList.add(new BasicNameValuePair("comments", strComments));

                    String webServiceUrl = Utilities.BASE_URL + "camps/booking_new";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, BOOK_CAMP, ParentBookCampDaysFragment.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        childrenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // check if already exists

                if (position == 0) {
                    return;
                }

                boolean alreadyExists = false;
                for (CampSelectedChildBean currentChild : selectedChildrenListing) {
                    if (currentChild.getChildBean().getId().equalsIgnoreCase(childrenListing.get(position).getId())) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (alreadyExists) {
                    // Do nothing as of now
                } else {
                    CampSelectedChildBean campSelectedChildBean = new CampSelectedChildBean();
                    campSelectedChildBean.setChildBean(childrenListing.get(position));
                    selectedChildrenListing.add(campSelectedChildBean);

                    parentCampChildrenNamesGridAdapter.notifyDataSetChanged();
                    parentCampChooseDatesListingAdapter.notifyDataSetChanged();

                    if (selectedChildrenListing.size() != 0) {
                        Utilities.setGridViewHeightBasedOnChildren(selectedChildrenGridView, 2);
                        Utilities.setListViewHeightBasedOnChildren(chooseDaysListView);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do Nothing
            }
        });

        selectedChildrenGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedChildrenListing.remove(position);

                parentCampChildrenNamesGridAdapter.notifyDataSetChanged();
                parentCampChooseDatesListingAdapter.notifyDataSetChanged();

                if (selectedChildrenListing.size() != 0) {
                    Utilities.setGridViewHeightBasedOnChildren(selectedChildrenGridView, 2);
                    Utilities.setListViewHeightBasedOnChildren(chooseDaysListView);
                }

                calculateCost();
            }
        });

        return view;
    }

    public void calculateCost() {

        totalAmountValue = 0;


        HashMap<Integer, HashMap> yearWiseMap = new HashMap<Integer, HashMap>();


        for (CampSelectedChildBean childBean : selectedChildrenListing) {
            yearWiseMap.clear();
            for (CampDateBean campDateBean : childBean.getSelectedDatesList()) {
                String selected_week_of_year = String.valueOf(campDateBean.getWeekOfYear());
                Date selected_day = convertStringToDate(campDateBean.getDate(), "yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(selected_day);
                int year = calendar.get(Calendar.YEAR);

                if (yearWiseMap.containsKey(year)) {
                    HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
                    map = yearWiseMap.get(year);
                    if (map.containsKey(selected_week_of_year)) {
                        ArrayList<String> selected_dates = new ArrayList<>();
                        selected_dates = map.get(selected_week_of_year);
                        selected_dates.add(campDateBean.getDate());
                        map.put(selected_week_of_year, selected_dates);
                    } else {
                        ArrayList<String> selected_date = new ArrayList<>();
                        selected_date.add(campDateBean.getDate());
                        map.put(selected_week_of_year, selected_date);

                    }
                    yearWiseMap.put(year, map);
                } else {
                    HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
                    ArrayList<String> selected_date = new ArrayList<>();
                    selected_date.add(campDateBean.getDate());
                    map.put(selected_week_of_year, selected_date);
                    yearWiseMap.put(year, map);
                }


            }
            int no_of_weeks = 0;
            int no_of_days_left = 0;
            for (Map.Entry<Integer, HashMap> Yearentry : yearWiseMap.entrySet()) {
                HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
                map = Yearentry.getValue();
                for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
                    ArrayList<String> weeks_array = entry.getValue();
                    if (weeks_array.size() == runs_on_days.size()) {
                        no_of_weeks = no_of_weeks + 1;
                    } else {
                        no_of_days_left = no_of_days_left + weeks_array.size();
                    }

                }
            }


            if (no_of_weeks != 0) {
                totalAmountValue = totalAmountValue + (no_of_weeks * (Float.parseFloat(clickedOnCamp.getSessionsList().get(sessionPosition).getPerWeekCost())));
            }
            if (no_of_days_left != 0) {
                totalAmountValue = totalAmountValue + (no_of_days_left * Float.parseFloat(clickedOnCamp.getSessionsList().get(sessionPosition).getPerDayCost()));
            }
        }


        DecimalFormat df = new DecimalFormat("#.00");

        String academy_currency = sharedPreferences.getString("academy_currency", null);

        if (totalAmountValue == 0) {
            totalAmount.setText("0.00 " + academy_currency);
        } else {
            totalAmount.setText(df.format(totalAmountValue) + " " + academy_currency);
        }
        /*if(discountAmountValue == 0) {
            discountAmount.setText("0.00 AED");
        } else {
            discountAmount.setText(df.format(discountAmountValue)+" AED");
        }
        if(netAmountValue == 0) {
            netAmount.setText("0.00 AED");
        } else {
            netAmount.setText(df.format(netAmountValue)+" AED");
        }*/
    }

    private void getChildrenListing() {
        if (Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "children/children_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_CHILDREN_LISTING, ParentBookCampDaysFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllAvailableDatesListing() {
        if (Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("camps_id", clickedOnCamp.getCampId()));
            if (clickedOnCamp.getLocationList().size() > 1) {
                nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnCamp.getLocationList().get(locationPosition).getLocationId()));

            } else {
                nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnCamp.getLocationList().get(0).getLocationId()));

            }
            nameValuePairList.add(new BasicNameValuePair("camp_sessions_id", clickedOnCamp.getSessionsList().get(sessionPosition).getSessionId()));

            String webServiceUrl = Utilities.BASE_URL + "camps/new_availability";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_ALL_AVAILABLE_DATES, ParentBookCampDaysFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

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

                            ChildBean childBean = new ChildBean();
                            childBean.setId("-1");

                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                            childBean.setFirstName("Please select "+verbiage_singular);
                            childBean.setFullName("Please select "+verbiage_singular);

                            childrenListing.add(childBean);

                            for (int i = 0; i < dataArray.length(); i++) {
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

                                childrenListing.add(childBean);
                            }
                        } else {
                            ChildBean childBean = new ChildBean();
                            childBean.setId("-1");
//                            childBean.setFirstName("Please select Child");
//                            childBean.setFullName("Please select Child");

                            String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                            String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);

                            childBean.setFirstName("Please select "+verbiage_singular);
                            childBean.setFullName("Please select "+verbiage_singular);

                            childrenListing.add(childBean);

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                childrenSpinnerAdapter.notifyDataSetChanged();

                if (childrenListing.size() == 2) {
                    childrenSpinner.setSelection(1);
                }

                getAllAvailableDatesListing();
                break;
            case GET_ALL_AVAILABLE_DATES:

                availableDatesListing.clear();

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONObject dataObject = responseObject.getJSONObject("data");

                            JSONArray availableDatesArray = dataObject.getJSONArray("available_dates");
                            CampDateBean campDateBean;
                            for (int i = 0; i < availableDatesArray.length(); i++) {
                                JSONObject availableDateObject = availableDatesArray.getJSONObject(i);

                                campDateBean = new CampDateBean();
                                campDateBean.setDate(availableDateObject.getString("date"));
                                campDateBean.setSeats(availableDateObject.getInt("seats"));
                                campDateBean.setSelected(false);
                                Date date = convertStringToDate(campDateBean.getDate(), "yyyy-MM-dd");
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                campDateBean.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));


                                availableDatesListing.add(campDateBean);
                            }


                            if (availableDatesArray.length() == 0) {
                                final Dialog dialog = new Dialog(getActivity());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.parent_dialog_no_dates_found);

                                TextView text1 = dialog.findViewById(R.id.text1);
                                TextView ok = dialog.findViewById(R.id.ok);

                                text1.setText("No dates available");

                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                            }
                            JSONArray runs_on_days_array = dataObject.getJSONArray("runs_on_days");
                            for (int i = 0; i < runs_on_days_array.length(); i++) {
                                String runDays = (String) runs_on_days_array.get(i);
                                runs_on_days.add(runDays);
                            }


                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case BOOK_CAMP:

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not reach server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
//                            Toast.makeText(ParentBookCampScreenOLD.this, message, Toast.LENGTH_SHORT).show();
//                            finish();

                            String netAmount = responseObject.getString("net_amount");
                            String ordersId = responseObject.getString("orders_id");

                            Intent intent = new Intent(getActivity(), ParentPaymentGatewayWebViewScreen.class);
                            intent.putExtra(AvenuesParams.ACCESS_CODE, Utilities.ACCESS_CODE);
                            intent.putExtra(AvenuesParams.MERCHANT_ID, Utilities.MERCHANT_ID);
                            intent.putExtra(AvenuesParams.ORDER_ID, ordersId);
//                            intent.putExtra(AvenuesParams.CURRENCY, Utilities.CURRENCY);
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                            String academy_currency = sharedPreferences.getString("academy_currency", null);
                            intent.putExtra(AvenuesParams.CURRENCY, academy_currency);
                            intent.putExtra(AvenuesParams.AMOUNT, netAmount);

//                            intent.putExtra(AvenuesParams.REDIRECT_URL, Utilities.BASE_URL + "payments/response_handler");
                            intent.putExtra(AvenuesParams.REDIRECT_URL, Utilities.REDIRECT_URL);
//                            intent.putExtra(AvenuesParams.CANCEL_URL, Utilities.BASE_URL + "payments/response_handler");
                            intent.putExtra(AvenuesParams.CANCEL_URL, Utilities.REDIRECT_URL);
                            intent.putExtra(AvenuesParams.RSA_KEY_URL, Utilities.BASE_URL + "payments/get_RSA");

                            startActivity(intent);

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

    private Date convertStringToDate(String dateString, String pattern) {
        Date date = null;

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            date = format.parse(dateString);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

}