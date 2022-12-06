package com.dunvant.application.parent.adapters;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dunvant.application.R;

import java.util.ArrayList;

public class WarningAdapter extends BaseAdapter{

    Context context;
    ArrayList<String> warningList;
    LayoutInflater layoutInflater;

    public WarningAdapter(Context context, ArrayList<String> warningList){
        this.context = context;
        this.warningList = warningList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return warningList.size();
    }

    @Override
    public Object getItem(int i) {
        return warningList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.parent_adapter_warning_item, null);

        TextView warningText = view.findViewById(R.id.warningText);

        warningText.setText("- "+warningList.get(i));

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int eightyPercent = (screenWidth * 80) / 100;
        float textWidthForTitle = warningText.getPaint().measureText(warningText.getText().toString());
        int numberOfLines = ((int) textWidthForTitle / eightyPercent) + 1;
        warningText.setLines(numberOfLines);

        return view;
    }
}