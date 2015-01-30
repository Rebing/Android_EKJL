package com.example.android_ekjl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;


public class URL_Reader extends AsyncTask<String, Void, String[]>{
	
	@Override
	protected String[] doInBackground(String... url) {
        try {
            return downloadURL(url[0]);
        } catch (IOException e) {
        	e.printStackTrace();
        	return null;
        }
    }
	
	private String[] downloadURL(String myurl) throws IOException {
	    
	    try {
	        URL url = new URL(myurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        // Starts the query
	        conn.connect();
	        
	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	        ArrayList<String> URL_List = new ArrayList<String>();
	        String inputLine;
	        
	        // Convert the InputStream into a string
	        while ((inputLine = in.readLine()) != null) {
	        	URL_List.add(inputLine);
	        }
	        
	        String[] URL_Data = new String[URL_List.size()];
	        URL_Data = URL_List.toArray(URL_Data);
	        in.close();
	        return URL_Data;
	        
	    } finally {
	    }
	}

}
