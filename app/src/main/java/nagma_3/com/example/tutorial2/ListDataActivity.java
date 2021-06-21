package nagma_3.com.example.tutorial2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class ListDataActivity extends AppCompatActivity {

    RecyclerView recycler;
    String fileName = "RoadData"+Utiilties.getCurrentTimeStamp()+".csv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        recycler = findViewById(R.id.recycler);

        fetchFiles();
    }

    public void fetchFiles() {

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
        }

        Log.e("Files", ""+filenames.size());
        setupRcyclerView(filenames);
        //return filenames;
    }

    public void setupRcyclerView(ArrayList<String> filenames){
        ListDataAdapter adapter = new ListDataAdapter(this,filenames);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
    }
}
