package com.example.edgepoint.skadoosh_naga;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphActivity3 extends AppCompatActivity {

    Spinner barangay_spinner;
    Spinner precinct_spinner;
    Spinner graph_spinner;
    public ArrayAdapter adapterGraph1;
    public static String Bgypsin;
    public static String Prcntpsin;
    public static String ListSpinner1;
    ProgressDialog progressDialog,loading;
    BroadcastReceiver broadcastReceiver;

    public static final String URL_COUNT_PRECINCT = "http://162.144.86.26/skadoosh_nagamayor/getCountPrecinct.php";
    public static final String URL_LineCOUNT = "http://162.144.86.26/skadoosh_nagamayor/getCountLinePrecinct.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph3);

        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        progressDialog = new ProgressDialog(GraphActivity3.this);
        progressDialog.setMessage("Generating Graph...");

        loading = new ProgressDialog(GraphActivity3.this);
        loading.setMessage("Loading Data...");
        loading.show();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                graphactivity3();
                backGraphButton();
                generateGraph3();
                generateMissingGraph3();
                generateLineGraph();
            }
        },1500);


    }

    public void graphactivity3(){
        barangay_spinner = findViewById(R.id.barangay_spinner);
        precinct_spinner =  findViewById(R.id.precinct_spinner);
        graph_spinner = findViewById(R.id.graph_spinner3);


        adapterGraph1 = ArrayAdapter.createFromResource(this, R.array.graph_column, android.R.layout.simple_spinner_item);
        adapterGraph1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graph_spinner.setAdapter(adapterGraph1);

        DatabaseAccess databaseAccessBarangay = DatabaseAccess.getInstance(this);
        databaseAccessBarangay.open();
        final List<String> barangays = databaseAccessBarangay.setDatabase("Barangay", 0);
        databaseAccessBarangay.close();

        ArrayAdapter<String> adapterBarangay = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, barangays);
        barangay_spinner.setAdapter(adapterBarangay);


        barangay_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Bgypsin = barangay_spinner.getSelectedItem().toString();

                DatabaseAccess databaseAccessPrecinct = DatabaseAccess.getInstance(GraphActivity3.this);
                databaseAccessPrecinct.open();
                List<String> precinct = databaseAccessPrecinct.getDatabase(Bgypsin,"Barangay=?","Precinct" ,1);
                databaseAccessPrecinct.close();

                ArrayAdapter<String> adapterPrecinct = new ArrayAdapter<>(GraphActivity3.this,android.R.layout.simple_spinner_item, precinct);
                adapterPrecinct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterPrecinct.notifyDataSetChanged();
                precinct_spinner.setAdapter(adapterPrecinct);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        loading.dismiss();
    }

    public void generateGraph3(){
        Button generategraphbtn = findViewById(R.id.generategraphbtn3);
        generategraphbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        graph();
                    }
                },1000);
            }
        });
    }

    public void graph(){
        Prcntpsin = precinct_spinner.getSelectedItem().toString();
        ListSpinner1 = graph_spinner.getSelectedItem().toString();

        List<String> labels = new ArrayList<>();
        String table_graph = "";

        switch (ListSpinner1){
            case "Mayor":
                labels.add("Siertong Nelson Legacion");
                labels.add("Nelson Legacion");
                labels.add("Siertong Tato Mendoza");
                labels.add("Tato Mendoza");
                labels.add("Siertong Luis Ortega");
                labels.add("Luis Ortega");
                labels.add("Mayo pang napipili");
                labels.add("Dai maboto");
                table_graph = "mayor_graph_stacked";
                break;
        }

        if ( checkNetworkConnection()){
            CountInServer(ListSpinner1,Prcntpsin,table_graph,"online", "withoutMissing");
        }
        else
        {
            CountInLocal(labels,ListSpinner1,Prcntpsin,table_graph,"offline");
        }

    }

    public void CountInLocal(List<String> labelsLocal,String candidateSpinner, String precinctSpinner, String table_graph_local, String connection_local){
        List<Integer> count = new ArrayList<>();
        DatabaseAccess datacount = DatabaseAccess.getInstance(GraphActivity3.this); //count the names
        datacount.open();
        for (int i=0; i<labelsLocal.size();i++)
        {
            if (candidateSpinner.equals("Kagawad")){
                count.add(datacount.getcountname3("true",labelsLocal.get(i),precinctSpinner));
            }
            else {count.add(datacount.getcountname3(labelsLocal.get(i),candidateSpinner, precinctSpinner));}
        }
        datacount.close();

        DatabaseAccess insertdata = DatabaseAccess.getInstance(GraphActivity3.this); //insert data
        insertdata.open();
//        for (int i=0; i<count.size();i++)
//        {
//            insertdata.insertCount(i, labelsLocal.get(i), count.get(i),table_graph_local);
//        }
        for (int i=0; i<count.size();i++)
        {
            if (labelsLocal.get(i).contains("Siertong")){
//                Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
                insertdata.insertCountStackedS(labelsLocal.get(i), count.get(i), table_graph_local);
            }
            else{
                insertdata.insertCountStacked(labelsLocal.get(i), count.get(i), table_graph_local);
            }
        }

        insertdata.close();
        progressDialog.dismiss();

        if (candidateSpinner.equals("Kagawad")) {
            openKagawadGraph(candidateSpinner, table_graph_local, connection_local);
        }
        else {
            openGraph3(candidateSpinner, table_graph_local, connection_local);
        }
    }

    public void generateMissingGraph3(){

        Button miss_generategraphbtn = findViewById(R.id.generategraphbtn_m3);
        miss_generategraphbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Mgraph();
                    }
                },1000);
            }
        });
    }

    public void Mgraph(){
        Prcntpsin = precinct_spinner.getSelectedItem().toString();
        ListSpinner1 = graph_spinner.getSelectedItem().toString();

        List<String> labels = new ArrayList<>();
        List<String> mlabels = new ArrayList<>();
        String table_graph = "";

        switch (ListSpinner1){
            case "Mayor":
                labels.add("Siertong Nelson Legacion");
                labels.add("Nelson Legacion");
                labels.add("Siertong Tato Mendoza");
                labels.add("Tato Mendoza");
                labels.add("Siertong Luis Ortega");
                labels.add("Luis Ortega");
                labels.add("Mayo pang napipili");
                labels.add("Dai maboto");
                labels.add("");
                mlabels.add("Siertong Nelson Legacion");
                mlabels.add("Nelson Legacion");
                mlabels.add("Siertong Tato Mendoza");
                mlabels.add("Tato Mendoza");
                mlabels.add("Siertong Luis Ortega");
                mlabels.add("Luis Ortega");
                mlabels.add("Mayo pang napipili");
                mlabels.add("Dai maboto");
                mlabels.add("Missing System");
//                table_graph = "mayor_Mgraph";
                table_graph = "mayor_graph_stackedM";
                break;
        }

        if ( checkNetworkConnection()){
            CountInServer(ListSpinner1,Prcntpsin,table_graph,"online","Missing");
        }
        else
        {
            MCountInLocal(labels,mlabels,ListSpinner1,Prcntpsin,table_graph,"offline");
        }

    }

    public void MCountInLocal(List<String> labelsLocal,List<String> MlabelsLocal,String candidateSpinner,String precinctSpinner, String table_graph_local,String connection_local) {
        List<Integer> count = new ArrayList<>();
        DatabaseAccess datacount = DatabaseAccess.getInstance(GraphActivity3.this); //count the names
        datacount.open();
        for (int i=0; i<labelsLocal.size();i++)
        {
            if (candidateSpinner.equals("Kagawad")){
                if (labelsLocal.get(i).equals("")){
                    count.add(datacount.getcountname3(labelsLocal.get(i),"ABONAL", precinctSpinner));
                }
                else count.add(datacount.getcountname3("true",labelsLocal.get(i), precinctSpinner));
            }
            else {count.add(datacount.getcountname3(labelsLocal.get(i),candidateSpinner, precinctSpinner));}
        }
        datacount.close();

        DatabaseAccess insertdata = DatabaseAccess.getInstance(GraphActivity3.this); //insert data
        insertdata.open();
//        for (int i=0; i<MlabelsLocal.size();i++)
//        {
//            insertdata.minsertCount(i, MlabelsLocal.get(i), count.get(i) ,table_graph_local);
//        }
        for (int i=0; i<count.size();i++)
        {
            if (labelsLocal.get(i).contains("Siertong")){
//                Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
                insertdata.insertCountStackedS(MlabelsLocal.get(i), count.get(i), table_graph_local);
            }
            else{
                insertdata.insertCountStacked(MlabelsLocal.get(i), count.get(i), table_graph_local);
            }
        }
        insertdata.close();

        progressDialog.dismiss();

        if (candidateSpinner.equals("Kagawad")) {
            openKagawadGraphM(candidateSpinner, table_graph_local,connection_local);
        }
        else {
            openMissingGraph3(candidateSpinner ,table_graph_local,connection_local);
        }
    }

    public void CountInServer(final String candidateSpinner,  final String precinctSpinn, final String table_graph_server,final String connection_server, final String generate){
        RequestQueue queue = Volley.newRequestQueue(GraphActivity3.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_COUNT_PRECINCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            int countArr = array.length();
                            if (countArr != 0){
                                //traversing through all the object
                                DatabaseAccess insertdata = DatabaseAccess.getInstance(GraphActivity3.this); //insert data
                                insertdata.open();
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject datalist = array.getJSONObject(i);

                                    String CandidateName = datalist.getString("CandidateName");
                                    int Count = datalist.getInt("Count");

                                    switch (generate){
                                        case "withoutMissing":
                                            if (CandidateName.contains("Siertong")){
                                                insertdata.insertCountStackedS(CandidateName, Count, table_graph_server);
                                            }
                                            else{
                                                insertdata.insertCountStacked(CandidateName, Count, table_graph_server);
                                            }
                                            break;
                                        case "Missing":
                                            if (CandidateName.equals("")){
                                                CandidateName = "Missing System";
                                                if (CandidateName.contains("Siertong")){
                                                    insertdata.insertCountStackedS(CandidateName, Count, table_graph_server);
                                                }
                                                else{
                                                    insertdata.insertCountStacked(CandidateName, Count, table_graph_server);
                                                }
                                            }
                                            else {
                                                if (CandidateName.contains("Siertong")){
                                                    insertdata.insertCountStackedS(CandidateName, Count, table_graph_server);
                                                }
                                                else{
                                                    insertdata.insertCountStacked(CandidateName, Count, table_graph_server);
                                                }
                                            }
                                            break;
                                    }
                                }
                                insertdata.close();
                                progressDialog.dismiss();
                                switch (generate){
                                    case "withoutMissing":
                                        if (candidateSpinner.equals("Kagawad")) {
                                            openKagawadGraph(candidateSpinner, table_graph_server,connection_server);
                                        }
                                        else {
                                            openGraph3(candidateSpinner, table_graph_server,connection_server);
                                        }
                                        break;
                                    case "Missing":
                                        if (candidateSpinner.equals("Kagawad")) {
                                            openKagawadGraphM(candidateSpinner, table_graph_server,connection_server);
                                        }
                                        else {
                                            openMissingGraph3(candidateSpinner, table_graph_server,connection_server);
                                        }
                                        break;
                                }
                            }
                            else{
                                Toast.makeText(GraphActivity3.this, "Data is Empty!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GraphActivity3.this, error+"", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("candidate", candidateSpinner);
                params.put("precinct", precinctSpinn);
                params.put("generate", generate);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void openGraph3(String label, String table_graph, String connection){

        Intent intent = new Intent(this, GenerateStackedGraphView.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        intent.putExtra("Connection",connection);
        startActivity(intent);
    }

    public void openMissingGraph3(String label, String table_graph, String connection){

        Intent intent = new Intent(this, GenerateStackedGraphViewM.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        intent.putExtra("Connection",connection);
        startActivity(intent);
    }

    public void openKagawadGraph(String label, String table_graph, String connection){

        Intent intent = new Intent(this, Graph_Kagawad.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        intent.putExtra("Connection",connection);
        startActivity(intent);
    }

    public void openKagawadGraphM(String label, String table_graph, String connection){

        Intent intent = new Intent(this, Graph_KagawadM.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        intent.putExtra("Connection",connection);
        startActivity(intent);
    }

    private void backGraphButton(){
        Button prevbutton = findViewById(R.id.backgraphbtn2);
        prevbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());

    }

    public void generateLineGraph(){
        Button LineGraph = findViewById(R.id.linechartpct_btn);
        LineGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Linegraph();
                    }
                },1000);

            }
        });
    }

    public void Linegraph(){
        Prcntpsin = precinct_spinner.getSelectedItem().toString();
        ListSpinner1 = graph_spinner.getSelectedItem().toString();

        DatabaseAccess databaseAccessBatch = DatabaseAccess.getInstance(GraphActivity3.this);
        databaseAccessBatch.open();
        int BatchCount = databaseAccessBatch.getBatchCount();
        databaseAccessBatch.close();

        if (BatchCount == 1){
            progressDialog.dismiss();
            Toast.makeText(GraphActivity3.this, "Cannot Generate Line Graph. Batch should be more than 1.", Toast.LENGTH_SHORT).show();
        }
        else {
            if ( checkNetworkConnection()){
                for (int x=1;x<=BatchCount;x++) {
                    String batchName = "batch"+x+"_list";
                    CountLineServer(ListSpinner1,Prcntpsin, "LineGraph_pct",batchName);
                }

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        openLineGraph(ListSpinner1,"LineGraph_pct");
                    }
                },8000);

            }
            else
            {
                progressDialog.dismiss();
                openLineGraph(ListSpinner1,"LineGraph_pct");
            }
        }

    }

    public void openLineGraph(String label, String table_graph){
        Intent intent = new Intent(this, LineGraph.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        startActivity(intent);
    }

    public void CountLineServer(final String canSpinner, final String precinctSpinner, final String table_lineGraph,final String batch_name){
        RequestQueue queue = Volley.newRequestQueue(GraphActivity3.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LineCOUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            int countArr = array.length();
                            if (countArr != 0){
                                DatabaseAccess insertdata = DatabaseAccess.getInstance(GraphActivity3.this); //insert data
                                insertdata.open();
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject datalist = array.getJSONObject(i);
                                    String CandidateName = datalist.getString("CandidateName");
                                    String BatchName = datalist.getString("BatchName");
                                    int Count = datalist.getInt("Count");
                                    insertdata.insertLineCount(Count,CandidateName,BatchName,table_lineGraph);

                                }
                                insertdata.close();
                            }
                            else{
                                Toast.makeText(GraphActivity3.this, "Data is Empty!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(UploadToServer.this, "Sending Failed! No Internet Connection.", Toast.LENGTH_LONG).show();
//                        progressDialog.dismiss();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("candidate", canSpinner);
                params.put("precinct", precinctSpinner);
                params.put("batch_name", batch_name);
                return params;
            }
        };

        queue.add(stringRequest);
    }

}
