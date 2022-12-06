package com.dunvant.application.parent.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dunvant.application.R;
import com.dunvant.application.beans.CampSelectedChildBean;
import com.dunvant.application.beans.CampWeekBean;
import com.dunvant.application.parent.fragments.ParentBookCampWeeksFragment;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentCampChooseWeeksListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<CampSelectedChildBean> childrenListing;
    ArrayList<CampWeekBean> availableWeeksListing;
    LayoutInflater layoutInflater;
    ListView chooseWeeksListView;
    ParentBookCampWeeksFragment parentBookCampWeeksFragment;

    Typeface helvetica;
    Typeface linoType;

    public ParentCampChooseWeeksListingAdapter(Context context, ArrayList<CampSelectedChildBean> childrenListing, ArrayList<CampWeekBean> availableWeeksListing, ListView chooseWeeksListView, ParentBookCampWeeksFragment parentBookCampWeeksFragment){
        this.context = context;
        this.childrenListing = childrenListing;
        this.availableWeeksListing = availableWeeksListing;
        this.chooseWeeksListView = chooseWeeksListView;
        this.parentBookCampWeeksFragment = parentBookCampWeeksFragment;
        layoutInflater = LayoutInflater.from(context);

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return childrenListing.size();
    }

    @Override
    public Object getItem(int position) {
        return childrenListing.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_camp_choose_week_item, null);

        TextView forName = (TextView) convertView.findViewById(R.id.forName);
        TextView lblChooseWeeks = (TextView) convertView.findViewById(R.id.lblChooseWeeks);
        TextView chooseWeek = (TextView) convertView.findViewById(R.id.chooseWeek);
        final GridView selectedWeeksGridView = (GridView) convertView.findViewById(R.id.selectedWeeksGridView);

        forName.setTypeface(helvetica);
        lblChooseWeeks.setTypeface(helvetica);
        chooseWeek.setTypeface(helvetica);

        final CampSelectedChildBean childBean = childrenListing.get(position);
        forName.setText("For " + childBean.getChildBean().getFullName());

        final SelectedWeeksAdapter selectedWeeksAdapter = new SelectedWeeksAdapter(childBean.getSelectedWeeksList());
        selectedWeeksGridView.setAdapter(selectedWeeksAdapter);

        if(!childBean.getSelectedWeeksList().isEmpty()){
            Utilities.setGridViewHeightBasedOnChildren(selectedWeeksGridView, 2);
        }

        chooseWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (CampWeekBean campWeekBean : availableWeeksListing) {
                    campWeekBean.setSelected(false);
                }

                for (CampWeekBean currentWeek : childBean.getSelectedWeeksList()) {
                    for (CampWeekBean campWeekBean : availableWeeksListing) {
                        if (currentWeek.getWeekName().equalsIgnoreCase(campWeekBean.getWeekName())) {
                            campWeekBean.setSelected(true);
                        }
                    }
                }

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.parent_dialog_camp_select_weeks);

                GridView weeksGridView = (GridView) dialog.findViewById(R.id.weeksGridView);
                Button done = (Button) dialog.findViewById(R.id.done);

                weeksGridView.setAdapter(new WeeksGridAdapterForPopup());

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        childBean.getSelectedWeeksList().clear();

                        for (CampWeekBean campWeekBean : availableWeeksListing) {
                            if (campWeekBean.isSelected()) {
                                // check if already exists
                                boolean alreadyExists = false;
                                for (CampWeekBean currentWeek : childBean.getSelectedWeeksList()) {
                                    if (currentWeek.getWeekName().equalsIgnoreCase(campWeekBean.getWeekName())) {
                                        alreadyExists = true;
                                    }
                                }

                                if (!alreadyExists) {
                                    childBean.getSelectedWeeksList().add(campWeekBean);
                                }
                            }
                        }

                        selectedWeeksAdapter.notifyDataSetChanged();
                        if(childBean.getSelectedWeeksList().size() != 0) {
                            Utilities.setGridViewHeightBasedOnChildren(selectedWeeksGridView, 2);
                            Utilities.setListViewHeightBasedOnChildren(chooseWeeksListView);
                        }
                        parentBookCampWeeksFragment.calculateCost();
                    }
                });

                dialog.show();
            }
        });

        return convertView;
    }

    class WeeksGridAdapterForPopup extends BaseAdapter {

        @Override
        public int getCount() {
            return availableWeeksListing.size();
        }

        @Override
        public Object getItem(int position) {
            return availableWeeksListing.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.parent_adapter_camp_available_week_item, null);

            final CheckBox weekCheckBox = (CheckBox) convertView.findViewById(R.id.weekCheckBox);
            weekCheckBox.setText(availableWeeksListing.get(position).getWeekName());

            weekCheckBox.setTypeface(helvetica);

            if (availableWeeksListing.get(position).isSelected()) {
                weekCheckBox.setChecked(true);
            }

            weekCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (availableWeeksListing.get(position).getSeats() > 0) {
                            availableWeeksListing.get(position).setSelected(true);
                            availableWeeksListing.get(position).setSeats(availableWeeksListing.get(position).getSeats() - 1);
                        } else {
                            Toast.makeText(context, "No seats left", Toast.LENGTH_SHORT).show();
                            weekCheckBox.setChecked(false);
                        }
                    } else {
                        availableWeeksListing.get(position).setSelected(false);
                        availableWeeksListing.get(position).setSeats(availableWeeksListing.get(position).getSeats() + 1);
                    }
                }
            });

            return convertView;
        }
    }

    class SelectedWeeksAdapter extends BaseAdapter {

        ArrayList<CampWeekBean> selectedWeeksList;

        public SelectedWeeksAdapter(ArrayList<CampWeekBean> selectedWeeksList){
            this.selectedWeeksList = selectedWeeksList;
        }

        @Override
        public int getCount() {
            return selectedWeeksList.size();
        }

        @Override
        public Object getItem(int position) {
            return selectedWeeksList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.parent_adapter_camp_selected_week_item, null);

            TextView week = (TextView) convertView.findViewById(R.id.week);
            week.setText(selectedWeeksList.get(position).getWeekName());

            week.setTypeface(helvetica);

            return convertView;
        }
    }
}