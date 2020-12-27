package fr.choco70.mysticlevels.managers;

import fr.choco70.mysticlevels.MysticLevels;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;

public class SkillsManager{

    private static MysticLevels plugin = MysticLevels.getPlugin(MysticLevels.class);
    private static PlayersManager playersManager = plugin.getPlayersManager();

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

    public static File getSkillFile(String skillName){
        File skillsDataFolder = new File(plugin.getDataFolder() + File.separator + "skills" + File.separator);
        return new File(skillsDataFolder, skillName +".yml");
    }

    public static FileConfiguration getSkillConfig(String skillName){
        return YamlConfiguration.loadConfiguration(getSkillFile(skillName));
    }

    public static ArrayList<String> getSkills(){
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
        while(playerPoints >= evaluatePointsFormula(skill, getFormula(), player)){
            playerPoints = playerPoints - evaluatePointsFormula(skill, getFormula(), player);
            setPlayerSkillPoints(player, skill.toUpperCase(), playerPoints);
            addSkillLevels(player, skill.toUpperCase(), 1);
        }
        setPlayerSkillPoints(player, skill.toUpperCase(), playerPoints);
    }

    public static Integer getSkillPoints(Player player, String skill){
        return playersManager.getPlayerConfig(player).getInt(skill.toUpperCase() + ".points", 0);
    }

    public static Integer getSkillLevel(Player player, String skill){
        return playersManager.getPlayerConfig(player).getInt(skill.toUpperCase() + ".level", 0);
    }

    public static Integer getXpToLevelUp(Player player, String skill){
        return evaluatePointsFormula(skill, getFormula(), player);
    }

    public void setPlayerSkillPoints(Player player, String skill, Integer points){
        FileConfiguration playerConfig = playersManager.getPlayerConfig(player);
        playerConfig.set(skill.toUpperCase() + ".points", points);
        playersManager.savePlayerConfig(playerConfig, player);
    }

    public static Integer getExperienceMultiplier(Player player, String skill){
        return (1 + getSkillLevel(player, skill.toUpperCase())/10);
    }

    public static Integer getMoneyMultiplier(Player player, String skill){
        return (1 + getSkillLevel(player, skill.toUpperCase())/10);
    }

    public boolean isSkill(String string){
        ArrayList<String> skills = getSkills();
        for (String skill : skills) {
            if (string.equalsIgnoreCase(skill)) {
                return true;
            }
        }
        return false;
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
        if(!doesGiveXP()){
            return false;
        }
        else{
            return getSkillConfig(skillName).getBoolean("CONFIG.give_xp", true);
        }
    }

    public boolean isGiveMoney(String skillName){
        if(!doesGiveMoney()){
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

    public static Integer getMaxSkillLevel(String skillName){
        if(hasMaxLevel(skillName)){

        }
        return getSkillConfig(skillName).getInt("CONFIG.max_level.level", 100);
    }

    public static boolean hasMaxLevel(String skillName){
        if(hasGlobalMaxLevel()){
            return true;
        }
        return getSkillConfig(skillName).getBoolean("CONFIG.max_level.enabled", false);
    }

    public static boolean isGlobalMaxLevelActive(){
        return plugin.getConfig().getBoolean("SETTINGS.global_max_level.active", true);
    }

    public static Integer getGlobalMaxLevel(){
        return plugin.getConfig().getInt("SETTINGS.global_max_level.level", 100);
    }

    public static boolean hasGlobalMaxLevel(){
        if(isGlobalMaxLevelActive()){
            return plugin.getConfig().getBoolean("SETTINGS.global_max_level.enabled", false);
        }
        return false;
    }

    public static int evaluatePointsFormula(String skillName, String formula, Player player){
        int skillLevel = getSkillLevel(player, skillName);
        String skillLevelPlaceholder = "\\{LEVEL}";
        formula = formula.toUpperCase().replaceAll(skillLevelPlaceholder, "" + skillLevel);
        try{
            double result = eval(formula);
            return (int) result;
        } catch(NumberFormatException e){
            return skillLevel * 10 + 10;
        }
    }

    public static String getFormula(){
        return plugin.getConfig().getString("SETTINGS.levelup_formula", "{LEVEL}*10 + 10");
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}