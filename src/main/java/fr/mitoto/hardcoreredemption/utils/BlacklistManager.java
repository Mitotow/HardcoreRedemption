package fr.mitoto.hardcoreredemption.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.mitoto.hardcoreredemption.Main;
import fr.mitoto.hardcoreredemption.configs.Constants;
import fr.mitoto.hardcoreredemption.configs.Messages;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Manages a blacklist of players using UUIDs.
 * The blacklist is stored in a JSON file and persists between server restarts.
 */
public class BlacklistManager {
    private static final HashSet<UUID> blacklist = new HashSet<>();
    private static final Plugin plugin = Main.getPlugin();
    private static final Path FILE_PATH = plugin.getDataFolder().toPath().resolve(Constants.BLACKLIST_PATH);
    private static final Gson gson = new Gson();
    private static boolean inMemory = false;

    /**
     * Creates the blacklist data file if it does not already exist.
     * If the file is successfully created, it is initialized as empty.
     * If the file already exists, a log message is recorded at the fine level.
     * If an error occurs during file creation, a severe log message is recorded.
     */
    private static void createFile() {
        if (inMemory) return;

        try {
            Files.createDirectories(FILE_PATH.getParent());
            Files.createFile(FILE_PATH);
            saveData();
        } catch (FileAlreadyExistsException e) {
            Bukkit.getLogger().severe(Messages.DATA_FILE_ALREADY_EXISTS);
        } catch (IOException e) {
            Bukkit.getLogger().severe(String.format(Messages.ERROR_CREATE_DATA_FILE_MESSAGE, e.getMessage()));
        }
    }

    /**
     * Saves the blacklist data to the file defined by {@link Constants#BLACKLIST_PATH}.
     * If an error occurs while writing, a severe error message is logged.
     */
    public static void saveData() {
        if (inMemory) return;

        try (Writer writer = new FileWriter(FILE_PATH.toFile())) {
            gson.toJson(blacklist, writer);
        } catch (IOException e) {
            Bukkit.getLogger().severe(String.format(Messages.ERROR_WRITE_DATA_MESSAGE, e.getMessage()));
        }
    }

    /**
     * Loads the blacklist data from the file defined by {@link Constants#BLACKLIST_PATH}.
     * If the file does not exist, it is created with an empty blacklist.
     * If an error occurs while reading, a severe error message is logged.
     */
    public static void loadData() {
        if (inMemory) return;

        if (!Files.exists(FILE_PATH)) {
            createFile();
            return;
        }

        try (Reader reader = new FileReader(FILE_PATH.toFile())) {
            Type type = new TypeToken<HashSet<UUID>>() {}.getType();
            Set<UUID> set = gson.fromJson(reader, type);
            if (set != null) {
                blacklist.clear();
                blacklist.addAll(set);
            }
        } catch (IOException e) {
            Bukkit.getLogger().severe(String.format(Messages.ERROR_READ_DATA_MESSAGE, e.getMessage()));
        }
    }

    public static void setInMemory(boolean value) {
        inMemory = value;
    }

    public static HashSet<UUID> getBlacklist() {
        return blacklist;
    }

    /**
     * Adds a player's UUID to the blacklist.
     * If the UUID was not already in the blacklist, the data is saved.
     *
     * @param uuid The UUID of the player to blacklist.
     */
    public static void addPlayerToBlacklist(UUID uuid) {
        if (blacklist.add(uuid)) {
            saveData();
        }
    }

    /**
     * Removes a player's UUID from the blacklist.
     * If the UUID was in the blacklist, the data is saved.
     *
     * @param uuid The UUID of the player to remove from the blacklist.
     */
    public static void removePlayerFromBlacklist(UUID uuid) {
        if (blacklist.remove(uuid)) {
            saveData();
        }
    }

    /**
     * Checks if a player's UUID is in the blacklist.
     *
     * @param uuid The UUID of the player to check.
     * @return {@code true} if the player is blacklisted, {@code false} otherwise.
     */
    public static boolean isBlacklisted(UUID uuid) {
        return blacklist.contains(uuid);
    }
}
