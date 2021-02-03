package fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hrmsconnect.ApiClient;
import com.example.hrmsconnect.ApiInterface;
import com.example.hrmsconnect.LeaveForm;
import com.example.hrmsconnect.MainActivity;
import com.example.hrmsconnect.R;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapters.LeaveAdapter;
import datamodel.LeaveModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class LeaveFragment extends Fragment {

    Button btnrqstl;
    TextView tvlbalance;

    RecyclerView rvleave;

    ProgressBar pbldata;

    LeaveAdapter la;


    SharedPreferences preferences;

    List<LeaveModel> list;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view=inflater.inflate(R.layout.fragment_leave,null);

        preferences = this.getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE);

        btnrqstl=view.findViewById(R.id.btnrqstl);
        rvleave=view.findViewById(R.id.rvleave);
        pbldata=view.findViewById(R.id.pbldata);
        tvlbalance=view.findViewById(R.id.lbalance);







 btnrqstl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    requestLeave();
                }
            });


        return view;
    }

    private void requestLeave() {

        Intent i=new Intent(getActivity(),LeaveForm.class);
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();

        tvlbalance.setText(preferences.getString("balance",""));


        list=new ArrayList<>();

        LinearLayoutManager llm=new LinearLayoutManager(getContext());
        rvleave.setLayoutManager(llm);


        Log.e("Response>>>",preferences.getString("emp_id","")+"");

        ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call=apiInterface.empleavedata(preferences.getString("emp_id",""));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                Log.e("response>>>",response.body().toString());
                try {
                    JSONObject object=new JSONObject(response.body().toString());
                    String msg=object.getString("status");

                    Log.e("response>>>",response.body().toString());


                    if (msg.equals("200")){

                        JSONArray array=object.getJSONArray("data");

                        for (int i=0;i<=array.length();i++)
                        {
                            JSONObject obj=array.getJSONObject(i);
                            String ldate=obj.getString("applied_date");
                            String ltype=obj.getString("leave_type");
                            String fromdate=obj.getString("start_date");
                            String todate=obj.getString("end_date");
                            String ldays=obj.getString("days");
                            String lreason=obj.getString("leave_desc");
                            String applyto=obj.getString("applied_to");
                            String ccto=obj.getString("cc_to");
                            String status=obj.getString("approved_status");

                            LeaveModel lm=new LeaveModel();

                            lm.setLdate(ldate);
                            lm.setLtype(ltype);
                            lm.setFromdate(fromdate);
                            lm.setTodate(todate);
                            lm.setLdays(ldays);
                            lm.setLreason(lreason);
                            lm.setApplyto(applyto);
                            lm.setCcto(ccto);
                            lm.setStatus(status);

                            list.add(lm);

                            la=new LeaveAdapter(getContext(),list);
                            rvleave.setAdapter(la);
                            pbldata.setVisibility(View.INVISIBLE);

                            la.notifyDataSetChanged();

                        }

                    }
                    else {
                        Toast.makeText(getContext(), ""+ Log.e("Error:",response.body().toString()), Toast.LENGTH_SHORT).show();
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
