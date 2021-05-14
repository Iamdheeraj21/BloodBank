package Notifications;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbank.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class NotificationAdapter extends FirebaseRecyclerAdapter<Notification, NotificationAdapter.NotificationViewHolder>
{
    public NotificationAdapter(@NonNull FirebaseRecyclerOptions<Notification> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull Notification model) {
        holder.submit_by.setText("Submit By:- "+model.getSubmitby());
        holder.title.setText(model.getTitle());
        holder.date.setText("Date :-"+model.getDate());
        holder.desc.setText(model.getDescription());
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        return new NotificationViewHolder(view);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView date,desc,title,submit_by;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.notification_date);
            desc=itemView.findViewById(R.id.notification_desc);
            title=itemView.findViewById(R.id.notification_title);
            submit_by=itemView.findViewById(R.id.submit_by);
        }
    }
}
