package org.koteyka.columns.manager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.koteyka.columns.param.Cords;
import org.koteyka.columns.param.Mode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerManager {

    private final GameManager gameManager;
    private List<UUID> playersInGame = new ArrayList<UUID>();
    private static final int MAX_PLAYERS = 8;

    public PlayerManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void addPlayersInGame() {
        this.playersInGame.clear();
        Bukkit.getOnlinePlayers().forEach(
                player -> this.playersInGame.add(player.getUniqueId())
        );
        this.playersInGame = new ArrayList<>(playersInGame.subList(0, Math.min(MAX_PLAYERS, playersInGame.size())));
        Collections.shuffle(this.playersInGame);
    }

    public List<Player> getPlayersInGame() {
        ArrayList<Player> players = new ArrayList<>();
        for (UUID uuid : playersInGame) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) players.add(player);
        }
        return players;
    }

    public void clearAllPlayers() {
        playersInGame.clear();
    }

    public List<Player> getLivedPlayers() {
        List<Player> lifePlayers = new ArrayList<>();
        for (Player player : getPlayersInGame()) {
            GameMode gameMode = player.getGameMode();
            if (gameMode != GameMode.SPECTATOR) {
                lifePlayers.add(player);
            }
        }
        return lifePlayers;
    }

    public void prepareLobby() {
        World world = gameManager.getWorld();
        world.getWorldBorder().setSize(10_000_000);
        for (Player player : Bukkit.getOnlinePlayers()) {
            preparePlayer(player, new Location(world, -34, 52, -45));
        }
    }

    public void prepareGame() {
        World world = gameManager.getWorld();
        Mode mode = gameManager.getMode();

        world.getWorldBorder().setSize(mode.getBorder().getStartBorderSize());
        world.getWorldBorder().setDamageBuffer(0.0);
        world.getWorldBorder().setCenter(mode.getBorder().getCenterX(), mode.getBorder().getCenterZ());

        for (int i = 0; i < playersInGame.size(); i++) {
            Player player = getPlayersInGame().get(i);
            Cords cords = mode.getCords().get(i);
            Location location = new Location(world, cords.x, cords.y, cords.z, cords.p, 0);
            preparePlayer(player, location);
        }
    }

    public void preparePlayer(Player p, Location location) {
        p.getInventory().clear();  // Очищаем весь инвентарь игрока
        for (PotionEffect effect : p.getActivePotionEffects())  // Проходимся по эффектам игрока
            p.removePotionEffect(effect.getType());  // Очищаем эффект у игрока
        p.setHealth(p.getMaxHealth());  // Лечим игрока
        p.setFoodLevel(20);  // Кормим игрока
        p.setFireTicks(0);  // Тушим игрока от огня
        p.setGameMode(GameMode.SURVIVAL);  // Ставим режим выживания
        p.teleport(location);  // Телепортируем игрока
    }
}
