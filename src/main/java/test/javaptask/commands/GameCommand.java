package test.javaptask.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import test.javaptask.entities.Colour;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import test.javaptask.services.GameService;


@RequiredArgsConstructor
public class GameCommand implements CommandExecutor {

    private final GameService gameService;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if(player.isOp()){
                if (strings.length == 1) {
                    String colour = strings[0].toUpperCase();
                    switch (colour) {
                        case "RED":
                        case "BLUE":
                        case "YELLOW":
                        case "GREEN":
                            gameService.startGame(player, Colour.valueOf(colour));
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                            player.sendMessage("You selected "+ colour);
                            break;
                        default:
                            player.sendMessage("Invalid color! Use RED, BLUE, YELLOW, or GREEN.");
                    }
                } else {
                    player.sendMessage("Please specify a color:"+ ChatColor.RED +"RED," + ChatColor.BLUE + "BLUE,"+ ChatColor.YELLOW+ "YELLOW,"+ "or"+ ChatColor.GREEN+"GREEN.");
                    player.sendMessage("after specifying a color a window will open and you will have to pick the color 10 times, gl!");
                }
            }else{
                player.sendMessage("You are not an operator!");
            }
            return true;
        }
        return false;
    }


}
