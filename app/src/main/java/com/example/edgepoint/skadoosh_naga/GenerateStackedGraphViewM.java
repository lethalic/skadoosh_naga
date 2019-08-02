package com.example.edgepoint.skadoosh_naga;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GenerateStackedGraphViewM extends AppCompatActivity {
    BarChart stackedChart;
    int legendcount = 0;
    private LinearLayout graph_layout;
    String[] legendName ={};
    String[] congressmanlegendName = {"Bordado", "Roco", "Villafuerte" ,"Undecided"};
    String[] mayorlegendName = {"Siertong Legacion","Legacion", "Siertong Mendoza","Mendoza", "Siertong Ortega","Ortega","Mayo pang napipili","Dai maboto", "No Entry"};
    String[] vicemayorlegendName = {"De Asis", "Miranda","Riva","Undecided"};
    String[] kagawadlegendName = {"Abonal", "Albeus","Arroyo","Baldemoro","Castillo","Del Castillo","Del Rosario","Lavadia"
            ,"Macaraig","Perez","Ranola","Roco","Rosales","San Juan","Sergio","Tuazon"};

int[] COLOURS = { Color.parseColor("#e6194B"), Color.parseColor("#3cb44b"),
        Color.parseColor("#ffe119"),Color.parseColor("#4363d8"),
        Color.parseColor("#f58231"), Color.parseColor("#911eb4"),
        Color.parseColor("#42d4f4"),Color.parseColor("#42d4f4"),
        Color.parseColor("#9A6324"),Color.parseColor("#9A6324"),
        Color.parseColor("#469990"),Color.parseColor("#469990")};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_stacked_graph_view_m);

        backGraphViewButton();
        saveImageButton();
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

        stackedChart = (BarChart) findViewById(R.id.stack_bargraph);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        ArrayList<BarEntry> barEntries = databaseAccess.getBarEntriesStacked(tableGraph);

        BarDataSet barDataSet = new BarDataSet(barEntries, "Count");
        databaseAccess.close();

        barDataSet.setColors(COLOURS);
        barDataSet.setValueFormatter(new MyValueFormatter());

        Legend legend = stackedChart.getLegend();
        legend.setEnabled(true);
        legend.setWordWrapEnabled(false);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextSize(12);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(12);
        legend.setXEntrySpace(2);
        //legend.setOrientation();

        LegendEntry[] legendEntries = new LegendEntry[legendName.length];
        for (int i=0; i<legendEntries.length; i++)
        {
            LegendEntry entry = new LegendEntry();
            if (i<7){
                entry.label =  String.valueOf(legendName[i]);
                entry.formColor = (COLOURS[i]);
                legendEntries[i] = entry;
            }
            if(i==7){
                entry.label =  String.valueOf(legendName[7]);
                entry.formColor = (COLOURS[8]);
                legendEntries[i] = entry;
            }
            if(i==8){
                entry.label =  String.valueOf(legendName[8]);
                entry.formColor = (COLOURS[10]);
                legendEntries[i] = entry;
            }
        }

        legend.setCustom(legendEntries);


        BarData theData = new BarData(barDataSet);
        theData.setValueTextSize(15);
        theData.setBarWidth(0.9f);

        stackedChart.setData(theData);
        stackedChart.setEnabled(true);
        stackedChart.setDescription(null);
        stackedChart.setDrawValueAboveBar(false);

        stackedChart.getXAxis().setDrawGridLines(false);

        stackedChart.setTouchEnabled(true);
        stackedChart.setDragEnabled(true);
        stackedChart.setScaleYEnabled(true);
        stackedChart.setScaleXEnabled(false);
        stackedChart.animateY(2000);
        stackedChart.getXAxis().setGranularityEnabled(true);
        stackedChart.setExtraOffsets(0, 5, 0, 20);
        stackedChart.invalidate();

        DatabaseAccess datacountM = DatabaseAccess.getInstance(GenerateStackedGraphViewM.this); //count the names
        datacountM.open();
        List<Integer> quaters2 = datacountM.getEntriesYStackedM();
        datacountM.close();
        final Integer[] temp = quaters2.toArray(new Integer[quaters2.size()]);

        final List<String> quarters1 = new ArrayList<>();

        for (int i=0; i<temp.length;i++)
        {
            int comp = temp[i];
            int div =0;
            for (int x=0; x<temp.length; x++){
                div = div + temp[x];
            }
            float cmptmp = ((float) comp / (float)div)  * 100;
            DecimalFormat df = new DecimalFormat("###,###,###.#");
            String dft = df.format(cmptmp);

            //String comptemp = Integer.toString(div);
            //Toast.makeText(GenerateStackedGraphView.this,dft,Toast.LENGTH_LONG).show();

            if (dft.equals("NaN")){ quarters1.add("0%"); }
            else quarters1.add(dft+"%");

        }

        final String[] quarters = quarters1.toArray(new String[quarters1.size()]);

//        for (int i=0; i<quarters.length; i++) {
//            Toast.makeText(GenerateStackedGraphView.this,quarters[i],Toast.LENGTH_LONG).show();
//        }
//
//        final String[] quarters = {"A", "B", "C", "D", "E"};
//
//        StringBuilder builder = new StringBuilder();
//        for(int i : quarters)
//        {
//            builder.append("" + i + " ");
//        }
//        Toast.makeText(GenerateStackedGraphView.this,builder,Toast.LENGTH_LONG).show();


        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };

        XAxis xAxis = stackedChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        xAxis.setTextSize(12);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);

        YAxis yAxis = stackedChart.getAxisRight();
        yAxis.setEnabled(false);
        YAxis yAxis1 = stackedChart.getAxisLeft();
        yAxis1.setValueFormatter(new LargeValueFormatter());
        yAxis1.setAxisMinimum(0f);

        stackedChart.invalidate();
    }


    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,###.#");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

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

    public void saveImageButton(){
        graph_layout = (LinearLayout) findViewById(R.id.mgraphview_layout);
        Button saveImagebutton = (Button) findViewById(R.id.Mssbuton);
        saveImagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder setbuilder = new AlertDialog.Builder(GenerateStackedGraphViewM.this);
                setbuilder.setTitle("Enter File Name:");
                final EditText filenameEdit = new EditText(GenerateStackedGraphViewM.this);
                setbuilder.setView(filenameEdit);

                setbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String filename = filenameEdit.getText().toString();

                        if (!filename.isEmpty()){
                            saveImage(graph_layout,filename);
                        }
                        else {
                            Toast.makeText(GenerateStackedGraphViewM.this, "Enter a file name", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

                setbuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                        dialog.dismiss();
                    }
                });

                AlertDialog setDialog = setbuilder.create();
                setDialog.show();

            }
        });
    }

    private void saveImage(View v,String File_name){
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = formatter.format(new Date());

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + File_name+" "+strDate + ".jpeg";

            // create bitmap screen capture
            v.setDrawingCacheEnabled(true);
            v.setDrawingCacheBackgroundColor(0xfffafafa);
            Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
            v.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            Toast.makeText(GenerateStackedGraphViewM.this, "Image saved", Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
}
