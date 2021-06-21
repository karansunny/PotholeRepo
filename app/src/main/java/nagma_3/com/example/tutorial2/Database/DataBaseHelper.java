package nagma_3.com.example.tutorial2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import nagma_3.com.example.tutorial2.MainActivity;
import nagma_3.com.example.tutorial2.model.UserDetailsEntity;

import static nagma_3.com.example.tutorial2.Utiilties.getDateTime;


/**
 * Helper to the database, manages versions and creation
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    public static String accValuex;
    //private static String DB_PATH = "";
    private static String DB_PATH = "/data/data/nagma_3.com.example.tutorial2/assets/";
    private static String DB_NAME = "PACSDB1";

    private SQLiteDatabase myDataBase;
    private final Context myContext;

    SQLiteDatabase db;

    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 2);
        if (android.os.Build.VERSION.SDK_INT >= 4.2) {


            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.myContext = context;
    }



    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            // do nothing - database already exist


        } else {

            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            //this.getReadableDatabase();

            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);


        } catch (SQLiteException e) {

            // database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;

    }

    public boolean databaseExist() {


        File dbFile = new File(DB_PATH + DB_NAME);

        return dbFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        this.getReadableDatabase().close();
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();


    }

    public void openDataBase() throws SQLException {

        // Open the database
        this.getReadableDatabase();
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }






    @Override


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long insertSurfaceUserDetails(UserDetailsEntity result) {
        long c = 0;


        try {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();



            //values.put("a_id", result.geta_id());
            values.put("accValuex", result.getaccValuex());
            values.put("accValuey", result.getaccValuey());
            values.put("accValuez", result.getaccValuez());
            values.put("gyrovaluex", result.getgyroValuex());
            values.put("gyrovaluey", result.getgyroValuey());
            values.put("gyrovaluez", result.getgyroValuez());
            values.put("latitude", result.getlatitude());
            values.put("longitude", result.getlongitude());
            values.put("speed", result.getSpeedfinal());
            values.put("Time", getCurrentTimeStamp());
            values.put("Annotate",result.getannotate());

            String[] whereArgs = new String[]{result.getaccValuex()};

          //c = db.update("road_data", values, "accValuex=? ", whereArgs);


            c = db.insert("RoadData", null, values);
             // c = db.insert("road_data", null, values,SQLiteDatabase.CONFLICT_REPLACE);

//            }

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return c;

    }




    public long deleteSchemeRecord(){

        long f = -1;

        try {

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete from RoadData");

            db.close();

        } catch (Exception e) {
            // TODO: handle exception
            return f;
        }
        return f;
    }

    public long InsertRecord(String annotate) {

       long d = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Annotate", annotate);
        values.put("Time",getCurrentTimeStamp());


        try {
            d = db.insert("AnnotateTable", null, values);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return d;
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

    public Cursor raw() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + "RoadData" , new String[]{});
        return res;
    }
}



//    public long setSurfaceDataToLocal(ArrayList<SurfaceSchemeEntity> list) {
//        String tableName = "SurfaceSchemeDetail";
//
//        long c = -1;
//
//        ArrayList<SurfaceSchemeEntity> info = list;
//
//        if (info != null) {
//            try {
//                SQLiteDatabase db = this.getReadableDatabase();
//
//                ContentValues values = new ContentValues();
//
//                for (int i = 0; i < info.size(); i++) {
//
//                    values.put("SCHEME_ID", info.get(i).getSCHEME_ID());
//                    values.put("SCHEME_NAME", info.get(i).getSCHEME_NAME());
//                    values.put("TYPE_OF_SCHEME", info.get(i).getTYPE_OF_SCHEME());
//                    values.put("District", info.get(i).getDistrict());
//                    values.put("Block", info.get(i).getBlock());
//                    values.put("Panchayat", info.get(i).getPanchayat());
//                    values.put("SOURCE_OF_WATER", info.get(i).getSOURCE_OF_WATER());
//                    values.put("Fund_Type", info.get(i).getFund_Type());
//                    values.put("FINANCIAL_YEAR", info.get(i).getFINANCIAL_YEAR());
//                    values.put("NIT_No", info.get(i).getNIT_No());
//                    values.put("SC_ST_Majority_Village", info.get(i).getSC_ST_Majority_Village());
//
//                    String[] whereArgs = new String[]{String.valueOf(info.get(i).getSCHEME_ID())};
//
//                    c = db.update(tableName, values, "SCHEME_ID=?", whereArgs);
//
//                    if(c < 1){
//                        values.put("Updated", "0");
//                        c = db.insert(tableName, null, values);
//                    }
//                }
//                db.close();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return c;
//            }
//        }
//        return c;
//    }

//    public ArrayList<SurfaceSchemeEntity> getSurfaceSchemeDetail(String finyr,String schemeid)
//    {
//        //PondInspectionDetail info = null;
//
//        ArrayList<SurfaceSchemeEntity> infoList = new ArrayList<SurfaceSchemeEntity>();
//        String whereCondition=getPostWhereConditionForStudentListForAttendance(finyr,schemeid);
//        try
//        {
//            SQLiteDatabase db = this.getReadableDatabase();
//            String[] params = new String[]{"0"};
//               //Cursor cur = db.rawQuery("Select * from SurfaceSchemeDetail WHERE "+ whereCondition +" AND Updated=?",params);
//            Cursor cur = db.rawQuery("Select * from SurfaceSchemeDetail WHERE Updated=? "+ whereCondition +" ",params);
//            int x = cur.getCount();
//            // db1.execSQL("Delete from UserDetail");
//
//            while (cur.moveToNext())
//            {
//                SurfaceSchemeEntity info = new SurfaceSchemeEntity();
//                info.setSCHEME_ID(cur.getString(cur.getColumnIndex("SCHEME_ID")));
//                info.setSCHEME_NAME(cur.getString(cur.getColumnIndex("SCHEME_NAME")));
//                info.setTYPE_OF_SCHEME(cur.getString(cur.getColumnIndex("TYPE_OF_SCHEME")));
//                info.setDistrict(cur.getString(cur.getColumnIndex("District")));
//                info.setBlock(cur.getString(cur.getColumnIndex("Block")));
//                info.setPanchayat(cur.getString(cur.getColumnIndex("Panchayat")));
//                info.setSOURCE_OF_WATER(cur.getString(cur.getColumnIndex("SOURCE_OF_WATER")));
//                info.setFund_Type(cur.getString(cur.getColumnIndex("Fund_Type")));
//                info.setFINANCIAL_YEAR(cur.getString(cur.getColumnIndex("FINANCIAL_YEAR")));
//                info.setNIT_No(cur.getString(cur.getColumnIndex("NIT_No")));
//                info.setSC_ST_Majority_Village(cur.getString(cur.getColumnIndex("SC_ST_Majority_Village")));
//                infoList.add(info);
//            }
//
//            cur.close();
//            db.close();
//            this.getReadableDatabase().close();
//        }
//        catch (Exception e)
//        {
//            // TODO: handle exception
//            e.printStackTrace();
//            //info = null;
//        }
//        return infoList;
//    }


