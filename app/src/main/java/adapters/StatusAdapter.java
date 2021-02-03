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

import datamodel.StatusModel;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.MyViewHolder> {

    Context context;
    List<StatusModel> list;

    public StatusAdapter(Context context, List<StatusModel> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dailystatus_itemlist,null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvtdate.setText(list.get(i).getTdate());
        myViewHolder.tvtid.setText(list.get(i).getTaid());
        myViewHolder.tvtitle.setText(list.get(i).getTitle());
        myViewHolder.tvstime.setText(list.get(i).getStime());
        myViewHolder.tvetime.setText(list.get(i).getEtime());
        myViewHolder.tvdesc.setText(list.get(i).getDesc());
        myViewHolder.tvstatus.setText(list.get(i).getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvtid;
        TextView tvtitle;
        TextView tvtdate;
        TextView tvstime;
        TextView tvetime;
        TextView tvdesc;
        TextView tvstatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvtid=itemView.findViewById(R.id.tvdtid);
            tvtitle=itemView.findViewById(R.id.tvdtitle);
            tvtdate=itemView.findViewById(R.id.tvdtdate);
            tvstime =itemView.findViewById(R.id.tvstime);
            tvetime =itemView.findViewById(R.id.tvetime);
            tvdesc=itemView.findViewById(R.id.tvdtdesc);
            tvstatus=itemView.findViewById(R.id.tvtastatus);
        }
    }
}
