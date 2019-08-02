package com.example.edgepoint.skadoosh_naga;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Information_Voter extends AppCompatActivity {
    String profString;
    List<String> SearchInfo;
    @Override
    protected void onResume() {
        super.onResume();
        DatabaseAccess databaseSearchInfo = DatabaseAccess.getInstance(Information_Voter.this);
        databaseSearchInfo.open();
        SearchInfo = databaseSearchInfo.setInfo(profString);
        databaseSearchInfo.close();

        TextView profile_display = (TextView) findViewById(R.id.info_area);
        String str_barangay = SearchInfo.get(0);
        String str_pricinct = SearchInfo.get(1);
        String str_votersname = SearchInfo.get(2);
        String str_address = SearchInfo.get(3);
        String str_phone = SearchInfo.get(8);

        profile_display.setText("BOTANTE : "+str_votersname+"\n\n"+
                "BARANGAY : "+str_barangay+" \n\n"+
                "ADDRESS : "+str_address+"\n\n"+
                "PRESINTO : "+str_pricinct+"\n\n"+
                "PHONE NO. : "+str_phone);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information__voter);
        Intent intent = getIntent();
        profString = intent.getStringExtra("VotersName");

        DatabaseAccess databaseSearchInfo = DatabaseAccess.getInstance(Information_Voter.this);
        databaseSearchInfo.open();
        SearchInfo = databaseSearchInfo.setInfo(profString);
        databaseSearchInfo.close();

        TextView profile_display = (TextView) findViewById(R.id.info_area);
        String str_barangay = SearchInfo.get(0);
        String str_pricinct = SearchInfo.get(1);
        String str_votersname = SearchInfo.get(2);
        String str_address = SearchInfo.get(3);
        String str_phone = SearchInfo.get(8);

        profile_display.setText("BOTANTE : "+str_votersname+"\n\n"+
                "BARANGAY : "+str_barangay+" \n\n"+
                "ADDRESS : "+str_address+"\n\n"+
                "PRESINTO : "+str_pricinct+"\n\n"+
                "PHONE NO. : "+str_phone);

        VotingPreferenceButton(profString);
        CollecDataButton(str_barangay,str_pricinct,str_votersname);
        BackButton();
    }



    public void VotingPreferenceButton(final String profString){
        Button preferencebutton = (Button) findViewById(R.id.preference_id);

        preferencebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder setbuilder = new AlertDialog.Builder(Information_Voter.this);
                setbuilder.setTitle("Enter Voting Preference Password");
                final EditText passwordtext = new EditText(Information_Voter.this);
                setbuilder.setView(passwordtext);

                setbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String passwordValue = passwordtext.getText().toString();

                        if (!passwordValue.isEmpty()){
                            DatabaseAccess databasePassword = DatabaseAccess.getInstance(Information_Voter.this);
                            databasePassword.open();
                            boolean checkPassword = databasePassword.votersPreference(passwordValue);
                            databasePassword.close();

                            if (checkPassword == true){
                                showPreferenceDialog(profString);
                            }
                            else {
                                Toast.makeText(Information_Voter.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(Information_Voter.this, "Enter a password", Toast.LENGTH_SHORT).show();
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

    private void showPreferenceDialog(String profString) {
        DatabaseAccess databaseSearchInfo = DatabaseAccess.getInstance(Information_Voter.this);
        databaseSearchInfo.open();
        List<String> SearchInfo = databaseSearchInfo.setInfo(profString);
        databaseSearchInfo.close();
        String str_mayor = SearchInfo.get(10);

        String votingPref = ("MAYOR : "+str_mayor);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Information_Voter.this);
        alertDialogBuilder.setTitle("Voting Preference");
        alertDialogBuilder.setMessage(votingPref);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.show();

    }

    public void CollecDataButton(final String barangay_form,final String precinct_form,final String voters_form ){
        Button backbutton = (Button) findViewById(R.id.collect_id);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Form form = new Form(barangay_form,precinct_form,voters_form);
                Intent intent = new Intent(Information_Voter.this, QuestionForm.class);
                intent.putExtra("Form",form);
                startActivity(intent);
//                showLoginDialog(precinct_form,form);
            }
        });

    }

//    private void showLoginDialog(final String pct, final Form formDialog) {
//        alertInvalid = new AlertDialog.Builder(Information_Voter.this);
//        alertInvalid.setMessage("Invalid Username & Password");
//        alertInvalid.setPositiveButton("OK",null);
//        alertError = new AlertDialog.Builder(Information_Voter.this);
//        alertError.setTitle("( Precinct : "+pct+" )");
//        alertError.setMessage("Invalid Login Credential.");
//        alertError.setPositiveButton("OK",null);
//
//        LayoutInflater li = LayoutInflater.from(this);
//        View prompt = li.inflate(R.layout.login_dialog, null);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setView(prompt);
//        final EditText user = (EditText) prompt.findViewById(R.id.login_name);
//        final EditText pass = (EditText) prompt.findViewById(R.id.login_password);
//        user.setText(pct);
//        alertDialogBuilder.setTitle("PRECINCT LOGIN");
//        alertDialogBuilder.setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        String password = pass.getText().toString();
//                        String username = user.getText().toString();
//                        try {
//
//                            DatabaseAccess databaseloginInfo = DatabaseAccess.getInstance(Information_Voter.this);
//                            databaseloginInfo.open();
//                            List<String> LoginInfo = databaseloginInfo.loginInfo(username,"users_table");
//                            databaseloginInfo.close();
//
//                            String str_username = LoginInfo.get(1);
//                            String str_password = LoginInfo.get(2);
//
//                            if (username.equals(pct) ){
//                                if (username.equals(str_username) && password.equals(str_password)){
//                                    Intent intent = new Intent(Information_Voter.this, QuestionForm.class);
//                                    intent.putExtra("Form",formDialog);
//                                    startActivity(intent);
//                                }
//                                else alertInvalid.show();
//                            }
//                            else alertError.show();
//
//                        } catch (Exception e) {
//                            alertInvalid.show();
//                        }
//                    }
//                });
//
//        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//
//            }
//        });
//
//        alertDialogBuilder.show();
//
//    }

    public void BackButton(){
        Button backbutton = (Button) findViewById(R.id.bckbtn);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
