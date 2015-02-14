package com.example.android_ekjl;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarAdapter extends ArrayAdapter<String>{
	private final Context context;
	private final String[] names;
	private final String[] dates;
	private final String[] downloads;
    private final String[] isImportant;

	public CalendarAdapter(Context context, String[] names, String[] dates, String[] downloads, String[] isImportant) {
		super(context, R.layout.rowlayout, names);
	    this.context = context;
	    this.names = names;
	    this.dates = dates;
	    this.downloads = downloads;
        this.isImportant = isImportant;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
		
		//Displays text
		TextView name_text = (TextView) rowView.findViewById(R.id.name);
		TextView date_text = (TextView) rowView.findViewById(R.id.date);
		name_text.setText(names[position]);
		date_text.setText(dates[position]);
		
		//Displays the download images
		if(downloads[position] != null) {
			ImageView image = (ImageView) rowView.findViewById(R.id.download);
			image.setImageResource(R.drawable.download);
			image.setId(position);
		};

        if(isImportant[position] == "1") {
            rowView.setBackgroundColor(Color.parseColor("#96c6eb"));
        }
		
		return rowView;
		
	}
}
