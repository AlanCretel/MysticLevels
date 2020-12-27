package fr.choco70.mysticlevels.managers;

import fr.choco70.mysticlevels.MysticLevels;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessagesManager{

    private static final MysticLevels plugin = MysticLevels.getPlugin(MysticLevels.class);

    public File getMessagesFile(){
        return new File(plugin.getDataFolder() + File.separator + "messages.yml");
    }

    public void createMessagesConfig(){
        File messagesConfigFile = getMessagesFile();
        if(!messagesConfigFile.exists()){
            try {
                if(!plugin.getDataFolder().exists()){
                    plugin.getDataFolder().mkdirs();
                }
                messagesConfigFile.createNewFile();
            } catch (IOException e) {
                System.out.println(ChatColor.RED + "[MysticLevels]: can't create messages files.");
            }
        }
    }

    public YamlConfiguration getMessagesConfig(){
        File messagesConfigFile = new File(plugin.getDataFolder() + File.separator + "messages.yml");
        if(!messagesConfigFile.exists()){
            createMessagesConfig();
        }

        return YamlConfiguration.loadConfiguration(messagesConfigFile);
    }

    public void updateMessages(){
        YamlConfiguration messagesConfig = getMessagesConfig();
        for (defaultMessages message : defaultMessages.values()) {
            if(!messagesConfig.isSet(message.name())){
                messagesConfig.set(message.name(), message.message);
            }
        }
        try {
            messagesConfig.save(getMessagesFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(String messageName){
        YamlConfiguration messagesConfig = getMessagesConfig();
        return messagesConfig.getString(messageName, defaultMessages.valueOf(messageName).message);
    }

    public enum defaultMessages{

        HELP_MESSAGE("§6=====-§7MysticLevels§6-=====\n§8  - §7/skills §6points §b<skill>\n§8  - §7/skills §6level §b<skill>\n§8  - §7/skills §6stats §b<skill>\n§8  - §7/skills §6info §b<skill>\n§8  - §7/skills §6list\n§8  - §7/skills §6help\n§6======================"),
        INFO_MONEY_GIVING("%PLUGIN_HEAD% §fMoney giving is set to §6 %MONEY_GIVING_ACTIVE%§f."),
        INFO_NB_SKILLS("%PLUGIN_HEAD% §fThey are §6%NB_OF_SKILLS%§f active skills."),
        INFO_XP_GIVING("%PLUGIN_HEAD% §fExperience giving is set to §6%XP_GIVING_ACTIVE%§f."),
        INVALID_SKILL("%PLUGIN_HEAD% §b%SKILL%§7 is not a valid skill."),
        LEVEL_INFO("%PLUGIN_HEAD% §7You are level §6%LEVEL%§7 in skill §b%SKILL%§7."),
        NO_SKILLS("%PLUGIN_HEAD% §4No skills present... report the issue to an administrator."),
        PLAYER_LEVEL_UP("%PLUGIN_HEAD% §7Your skill §b%SKILL%  §7is now level §6%LEVEL%§7."),
        PLAYERS_ONLY("This sub-command is player only..."),
        POINTS_INFO("%PLUGIN_HEAD% §7You have §6%POINTS%§7 points in skill §b %SKILL%§7."),
        SKILLS_INFO_BODY("§7  - §fName: §b%SKILL%\n§7  - §fLevel: §6%LEVEL%§7/§b%MAX_LEVEL%§f.\n§7  - §fPoints: §6%POINTS%§7/§b%REQUIRED_POINTS%§f.\n§7  - §fXpDrop: §b%XP_GIVING_ACTIVE%\n§7  - §fMoneyDrop: §b%MONEY_GIVING_ACTIVE%"),
        SKILLS_INFO_FOOT("§6====================="),
        SKILLS_INFO_HEAD("§6=====-§7SKILL INFO§6-====="),
        SKILLS_LIST_FOOT("§6==========================="),
        SKILLS_LIST_HEAD("§6=========-[§7SKILLS§6]-========="),
        SKILLS_LIST_SKILL("§7  - §9%SKILL%"),
        SKILLS_STATS_BODY("§fLevel: §6%LEVEL%\n§fPoints §6%POINTS%§7/§b%REQUIRED_POINTS%§f to level up.\n§fMoney reward multiplier: §6%MONEY_MULTIPLIER%\n§fXP reward multiplier: §6%XP_MULTIPLIER%"),
        SKILLS_STATS_FOOT("§6================================="),
        SKILLS_STATS_HEAD("§6==========-§7%SKILL%§6-==========");

        String message;

        defaultMessages(String message) {
            this.message = message;
        }
    }

}
