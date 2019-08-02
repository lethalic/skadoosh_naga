package com.example.edgepoint.skadoosh_naga;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Graph_KagawadM extends AppCompatActivity {

    HorizontalBarChart HbarChart;

    int legendcount = 0;
    String[] legendName ={};
    String[] kagawadlegendName = {"Abonal", "Albeus","Arroyo","Baldemoro","Castillo","Del Castillo","Del Rosario","Lavadia"
            ,"Macaraig","Perez","Ranola","Roco","Rosales","San Juan","Sergio","Tuazon", "No Entry"};

    int[] COLOURS = { Color.parseColor("#e6194B"), Color.parseColor("#3cb44b"), Color.parseColor("#ffe119"),Color.parseColor("#4363d8"),
            Color.parseColor("#f58231"), Color.parseColor("#911eb4"), Color.parseColor("#42d4f4"),Color.parseColor("#f032e6"),
            Color.parseColor("#000075"),Color.parseColor("#fabebe"),Color.parseColor("#9A6324"),Color.parseColor("#469990"),
            Color.parseColor("#bfef45"),Color.parseColor("#800000"),Color.parseColor("#aaffc3"),Color.parseColor("#808000"),
            Color.parseColor("#00b3ca")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph__kagawad_m);

        backGraphViewButton();

        Intent intent = getIntent();
        String label = intent.getStringExtra("Label");
        String tableGraph = intent.getStringExtra("Table");
        String connection = intent.getStringExtra("Connection");

        DatabaseAccess databaseAccessDate = DatabaseAccess.getInstance(this);
        databaseAccessDate.open();
        long DateMilliSeconds = databaseAccessDate.getUploadDate();
        databaseAccessDate.close();

        DateFormat dateformat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(DateMilliSeconds);
        String Date = dateformat.format(calendar.getTime());

        TextView date_tv = (TextView) findViewById(R.id.Mkdate_id);

        TextView connection_tv = (TextView) findViewById(R.id.Mkconnection_id);
        if (connection.equals("online")){
            date_tv.setText("Data in Main Server");
            connection_tv.setTextColor(Color.parseColor("#00ff00"));
        }else {
            if (DateMilliSeconds == 0){
                date_tv.setText("Local Data");
            }
            else {
                date_tv.setText("As of "+Date);
            }
            connection_tv.setTextColor(Color.parseColor("#ff0000"));
        }
        connection_tv.setText("("+connection+")");

        legendName = kagawadlegendName;

        TextView label_tv = (TextView) findViewById(R.id.graphlabel_tv_id);
        label_tv.setText(label);

        HbarChart = (HorizontalBarChart) findViewById(R.id.hbargraphM);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        ArrayList<BarEntry> barEntries = databaseAccess.getBarEntriesM(tableGraph);

        BarDataSet barDataSet = new BarDataSet(barEntries, "Count");
        databaseAccess.close();

        barDataSet.setColors(COLOURS);
        barDataSet.setValueFormatter(new MyValueFormatter());

        BarData theData = new BarData(barDataSet);
        theData.setValueTextSize(15);
        theData.setBarWidth(0.9f);

        HbarChart.setData(theData);
        HbarChart.setEnabled(true);
        HbarChart.setDescription(null);
        HbarChart.setDrawValueAboveBar(true);
//        HbarChart.setExtraBottomOffset(100f);
//        HbarChart.setViewPortOffsets(0, 0, 0, 0);
//        HbarChart.getXAxis().setDrawGridLines(true);

        Legend legend = HbarChart.getLegend();
        legend.setEnabled(true);
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextSize(12);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(12);
        legend.setXEntrySpace(2);

        LegendEntry[] legendEntries = new LegendEntry[legendName.length];
        for (int i=0; i<legendEntries.length; i++)
        {
            LegendEntry entry = new LegendEntry();
            entry.label =  String.valueOf(legendName[i]);
            entry.formColor = (COLOURS[i]);
            legendEntries[i] = entry;
        }

        legend.setCustom(legendEntries);

        HbarChart.setTouchEnabled(true);
        HbarChart.setDragEnabled(true);
        HbarChart.setScaleYEnabled(false);
        HbarChart.setScaleXEnabled(true);
        HbarChart.animateY(2000);
        HbarChart.setVisibleXRange(17f, 17f);
        HbarChart.setExtraOffsets(0, 0, 50, 0);

        DatabaseAccess datacountM = DatabaseAccess.getInstance(Graph_KagawadM.this); //count the names
        datacountM.open();
        List<Integer> quaters2 = datacountM.getEntriesY(tableGraph);
        datacountM.close();
        final Integer[] temp = quaters2.toArray(new Integer[quaters2.size()]);

        final List<String> quarters1 = new ArrayList<>();

        for (int i=0; i<temp.length;i++)
        {
            int comp = temp[i];
            int div =0;
            for (int x=0; x<legendName.length; x++){
                div = div + temp[x];
            }
            float cmptmp = ((float) comp / (float)div)  * 100;
            DecimalFormat df = new DecimalFormat("###,###,###.#");
            String dft = df.format(cmptmp);
            if (dft.equals("NaN")){ quarters1.add("0%"); }
            else quarters1.add(dft+"%");

        }


        final String[] quarters = quarters1.toArray(new String[quarters1.size()]);


        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };

        XAxis xAxis = HbarChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(quarters1.size(),false);

        YAxis yAxisR = HbarChart.getAxisRight();
        yAxisR.setEnabled(false);
        yAxisR.setAxisMaximum(0f);
        yAxisR.setGranularity(1f);


        YAxis yAxisL = HbarChart.getAxisLeft();
        yAxisL.setEnabled(true);
        yAxisL.setValueFormatter(new LargeValueFormatter());
        yAxisL.setAxisMinimum(0f);
        yAxisL.setGranularity(1f);

        HbarChart.invalidate();
    }
    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,###.#");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
//            float mvalue = (value/105364)*100;
//            String xformat = mvalue1 + " % " + mvalue2;
//            return mFormat.format(value) + " (" + mFormat.format(mvalue) + "%" +")";
            return mFormat.format(value);
        }
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
