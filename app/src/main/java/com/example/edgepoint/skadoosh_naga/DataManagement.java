package com.example.edgepoint.skadoosh_naga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DataManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_management);

        homeButton();
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.upload_menu) {
            Intent i = new Intent(DataManagement.this, UploadToServer.class);
            startActivity(i);
        }

        if (v.getId() == R.id.batch_menu) {
            Intent i = new Intent(DataManagement.this, DownloadBatch.class);
            startActivity(i);
        }

        if (v.getId() == R.id.canvass_menu) {
            Intent i = new Intent(DataManagement.this, EndCanvassing.class);
            startActivity(i);
        }

        if (v.getId() == R.id.download_menu) {
            Intent i = new Intent(DataManagement.this, DownloadFromServer.class);
            startActivity(i);
        }

    }

    private void homeButton(){
        Button prevbutton = (Button) findViewById(R.id.dmhome_btn);
        prevbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DataManagement.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

    }

}
