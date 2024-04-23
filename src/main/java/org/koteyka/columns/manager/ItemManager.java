package org.koteyka.columns.manager;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ItemManager {

    private static List<Material> enabledMaterials = new ArrayList<>();
    private final World world;

    public ItemManager(World world) {
        this.world = world;
    }

    private void initMaterials() {
        if (enabledMaterials.isEmpty()) {
            for (Material material : Material.values()) {
                if (!material.isEnabledByFeature(world)) continue;
                if (!material.isItem()) continue;
                enabledMaterials.add(material);
            }
        }
    }

    private ItemStack enchantItem(ItemStack itemStack) {
        List<Enchantment> possible = new ArrayList<Enchantment>();

        for (Enchantment ench : Enchantment.values()) {
            // Check if the enchantment can be applied to the item, save it if it can
            if (ench.canEnchantItem(itemStack)) {
                possible.add(ench);
            }
        }

        for (int i = 0; i < (new Random()).nextInt(5); i++) {
            if (!possible.isEmpty()) {
                // Randomize the enchantments
                Collections.shuffle(possible);
                // Get the first enchantment in the shuffled list
                Enchantment chosen = possible.get(0);
                // Apply the enchantment with a random level between 1 and the max level
                itemStack.addEnchantment(chosen, 1 + (int) (Math.random() * ((chosen.getMaxLevel() - 1) + 1)));
            }
        }

        return itemStack;
    }

    public ItemStack generateItem() {
        initMaterials();
        ItemStack itemStack = new ItemStack(enabledMaterials.get(new Random().nextInt(enabledMaterials.size())));
        return enchantItem(itemStack);
    }
}
