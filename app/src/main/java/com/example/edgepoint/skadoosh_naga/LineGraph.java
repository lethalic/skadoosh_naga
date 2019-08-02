package com.example.edgepoint.skadoosh_naga;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Map;

public class LineGraph extends AppCompatActivity {

    private static final String TAG = "LineGraph";

    private LineChart lchart;

    String LineChartName;

    int[] COLOURS = { Color.parseColor("#e6194B"), Color.parseColor("#3cb44b"), Color.parseColor("#ffe119"),Color.parseColor("#4363d8"),
            Color.parseColor("#f58231"), Color.parseColor("#911eb4"), Color.parseColor("#42d4f4"),Color.parseColor("#f032e6"),
            Color.parseColor("#000075"),Color.parseColor("#fabebe"),Color.parseColor("#9A6324"),Color.parseColor("#469990"),
            Color.parseColor("#bfef45"),Color.parseColor("#800000"),Color.parseColor("#aaffc3"),Color.parseColor("#808000"),
            Color.parseColor("#00b3ca")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        backGraphViewButton();

        Intent intent = getIntent();
        String label = intent.getStringExtra("Label");
        String tableGraph = intent.getStringExtra("Table");

        TextView label_tv = (TextView) findViewById(R.id.graphlabel_tv_id);
        label_tv.setText(label);

        lchart = (LineChart) findViewById(R.id.linechart);
        Mayor(tableGraph);

        lchart.setTouchEnabled(true);
        lchart.setDragEnabled(true);
        lchart.setScaleEnabled(true);
        lchart.animateX(2000);

        lchart.setEnabled(true);
        lchart.setDescription(null);
        //  lchart.setVisibleXRange(0f,6f);

        String[] values = new String[] { "Batch 1", "Batch 2", "Batch 3", "Batch 4", "Batch 5", "Batch 6", "Batch 7", "Batch 8","Batch 9", "Batch 10", "Batch 11", "Batch 12"};

        XAxis xAxis = lchart.getXAxis();
        xAxis.setAxisMinimum(0f);
        xAxis.setValueFormatter(new MyXaxisValuesFormatter(values));
        xAxis.setGranularity(1f);
        xAxis.setTextSize(11f);

        lchart.invalidate();
    }

    public class MyXaxisValuesFormatter implements IAxisValueFormatter{

        public String[] mValues;
        public MyXaxisValuesFormatter(String[] values){
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axisBase){
            return mValues[(int)value];
        }


    }

    public void Mayor(String table_Graph){

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        ArrayList<Entry>  LineEntries1 = databaseAccess.getLineEntries(table_Graph, 1);
        ArrayList<Entry>  LineEntries2 = databaseAccess.getLineEntries(table_Graph, 2);
        ArrayList<Entry>  LineEntries3 = databaseAccess.getLineEntries(table_Graph, 3);
        ArrayList<Entry>  LineEntries4 = databaseAccess.getLineEntries(table_Graph, 4);
        ArrayList<Entry>  LineEntries5 = databaseAccess.getLineEntries(table_Graph, 5);
        ArrayList<Entry>  LineEntries6 = databaseAccess.getLineEntries(table_Graph, 6);

        databaseAccess.close();

        LineDataSet set1, set2, set3, set4, set5, set6;

        set1 = new LineDataSet(LineEntries1, "Siertong Legacion");
        set1.setFillAlpha(110);

        set2 = new LineDataSet(LineEntries2, "Legacion");
        set2.setFillAlpha(110);

        set3 = new LineDataSet(LineEntries3, "Siertong Mendoza");
        set3.setFillAlpha(110);

        set4 = new LineDataSet(LineEntries4, "Mendoza");
        set4.setFillAlpha(110);

        set5 = new LineDataSet(LineEntries5, "Mayo pang napili");
        set5.setFillAlpha(110);

        set6 = new LineDataSet(LineEntries6, "Dai maboto");
        set6.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);
        dataSets.add(set5);
        dataSets.add(set6);

        LineData data = new LineData(dataSets);

        set1.setColor(COLOURS[0]);
        set1.setCircleColor(COLOURS[0]);
        set1.setCircleHoleColor(COLOURS[0]);

        set2.setColor(COLOURS[1]);
        set2.setCircleColor(COLOURS[1]);
        set2.setCircleHoleColor(COLOURS[1]);

        set3.setColor(COLOURS[2]);
        set3.setCircleColor(COLOURS[2]);
        set3.setCircleHoleColor(COLOURS[2]);

        set4.setColor(COLOURS[3]);
        set4.setCircleColor(COLOURS[3]);
        set4.setCircleHoleColor(COLOURS[3]);

        set5.setColor(COLOURS[4]);
        set5.setCircleColor(COLOURS[4]);
        set5.setCircleHoleColor(COLOURS[4]);

        set6.setColor(COLOURS[5]);
        set6.setCircleColor(COLOURS[5]);
        set6.setCircleHoleColor(COLOURS[5]);

        lchart.setData(data);

    }

    public void backGraphViewButton(){
        Button prev2button = (Button) findViewById(R.id.backgraphviewbtn);
        prev2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



}
