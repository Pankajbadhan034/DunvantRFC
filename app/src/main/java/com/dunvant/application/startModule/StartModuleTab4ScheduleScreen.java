package com.dunvant.application.startModule;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.events.calendar.views.EventsCalendar;
import com.dunvant.application.R;
import com.dunvant.application.calendarquickstart.MainActivity;
import com.skyhope.eventcalenderlibrary.listener.CalenderDayClickListener;
import com.skyhope.eventcalenderlibrary.model.DayContainerModel;
import com.skyhope.eventcalenderlibrary.model.Event;

//import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class StartModuleTab4ScheduleScreen extends AppCompatActivity  {
    public static CalendarEvent calenderEvent;
    EventsCalendar eventsCalendar;
    TextView dateTV;
    TextView eventTV;
    ImageView backButton;
  //  ListView listView;
    public static ArrayList<String> dataArrayList = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_module_tab4_schedule_screen);
        backButton = findViewById(R.id.backButton);
       // listView = findViewById(R.id.listView);
         calenderEvent = findViewById(R.id.calender_event);
         dateTV = findViewById(R.id.dateTV);
         eventTV = findViewById(R.id.eventTV);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String currentDate = sdf.format(new Date());

        dateTV.setText("DATE: "+currentDate);
        eventTV.setText("EVENT: N/A");

        Button buttonClick = findViewById(R.id.buttonClick);
        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(StartModuleTab4ScheduleScreen.this, MainActivity.class);
                startActivity(obj);
            }
        });

        calenderEvent.initCalderItemClickCallback(new CalenderDayClickListener() {
            @Override
            public void onGetDay(DayContainerModel dayContainerModel) {
               // Log.d(TAG, dayContainerModel.getDate());
                try{
//                    System.out.println("HERE::"+dayContainerModel.getEvent().getEventText());
                    dateTV.setText("DATE: "+dayContainerModel.getDate());
                    eventTV.setText("EVENT: "+dayContainerModel.getEvent().getEventText());


                }catch (Exception e){
                    e.printStackTrace();
                    dateTV.setText("DATE: "+dayContainerModel.getDate());
                    eventTV.setText("EVENT: N/A");
                }

            //    Toast.makeText(StartModuleTab4ScheduleScreen.this, ""+dayContainerModel.getDate(), Toast.LENGTH_SHORT).show();
            }
        });


        readCalendarEvent(StartModuleTab4ScheduleScreen.this);
//        Collections.sort(dataArrayList, Collections.<String>reverseOrder());
//        Collections.reverse(dataArrayList);
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, dataArrayList);
//        listView.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        eventsCalendar = findViewById(R.id.eventsCalendar);

        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdfs.parse("2021-05-20"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 40);


        eventsCalendar.setSelectionMode(0) //set mode of Calendar
                .setToday(Calendar.getInstance()) //set today's date [today: Calendar]
                .setMonthRange(Calendar.getInstance(), Calendar.getInstance()) //set starting month [start: Calendar] and ending month [end: Calendar]
                .setWeekStartDay(Calendar.SUNDAY, false) //set start day of the week as you wish [startday: Int, doReset: Boolean]
                .setDateTextFontSize(16f) //set font size for dates
                .setMonthTitleFontSize(16f) //set font size for title of the calendar
                .setWeekHeaderFontSize(16f) //set font size for week names
                //.setCallback(this) //set the callback for EventsCalendar
                .addEvent(c);




    }

    public static void readCalendarEvent(Context context) {

        System.out.println("HERE!!!!");

        dataArrayList.clear();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar/events"), (new String[] { "_id", "title", "organizer", "dtstart", "dtend"}), null, null, null);

        List<GoogleCalendar> gCalendar = new ArrayList<GoogleCalendar>();
        try {

            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    GoogleCalendar googleCalendar = new GoogleCalendar();
                    // event_ID: ID of tabel Event
                    int event_ID = cursor.getInt(0);
                    googleCalendar.setEvent_id(event_ID);
                    // title of Event
                    String title = cursor.getString(1);
                    googleCalendar.setTitle(title);
                    String mOrganizer = cursor.getString(2);
                    googleCalendar.setOrganizer(mOrganizer);
                    // Date start of Event
                    String dtStart = cursor.getString(3);
                    googleCalendar.setDtstart(dtStart);
                    // Date end of Event
                    String dtEnd = cursor.getString(4);
                    googleCalendar.setDtend(dtEnd);
                    gCalendar.add(googleCalendar);

                    Date mDate = new Date(cursor.getLong(3));
                    //System.out.println("Current mDate => " + mDate);
                    Date nDate = new Date(cursor.getLong(4));

                    int currentYear = Calendar.getInstance().get(Calendar.YEAR);




//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//                    String formattedDate = df.format(c);
//                    System.out.println("formattedDate " + formattedDate);



                    if(mDate.toString().contains(""+currentYear)){
                        System.out.println("DATE_start:: "+mDate.toString()+" Title:: "+googleCalendar.getTitle());
                        dataArrayList.add("DATE - "+mDate.toString()+"\nEVENT - "+googleCalendar.getTitle());

                        Event event = new Event(cursor.getLong(3) , googleCalendar.getTitle(), Color.parseColor("#000000"));
                        calenderEvent.addEvent(event);

                    }

                    //  System.out.println("CaledarM "+googleCalendar.getTitle()+" name = "+googleCalendar.getOrganizer()+" dateStart = "+googleCalendar.getDtstart()+" Size = " + gCalendar.size());

                }


                //   Collections.sort(dateArrayList, Collections.<Date>reverseOrder());


            }else{
               // System.out.println("HERE2!!!!");
            }
        } catch (AssertionError ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onDayLongPressed(@Nullable Calendar calendar) {
//
//    }
//
//    @Override
//    public void onDaySelected(@Nullable Calendar calendar) {
//        Toast.makeText(this, "here:"+calendar.getTime(), Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onMonthChanged(@Nullable Calendar calendar) {
//
//    }

    public static class GoogleCalendar {

        private int event_id;
        private String title,
                organizer,
                dtstart,
                dtend;

        public GoogleCalendar()
        {
        }

        public int getEvent_id() {
            return event_id;
        }

        public void setEvent_id(int calendar_id) {
            event_id = calendar_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOrganizer() {
            return organizer;
        }

        public void setOrganizer(String description) {
            this.organizer = description;
        }

        public String getDtstart() {
            return dtstart;
        }

        public void setDtstart(String dtstart1) {
            this.dtstart = dtstart1;
        }

        public String getDtend() {
            return dtend;
        }

        public void setDtend(String dtend1) {
            this.dtend = dtend1;
        }
    }

}