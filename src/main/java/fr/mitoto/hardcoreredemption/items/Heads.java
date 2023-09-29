package fr.mitoto.hardcoreredemption.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Heads {
    public static ItemStack getOfflinePlayerHead(OfflinePlayer p) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        if(skull == null) return item;
        skull.setDisplayName(ChatColor.YELLOW + p.getName());
        skull.setOwningPlayer(p);
        item.setItemMeta(skull);
        return item;
    }
}
