package com.dunvant.application.startModule.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.CovidQuestionBean;

import java.util.ArrayList;
import java.util.Calendar;

public class StartModuleDailyWellnessAnswersAdapter extends BaseAdapter {

    Context context;
    ArrayList<CovidQuestionBean> startModuleResourcebeanArrayList;

    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public StartModuleDailyWellnessAnswersAdapter(Context context, ArrayList<CovidQuestionBean> startModuleResourcebeanArrayList) {
        this.context = context;
        this.startModuleResourcebeanArrayList = startModuleResourcebeanArrayList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(), "fonts/LinotypeOrdinarRegular.ttf");

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .showImageForEmptyUri(R.drawable.placeholder)
                .showImageOnFail(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(1000))
                .build();

    }

    @Override
    public int getCount() {
        return startModuleResourcebeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return startModuleResourcebeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.start_module_daily_answers_adapter, null);

        TextView question = (TextView) convertView.findViewById(R.id.question);
        final RadioButton one = (RadioButton) convertView.findViewById(R.id.one);
        final RadioButton two = (RadioButton) convertView.findViewById(R.id.two);
        final RadioButton three = (RadioButton) convertView.findViewById(R.id.three);
        final RadioButton four = (RadioButton) convertView.findViewById(R.id.four);
        final RadioButton five = (RadioButton) convertView.findViewById(R.id.five);
        final RadioButton six = (RadioButton) convertView.findViewById(R.id.six);
        LinearLayout linear1 = convertView.findViewById(R.id.linear1);
        LinearLayout linear2 = convertView.findViewById(R.id.linear2);
        final TextView timeBT = convertView.findViewById(R.id.timeBT);

        final CovidQuestionBean covidQuestionBean = startModuleResourcebeanArrayList.get(position);

        question.setText(covidQuestionBean.getQuestion());

        if(position==0 || position==1){
            linear1.setVisibility(View.VISIBLE);
            linear2.setVisibility(View.GONE);
        }else{
            linear1.setVisibility(View.GONE);
            linear2.setVisibility(View.VISIBLE);
        }

        if(position==7){
            six.setVisibility(View.VISIBLE);
        }else{
            six.setVisibility(View.INVISIBLE);
        }


        if(position==0 || position==1){
            if(covidQuestionBean.getChecked().equalsIgnoreCase("")){
                timeBT.setText("Choose Time");
            }else{
                timeBT.setText(covidQuestionBean.getChecked());
            }

        }else{
            if(covidQuestionBean.getChecked().equalsIgnoreCase("1")){
                one.setChecked(true);
                two.setChecked(false);
                three.setChecked(false);
                four.setChecked(false);
                five.setChecked(false);
                six.setChecked(false);
            }

            if(covidQuestionBean.getChecked().equalsIgnoreCase("2")){
                one.setChecked(false);
                two.setChecked(true);
                three.setChecked(false);
                four.setChecked(false);
                five.setChecked(false);
                six.setChecked(false);
            }

            if(covidQuestionBean.getChecked().equalsIgnoreCase("3")){
                one.setChecked(false);
                two.setChecked(false);
                three.setChecked(true);
                four.setChecked(false);
                five.setChecked(false);
                six.setChecked(false);
            }

            if(covidQuestionBean.getChecked().equalsIgnoreCase("4")){
                one.setChecked(false);
                two.setChecked(false);
                three.setChecked(false);
                four.setChecked(true);
                five.setChecked(false);
                six.setChecked(false);
            }

            if(covidQuestionBean.getChecked().equalsIgnoreCase("5")){
                one.setChecked(false);
                two.setChecked(false);
                three.setChecked(false);
                four.setChecked(false);
                five.setChecked(true);
                six.setChecked(false);
            }

            if(covidQuestionBean.getChecked().equalsIgnoreCase("6")){
                one.setChecked(false);
                two.setChecked(false);
                three.setChecked(false);
                four.setChecked(false);
                five.setChecked(false);
                six.setChecked(true);
            }

        }


        timeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        covidQuestionBean.setChecked( selectedHour + ":" + selectedMinute);
                        notifyDataSetChanged();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Choose Time");
                mTimePicker.show();


            }
        });

        one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                covidQuestionBean.setChecked("1");
                notifyDataSetChanged();
            }
        });

        two.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                covidQuestionBean.setChecked("2");
                notifyDataSetChanged();
            }
        });

        three.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                covidQuestionBean.setChecked("3");
                notifyDataSetChanged();
            }
        });

        four.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                covidQuestionBean.setChecked("4");
                notifyDataSetChanged();
            }
        });

        five.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                covidQuestionBean.setChecked("5");
                notifyDataSetChanged();
            }
        });

        six.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                covidQuestionBean.setChecked("6");
                notifyDataSetChanged();
            }
        });



        return convertView;
    }
}