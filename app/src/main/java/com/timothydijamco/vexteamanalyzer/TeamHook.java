package com.timothydijamco.vexteamanalyzer;

/**
 * Created by Timothy Dijamco on 8/3/2015.
 */
public class TeamHook {
    private String teamNumber;
    private int lastUpdated;

    public TeamHook(String teamNumber, int lastUpdated) {
        this.teamNumber = teamNumber;
        this.lastUpdated = lastUpdated;
    }

    public String getTeamNumber() {
        return teamNumber;
    }
    public int getLastUpdated() {
        return lastUpdated;
    }
}
