package fr.choco70.mysticlevels;

import fr.choco70.mysticlevels.commands.CommandSkills;
import fr.choco70.mysticlevels.commands.tabCompleters.SkillsTabCompleter;
import fr.choco70.mysticlevels.listeners.*;
import fr.choco70.mysticlevels.managers.*;
import fr.choco70.mysticlevels.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;

public class MysticLevels extends JavaPlugin{

    private PlayersManager playersManager;
    private SkillsManager skillsManager;
    private ConfigManager configManager;
    private EconomyLink economyLink;
    private PlaceHoldersManager placeHoldersManager;
    private MessagesManager messagesManager;
    private Metrics metrics;
    private ArrayList<Skill> skills = new ArrayList<>();

    @Override
    public void onEnable(){
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "MysticLevels: Plugin enabled");
        FileConfiguration config = this.getConfig();
        config.options().copyDefaults(true);

        metrics = new Metrics(this, 9471);

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
        messagesManager.createMessagesConfig();
        messagesManager.updateMessages();

    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "MysticLevels: Plugin disabled");
    }

    private void instanceClasses(){
        messagesManager = new MessagesManager();
        configManager = new ConfigManager();
        playersManager = new PlayersManager();
        skillsManager = new SkillsManager();

        try{
            double test = skillsManager.eval(skillsManager.getFormula().replaceAll("\\{LEVEL}", "1"));
        }catch(NumberFormatException e){
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error in the formula. Default formula will be used. (LEVEL * 10 + 10)");
        }

        if(getServer().getPluginManager().isPluginEnabled("Vault")){
            economyLink = new EconomyLink();
        }
        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
            placeHoldersManager = new PlaceHoldersManager(this);
            placeHoldersManager.register();
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

    public MessagesManager getMessagesManager() {
        return this.messagesManager;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }

    public void addSkill(Skill skill){
        skills.add(skill);
    }

    public void removeSkill(Skill skill){
        skills.remove(skill);
    }
}
