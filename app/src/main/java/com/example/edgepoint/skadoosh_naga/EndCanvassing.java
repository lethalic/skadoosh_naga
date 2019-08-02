package com.example.edgepoint.skadoosh_naga;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EndCanvassing extends AppCompatActivity {
    EditText email,user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_canvassing);

        user = (EditText) findViewById(R.id.fullname);
        email = (EditText) findViewById(R.id.email);


        HomeButton();
        BackButton();

        Button buttonRequest = (Button) findViewById(R.id.request_btn);
        //adding click listener to button
        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( checkNetworkConnection()){
                    if (user.getText().toString().equals("") && email.getText().toString().equals("")){
                        Toast.makeText(EndCanvassing.this,"Please Fill-up Full name and Email", Toast.LENGTH_SHORT).show();
                    }else{
                        sendMail();
                    }
                }
                else Toast.makeText(EndCanvassing.this, "No Internet Connection.", Toast.LENGTH_LONG).show();

            }
        });
    }


    public void sendMail(){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EndCanvassing.this);
                alertDialogBuilder.setTitle("Confirm Send Request.");
                alertDialogBuilder.setMessage("Are you sure?");
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick (DialogInterface dialogInterface,int i){
                        try
                        {
                            String emailstr = email.getText().toString();
                            LongOperation l=new LongOperation();
                            l.execute(emailstr);  //sends the email in background
                            Toast.makeText(EndCanvassing.this, l.get(), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }

                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick (DialogInterface dialog,int which){
                    }

                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
    }

    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());

    }

    private void HomeButton(){
        Button homebtn = (Button) findViewById(R.id.home_canvass);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EndCanvassing.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

    }
    private void BackButton(){
        Button prevbutton = (Button) findViewById(R.id.bck_canvass);
        prevbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
