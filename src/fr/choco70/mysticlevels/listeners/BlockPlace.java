package fr.choco70.mysticlevels.listeners;

import fr.choco70.mysticlevels.MysticLevels;
import fr.choco70.mysticlevels.utils.EconomyLink;
import fr.choco70.mysticlevels.utils.PlayersManager;
import fr.choco70.mysticlevels.utils.SkillsManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;

public class BlockPlace implements Listener{

    private MysticLevels plugin = MysticLevels.getPlugin(MysticLevels.class);
    private SkillsManager skillsManager = plugin.getSkillsManager();
    private EconomyLink economyLink = plugin.getEconomyLink();
    private PlayersManager playersManager = plugin.getPlayersManager();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if(!e.isCancelled() && e.canBuild() && (!e.getPlayer().isOp() || !e.getPlayer().getGameMode().equals(GameMode.CREATIVE))){
            Player player = e.getPlayer();
            Block block = e.getBlockPlaced();
            block.setMetadata("PlacedBy", new FixedMetadataValue(plugin, player.getUniqueId()));
        }

        Block block = e.getBlockPlaced();
        Material blockMaterial = block.getType();
        Player player = e.getPlayer();

        playersManager.createPlayerFile(player);

        ArrayList<String> skills = skillsManager.getActiveSkills();

        for (String skill : skills) {
            FileConfiguration skillConfig = skillsManager.getSkillConfig(skillsManager.getSkillByName(skill));
            if (skillConfig.isConfigurationSection("EVENTS.PLACE")) {
                if (skillConfig.isConfigurationSection("EVENTS.PLACE." + blockMaterial.name())) {
                    if (skillsManager.isGiveXP(skill)) {
                        Integer xp = skillConfig.getInt("EVENTS.PLACE." + blockMaterial.name() + ".xp", 0);
                        if (!(xp == 0)) {
                            Entity xpOrb = block.getWorld().spawnEntity(block.getLocation(), EntityType.EXPERIENCE_ORB);
                            ((ExperienceOrb) xpOrb).setExperience((xp * skillsManager.getExperienceMultiplier(player, skill)));
                        }
                    }
                    if (plugin.getServer().getPluginManager().isPluginEnabled("Vault")) {
                        if (economyLink.setupEconomy()) {
                            if (skillsManager.isGiveMoney(skill)) {
                                Double money = skillConfig.getDouble("EVENTS.PLACE." + blockMaterial.name() + ".money", 0);
                                if (money != 0) {
                                    economyLink.addMoney(player, (money * skillsManager.getMoneyMultiplier(player, skill)));
                                }
                            }
                        }
                    }
                    Integer points = skillConfig.getInt("EVENTS.PLACE." + blockMaterial.name() + ".points", 1);
                    skillsManager.addSkillPoints(player, skill, points);
                }
            }
        }
    }
}
