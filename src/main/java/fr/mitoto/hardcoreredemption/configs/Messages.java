package fr.mitoto.hardcoreredemption.configs;

import org.bukkit.ChatColor;

public class Messages {
    // Global
    public static final String ON_ENABLE_MESSAGE = "HardcoreRedemption is enable.";
    public static final String ON_DISABLE_MESSAGE = "HardcoreRedemption is disable.";

    // On revive
    public static final String REVIVE_MESSAGE = ChatColor.GREEN
            + "%s"
            + ChatColor.WHITE
            + " has been revived by "
            + ChatColor.AQUA
            + "%s";

    // On death
    public static final String DEATH_MESSAGE = ChatColor.RED + "%s" + ChatColor.WHITE + " is dead ... Craft a Totem of Redemption to revive him.";
    public static final String BAN_MESSAGE = "DEAD: You are dead, maybe someone will help you ...";
}
