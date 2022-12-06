package com.dunvant.application.parent.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CardBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParentAddCardFragment extends Fragment implements IWebServiceCallback{

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

    Typeface helvetica;
    Typeface linoType;

    EditText cardName;
    EditText cardNumber;
    EditText expiryMonth;
    EditText expiryYear;
    Button save;

    private final String GET_CARD_DETAILS = "GET_CARD_DETAILS";
    private final String SAVE_CARD_DETAILS = "SAVE_CARD_DETAILS";

    CardBean cardBean;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_fragment_add_card, container, false);

        cardName = view.findViewById(R.id.cardName);
        cardNumber = view.findViewById(R.id.cardNumber);
        expiryMonth = view.findViewById(R.id.expiryMonth);
        expiryYear = view.findViewById(R.id.expiryYear);
        save = view.findViewById(R.id.save);

        getCardDetails();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strCardName = cardName.getText().toString().trim();
                String strCardNumber = cardNumber.getText().toString().trim();
                String strExpiryMonth = expiryMonth.getText().toString().trim();
                String strExpiryYear = expiryYear.getText().toString().trim();

                if(strCardName == null || strCardName.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter Card Name", Toast.LENGTH_SHORT).show();
                } else if(strCardNumber == null || strCardNumber.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter Card Number", Toast.LENGTH_SHORT).show();
                } else if(strExpiryMonth == null || strExpiryMonth.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter Expiry Month", Toast.LENGTH_SHORT).show();
                } else if(strExpiryYear == null || strExpiryYear.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter Expiry Year", Toast.LENGTH_SHORT).show();
                } else {
                    if(Utilities.isNetworkAvailable(getActivity())) {

                        List<NameValuePair> nameValuePairList = new ArrayList<>();
                        nameValuePairList.add(new BasicNameValuePair("name_card", strCardName));
                        nameValuePairList.add(new BasicNameValuePair("card_no", strCardNumber));
                        nameValuePairList.add(new BasicNameValuePair("exp_mon", strExpiryMonth));
                        nameValuePairList.add(new BasicNameValuePair("exp_year", strExpiryYear));

                        String webServiceUrl = Utilities.BASE_URL + "account/add_payment_card";

                        ArrayList<String> headers = new ArrayList<>();
                        headers.add("X-access-uid:"+loggedInUser.getId());
                        headers.add("X-access-token:"+loggedInUser.getToken());

                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, SAVE_CARD_DETAILS, ParentAddCardFragment.this, headers);
                        postWebServiceAsync.execute(webServiceUrl);

                    } else {
                        Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cardName.setTypeface(helvetica);
        cardNumber.setTypeface(helvetica);
        expiryMonth.setTypeface(helvetica);
        expiryYear.setTypeface(helvetica);
        save.setTypeface(linoType);

        return view;
    }

    private void showCardData(){
        if(cardBean != null){
            cardName.setText(cardBean.getName());
            cardNumber.setText(cardBean.getCardNumber());
            expiryMonth.setText(cardBean.getExpiryMonth());
            expiryYear.setText(cardBean.getExpiryYear());
        }
    }

    private void getCardDetails(){
        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "account/payment_card_details";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:"+loggedInUser.getToken());

            GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_CARD_DETAILS, ParentAddCardFragment.this, headers);
            getWebServiceWithHeadersAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag){
            case GET_CARD_DETAILS:

                if(response == null){
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status){
                            cardBean = new CardBean();

                            JSONObject dataObject = responseObject.getJSONObject("data");
                            cardBean.setId(dataObject.getString("id"));
                            cardBean.setUsersId(dataObject.getString("users_id"));
                            cardBean.setName(dataObject.getString("name"));
                            cardBean.setCardNumber(dataObject.getString("card_number"));
                            cardBean.setExpiryMonth(dataObject.getString("expiry_month"));
                            cardBean.setExpiryYear(dataObject.getString("expiry_year"));

                            showCardData();

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case SAVE_CARD_DETAILS:

                if(response == null){
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        if(status){

                        } else {

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