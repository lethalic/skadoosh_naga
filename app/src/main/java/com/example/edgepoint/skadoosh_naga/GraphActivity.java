package com.example.edgepoint.skadoosh_naga;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class GraphActivity extends AppCompatActivity {
    Spinner graph_spinner;
    public ArrayAdapter adapterGraph;
    public static String ListSpinner;
    ProgressDialog progressDialog;
    BroadcastReceiver broadcastReceiver;

    public static final String URL_COUNT = "http://162.144.86.26/skadoosh_nagamayor/getCountCity.php";
    public static final String URL_LineCOUNT = "http://162.144.86.26/skadoosh_nagamayor/getCountLineCity.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        progressDialog = new ProgressDialog(GraphActivity.this);
        progressDialog.setMessage("Generating Graph...");

        graph_spinner = findViewById(R.id.graph_spinner);
        adapterGraph = ArrayAdapter.createFromResource(this, R.array.graph_column, android.R.layout.simple_spinner_item);
        adapterGraph.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graph_spinner.setAdapter(adapterGraph);

        backGraphButton();
        generateGraph();
        generateMissingGraph();
        generateLineGraph();

    }

    public void generateGraph(){

        Button generategraphbtn =  findViewById(R.id.generategraphbtn);
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
        ListSpinner = graph_spinner.getSelectedItem().toString();
        List<String> labels = new ArrayList<>();
        String table_graph = "";
        switch (ListSpinner){
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
            //              table_graph = "mayor_graph";
        }

        if ( checkNetworkConnection()){
            CountInServer(ListSpinner,table_graph,"online","withoutMissing");
        }
        else
        {

            CountInLocal(labels,ListSpinner,table_graph,"offline");
        }

    }

    public void CountInLocal(List<String> labelsLocal,String candidateSpinner, String table_graph_local, String connection_local){
        List<Integer> count = new ArrayList<>();
        DatabaseAccess datacount = DatabaseAccess.getInstance(GraphActivity.this); //count the names
        datacount.open();

        for (int i=0; i<labelsLocal.size();i++)
        {
            if (ListSpinner.equals("Kagawad")){
                count.add(datacount.getcountname("true",labelsLocal.get(i)));
            }
            else {count.add(datacount.getcountname(labelsLocal.get(i),ListSpinner));}
        }
        datacount.close();

        DatabaseAccess insertdata = DatabaseAccess.getInstance(GraphActivity.this); //insert data
        insertdata.open();
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

//        for (int i=0; i<count.size();i++)
//        {
//            if (labelsLocal.get(i).contains("Siertong")){
////                Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
//                insertdata.insertCountStackedS(labelsLocal.get(i), count.get(i), table_graph_local);
//            }
//            else
//                insertdata.insertCountStacked(labelsLocal.get(i), count.get(i), table_graph_local);
//
//        }


        progressDialog.dismiss();

        if (candidateSpinner.equals("Kagawad")) {
            openKagawadGraph(candidateSpinner, table_graph_local, connection_local);
        }
        else {
            openGraph(candidateSpinner, table_graph_local, connection_local);
        }

    }

    public void generateMissingGraph(){

        Button miss_generategraphbtn = findViewById(R.id.generategraphbtn_m2);
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
        ListSpinner = graph_spinner.getSelectedItem().toString();

        List<String> labels = new ArrayList<>();
        List<String> mlabels = new ArrayList<>();
        String table_graph = "";
        switch (ListSpinner){
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
            CountInServer(ListSpinner,table_graph,"online","Missing");
        }
        else
        {
            MCountInLocal(labels,mlabels,ListSpinner,table_graph,"offline");
        }

    }

    public void MCountInLocal(List<String> labelsLocal,List<String> MlabelsLocal,String candidateSpinner, String table_graph_local, String connection_local){
        List<Integer> count = new ArrayList<>();
        DatabaseAccess datacount = DatabaseAccess.getInstance(GraphActivity.this); //count the names
        datacount.open();
        for (int i=0; i<labelsLocal.size();i++)
        {
            if (candidateSpinner.equals("Kagawad")){
                if (labelsLocal.get(i).equals("")){
                    count.add(datacount.getcountname(labelsLocal.get(i),"ABONAL"));
                }
                else count.add(datacount.getcountname("true",labelsLocal.get(i)));
            }
            else {count.add(datacount.getcountname(labelsLocal.get(i),candidateSpinner));}
        }
        datacount.close();


        DatabaseAccess insertdata = DatabaseAccess.getInstance(GraphActivity.this); //insert data
        insertdata.open();
//        for (int i=0; i<MlabelsLocal.size();i++)
//        {
//            insertdata.minsertCount(i, MlabelsLocal.get(i), count.get(i),table_graph_local);
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

//        StringBuilder builder = new StringBuilder();
//        for(int i : count)
//        {
//            builder.append("" + i + " ");
//        }
//        Toast.makeText(GraphActivity.this,builder,Toast.LENGTH_LONG).show();


        progressDialog.dismiss();
        if (candidateSpinner.equals("Kagawad")) {
            openKagawadGraphM(candidateSpinner, table_graph_local, connection_local);
        }
        else {
            openMissingGraph(candidateSpinner, table_graph_local, connection_local);
        }

    }

    public void CountInServer(final String candidateSpinner, final String table_graph_server, final String connection_server, final String generate){
        RequestQueue queue = Volley.newRequestQueue(GraphActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_COUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            int countArr = array.length();
                            if (countArr != 0){
                                //traversing through all the object
                                DatabaseAccess insertdata = DatabaseAccess.getInstance(GraphActivity.this); //insert data
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
                                            openGraph(candidateSpinner, table_graph_server,connection_server);
                                        }
                                        break;
                                    case "Missing":
                                        if (candidateSpinner.equals("Kagawad")) {
                                            openKagawadGraphM(candidateSpinner, table_graph_server,connection_server);
                                        }
                                        else {
                                            openMissingGraph(candidateSpinner, table_graph_server,connection_server);
                                        }
                                        break;
                                }


                            }
                            else{
                                Toast.makeText(GraphActivity.this, "Data is Empty!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(GraphActivity.this, "Sending Failed! No Internet Connection.", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("candidate", candidateSpinner);
                params.put("generate", generate);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void openGraph(String label, String table_graph, String connection){

        Intent intent = new Intent(this, GenerateStackedGraphView.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        intent.putExtra("Connection",connection);
        startActivity(intent);
    }

    public void openMissingGraph(String label, String table_graph, String connection){

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
        Button prevbutton = findViewById(R.id.backgraphbtn);
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
        Button LineGraph = findViewById(R.id.linechart_btn);
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
        ListSpinner = graph_spinner.getSelectedItem().toString();
        DatabaseAccess databaseAccessBatch = DatabaseAccess.getInstance(GraphActivity.this);
        databaseAccessBatch.open();
        int BatchCount = databaseAccessBatch.getBatchCount();
        databaseAccessBatch.close();

        if (BatchCount == 1){
            progressDialog.dismiss();
            Toast.makeText(GraphActivity.this, "Cannot Generate Line Graph. Batch should be more than 1.", Toast.LENGTH_SHORT).show();
        }
        else {
            if ( checkNetworkConnection()){
                for (int x=1;x<=BatchCount;x++) {
                    String batchName = "batch"+x+"_list";
                    CountLineServer(ListSpinner, "LineGraph",batchName);
                }

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        openLineGraph(ListSpinner,"LineGraph");
                    }
                },5000);

            }
            else
            {
                progressDialog.dismiss();
                openLineGraph(ListSpinner, "LineGraph");
            }
        }


    }

    public void openLineGraph(String label, String table_graph){
        Intent intent = new Intent(this, LineGraph.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        startActivity(intent);
    }

    public void CountLineServer(final String canSpinner, final String table_lineGraph,final String batch_name){
        RequestQueue queue = Volley.newRequestQueue(GraphActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LineCOUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            int countArr = array.length();
                            if (countArr != 0){
                                DatabaseAccess insertdata = DatabaseAccess.getInstance(GraphActivity.this); //insert data
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
                                Toast.makeText(GraphActivity.this, "Data is Empty!", Toast.LENGTH_SHORT).show();
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
                params.put("batch_name", batch_name);
                return params;
            }
        };

        queue.add(stringRequest);
    }

}
