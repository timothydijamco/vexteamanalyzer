package com.timothydijamco.vexteamanalyzer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy Dijamco on 8/3/2015.
 */
public class TeamListAdapter extends BaseAdapter {
   private Context context;

    public TeamListAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        DatabaseHandler dbh = new DatabaseHandler(context);
        return dbh.getTeamsCount();
    }

    @Override
    public TeamHook getItem(int position) {
        DatabaseHandler dbh = new DatabaseHandler(context);
        List<TeamHook> teams = new ArrayList<TeamHook>();
        teams = dbh.getListOfTeams();

        return teams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
