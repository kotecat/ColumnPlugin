package org.koteyka.columns.manager;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import org.koteyka.columns.ColumnsPlugin;
import org.koteyka.columns.param.Cords;
import org.koteyka.columns.param.Mode;
import org.koteyka.columns.state.GameState;
import org.koteyka.columns.task.BorderTask;
import org.koteyka.columns.task.CountdownStartTask;
import org.koteyka.columns.task.GiveItemsTask;
import org.koteyka.columns.utils.Utils;

import java.util.List;

public class GameManager {

    private final ColumnsPlugin plugin;
    private final PlayerManager playerManager;
    private GameState gameState = GameState.LOBBY;
    private World world;

    private CountdownStartTask countdownStartTask;
    private GiveItemsTask giveItemsTask;
    private BorderTask borderTask;

    public GameManager(ColumnsPlugin plugin) {
        this.plugin = plugin;
        this.playerManager = new PlayerManager(this);
        this.world = Bukkit.getWorlds().get(0);
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
                if (countdownStartTask != null && !countdownStartTask.isCancelled()) {
                    countdownStartTask.cancel();
                }
                if (giveItemsTask != null && !giveItemsTask.isCancelled()) {
                    giveItemsTask.cancel();
                }
                if (borderTask != null && !borderTask.isCancelled()) {
                    borderTask.cancel();
                }
                break;

            case STARTING:
                Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "starting!");
                playerManager.addPlayersInGame();
                prepareGameLocation();
                // tasks init
                this.countdownStartTask = new CountdownStartTask(this);
                this.countdownStartTask.runTaskTimer(plugin, 0L, 20L);
                break;

            case ACTIVE:
                Bukkit.broadcastMessage("GO!");
                playerManager.prepareGame();
                // tasks init
                this.giveItemsTask = new GiveItemsTask(this);
                this.giveItemsTask.runTaskTimer(plugin, getMode().getCountDownItem(), getMode().getCountDownItem());
                this.borderTask = new BorderTask(this);
                this.borderTask.runTaskLater(plugin, getMode().getBorder().getTimeToWait() * 20L);
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

        String textKill = ChatColor.BLUE + "%s".formatted(player.getName())
                + ChatColor.RED + " was killed";

        if(player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) player.getLastDamageCause()).getDamager() instanceof Player) {
                Player damager = (Player) ((EntityDamageByEntityEvent) player.getLastDamageCause()).getDamager();
                textKill = textKill + ChatColor.GOLD + " -> " + ChatColor.BLUE + "%s".formatted(
                        damager.getName()
                );
            }
        }

        Bukkit.broadcastMessage(
                ChatColor.RED + "[!] > " + textKill
        );

        List<Player> livedPlayers = playerManager.getLivedPlayers();

        if (livedPlayers.size() <= 1) {
            stop();
            if (!livedPlayers.isEmpty()) {
                Bukkit.broadcastMessage(ChatColor.AQUA
                        + "%s Win".formatted(livedPlayers.get(0).getName()));
            }
        }
    }

    private void stop() {
        setGameState(GameState.LOBBY);
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
