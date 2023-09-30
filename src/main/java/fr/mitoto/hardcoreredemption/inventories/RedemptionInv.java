package fr.mitoto.hardcoreredemption.inventories;

import fr.mitoto.hardcoreredemption.Main;
import fr.mitoto.hardcoreredemption.items.Heads;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RedemptionInv implements Listener {

    private static Inventory inv = null;

    public static Inventory getInventory() {
        if(inv == null) inv = Bukkit.createInventory(null, 45, "Chose a player to forgive");
        updateInv();
        return inv;
    }

    public static void updateInv() {
        if(inv == null) return;
        inv.clear();
        ItemStack borderItem = new ItemStack(Material.GREEN_STAINED_GLASS);
        boolean setItem;
        List<Integer> rBorder = List.of(17, 26, 35);
        for(int i = 0; i<inv.getSize(); i++) {
            setItem = (i <= 9 || i>= 36 || i%9 == 0 || rBorder.contains(i));
            if(setItem) inv.setItem(i, borderItem);
        }
        Set<OfflinePlayer> bans = Main.getPlugin().getServer().getBannedPlayers();
        for(OfflinePlayer player : bans) {
            ItemStack head = Heads.getOfflinePlayerHead(player);
            for(int i = 0; i<inv.getSize()-1; i++) {
                if(inv.getItem(i) == null) {
                    inv.setItem(i, head);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if(!e.getInventory().equals(inv)) return;
        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        if(item==null || item.getType()!=Material.PLAYER_HEAD) return;

        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) return;

        String expectedName = ChatColor.stripColor(itemMeta.getDisplayName());  // Get expected Player Name to found
        Player player = (Player) e.getWhoClicked();
        Server server = Main.getPlugin().getServer();

        OfflinePlayer bannedPlayer = null;
        for(OfflinePlayer ofPlayer : server.getBannedPlayers()) {
            if(!ofPlayer.isBanned()) continue; // Player is not ban;
            if(Objects.equals(ofPlayer.getName(), expectedName)) {
                // Banned player found
                bannedPlayer = ofPlayer;
                break;
            }
        }

        if(bannedPlayer == null) return;                                            // Banned Player not found
        BanList<PlayerProfile> banList = server.getBanList(BanList.Type.PROFILE);   // Get list of banned players
        banList.pardon(bannedPlayer.getPlayerProfile());                            // Unban banned player

        /* Broadcast a message to tell everyone that player is returned */
        server.broadcast(
                ChatColor.GREEN
                + bannedPlayer.getName()
                + ChatColor.WHITE
                + " has been revived by "
                + ChatColor.AQUA
                + player.getDisplayName()
        , Server.BROADCAST_CHANNEL_USERS);

        player.closeInventory();                                                    // Close inventory (to avoid update inventory problems)

        /* Remove Totem */
        PlayerInventory pinv = player.getInventory();
        int index = pinv.getHeldItemSlot();
        ItemStack totem = pinv.getItem(index);
        if(totem != null) {
            pinv.remove(totem);
        }

        player.playEffect(EntityEffect.TOTEM_RESURRECT);                            // Play totem resurrect effect

        for(Player p : server.getOnlinePlayers()) {                                 // Player sound of wither spawn for each player online
            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 0f);
        }
    }

}
