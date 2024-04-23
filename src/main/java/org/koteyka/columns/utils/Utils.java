package org.koteyka.columns.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;


public class Utils {

    public static void fillBlock(Vector p1, Vector p2, Material material, World world) {
        if (!material.isBlock()) return;

        Vector maximum = Vector.getMaximum(p1, p2);
        Vector minimum = Vector.getMinimum(p1, p2);

        for (int x = minimum.getBlockX(); x <= maximum.getBlockX(); x++) {
            for (int y = minimum.getBlockY(); y <= maximum.getBlockY(); y++) {
                for (int z = minimum.getBlockZ(); z <= maximum.getBlockZ(); z++) {
                    Block blockAt = world.getBlockAt(x, y, z);
                    if (blockAt.getType() == Material.BARRIER) continue;
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

}
