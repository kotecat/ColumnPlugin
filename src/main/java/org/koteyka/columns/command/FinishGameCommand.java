package org.koteyka.columns.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.koteyka.columns.enums.GameState;
import org.koteyka.columns.manager.GameManager;

public class FinishGameCommand implements CommandExecutor {

    private final GameManager gameManager;

    public FinishGameCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GameState gameState = gameManager.getGameState();
        if (gameState == GameState.STARTING) {
            sender.sendMessage(ChatColor.RED + "[!] Game is not starting\n[>] Try again later...");
            return true;
        }
        gameManager.setGameState(GameState.LOBBY);
        return true;
    }
}
