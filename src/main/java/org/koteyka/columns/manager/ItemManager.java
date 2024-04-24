package org.koteyka.columns.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

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
                if (!material.isItem()) continue;
                enabledMaterials.add(material);
            }
        }
    }

    private void madePotion(ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) return;
        if (!(itemStack.getItemMeta() instanceof PotionMeta)) return;

        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        List<PotionType> potions = new ArrayList<>(List.of(PotionType.values().clone()));
        Collections.shuffle(potions);
        PotionType potionEffect = potions.get(0);
        Random random = new Random();

        boolean madeExt = random.nextBoolean();
        potionMeta.setBasePotionData(new PotionData(
                potionEffect,
                potionEffect.isExtendable() && madeExt,
                potionEffect.isUpgradeable() && !madeExt
        ));

        itemStack.setItemMeta(potionMeta);
    }

    private void enchantItem(ItemStack itemStack) {
        List<Enchantment> possible = new ArrayList<Enchantment>();

        for (Enchantment ench : Enchantment.values()) {
            // Check if the enchantment can be applied to the item, save it if it can
            if (itemStack.getType() == Material.ENCHANTED_BOOK
                || ench.canEnchantItem(itemStack)) {
                possible.add(ench);
            }
        }

        // Randomize the enchantments
        Collections.shuffle(possible);

        if (itemStack.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta esm = (EnchantmentStorageMeta) itemStack.getItemMeta();
            Enchantment ench = possible.get(0);
            esm.addStoredEnchant(ench, 1 + (int) (Math.random() * ((ench.getMaxLevel() - 1) + 1)), true);
            itemStack.setItemMeta(esm);
            return;
        }

        for (int i = 0; i < (new Random()).nextInt(5); i++) {
            if (!possible.isEmpty()) {
                // Get the first enchantment in the shuffled list
                Enchantment chosen = possible.get(0);
                // Apply the enchantment with a random level between 1 and the max level
                itemStack.addEnchantment(chosen, 1 + (int) (Math.random() * ((chosen.getMaxLevel() - 1) + 1)));
            }
        }
    }

    public ItemStack generateItem() {
        initMaterials();

        ItemStack itemStack = new ItemStack(enabledMaterials.get(new Random().nextInt(enabledMaterials.size())));

        madePotion(itemStack);
        enchantItem(itemStack);

        return itemStack;
    }
}
