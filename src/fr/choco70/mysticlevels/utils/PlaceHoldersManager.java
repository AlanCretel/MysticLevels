package fr.choco70.mysticlevels.utils;

import fr.choco70.mysticlevels.MysticLevels;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceHoldersManager extends PlaceholderExpansion{

    private MysticLevels plugin;

    public PlaceHoldersManager(MysticLevels plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public String getIdentifier() {
        return "mysticlevels";
    }

    @Override
    public String getAuthor() {
        return "Choco70";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier){
        Player p = player.getPlayer();
        if(p == null){
            return null;
        }
        for (String skill : SkillsManager.getSkills()) {
            if(identifier.equalsIgnoreCase("level_" + skill)){
                return SkillsManager.getSkillLevel(p, skill).toString();
            }
            if(identifier.equalsIgnoreCase("points_" + skill)){
                return SkillsManager.getSkillPoints(p, skill).toString();
            }
            if(identifier.equalsIgnoreCase("max-level_" + skill)){
                if(SkillsManager.hasMaxLevel(skill)){
                    return SkillsManager.getMaxSkillLevel(skill).toString();
                }
                else{
                    return "inf.";
                }
            }
            if(identifier.equalsIgnoreCase("required-points_" + skill)){
                return SkillsManager.getXpToLevelUp(p, skill).toString();
            }
            if(identifier.equalsIgnoreCase("xp-multiplier_" + skill)){
                return SkillsManager.getExperienceMultiplier(p, skill).toString();
            }
            if(identifier.equalsIgnoreCase("money-multiplier_" + skill)){
                return SkillsManager.getMoneyMultiplier(p, skill).toString();
            }
        }
        return null;
    }
}