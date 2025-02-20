package fr.mitoto.hardcoreredemption.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Utility class for creating player head items.
 */
public class Heads {
    /**
     * Creates an {@link ItemStack} representing the head of an offline player.
     * The head item will display the player's name in yellow.
     *
     * @param p The offline player whose head should be created.
     * @return An {@link ItemStack} of a player head with the specified player's skin and name.
     */
    public static ItemStack getOfflinePlayerHead(OfflinePlayer p) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        if (skull == null) return item;

        skull.setDisplayName(ChatColor.YELLOW + p.getName());
        skull.setOwningPlayer(p);
        item.setItemMeta(skull);

        return item;
    }
}
