package test.javaPTask;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import test.javaPTask.Entities.GlobalVariables;
import test.javaPTask.Services.PlayerService;
import test.javaPTask.Commands.Game;
import test.javaPTask.Commands.GameTabCompleter;

public final class JavaPTask extends JavaPlugin implements Listener {

    private PlayerService playerService;
    private GlobalVariables globalVariables;

    @Override
    public void onEnable() {
        // Plugin startup logic
        playerService = new PlayerService();
        globalVariables = new GlobalVariables();
        globalVariables.setOperatorList(playerService.loadOperatorList(getDataFolder().toPath().resolve("C:\\Users\\Vamp\\Documents\\AAAProject\\MinecraftServer\\Server\\ops.json")));
        playerService = new PlayerService(globalVariables);
        Game game = new Game(playerService);
        getCommand("game").setExecutor(game);
        getCommand("game").setTabCompleter(new GameTabCompleter());
        getServer().getPluginManager().registerEvents(game, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("SHUTDOWN!");
    }
}
