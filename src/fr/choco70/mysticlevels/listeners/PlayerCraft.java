package fr.choco70.mysticlevels.listeners;

import fr.choco70.mysticlevels.MysticLevels;
import fr.choco70.mysticlevels.utils.EconomyLink;
import fr.choco70.mysticlevels.utils.PlayersManager;
import fr.choco70.mysticlevels.utils.SkillsManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.ArrayList;

public class PlayerCraft implements Listener {

    private MysticLevels plugin = MysticLevels.getPlugin(MysticLevels.class);
    private SkillsManager skillsManager = plugin.getSkillsManager();
    private EconomyLink economyLink = plugin.getEconomyLink();
    private PlayersManager playersManager = plugin.getPlayersManager();

    @EventHandler
    public void onPlayerCraft(CraftItemEvent e){
        if(e.getWhoClicked() instanceof Player){
            Player player = (Player)e.getWhoClicked();
            Material itemMaterial = e.getRecipe().getResult().getType();

            playersManager.createPlayerFile(player);

            Integer amount = e.getRecipe().getResult().getAmount();
            e.getResult().ordinal();

            ArrayList<String> skills = skillsManager.getActiveSkills();

            for (String skill : skills) {
                FileConfiguration skillConfig = skillsManager.getSkillConfig(skillsManager.getSkillByName(skill));
                if (skillConfig.isConfigurationSection("EVENTS.CRAFT")) {
                    if (skillConfig.isConfigurationSection("EVENTS.CRAFT." + itemMaterial.name())) {
                        if (skillsManager.isGiveXP(skill)) {
                            Integer xp = skillConfig.getInt("EVENTS.CRAFT." + itemMaterial.name() + ".xp", 0) * amount;
                            if (!(xp == 0)) {
                                Entity xpOrb = player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
                                ((ExperienceOrb) xpOrb).setExperience((xp * skillsManager.getExperienceMultiplier(player, skill)));
                            }
                        }
                        if (plugin.getServer().getPluginManager().isPluginEnabled("Vault")) {
                            if (economyLink.setupEconomy()) {
                                if (skillsManager.isGiveMoney(skill)) {
                                    Double money = skillConfig.getDouble("EVENTS.CRAFT." + itemMaterial.name() + ".money", 0) * amount;
                                    if (money != 0) {
                                        economyLink.addMoney(player, (money * skillsManager.getMoneyMultiplier(player, skill)));
                                    }
                                }
                            }
                        }
                        Integer points = skillConfig.getInt("EVENTS.CRAFT." + itemMaterial.name() + ".points", 1) * amount;
                        skillsManager.addSkillPoints(player, skill, points);
                    }
                }
            }
        }
    }

}
