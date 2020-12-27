package fr.choco70.mysticlevels.commands;

import com.sun.org.glassfish.gmbal.ParameterNames;
import fr.choco70.mysticlevels.MysticLevels;
import fr.choco70.mysticlevels.managers.MessagesManager;
import fr.choco70.mysticlevels.managers.SkillsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandSkills implements CommandExecutor{

    private final MysticLevels plugin = MysticLevels.getPlugin(MysticLevels.class);
    private final SkillsManager skillsManager = plugin.getSkillsManager();
    private final MessagesManager messagesManager = plugin.getMessagesManager();
    private final String pluginHeader = plugin.getConfig().getString("SETTINGS.plugin_header", "§6[MysticLevels]§7");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        if(arguments.length == 0){
            sendHelpInfos(sender);
        }
        else{
            if(arguments.length == 1){
                if(arguments[0].equalsIgnoreCase("list")){
                    if(skillsManager.getActiveSkills().size() == 0){
                        sender.sendMessage(formatMessage(messagesManager.getMessage("NO_SKILLS")));
                    }
                    else{
                        sender.sendMessage(formatMessage(messagesManager.getMessage("SKILLS_LIST_HEAD")));
                        ArrayList<String> skills = skillsManager.getActiveSkills();
                        for (String skill : skills) {
                            sender.sendMessage(formatMessage(messagesManager.getMessage("SKILLS_LIST_SKILL"), skill));
                        }
                        sender.sendMessage(formatMessage(messagesManager.getMessage("SKILLS_LIST_FOOT")));
                    }
                }
                else if(arguments[0].equalsIgnoreCase("info")){
                    sender.sendMessage(formatMessage(messagesManager.getMessage("INFO_NB_SKILLS")).replaceAll("%NB_OF_SKILLS%", "" + skillsManager.getActiveSkills().size()));
                    sender.sendMessage(formatMessage(messagesManager.getMessage("INFO_MONEY_GIVING"), "", 0, 0, 0, 0, true, skillsManager.doesGiveMoney(), 0, 0));
                    sender.sendMessage(formatMessage(messagesManager.getMessage("INFO_XP_GIVING"), "", 0, 0, 0, 0, skillsManager.doesGiveXP(), true, 0, 0));
                }
                else{
                    sendHelpInfos(sender);
                }
            }
            else if(arguments.length == 2){
                if(sender instanceof Player){
                    Player player = (Player)sender;
                    if(arguments[0].equalsIgnoreCase("points")){
                        if(skillsManager.isSkill(arguments[1]) && skillsManager.isActiveSkill(arguments[1])){
                            String skill = skillsManager.getSkillByName(arguments[1]);
                            Integer points = skillsManager.getSkillPoints(player, skill);
                            player.sendMessage(formatMessage(messagesManager.getMessage("POINTS_INFO"), skill, 0, points, skillsManager.getXpToLevelUp(player, skill), skillsManager.getMaxSkillLevel(skill), skillsManager.isGiveXP(skill), skillsManager.isGiveMoney(skill), skillsManager.getExperienceMultiplier(player, skill), skillsManager.getMoneyMultiplier(player, skill)));
                        }
                        else{
                            player.sendMessage(formatMessage(messagesManager.getMessage("INVALID_SKILL"), arguments[1]));
                        }
                    }
                    else if(arguments[0].equalsIgnoreCase("level")){
                        if(skillsManager.isSkill(arguments[1]) && skillsManager.isActiveSkill(arguments[1])){
                            String skill = skillsManager.getSkillByName(arguments[1]);
                            Integer level = skillsManager.getSkillLevel(player, skill);
                            player.sendMessage(formatMessage(messagesManager.getMessage("LEVEL_INFO"), skill, level, 0, skillsManager.getXpToLevelUp(player, skill), skillsManager.getMaxSkillLevel(skill), skillsManager.isGiveXP(skill), skillsManager.isGiveMoney(skill), skillsManager.getExperienceMultiplier(player, skill), skillsManager.getMoneyMultiplier(player, skill)));
                        }
                        else{
                            player.sendMessage(formatMessage(messagesManager.getMessage("INVALID_SKILL"), arguments[1]));
                        }
                    }
                    else if(arguments[0].equalsIgnoreCase("stats")){
                        if(skillsManager.isSkill(arguments[1]) && skillsManager.isActiveSkill(arguments[1])){
                            String skill = skillsManager.getSkillByName(arguments[1]);
                            Integer level = skillsManager.getSkillLevel(player, skill);
                            Integer points = skillsManager.getSkillPoints(player, skill);
                            player.sendMessage(formatMessage(messagesManager.getMessage("SKILLS_STATS_HEAD"), skill));
                            player.sendMessage(formatMessage(messagesManager.getMessage("SKILLS_STATS_BODY"), skill, level, points, skillsManager.getXpToLevelUp(player, skill), skillsManager.getMaxSkillLevel(skill), skillsManager.isGiveXP(skill), skillsManager.isGiveMoney(skill), skillsManager.getExperienceMultiplier(player, skill), skillsManager.getMoneyMultiplier(player, skill)));
                            player.sendMessage(formatMessage(messagesManager.getMessage("SKILLS_STATS_FOOT")));
                        }
                        else{
                            player.sendMessage(formatMessage(messagesManager.getMessage("INVALID_SKILL"), arguments[1]));
                        }
                    }
                    else if(arguments[0].equalsIgnoreCase("info")){
                        if(skillsManager.isSkill(arguments[1]) && skillsManager.isActiveSkill(arguments[1])){
                            String skill = skillsManager.getSkillByName(arguments[1]);
                            Integer level = skillsManager.getSkillLevel(player, skill);
                            Integer points = skillsManager.getSkillPoints(player, skill);
                            player.sendMessage(formatMessage(messagesManager.getMessage("SKILLS_INFO_HEAD")));
                            player.sendMessage(formatMessage(messagesManager.getMessage("SKILLS_INFO_BODY"), skill, level, points, skillsManager.getXpToLevelUp(player, skill), skillsManager.getMaxSkillLevel(skill), skillsManager.isGiveXP(skill), skillsManager.isGiveMoney(skill), skillsManager.getExperienceMultiplier(player, skill), skillsManager.getMoneyMultiplier(player, skill)));
                            player.sendMessage(formatMessage(messagesManager.getMessage("SKILLS_INFO_FOOT")));
                        }
                        else{
                            player.sendMessage(formatMessage(messagesManager.getMessage("INVALID_SKILL"), arguments[1]));
                        }
                    }
                    else{
                        sendHelpInfos(sender);
                    }
                }
                else{
                    sender.sendMessage(formatMessage(messagesManager.getMessage("PLAYERS_ONLY")));
                }
            }
        }
        return true;
    }

    private void sendHelpInfos(CommandSender sender){
        sender.sendMessage(formatMessage(messagesManager.getMessage("HELP_MESSAGE")));
    }

    private String formatMessage(String message, String skillName, int skillLevel, int points, int requiredPoints, int maxLevel, boolean xpGiving, boolean moneyGiving, int xpMultiplier, int moneyMultiplier){
        String skillNamePlaceholder = "%SKILL%";
        String skillLevelPlaceholder = "%LEVEL%";
        String pointsPlaceholder = "%POINTS";
        String requiredPointsPlaceholder = "%REQUIRED_POINTS%";
        String maxLevelPlaceholder = "%MAX_LEVEL%";
        String xpGivingPlaceholder = "%XP_GIVING_ACTIVE%";
        String moneyGivingPlaceholder = "%MONEY_GIVING_ACTIVE";
        String xpMultiplierPlaceholder = "%XP_MULTIPLIER%";
        String moneyMultiplierPlaceholder = "%MONEY_MULTIPLIER%";
        String pluginHeadPlaceholder = "%PLUGIN_HEAD%";

        message = message.replaceAll(skillNamePlaceholder, skillName);
        message = message.replaceAll(skillLevelPlaceholder, "" + skillLevel);
        message = message.replaceAll(pointsPlaceholder, "" + points);
        message = message.replaceAll(requiredPointsPlaceholder, "" + requiredPoints);
        message = message.replaceAll(maxLevelPlaceholder, "" + maxLevel);
        message = message.replaceAll(xpGivingPlaceholder, "" + xpGiving);
        message = message.replaceAll(moneyGivingPlaceholder, "" + moneyGiving);
        message = message.replaceAll(xpMultiplierPlaceholder, "" + xpMultiplier);
        message = message.replaceAll(pluginHeadPlaceholder, pluginHeader);
        return message.replaceAll(moneyMultiplierPlaceholder, "" + moneyMultiplier);
    }

    private String formatMessage(String message, String skillName){
        return formatMessage(message, skillName, 0, 0, 0, 0, false, false, 0, 0);
    }

    private String formatMessage(String message){
        return formatMessage(message, "");
    }
}
