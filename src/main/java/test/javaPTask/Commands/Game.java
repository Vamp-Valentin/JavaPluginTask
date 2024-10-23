package test.javaPTask.Commands;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import test.javaPTask.Services.PlayerService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@AllArgsConstructor
public class Game implements CommandExecutor, Listener {
    private final PlayerService playerService;


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equalsIgnoreCase("game")) {
            if (commandSender instanceof Player player) {
                String playerUUID = player.getUniqueId().toString();
                if(playerService.isOperator(playerUUID)){
                    if (strings.length == 1) {
                        String colour = strings[0].toUpperCase();

                        // Validate the argument against the allowed colors
                        switch (colour) {
                            case "RED":
                            case "BLUE":
                            case "YELLOW":
                            case "GREEN":
                                openGameGUI(player);
                                player.sendMessage("You selected "+ ChatColor.valueOf(colour)+ colour);
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
        }
        return false;
    }

    private void openGameGUI(Player player){
        Set<Integer> usedSlots = new HashSet<>();;
        int callCount = 0;
        Inventory inventory = Bukkit.createInventory(null, 54, "Game UI");
        ItemStack red = createColoredPane(Material.RED_STAINED_GLASS_PANE, ChatColor.RED);
        ItemStack blue = createColoredPane(Material.BLUE_STAINED_GLASS_PANE, ChatColor.BLUE);
        ItemStack yellow = createColoredPane(Material.YELLOW_STAINED_GLASS_PANE, ChatColor.YELLOW);
        ItemStack green = createColoredPane(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN);

        inventory.setItem(getSlot(usedSlots, callCount), red);
        inventory.setItem(getSlot(usedSlots, callCount), blue);
        inventory.setItem(getSlot(usedSlots, callCount), yellow);
        inventory.setItem(getSlot(usedSlots, callCount), green);

        player.openInventory(inventory);
    }

    private int getSlot(Set<Integer> usedSlots, int callCount) {
        Random r = new Random();
        int slot;
        if (callCount >= 4) {
            usedSlots.clear();
            callCount = 0;
        }
        do {
            slot = r.nextInt(54);
        } while (usedSlots.contains(slot));
        usedSlots.add(slot);
        callCount++;
        return slot;
    }

    private ItemStack createColoredPane(Material material, ChatColor nameColor){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(nameColor + "COLOR PANE");
            meta.setLore(Collections.singletonList(ChatColor.WHITE + "CLICK ME!"));
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Game UI")) {
            event.setCancelled(true);

            // Check if the clicked item is one of the colored panes
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
                String displayName = clickedItem.getItemMeta().getDisplayName();
                if (displayName.contains("COLOR PANE")) {
                    // Reopen the inventory with new random panes
                    Player player = (Player) event.getWhoClicked();
                    openGameGUI(player);
                }
            }
        }
    }
}
