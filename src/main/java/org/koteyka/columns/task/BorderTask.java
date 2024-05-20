package org.koteyka.columns.task;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.koteyka.columns.manager.GameManager;
import org.koteyka.columns.param.BorderConf;

public class BorderTask extends BukkitRunnable {

    private final GameManager gameManager;
    private BukkitTask task;

    public BorderTask(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        World world = gameManager.getWorld();
        WorldBorder worldBorder = world.getWorldBorder();
        BorderConf border = gameManager.getMode().getBorder();
        int timeToEnd = border.getTimeToEnd() + (gameManager.getCountPlayers() * 3);
        System.out.println("TTE: " + timeToEnd);
        worldBorder.setSize(1.0, timeToEnd);

        task = Bukkit.getScheduler().runTaskLater(gameManager.getPlugin(), () -> {
            worldBorder.setCenter(border.getEndX(), border.getEndZ());
            worldBorder.setDamageBuffer(border.getEndDamageBuffer());
            worldBorder.setDamageAmount(border.getEndDamageAmount());
        }, timeToEnd * 20L);
    }

    @Override
    public void cancel() {
        super.cancel();
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }
}
