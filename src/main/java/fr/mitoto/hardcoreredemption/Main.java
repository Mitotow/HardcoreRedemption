package fr.mitoto.hardcoreredemption;

import fr.mitoto.hardcoreredemption.inventories.RedemptionInv;
import fr.mitoto.hardcoreredemption.items.RedemptionTotem;
import fr.mitoto.hardcoreredemption.listeners.EntitiesListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {
    private final Logger logger;
    private final PluginManager pm;
    private static Main instance;

    public Main() {
        this.logger = getLogger();
        this.pm = getServer().getPluginManager();
        instance = this;
    }

    @Override
    public void onEnable() {
        this.logger.info("Ready !");

        /* Adding Listeners */
        pm.registerEvents(new EntitiesListener(), this);
        pm.registerEvents(new RedemptionInv(), this);

        Bukkit.addRecipe(RedemptionTotem.getRecipe()); // Adding recipe of Redemption Totem
    }

    @Override
    public void onDisable() {
        this.logger.info("Disabled");
    }

    public static Main getPlugin() {
        return instance;
    }
}
