package com.dunvant.application.startModule.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.dunvant.application.R;
import com.dunvant.application.beans.CovidViewReportBean;

import java.util.ArrayList;

public class StartModuleCovidAnswersViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<CovidViewReportBean>covidViewReportBeanArrayList;

    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public StartModuleCovidAnswersViewAdapter(Context context, ArrayList<CovidViewReportBean> covidViewReportBeanArrayList) {
        this.context = context;
        this.covidViewReportBeanArrayList = covidViewReportBeanArrayList;
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
        return covidViewReportBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return covidViewReportBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.start_module_covid_answers_adapter, null);

        TextView question = (TextView) convertView.findViewById(R.id.question);
        TextView answer = (TextView) convertView.findViewById(R.id.answer);
        final RadioButton yes = (RadioButton) convertView.findViewById(R.id.yes);
        final RadioButton no = (RadioButton) convertView.findViewById(R.id.no);

        final CovidViewReportBean covidQuestionBean = covidViewReportBeanArrayList.get(position);

        question.setText("Question: "+covidQuestionBean.getQuestion());
        answer.setText("Answer: "+covidQuestionBean.getAnswer());


        return convertView;
    }
}