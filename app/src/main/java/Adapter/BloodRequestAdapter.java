package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.R;

import java.util.List;

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.BloodRequestsViewHolder>
{
    Context context;
    List<BloodRequest> list;

    public BloodRequestAdapter(Context context, List<BloodRequest> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BloodRequestAdapter.BloodRequestsViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.requests,parent,false);
        return new BloodRequestsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull  BloodRequestAdapter.BloodRequestsViewHolder holder, int position)
    {
        BloodRequest bloodRequest= list.get(position);
        String app_no= bloodRequest.getApplication_no();
        String app_name= bloodRequest.getApplication_name();
        String app_mobile= bloodRequest.getApplication_mobile();
        String app_bloodgroup= bloodRequest.getApplication_bloodGroup();
        String status=bloodRequest.getApplication_status();

        holder.textView_app_no.setText("Application No :"+ app_no);
        holder.textView_app_name.setText("Name :"+app_name);
        holder.textView_app_mobile.setText("Mobile No :"+status);
        holder.textView_app_bloodGroup.setText("Blood Group :"+app_mobile);


        holder.checkBox.setEnabled(false);
        if(status.equals("Complete")){
            holder.checkBox.setChecked(true);
            holder.checkBox.setText("Complete");}
        else
        {
            holder.checkBox.setChecked(false);
            holder.checkBox.setText("Active");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class BloodRequestsViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView_app_no,textView_app_name,textView_app_mobile,textView_app_bloodGroup;
        CheckBox checkBox;
        public BloodRequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_app_no=itemView.findViewById(R.id.textview_applicationNo);
            textView_app_name=itemView.findViewById(R.id.textview_application_name);
            textView_app_mobile=itemView.findViewById(R.id.textview_application_mobileno);
            textView_app_bloodGroup=itemView.findViewById(R.id.textview_application_bloodgroup);
            checkBox=itemView.findViewById(R.id.application_status);
        }
    }
}
