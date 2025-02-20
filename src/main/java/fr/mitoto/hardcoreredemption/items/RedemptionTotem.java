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

/**
 * Represents the Redemption Totem, a custom item that players can craft and use in the game.
 * The totem is crafted using a special recipe and identified using persistent data.
 */
public class RedemptionTotem extends ItemStack {
    private static final NamespacedKey namespacedKey = new NamespacedKey(Main.getPlugin(), Constants.REDEMPTION_TOTEM_KEY);

    /**
     * Constructs a new Redemption Totem item with custom metadata.
     * The item is given a special name, lore, and a persistent data tag for identification.
     */
    public RedemptionTotem() {
        super(Material.TOTEM_OF_UNDYING);
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) return;

        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + Constants.REDEMPTION_TOTEM_TITLE);
        itemMeta.setLore(Collections.singletonList(ChatColor.GRAY + Constants.REDEMPTION_TOTEM_LORE));
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.BYTE, (byte) 1);
        itemMeta.setCustomModelData(Constants.REDEMPTION_TOTEM_CUSTOM_MODEL_DATA);
        setItemMeta(itemMeta);
    }

    /**
     * Creates and returns the crafting recipe for the Redemption Totem.
     * The recipe follows a specific pattern using Soul Sand, Gold Blocks, and a Diamond Block.
     *
     * @return A {@link ShapedRecipe} for the Redemption Totem.
     */
    public static ShapedRecipe getRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(namespacedKey, new RedemptionTotem());

        recipe.shape("ABA", "BCB", "ABA");
        recipe.setIngredient('A', Material.SOUL_SAND);
        recipe.setIngredient('B', Material.GOLD_BLOCK);
        recipe.setIngredient('C', Material.DIAMOND_BLOCK);

        return recipe;
    }

    /**
     * Checks whether a given item is a Redemption Totem by verifying its persistent data.
     *
     * @param item The {@link ItemStack} to check.
     * @return {@code true} if the item is a Redemption Totem, {@code false} otherwise.
     */
    public static boolean isRedemptionTotem(ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta == null) return false;

            return itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.BYTE);
        }
        return false;
    }
}
