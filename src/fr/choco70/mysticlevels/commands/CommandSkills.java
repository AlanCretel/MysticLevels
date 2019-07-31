package fr.choco70.mysticlevels.commands;

import fr.choco70.mysticlevels.MysticLevels;
import fr.choco70.mysticlevels.utils.SkillsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandSkills implements CommandExecutor{

    private MysticLevels plugin = MysticLevels.getPlugin(MysticLevels.class);
    private SkillsManager skillsManager = plugin.getSkillsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments){
        String pluginHeader = plugin.getConfig().getString("SETTINGS.plugin_header");
        if(arguments.length == 0){
            sendHelpInfos(sender);
        }
        else{
            if(arguments.length == 1){
                if(arguments[0].equalsIgnoreCase("list")){
                    if(skillsManager.getActiveSkills().size() == 0){
                        sender.sendMessage(pluginHeader + "§4No skills present... report the issue to an administrator.");
                    }
                    else{
                        sender.sendMessage(ChatColor.GOLD + "=========-["  + ChatColor.GRAY + "SKILLS" + ChatColor.GOLD + "]-=========");
                        ArrayList<String> skills = skillsManager.getActiveSkills();
                        String skillPrefix = "§7  - ";
                        for (String skill : skills) {
                            sender.sendMessage(skillPrefix + ChatColor.BLUE + skill.toUpperCase());
                        }
                        sender.sendMessage(ChatColor.GOLD + "===========================");
                    }
                }
                else if(arguments[0].equalsIgnoreCase("info")){
                    sender.sendMessage(pluginHeader + "§fThey are §6" + skillsManager.getActiveSkills().size() + "§f skills active.");
                    sender.sendMessage(pluginHeader + "§fMoney giving is set to §6" + skillsManager.doesGiveMoney() + "§f.");
                    sender.sendMessage(pluginHeader + "§fExperience giving is set to §6" + skillsManager.doesGiveXP() + "§f.");
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
                            player.sendMessage(pluginHeader + "§7You have §6" + points + "§7 points in skill §b" + skill + "§7.");
                        }
                        else{
                            player.sendMessage(pluginHeader + "§b" + arguments[1] + "§7 is not a valid skill.");
                        }
                    }
                    else if(arguments[0].equalsIgnoreCase("level")){
                        if(skillsManager.isSkill(arguments[1]) && skillsManager.isActiveSkill(arguments[1])){
                            String skill = skillsManager.getSkillByName(arguments[1]);
                            Integer level = skillsManager.getSkillLevel(player, skill);
                            player.sendMessage(pluginHeader + "§7You are level §6" + level + "§7 in skill §b" + skill + "§7.");
                        }
                        else{
                            player.sendMessage(pluginHeader + "§b" + arguments[1] + "§7 is not a valid skill.");
                        }
                    }
                    else if(arguments[0].equalsIgnoreCase("stats")){
                        if(skillsManager.isSkill(arguments[1]) && skillsManager.isActiveSkill(arguments[1])){
                            String skill = skillsManager.getSkillByName(arguments[1]);
                            Integer level = skillsManager.getSkillLevel(player, skill);
                            Integer points = skillsManager.getSkillPoints(player, skill);
                            Integer nextLevel = (skillsManager.getSkillLevel(player, skill) * 10 + 10);
                            String header = ("§6==========-§7" + skill.toUpperCase() + "§6-==========");
                            player.sendMessage(header);
                            player.sendMessage("§fLevel: §6" + level);
                            player.sendMessage("§fPoints §6" + points + "§7/§b" + nextLevel + "§f to level up.");
                            player.sendMessage("§fMoney reward multiplier: §6" + skillsManager.getMoneyMultiplier(player, skill));
                            player.sendMessage("§fXP reward multiplier: §6" + skillsManager.getExperienceMultiplier(player, skill));
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < header.toCharArray().length - 6; i++) {
                                stringBuilder.append("=");
                            }
                            sender.sendMessage("§6" + stringBuilder.toString());
                        }
                        else{
                            player.sendMessage(pluginHeader + "§b" + arguments[1] + "§7 is not a valid skill or skill is disabled.");
                        }
                    }
                    else if(arguments[0].equalsIgnoreCase("info")){
                        if(skillsManager.isSkill(arguments[1]) && skillsManager.isActiveSkill(arguments[1])){
                            String skill = skillsManager.getSkillByName(arguments[1]);
                            Integer level = skillsManager.getSkillLevel(player, skill);
                            Integer points = skillsManager.getSkillPoints(player, skill);
                            player.sendMessage("§6=====-§7SKILL INFO§6-=====");
                            player.sendMessage("§7  - §fName: §b" + skill.toUpperCase());
                            player.sendMessage("§7  - §fLevel: §6" + level + "§7/§b" + skillsManager.getMaxSkillLevel(skill) + "§f.");
                            player.sendMessage("§7  - §fPoints: §6" + points + "§7/§b" + skillsManager.getXpToLevelUp(player, skill) + "§f.");
                            player.sendMessage("§7  - §fXpDrop: §b" + skillsManager.isGiveXP(skill));
                            player.sendMessage("§7  - §fMoneyDrop: §b" + skillsManager.isGiveMoney(skill));
                            player.sendMessage("§6=====================");
                        }
                        else{
                            player.sendMessage(pluginHeader + "§b" + arguments[1] + "§7 is not a valid skill or skill is disabled.");
                        }
                    }
                    else{
                        sendHelpInfos(sender);
                    }
                }
                else{
                    sender.sendMessage("This sub-command is player only...");
                }
            }
        }
        return true;
    }

    private void sendHelpInfos(CommandSender sender){
        sender.sendMessage("§6=====-§7MysticLevels§6-=====");
        sender.sendMessage("§8  - §7/skills §6points §b<skill>");
        sender.sendMessage("§8  - §7/skills §6level §b<skill>");
        sender.sendMessage("§8  - §7/skills §6stats §b<skill>");
        sender.sendMessage("§8  - §7/skills §6info §b<skill>");
        sender.sendMessage("§8  - §7/skills §6list");
        sender.sendMessage("§8  - §7/skills §6help");
        sender.sendMessage("§6======================");
    }
}
