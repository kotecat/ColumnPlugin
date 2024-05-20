package org.koteyka.columns.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.GlowItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;
import org.koteyka.columns.manager.GameManager;
import org.koteyka.columns.param.Mode;

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



        world.getBlockAt(location.clone().add(0, 2, 0)).setType(material);
        world.getBlockAt(location.clone().add(1, 1, 0)).setType(material);
        world.getBlockAt(location.clone().add(-1, 1, 0)).setType(material);
        world.getBlockAt(location.clone().add(0, 1, -1)).setType(material);
        world.getBlockAt(location.clone().add(0, 1, 1)).setType(material);
    }

    public static void createPlatform(GameManager gameManager, Material material, int Y) {
        if (!material.isBlock()) return;
        List<Integer> range;
//        if (gameManager.getMode() == Mode.TWELVE) {
//            range = Arrays.asList(-2, -1, 0, 1, 2);
//        } else {
//            range = Arrays.asList(-1, 0, 1);
//        }
        range = Arrays.asList(-2, -1, 0, 1, 2);
        World world = gameManager.getWorld();
        Location location = new Location(world, 0, Y, 0);
        for (int i : range) {
            for (int j : range) {
                if (Math.abs(i) == 2 && Math.abs(j) == 2) continue;
                world.getBlockAt(location.clone().add(i, 0, j)).setType(material);
            }
        }
//        world.getBlockAt(location.clone().add(0, 1, 0)).setType(Material.GLOW_ITEM_FRAME);
        GlowItemFrame glowItemFrame = (GlowItemFrame) world.spawnEntity(location.clone().add(0, 1, 0), EntityType.GLOW_ITEM_FRAME);
        glowItemFrame.setItem(new ItemStack(Material.GOLDEN_APPLE));

        if (gameManager.getCountPlayers() >= 5) {
            createSubPlatform(Y, 9, 0, world);
            createSubPlatform(Y, 0, 9, world);
            createSubPlatform(Y, -9, 0, world);
            createSubPlatform(Y, 0, -9, world);
        }
    }

    private static void createSubPlatform(int Y, int X, int Z, World world) {
        List<Integer> range = Arrays.asList(-1, 0, 1);
        Location location = new Location(world, X, Y, Z);
        for (int i : range) {
            for (int j : range) {
                if (Math.abs(i) == 2 && Math.abs(j) == 2) continue;
                world.getBlockAt(location.clone().add(i, 0, j)).setType(Material.BEDROCK);
            }
            world.getBlockAt(location.clone().add(0, 1, 0)).setType(Material.WARPED_STEM);
        }
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

    public static void sendActionBars(String text, GameManager gameManager) {
        for (Player player : gameManager.getPlayerManager().getLivedPlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
        }
    }
}
