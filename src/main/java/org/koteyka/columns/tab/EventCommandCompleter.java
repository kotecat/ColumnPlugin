package org.koteyka.columns.tab;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.koteyka.columns.enums.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventCommandCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> events = new ArrayList<>();

        if (args.length <= 1) {
            for (Event event : Event.values()) {
                events.add(event.toString().toLowerCase());
            }
        }

        return events;
    }
}
