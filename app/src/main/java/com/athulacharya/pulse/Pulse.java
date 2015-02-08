package com.athulacharya.pulse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Map;

// TODO: add help button/dialog
// TODO: add rest of vitals: breathing, responsiveness, pupils, skin


public class Pulse extends Activity {

    public static final String DATA_FILE = "PulseDataFile";

    private Deque<Date> beats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse);

        beats = new ArrayDeque<>();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pulse, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_save:
                saveBeats();
                return true;

            case R.id.action_clear:
                clearBeats();
                return true;

            case R.id.action_view_list:
                viewBeats();
                return true;

            case R.id.action_help:
                //TODO
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void viewBeats() {
        Intent intent = new Intent(this, ViewVitals.class);
        startActivity(intent);
    }

    private void saveBeats() {
        TextView textView = (TextView) findViewById(R.id.textview_bpm);
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");

        // Don't store "--"
        if (textView.getText().equals(getString(R.string.blank_bpm))) return;

        SharedPreferences pref = getSharedPreferences(DATA_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(df.format(new Date()), textView.getText().toString());
        editor.apply();

        // After you save a heart rate, you probably don't want to keep tapping it
        clearBeats();
    }

    private void clearBeats() {
        TextView textView = (TextView) findViewById(R.id.textview_bpm);
        beats.clear();
        textView.setTextColor(Color.BLACK);
        textView.setText(R.string.blank_bpm);
    }

    public void beat(View view) {
        TextView textView = (TextView) findViewById(R.id.textview_bpm);
        beats.addLast(new Date());

        // Moving average of the last 20
        if(beats.size() > 20) {
            beats.removeFirst();
            textView.setTextColor(Color.parseColor("#009900"));
        } else {
            textView.setTextColor(Color.RED);
        }

        // Five beats is enough to make a rough calculation
        if(beats.size() > 5) {
            double difference = (double)(beats.peekLast().getTime() - beats.peekFirst().getTime());
            double bpms = ((double)beats.size()-1) / difference;
            double bps = bpms * 1000;
            double bpm = bps * 60;

            textView.setText(String.format("%.2f", bpm));
        }
    }
}
