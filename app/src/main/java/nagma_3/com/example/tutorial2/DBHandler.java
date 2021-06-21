package nagma_3.com.example.tutorial2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "usersdb";
    private static final String TABLE_Users = "userdetails";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_xAccValue = "xAccValue";
    private static final String KEY_yAccValue = "yAccValue";
    private static final String KEY_zAccValue = "zAccValue";
    private static final String KEY_xGyroValue = "xGyroValue";
    private static final String KEY_yGyroValue = "yGyroValue";
    private static final String KEY_zGyrovalue = "zGyroValue";
    private static final String KEY_lattitiude = "latitude";
    private static final String KEY_longitude = "longitude";
    private static final String KEY_speedometer = "speed";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Users +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_xAccValue + " TEXT," + KEY_yAccValue + " TEXT," + KEY_zAccValue + " TEXT," + KEY_xGyroValue + " TEXT," + KEY_yGyroValue + " TEXT," + KEY_zGyrovalue + " TEXT," + KEY_lattitiude + " TEXT," +
                KEY_longitude + " TEXT," +  KEY_speedometer + " TEXT" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Users);
        // Create tables again
        onCreate(db);
    }
    // **** CRUD (Create, Read, Update, Delete) Operations ***** //

    // Adding new User Details
    void insertUserDetails(String xAccValue, String yAccValue, String zAccValue, String xGyroValue,String yGyroValue,String zGyroValue,String latitude,String longitude,String speed) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_xAccValue, xAccValue);
        cValues.put(KEY_yAccValue, yAccValue);
        cValues.put(KEY_zAccValue, zAccValue);
        cValues.put(KEY_xGyroValue, xGyroValue);
        cValues.put(KEY_yGyroValue, yGyroValue);
        cValues.put(KEY_zGyrovalue, zGyroValue);
        cValues.put(KEY_xAccValue, xAccValue);
        cValues.put(KEY_xAccValue, xAccValue);
        cValues.put(KEY_lattitiude, latitude);
        cValues.put(KEY_longitude, longitude);
        cValues.put(KEY_speedometer, speed);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_Users, null, cValues);
        db.close();
    }
    // Get User Details
}