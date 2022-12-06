package com.dunvant.application.parent.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dunvant.application.R;
import com.dunvant.application.beans.PitchBookingDateBean;
import com.dunvant.application.beans.UserBean;
import com.dunvant.application.utils.Utilities;
import com.dunvant.application.webservices.IWebServiceCallback;

import java.util.ArrayList;

public class ParentPitchBookingDatesNewForCalendarViewAdapter  extends BaseAdapter implements IWebServiceCallback {

    Context context;
    ArrayList<PitchBookingDateBean> bookingDatesList;
    LayoutInflater layoutInflater;
    ParentPitchSummaryNewForCalendarViewAdapter parentPitchSummaryAdapter;
    ListView pitchesDetailListView;
    Typeface helvetica;
    Typeface linoType;

    ListView bookingDatesListView;
    String pitchId;

    SharedPreferences sharedPreferences;
    UserBean loggedInUser;

//    int removeFromPosition;
//    private final String REMOVE_SLOT = "REMOVE_SLOT";

    public ParentPitchBookingDatesNewForCalendarViewAdapter(Context context, ArrayList<PitchBookingDateBean> bookingDatesList, ParentPitchSummaryNewForCalendarViewAdapter parentPitchSummaryAdapter, ListView pitchesDetailListView, ListView bookingDatesListView, String pitchId){
        this.context = context;
        this.bookingDatesList = bookingDatesList;
        this.layoutInflater = LayoutInflater.from(context);
        this.parentPitchSummaryAdapter = parentPitchSummaryAdapter;
        this.pitchesDetailListView = pitchesDetailListView;
        this.pitchId = pitchId;
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

        this.bookingDatesListView = bookingDatesListView;

        sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
        if(jsonLoggedInUser != null) {
            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
        }
    }

    @Override
    public int getCount() {
        return bookingDatesList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookingDatesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_pitch_booking_date_item, null);

        TextView lblDate = (TextView) convertView.findViewById(R.id.lblDate);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView lblTime = (TextView) convertView.findViewById(R.id.lblTime);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView lblDuration = (TextView) convertView.findViewById(R.id.lblDuration);
        TextView duration = (TextView) convertView.findViewById(R.id.duration);
        ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        TextView price = (TextView) convertView.findViewById(R.id.price);

        final PitchBookingDateBean bookingDateBean = bookingDatesList.get(position);
        date.setText(bookingDateBean.getShowBookingDate());
        time.setText(bookingDateBean.getTime());
        duration.setText(bookingDateBean.getInterval()+" hours");
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);

        price.setText(bookingDateBean.getAmount()+" "+academy_currency);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int fiftyPercent = (screenWidth * 50) / 100;
        float textWidthForTitle = date.getPaint().measureText(date.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / fiftyPercent) + 1;
        date.setLines(numberOfLines);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.parent_dialog_delete);

                TextView yes = (TextView) dialog.findViewById(R.id.yes);
                TextView no = (TextView) dialog.findViewById(R.id.no);

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                        parentPitchSummaryAdapter.deleteSlot(pitchId, bookingDatesList.get(position));
                    }
                });

                dialog.show();
            }
        });

        lblDate.setTypeface(helvetica);
        date.setTypeface(helvetica);
        lblTime.setTypeface(helvetica);
        time.setTypeface(helvetica);
        lblDuration.setTypeface(helvetica);
        duration.setTypeface(helvetica);
        price.setTypeface(helvetica);

        return convertView;
    }

    @Override
    public void onWebServiceResponse(String response, String tag) {
        /*switch (tag){
            case REMOVE_SLOT:

                if(response == null) {
                    Toast.makeText(context, "Could not connect to server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(response);

                        boolean status = responseObject.getBoolean("status");
                        String message = responseObject.getString("message");

                        if(status) {
                            bookingDatesList.remove(removeFromPosition);
                            notifyDataSetChanged();
                            parentPitchSummaryAdapter.notifyDataSetChanged();
                            Utilities.setListViewHeightBasedOnChildren(bookingDatesListView);
                            Utilities.setListViewHeightBasedOnChildren(pitchesDetailListView);
                        }

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }*/
    }
}