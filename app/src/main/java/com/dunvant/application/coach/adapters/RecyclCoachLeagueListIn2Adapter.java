package com.dunvant.application.coach.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.dunvant.application.R;
import com.dunvant.application.beans.CoachLeagueTimedArrayBean;

import java.util.ArrayList;

public class RecyclCoachLeagueListIn2Adapter extends BaseAdapter {
    Dialog dialog;
    Context context;
    LayoutInflater layoutInflater;

    Typeface helvetica;
    Typeface linoType;
    ArrayList<CoachLeagueTimedArrayBean> arrayBeanArrayList;
    RecycleCoachLeagueTeam2Adapter recycleCoachLeagueTeam2Adapter;
    String attId;
    String scoreValue;

    public RecyclCoachLeagueListIn2Adapter(Context context, ArrayList<CoachLeagueTimedArrayBean> arrayBeanArrayList, RecycleCoachLeagueTeam2Adapter recycleCoachLeagueTeam2Adapter, String attId, String scoreValue) {
        this.context = context;
        this.arrayBeanArrayList = arrayBeanArrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.recycleCoachLeagueTeam2Adapter = recycleCoachLeagueTeam2Adapter;
        this.attId = attId;
        this.scoreValue = scoreValue;

        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
    }

    @Override
    public int getCount() {
        return arrayBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.recycle_coach_league_list_in_adapter, null);

        final Button editText = (Button) convertView.findViewById(R.id.editText);

        CoachLeagueTimedArrayBean coachLeagueTimedArrayBean = arrayBeanArrayList.get(position);
        editText.setText(coachLeagueTimedArrayBean.getValue());
        editText.setTypeface(helvetica);
        if(coachLeagueTimedArrayBean.getValue().equalsIgnoreCase("")){
            editText.setText("");
        }else{
            editText.setText(coachLeagueTimedArrayBean.getValue());
        }



        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.coach_dialog_recycle_edit);
                dialog.setTitle(R.string.ifa_dialog);

                // set the custom dialog components - text, image and button
                final EditText parentPassword = (EditText) dialog.findViewById(R.id.parentPassword);
                Button submit = (Button) dialog.findViewById(R.id.submit);

//                parentPassword.setTypeface(helvetica);
//                submit.setTypeface(linoType);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String parentPasswordStr = parentPassword.getText().toString();
                        if (parentPasswordStr == null || parentPasswordStr.isEmpty()) {
                            dialog.dismiss();
                            // Toast.makeText(context, "Please enter Parent Password", Toast.LENGTH_LONG).show();
                        } else {
                            arrayBeanArrayList.get(position).setValue(parentPassword.getText().toString().trim());
                            notifyDataSetChanged();
                            recycleCoachLeagueTeam2Adapter.timedValuesStr = "";
                            recycleCoachLeagueTeam2Adapter.commaSeprate(attId, scoreValue);
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();


            }
        });

//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    //do something
//                    arrayBeanArrayList.get(position).setValue(editText.getText().toString().trim());
//
//                }
//                return false;
//            }
//        });



        return convertView;
    }
}