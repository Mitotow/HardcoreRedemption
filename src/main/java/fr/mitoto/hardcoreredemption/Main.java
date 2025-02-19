package fr.mitoto.hardcoreredemption;

import fr.mitoto.hardcoreredemption.configs.Messages;
import fr.mitoto.hardcoreredemption.items.RedemptionTotem;
import fr.mitoto.hardcoreredemption.listeners.EntitiesListener;
import fr.mitoto.hardcoreredemption.listeners.RedemptionInventoryListener;
import fr.mitoto.hardcoreredemption.utils.BlacklistManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {
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
        BlacklistManager.loadData();

        this.registerListeners();
        this.registerRecipes();

        this.logger.info(Messages.ON_ENABLE_MESSAGE);
    }

    @Override
    public void onDisable() {
        BlacklistManager.saveData();

        this.logger.info(Messages.ON_DISABLE_MESSAGE);
    }

    /** Registers all event listeners for the plugin. */
    private void registerListeners() {
        pm.registerEvents(new EntitiesListener(), this);
        pm.registerEvents(new RedemptionInventoryListener(), this);
    }

    /** Registers all custom crafting recipes for the plugin. */
    private void registerRecipes() {
        Bukkit.addRecipe(RedemptionTotem.getRecipe());
    }

    public static Main getPlugin() {
        return plugin;
    }
}
