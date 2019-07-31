package fr.choco70.mysticlevels.utils;

import fr.choco70.mysticlevels.MysticLevels;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;

public class SkillsManager{

    private MysticLevels plugin = MysticLevels.getPlugin(MysticLevels.class);
    private PlayersManager playersManager = plugin.getPlayersManager();

    public void createSkillFile(String skillName){
        File pluginDataFolder = plugin.getDataFolder();
        if(!pluginDataFolder.exists()){
            pluginDataFolder.mkdir();
        }
        File skillsDataFolder = new File(pluginDataFolder + File.separator + "skills" + File.separator);
        if(!skillsDataFolder.exists()){
            skillsDataFolder.mkdir();
        }
        File skillFile = new File(skillsDataFolder, skillName.toUpperCase() + ".yml");
        if(!skillFile.exists()){
            skillFile.mkdir();
        }
    }

    public File getSkillFile(String skillName){
        File skillsDataFolder = new File(plugin.getDataFolder() + File.separator + "skills" + File.separator);
        return new File(skillsDataFolder, skillName +".yml");
    }

    public FileConfiguration getSkillConfig(String skillName){
        return YamlConfiguration.loadConfiguration(getSkillFile(skillName));
    }

    public ArrayList<String> getSkills(){
        File pluginDataFolder = plugin.getDataFolder();
        if(!pluginDataFolder.exists()){
            pluginDataFolder.mkdir();
        }
        File skillsDataFolder = new File(pluginDataFolder + File.separator + "skills");
        if(!skillsDataFolder.exists()){
            skillsDataFolder.mkdir();
        }

        ArrayList<File> skillFiles = new ArrayList<>();
        File[] files = skillsDataFolder.listFiles();
        for (File file : files) {
            if(file.getName().endsWith(".yml")){
                skillFiles.add(file);
            }
        }
        ArrayList<String> skills = new ArrayList<>();
        for (File skillFile : skillFiles) {
            skills.add(skillFile.getName().replace(".yml", "").replaceAll(" ", "_"));
        }
        return skills;
    }

    public void addSkillLevels(Player player, String skill, Integer levels){
        String pluginHeader = plugin.getConfig().getString("SETTINGS.plugin_header", "§6[MysticLevels]: ");
        FileConfiguration playerConfig = playersManager.getPlayerConfig(player);
        playerConfig.set(skill.toUpperCase() + ".level", getSkillLevel(player, skill.toUpperCase()) + levels);
        playersManager.savePlayerConfig(playerConfig, player);
        player.sendMessage(pluginHeader + "§7Your skill §b" + skill.toUpperCase() + " §7is now level §6" + getSkillLevel(player, skill.toUpperCase()).toString() + "§7.");
    }

    public void addSkillPoints(Player player, String skill, Integer points){
        Integer playerPoints = getSkillPoints(player, skill.toUpperCase());
        playerPoints = playerPoints + points;
        while(playerPoints >= (getSkillLevel(player, skill.toUpperCase()) * 10 + 10)){
            playerPoints = playerPoints - (getSkillLevel(player, skill.toUpperCase()) * 10 + 10);
            setPlayerSkillPoints(player, skill.toUpperCase(), playerPoints);
            addSkillLevels(player, skill.toUpperCase(), 1);
        }
        setPlayerSkillPoints(player, skill.toUpperCase(), playerPoints);
    }

    public Integer getSkillPoints(Player player, String skill){
        return playersManager.getPlayerConfig(player).getInt(skill.toUpperCase() + ".points", 0);
    }

    public Integer getSkillLevel(Player player, String skill){
        return playersManager.getPlayerConfig(player).getInt(skill.toUpperCase() + ".level", 0);
    }

    public Integer getXpToLevelUp(Player player, String skill){
        return (getSkillLevel(player, skill.toUpperCase()) * 10 + 10);
    }

    public void setPlayerSkillPoints(Player player, String skill, Integer points){
        FileConfiguration playerConfig = playersManager.getPlayerConfig(player);
        playerConfig.set(skill.toUpperCase() + ".points", points);
        playersManager.savePlayerConfig(playerConfig, player);
    }

    public Integer getExperienceMultiplier(Player player, String skill){
        return (1 + getSkillLevel(player, skill.toUpperCase())/10);
    }

    public Integer getMoneyMultiplier(Player player, String skill){
        return (1 + getSkillLevel(player, skill.toUpperCase())/10);
    }

    public boolean isSkill(String string){
        ArrayList<String> skills = getSkills();
        boolean result = false;
        for (String skill : skills) {
            if(string.equalsIgnoreCase(skill)){
                result = true;
            }
        }
        return result;
    }

    public String getSkillByName(String name){
        if(isSkill(name)){
            ArrayList<String> skills = getSkills();
            String skillName = "";
            for (String skill : skills) {
                if(name.equalsIgnoreCase(skill)){
                    skillName = skill;
                }
            }
            return skillName;
        }
        else{
            return null;
        }
    }

    public boolean isGiveXP(String skillName){
        FileConfiguration pluginConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        if(!pluginConfig.getBoolean("SETTINGS.give_xp", true)){
            return false;
        }
        else{
            return getSkillConfig(skillName).getBoolean("CONFIG.give_xp", true);
        }
    }

    public boolean isGiveMoney(String skillName){
        FileConfiguration pluginConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        if(!pluginConfig.getBoolean("SETTINGS.give_money", false)){
            return false;
        }
        else{
            return getSkillConfig(skillName).getBoolean("CONFIG.give_money", false);
        }
    }

    public boolean isActiveSkill(String skillName){
        return getSkillConfig(skillName).getBoolean("CONFIG.active", true);
    }

    public ArrayList<String> getActiveSkills(){
        ArrayList<String> activeSkills = new ArrayList<>();
        ArrayList<String> skills = getSkills();
        for (String skill : skills) {
            if(isActiveSkill(skill)){
                activeSkills.add(skill);
            }
        }
        return activeSkills;
    }

    public boolean doesGiveMoney(){
        FileConfiguration pluginConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        return pluginConfig.getBoolean("SETTINGS.give_money");
    }

    public boolean doesGiveXP(){
        FileConfiguration pluginConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        return pluginConfig.getBoolean("SETTINGS.give_xp");
    }

    public int getMaxSkillLevel(String skillName){
        return getSkillConfig(skillName).getInt("CONFIG.max_level", 100);
    }
}
