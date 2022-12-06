package com.dunvant.application.startModule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.R;
import com.dunvant.application.beans.StartModuleResourcebean;
import com.dunvant.application.parent.ParentViewVideoInFullScreen;
import com.dunvant.application.startModule.adapter.StartModuleResourceLinkAdapter;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceAsync;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class StartModuleResourceScreen extends AppCompatActivity implements IWebServiceCallback {
    private final String RESOURCE_PAGES_DATA = "RESOURCE_PAGES_DATA";
    ArrayList<StartModuleResourcebean> startModuleResourcebeanArrayList = new ArrayList<>();
    TextView title;
    ImageView backButton;
    String clickIdStr, titleStr;
    ListView listView;
    String nameDynamic;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_resource_activity);
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        listView = findViewById(R.id.listView);
        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        clickIdStr = getIntent().getStringExtra("id");
        titleStr = getIntent().getStringExtra("title");

        try{
            nameDynamic = getIntent().getStringExtra("nameDynamic");
            System.out.println("HERE:: "+nameDynamic);
            title.setText(nameDynamic);
        }catch (Exception e){
            title.setText(titleStr);
        }


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(titleStr.equalsIgnoreCase("video")){
                    if(startModuleResourcebeanArrayList.get(i).getType().equalsIgnoreCase("1")){
                        // old code
                        // String url = startModuleResourcebeanArrayList.get(i).getUrl()+""+startModuleResourcebeanArrayList.get(i).getVideo();
                        //new code -  changed by Deepak
                        String url = startModuleResourcebeanArrayList.get(i).getImage_url()+""+startModuleResourcebeanArrayList.get(i).getVideo();
                        System.out.println("URLHERE:: "+url);
                        Intent viewImageInFullScreen = new Intent(StartModuleResourceScreen.this, ParentViewVideoInFullScreen.class);
                        viewImageInFullScreen.putExtra("videoUrl", url);
                        startActivity(viewImageInFullScreen);
                    }else{
                        String url = startModuleResourcebeanArrayList.get(i).getUrl();
                        try{
                            Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                            startActivity(browserIntent);
                        }catch (Exception e){
                            Toast.makeText(StartModuleResourceScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }else if(titleStr.equalsIgnoreCase("analysis")){

                    Intent viewImageInFullScreen = new Intent(StartModuleResourceScreen.this, StartModuleAnalysisDescriptionScreen.class);
                    viewImageInFullScreen.putExtra("desc", startModuleResourcebeanArrayList.get(i).getDescription());
                    startActivity(viewImageInFullScreen);

//                    final Dialog dialog = new Dialog(StartModuleResourceScreen.this);
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setContentView(R.layout.start_module_dialog_analysis);
//
//                    TextView text =  dialog.findViewById(R.id.text);
//                    text.setText(Html.fromHtml(startModuleResourcebeanArrayList.get(i).getDescription()));
//                    dialog.show();


                }else{
                    String url = startModuleResourcebeanArrayList.get(i).getUrl();
                    try{
                        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                        startActivity(browserIntent);
                    }catch (Exception e){
                        Toast.makeText(StartModuleResourceScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }



//                    Intent obj = new Intent(StartModuleResourceScreen.this, StartModuleWebViewScreen.class);
//                    obj.putExtra("link", url);
//                    startActivity(obj);
                }


            }
        });

        resourcePages();
    }

    private void resourcePages() {
        if (Utilities.isNetworkAvailable(StartModuleResourceScreen.this)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();

            String academy_id_stored = sharedPreferences.getString("academy_id_stored", "");
            nameValuePairList.add(new BasicNameValuePair("academy_id", academy_id_stored));
            nameValuePairList.add(new BasicNameValuePair("page_cat", clickIdStr));

            String webServiceUrl = Utilities.BASE_URL + "osp_aca/resource_pages";

            PostWebServiceAsync postWebServiceAsync = new PostWebServiceAsync(StartModuleResourceScreen.this, nameValuePairList, RESOURCE_PAGES_DATA, StartModuleResourceScreen.this);
            postWebServiceAsync.execute(webServiceUrl);

        } else {
            Toast.makeText(StartModuleResourceScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case RESOURCE_PAGES_DATA:

                if (response == null) {
                    Toast.makeText(StartModuleResourceScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");
                        startModuleResourcebeanArrayList.clear();
                        if (status) {
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                StartModuleResourcebean startModuleResourcebean = new StartModuleResourcebean();
                                startModuleResourcebean.setImage_url(jsonObject.getString("image_url"));
                                startModuleResourcebean.setVideo_thumbnail(jsonObject.getString("video_thumbnail"));
                                startModuleResourcebean.setTitle(jsonObject.getString("title"));
                                startModuleResourcebean.setDescription(jsonObject.getString("description"));
                                startModuleResourcebean.setPage_cat(jsonObject.getString("page_cat"));
                                startModuleResourcebean.setUrl(jsonObject.getString("url"));
                                startModuleResourcebean.setVideo(jsonObject.getString("vedio"));
                                startModuleResourcebean.setDate_formatted(jsonObject.getString("date_formatted"));
                                startModuleResourcebean.setType(jsonObject.getString("video_type"));

                                startModuleResourcebeanArrayList.add(startModuleResourcebean);
                            }

                            StartModuleResourceLinkAdapter parentOnlineShoppingAdapter = new StartModuleResourceLinkAdapter(StartModuleResourceScreen.this, startModuleResourcebeanArrayList, titleStr);
                            listView.setAdapter(parentOnlineShoppingAdapter);

                        } else {
                            Toast.makeText(StartModuleResourceScreen.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StartModuleResourceScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}