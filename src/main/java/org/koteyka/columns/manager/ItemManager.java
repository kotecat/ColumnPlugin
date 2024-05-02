package org.koteyka.columns.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.CommandBlock;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.*;

public class ItemManager {

    private static List<Material> enabledMaterials = new ArrayList<>();
    private final World world;

    private final int POTIONS_UP = 1;
    private final int ENCH_BOOKS = 1;
    private final int ARROWS = 1;

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
        addUpMaterial(Material.POTION, POTIONS_UP);
        addUpMaterial(Material.SPLASH_POTION, POTIONS_UP);
        addUpMaterial(Material.LINGERING_POTION, POTIONS_UP);

        addUpMaterial(Material.ENCHANTED_BOOK, ENCH_BOOKS);

        addUpMaterial(Material.TIPPED_ARROW, ARROWS);
        Collections.shuffle(enabledMaterials);
    }

    private void addUpMaterial(Material material, int count) {
        for (int i = 0; i < count; i++) {
            enabledMaterials.add(material);
        }
    }

    private void madePotion(ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) return;
        if (!(itemStack.getItemMeta() instanceof PotionMeta)) return;

        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        List<PotionType> potions = new ArrayList<>(Arrays.asList((PotionType.values().clone())));
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

    private Material getMaterial() {
        int index = new Random().nextInt(enabledMaterials.size());
        return enabledMaterials.get(index);
    }

    public ItemStack generateItem() {
        initMaterials();

        ItemStack itemStack = new ItemStack(getMaterial());

        madePotion(itemStack);
        enchantItem(itemStack);

        return itemStack;
    }
}
