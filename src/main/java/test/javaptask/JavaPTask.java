package test.javaptask;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import test.javaptask.commands.GameCommand;
import test.javaptask.commands.GameTabCompleter;
import test.javaptask.listeners.GameListener;
import test.javaptask.services.GameService;

public final class JavaPTask extends JavaPlugin implements Listener {

    private GameService gameService;


    @Override
    public void onEnable() {
        this.gameService = new GameService(this);
        getCommand("game").setExecutor(new GameCommand(gameService));
        getCommand("game").setTabCompleter(new GameTabCompleter());
        getServer().getPluginManager().registerEvents(new GameListener(gameService), this);
    }

    @Override
    public void onDisable() {
    }
}
