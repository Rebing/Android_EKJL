package com.example.android_ekjl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Environment;

public class GetDownload extends AsyncTask<String, Void, String>{
	
	@Override
	protected String doInBackground(String... url) {
		//Gets the filepath of external storage
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EKJL_PDFs/";
		File file = new File(path, url[1]);
		if(!file.exists()) {
			return downloadURL(url[0], file);
		} else {
			return "File already exists";
		}
    }
	
	//Downloads the PDF from site
	private String downloadURL(String download, File file) {
		try {
			URL url = new URL(download);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			
	        urlConnection.setRequestMethod("GET");
	        urlConnection.setDoOutput(true);
	        urlConnection.connect();
	        
	        FileOutputStream fOut = new FileOutputStream(file);
	        InputStream inputStream = urlConnection.getInputStream();

	        byte[] buffer = new byte[1024];
	        int bufferLength = 0; 

	        while((bufferLength = inputStream.read(buffer)) > 0 ) {
	                fOut.write(buffer, 0, bufferLength);
	        }
	        
	        fOut.close();
	        return "Download complete";
		} catch (MalformedURLException e) {
	        e.printStackTrace();
		} catch (IOException e) {
		        e.printStackTrace();
		} return "Download error";
	}

}
