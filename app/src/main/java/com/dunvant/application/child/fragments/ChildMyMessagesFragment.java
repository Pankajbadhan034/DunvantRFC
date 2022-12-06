package com.dunvant.application.child.fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildMyMessagesBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.adapters.ChildInboxMessagesAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.GetWebServiceWithHeadersAsync;
import com.dunvant.application.webservices.IWebServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DEVLABS\pbadhan on 6/12/16.
 */
public class ChildMyMessagesFragment extends Fragment implements IWebServiceCallback {
    //Button inbox;
    //Button sent;
    TextView text;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    ListView msgList;
    ChildInboxMessagesAdapter childInboxMessagesAdapter;
    ArrayList<ChildMyMessagesBean> childMyMessagesBeanArrayList = new ArrayList<>();
    private final String GET_INBOX = "GET_INBOX";
    Typeface helvetica;
    Typeface linoType;
   // private final String GET_SENT = "GET_SENT";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        helvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getActivity().getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_fragment_my_messages, container, false);
        msgList=(ListView) view.findViewById(R.id.msgList);
        text = (TextView) view.findViewById(R.id.text);

       // sent = (Button) view.findViewById(R.id.sent);
       // inbox = (Button) view.findViewById(R.id.inbox);

        if(Utilities.isNetworkAvailable(getActivity())) {

            String webServiceUrl = Utilities.BASE_URL + "inbox/received_messages";
            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:"+loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            if(Utilities.isNetworkAvailable(getActivity())) {
                GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_INBOX, ChildMyMessagesFragment.this, headers);
                getWebServiceWithHeadersAsync.execute(webServiceUrl);
            }else{
                Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }

//        inbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                inboxTab();
//                if(Utilities.isNetworkAvailable(getActivity())) {
//
//                    String webServiceUrl = Utilities.BASE_URL + "api/inbox/received_messages";
//                    ArrayList<String> headers = new ArrayList<>();
//                    headers.add("X-access-uid:"+loggedInUser.getId());
//                    headers.add("X-access-token:" + loggedInUser.getToken());
//
//                    if(Utilities.isNetworkAvailable(getActivity())) {
//                        GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_INBOX, ChildMyMessagesFragment.this, headers);
//                        getWebServiceWithHeadersAsync.execute(webServiceUrl);
//                    }else{
//                        Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        sent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sentTab();
//                if(Utilities.isNetworkAvailable(getActivity())) {
//
//                    String webServiceUrl = Utilities.BASE_URL + "api/inbox/sent_messages";
//                    ArrayList<String> headers = new ArrayList<>();
//                    headers.add("X-access-uid:"+loggedInUser.getId());
//                    headers.add("X-access-token:" + loggedInUser.getToken());
//
//                    if(Utilities.isNetworkAvailable(getActivity())) {
//                        GetWebServiceWithHeadersAsync getWebServiceWithHeadersAsync = new GetWebServiceWithHeadersAsync(getActivity(), GET_SENT, ChildMyMessagesFragment.this, headers);
//                        getWebServiceWithHeadersAsync.execute(webServiceUrl);
//                    }else{
//                        Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Toast.makeText(getActivity(), R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        return view;
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_INBOX:

                childMyMessagesBeanArrayList.clear();

                if(response == null) {
                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {

                            ChildMyMessagesBean childMyMessagesBean;

                            String responseArray=responseObject.getString("data");
                            JSONArray myFriendsArray=new JSONArray(responseArray);

                         //   if(myFriendsArray.length()==0||myFriendsArray==null){
                            //    dataRecieved();

                            for(int i=0;i<myFriendsArray.length();i++){
                                JSONObject myFriendObject = myFriendsArray.getJSONObject(i);
                                childMyMessagesBean = new ChildMyMessagesBean();
                                childMyMessagesBean.setUserId(myFriendObject.getString("users_id"));
                                childMyMessagesBean.setRecieverName(myFriendObject.getString("receiver_name"));
                                childMyMessagesBean.setReceiverDpUrl(myFriendObject.getString("receiver_dp_url"));
                                childMyMessagesBean.setSenderName(myFriendObject.getString("sender_name"));
                                childMyMessagesBean.setMessage(myFriendObject.getString("message"));
                                childMyMessagesBean.setMessageFormattedDate(myFriendObject.getString("message_date_formatted"));
                                childMyMessagesBean.setSenderDpUrl(myFriendObject.getString("sender_dp_url"));
                                String parentId = myFriendObject.getString("parent_id");
                                childMyMessagesBean.setUnreadCount(myFriendObject.getString("unread_count"));
                                childMyMessagesBean.setUnreadMessagesIds(myFriendObject.getString("unread_msgs_ids"));

                                if(parentId.equalsIgnoreCase("null")){
                                    childMyMessagesBean.setParentId(myFriendObject.getString("id"));
                                }else{
                                    childMyMessagesBean.setParentId(myFriendObject.getString("parent_id"));
                                }


                                if(loggedInUser.getId().equalsIgnoreCase(myFriendObject.getString("users_id"))){
                                    childMyMessagesBean.setFriendId(myFriendObject.getString("recipient_id"));
                                }else{
                                    childMyMessagesBean.setFriendId(myFriendObject.getString("users_id"));
                                }

                                childMyMessagesBean.setSubject(myFriendObject.getString("subject"));



                                childMyMessagesBeanArrayList.add(childMyMessagesBean);

                            }
                            childInboxMessagesAdapter = new ChildInboxMessagesAdapter(getActivity(),childMyMessagesBeanArrayList);
                            msgList.setAdapter(childInboxMessagesAdapter);
                            childInboxMessagesAdapter.notifyDataSetChanged();

//                                System.out.println("Size:::"+childMyMessagesBeanArrayList.size());
                           // }
//                            else {
                            dataRecieved();
//                            }

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            noDataRecieved();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
//
//            case GET_SENT:
//
//                childMyMessagesBeanArrayList.clear();
//
//                if(response == null) {
//                    Toast.makeText(getActivity(), "Could not connect to server", Toast.LENGTH_SHORT).show();
//                } else {
//                    try {
//                        JSONObject responseObject = new JSONObject(response);
//
//                        boolean status = responseObject.getBoolean("status");
//                        String message = responseObject.getString("message");
//
//                        if (status) {
//
//                            ChildMyMessagesBean childMyMessagesBean;
//                            String responseArray=responseObject.getString("data");
//                            JSONArray myFriendsArray=new JSONArray(responseArray);
//                            System.out.println("array__"+myFriendsArray);
//                            for(int i=0;i<myFriendsArray.length();i++){
//                                JSONObject myFriendObject = myFriendsArray.getJSONObject(i);
//                                childMyMessagesBean = new ChildMyMessagesBean();
//                                childMyMessagesBean.setSenderName(myFriendObject.getString("sender_name"));
//                                childMyMessagesBean.setMessage(myFriendObject.getString("message"));
//                                childMyMessagesBean.setMessageFormattedDate(myFriendObject.getString("message_date_formatted"));
//                                childMyMessagesBean.setSenderDpUrl(myFriendObject.getString("sender_dp_url"));
//
//                                childMyMessagesBeanArrayList.add(childMyMessagesBean);
//
//                            }
//                            childInboxMessagesAdapter = new ChildInboxMessagesAdapter(getActivity(),childMyMessagesBeanArrayList);
//                            msgList.setAdapter(childInboxMessagesAdapter);
//                            childInboxMessagesAdapter.notifyDataSetChanged();
//
//                        } else {
//                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                break;
        }
    }

//    public void inboxTab(){
//        inbox.setBackgroundColor(Color.parseColor("#ffe02e"));
//        inbox.setTextColor(Color.parseColor("#333333"));
//        sent.setBackgroundColor(Color.parseColor("#333333"));
//        sent.setTextColor(Color.parseColor("#ffffff"));
//    }
//    public void sentTab(){
//        inbox.setBackgroundColor(Color.parseColor("#333333"));
//        inbox.setTextColor(Color.parseColor("#ffffff"));
//        sent.setBackgroundColor(Color.parseColor("#ffe02e"));
//        sent.setTextColor(Color.parseColor("#333333"));
//    }

    public void dataRecieved(){
        text.setVisibility(View.GONE);
        msgList.setVisibility(View.VISIBLE);
    }

    public void noDataRecieved(){
        text.setVisibility(View.VISIBLE);
        msgList.setVisibility(View.GONE);
    }
}
