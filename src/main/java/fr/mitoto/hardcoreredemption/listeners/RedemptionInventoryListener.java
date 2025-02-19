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

public class RedemptionInventoryListener implements Listener {
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Inventory inventory = e.getClickedInventory();
        if(!RedemptionInventory.isRedemptionInventory(inventory)) return;
        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        if(item==null || item.getType()!= Material.PLAYER_HEAD) return;

        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) return;

        String expectedName = ChatColor.stripColor(itemMeta.getDisplayName());
        Player player = (Player) e.getWhoClicked();
        Server server = Main.getPlugin().getServer();

        // Remove blacklisted player from the blacklist
        Player blacklistedPlayer = server.getPlayer(expectedName);
        if (blacklistedPlayer == null || !BlacklistManager.isBlacklisted(blacklistedPlayer.getUniqueId())) {
            RedemptionInventory.updateInventory(inventory);
            e.setCancelled(true);
            return;
        }
        BlacklistManager.removePlayerFromBlacklist(blacklistedPlayer.getUniqueId());

        // Broadcast a message to tell everyone that player is returned
        server.broadcast(
                String.format(Messages.REVIVE_MESSAGE, blacklistedPlayer.getDisplayName(), player.getDisplayName()),
                Server.BROADCAST_CHANNEL_USERS);

        // Close inventory (to avoid update inventory problems)
        player.closeInventory();

        // Remove totem from player's inventory
        PlayerInventory playerInventory = player.getInventory();
        int index = playerInventory.getHeldItemSlot();
        ItemStack totem = playerInventory.getItem(index);
        if(totem != null && RedemptionTotem.isRedemptionTotem(totem)) {
            playerInventory.setItem(index, null);
        }

        // Play totem resurrect effect
        player.playEffect(EntityEffect.TOTEM_RESURRECT);

        // TODO: Sound will be reworked by issue #6
        // Player sound of wither spawn for each player online
        server.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 0f));
    }
}
