package com.example.edgepoint.skadoosh_naga;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class mGenerateGraphView extends AppCompatActivity {

    BarChart barChart;
    String[] legendName = {};
    String[] congressmanlegendName = {"Bordado", "Roco", "Villafuerte" ,"Undecided" , "No Entry"};
    String[] mayorlegendName = {"Siertong Legacion","Legacion", "Siertong Mendoza","Mendoza","Siertong Ortega","Ortega","Mayo pang napipili","Dai maboto", "No Entry"};
    String[] vicemayorlegendName = {"De Asis", "Miranda","Riva","Undecided" , "No Entry"};
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
        setContentView(R.layout.activity_m_generate_graph_view);

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

        TextView date_tv = (TextView) findViewById(R.id.Mdate_id);

        TextView connection_tv = (TextView) findViewById(R.id.Mconnection_id);
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

        if (label.equals("Congressman")){
            legendName = congressmanlegendName;
        }
        else if (label.equals("Mayor")){
            legendName = mayorlegendName;
        }
        else if (label.equals("ViceMayor")){
            legendName = vicemayorlegendName;
        }
        else if (label.equals("Kagawad")){
            legendName = kagawadlegendName;
        }

        TextView label_tv = (TextView) findViewById(R.id.Mgraphlabel_tv_id);
        label_tv.setText(label);

        barChart = (BarChart) findViewById(R.id.mbarchart);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        ArrayList<BarEntry> barEntries = databaseAccess.getBarEntriesM(tableGraph);

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        databaseAccess.close();

        BarData theData = new BarData(barDataSet);
        theData.setValueTextSize(15);
        theData.setBarWidth(0.9f);

        barDataSet.setColors(COLOURS);

        barDataSet.setValueFormatter(new MyValueFormatter());

        Legend legend = barChart.getLegend();
        legend.setEnabled(true);
        legend.setWordWrapEnabled(false);
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

        barChart.setData(theData);
        barChart.setEnabled(true);
        barChart.setDescription(null);
        barChart.setDrawValueAboveBar(false);

        //barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getXAxis().setDrawGridLines(false);

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleYEnabled(true);
        barChart.setScaleXEnabled(false);
        barChart.animateY(2000);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.setExtraOffsets(0, 5, 0, 20);


        DatabaseAccess datacountM = DatabaseAccess.getInstance(mGenerateGraphView.this); //count the names
        datacountM.open();
        List<Integer> quaters2 = datacountM.getEntriesYM(tableGraph);
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
            float cmptmp = ((float) comp / (float) div)  * 100;
            DecimalFormat df = new DecimalFormat("###,###,###.#");
            String dft = df.format(cmptmp);

            String comptemp = Float.toString(cmptmp);
            //Toast.makeText(mGenerateGraphView.this,comptemp,Toast.LENGTH_LONG).show();
            quarters1.add(dft+"%");
        }

        final String[] quarters = quarters1.toArray(new String[quarters1.size()]);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        xAxis.setTextSize(12);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);

        YAxis yAxis = barChart.getAxisRight();
        yAxis.setEnabled(false);
        YAxis yAxis1 = barChart.getAxisLeft();
        yAxis1.setValueFormatter(new LargeValueFormatter());
        yAxis1.setAxisMinimum(0f);

        barChart.invalidate();
    }

    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,###.#");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            //float mvalue = (value/105364)*100;
            //String xformat = mvalue1 + " % " + mvalue2;
            //return mFormat.format(value) + " (" + mFormat.format(mvalue) + "%" +")";
            return mFormat.format(value);
        }
    }




    public void backGraphViewButton(){
        Button prev2button = (Button) findViewById(R.id.Mbackgraphviewbtn);
        prev2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
