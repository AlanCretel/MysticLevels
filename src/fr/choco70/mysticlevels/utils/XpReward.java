package fr.choco70.mysticlevels.utils;

public class XpReward extends Reward{

    private int minAmount, maxAmount;

    public XpReward(double chance, int id, int minAmount, int maxAmount) {
        super(chance, id);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }
}
