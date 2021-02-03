package fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hrmsconnect.ApiClient;
import com.example.hrmsconnect.ApiInterface;
import com.example.hrmsconnect.MainActivity;
import com.example.hrmsconnect.R;
import com.example.hrmsconnect.SendMail;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.MailAdapter;
import datamodel.MailModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MailFragment extends Fragment {

    FloatingActionButton fabmail;

    RecyclerView rvmail;
    List<MailModel> list;

    MailAdapter ma;
    SharedPreferences preferences;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_mail,null);
        fabmail=view.findViewById(R.id.fabmail);
        rvmail=view.findViewById(R.id.rvmail);

        fabmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SendMail.class));
            }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        list=new ArrayList<>();

        LinearLayoutManager llm=new LinearLayoutManager(getContext());
        rvmail.setLayoutManager(llm);

        preferences = this.getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE);

        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call=apiInterface.fetchmail(preferences.getString("email",""));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                try {
                    JSONObject object=new JSONObject(response.body().toString());
                    String msg=object.getString("status");

                    if (msg.equals("200")){

                        JSONArray array=object.getJSONArray("data");

                        for (int i=0;i<array.length();i++){
                            JSONObject obj=array.getJSONObject(i);

                            String from=obj.getString("from_email_id");
                            String to=obj.getString("to_email_id");
                            String subject=obj.getString("subject");
                            String body=obj.getString("body");
                            String document=obj.getString("document");
                            String time=obj.getString("mailtime");



                            MailModel mm=new MailModel();

                            if (from.equals(preferences.getString("email",""))){

                                mm.setPropic(preferences.getString("profile",""));

                            }

                            mm.setFrom(from);
                            mm.setTo(to);
                            mm.setSubject(subject);
                            mm.setBody(body);
                            mm.setDocument(document);
                            mm.setTime(time);

                            list.add(mm);

                            ma=new MailAdapter(getContext(),list);

                            rvmail.setAdapter(ma);

                            ma.notifyDataSetChanged();


                        }
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
}
