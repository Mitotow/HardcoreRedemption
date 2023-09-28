package fr.mitoto.hardcoreredemption.listeners;

import fr.mitoto.hardcoreredemption.items.RedemptionTotem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

public class Entities implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        e.setDroppedExp(0);     // No levels drop
        e.setKeepLevel(false);  // Player don't keep levels
        e.getDrops().clear();   // Clear player inventory
        e.setDeathMessage(ChatColor.RED + p.getDisplayName() + ChatColor.WHITE + ChatColor.BOLD + " is dead ...");
        p.ban("You are dead, maybe someone will help you ...", (Date) null, p.getName(), true);
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent e) {
        /* Avoid Totem_Of_Undying effect when holding RedemptionTotem */
        if(e.getEntity() instanceof Player p) {
            EquipmentSlot slot = e.getHand();
            if(slot == null) return;
            ItemStack item = p.getInventory().getItem(slot);
            if(item == null) return;
            e.setCancelled(RedemptionTotem.isRedemptionTotem(item));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage(ChatColor.GOLD + p.getDisplayName() + ChatColor.WHITE + " joins the game !");
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.setQuitMessage(ChatColor.GOLD + p.getDisplayName() + ChatColor.WHITE + " quit the game !");
    }
}
