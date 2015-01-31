//Developed by Mikk Mihkel Nurges

package com.example.android_ekjl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	private int cal_size = 20; //How many competitions to display in final list to user (max 20)
	private String[] downloads = new String[cal_size];
	private String[] names = new String[cal_size];
	private String[] dates = new String[cal_size];
	
	String subfolder = "/EKJL_PDFs/";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		long currentDate = new Date().getTime();
        
		//Creates directory for PDFs and deletes obsolete PDFs
		Launcher(subfolder, currentDate);
		
        Map<Integer, String[]> comps = new HashMap<Integer, String[]>();
        
		try {
			String stringURL = "http://ekjl.ee/kalender";
			//Checks connection
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		    
		    if (networkInfo != null && networkInfo.isConnected()) {
		        //Gets HTML from website
		        URL_Reader reader = new URL_Reader();
		        reader.execute(stringURL);
		        String[] URLData = reader.get();

		        //Fills the arrays
			    comps = Competitions(URLData);
			    for(int x = 0; x < cal_size; x++) {
			    	names[x] = comps.get(x)[0];
			    	dates[x] = comps.get(x)[1];
			    	downloads[x] = comps.get(x)[2];
			    }
		    } else {
		        Toast.makeText(this, "No network connection", Toast.LENGTH_LONG).show();
		    } 
		    
			MyArrayAdapter adapter = new MyArrayAdapter(this, names, dates, downloads);
			setListAdapter(adapter);
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//Scans through the HTML data and finds the required lines for name, date and download
	private static Map<Integer, String[]> Competitions(String[] url_data) throws MalformedURLException, IOException {
        
        Map<Integer, String[]> comps = new HashMap<Integer, String[]>();
        int counter = 0;
        boolean next = false;
        String s = "caption06";
        String dl1 = "class=\"icon-schedule\"";
        String dl2 = "class=\"icon-manual\"";
        String name = null;
        String download = null; //Is null if the competition has no PDF for download
        
        //Read website's HTML
        for(int x = 0; x < url_data.length; x++) {
        	//Finds the name and date
        	String inputLine = url_data[x];
        	//Name
        	if(inputLine.contains(s)) {
        		name = splitter(inputLine, ">");
        		next = true;
        	}
        	//Date
        	else if(next) {
        		String date = splitter(inputLine, ">");
        		String[] current = {name, date, download};
        		comps.put(counter, current);
        		
        		counter++;
        		download = null;
        		next = false;
        	}
        	//PDF
        	else if(inputLine.contains(dl1) || inputLine.contains(dl2)) {
        		int start = inputLine.indexOf("href") + 6;
        		int finish = inputLine.indexOf("title", start) - 2;
        		download = ("http://ekjl.ee/" + inputLine.substring(start, finish)).replace(" ", "%20");
        		String fileName = splitter(download, "/");
        		
        		//Downloads the PDF, if it doesn't exist yet
        		GetDownload getDL = new GetDownload();
        		getDL.execute(download, fileName);
        	}
        }
        return comps;
    }
	
	//Function for splitting an URL line and getting only name, date or download information
	public static String splitter(String str, String sign) {
		if(sign.equals(">")) {
			String[] first = str.split(">");
			String[] second = first[1].split("<");
			return second[0];
		} else if(sign.equals("/")) {
			String[] split = str.split(sign);
			return split[split.length - 1];
		} else {
			return "Incorrect sign";
		}
	}
	
	//Activated when the download image is clicked
	public void OnClick(View view) {
		//Assigns path and file name for PDF to be opened
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + subfolder;
		String fileName = splitter(downloads[view.getId()], "/");
		File file = new File(path, fileName);
		
		//Creates and Intent for opening the PDF
		Intent target = new Intent(Intent.ACTION_VIEW);
		target.setDataAndType(Uri.fromFile(file),"application/pdf");
		target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		
		Intent intent = Intent.createChooser(target, "Open File");
		//Opens PDF if possible
		try {
		    startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();
		}   
	}
	
	private void Launcher(String folder, long currentTime) {
		SharedPreferences pref = getSharedPreferences("Directory", 0);
		File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + folder);
		
		//Creates directory on first launch
		if(!pref.getBoolean("dir_made", false)) {
			Editor prefEditor = pref.edit();
			prefEditor.putBoolean("dir_made", true);
			prefEditor.commit();
			dir.mkdir();
		}
		
		//Deletes files that are no longer needed
		File[] files = dir.listFiles();
		for(int x = 0; x < files.length; x++) {
			long fileCreated = files[x].lastModified();
			long days = TimeUnit.MILLISECONDS.convert(21, TimeUnit.DAYS); //21 days to milliseconds
			if((currentTime - fileCreated) > days) {
				files[x].delete();
			}
		}
	}
}
