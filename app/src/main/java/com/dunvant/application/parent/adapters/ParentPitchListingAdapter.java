package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dunvant.application.R;
import com.dunvant.application.beans.FacilityBean;
import com.dunvant.application.beans.PitchBean;
import com.dunvant.application.parent.ParentBookPitchScreen;
import com.dunvant.application.utils.Utilities;

import java.util.ArrayList;

public class ParentPitchListingAdapter extends BaseAdapter{

    Context context;
    ArrayList<PitchBean> pitchesList;
    FacilityBean clickedOnFacility;
    LayoutInflater layoutInflater;
    Typeface helvetica;
    Typeface linoType;

    public ParentPitchListingAdapter(Context context, ArrayList<PitchBean> pitchesList, FacilityBean clickedOnFacility) {
        this.context = context;
        this.pitchesList = pitchesList;
        this.clickedOnFacility = clickedOnFacility;
        layoutInflater = LayoutInflater.from(context);
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return pitchesList.size();
    }

    @Override
    public Object getItem(int position) {
        return pitchesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.parent_adapter_pitch_item, null);

        TextView pitchName = convertView.findViewById(R.id.pitchName);

        TextView lblSlotOne = convertView.findViewById(R.id.lblSlotOne);
        TextView lblSlotTwo = convertView.findViewById(R.id.lblSlotTwo);
        TextView lblSlotThree = convertView.findViewById(R.id.lblSlotThree);

        LinearLayout simplePitchLinear = convertView.findViewById(R.id.simplePitchLinear);
        TextView simpleSlotOneCost = convertView.findViewById(R.id.simpleSlotOneCost);
        TextView simpleSlotTwoCost = convertView.findViewById(R.id.simpleSlotTwoCost);
        TextView simpleSlotThreeCost = convertView.findViewById(R.id.simpleSlotThreeCost);

        LinearLayout fullPitchLinear = convertView.findViewById(R.id.fullPitchLinear);
        TextView fullSlotOneCost = convertView.findViewById(R.id.fullSlotOneCost);
        TextView fullSlotTwoCost = convertView.findViewById(R.id.fullSlotTwoCost);
        TextView fullSlotThreeCost = convertView.findViewById(R.id.fullSlotThreeCost);

        LinearLayout specialPricesLinear = convertView.findViewById(R.id.specialPricesLinear);
        ListView specialPricesListView = convertView.findViewById(R.id.specialPricesListView);

        TextView lblPitchPricing = convertView.findViewById(R.id.lblPitchPricing);
        TextView lblFullPitchPricing = convertView.findViewById(R.id.lblFullPitchPricing);

        /*LinearLayout simplePitchLinear = convertView.findViewById(R.id.simplePitchLinear);
        TextView lblSimplePitch = convertView.findViewById(R.id.lblSimplePitch);
        TextView lblSimpleSunThu = convertView.findViewById(R.id.lblSimpleSunThu);
        TextView lblSimpleFri = convertView.findViewById(R.id.lblSimpleFri);
        TextView lblSimpleSat = convertView.findViewById(R.id.lblSimpleSat);
        TextView lblSimpleOffPeakRate = convertView.findViewById(R.id.lblSimpleOffPeakRate);
        TextView simpleOffPeakTimings = convertView.findViewById(R.id.simpleOffPeakTimings);
        TextView simpleOffPeakSunThuCost = convertView.findViewById(R.id.simpleOffPeakSunThuCost);
        TextView simpleOffPeakFriCost = convertView.findViewById(R.id.simpleOffPeakFriCost);
        TextView simpleOffPeakSatCost = convertView.findViewById(R.id.simpleOffPeakSatCost);
        TextView lblSimpleNormalRate = convertView.findViewById(R.id.lblSimpleNormalRate);
        TextView simpleNormalRateTimings = convertView.findViewById(R.id.simpleNormalRateTimings);
        TextView simpleNormalSunThuCost = convertView.findViewById(R.id.simpleNormalSunThuCost);
        TextView simpleNormalFriCost = convertView.findViewById(R.id.simpleNormalFriCost);
        TextView simpleNormalSatCost = convertView.findViewById(R.id.simpleNormalSatCost);
        TextView lblSimplePeakRate = convertView.findViewById(R.id.lblSimplePeakRate);
        TextView simplePeakRateTiming = convertView.findViewById(R.id.simplePeakRateTiming);
        TextView simplePeakSunThuCost = convertView.findViewById(R.id.simplePeakSunThuCost);
        TextView simplePeakFriCost = convertView.findViewById(R.id.simplePeakFriCost);
        TextView simplePeakSatCost = convertView.findViewById(R.id.simplePeakSatCost);

        LinearLayout fullPitchLinear = convertView.findViewById(R.id.fullPitchLinear);
        TextView lblFullPitch = convertView.findViewById(R.id.lblFullPitch);
        TextView lblFullSunThu = convertView.findViewById(R.id.lblFullSunThu);
        TextView lblFullFri = convertView.findViewById(R.id.lblFullFri);
        TextView lblFullSat = convertView.findViewById(R.id.lblFullSat);
        TextView lblFullOffPeakRate = convertView.findViewById(R.id.lblFullOffPeakRate);
        TextView fullOffPeakTimings = convertView.findViewById(R.id.fullOffPeakTimings);
        TextView fullOffPeakSunThuCost = convertView.findViewById(R.id.fullOffPeakSunThuCost);
        TextView fullOffPeakFriCost = convertView.findViewById(R.id.fullOffPeakFriCost);
        TextView fullOffPeakSatCost = convertView.findViewById(R.id.fullOffPeakSatCost);
        TextView lblFullNormalRate = convertView.findViewById(R.id.lblFullNormalRate);
        TextView fullNormalRateTimings = convertView.findViewById(R.id.fullNormalRateTimings);
        TextView fullNormalSunThuCost = convertView.findViewById(R.id.fullNormalSunThuCost);
        TextView fullNormalFriCost = convertView.findViewById(R.id.fullNormalFriCost);
        TextView fullNormalSatCost = convertView.findViewById(R.id.fullNormalSatCost);
        TextView lblFullPeakRate = convertView.findViewById(R.id.lblFullPeakRate);
        TextView fullPeakRateTiming = convertView.findViewById(R.id.fullPeakRateTiming);
        TextView fullPeakSunThuCost = convertView.findViewById(R.id.fullPeakSunThuCost);
        TextView fullPeakFriCost = convertView.findViewById(R.id.fullPeakFriCost);
        TextView fullPeakSatCost = convertView.findViewById(R.id.fullPeakSatCost);*/

        Button bookButton = convertView.findViewById(R.id.bookButton);

        final PitchBean pitchBean = pitchesList.get(position);

        pitchName.setText(pitchBean.getPitchName()+" (Available "+pitchBean.getFromDateFormatted()+" - "+pitchBean.getToDateFormatted()+")");
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int ninetyPercent = (screenWidth * 90) / 100;
        float textWidthForTitle = pitchName.getPaint().measureText(pitchName.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / ninetyPercent) + 1;
        pitchName.setLines(numberOfLines);

        lblPitchPricing.setText("PITCH PRICING PER "+pitchBean.getPitchDurationLabel());
        lblFullPitchPricing.setText("FULL PITCH PRICING PER "+pitchBean.getPitchDurationLabel());

        int thirtyPercent = (screenWidth * 30) / 100;
        textWidthForTitle = lblPitchPricing.getPaint().measureText(lblPitchPricing.getText().toString());
        numberOfLines = ((int) textWidthForTitle / thirtyPercent) + 1;
        lblPitchPricing.setLines(numberOfLines);

        textWidthForTitle = lblFullPitchPricing.getPaint().measureText(lblFullPitchPricing.getText().toString());
        numberOfLines = ((int) textWidthForTitle / thirtyPercent) + 1;
        lblFullPitchPricing.setLines(numberOfLines);

        SharedPreferences sharedPreferences = context.getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String academy_currency = sharedPreferences.getString("academy_currency", null);

        if(pitchBean.getSimplePitchList() == null || pitchBean.getSimplePitchList().isEmpty()){
            simplePitchLinear.setVisibility(View.GONE);
        } else {
            lblSlotOne.setText(pitchBean.getSimplePitchList().get(0).getDayLabel()+"("+academy_currency+")");
            lblSlotTwo.setText(pitchBean.getSimplePitchList().get(1).getDayLabel()+"("+academy_currency+")");
            lblSlotThree.setText(pitchBean.getSimplePitchList().get(2).getDayLabel()+"("+academy_currency+")");

            simpleSlotOneCost.setText(pitchBean.getSimplePitchList().get(0).getHourPrice());
            simpleSlotTwoCost.setText(pitchBean.getSimplePitchList().get(1).getHourPrice());
            simpleSlotThreeCost.setText(pitchBean.getSimplePitchList().get(2).getHourPrice());
        }



        if(pitchBean.getFullPitchList() == null || pitchBean.getFullPitchList().isEmpty()){
            fullPitchLinear.setVisibility(View.GONE);
        } else {
            lblSlotOne.setText(pitchBean.getFullPitchList().get(0).getDayLabel()+"("+academy_currency+")");
            lblSlotTwo.setText(pitchBean.getFullPitchList().get(1).getDayLabel()+"("+academy_currency+")");
            lblSlotThree.setText(pitchBean.getFullPitchList().get(2).getDayLabel()+"("+academy_currency+")");

            fullSlotOneCost.setText(pitchBean.getFullPitchList().get(0).getHourPrice());
            fullSlotTwoCost.setText(pitchBean.getFullPitchList().get(1).getHourPrice());
            fullSlotThreeCost.setText(pitchBean.getFullPitchList().get(2).getHourPrice());
        }

        if(pitchBean.getSpecialPriceList() == null || pitchBean.getSpecialPriceList().isEmpty()) {
            specialPricesLinear.setVisibility(View.GONE);
        } else {
            specialPricesListView.setAdapter(new ParentPitchSpecialPricesAdapter(context, pitchBean.getSpecialPriceList()));
            Utilities.setListViewHeightBasedOnChildren(specialPricesListView);
        }

        /*if(pitchBean.getSimplePitch() == null){
            simplePitchLinear.setVisibility(View.GONE);
        } else {
            simpleOffPeakTimings.setText("("+pitchBean.getSimplePitch().getOffRate().getTimeRange()+")");
            simpleOffPeakSunThuCost.setText(pitchBean.getSimplePitch().getOffRate().getSundayToThursday().getHourPrice());
            simpleOffPeakFriCost.setText(pitchBean.getSimplePitch().getOffRate().getFriday().getHourPrice());
            simpleOffPeakSatCost.setText(pitchBean.getSimplePitch().getOffRate().getSaturday().getHourPrice());

            simpleNormalRateTimings.setText("("+pitchBean.getSimplePitch().getNormalRate().getTimeRange()+")");
            simpleNormalSunThuCost.setText(pitchBean.getSimplePitch().getNormalRate().getSundayToThursday().getHourPrice());
            simpleNormalFriCost.setText(pitchBean.getSimplePitch().getNormalRate().getFriday().getHourPrice());
            simpleNormalSatCost.setText(pitchBean.getSimplePitch().getNormalRate().getSaturday().getHourPrice());

            simplePeakRateTiming.setText("("+pitchBean.getSimplePitch().getPeakRate().getTimeRange()+")");
            simplePeakSunThuCost.setText(pitchBean.getSimplePitch().getPeakRate().getSundayToThursday().getHourPrice());
            simplePeakFriCost.setText(pitchBean.getSimplePitch().getPeakRate().getFriday().getHourPrice());
            simplePeakSatCost.setText(pitchBean.getSimplePitch().getPeakRate().getSaturday().getHourPrice());
        }

        if(pitchBean.getFullPitch() == null){
            fullPitchLinear.setVisibility(View.GONE);
        } else {
            fullOffPeakTimings.setText("("+pitchBean.getFullPitch().getOffRate().getTimeRange()+")");
            fullOffPeakSunThuCost.setText(pitchBean.getFullPitch().getOffRate().getSundayToThursday().getHourPrice());
            fullOffPeakFriCost.setText(pitchBean.getFullPitch().getOffRate().getFriday().getHourPrice());
            fullOffPeakSatCost.setText(pitchBean.getFullPitch().getOffRate().getSaturday().getHourPrice());

            fullNormalRateTimings.setText("("+pitchBean.getFullPitch().getNormalRate().getTimeRange()+")");
            fullNormalSunThuCost.setText(pitchBean.getFullPitch().getNormalRate().getSundayToThursday().getHourPrice());
            fullNormalFriCost.setText(pitchBean.getFullPitch().getNormalRate().getFriday().getHourPrice());
            fullNormalSatCost.setText(pitchBean.getFullPitch().getNormalRate().getSaturday().getHourPrice());

            fullPeakRateTiming.setText("("+pitchBean.getFullPitch().getPeakRate().getTimeRange()+")");
            fullPeakSunThuCost.setText(pitchBean.getFullPitch().getPeakRate().getSundayToThursday().getHourPrice());
            fullPeakFriCost.setText(pitchBean.getFullPitch().getPeakRate().getFriday().getHourPrice());
            fullPeakSatCost.setText(pitchBean.getFullPitch().getPeakRate().getSaturday().getHourPrice());
        }*/

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent bookPitchScreen = new Intent(context, ParentBookPitchScreen.class);
                bookPitchScreen.putExtra("clickedOnFacility", clickedOnFacility);
                bookPitchScreen.putExtra("clickedOnPitch", pitchBean);
                context.startActivity(bookPitchScreen);
            }
        });

        pitchName.setTypeface(helvetica);

        /*lblSlotOne.setTypeface(helvetica);
        lblSlotTwo.setTypeface(helvetica);
        lblSlotThree.setTypeface(helvetica);*/

        simpleSlotOneCost.setTypeface(helvetica);
        simpleSlotTwoCost.setTypeface(helvetica);
        simpleSlotThreeCost.setTypeface(helvetica);

        fullSlotOneCost.setTypeface(helvetica);
        fullSlotTwoCost.setTypeface(helvetica);
        fullSlotThreeCost.setTypeface(helvetica);

        /*lblSimplePitch.setTypeface(helvetica);
        lblSimpleSunThu.setTypeface(helvetica);
        lblSimpleFri.setTypeface(helvetica);
        lblSimpleSat.setTypeface(helvetica);
        lblSimpleOffPeakRate.setTypeface(helvetica);
        simpleOffPeakTimings.setTypeface(helvetica);
        simpleOffPeakSunThuCost.setTypeface(helvetica);
        simpleOffPeakFriCost.setTypeface(helvetica);
        simpleOffPeakSatCost.setTypeface(helvetica);
        lblSimpleNormalRate.setTypeface(helvetica);
        simpleNormalRateTimings.setTypeface(helvetica);
        simpleNormalSunThuCost.setTypeface(helvetica);
        simpleNormalFriCost.setTypeface(helvetica);
        simpleNormalSatCost.setTypeface(helvetica);
        lblSimplePeakRate.setTypeface(helvetica);
        simplePeakRateTiming.setTypeface(helvetica);
        simplePeakSunThuCost.setTypeface(helvetica);
        simplePeakFriCost.setTypeface(helvetica);
        simplePeakSatCost.setTypeface(helvetica);
        lblFullPitch.setTypeface(helvetica);
        lblFullSunThu.setTypeface(helvetica);
        lblFullFri.setTypeface(helvetica);
        lblFullSat.setTypeface(helvetica);
        lblFullOffPeakRate.setTypeface(helvetica);
        fullOffPeakTimings.setTypeface(helvetica);
        fullOffPeakSunThuCost.setTypeface(helvetica);
        fullOffPeakFriCost.setTypeface(helvetica);
        fullOffPeakSatCost.setTypeface(helvetica);
        lblFullNormalRate.setTypeface(helvetica);
        fullNormalRateTimings.setTypeface(helvetica);
        fullNormalSunThuCost.setTypeface(helvetica);
        fullNormalFriCost.setTypeface(helvetica);
        fullNormalSatCost.setTypeface(helvetica);
        lblFullPeakRate.setTypeface(helvetica);
        fullPeakRateTiming.setTypeface(helvetica);
        fullPeakSunThuCost.setTypeface(helvetica);
        fullPeakFriCost.setTypeface(helvetica);
        fullPeakSatCost.setTypeface(helvetica);*/

        bookButton.setTypeface(linoType);

        return convertView;
    }
}