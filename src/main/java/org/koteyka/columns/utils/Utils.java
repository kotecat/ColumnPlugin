package org.koteyka.columns.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import java.util.*;


public class Utils {

    public static void fillBlock(Vector p1, Vector p2, Material material, World world) {
        if (!material.isBlock()) return;

        Vector maximum = Vector.getMaximum(p1, p2);
        Vector minimum = Vector.getMinimum(p1, p2);

        for (int x = minimum.getBlockX(); x <= maximum.getBlockX(); x++) {
            for (int y = minimum.getBlockY(); y <= maximum.getBlockY(); y++) {
                for (int z = minimum.getBlockZ(); z <= maximum.getBlockZ(); z++) {
                    Block blockAt = world.getBlockAt(x, y, z);
                    blockAt.setType(material);
                }
            }
        }

    }

    public static void createColumn(Location location, int height) {
        World world = location.getWorld();
        for (int i = 0; i < height; i++) {
            location.subtract(0, 1, 0);
            Block blockAt = world.getBlockAt(location);
            blockAt.setType(Material.BEDROCK);
        }
    }

    public static void createCapsule(Location location, Material material) {
        World world = location.getWorld();

        if (!material.isBlock()) return;

        world.getBlockAt(location.clone().add(0, 2, 0)).setType(material);
        world.getBlockAt(location.clone().add(1, 1, 0)).setType(material);
        world.getBlockAt(location.clone().add(-1, 1, 0)).setType(material);
        world.getBlockAt(location.clone().add(0, 1, -1)).setType(material);
        world.getBlockAt(location.clone().add(0, 1, 1)).setType(material);
    }

    public static void invertTime(World world) {
        world.setTime(world.getTime() + 12000);
    }

    public static void removeOneItem(Material material, Inventory inventory) {
        for (ItemStack itemStack : inventory) {
            if (itemStack == null) continue;
            if (itemStack.getType() == material) {
                int amount = itemStack.getAmount();
                amount--;
                itemStack.setAmount(amount);
                break;
            }
        }
    }

    public static boolean is1_20Version() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        return bukkitVersion.startsWith("1.20");
    }

    public static void allWorldsBorderSet(int size) {
        for (World world : Bukkit.getWorlds()) {
            world.getWorldBorder().setSize(size);
        }
    }
}
