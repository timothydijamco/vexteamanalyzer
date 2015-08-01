package com.timothydijamco.vexteamanalyzer;

/**
 * Created by JaneRN on 5/18/2015.
 */
public class Award {
    private String name;
    private int count;

    public Award(String name) {
        this.name = name;
        count = 1;
    }

    public String getName() { return name; }
    public int getCount() { return count; }
    public void increment() { count++; }
}
