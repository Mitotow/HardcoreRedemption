package fr.mitoto.hardcoreredemption.inventories;

import fr.mitoto.hardcoreredemption.Main;
import fr.mitoto.hardcoreredemption.configs.Constants;
import fr.mitoto.hardcoreredemption.items.Heads;
import fr.mitoto.hardcoreredemption.utils.BlacklistManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Manages the redemption inventory, which displays a list of blacklisted players.
 * This inventory allows players to interact with the redemption system.
 */
public class RedemptionInventory {
    public static ItemStack borderItem = new ItemStack(Material.GREEN_STAINED_GLASS);
    private static final HashMap<UUID, Inventory> inventories = new HashMap<>();

    /**
     * Creates or retrieves the redemption inventory for a given player.
     * If the player already has an inventory, it is returned; otherwise, a new one is created.
     *
     * @param player The player for whom the inventory is created.
     * @return The redemption inventory associated with the player.
     */
    public static Inventory createInventory(Player player) {
        if (inventories.containsKey(player.getUniqueId())) {
            return inventories.get(player.getUniqueId());
        }

        Inventory inventory = Bukkit.createInventory(null, 45, Constants.REDEMPTION_INVENTORY_TITLE);
        setBorders(inventory);
        updateInventory(inventory);

        inventories.put(player.getUniqueId(), inventory);
        return inventory;
    }

    /**
     * Sets the borders of the inventory with the defined border item.
     * The borders are placed around the edges of the inventory.
     *
     * @param inventory The inventory in which the borders are set.
     */
    private static void setBorders(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (i <= 8 || (i >= 36 && i <= 44) || i % 9 == 0 || (i + 1) % 9 == 0) {
                inventory.setItem(i, borderItem);
            }
        }
    }

    /**
     * Places the player's head in the first available slot in the inventory.
     *
     * @param inventory The inventory in which the player's head will be placed.
     * @param player The offline player whose head is added.
     */
    private static void placeHeadInInventory(Inventory inventory, OfflinePlayer player) {
        ItemStack head = Heads.getOfflinePlayerHead(player);
        for (int i = 0; i < inventory.getSize() - 1; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, head);
                break;
            }
        }
    }

    /**
     * Updates the inventory by clearing it, setting the borders, and adding
     * heads of blacklisted players.
     *
     * @param inventory The inventory to update.
     */
    public static void updateInventory(Inventory inventory) {
        inventory.clear();
        setBorders(inventory);
        Server server = Main.getPlugin().getServer();
        BlacklistManager.getBlacklist().forEach(uuid -> {
            Player player = server.getPlayer(uuid);
            if (player != null) {
                placeHeadInInventory(inventory, player);
            } else {
                Bukkit.getLogger().warning("Player " + uuid + " not found");
            }
        });
    }

    /**
     * Checks if the given inventory is a redemption inventory.
     *
     * @param inventory The inventory to check.
     * @return {@code true} if the inventory is a redemption inventory, {@code false} otherwise.
     */
    public static boolean isRedemptionInventory(Inventory inventory) {
        return inventory != null && inventories.containsValue(inventory);
    }
}
