package fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Anchor;
import com.anychart.graphics.vector.Stroke;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    SharedPreferences preferences;
    String balancerem;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_home,null);

        preferences = this.getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE);

        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call=apiInterface.balancegraph(preferences.getString("emp_id",""));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                Log.e("response>>>",response.body().toString());
                try {
                    JSONObject object=new JSONObject(response.body().toString());
                    String msg=object.getString("status");

                    if (msg.equals("200")){
                        JSONArray array=object.getJSONArray("data");

                        for (int i=0;i<array.length();i++){
                            JSONObject obj=array.getJSONObject(i);

                            balancerem=obj.getString("balance");
                            Log.e("balance",balancerem+"");

                            int balrem=Integer.parseInt(balancerem);
                            int baltaken=50-balrem;

                            AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
                            APIlib.getInstance().setActiveAnyChartView(anyChartView);


                            Pie pie = AnyChart.pie();

                            pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {

                                @Override
                                public void onClick(Event event) {
                                    Toast.makeText(getActivity(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
                                }
                            });

                            List<DataEntry> data = new ArrayList<>();
                            data.add(new ValueDataEntry("Leave Remaining", balrem));
                            data.add(new ValueDataEntry("Leave Taken", baltaken));



                            pie.data(data);

                            pie.title("Leave Percentage in Year 2019 (% out of 50)");

                            pie.labels().position("outside");

                            pie.legend().title().enabled(true);
                            pie.legend().title()
                                    .text("Retail channels")
                                    .padding(0d, 0d, 10d, 0d);

                            pie.legend()
                                    .position("center-bottom")
                                    .itemsLayout(LegendLayout.HORIZONTAL)
                                    .align(Align.CENTER);

                            anyChartView.setChart(pie);
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



        Call<JsonElement> call1=apiInterface.taskgraph(preferences.getString("emp_id",""));
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                try {
                    JSONObject object=new JSONObject(response.body().toString());
                    String st=object.getString("status");

                    if (st.equals("200")){

                        JSONArray array=object.getJSONArray("data");
                        for (int i=0;i<array.length();i++){

                            JSONObject obj=array.getJSONObject(i);

                            int taskcount=i;

                            AnyChartView anyChartView1 = view.findViewById(R.id.any_chart_view1);
                            APIlib.getInstance().setActiveAnyChartView(anyChartView1);

                            Cartesian cartesian = AnyChart.line();

                            cartesian.animation(true);

                            cartesian.padding(10d, 20d, 5d, 20d);

                            cartesian.crosshair().enabled(true);
                            cartesian.crosshair()
                                    .yLabel(true)
                                    // TODO ystroke
                                    .yStroke((Stroke) null, null, null, (String) null, (String) null);

                            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

                            cartesian.title("Task Completed as per the Month");

                            cartesian.yAxis(0).title("Number of Task Completed");
                            cartesian.xAxis(0).labels().padding(2d, 2d, 2d, 5d);

                            List<DataEntry> seriesData = new ArrayList<>();
                            seriesData.add(new CustomDataEntry("January", 0));
                            seriesData.add(new CustomDataEntry("February", 0));
                            seriesData.add(new CustomDataEntry("March", 0));
                            seriesData.add(new CustomDataEntry("April", 0));
                            seriesData.add(new CustomDataEntry("May", taskcount));
                            seriesData.add(new CustomDataEntry("June", 0));
                            seriesData.add(new CustomDataEntry("July", 0));
                            seriesData.add(new CustomDataEntry("August", 0));
                            seriesData.add(new CustomDataEntry("September", 0));
                            seriesData.add(new CustomDataEntry("October", 0));
                            seriesData.add(new CustomDataEntry("November", 0));
                            seriesData.add(new CustomDataEntry("December", 0));

                            Set set = Set.instantiate();
                            set.data(seriesData);
                            Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

                            Line series1 = cartesian.line(series1Mapping);
                            series1.name("Task");
                            series1.hovered().markers().enabled(true);
                            series1.hovered().markers()
                                    .type(MarkerType.CIRCLE)
                                    .size(4d);
                            series1.tooltip()
                                    .position("right")
                                    .anchor(String.valueOf(Anchor.LEFT_CENTER))
                                    .offsetX(5d)
                                    .offsetY(5d);



                            cartesian.legend().enabled(true);
                            cartesian.legend().fontSize(13d);
                            cartesian.legend().padding(0d, 0d, 10d, 0d);

                            anyChartView1.setChart(cartesian);


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









     return view;



    }
    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);
        }
    }


}

