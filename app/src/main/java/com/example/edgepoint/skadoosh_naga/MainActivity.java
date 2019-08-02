package com.example.edgepoint.skadoosh_naga;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mNetworkReceiver = new NetworkMonitor();
//        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//        Button formbutton =(Button) findViewById(R.id.Bfillform);

//        formbutton.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorPrimary));

    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.Bfillform) {
            Intent i = new Intent(MainActivity.this, BarangayForm.class);
            startActivity(i);
        }

        if (v.getId() == R.id.Bviewgraphs) {
            showLoginDialog("graph");
//            Intent i = new Intent(MainActivity.this, GraphMenu.class);
//            startActivity(i);
        }

        if (v.getId() == R.id.Bviewprofile) {
            showLoginDialog("profile");
//            Intent i = new Intent(MainActivity.this, RecyclerSearchView.class);
//            startActivity(i);
        }

        if (v.getId() == R.id.Bupload2server) {
//            String submit = "main_submit";
            Intent i = new Intent(MainActivity.this, DataManagement.class);
//            i.putExtra("submit",submit);
            startActivity(i);
        }


    }

//    public void onButtonClick(View v) {
//        if (v.getId() == R.id.Bfillform) {
//            Intent i = new Intent(MainActivity.this, BarangayForm.class);
//            startActivity(i);
//        }
//
////        if (v.getId() == R.id.Bviewgraphs) {
////            showLoginDialog("graph");
//////            Intent i = new Intent(MainActivity.this, GraphMenu.class);
//////            startActivity(i);
////        }
//
//        if (v.getId() == R.id.Bviewprofile) {
//            showLoginDialog("profile");
////            Intent i = new Intent(MainActivity.this, RecyclerSearchView.class);
////            startActivity(i);
//        }
//
//        if (v.getId() == R.id.Bupload2server) {
////            String submit = "main_submit";
//            Intent i = new Intent(MainActivity.this, UploadToServer.class);
////            i.putExtra("submit",submit);
//            startActivity(i);
//        }
//
//
//    }

    private void showLoginDialog(final String ch) {
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.login_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);
        final EditText user = (EditText) prompt.findViewById(R.id.login_name);
        final EditText pass = (EditText) prompt.findViewById(R.id.login_password);
        alertDialogBuilder.setTitle("ADMIN LOGIN");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String password = pass.getText().toString();
                        String username = user.getText().toString();
                        try {

                            DatabaseAccess databaseloginInfo = DatabaseAccess.getInstance(MainActivity.this);
                            databaseloginInfo.open();

                            if (ch == "graph"){

                                boolean LoginInfo = databaseloginInfo.loginInfo(username,password,"graph_login");

                                if (LoginInfo == true){
                                    Intent i = new Intent(MainActivity.this, GraphMenu.class);
                                    i.putExtra("username",username);
                                    i.putExtra("password",password);
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(MainActivity.this,"Invalid Username or Password!", Toast.LENGTH_LONG).show();
                                }


                            }
                            else if (ch == "profile"){
                                boolean LoginInfo = databaseloginInfo.loginInfo(username,password,"admin_table");

                                if (LoginInfo == true){
                                    Intent i = new Intent(MainActivity.this, RecyclerSearchView.class);
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(MainActivity.this,"Invalid Username or Password!", Toast.LENGTH_LONG).show();
                                }

                            }
                            databaseloginInfo.close();

                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this,"Invalid Username or Password!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });

        alertDialogBuilder.show();

    }

}
