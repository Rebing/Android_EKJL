package com.example.android_ekjl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Settings extends Activity {
    static private int max_size = 60; //Maximum number of events allowed
    static private int increment = 5;
    SharedPreferences calendarPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        //Gets the quantity of events to display
        calendarPrefs = getSharedPreferences("Calendar", 0);
        int cal_size = calendarPrefs.getInt("cal_size", 20);

        setNumber(cal_size);
    }

    //Sets the number of current competitions to display as text
    private void setNumber(int size) {
        TextView compText = (TextView)findViewById(R.id.nrOfComps);
        compText.setText("Competitions to display: " + size);

        calendarPrefs = getSharedPreferences("Calendar", 0);
        Editor prefEditor = calendarPrefs.edit();
        prefEditor.putInt("cal_size", size);
        prefEditor.commit();
    }

    //When the "settings" textview is clicked
    public void NumberOnClick(View view) {
        //Creates the spinner popup
        AlertDialog.Builder spinner = new AlertDialog.Builder(this);
        spinner.setTitle(R.string.chooseNr);

        //Creates list of numbers from 1 to max_size
        String[] nrList = new String[max_size/increment];
        for(int x = increment; x <= max_size; x += increment) {
            nrList[(x/increment)-1] = Integer.toString(x);
        }

        //Adds the numbers to the spinner and makes it clickable
        spinner.setItems(nrList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                setNumber((i+1)*increment);
            }
        });

        spinner.show();
    }
}
