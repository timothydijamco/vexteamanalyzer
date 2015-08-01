package com.timothydijamco.vexteamanalyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy Dijamco on 5/17/2015.
 */
public class Team {
    private String teamNumber;
    private List<Event> events;
    // Helpers
    private int totalRankings;
    private int totalScore;
    private int matches;
    // Stats
    private int competitionsAttended;
    private int wins;
    private int ties;
    private int losses;
    private int maxMatchScore;
    private int maxRobotSkills;
    private int maxProgrammingSkills;
    private List<Award> awards;
    private int awardCount;

    public Team(String teamNumber) {
        this.teamNumber = teamNumber;
        events = new ArrayList<Event>();
        totalRankings = 0;
        totalScore = 0;
        matches = 0;
        competitionsAttended = 0;
        wins = 0;
        ties = 0;
        losses = 0;
        maxMatchScore = 0;
        maxRobotSkills = 0;
        maxProgrammingSkills = 0;
        awards = new ArrayList<Award>();
    }

    // Getters
    public String getTeamNumber() { return teamNumber; }
    public List<Event> getEvents() { return events; }
    public int getCompetitionsAttended() { return competitionsAttended; }
    public int getMaxMatchScore() { return maxMatchScore; }
    public int getMaxRobotSkills() { return maxRobotSkills; }
    public int getMaxProgrammingSkills() { return maxProgrammingSkills; }
    public List<Award> getAwards() { return awards; }
    public int getAwardCount() { return awardCount; }

    // Setters
    public void setCompetitionsAttended(int competitionsAttended) { this.competitionsAttended = competitionsAttended; }

    // Adders
    public void addEvent(Event event) { events.add(event); }
    public void tryAddAward(String awardName) {
        for (Award a : awards) {
            if (awardName.equals(a.getName())) {
                a.increment();
                awardCount++;
                return;
            }
        }
        awards.add(new Award(awardName));
        awardCount++;
    }

    // Maximum value setters (attempters)
    public void tryNewMaxMatchScore(int score) {
        if (score > maxMatchScore) {
            maxMatchScore = score;
        }
    }
    public void tryNewMaxRobotSkills(int score) {
        if (score > maxRobotSkills) {
            maxRobotSkills = score;
        }
    }
    public void tryNewMaxProgrammingSkills(int score) {
        if (score > maxProgrammingSkills) {
            maxProgrammingSkills = score;
        }
    }

    // Incrementers
    public void incrementTotalRankingsBy(int ranking) { totalRankings += ranking; }
    public void incrementTotalScoreBy(int score) { totalScore += score; }
    public void incrementCompetitionsAttended() { competitionsAttended++; }
    public void incrementMatches() { matches++; }
    public void incrementWins() { wins++; }
    public void incrementTies() { ties++; }
    public void incrementLosses() { losses++; }
    public void incrementWinsBy(int wins) { this.wins += wins; }
    public void incrementTiesBy(int ties) { this.ties += ties; }
    public void incrementLossesBy(int losses) { this.losses += losses; }

    // Calculators
    public double calculateAvgMatchScore() { return ((double) totalScore)/matches;  }
    public double calculateWinPercent() {
        if ((wins+ties+losses)!=0) {
            return (((double) wins)/(wins+ties+losses))*100;
        } else {
            return 0;
        }
    }
    public double calculateAvgRanking() { return ((double) totalRankings)/competitionsAttended; }
}
