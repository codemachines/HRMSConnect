package adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hrmsconnect.ApiClient;
import com.example.hrmsconnect.ApiInterface;
import com.example.hrmsconnect.MainActivity;
import com.example.hrmsconnect.R;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import datamodel.TodoModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> {

    Context context;
    List<TodoModel> list;
//    String status;
    SharedPreferences preferences;

    public TodoAdapter(Context context,  List<TodoModel> list) {
        this.context=context;

        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_itemlist,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        myViewHolder.tvtid.setText(list.get(i).getTid());
        myViewHolder.tvtitle.setText(list.get(i).getTitle());
        myViewHolder.tvtdate.setText(list.get(i).getTdate());
        myViewHolder.tvdeadline.setText(list.get(i).getTdeadline());
        myViewHolder.tvdesc.setText(list.get(i).getTdesc());
        myViewHolder.tvgb.setText(list.get(i).getTgb());
        myViewHolder.tvstatus.setText(list.get(i).getTstatus());

        if (list.get(i).getTstatus().equals("Completed")){
            myViewHolder.btndone.setVisibility(View.GONE);
        }else if (list.get(i).getTstatus().equals("Pending")){
            myViewHolder.btndone.setVisibility(View.VISIBLE);
        }

        myViewHolder.btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String task="Completed";
                ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
                Call<JsonElement> call=apiInterface.emptaskstatus(list.get(i).getTid(),task);
                call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                        try {
                            JSONObject object = new JSONObject(response.body().toString());
                            String msg = object.getString("status");

                            if (msg.equals("200")){
                                myViewHolder.tvstatus.setText("Completed");
                                myViewHolder.btndone.setVisibility(View.GONE);
                                Toast.makeText(context, "Task Completed", Toast.LENGTH_SHORT).show();
                            }else {
                                Log.e("Error:",response.body().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {

                    }
                });
            }
        });



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
        TextView tvgb;
        TextView tvstatus;
        Button btndone;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvtid=itemView.findViewById(R.id.tvetid);
            tvtitle=itemView.findViewById(R.id.tvettitle);
            tvtdate=itemView.findViewById(R.id.tvetdate);
            tvdeadline=itemView.findViewById(R.id.tvetdeadline);
            tvdesc=itemView.findViewById(R.id.tvetdesc);
            tvstatus=itemView.findViewById(R.id.tvetstatus);
            tvgb=itemView.findViewById(R.id.tvgivenby);
            btndone=itemView.findViewById(R.id.btndone);

        }
    }
}
