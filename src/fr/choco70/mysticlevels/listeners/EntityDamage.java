package fr.choco70.mysticlevels.listeners;

import fr.choco70.mysticlevels.MysticLevels;
import fr.choco70.mysticlevels.utils.EconomyLink;
import fr.choco70.mysticlevels.utils.PlayersManager;
import fr.choco70.mysticlevels.utils.SkillsManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;

public class EntityDamage implements Listener{

    private MysticLevels plugin = MysticLevels.getPlugin(MysticLevels.class);
    private SkillsManager skillsManager = plugin.getSkillsManager();
    private EconomyLink economyLink = plugin.getEconomyLink();
    private PlayersManager playersManager = plugin.getPlayersManager();

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Damageable){
            Damageable entity = (Damageable)e.getEntity();
            if(entity.getHealth() - e.getFinalDamage() <= 0){
                String entityType = entity.getType().name();
                Player player = (Player)e.getDamager();

                playersManager.createPlayerFile(player);

                ArrayList<String> skills = skillsManager.getActiveSkills();

                for (String skill : skills) {
                    FileConfiguration skillConfig = skillsManager.getSkillConfig(skillsManager.getSkillByName(skill));
                    if (skillConfig.isConfigurationSection("EVENTS.KILL")) {
                        if (skillConfig.isConfigurationSection("EVENTS.KILL." + entityType)) {
                            if (skillsManager.isGiveXP(skill)) {
                                Integer xp = skillConfig.getInt("EVENTS.KILL." + entityType + ".xp", 0);
                                if (!(xp == 0)) {
                                    Entity xpOrb = entity.getWorld().spawnEntity(entity.getLocation(), EntityType.EXPERIENCE_ORB);
                                    ((ExperienceOrb) xpOrb).setExperience((xp * skillsManager.getExperienceMultiplier(player, skill)));
                                }
                            }
                            if (plugin.getServer().getPluginManager().isPluginEnabled("Vault")) {
                                if (economyLink.setupEconomy()) {
                                    if (skillsManager.isGiveMoney(skill)) {
                                        Double money = skillConfig.getDouble("EVENTS.KILL." + entityType + ".money", 0);
                                        if (money != 0) {
                                            economyLink.addMoney(player, (money * skillsManager.getMoneyMultiplier(player, skill)));
                                        }
                                    }
                                }
                            }
                            Integer points = skillConfig.getInt("EVENTS.KILL." + entityType + ".points", 1);
                            skillsManager.addSkillPoints(player, skill, points);
                        }
                    }
                }
            }
        }
    }

}
