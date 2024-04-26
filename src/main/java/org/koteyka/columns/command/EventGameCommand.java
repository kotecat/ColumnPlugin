package org.koteyka.columns.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.koteyka.columns.manager.GameManager;
import org.koteyka.columns.task.event.DamageCountdown;
import org.koteyka.columns.utils.Utils;

public class EventGameCommand implements CommandExecutor {

    private final GameManager gameManager;

    public EventGameCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return true;
    }
}
