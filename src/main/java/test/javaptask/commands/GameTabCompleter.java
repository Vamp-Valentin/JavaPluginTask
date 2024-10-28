package test.javaptask.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameTabCompleter implements TabCompleter {
    private final List<String> options = List.of("RED", "BLUE", "YELLOW", "GREEN");
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            // Check if the command is 'game'
            if (command.getName().equalsIgnoreCase("game")) {
                if (strings.length == 1) { // We're looking for completions for the first argument
                    List<String> completions = new ArrayList<>();
                    for (String option : options) {
                        if (option.toLowerCase().startsWith(strings[0].toLowerCase())) {
                            completions.add(option);
                        }
                    }
                    return completions; // Return the matching options
                }
            }
        }
        return Collections.emptyList(); // No completions
    }
}

