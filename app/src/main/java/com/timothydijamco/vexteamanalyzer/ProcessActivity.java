package com.timothydijamco.vexteamanalyzer;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class ProcessActivity extends ActionBarActivity {
    private static final String TAG = "ProcessActivityTag";
    private int[] unixTimes = {1398902400,1401580800,1404172800,1406851200,1409529600,1412121600,1414800000,1417392000,1420070400,1422748800,1425168000,1427846400,1430438400};
    private String[] labels = {"5/1/14","6/1/14","7/1/14","8/1/14","9/1/14","10/1/14","11/1/14","12/1/14","1/1/15","2/1/15","3/1/15","4/1/15","5/1/15"};
    private List<AxisValue> axisValues = new ArrayList<AxisValue>();
    private List<Event> events = new ArrayList<Event>();
    private Team team;
    private Spinner chartSpinner;
    private String seasonReadable;
    private int chartSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        Intent intent = getIntent();
        String teamNumber = intent.getStringExtra(MainActivity.TEAM_NUMBER).toUpperCase();
        seasonReadable = intent.getStringExtra(MainActivity.SEASON);
        String season = seasonReadable.replace(" ","%20");
        team = new Team(teamNumber);

        chartSpinner = (Spinner)findViewById(R.id.chartSpinner);

        Log.i(TAG, "onCreate (1)");

        // Prepare spinner
        chartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //LineChartView chart1 = (LineChartView) findViewById(R.id.chart);
                View chart1Container = (View) findViewById(R.id.chart1Container);
                View chart2Container = (View) findViewById(R.id.chart2Container);
                if (i==0) {
                    chart1Container.setVisibility(View.VISIBLE);
                    chart2Container.setVisibility(View.INVISIBLE);
                } else {
                    chart1Container.setVisibility(View.INVISIBLE);
                    chart2Container.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });

        Log.i(TAG, "onCreate (2)");

        // Prepare chart size
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int displayWidth = displayMetrics.widthPixels;

        View chart1Container = (View) findViewById(R.id.chart1Container);
        RelativeLayout.LayoutParams chart1Params = (RelativeLayout.LayoutParams) chart1Container.getLayoutParams();
        chart1Params.height = (int)(displayWidth*.9);
        chart1Container.setLayoutParams(chart1Params);

        View chart2Container = (View) findViewById(R.id.chart2Container);
        RelativeLayout.LayoutParams chart2Params = (RelativeLayout.LayoutParams) chart2Container.getLayoutParams();
        chart2Params.height = (int)(displayWidth*.9);
        chart2Container.setLayoutParams(chart2Params);

        // Prepare axis values
        Log.i(TAG, "Preparing axis values...");
        for (int k = 0; k < unixTimes.length; k++) {
            axisValues.add(new AxisValue(unixTimes[k]).setLabel(labels[k]));
        }

        if (seasonReadable.equalsIgnoreCase("Nothing But Net")) {
            chartSpinner.setVisibility(View.GONE);
            chart1Container.setVisibility(View.GONE);
            chart2Container.setVisibility(View.GONE);
        }

        // Actionbar
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#CF9611"));
        ab.setBackgroundDrawable(colorDrawable);
        ab.setTitle(teamNumber);
        ab.setSubtitle(seasonReadable);

        // Pull and process data
        AsyncDownloader downloader = new AsyncDownloader();
        downloader.execute(teamNumber, season);

    }

    private class AsyncDownloader extends AsyncTask<String, String, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            URL requestURL;
            URLConnection con;
            //==== Get events ====/
            try {
                Log.i(TAG, "Getting events...");
                requestURL = new URL("http://api.vex.us.nallen.me/get_events?team="+params[0]+"&season="+params[1]);

                con = requestURL.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

                int cp;
                while((cp=rd.read()) != -1) {
                    //Log.i(TAG, String.valueOf(cp));
                    sb.append((char) cp);
                }
                publishProgress("events", sb.toString(), params[0]);
            } catch (Exception e) {
                publishProgress();
                Log.i(TAG, "Error in connection or reading");
                //throw new IllegalStateException();
            }
            //==== Get rankings ====/
            try {
                Log.i(TAG, "Getting rankings...");
                requestURL = new URL("http://api.vex.us.nallen.me/get_rankings?team="+params[0]+"&season="+params[1]);

                con = requestURL.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

                int cp;
                while((cp=rd.read()) != -1) {
                    //Log.i(TAG, String.valueOf(cp));
                    sb.append((char) cp);
                }
                publishProgress("rankings", sb.toString(), params[0]);
            } catch (Exception e) {
                publishProgress();
                Log.i(TAG, "Error in connection or reading");
                //throw new IllegalStateException();
            }
            //==== Get matches ====/
            try {
                Log.i(TAG, "Getting matches...");
                requestURL = new URL("http://api.vex.us.nallen.me/get_matches?team="+params[0]+"&season="+params[1]);

                con = requestURL.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

                int cp;
                while((cp=rd.read()) != -1) {
                    //Log.i(TAG, String.valueOf(cp));
                    sb.append((char) cp);
                }
                publishProgress("matches", sb.toString(), params[0]);
            } catch (Exception e) {
                publishProgress();
                Log.i(TAG, "Error in connection or reading");
                //throw new IllegalStateException();
            }
            //==== Get skills ====/
            try {
                Log.i(TAG, "Getting skills...");
                requestURL = new URL("http://api.vex.us.nallen.me/get_skills?team="+params[0]+"&season="+params[1]);

                con = requestURL.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

                int cp;
                while((cp=rd.read()) != -1) {
                    //Log.i(TAG, String.valueOf(cp));
                    sb.append((char) cp);
                }
                publishProgress("skills", sb.toString(), params[0]);
            } catch (Exception e) {
                publishProgress();
                Log.i(TAG, "Error in connection or reading");
                //throw new IllegalStateException();
            }
            //==== Get awards ====/
            try {
                Log.i(TAG, "Getting awards...");
                requestURL = new URL("http://api.vex.us.nallen.me/get_awards?team=\"+params[0]+\"&season="+params[1]);

                con = requestURL.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

                int cp;
                while((cp=rd.read()) != -1) {
                    //Log.i(TAG, String.valueOf(cp));
                    sb.append((char) cp);
                }
                publishProgress("awards", sb.toString(), params[0]);
            } catch (Exception e) {
                publishProgress();
                Log.i(TAG, "Error in connection or reading");
                //throw new IllegalStateException();
            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values.length == 0) {
                Log.i(TAG, "No data to process.");
            } else {
                Log.i(TAG, "Begin processing data...");
                try {
                    JSONObject jsonObj = new JSONObject(values[1]);
                    JSONArray resultArray = jsonObj.getJSONArray("result");
                    TextView textValue;
                    switch (values[0]) {
                        case "events":
                            Log.i(TAG, "Processing events...");
                            for (int k = 0; k < resultArray.length(); k++) {
                            JSONObject resultObj = resultArray.getJSONObject(k);
                            if (!resultObj.getString("start").substring(0,10).equals("0000-00-00")) {
                                events.add(new Event(resultObj.getString("sku"), resultObj.getString("start")));
                            }
                        }
                            break;
                        case "rankings":
                            Log.i(TAG, "Processing rankings...");
                            team.setCompetitionsAttended(jsonObj.getInt("size"));
                            textValue = (TextView) findViewById(R.id.competitionsAttendedValue);
                            textValue.setText(String.valueOf(team.getCompetitionsAttended()));

                            for (int k = 0; k < resultArray.length(); k++) {
                                JSONObject resultObj = resultArray.getJSONObject(k);
                                team.tryNewMaxMatchScore(resultObj.getInt("max_score"));
                                team.incrementWinsBy(resultObj.getInt("wins"));
                                team.incrementTiesBy(resultObj.getInt("ties"));
                                team.incrementLossesBy(resultObj.getInt("losses"));
                                team.incrementTotalRankingsBy(resultObj.getInt("rank"));

                                for (Event e : events) {
                                    if (e.getSku().equals(resultObj.getString("sku"))) {
                                        e.incrementWinsBy(resultObj.getInt("wins"));
                                        e.incrementTiesBy(resultObj.getInt("ties"));
                                        e.incrementLossesBy(resultObj.getInt("losses"));
                                    }
                                }
                            }
                            textValue = (TextView) findViewById(R.id.averageRankingValue);
                            textValue.setText(String.format("%.2f", team.calculateAvgRanking()));

                            textValue = (TextView) findViewById(R.id.winPercentValue);
                            textValue.setText(String.format("%.2f", team.calculateWinPercent()) + "%");

                            textValue = (TextView) findViewById(R.id.highestMatchScoreValue);
                            textValue.setText(String.valueOf(team.getMaxMatchScore()));
                            break;
                        case "matches":
                            Log.i(TAG, "Processing matches...");
                            String side;
                            for (int k = 0; k < resultArray.length(); k++) {
                                JSONObject resultObj = resultArray.getJSONObject(k);
                                if (resultObj.getString("red1").equals(values[2]) || resultObj.getString("red2").equals(values[2]) || resultObj.getString("red3").equals(values[2])) {
                                    if (resultObj.getInt("redscore") != 0) {
                                        side = "red";
                                        team.incrementMatches();
                                    } else {
                                        side = "null";
                                    }
                                } else {
                                    if (resultObj.getInt("bluescore") != 0) {
                                        side = "blue";
                                        team.incrementMatches();
                                    } else {
                                        side = "null";
                                    }
                                }
                                if (!side.equals("null")) {
                                    team.incrementTotalScoreBy(resultObj.getInt(side+"score"));
                                }

                                for (Event e : events) {
                                    if (!side.equals("null") && e.getSku().equals(resultObj.getString("sku"))) {
                                        e.incrementTotalScoreBy(resultObj.getInt(side+"score"));
                                        e.incrementMatches();
                                        break;
                                    }
                                }
                            }
                            textValue = (TextView) findViewById(R.id.averageMatchScoreValue);
                            textValue.setText(String.format("%.1f", team.calculateAvgMatchScore()));

                            // ==== Generate graph ==== //
                            Log.i(TAG, "Generating the graph...");

                            LineChartView chart1 = (LineChartView) findViewById(R.id.chart);
                            chart1.setLineChartData(getAvgMatchScoreChartData());
                            chart1.setInteractive(false);

                            LineChartView chart2 = (LineChartView) findViewById(R.id.chart2);
                            chart2.setLineChartData(getWinPercentChartData());
                            chart2.setInteractive(false);

                            Viewport v = new Viewport(chart1.getCurrentViewport());
                            Viewport v2 = new Viewport(chart1.getCurrentViewport());
                            v.bottom = 0;
                            v2.bottom = 0;
                            v2.top = 100;
                            if (seasonReadable.equalsIgnoreCase("Skyrise")) {
                                v.top = 100;
                            } else if (seasonReadable.equalsIgnoreCase("Nothing But Net")) {
                                v.top = 300;
                            }

                            chart1.setCurrentViewport(v);
                            chart1.setMaximumViewport(v);
                            chart2.setCurrentViewport(v2);
                            chart2.setMaximumViewport(v2);
                            break;
                        case "skills":
                            Log.i(TAG, "Processing skills...");
                            for (int k = 0; k < resultArray.length(); k++) {
                                JSONObject resultObj = resultArray.getJSONObject(k);
                                if (resultObj.getInt("type") == 0) {
                                    team.tryNewMaxRobotSkills(resultObj.getInt("score"));
                                }
                                if (resultObj.getInt("type") == 1) {
                                    team.tryNewMaxProgrammingSkills(resultObj.getInt("score"));
                                }
                            }
                            textValue = (TextView) findViewById(R.id.highestRobotSkillSValue);
                            textValue.setText(String.valueOf(team.getMaxRobotSkills()));

                            textValue = (TextView) findViewById(R.id.highestProgrammingSkillSValue);
                            textValue.setText(String.valueOf(team.getMaxProgrammingSkills()));
                            break;
                        case "awards":
                            Log.i(TAG, "Processing awards...");
                            for (int k = 0; k < resultArray.length(); k++) {
                                JSONObject resultObj = resultArray.getJSONObject(k);
                                team.tryAddAward(resultObj.getString("name"));
                            }

                            String awardsString = "";
                            for (Award award : team.getAwards()) {
                                awardsString += award.getCount() + "x " + award.getName() + "\n";
                            }
                            textValue = (TextView) findViewById(R.id.awardsValue);
                            textValue.setText(awardsString);

                            textValue = (TextView) findViewById(R.id.awardsCount);
                            textValue.setText("(" + String.valueOf(team.getAwardCount()) + ")");


                            // Temporary hack to fix problem with chart settings not updating:
                            LineChartView c1 = (LineChartView) findViewById(R.id.chart);
                            LineChartView c2 = (LineChartView) findViewById(R.id.chart2);

                            Viewport vp = new Viewport(c1.getCurrentViewport());
                            Viewport vp2 = new Viewport(c1.getCurrentViewport());
                            vp.bottom = 0;
                            vp2.bottom = 0;
                            vp2.top = 100;
                            if (seasonReadable.equalsIgnoreCase("Skyrise")) {
                                vp.top = 100;
                            } else if (seasonReadable.equalsIgnoreCase("Nothing But Net")) {
                                vp.top = 300;
                            }

                            c1.setCurrentViewport(vp);
                            c1.setMaximumViewport(vp);
                            c2.setCurrentViewport(vp2);
                            c2.setMaximumViewport(vp2);
                            break;
                    }


                } catch (Exception e) {
                    Log.i(TAG, "Error in JSON object");
                }
            }
            super.onProgressUpdate(values);
        }

    }

    private LineChartData getAvgMatchScoreChartData() {
        List<PointValue> avgMatchScorePoints = new ArrayList<PointValue>();
        for (Event event : events) {
            if (event.calculateAverageScore() > 0.01) {
                avgMatchScorePoints.add(new PointValue(event.getDate_inSeconds(), (float) event.calculateAverageScore()));
                //points.get(points.size()-1).setLabel(event.getDate_inString());
            }
        }

        Line avgMatchScoreLine = new Line(avgMatchScorePoints).setColor(0xFF4B86C9);//.setHasLabelsOnlyForSelected(true);
        List<Line> chart1Lines = new ArrayList<Line>();
        chart1Lines.add(avgMatchScoreLine);

        LineChartData chart1Data = new LineChartData();
        chart1Data.setLines(chart1Lines);

        Axis axisX = (new Axis(axisValues)).setHasLines(true).setTextColor(Color.DKGRAY); //new Axis(axisValues);
        Axis chart1AxisY = Axis.generateAxisFromRange(0,300,10).setHasLines(true).setTextColor(Color.DKGRAY);

        axisX.setName("Date");
        chart1AxisY.setName("Average match score");

        chart1Data.setAxisXBottom(axisX);
        chart1Data.setAxisYLeft(chart1AxisY);

        return chart1Data;
    }

    private LineChartData getWinPercentChartData() {
        List<PointValue> winPercentPoints = new ArrayList<PointValue>();
        for (Event event : events) {
            if (event.calculateWinPercent() > 0.01) {
                winPercentPoints.add(new PointValue(event.getDate_inSeconds(), (float) event.calculateWinPercent()));
            }
        }
        Line winPercentLine = new Line(winPercentPoints).setColor(0xFF549E6E);
        List<Line> chart2Lines = new ArrayList<Line>();
        chart2Lines.add(winPercentLine);

        LineChartData chart2Data = new LineChartData();
        chart2Data.setLines(chart2Lines);

        Axis axisX = (new Axis(axisValues)).setHasLines(true).setTextColor(Color.DKGRAY); //new Axis(axisValues);
        Axis chart2AxisY = Axis.generateAxisFromRange(0,100,10).setHasLines(true).setTextColor(Color.DKGRAY);

        axisX.setName("Date");
        chart2AxisY.setName("Percent of Matches Won");

        chart2Data.setAxisXBottom(axisX);
        chart2Data.setAxisYLeft(chart2AxisY);

        return chart2Data;
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

    public void saveTeam(View view) {
        DatabaseHandler dbh = new DatabaseHandler(this);
        dbh.addTeam(team);
    }
    public void getTeam(View view) {
        DatabaseHandler dbh = new DatabaseHandler(this);
        Team testTeam = dbh.getTeam("1224S");
        Log.i("TAG", testTeam.getTeamNumber() + " " + String.valueOf(testTeam.getMaxMatchScore()));
        Toast.makeText(this, String.valueOf(testTeam.getMaxMatchScore()), Toast.LENGTH_LONG).show();
    }
}
