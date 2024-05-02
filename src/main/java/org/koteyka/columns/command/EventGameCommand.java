package org.koteyka.columns.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.koteyka.columns.enums.Event;
import org.koteyka.columns.manager.GameManager;

public class EventGameCommand implements CommandExecutor {

    private final GameManager gameManager;

    public EventGameCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "[!] Empty event name");
            return true;
        }

        String eventName = args[0];
        Event event;
        try {
            event = Event.valueOf(eventName.toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "[!] Event not found");
            return true;
        }

        // Run event
        gameManager.runEvent(event);

        return true;
    }
}
