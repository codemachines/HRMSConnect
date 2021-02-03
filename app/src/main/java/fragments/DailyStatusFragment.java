package fragments;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;

import com.example.hrmsconnect.ApiClient;
import com.example.hrmsconnect.ApiInterface;
import com.example.hrmsconnect.MainActivity;
import com.example.hrmsconnect.R;
import com.example.hrmsconnect.TimeSheet;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapters.StatusAdapter;
import datamodel.StatusModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class DailyStatusFragment extends Fragment {

    RecyclerView rvdstatus;

    ProgressBar pbdstatus;

    Button btnaddstatus;

    StatusAdapter sta;

    List<StatusModel> list;

    SharedPreferences preferences;


    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_daily_status,null);

        rvdstatus=view.findViewById(R.id.rvdstatus);
        pbdstatus=view.findViewById(R.id.pbdstatus);
        btnaddstatus=view.findViewById(R.id.btnaddstatus);

        preferences = this.getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE);



        btnaddstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TimeSheet.class));
            }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        list=new ArrayList<>();

        LinearLayoutManager llm=new LinearLayoutManager(getContext());
        rvdstatus.setLayoutManager(llm);

        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call=apiInterface.dailystatusdata(preferences.getString("emp_id",""));
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
                            String tdate=obj.getString("task_date");
                            String taid=obj.getString("admin_task_id");
                            String ttitle=obj.getString("task_title");
                            String stime=obj.getString("start_time");
                            String etime=obj.getString("end_time");
                            String desc=obj.getString("task_desc");
                            String status=obj.getString("task_status");

                            StatusModel stm=new StatusModel();

                            stm.setTdate(tdate);
                            stm.setTaid(taid);
                            stm.setTitle(ttitle);
                            stm.setStime(stime);
                            stm.setEtime(etime);
                            stm.setDesc(desc);
                            stm.setStatus(status);

                            list.add(stm);

                            sta=new StatusAdapter(getContext(),list);
                            rvdstatus.setAdapter(sta);
                            pbdstatus.setVisibility(View.GONE);

                            sta.notifyDataSetChanged();
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
