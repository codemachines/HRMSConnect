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

import datamodel.LeaveModel;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.MyViewHolder> {

    Context context;
    List<LeaveModel> list;
    public LeaveAdapter(Context context, List<LeaveModel> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leave_itemlist,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvldate.setText(list.get(i).getLdate());
        myViewHolder.tvltype.setText(list.get(i).getLtype());
        myViewHolder.tvlsdate.setText(list.get(i).getFromdate());
        myViewHolder.tvledate.setText(list.get(i).getTodate());
        myViewHolder.tvldays.setText(list.get(i).getLdays());
        myViewHolder.tvldec.setText(list.get(i).getLreason());
        myViewHolder.tvlapplyto.setText(list.get(i).getApplyto());
        myViewHolder.tvlccto.setText(list.get(i).getCcto());
        myViewHolder.tvlstatus.setText(list.get(i).getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvldate;
        TextView tvltype;
        TextView tvlsdate;
        TextView tvledate;
        TextView tvldays;
        TextView tvldec;
        TextView tvlapplyto;
        TextView tvlccto;
        TextView tvlstatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvldate=itemView.findViewById(R.id.tvldate);
            tvltype=itemView.findViewById(R.id.tvltype);
            tvlsdate=itemView.findViewById(R.id.tvlsdate);
            tvledate=itemView.findViewById(R.id.tvledate);
            tvldays=itemView.findViewById(R.id.tvldays);
            tvldec=itemView.findViewById(R.id.tvldec);
            tvlapplyto=itemView.findViewById(R.id.tvlapplyto);
            tvlccto=itemView.findViewById(R.id.tvlccto);
            tvlstatus=itemView.findViewById(R.id.tvlstatus);

        }
    }
}
