//package com.dunvant.application.child.smartBand.adapter;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.dunvant.application.R;
//import com.zeroner.android_zeroner_ble.model.WristBand;
//
//import java.util.List;
//
//public class DeviceSearchAdapter extends AbsListAdapter<WristBand> {
//
//	public DeviceSearchAdapter(Context context, List<WristBand> mList) {
//		super(context, mList);
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder viewHolder;
//		if (convertView == null) {
//			convertView = mInflater.inflate(R.layout.view_device_list_item, null);
//			viewHolder = new ViewHolder();
//			viewHolder.deviceName = (TextView) convertView.findViewById(R.id.device_name);
//			convertView.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
//		viewHolder.deviceName.setText(getItem(position).getName());
//		return convertView;
//	}
//
//	class ViewHolder {
//		private TextView deviceName;
//		private ImageView rssi;
//	}
//}
