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
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ParentOnlineStoreBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.ParentMainScreen;
import com.dunvant.application.parent.ParentOnlineShoppingParentchildProductTabsScreen;
import com.dunvant.application.parent.adapters.ParentOnlineShoppingAdapter;
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

public class ParentOnlineShoppingFragment extends Fragment implements IWebServiceCallback {

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;
    private final String ONLINE_DATA = "ONLINE_DATA";
    GridView gridView;
    ArrayList<ParentOnlineStoreBean> parentOnlineStoreBeanArrayList = new ArrayList<>();
    ParentOnlineStoreBean parentOnlineStoreBean;
    String userCount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_online_shopping_fragment, container, false);

        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
        helvetica = Typeface.createFromAsset(getActivity().getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        gridView = view.findViewById(R.id.gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), ParentOnlineShoppingParentchildProductTabsScreen.class);
                intent.putExtra("parentID", parentOnlineStoreBeanArrayList.get(i).getId());
                intent.putExtra("categoryName", parentOnlineStoreBeanArrayList.get(i).getCategoryName());
                startActivity(intent);
            }
        });




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onlineShoppingData();
    }

    private void onlineShoppingData() {
        if(Utilities.isNetworkAvailable(getActivity())) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));
            nameValuePairList.add(new BasicNameValuePair("userid", loggedInUser.getId()));

            String webServiceUrl = Utilities.BASE_URL + "product/parent_category_list";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(getActivity(), nameValuePairList, ONLINE_DATA, ParentOnlineShoppingFragment.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        }else{
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case ONLINE_DATA:

                parentOnlineStoreBeanArrayList.clear();

                if (response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                         String message = responseObject.getString("message");

                        if(status) {
                            userCount = responseObject.getString("cartCount");
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            for(int i=0; i<jsonArray.length(); i++){
                                parentOnlineStoreBean = new ParentOnlineStoreBean();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                parentOnlineStoreBean.setId(jsonObject.getString("id"));
                                parentOnlineStoreBean.setAcademiesId(jsonObject.getString("academies_id"));
                                parentOnlineStoreBean.setCategoryName(jsonObject.getString("category_name"));
                                parentOnlineStoreBean.setParentCategory(jsonObject.getString("parent_category"));
                                parentOnlineStoreBean.setDescription(jsonObject.getString("description"));
                                parentOnlineStoreBean.setImage(jsonObject.getString("image"));
                                parentOnlineStoreBean.setState(jsonObject.getString("state"));

                                parentOnlineStoreBeanArrayList.add(parentOnlineStoreBean);
                            }

                            ParentOnlineShoppingAdapter parentOnlineShoppingAdapter = new ParentOnlineShoppingAdapter(getActivity(), parentOnlineStoreBeanArrayList);
                            gridView.setAdapter(parentOnlineShoppingAdapter);
                            ((ParentMainScreen)getActivity()).loadCountShoppingStore(userCount);

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
