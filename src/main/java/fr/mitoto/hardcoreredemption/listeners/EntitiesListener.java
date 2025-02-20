package fr.mitoto.hardcoreredemption.listeners;

import fr.mitoto.hardcoreredemption.Main;
import fr.mitoto.hardcoreredemption.configs.Constants;
import fr.mitoto.hardcoreredemption.configs.Messages;
import fr.mitoto.hardcoreredemption.inventories.RedemptionInventory;
import fr.mitoto.hardcoreredemption.items.RedemptionTotem;
import fr.mitoto.hardcoreredemption.utils.BlacklistManager;
import fr.mitoto.hardcoreredemption.utils.SoundUtils;
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

/**
 * Handles various player-related events such as deaths, resurrections, interactions, and joins.
 * This class ensures the correct functionality of the hardcore redemption mechanics.
 */
public class EntitiesListener implements Listener {
    /**
     * Handles player join events.
     * If a player is blacklisted, they are automatically kicked from the server.
     *
     * @param e The {@link PlayerJoinEvent} triggered when a player joins the server.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (BlacklistManager.isBlacklisted(player.getUniqueId())) {
            player.kickPlayer(Messages.KICK_MESSAGE);
            e.setJoinMessage(null);
        }
    }

    /**
     * Handles player deaths and bans them when their death count reaches the configured threshold.
     * - The player loses all inventory and experience upon death.
     * - The player is blacklisted when reaching the max death limit.
     * - A global death message is broadcasted.
     * - All online players hear a wither death sound effect.
     *
     * @param e The {@link PlayerDeathEvent} triggered when a player dies.
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        int deathCount = player.getStatistic(Statistic.DEATHS) + 1;
        if (deathCount % Constants.MAX_DEATHS != 0) return;

        // Ensure the death count is correctly updated
        player.setStatistic(Statistic.DEATHS, deathCount);

        // Remove all player inventory and experience upon death
        e.setKeepLevel(false);
        e.setDroppedExp(0);
        e.setKeepInventory(false);
        e.getDrops().clear();
        player.getInventory().clear();

        // Blacklist the player and kick them from the server
        e.setDeathMessage(String.format(Messages.DEATH_MESSAGE, player.getDisplayName()));
        BlacklistManager.addPlayerToBlacklist(player.getUniqueId());
        player.kickPlayer(Messages.KICK_MESSAGE);
        SoundUtils.broadcastSound(Sound.ENTITY_WITHER_DEATH, 0.5f, 0f);
    }

    /**
     * Handles entity resurrection events to prevent the use of normal totems
     * when a player is holding a {@link RedemptionTotem}.
     *
     * @param e The {@link EntityResurrectEvent} triggered when an entity attempts to resurrect.
     */
    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent e) {
        if (e.getEntity() instanceof Player player) {
            EquipmentSlot hand = e.getHand();
            if (hand == null) return;

            ItemStack item = player.getInventory().getItem(hand);
            if (item == null) return;

            e.setCancelled(RedemptionTotem.isRedemptionTotem(item));
        }
    }

    /**
     * Handles player interactions to detect when they use a {@link RedemptionTotem}.
     * If the player is holding a Redemption Totem, it opens the custom redemption inventory.
     *
     * @param e The {@link PlayerInteractEvent} triggered when a player interacts.
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        PlayerInventory pInv = player.getInventory();
        EquipmentSlot slot = e.getHand();
        if (slot == null) return;

        ItemStack handItem = pInv.getItem(slot);
        if (handItem == null) return;

        if (RedemptionTotem.isRedemptionTotem(handItem)) {
            player.openInventory(RedemptionInventory.createInventory(player));
        }
    }

    /**
     * Handles player kick events.
     * If a player is banned due to death, their leave message is suppressed.
     *
     * @param e The {@link PlayerKickEvent} triggered when a player is kicked.
     */
    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        Player player = e.getPlayer();
        if (player.isBanned() && e.getReason().startsWith("DEAD:")) {
            e.setLeaveMessage("");
        }
    }
}
