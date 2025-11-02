package fr.mitoto.hardcoreredemption.utils;

import fr.mitoto.hardcoreredemption.Main;
import fr.mitoto.hardcoreredemption.configs.Constants;
import fr.mitoto.hardcoreredemption.items.Heads;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

/**
 * Utility class for working with Minecraft inventories.
 * This class contains methods that help to manipulate and query items in an inventory.
 */
public class InventoryUtils {
    private static ItemStack previousPaginationItem = null;
    private static ItemStack nextPaginationItem = null;
    private static ItemStack previousDisabledPaginationItem = null;
    private static ItemStack nextDisabledPaginationItem = null;

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

    public static InventoryPaginator<ItemStack> getInventoryPaginator() {
        final InventoryPaginator<ItemStack> paginator = new InventoryPaginator<>(Constants.MAX_HEADS_PER_INVENTORY);
        /*BlacklistManager.getBlacklist().forEach(uuid -> {
            Player player = Main.getPlugin().getServer().getPlayer(uuid);

           if (player != null) {
                ItemStack head = Heads.getOfflinePlayerHead(player);
                paginator.add(head);
            } else {
                Bukkit.getLogger().warning("Player " + uuid + " not found");
            }
        });*/

        for (int i = 0; i < 50; i++) {
            paginator.add(new ItemStack(Material.DIAMOND_SWORD));
        }

        return paginator;
    }

    private static ItemStack createPaginationBorderItem(Material material, String name) {
        final ItemStack itemStack = new ItemStack(material);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;

        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ItemStack getNextBorderItem(boolean disabled) {
        if (disabled) {
            if (nextDisabledPaginationItem == null) {
                nextDisabledPaginationItem = createPaginationBorderItem(Material.GRAY_BANNER, Constants.PAGINATION_NEXT);
            }

            return nextDisabledPaginationItem;
        }

        if (nextPaginationItem == null) {
            nextPaginationItem = createPaginationBorderItem(Material.GREEN_BANNER, Constants.PAGINATION_NEXT);
        }

        return nextPaginationItem;
    }

    public static ItemStack getPreviousBorderItem(boolean disabled) {
        if (disabled) {
            if (previousDisabledPaginationItem == null) {
                previousDisabledPaginationItem = createPaginationBorderItem(Material.GRAY_BANNER, Constants.PAGINATION_PREVIOUS);
            }

            return previousDisabledPaginationItem;
        }

        if (previousPaginationItem == null) {
            previousPaginationItem = createPaginationBorderItem(Material.GREEN_BANNER, Constants.PAGINATION_PREVIOUS);
        }

        return previousPaginationItem;
    }
}
