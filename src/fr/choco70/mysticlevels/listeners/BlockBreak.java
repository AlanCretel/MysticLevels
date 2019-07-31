package fr.choco70.mysticlevels.listeners;

import fr.choco70.mysticlevels.MysticLevels;
import fr.choco70.mysticlevels.utils.EconomyLink;
import fr.choco70.mysticlevels.utils.PlayersManager;
import fr.choco70.mysticlevels.utils.SkillsManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

public class BlockBreak implements Listener {

    private MysticLevels plugin = MysticLevels.getPlugin(MysticLevels.class);
    private SkillsManager skillsManager = plugin.getSkillsManager();
    private PlayersManager playersManager = plugin.getPlayersManager();
    private EconomyLink economyLink = plugin.getEconomyLink();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(!e.isCancelled()){
            Player player = e.getPlayer();
            Block block = e.getBlock();
            Material blockMaterial = block.getType();
            BlockData blockData = block.getBlockData();

            playersManager.createPlayerFile(player);

            ArrayList<String> skills = skillsManager.getActiveSkills();

            for (String skill : skills) {
                FileConfiguration skillConfig = skillsManager.getSkillConfig(skillsManager.getSkillByName(skill));
                if(skillConfig.isConfigurationSection("EVENTS.BREAK")){
                    if(skillConfig.isConfigurationSection("EVENTS.BREAK." + blockMaterial.name()) && !block.hasMetadata("PlacedBy")){
                        if(skillsManager.isGiveXP(skill)){
                            Integer xp = skillConfig.getInt("EVENTS.BREAK." + blockMaterial.name() + ".xp",0);
                            if(!(xp == 0)){
                                Entity xpOrb = block.getWorld().spawnEntity(block.getLocation(), EntityType.EXPERIENCE_ORB);
                                ((ExperienceOrb)xpOrb).setExperience((xp * skillsManager.getExperienceMultiplier(player, skill)));
                            }
                        }
                        if(plugin.getServer().getPluginManager().isPluginEnabled("Vault")){
                            if(economyLink.setupEconomy()){
                                if(skillsManager.isGiveMoney(skill)){
                                    Double money = skillConfig.getDouble("EVENTS.BREAK." + blockMaterial.name() + ".money",0);
                                    if(money != 0){
                                        economyLink.addMoney(player, (money * skillsManager.getMoneyMultiplier(player, skill)));
                                    }
                                }
                            }
                        }
                        Integer points = skillConfig.getInt("EVENTS.BREAK." + blockMaterial.name() + ".points", 1);
                        skillsManager.addSkillPoints(player, skill, points);
                    }
                }
                if(skillConfig.isConfigurationSection("EVENTS.HARVEST")){
                    if(skillConfig.isConfigurationSection("EVENTS.HARVEST." + blockMaterial.name())){
                        if(blockData instanceof Ageable){
                            if(((Ageable) blockData).getAge() == ((Ageable) blockData).getMaximumAge()){
                                if(skillsManager.isGiveXP(skill)){
                                    Integer xp = skillConfig.getInt("EVENTS.HARVEST." + blockMaterial.name() + ".xp",0);
                                    if(!(xp == 0)){
                                        Entity xpOrb = block.getWorld().spawnEntity(block.getLocation(), EntityType.EXPERIENCE_ORB);
                                        ((ExperienceOrb)xpOrb).setExperience((xp*skillsManager.getExperienceMultiplier(player, skill)));
                                    }
                                }
                                if(plugin.getServer().getPluginManager().isPluginEnabled("Vault")){
                                    if(economyLink.setupEconomy()){
                                        if(skillsManager.isGiveMoney(skill)){
                                            Double money = skillConfig.getDouble("EVENTS.HARVEST." + blockMaterial.name() + ".money",0);
                                            if(money != 0){
                                                economyLink.addMoney(player, (money * skillsManager.getMoneyMultiplier(player, skill)));
                                            }
                                        }
                                    }
                                }
                                Integer points = skillConfig.getInt("EVENTS.HARVEST." + blockMaterial.name() + ".points", 1);
                                skillsManager.addSkillPoints(player, skill, points);
                            }
                        }
                    }
                }
            }
        }
    }
}
