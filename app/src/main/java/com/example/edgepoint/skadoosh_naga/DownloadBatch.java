package com.example.edgepoint.skadoosh_naga;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DownloadBatch extends AppCompatActivity {
    Button buttonDownload;
    private static final String URL_DATABASE = "http://162.144.86.26/skadoosh_nagamayor/getBatch.php";
    private BroadcastReceiver broadcastReceiver;
    ArrayAdapter<String> adapterBatchList;
    ListView BatchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_batch);
        buttonDownload = (Button) findViewById(R.id.download_btn);
        BatchList = (ListView) findViewById(R.id.batch_listview);

        TextView batch_tv = findViewById(R.id.batch_tv);

        DatabaseAccess databaseSearchUpload = DatabaseAccess.getInstance(DownloadBatch.this);
        databaseSearchUpload.open();
        String BatchName = databaseSearchUpload.getBatchName();
        databaseSearchUpload.close();

        batch_tv.setText("BATCH FILE ( "+BatchName+" )");

        HomeButton();
        BackButton();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ( checkNetworkConnection()){
                    getBatch();
                }
                else
                {
                    buttonDownload.setVisibility(View.INVISIBLE);
                    BatchList.setAdapter(null);
                    Toast.makeText(DownloadBatch.this, "Unable to Connect. No Internet Connection.", Toast.LENGTH_LONG).show();
                }
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    private void getBatch() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating From Server...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATABASE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            int count = array.length();
                            String batch_Nstring = "";
                            int batch_Ncount = 0;
                            if (count != 0){
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject datalist = array.getJSONObject(i);

                                    int batch_count = datalist.getInt("batch_count");
                                    String batch_name = datalist.getString("batch_name");

                                    DatabaseAccess databaseAccessBatch = DatabaseAccess.getInstance(DownloadBatch.this);
                                    databaseAccessBatch.open();
                                    String BatchName = databaseAccessBatch.getBatchName();
                                    databaseAccessBatch.close();

                                    if (batch_name.equals(BatchName)){
                                        Toast.makeText(DownloadBatch.this, "Batch File is Empty", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(DownloadBatch.this, "New Batch File is Available", Toast.LENGTH_SHORT).show();
                                        batchlist(batch_name);
                                        batch_Nstring = batch_name;
                                        batch_Ncount = batch_count;
                                    }
                                }

                                Download(batch_Nstring,batch_Ncount);
                                progressDialog.dismiss();
                            }
                            else{
                                Toast.makeText(DownloadBatch.this, "Data is Empty!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DownloadBatch.this, "Unable to Connect. No Internet Connection.", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void batchlist(String batchArr){
        buttonDownload.setVisibility(View.VISIBLE);
        List<String> batchName = new ArrayList<> ();
        batchName.add(batchArr);

        adapterBatchList = new ArrayAdapter<String>(DownloadBatch.this,android.R.layout.simple_list_item_1, batchName);
        BatchList.setAdapter(adapterBatchList);
    }

    public void Download(final String batch_name, final int batch_count){
        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progress = new ProgressDialog(DownloadBatch.this);
                progress.setMessage("Saving New Batch File and Resetting All Data...");
                progress.show();

                DatabaseAccess databaseAccessSave= DatabaseAccess.getInstance(DownloadBatch.this);
                databaseAccessSave.open();
                boolean checkBatch = databaseAccessSave.checkDuplicateBatch(batch_name);
                boolean saveBatch = databaseAccessSave.updateBatch(batch_name,batch_count);
                if (checkBatch == true){
                    boolean updateBatch = databaseAccessSave.updateBatchFile(batch_name);
                    if(saveBatch == true && updateBatch == true) {
                        buttonDownload.setVisibility(View.INVISIBLE);
                        BatchList.setAdapter(null);
                        boolean deleteData = databaseAccessSave.resetAllData();
                        if(deleteData == true){
                            Toast.makeText(DownloadBatch.this,"New Batch Saved",Toast.LENGTH_LONG).show();
                            progress.dismiss();
                        }
                    }
                    else
                        Toast.makeText(DownloadBatch.this,"Batch Not Save",Toast.LENGTH_LONG).show();
                }
                else {
                    boolean insertBatch = databaseAccessSave.insertBatch(batch_name);
                    if(saveBatch == true && insertBatch == true) {
                        buttonDownload.setVisibility(View.INVISIBLE);
                        BatchList.setAdapter(null);
                        boolean deleteData = databaseAccessSave.resetAllData();
                        if(deleteData == true){
                            Toast.makeText(DownloadBatch.this,"New Batch Saved",Toast.LENGTH_LONG).show();
                            progress.dismiss();
                        }
                    }
                    else
                        Toast.makeText(DownloadBatch.this,"Batch Not Save",Toast.LENGTH_LONG).show();
                }

                databaseAccessSave.close();

            }
        });
    }

    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());

    }

    private void HomeButton(){
        Button homebtn = (Button) findViewById(R.id.homedown_btn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DownloadBatch.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

    }
    private void BackButton(){
        Button prevbutton = (Button) findViewById(R.id.bckdown_btn);
        prevbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
