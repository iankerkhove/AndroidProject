package com.example.ian.werkstuk;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ian on 20/12/2017.
 */

/*public class ApiCall {

    JSONObject object = null;

    JSONObject makeCall(final String request) {
        new AsyncTask<Void, Void, String>() {
            Exception exception;

            protected String doInBackground(Void... urls) {
                try {
                    URL url = new URL(request);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    //Get of Post
                    urlConnection.setRequestMethod("GET");
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        return stringBuilder.toString();
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    return null;
                }
            }

        }.execute();
    return object;
    }

    void onPostExecute(String response) {
        String titel = "";

        if (response == null) {
            response = "THERE WAS AN ERROR";
        } else {

            JSONArray result = new JSONArray();
            try {
                object = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    return object;

}
}*/

