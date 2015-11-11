package com.timothydijamco.vexteamanalyzer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.app.Fragment;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends Activity {
    public final static String TEAM_NUMBER = "com.timothydijamco.vexteamanalyzer.TEAM_NUMBER";
    public final static String SEASON = "com.timothydijamco.vexteamanalyzer.SEASON";
    ActionBar.Tab tab1, tab2;
    Fragment fragmentTab1 = new FragmentSearchTab();
    Fragment fragmentTab2 = new FragmentSearchTab();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        ActionBar ab = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#CF9611"));
        // ab.setBackgroundDrawable(colorDrawable);
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        tab1 = ab.newTab().setText("Search");
        tab2 = ab.newTab().setText("Saved");

        tab1.setTabListener(new MyTabListener(fragmentTab1));
        tab2.setTabListener(new MyTabListener(fragmentTab2));

        ab.addTab(tab1);
        ab.addTab(tab2);

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
