package org.koteyka.columns;

import org.bukkit.plugin.java.JavaPlugin;
import org.koteyka.columns.command.EventGameCommand;
import org.koteyka.columns.command.FinishGameCommand;
import org.koteyka.columns.command.StartGameCommand;
import org.koteyka.columns.event.ItemUseEvent;
import org.koteyka.columns.event.DamageListener;
import org.koteyka.columns.manager.GameManager;
import org.koteyka.columns.tab.EventCommandCompleter;
import org.koteyka.columns.tab.FinishCommandCompleter;
import org.koteyka.columns.tab.StartCommandCompleter;

public final class ColumnsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        GameManager gameManager = new GameManager(this);

        // Register cmd and tab completer
        getCommand("start").setExecutor(new StartGameCommand(gameManager));
        getCommand("start").setTabCompleter(new StartCommandCompleter());
        getCommand("finish").setExecutor(new FinishGameCommand(gameManager));
        getCommand("finish").setTabCompleter(new FinishCommandCompleter());
        getCommand("event").setExecutor(new EventGameCommand(gameManager));
        getCommand("event").setTabCompleter(new EventCommandCompleter());

        // Register event listeners
        getServer().getPluginManager().registerEvents(new DamageListener(gameManager), this);
        getServer().getPluginManager().registerEvents(new ItemUseEvent(gameManager), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("disabling...");
    }
}
