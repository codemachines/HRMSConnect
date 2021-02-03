package fragments;

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

import adapters.SalaryAdapter;
import datamodel.SalaryModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class SalaryFragment extends Fragment {

    RecyclerView rvsalary;

    ProgressBar pbsalary;

    SalaryAdapter sa;

    TextView tvsalert;

    SharedPreferences preferences;

    List<SalaryModel> list;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_salary, null);

        rvsalary = view.findViewById(R.id.rvsalary);

        pbsalary = view.findViewById(R.id.pbsdata);

        tvsalert=view.findViewById(R.id.tvsalert);

        preferences = this.getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvsalary.setLayoutManager(llm);
        list = new ArrayList<>();

        if (list.size()==0){

            pbsalary.setVisibility(View.GONE);
            tvsalert.setVisibility(View.VISIBLE);
        }else {
            pbsalary.setVisibility(View.GONE);
            tvsalert.setVisibility(View.GONE);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call = apiInterface.empSaldata(preferences.getString("emp_id", ""));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                try {
                    JSONObject object = new JSONObject(response.body().toString());
                    String msg = object.getString("status");

                    if (msg.equals("200")) {
                        JSONArray array = object.getJSONArray("data");

                        for (int i = 0; i <= array.length(); i++) {

                            JSONObject obj = array.getJSONObject(i);

                            String sdate = obj.getString("salary_date");
                            String twd = obj.getString("total_working_days");
                            String datt = obj.getString("days_attended");
                            String bs = obj.getString("basic_salary");
                            String tax = obj.getString("tax");
                            String allowance = obj.getString("allowance");
                            String total_salary = obj.getString("total_salary");

                            SalaryModel sm = new SalaryModel();

                            sm.setSdate(sdate);
                            sm.setTwd(twd);
                            sm.setDatt(datt);
                            sm.setBs(bs);
                            sm.setTax(tax);
                            sm.setAllowance(allowance);
                            sm.setTotal_salary(total_salary);

                            list.add(sm);

                            sa = new SalaryAdapter(getContext(), list);

                            rvsalary.setAdapter(sa);

                            pbsalary.setVisibility(View.INVISIBLE);

                            sa.notifyDataSetChanged();


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
