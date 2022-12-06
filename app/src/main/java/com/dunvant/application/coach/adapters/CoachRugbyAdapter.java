package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.CoachRugbyBean;
import com.dunvant.application.coach.CoachRugbyScreen;

import java.util.ArrayList;

public class CoachRugbyAdapter extends BaseAdapter{

    Context context;
    ArrayList<CoachRugbyBean> coachRugbyBeanArrayList;

    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public CoachRugbyAdapter(Context context, ArrayList<CoachRugbyBean> coachRugbyBeanArrayList){
        this.context = context;
        this.coachRugbyBeanArrayList = coachRugbyBeanArrayList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return coachRugbyBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return coachRugbyBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_rugby_adapter, null);

        RelativeLayout mainRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mainRelativeLayout);
        ImageView docIcon = (ImageView) convertView.findViewById(R.id.docIcon);
        TextView docTitle = (TextView) convertView.findViewById(R.id.docTitle);

        final CoachRugbyBean coachRugbyBean = coachRugbyBeanArrayList.get(position);

        docTitle.setText(coachRugbyBean.getTitle());

        docTitle.setTypeface(linoType);

        if(coachRugbyBean.getExt().equalsIgnoreCase("doc") || coachRugbyBean.getExt().equalsIgnoreCase("docx")){
            docIcon.setBackgroundResource(R.drawable.doc);
        }else if(coachRugbyBean.getExt().equalsIgnoreCase("pdf")){
            docIcon.setBackgroundResource(R.drawable.pdfnew);
        }else if(coachRugbyBean.getExt().equalsIgnoreCase("mp4")){
            docIcon.setBackgroundResource(R.drawable.videonew);
        }else if(coachRugbyBean.getExt().equalsIgnoreCase("png") || coachRugbyBean.getExt().equalsIgnoreCase("jpg") || coachRugbyBean.getExt().equalsIgnoreCase("jpeg")){
            docIcon.setBackgroundResource(R.drawable.image);
        }else if(coachRugbyBean.getExt().equalsIgnoreCase("ppt")){
            docIcon.setBackgroundResource(R.drawable.ppt);
        }else if(coachRugbyBean.getExt().equalsIgnoreCase("xls") || coachRugbyBean.getExt().equalsIgnoreCase("xlsx")){
            docIcon.setBackgroundResource(R.drawable.excel);
        }else if(coachRugbyBean.getExt().equalsIgnoreCase("txt")){
            docIcon.setBackgroundResource(R.drawable.notepad);
        }else{
            docIcon.setBackgroundResource(R.drawable.genericdoc);
        }

        if(position % 2 == 0) {
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            docTitle.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            docTitle.setTextColor(context.getResources().getColor(R.color.white));
        }

        mainRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(context, CoachRugbyScreen.class);
                if(coachRugbyBean.getExt().equalsIgnoreCase("doc") || coachRugbyBean.getExt().equalsIgnoreCase("docx")){
                   obj.putExtra("ext", "webview");
                   obj.putExtra("fileName", coachRugbyBean.getFileName());
                }else if(coachRugbyBean.getExt().equalsIgnoreCase("pdf")){
                    obj.putExtra("ext", "webview");
                    obj.putExtra("fileName", coachRugbyBean.getFileName());
                }else if(coachRugbyBean.getExt().equalsIgnoreCase("mp4")){
                    obj.putExtra("ext", "video");
                    obj.putExtra("fileName", coachRugbyBean.getFileName());;
                }else if(coachRugbyBean.getExt().equalsIgnoreCase("png") || coachRugbyBean.getExt().equalsIgnoreCase("jpg") || coachRugbyBean.getExt().equalsIgnoreCase("jpeg")){
                    obj.putExtra("ext", "image");
                    obj.putExtra("fileName", coachRugbyBean.getFileName());
                }else if(coachRugbyBean.getExt().equalsIgnoreCase("ppt")){
                    obj.putExtra("ext", "webview");
                    obj.putExtra("fileName", coachRugbyBean.getFileName());
                }else if(coachRugbyBean.getExt().equalsIgnoreCase("xls") || coachRugbyBean.getExt().equalsIgnoreCase("xlsx")){
                    obj.putExtra("ext", "webview");
                    obj.putExtra("fileName", coachRugbyBean.getFileName());
                }else if(coachRugbyBean.getExt().equalsIgnoreCase("txt")){
                    obj.putExtra("ext", "webview");
                    obj.putExtra("fileName", coachRugbyBean.getFileName());
                }
                context.startActivity(obj);
            }
        });

        return convertView;
    }
}