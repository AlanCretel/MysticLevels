package fr.choco70.mysticlevels.managers;

import fr.choco70.mysticlevels.MysticLevels;

import java.io.File;
import java.util.ArrayList;

public class ConfigManager{

    private final MysticLevels plugin = MysticLevels.getPlugin(MysticLevels.class);
    private ArrayList<String> defaultSkills = new ArrayList<>();

    public void setDefaultSkills() {
        ArrayList<String> defaultSkills = new ArrayList<>();
        defaultSkills.add("excavating");
        defaultSkills.add("farming");
        defaultSkills.add("herborist");
        defaultSkills.add("mining");
        defaultSkills.add("woodcutting");
        defaultSkills.add("example");
        this.defaultSkills = defaultSkills;
    }

    public void copyDefaultConfigs(){
        File pluginDataFolder = plugin.getDataFolder();
        if(!pluginDataFolder.exists()){
            pluginDataFolder.mkdir();
        }
        File skillsDataFolder = new File(pluginDataFolder + File.separator + "skills" + File.separator);
        if(!skillsDataFolder.exists()){
            skillsDataFolder.mkdir();
        }

        File[] skillFiles = skillsDataFolder.listFiles();
        ArrayList<String> skillFileNames = new ArrayList<>();
        for (File skillFile : skillFiles){
            skillFileNames.add(skillFile.getName().replace(".yml", ""));
        }
        String skillsFolder = "skills" + File.separator;
        for (String defaultSkill : defaultSkills) {
            if(!skillFileNames.contains(defaultSkill)){
                plugin.saveResource(skillsFolder + defaultSkill + ".yml", false);
            }
        }
    }

}
