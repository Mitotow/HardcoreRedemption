package fr.mitoto.hardcoreredemption.inventories;

import fr.mitoto.hardcoreredemption.Main;
import fr.mitoto.hardcoreredemption.configs.Constants;
import fr.mitoto.hardcoreredemption.configs.Messages;
import fr.mitoto.hardcoreredemption.items.Heads;
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

import java.util.Objects;

public class RedemptionInv implements Listener {
    public static Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, 45, Constants.REDEMPTION_INVENTORY_TITLE);
        setInventoryBorders(inventory);
        updateInventory(inventory);

        return inventory;
    }

    private static void setInventoryBorders(Inventory inventory) {
        ItemStack borderItem = new ItemStack(Material.GREEN_STAINED_GLASS);
        for(int i = 0; i<inventory.getSize(); i++) {
            if(i <= 8 || (i >= 36 && i <= 44) || i % 9 == 0 || (i + 1) % 9 == 0) {
                inventory.setItem(i, borderItem);
            }
        }
    }

    private static void placeHeadInInventory(Inventory inventory, OfflinePlayer player) {
        ItemStack head = Heads.getOfflinePlayerHead(player);
        for(int i = 0; i<inventory.getSize()-1; i++) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, head);
                break;
            }
        }
    }

    private static void updateInventory(Inventory inventory) {
        inventory.remove(new ItemStack(Material.PLAYER_HEAD));
        Server server = Main.getPlugin().getServer();
        BlacklistManager.getBlacklist().forEach(uuid -> {
            Player player = server.getPlayer(uuid);
            if (player != null)
                placeHeadInInventory(inventory, player);
            else Bukkit.getLogger().warning("Player " + uuid + " not found");
        });
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if(!e.getView().getTitle().equals(Constants.REDEMPTION_INVENTORY_TITLE)) return;
        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        if(item==null || item.getType()!=Material.PLAYER_HEAD) return;

        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) return;

        String expectedName = ChatColor.stripColor(itemMeta.getDisplayName());
        Player player = (Player) e.getWhoClicked();
        Server server = Main.getPlugin().getServer();

        // Remove blacklisted player from the blacklist
        Player blacklistedPlayer = server.getPlayer(expectedName);
        if (blacklistedPlayer == null || !BlacklistManager.isBlacklisted(blacklistedPlayer.getUniqueId())) {
            updateInventory(Objects.requireNonNull(e.getClickedInventory()));
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
