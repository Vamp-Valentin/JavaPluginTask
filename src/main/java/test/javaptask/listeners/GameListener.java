package test.javaptask.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import test.javaptask.entities.GameDTO;
import test.javaptask.services.GameService;

@RequiredArgsConstructor
public class GameListener implements Listener {

    private final GameService gameService;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Game UI")) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
                String displayName = clickedItem.getItemMeta().getDisplayName();
                Player player = (Player) event.getWhoClicked();
                GameDTO playerData = gameService.getPlayerColorFromList(player);
                if (displayName.contains(playerData.getColour().toString() + " PANE")) {
                    if (playerData.getPoints()  >= 9){
                        gameService.endGame(player);
                        player.sendMessage("CONGRATS YOU WON THE GAME!");
                        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                    }
                    else{
                        playerData.incrementPoints();
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
                        gameService.startGame(player, playerData.getColour());
                    }
                }
                else {
                    gameService.endGame(player);
                    player.sendMessage("You pressed the wrong pane. YOU LOST!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                }
            }
        }
    }
}
