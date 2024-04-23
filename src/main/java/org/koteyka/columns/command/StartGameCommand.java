package org.koteyka.columns.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.koteyka.columns.manager.GameManager;
import org.koteyka.columns.state.GameState;

public class StartGameCommand implements CommandExecutor {

    private final GameManager gameManager;

    public StartGameCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        gameManager.setWorld(player.getWorld());
        GameState gameState = gameManager.getGameState();

        if (gameState == GameState.ACTIVE) {
            gameManager.setGameState(GameState.LOBBY);
        }

        gameManager.setGameState(GameState.STARTING);

        return true;
    }
}
