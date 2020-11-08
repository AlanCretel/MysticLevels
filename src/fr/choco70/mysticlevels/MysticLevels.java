package fr.choco70.mysticlevels;

import fr.choco70.mysticlevels.commands.CommandSkills;
import fr.choco70.mysticlevels.commands.tabCompleters.SkillsTabCompleter;
import fr.choco70.mysticlevels.listeners.*;
import fr.choco70.mysticlevels.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class MysticLevels extends JavaPlugin{

    private PlayersManager playersManager;
    private SkillsManager skillsManager;
    private ConfigManager configManager;
    private EconomyLink economyLink;
    private PlaceHoldersManager placeHoldersManager;
    private boolean placeholders = false;

    @Override
    public void onEnable(){
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "MysticLevels: Plugin enabled");
        FileConfiguration config = this.getConfig();
        config.options().copyDefaults(true);

        try {
            config.save(this.getDataFolder() + File.separator + "config.yml");
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "MysticLevels: Configuration loaded");
        } catch (IOException e) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "MysticLevels: Error loading configuration, stopping plugin...");
            getServer().getPluginManager().disablePlugin(this);
            e.printStackTrace();
        }

        instanceClasses();
        registerCommands();
        setupTabCompleters();
        registerEvents();

        configManager.setDefaultSkills();
        configManager.copyDefaultConfigs();
        if(placeholders){
            placeHoldersManager.registerPlaceholders();
        }
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "MysticLevels: Plugin disabled");
    }

    private void instanceClasses(){
        configManager = new ConfigManager();
        playersManager = new PlayersManager();
        skillsManager = new SkillsManager();
        if(getServer().getPluginManager().isPluginEnabled("Vault")){
            economyLink = new EconomyLink();
        }
        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
            placeHoldersManager = new PlaceHoldersManager();
            placeholders = true;
        }
    }

    private void registerCommands(){
        this.getCommand("skills").setExecutor(new CommandSkills());
    }

    private void setupTabCompleters(){
        this.getCommand("skills").setTabCompleter(new SkillsTabCompleter());
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(), this);
        getServer().getPluginManager().registerEvents(new PlayerCraft(), this);
        getServer().getPluginManager().registerEvents(new EntityDamage(), this);
    }

    public PlayersManager getPlayersManager(){
        return playersManager;
    }

    public SkillsManager getSkillsManager(){
        return skillsManager;
    }

    public EconomyLink getEconomyLink(){
        return economyLink;
    }
}
