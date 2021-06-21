package nagma_3.com.example.tutorial2;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListDataAdapter extends RecyclerView.Adapter<ListDataAdapter.ViewHolder> {

    Activity activity;
    LayoutInflater mInflater;
    ArrayList<String> list=new ArrayList<>();

    public ListDataAdapter(Activity listViewshowedit, ArrayList<String> list) {
        this.activity=listViewshowedit;
        this.list=list;
        mInflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_list_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
       // final JobOfferPostedEntity info = ThrList.get(position);


        holder.tv_slno.setText(String.valueOf(position+1)+").");
        holder.tv_filename.setText(list.get(position));


    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_filename,tv_slno;


        Button btn_accpt,btn_rjct;
        LinearLayout ll_btn,ll_status,ll_selection;
        ViewHolder(View itemView) {
            super(itemView);
            tv_slno=itemView.findViewById(R.id.tv_slno);
            tv_filename=itemView.findViewById(R.id.tv_filename);
        }

        @Override
        public void onClick(View v) {

        }

    }


    public String getGenderHindi(String gender)
    {
        return gender;
    }

}
