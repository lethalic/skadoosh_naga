package com.example.edgepoint.skadoosh_naga;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionForm extends AppCompatActivity {

    ListView simpleList;
    String[] questions,ListArr,choiceCongr,choiceMayor,choiceViceMayor,choiceKagawad;
    List<String> listahan = new ArrayList<>();
    String votersname_form, congressman="", mayor="", vicemayor="";
    Button submit;
    AlertDialog.Builder alert;
    RadioGroup rgroup_congressman,rgroup_mayor,rgroup_vicemayor;
    AppCompatCheckBox[] rcheck_kagawad;
    ProgressDialog progressDialog;
    EditText phonenumber,encoder;
    int lenghtCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_form);

        Intent intent = getIntent();

        Form form = intent.getParcelableExtra("Form");

        votersname_form = form.getName();

        TextView vname_tv = (TextView) findViewById(R.id.vname_tv_id);
        vname_tv.setText("("+votersname_form+")");

        DatabaseAccess databaseAccessDeceased = DatabaseAccess.getInstance(QuestionForm.this);
        databaseAccessDeceased.open();
        String deceased = databaseAccessDeceased.getDeceasedStr(votersname_form);
        databaseAccessDeceased.close();

        if (deceased.equals("yes")){
            Toast.makeText(getApplicationContext(), votersname_form+" already deceased", Toast.LENGTH_LONG).show();
            restoreDeceased();
        }
        else if (deceased.equals("no")){
            runQuestionForm();
        }
        else {
            showDeceasedDialog();
        }

    }

    public void restoreDeceased(){
        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(QuestionForm.this);
        confirmationDialog.setTitle("Restore "+votersname_form+"?");

        confirmationDialog.setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        progressDialog = new ProgressDialog(QuestionForm.this);
                        progressDialog.setMessage("Restoring Data...");
                        progressDialog.show();
                        savingtoDatabase(votersname_form,"","","","off","");
                    }
                });

        confirmationDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
                dialog.cancel();
            }
        });

        confirmationDialog.show();
    }

    public void showDeceasedDialog(){

        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(QuestionForm.this);
        confirmationDialog.setTitle("Is "+votersname_form+" deceased?");

        confirmationDialog.setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        progressDialog = new ProgressDialog(QuestionForm.this);
                        progressDialog.setMessage("Saving Data...");
                        progressDialog.show();
                        savingtoDatabase(votersname_form,"","","","on","yes");
                    }
                });

        confirmationDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                runQuestionForm();
                dialog.cancel();
            }
        });

        confirmationDialog.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
                dialog.cancel();
            }
        });

        confirmationDialog.show();
    }

    public void runQuestionForm(){
        progressDialog = new ProgressDialog(QuestionForm.this);
        progressDialog.setMessage("Loading Questions...");
        progressDialog.show();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                QuestionForm(votersname_form);
                submitForm();
                form4PrevButton();
            }
        },1000);
    }

    public void QuestionForm(String votersName){
        DatabaseAccess databaseAccessInfo = DatabaseAccess.getInstance(this);
        databaseAccessInfo.open();
            List<String> StringList = databaseAccessInfo.getListinfo(votersName);
        databaseAccessInfo.close();

        questions = getResources().getStringArray(R.array.questions);
        choiceMayor = getResources().getStringArray(R.array.choice_mayor);

        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.linear1);
        for (int k = 0; k < questions.length; k++) {
            //create text button
            TextView title = new TextView(this);
            title.setText(questions[k]);
            title.setTextSize(18);
            title.setTypeface(null, Typeface.BOLD);
            title.setTextColor(Color.parseColor("#FF0099CC"));
            mLinearLayout.addView(title);
            // create radio button


            if (k == 0){
                final AppCompatRadioButton[] rbutton_mayor = new AppCompatRadioButton[choiceMayor.length];
                rgroup_mayor = new RadioGroup(this);
                rgroup_mayor.setOrientation(RadioGroup.VERTICAL);
                for (int i = 0; i < choiceMayor.length; i++) {
                    rbutton_mayor[i] = new AppCompatRadioButton(this);
                    rbutton_mayor[i].setTextSize(18);
                    rgroup_mayor.addView(rbutton_mayor[i]);
                    rbutton_mayor[i].setText(choiceMayor[i]);
                    String[] splitshoice = choiceMayor[i].split(",");
                    if (StringList.get(2).equals(splitshoice[0])){
                        rbutton_mayor[i].setChecked(true);
                    }
                }
                mLinearLayout.addView(rgroup_mayor);
            }
            else if (k == 1){
                phonenumber = new EditText(this);
                phonenumber.setText(StringList.get(0));
                phonenumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                phonenumber.setHintTextColor(getResources().getColor(R.color.colorHint));
                phonenumber.setHint("Enter Phone Number");
                mLinearLayout.addView(phonenumber);
            }
            else if (k == 2){
                encoder = new EditText(this);
                encoder.setText(StringList.get(1));
                encoder.setHintTextColor(getResources().getColor(R.color.colorHint));
                encoder.setHint("Encoder Name");
                mLinearLayout.addView(encoder);
            }

        }
        progressDialog.dismiss();
    }

    public void submitForm(){
        submit = (Button) findViewById(R.id.submit_id);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton mayorradio = ((RadioButton)findViewById(rgroup_mayor.getCheckedRadioButtonId()));

                String contentPhone = phonenumber.getText().toString();
                String contentEncoder = encoder.getText().toString();

                String indicator_on_off = "on";
                String deceased_yes_no = "no";

                if (mayorradio == null || contentPhone == null || contentEncoder == null)
                {
                    Toast.makeText(getApplicationContext(), "Fill-Up Blank!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    mayor = mayorradio.getText().toString();

                    savingtoDatabase(votersname_form,mayor,contentPhone,contentEncoder,indicator_on_off,deceased_yes_no);

                }

            }

        });
    }

    public void savingtoDatabase(String voters,String mayor,String phone,String encoder,String indicator_on_off,String deceased_yes_no){

        DatabaseAccess databaseAccessSubmit = DatabaseAccess.getInstance(QuestionForm.this);
        databaseAccessSubmit.open();

        boolean checkDuplicate = databaseAccessSubmit.checkDuplicateUpload(votersname_form);

        if(checkDuplicate == true){
            boolean submitUpdate = databaseAccessSubmit.updateData(voters,mayor,phone,encoder,indicator_on_off,deceased_yes_no);


            if(submitUpdate == true) {
                Toast.makeText(QuestionForm.this,"Data Saved.",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                finish();
            }
            else {
                Toast.makeText(QuestionForm.this, "Data not Submitted", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
        else {
            boolean submitInsert= databaseAccessSubmit.insertOnConflictData(voters,mayor,phone,encoder,indicator_on_off,deceased_yes_no);


            if(submitInsert == true) {
                Toast.makeText(QuestionForm.this,"Data Saved.",Toast.LENGTH_LONG).show();
                finish();
                progressDialog.dismiss();
            }
            else {
                Toast.makeText(QuestionForm.this, "Data not Submitted", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }

        databaseAccessSubmit.close();
    }

    public void form4PrevButton(){
        Button prev4button = (Button) findViewById(R.id.prev4btn_id);
        prev4button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
