package test.javaPTask;

import lombok.AllArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import test.javaPTask.Entities.GlobalVariables;
import test.javaPTask.Entities.Operator;
import test.javaPTask.Services.PlayerService;
import test.javaPTask.commands.Game;

import java.util.ArrayList;
import java.util.List;

public final class JavaPTask extends JavaPlugin {

    private PlayerService playerService;
    private GlobalVariables globalVariables;

    @Override
    public void onEnable() {
        // Plugin startup logic
        playerService = new PlayerService();
        globalVariables = new GlobalVariables();
        globalVariables.setOperatorList(playerService.loadOperatorList(getDataFolder().toPath().resolve("C:\\Users\\Vamp\\Documents\\AAAProject\\MinecraftServer\\Server\\ops.json")));
        playerService = new PlayerService(globalVariables);
        getCommand("game").setExecutor(new Game(playerService));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("SHUTDOWN!");
    }
}
