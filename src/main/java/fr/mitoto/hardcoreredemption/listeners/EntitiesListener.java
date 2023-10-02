package fr.mitoto.hardcoreredemption.listeners;

import fr.mitoto.hardcoreredemption.Main;
import fr.mitoto.hardcoreredemption.inventories.RedemptionInv;
import fr.mitoto.hardcoreredemption.items.RedemptionTotem;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Date;

public class EntitiesListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        e.setDroppedExp(0);     // No levels drop
        e.setKeepLevel(false);  // Player don't keep levels
        e.getDrops().clear();   // Clear player inventory
        e.setDeathMessage(ChatColor.RED + p.getDisplayName() + ChatColor.WHITE + " is dead ...");
        p.ban("You are dead, maybe someone will help you ...", (Date) null, p.getName(), true);
        for(Player op : Main.getPlugin().getServer().getOnlinePlayers()) {
            // Player sound of wither death for each player online
            op.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 0.5f, 0f);
        }
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
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        PlayerInventory pInv = p.getInventory();
        EquipmentSlot slot = e.getHand();
        if(slot == null) return;
        ItemStack handItem = pInv.getItem(slot);
        if(handItem == null) return;
        if(RedemptionTotem.isRedemptionTotem(handItem)) {
            p.openInventory(new RedemptionInv().getInventory());
        }
    }
}
