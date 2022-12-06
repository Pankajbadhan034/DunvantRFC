//package com.dunvant.application.child.smartBand;
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//
//import com.zeroner.android_zeroner_ble.utils.LogUtil;
//
//public class TimeService extends Service {
//	private Thread workThread;
//
//	@Override
//	public IBinder onBind(Intent intent) {
////		System.out.println("onBindCalled");
//		return null;
//	}
//	@Override
//	public void onCreate() {
//		super.onCreate();
//		workThread = new Thread() {
//			@Override
//			public void run() {
//				while (true) {
////					System.out.println("onCreateWhileCalled");
//					try {
//						Thread.sleep(15000);
//						LogUtil.i("time" + System.currentTimeMillis());
//						Intent intent = new Intent("COM.ACTION.TIMESERVICE");
//						TimeService.this.sendBroadcast(intent);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		};
//		workThread.start();
//	}
//
//
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
////		System.out.println("onStartCommandCalled");
//		return super.onStartCommand(intent, flags, startId);
////		return START_STICKY;
//	}
//
//
//
//	@Override
//	public void onDestroy() {
//		stopForeground(true);
//		super.onDestroy();
////		System.out.println("onDestroyCalled");
//	}
//
//}
