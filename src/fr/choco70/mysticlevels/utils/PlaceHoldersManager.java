package fr.choco70.mysticlevels.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceHoldersManager {

    public void registerPlaceholders(){

        PlaceholderAPI.registerPlaceholderHook("mysticlevels", new PlaceholderHook() {
            @Override
            public String onRequest(OfflinePlayer p, String params){
                if(p != null && p.isOnline()){
                    return onPlaceholderRequest(p.getPlayer(), params);
                }
                return null;
            }

            @Override
            public String onPlaceholderRequest(Player p, String params) {
                if(p == null){
                    return null;
                }
                for (String skill : SkillsManager.getSkills()) {
                    if(params.equalsIgnoreCase("level_" + skill)){
                        return SkillsManager.getSkillLevel(p, skill).toString();
                    }
                    if(params.equalsIgnoreCase("points_" + skill)){
                        return SkillsManager.getSkillPoints(p, skill).toString();
                    }
                    if(params.equalsIgnoreCase("max-level_" + skill)){
                        if(SkillsManager.hasMaxLevel(skill)){
                            return SkillsManager.getMaxSkillLevel(skill).toString();
                        }
                        else{
                            return "inf.";
                        }
                    }
                    if(params.equalsIgnoreCase("required-points_" + skill)){
                        return SkillsManager.getXpToLevelUp(p, skill).toString();
                    }
                    if(params.equalsIgnoreCase("xp-multiplier_" + skill)){
                        return SkillsManager.getExperienceMultiplier(p, skill).toString();
                    }
                    if(params.equalsIgnoreCase("money-multiplier_" + skill)){
                        return SkillsManager.getMoneyMultiplier(p, skill).toString();
                    }
                }
                return null;
            }
        });
    }
}