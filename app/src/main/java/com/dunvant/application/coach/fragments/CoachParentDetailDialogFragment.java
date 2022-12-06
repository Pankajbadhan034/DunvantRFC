package com.dunvant.application.coach.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.DataRegistrationBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachParentDetailDialogAdapter;
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
import androidx.fragment.app.DialogFragment;

public class CoachParentDetailDialogFragment extends DialogFragment implements IWebServiceCallback{
    ArrayList<DataRegistrationBean> dataRegistrationBeanArrayList = new ArrayList<>();
    ListView list;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

//    DatesResultBean clickedOnDateResult;

    TextView childName;
    TextView lblParentContactDetails;
    TextView parentName;
    TextView parentEmail;
    TextView parentContactNumber;
    TextView medicalCondition;
    TextView className;
    TextView fieldCLub;

    String strChildId;
    String strChildName;
    UserBean parentBean;

    private final String GET_PARENT_INFO = "GET_PARENT_INFO";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coach_fragment_parent_detail, container, false);

        childName = view.findViewById(R.id.childName);
        lblParentContactDetails = view.findViewById(R.id.lblParentContactDetails);
        parentName = view.findViewById(R.id.parentName);
        parentEmail = view.findViewById(R.id.parentEmail);
        parentContactNumber = view.findViewById(R.id.parentContactNumber);
        medicalCondition = view.findViewById(R.id.medicalCondition);
        className = view.findViewById(R.id.className);
        fieldCLub = view.findViewById(R.id.fieldCLub);
        list = view.findViewById(R.id.list);

        Bundle bundle = getArguments();
        if(bundle != null){
//            clickedOnDateResult = (DatesResultBean) bundle.getSerializable("clickedOnDateResult");
            strChildId = bundle.getString("childId");
            strChildName = bundle.getString("childName");

            childName.setText(strChildName);

            getParentInfo();
        }

        return view;
    }

    private void showInfo(){
        parentName.setText("Parent Name: "+parentBean.getFullName());
        parentEmail.setText("Parent Email: "+parentBean.getEmail());
        parentContactNumber.setText("Parent Contact No: "+parentBean.getContactNumbers());
        medicalCondition.setText("Medical Condition: "+parentBean.getMedicalCondition());
        fieldCLub.setText("Club: "+parentBean.getFieldCLub());
        className.setText("Class Name: "+parentBean.getClassName());

        CoachParentDetailDialogAdapter coachRugbyAdapter = new CoachParentDetailDialogAdapter(getActivity(), dataRegistrationBeanArrayList);
        list.setAdapter(coachRugbyAdapter);

    }

    private void getParentInfo(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("child_id", strChildId));

            String webServiceUrl = Utilities.BASE_URL + "coach/get_child_parent_info";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, GET_PARENT_INFO, CoachParentDetailDialogFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case GET_PARENT_INFO:

                if(response == null){
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status){

                            JSONObject dataObject = responseObject.getJSONObject("data");
                            parentBean = new UserBean();

                            parentBean.setUsername(dataObject.getString("username"));
                            parentBean.setEmail(dataObject.getString("email"));
                            parentBean.setFirstName(dataObject.getString("first_name"));
                            parentBean.setLastName(dataObject.getString("last_name"));
                            parentBean.setFullName(dataObject.getString("full_name"));
                            parentBean.setContactNumbers(dataObject.getString("contact_numbers"));
                            parentBean.setMedicalCondition(dataObject.getString("medical_conditions"));
                            parentBean.setClassName(dataObject.getString("classname"));
                            parentBean.setFieldCLub(dataObject.getString("club"));


                            JSONArray data_registration = responseObject.getJSONArray("data_registration");
                            for(int i=0; i<data_registration.length(); i++){
                                DataRegistrationBean dataRegistrationBean = new DataRegistrationBean();
                                JSONObject jsonObject = data_registration.getJSONObject(i);
                                dataRegistrationBean.setLabel_name(jsonObject.getString("label_name"));
                                dataRegistrationBean.setValue(jsonObject.getString("value"));

                                dataRegistrationBeanArrayList.add(dataRegistrationBean);
                            }

                            showInfo();
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