package com.dunvant.application.child.smartBand.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dunvant.application.R;

import java.util.List;

public class BluetoothDataAdapter extends AbsListAdapter<String> {

	public BluetoothDataAdapter(Context context, List<String> mList) {
		super(context, mList);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.view_device_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.deviceName = (TextView) convertView.findViewById(R.id.device_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.deviceName.setText(getItem(position));
		return convertView;
	}

	class ViewHolder {
		private TextView deviceName;
		private ImageView rssi;
	}
}
