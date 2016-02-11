package com.timothydijamco.vexteamanalyzer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    public final static String TEAM_NUMBER = "com.timothydijamco.vexteamanalyzer.TEAM_NUMBER";
    public final static String SEASON = "com.timothydijamco.vexteamanalyzer.SEASON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#CF9611"));
        ab.setBackgroundDrawable(colorDrawable);

        Button submit = (Button) findViewById(R.id.button);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void process(View view) {
        EditText teamNumber = (EditText) findViewById(R.id.teamNumberET);
        Spinner season = (Spinner) findViewById(R.id.seasonSpinner);
        String teamNumberValue = teamNumber.getText().toString();
        String seasonValue = season.getSelectedItem().toString();
        TextView error = (TextView) findViewById(R.id.errorTV);
        if ((teamNumberValue==null || teamNumberValue.trim().equals("")) && (seasonValue==null || seasonValue.trim().equals(""))) {
            error.setText("Please enter a team number and season");
        } else if (teamNumberValue==null || teamNumberValue.trim().equals("")) {
            error.setText("Please enter a team number");
        } else if (seasonValue==null || seasonValue.trim().equals("")) {
            error.setText("Please enter a season");
        } else {
            Intent intent = new Intent(this, ProcessActivity.class);
            intent.putExtra(TEAM_NUMBER, teamNumberValue);
            intent.putExtra(SEASON, seasonValue);
            startActivity(intent);
        }
    }
}