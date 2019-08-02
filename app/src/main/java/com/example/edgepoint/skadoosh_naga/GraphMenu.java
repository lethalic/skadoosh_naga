package com.example.edgepoint.skadoosh_naga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GraphMenu extends AppCompatActivity {

    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    String password,username;
    int graphLevelInfo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_menu);

//        loadDatabaseServer();
        homeGraphButton();
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        DatabaseAccess databasegraphlevel = DatabaseAccess.getInstance(GraphMenu.this);
        databasegraphlevel.open();
        graphLevelInfo = databasegraphlevel.graphlevelcount(username,password,"graph_login");
        databasegraphlevel.close();
        homeGraphButton();
    }

    public void onButtonClick(View v) {

        if (v.getId() == R.id.city_level) {
            if (graphLevelInfo > 2) {
                Intent i = new Intent(GraphMenu.this, GraphActivity.class);
                startActivity(i);
            }
            else Toast.makeText(GraphMenu.this,"Cannot Access using the login credentials", Toast.LENGTH_LONG).show();
        }

        if (v.getId() == R.id.barangay_level) {

            if (graphLevelInfo > 1) {
                Intent i = new Intent(GraphMenu.this, GraphActivity2.class);
                startActivity(i);
            }
            else Toast.makeText(GraphMenu.this,"Cannot Access using the login credentials", Toast.LENGTH_LONG).show();
        }

        if (v.getId() == R.id.precinct_level) {

            if (graphLevelInfo > 0) {
                Intent i = new Intent(GraphMenu.this, GraphActivity3.class);
                startActivity(i);
            }
            else Toast.makeText(GraphMenu.this,"Cannot Access using the login credentials", Toast.LENGTH_LONG).show();
        }

    }

    private void homeGraphButton(){
        Button prevbutton = (Button) findViewById(R.id.backgraphmenu);
        prevbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GraphMenu.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

    }

}
