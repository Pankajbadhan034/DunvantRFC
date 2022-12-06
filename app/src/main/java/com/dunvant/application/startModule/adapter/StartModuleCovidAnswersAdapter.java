package com.dunvant.application.startModule.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.CovidQuestionBean;

import java.util.ArrayList;

public class StartModuleCovidAnswersAdapter extends BaseAdapter {

    Context context;
    ArrayList<CovidQuestionBean> startModuleResourcebeanArrayList;

    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public StartModuleCovidAnswersAdapter(Context context, ArrayList<CovidQuestionBean> startModuleResourcebeanArrayList) {
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
        convertView = layoutInflater.inflate(R.layout.start_module_covid_answers_adapter, null);

        TextView question = (TextView) convertView.findViewById(R.id.question);
        final RadioButton yes = (RadioButton) convertView.findViewById(R.id.yes);
        final RadioButton no = (RadioButton) convertView.findViewById(R.id.no);
        final TextView answer = convertView.findViewById(R.id.answer);
        answer.setVisibility(View.GONE);
        yes.setVisibility(View.VISIBLE);
        no.setVisibility(View.VISIBLE);

        final CovidQuestionBean covidQuestionBean = startModuleResourcebeanArrayList.get(position);

        question.setText(covidQuestionBean.getQuestion());

        if(covidQuestionBean.getChecked().equalsIgnoreCase("Normal")){
            yes.setChecked(true);
            no.setChecked(false);
        }

        if(covidQuestionBean.getChecked().equalsIgnoreCase("Not Right")){
            no.setChecked(true);
            yes.setChecked(false);
        }

        if(covidQuestionBean.getChecked().equalsIgnoreCase("yes")){
            yes.setChecked(true);
            no.setChecked(false);
        }

        if(covidQuestionBean.getChecked().equalsIgnoreCase("no")){
            no.setChecked(true);
            yes.setChecked(false);
        }


        if(position==startModuleResourcebeanArrayList.size()-1){
            no.setVisibility(View.GONE);
        }


        if(position==0){
            yes.setText("Normal");
            no.setText("Not Right");
        }else{
            yes.setText("Yes");
            no.setText("No");
        }


        yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(position==0){
                    covidQuestionBean.setChecked("Normal");
                }else{
                    covidQuestionBean.setChecked("yes");
                }

                notifyDataSetChanged();
            }
        });

        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(position==0){
                    covidQuestionBean.setChecked("Not Right");
                }else{
                    covidQuestionBean.setChecked("no");
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}