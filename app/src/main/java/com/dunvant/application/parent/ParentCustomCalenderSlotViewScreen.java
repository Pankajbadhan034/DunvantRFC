package com.dunvant.application.parent;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.PitchSlotsBean;
import com.dunvant.application.beans.PitchSlotsRowBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.parent.adapters.ParentCustomListAdapter;
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

import androidx.appcompat.app.AppCompatActivity;

public class ParentCustomCalenderSlotViewScreen extends AppCompatActivity implements IWebServiceCallback {
    int listViewItemClickPosition;
    String selectedSlot;
    String selectedPitchId;
    private final String SELECT_SLOT = "SELECT_SLOT";
    private final String UNSELECT_SLOT = "UNSELECT_SLOT";
    private final String SUBMIT_SLOTS = "SUBMIT_SLOTS";
    Typeface helvetica;
    Typeface linoType;
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    LinearLayout linear;
    ListView modeList2;
    ArrayList<ListView> listViewArrayList = new ArrayList<>();
    ArrayList<PitchSlotsRowBean> coloumnArrayList;
    ArrayList<PitchSlotsBean> pitchSlotsBeanArrayList;
    String dateStr;
    ArrayList<ArrayList<PitchSlotsRowBean>>allcollumnsArrayLists = new ArrayList<>();
    ArrayList<ArrayList<PitchSlotsBean>>allPitchSlotsBeanArrayList = new ArrayList<>();
    ArrayList<ParentCustomListAdapter> parentCustomListAdapterArrayList = new ArrayList<>();
    int JValue;
    ArrayList<PitchSlotsBean> selectedPitchSlotList;
    ArrayList<PitchSlotsRowBean> selectedArrayListTemp = new ArrayList<>();
    Button submit;
    ImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_custom_calender_slot_view_activity);

        helvetica = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

        pitchSlotsBeanArrayList = (ArrayList<PitchSlotsBean>) getIntent().getSerializableExtra("pitchSlotsBeanArrayList");
        dateStr = getIntent().getStringExtra("date");

        linear = findViewById(R.id.linear);
        submit = findViewById(R.id.submit);
        backButton = findViewById(R.id.backButton);
        //list1 = findViewById(R.id.list1);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //submitSlots();
            }
        });

        customListsViewItems();

    }


    public void customListsViewItems(){

        for(int i=0; i<pitchSlotsBeanArrayList.size(); i++){
            modeList2 = new ListView(this);
            coloumnArrayList = new ArrayList<>();
            for(int j=0; j<pitchSlotsBeanArrayList.get(i).getStringArrayList().size(); j++){
                coloumnArrayList.add(pitchSlotsBeanArrayList.get(i).getStringArrayList().get(j));


                if(j==0){
                   // ParentCustomListAdapter adapter1 = new ParentCustomListAdapter(ParentCustomCalenderSlotViewScreen.this, coloumnArrayList);
                 //   list1.setAdapter(adapter1);
                }
            }




//            ParentCustomListAdapter adapter = new ParentCustomListAdapter(ParentCustomCalenderSlotViewScreen.this, coloumnArrayList);
//            modeList2.setAdapter(adapter);
//
//            linear.addView(modeList2);
//
//            Utilities.setListViewHeightBasedOnChildren(modeList2);
//            listViewArrayList.add(modeList2);
//
//            parentCustomListAdapterArrayList.add(adapter);
//            allcollumnsArrayLists.add(coloumnArrayList);
//            allPitchSlotsBeanArrayList.add(pitchSlotsBeanArrayList);
        }

        for(int j = 0; j<listViewArrayList.size(); j++){
            final ArrayList<PitchSlotsRowBean> selectedArrayList =  allcollumnsArrayLists.get(j);
            selectedPitchSlotList =  allPitchSlotsBeanArrayList.get(j);

            final int finalJ = j;
            listViewArrayList.get(j).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    listViewItemClickPosition = position;

                    if(listViewItemClickPosition==0 || selectedArrayList.get(listViewItemClickPosition).getSlotName().equalsIgnoreCase("--")){

                    }else{
                        JValue = finalJ;
                        selectedSlot = selectedArrayList.get(listViewItemClickPosition).getSlotName();
                        selectedPitchId = selectedPitchSlotList.get(JValue).getPitchId();
                        selectedArrayListTemp = selectedArrayList;




                        if(selectedArrayList.get(listViewItemClickPosition).isClicked()){
                            slotUnselected(selectedSlot,selectedPitchId);
                        }else{
                            if(selectedArrayList.get(listViewItemClickPosition).isDisabled()){

                            }else{
                                slotSelected(selectedSlot,selectedPitchId);
                            }

                        }



                    }
                }
            });
        }
    }


    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case SELECT_SLOT:
                if (response == null) {
                    Toast.makeText(ParentCustomCalenderSlotViewScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
//                            childPostsBeanArrayList.get(acceptCountPosition).setChallengeChallengeStatus("accepted_challenge");
//                            notifyDataSetChanged();

                            JSONArray jsonArray = responseObject.getJSONArray("data");

                            ArrayList<String> disabledIdsArray = new ArrayList<>();

                            for(int i=0; i<jsonArray.length(); i++){
                                disabledIdsArray.add(jsonArray.getString(i));
                            }

                            for(int j=0; j<disabledIdsArray.size(); j++){

                                if(selectedPitchId.equalsIgnoreCase(disabledIdsArray.get(j))){
                                    selectedArrayListTemp.get(listViewItemClickPosition).setClicked(true);
                                    parentCustomListAdapterArrayList.get(JValue).notifyDataSetChanged();

                                }else{

                                    for(int s=0; s<selectedPitchSlotList.size(); s++){

                                        if(selectedPitchSlotList.get(s).getPitchId().equalsIgnoreCase(disabledIdsArray.get(j))){
                                            System.out.println("1a::"+selectedPitchSlotList.get(s).getPitchId());
                                            System.out.println("1b::"+disabledIdsArray.get(j));

                                            for(int k=0; k<selectedPitchSlotList.get(s).getStringArrayList().size(); k++){
                                                PitchSlotsRowBean pitchSlotsRowBean = selectedPitchSlotList.get(s).getStringArrayList().get(k);

                                                if(pitchSlotsRowBean.getSlotName().equalsIgnoreCase(selectedSlot)){
                                                    System.out.println("position::"+pitchSlotsRowBean.getSlotName());
                                                    selectedPitchSlotList.get(s).getStringArrayList().get(k).setDisabled(true);
                                                     parentCustomListAdapterArrayList.get(s).notifyDataSetChanged();
                                                }


                                            }


                                        }

                                    }


                                }
                            }








                        }else{
                            Toast.makeText(ParentCustomCalenderSlotViewScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentCustomCalenderSlotViewScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;

            case UNSELECT_SLOT:
                if (response == null) {
                    Toast.makeText(ParentCustomCalenderSlotViewScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
//                            childPostsBeanArrayList.get(acceptCountPosition).setChallengeChallengeStatus("accepted_challenge");
//                            notifyDataSetChanged();

                            JSONArray jsonArray = responseObject.getJSONArray("data");

                            ArrayList<String> disabledIdsArray = new ArrayList<>();

                            for(int i=0; i<jsonArray.length(); i++){
                                disabledIdsArray.add(jsonArray.getString(i));
                            }







                            for(int j=0; j<disabledIdsArray.size(); j++){

                                if(selectedPitchId.equalsIgnoreCase(disabledIdsArray.get(j))){
                                    selectedArrayListTemp.get(listViewItemClickPosition).setClicked(false);
                                    parentCustomListAdapterArrayList.get(JValue).notifyDataSetChanged();

                                }else{

                                    for(int s=0; s<selectedPitchSlotList.size(); s++){

                                        if(selectedPitchSlotList.get(s).getPitchId().equalsIgnoreCase(disabledIdsArray.get(j))){
                                            System.out.println("1a::"+selectedPitchSlotList.get(s).getPitchId());
                                            System.out.println("1b::"+disabledIdsArray.get(j));

                                            for(int k=0; k<selectedPitchSlotList.get(s).getStringArrayList().size(); k++){
                                                PitchSlotsRowBean pitchSlotsRowBean = selectedPitchSlotList.get(s).getStringArrayList().get(k);

                                                if(pitchSlotsRowBean.getSlotName().equalsIgnoreCase(selectedSlot)){
                                                    System.out.println("position::"+pitchSlotsRowBean.getSlotName());
                                                    selectedPitchSlotList.get(s).getStringArrayList().get(k).setDisabled(false);
                                                    parentCustomListAdapterArrayList.get(s).notifyDataSetChanged();
                                                }


                                            }


                                        }

                                    }


                                }
                            }








                        }else{
                            Toast.makeText(ParentCustomCalenderSlotViewScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentCustomCalenderSlotViewScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;

            case SUBMIT_SLOTS:
                if (response == null) {
                    Toast.makeText(ParentCustomCalenderSlotViewScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if (status) {
                            JSONArray jsonArray = responseObject.getJSONArray("data");

                        }else{
                            Toast.makeText(ParentCustomCalenderSlotViewScreen.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ParentCustomCalenderSlotViewScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                break;

        }
    }

    public void slotSelected(String slot, String pitchID){
        if (Utilities.isNetworkAvailable(ParentCustomCalenderSlotViewScreen.this)) {

            final List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("date", dateStr));
            nameValuePairList.add(new BasicNameValuePair("slot", slot));
            nameValuePairList.add(new BasicNameValuePair("pitch_id", pitchID));

            final ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            String webServiceUrl = Utilities.BASE_URL + "front_calendar/select_slot";
            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentCustomCalenderSlotViewScreen.this, nameValuePairList, SELECT_SLOT, ParentCustomCalenderSlotViewScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentCustomCalenderSlotViewScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    public void slotUnselected(String slot, String pitchID){
        if (Utilities.isNetworkAvailable(ParentCustomCalenderSlotViewScreen.this)) {

            final List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("date", dateStr));
            nameValuePairList.add(new BasicNameValuePair("slot", slot));
            nameValuePairList.add(new BasicNameValuePair("pitch_id", pitchID));

            final ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            String webServiceUrl = Utilities.BASE_URL + "front_calendar/unselect_slot";
            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentCustomCalenderSlotViewScreen.this, nameValuePairList, UNSELECT_SLOT, ParentCustomCalenderSlotViewScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentCustomCalenderSlotViewScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    public void submitSlots(){
        if (Utilities.isNetworkAvailable(ParentCustomCalenderSlotViewScreen.this)) {

            final List<NameValuePair> nameValuePairList = new ArrayList<>();
            nameValuePairList.add(new BasicNameValuePair("user_id", loggedInUser.getId()));
            nameValuePairList.add(new BasicNameValuePair("academy_id", loggedInUser.getAcademiesId()));

            final ArrayList<String> headers = new ArrayList<>();
            headers.add("X-access-uid:" + loggedInUser.getId());
            headers.add("X-access-token:" + loggedInUser.getToken());

            String webServiceUrl = Utilities.BASE_URL + "front_calendar/booked_pitches";
            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ParentCustomCalenderSlotViewScreen.this, nameValuePairList, SUBMIT_SLOTS, ParentCustomCalenderSlotViewScreen.this, headers);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(ParentCustomCalenderSlotViewScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

//    public class CustomListAdaptersItems  extends BaseAdapter {
//        SharedPreferences sharedPreferences;
//        UserBean loggedInUser;
//        Context context;
//        ArrayList<String> coloumnArrayList;
//        LayoutInflater layoutInflater;
//
//        Typeface helvetica;
//        Typeface linoType;
//        String dateStr;
//        String pitchID;
//
//        public CustomListAdaptersItems(Context context, ArrayList<String> coloumnArrayList, String pitchID, String dateStr){
//            this.context = context;
//            this.coloumnArrayList = coloumnArrayList;
//            this.dateStr = dateStr;
//            this.pitchID = pitchID;
//            layoutInflater = LayoutInflater.from(context);
//
//
//            helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
//            linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");
//
//            sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//            Gson gson = new Gson();
//            String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
//            if (jsonLoggedInUser != null) {
//                loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
//            }
//
//        }
//
//        @Override
//        public int getCount() {
//            return coloumnArrayList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return coloumnArrayList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            if(position==0){
//                convertView = layoutInflater.inflate(R.layout.list_adapters_title_item, null);
//
//                final TextView categoryname = (TextView) convertView.findViewById(R.id.categoryname);
//                categoryname.setTypeface(helvetica);
//
//                final String childChooseMusicCategoryBean = coloumnArrayList.get(position);
//                categoryname.setText(childChooseMusicCategoryBean);
//
//            }else{
//                convertView = layoutInflater.inflate(R.layout.list_adapters_item, null);
//
//                final TextView categoryname = (TextView) convertView.findViewById(R.id.categoryname);
//                categoryname.setTypeface(helvetica);
//
//                final String childChooseMusicCategoryBean = coloumnArrayList.get(position);
//                categoryname.setText(childChooseMusicCategoryBean);
//
//
//
//                categoryname.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
//            }
//
//
//            return convertView;
//        }
//    }

//    public class CustomListAdapters  extends BaseAdapter {
//        SharedPreferences sharedPreferences;
//        UserBean loggedInUser;
//        Context context;
//        ArrayList<String> coloumnArrayList;
//        LayoutInflater layoutInflater;
//
//        Typeface helvetica;
//        Typeface linoType;
//
//        public CustomListAdapters(Context context, ArrayList<String> coloumnArrayList){
//            this.context = context;
//            this.coloumnArrayList = coloumnArrayList;
//            layoutInflater = LayoutInflater.from(context);
//
//            helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
//            linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");
//
//            sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//            Gson gson = new Gson();
//            String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
//            if (jsonLoggedInUser != null) {
//                loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
//            }
//
//        }
//
//        @Override
//        public int getCount() {
//            return coloumnArrayList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return coloumnArrayList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            convertView = layoutInflater.inflate(R.layout.list_adapters_title_item, null);
//            final TextView categoryname = (TextView) convertView.findViewById(R.id.categoryname);
//            categoryname.setTypeface(helvetica);
//
//            final String childChooseMusicCategoryBean = coloumnArrayList.get(position);
//            categoryname.setText(childChooseMusicCategoryBean);
//
////            categoryname.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    Toast.makeText(context, ""+categoryname.getText().toString(), Toast.LENGTH_SHORT).show();
////                }
////            });
//
//            return convertView;
//        }
//    }
}