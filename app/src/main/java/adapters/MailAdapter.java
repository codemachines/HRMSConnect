package adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hrmsconnect.R;

import org.w3c.dom.Text;

import java.util.List;

import datamodel.MailModel;

public class MailAdapter extends RecyclerView.Adapter<MailAdapter.MyViewHolder> {

    Context context;
    List<MailModel> list;
    public MailAdapter(Context context, List<MailModel> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mail_itemlist,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        myViewHolder.subject.setText(list.get(i).getSubject());
        myViewHolder.body.setText(list.get(i).getBody());
        myViewHolder.time.setText(list.get(i).getTime());
        if (list.get(i).getPropic()!=""){
            Glide.with(context).load(list.get(i).getPropic()).into(myViewHolder.propic);
        }
        else if (list.get(i).getPropic() == ""){
//            myViewHolder.propic.setImageResource(R.mipmap.man);
            Glide.with(context).load(R.mipmap.man).into(myViewHolder.propic);
        }

        myViewHolder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.mail_body);
                TextView tvfrom=dialog.findViewById(R.id.dfrom);
                TextView tvto=dialog.findViewById(R.id.dto);
                TextView tvsub=dialog.findViewById(R.id.dsub);
                TextView tvbody=dialog.findViewById(R.id.dbody);
                ImageView ivdoc=dialog.findViewById(R.id.ddoc);
                TextView tvtime=dialog.findViewById(R.id.ddate);

                tvfrom.setText(list.get(i).getFrom());
                tvto.setText(list.get(i).getTo());
                tvsub.setText(list.get(i).getSubject());
                tvbody.setText(list.get(i).getBody());

                Glide.with(context).load(list.get(i).getDocument()).into(ivdoc);

                tvtime.setText(list.get(i).getTime());

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView from;
        TextView subject;
        TextView body;
        TextView time;
        ImageView propic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            subject=itemView.findViewById(R.id.tvmailsub);
            body=itemView.findViewById(R.id.tvmailbody);
            time=itemView.findViewById(R.id.tvmailtime);
            propic=itemView.findViewById(R.id.chathead);


        }
    }
}
