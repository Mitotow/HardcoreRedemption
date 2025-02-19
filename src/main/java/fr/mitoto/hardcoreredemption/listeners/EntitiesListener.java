package fr.mitoto.hardcoreredemption.listeners;

import fr.mitoto.hardcoreredemption.Main;
import fr.mitoto.hardcoreredemption.configs.Constants;
import fr.mitoto.hardcoreredemption.configs.Messages;
import fr.mitoto.hardcoreredemption.inventories.RedemptionInventory;
import fr.mitoto.hardcoreredemption.items.RedemptionTotem;
import fr.mitoto.hardcoreredemption.utils.BlacklistManager;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class EntitiesListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (BlacklistManager.isBlacklisted(player.getUniqueId())) {
            player.kickPlayer(Messages.KICK_MESSAGE);
            e.setJoinMessage(null);
        }
    }

    /*--            onPlayerDeath event                --
     * This handler is the main handler of the plugin
     * where players are banned for their deaths.
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        int deathCount = player.getStatistic(Statistic.DEATHS)+1;
        if(deathCount%Constants.MAX_DEATHS != 0) return;

        // Fix error when player's death count was not updated
        player.setStatistic(Statistic.DEATHS, deathCount);

        // Clear everything from the player when dying
        e.setKeepLevel(false);
        e.setDroppedExp(0);
        e.setKeepInventory(false);
        e.getDrops().clear();
        player.getInventory().clear();

        // Manage player death and add the player to the blacklist
        e.setDeathMessage(String.format(Messages.DEATH_MESSAGE, player.getDisplayName()));
        BlacklistManager.addPlayerToBlacklist(player.getUniqueId());
        player.kickPlayer(Messages.KICK_MESSAGE);

        // TODO: Sound will be reworked by issue #6
        for(Player op : Main.getPlugin().getServer().getOnlinePlayers()) {
            // Player sound of wither death for each player online
            op.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 0.5f, 0f);
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
        if(e.getEntity() instanceof Player player) {
            EquipmentSlot hand = e.getHand();
            if(hand == null) return;

            ItemStack item = player.getInventory().getItem(hand);
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
        Player player = e.getPlayer();
        PlayerInventory pInv = player.getInventory();
        EquipmentSlot slot = e.getHand();
        if(slot == null) return;

        ItemStack handItem = pInv.getItem(slot);
        if(handItem == null) return;

        if(RedemptionTotem.isRedemptionTotem(handItem)) {
            player.openInventory(RedemptionInventory.createInventory(player));
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        Player player = e.getPlayer();
        if(player.isBanned() && e.getReason().startsWith("DEAD:")) e.setLeaveMessage("");
    }
}
