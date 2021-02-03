package fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.hrmsconnect.AddTask;
import com.example.hrmsconnect.ApiClient;
import com.example.hrmsconnect.ApiInterface;
import com.example.hrmsconnect.MainActivity;
import com.example.hrmsconnect.R;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.TaskAdapter;
import adapters.TodoAdapter;
import datamodel.TaskModel;
import datamodel.TodoModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class TodoFragment extends Fragment {

    RecyclerView rvtodo;

    Button btnaddtask;

    ProgressBar pbtodo;

    TodoAdapter toa;

    SharedPreferences preferences;

    List<TodoModel> list;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_todo,null);

        rvtodo=view.findViewById(R.id.rvtodo);

        btnaddtask=view.findViewById(R.id.btnaddtask);

        pbtodo=view.findViewById(R.id.pbtodo);

        preferences = this.getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE);



        btnaddtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddTask.class));
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        list=new ArrayList<>();
        toa=new TodoAdapter(getActivity(),list);
        LinearLayoutManager llm=new LinearLayoutManager(getActivity());
        rvtodo.setLayoutManager(llm);
        rvtodo.setItemAnimator(new DefaultItemAnimator());
        rvtodo.setNestedScrollingEnabled(false);
        rvtodo.setAdapter(toa);
        toa.notifyDataSetChanged();

        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call=apiInterface.emptaskdata(preferences.getString("emp_id",""));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                try {
                    JSONObject object= new JSONObject(response.body().toString());
                    String msg=object.getString("status");

                    if (msg.equals("200")){
                        JSONArray array=object.getJSONArray("data");
                        for (int i=0;i<array.length();i++){

                            JSONObject obj=array.getJSONObject(i);

                            String tid=obj.getString("emp_task_id");
                            String title=obj.getString("task_title");
                            String tdate=obj.getString("task_date");
                            String tdeadline=obj.getString("task_deadline");
                            String tdesc=obj.getString("task_desc");
                            String tgby=obj.getString("task_givenby");
                            String tstatus=obj.getString("task_status");

                            if (tstatus.equals("Completed")){
                            }

                            TodoModel tom=new TodoModel();

                            tom.setTid(tid);
                            tom.setTitle(title);
                            tom.setTdate(tdate);
                            tom.setTdeadline(tdeadline);
                            tom.setTdesc(tdesc);
                            tom.setTgb(tgby);
                            tom.setTstatus(tstatus);

                            list.add(tom);

                            pbtodo.setVisibility(View.INVISIBLE);

                            toa.notifyDataSetChanged();

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
