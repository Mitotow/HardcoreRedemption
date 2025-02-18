package fr.mitoto.hardcoreredemption.utils;

import fr.mitoto.hardcoreredemption.Main;
import org.bukkit.BanList;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.profile.PlayerProfile;

import java.util.Objects;
import java.util.Optional;

/**
 * Utility class for managing banned players in a Spigot server.
 */
public class BanUtils {
    private static final Server server = Main.getPlugin().getServer();

    /**
     * Finds a banned player by their name.
     *
     * @param name The name of the player to search for.
     * @return An {@code Optional} containing the banned {@code OfflinePlayer} if found, otherwise an empty {@code Optional}.
     */
    public static Optional<OfflinePlayer> findBannedPlayerByName(String name) {
        return server.getBannedPlayers()
                .stream()
                .filter(p -> Objects.equals(p.getName(), name))
                .findFirst();
    }

    /**
     * Unbans a player from the server.
     *
     * @param player The {@code OfflinePlayer} to unban.
     */
    public static void unbanPlayer(OfflinePlayer player) {
        BanList<PlayerProfile> banList = server.getBanList(BanList.Type.PROFILE);
        banList.pardon(player.getPlayerProfile());
    }

    /**
     * Finds a banned player by name and unbans them if they exist.
     *
     * @param name The name of the player to search for and unban.
     * @return {@code true} if the player was found and unbanned, {@code false} otherwise.
     */
    public static boolean findAndUnbanPlayer(String name) {
        Optional<OfflinePlayer> player = findBannedPlayerByName(name);
        if (player.isPresent()) {
            unbanPlayer(player.get());
            return true;
        }

        return false;
    }
}
