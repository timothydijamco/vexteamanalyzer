package com.timothydijamco.vexteamanalyzer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Timothy Dijamco on 5/15/2015.
 */
public class Event {
    private String sku;
    private Date date; // 0000-00-00
    private int ranking;
    private int wins;
    private int ties;
    private int losses;
    private int matches;
    private int totalScore;

    public Event(String sku, String rawStartDate) {
        this.sku = sku;
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(rawStartDate.substring(0,4)));
        cal.set(Calendar.MONTH, Integer.parseInt(rawStartDate.substring(5,7))-1);
        cal.set(Calendar.DATE, Integer.parseInt(rawStartDate.substring(8,10)));
        date = cal.getTime();
    }

    // Getters
    public String getSku() { return sku; }
    public Date getDate() { return date; }
    public String getDate_inString() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");
        return df.format(date);
    }
    public long getDate_inSeconds() {
        return date.getTime()/1000;
    }
    public int getRanking() { return ranking; }
    public int getMatches() { return matches; }
    public int getTotalScore() { return totalScore; }

    // Setters
    public void setRanking(int ranking) { this.ranking = ranking; }
    public void setWins(int wins) { this.wins = wins; }
    public void setTies(int ties) { this.ties = ties; }
    public void setLosses(int losses) { this.losses = losses; }
    public void setMatches(int matches) { this.matches = matches; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    // Incrementors
    public void incrementTotalScoreBy(int score) { totalScore += score; }
    public void incrementMatches() { matches++; }
    public void incrementWins() { wins++; }
    public void incrementTies() { ties++; }
    public void incrementLosses() { losses++; }
    public void incrementWinsBy(int wins) { this.wins += wins; }
    public void incrementTiesBy(int ties) { this.ties += ties; }
    public void incrementLossesBy(int losses) { this.losses += losses; }

    // Calculators
    public double calculateAverageScore() { return ((double) totalScore)/matches; }
    public double calculateWinPercent() {
        if ((wins+ties+losses)!=0) {
            return (((double) wins)/(wins+ties+losses))*100;
        } else {
            return 0;
        }
    }
}
