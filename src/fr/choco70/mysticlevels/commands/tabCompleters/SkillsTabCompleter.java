package fr.choco70.mysticlevels.commands.tabCompleters;

import fr.choco70.mysticlevels.MysticLevels;
import fr.choco70.mysticlevels.utils.SkillsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkillsTabCompleter implements TabCompleter{

    private MysticLevels plugin = MysticLevels.getPlugin(MysticLevels.class);
    private SkillsManager skillsManager = plugin.getSkillsManager();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] arguments){
        if(arguments.length == 0){
            return getSubCommands();
        }
        else if(arguments.length == 1){
            ArrayList<String> subCommands = getSubCommands();
            ArrayList<String> suggestions = new ArrayList<>();
            for (String subCommand : subCommands) {
                if(subCommand.startsWith(arguments[0])){
                    suggestions.add(subCommand);
                }
            }
            Collections.sort(suggestions);
            return suggestions;
        }
        else if(arguments.length == 2){
            ArrayList<String> skillNames = skillsManager.getActiveSkills();
            ArrayList<String> suggestions = new ArrayList<>();
            for (String skillName : skillNames) {
                if(skillName.startsWith(arguments[1])){
                    suggestions.add(skillName);
                }
            }
            Collections.sort(suggestions);
            return suggestions;
        }
        else{
            return new ArrayList<>();
        }
    }

    private ArrayList<String> getSubCommands(){
        ArrayList<String> subCommands = new ArrayList<>();
        subCommands.add("points");
        subCommands.add("level");
        subCommands.add("stats");
        subCommands.add("info");
        subCommands.add("help");
        subCommands.add("list");
        Collections.sort(subCommands);
        return subCommands;
    }
}
