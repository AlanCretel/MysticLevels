package fr.choco70.mysticlevels.managers;

import fr.choco70.mysticlevels.MysticLevels;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayersManager{

    private MysticLevels plugin = MysticLevels.getPlugin(MysticLevels.class);

    public void createPlayerFile(Player player){
        File playersForlder = new File(plugin.getDataFolder() + File.separator + "players" + File.separator);
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        if(!playersForlder.exists()){
            playersForlder.mkdir();
        }
        File file = new File(playersForlder,player.getUniqueId().toString() + ".yml");

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getPlayerFile(Player player){
        File playersForlder = new File(plugin.getDataFolder() + File.separator + "players" + File.separator);
        return new File(playersForlder,player.getUniqueId().toString() + ".yml");
    }

    public FileConfiguration getPlayerConfig(Player player){
        return YamlConfiguration.loadConfiguration(getPlayerFile(player));
    }

    public void savePlayerConfig(FileConfiguration playerConfig, Player player){
        try {
            playerConfig.save(getPlayerFile(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
