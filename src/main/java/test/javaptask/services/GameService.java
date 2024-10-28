package test.javaptask.services;

import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import test.javaptask.entities.Colour;
import test.javaptask.entities.GameDTO;

import java.util.*;

public class GameService {
    private final JavaPlugin plugin;

    public GameService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private final HashMap<UUID, GameDTO> playerGameEntities = new HashMap<>();

    public GameDTO getPlayerColorFromList(Player player){
        GameDTO playerData = playerGameEntities.get(player.getUniqueId());
        if (playerData != null){
            return playerData;
        }
        return null;
    }

    public void startGame(Player player, Colour colour){
        if (!playerGameEntities.containsKey(player.getUniqueId())){
            GameDTO game = new GameDTO(colour);
            playerGameEntities.put(player.getUniqueId(), game);
            openGameGUI(player);
        }
        openGameGUI(player);
    }

    public void endGame(Player player){
        GameDTO playerData = playerGameEntities.get(player.getUniqueId());
        if(playerData != null){
            if (playerData.getPoints() >= 9){
                launchPlayerAndSpawnFireworks(player);
            }
        }
        player.closeInventory();
        playerGameEntities.remove(player.getUniqueId());
    }

    public void openGameGUI(Player player){
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

    private void launchPlayerAndSpawnFireworks(Player player){
        final int[] count = {0};
        player.setVelocity(player.getVelocity().add(new Vector(0, 1, 0)));

         Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (count[0] == 5){
                Bukkit.getScheduler().cancelTasks(plugin);
            }
            Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
            FireworkMeta meta = firework.getFireworkMeta();
            meta.addEffect(FireworkEffect.builder().withColor(Color.RED).build());
            firework.setFireworkMeta(meta);
            count[0]++;
        }, 0L, 20L);
    }
}
