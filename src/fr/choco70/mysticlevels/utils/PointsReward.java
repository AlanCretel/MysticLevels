package fr.choco70.mysticlevels.utils;

public class PointsReward extends Reward{

    private double minAmount, maxAmount;

    public PointsReward(double chance, int id, double minAmount, double maxAmount) {
        super(chance, id);
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }
}
