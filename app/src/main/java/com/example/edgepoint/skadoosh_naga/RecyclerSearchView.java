package com.example.edgepoint.skadoosh_naga;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecyclerSearchView extends AppCompatActivity implements RecyclerSearchViewAdapter.OnItemCLickListener {
    private RecyclerSearchViewAdapter adapter;
    private ArrayList<ViewInfoRecycler> exampleList;

    private RecyclerView dRecyclerView;
    private RecyclerView.LayoutManager dLayoutManager;
    ProgressDialog progressDialog;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_search_view);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
                setUpRecyclerView();
                SearchBackButton();
            }
        },1000);

    }

    private void setUpRecyclerView() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        exampleList = databaseAccess.getRecyclerVoterName();
        databaseAccess.close();
        dRecyclerView = findViewById(R.id.recycler_view);
        dRecyclerView.setHasFixedSize(true);
        dLayoutManager = new LinearLayoutManager(this);
        adapter = new RecyclerSearchViewAdapter(exampleList);

        dRecyclerView.setLayoutManager(dLayoutManager);
        dRecyclerView.setAdapter(adapter);
        progressDialog.dismiss();
        adapter.setOnItemClickListener(RecyclerSearchView.this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        return true;
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, Information_Voter.class);
        ViewInfoRecycler clickedItem = exampleList.get(position);
        //Toast.makeText(getApplicationContext(),"Clicked "+clickedItem.getName(),Toast.LENGTH_LONG).show();

        detailIntent.putExtra("VotersName", clickedItem.getName());
        startActivity(detailIntent);
    }
    public void SearchBackButton(){
        Button prev2button = (Button) findViewById(R.id.backprofbtn);

        prev2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RecyclerSearchView.this);
                alertDialogBuilder.setTitle("Confirm Exit");
                alertDialogBuilder.setMessage("This will restart the loading process. Are you sure you want to EXIT?");
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("YES",new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick (DialogInterface dialogInterface,int i){
                        finish();
                    }

                });

                alertDialogBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick (DialogInterface dialog,int which){
                    }

                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }
}
