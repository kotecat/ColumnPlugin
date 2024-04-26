package org.koteyka.columns.task.event;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.koteyka.columns.manager.GameManager;

public class DamageCountdown extends BukkitRunnable {

    private final GameManager gameManager;
    private int duration;
    private int countdown;
    private double amount;

    private int countdownIncrement = 1;

    public DamageCountdown(GameManager gameManager, int duration, int countdown, double amount) {
        this.gameManager = gameManager;
        this.countdown = countdown;
        this.duration = duration;
        this.amount = amount;
    }

    public void damage() {
        for (Player player : gameManager.getPlayerManager().getPlayersInGame()) {
            player.damage(amount);
        }
    }

    @Override
    public void run() {
        duration--;
        if (duration <= 0) {
            cancel();
        }
        countdownIncrement++;
        if (countdownIncrement >= countdown) {
            damage();
            countdownIncrement = 1;
        }
    }
}
