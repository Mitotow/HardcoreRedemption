package fr.mitoto.hardcoreredemption.listeners;

import fr.mitoto.hardcoreredemption.Main;
import fr.mitoto.hardcoreredemption.configs.Messages;
import fr.mitoto.hardcoreredemption.inventories.RedemptionInventory;
import fr.mitoto.hardcoreredemption.items.RedemptionTotem;
import fr.mitoto.hardcoreredemption.utils.BlacklistManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Handles interactions within the redemption inventory.
 * This listener ensures that players can remove blacklisted players
 * from the blacklist using the Redemption Totem.
 */
public class RedemptionInventoryListener implements Listener {

    /**
     * Handles clicks inside the redemption inventory.
     * - Prevents players from moving items inside this inventory.
     * - Detects when a player clicks on a player head.
     * - If the clicked player is blacklisted, they are removed from the blacklist.
     * - The interacting player consumes a Redemption Totem upon revival.
     * - A global message is broadcasted to announce the revival.
     * - Plays a visual and sound effect for the resurrection.
     *
     * @param e The {@link InventoryClickEvent} triggered when a player clicks in an inventory.
     */
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Inventory inventory = e.getClickedInventory();
        if (!RedemptionInventory.isRedemptionInventory(inventory)) return;

        // Cancel any modifications to the inventory
        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() != Material.PLAYER_HEAD) return;

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;

        // Get the name of the player to be revived
        String expectedName = ChatColor.stripColor(itemMeta.getDisplayName());
        Player player = (Player) e.getWhoClicked();
        Server server = Main.getPlugin().getServer();

        // Attempt to retrieve the blacklisted player
        Player blacklistedPlayer = server.getPlayer(expectedName);
        if (blacklistedPlayer == null || !BlacklistManager.isBlacklisted(blacklistedPlayer.getUniqueId())) {
            RedemptionInventory.updateInventory(inventory);
            e.setCancelled(true);
            return;
        }

        // Remove the player from the blacklist
        BlacklistManager.removePlayerFromBlacklist(blacklistedPlayer.getUniqueId());

        // Announce to the server that the player has been revived
        server.broadcast(
                String.format(Messages.REVIVE_MESSAGE, blacklistedPlayer.getDisplayName(), player.getDisplayName()),
                Server.BROADCAST_CHANNEL_USERS);

        // Close the inventory to prevent UI update issues
        player.closeInventory();

        // Remove the Redemption Totem from the player's inventory
        PlayerInventory playerInventory = player.getInventory();
        int index = playerInventory.getHeldItemSlot();
        ItemStack totem = playerInventory.getItem(index);
        if (totem != null && RedemptionTotem.isRedemptionTotem(totem)) {
            playerInventory.setItem(index, null);
        }

        // Play sound and effect
        player.playEffect(EntityEffect.TOTEM_RESURRECT);
        // TODO: Sound will be reworked by issue #6
        server.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 0f));
    }
}
