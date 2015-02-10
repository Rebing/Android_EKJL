package com.example.android_ekjl;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URL_Reader extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... url) {
        try {
            downloadURL(url[0], url[1], url[2]);
            return "Done";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }


    //Downloads the HTML from URL
    private void downloadURL(String myurl, String DataName, String compQuantity) throws IOException {

        try {
            //Writes HTML data to file
            FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DataName);
            BufferedWriter out = new BufferedWriter(fw);

            int quantity = Integer.parseInt(compQuantity);
            while(quantity > 0) {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                String inputLine;
                //Writes the InputStream into a file
                while ((inputLine = in.readLine()) != null) {
                    out.write(inputLine + "\n");
                }

                quantity -= 20;
            }

            out.close();

        } finally {
        }
    }

}