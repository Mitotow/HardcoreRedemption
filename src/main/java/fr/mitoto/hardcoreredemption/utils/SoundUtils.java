package fr.mitoto.hardcoreredemption.utils;

import fr.mitoto.hardcoreredemption.Main;
import org.bukkit.Server;
import org.bukkit.Sound;

/** Utility class for handling sound effects in the plugin. */
public class SoundUtils {
    private static final Server server = Main.getPlugin().getServer();

    /**
     * Broadcasts a sound to all online players.
     *
     * @param sound  The sound to play.
     */
    public static void broadcastSound(Sound sound) {
        server.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), sound, 0.5f, 0f));
    }

    /**
     * Broadcasts a sound to all online players.
     *
     * @param sound  The sound to play.
     * @param volume The volume of the sound.
     * @param pitch  The pitch of the sound.
     */
    public static void broadcastSound(Sound sound, float volume, float pitch) {
        server.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
    }
}
