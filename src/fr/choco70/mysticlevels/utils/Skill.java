package fr.choco70.mysticlevels.utils;

import java.util.ArrayList;

public class Skill {

    private ArrayList<Event> events = new ArrayList<>();
    private int maxLevel;
    private String name;
    private boolean active;
    private String pointsFormula;

    public Skill(String name, int maxLevel, boolean active){
        this.name = name;
        this.maxLevel = maxLevel;
        this.active = active;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addEvent(Event event){
        events.add(event);
    }

    public void removeEvent(Event event){
        events.remove(event);
    }

    public String getPointsFormula() {
        return pointsFormula;
    }

    public void setPointsFormula(String pointsFormula) {
        this.pointsFormula = pointsFormula;
    }
}
