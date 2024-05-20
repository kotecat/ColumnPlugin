package org.koteyka.columns.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.koteyka.columns.enums.PlayMode;
import org.koteyka.columns.manager.GameManager;
import org.koteyka.columns.enums.GameState;

public class StartGameCommand implements CommandExecutor {

    private final GameManager gameManager;

    public StartGameCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        World world;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please enter game mode." + ChatColor.GOLD + " ( ex. /start default )");
            return true;
        }

        PlayMode playMode = null;
        try {
            playMode = PlayMode.valueOf(args[0].replace(" ", "").toUpperCase());
        } catch (IllegalArgumentException ignored) {}

        if (playMode == null) {
            sender.sendMessage(ChatColor.RED + "Game mode not found" + ChatColor.GOLD + " ( try /start default )");
            return true;
        }

        if (args.length > 1) {
            String worldName = args[1];
            world = Bukkit.getWorld(worldName);
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Please enter the WORLD NAME to start the game");
                return true;
            };
            Player player = (Player) sender;
            world = player.getWorld();
        }

        if (world == null) {
            sender.sendMessage(ChatColor.RED + "World not found");
            return true;
        }

        gameManager.setWorld(world);
        GameState gameState = gameManager.getGameState();

        if (gameState == GameState.ACTIVE) {
            gameManager.setGameState(GameState.LOBBY);
        }

        gameManager.setPlayMode(playMode);
        gameManager.setGameState(GameState.STARTING);

        return true;
    }
}
