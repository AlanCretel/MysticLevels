package fr.choco70.mysticlevels.utils;

public class CommandReward extends Reward{

    private String command;

    public CommandReward(double chance, int id, String command) {
        super(chance, id);
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
