package com.dunvant.application.coach.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.DocumentBean;

import java.util.ArrayList;

public class CoachReceivedDocumentsListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<DocumentBean> documentsList;

    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;

    public CoachReceivedDocumentsListingAdapter(Context context, ArrayList<DocumentBean> documentsList){
        this.context = context;
        this.documentsList = documentsList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return documentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return documentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.coach_adapter_received_document_item, null);

        RelativeLayout mainRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mainRelativeLayout);
        ImageView docIcon = (ImageView) convertView.findViewById(R.id.docIcon);
        TextView docTitle = (TextView) convertView.findViewById(R.id.docTitle);
        TextView comments = (TextView) convertView.findViewById(R.id.comments);

        final DocumentBean documentBean = documentsList.get(position);

        docTitle.setText(documentBean.getTitle());
        comments.setText(documentBean.getComments());

        docTitle.setTypeface(linoType);
        comments.setTypeface(helvetica);

        if(position % 2 == 0) {
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
            docTitle.setTextColor(context.getResources().getColor(R.color.white));
            comments.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            mainRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            docTitle.setTextColor(context.getResources().getColor(R.color.black));
            comments.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }
}