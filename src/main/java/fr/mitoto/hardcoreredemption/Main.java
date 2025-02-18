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
    private static Main plugin;

    public Main() {
        this.logger = getLogger();
        this.pm = getServer().getPluginManager();
        plugin = this;
    }

    @Override
    public void onEnable() {
        this.logger.info("Ready !");

        this.registerListeners();
        this.registerRecipes();
    }

    @Override
    public void onDisable() {
        this.logger.info("Disabled");
    }

    /** Registers all event listeners for the plugin. */
    private void registerListeners() {
        pm.registerEvents(new EntitiesListener(), this);
        pm.registerEvents(new RedemptionInv(), this);
    }

    /** Registers all custom crafting recipes for the plugin. */
    private void registerRecipes() {
        Bukkit.addRecipe(RedemptionTotem.getRecipe());
    }

    public static Main getPlugin() {
        return plugin;
    }
}
