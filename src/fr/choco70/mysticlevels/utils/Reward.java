package fr.choco70.mysticlevels.utils;

public class Reward{

    private double chance;
    private int id;

    public Reward(double chance, int id){
        this.chance = chance;
        this.id = id;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
