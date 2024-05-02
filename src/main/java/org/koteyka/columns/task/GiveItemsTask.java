package org.koteyka.columns.task;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.koteyka.columns.manager.GameManager;
import org.koteyka.columns.manager.ItemManager;

import java.util.List;

public class GiveItemsTask extends BukkitRunnable {
    public final GameManager gameManager;
    public final ItemManager itemManager;

    public GiveItemsTask(GameManager gameManager) {
        this.gameManager = gameManager;
        this.itemManager = new ItemManager(gameManager.getWorld());
    }

    @Override
    public void run() {
        gameManager.getPlayerManager().giveItems(itemManager.generateItem());
    }
}
