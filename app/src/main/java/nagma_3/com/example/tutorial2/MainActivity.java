package nagma_3.com.example.tutorial2;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import io.michaelrocks.paranoid.Obfuscate;
import nagma_3.com.example.tutorial2.Database.DataBaseHelper;
import nagma_3.com.example.tutorial2.model.UserDetailsEntity;
import nagma_3.com.example.tutorial2.workmanager.LocationBCReciever;

@Obfuscate
public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION = 99;

    private SensorManager sensorManager;
    private Button SpeedBreaker;
    private Button PotHole;

    private Button Stop;

    Sensor accelerometer, mGyro;


    TextView xAccValue, yAccValue, zAccValue, xGyroValue, yGyroValue, zGyroValue, lat_txt, long_txt, checkspeed;
    TextView tv_count,tv_acc_count,tv_gyro_count,tv_timer;

    LocationManager locationManager;
    LocationManager mlocManager = null;

    Location loc;
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;

    String latitude, longitud;

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;

    LocationTrack locationTrack;
    private ProgressDialog dialog;

    static Location LastLocation = null;
    private final int UPDATE_LATLNG = 2;
    DataBaseHelper dataBaseHelper;

    String accValx = "";
    String accValy = "";
    String accValz = "";
    String gyroValx = "";
    String gyroValy = "";
    String gyroValz = "";

    int counts;
    long start;

    double lat1, lon1, distance, speed, lon2, lat2;

    int insertCount = 0;
    int insertAccCount = 0;
    int insertGyroCount = 0;
    long finish;

    int seconds = 0, minutes = 0, hour = 0;

    //alt2 = location.getAltitude();
    long time;
    double time1;
    String speedfinal = "";
    String annotate = "";

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor sensors;

    private LineChart mChart;
    private Thread thread;
    private boolean plotData = true;

    LocationBCReciever receiver;
    IntentFilter intentFilter;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this, "data recieved successful!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "data recieved");
            //displayToast(" Data received");
            lat_txt = (TextView) findViewById(R.id.lat_txt);
            long_txt = (TextView) findViewById(R.id.long_txt);
            String s1 = intent.getStringExtra("latitude");
            String s2 = intent.getStringExtra("longitude");
            lat_txt.setText(s1);
            long_txt.setText(s2);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //Intent serviceIntent = new Intent(MainActivity.this, TrackLocationService.class);
        //Log.d(TAG,"accValx from main activity"+accValx);
        //serviceIntent.putExtra("accValx",accValx);
        //serviceIntent.putExtra("accValy",accValy);
        //serviceIntent.putExtra("accValz",accValz);
        //serviceIntent.putExtra("gyroValx",gyroValx);
       // serviceIntent.putExtra("gyroValy",gyroValy);
       // serviceIntent.putExtra("gyroValz",gyroValz);
        //startService(serviceIntent);
       // serviceIntent.putExtra("accValx",accValx);


        //startService(new Intent(MainActivity.this, TrackLocationService.class));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MAIN");
        registerReceiver(broadcastReceiver, intentFilter);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBaseHelper = new DataBaseHelper(this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        //setupDatabase();

        xAccValue = (TextView) findViewById(R.id.xValue);
        yAccValue = (TextView) findViewById(R.id.yValue);
        zAccValue = (TextView) findViewById(R.id.zValue);
        xGyroValue = (TextView) findViewById(R.id.xGyroValue);
        yGyroValue = (TextView) findViewById(R.id.yGyroValue);
        zGyroValue = (TextView) findViewById(R.id.zGyroValue);


        tv_count =  findViewById(R.id.tv_count);
        tv_acc_count =  findViewById(R.id.tv_acc_count);
        tv_gyro_count =  findViewById(R.id.tv_gyro_count);
        tv_timer =  findViewById(R.id.tv_timer);

        checkspeed = (TextView) findViewById(R.id.checkspeed);

        dialog = new ProgressDialog(this);

        long e = dataBaseHelper.deleteSchemeRecord();
        if(e < 0){
                Toast.makeText(MainActivity.this, "Previous Record  Deleted",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "Previous Record  Not Deleted",Toast.LENGTH_SHORT).show();
            }


        SpeedBreaker = (Button) findViewById(R.id.SpeedBreaker);

        Stop = (Button) findViewById(R.id.Stop);
        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unregisterReceiver(receiver);

                receiver = new LocationBCReciever();
                intentFilter = new IntentFilter(LocationBCReciever.ACTION_STOP);
                registerReceiver(receiver, intentFilter);
//
                Intent intent = new Intent(LocationBCReciever.ACTION_STOP);
                sendBroadcast(intent);
                //unregisterReceiver(receiver);
                finish();

            }
        });


        PotHole = (Button) findViewById(R.id.PotHole);

        setupSensor();
        //setupLocation();
        //startBCReciver();
        setupChart();

        startTimer();

        //feedMultiple();

        SpeedBreaker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                annotate = "Speedbreaker";
                if (annotate == "") {
                    Toast.makeText(MainActivity.this, "Annotation Not Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Annotation Inserted", Toast.LENGTH_SHORT).show();
                }

            }

        });




        PotHole.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                annotate = "Pothole";
                if (annotate == "") {
                    Toast.makeText(MainActivity.this, "Annotation Not Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Annotation Inserted", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }


    public void startBCReciver() {
        Log.d(TAG,"on start button1");
//        receiver = new LocationBCReciever();
//        intentFilter = new IntentFilter(LocationBCReciever.ACTION_TRACK);

//        Intent intentToFire = new Intent();
//        //intentToFire.setComponent(new ComponentName("app.bih.in.nic.assamlocationcapture","app.bih.in.nic.assamlocationcapture.ui.MyAlarmReceiver"));
//
//        // Intent intentToFire = new Intent(getBaseContext(), MyAlarmReceiver.class);
//        intentToFire.setAction(LocationBCReciever.ACTION_TRACK);
//        sendBroadcast(intentToFire);

//        PendingIntent alarmIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intentToFire, 0);
//        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
//
//        long interval = 1 * 500 * 1; // 2 minute
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.SECOND, 1);
//        long afterTenSeconds = c.getTimeInMillis();
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, afterTenSeconds, interval, alarmIntent);
        Log.d(TAG,"on start button2");
        receiver = new LocationBCReciever();
        intentFilter = new IntentFilter(LocationBCReciever.ACTION_TRACK);
        registerReceiver(receiver, intentFilter);

        Intent intent = new Intent(LocationBCReciever.ACTION_TRACK);
        sendBroadcast(intent);
        Log.d(TAG,"on start button4");
    }


    public void startTimer(){
        Timer timer = new Timer();
        Log.d(TAG,"on start button5");

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Log.d(TAG,"on start button6");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"on start button7");
                        if (seconds == 60) {
                            Log.d(TAG,"on start button3");
                            tv_timer.setText(String.format("%02d", hour) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                            minutes = seconds / 60;
                            seconds = seconds % 60;
                            hour = minutes / 60;
                        }
                        seconds += 1;
                        tv_timer.setText(String.format("%02d", hour) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                    }
                });
            }
        }, 0, 1000);
    }

    public void setupChart(){
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 0; i < sensors.size(); i++) {
            Log.d(TAG, "onCreate: Sensor " + i + ": " + sensors.get(i).toString());
        }

        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        }

        mChart = (LineChart) findViewById(R.id.chart1);

        // enable description text
        mChart.getDescription().setEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(10f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setDrawBorders(false);
        mChart.getDescription().setText("Graph for Accelerometer Data");
    }

    private void addEntry(SensorEvent event) {

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

//            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 80) + 10f), 0);
            data.addEntry(new Entry(set.getEntryCount(), event.values[0] + 5), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(150);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());

        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.MAGENTA);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }

    private void feedMultiple() {

        if (thread != null) {
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    plotData = true;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (thread != null) {
            thread.interrupt();
        }
        //mSensorManager.unregisterListener(this);
        //unregisterReceiver(receiver);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //getContext().registerReceiver(broadcastReceiver, new IntentFilter());
//        receiver = new LocationBCReciever();
//        intentFilter = new IntentFilter(LocationBCReciever.ACTION_TRACK);
        //registerReceiver(receiver, intentFilter);

//        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
//        setupSensor();
    }



    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);

//        mlocManager.removeUpdates(mlistener);
//        mSensorManager.unregisterListener(MainActivity.this);
//        locationManager.removeUpdates(mlistener);
        //thread.interrupt();
        super.onDestroy();
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.example.andy.myapplication");
//        registerReceiver(broadcastReceiver, intentFilter);
//    }

    /*public void setupLocation() {
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        locationManager();
    }
*/
    public void setupSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered accelerometer listener");


        } else {
            xAccValue.setText("Accelerometer Not Supported");
            yAccValue.setText("Accelerometer Not Supported");
            zAccValue.setText("Accelerometer Not Supported");
        }
        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mGyro != null) {
            sensorManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Gyro listener");
        } else {
            xGyroValue.setText("Gyroscope Not Supported");
            yGyroValue.setText("Gyroscope Not Supported");
            zGyroValue.setText("Gyroscope Not Supported");
        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            Log.e("SensorDebug", "onSensorChangedAcc: X:" + sensorEvent.values[0] + "Y: " + sensorEvent.values[1] + "Z: " + sensorEvent.values[2]);

            accValx = Double.toString(sensorEvent.values[0]);
            accValy = Double.toString(sensorEvent.values[1]);
            accValz = Double.toString(sensorEvent.values[2]);

            insertAccCount += 1;
            tv_acc_count.setText(""+insertAccCount);

            xAccValue.setText(String.format("%.3f",sensorEvent.values[0]));
            yAccValue.setText(String.format("%.3f",sensorEvent.values[1]));
            zAccValue.setText(String.format("%.3f",sensorEvent.values[2]));

            //Toast.makeText(MainActivity.this, "Record  Inserted",Toast.LENGTH_SHORT).show();
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            Log.d(TAG, "onSensorChangedGyro: X:" + sensorEvent.values[0] + "Y: " + sensorEvent.values[1] + "Z: " + sensorEvent.values[2]);
            gyroValx = Double.toString(sensorEvent.values[0]);
            gyroValy = Double.toString(sensorEvent.values[0]);
            gyroValz = Double.toString(sensorEvent.values[0]);

            insertGyroCount += 1;
            tv_gyro_count.setText(""+insertGyroCount);

            xGyroValue.setText(String.format("%.3f",sensorEvent.values[0]));
            yGyroValue.setText(String.format("%.3f",sensorEvent.values[1]));
            zGyroValue.setText(String.format("%.3f",sensorEvent.values[2]));

            //Intent intent2 = new Intent();
            //intent2.setAction("android.intent.action.MAIN");
            //sendBroadcast(intent2);
            Intent i = new Intent(MainActivity.this, TrackLocationService.class);
            Log.d(TAG,"accValx from main activity"+accValx);
            i.putExtra("accValx",accValx);
            i.putExtra("accValy",accValy);
            i.putExtra("accValz",accValz);
            i.putExtra("gyroValx",gyroValx);
            i.putExtra("gyroValy",gyroValy);
            i.putExtra("gyroValz",gyroValz);
            startService(i);
           // sendBroadcast(intent2);

        }

        if (plotData) {
            addEntry(sensorEvent);
            plotData = false;

//            annotate.setText(Annotation);

        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void locationManager() {
        //dialog.setMessage("Tracking Location...");
        dialog.show();

        //if (GlobalVariables.glocation == null) {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
//
//
//        }else {
        } else {
            mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, (float) 0.0, mlistener);
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, (float) 0.0, mlistener);

        }

    }

    private void updateUILocation(Location location) {

        Message.obtain(
                mHandler,
                UPDATE_LATLNG,
                new DecimalFormat("#.0000000").format(location.getLatitude())
                        + "-"
                        + new DecimalFormat("#.0000000").format(location
                        .getLongitude()) + "-" + location.getAccuracy() + "-" + location.getTime())
                .sendToTarget();

    }

    private final LocationListener mlistener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            if (Utiilties.isGPSEnabled(MainActivity.this)) {

                LastLocation = location;
                if (location.getLatitude() > 0.0) {
                    if (location.getAccuracy() > 0 && location.getAccuracy() < 150) {

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        latitude = Double.toString(location.getLatitude());
                        longitud = Double.toString(location.getLongitude());
                        Log.e(TAG, "onLocationChanged: "+latitude+""+longitud );
                        //lat_txt.setText("" + latitude);
                        //long_txt.setText("" + longitud);
                        checkspeed.setText("" + Double.toString(0.0) + "Km/h");

                    } else {
                        if (this != null){
                            dialog.setMessage("Wait for gps to become stable");
                            dialog.show();
                        }
                    }


                    if (counts == 0) {
                        start = System.nanoTime();//a timer starts
                        lat1 = location.getLatitude();//the coordinates are saved
                        lon1 = location.getLongitude();
                        distance = 0;//I initialize the distance
                        //alt1 = location.getAltitude();//this would also save the altitude
                        counts += 1;
                    } else if (counts == 5) {//if the location has changed 5 times (I choose 5 times nad not 0 in order for the calculations to be less)
                        finish = System.nanoTime();//I finish the timer
                        lat2 = location.getLatitude();//the coordinates are saved
                        lon2 = location.getLongitude();
//                       alt2 = location.getAltitude();
                        time = finish - start;//I save the time in nanoseconds
                        time1 = (double) time / 1_000_000_000.0;//I convert time in seconds
                        distance = distance + measureDistance(lat1, lat2, lon1, lon2);//I calculate the distance between two points through my custom method measureDistance
                        speed = distance / time1;//calculate the speed in meters/second
                        speed = speed * 3.6;//convert speed from m/s to km/h
                        speed = (int) speed;//i get rid of the decimal numbers
                        speedfinal = Double.toString(speed);

                        checkspeed.setText("" + Double.toString(speed) + "Km/h");//the speed is shown

                        start = 0;//restart time and counter
                        finish = 0;
                        counts = 0;
                    } else {
                        lat2 = location.getLatitude();
                        lon2 = location.getLongitude();
                        distance = distance + measureDistance(lat1, lat2, lon1, lon2);//i calculate the distance travelled
                        lat1 = lat2;
                        lon1 = lon2;
                        counts += 1;
                    }


                }


            } else {
                Message.obtain(
                        mHandler,
                        UPDATE_LATLNG,
                        new DecimalFormat("#.0000000").format(location.getLatitude())
                                + "-"
                                + new DecimalFormat("#.0000000").format(location
                                .getLongitude()) + "-" + location.getAccuracy() + "-" + location.getTime())
                        .sendToTarget();
            }


            UserDetailsEntity record = new UserDetailsEntity(accValx.toString(), accValy.toString(), accValz.toString(), gyroValy.toString(), gyroValz.toString(), gyroValx.toString(), latitude.toString(), longitud.toString(), speedfinal.toString(), annotate.toString());
//            long c = dataBaseHelper.insertSurfaceUserDetails(record);
            dataBaseHelper.insertSurfaceUserDetails(record);
            annotate = "";
            insertCount += 1;
            tv_count.setText("I: "+insertCount);
//            if(c < 0){
//                Toast.makeText(MainActivity.this, "Record Not Inserted",Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(MainActivity.this, "Record Inserted",Toast.LENGTH_SHORT).show();
//            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }


    };


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_LATLNG:
                    String[] LatLon = ((String) msg.obj).split("-");
//
                    Log.e("", "Lat-Long" + LatLon[0] + "   " + LatLon[1]);


                    break;
            }
        }
    };

    public double distanceBetweenCoordinatesInMtrs(double lat1, double long1, double lat2, double long2) {
        Location startPoint = new Location("locationA");
        startPoint.setLatitude(lat1);
        startPoint.setLongitude(long1);

        Location endPoint = new Location("locationA");
        endPoint.setLatitude(lat2);
        endPoint.setLongitude(long2);

        double distance = startPoint.distanceTo(endPoint);
        return distance;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public double measureDistance(Double lat1, Double lat2, Double lon1, Double lon2) {//custom method that calculates distance between two points
        //formula found on the web
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        return distance;
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentTimeStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

//    private void ShareFile() {
//        File exportDir = getExternalFilesDir(null);
//        String fileName = "RoadData.csv";
//        File sharingGifFile = new File(exportDir, fileName);
//        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//        shareIntent.setType("application/csv");
//        Uri uri = Uri.fromFile(sharingGifFile);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        startActivity(Intent.createChooser(shareIntent, "Share CSV"));
//    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


//    @Override
//    protected void onStop() {
//        super.onStop();
//
//
//    }
}



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

