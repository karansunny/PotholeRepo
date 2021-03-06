package nagma_3.com.example.tutorial2.workmanager;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.JobIntentService;

import com.github.mikephil.charting.charts.LineChart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import nagma_3.com.example.tutorial2.Database.DataBaseHelper;
import nagma_3.com.example.tutorial2.LocationTrack;
import nagma_3.com.example.tutorial2.MainActivity;
import nagma_3.com.example.tutorial2.Utiilties;
import nagma_3.com.example.tutorial2.model.UserDetailsEntity;

public class TrackLocationService extends JobIntentService {

    private static final int JOB_ID = 2;
    private static final int REQUEST_LOCATION = 99;

    private SensorManager sensorManager;
    Sensor accelerometer, mGyro;

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
    private boolean isCounterActivated = false;

    ScheduledExecutorService scheduler;

    Context context;
    @Override
    public void onCreate() {
        //super.onCreate();
        Log.e("Service: ","Started");

        dataBaseHelper = new DataBaseHelper(this);

        setupLocation();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.e("Service: ","Started From Handle Work");


    }


    public static void enqueueWork(Context context, Intent intent)
    {
        //enqueueWork(context, TrackLocationService.class, JOB_ID, intent);
        //setupLocation(context);
    }

    @Override
    public void onDestroy()
    {
        Log.e("Stop Service", "onDestroy:MyLoc.Ser");
        mlocManager.removeUpdates(mlistener);

//        Intent intent = new Intent(this, TrackLocationService.class);
//        stopService(intent);
        super.onDestroy();

    }

    public void startTimer(){
//        Timer timer = new Timer();
//
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            synchronized public void run() {
//
//            }
//        },1000,1);

        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        // call service
                        Log.e("Pass Data Lat: ", latitude);
                    }
                }, 500, 5, TimeUnit.SECONDS);
    }

    public void setupLocation() {
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        locationManager();
    }

    private void locationManager() {
        Log.e("Location:", "Tracking Location...");
        //dialog.setMessage("Tracking Location...");
        //dialog.show();

        //if (GlobalVariables.glocation == null) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ActivityCompat.requestPermissions(, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
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

    private final LocationListener mlistener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            if (Utiilties.isGPSEnabled(getApplicationContext())) {
                Log.e("Location: ", "Location Changed");
                LastLocation = location;
                if (location.getLatitude() > 0.0) {
                    if (location.getAccuracy() > 0 && location.getAccuracy() < 150) {

//                        if (dialog.isShowing()) {
//                            dialog.dismiss();
//                        }

                        latitude = Double.toString(location.getLatitude());
                        longitud = Double.toString(location.getLongitude());
                        Log.e("Location: ", "Lat-"+latitude+", Long-"+longitud);
                        if(!isCounterActivated){
                            isCounterActivated = true;
                            startTimer();
                        }
//                        lat_txt.setText("" + latitude);
//                        long_txt.setText("" + longitud);
//                        checkspeed.setText("" + Double.toString(0.0) + "Km/h");

                    } else {
                        if (this != null){
//                            dialog.setMessage("Wait for gps to become stable");
//                            dialog.show();
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

                        //checkspeed.setText("" + Double.toString(speed) + "Km/h");//the speed is shown

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


            //UserDetailsEntity record = new UserDetailsEntity(accValx.toString(), accValy.toString(), accValz.toString(), gyroValy.toString(), gyroValz.toString(), gyroValx.toString(), latitude.toString(), longitud.toString(), speedfinal.toString(), annotate.toString());
//            long c = dataBaseHelper.insertSurfaceUserDetails(record);
            //dataBaseHelper.insertSurfaceUserDetails(record);
            annotate = "";
            insertCount += 1;
            Log.e("Location Count: ", ""+insertCount);
            //tv_count.setText("I: "+insertCount);
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
}
