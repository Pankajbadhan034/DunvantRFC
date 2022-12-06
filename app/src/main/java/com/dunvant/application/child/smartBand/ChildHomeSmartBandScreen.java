//package com.dunvant.application.child.smartBand;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.FragmentManager;
//import android.app.ProgressDialog;
//import android.bluetooth.BluetoothAdapter;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.graphics.Typeface;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.dunvant.application.R;
//import com.dunvant.application.beans.UserBean;
//import com.dunvant.application.child.ChildTrackWorkoutScreen;
//import com.dunvant.application.child.smartBand.adapter.BluetoothDataAdapter;
//import com.dunvant.application.child.smartBand.bean.ChildSmartBandHeartRateHourDataBean;
//import com.dunvant.application.child.smartBand.bean.ChildSmartBandSegmentDataBean;
//import com.dunvant.application.child.smartBand.fragment.ScannerFragment;
//import com.dunvant.application.utils.Utilities;
//import com.dunvant.application.webservices.IWebServiceCallback;
//import com.dunvant.application.webservices.PostWebServiceWithHeadersAsync;
//import com.zeroner.android_zeroner_ble.bluetooth.BluetoothDataParseBiz;
//import com.zeroner.android_zeroner_ble.bluetooth.WristBandDevice;
//import com.zeroner.android_zeroner_ble.bluetooth.task.NewAgreementBackgroundThreadManager;
//import com.zeroner.android_zeroner_ble.bluetooth.task.WriteOneDataTask;
//import com.zeroner.android_zeroner_ble.model.Alerm;
//import com.zeroner.android_zeroner_ble.model.CurrData_0x29;
//import com.zeroner.android_zeroner_ble.model.DeviceSetting;
//import com.zeroner.android_zeroner_ble.model.FMdeviceInfo;
//import com.zeroner.android_zeroner_ble.model.HeartRateDetial;
//import com.zeroner.android_zeroner_ble.model.HeartRateParams;
//import com.zeroner.android_zeroner_ble.model.KeyModel;
//import com.zeroner.android_zeroner_ble.model.Power;
//import com.zeroner.android_zeroner_ble.model.QuietModeInfo;
//import com.zeroner.android_zeroner_ble.model.Result;
//import com.zeroner.android_zeroner_ble.model.ScheduleResult;
//import com.zeroner.android_zeroner_ble.model.SportType;
//import com.zeroner.android_zeroner_ble.model.TB_v3_heartRate_data_hours;
//import com.zeroner.android_zeroner_ble.model.TB_v3_sleep_data;
//import com.zeroner.android_zeroner_ble.model.TB_v3_sport_data;
//import com.zeroner.android_zeroner_ble.model.UserInfo;
//import com.zeroner.android_zeroner_ble.model.WristBand;
//import com.zeroner.android_zeroner_ble.utils.ByteUtil;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.lang.reflect.Type;
//import java.text.DateFormat;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//
//public class ChildHomeSmartBandScreen extends AppCompatActivity implements IWebServiceCallback, ScannerFragment.OnDeviceSelectedListener {
//    String prefText;
//    // Run time permission
//    public static final int MULTIPLE_PERMISSIONS = 10;
//    String[] permissions = new String[] {
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION
//    };
//
//    //3.1	device information 0x00
//    public static final int CMD_DEVICE_MESSAGE=0X00;
//    //3.2	power 0x01
//    public static final int CMD_DEVICE_POWER=0x01;
//    //3.25	 segmental frozen data synchronism 0x28
//    public static final int CMD_SEGMENT_DATA=0x28;
//    //3.26	daily frozen data synchronism; 0x29
//    public static final int CMD_DIALY_CURR_DATA=0x29;
//    //3.17	Query device support movement type  0x1A
//    public static final int CMD_DEVICE_SURPORT=0x1a;
//    //3.34	Bracelet mode control 0x40
//    public static final int CMD_MANUAL_MODE_CONTROL=0x40;
//    //3.37	Heart rate parameters  0x50
//    public static final int CMD_SETTING_HEARTRATE_PARAMS=0x50;
//    //3.38	Segmented heart rate data synchronization 0x51
//    public static final int CMD_HEARTRATE_DATA=0x51;
//    //3.38	53  Heart rate data
//    public static final int CMD_HEARTRATE_DATA_HOUR=0x53;
//    //schedule
//    public static final int CMD_schedule=0x1D;
//    //Configurable schedule
//    public static final int CMD_schedule_info=0x1e;
//    public static final int CMD_user_info=0x21;
//    public static final int CMD_device_settings=0x19;
//
//    public static final int CMD_alarm_info=0x15;
//
//    public static final int CMD_quiet_mode_info=0x06;
//
//    private Context mContext;
//    Calendar calendar;
//    private ArrayList<String> data;
//    private MainDataCallback myCallback;
//    private BluetoothDataAdapter mAdapter;
//    JSONArray jsonArray;
//    JSONArray jsonSportData;
//    Typeface helvetica;
//    Typeface linoType;
//    SharedPreferences sharedPreferences;
//    UserBean loggedInUser;
//    private final String SAVE_HEARTRATE = "SAVE_HEARTRATE";
//    private final String SAVE_SPORT = "SAVE_SPORT";
//    boolean heartRateDataAvailable = false;
//    boolean heartRateDataExist = false;
//    boolean sportDataExist = false;
//
//    RelativeLayout sportScreen;
//    RelativeLayout sportScreen2;
//    RelativeLayout sportScreen3;
//    RelativeLayout hearRateScreen;
//    ImageView backButton;
//    TextView connectLabel;
//    TextView connectClick;
//    TextView stepsText;
//    TextView caloriesText;
//    ImageView syncData;
//    TextView maximumHeart;
//    //TextView minimumHeart;
//    TextView distanceText;
//    String steps, calories, distance, heartRateMax, heartRateMin;
//    Button startTracking;
//    String bluetoothStatus="disconnected";
//    ArrayList<ChildSmartBandSegmentDataBean> childSmartBandSegmentDataBeanArrayList = new ArrayList<>();
//    ArrayList<ChildSmartBandSegmentDataBean> storedChildSmartBandSegmentDataBeanArrayList = new ArrayList<>();
//    String day="", month="", year="", week="";
//    Type listOfObjects = new TypeToken<ArrayList<ChildSmartBandSegmentDataBean>>(){}.getType();
//
//    ArrayList<ChildSmartBandHeartRateHourDataBean> childSmartBandHeartRateHourDataBeanArrayList = new ArrayList<>();
//    Type heartRateListOfObjects = new TypeToken<ArrayList<ChildSmartBandHeartRateHourDataBean>>(){}.getType();
//    ArrayList<ChildSmartBandHeartRateHourDataBean> storedChildSmartBandHeartRateHourDataBeanArrayList = new ArrayList<>();
//    TextView lblTitle;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.child_activity_home_smart_band_screen);
//        mContext = this;
//        Intent service=new Intent(mContext,TimeService.class);
//        startService(service);
//        calendar = Calendar.getInstance();
//        data = new ArrayList<String>();
//        myCallback=new MainDataCallback(mContext);
//        mAdapter = new BluetoothDataAdapter(mContext, data);
//
//        helvetica = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
//        linoType = Typeface.createFromAsset(getAssets(),"fonts/LinotypeOrdinarRegular.ttf");
//
//        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        String jsonLoggedInUser = sharedPreferences.getString("loggedInUser", null);
//        if (jsonLoggedInUser != null) {
//            loggedInUser = gson.fromJson(jsonLoggedInUser, UserBean.class);
//        }
//        lblTitle = findViewById(R.id.lblTitle);
//        prefText = getIntent().getStringExtra("prefText");
//        lblTitle.setText(prefText);
//
//        backButton = (ImageView) findViewById(R.id.backButton);
//        sportScreen = (RelativeLayout) findViewById(R.id.sportScreen);
//        sportScreen2 = (RelativeLayout) findViewById(R.id.sportScreen2);
//        sportScreen3 = (RelativeLayout) findViewById(R.id.sportScreen3);
//        hearRateScreen = (RelativeLayout) findViewById(R.id.hearRateScreen);
//        connectLabel = (TextView) findViewById(R.id.connectLabel);
//        connectClick = (TextView) findViewById(R.id.connectClick);
//        stepsText = (TextView) findViewById(R.id.stepsText);
//        caloriesText = (TextView) findViewById(R.id.caloriesText);
//        syncData = (ImageView) findViewById(R.id.syncData);
//        maximumHeart = (TextView) findViewById(R.id.maximumHeart);
//       // minimumHeart = (TextView) findViewById(R.id.minimumHeart);
//        distanceText = (TextView) findViewById(R.id.distanceText);
//        startTracking = (Button) findViewById(R.id.startTracking);
//
//        sportScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////               sportScreenData();
//                Intent obj = new Intent(ChildHomeSmartBandScreen.this, ChildSportSmartBandHistory.class);
//                startActivity(obj);
//            }
//        });
//
//        sportScreen2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent obj = new Intent(ChildHomeSmartBandScreen.this, ChildSportSmartBandHistory.class);
//                startActivity(obj);
//            }
//        });
//
//        sportScreen3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent obj = new Intent(ChildHomeSmartBandScreen.this, ChildSportSmartBandHistory.class);
//                startActivity(obj);
//            }
//        });
//
//        startTracking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent obj = new Intent(ChildHomeSmartBandScreen.this, ChildTrackWorkoutScreen.class);
//                startActivity(obj);
//            }
//        });
//
//        hearRateScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                heartRateScreenData();
//                Intent obj = new Intent(ChildHomeSmartBandScreen.this, ChildHeartRateHistorySmartBandScreen.class);
//                startActivity(obj);
//            }
//        });
//
//        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//
//        steps = sharedPreferences.getString("steps", null);
//        calories = sharedPreferences.getString("calories",null);
//        distance = sharedPreferences.getString("distance",null);
//        heartRateMax = sharedPreferences.getString("heartRateMax",null);
//        heartRateMin = sharedPreferences.getString("heartRateMin", null);
//
//        if(steps==null){
//
//        }else{
//            stepsText.setText(steps);
//            caloriesText.setText(calories);
//            distanceText.setText(distance);
//            maximumHeart.setText(heartRateMax+"/"+heartRateMin);
//          //  minimumHeart.setText(heartRateMin+" Minimum Heart Rate");
//        }
//
//
//
//        syncData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(WristBandDevice.getInstance(mContext).isConnected()==true){
//                    //childSmartBandSegmentDataBeanArrayList.clear();
//                    syncdataLoader();
//                }else{
//                    Toast.makeText(ChildHomeSmartBandScreen.this, "Please connect bluetooth with smart band", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        connectClick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= 23) {
//                    // only for newer versions
//                    clickBluetooth();
//                }
//                else{
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ChildHomeSmartBandScreen.this);
//                    builder1.setMessage("Minimum supported Android version is 6.0");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Ok",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    finish();
//                                    dialog.cancel();
//                                }
//                            });
//
//
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                }
//
//
//            }
//        });
//
//        connectLabel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= 23) {
//                    // only for gingerbread and newer versions
//                    clickBluetooth();
//                }else{
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ChildHomeSmartBandScreen.this);
//                    builder1.setMessage("Minimum supported Android version is 6.0");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Ok",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    finish();
//                                    dialog.cancel();
//                                }
//                            });
//
//
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                }
//            }
//        });
//
//
//        try{
//            WristBandDevice.getInstance(mContext).connect();
////            System.out.println("ConnectValue::"+ WristBandDevice.getInstance(mContext).connect());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
//    private void showDeviceScanningDialog() {
//        final FragmentManager fm = getFragmentManager();
//        final ScannerFragment dialog = ScannerFragment.getInstance(ChildHomeSmartBandScreen.this);
//        dialog.show(fm, "scan_fragment");
////        dialog.setCancelable(false);
//    }
//
////    @Override
////        public void onDeviceSelected(WristBand device, String name) {
////        System.out.println("onDeviceSelected");
////        getWristBand();
////        WristBandDevice.getInstance(mContext).connect(device);
////        SharedPreferences mySharedPreferences= getSharedPreferences("test", Activity.MODE_PRIVATE);
////        SharedPreferences.Editor editor = mySharedPreferences.edit();
////        editor.putString("mac", device.getAddress());
////        editor.putString("name", device.getName());
////        editor.commit();
////
////
////
////    }
//
//    @Override
//    public void onDeviceSelected(final WristBand device, String name) {
//        this.runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                getWristBand();
//                WristBandDevice.getInstance(mContext).connect(device);
//                SharedPreferences mySharedPreferences = getSharedPreferences("test", Activity.MODE_PRIVATE);
//                SharedPreferences.Editor editor = mySharedPreferences.edit();
//                editor.putString("mac", device.getAddress());
//                editor.putString("name", device.getName());
//                editor.commit();
//            }
//        });
//    }
//
//    @Override
//    public void onDialogCanceled() {
////        System.out.println("onDialogCanceled");
//        WristBandDevice.getInstance(mContext).stopLeScan();
//    }
//
//    @Override
//    public void onWebServiceResponse(String response, String tag) {
//        switch ( tag){
//            case SAVE_HEARTRATE:
//                if (response == null) {
//                    Toast.makeText(ChildHomeSmartBandScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
//                } else {
//                    try {
//                        JSONObject responseObject = new JSONObject(response);
//                        boolean status = responseObject.getBoolean("status");
//                        String message = responseObject.getString("message");
//                        if(status){
//                            Toast.makeText(ChildHomeSmartBandScreen.this, message, Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(ChildHomeSmartBandScreen.this, message, Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(ChildHomeSmartBandScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            break;
//
//            case SAVE_SPORT:
//                if (response == null) {
//                    Toast.makeText(ChildHomeSmartBandScreen.this, "Could not connect to server", Toast.LENGTH_SHORT).show();
//                } else {
//                    try {
//                        JSONObject responseObject = new JSONObject(response);
//                        boolean status = responseObject.getBoolean("status");
//                        String message = responseObject.getString("message");
//                        if(status){
//                            Toast.makeText(ChildHomeSmartBandScreen.this, message, Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(ChildHomeSmartBandScreen.this, message, Toast.LENGTH_SHORT).show();
//                        }
//
//
//                        if(heartRateDataAvailable==true){
////                        System.out.println("hereTrueCondHERE:");
//                        if (Utilities.isNetworkAvailable(ChildHomeSmartBandScreen.this)) {
//                            List<NameValuePair> nameValuePairList = new ArrayList<>();
//                            nameValuePairList.add(new BasicNameValuePair("segment_data", jsonArray.toString()));
//
//                            String webServiceUrl = Utilities.BASE_URL + "children/sync_heart_data";
//
//                            ArrayList<String> headers = new ArrayList<>();
//                            headers.add("X-access-uid:" + loggedInUser.getId());
//                            headers.add("X-access-token:" + loggedInUser.getToken());
//
//                            PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildHomeSmartBandScreen.this, nameValuePairList, SAVE_HEARTRATE, ChildHomeSmartBandScreen.this, headers);
//                            postWebServiceAsync.execute(webServiceUrl);
//                        } else {
//                            Toast.makeText(ChildHomeSmartBandScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//                        }
//                        heartRateDataAvailable = false;
//                    }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Toast.makeText(ChildHomeSmartBandScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//
//        }
//
//    }
//
//
//    class MainDataCallback extends BluetoothDataParseBiz {
//
//        public MainDataCallback(Context context) {
//            super(context);
//        }
//
//        @Override
//        public void connectStatue(boolean isConnect) {
//            super.connectStatue(isConnect);
//            data.add(isConnect + "");
////            System.out.println("connectStatus:::" + isConnect);
//            if(isConnect==true){
//                bluetoothStatus="Connected";
//                connectClick.setText("Disconnect");
//                syncdataLoader();
//                connectLabel.setText("Connected");
//            }else if(isConnect==false){
//                bluetoothStatus="disconnected";
//                connectClick.setText("Connect");
//                connectLabel.setText("Unconnected");
//            }
//
//
//            mAdapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void discoveredServices(int status) {
//            super.discoveredServices(status);
//            data.add(String.valueOf(status));
//            mAdapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void onWristBandFindNewAgreement(WristBand dev) {
//            super.onWristBandFindNewAgreement(dev);
//        }
//
//        @Override
//        public void onCharacteristicWriteData() {
//            super.onCharacteristicWriteData();
//            data.add("success");
//            mAdapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void getNFCData(int type, byte[] datas) {
//            super.getNFCData(type, datas);
//            data.add(ByteUtil.bytesToString(datas));
//            mAdapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void getData(int type, Result result) {
//            super.getData(type, result);
//            switch (type) {
//                case CMD_DEVICE_MESSAGE :
//                    FMdeviceInfo info=(FMdeviceInfo) result;
//                    data.add(info.toJson());
////                    System.out.println("CMD_DEVICE_MESSAGE"+info.toJson());
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                case CMD_DEVICE_POWER:
//                    Power power=(Power) result;
//                    data.add(power.toJson());
////                    System.out.println("CMD_DEVICE_POWER" + power.toJson());
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                case CMD_DEVICE_SURPORT:
//                    SportType sportType=(SportType) result;
//                    data.add(sportType.toJson());
////                    System.out.println("CMD_DEVICE_SURPORT" + sportType.toJson());
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                case CMD_DIALY_CURR_DATA:
//                    CurrData_0x29 curr=(CurrData_0x29) result;
//                    data.add(curr.toJson());
////                    System.out.println("CMD_DIALY_CURR_DATA" + curr.toJson());
//
//                    try{
//                        JSONObject jsonObject = new JSONObject(curr.toJson());
//                        String calories = jsonObject.getString("sportCalories");
//                        String distance = jsonObject.getString("sportDistances");
//                        String steps = jsonObject.getString("sportSteps");
//
//                        double km = Double.parseDouble(distance);
//                        km = km/1000;
//
//                        stepsText.setText(steps);
//                        caloriesText.setText(calories+"");
//                        distanceText.setText(new DecimalFormat("##.##").format(km)+" km");
//
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("steps", steps);
//                        editor.putString("calories", calories+"");
//                        editor.putString("distance", new DecimalFormat("##.##").format(km)+" km");
//                        editor.commit();
//
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//
//
//
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                case CMD_HEARTRATE_DATA:
//                    HeartRateDetial detial=(HeartRateDetial) result;
//                    data.add(detial.toJson());
////                    System.out.println("CMD_HEARTRATE_DATA" + detial.toJson());
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                case CMD_MANUAL_MODE_CONTROL:
//                    KeyModel keymode=(KeyModel) result;
//                    data.add(keymode.toJson());
////                    System.out.println("CMD_MANUAL_MODE_CONTROL" + keymode.toJson());
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                case CMD_SETTING_HEARTRATE_PARAMS:
//                    HeartRateParams params=(HeartRateParams) result;
//                    data.add(params.toJson());
////                    System.out.println("CMD_SETTING_HEARTRATE_PARAMS" + params.toJson());
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                case CMD_SEGMENT_DATA:
//                    if(result instanceof TB_v3_sleep_data){
//                        data.add(result.toJson());
//                        mAdapter.notifyDataSetChanged();
//                    }else if(result instanceof TB_v3_sport_data){
//                        data.add(result.toJson());
//                        mAdapter.notifyDataSetChanged();
////                        System.out.println("segmentSportData::" + result.toJson());
//
//
//
//
//                        try{
//                            final ChildSmartBandSegmentDataBean   childSmartBandSegmentDataBean = new ChildSmartBandSegmentDataBean();
//                            JSONObject jsonObject = new JSONObject(result.toJson());
//
//                            if(jsonObject.getString("start_time").equalsIgnoreCase("65535")){
////                                System.out.println("NoDataHERE::");
//
//                            }else {
//                                ArrayList<ChildSmartBandSegmentDataBean> temDataSend = new ArrayList<>();
//                                jsonSportData = new JSONArray();
//
//                                childSmartBandSegmentDataBean.setUploaded(jsonObject.getString("_uploaded"));
//                                childSmartBandSegmentDataBean.setCalories(jsonObject.getString("calorie"));
//                                childSmartBandSegmentDataBean.setCompleteProgress(jsonObject.getString("complete_progress"));
//                                childSmartBandSegmentDataBean.setDay(jsonObject.getString("day"));
//
//                                JSONObject detailData = new JSONObject(jsonObject.getString("detail_data"));
//                                childSmartBandSegmentDataBean.setActivity(detailData.getString("activity"));
//                                childSmartBandSegmentDataBean.setCount(detailData.getString("count"));
//                                childSmartBandSegmentDataBean.setDistance(detailData.getString("distance"));
//                                childSmartBandSegmentDataBean.setSteps(detailData.getString("step"));
//
//                                childSmartBandSegmentDataBean.setEndTime(jsonObject.getString("end_time"));
//                                childSmartBandSegmentDataBean.setIndexNo(jsonObject.getString("index"));
//                                childSmartBandSegmentDataBean.setMonth(jsonObject.getString("month"));
//                                childSmartBandSegmentDataBean.setReserved(jsonObject.getString("reserved"));
//                                childSmartBandSegmentDataBean.setSportType(jsonObject.getString("sport_type"));
//                                childSmartBandSegmentDataBean.setStartTime(jsonObject.getString("start_time"));
//                                childSmartBandSegmentDataBean.setUid(jsonObject.getString("uid"));
//                                childSmartBandSegmentDataBean.setWeek(jsonObject.getString("week"));
//                                childSmartBandSegmentDataBean.setYear(jsonObject.getString("year"));
//
//
//                                temDataSend.add(childSmartBandSegmentDataBean);
//
//
//                                day = jsonObject.getString("day");
//                                month = jsonObject.getString("month");
//                                year = jsonObject.getString("year");
//                                week = jsonObject.getString("week");
//
//
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.putString("day", day);
//                                editor.putString("month", month);
//                                editor.putString("year", year);
//                                editor.commit();
//
//                                sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//                                Gson gson = new Gson();
//                                String response = sharedPreferences.getString("segmentData", null);
//                                if (response == null) {
//                                } else {
//                                    childSmartBandSegmentDataBeanArrayList = gson.fromJson(response, listOfObjects);
//                                }
//
//                                if (childSmartBandSegmentDataBeanArrayList != null && !childSmartBandSegmentDataBeanArrayList.isEmpty()) {
////                                    System.out.println("hereIF::");
//                                    final ChildSmartBandSegmentDataBean childSmartBandSegmentDataBean1 = childSmartBandSegmentDataBeanArrayList.get(0);
//                                    if (childSmartBandSegmentDataBean1.getDay().equalsIgnoreCase(jsonObject.getString("day")) &&
//                                            childSmartBandSegmentDataBean1.getWeek().equalsIgnoreCase(jsonObject.getString("week"))) {
////                                        System.out.println("hereInnerIF::");
//                                        childSmartBandSegmentDataBeanArrayList.add(childSmartBandSegmentDataBean);
//                                    } else {
////                                        System.out.println("hereInnerELSE::");
//                                        //sharedPreferences.edit().remove("segmentData").commit();
//                                        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//                                        SharedPreferences.Editor editor2 = sharedPreferences.edit();
//                                        editor2.remove("segmentData");
//                                        editor2.apply();
//                                        childSmartBandSegmentDataBeanArrayList.clear();
//
//                                        childSmartBandSegmentDataBeanArrayList.add(childSmartBandSegmentDataBean);
//
//
//                                    }
//
//                                } else {
////                                    System.out.println("hereELSE::");
//                                    childSmartBandSegmentDataBeanArrayList.add(childSmartBandSegmentDataBean);
//                                }
//                                sportDataExist=true;
//                                storeSegementListData();
//
//
//                                //add data to json array
//                                for (int i = 0; i < temDataSend.size(); i++) {
//                                    final ChildSmartBandSegmentDataBean childSmartBandSegmentDataBean1 = temDataSend.get(i);
//
//                                    //duration
//                                    int hourStartTime = Integer.parseInt(childSmartBandSegmentDataBean1.getStartTime());
//                                    int minStartTime = Integer.parseInt(childSmartBandSegmentDataBean1.getStartTime());
//                                    hourStartTime = hourStartTime / 60;
//                                    minStartTime = minStartTime % 60;
//                                    int hourEndTime = Integer.parseInt(childSmartBandSegmentDataBean1.getEndTime());
//                                    int minEndTime = Integer.parseInt(childSmartBandSegmentDataBean1.getEndTime());
//                                    hourEndTime = hourEndTime / 60;
//                                    minEndTime = minEndTime % 60;
//
//                                    String hourStartTimeStr;
//                                    if(hourStartTime<10){
//                                        hourStartTimeStr = "0"+hourStartTime;
//                                    }else{
//                                        hourStartTimeStr = ""+hourStartTime;
//                                    }
//
//                                    String minStartTimeStr;
//                                    if(minStartTime<10){
//                                        minStartTimeStr = "0"+minStartTime;
//                                    }else{
//                                        minStartTimeStr = ""+minStartTime;
//                                    }
//
//                                    String hourEndtimeStr;
//                                    if(hourEndTime<10){
//                                        hourEndtimeStr = "0"+hourEndTime;
//                                    }else{
//                                        hourEndtimeStr = ""+hourEndTime;
//                                    }
//
//                                    String minEndTimeStr;
//                                    if(minEndTime<10){
//                                        minEndTimeStr = "0"+minEndTime;
//                                    }else{
//                                        minEndTimeStr = ""+minEndTime;
//                                    }
//
//
//                                    String startTimeLong = hourStartTimeStr + ":" + minStartTimeStr+":00";
//                                    String  endTimeLong = hourEndtimeStr + ":" + minEndTimeStr+":00";
//
//                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//                                    Date date1 = format.parse(startTimeLong);
//                                    Date date2 = format.parse(endTimeLong);
//                                    long difference = date2.getTime() - date1.getTime();
//
//                                    Date date = new Date(difference);
//                                    DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//                                    String timeFormatted = formatter.format(date);
//
////                                    System.out.println("FormattedDate::"+timeFormatted);
//
//                                    //distance
//                                    double km = Double.parseDouble(childSmartBandSegmentDataBean1.getDistance());
//                                    km = km / 1000;
//
//                                    //steps
//                                    String stepsValue = childSmartBandSegmentDataBean1.getSteps();
//
//                                    //calories
//                                    String caloriesValue = childSmartBandSegmentDataBean1.getCalories();
//
//                                    // activity value
//                                    String activityValue;
//                                    if (childSmartBandSegmentDataBean1.getSportType().equalsIgnoreCase("1")) {
//                                        // walking
//                                        activityValue = "walking";
//                                    } else if (childSmartBandSegmentDataBean1.getSportType().equalsIgnoreCase("7")) {
//                                        //running
//                                        activityValue = "running";
//                                    } else if (childSmartBandSegmentDataBean1.getSportType().equalsIgnoreCase("130")) {
//                                        //football
//                                        activityValue = "football";
//                                    } else if (childSmartBandSegmentDataBean1.getSportType().equalsIgnoreCase("136")) {
//                                        //cycle
//                                        activityValue = "cycling";
//                                    } else {
////                                        System.out.println("HERE_ELSE_PART");
//                                        activityValue = "walking";
//                                    }
//
//                                    //sport type
//                                    String sportTypeStr = childSmartBandSegmentDataBean1.getSportType();
//
//                                    //date
//                                    String dayStr;
//                                    String monthStr;
//                                    int day = Integer.parseInt(childSmartBandSegmentDataBean1.getDay());
//                                    int month = Integer.parseInt(childSmartBandSegmentDataBean1.getMonth());
//                                    if (day < 10) {
//                                        dayStr = "0" + day;
//                                    } else {
//                                        dayStr = childSmartBandSegmentDataBean1.getDay();
//                                    }
//                                    if (month < 10) {
//                                        monthStr = "0" + month;
//                                    } else {
//                                        monthStr = childSmartBandSegmentDataBean1.getMonth();
//                                    }
//                                    String dateStr = childSmartBandSegmentDataBean1.getYear() + "-" + monthStr + "-" + dayStr;
//
//
//                                    try {
//                                        JSONObject jsonObjectData = new JSONObject();
//                                        jsonObjectData.put("start_time", startTimeLong);
//                                        jsonObjectData.put("end_time", endTimeLong);
//                                        jsonObjectData.put("duration", timeFormatted);
//                                        jsonObjectData.put("distance", new DecimalFormat("##.##").format(km));
//                                        jsonObjectData.put("speed", "");
//                                        jsonObjectData.put("steps", stepsValue);
//                                        jsonObjectData.put("calories", caloriesValue);
//                                        jsonObjectData.put("activity", activityValue);
//                                        jsonObjectData.put("sport_type", sportTypeStr);
//                                        jsonObjectData.put("created_at", dateStr);
//                                        jsonObjectData.put("synced_by", "1");
//
//                                        jsonSportData.put(jsonObjectData);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                                // sport web service hit here
//
////                                final ProgressDialog pDialog = Utilities.createProgressDialog(ChildHomeSmartBandScreen.this);
////                                pDialog.onStart();
//                                try {
////                                    new Handler().postDelayed(new Runnable() {
////                                        @Override
////                                        public void run() {
////                                            pDialog.cancel();
//                                            try{
////                                                System.out.println("hereTrueCond:");
//                                                if (Utilities.isNetworkAvailable(ChildHomeSmartBandScreen.this)) {
////                                                    System.out.println("valueHere::" + jsonSportData.toString());
//                                                    if (jsonSportData.toString().equalsIgnoreCase("[]")) {
//                                                        Toast.makeText(ChildHomeSmartBandScreen.this, "Refreshed successfully", Toast.LENGTH_SHORT).show();
//                                                    } else {
//
//                                                        List<NameValuePair> nameValuePairList = new ArrayList<>();
//                                                        nameValuePairList.add(new BasicNameValuePair("segment_data", jsonSportData.toString()));
//
//                                                        String webServiceUrl = Utilities.BASE_URL + "children/save_track_workout";
//
//                                                        ArrayList<String> headers = new ArrayList<>();
//                                                        headers.add("X-access-uid:" + loggedInUser.getId());
//                                                        headers.add("X-access-token:" + loggedInUser.getToken());
//
//                                                        PostWebServiceWithHeadersAsync postWebServiceAsync = new PostWebServiceWithHeadersAsync(ChildHomeSmartBandScreen.this, nameValuePairList, SAVE_SPORT, ChildHomeSmartBandScreen.this, headers);
//                                                        postWebServiceAsync.execute(webServiceUrl);
//                                                    }
//
//                                                } else {
//                                                    Toast.makeText(ChildHomeSmartBandScreen.this, R.string.internet_not_available, Toast.LENGTH_SHORT).show();
//                                                }
//                                            }catch(Exception e){
//                                                e.printStackTrace();
//                                            }
//
//
//
////                                        }
////                                    }, 6000);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//
//                            }
//
//
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//
//
//                    }else if(result instanceof  TB_v3_heartRate_data_hours){
//                        data.add(result.toJson());
//                        mAdapter.notifyDataSetChanged();
//                        String segmentHeartRateData = result.toJson();
////                        System.out.println("segmentHeartRateData::" + segmentHeartRateData);
//                    }
////                    System.out.println("CMD_SEGMENT_DATA" + result.toJson());
//                    break;
//                case CMD_HEARTRATE_DATA_HOUR:
//                    TB_v3_heartRate_data_hours heartrate_hour= (TB_v3_heartRate_data_hours) result;
//                    data.add(heartrate_hour.toJson());
////                    System.out.println("CMD_HEARTRATE_DATA_HOUR" + heartrate_hour.toJson());
//
//                    try{
//                        ChildSmartBandHeartRateHourDataBean   childSmartBandHeartRateHourDataBean = new ChildSmartBandHeartRateHourDataBean();
//                        JSONObject jsonObject = new JSONObject(heartrate_hour.toJson());
//                        if(jsonObject.getString("hours").equalsIgnoreCase("255")){
////                            System.out.println("NoDataHEREheartRate::");
//                        }else {
////                            String day = jsonObject.getString("day");
////                            String hours = jsonObject.getString("hours");
////                            String month = jsonObject.getString("month");
////                            String time_stamp = jsonObject.getString("time_stamp");
////                            String week = jsonObject.getString("week");
////                            String year = jsonObject.getString("year");
//                            String detail_data = jsonObject.getString("detail_data");
//
//                            childSmartBandHeartRateHourDataBean.setDay(jsonObject.getString("day"));
//                            childSmartBandHeartRateHourDataBean.setHours(jsonObject.getString("hours"));
//                            childSmartBandHeartRateHourDataBean.setMonth(jsonObject.getString("month"));
//                            childSmartBandHeartRateHourDataBean.setTimeStamp(jsonObject.getString("time_stamp"));
//                            childSmartBandHeartRateHourDataBean.setWeek(jsonObject.getString("week"));
//                            childSmartBandHeartRateHourDataBean.setYear(jsonObject.getString("year"));
//                            childSmartBandHeartRateHourDataBean.setDetailData(jsonObject.getString("detail_data"));
//
//                            try{
//                                detail_data = detail_data.replace("[", "");
//                                detail_data = detail_data.replace("]", "");
//
//                                ArrayList<Integer> heartRatevalue = new ArrayList<>();
//                                String[] heartRate = detail_data.split(",");
//                                for (String single : heartRate) {
////                                    System.out.println("singleValue::" + single);
//                                    if (single.equalsIgnoreCase("[255") || single.equalsIgnoreCase("255") || single.equalsIgnoreCase("255]")) {
//
//                                    } else {
//                                        heartRatevalue.add(Integer.parseInt(single));
//
//
////                                        System.out.println("MaxHeartRate::" + Collections.max(heartRatevalue));
////                                        System.out.println("MinHeartRate::" + Collections.min(heartRatevalue));
//
//                                        maximumHeart.setText(Collections.max(heartRatevalue) + "/" + Collections.min(heartRatevalue));
//                                        //minimumHeart.setText(Collections.min(heartRatevalue) + " Minimum Heart Rate");
//
//                                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                                        editor.putString("heartRateMax", "" + Collections.max(heartRatevalue));
//                                        editor.putString("heartRateMin", "" + Collections.min(heartRatevalue));
//                                        editor.commit();
//
//
//
//                                        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//                                        Gson gson = new Gson();
//                                        String response = sharedPreferences.getString("heartRateData", null);
//                                        if (response == null) {
//                                        } else {
//                                            childSmartBandHeartRateHourDataBeanArrayList = gson.fromJson(response, heartRateListOfObjects);
//                                        }
//
//                                        if (childSmartBandHeartRateHourDataBeanArrayList != null && !childSmartBandHeartRateHourDataBeanArrayList.isEmpty()) {
////                                            System.out.println("hereIFHeartRate::");
//                                            final ChildSmartBandHeartRateHourDataBean childSmartBandHeartRateHourDataBean1 = childSmartBandHeartRateHourDataBeanArrayList.get(0);
//                                            if (childSmartBandHeartRateHourDataBean1.getDay().equalsIgnoreCase(jsonObject.getString("day")) &&
//                                                    childSmartBandHeartRateHourDataBean1.getWeek().equalsIgnoreCase(jsonObject.getString("week"))) {
////                                                System.out.println("hereInnerIFHeartRate::");
//                                                childSmartBandHeartRateHourDataBeanArrayList.add(childSmartBandHeartRateHourDataBean);
//                                            } else {
////                                                System.out.println("hereInnerELSEheartRate::");
//                                                //sharedPreferences.edit().remove("segmentData").commit();
//                                                sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//                                                SharedPreferences.Editor editor2 = sharedPreferences.edit();
//                                                editor2.remove("heartRateData");
//                                                editor2.apply();
//                                                childSmartBandHeartRateHourDataBeanArrayList.clear();
//
//                                                childSmartBandHeartRateHourDataBeanArrayList.add(childSmartBandHeartRateHourDataBean);
//
//
//                                            }
//
//                                        } else {
////                                            System.out.println("hereELSEheartRate::");
//                                            childSmartBandHeartRateHourDataBeanArrayList.add(childSmartBandHeartRateHourDataBean);
//                                        }
//
//                                        heartRateDataExist=true;
//                                        storeSegementListData();
//
//
//                                        convertHeartRateDataToWebserviceJson();
//                                        heartRateDataAvailable = true;
//
//                                    }
//                                }
//
//
//
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//
//
//
//
//                        }
//
//                    }catch (Exception e){
//                        e.printStackTrace();
//
//                    }
//
//
//                    mAdapter.notifyDataSetChanged();
//                    break;
//
//                case CMD_schedule:
//                case CMD_schedule_info:
//                    ScheduleResult scheduleResult= (ScheduleResult) result;
//                    data.add(scheduleResult.toJson());
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                case CMD_user_info:
//                    /**
//                     * gender 1:girl  0:boy
//                     */
//                    UserInfo user= (UserInfo) result;
//                    data.add(user.toJson());
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                case CMD_device_settings:
//                    /***
//                     *  on :1 off:0  .If the bracelet is not supported, the default is 0
//                     *  unit 0:metric  1:Inch
//                     */
//                    DeviceSetting device= (DeviceSetting) result;
//                    data.add(device.toJson());
//                    boolean flag=WristBandDevice.getInstance(mContext).isConnected();
//                    data.add(flag+"");
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                case CMD_alarm_info:
//                    Alerm alarm = (Alerm) result;
//                    data.add(alarm.toJson());
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                case CMD_quiet_mode_info:
//                    QuietModeInfo modeInfo = (QuietModeInfo) result;
//                    data.add(modeInfo.toJson());
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                default:
//                    break;
//            }
//            mAdapter.notifyDataSetChanged();
//        }
//
//
//
//    }
//    private WristBandDevice getWristBand() {
//        WristBandDevice device=WristBandDevice.getInstance(mContext);
//        device.setCallbackHandler(myCallback);
//        return device;
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
//
//    public void syncdataLoader(){
//        final ProgressDialog pDialogSync = Utilities.createProgressDialog(ChildHomeSmartBandScreen.this);
//        pDialogSync.onStart();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // current data
//                byte[] dailyData = null;
//                dailyData = getWristBand().setWristBand_3BVersion_DialydataCurr(1);
//                WriteOneDataTask dailyTask = new WriteOneDataTask(mContext, dailyData);
//                NewAgreementBackgroundThreadManager.getInstance().addTask(dailyTask);
//
//                //chunk data
//                byte[] segmentDataByte = null;
//                segmentDataByte = getWristBand().setWristBand_3BVersion_Dialydata(1, true, 0);
//                WriteOneDataTask segmentData = new WriteOneDataTask(mContext, segmentDataByte);
//                NewAgreementBackgroundThreadManager.getInstance().addTask(segmentData);
//
//                //hour heartRateData
//                byte[] heartRateData = null;
//                heartRateData = getWristBand().syncHeartRateHoursData(1);
//                WriteOneDataTask heartRateTask = new WriteOneDataTask(mContext, heartRateData);
//                NewAgreementBackgroundThreadManager.getInstance().addTask(heartRateTask);
//
//                //Toast.makeText(ChildHomeSmartBandScreen.this, "Refreshed successfully", Toast.LENGTH_SHORT).show();
//                pDialogSync.cancel();
//            }
//        }, 5000);
//
//    }
//
//    public void storeSegementListData(){
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(childSmartBandSegmentDataBeanArrayList);
//        String jsonHeartRate = gson.toJson(childSmartBandHeartRateHourDataBeanArrayList);
//
//        if(sportDataExist==true){
//            editor.putString("segmentData", json);
//            sportDataExist=false;
//        }
//        if(heartRateDataExist==true){
//            editor.putString("heartRateData", jsonHeartRate);
//            heartRateDataExist=false;
//        }
//        editor.commit();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkPermissions()){
////                Toast.makeText(ChildTrackWorkoutScreen.this, "Permissions already granted", Toast.LENGTH_SHORT).show();
//            } else {
////                Toast.makeText(ChildTrackWorkoutScreen.this, "Initially permission not available", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }
//
//    private boolean checkPermissions() {
//        int result;
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        for (String p:permissions) {
//            result = ContextCompat.checkSelfPermission(ChildHomeSmartBandScreen.this, p);
//            if (result != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(p);
//            }
//        }
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MULTIPLE_PERMISSIONS:{
//                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    // permissions granted.
//                } else {
//                    // no permissions granted.
//                    Toast.makeText(ChildHomeSmartBandScreen.this, "This feature cannot work without Permissions. \nTry Again and allow permissions or allow permissions in Applications Settings", Toast.LENGTH_LONG).show();
//                    finish();
//                }
//                return;
//            }
//        }
//    }
//
//    public void sportScreenData(){
//        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//
//        Gson gson = new Gson();
//        String response=sharedPreferences.getString("segmentData" , null);
//        storedChildSmartBandSegmentDataBeanArrayList = gson.fromJson(response, listOfObjects);
//
//        steps = sharedPreferences.getString("steps", null);
//        calories = sharedPreferences.getString("calories",null);
//        distance = sharedPreferences.getString("distance",null);
//        day = sharedPreferences.getString("day", null);
//        month = sharedPreferences.getString("month", null);
//        year = sharedPreferences.getString("year", null);
//
//        Intent obj = new Intent(ChildHomeSmartBandScreen.this,  ChildSportSmartBandScreen.class);
//        obj.putExtra("calories", calories);
//        obj.putExtra("steps", steps);
//        obj.putExtra("distance", distance);
//        obj.putExtra("bluetoothStatus", bluetoothStatus);
//        obj.putExtra("childSmartBandSegmentDataBeanArrayList", storedChildSmartBandSegmentDataBeanArrayList);
//        obj.putExtra("dateStr",day+"-"+month+"-"+year);
//        startActivity(obj);
//    }
//
//    public void heartRateScreenData(){
//        sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//
//        Gson gson = new Gson();
//        String response=sharedPreferences.getString("heartRateData" , null);
//        storedChildSmartBandHeartRateHourDataBeanArrayList = gson.fromJson(response, heartRateListOfObjects);
//
//                heartRateMax = sharedPreferences.getString("heartRateMax", null);
//                heartRateMin = sharedPreferences.getString("heartRateMin", null);
//
//        Intent obj = new Intent(ChildHomeSmartBandScreen.this,  ChildHeartRateSmartBandScreen.class);
//        obj.putExtra("heartRateMin", heartRateMin);
//        obj.putExtra("heartRateMax", heartRateMax);
//        obj.putExtra("bluetoothStatus", bluetoothStatus);
//        obj.putExtra("childSmartBandHeartRateHourDataBeanArrayList", storedChildSmartBandHeartRateHourDataBeanArrayList);
//        startActivity(obj);
//    }
//
//    public void convertHeartRateDataToWebserviceJson(){
//        jsonArray = new JSONArray();
//        for(int i=0; i<childSmartBandHeartRateHourDataBeanArrayList.size();i++){
//            final ChildSmartBandHeartRateHourDataBean childSmartBandHeartRateHourDataBean = childSmartBandHeartRateHourDataBeanArrayList.get(i);
//            String day = childSmartBandHeartRateHourDataBean.getDay();
//            String month = childSmartBandHeartRateHourDataBean.getMonth();
//            String year = childSmartBandHeartRateHourDataBean.getYear();
//            String hours = childSmartBandHeartRateHourDataBean.getHours();
//            String detail_data = childSmartBandHeartRateHourDataBean.getDetailData();
//            try{
////                System.out.println("Before_detail_data::"+detail_data);
//                detail_data = detail_data.replace("[", "");
//                detail_data = detail_data.replace("]", "");
////                System.out.println("After_detail_data::"+detail_data);
//
//                ArrayList<Integer> heartRateValue = new ArrayList<>();
//                String[] heartRate = detail_data.split(",");
//                for (String single : heartRate) {
////                    System.out.println("singleValue::" + single);
//                    if (single.equalsIgnoreCase("[255") || single.equalsIgnoreCase("255") || single.equalsIgnoreCase("255]")) {
//
//                    } else {
//                        heartRateValue.add(Integer.parseInt(single));
//                    }
//                }
//                int max = Collections.max(heartRateValue);
//                int min = Collections.min(heartRateValue);
//
//                JSONObject jsonObject = new JSONObject();
//                int monthValue = Integer.parseInt(month);
//                int dayValue = Integer.parseInt(day);
//                if(monthValue<10){
//                    month = "0"+month;
//                }
//                if(dayValue<10){
//                    day = "0"+day;
//                }
//                jsonObject.put("date", year+"-"+month+"-"+day);
//                jsonObject.put("max_val", max);
//                jsonObject.put("min_val", min);
//                jsonObject.put("hours", hours);
//
//                jsonArray.put(jsonObject);
//
////                System.out.println("jsonArrayHere"+jsonArray.toString());
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void clickBluetooth(){
//        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
////      System.out.println("connectValue::"+WristBandDevice.getInstance(mContext).connect());
//
//
//        if(WristBandDevice.getInstance(mContext).isConnected()==false){
//            if (!mBluetoothAdapter.isEnabled()){
//                try {
//                    final ProgressDialog pDialog = Utilities.createProgressDialog(ChildHomeSmartBandScreen.this);
//                    pDialog.onStart();
//
//                    mBluetoothAdapter.enable();
//
//                    WristBandDevice.getInstance(mContext).connect();
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            pDialog.cancel();
//                            showDeviceScanningDialog();
//                        }
//                    }, 3000);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }else{
//                showDeviceScanningDialog();
//            }
//        }else{
//            AlertDialog.Builder builder;
//            builder = new AlertDialog.Builder(ChildHomeSmartBandScreen.this);
//            builder.setTitle(R.string.ifa_dialog)
//                    .setMessage("Are you sure you want to disconnect Bluetooth?")
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            WristBandDevice.getInstance(mContext).disconnect();
//                            bluetoothStatus="disconnected";
//                            mBluetoothAdapter.disable();
//                            Toast.makeText(ChildHomeSmartBandScreen.this, "Bluetooth disabled successfully", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // do nothing
//                        }
//                    })
//                    .setIcon(R.drawable.placeholder)
//                    .show();
//        }
//    }
//}
//
//
//
////心率数据HeartRateDetial [sport_type=240, start_time=610, end_time=610, energy=0.6, r1Time=0, r2Time=0, r3Time=0, r4Time=0, r5Time=0, r1Energy=0.0, r2Energy=0.0, r3Energy=0.0, r4Energy=0.0, r5Energy=0.0, r1Hr=0, r2Hr=0, r3Hr=0, r4Hr=0, r5Hr=0]
////
////        CMD_SETTING_HEARTRATE_PARAMS{"heartrateExist":2,"statue":0,"strong":1,"time":5,"version":0}
////
////
////        CMD_HEARTRATE_DATA_HOUR{"_uploaded":0,"day":19,"detail_data":[255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255],"hours":0,"month":6,"time_stamp":12577,"uid":0,"week":25,"year":2017}
////
////        CMD_HEARTRATE_DATA_HOUR{"_uploaded":0,"day":19,"detail_data":[255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255],"hours":1,"month":6,"time_stamp":12578,"uid":0,"week":25,"year":2017}
////
////        CMD_HEARTRATE_DATA_HOUR{"_uploaded":0,"day":19,"detail_data":[255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255],"hours":2,"month":6,"time_stamp":12579,"uid":0,"week":25,"year":2017}
////
////        CMD_HEARTRATE_DATA_HOUR{"_uploaded":0,"day":19,"detail_data":[255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255],"hours":3,"month":6,"time_stamp":12580,"uid":0,"week":25,"year":2017}
////
////        CMD_HEARTRATE_DATA_HOUR{"_uploaded":0,"day":19,"detail_data":[255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255],"hours":4,"month":6,"time_stamp":12581,"uid":0,"week":25,"year":2017}
////
////        CMD_HEARTRATE_DATA_HOUR{"_uploaded":0,"day":19,"detail_data":[255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255],"hours":5,"month":6,"time_stamp":12582,"uid":0,"week":25,"year":2017}
////
////        CMD_HEARTRATE_DATA_HOUR{"_uploaded":0,"day":19,"detail_data":[255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255],"hours":6,"month":6,"time_stamp":12583,"uid":0,"week":25,"year":2017}
////
////        CMD_HEARTRATE_DATA_HOUR{"_uploaded":0,"day":19,"detail_data":[255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255],"hours":7,"month":6,"time_stamp":12584,"uid":0,"week":25,"year":2017}
////
////        CMD_HEARTRATE_DATA_HOUR{"_uploaded":0,"day":19,"detail_data":[255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255],"hours":8,"month":6,"time_stamp":12585,"uid":0,"week":25,"year":2017}
////
////        CMD_HEARTRATE_DATA_HOUR{"_uploaded":0,"day":19,"detail_data":[255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255],"hours":9,"month":6,"time_stamp":12586,"uid":0,"week":25,"year":2017}
////
////        CMD_HEARTRATE_DATA_HOUR{"_uploaded":0,"day":255,"detail_data":[255,255,255,255,255,255,255,66,66,66,66,67,67,67,67,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255,255],"hours":255,"month":255,"time_stamp":583935,"uid":0,"week":51,"year":255}
