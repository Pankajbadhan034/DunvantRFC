package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.AgeGroupAttendanceBean;
import com.dunvant.application.beans.ScoreDatesCoachBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;
import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CoachScoreDatesAdapter  extends BaseAdapter implements IWebServiceCallback {
    private final String SHARE_SCORE = "SHARE_SCORE";
    SharedPreferences sharedPreferences;
    UserBean loggedInUser;
    Context context;
    ArrayList<ScoreDatesCoachBean> scoreDatesCoachBeanArrayList;
    String galleryPath;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    String childId;
    String sessionId;

    Typeface helvetica;
    Typeface linoType;

    AgeGroupAttendanceBean clickedOnAgeGroup;

    public CoachScoreDatesAdapter(Context context, ArrayList<ScoreDatesCoachBean> scoreDatesCoachBeanArrayList, String childId, String sessionId){
        this.context = context;
        this.scoreDatesCoachBeanArrayList = scoreDatesCoachBeanArrayList;
        this.childId = childId;
        this.sessionId = sessionId;
        layoutInflater = LayoutInflater.from(context);

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }

    }

    @Override
    public int getCount() {
        return scoreDatesCoachBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return scoreDatesCoachBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_score_dates_item, null);
        final TextView date = convertView.findViewById(R.id.date);
        final TextView shareReport = convertView.findViewById(R.id.shareReport);

        date.setTypeface(helvetica);
        shareReport.setTypeface(helvetica);

        final ScoreDatesCoachBean scoreDatesCoachBean = scoreDatesCoachBeanArrayList.get(position);
        date.setText(scoreDatesCoachBean.getSessiondate());

        shareReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utilities.isNetworkAvailable(context)) {

                    List<NameValuePair> nameValuePairList = new ArrayList<>();
                    nameValuePairList.add(new BasicNameValuePair("sessions_id", sessionId));
                    nameValuePairList.add(new BasicNameValuePair("child_id", childId));
                    nameValuePairList.add(new BasicNameValuePair("report_date", scoreDatesCoachBean.getSessiondate()));

                    String webServiceUrl = Utilities.BASE_URL + "coach/share_score";

                    ArrayList<String> headers = new ArrayList<>();
                    headers.add("X-access-uid:"+loggedInUser.getId());
                    headers.add("X-access-token:"+loggedInUser.getToken());

                    PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(context, nameValuePairList, SHARE_SCORE, CoachScoreDatesAdapter.this, headers);
                    postWebServiceAsync.execute(webServiceUrl);

                } else {
                    Toast.makeText(context, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        });


        return convertView;
    }


    @Override
    public void onWebServiceResponse(String response, String tag) {
        switch (tag) {
            case SHARE_SCORE:
                if(response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
