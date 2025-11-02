package fr.mitoto.hardcoreredemption.inventories;

import fr.mitoto.hardcoreredemption.configs.Constants;
import fr.mitoto.hardcoreredemption.utils.InventoryPaginator;
import fr.mitoto.hardcoreredemption.utils.InventoryUtils;
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
    private static final HashMap<UUID, RedemptionInventory> inventories = new HashMap<>();
    private final Inventory inventory;
    private InventoryPaginator<ItemStack> paginator;

    public RedemptionInventory(Player player) {
        this.paginator = InventoryUtils.getInventoryPaginator();
        this.inventory = Bukkit.createInventory(player, 45, formatInventoryTitle());
        updateInventory();
    }

    public String formatInventoryTitle() {
        return String.format(Constants.REDEMPTION_INVENTORY_TITLE, paginator.getActualPageNumber(), paginator.size());
    }

    /**
     * Return the inventory
     *
     * @return inventory
     */
    public Inventory getInventory() {
        return this.inventory;
    }

    public void nextPage() {
        paginator.next();
        updateInventory();
    }

    public void previousPage() {
        paginator.previous();
        updateInventory();
    }

    /**
     * Creates or retrieves the redemption inventory for a given player.
     * If the player already has an inventory, it is returned; otherwise, a new one is created.
     *
     * @param player The player for whom the inventory is created.
     * @return The redemption inventory associated with the player.
     * @throws IllegalStateException Player does not have a redemption inventory
     */
    public static RedemptionInventory getRedemptionInventory(Player player) {
        if (inventories.containsKey(player.getUniqueId())) {
            return inventories.get(player.getUniqueId());
        }

        final RedemptionInventory redemptionInventory = new RedemptionInventory(player);

        inventories.put(player.getUniqueId(), redemptionInventory);
        return redemptionInventory;
    }

    /**
     * Sets the borders of the inventory with the defined border item.
     * The borders are placed around the edges of the inventory.
     */
    private void setBorders() {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (i <= 8 || (i >= 36 && i <= 44) || i % 9 == 0 || (i + 1) % 9 == 0) {
                inventory.setItem(i, borderItem);
            }
        }

        inventory.setItem(39, InventoryUtils.getPreviousBorderItem(!this.paginator.hasPrevious()));
        inventory.setItem(41, InventoryUtils.getNextBorderItem(!this.paginator.hasNext()));
    }

    /**
     * Places the player's head in the first available slot in the inventory.
     *
     * @param head The item stack
     */
    private void placeHeadInInventory(ItemStack head) {
        for (int i = 0; i < inventory.getSize() - 1; i++) {
            if (this.inventory.getItem(i) == null) {
                this.inventory.setItem(i, head);
                break;
            }
        }
    }

    public void fullUpdateInventory() {
        this.paginator = InventoryUtils.getInventoryPaginator();
        updateInventory();
    }

    /**
     * Updates the inventory by clearing it, setting the borders, and adding
     * heads of blacklisted players.
     */
    public void updateInventory() {
        this.inventory.clear();
        this.paginator.getActual().forEach(this::placeHeadInInventory);
        setBorders();
    }

    /**
     * Checks if the given inventory is a redemption inventory.
     *
     * @param inventory The inventory to check.
     * @return {@code true} if the inventory is a redemption inventory, {@code false} otherwise.
     */
    public static boolean isRedemptionInventory(Player player, Inventory inventory) {
        if (inventory == null) return false;

        RedemptionInventory redemptionInventory = inventories.get(player.getUniqueId());
        if (redemptionInventory == null) return false;

        return redemptionInventory.getInventory().equals(inventory);
    }
}
