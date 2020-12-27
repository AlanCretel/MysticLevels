package fr.choco70.mysticlevels.utils;

import java.util.ArrayList;

public class Event {

    private Skill skill;
    private EventType type;
    public ArrayList<Reward> rewards;
    private String name;

    public Event(Skill skill, EventType type, String name){
        this.skill = skill;
        this.type = type;
        this.rewards = new ArrayList<>();
        this.name = name;
    }

    public EventType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Skill getSkill() {
        return skill;
    }
}
