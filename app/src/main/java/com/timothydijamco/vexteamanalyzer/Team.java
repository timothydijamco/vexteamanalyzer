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
    private double winPercent;
    private double avgRanking;
    private int maxMatchScore;
    private double avgMatchScore;
    private int maxRobotSkills;
    private int maxProgrammingSkills;
    private List<Award> awards;
    private int awardCount;

    public Team(String teamNumber) {
        this.teamNumber = teamNumber;
        events = new ArrayList<Event>();
        totalRankings = 0;
        avgRanking = -1;
        totalScore = 0;
        matches = 0;
        competitionsAttended = 0;
        wins = 0;
        ties = 0;
        losses = 0;
        winPercent = -1;
        maxMatchScore = 0;
        avgMatchScore = -1;
        maxRobotSkills = 0;
        maxProgrammingSkills = 0;
        awards = new ArrayList<Award>();
    }
    public Team(String teamNumber, int competitionsAttended, double avgRanking, double winPercent, int maxMatchScore, double avgMatchScore, int maxRobotSkills, int maxProgrammingSkills) {
        this.teamNumber = teamNumber;
        this.competitionsAttended = competitionsAttended;
        this.avgRanking = avgRanking;
        this.winPercent = winPercent;
        this.maxMatchScore = maxMatchScore;
        this.avgMatchScore = avgMatchScore;
        this.maxRobotSkills = maxRobotSkills;
        this.maxProgrammingSkills = maxProgrammingSkills;
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
    public double calculateAvgMatchScore() {
        if (Math.abs(avgMatchScore - -1) < 0.01) {
            avgMatchScore = ((double) totalScore) / matches;
        }
        return avgMatchScore;
    }
    public double calculateWinPercent() {
        if (Math.abs(winPercent - -1) < 0.01) {
            if ((wins + ties + losses) != 0) {
                winPercent = (((double) wins) / (wins + ties + losses)) * 100;
            } else {
                winPercent = 0;
            }
        }
        return winPercent;
    }
    public double calculateAvgRanking() {
        if (Math.abs(avgRanking - -1) < 0.01) {
            avgRanking = ((double) totalRankings) / competitionsAttended;
        }
        return avgRanking;
    }
}
