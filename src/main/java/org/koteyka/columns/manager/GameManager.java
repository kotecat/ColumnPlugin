package org.koteyka.columns.manager;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.koteyka.columns.ColumnsPlugin;
import org.koteyka.columns.enums.Event;
import org.koteyka.columns.param.Cords;
import org.koteyka.columns.param.Mode;
import org.koteyka.columns.enums.GameState;
import org.koteyka.columns.task.BorderTask;
import org.koteyka.columns.task.CountdownStartTask;
import org.koteyka.columns.task.GiveItemsTask;
import org.koteyka.columns.task.event.DamageCountdown;
import org.koteyka.columns.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameManager {

    private final ColumnsPlugin plugin;
    private final PlayerManager playerManager;
    private GameState gameState = GameState.NONE;
    private World world;

    private List<BukkitRunnable> tasks = new ArrayList<>();

    public GameManager(ColumnsPlugin plugin) {
        this.plugin = plugin;
        this.playerManager = new PlayerManager(this);
        this.world = Bukkit.getWorlds().get(0);
        this.setGameState(GameState.LOBBY);
    }

    public void setGameState(GameState gameState) {
        if (this.gameState == gameState) return;
        if (this.gameState == GameState.ACTIVE && gameState == GameState.LOBBY) {
            Bukkit.broadcastMessage(ChatColor.RED + "Game Finished!");
        }

        this.gameState = gameState;

        switch (gameState) {
            case LOBBY:
                playerManager.prepareLobby();
                // tasks cancel
                tasks.forEach((bukkitRunnable -> {
                    if (bukkitRunnable != null && !bukkitRunnable.isCancelled()) {
                        bukkitRunnable.cancel();
                    }
                }));
                break;

            case STARTING:
                Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "starting!");
                playerManager.addPlayersInGame();
                prepareGameLocation();
                madeCapsule(Material.GLASS);
                // tasks init
                BukkitRunnable countdownStartTask = new CountdownStartTask(this);
                countdownStartTask.runTaskTimerAsynchronously(plugin, 0L, 20L);
                tasks.add(countdownStartTask);
                break;

            case ACTIVE:
                Bukkit.broadcastMessage("GO!");
                playerManager.prepareGame();
                // tasks init
                BukkitRunnable giveItemsTask = new GiveItemsTask(this);
                giveItemsTask.runTaskTimer(plugin, getMode().getCountDownItem(), getMode().getCountDownItem());
                tasks.add(giveItemsTask);
                BukkitRunnable borderTask = new BorderTask(this);
                borderTask.runTaskLater(plugin, getMode().getBorder().getTimeToWait() * 20L);
                tasks.add(borderTask);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    madeCapsule(Material.AIR);
                }, 3 * 20L);
                break;
        }
    }

    public void onDie(Player player) {
        player.getInventory().clear();

        if (gameState != GameState.ACTIVE) {
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(player.getMaxHealth());
            player.teleport(world.getSpawnLocation());
            return;
        }

        Cords c = getMode().getCords().get(0);
        player.teleport(new Location(world, 0, c.y, 0));
        player.setGameMode(GameMode.SPECTATOR);
        player.setHealth(player.getMaxHealth());

        String textKill = getTextKill(player);

        Bukkit.broadcastMessage(
                ChatColor.RED + "[!] > " + textKill
        );

        List<Player> livedPlayers = playerManager.getLivedPlayers();

        if (livedPlayers.size() <= 1) {
            stop();
            if (!livedPlayers.isEmpty()) {
                Bukkit.broadcastMessage(String.format(
                        "%s%s Win",
                        ChatColor.AQUA, livedPlayers.get(0).getName()));
            }
        }
    }

    private static String getTextKill(Player player) {
        String textKill = ChatColor.BLUE + player.getName()
                        + ChatColor.RED + " was killed";

        if(player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) player.getLastDamageCause()).getDamager() instanceof Player) {
                Player damager = (Player) ((EntityDamageByEntityEvent) player.getLastDamageCause()).getDamager();
                textKill = textKill + ChatColor.GOLD + " -> " + ChatColor.BLUE + damager.getName();
            }
        }
        return textKill;
    }

    private void stop() {
        setGameState(GameState.LOBBY);
        getPlayerManager().clearAllPlayers();
    }

    public void prepareGameLocation() {
        clearGameLocation();
        createColumns();
    }

    private void createColumns() {
        Mode mode = getMode();
        for (Cords cord : mode.getCords()) {
            Location location = new Location(world, cord.x, cord.y, cord.z);
            Utils.createColumn(location, mode.getColumnHeight());
        }
    }

    private void madeCapsule(Material material) {
        Mode mode = getMode();
        for (Cords cord : mode.getCords()) {
            Location location = new Location(world, cord.x, cord.y, cord.z);
            Utils.createCapsule(location, material);
        }
    }

    public void clearGameLocation() {
        Utils.fillBlock(
                new Vector(-39, world.getMinHeight(), -39),
                new Vector(39, world.getMaxHeight(), 39),
                Material.AIR,
                this.world
        );
        for (Entity entity : world.getEntities()) {
            if (entity.getType() == EntityType.PLAYER) continue;
            entity.remove();
        }
    }

    public void runEvent(Event event) {
        Random random = new Random();

        getPlayerManager().spawnParticles(Particle.TOTEM, 500);
        switch (event) {
            case DAMAGE:
                int duration = random.nextInt(7) + 6;
                int countdown = 3 - random.nextInt(1);
                double amount = (double) (random.nextInt(5) + 8) / 10;
                String format = String.format(
                        "§6All players receive §2%.1f§c damage §6every §2%d§6 seconds!\nfor §2%d§6 seconds",
                        amount, countdown, duration
                );
                Bukkit.broadcastMessage("§4[!] " + format);
                BukkitRunnable damageCountdown = new DamageCountdown(this, duration, countdown, amount);
                damageCountdown.runTaskTimer(plugin, 0L, 20L);
                tasks.add(damageCountdown);
                break;
            case RAND_ITEM:
                ItemManager itemManager = new ItemManager(world);
                getPlayerManager().giveItems(itemManager.generateItem());
                Bukkit.broadcastMessage("§4[!] §6Everyone gets an §4extra §2ITEM§6!");
                break;
            case RAND_EFFECT:
                getPlayerManager().giveEffects();
                Bukkit.broadcastMessage("§4[!] §6Everyone gets an §4extra §cEFFECT§6!");
                break;
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public ColumnsPlugin getPlugin() {
        return plugin;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Mode getMode() {
        int size = playerManager.getPlayersInGame().size();

        if (size <= 4) {
            return Mode.FOUR;
        } else if (size <= 6) {
            return Mode.EIGHT;
        } else {
            return Mode.EIGHT;
        }
    }
}
