package test.javaPTask.Commands;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import test.javaPTask.Entities.Colour;
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

@RequiredArgsConstructor
public class Game implements CommandExecutor, Listener {
    private final PlayerService playerService;
    private Colour chosenColour;
    private int points = 0;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equalsIgnoreCase("game")) {
            if (commandSender instanceof Player player) {
                String playerUUID = player.getUniqueId().toString();
                if(player.isOp()){
                    if (strings.length == 1) {
                        String colour = strings[0].toUpperCase();
                        switch (colour) {
                            case "RED":
                            case "BLUE":
                            case "YELLOW":
                            case "GREEN":
                                openGameGUI(player);
                                chosenColour = Colour.valueOf(colour);
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                                player.sendMessage("You selected "+ chosenColour);
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
        ItemStack red = createColoredPane(Material.RED_STAINED_GLASS_PANE, ChatColor.RED, "RED PANE");
        ItemStack blue = createColoredPane(Material.BLUE_STAINED_GLASS_PANE, ChatColor.BLUE, "BLUE PANE");
        ItemStack yellow = createColoredPane(Material.YELLOW_STAINED_GLASS_PANE, ChatColor.YELLOW, "YELLOW PANE");
        ItemStack green = createColoredPane(Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN, "GREEN PANE");

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

    private ItemStack createColoredPane(Material material, ChatColor nameColor, String colour){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(nameColor + colour);
            meta.setLore(Collections.singletonList(ChatColor.WHITE + "CLICK ME!"));
            item.setItemMeta(meta);
        }
        return item;
    }

    private void SpawnFireworks(Player player, int count) throws InterruptedException {
        Location loc = player.getLocation();
        for (int i = 0; i < count; i++) {
            loc.subtract(0, 1, 0);
            Firework firework = (Firework) player.getWorld().spawn(loc, Firework.class);
            FireworkMeta meta = firework.getFireworkMeta();
            FireworkEffect effect = FireworkEffect.builder()
                    .withColor(Color.RED)
                    .with(FireworkEffect.Type.BALL)
                    .withTrail()
                    .withFlicker()
                    .build();

            meta.addEffect(effect);
            meta.setPower(1);
            firework.setFireworkMeta(meta);
            firework.detonate();
            Thread.sleep(500);
        }
    }

    private void PlayerJump(Player player)   {
        player.setVelocity(new Vector(0, 1, 0));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) throws InterruptedException {
        if (event.getView().getTitle().equals("Game UI")) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
                String displayName = clickedItem.getItemMeta().getDisplayName();
                Player player = (Player) event.getWhoClicked();
                if (displayName.contains(chosenColour.toString() + " PANE")) {
                    if (points >= 9){
                        player.closeInventory();
                        player.sendMessage("CONGRATS YOU WON THE GAME!");
                        SpawnFireworks(player, 10);
                        PlayerJump(player);
                        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                        points = 0;

                    }
                    else{
                        points++;
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
                        openGameGUI(player);
                    }
                }
                else {
                    points = 0;
                    player.sendMessage("You pressed the wrong pane. YOU LOST!");
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                }
            }
        }
    }
}
