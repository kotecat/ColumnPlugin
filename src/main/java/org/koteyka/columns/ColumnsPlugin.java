package org.koteyka.columns;

import org.bukkit.plugin.java.JavaPlugin;
import org.koteyka.columns.command.EventGameCommand;
import org.koteyka.columns.command.StartGameCommand;
import org.koteyka.columns.event.ClockClickEvent;
import org.koteyka.columns.event.DamageListener;
import org.koteyka.columns.manager.GameManager;

public final class ColumnsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        GameManager gameManager = new GameManager(this);

        getCommand("start").setExecutor(new StartGameCommand(gameManager));
        getCommand("event").setExecutor(new EventGameCommand(gameManager));
        getServer().getPluginManager().registerEvents(new DamageListener(gameManager), this);
        getServer().getPluginManager().registerEvents(new ClockClickEvent(gameManager), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("disabling...");
    }
}
