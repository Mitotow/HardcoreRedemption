package fr.mitoto.hardcoreredemption.items;

import fr.mitoto.hardcoreredemption.Main;
import fr.mitoto.hardcoreredemption.configs.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collections;

public class RedemptionTotem extends ItemStack {
    private static final NamespacedKey namespacedKey = new NamespacedKey(Main.getPlugin(), Constants.REDEMPTION_TOTEM_KEY);

    public RedemptionTotem() {
        super(Material.TOTEM_OF_UNDYING);
        ItemMeta itemMeta = getItemMeta();
        if(itemMeta == null) return;
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + Constants.REDEMPTION_TOTEM_TITLE);
        itemMeta.setLore(Collections.singletonList(ChatColor.GRAY + Constants.REDEMPTION_TOTEM_LORE));
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.BYTE, (byte) 1);
        setItemMeta(itemMeta);
    }

    public static ShapedRecipe getRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(namespacedKey, new RedemptionTotem());

        recipe.shape("ABA", "BCB", "ABA");
        recipe.setIngredient('A', Material.SOUL_SAND);
        recipe.setIngredient('B', Material.GOLD_BLOCK);
        recipe.setIngredient('C', Material.DIAMOND_BLOCK);

        return recipe;
    }

    public static boolean isRedemptionTotem(ItemStack item) {
        if(item.hasItemMeta()) {
            ItemMeta itemMeta = item.getItemMeta();

            if(itemMeta == null) return false;
            return itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.BYTE);
        }
        return false;
    }
}
