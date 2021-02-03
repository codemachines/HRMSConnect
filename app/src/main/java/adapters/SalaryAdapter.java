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

import datamodel.SalaryModel;

public class SalaryAdapter extends RecyclerView.Adapter<SalaryAdapter.MyViewholder> {

    Context context;
    List<SalaryModel> list;
    public SalaryAdapter(Context context, List<SalaryModel> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.salary_itemlist,null);

        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder myViewholder, int i) {

        myViewholder.tvsd.setText(list.get(i).getSdate());
        myViewholder.tvtwd.setText(list.get(i).getTwd());
        myViewholder.tvdat.setText(list.get(i).getDatt());
        myViewholder.tvbs.setText(list.get(i).getBs());
        myViewholder.tvtax.setText(list.get(i).getTax());
        myViewholder.tvall.setText(list.get(i).getAllowance());
        myViewholder.tvtotsal.setText(list.get(i).getTotal_salary());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {

        TextView tvsd;
        TextView tvtwd;
        TextView tvdat;
        TextView tvbs;
        TextView tvtax;
        TextView tvall;
        TextView tvtotsal;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);

            tvsd=itemView.findViewById(R.id.tvsd);
            tvtwd=itemView.findViewById(R.id.tvtwd);
            tvdat=itemView.findViewById(R.id.tvdat);
            tvbs=itemView.findViewById(R.id.tvbs);
            tvtax=itemView.findViewById(R.id.tvtax);
            tvall=itemView.findViewById(R.id.tvall);
            tvtotsal=itemView.findViewById(R.id.tvtotsal);
        }
    }
}
