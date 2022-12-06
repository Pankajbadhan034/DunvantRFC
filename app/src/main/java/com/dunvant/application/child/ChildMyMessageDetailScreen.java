package com.dunvant.application.child;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.ChildMyMessageDetailBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.child.adapters.ChildMyMessagesDetailAdapter;
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

public class ChildMyMessageDetailScreen extends AppCompatActivity implements IWebServiceCallback {
//    TextView backToGameLabel;
    String parentId;
    String friendId;
    String name;
    TextView title;
    ImageView backButton;
    ImageView refresh;
    Button delete;
    Button reply;
    ListView list;
    ChildMyMessageDetailBean childMyMessageDetailBean;
    ArrayList<ChildMyMessageDetailBean> childMyMessageDetailBeanArrayList = new ArrayList<>();
    ChildMyMessagesDetailAdapter childMyMessagesDetailAdapter;
    private final String GET_MESSAGES_DETAIL = "GET_MESSAGES_DETAIL";
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Typeface helvetica;
    Typeface linoType;
    String unreadMessageIds;
    String subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_activity_my_message_detail_screen);

        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        parentId = getIntent().getStringExtra("parentId");
        name = getIntent().getStringExtra("name");
        friendId = getIntent().getStringExtra("friendId");
        unreadMessageIds = getIntent().getStringExtra("unread_msgs_ids");
        subject = getIntent().getStringExtra("subject");
        //System.out.println("unReadMessageIds::"+unreadMessageIds);
        title = (TextView) findViewById(R.id.title);
        title.setText(name);

        list = (ListView) findViewById(R.id.list);
        delete = (Button) findViewById(R.id.delete);
        reply = (Button) findViewById(R.id.reply);
        backButton = (ImageView) findViewById(R.id.backButton);
       // backToGameLabel = (TextView) findViewById(R.id.backToGameLabel);
        refresh = (ImageView) findViewById(R.id.refresh);

        title.setTypeface(linoType);
        delete.setTypeface(helvetica);
        reply.setTypeface(helvetica);
      //  backToGameLabel.setTypeface(helvetica);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(ChildMyMessageDetailScreen.this)) {
                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("parent_id", parentId));
                    nameValuePairList.add(new BasicNameValuePair("unreadMessageIds", unreadMessageIds));

                    String webServiceUrl = Utilities.BASE_URL + "inbox/conversation_messages";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:" + loggedInUser.getId());
                    headers.add("X-access-token:" + loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyMessageDetailScreen.this, nameValuePairList, GET_MESSAGES_DETAIL, ChildMyMessageDetailScreen.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);
                } else {
                    Toast.makeText(ChildMyMessageDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });

            if (Utilities.isNetworkAvailable(ChildMyMessageDetailScreen.this)) {
                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(new BasicNameValuePair("parent_id", parentId));

                String webServiceUrl = Utilities.BASE_URL + "inbox/conversation_messages";

                ArrayList<String> headers = new ArrayList<>();
                headers.add("X-access-uid:" + loggedInUser.getId());
                headers.add("X-access-token:" + loggedInUser.getToken());

                PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyMessageDetailScreen.this, nameValuePairList, GET_MESSAGES_DETAIL, ChildMyMessageDetailScreen.this, headers);
                postWebServiceAsync.execute(webServiceUrl);
            } else {
                Toast.makeText(ChildMyMessageDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
            }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent obj = new Intent(ChildMyMessageDetailScreen.this,ChildMyMessagesReplyScreen.class);
                obj.putExtra("parentId", parentId);
                obj.putExtra("friendId", friendId);
                obj.putExtra("subject", subject);
                startActivity(obj);
            }
        });


    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case GET_MESSAGES_DETAIL:
                childMyMessageDetailBeanArrayList.clear();
                if (response == null) {
                    Toast.makeText(ChildMyMessageDetailScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        if(status){
                            JSONArray data = responseObject.getJSONArray("data");
                            for(int i=0; i<data.length(); i++){
                                JSONObject jsonObject = data.getJSONObject(i);
                                childMyMessageDetailBean = new ChildMyMessageDetailBean();
                                childMyMessageDetailBean.setId(jsonObject.getString("id"));
                                childMyMessageDetailBean.setParentId(jsonObject.getString("parent_id"));
                                childMyMessageDetailBean.setUsersId(jsonObject.getString("users_id"));
                                childMyMessageDetailBean.setRecipientId(jsonObject.getString("recipient_id"));
                                childMyMessageDetailBean.setSubject(jsonObject.getString("subject"));
                                childMyMessageDetailBean.setMessage(jsonObject.getString("message"));
                                childMyMessageDetailBean.setState(jsonObject.getString("state"));
                                childMyMessageDetailBean.setCreatedAtDate(jsonObject.getString("created_at"));
                                childMyMessageDetailBean.setSiteMediaId(jsonObject.getString("site_media_id"));
                                childMyMessageDetailBean.setCreatedAtFormatted(jsonObject.getString("created_at_formatted"));
                                childMyMessageDetailBean.setMessageDateFormatted(jsonObject.getString("message_date_formatted"));
                                childMyMessageDetailBean.setMessageTimeFormatted(jsonObject.getString("message_time_formatted"));
                                childMyMessageDetailBean.setSenderDpUrl(jsonObject.getString("sender_dp_url"));
                                childMyMessageDetailBean.setReceiverDpUrl(jsonObject.getString("receiver_dp_url"));
                                childMyMessageDetailBean.setFileUrl(jsonObject.getString("file_url"));
                                childMyMessageDetailBean.setMediaType(jsonObject.getString("media_type"));

                                childMyMessageDetailBeanArrayList.add(childMyMessageDetailBean);

                            }
                            //System.out.println("sizeList::"+childMyMessageDetailBeanArrayList.size());
                            childMyMessagesDetailAdapter = new ChildMyMessagesDetailAdapter(ChildMyMessageDetailScreen.this,childMyMessageDetailBeanArrayList);
                            list.setAdapter(childMyMessagesDetailAdapter);
                            childMyMessagesDetailAdapter.notifyDataSetChanged();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChildMyMessageDetailScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;




        }
    }

    @Override
    protected void onResume() {
        if (Utilities.isNetworkAvailable(ChildMyMessageDetailScreen.this)) {
            childMyMessageDetailBeanArrayList.clear();
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("parent_id", parentId));

            String webServiceUrl = Utilities.BASE_URL + "inbox/conversation_messages";

            ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildMyMessageDetailScreen.this, nameValuePairList, GET_MESSAGES_DETAIL, ChildMyMessageDetailScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);
        } else {
            Toast.makeText(ChildMyMessageDetailScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
        super.onResume();

    }
}
