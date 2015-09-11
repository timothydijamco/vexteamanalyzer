package com.timothydijamco.vexteamanalyzer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy Dijamco on 8/1/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "teamsManager";

    private static final String TABLE_TEAMS = "teams";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COMPETITIONS = "competitions";
    private static final String KEY_RANKING_AVG = "ranking_avg";
    private static final String KEY_WINPC = "winpc";
    private static final String KEY_MATCHSCORE_MAX = "matchscore_max";
    private static final String KEY_MATCHSCORE_AVG = "matchscore_avg";
    private static final String KEY_ROBOTSKILLS_MAX = "robotskills_max";
    private static final String KEY_PROGRAMMINGSKILLS_MAX = "programmingskills_max";
    private static final String KEY_UPDATED = "updated";

    public DatabaseHandler (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TEAMS_TABLE = "CREATE TABLE " + TABLE_TEAMS + "(" +
                                        KEY_ID + " INTEGER PRIMARY KEY," +
                                        KEY_NAME + " TEXT," +
                                        KEY_COMPETITIONS + " INTEGER," +
                                        KEY_RANKING_AVG + " REAL," +
                                        KEY_WINPC + " REAL," +
                                        KEY_MATCHSCORE_MAX + " INTEGER," +
                                        KEY_MATCHSCORE_AVG + " REAL," +
                                        KEY_ROBOTSKILLS_MAX + " INTEGER," +
                                        KEY_PROGRAMMINGSKILLS_MAX + " INTEGER," +
                                        KEY_UPDATED + " INTEGER" + ")";
        db.execSQL(CREATE_TEAMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
        onCreate(db);
    }

    public void addTeam(Team team) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, team.getTeamNumber());
        values.put(KEY_COMPETITIONS, team.getCompetitionsAttended());
        values.put(KEY_RANKING_AVG, team.calculateAvgRanking());
        values.put(KEY_MATCHSCORE_MAX, team.getMaxMatchScore());
        values.put(KEY_MATCHSCORE_AVG, team.calculateAvgMatchScore());
        values.put(KEY_ROBOTSKILLS_MAX, team.getMaxRobotSkills());
        values.put(KEY_PROGRAMMINGSKILLS_MAX, team.getMaxProgrammingSkills());

        int currentTime = (int)(System.currentTimeMillis()/1000);
        values.put(KEY_UPDATED, currentTime);

        db.insert(TABLE_TEAMS, null, values);
        db.close();
    }

    public Team getTeam(String teamNumber) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_TEAMS,
                new String[]{KEY_ID, KEY_NAME, KEY_COMPETITIONS, KEY_RANKING_AVG, KEY_WINPC, KEY_MATCHSCORE_MAX, KEY_MATCHSCORE_AVG, KEY_ROBOTSKILLS_MAX, KEY_PROGRAMMINGSKILLS_MAX, KEY_UPDATED},
                KEY_NAME + "=?",
                new String[]{String.valueOf(teamNumber)},
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Team team = new Team(teamNumber, cursor.getInt(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getInt(5), cursor.getDouble(6), cursor.getInt(7), cursor.getInt(8));

        cursor.close();

        return team;
    }

    public List<TeamHook> getListOfTeams() {
        List<TeamHook> teams = new ArrayList<TeamHook>();

        String query = "SELECT " + KEY_NAME + "," + KEY_UPDATED + " FROM " + TABLE_TEAMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                TeamHook team = new TeamHook(cursor.getString(0), cursor.getInt(1));
                teams.add(team);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return teams;
    }

    public int getTeamsCount() {
        String query = "SELECT * FROM " + TABLE_TEAMS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();

        return cursor.getCount();
    }

}
