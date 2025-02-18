package fr.mitoto.hardcoreredemption.configs;

import org.bukkit.ChatColor;

public class Messages {
    // Global
    public static final String ON_ENABLE_MESSAGE = "HardcoreRedemption is enable.";
    public static final String ON_DISABLE_MESSAGE = "HardcoreRedemption is disable.";

    // Blacklist Manager
    public final static String ERROR_READ_DATA_MESSAGE = String.format("Failed to load %s : ", Constants.BLACKLIST_PATH) + "%s";
    public final static String ERROR_WRITE_DATA_MESSAGE = String.format("Failed to write in %s : ", Constants.BLACKLIST_PATH) + "%s";
    public final static String ERROR_CREATE_DATA_FILE_MESSAGE = String.format("Failed to create %s : ", Constants.BLACKLIST_PATH) + "%s";
    public final static String DATA_FILE_ALREADY_EXISTS = String.format("Failed to create %s : File already exists", Constants.BLACKLIST_PATH);

    // On revive
    public static final String REVIVE_MESSAGE = ChatColor.GREEN
            + "%s"
            + ChatColor.WHITE
            + " has been revived by "
            + ChatColor.AQUA
            + "%s";

    // On death
    public static final String DEATH_MESSAGE = ChatColor.RED + "%s" + ChatColor.WHITE + " is dead ... Craft a Totem of Redemption to revive him.";
    public static final String KICK_MESSAGE = "DEAD: You are dead, maybe someone will help you ...";
}
