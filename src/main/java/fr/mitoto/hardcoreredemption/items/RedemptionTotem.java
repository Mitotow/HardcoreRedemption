package fr.mitoto.hardcoreredemption.items;

import fr.mitoto.hardcoreredemption.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collections;

public class RedemptionTotem extends ItemStack {
    private static final String key = "redemption_totem";

    public RedemptionTotem() {
        super(Material.TOTEM_OF_UNDYING);
        ItemMeta itemMeta = getItemMeta();
        if(itemMeta == null) return;
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "RedemptionTotem");
        itemMeta.setLore(Collections.singletonList(ChatColor.GRAY + "The Redemption Totem is a sacred item used within a special structure to revive a fallen player."));
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), key), PersistentDataType.BYTE, (byte) 1);
        setItemMeta(itemMeta);
    }

    public static ShapedRecipe getRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), key), new RedemptionTotem());
        recipe.shape("ABA", "BCB", "ABA");

        recipe.setIngredient('A', Material.SOUL_SAND);
        recipe.setIngredient('B', Material.GOLD_BLOCK);
        recipe.setIngredient('C', Material.WITHER_SKELETON_SKULL);

        return recipe;
    }

    public static boolean isRedemptionTotem(ItemStack item) {
        if(item.hasItemMeta()) {
            ItemMeta itemMeta = item.getItemMeta();
            if(itemMeta == null) return false;
            return itemMeta.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), RedemptionTotem.getKey()), PersistentDataType.BYTE);
        }
        return false;
    }

    public static String getKey() {
        return key;
    }
}
