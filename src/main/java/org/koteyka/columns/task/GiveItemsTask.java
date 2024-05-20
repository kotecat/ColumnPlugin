package org.koteyka.columns.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.koteyka.columns.manager.GameManager;
import org.koteyka.columns.manager.ItemManager;
import org.koteyka.columns.utils.Utils;

import java.util.List;

public class GiveItemsTask extends BukkitRunnable {
    public final GameManager gameManager;
    public final ItemManager itemManager;
    private int countDown = 0;
    private int count = 0;

    public GiveItemsTask(GameManager gameManager, int countDown) {
        this.gameManager = gameManager;
        this.itemManager = new ItemManager(gameManager.getWorld());
        this.countDown = countDown;
        this.count = countDown;
    }

    @Override
    public void run() {
        count--;
        if (count <= 0) {
            give();
            count = countDown;
        }
        Utils.sendActionBars(makeActionBar(), gameManager);
    }

    private String makeActionBar() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(ChatColor.DARK_PURPLE);
        stringBuilder.append("Next item");
        stringBuilder.append(ChatColor.WHITE);
        stringBuilder.append(" - ");

        double part = (double) count / countDown;
        if (part >= 0.7)     stringBuilder.append(ChatColor.RED);
        else if (part >= 0.4) stringBuilder.append(ChatColor.YELLOW);
        else                  stringBuilder.append(ChatColor.GREEN);
        for (int i = 0; i < count; i++) {
            stringBuilder.append('|');
        }

        stringBuilder.append(ChatColor.GRAY);
        for (int i = 0; i < countDown - count; i++) {
            stringBuilder.append('|');
        }

        return stringBuilder.toString();
    }

    private void give() {
        gameManager.getPlayerManager().giveItems();
    }
}
