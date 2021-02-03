package fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import datamodel.TaskModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class TaskFragment extends Fragment {

    RecyclerView rvtask;

    ProgressBar pbtask;

    TaskAdapter ta;

    TextView tvalert;

    SharedPreferences preferences;

    List<TaskModel> list;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_task, null);

        rvtask = view.findViewById(R.id.rvtask);

        pbtask = view.findViewById(R.id.pbtdata);

        tvalert=view.findViewById(R.id.tvalert);



        preferences = this.getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvtask.setLayoutManager(llm);
        list = new ArrayList<>();
        Log.e("response>>",""+list.size());

        if (list.size()==0){

            pbtask.setVisibility(View.GONE);
            tvalert.setVisibility(View.VISIBLE);
        }else {
            pbtask.setVisibility(View.GONE);
            tvalert.setVisibility(View.GONE);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call = apiInterface.taskdata(preferences.getString("emp_id", ""));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                try {
                    JSONObject object = new JSONObject(response.body().toString());
                    String msg = object.getString("status");

                    if (msg.equals("200")) {
                        JSONArray array = object.getJSONArray("data");

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject obj = array.getJSONObject(i);

                            String tid = obj.getString("admin_task_id");
                            String title = obj.getString("task_title");
                            String tdate = obj.getString("task_date");
                            String tdeadline = obj.getString("task_deadline");
                            String tdesc = obj.getString("task_desc");
                            String tstatus = obj.getString("task_status");

                            TaskModel tm = new TaskModel();

                            tm.setTid(tid);
                            tm.setTitle(title);
                            tm.setTdate(tdate);
                            tm.setTdeadline(tdeadline);
                            tm.setTdesc(tdesc);
                            tm.setTstatus(tstatus);

                            list.add(tm);

                            ta = new TaskAdapter(getContext(), list);

                            rvtask.setAdapter(ta);

                            pbtask.setVisibility(View.INVISIBLE);

                            ta.notifyDataSetChanged();



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
