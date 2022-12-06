package com.dunvant.application.coach.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.CoachRugbyBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.coach.adapters.CoachRugbyAdapter;
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

import androidx.fragment.app.Fragment;

public class CoachRugbyFragment extends Fragment implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;
    private final String RUGUBY_DATA = "RUGUBY_DATA";
    ListView listView;
    ArrayList<CoachRugbyBean> coachRugbyBeanArrayList = new ArrayList<>();
    CoachRugbyBean coachRugbyBean;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coach_rugby_fragment, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        listView = view.findViewById(R.id.listView);

        rugubyData();


        return view;
    }

    private void rugubyData() {
        if(Utilities.isNetworkAvailable(getActivity())) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            if (loggedInUser.getRoleCode().equalsIgnoreCase("parent_role")) {
                nameValuePairList.add(new BasicNameValuePair("type", "1"));
            }else{
                nameValuePairList.add(new BasicNameValuePair("type", "2"));
            }

            String webServiceUrl = Utilities.BASE_URL + "user_posts/rugby";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, RUGUBY_DATA, CoachRugbyFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        }else{
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case RUGUBY_DATA:

                coachRugbyBeanArrayList.clear();

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                       // String message = responseObject.getString("message");

                        if(status) {
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            for(int i=0; i<jsonArray.length(); i++){
                                coachRugbyBean = new CoachRugbyBean();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                coachRugbyBean.setId(jsonObject.getString("id"));
                                coachRugbyBean.setTitle(jsonObject.getString("title"));
                                coachRugbyBean.setFileName(jsonObject.getString("file_name"));
                                coachRugbyBean.setDocumentFor(jsonObject.getString("document_for"));
                                coachRugbyBean.setState(jsonObject.getString("state"));
                                coachRugbyBean.setExt(jsonObject.getString("ext"));
                                coachRugbyBean.setFileUrl(jsonObject.getString("file_url"));

                                coachRugbyBeanArrayList.add(coachRugbyBean);
                            }

                CoachRugbyAdapter coachRugbyAdapter = new CoachRugbyAdapter(getActivity(), coachRugbyBeanArrayList);
                listView.setAdapter(coachRugbyAdapter);

                        } else {
                        //    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
