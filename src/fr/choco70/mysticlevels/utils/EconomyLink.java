package fr.choco70.mysticlevels.utils;

import fr.choco70.mysticlevels.MysticLevels;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyLink{

    private MysticLevels plugin = MysticLevels.getPlugin(MysticLevels.class);
    private Economy economy;

    public boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public void addMoney(Player player, Double amount){
        if(setupEconomy()){
            economy.depositPlayer(player, amount);
        }
    }

}
