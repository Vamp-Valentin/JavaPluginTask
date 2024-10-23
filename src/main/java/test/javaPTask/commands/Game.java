package test.javaPTask.commands;

import lombok.AllArgsConstructor;
import test.javaPTask.Services.PlayerService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class Game implements CommandExecutor {
    private final PlayerService playerService;


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equalsIgnoreCase("game")) {
            if (commandSender instanceof Player player) {
                String playerUUID = player.getUniqueId().toString();

                if(playerService.isOperator(playerUUID)){
                    player.sendMessage("Access!");
                } else {
                    player.sendMessage("No Access!");
                }
                return true;
            }
        }
        return false;
    }
}
