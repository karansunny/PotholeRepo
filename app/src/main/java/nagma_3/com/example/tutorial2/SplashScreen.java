package nagma_3.com.example.tutorial2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import nagma_3.com.example.tutorial2.Database.DataBaseHelper;

public class SplashScreen extends AppCompatActivity {

    private Button Start;
    private Button Export;
    private Button listfile;
    private static final String TAG = "SplashScreen" ;
    TextView listfiles;


    String fileName = "RoadData"+Utiilties.getCurrentTimeStamp()+".csv";

    DataBaseHelper dataBaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        dataBaseHelper = new DataBaseHelper(this);
        setupDatabase();
        listfiles = (TextView) findViewById(R.id.listfiles);
        listfiles.setVisibility(View.INVISIBLE);

        Start = (Button) findViewById(R.id.Start);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listfiles.setVisibility(View.INVISIBLE);
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                //startService(new Intent(SplashScreen.this, TrackLocationService.class));

            }
        });


        Export = (Button) findViewById(R.id.Export);
        Export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listfiles.setVisibility(View.INVISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

                    new ExportDatabaseCSVTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } else {

                    new ExportDatabaseCSVTask().execute();
                }
                Log.d(TAG,"on export button click");
            }
        });

        listfile = (Button) findViewById(R.id.Show_Files);
        listfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listfile.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                Intent intent = new Intent(SplashScreen.this, ListDataActivity.class);
                startActivity(intent);
//               ArrayList<String> myList = FetchFiles();
////                FetchFiles();
//
//                Log.d(TAG, "afterClick of list button ");
//
//                listfiles.setText(""+myList);
//                listfiles.setVisibility(View.VISIBLE);

            }
        });
    }


    public void setupDatabase() {

        try {
            dataBaseHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            dataBaseHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
    }


    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, String> {



        private final ProgressDialog dialog = new ProgressDialog(SplashScreen.this);
        DataBaseHelper dataBaseHelper;



//        public ExportDatabaseCSVTask() {
//            dialog = new ProgressDialog(SplashScreen.this);
//        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
            dataBaseHelper = new DataBaseHelper(SplashScreen.this);
        }

        protected String doInBackground(final String... args) {

            File exportDir = getExternalFilesDir(null);

            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, fileName);
            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                Cursor curCSV = dataBaseHelper.raw();
                if(curCSV.getCount() > 0){
                    csvWrite.writeNext(curCSV.getColumnNames());
                    while (curCSV.moveToNext()) {
                        String arrStr[] = null;
                        String[] mySecondStringArray = new String[curCSV.getColumnNames().length];
                        for (int i = 0; i < curCSV.getColumnNames().length; i++) {
                            mySecondStringArray[i] = curCSV.getString(i);
                        }
                        csvWrite.writeNext(mySecondStringArray);
                    }
                    csvWrite.close();
                    curCSV.close();
                    return "Success";
                }else{
                    return "No Record Found, Please start your trip first.";
                }

            } catch (IOException e) {
                return "Failed";
            }
        }

        protected void onPostExecute(final String message) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            if (message.equals("Success")) {
                //Toast.makeText(MainActivity.this, "Export successful!", Toast.LENGTH_SHORT).show();
                if(!isFinishing())
                    showAlertForShare();


            } else {
                Toast.makeText(SplashScreen.this, message, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void showAlertForShare() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setTitle("Export Successfull!!");
        builder.setMessage("Do you want to share this file")
                .setPositiveButton("Share",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                ShareFile();
                                Log.d(TAG, "afterClick of share file");
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.dismiss();
                            }
                        });
        builder.create().show();
        Log.d(TAG,"Alert for share");
    }

    private void ShareFile() {
        File exportDir = getExternalFilesDir(null);
//        String fileName = "RoadData.csv";
        File sharingGifFile = new File(exportDir, fileName);
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("application/csv");
        Uri uri;
        if (Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(sharingGifFile);
        } else {
            uri = Uri.parse(sharingGifFile.getPath()); // My work-around for new SDKs, doesn't work in Android 10.
        }
//        Uri uri = Uri.fromFile(sharingGifFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "Share CSV"));
        Log.d(TAG,"plz share");
    }

    private ArrayList<String> FetchFiles() {

        ArrayList<String> filenames = new ArrayList<String>();
        Context context = getApplicationContext();
//        String folder = context.getFilesDir().getAbsolutePath() ;
        String path = context.getExternalFilesDir(null).toString();


        File directory = new File(path);
        File[] files = directory.listFiles();

//        for (int i = 0; i < files.length; i++)
//        {
//
//            String file_name = files[i].getName();
//            // you can store name to arraylist and use it later
//            filenames.add(file_name);
//            Log.d(TAG, "afterClick of list button "+filenames);
//        }
//        return filenames;
        for (File file : files) {
            filenames.add(file.getName());
            Log.d(TAG, "afterClick of list button "+filenames);
        }
        return filenames;
    }
}
