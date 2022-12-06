package com.dunvant.application.calendarquickstart;

import android.os.AsyncTask;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.dunvant.application.beans.CalendarListBean;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An asynchronous task that handles the Google Calendar API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */

/**
 * Created by miguel on 5/29/15.
 */

public class ApiAsyncTask extends AsyncTask<Void, Void, Void> {
    private MainActivity mActivity;

    /**
     * Constructor.
     * @param activity MainActivity that spawned this task.
     */
    ApiAsyncTask(MainActivity activity) {
        this.mActivity = activity;
    }

    /**
     * Background task to call Google Calendar API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            mActivity.clearResultsText();
            mActivity.updateResultsText(getDataFromApi());

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
            mActivity.showGooglePlayServicesAvailabilityErrorDialog(
                    availabilityException.getConnectionStatusCode());

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            mActivity.startActivityForResult(
                    userRecoverableException.getIntent(),
                    MainActivity.REQUEST_AUTHORIZATION);

        } catch (IOException e) {
            System.out.println("MEssageHere::"+e.getMessage());
            mActivity.updateStatus("The following error occurred: " +
                    e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     * @return List of Strings describing returned events.
     * @throws IOException
     */
    private ArrayList<CalendarListBean> getDataFromApi() throws IOException, ParseException {
        // List the next 200 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        ArrayList<CalendarListBean> eventStrings = new ArrayList<CalendarListBean>();
        Events events = mActivity.mService.events().list("primary")
                .setMaxResults(200)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            DateTime end = event.getEnd().getDateTime();
            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
            }
            if (end == null) {
                // All-day events don't have start times, so just use
                // the start date.
                end = event.getEnd().getDate();
            }

            System.out.println("StatTime:: "+start);

            if(event.getOrganizer().containsValue("pankpv17@gmail.com")){
                CalendarListBean calendarListBean = new CalendarListBean();
                calendarListBean.setOragniserName(""+event.getOrganizer());
                calendarListBean.setDateTimeFormatted("");
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    Date date = sdf.parse(""+start);
                    long millis = date.getTime();
                    calendarListBean.setDateTimeMilli(""+millis);


                    calendarListBean.setStartTime(""+event.getStart());
                    calendarListBean.setEndTime(""+event.getEnd());
                    calendarListBean.setEventDetail(""+event.getSummary());
                    eventStrings.add(calendarListBean);

                }catch (Exception e){
                    e.printStackTrace();

                    try{
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = sdf.parse(""+start);
                        long millis = date.getTime();
                        calendarListBean.setDateTimeMilli(""+millis);


                        calendarListBean.setStartTime(""+event.getStart());
                        calendarListBean.setEndTime(""+event.getEnd());
                        calendarListBean.setEventDetail(""+event.getSummary());
                        eventStrings.add(calendarListBean);
                    }catch (Exception ee){
                        ee.printStackTrace();
                    }
                }


//                eventStrings.add("OrgName: "+event.getOrganizer()+" - "+String.format("%s %s", " Summary:"+event.getSummary(), " StartTime:"+start+" EndTime:"+end));
            }

        }
        return eventStrings;
    }

}