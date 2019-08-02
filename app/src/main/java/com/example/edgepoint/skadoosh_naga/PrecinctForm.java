package com.example.edgepoint.skadoosh_naga;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class PrecinctForm extends AppCompatActivity {

    ListView precinctList;
    String barangay_form;
    Form form;
    AlertDialog.Builder alertInvalid,alertError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precinct_form);

        Intent intent = getIntent();

        form = intent.getParcelableExtra("Form");

        barangay_form = form.getBarangay();


        DatabaseAccess databaseAccessPrecinct = DatabaseAccess.getInstance(this);
        databaseAccessPrecinct.open();
        List<String> precinct = databaseAccessPrecinct.getDatabase(barangay_form,"Barangay=?","Precinct" ,1);
        databaseAccessPrecinct.close();

        precinctList = (ListView) findViewById(R.id.list_voters);
        precinctList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, precinct));

        precinctList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String precinct_form =  (String) precinctList.getItemAtPosition(position);

                form = new Form(barangay_form,precinct_form,"");

                showLoginDialog(precinct_form);

            }
        });

        form2PrevButton();

        homeButton();

    }

    private void showLoginDialog(final String pct) {
        alertInvalid = new AlertDialog.Builder(PrecinctForm.this);
        alertInvalid.setMessage("Invalid Username & Password");
        alertInvalid.setPositiveButton("OK",null);
        alertError = new AlertDialog.Builder(PrecinctForm.this);
        alertError.setTitle("( Precinct : "+pct+" )");
        alertError.setMessage("Invalid Login Credential.");
        alertError.setPositiveButton("OK",null);

        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.login_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);
        final EditText user = (EditText) prompt.findViewById(R.id.login_name);
        final EditText pass = (EditText) prompt.findViewById(R.id.login_password);
        user.setText(pct);
        alertDialogBuilder.setTitle("PRECINCT LOGIN");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String password = pass.getText().toString();
                        String username = user.getText().toString();
                        try {

                            DatabaseAccess databaseloginInfo = DatabaseAccess.getInstance(PrecinctForm.this);
                            databaseloginInfo.open();
                            if (username.equals(pct) ){
                                boolean LoginInfo = databaseloginInfo.loginInfo(username,password,"users_table");
                                if (LoginInfo == true){
                                    Intent intent = new Intent(PrecinctForm.this, VotersForm.class);
                                    intent.putExtra("Form",form);
                                    startActivity(intent);
                                }
                                else alertInvalid.show();
                            }
                            else alertError.show();
                            databaseloginInfo.close();

                        } catch (Exception e) {
                            alertInvalid.show();
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

    private void homeButton() {
        Button btnBack = (Button) findViewById(R.id.homebtn);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PrecinctForm.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    public void form2PrevButton(){
        Button prev2button = (Button) findViewById(R.id.prev2btn_id);
        prev2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
