//
//package com.dunvant.application.child.smartBand.fragment;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.ListView;
//
//import com.dunvant.application.R;
//import com.dunvant.application.child.smartBand.adapter.DeviceSearchAdapter;
//import com.zeroner.android_zeroner_ble.bluetooth.BluetoothDataParseBiz;
//import com.zeroner.android_zeroner_ble.bluetooth.WristBandDevice;
//import com.zeroner.android_zeroner_ble.model.WristBand;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class ScannerFragment extends DialogFragment {
//
//	private OnDeviceSelectedListener mListener;
//	private DeviceSearchAdapter mAdapter;
//	private Button mScanButton;
//	private List<WristBand> devs = new ArrayList<WristBand>();
//	private DataCallback myCallbackListener;
//
//	public static ScannerFragment getInstance(final Context context) {
//		final ScannerFragment fragment = new ScannerFragment();
//		return fragment;
//	}
//
//
//	public static interface OnDeviceSelectedListener {
//		public void onDeviceSelected(final WristBand device, final String name);
//		public void onDialogCanceled();
//	}
//	@Override
//	public void onAttach(Context context) {
//		super.onAttach(context);
//		try {
//			this.mListener = (OnDeviceSelectedListener) context;
//		} catch (final ClassCastException e) {
//			throw new ClassCastException(context.toString() + " must implement OnDeviceSelectedListener");
//		}
//	}
//
////	@Override
////	public void onAttach(final Activity activity) {
////		super.onAttach(activity);
////		try {
////			this.mListener = (OnDeviceSelectedListener) activity;
////		} catch (final ClassCastException e) {
////			throw new ClassCastException(activity.toString() + " must implement OnDeviceSelectedListener");
////		}
////	}
//
//	@Override
//	public void onCreate(final Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		myCallbackListener=new DataCallback(getActivity());
//		getWristBand().stopLeScan();
//		getWristBand().startLeScan();
//	}
//
//	@Override
//	public void onDestroyView() {
//		super.onDestroyView();
//	}
//
//
//	@Override
//	public Dialog onCreateDialog(final Bundle savedInstanceState) {
//		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//		final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_device_selection, null);
//		final ListView listview = (ListView) dialogView.findViewById(android.R.id.list);
//		listview.setEmptyView(dialogView.findViewById(android.R.id.empty));
//		listview.setAdapter(mAdapter = new DeviceSearchAdapter(getActivity(), devs));
//
//		builder.setTitle("Scanning...");
//		final AlertDialog dialog = builder.setView(dialogView).create();
//		dialog.setCancelable(false);
//		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
//				getWristBand().stopLeScan();
//				dialog.dismiss();
//				final WristBand d = (WristBand) mAdapter.getItem(position);
//				mListener.onDeviceSelected(d,d.getName()==null?"xx":d.getName());
//			}
//		});
//
//		mScanButton = (Button) dialogView.findViewById(R.id.action_cancel);
//		mScanButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (v.getId() == R.id.action_cancel) {
////					System.out.println("outerIfScan");
//					if(getWristBand().isScanning()){
////						System.out.println("isScanningIf");
//						getWristBand().stopLeScan();
//					}else{
////						System.out.println("isScanningElse");
//						getWristBand().startLeScan();
//					}
//				}
//			}
//		});
//		return dialog;
//	}
//
//	private WristBandDevice getWristBand() {
//		WristBandDevice device= WristBandDevice.getInstance(getActivity());
//		device.setCallbackHandler(myCallbackListener);
//		return device;
//	}
//
//	@Override
//	public void onCancel(DialogInterface dialog) {
//		super.onCancel(dialog);
//		try {
//			mListener.onDialogCanceled();
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//	}
//
//	class DataCallback extends BluetoothDataParseBiz {
//
//		public DataCallback(Context context) {
//			super(context);
//		}
//
//		@Override
//		public void connectStatue(boolean isConnect) {
//			super.connectStatue(isConnect);
//		}
//
//
//
//		@Override
//		public void onWristBandFindNewAgreement(WristBand dev) {
//			super.onWristBandFindNewAgreement(dev);
//			if(!devs.contains(dev)){
//				devs.add(dev);
//				mAdapter.notifyDataSetChanged();
//			}
//		}
//	}
//
//
//}
