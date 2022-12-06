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
import com.dunvant.application.beans.CampDateBean;
import com.dunvant.application.beans.CampSelectedChildBean;
import com.dunvant.application.parent.fragments.ParentBookCampDaysFragment;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentCampChooseDatesListingAdapter extends BaseAdapter {

    Context context;
    ArrayList<CampSelectedChildBean> childrenListing;
    ArrayList<CampDateBean> availableDatesListing;
    ParentBookCampDaysFragment parentBookCampDaysFragment;
    LayoutInflater layoutInflater;
    ListView chooseDaysListView;

    Typeface helvetica;
    Typeface linoType;

    public ParentCampChooseDatesListingAdapter(Context context, ArrayList<CampSelectedChildBean> childrenListing, ArrayList<CampDateBean> availableDatesListing, ListView chooseDaysListView, ParentBookCampDaysFragment parentBookCampDaysFragment) {
        this.context = context;
        this.childrenListing = childrenListing;
        this.availableDatesListing = availableDatesListing;
        this.chooseDaysListView = chooseDaysListView;
        this.parentBookCampDaysFragment = parentBookCampDaysFragment;
        this.layoutInflater = LayoutInflater.from(context);

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_camp_choose_date_item, null);

        TextView forName = (TextView) convertView.findViewById(R.id.forName);
        TextView lblChooseDates = (TextView) convertView.findViewById(R.id.lblChooseDates);
        TextView chooseDate = (TextView) convertView.findViewById(R.id.chooseDate);
        final GridView selectedDatesGridView = (GridView) convertView.findViewById(R.id.selectedDatesGridView);

        forName.setTypeface(helvetica);
        lblChooseDates.setTypeface(helvetica);
        chooseDate.setTypeface(helvetica);

        final CampSelectedChildBean childBean = childrenListing.get(position);
        forName.setText("For " + childBean.getChildBean().getFullName());

        final SelectedDatesAdapter selectedDatesAdapter = new SelectedDatesAdapter(childBean.getSelectedDatesList());
        selectedDatesGridView.setAdapter(selectedDatesAdapter);

        if(!childBean.getSelectedDatesList().isEmpty()){
            Utilities.setGridViewHeightBasedOnChildren(selectedDatesGridView, 2);
        }

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (CampDateBean campDateBean : availableDatesListing) {
                    campDateBean.setSelected(false);
                }

                for (CampDateBean currentDate : childBean.getSelectedDatesList()) {
                    for (CampDateBean campDateBean : availableDatesListing) {
                        if (currentDate.getDate().equalsIgnoreCase(campDateBean.getDate())) {
                            campDateBean.setSelected(true);
                        }
                    }
                }

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.parent_dialog_camp_select_dates);

                GridView datesGridView = (GridView) dialog.findViewById(R.id.datesGridView);
                Button done = (Button) dialog.findViewById(R.id.done);

                datesGridView.setAdapter(new DatesGridAdapterForPopup());

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        childBean.getSelectedDatesList().clear();

                        for (CampDateBean campDateBean : availableDatesListing) {
                            if (campDateBean.isSelected()) {
                                // check if already exists
                                boolean alreadyExists = false;
                                for (CampDateBean currentDate : childBean.getSelectedDatesList()) {
                                    if (currentDate.getDate().equalsIgnoreCase(campDateBean.getDate())) {
                                        alreadyExists = true;
                                    }
                                }

                                if (!alreadyExists) {
                                    childBean.getSelectedDatesList().add(campDateBean);
                                }
                            }
                        }

                        selectedDatesAdapter.notifyDataSetChanged();
                        if(childBean.getSelectedDatesList().size() != 0) {
                            Utilities.setGridViewHeightBasedOnChildren(selectedDatesGridView, 2);
                            Utilities.setListViewHeightBasedOnChildren(chooseDaysListView);
                        }
                        parentBookCampDaysFragment.calculateCost();
                    }
                });

                dialog.show();
            }
        });

        return convertView;
    }

    class DatesGridAdapterForPopup extends BaseAdapter {

        @Override
        public int getCount() {
            return availableDatesListing.size();
        }

        @Override
        public Object getItem(int position) {
            return availableDatesListing.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.parent_adapter_camp_available_date_item, null);

            final CheckBox dateCheckBox = (CheckBox) convertView.findViewById(R.id.dateCheckBox);
            dateCheckBox.setText(availableDatesListing.get(position).getDate());

            dateCheckBox.setTypeface(helvetica);

            if (availableDatesListing.get(position).isSelected()) {
                dateCheckBox.setChecked(true);
            }

            dateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (availableDatesListing.get(position).getSeats() > 0) {
                            availableDatesListing.get(position).setSelected(true);
                            availableDatesListing.get(position).setSeats(availableDatesListing.get(position).getSeats() - 1);
                        } else {
                            Toast.makeText(context, "No seats left", Toast.LENGTH_SHORT).show();
                            dateCheckBox.setChecked(false);
                        }
                    } else {
                        availableDatesListing.get(position).setSelected(false);
                        availableDatesListing.get(position).setSeats(availableDatesListing.get(position).getSeats() + 1);
                    }
                }
            });

            return convertView;
        }
    }

    class SelectedDatesAdapter extends BaseAdapter {

        ArrayList<CampDateBean> selectedDatesList;

        public SelectedDatesAdapter(ArrayList<CampDateBean> selectedDatesList){
            this.selectedDatesList = selectedDatesList;
        }

        @Override
        public int getCount() {
            return selectedDatesList.size();
        }

        @Override
        public Object getItem(int position) {
            return selectedDatesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.parent_adapter_camp_selected_date_item, null);

            TextView date = (TextView) convertView.findViewById(R.id.date);
            date.setText(selectedDatesList.get(position).getDate());

            date.setTypeface(helvetica);

            return convertView;
        }
    }
}