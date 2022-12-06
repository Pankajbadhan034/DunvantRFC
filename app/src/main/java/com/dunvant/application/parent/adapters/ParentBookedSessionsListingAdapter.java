package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.ChildBean;
import com.dunvant.application.beans.OrderSessionBean;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentBookedSessionsListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<OrderSessionBean> orderSessionList;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentBookedSessionsListingAdapter(Context context, ArrayList<OrderSessionBean> orderSessionList) {
        this.context = context;
        this.orderSessionList = orderSessionList;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return orderSessionList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderSessionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_booked_session_item, null);

        CheckBox isTrial = (CheckBox) convertView.findViewById(R.id.isTrial);
        TextView childrenNames = (TextView) convertView.findViewById(R.id.childrenNames);
        TextView termName = (TextView) convertView.findViewById(R.id.termName);
        TextView timings = (TextView) convertView.findViewById(R.id.timings);
        TextView ageGroup = (TextView) convertView.findViewById(R.id.ageGroup);
        ListView datesListView = (ListView) convertView.findViewById(R.id.datesListView);
//        TextView sessionPayment = (TextView) convertView.findViewById(R.id.sessionPayment);

        isTrial.setTypeface(helvetica);
        childrenNames.setTypeface(helvetica);
        termName.setTypeface(helvetica);
        timings.setTypeface(helvetica);
        ageGroup.setTypeface(helvetica);

        OrderSessionBean sessionBean = orderSessionList.get(position);

        if(sessionBean.getIsTrial().equalsIgnoreCase("1")) {
            isTrial.setChecked(true);
        } else {
            isTrial.setChecked(false);
        }

        String strChildrenNames = "";
        for(ChildBean childBean: sessionBean.getChildrenList()) {
            strChildrenNames += childBean.getFullName()+", ";
        }
        childrenNames.setText(strChildrenNames);

        termName.setText(sessionBean.getTermsName());
        timings.setText(sessionBean.getShowStartTime()+" - "+sessionBean.getShowEndTime());
        ageGroup.setText(sessionBean.getGroupName());
//        sessionPayment.setText(sessionBean.getSessionCost()+" AED");

        datesListView.setAdapter(new ParentBookedSessionDatesAdapter(context, sessionBean.getBookingDetailsList()));
        Utilities.setListViewHeightBasedOnChildren(datesListView);

        return convertView;
    }
}