package com.example.edgepoint.skadoosh_naga;

import android.os.AsyncTask;
import android.util.Log;

public class LongOperation extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... params) {
        try {

            String email = params[0];
            GMailSender sender = new GMailSender("skadoosh.developers@gmail.com", "skad00sh987");
            sender.sendMail("Confirmation message for closing of canvassing",
                    "You have sent a request for the closing of canvassing. Please confirm in this e-mail.",
                    "skadoosh.developers@gmail.com","skadoosh.developers@gmail.com,"+email)                   ;

        } catch (Exception e) {
            Log.e("error", e.getMessage(), e);
            return "Email Not Sent";
        }

        return "Email Sent";
    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("LongOperation",result+"");
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }
}
