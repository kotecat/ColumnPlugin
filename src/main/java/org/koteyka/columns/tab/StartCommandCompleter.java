package org.koteyka.columns.tab;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.koteyka.columns.enums.Event;
import org.koteyka.columns.enums.PlayMode;

import java.util.ArrayList;
import java.util.List;

public class StartCommandCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> worlds = new ArrayList<>();

        if (args.length <= 1) {
            for (PlayMode playMode : PlayMode.values()) {
                worlds.add(playMode.name().toLowerCase());
            }
        }

        if (args.length == 2) {
            for (World world : Bukkit.getWorlds()) {
                worlds.add(world.getName().toLowerCase());
            }
        }

        return worlds;
    }
}
