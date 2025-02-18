package fr.mitoto.hardcoreredemption.listeners;

import fr.mitoto.hardcoreredemption.Main;
import fr.mitoto.hardcoreredemption.configs.Constants;
import fr.mitoto.hardcoreredemption.inventories.RedemptionInv;
import fr.mitoto.hardcoreredemption.items.RedemptionTotem;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Date;

public class EntitiesListener implements Listener {

    /*--            onPlayerDeath event                --
     * This handler is the main handler of the plugin
     * where players are banned for their deaths.
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        int deathCount = p.getStatistic(Statistic.DEATHS)+1;
        if(deathCount%Constants.MAX_DEATHS != 0) return;

        // Fix error when player get banned on death, doesn't update his death count
        p.setStatistic(Statistic.DEATHS, deathCount);

        // Clear everything from the player when dying
        e.setKeepLevel(false);
        e.setDroppedExp(0);
        e.setKeepInventory(false);
        e.getDrops().clear();
        p.getInventory().clear();

        // TODO: Messages will be reworked by issue #4
        e.setDeathMessage(ChatColor.RED + p.getDisplayName() + ChatColor.WHITE + " is dead ...");
        p.ban("DEAD: You are dead, maybe someone will help you ...", (Date) null, p.getName(), true);

        // TODO: Sound will be reworked by issue #6
        for(Player op : Main.getPlugin().getServer().getOnlinePlayers()) {
            // Player sound of wither death for each player online
            op.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 0.5f, 0f);
        }
    }

    /*--            onEntityResurrect event            --
     * This event is used to disable the resurrection
     * effect when a totem of undying is used if a player
     * is using a Redemption Totem.
     */
    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent e) {
        // Avoid Totem_Of_Undying effect when holding RedemptionTotem
        if(e.getEntity() instanceof Player p) {
            EquipmentSlot hand = e.getHand();
            if(hand == null) return;

            ItemStack item = p.getInventory().getItem(hand);
            if(item == null) return;

            e.setCancelled(RedemptionTotem.isRedemptionTotem(item));
        }
    }

    /*--            onPlayerInteract event            --
    * This event is used to see if a player is using the
    * Redemption Totem and open a custom inventory if
    * he's holding a totem.
    */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        PlayerInventory pInv = p.getInventory();
        EquipmentSlot slot = e.getHand();
        if(slot == null) return;

        ItemStack handItem = pInv.getItem(slot);
        if(handItem == null) return;

        if(RedemptionTotem.isRedemptionTotem(handItem)) {
            p.openInventory(RedemptionInv.createInventory());
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        if(p.isBanned() && e.getReason().startsWith("DEAD:")) e.setLeaveMessage("");
    }
}
