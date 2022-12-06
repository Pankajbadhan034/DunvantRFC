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
import com.dunvant.application.beans.CampSelectedChildBean;
import com.dunvant.application.beans.CampWeekBean;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.ParentBookCampSummaryScreen;
import com.dunvant.application.parent.ParentPaymentGatewayWebViewScreen;
import com.dunvant.application.parent.adapters.ParentCampChildrenNamesGridAdapter;
import com.dunvant.application.parent.adapters.ParentCampChooseWeeksListingAdapter;
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
import java.util.ArrayList;
import java.util.List;

public class ParentBookCampWeeksFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Typeface helvetica;
    Typeface linoType;

    Spinner childrenSpinner;
    GridView selectedChildrenGridView;
    ListView chooseWeeksListView;
    EditText comments;
    TextView lblTotalAmount;
    TextView totalAmount;
//    TextView discountAmount;
//    TextView netAmount;
    Button makePayment;

    CampBean clickedOnCamp;
    int locationPosition;
    int sessionPosition;

    private final String GET_CHILDREN_LISTING = "GET_CHILDREN_LISTING";
    private final String GET_ALL_AVAILABLE_WEEKS = "GET_ALL_AVAILABLE_WEEKS";
    private final String BOOK_CAMP = "BOOK_CAMP";

    ArrayList<ChildBean> childrenListing = new ArrayList<>();
    ArrayList<CampWeekBean> availableWeeksListing = new ArrayList<>();
    ArrayList<CampSelectedChildBean> selectedChildrenListing = new ArrayList<>();

    ParentChildrenSpinnerAdapter childrenSpinnerAdapter;
    ParentCampChildrenNamesGridAdapter parentCampChildrenNamesGridAdapter;
    ParentCampChooseWeeksListingAdapter parentCampChooseWeeksListingAdapter;

    float totalAmountValue = 0/*, discountAmountValue = 0, netAmountValue = 0*/;

    public ParentBookCampWeeksFragment(CampBean clickedOnCamp, int locationPosition, int sessionPosition){
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
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_book_camp_weeks, container, false);

        childrenSpinner = (Spinner) view.findViewById(R.id.childrenSpinner);
        selectedChildrenGridView = (GridView) view.findViewById(R.id.selectedChildrenGridView);
        chooseWeeksListView = (ListView) view.findViewById(R.id.chooseWeeksListView);
        comments = (EditText) view.findViewById(R.id.comments);
        lblTotalAmount = (TextView) view.findViewById(R.id.lblTotalAmount);
        totalAmount = (TextView) view.findViewById(R.id.totalAmount);
        String academy_currency = sharedPreferences.getString("academy_currency", null);
        totalAmount.setText("0.00 "+academy_currency);
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

        parentCampChooseWeeksListingAdapter = new ParentCampChooseWeeksListingAdapter(getActivity(), selectedChildrenListing, availableWeeksListing, chooseWeeksListView, ParentBookCampWeeksFragment.this);
        chooseWeeksListView.setAdapter(parentCampChooseWeeksListingAdapter);

        getChildrenListing();

        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalAmountValue == 0){
                    String verbiage_singular = sharedPreferences.getString("verbiage_singular", null);
                    String verbiage_plural = sharedPreferences.getString("verbiage_plural", null);
                    Toast.makeText(getActivity(), "Please choose at least one week for one "+verbiage_singular, Toast.LENGTH_SHORT).show();

                    return;
                }
                String strComments = comments.getText().toString().trim();
                /*if(strComments == null || strComments.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter Comment", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                String strBookingType = "2";
//                String strChildIds = "";
//                ArrayList<String> paramNames = new ArrayList<>();
//                ArrayList<String> dateCSV = new ArrayList<>();

                JSONArray selectedChildrenArray = new JSONArray();

                for(CampSelectedChildBean childBean : selectedChildrenListing){

                    for(CampWeekBean campWeekBean : childBean.getSelectedWeeksList()){
                        JSONObject childObject = new JSONObject();
                        try {
                            childObject.put("child_id", childBean.getChildBean().getId());
                            childObject.put("bdate", campWeekBean.getWeekDates());

                            selectedChildrenArray.put(childObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    /*strChildIds += childBean.getChildBean().getId()+",";

                    String strParamName = "camp_dates_"+childBean.getChildBean().getId();
                    String strDates = "";
                    for(CampWeekBean campWeekBean : childBean.getSelectedWeeksList()){
                        strDates += campWeekBean.getWeekDates()+",";
                    }
                    if (strDates != null && strDates.length() > 0 && strDates.charAt(strDates.length() - 1) == ',') {
                        strDates = strDates.substring(0, strDates.length() - 1);
                    }

                    if(!childBean.getSelectedWeeksList().isEmpty()){
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

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, BOOK_CAMP, ParentBookCampWeeksFragment.this, headers);
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

                if(position == 0) {
                    return;
                }

                boolean alreadyExists = false;
                for(CampSelectedChildBean currentChild : selectedChildrenListing){
                    if(currentChild.getChildBean().getId().equalsIgnoreCase(childrenListing.get(position).getId())){
                        alreadyExists = true;
                        break;
                    }
                }

                if(alreadyExists) {
                    // Do nothing as of now
                } else {
                    CampSelectedChildBean campSelectedChildBean = new CampSelectedChildBean();
                    campSelectedChildBean.setChildBean(childrenListing.get(position));
                    selectedChildrenListing.add(campSelectedChildBean);

                    parentCampChildrenNamesGridAdapter.notifyDataSetChanged();
                    parentCampChooseWeeksListingAdapter.notifyDataSetChanged();

                    if(selectedChildrenListing.size() != 0){
                        Utilities.setGridViewHeightBasedOnChildren(selectedChildrenGridView, 2);
                        Utilities.setListViewHeightBasedOnChildren(chooseWeeksListView);
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
                parentCampChooseWeeksListingAdapter.notifyDataSetChanged();

                if(selectedChildrenListing.size() != 0){
                    Utilities.setGridViewHeightBasedOnChildren(selectedChildrenGridView, 2);
                    Utilities.setListViewHeightBasedOnChildren(chooseWeeksListView);
                }

                calculateCost();
            }
        });

        return view;
    }

    public void calculateCost(){

        totalAmountValue = 0;
//        discountAmountValue = 0;
//        netAmountValue = 0;

        for(CampSelectedChildBean childBean : selectedChildrenListing){
            for(CampWeekBean campWeekBean : childBean.getSelectedWeeksList()) {
                totalAmountValue += Float.parseFloat(clickedOnCamp.getSessionsList().get(sessionPosition).getPerWeekCost());
            }
        }

//        netAmountValue = totalAmountValue;

        DecimalFormat df = new DecimalFormat("#.00");

        String academy_currency = sharedPreferences.getString("academy_currency", null);

        if(totalAmountValue == 0){
            totalAmount.setText("0.00 "+academy_currency);
        }else {
            totalAmount.setText(df.format(totalAmountValue)+" "+academy_currency);
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
        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "children/children_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_CHILDREN_LISTING, ParentBookCampWeeksFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllAvailableWeeksListing() {
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("camps_id", clickedOnCamp.getCampId()));
            if(clickedOnCamp.getLocationList().size()>1){
                nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnCamp.getLocationList().get(locationPosition).getLocationId()));
            }
            else {
                nameValuePairList.add(new BasicNameValuePair("locations_id", clickedOnCamp.getLocationList().get(0).getLocationId()));

            }

           nameValuePairList.add(new BasicNameValuePair("camp_sessions_id", clickedOnCamp.getSessionsList().get(sessionPosition).getSessionId()));

            String webServiceUrl = Utilities.BASE_URL + "camps/new_availability";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_ALL_AVAILABLE_WEEKS, ParentBookCampWeeksFragment.this, headers);
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
                            // change 8fb2019
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
                                childBean.setDateOfBirth(childObject.getString("dob"));
                                childBean.setMedicalCondition(childObject.getString("medical_conditions"));

                                childrenListing.add(childBean);
                            }
                        } else {
                            ChildBean childBean = new ChildBean();
                            childBean.setId("-1");
                            // change 8fb2019
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

                if(childrenListing.size() == 2) {
                    childrenSpinner.setSelection(1);
                }

                getAllAvailableWeeksListing();
                break;
            case GET_ALL_AVAILABLE_WEEKS:

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            JSONObject dataObject = responseObject.getJSONObject("data");

                            JSONArray availableWeeksArray = dataObject.getJSONArray("available_weeks");
                            CampWeekBean campWeekBean;
                            for(int i=0;i<availableWeeksArray.length();i++){
                                JSONObject availableWeekObject = availableWeeksArray.getJSONObject(i);

                                campWeekBean = new CampWeekBean();
                                campWeekBean.setWeekName(availableWeekObject.getString("week_name"));
                                campWeekBean.setWeekDates(availableWeekObject.getString("week_dates"));
                                campWeekBean.setSeats(availableWeekObject.getInt("seats"));
                                campWeekBean.setSelected(false);

                                availableWeeksListing.add(campWeekBean);
                            }

                            if(availableWeeksArray.length() == 0){
                                final Dialog dialog = new Dialog(getActivity());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.parent_dialog_no_dates_found);

                                TextView text1 = dialog.findViewById(R.id.text1);
                                TextView ok = dialog.findViewById(R.id.ok);

                                text1.setText("No weeks available");

                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
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

                if(response == null) {
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
}