package com.athulacharya.pulse;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;


public class ViewVitals extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_view_vitals);

        refreshView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_vitals, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_clear_file:
                SharedPreferences pref = getSharedPreferences(Pulse.DATA_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                refreshView();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshView() {
        TextView textView = (TextView) findViewById(R.id.textview_vitals);
        textView.setText("");

        SharedPreferences pref = getSharedPreferences(Pulse.DATA_FILE, Context.MODE_PRIVATE);
        Map<String, ?> map = pref.getAll();

        // Get the keys and sort them
        LinkedList<String> keys = new LinkedList<>();
        for (Map.Entry<String, ?> entry : map.entrySet()) keys.add(entry.getKey());
        Collections.sort(keys);

        // Retrieve them in order and display them
        for (String key : keys) textView.append(key + " — " + pref.getString(key, "ERROR") + " BPM\n");
    }
}
