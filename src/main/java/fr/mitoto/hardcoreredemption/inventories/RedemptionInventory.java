package fr.mitoto.hardcoreredemption.inventories;

import fr.mitoto.hardcoreredemption.Main;
import fr.mitoto.hardcoreredemption.configs.Constants;
import fr.mitoto.hardcoreredemption.items.Heads;
import fr.mitoto.hardcoreredemption.utils.BlacklistManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class RedemptionInventory {
    public static ItemStack borderItem = new ItemStack(Material.GREEN_STAINED_GLASS);
    private static final HashMap<UUID, Inventory> inventories = new HashMap<>();

    public static Inventory createInventory(Player player) {
        if (inventories.containsKey(player.getUniqueId())) {
            return inventories.get(player.getUniqueId());
        }

        Inventory inventory = Bukkit.createInventory(null, 45, Constants.REDEMPTION_INVENTORY_TITLE);
        setBorders(inventory);
        updateInventory(inventory);

        inventories.put(player.getUniqueId(), inventory);

        return inventory;
    }

    private static void setBorders(Inventory inventory) {
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

    public static void updateInventory(Inventory inventory) {
        inventory.clear();
        setBorders(inventory);
        Server server = Main.getPlugin().getServer();
        BlacklistManager.getBlacklist().forEach(uuid -> {
            Player player = server.getPlayer(uuid);
            if (player != null)
                placeHeadInInventory(inventory, player);
            else Bukkit.getLogger().warning("Player " + uuid + " not found");
        });
    }

    public static boolean isRedemptionInventory(Inventory inventory) {
        return inventory != null && inventories.containsValue(inventory);
    }
}
