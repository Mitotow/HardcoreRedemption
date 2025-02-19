package fr.mitoto.hardcoreredemption.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Utility class for working with Minecraft inventories.
 * This class contains methods that help to manipulate and query items in an inventory.
 */
public class InventoryUtils {

    /**
     * Finds the slot index of a specific item in an inventory.
     * The item is considered found if it is similar to the provided item (using the {@link ItemStack#isSimilar(ItemStack)} method).
     *
     * @param inventory The inventory to search through.
     * @param item The {@link ItemStack} to find in the inventory.
     * @return The index of the slot where the item is found, or -1 if the item is not in the inventory.
     */
    public static int getItemSlot(Inventory inventory, ItemStack item) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack inInventoryItem = inventory.getItem(i);
            if (inInventoryItem != null && inInventoryItem.isSimilar(item)) {
                return i;
            }
        }

        return -1;
    }
}
