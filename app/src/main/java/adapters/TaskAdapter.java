package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hrmsconnect.R;

import java.util.List;

import datamodel.TaskModel;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    Context context;
    List<TaskModel> list;
    public TaskAdapter(Context context, List<TaskModel> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_itemlist,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvtid.setText(list.get(i).getTid());
        myViewHolder.tvtitle.setText(list.get(i).getTitle());
        myViewHolder.tvtdate.setText(list.get(i).getTdate());
        myViewHolder.tvdeadline.setText(list.get(i).getTdeadline());
        myViewHolder.tvdesc.setText(list.get(i).getTdesc());
        myViewHolder.tvstatus.setText(list.get(i).getTstatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvtid;
        TextView tvtitle;
        TextView tvtdate;
        TextView tvdeadline;
        TextView tvdesc;
        TextView tvstatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvtid=itemView.findViewById(R.id.tvtid);
            tvtitle=itemView.findViewById(R.id.tvtitle);
            tvtdate=itemView.findViewById(R.id.tvtdate);
            tvdeadline=itemView.findViewById(R.id.tvdeadline);
            tvdesc=itemView.findViewById(R.id.tvdesc);
            tvstatus=itemView.findViewById(R.id.tvstatus);
        }
    }
}
