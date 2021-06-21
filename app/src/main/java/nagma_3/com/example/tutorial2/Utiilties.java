package nagma_3.com.example.tutorial2;



import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;



public class Utiilties {

    public Utiilties() {
        // TODO Auto-generated constructor stub
    }

    public static long getDateDifferenceFromCurrentDate(String fdate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date fromDate = null, todayWithZeroTime=null;
        long daysDiff = 0;

        try {
            fromDate = dateFormat.parse(fdate);

//            Date today = new Date();
//            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(fromDate != null){
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(fromDate);

            long msDiff = Calendar.getInstance().getTimeInMillis() - cCal.getTimeInMillis();
            daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
        }

        return (daysDiff);
    }

    public static void ShowMessage(Context context, String Title, String Message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(Title);
        alertDialog.setMessage(Message);
        alertDialog.show();
    }


//    public static void showAlet(final Context context){
//
//        if (Utiilties.isOnline(context) == false) {
//            AlertDialog.Builder ab = new AlertDialog.Builder(context);
//            ab.setCancelable(false);
//            ab.setMessage(Html
//                    .fromHtml("<font color=#000000>Internet Connection is not avaliable..Please Turn ON Network Connection OR Continue With Off-line Mode..\nTo Turn ON Network Connection Press Yes Button else To Continue With Off-Line Mode Press No Button..</font>"));
//            ab.setPositiveButton("Turn On Network Connection",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog,
//                                            int whichButton) {
////                            GlobalVariables.isOffline = false;
//                            Intent I = new Intent(
//                                    Settings.ACTION_WIRELESS_SETTINGS);
//                            context.startActivity(I);
//                        }
//                    });
//            ab.setNegativeButton("Continue Offline",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog,
//                                            int whichButton) {
//
////                            GlobalVariables.isOffline = true;
//                        }
//                    });
//
//            ab.create().getWindow().getAttributes().windowAnimations = R.style.AppBaseTheme;
//
//            ab.show();
//        }else {
//
//            GlobalVariables.isOffline = false;
//            // new CheckUpdate().execute();
//        }
//
//    }


    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() == true);
    }

    public static Bitmap GenerateThumbnail(Bitmap imageBitmap,
                                           int THUMBNAIL_HEIGHT, int THUMBNAIL_WIDTH) {

        Float width = new Float(imageBitmap.getWidth());
        Float height = new Float(imageBitmap.getHeight());
        Float ratio = width / height;
        Bitmap CompressedBitmap = Bitmap.createScaledBitmap(imageBitmap,
                (int) (THUMBNAIL_HEIGHT * ratio), THUMBNAIL_WIDTH, false);

        return CompressedBitmap;
    }



    public static Bitmap DrawText(Activity activity, Bitmap mBitmap, String displaytext1,
                                  String displaytext2, String displaytext3, String displaytext4) {
        Bitmap bmOverlay = Bitmap.createBitmap(mBitmap.getWidth(),
                mBitmap.getHeight(), Bitmap.Config.ARGB_4444);
        // create a canvas on which to draw
        Canvas canvas = new Canvas(bmOverlay);

        Paint paint = new Paint();
        paint.setColor(activity.getResources().getColor(R.color.holo_red_dark));
        paint.setTextSize(40);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setFakeBoldText(false);
        paint.setShadowLayer(1, 0, 0, Color.BLACK);

        // if the background image is defined in main.xml, omit this line
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        canvas.drawText(displaytext1, 10, mBitmap.getHeight() - 100, paint);
        canvas.drawText(displaytext2, 10, mBitmap.getHeight() - 50, paint);

        canvas.drawText(displaytext3, 10, mBitmap.getHeight() - 150, paint);

        canvas.drawText(displaytext4, 10, mBitmap.getHeight() - 200, paint);
        // set the bitmap into the ImageView
        return bmOverlay;
    }


    public static Object deserialize(byte[] data) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getDateString() {
        SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");

        String newDateStr = postFormater.format(Calendar.getInstance().getTime());
        return newDateStr;
    }

    public static String getDateString(String Formats) {
        SimpleDateFormat postFormater = new SimpleDateFormat(Formats);

        String newDateStr = postFormater.format(Calendar.getInstance()
                .getTime());
        return newDateStr;
    }



//    public static void setActionBarBackground(Activity  activity)
//    {
//        ActionBar actionBar;
//        actionBar=activity.getActionBar();
//        Resources res = activity.getResources();
//        //Drawable drawable = res.getDrawable(R.drawable.digitallogo2);
//        actionBar.setBackgroundDrawable(drawable);
//    }


    public static void setStatusBarColor(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= 21) {

            Window window = activity.getWindow();


            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(Color.parseColor("#1565a9"));
        }
    }




    public static String getCurrentDate()
    {
        Calendar cal=Calendar.getInstance();
        int day=cal.get(Calendar.DAY_OF_MONTH);
        int month=cal.get(Calendar.MONTH);
        int year=cal.get(Calendar.YEAR);
        month=month+1;

        int h=cal.get(Calendar.HOUR);
        int m=cal.get(Calendar.MINUTE);
        int s=cal.get(Calendar.SECOND);

        String date=month+"/"+day+"/"+year;
        return date;

    }

    public static String getCurrentDateDMY()
    {
        Calendar cal=Calendar.getInstance();
        int day=cal.get(Calendar.DAY_OF_MONTH);
        int month=cal.get(Calendar.MONTH);
        int year=cal.get(Calendar.YEAR);
        month=month+1;

        int h=cal.get(Calendar.HOUR);
        int m=cal.get(Calendar.MINUTE);
        int s=cal.get(Calendar.SECOND);

        String date=day+"-"+month+"-"+year;
        return date;

    }




    public static String getCurrentDateWithTime() throws ParseException {

        SimpleDateFormat f = new SimpleDateFormat("MMM d,yyyy HH:mm");
        Date date=null;
        date=f.parse(getDateString());
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d,yyyy HH:mm a");
        String dateString = formatter.format(date);
        return dateString;
    }



    public static String getDateTime() {

        String date=getDateString();
        String a="";
        StringTokenizer st=new StringTokenizer(date," ");
        while (st.hasMoreTokens()){
            a=st.nextToken();
        }

        if(a.equals("a.m."))
        {

            date=date.replace(a,"AM");
        }
        if(a.equals("p.m."))
        {
            date=date.replace(a,"PM");


        }


        return date;
    }



    public static String getshowCurrentDate()
    {
        Calendar cal=Calendar.getInstance();
        int day=cal.get(Calendar.DAY_OF_MONTH);
        int month=cal.get(Calendar.MONTH);
        int year=cal.get(Calendar.YEAR);
        month =month+1;

        int h=cal.get(Calendar.HOUR);
        int m=cal.get(Calendar.MINUTE);
        int s=cal.get(Calendar.SECOND);

        String date=day+"/"+month+"/"+year;
        return date;

    }

    public static  String parseDate(String date)
    {
        StringTokenizer st=new StringTokenizer(date,"/");
        String month="",day="",year="";
        try {
            month = st.nextToken();
            day = st.nextToken();
            year = st.nextToken();
        }catch (Exception e){e.printStackTrace();}

        return day+"/"+month+"/"+year;
    }

    public static void displayPromptForEnablingGPS(final Activity activity)
    {

        final AlertDialog.Builder builder =  new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Do you want open GPS setting?";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                                activity.finish();
                            }
                        });
        builder.create().show();
    }
    public static boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    public static boolean isfrontCameraAvalable(){
        int numCameras= Camera.getNumberOfCameras();
        for(int i=0;i<numCameras;i++){
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if(Camera.CameraInfo.CAMERA_FACING_FRONT == info.facing){
                return true;
            }
        }
        return false;
    }
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    //TODO check android.support.v and correct this.
//    public static boolean checkPermission(final Context context)
//    {
//        int currentAPIVersion = Build.VERSION.SDK_INT;
//        if(currentAPIVersion>= Build.VERSION_CODES.M)
//        {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
//                    alertBuilder.setCancelable(true);
//                    alertBuilder.setTitle("Permission necessary");
//                    alertBuilder.setMessage("External storage permission is necessary");
//                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                        }
//                    });
//                    AlertDialog alert = alertBuilder.create();
//                    alert.show();
//                } else {
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                }
//                return false;
//            } else {
//                return true;
//            }
//        } else {
//            return true;
//        }
//    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static byte[] bitmaptoByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static String BitArrayToString(byte[] b1) {
        byte[] b = b1;
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static String getTypeOfSanrachna(String code){
        switch (code){
            case "T":
                return "तालाब";
            case "N":
                return "नहर";
            case "A":
                return "अाहर";
            case "P":
                return "पईन";
            case "ND":
                return "छोटी-छोटी नदिया";
            case "NL":
                return "छोटी-छोटी नाला";
            case "JS":
                return "पहाड़ी क्षेत्रों में जल संग्रहण";
            case "BCJ":
                return "भवनों में छत वर्षा जल संचयन";
            case "PSSV":
                return "पौधशाला सृजन एवं सघन वृक्षारोपण";
            case "JKTS":
                return "जैविक खेती एवं टपकन सिंचाई";
            case "SUUP":
                return "सौर उर्जा उपयोग को प्रोत्साहन एवं उर्जा की बचत";
            case "O":
                return "अन्य";
            default:
                return "अन्य";
        }
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS");
            String currentTimeStamp = formatter.format(new java.util.Date()); // Find todays date

            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
