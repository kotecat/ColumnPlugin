package org.koteyka.columns;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.koteyka.columns.command.StartGameCommand;
import org.koteyka.columns.event.DamageListener;
import org.koteyka.columns.manager.GameManager;
import org.koteyka.columns.state.GameState;

public final class ColumnsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        GameManager gameManager = new GameManager(this);

        getCommand("start").setExecutor(new StartGameCommand(gameManager));
        getServer().getPluginManager().registerEvents(new DamageListener(gameManager), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("disabling...");
    }
}
