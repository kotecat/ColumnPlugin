package org.koteyka.columns.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.koteyka.columns.manager.GameManager;
import org.koteyka.columns.state.GameState;

public class CountdownStartTask extends BukkitRunnable {

    private GameManager gameManager;

    public CountdownStartTask(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private int timeLeft = 3;

    @Override
    public void run() {
        if (timeLeft <= 0) {
            cancel();
            gameManager.setGameState(GameState.ACTIVE);
            return;
        }
        Bukkit.broadcastMessage(
                ChatColor.GOLD + "Game start - "
                + ChatColor.DARK_GREEN + timeLeft + "s"
        );
        timeLeft--;
    }
}
